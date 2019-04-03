package com.eric.tools.string;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 *@description:字符串工具类
 *@author:cuigs
 *@Email:cuigs@ti-net.com.cn
 *@Version:1.0
 *@Date:2019/4/3
 */
public class SimpleStringUtil {
    public static int countCharInString(String srcStr, String findStr) {
        int count = 0;
        Pattern pattern = Pattern.compile(findStr);// 通过静态方法compile(String regex)方法来创建,将给定的正则表达式编译并赋予给Pattern类
        Matcher matcher = pattern.matcher(srcStr);//
        while (matcher.find()) {// boolean find() 对字符串进行匹配,匹配到的字符串可以在任何位置
            count++;
        }
        return count;
    }


}
