package com.eric.tools.csv;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * @ClassName: FileConstantUtils
 * @Description: CSV常量工具类
 * @Author: Eric
 * @Date: 2019/3/24 0024
 * @Email: xiao_cui_vip@163.com
 */
public class FileConstantUtils {

    public final static List<String> API_HEAD_LIST = Arrays.asList(
            "API编号",
            "API内容",
            "应用属性",
            "APK编号",
            "应用名称"
    );
    public final static List<String> AU_HEAD_LIST = Arrays.asList(
            "权限编号",
            "权限特征",
            "应用属性",
            "APK编号",
            "应用名称"
    );

    // 导出文件路径   E:\BiSheProjects\CSV
    public final static String DOWNLOAD_FILE_PATH = "E:" + File.separator + "BiSheProjects" + File.separator + "CSV" + File.separator;
    public final static String FILE_NAME = "Android数据集";
}
