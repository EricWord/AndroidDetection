package com.eric.test;

import org.junit.Test;

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
}
