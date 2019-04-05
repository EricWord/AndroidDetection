package com.eric.tools.decode;


import brut.androlib.AndrolibException;
import brut.androlib.ApkDecoder;
import brut.directory.DirectoryException;

import java.io.File;
import java.io.IOException;

/**
 * 调用APKTool对APK文件进行反编译的工具类
 */
public class APKTool {
    /**
     * @param src  文件来源
     * @param dest 文件存放地址
     */
    public static void decode(String src, String dest) {
        //判断目标文件是否已经存在
        File destFile = new File(dest);
        //目标文件存在&&是目录 &&内容不为空
        if (destFile.exists() && destFile.isDirectory() && destFile.listFiles().length > 0) {
            System.out.println(Thread.currentThread().getName() + ":该apk文件已经被反编译过，已跳过该apk，继续反编译下一个apk文件.....");
            //结束
            return;

        }
        ApkDecoder decoder = new ApkDecoder();
        decoder.setApkFile(new File(src));
//        decoder.
        try {
            decoder.setOutDir(new File(dest));
            decoder.decode();
        } catch (AndrolibException e) {
            e.printStackTrace();
        } catch (DirectoryException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
