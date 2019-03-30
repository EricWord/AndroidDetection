package com.eric.tools.file;


import com.eric.tools.decode.APKTool;
import com.eric.tools.time.TimeConvert;

import java.time.Duration;
import java.time.Instant;

/**
 * @ClassName: FileRunnable
 * @Description: 实现了Runnable接口的多线程文件操作类
 * @Author: Eric
 * @Date: 2019/3/7 0007
 * @Email: xiao_cui_vip@163.com
 */

//该类暂时已经用不到了，如果后续确认不在需要该类时可以将其删除
/*
public class FileRunnable implements Runnable {
    //APK文件源路径(文件夹)
    private String src;
    //反编译文件输出路径(文件夹)
    private String des;

    public FileRunnable() {
    }

    public FileRunnable(String src, String des) {
        this.src = src;
        this.des = des;
    }

    @Override
    public void run() {
        Instant start = Instant.now();
        System.out.println(">>>线程：" + Thread.currentThread().getName() + "开始执行<<<");
        APKTool.decode(src, des);
        Instant end = Instant.now();
        System.out.print(">>>线程：" + Thread.currentThread().getName() + "执行完毕,耗时：");
        TimeConvert.convert(Duration.between(start, end).toMillis());
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
*/
