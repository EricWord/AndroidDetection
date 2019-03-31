package com.eric.service;


import com.eric.bean.*;
import com.eric.dao.*;
import com.eric.tools.csv.CreateCSVUtils;
import com.eric.tools.csv.FileConstantUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
     * 创建CSV格式的文件
     */
    public void createCSVFile() {
        List<Apk> dataList = new ArrayList<>();
        Integer apiId = -1;
        //获取数据库中已有的apk记录
        List<Apk> apks = apkMapper.selectByExample(null);
        //遍历每一个apk记录
        for (Apk apk : apks) {
            //获取每一个apk的id
            Integer apkId = apk.getApkId();
            //根据apk id来查询与之关联的api信息
            ApiApkMapExample apiApkMapExample = getApiApkMapExample(apkId);
            List<ApiApkMap> apiApkMaps = apiApkMapMapper.selectByExample(apiApkMapExample);
            List<Api> apiList = new ArrayList<>();
            //获取到的api记录数不为0
            if (apiApkMaps.size() > 0) {
                //遍历
                apiApkMapOperate(apiApkMaps, apiList);
                apk.setApiList(apiList);
            }
            //根据apkId来查询与之关联的权限信息
            AuthorityApkMapExample authorityApkMapExample = getAuthorityApkMapExample(apkId);
            List<AuthorityApkMap> authorityApkMaps = authorityApkMapMapper.selectByExample(authorityApkMapExample);
            List<Authority> authorityList = new ArrayList<>();
            //遍历authorityApkMaps
            authorityApkMapsOperate(authorityApkMaps, authorityList);
            apk.setAuthorityList(authorityList);
            dataList.add(apk);

        }
        try {
            CreateCSVUtils.createCSVFile(dataList, FileConstantUtils.HEAD_LIST);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void apiApkMapOperate(List<ApiApkMap> apiApkMaps, List<Api> apiList) {
        Integer apiId;
        for (ApiApkMap apiApkMap : apiApkMaps) {
            apiId = apiApkMap.getApiId();
            //根据apiid查询api
            Api api = apiMapper.selectByPrimaryKey(apiId);
            apiList.add(api);
        }
    }

    /**
     * 遍历authorityApkMaps
     * @param authorityApkMaps
     * @param authorityList
     */
    public void authorityApkMapsOperate(List<AuthorityApkMap> authorityApkMaps, List<Authority> authorityList) {
        for (AuthorityApkMap authorityApkMap : authorityApkMaps) {
            //获取authorityId
            Integer authorityId = authorityApkMap.getAuthorityId();
            //根据authorityId来获取权限信息
            Authority authority = authorityMapper.selectByPrimaryKey(authorityId);
            authorityList.add(authority);

        }
    }

    public AuthorityApkMapExample getAuthorityApkMapExample(Integer apkId) {
        AuthorityApkMapExample authorityApkMapExample = new AuthorityApkMapExample();
        AuthorityApkMapExample.Criteria authorityApkMapExampleCriteria = authorityApkMapExample.createCriteria();
        authorityApkMapExampleCriteria.andApkIdEqualTo(apkId);
        return authorityApkMapExample;
    }

    public ApiApkMapExample getApiApkMapExample(Integer apkId) {
        ApiApkMapExample apiApkMapExample = new ApiApkMapExample();
        ApiApkMapExample.Criteria apiApkMapExampleCriteria = apiApkMapExample.createCriteria();
        apiApkMapExampleCriteria.andApkIdEqualTo(apkId);
        return apiApkMapExample;
    }


}
