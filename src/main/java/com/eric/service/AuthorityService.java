package com.eric.service;

import com.eric.bean.*;
import com.eric.dao.ApkMapper;
import com.eric.dao.AuthorityApkMapMapper;
import com.eric.dao.AuthorityMapper;
import com.eric.tools.AndroidManifestHelper.AndroidManifestAnalyze;
import com.eric.tools.AndroidManifestHelper.AuthorityInsertCallable;
import com.eric.tools.MD5.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @ClassName: AuthorityService
 * @Description: 权限
 * @Author: Eric
 * @Date: 2019/3/16 0016
 * @Email: xiao_cui_vip@163.com
 */
@Service
public class AuthorityService {

    @Autowired
    AuthorityMapper authorityMapper;

    @Autowired
    ApkMapper apkMapper;
    @Autowired
    AuthorityApkMapMapper authorityApkMapMapper;

    /**
     * 提取权限信息并存入数据库
     * 注意 一般情况下AndroidManifest.xml只在包名根目录下，但是也存在在其他目录或者包含多个AndroidManifest.xml
     * 的情形，所以遍历目录的时候不能只遍历一层
     *
     * @param src 源路径，到达包名那一级别
     */
    public void saveAuthority(String src) {
        //设置线程池的大小为20
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "20");
        int apkId = -1;
        //获取包名
        String packageName = getPackageName(src);
        Apk apk = new Apk(packageName, 0);
        //在插入之前先判断数据库中有没有
        ApkExample apkExample = getApkExample(packageName);
        List<Apk> apks = apkMapper.selectByExample(apkExample);
        if (apks.size() == 0) {
            System.out.println(Thread.currentThread().getName() + "当前正在提取权限的应用名称为：" + packageName + ":数据库中没有该apk记录，正在执行插入....");
            //数据库中没有，插入
            if (null != apkMapper) {
                apkMapper.insertSelective(apk);
                System.out.println(Thread.currentThread().getName() + "当前正在提取权限的应用名称为：" + packageName + ":apk记录插入完成");
            } else {
                System.out.println(Thread.currentThread().getName() + "当前正在提取权限的应用名称为：" + packageName + ":apkMapper为空");
            }
            //获取id
            if (apk.getApkId() != null) {
                apkId = apk.getApkId();

            } else {
                System.out.println(Thread.currentThread().getName() + "当前正在提取权限的应用名称为：" + packageName + ":返回的id是空");
            }
        } else {
            System.out.println(Thread.currentThread().getName() + "当前正在提取权限的应用名称为：" + packageName + ":数据库已经存在该apk记录，记录数为：" + apks.size());
            //数据库中已经存在
            //获取Id
            Apk apk1 = apks.get(0);
            apkId = apk1.getApkId();
        }
        //创建文件
        File file = new File(src);
        //文件存在
        if (file.exists()) {
            LinkedList<File> list = new LinkedList<>();
            //列出当前目录下的文件
            File[] files = file.listFiles();
            //遍历每一个文件
            FileOperate(apkId, list, files, packageName);
            File temp;
            //队列不空
            while (!list.isEmpty()) {
                //选取List的第一个文件
                temp = list.removeFirst();
                File[] listFiles = temp.listFiles();
                //遍历
                FileOperate(apkId, list, listFiles, packageName);
            }
        } else {
            //文件不存在
            return;
        }
    }

    /**
     * 遍历文件夹下的所有文件
     *
     * @param apkId 应用id
     * @param list  文件列表
     * @param files 文件名称数组
     */
    public void FileOperate(int apkId, LinkedList<File> list, File[] files, String packageName) {
        for (File f : files) {
            //是目录
            if (f.isDirectory()) {
                //将文件夹加入队列
                list.add(f);
            } else {
                //是文件
                //获取文件路径
                String path = f.getAbsolutePath();
                //按照斜线分割
                String[] split = path.split("\\\\");
                //判断是否是AndroidManifest.xml文件
                if (split[split.length - 1].equals("AndroidManifest.xml")) {
                    //是AndroidManifest.xml文件
                    List<String> authorityList = null;
                    try {
                        //这个方法如果读入到的AndroidManifest.xml是乱码的话会
                        //抛出异常，这里要try一下
                        authorityList = AndroidManifestAnalyze.xmlHandle(path);
                    } catch (Exception e) {
                        System.out.println(Thread.currentThread().getName() + "当前正在提取权限的应用名称为：" + packageName + ":读取xml文件时出现异常");
                    }
                    if (null != authorityList) {
                        int finalApkId = apkId;
                        authorityList.parallelStream().forEach(au -> {
                            System.out.println("线程："+Thread.currentThread().getName()+"开始执行,正在提取权限的应用名称为:"+packageName);
                            authorityOperate(finalApkId, au, packageName);
                        });
                    }

                }
            }
        }
    }

    /**
     * 权限操作方法
     *
     * @param apkId 应用id
     * @param au    权限
     */
    public void authorityOperate(int apkId, String au, String packageName) {
        //获取权限的md5值
        String auMd5 = MD5Utils.MD5Encode(au, "utf8");
        //先查询数据库中有没有该权限
        AuthorityExample authorityExample = getAuthorityExample(auMd5);
        List<Authority> authorities = null;
        if (null == authorityMapper) {
            System.out.println(Thread.currentThread().getName() + "当前正在提取权限的应用名称为：" + packageName + ":authorityMapper为空");
        } else {
            authorities = authorityMapper.selectByExample(authorityExample);

        }
        //数据库中没有该权限
        if (authorities.size() == 0) {
            System.out.println(Thread.currentThread().getName() + "当前正在提取权限的应用名称为：" + packageName + ":数据库中没有该权限记录,正在执行插入操作....");
            Authority authority = new Authority(au, auMd5);
            authorityMapper.insertSelective(authority);
            System.out.println(Thread.currentThread().getName() + "当前正在提取权限的应用名称为：" + packageName + ":权限记录插入成功，正在插入权限-apk关系...");
            Integer authorityId = authority.getAuthorityId();
            AuthorityApkMap authorityApkMap = new AuthorityApkMap(apkId, authorityId);
            authorityApkMapMapper.insertSelective(authorityApkMap);
            System.out.println(Thread.currentThread().getName() + "当前正在提取权限的应用名称为：" + packageName + ":权限-apk关系插入成功");
        } else if (authorities.size() == 1) {
            //数据库中有1条权限记录
            System.out.println(Thread.currentThread().getName() + "当前正在提取权限的应用名称为：" + packageName + ":数据库中已经存在1条该权限记录，正在查询权限-apk映射关系是否存在....");
            Authority authority = authorities.get(0);
            Integer authorityId = authority.getAuthorityId();
            //查询映射关系是否在数据库中已经存在
            AuthorityApkMapExample authorityApkMapExample = getAuthorityApkMapExample(authorityId);
            List<AuthorityApkMap> authorityApkMaps = authorityApkMapMapper.selectByExample(authorityApkMapExample);
            //数据库中没有该映射关系
            if (authorityApkMaps.size() == 0) {
                System.out.println(Thread.currentThread().getName() + "当前正在提取权限的应用名称为：" + packageName + ":数据库中不存在权限-apk映射关系,正在插入该映射关系....");
                //插入该映射关系
                AuthorityApkMap authorityApkMap = new AuthorityApkMap(apkId, authorityId);
                authorityApkMapMapper.insertSelective(authorityApkMap);
                System.out.println(Thread.currentThread().getName() + "当前正在提取权限的应用名称为：" + packageName + ":权限-apk映射关系插入完成");
            } else if (authorityApkMaps.size() == 1) {
                System.out.println(Thread.currentThread().getName() + "当前正在提取权限的应用名称为：" + packageName + ":数据库中已经存在一条权限-apk映射关系记录，本次未执行插入操作");

            } else if (authorityApkMaps.size() > 1) {
                //理论上不可能大于1
                System.out.println(Thread.currentThread().getName() + "当前正在提取权限的应用名称为：" + packageName + ":>>>>>>>>>>>>数据库中存在多条相同的权限-apk映射关系");
            }

        } else if (authorities.size() > 1) {
            //存在多余1条的权限，理论上不可能
            System.out.println(Thread.currentThread().getName() + "当前正在提取权限的应用名称为：" + packageName + ":>>>>>>>>>>>>>数据库中存在多条相同的权限记录");

        }
    }

    /**
     * 获取根据id查询数据库中是否已经存在相应的权限记录的example
     *
     * @param authorityId 权限id
     * @return example
     */
    public AuthorityApkMapExample getAuthorityApkMapExample(Integer authorityId) {
        AuthorityApkMapExample authorityApkMapExample = new AuthorityApkMapExample();
        AuthorityApkMapExample.Criteria authorityApkMapCriteria = authorityApkMapExample.createCriteria();
        authorityApkMapCriteria.andAuthorityIdEqualTo(authorityId);
        return authorityApkMapExample;
    }

    /**
     * 获取根据权限内容md5值来查询数据库中是否存在相同记录的example
     *
     * @param auMd5 权限内容md5值
     * @return example
     */
    public AuthorityExample getAuthorityExample(String auMd5) {
        AuthorityExample authorityExample = new AuthorityExample();
        AuthorityExample.Criteria criteria = authorityExample.createCriteria();
        criteria.andAuthorityMd5EqualTo(auMd5);
        return authorityExample;
    }

    /**
     * 获取格局包名来查询数据库中是否存在相同的apk记录的example
     *
     * @param packageName 包名
     * @return example
     */
    public ApkExample getApkExample(String packageName) {
        ApkExample apkExample = new ApkExample();
        ApkExample.Criteria apkCriteria = apkExample.createCriteria();
        apkCriteria.andPackageNameEqualTo(packageName);
        return apkExample;
    }

    /**
     * 获取包名
     *
     * @param src 应用包文件夹
     * @return
     */
    public String getPackageName(String src) {
        String[] packageNamSplits = src.split("\\\\");
        return packageNamSplits[packageNamSplits.length - 1];
    }
}
