package com.eric.test;

import com.eric.bean.Apk;
import com.eric.dao.ApkMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @ClassName: MapperTest
 * @Description: 测试Mapper映射文件
 * @Author: Eric
 * @Date: 2019/3/13 0013
 * @Email: xiao_cui_vip@163.com
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class MapperTest {
    @Autowired
    ApkMapper apkMapper;

    @Test
    public  void testApkMapper(){
//        Apk apk = new Apk("com.eric.testAPP4", 1);
//        int i=apkMapper.insertSelective(apk);
//        System.out.println(i);
        List<Apk> apks = apkMapper.selectByExample(null);
        apks.forEach(item->{
            System.out.println(item.toString());
        });


    }
}
