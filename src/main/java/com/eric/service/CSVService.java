package com.eric.service;


import com.eric.bean.*;
import com.eric.dao.*;
import com.eric.tools.csv.CSVUtils;
import com.eric.tools.csv.FileConstantUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/*
 *@description:生成CSV格式文件的服务层
 *@author:cuigs
 *@Email:cuigs@ti-net.com.cn
 *@Version:1.0
 *@Date:2019/3/31
 */
@Service
public class CSVService {
    @Autowired
    ApkMapper apkMapper;
    @Autowired
    ApiMapper apiMapper;
    @Autowired
    AuthorityMapper authorityMapper;
    @Autowired
    ApiApkMapMapper apiApkMapMapper;
    @Autowired
    AuthorityApkMapMapper authorityApkMapMapper;

    /**
     * 将数据库中的表转为CSV格式的文件，该方法只需要执行一次即可
     */
    public void createCSVFile() {
        DateTime dateTime = new DateTime();
        //当前时间
        String stringDate = dateTime.toString("yyyy_MM_dd_HH_mm_ss", Locale.CHINESE);
        //将apk表转为CSV格式的文件
        tableApk2CSV(stringDate);
        //将API表转为CSV格式的文件
        tableApi2CSV(stringDate);
        //将权限表转为CSV格式的文件
        tableAuthority2CSV(stringDate);
        //将API-应用关系映射表转为CSV格式的文件
        tableApiApkMap2CSV(stringDate);
        //将权限-应用关系映射表转为CSV格式文件
        tableAuthorityApkMap2CSV(stringDate);

    }

    /**
     * 将权限-应用关系映射表转为CSV格式文件
     * @param stringDate 用于拼接文件名的当前时间，保证每次生成的CSV文件名称不重复
     */
    public void tableAuthorityApkMap2CSV(String stringDate) {
        //tb_authority_apk_map
        List<AuthorityApkMap> authorityApkMaps = authorityApkMapMapper.selectByExample(null);
        //遍历每一个authorityApkMap
        List<String> authorityApkMapTableFiledList = new ArrayList<>();
        for (AuthorityApkMap authorityApkMap : authorityApkMaps) {
            authorityApkMapTableFiledList.add(authorityApkMap.getAuthorityId().toString());
            authorityApkMapTableFiledList.add(authorityApkMap.getApkId().toString());
        }
        System.out.println(">>>>>>authorityApkMapTableFiledList的大小为:"+authorityApkMapTableFiledList.size());
        //将tb_authority_apk_map数据库表转为CSV格式的文件
        CSVUtils.createCSVFile(FileConstantUtils.TABLE_AUTHORITY_APK_MAP_LIST, authorityApkMapTableFiledList, "D:\\cgs\\File\\CSV", "tb_authority_apk_map_" + stringDate);
        authorityApkMapTableFiledList.clear();
    }

    /**
     * 将API-应用关系映射表转为CSV格式的文件
     * @param stringDate 用于拼接文件名的当前时间，保证每次生成的CSV文件名称不重复
     */
    public void tableApiApkMap2CSV(String stringDate) {
        List<ApiApkMap> apiApkMaps = apiApkMapMapper.selectByExample(null);
        //遍历每一个apiApkMaps
        List<String> apiApkMapsTableFiledList = new ArrayList<>();
        for (ApiApkMap apiApkMap : apiApkMaps) {
            apiApkMapsTableFiledList.add(apiApkMap.getApiId().toString());
            apiApkMapsTableFiledList.add(apiApkMap.getApkId().toString());
        }
        System.out.println(">>>>>>>apiApkMapsTableFiledList的大小为："+apiApkMapsTableFiledList.size());
        //将tb_api_apk_map数据库表转为CSV格式的文件
        CSVUtils.createCSVFile(FileConstantUtils.TABLE_API_APK_MAP_LIST, apiApkMapsTableFiledList, "D:\\cgs\\File\\CSV", "tb_api_apk_map_" + stringDate);
        apiApkMapsTableFiledList.clear();
    }

    /**
     * 将权限表转为CSV格式的文件
     * @param stringDate 用于拼接文件名的当前时间，保证每次生成的CSV文件名称不重复
     */
    public void tableAuthority2CSV(String stringDate) {
        List<Authority> authorities = authorityMapper.selectByExample(null);
        //遍历每一个authority
        List<String> authorityTableFiledList = new ArrayList<>();
        for (Authority authority : authorities) {

            authorityTableFiledList.add(authority.getAuthorityId().toString());
            authorityTableFiledList.add(authority.getAuthorityContent());
            authorityTableFiledList.add(authority.getAuthorityMd5());
        }

        System.out.println(">>>>>>authorityTableFiledList的大小为："+authorityTableFiledList.size());

        //将tb_authority数据库表转为CSV格式的文件
        CSVUtils.createCSVFile(FileConstantUtils.TABLE_AUTHORITY_LIST, authorityTableFiledList, "D:\\cgs\\File\\CSV", "tb_authority_" + stringDate);
        //释放空间，防止堆溢出
        authorityTableFiledList.clear();
    }

    /**
     * 将API表转为CSV格式的文件
     * @param stringDate 用于拼接文件名的当前时间，保证每次生成的CSV文件名称不重复
     */
    public void tableApi2CSV(String stringDate) {
        List<Api> apis = apiMapper.selectByExample(null);
        //遍历每一个api
        List<String> apiTableFiledList = new ArrayList<>();
        for (Api api : apis) {
            apiTableFiledList.add(api.getApiId().toString());
            apiTableFiledList.add(api.getApiContent());
            apiTableFiledList.add(api.getApiMad5());
        }
        System.out.println(">>>>>>>apiTableFiledList的大小为："+apiTableFiledList.size());
        //将tb_api数据库表转为CSV格式的文件
        CSVUtils.createCSVFile(FileConstantUtils.TABLE_API_LIST, apiTableFiledList, "D:\\cgs\\File\\CSV", "tb_api_" + stringDate);
        apiTableFiledList.clear();
    }

    /**
     * 将apk表转为CSV格式的文件
     * @param stringDate 用于拼接文件名的当前时间，保证每次生成的CSV文件名称不重复
     */

    public void tableApk2CSV(String stringDate) {
        //查询数tb_apk表中的所有记录
        List<Apk> apks = apkMapper.selectByExample(null);
        //遍历每一个apk
        List<String> tableFiledList =new ArrayList<>();
        for (Apk apk : apks) {
            tableFiledList.add(apk.getApkId().toString());
            tableFiledList.add(apk.getPackageName());
            tableFiledList.add(apk.getApkAttribute().toString());

        }
        System.out.println(">>>>>tableFiledList的大小为："+tableFiledList.size());
        //将tb_apk数据库转为CSV格式的文件
        CSVUtils.createCSVFile(FileConstantUtils.TABLE_APK_LIST, tableFiledList, "D:\\cgs\\File\\CSV", "tb_apk_" + stringDate);
        tableFiledList.clear();
    }


}
