package com.eric.fxmlController;

import com.eric.tools.decode.APKTool;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.joda.time.DateTime;

import java.io.File;
import java.util.List;
import java.util.Locale;


/*
 *@description:批量反编译Apk文件
 *@author:cuigs
 *@Email:cuigs@ti-net.com.cn
 *@Version:1.0
 *@Date:2019/4/10
 */
public class MultipleApkDecompileController {
    public MultipleApkDecompileController() {
    }

    //初始化方法，必写
    @FXML
    private void initialize() {
        System.out.println("initialize MultipleApkDecompileController");

    }

    //选择多个APK文件路径Label
    @FXML
    private Label choseManyApkPathLabel;
    //反编译结果存放文件夹路径
    @FXML
    private Label DecompileResultSavePathLabel;
    //提示信息Label
    @FXML
    private Label tipsLabel;

    //文件路径
    private String filePath = null;
    //文件夹路径
    private String dirPath = null;


    /**
     * 设置反编译输出路径
     */

    public void setDecompileResultSavePath() {

    }

    /**
     * 选择多个Apk文件
     */
    @FXML
    public void chooseManyApk() {
        //当前时间
        DateTime dateTime = new DateTime();
        String stringDate = dateTime.toString("yyyy_MM_dd_HH_mm_ss", Locale.CHINESE);
        Stage st = new Stage();
        FileChooser fc = new FileChooser();
        //设置标题
        fc.setTitle("选择多个Apk文件");
        //设置初始路径
        fc.setInitialDirectory(new File("D://"));
        //设置打开的文件类型
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("文件类型", "*.apk"));
        List<File> files = fc.showOpenMultipleDialog(st);
        if (null != files) {
            for (File file : files) {
                //获取文件路径
                String path = file.getAbsolutePath();
                if (null != dirPath) {
                    APKTool.decode(path, dirPath + File.separator + "DecompileResults_" + stringDate);
                } else {
                    tipsLabel.setText("请先选择反编译结果存放文件夹再点击反编译按钮哦！");
                    tipsLabel.setTextFill(Paint.valueOf("#FF0000"));

                }

            }
        }

    }


    /**
     * 开始反编译按钮点击事件
     */
    public void startDecompile() {

    }
}
