package com.eric.tools.file;


import com.eric.tools.decode.APKTool;
import com.eric.tools.time.TimeConvert;

import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.Callable;

/**
 * @ClassName: FileCallable
 * @Description:
 * @Author: Eric
 * @Date: 2019/3/5 0005
 * @Email: xiao_cui_vip@163.com
 */

//该类暂时已经用不到了，如果后续确认不在需要该类时可以将其删除
/*
public class FileCallable implements Callable<Integer> {
    //任务数量(编号)
    private String taskNum;
    //APK文件源路径(文件夹)
    private String src;
    //反编译文件输出路径(文件夹)
    private String des;

    public FileCallable() {
    }

    public FileCallable(String taskNum) {
        this.taskNum = taskNum;
    }

    public FileCallable(String taskNum, String src, String des) {
        this.taskNum = taskNum;
        this.src = src;
        this.des = des;
    }

    public String getTaskNum() {
        return taskNum;
    }

    public void setTaskNum(String taskNum) {
        this.taskNum = taskNum;
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

    @Override
    public Integer call() throws Exception {
        System.out.println(">>>>>第" + taskNum + "个线程启动<<<<<<");
        //开始时间
        Instant start = Instant.now();
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

                APKTool.decode(f.toString(), des + "\\"+finalPath + "\\");
            }
        }
        Instant end = Instant.now();
        System.out.print(">>>>>第" + taskNum + "个线程完成任务,耗时：");
        TimeConvert.convert(Duration.between(start,end).toMillis());



        //最后返回反编译成功的文件个数
        return fs.length;
    }
}
*/
