package com.eric.test;

import com.eric.bean.*;
import com.eric.dao.ApiApkMapMapper;
import com.eric.dao.ApiMapper;
import com.eric.dao.ApkMapper;
import com.eric.service.APIService;
import com.eric.tools.api.APIHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @ClassName: TestTools
 * @Description: 测试反编译、API提取、权限提取
 * @Author: Eric
 * @Date: 2019/3/13 0013
 * @Email: xiao_cui_vip@163.com
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class TestTools {
    @Autowired
    ApkMapper apkMapper;
    @Autowired
    ApiMapper apiMapper;
    @Autowired
    ApiApkMapMapper apiApkMapMapper;
    @Autowired
    APIService apiService;


    @Test
    public void test1() {
        List<String> list = APIHelper.handle("E:\\BiSheProjects\\APKs\\des\\com.anybeen.mark.app\\smali\\me\\zhanghai\\android\\materialprogressbar\\SingleHorizontalProgressDrawable.smali");
        Apk apk = new Apk("dsdd", 0);
        //下面这个方法只需要调用一次
        int apkId = apkMapper.insertSelective(apk);
        list.forEach(item -> {
            Api api = new Api(item, "xxxsdshdv");
            apiMapper.insertSelective(api);
            Integer apiId = api.getApiId();
            ApiApkMap apiApkMap = new ApiApkMap(apkId, apiId);
            apiApkMapMapper.insert(apiApkMap);
        });


    }

    @Test
    public void test2() {
        Boolean res = null;
        if (apiService != null) {
            res = apiService.saveApi("E:\\BiSheProjects\\APKs\\des\\com.anybeen.mark.app");
        }
        System.out.println(res);
    }


    @Test
    public void test3() {
        ApkExample apkExample = new ApkExample();
        ApkExample.Criteria criteria = apkExample.createCriteria();
        criteria.andPackageNameEqualTo("com.anybeen.mark.app");
        List<Apk> apks = apkMapper.selectByExample(apkExample);
        System.out.println(apks.size());
    }

    @Test
    public void test4() {
        //获取应用的包名
        String[] split = "E:\\BiSheProjects\\APKs\\des\\com.anybeen.mark.app2019002".split("\\\\");
        //获取包名
        String packageName = split[split.length - 1];
        Apk apk = new Apk(packageName, 0);
        apkMapper.insertSelective(apk);
        int apkId = apk.getApkId();
        System.out.println(apkId);

    }

    /**
     * 测试根据api的md5值来查询api(间接地通过Example实现)
     */
    @Test
    public void test5(){
        ApiExample example = new ApiExample();
        ApiExample.Criteria criteria = example.createCriteria();
        criteria.andApiMad5EqualTo("7c12ae96db72a5c0f78db57b2cbf79a5");
        List<Api> apis = apiMapper.selectByExample(example);
        System.out.println(apis.size());
    }

}
