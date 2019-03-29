package com.eric.test;

import com.eric.bean.*;
import com.eric.dao.*;
import com.eric.tools.csv.CreateCSVUtils;
import com.eric.tools.csv.FileConstantUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: MapperTest
 * @Description: 测试Mapper映射文件
 * @Author: Eric
 * @Date: 2019/3/13 0013
 * @Email: xiao_cui_vip@163.com
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class MapperTest {
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

    @Test
    public void testApkMapper() {
//        Apk apk = new Apk("com.eric.testAPP4", 1);
//        int i=apkMapper.insertSelective(apk);
//        System.out.println(i);
        List<Apk> apks = apkMapper.selectByExample(null);
        apks.forEach(item -> {
            System.out.println(item.toString());
        });


    }

    @Test
    public void testCreateCSV() {
        List<Apk> dataList = new ArrayList<>();
        Integer apiId = -1;
        List<Apk> apks = apkMapper.selectByExample(null);
        for (Apk apk : apks) {
            //获取每一个apk的id
            Integer apkId = apk.getApkId();
            //根据apk id来查询与之关联的api信息
            ApiApkMapExample apiApkMapExample = new ApiApkMapExample();
            ApiApkMapExample.Criteria apiApkMapExampleCriteria = apiApkMapExample.createCriteria();
            apiApkMapExampleCriteria.andApkIdEqualTo(apkId);
            List<ApiApkMap> apiApkMaps = apiApkMapMapper.selectByExample(apiApkMapExample);
            List<Api> apiList = new ArrayList<>();
            if (apiApkMaps.size() > 0) {
                //遍历
                for (ApiApkMap apiApkMap : apiApkMaps) {
                    apiId = apiApkMap.getApiId();
                    //根据apiid查询api
                    Api api = apiMapper.selectByPrimaryKey(apiId);
                    apiList.add(api);
                }
                apk.setApiList(apiList);
            }
            //根据apkId来查询与之关联的权限信息
            AuthorityApkMapExample authorityApkMapExample = new AuthorityApkMapExample();
            AuthorityApkMapExample.Criteria authorityApkMapExampleCriteria = authorityApkMapExample.createCriteria();
            authorityApkMapExampleCriteria.andApkIdEqualTo(apkId);
            List<AuthorityApkMap> authorityApkMaps = authorityApkMapMapper.selectByExample(authorityApkMapExample);
            List<Authority> authorityList = new ArrayList<>();
            //遍历authorityApkMaps
            for (AuthorityApkMap authorityApkMap : authorityApkMaps) {
                //获取authorityId
                Integer authorityId = authorityApkMap.getAuthorityId();
                //根据authorityId来获取权限信息
                Authority authority = authorityMapper.selectByPrimaryKey(authorityId);
                authorityList.add(authority);

            }
            apk.setAuthorityList(authorityList);
            dataList.add(apk);

        }
        try {
            CreateCSVUtils.createCSVFile(dataList, FileConstantUtils.API_HEAD_LIST, FileConstantUtils.AU_HEAD_LIST);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
