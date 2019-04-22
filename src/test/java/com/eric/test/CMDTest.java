package com.eric.test;

import com.eric.tools.cmd.CMDHelper;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

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
    @Test
    public  void test2(){
        String s="abc";
        String newS = s.replace("b", "e");
        System.out.println("newS="+newS+",s="+s);

    }

    /**
     * 获取控制台输出内容
     */
    @Test
    public void test3(){




    }
}
