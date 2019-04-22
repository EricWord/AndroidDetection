package com.eric.test;

import com.eric.tools.javaCallPython.JavaCallPythonUtil;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: JavaCallPythonTest
 * @Description: java代码中调用python
 * @Author: Eric
 * @Date: 2019/4/20 0020
 * @Email: xiao_cui_vip@163.com
 */
public class JavaCallPythonTest {

/*    public static void main(String[] args) {
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



    }*/

    /**
     * 测试JAVA调用pytho程序logic
     */
    @Test
    public void testJavaCallPythonLogic() {

        String csvPath = "E:\\BiSheData\\CSV\\androidDetection_all.csv";
        try {
            String[] pyArgs = new String[]{"python", "E:\\projects\\AndroidDetectionPythonVersion\\logicregressionAlgorithm\\LogicCallByJava.py",csvPath};
            Process proc = Runtime.getRuntime().exec(pyArgs);// 执行py文件

            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream(),"GBK"));
            String line = null;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();
            //下面的方法执行完成之后，若返回值为0表示执行成功，若返回值为1表示执行失败
            int wait = proc.waitFor();
            String execResult=(wait==0?"成功":"失败");
            System.out.println("Java调用python程序执行"+execResult);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void test2(){
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
            int wait = proc.waitFor();
            String execResult=(wait==0?"成功":"失败");
            System.out.println("Java调用python程序执行"+execResult);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    @Test
    public void testExtractAuthority(){
        String res = JavaCallPythonUtil.callExtractAuthorityPython("E:\\projects\\AndroidDetectionPythonVersion\\featureProject\\ExtractAuthority.py", "E:/BiSheData/goodApks/com.hudongzuoye.apk");
        if(null!=res){

        System.out.println(res);
        }else {
            System.out.println("返回结果为空");
        }


    }

    @Test
    public void testTrainModel(){

        String temp="";
        try {
            String[] pyArgs = new String[]{"python", "E:\\projects\\AndroidDetectionPythonVersion\\logicregressionAlgorithm\\LogicCallByJava.py", "E:\\BiSheData\\CSV\\androidDetection_all.csv"};
            Process proc = Runtime.getRuntime().exec(pyArgs);// 执行py文件

            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream(), "GBK"));
            while ((temp = in.readLine()) != null) {

                System.out.println(temp);
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



    }





}
