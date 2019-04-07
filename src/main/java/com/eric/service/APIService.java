package com.eric.service;

import com.eric.bean.*;
import com.eric.dao.ApiApkMapMapper;
import com.eric.dao.ApiMapper;
import com.eric.dao.ApkMapper;
import com.eric.exception.MultipleDuplicateValuesInDatabaseException;
import com.eric.tools.MD5.MD5Utils;
import com.eric.tools.api.APIHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName: APIService
 * @Description: api管理(存储 、 读取等)
 * @Author: Eric
 * @Date: 2019/3/13 0013
 * @Email: xiao_cui_vip@163.com
 */
@Service
public class APIService {

    private static final Logger logger = LoggerFactory.getLogger(APIService.class);
    @Autowired
    ApkMapper apkMapper;
    @Autowired
    ApiMapper apiMapper;
    @Autowired
    ApiApkMapMapper apiApkMapMapper;
    //统计一个应用包下有多少.smali文件 泛型里面Sting表示包名 后面的ArrayList<String>存储smali文件绝对路径
    private ConcurrentHashMap<String, ArrayList<String>> packageNameAndSmaliMap = new ConcurrentHashMap<>(30);
    private String packageName="";

    public void batchSaveApi(String src, int apkAttribute) {
        ConcurrentHashMap<String, ArrayList<String>> smaliFiles = countSmaliFile(src);
        smaliFiles.forEach((name,list)->{
            int apkId=getApkId(apkAttribute, packageName);
            int finalApkId = apkId;
            List<String> smaliFilePathList = Collections.synchronizedList(list);
            //设置线程池的大小为10
            int threadNumber=(list.size()<50)?list.size():50;
            System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", String.valueOf(threadNumber));
            smaliFilePathList.parallelStream().forEach(path->{
                File file = new File(path);
                try {
                    smaliFileOperate(finalApkId, file);
                } catch (MultipleDuplicateValuesInDatabaseException e) {
                    e.printStackTrace();
                }

            });


        });

    }

/*    *//**
     * 批量提取api特征到数据库
     *
     * @param src          包含有应用包名的总路径
     * @param apkAttribute 应用的属性 0表示正常应用，1表示恶意应用
     *//*
    public void batchSaveApi(String src, int apkAttribute) {
        //设置线程池的大小为10
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "10");
        File file = new File(src);
        //判断文件是否存在
        if (file.exists()) {
            //文件存在
            //文件是目录
            if (file.isDirectory()) {
                LinkedList<File> list = new LinkedList<>();
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
                    apkId = checkBeforeInsertApi(apkAttribute, path);
                    //文件存在
                    if (f.exists()) {
                        //判断是文件还是文件夹
                        //如果是文件夹
                        if (f.isDirectory()) {
                            File[] currentFileArray = f.listFiles();
                            //遍历每一个文件
                            for (File newCurrentFile : currentFileArray) {
                                fileOperate(list, apkId, newCurrentFile);
                            }
                            File tempFile;
                            //list不空
                            while (!list.isEmpty()) {
                                //选取list的第一个文件
                                tempFile = list.removeFirst();
                                currentFileArray = tempFile.listFiles();
                                for (File newCurrentFile : currentFileArray) {
                                    //目录
                                    fileOperate(list, apkId, newCurrentFile);
                                }
                            }

                        } else {
                            //当前文件不是文件夹是文件
                            try {
                                smaliFileOperate(apkId, f);
                            } catch (MultipleDuplicateValuesInDatabaseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    System.out.println("--------------线程" + currentThreadName + "执行结束------------");
                });
            } else {
                //不是目录
                System.out.println(Thread.currentThread().getName() + ":>>>>>>>>>>>>请输入包含应用包名的路径！");
            }
        }
    }*/

    /**
     * 遍历每一个文件
     * @param list 列表
     * @param apkId 应用id
     * @param newCurrentFile 当前文件
     */
    public void fileOperate(LinkedList<File> list, int apkId, File newCurrentFile) {
        //是目录
        if (newCurrentFile.isDirectory()) {
            //将文件夹加入队列
            list.add(newCurrentFile);
        } else {
            try {
                smaliFileOperate(apkId, newCurrentFile);
            } catch (MultipleDuplicateValuesInDatabaseException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * smali文件操作方法
     *
     * @param apkId          应用id
     * @param newCurrentFile 当前操作的文件
     */
    public void smaliFileOperate(int apkId, File newCurrentFile) throws MultipleDuplicateValuesInDatabaseException {
        //文件路径
        String filePath = newCurrentFile.getAbsolutePath();
        //该文件是否.smali文件
        int len = filePath.length();
        String s = filePath.substring(filePath.length() - 6, len);
        if (s.equals(".smali")) {
            //提取API信息
            List<String> apiList = APIHelper.handle(filePath);
            for (String item : apiList) {
                apiOperate(apkId, item, filePath);
            }
        }
    }

    /**
     * 在进行api插入前先进行检查，主要检查数据库中是否已经存在相同记录
     *
     * @param apkAttribute 应用属性，0表示正常应用，1表示恶意应用
     * @param path         路径
     * @return 当前应用id
     */
    public int checkBeforeInsertApi(int apkAttribute, String path) {
        String packageName = getPackageName(path);
        int apkId = getApkId(apkAttribute, packageName);
        return apkId;
    }

    public synchronized int getApkId(int apkAttribute, String packageName) {
        int apkId=-1;
        Apk apk = new Apk(packageName, apkAttribute);
        //在插入之前先判断数据库中有没有
        ApkExample apkExample = new ApkExample();
        ApkExample.Criteria apkCriteria = apkExample.createCriteria();
        apkCriteria.andPackageNameEqualTo(packageName);
        apkCriteria.andApkAttributeEqualTo(apkAttribute);
        List<Apk> apks = apkMapper.selectByExample(apkExample);
        if (apks.size() == 0) {
            //数据库中没有，插入
            if (null != apkMapper) {
                System.out.println(Thread.currentThread().getName() + ":数据库中没有该apk记录，正在执行插入....");

                //插入成功的apk记录数
                int apkInsertNum = apkMapper.insertSelective(apk);
                if (apkInsertNum > 0) {
                    System.out.println(Thread.currentThread().getName() + ":apk记录插入成功");

                } else {
                    System.err.println(Thread.currentThread().getName() + ":apk记录插入失败");
                }
            } else {
                System.out.println(Thread.currentThread().getName() + ":apkMapper为空");
            }

            //获取id
            if (apk.getApkId() != null) {

                apkId = apk.getApkId();

            } else {
                System.out.println(Thread.currentThread().getName() + ":返回的id是空");
            }


        } else {
            //数据库中已经存在与当前api相同的记录
            apkId = getApkId(apks);
        }
        return apkId;
    }

    public String getPackageName(String path) {
        //获取应用的包名
        String[] split = path.split("\\\\");
        //获取包名
        return split[split.length - 1];
    }

    /**
     * 获取应用id
     *
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
     *
     * @param apkId 应用id
     * @param item  要操作的某一条具体的api
     */
    public synchronized void apiOperate(int apkId, String item,  String path) throws MultipleDuplicateValuesInDatabaseException {
        String md5Value = MD5Utils.MD5Encode(item, "utf8");
        //先查询数据库中有没有该API
        ApiExample apiExample = getApiExample(md5Value);
        List<Api> apis = null;
        if (apiMapper == null) {
            System.out.println(Thread.currentThread().getName() + "当前正在操作的文件是：" + path + ":ApiService中的apiMapper是空");
        } else {
            apis = apiMapper.selectByExample(apiExample);
        }
        //数据库中没有该API记录
        if (apis.size() == 0) {
            System.out.println(Thread.currentThread().getName() + "当前正在操作的文件是：" + path + ":数据库中没有当前api记录，开始插入该api记录....");
            //构建要插入数据库中的api对象
            Api api = new Api(item, MD5Utils.MD5Encode(item, "utf8"));
            //返回值是插入到数据库中的api的数量
            int apiNum = apiMapper.insertSelective(api);
            //是否插入成功的判断
            String isApiInsertSuccess = (apiNum > 0) ? "成功" : "失败";
            System.out.println(Thread.currentThread().getName() + ":api记录映射关系记录插入" + isApiInsertSuccess);
            System.out.println(Thread.currentThread().getName() + "当前正在操作的文件是：" + path + ":开始插入api-apk映射关系");
            Integer apiId = api.getApiId();
            ApiApkMap apiApkMap = new ApiApkMap(apkId, apiId);
            //插入的api 和apk的映射关系的条数
            int apiApkMapNum = apiApkMapMapper.insertSelective(apiApkMap);
            String isApiApkInsertSuccess = (apiApkMapNum > 0) ? "成功" : "失败";
            System.out.println(Thread.currentThread().getName() + ":api-apk映射关系记录插入" + isApiApkInsertSuccess);
            System.out.println(Thread.currentThread().getName() + "当前正在操作的文件是：" + path + "当前正在操作的文件是：" + path + ":插入api-apk映射关系完成");
        } else if (apis.size() == 1) {
            //数据库中有一条该记录
            System.out.println(Thread.currentThread().getName() + "当前正在操作的文件是：" + path + ":数据库中存在当前api记录，正在查询api-apk映射关系是否存在....");
            Api api1 = null;
            api1 = apis.get(0);
            //获取api id
            Integer apiId = api1.getApiId();
            //查询映射关系是否在数据库中已经存在
            ApiApkMapExample apiApkMapExample = getApiApkMapExample(apkId, apiId);
            List<ApiApkMap> apiApkMaps = apiApkMapMapper.selectByExample(apiApkMapExample);
            if (apiApkMaps.size() == 0) {
                //数据库中没有该映射关系
                System.out.println(Thread.currentThread().getName() + "当前正在操作的文件是：" + path + ":数据库中不存在当前api-apk映射关系，正在插入该映射关系...");
                ApiApkMap apiApkMap = new ApiApkMap(apkId, apiId);
                //插入的api 和apk的映射关系的条数
                int apiApkMapNum = apiApkMapMapper.insertSelective(apiApkMap);
                String isApiApkInsertSuccess = (apiApkMapNum > 0) ? "成功" : "失败";

                System.out.println(Thread.currentThread().getName() + ":api-apk映射关系记录插入" + isApiApkInsertSuccess);
                System.out.println(Thread.currentThread().getName() + "当前正在操作的文件是：" + path + ":api-apk映射关系插入完成....");
            } else if (apiApkMaps.size() == 1) {
                //数据库中已经存在一条api-apk映射关系
                System.out.println(Thread.currentThread().getName() + "当前正在操作的文件是：" + path + ":>>>>>>>>数据库中已经存在一条api-apk映射关系，本次未进行插入操作");
            } else if (apiApkMaps.size() > 1) {
                //数据库中存在多条相同的映射关系
                //理论上不可能
                System.out.println(Thread.currentThread().getName() + "当前正在操作的文件是：" + path + ":>>>>>>>>数据库中存在多条相同的api-apk映射关系记录，记录数为:" + apiApkMaps.size());
            }
        } else if (apis.size() > 1) {
            //数据库中有多余一条记录
            //数据库中存在多条相同的映射关系
            //理论上不可能
            throw  new MultipleDuplicateValuesInDatabaseException("数据库中有"+apis.size()+"条相同的记录");

        }
    }

    /**
     * 获取用户根据example进行数据库查询的example，主要用户判断数据库中是否已经存在相同记录
     *
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
     *
     * @param md5Value md5值
     * @return 用于判断的example对象
     */

    public ApiExample getApiExample(String md5Value) {
        ApiExample apiExample = new ApiExample();
        ApiExample.Criteria criteria = apiExample.createCriteria();
        criteria.andApiMad5EqualTo(md5Value);
        return apiExample;
    }


    /**
     * 统计一个应用包下有多少个smali文件并加入队列
     *
     * @param packageSrc 包所在的路径
     * @return
     */
    public ConcurrentHashMap<String, ArrayList<String>> countSmaliFile(String packageSrc) {

        File file = new File(packageSrc);
        //判断文件是否存在
        if (file.exists()) {
            //文件存在
            //文件是目录
            if (file.isDirectory()) {
                LinkedList<File> list = new LinkedList<>();
                //目录下的所有文件
                File[] files = file.listFiles();
                //转为list，为了使用jdk8中的多线程并发，见其下面的那行代码
                List<File> flist = Arrays.asList(files);
                List<File> fileList = Collections.synchronizedList(flist);
                for (File f : files) {
                    //smali文件队列
                    ArrayList<String> smaliList = new ArrayList<>();
                    //获取当前正在执行的线程的名称
                    String currentThreadName = Thread.currentThread().getName();
                    //输出当前开始执行的线程名称
                    System.out.println("搜索smali文件的线程" + currentThreadName + "开始执行");
                    //获取当前文件的绝对路径
                    String path = f.getAbsolutePath();
                    //获取包名
                    packageName = getPackageName(path);
                    //文件存在
                    if (f.exists()) {
                        //判断是文件还是文件夹
                        //如果是文件夹
                        if (f.isDirectory()) {
                            File[] currentFileArray = f.listFiles();
                            //遍历每一个文件
                            for (File newCurrentFile : currentFileArray) {
                                //是目录
                                if (newCurrentFile.isDirectory()) {
                                    //将文件夹加入队列
                                    list.add(newCurrentFile);
                                } else {
                                    //文件路径
                                    checkAndAddSmali2List(smaliList, newCurrentFile);
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
                                        //文件路径
                                        checkAndAddSmali2List(smaliList, newCurrentFile);
                                    }
                                }
                            }
                        } else {
                            //当前文件不是文件夹是文件
                            checkAndAddSmali2List(smaliList, f);
                        }
                    }
                    System.out.println("--------------线程" + currentThreadName + "执行结束------------");
                    packageNameAndSmaliMap.put(packageName, smaliList);

                }
            } else {
                //不是目录
                System.out.println(Thread.currentThread().getName() + ":>>>>>>>>>>>>请输入包含应用包名的路径！");
            }
        }
        return packageNameAndSmaliMap;
    }

    /**
     * 检查是否是smali文件，是的话加入队列
     *
     * @param smaliList      smali文件列表
     * @param newCurrentFile 当前文件
     */
    public void checkAndAddSmali2List(List<String> smaliList, File newCurrentFile) {
        String filePath = newCurrentFile.getAbsolutePath();
        //该文件是否.smali文件
        int len = filePath.length();
        String s = filePath.substring(filePath.length() - 6, len);
        if (s.equals(".smali")) {
            //加入队列
            smaliList.add(filePath);

        }
    }

}






