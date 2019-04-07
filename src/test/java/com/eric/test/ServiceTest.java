package com.eric.test;/*
 *@description:测试服务层
 *@author:cuigs
 *@Email:cuigs@ti-net.com.cn
 *@Version:1.0
 *@Date:2019/3/29
 */

import com.eric.bean.Apk;
import com.eric.dao.ApkMapper;
import com.eric.service.*;
import com.eric.tools.AndroidManifestHelper.AndroidManifestAnalyze;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class ServiceTest {

    //批量反编译服务类
//    @Autowired
//    ApkBatchDecompileService apkBatchDecompileService;
    //批量提取api 特征服务类
    @Autowired
    APIService apiService;
    @Autowired
    ApkMapper apkMapper;

    @Autowired
    AuthorityService authorityService;

    @Autowired
    DeCompileService deCompileService;
    @Autowired
    CSVService csvService;


    /**
     * 测试批量反编译
     */
    @Test
    public void testApkBatchDe() {
//        apkBatchDecompileService.batchDecompileApk("D:\\cgs\\File\\data\\badApkTest","D:\\cgs\\File\\data\\badApkTestResult");
//        apkBatchDecompileService.batchDecompileApk("E:\\7BiShe\\goodAPKs\\301-600","E:\\7BiShe\\DeCompileResults\\goodApksDecompileResult\\301-600");

    }

    /**
     * 测试批量反编译恶意应用
     */
    @Test
    public void testApkBatchDeBadApks() {
//        apkBatchDecompileService.batchDecompileApk("D:\\cgs\\File\\data\\badApkTest","D:\\cgs\\File\\data\\badApkTestResult");
//        apkBatchDecompileService.batchDecompileApk("E:\\7BiShe\\badAPKs\\12","E:\\7BiShe\\DeCompileResults\\badApksDecompileResult\\901-1200");

    }

    /**
     * 测试api service层批量提取api特征到数据库，以解决之前api和apk关系表存在问题的bug
     * 这个没有修复，遇到了栈溢出的异常
     */
    @Test
    public void testAPIService() {
        //良性应用前300个
        /**
         * 多线程那块出了点问题
         * 2019.03.29 17:06修复了问题，进行测试
         */
        apiService.batchSaveApi("D:\\cgs\\File\\data\\goodApkTest", 0);

    }
/*    @Test
    public void testAuthorityService(){
        authorityService.saveAuthority("D:\\cgs\\File\\data\\goodAPKSDeCompileResult");


    }*/

    /**
     * 测试批量反编译
     */
    @Test
    public void testDeCompileService() {
        deCompileService.batchDeCompile("D:\\cgs\\File\\data\\testAPK", "D:\\cgs\\File\\data\\testAPKDecompileOutput");


    }

    /**
     * 测试权限批量提取
     */
    @Test
    public void testAuthorityService() {
//        authorityService.saveAuthority("D:\\cgs\\File\\data\\testAPKDecompileOutput", 0);

    }

    @Test
    public void testCSV() {
        List<Apk> apks = apkMapper.selectByExample(null);
        List<List<String>> dataList = new ArrayList<>();
        for (Apk apk : apks) {
            String[] tableFiled = apk.toString().split(":");
            List<String> tableFiledList = Arrays.asList(tableFiled);
            dataList.add(tableFiledList);

        }

//        CSVUtils.createCSVFile(FileConstantUtils.TABLE_APK_LIST,dataList,"D:\\cgs\\File\\CSV","tb_apk");


    }

    @Test
    public void testCSVService() {
        csvService.createCSVFile();

    }


    //--------------------------2019.04.03新增批量提取权限测试方法
    @Test
    public void testAuthorityService1() {
        authorityService.saveAuthority("D:\\cgs\\File\\data\\testAPKDecompileOutput", 0);

    }

    @Test
    public void testAuthorityService2() {
//        authorityService.saveAuthority("D:\\cgs\\File\\data\\testAPKDecompileOutput", 0);

    }

    @Test
    public void testAuthorityService3() {
//        authorityService.saveAuthority("D:\\cgs\\File\\data\\testAPKDecompileOutput", 0);

    }


    //--------------------------2019.04.03新增批量提取API测试方法,正常应用的API提取
    //如果是恶意应用记得将属性字段更换为1

    @Test
    public void testGoodApksAPIService1() {
        apiService.batchSaveApi("D:\\cgs\\File\\data\\test20190407", 0);

    }

    @Test
    public void testGoodApksAPIService2() {
        apiService.batchSaveApi("", 0);

    }

    @Test
    public void testGoodApksAPIService3() {
        apiService.batchSaveApi("", 0);

    }

    @Test
    public void testGetId() {
        List<String> androidManifestXmlList = authorityService.getAndroidManifestXmlList("D:\\cgs\\File\\data\\goodAPKSDeCompileResult");
        for (String xmlPath : androidManifestXmlList) {
            System.out.println(xmlPath);
           /* String[] tempPath = xmlPath.split("\\\\");
            String packageName = "";
            for (int i = 0; i < tempPath.length; i++) {
                if (SimpleStringUtil.countCharInString(tempPath[i], ".") > 2) {
                    packageName = tempPath[i-1];
                }

            }

            System.out.println(packageName);*/
        }

    }

    /**
     * 测试获取包名
     */
    @Test
    public void test99() {
        String name = AndroidManifestAnalyze.findPackage("D:\\cgs\\File\\data\\goodAPKSDeCompileResult\\com.borderxlab.bieyang\\AndroidManifest.xml");
        System.out.println(name);


    }

    @Test
    public void test100(){
        List<String> list=new LinkedList<>();
        String s = ((LinkedList<String>) list).removeFirst();
        System.out.println(s);

    }

    @Test
    public void test101(){
        List<String> list = authorityService.getAndroidManifestXmlList("D:\\cgs\\File\\data\\goodAPKSDeCompileResult");
        for (String s : list) {
            System.out.println(s);

        }

    }

    /**
     * 针对批量提取权限效率较高的情况，专门设计的测试方法
     */

    @Test
    public void bestBatchInsertAuthority(){
        File file = new File("");
        if(file.isDirectory()){
            File[] currentFiles = file.listFiles();
            for (File currentFile : currentFiles) {
                if(currentFile.isDirectory()){
                    authorityService.saveAuthority(currentFile.getAbsolutePath(),0);
                }
            }

        }

    }


    @Test
    public void  testCountSmaliFile(){
        ConcurrentHashMap<String, ArrayList<String>> smaliFile = apiService.countSmaliFile("E:\\7BiShe\\DeCompileResults\\goodApksDecompileResult\\1-300\\1-4");
        smaliFile.forEach((name,list)->{
            System.out.println(name+"包下共有"+list.size()+"个.smali文件");

        });


    }








}
