package com.eric.tools.api;

import com.eric.bean.*;
import com.eric.dao.ApiApkMapMapper;
import com.eric.dao.ApiMapper;
import com.eric.dao.ApkMapper;
import com.eric.tools.MD5.MD5Utils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 提取API特征的工具类
 */
@Component
public class APIHelper {
    @Resource
    private ApiApkMapMapper apiApkMapMapper;
    @Resource
    private ApiMapper apiMapper;
    @Resource
    private ApkMapper apkMapper;
    public static APIHelper apiHelper;

    @PostConstruct
    public void init() {
        apiHelper = this;

    }
    /*
     *
     * 主要提取思路：
     * 1.遍历每个.smali文件
     * 2.根据API在.smali文件中的格式声明：
     *  .method<访问权限>[修饰关键字]<API原型>
     *
     */


    /**
     * 非递归遍历文件夹中的所有文件，提取api存入数据库
     *
     * @param src          路径，传入的路径是到应用包名的文件夹
     * @param apkAttribute 应用属性，0表示正常应用 1表示恶意应用
     * @return 存储是否成功
     */
    public  Boolean saveApi(String src, int apkAttribute) {
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
        List<Apk> apks = apiHelper.apkMapper.selectByExample(apkExample);
        if (apks.size() == 0) {
            //数据库中没有，插入
            if (null != apiHelper.apkMapper) {

                apiHelper.apkMapper.insertSelective(apk);
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
                            if (apiHelper.apiMapper == null) {
                                System.out.println("ApiInsertCallable中的apiMapper是空");
                            } else {
                                apis = apiHelper.apiMapper.selectByExample(apiExample);
                            }
                            //数据库中没有该API
                            if (apis.size() == 0) {
                                //构建要插入数据库中的api对象
                                Api api = new Api(item, MD5Utils.MD5Encode(item, "utf8"));
                                apiHelper.apiMapper.insertSelective(api);
                                Integer apiId = api.getApiId();
                                ApiApkMap apiApkMap = new ApiApkMap(apkId, apiId);
                                apiHelper.apiApkMapMapper.insertSelective(apiApkMap);
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
                                    List<ApiApkMap> apiApkMaps = apiHelper.apiApkMapMapper.selectByExample(apiApkMapExample);
                                    if (apiApkMaps.size() == 0) {
                                        //数据库中没有该映射关系
                                        ApiApkMap apiApkMap = new ApiApkMap(apkId, apiId);
                                        //插入
                                        apiHelper.apiApkMapMapper.insertSelective(apiApkMap);
                                    }//否则什么也不做
                                } else {
                                    System.out.println("数据库中有多条重复的api");

                                }

                            }
                            //新改内容结束------


                            //ApiInsertCallable task = new ApiInsertCallable(item, apkId);
                            //pool1.submit(task);
                        }
                        //关闭线程池
                        //pool1.shutdown();
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
                                if (apiHelper.apiMapper == null) {
                                    System.out.println("ApiInsertCallable中的apiMapper是空");
                                } else {
                                    apis = apiHelper.apiMapper.selectByExample(apiExample);
                                }
                                //数据库中没有该API
                                if (apis.size() == 0) {
                                    //构建要插入数据库中的api对象
                                    Api api = new Api(item, MD5Utils.MD5Encode(item, "utf8"));
                                    apiHelper.apiMapper.insertSelective(api);
                                    Integer apiId = api.getApiId();
                                    ApiApkMap apiApkMap = new ApiApkMap(apkId, apiId);
                                    apiHelper.apiApkMapMapper.insertSelective(apiApkMap);
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
                                        List<ApiApkMap> apiApkMaps = apiHelper.apiApkMapMapper.selectByExample(apiApkMapExample);
                                        if (apiApkMaps.size() == 0) {
                                            //数据库中没有该映射关系
                                            ApiApkMap apiApkMap = new ApiApkMap(apkId, apiId);
                                            //插入
                                            apiHelper.apiApkMapMapper.insertSelective(apiApkMap);
                                        }//否则什么也不做
                                    } else {
                                        System.out.println("数据库中有多条重复的api");

                                    }

                                }
                            }
                            //关闭线程池
//                            pool1.shutdown();
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
     * 读取.smali文件并提取API
     *
     * @param src 文件路径
     */
    public static List<String> handle(String src) {
        List<String> list = new ArrayList<>();
        BufferedReader br = null;
        try {
            //源文件
            File srcFile = new File(src);
            FileReader fr = new FileReader(srcFile);
            br = new BufferedReader(fr);
            String str;
            while ((str = br.readLine()) != null) {
                //判断当前行是否以.method开始
                boolean flag = str.startsWith(".method");
                if (flag) {
                    //是以.method开始
                    //提取API
                    list.add(str);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }
}
