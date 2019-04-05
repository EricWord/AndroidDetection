package com.eric.tools.cmd;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/*
 *@description:执行控制台命令的工具类
 *@author:cuigs
 *@Email:cuigs@ti-net.com.cn
 *@Version:1.0
 *@Date:2019/4/5
 */
public class CMDHelper {

    public static void exeCmd(String commandStr,String des) {
        BufferedReader br = null;
        try {
            Process p = Runtime.getRuntime().exec(commandStr);
            br = new BufferedReader(new InputStreamReader(p.getInputStream(), Charset.forName("UTF-8")));
            String line = null;
            StringBuilder sb = new StringBuilder();
            FileWriter fw = new FileWriter(des);
            BufferedWriter bw = new BufferedWriter(fw);
            while ((line = br.readLine()) != null) {
                bw.write(line + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
