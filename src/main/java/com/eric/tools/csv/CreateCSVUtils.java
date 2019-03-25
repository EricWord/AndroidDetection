package com.eric.tools.csv;

import com.eric.bean.Api;
import com.eric.bean.Apk;
import com.eric.bean.Authority;
import org.joda.time.DateTime;

import java.io.*;
import java.util.List;
import java.util.Locale;

/**
 * @ClassName: CreateCSVUtils
 * @Description: 创建csv文件
 * @Author: Eric
 * @Date: 2019/3/24 0024
 * @Email: xiao_cui_vip@163.com
 */
public class CreateCSVUtils {
    /**
     *      * 创建CSV文件类型
     *      * @param dataLists
     *      * @return
     *      
     */

    public static File createCSVFile(List<Apk> dataLists, List<String> apiHeadList,List<String> auHeadList) throws IOException {

        File csvFile = null;
        File csvFile2 = null;
        BufferedWriter csvWrite = null;
        BufferedWriter csvWrite2 = null;
        DateTime dateTime = new DateTime();
        String stringDate = dateTime.toString("yyyy_MM_dd_HH_mm_ss", Locale.CHINESE);
        try {

            //定义文件类型
            csvFile = new File(FileConstantUtils.DOWNLOAD_FILE_PATH + File.separator + FileConstantUtils.FILE_NAME +"_api_"+ stringDate + ".csv");
            csvFile2 = new File(FileConstantUtils.DOWNLOAD_FILE_PATH + File.separator + FileConstantUtils.FILE_NAME +"_authority_"+ stringDate + ".csv");
            //去文件目录
            File parent = csvFile.getParentFile();
            File parent2 = csvFile2.getParentFile();
            if (parent.exists()) {
                parent.mkdirs();

            }
            if (parent2.exists()) {
                parent2.mkdirs();

            }
            //创建文件
            csvFile.createNewFile();
            csvFile2.createNewFile();
            csvWrite = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile), "GB2312"), 1024);
            csvWrite2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile), "GB2312"), 1024);

            //写入表头
            writeHead(apiHeadList, csvWrite);
            writeHead(auHeadList, csvWrite2);
            //写入数据
            if (dataLists.size() > 0) {
                for (Apk apk : dataLists) {
                    if(apk!=null){

                        writeDataApi(apk, csvWrite);
                        writeDataAu(apk, csvWrite2);
                    }else{
                        System.out.println("现在apk为null");
                    }
                }
            }
            csvWrite.flush();
            csvWrite2.flush();
        } catch (IOException e) {
            throw new IOException("文件生成失败");
        } finally {
            try {
                csvWrite.close();
                csvWrite2.close();

            } catch (IOException e) {

                throw new IOException("关闭文件流失败");
            }
        }

        return csvFile;
    }


    /**
     *      * 将数据按行写入数据
     *      *
     *      * @param dataList
     *      * @param csvWreite
     *      * @throws IOException
     *      
     */
    private static void writeDataApi(Apk apk, BufferedWriter csvWreite) throws IOException {
        //获取apk id
        Integer apkId = apk.getApkId();
        //获取apk名称
        String apkName = apk.getPackageName();
        //获取apk属性
        Integer apkAttribute = apk.getApkAttribute();
        String attr = (apkAttribute == 0 ? "正常应用" : "恶意应用");
        //获取api列表
        List<Api> apiList = apk.getApiList();
        for (Api api : apiList) {
            if(api!=null){
                StringBuffer buffer1 = new StringBuffer();
                StringBuffer buffer2 = new StringBuffer();
                StringBuffer buffer3 = new StringBuffer();
                StringBuffer buffer4 = new StringBuffer();
                StringBuffer buffer5 = new StringBuffer();
                String rowStr1 = buffer1.append(api.getApiId()+",").toString();
                csvWreite.write(rowStr1);
                String rowStr2 = buffer2.append(api.getApiContent()+",").toString();
                csvWreite.write(rowStr2);
                String rowStr3 = buffer3.append(attr+",").toString();
                csvWreite.write(rowStr3);
                String rowStr4 = buffer4.append(apkId+",").toString();
                csvWreite.write(rowStr4);
                String rowStr5 = buffer5.append(apkName+",").toString();
                csvWreite.write(rowStr5);
                csvWreite.newLine();

            }else{
                System.out.println("现在api为null");
            }


        }



    }

    /**
     * 权限信息生成CSV文件
     * @param apk apk文件
     * @param csvWreite
     * @throws IOException
     */
    private static void writeDataAu(Apk apk, BufferedWriter csvWreite) throws IOException {
        //获取apk id
        Integer apkId = apk.getApkId();
        //获取apk名称
        String apkName = apk.getPackageName();
        //获取apk属性
        Integer apkAttribute = apk.getApkAttribute();
        String attr = (apkAttribute == 0 ? "正常应用" : "恶意应用");

        //获取权限列表
        List<Authority> authorityList = apk.getAuthorityList();
        for (Authority au : authorityList) {
            if(au!=null){
                StringBuffer buffer1 = new StringBuffer();
                StringBuffer buffer2 = new StringBuffer();
                StringBuffer buffer3 = new StringBuffer();
                StringBuffer buffer4 = new StringBuffer();
                StringBuffer buffer5 = new StringBuffer();
                String rowStr1 = buffer1.append(au.getAuthorityId()+",").toString();
                csvWreite.write(rowStr1);
                String rowStr2 = buffer2.append(au.getAuthorityContent()+",").toString();
                csvWreite.write(rowStr2);
                String rowStr3 = buffer3.append(attr+",").toString();
                csvWreite.write(rowStr3);
                String rowStr4 = buffer4.append(apkId+",").toString();
                csvWreite.write(rowStr4);
                String rowStr5 = buffer5.append(apkName+",").toString();
                csvWreite.write(rowStr5);
                csvWreite.newLine();

            }else{
                System.out.println("现在au为null");
            }


        }



    }

    private static void writeHead(List<String> dataList, BufferedWriter csvWreite) throws IOException {

        for (String data : dataList) {
            StringBuffer buffer = new StringBuffer();
            String rowStr = buffer.append(data+",").toString();
            csvWreite.write(rowStr);


        }
        csvWreite.newLine();
    }

}
