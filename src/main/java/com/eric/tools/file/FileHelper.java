package com.eric.tools.file;


import com.eric.tools.time.TimeConvert;

import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @ClassName: FileHelper
 * @Description: 文件操作工具
 * @Author: Eric
 * @Date: 2019/3/5 0005
 * @Email: xiao_cui_vip@163.com
 */
//该类暂时已经用不到了，如果后续确认不在需要该类时可以将其删除
/*
public class FileHelper {
    //线程数
    private img final Integer TASK_SIZE = 10;

    */
/**
     * 多线程反编译APK文件
     *
     * @param src  文件来源路径
     * @param dest 反编译结果输出路径
     *//*

    public img void batchDeCompile(String src, String dest) {
        Instant start = Instant.now();
        System.out.println("----程序开始运行----");
        // 创建一个线程池
        ExecutorService pool = Executors.newFixedThreadPool(TASK_SIZE);
        // 创建多个有返回值的任务
        List<Future> list = new ArrayList<Future>();
        for (int i = 0; i < TASK_SIZE; i++) {
            FileCallable task = new FileCallable(TASK_SIZE + "", src, dest);
            Future<Integer> f = pool.submit(task);
            list.add(f);
        }
        //关闭线程池
        pool.shutdown();
        // 获取所有并发任务的运行结果
        for (Future f : list) {
            // 从Future对象上获取任务的返回值，并输出到控制台
            try {
                System.out.println(">>>" + f.get().toString());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        Instant end = Instant.now();
        System.out.println("----程序运行结束----");
        TimeConvert.convert(Duration.between(start, end).toMillis());


    }

    */
/**
     * 调用基于实现unnable接口实现的多线程进行反编译
     *
     * @param src  源路径
     * @param dest 反编译文件输出路径
     *//*

    public  img void batchDecompileOnRunnable(String src, String dest) {
        FileRunnable task = new FileRunnable(src, dest);
        //创建线程池
        ExecutorService pool = Executors.newFixedThreadPool(5);
        //获取其file对象
        File file = new File(src);
        //遍历src下的文件和目录，放在File数组中
        File[] fs = file.listFiles();
        //"最后一公里"的路径
        String finalPath = "";
        for (File f : fs) {
            //若非目录(即文件)，反编译
            if (!f.isDirectory()) {
                //每个APK文件反编译后对应自己的文件夹(名称为包名去掉后面的".apk")
                String[] split = f.toString().split("\\\\");
                String s = split[split.length - 1];
                finalPath = s.substring(0, s.length() - 4);
                task.setSrc(f.toString());
                task.setDes(dest + "\\" + finalPath + "\\");
                pool.submit(task);
            }
        }
        //关闭线程池
        pool.shutdown();
    }
}
*/

