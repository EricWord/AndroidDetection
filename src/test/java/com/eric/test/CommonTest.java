package com.eric.test;

import com.eric.constrant.AuthorityConstrant;
import com.eric.tools.csv.CSVUtils;
import org.junit.Test;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
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
    public void testFindFile()  {
        String s = CommonTest.class.getResource("/python/ExtractAuthority2Txt.py").toExternalForm();
        if(null!=s){
            File file = new File(s);
//            System.out.println(file.getPath());
            String[] split = file.getPath().split("\\\\");
            for (int i = 1; i < split.length-1; i++) {
                System.out.print(split[i]+File.separator);

            }
            System.out.println(split[split.length-1]);

        }

    }
    @Test
    public void testGetPath(){
        System.out.println(System.getProperty("user.dir"));
//        System.out.println(Thread.currentThread().getContextClassLoader().getResource(""));
    }

    /**
     * 测试执行项目中的python文件
     */
    @Test
    public void testExecPythonInCurrentDir() throws IOException {
        //基础路径  E:\projects\AndroidDetection
        String basePath = System.getProperty("user.dir");

        //要执行的python文件的名称
        String pyName="LogicCallByJava.py";
        //最终文件的绝对路径
        String finalPyPath=basePath+File.separator+"src"+File.separator+"main"+File.separator+"python"+File.separator+pyName;
        String csvFilePath="G:\\7BiShe\\dataset\\method1\\androidDetection_part_2500good_2500bad.csv";
        String[] logicCallByJavaPyArgs = new String[]{"python ", finalPyPath, csvFilePath};
        Process proc = Runtime.getRuntime().exec(logicCallByJavaPyArgs);// 执行py文件
        BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream(), "GBK"));
        String temp="";
        while ((temp = in.readLine()) != null) {
            System.out.println(temp);

        }

    }

    @Test
    public void testGetFileName(){
        File dirFile = new File("G:\\7BiShe\\badAPKs\\1801-2100\\1801-2100HasDone");
        if(dirFile.isDirectory()){
            File[] files = dirFile.listFiles();
            for (File file : files) {
                String fileName = file.getName();
                String substring = fileName.substring(0, fileName.length() - 4);
                System.out.println(substring);

            }
        }

    }

    /**
     * 测试批量检测
     *
     */

    @Test
    public void testBatchPredict() throws IOException, InterruptedException {
        //所有的权限
        List<String> allAuthorityList = Arrays.asList(AuthorityConstrant.AUTHORITY_ARRAY);
        //1.生成表头
        //构造符合工具类要求的表头List
        List<String> head = new ArrayList<>();
        System.out.println("开始构造csv表头数据...");
        head.add("package_name");
        for (String au : allAuthorityList) {
            head.add(au);

        }
        head.add("apk_attribute");
        System.out.println("csv表头数据构造完毕...");
        //2.生成每一行数据,构造每一行的数据
        List<String> dataList = new ArrayList<>();
        System.out.println("开始构造每一行数据...");
        //添加包名，包名没用，默认unknown
        dataList.add("unknown");
        //当前应用的权限
        List<String> currentApkAuthorityList = new ArrayList<>();
        String extractAuthority2TxtPyPath ="E:\\projects\\AndroidDetection\\src\\main\\python\\ExtractAuthority2Txt.py";
        File dirFile = new File("G:\\7BiShe\\badAPKs\\1801-2100\\1801-2100HasDone");
        if(dirFile.isDirectory()){
            File[] files = dirFile.listFiles();
            System.out.println("开始遍历每一个apk文件...");
            for (File file : files) {
                String detectApkPath = file.getAbsolutePath();
                String fileNameWithSuffix = file.getName();
                String fileName = fileNameWithSuffix.substring(0, fileNameWithSuffix.length() - 4);
                String authorityResSavePath ="E:\\BiSheData\\temp\\testPredict\\"+fileName+".txt";
                String[] extractAuthority2TxtPyArgs = new String[]{"python ", extractAuthority2TxtPyPath, detectApkPath, authorityResSavePath};
                System.out.println("开始调用提取权限的python程序...");
                Process proc = Runtime.getRuntime().exec(extractAuthority2TxtPyArgs);// 执行py文件
                //执行完毕开始读取提取出的权限TXT
                File authorityResFile = new File(authorityResSavePath);
                int wait = proc.waitFor();
                if (wait == 0 && authorityResFile.exists()) {
                    System.out.println("提取权限的python程序执行成功！");
                    FileReader reader = new FileReader(authorityResSavePath);
                    BufferedReader br = new BufferedReader(reader);
                    for (String au : allAuthorityList) {
                        if (currentApkAuthorityList.contains(au)) {
                            dataList.add(1 + "");
                        } else {
                            dataList.add(0 + "");
                        }


                    }
                    //最后一个应用属性未知，默认值为2
                    dataList.add(2 + "");
                    System.out.println("每一行csv数据构造完毕！");

                }else{
                    System.out.println("调用python程序失败...");
                }

            }

            CSVUtils.createCSVFile(head, dataList, "E:\\BiSheData\\temp\\testPredict", "srcApkFeature");
            String logicPredictModelPklPath ="E:\\projects\\AndroidDetection\\predictModel\\predict_model.pkl";
            String logicPredictModelCSVPath ="E:\\BiSheData\\temp\\testPredict\\srcApkFeature.csv";
            String logicPredictModelPyPath ="E:\\projects\\AndroidDetection\\src\\main\\python\\LogicPredictModel.py";
            //先判断调用python程序必须的两个文件是否存在
            File logicPredictModelPyFile = new File(logicPredictModelPyPath);
            File logicPredictModelCSVFile = new File(logicPredictModelCSVPath);
            //两个文件都存在
            if (logicPredictModelPyFile.exists() && logicPredictModelCSVFile.exists()) {
                System.out.println("调用预测模型的所需要的两个文件都存在!");

                //调用python程序
                String[] apkDetectArgs = new String[]{"python ", logicPredictModelPyPath, logicPredictModelCSVPath, logicPredictModelPklPath};
                System.out.println("开始调用预测模型...");
                Process apkDetectProc = Runtime.getRuntime().exec(apkDetectArgs);// 执行py文件

                BufferedReader apkDetectIn = new BufferedReader(new InputStreamReader(apkDetectProc.getInputStream(), "GBK"));
                String apkDetecTemp = "";
                while ((apkDetecTemp = apkDetectIn.readLine()) != null) {

                    System.out.println("预测结果为:"+apkDetecTemp);

                }
            }
        }

    }

    /**
     * 测试直接根据csv文件进行测试
     */
    @Test
    public void testSinglePredict() throws IOException {
        String logicPredictModelPklPath ="E:\\projects\\AndroidDetection\\predictModel\\predict_model.pkl";
        String logicPredictModelCSVPath ="E:\\BiSheData\\temp\\androidDetection_part_1good_100bad.csv";
        String logicPredictModelPyPath ="E:\\projects\\AndroidDetection\\src\\main\\python\\LogicPredictModel.py";
        //先判断调用python程序必须的两个文件是否存在
        File logicPredictModelPyFile = new File(logicPredictModelPyPath);
        File logicPredictModelCSVFile = new File(logicPredictModelCSVPath);
        //两个文件都存在
        if (logicPredictModelPyFile.exists() && logicPredictModelCSVFile.exists()) {
            System.out.println("调用预测模型的所需要的两个文件都存在!");

            //调用python程序
            String[] apkDetectArgs = new String[]{"python ", logicPredictModelPyPath, logicPredictModelCSVPath, logicPredictModelPklPath};
            System.out.println("开始调用预测模型...");
            Process apkDetectProc = Runtime.getRuntime().exec(apkDetectArgs);// 执行py文件

            BufferedReader apkDetectIn = new BufferedReader(new InputStreamReader(apkDetectProc.getInputStream(), "GBK"));
            String apkDetecTemp = "";
            while ((apkDetecTemp = apkDetectIn.readLine()) != null) {

                System.out.println(apkDetecTemp);

            }
        }


    }
    @Test
    public void testContain(){
        String path="G:\\7BiShe\\badAPKs\\1201-1500\\1201-1500HasDone";
        String[] split = path.split("\\\\");
        List<String> list = Arrays.asList(split);
        if(list.contains("badAPKs")){
            System.out.println("恶意应用");

        }else{
            System.out.println("正常应用");
        }

    }


}
