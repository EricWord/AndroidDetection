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
        ApkDecoder decoder = new ApkDecoder();
        decoder.setApkFile(new File(src));
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
