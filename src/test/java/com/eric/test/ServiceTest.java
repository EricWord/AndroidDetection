package com.eric.test;/*
 *@description:测试服务层
 *@author:cuigs
 *@Email:cuigs@ti-net.com.cn
 *@Version:1.0
 *@Date:2019/3/29
 */

import com.eric.service.ApkBatchDecompileService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class ServiceTest {

    //批量反编译服务类
    @Autowired
    ApkBatchDecompileService apkBatchDecompileService;

    /**
     * 测试批量反编译
     */
    @Test
    public void testApkBatchDe(){
//        apkBatchDecompileService.batchDecompileApk("D:\\cgs\\File\\data\\badApkTest","D:\\cgs\\File\\data\\badApkTestResult");
        apkBatchDecompileService.batchDecompileApk("E:\\7BiShe\\goodAPKs\\1-300","E:\\7BiShe\\DeCompileResults\\goodApksDecompileResult\\1-300");

    }


}
