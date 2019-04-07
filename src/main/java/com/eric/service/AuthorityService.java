package com.eric.service;

import com.eric.bean.*;
import com.eric.dao.ApkMapper;
import com.eric.dao.AuthorityApkMapMapper;
import com.eric.dao.AuthorityMapper;
import com.eric.exception.MultipleDuplicateValuesInDatabaseException;
import com.eric.tools.AndroidManifestHelper.AndroidManifestAnalyze;
import com.eric.tools.MD5.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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


    //    @Transactional(propagation = Propagation.REQUIRED , readOnly = false)
    public void saveAuthority(String path, int apkAttribute) {
        //设置线程池的大小为10
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "30");
        final int[] apkId = {-1};
//        List<String> amxList = getAndroidManifestXmlList(path);
        List<String> amxList = getSimpleAndroidManifestXmlList(path);
        if (amxList != null && amxList.size() > 0) {
            amxList.parallelStream().forEach(xmlPath -> {
                //当前线程名称
                String currentThreadName = Thread.currentThread().getName();
                System.out.println("-------------" + currentThreadName + "线程开始启动-----------");
                List<String> authorityList = null;
                try {

                    authorityList = AndroidManifestAnalyze.xmlHandle(xmlPath);
                } catch (Exception e) {
                    System.out.println(currentThreadName + ":解析xml时出现异常....");

                }
                //包名
                String packageName = "";
                if (null != AndroidManifestAnalyze.findPackage(xmlPath)) {

                    packageName = AndroidManifestAnalyze.findPackage(xmlPath);
                }
                try {
                    checkBeforeInsertAuthority(apkAttribute, apkId, packageName);
                } catch (MultipleDuplicateValuesInDatabaseException e) {
                    e.printStackTrace();
                }
                insertAuthority(apkId, authorityList, packageName);
            });
        }
    }

    /**
     * 向数据库中插入权限
     *
     * @param apkId         应用id
     * @param authorityList 权限列表
     * @param packageName   包名
     */
    public void insertAuthority(int[] apkId, List<String> authorityList, String packageName) {
        for (String au : authorityList) {
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

            if (authorities.size() > 1) {
                //存在多余1条的权限，理论上不可能,出现则抛出异常
                new MultipleDuplicateValuesInDatabaseException("数据库中存在" + authorities.size() + "条相同的权限信息");

            }

            if (authorities.size() == 1) {
                //数据库中有1条权限记录
                System.out.println(Thread.currentThread().getName() + "当前正在提取权限的应用名称为：" + packageName + ":数据库中已经存在1条该权限记录，正在查询权限-apk映射关系是否存在....");
                Authority authority = authorities.get(0);
                Integer authorityId = authority.getAuthorityId();
                //查询映射关系是否在数据库中已经存在
                AuthorityApkMapExample authorityApkMapExample = getAuthorityApkMapExample(authorityId, apkId[0]);
                List<AuthorityApkMap> authorityApkMaps = authorityApkMapMapper.selectByExample(authorityApkMapExample);
                //数据库中没有该映射关系
                if (checkBeforeInsertAuthorityApkMap(apkId, packageName, authorityId, authorityApkMaps)) {

                    return;
                }

            }

            //数据库中没有该权限
            if (authorities.size() == 0) {
                System.out.println(Thread.currentThread().getName() + "当前正在提取权限的应用名称为：" + packageName + ":数据库中没有该权限记录,正在执行插入操作....");
                Authority authority = new Authority(au, auMd5);
                authorityMapper.insertSelective(authority);
                System.out.println(Thread.currentThread().getName() + "当前正在提取权限的应用名称为：" + packageName + ":权限记录插入成功，正在插入权限-apk关系...");
                Integer authorityId = authority.getAuthorityId();
                AuthorityApkMap authorityApkMap = new AuthorityApkMap(apkId[0], authorityId);
                authorityApkMapMapper.insertSelective(authorityApkMap);
                System.out.println(Thread.currentThread().getName() + "当前正在提取权限的应用名称为：" + packageName + ":权限-apk关系插入成功");
                return;
            }


        }
    }

    public boolean checkBeforeInsertAuthorityApkMap(int[] apkId, String packageName, Integer authorityId, List<AuthorityApkMap> authorityApkMaps) {
        if (authorityApkMaps.size() == 0) {
            System.out.println(Thread.currentThread().getName() + "当前正在提取权限的应用名称为：" + packageName + ":数据库中不存在权限-apk映射关系,正在插入该映射关系....");
            //插入该映射关系
            AuthorityApkMap authorityApkMap = new AuthorityApkMap(apkId[0], authorityId);
            authorityApkMapMapper.insertSelective(authorityApkMap);
            System.out.println(Thread.currentThread().getName() + "当前正在提取权限的应用名称为：" + packageName + ":权限-apk映射关系插入完成");
            return true;
        }
        if (authorityApkMaps.size() == 1) {
            System.out.println(Thread.currentThread().getName() + "当前正在提取权限的应用名称为：" + packageName + ":数据库中已经存在一条权限-apk映射关系记录，本次未执行插入操作");
            return true;
        }
        if (authorityApkMaps.size() > 1) {
            //理论上不可能大于1，出现则抛出异常
            new MultipleDuplicateValuesInDatabaseException("数据库中存在" + authorityApkMaps.size() + "条相同的权限-apk映射关系");
        }
        return false;
    }

    /**
     * 插入权限前进行检查是否存在相同记录
     *
     * @param apkAttribute 应用属性
     * @param apkId        应用id
     * @param packageName  包名
     */
    public void checkBeforeInsertAuthority(int apkAttribute, int[] apkId, String packageName) throws MultipleDuplicateValuesInDatabaseException {
        Apk apk = new Apk(packageName, apkAttribute);
        //在插入之前先判断数据库中有没有
        ApkExample apkExample = getApkExample(packageName);
        List<Apk> apks = apkMapper.selectByExample(apkExample);
        if (apks.size() >= 2) {
            //理论上不可能，出现就抛出异常
            throw new MultipleDuplicateValuesInDatabaseException("数据库中存在" + apks.size() + "条相同的apk记录");

        }
        //数据库中已经存在一条相同的apk记录
        if (apks.size() == 1) {
            System.out.println(Thread.currentThread().getName() + "当前正在提取权限的应用名称为：" + packageName + ":数据库已经存在该apk记录，记录数为：" + apks.size());
            //获取Id
            Apk apk1 = apks.get(0);
            apkId[0] = apk1.getApkId();
        }

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
                apkId[0] = apk.getApkId();
            } else {
                System.out.println(Thread.currentThread().getName() + "当前正在提取权限的应用名称为：" + packageName + ":返回的id是空");
            }
        }


    }


    /**
     * 获取getAndroidManifestXml列表
     *
     * @param path 路径
     * @return 权限清单列表
     */
    public List<String> getAndroidManifestXmlList(String path) {
        int total = 0;
        System.out.println(">>>>>>开始获取AndroidManifest.xml文件列表........");
        List<String> androidManifestXmlList = new ArrayList<>();
        File file = new File(path);
        if (file.exists()) {
            LinkedList<File> list = new LinkedList<File>();
            File[] files = file.listFiles();
            for (File file2 : files) {
                //搜寻xml文件的多线程名称
                String finXmlThreadName = Thread.currentThread().getName();
                total = addFile2List(total, androidManifestXmlList, list, file2, finXmlThreadName);

            }

            File temp_file;
            while (!list.isEmpty()) {
                temp_file = list.removeFirst();
                files = temp_file.listFiles();
                for (File file2 : files) {
                    //搜寻xml文件的多线程名称
                    String finXmlThreadName = Thread.currentThread().getName();
                    total = addFile2List(total, androidManifestXmlList, list, file2, finXmlThreadName);

                }

            }
            System.out.println(">>>>>>获取AndroidManifest.xml文件列表完成........");
            return androidManifestXmlList;
        } else {
            System.out.println("文件不存在!");
            return null;
        }
    }


    public List<String> getSimpleAndroidManifestXmlList(String path) {
        int total = 0;
        System.out.println(">>>>>>开始获取AndroidManifest.xml文件列表........");
        List<String> androidManifestXmlList = new ArrayList<>();
        File file = new File(path);
        if (file.exists()) {
            File[] files = file.listFiles();
            for (File f : files) {
                if (f.isDirectory()) {
                    File[] currentFiles = f.listFiles();
                    for (File currentFile : currentFiles) {
                        if (!currentFile.isDirectory()) {
                            String currentFileAbsolutePath = currentFile.getAbsolutePath();
                            String[] arr = currentFileAbsolutePath.split("\\\\");
                            if (arr[arr.length - 1].equals("AndroidManifest.xml")) {
                                androidManifestXmlList.add(currentFileAbsolutePath);
                                total++;
                                System.out.println("当前获取到的AndroidManifest.xml数量为：" + total);


                            }

                        }
                    }

                }

            }

        }

        return androidManifestXmlList;

    }

    public int addFile2List(int total, List<String> androidManifestXmlList, LinkedList<File> list, File file2, String finXmlThreadName) {
        //当前搜寻路径
        String file2AbsolutePath = file2.getAbsolutePath();
        //System.out.println(finXmlThreadName + ":当前正在搜寻的路径为:" + file2AbsolutePath);
        if (file2.isDirectory()) {
            //是文件夹
            list.add(file2);
        } else {
            //不是文件夹，判断是否是AndroidManifest.xml文件
            //获取文件路径
            total = getTotal(total, androidManifestXmlList, file2, finXmlThreadName);
        }
        return total;
    }

    /**
     * 获取队列中的AndroidManifest.xml总数
     *
     * @param total                  xml文件总数
     * @param androidManifestXmlList 文件列表
     * @param file2                  文件
     * @return 返回总数
     */
    public int getTotal(int total, List<String> androidManifestXmlList, File file2, String finXmlThreadName) {
        //获取文件路径
        String currentFilePath = file2.getAbsolutePath();
        //按照斜线分割
        String[] pathArr = currentFilePath.split("\\\\");
        //判断是否是AndroidManifest.xml文件
        if (pathArr[pathArr.length - 1].equals("AndroidManifest.xml") && !pathArr[pathArr.length - 2].equals("original")) {
            androidManifestXmlList.add(currentFilePath);
            total++;
            System.out.println(finXmlThreadName + "当前获取到的AndroidManifest.xml文件总数为：" + total);

        }
        return total;
    }

    /**
     * 获取id
     *
     * @param apks 应用list
     * @return id
     */
    public int getApkId(List<Apk> apks) {
        int apkId;//数据库中已经存在
        //获取Id
        Apk apk1 = apks.get(0);
        apkId = apk1.getApkId();
        return apkId;
    }


    /**
     * 获取根据id查询数据库中是否已经存在相应的权限记录的example
     *
     * @param authorityId 权限id
     * @return example
     */
    public AuthorityApkMapExample getAuthorityApkMapExample(Integer authorityId, int apkId) {
        AuthorityApkMapExample authorityApkMapExample = new AuthorityApkMapExample();
        AuthorityApkMapExample.Criteria authorityApkMapCriteria = authorityApkMapExample.createCriteria();
        authorityApkMapCriteria.andAuthorityIdEqualTo(authorityId);
        authorityApkMapCriteria.andApkIdEqualTo(apkId);
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

}
