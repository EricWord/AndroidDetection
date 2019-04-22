package com.eric.test;

import org.python.core.Py;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @ClassName: JavaCallPythonTest
 * @Description: java代码中调用python
 * @Author: Eric
 * @Date: 2019/4/20 0020
 * @Email: xiao_cui_vip@163.com
 */
public class JavaCallPythonTest {

    public static void main(String[] args) {
        int a = 18;
        int b = 23;
        try {
            String[] pyArgs = new String[] { "python", "E:\\projects\\AndroidDetectionPythonVersion\\test\\JavaCallPythonTest.py", String.valueOf(a), String.valueOf(b) };
            Process proc = Runtime.getRuntime().exec(pyArgs);// 执行py文件

            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();
            proc.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



    }
}
