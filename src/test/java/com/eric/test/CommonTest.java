package com.eric.test;

import org.junit.Test;

import java.io.File;

/*
 *@description:普通测试类，不涉及数据库连接，mapper文件
 *@author:cuigs
 *@Email:cuigs@ti-net.com.cn
 *@Version:1.0
 *@Date:2019/4/6
 */
public class CommonTest {

    @Test
    public void test1(){
        String s="D:\\cgs\\File\\data\\goodAPKSDeCompileResult\\com.book.search.goodsearchbook\\smali\\com\\qihoo\\util\\Configuration.smali";
        int len=s.length();
        String res=s.substring(len-6,len);
        System.out.println(res);

    }

    /**
     * 遍历一个文件夹下的所有文件，列出文件名
     */
    @Test
    public void test2(){
        File file = new File("D:\\cgs\\File\\data\\0test0412\\BadApkResult");
        String[] list = file.list();
        for (String s : list) {
            System.out.println(s);
        }

    }





}
