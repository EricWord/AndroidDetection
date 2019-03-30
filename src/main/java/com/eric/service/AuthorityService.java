package com.eric.service;

import com.eric.bean.Apk;
import com.eric.bean.ApkExample;
import com.eric.dao.ApkMapper;
import com.eric.dao.AuthorityApkMapMapper;
import com.eric.dao.AuthorityMapper;
import com.eric.tools.AndroidManifestHelper.AndroidManifestAnalyze;
import com.eric.tools.AndroidManifestHelper.AuthorityInsertCallable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
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
        int apkId = -1;
        //获取包名
        String packageName = getPackageName(src);
        Apk apk = new Apk(packageName, 0);
        //在插入之前先判断数据库中有没有
        ApkExample apkExample = new ApkExample();
        ApkExample.Criteria apkCriteria = apkExample.createCriteria();
        apkCriteria.andPackageNameEqualTo(packageName);
        List<Apk> apks = apkMapper.selectByExample(apkExample);
        if (apks.size() == 0) {
            //数据库中没有，插入
            if (null != apkMapper) {

                apkMapper.insertSelective(apk);
            } else {
                System.out.println("apkMapper为空");
            }

            //获取id
            if (apk.getApkId() != null) {

                apkId = apk.getApkId();

            } else {
                System.out.println("返回的id是空");
            }


        } else {
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
            File[] files = file.listFiles();
            //遍历每一个文件
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
                            System.out.println("读取xml文件时出现异常");

                        }
                        //创建线程池
                        ExecutorService pool = Executors.newFixedThreadPool(10);
                        for (String au : authorityList) {
                            System.out.println(au);
                            AuthorityInsertCallable task = new AuthorityInsertCallable(au, apkId);
                            pool.submit(task);
                        }

                        //关闭线程池
                        pool.shutdown();
                    }
                }
            }
            File temp;
            //队列不空
            while (!list.isEmpty()) {
                //选取List的第一个文件
                temp = list.removeFirst();
                File[] listFiles = temp.listFiles();
                //遍历
                for (File listFile : listFiles) {
                    //目录
                    if (listFile.isDirectory()) {
                        //加入队列
                        list.add(listFile);
                    } else {
                        //是文件
                        //获取文件路径
                        String listFileAbsolutePath = listFile.getAbsolutePath();
                        //判断是否是AndroidManifest.xml文件
                        String[] split = listFileAbsolutePath.split("\\\\");
                        if (split[split.length - 1].equals("AndroidManifest.xml")) {
                            //是AndroidManifest.xml文件
                            //创建线程池
                            ExecutorService pool2 = Executors.newFixedThreadPool(10);
                            List<String> authorityList2 = null;
                            try {
                                //这个方法如果读入到的AndroidManifest.xml是乱码的话会
                                //抛出异常，这里要try一下
                                authorityList2 = AndroidManifestAnalyze.xmlHandle(listFileAbsolutePath);

                            } catch (Exception e) {

                            }
                            for (String au2 : authorityList2) {
                                System.out.println(au2);
                                AuthorityInsertCallable task = new AuthorityInsertCallable(au2, apkId);
                                pool2.submit(task);
                            }
                            //关闭线程池，不要忘记
                            pool2.shutdown();
                        }
                    }
                }
            }
        } else {
            //文件不存在
            return;
        }
    }

    /**
     * 获取包名
     * @param src 应用包文件夹
     * @return
     */
    public String getPackageName(String src) {
        String[] packageNamSplits = src.split("\\\\");
        return packageNamSplits[packageNamSplits.length - 1];
    }
}
