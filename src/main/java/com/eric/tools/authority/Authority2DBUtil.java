package com.eric.tools.authority;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/*
 *@description:将TXT文本中的权限存入数据库
 *@author:cuigs
 *@Email:cuigs@ti-net.com.cn
 *@Version:1.0
 *@Date:2019/4/14
 */
public class Authority2DBUtil {
    /**
     * 将TXT中的权限存入数据库中
     *
     * @param src TXT文件路径
     */

    public static void saveAuthority2DB(String src) {
        try (FileReader reader = new FileReader(src);
             BufferedReader br = new BufferedReader(reader)
        ) {
            String line;
            //网友推荐更加简洁的写法
            while ((line = br.readLine()) != null) {
                // 一次读入一行数据
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
