package com.eric.tools.api;
/*
 *@description:多线程提取api
 *@author:cuigs
 *@Email:cuigs@ti-net.com.cn
 *@Version:1.0
 *@Date:2019/3/29
 */

import com.eric.bean.*;
import com.eric.dao.ApiApkMapMapper;
import com.eric.dao.ApiMapper;
import com.eric.dao.ApkMapper;
import com.eric.tools.MD5.MD5Utils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;

@Component
public class ApiInsertRunnable implements Runnable {
    @Resource
    private ApiApkMapMapper apiApkMapMapper;
    @Resource
    private ApiMapper apiMapper;
    @Resource
    private ApkMapper apkMapper;
    public static ApiInsertRunnable apiInsertRunnable;
    //路径
    private String src;
    //属性
    private int apkAttribute;
    @PostConstruct
    public void init() {
        apiInsertRunnable = this;
    }

    public ApiInsertRunnable() {
    }

    public ApiInsertRunnable(String src, int apkAttribute) {
        this.src = src;
        this.apkAttribute = apkAttribute;
    }

    @Override
    public void run() {
        //线程开始执行时间
        Instant start = Instant.now();
        String name = Thread.currentThread().getName();
        System.out.println("线程"+name+"开始执行.....");
        int apkId = -1;
        //获取应用的包名
        String[] split = src.split("\\\\");
        //获取包名
        String packageName = split[split.length - 1];
        Apk apk = new Apk(packageName, apkAttribute);
        //在插入之前先判断数据库中有没有
        ApkExample apkExample = new ApkExample();
        ApkExample.Criteria apkCriteria = apkExample.createCriteria();
        apkCriteria.andPackageNameEqualTo(packageName);
        List<Apk> apks = apiInsertRunnable.apkMapper.selectByExample(apkExample);
        if (apks.size() == 0) {
            //数据库中没有，插入
            if (null != apiInsertRunnable.apkMapper) {

                apiInsertRunnable.apkMapper.insertSelective(apk);
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
                    //文件路径
                    String filePath = f.getAbsolutePath();
                    //该文件是否.smali文件
                    String s = filePath.substring(filePath.length() - 6, filePath.length());
                    if (s.equals(".smali")) {
                        //提取API信息
                        List<String> apiList = APIHelper.handle(filePath);
                        for (String item : apiList) {
                            System.out.println(item);
                            //下面是新改的-----
                            String md5Value = MD5Utils.MD5Encode(item, "utf8");
                            //先查询数据库中有没有该API
                            ApiExample apiExample = new ApiExample();
                            ApiExample.Criteria criteria = apiExample.createCriteria();
                            criteria.andApiMad5EqualTo(md5Value);
                            List<Api> apis = null;
                            if (apiInsertRunnable.apiMapper == null) {
                                System.out.println("ApiInsertCallable中的apiMapper是空");
                            } else {
                                apis = apiInsertRunnable.apiMapper.selectByExample(apiExample);
                            }
                            //数据库中没有该API
                            if (apis.size() == 0) {
                                //构建要插入数据库中的api对象
                                Api api = new Api(item, MD5Utils.MD5Encode(item, "utf8"));
                                apiInsertRunnable.apiMapper.insertSelective(api);
                                Integer apiId = api.getApiId();
                                ApiApkMap apiApkMap = new ApiApkMap(apkId, apiId);
                                apiInsertRunnable.apiApkMapMapper.insertSelective(apiApkMap);
                            } else {
                                Api api1 = null;
                                //数据库中有该记录
                                if (apis.size() == 1) {
                                    api1 = apis.get(0);
                                    //获取api id
                                    Integer apiId = api1.getApiId();
                                    //查询映射关系是否在数据库中已经存在
                                    ApiApkMapExample apiApkMapExample = new ApiApkMapExample();
                                    ApiApkMapExample.Criteria apiApkCriteria = apiApkMapExample.createCriteria();
                                    apiApkCriteria.andApiIdEqualTo(apiId);
                                    apiApkCriteria.andApkIdEqualTo(apkId);
                                    List<ApiApkMap> apiApkMaps = apiInsertRunnable.apiApkMapMapper.selectByExample(apiApkMapExample);
                                    if (apiApkMaps.size() == 0) {
                                        //数据库中没有该映射关系
                                        ApiApkMap apiApkMap = new ApiApkMap(apkId, apiId);
                                        //插入
                                        apiInsertRunnable.apiApkMapMapper.insertSelective(apiApkMap);
                                    }//否则什么也不做
                                } else {
                                    System.out.println("数据库中有多条重复的api");

                                }

                            }
                            //新改内容结束------
                        }
                    }
                }
            }
            File tempFile;
            //list不空
            while (!list.isEmpty()) {
                //选取list的第一个文件
                tempFile = list.removeFirst();
                files = tempFile.listFiles();
                for (File f1 : files) {
                    //目录
                    if (f1.isDirectory()) {
                        //加入队列
                        list.add(f1);
                    } else {
                        //是文件
                        //文件路径
                        String filePath = f1.getAbsolutePath();
                        //System.out.println(filePath);
                        //该文件是否.smali文件
                        String s = filePath.substring(filePath.length() - 6, filePath.length());
                        if (s.equals(".smali")) {
                            //提取API信息
                            List<String> apiList = APIHelper.handle(filePath);
                            for (String item : apiList) {
                                String md5Value = MD5Utils.MD5Encode(item, "utf8");
                                //先查询数据库中有没有该API
                                ApiExample apiExample = new ApiExample();
                                ApiExample.Criteria criteria = apiExample.createCriteria();
                                criteria.andApiMad5EqualTo(md5Value);
                                List<Api> apis = null;
                                if (apiInsertRunnable.apiMapper == null) {
                                    System.out.println("ApiInsertCallable中的apiMapper是空");
                                } else {
                                    apis = apiInsertRunnable.apiMapper.selectByExample(apiExample);
                                }
                                //数据库中没有该API
                                if (apis.size() == 0) {
                                    //构建要插入数据库中的api对象
                                    Api api = new Api(item, MD5Utils.MD5Encode(item, "utf8"));
                                    apiInsertRunnable.apiMapper.insertSelective(api);
                                    Integer apiId = api.getApiId();
                                    ApiApkMap apiApkMap = new ApiApkMap(apkId, apiId);
                                    apiInsertRunnable.apiApkMapMapper.insertSelective(apiApkMap);
                                } else {
                                    Api api1 = null;
                                    //数据库中有该记录
                                    if (apis.size() == 1) {
                                        api1 = apis.get(0);
                                        //获取api id
                                        Integer apiId = api1.getApiId();
                                        //查询映射关系是否在数据库中已经存在
                                        ApiApkMapExample apiApkMapExample = new ApiApkMapExample();
                                        ApiApkMapExample.Criteria apiApkCriteria = apiApkMapExample.createCriteria();
                                        apiApkCriteria.andApiIdEqualTo(apiId);
                                        apiApkCriteria.andApkIdEqualTo(apkId);
                                        List<ApiApkMap> apiApkMaps = apiInsertRunnable.apiApkMapMapper.selectByExample(apiApkMapExample);
                                        if (apiApkMaps.size() == 0) {
                                            //数据库中没有该映射关系
                                            ApiApkMap apiApkMap = new ApiApkMap(apkId, apiId);
                                            //插入
                                            apiInsertRunnable.apiApkMapMapper.insertSelective(apiApkMap);
                                        }//否则什么也不做
                                    } else {
                                        System.out.println("数据库中有多条重复的api");

                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        System.out.print("线程"+name+"执行结束<<<<<<");
        Instant end = Instant.now();
        long seconds = Duration.between(start, end).getSeconds();
        System.out.print(".....耗时："+seconds+"秒");
        System.out.println();
    }
}
