package com.eric.tools.csv;

import com.eric.bean.Api;
import com.eric.bean.Apk;
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

    public static File createCSVFile(List<Apk> dataLists, List<String> headList) throws IOException {

        File csvFile = null;
        BufferedWriter csvWrite = null;
        DateTime dateTime = new DateTime();
        String stringDate = dateTime.toString("yyyy年MM月dd日HH时mm分ss秒", Locale.CHINESE);
        try {

            //定义文件类型
            csvFile = new File(FileConstantUtils.DOWNLOAD_FILE_PATH + File.separator + FileConstantUtils.FILE_NAME + stringDate + ".csv");
            //去文件目录
            File parent = csvFile.getParentFile();
            if (parent.exists()) {
                parent.mkdirs();

            }
            //创建文件
            csvFile.createNewFile();
            csvWrite = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile), "GB2312"), 1024);

            //写入表头
            writeHead(headList, csvWrite);
            //写入数据
            if (dataLists.size() > 0) {
                for (Apk apk : dataLists) {
                    if(apk!=null)
                    writeData(apk, csvWrite);
                }
            }
            csvWrite.flush();
        } catch (IOException e) {
            throw new IOException("文件生成失败");
        } finally {
            try {
                csvWrite.close();

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
    private static void writeData(Apk apk, BufferedWriter csvWreite) throws IOException {
        //获取apk id
        Integer apkId = apk.getApkId();
        //获取apk名称
        String apkName = apk.getPackageName();
        //获取apk属性
        Integer apkAttribute = apk.getApkAttribute();
        String attr = (apkAttribute == 0 ? "正常应用" : "恶意应用");
        //获取权限列表
        List<Api> apiList = apk.getApiList();
        for (Api api : apiList) {
            if(api!=null){
                StringBuffer buffer = new StringBuffer();
                String rowStr1 = buffer.append(api.getApiId()+",").toString();
                csvWreite.write(rowStr1);
                String rowStr2 = buffer.append(api.getApiContent()+",").toString();
                csvWreite.write(rowStr2);
                String rowStr3 = buffer.append(attr+",").toString();
                csvWreite.write(rowStr3);
                String rowStr4 = buffer.append(apkId+",").toString();
                csvWreite.write(rowStr4);
                String rowStr5 = buffer.append(apkName+",").toString();
                csvWreite.write(rowStr5);
                csvWreite.newLine();

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
