package com.eric.test;

import com.eric.bean.*;
import com.eric.dao.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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


    }

    @Test
    public void testApkInsert() {
        int num = apkMapper.insertSelective(new Apk("com.anybeen.mark.app", 0));
        System.out.println(num);

    }

    /**
     * 测试如果数据库不存在要查询的记录，返回结果是什么
     */
    @Test
    public void testSelectByExample() {
        ApkExample apkExample = new ApkExample();
        ApkExample.Criteria apkCriteria = apkExample.createCriteria();
        apkCriteria.andPackageNameEqualTo("packageName20190406");
        apkCriteria.andApkAttributeEqualTo(3);
        List<Apk> apks = apkMapper.selectByExample(apkExample);
        System.out.println(apks.size());

    }


    /**
     * 查询数据库中重复的api_apk映射关系
     */
    @Test
    public void testFindRepeatedApi() {
        List<ApiApkMap> apiApkMaps = apiApkMapMapper.selectByExample(null);
        for (ApiApkMap apiApkMap : apiApkMaps) {
            ApiApkMapExample apiApkMapExample = new ApiApkMapExample();
            ApiApkMapExample.Criteria criteria = apiApkMapExample.createCriteria();
            criteria.andApiIdEqualTo(apiApkMap.getApiId());
            criteria.andApkIdEqualTo(apiApkMap.getApkId());
            List<ApiApkMap> apiApkMapList = apiApkMapMapper.selectByExample(apiApkMapExample);
            if (apiApkMapList.size() >= 2) {
                for (ApiApkMap apkMap : apiApkMapList) {
                    System.out.println(apkMap.getApkId() + "--" + apkMap.getApiId());
                }
            }else{
                for (ApiApkMap apkMap : apiApkMapList) {
                    System.out.println("apkId="+apkMap.getApkId() + ":apiId=" + apkMap.getApiId());
                }

            }
        }


    }

    /**
     * 测试获取第一个元素
     */
    @Test
    public void testApiApkMap(){
        ApiApkMapExample apiApkMapExample = new ApiApkMapExample();
        ApiApkMapExample.Criteria criteria = apiApkMapExample.createCriteria();
        criteria.andApiIdEqualTo(301);
        criteria.andApkIdEqualTo(1);
        List<ApiApkMap> apiApkMaps = apiApkMapMapper.selectByExample(apiApkMapExample);
        if(apiApkMaps.size()>=2){
            System.out.println("数据库中存在多条相同记录");
        }
        if(apiApkMaps.size()==1){
            System.out.println("数据库中已经由一条记录，未执行任何操作...");

        }
        if(apiApkMaps.size()==0){
            ApiApkMap apiApkMap = new ApiApkMap(1,301);
            int i = apiApkMapMapper.insertSelective(apiApkMap);
            System.out.println("成功插入"+i+"条记录");

        }



    }


    @Test
    public void testInsert(){
        Apk apk = new Apk("test20190407",0);
        int i = apkMapper.insertSelective(apk);
        System.out.println(i);

    }
}
