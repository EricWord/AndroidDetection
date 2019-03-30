package com.eric.tools.threadPool;/*
 *@description:多线程线程池工具类
 *@author:cuigs
 *@Email:cuigs@ti-net.com.cn
 *@Version:1.0
 *@Date:2019/3/29
 */

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
public class ThreadPoolUtil {

    public static ThreadPoolExecutor threadPool;

    /**
     * 无返回值直接执行
     * @param runnable
     */
    public  static void execute(Runnable runnable){
        getThreadPool().execute(runnable);
    }

    /**
     * 返回值直接执行
     * @param callable
     */
    public  static <T> Future<T> submit(Callable<T> callable){
        return   getThreadPool().submit(callable);
    }


    /**
     * dcs获取线程池
     * @return 线程池对象
     */
    public static ThreadPoolExecutor getThreadPool() {
        if (threadPool != null) {
            return threadPool;
        } else {
            synchronized (ThreadPoolUtil.class) {
                if (threadPool == null) {
                    threadPool = new ThreadPoolExecutor(2, 6, 60, TimeUnit.SECONDS,
                            new LinkedBlockingQueue<>(8), new ThreadPoolExecutor.CallerRunsPolicy());
                }
                return threadPool;
            }
        }
    }

}