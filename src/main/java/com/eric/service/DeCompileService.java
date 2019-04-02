package com.eric.service;

import com.eric.tools.decode.APKTool;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/*
 *@description:反编译服务层
 *@author:cuigs
 *@Email:cuigs@ti-net.com.cn
 *@Version:1.0
 *@Date:2019/3/30
 */
@Service
public class DeCompileService {
    /**
     * 批量反编译
     *
     * @param src 源路径
     * @param des 目标路径
     */
    public void batchDeCompile(String src, String des) {
        //设置线程池的大小为20
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "20");
        //获取其file对象
        File file = new File(src);
        //遍历src下的文件和目录，放在File数组中
        File[] fs = file.listFiles();
        //将数组转为List
        List<File> fileList = Arrays.asList(fs);
        //这个地方多线程反编译需要try一下，防止因为出现异常而终止所有进程

        fileList.parallelStream().forEach(f -> {
            //当前线程名称
            String currentThreadName = Thread.currentThread().getName();
            System.out.println("线程：" + currentThreadName + "开始执行.......");
            //不是目录的话就是apk文件了
            try {

                if (!f.isDirectory()) {
                    //每个APK文件反编译后对应自己的文件夹(名称为包名去掉后面的".apk")
                    String[] split = f.toString().split("\\\\");
                    String s = split[split.length - 1];
                    String finalPath = s.substring(0, s.length() - 4);
                    System.out.println(Thread.currentThread().getName() + ":当前正在反编译的apk文件为：" + finalPath);
                    APKTool.decode(f.toString(), des + "\\" + finalPath + "\\");
                }
            } catch (Exception e) {
                System.out.println(Thread.currentThread().getName() + ":多线程反编译APK文件时出现异常");
            }
        });
    }
}
