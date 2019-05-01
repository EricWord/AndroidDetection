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


    public static File createCSVFile(List<String> head, List<String> dataList,
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
                    csvFile), "GB2312"), 1024);
            // 写入文件头部
            writeHead(head, csvWtriter);
            csvWtriter.flush();
            writeRow(dataList, csvWtriter, head.size());
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
     * 写入表头
     *
     * @param row
     * @param csvWriter
     * @throws IOException
     */
    private static void writeHead(List<String> row, BufferedWriter csvWriter) throws IOException {
        // 写入文件头部
        for (int i = 0; i < row.size(); i++) {
            if (i != row.size() - 1) {
                csvWriter.write(row.get(i) + ",");

            } else {
                csvWriter.write(row.get(i));
            }
        }
    }

    /**
     * 写一行数据方法
     *
     * @param row
     * @param csvWriter
     * @throws IOException
     */
    private static void writeRow(List<String> row, BufferedWriter csvWriter, int headLength) throws IOException {
        // 写入文件内容

        for (int i = 0; i < row.size(); i++) {
            //第一行数据要先写入一个换行
            if (i == 0) {
                csvWriter.newLine();
            }
            //换行逻辑判断
            if ((i + 1) % headLength == 0) {
                csvWriter.write(row.get(i));
                //写入换行
                csvWriter.newLine();
            } else {

                csvWriter.write(row.get(i) + ",");
            }

        }
        //写入换行
        csvWriter.newLine();
        csvWriter.flush();
    }
}