package com.eric.test;

import com.eric.tools.cmd.CMDHelper;
import org.junit.Test;

/*
 *@description:cmd测试
 *@author:cuigs
 *@Email:cuigs@ti-net.com.cn
 *@Version:1.0
 *@Date:2019/4/5
 */
public class CMDTest {

    @Test
    public  void test1(){
        CMDHelper.exeCmd("java -jar D:/cgs/software/jar/AXMLPrinter2.jar D:\\cgs\\File\\data\\goodAPKSDeCompileResult\\com.boetech.xiangread\\original\\AndroidManifest.xml","D:\\cgs\\File\\data\\goodAPKSDeCompileResult\\com.boetech.xiangread\\original\\AndroidManifestNew.xml");

    }
}
