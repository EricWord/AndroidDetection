package com.eric.service;

import com.eric.bean.*;
import com.eric.dao.ApiApkMapMapper;
import com.eric.dao.ApiMapper;
import com.eric.dao.ApkMapper;
import com.eric.tools.MD5.MD5Utils;
import com.eric.tools.api.APIHelper;
import com.eric.tools.api.ApiInsertCallable;
import com.eric.tools.threadPool.ThreadPoolUtil;
import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

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
     * 批量提取api特征到数据库
     *
     * @param src          包含有应用包名的总路径
     * @param apkAttribute 应用的属性 0表示正常应用，1表示恶意应用
     */
    public void batchSaveApi(String src, int apkAttribute) {
        //设置线程池的大小为10
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "10");
        File file = new File(src);
        //判断文件是否存在
        if (file.exists()) {
            //文件存在
            //文件是目录
            if (file.isDirectory()) {
                //目录下的所有文件
                File[] files = file.listFiles();
                //转为list，为了使用jdk8中的多线程并发，见其下面的那行代码
                List<File> fileList = Arrays.asList(files);
                fileList.parallelStream().forEach(f -> {
                    //获取当前正在执行的线程的名称
                    String currentThreadName = Thread.currentThread().getName();
                    //输出当前开始执行的线程名称
                    System.out.println("线程" + currentThreadName + "开始执行");
                    //获取当前文件的绝对路径
                    String path = f.getAbsolutePath();
                    int apkId = -1;
                    apkId = checkBeforeInsertApi(apkAttribute, path, apkId);
                    //当前操作的文件
                    File currentFile = new File(path);
                    //文件存在
                    if (currentFile.exists()) {
                        //判断是文件还是文件夹
                        //如果是文件夹
                        if (currentFile.isDirectory()) {
                            LinkedList<File> list = new LinkedList<>();
                            File[] currentFileArray = currentFile.listFiles();
                            //遍历每一个文件
                            for (File newCurrentFile : currentFileArray) {
                                //是目录
                                if (newCurrentFile.isDirectory()) {
                                    //将文件夹加入队列
                                    list.add(newCurrentFile);
                                } else {
                                    smaliFileOperate(apkId, newCurrentFile, Thread.currentThread().getName()+":>>>>>>>>数据库中存在多条相同的api记录，记录数为:");
                                }
                            }
                            File tempFile;
                            //list不空
                            while (!list.isEmpty()) {
                                //选取list的第一个文件
                                tempFile = list.removeFirst();
                                currentFileArray = tempFile.listFiles();
                                for (File newCurrentFile : currentFileArray) {
                                    //目录
                                    if (newCurrentFile.isDirectory()) {
                                        //加入队列
                                        list.add(newCurrentFile);
                                    } else {
                                        //是文件
                                        smaliFileOperate(apkId, newCurrentFile, Thread.currentThread().getName()+":>>>>>>>数据库中有多条相同的api记录，相同的api记录数为：");
                                    }
                                }
                            }

                        } else {
                            //当前文件不是文件夹是文件
                            //这里的代码一会抽取出来后补充上
                            smaliFileOperate(apkId, currentFile, Thread.currentThread().getName()+":>>>>>>>数据库中有多条相同的api记录，相同的api记录数为：");
                        }
                    }
                    System.out.println("--------------线程" + currentThreadName + "执行结束------------");
                });
            } else {
                //不是目录
                System.out.println(Thread.currentThread().getName()+":>>>>>>>>>>>>请输入包含应用包名的路径！");
            }
        }
    }

    /**
     * smali文件操作方法
     * @param apkId 应用id
     * @param newCurrentFile 当前操作的文件
     * @param s2 提示信息
     */
    public void smaliFileOperate(int apkId, File newCurrentFile, String s2) {
        //文件路径
        String filePath = newCurrentFile.getAbsolutePath();
        //该文件是否.smali文件
        String s = filePath.substring(filePath.length() - 6, filePath.length());
        if (s.equals(".smali")) {
            //提取API信息
            List<String> apiList = APIHelper.handle(filePath);
            for (String item : apiList) {
                apiOperate(apkId, item, s2,filePath);
            }
        }
    }

    /**
     * 在进行api插入前先进行检查，主要检查数据库中是否已经存在相同记录
     * @param apkAttribute 应用属性，0表示正常应用，1表示恶意应用
     * @param path 路径
     * @param apkId 应用id
     * @return 当前应用id
     */
    public int checkBeforeInsertApi(int apkAttribute, String path, int apkId) {
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
                System.out.println(Thread.currentThread().getName()+":apkMapper为空");
            }

            //获取id
            if (apk.getApkId() != null) {

                apkId = apk.getApkId();

            } else {
                System.out.println(Thread.currentThread().getName()+":返回的id是空");
            }


        } else {
            apkId = getApkId(apks);
        }
        return apkId;
    }

    /**
     * 获取应用id
     * @param apks 包含多个apk的List
     * @return 应用id
     */
    public int getApkId(List<Apk> apks) {
        int apkId;//数据库中已经存在
        //获取Id
        Apk apk1 = apks.get(0);
        apkId = apk1.getApkId();
        return apkId;
    }

    /**
     * api操作方法
     * @param apkId 应用id
     * @param item 要操作的某一条具体的api
     * @param s2 提示信息
     */
    public void apiOperate(int apkId, String item, String s2,String path) {
        String md5Value = MD5Utils.MD5Encode(item, "utf8");
        //先查询数据库中有没有该API
        ApiExample apiExample = getApiExample(md5Value);
        List<Api> apis = null;
        if (apiMapper == null) {
            System.out.println(Thread.currentThread().getName()+"当前正在操作的文件是："+path+":ApiInsertCallable中的apiMapper是空");
        } else {
            apis = apiMapper.selectByExample(apiExample);
        }
        //数据库中没有该API记录
        if (apis.size() == 0) {
            System.out.println(Thread.currentThread().getName()+"当前正在操作的文件是："+path+":数据库中没有当前api记录，开始插入该api记录....");
            //构建要插入数据库中的api对象
            Api api = new Api(item, MD5Utils.MD5Encode(item, "utf8"));
            apiMapper.insertSelective(api);
            System.out.println(Thread.currentThread().getName()+"当前正在操作的文件是："+path+":插入api记录完成....,开始插入api-apk映射关系");
            Integer apiId = api.getApiId();
            ApiApkMap apiApkMap = new ApiApkMap(apkId, apiId);
            apiApkMapMapper.insertSelective(apiApkMap);
            System.out.println(Thread.currentThread().getName()+"当前正在操作的文件是："+path+"当前正在操作的文件是："+path+":插入api-apk映射关系完成");
        } else if (apis.size() == 1) {
            //数据库中有一条该记录
            System.out.println(Thread.currentThread().getName()+"当前正在操作的文件是："+path+":数据库中存在当前api记录，正在查询api-apk映射关系是否存在....");
            Api api1 = null;
            api1 = apis.get(0);
            //获取api id
            Integer apiId = api1.getApiId();
            //查询映射关系是否在数据库中已经存在
            ApiApkMapExample apiApkMapExample = getApiApkMapExample(apkId, apiId);
            List<ApiApkMap> apiApkMaps = apiApkMapMapper.selectByExample(apiApkMapExample);
            if (apiApkMaps.size() == 0) {
                //数据库中没有该映射关系
                System.out.println(Thread.currentThread().getName()+"当前正在操作的文件是："+path+":数据库中不存在当前api-apk映射关系，正在插入该映射关系...");
                ApiApkMap apiApkMap = new ApiApkMap(apkId, apiId);
                //插入
                apiApkMapMapper.insertSelective(apiApkMap);
                System.out.println(Thread.currentThread().getName()+"当前正在操作的文件是："+path+":api-apk映射关系插入完成....");
            } else if (apiApkMaps.size() == 1) {
                //数据库中已经存在一条api-apk映射关系
                System.out.println(Thread.currentThread().getName()+"当前正在操作的文件是："+path+":>>>>>>>>数据库中已经存在一条api-apk映射关系，本次未进行插入操作");
            } else if (apiApkMaps.size() > 1) {
                //数据库中存在多条相同的映射关系
                //理论上不可能
                System.out.println(Thread.currentThread().getName()+"当前正在操作的文件是："+path+":>>>>>>>>数据库中存在多条相同的api-apk映射关系记录，记录数为:" + apiApkMaps.size());
            }
        } else if (apis.size() > 1) {
            //数据库中有多余一条记录
            //数据库中存在多条相同的映射关系
            //理论上不可能
            System.out.println(Thread.currentThread().getName()+":"+s2 + apis.size());

        }
    }

    /**
     * 获取用户根据example进行数据库查询的example，主要用户判断数据库中是否已经存在相同记录
     * @param apkId 应用id
     * @param apiId api id
     * @return 用于判断的example对象
     */
    public ApiApkMapExample getApiApkMapExample(int apkId, Integer apiId) {
        ApiApkMapExample apiApkMapExample = new ApiApkMapExample();
        ApiApkMapExample.Criteria apiApkCriteria = apiApkMapExample.createCriteria();
        apiApkCriteria.andApiIdEqualTo(apiId);
        apiApkCriteria.andApkIdEqualTo(apkId);
        return apiApkMapExample;
    }

    /**
     * 获取根据api md5值来进行数据库查询的example
     * @param md5Value md5值
     * @return 用于判断的example对象
     */

    public ApiExample getApiExample(String md5Value) {
        ApiExample apiExample = new ApiExample();
        ApiExample.Criteria criteria = apiExample.createCriteria();
        criteria.andApiMad5EqualTo(md5Value);
        return apiExample;
    }
}
