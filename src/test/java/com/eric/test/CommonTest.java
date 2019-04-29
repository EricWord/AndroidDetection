package com.eric.test;

import org.junit.Test;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
 *@description:普通测试类，不涉及数据库连接，mapper文件
 *@author:cuigs
 *@Email:cuigs@ti-net.com.cn
 *@Version:1.0
 *@Date:2019/4/6
 */
public class CommonTest {

    @Test
    public void test1() {
        String s = "D:\\cgs\\File\\data\\goodAPKSDeCompileResult\\com.book.search.goodsearchbook\\smali\\com\\qihoo\\util\\Configuration.smali";
        int len = s.length();
        String res = s.substring(len - 6, len);
        System.out.println(res);

    }

    /**
     * 遍历一个文件夹下的所有文件，列出文件名
     */
    @Test
    public void test2() {
        File file = new File("D:\\cgs\\File\\data\\0test0412\\BadApkResult");
        String[] list = file.list();
        for (String s : list) {
            System.out.println(s);
        }

    }

    /**
     * 打开文件夹测试
     */
    @Test
    public void testOpenDir() {
        try {
            Desktop.getDesktop().open(new File("G:\\7BiShe\\DeCompileResults\\badApksDecompileResult"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testContains() {
        List<String> list = new ArrayList<String>();
        list.add("草莓");         //向列表中添加数据
        list.add("香蕉");        //向列表中添加数据
        list.add("菠萝");        //向列表中添加数据
        for (int i = 0; i < list.size(); i++) {    //通过循环输出列表中的内容
            System.out.println(i + ":" + list.get(i));
        }
        String o = "香蕉";
        System.out.println("list对象中是否包含元素" + o + ":" + list.contains(o));

    }

    /**
     * 测试项目中的文件路径该怎么填才能正确定位文件
     */

    @Test
    public void testFilePath(){
        File file = new File("/src/main/java/com/eric/python/ExtractAuthority2Txt.py");
        if(file.exists()){
            System.out.println("文件存在");
        }else{
            System.out.println("文件不存在！");
        }


    }

    /**
     * 测试查找类路径下的文件
     */
    @Test
    public void testFindFile(){
        String s = CommonTest.class.getResource("/python/ExtractAuthority2Txt.py").toExternalForm();
        if(null!=s){
            System.out.println(s);
        }

    }


}
