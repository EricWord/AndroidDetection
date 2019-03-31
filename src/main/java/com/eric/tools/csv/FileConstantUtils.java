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

    public final static List<String> HEAD_LIST = Arrays.asList(
            "API_Content",
            "Authority_Content",
            "APK_Attribute"
    );
    //tb_apk表的表头
    public final static List<String> TABLE_APK_LIST = Arrays.asList(
            "apk_id",
            "package_name",
            "apk_attribute"
    );

    //tb_api表的表头
    public final static List<String> TABLE_API_LIST = Arrays.asList(
            "api_id",
            "api_content",
            "api_mad5"
    );

    //tb_authority表的表头
    public final static List<String> TABLE_AUTHORITY_LIST = Arrays.asList(
            "authority_id",
            "authority_content",
            "authority_md5"
    );

    //tb_api_apk_map表的表头
    public final static List<String> TABLE_API_APK_MAP_LIST = Arrays.asList(
            "apk_id",
            "api_id"
    );

    //tb_authority_apk_map表的表头
    public final static List<String> TABLE_AUTHORITY_APK_MAP_LIST = Arrays.asList(
            "apk_id",
            "authority_id"
    );
    // 导出文件路径   D:\cgs\File\CSV\
    public final static String DOWNLOAD_FILE_PATH = "D:" + File.separator + "cgs" + File.separator + "File" + File.separator + "CSV" + File.separator;
    public final static String FILE_NAME = "Android数据集";
}
