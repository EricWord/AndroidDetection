package com.eric.service;

import com.eric.bean.*;
import com.eric.dao.ApiApkMapMapper;
import com.eric.dao.ApiMapper;
import com.eric.dao.ApkMapper;
import com.eric.tools.MD5.MD5Utils;
import com.eric.tools.api.APIHelper;
import com.eric.tools.api.ApiInsertCallable;
import com.eric.tools.threadPool.ThreadPoolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @ClassName: APIService
 * @Description: api管理(存储 、 读取等)
 * @Author: Eric
 * @Date: 2019/3/13 0013
 * @Email: xiao_cui_vip@163.com
 */
@Service
public class APIService {
    @Autowired
    ApkMapper apkMapper;
    @Autowired
    ApiMapper apiMapper;
    @Autowired
    ApiApkMapMapper apiApkMapMapper;


    /**
     * 单线程
     * 非递归遍历文件夹中的所有文件，提取api存入数据库
     *
     * @param src          路径，传入的路径是到应用包名的文件夹
     * @param apkAttribute 应用属性，0表示正常应用 1表示恶意应用
     * @return 存储是否成功
     */
    public Boolean saveApi(String src, int apkAttribute) {
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
                            if (apiMapper == null) {
                                System.out.println("ApiInsertCallable中的apiMapper是空");
                            } else {
                                apis = apiMapper.selectByExample(apiExample);
                            }
                            //数据库中没有该API
                            if (apis.size() == 0) {
                                //构建要插入数据库中的api对象
                                Api api = new Api(item, MD5Utils.MD5Encode(item, "utf8"));
                                apiMapper.insertSelective(api);
                                Integer apiId = api.getApiId();
                                ApiApkMap apiApkMap = new ApiApkMap(apkId, apiId);
                                apiApkMapMapper.insertSelective(apiApkMap);
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
                                    List<ApiApkMap> apiApkMaps = apiApkMapMapper.selectByExample(apiApkMapExample);
                                    if (apiApkMaps.size() == 0) {
                                        //数据库中没有该映射关系
                                        ApiApkMap apiApkMap = new ApiApkMap(apkId, apiId);
                                        //插入
                                        apiApkMapMapper.insertSelective(apiApkMap);
                                    }//否则什么也不做
                                } else {
                                    System.out.println("数据库中有多条重复的api");

                                }

                            }
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
//                            ExecutorService pool2 = Executors.newFixedThreadPool(10);
                            for (String item : apiList) {
                                String md5Value = MD5Utils.MD5Encode(item, "utf8");
                                //先查询数据库中有没有该API
                                ApiExample apiExample = new ApiExample();
                                ApiExample.Criteria criteria = apiExample.createCriteria();
                                criteria.andApiMad5EqualTo(md5Value);
                                List<Api> apis = null;
                                if (apiMapper == null) {
                                    System.out.println("ApiInsertCallable中的apiMapper是空");
                                } else {
                                    apis = apiMapper.selectByExample(apiExample);
                                }
                                //数据库中没有该API
                                if (apis.size() == 0) {
                                    //构建要插入数据库中的api对象
                                    Api api = new Api(item, MD5Utils.MD5Encode(item, "utf8"));
                                    apiMapper.insertSelective(api);
                                    Integer apiId = api.getApiId();
                                    ApiApkMap apiApkMap = new ApiApkMap(apkId, apiId);
                                    apiApkMapMapper.insertSelective(apiApkMap);
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
                                        List<ApiApkMap> apiApkMaps = apiApkMapMapper.selectByExample(apiApkMapExample);
                                        if (apiApkMaps.size() == 0) {
                                            //数据库中没有该映射关系
                                            ApiApkMap apiApkMap = new ApiApkMap(apkId, apiId);
                                            //插入
                                            apiApkMapMapper.insertSelective(apiApkMap);
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
            return true;
        } else {
            //文件不存在
            return false;
        }
    }


    /**
     * 批量提取api特征到数据库
     *
     * @param src 包含各个应用包的总文件夹
     */
    public void batchSaveApi(String src, int apkAttribute) {
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "10");
        long start = System.currentTimeMillis();
        File file = new File(src);
        if (file.exists()) {
            //文件存在
            //文件是目录
            if (file.isDirectory()) {
                //目录下的所有文件
                File[] files = file.listFiles();
                List<File> fileList = Arrays.asList(files);
                fileList.parallelStream().forEach(f -> {

                    String path = f.getAbsolutePath();
                    int apkId = -1;
                    //获取应用的包名
                    String[] split = path.split("\\\\");
                    //获取包名
                    String packageName = split[split.length - 1];
                    Apk apk = new Apk(packageName, apkAttribute);
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
                    File file2 = new File(path);
                    //文件存在
                    if (file.exists()) {
                        LinkedList<File> list = new LinkedList<>();
                        File[] files2 = file.listFiles();
                        //遍历每一个文件
                        for (File f2 : files2) {
                            //是目录
                            if (f2.isDirectory()) {
                                //将文件夹加入队列
                                list.add(f2);
                            } else {
                                //文件路径
                                String filePath = f2.getAbsolutePath();
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
                                        if (apiMapper == null) {
                                            System.out.println("ApiInsertCallable中的apiMapper是空");
                                        } else {
                                            apis = apiMapper.selectByExample(apiExample);
                                        }
                                        //数据库中没有该API
                                        if (apis.size() == 0) {
                                            //构建要插入数据库中的api对象
                                            Api api = new Api(item, MD5Utils.MD5Encode(item, "utf8"));
                                            apiMapper.insertSelective(api);
                                            Integer apiId = api.getApiId();
                                            ApiApkMap apiApkMap = new ApiApkMap(apkId, apiId);
                                            apiApkMapMapper.insertSelective(apiApkMap);
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
                                                List<ApiApkMap> apiApkMaps = apiApkMapMapper.selectByExample(apiApkMapExample);
                                                if (apiApkMaps.size() == 0) {
                                                    //数据库中没有该映射关系
                                                    ApiApkMap apiApkMap = new ApiApkMap(apkId, apiId);
                                                    //插入
                                                    apiApkMapMapper.insertSelective(apiApkMap);
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
                            files2 = tempFile.listFiles();
                            for (File f3 : files2) {
                                //目录
                                if (f3.isDirectory()) {
                                    //加入队列
                                    list.add(f3);
                                } else {
                                    //是文件
                                    //文件路径
                                    String filePath = f3.getAbsolutePath();
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
                                            if (apiMapper == null) {
                                                System.out.println("ApiInsertCallable中的apiMapper是空");
                                            } else {
                                                apis = apiMapper.selectByExample(apiExample);
                                            }
                                            //数据库中没有该API
                                            if (apis.size() == 0) {
                                                //构建要插入数据库中的api对象
                                                Api api = new Api(item, MD5Utils.MD5Encode(item, "utf8"));
                                                apiMapper.insertSelective(api);
                                                Integer apiId = api.getApiId();
                                                ApiApkMap apiApkMap = new ApiApkMap(apkId, apiId);
                                                apiApkMapMapper.insertSelective(apiApkMap);
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
                                                    List<ApiApkMap> apiApkMaps = apiApkMapMapper.selectByExample(apiApkMapExample);
                                                    if (apiApkMaps.size() == 0) {
                                                        //数据库中没有该映射关系
                                                        ApiApkMap apiApkMap = new ApiApkMap(apkId, apiId);
                                                        //插入
                                                        apiApkMapMapper.insertSelective(apiApkMap);
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


                });
                /* try {


                 *//* for (File f : files) {
                        ApiInsertCallable task = new ApiInsertCallable(f.getAbsolutePath(), apkAttribute);
                        ThreadPoolUtil.submit(task);
                    }*//*
                } catch (Exception e) {

                    System.out.println("批量提取api特征到数据库时发生异常，异常信息为：" + e.getMessage());
                }*/
            }
        }
    }
}
