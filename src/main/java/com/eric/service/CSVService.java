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
        //查询数tb_apk表中的所有记录
        List<Apk> apks = apkMapper.selectByExample(null);
        //将apk的字段转储到List中
        List<List<String>> dataList = new ArrayList<>();
        //遍历每一个apk
        List<String> tableFiledList =new ArrayList<>();
        for (Apk apk : apks) {
            tableFiledList.add(apk.getApkId().toString());
            tableFiledList.add(apk.getPackageName());
            tableFiledList.add(apk.getApkAttribute().toString());
            dataList.add(tableFiledList);

        }
        System.out.println(">>>>>dataList的大小为："+dataList.size());
        //将tb_apk数据库转为CSV格式的文件
        CSVUtils.createCSVFile(FileConstantUtils.TABLE_APK_LIST, dataList, "D:\\cgs\\File\\CSV", "tb_apk_" + stringDate);
        dataList.clear();

        //--------------------------------------------------------
        List<Api> apis = apiMapper.selectByExample(null);
        //将api的字段转储到List中
        List<List<String>> apiDataList = new ArrayList<>();
        //遍历每一个api
        List<String> apiTableFiledList = new ArrayList<>();
        for (Api api : apis) {
            apiTableFiledList.add(api.getApiId().toString());
            apiTableFiledList.add(api.getApiContent());
            apiTableFiledList.add(api.getApiMad5());
            apiDataList.add(apiTableFiledList);
        }
        System.out.println(">>>>>>>apiDataList的大小为："+apiDataList.size());
        //将tb_api数据库表转为CSV格式的文件
        CSVUtils.createCSVFile(FileConstantUtils.TABLE_API_LIST, apiDataList, "D:\\cgs\\File\\CSV", "tb_api_" + stringDate);
        apiDataList.clear();

        //--------------------------------------------------------

        List<Authority> authorities = authorityMapper.selectByExample(null);
        //将authority的字段转储到List中
        List<List<String>> authorityDataList = new ArrayList<>();
        //遍历每一个authority
        List<String> authorityTableFiledList = new ArrayList<>();
        for (Authority authority : authorities) {

            authorityTableFiledList.add(authority.getAuthorityId().toString());
            authorityTableFiledList.add(authority.getAuthorityContent());
            authorityTableFiledList.add(authority.getAuthorityMd5());
            authorityDataList.add(authorityTableFiledList);
        }

        System.out.println(">>>>>>authorityDataList的大小为："+authorityDataList.size());

        //将tb_authority数据库表转为CSV格式的文件
        CSVUtils.createCSVFile(FileConstantUtils.TABLE_AUTHORITY_LIST, authorityDataList, "D:\\cgs\\File\\CSV", "tb_authority_" + stringDate);
        //释放空间，防止堆溢出
        authorityDataList.clear();

        //--------------------------------------------------------
        List<ApiApkMap> apiApkMaps = apiApkMapMapper.selectByExample(null);
        //将apiApkMap字段转储到List中
        List<List<String>> apiApkMapsDataList = new ArrayList<>();
        //遍历每一个apiApkMaps
        List<String> apiApkMapsTableFiledList = new ArrayList<>();
        for (ApiApkMap apiApkMap : apiApkMaps) {
            apiApkMapsTableFiledList.add(apiApkMap.getApiId().toString());
            apiApkMapsTableFiledList.add(apiApkMap.getApkId().toString());
            apiApkMapsDataList.add(apiApkMapsTableFiledList);
        }
        System.out.println(">>>>>>>apiApkMapsDataList的大小为："+apiApkMapsDataList.size());
        //将tb_api_apk_map数据库表转为CSV格式的文件
        CSVUtils.createCSVFile(FileConstantUtils.TABLE_API_APK_MAP_LIST, apiApkMapsDataList, "D:\\cgs\\File\\CSV", "tb_api_apk_map_" + stringDate);
        apiApkMapsDataList.clear();

        //--------------------------------------------------------

        //tb_authority_apk_map
        List<AuthorityApkMap> authorityApkMaps = authorityApkMapMapper.selectByExample(null);
        //将authorityApkMap字段转储到List中
        List<List<String>> authorityApkMapsDataList = new ArrayList<>();
        //遍历每一个authorityApkMap
        List<String> authorityApkMapTableFiledList = new ArrayList<>();
        for (AuthorityApkMap authorityApkMap : authorityApkMaps) {
            authorityApkMapTableFiledList.add(authorityApkMap.getAuthorityId().toString());
            authorityApkMapTableFiledList.add(authorityApkMap.getApkId().toString());
            authorityApkMapsDataList.add(authorityApkMapTableFiledList);
        }
        System.out.println(">>>>>>authorityApkMapsDataList的大小为:"+authorityApkMapsDataList.size());
        //将tb_authority_apk_map数据库表转为CSV格式的文件
        CSVUtils.createCSVFile(FileConstantUtils.TABLE_AUTHORITY_APK_MAP_LIST, authorityApkMapsDataList, "D:\\cgs\\File\\CSV", "tb_authority_apk_map_" + stringDate);
        authorityApkMapsDataList.clear();
    }


}
