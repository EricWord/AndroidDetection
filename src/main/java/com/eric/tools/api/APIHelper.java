package com.eric.tools.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 提取API特征的工具类
 */
public class APIHelper {
    /*
     *
     * 主要提取思路：
     * 1.遍历每个.smali文件
     * 2.根据API在.smali文件中的格式声明：
     *  .method<访问权限>[修饰关键字]<API原型>
     *
     */

    /**
     * 读取.smali文件并提取API
     *
     * @param src  文件路径
     */
    public static List<String> handle(String src) {
        List<String> list = new ArrayList<>();
        BufferedReader br = null;
        try {
            //源文件
            File srcFile = new File(src);
            FileReader fr = new FileReader(srcFile);
            br = new BufferedReader(fr);
            String str;
            while ((str = br.readLine()) != null) {
                //判断当前行是否以.method开始
                boolean flag = str.startsWith(".method");
                if (flag) {
                    //是以.method开始
                    //提取API
                    list.add(str);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }
}
