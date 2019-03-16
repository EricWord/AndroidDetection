package com.eric.service;

import com.eric.bean.Api;
import com.eric.bean.ApiApkMap;
import com.eric.bean.Apk;
import com.eric.bean.ApkExample;
import com.eric.dao.ApiApkMapMapper;
import com.eric.dao.ApiMapper;
import com.eric.dao.ApkMapper;
import com.eric.tools.MD5.MD5Utils;
import com.eric.tools.api.APIHelper;
import com.eric.tools.decode.APKTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ContextConfiguration;

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
        int apkId=-1;
        //获取应用的包名
        String[] split = src.split("\\\\");
        //获取包名
        String packageName = split[split.length - 1];
        Apk apk = new Apk(packageName, 0);
        if(null!=apkMapper){

        apkMapper.insertSelective(apk);
        }else{
            System.out.println("apkMapper为空");
        }


        if(apk.getApkId()!=null){

            apkId = apk.getApkId();

        }else{
            System.out.println("返回的id是空");
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
                                String md5Value=MD5Utils.MD5Encode(item, "utf8");
                                //先查询数据库中有没有该API
                                Api api = new Api(item, MD5Utils.MD5Encode(item, "utf8"));
                                apiMapper.insertSelective(api);
                                Integer apiId = api.getApiId();
                                //这句注释掉了，记得到时候打开注释
                                ApiApkMap apiApkMap = new ApiApkMap(apkId, apiId);
                                apiApkMapMapper.insert(apiApkMap);
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
