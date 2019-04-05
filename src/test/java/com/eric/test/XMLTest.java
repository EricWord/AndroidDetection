package com.eric.test;

import com.eric.tools.AndroidManifestHelper.AndroidManifestAnalyze;
import org.junit.Test;

import java.util.List;

/*
 *@description:测试AndroidManifest.xml文件的读取
 *@author:cuigs
 *@Email:cuigs@ti-net.com.cn
 *@Version:1.0
 *@Date:2019/4/5
 */
public class XMLTest {

    @Test
    public  void test1(){
        List<String> list = AndroidManifestAnalyze.xmlHandle("");
        for (String s : list) {
            System.out.println(s);

        }

    }
}
