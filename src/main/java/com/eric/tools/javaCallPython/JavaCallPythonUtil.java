package com.eric.tools.javaCallPython;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @ClassName: JavaCallPythonUtil
 * @Description: java代码中调用python工具类
 * @Author: Eric
 * @Date: 2019/4/22 0022
 * @Email: xiao_cui_vip@163.com
 */
public class JavaCallPythonUtil {


    /**
     * 调用python提取权限的代码
     *
     * @param filePath python文件所在路径
     * @param apkSrc   apk文件所在的路径
     */
    public static String callExtractAuthorityPython(String filePath, String apkSrc) {
            String temp="";
        try {
            String[] pyArgs = new String[]{"python", filePath, apkSrc};
            Process proc = Runtime.getRuntime().exec(pyArgs);// 执行py文件

            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream(), "GBK"));
            while ((temp = in.readLine()) != null) {
                temp+=temp;

            }
            in.close();
            //下面的方法执行完成之后，若返回值为0表示执行成功，若返回值为1表示执行失败
            int wait = proc.waitFor();
            String execResult = (wait == 0 ? "成功" : "失败");
            System.out.println("Java调用python程序执行" + execResult);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return temp;

    }
}
