package com.eric.tools.time;

/**
 * 时间转换工具类
 * 将毫秒转为正常的时分秒
 */
public class TimeConvert {
    /**
     * 将毫秒转换为正常的时分秒
     *
     * @param mesc 毫秒数
     */
    public static void convert(long mesc) {
        //将毫秒转为秒
        long totalSeconds = mesc / 1000;
        //秒数
        long currentSecond = totalSeconds % 60;
        //求出现在的分
        long totalMinutes = totalSeconds / 60;
        long currentMinute = totalMinutes % 60;
        //求出现在的小时
        long totalHour = totalMinutes / 60;
        long currentHour = totalHour % 24;
        //小时数大于零
        if (currentHour > 0) {
            System.out.println("耗时：" + currentHour + "时" + currentMinute + "分" + currentSecond + "秒");
        } else if (currentMinute > 0) {
            System.out.println("耗时：" + currentMinute + "分" + currentSecond + "秒");

        } else {
            System.out.println("耗时：" + currentSecond + "秒");

        }


    }
}
