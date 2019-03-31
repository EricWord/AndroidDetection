package com.eric.tools.csv;

import java.io.*;
import java.util.List;

/*
 *@description:数据库表转CSV工具类
 *@author:cuigs
 *@Email:cuigs@ti-net.com.cn
 *@Version:1.0
 *@Date:2019/3/31
 */
public class CSVUtils {
    /**
     * CSV文件生成方法
     *
     * @param head
     * @param dataList
     * @param outPutPath
     * @param filename
     * @return
     */
    public static File createCSVFile(List<String> head, List<List<String>> dataList,
                                     String outPutPath, String filename) {
        File csvFile = null;
        BufferedWriter csvWtriter = null;
        try {
            csvFile = new File(outPutPath + File.separator + filename + ".csv");
            File parent = csvFile.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            csvFile.createNewFile();
            // GB2312使正确读取分隔符","
            csvWtriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
                    csvFile), "UTF-8"), 1024);
            // 写入文件头部
            writeRow(head, csvWtriter);
            // 写入文件内容
            for (List<String> row : dataList) {
                writeRow(row, csvWtriter);
            }
            csvWtriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                csvWtriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return csvFile;
    }

    /**
     * 写一行数据方法
     *
     * @param row
     * @param csvWriter
     * @throws IOException
     */
    private static void writeRow(List<String> row, BufferedWriter csvWriter) throws IOException {
        // 写入文件头部
        for (String data : row) {
            StringBuffer sb = new StringBuffer();
            String rowStr = sb.append(",").append(data).append(",").toString();
            csvWriter.write(rowStr);
            csvWriter.flush();
        }
        csvWriter.newLine();

    }
}