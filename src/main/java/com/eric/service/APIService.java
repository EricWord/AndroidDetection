package com.eric.service;

import com.eric.bean.*;
import com.eric.dao.ApiApkMapMapper;
import com.eric.dao.ApiMapper;
import com.eric.dao.ApkMapper;
import com.eric.tools.MD5.MD5Utils;
import com.eric.tools.api.APIHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

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
     * 非递归遍历文件夹中的所有文件，提取api存入数据库
     *
     * @param src 路径
     * @return 存储是否成功
     */
    public Boolean saveApi(String src) {
        int apkId = -1;
        //获取应用的包名
        String[] split = src.split("\\\\");
        //获取包名
        String packageName = split[split.length - 1];
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


        }else{
            //数据库中已经存在
            //获取Id
            Apk apk1 = apks.get(0);
            apkId=apk1.getApkId();
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
                            Api api = new Api(item, MD5Utils.MD5Encode(item, "utf8"));
                            apiMapper.insertSelective(api);
                            Integer apiId = api.getApiId();
                            ApiApkMap apiApkMap = new ApiApkMap(apkId, apiId);
                            apiApkMapMapper.insert(apiApkMap);
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
                        System.out.println(filePath);
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
                                List<Api> apis = apiMapper.selectByExample(apiExample);
                                Api api = new Api(item, MD5Utils.MD5Encode(item, "utf8"));
                                //数据库中没有该API
                                if (apis.size() == 0) {
                                    apiMapper.insertSelective(api);
                                    Integer apiId = api.getApiId();
                                    ApiApkMap apiApkMap = new ApiApkMap(apkId, apiId);
                                    apiApkMapMapper.insert(apiApkMap);
                                } else {
                                    //数据库中有该记录
                                    Api api1 = apis.get(0);
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
                                        apiApkMapMapper.insert(apiApkMap);
                                    }//否则什么也不做
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
}
