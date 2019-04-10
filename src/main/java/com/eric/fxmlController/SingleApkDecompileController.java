package com.eric.fxmlController;

import com.eric.tools.decode.APKTool;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Paint;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.joda.time.DateTime;

import java.io.File;
import java.util.Locale;

/*
 *@description:单一APK文件反编译FXML控制器
 *@author:cuigs
 *@Email:cuigs@ti-net.com.cn
 *@Version:1.0
 *@Date:2019/4/10
 */
public class SingleApkDecompileController {

    public SingleApkDecompileController() {
    }

    //初始化方法，必写
    @FXML
    private void initialize() {
        System.out.println("initialize SingleApkDecompileController");

    }

    //选择的一个APK文件的路径
    @FXML
    private Label choseOneApkPathLabel;
    //反编译结果存放文件夹路径
    @FXML
    private Label DecompileResultSavePathLabel;
    //提示信息Label
    @FXML
    private Label tipsLabel;
    //反编译信息文本域
    @FXML
    private TextArea decompileInfoTextArea;
    //文件路径
    private String filePath = null;
    //文件夹路径
    private String dirPath = null;

    /**
     * 选择一个文件按钮点击事件
     */
    public void chooseOneApk() {
        //清空提示文字
        tipsLabel.setText("");
        Stage st = new Stage();
        FileChooser fc = new FileChooser();
        //设置标题
        fc.setTitle("单选文件");
        //设置初始路径
        fc.setInitialDirectory(new File("D://"));
        //设置打开的文件类型
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("文件类型", "*.apk"));
        File file = fc.showOpenDialog(st);
        if (null != file) {
            //文件路径
            String path = file.getAbsolutePath();
            filePath = path;
            choseOneApkPathLabel.setText("选择的文件路径：" + path);
        }


    }

    /**
     * 设置反编译结果存放文件夹
     */
    public void setDecompileResultSavePath() {
        //清空提示文字
        tipsLabel.setText("");

        Stage st = new Stage();
        DirectoryChooser dc = new DirectoryChooser();
        dc.setInitialDirectory(new File("D:" + File.separator));
        dc.setTitle("选择文件夹");
        File file = dc.showDialog(st);
        if (null != file) {
            String absolutePath = file.getAbsolutePath();
            dirPath = absolutePath;
            if (null != dirPath) {
                DecompileResultSavePathLabel.setText(dirPath);

            }

        }

    }

    /**
     * 开始反编译按钮点击事件
     */
    public void startDecompile() {
        DateTime dateTime = new DateTime();
        //清空日志文本域
        decompileInfoTextArea.setText("");
        //允许换行
        decompileInfoTextArea.setWrapText(true);
        //当前时间
        String stringDate = dateTime.toString("yyyy_MM_dd_HH_mm_ss", Locale.CHINESE);
        //路径不为空
        if (null != filePath && null != dirPath) {
            decompileInfoTextArea.appendText("开始反编译.....\n");

            decompileInfoTextArea.appendText("正在反编译.....\n");
            new Thread(new Runnable() {
                @Override
                public void run() {

                    APKTool.decode(filePath, dirPath + File.separator + "DecompileResults_" + stringDate);
                }
            }).start();
//                    decompileInfoTextArea.appendText("反编译完成.....\n");

        } else if (null == filePath && null != dirPath) {
            tipsLabel.setText("请先选择要反编译的Apk文件再点击反编译按钮哦！");
            tipsLabel.setTextFill(Paint.valueOf("#FF0000"));

        } else if (null != filePath && null == dirPath) {
            tipsLabel.setText("请先选择反编译结果存放文件夹再点击反编译按钮哦！");
            tipsLabel.setTextFill(Paint.valueOf("#FF0000"));

        } else {
            tipsLabel.setText("请先选择要反编译的Apk文件和反编译结果存放文件夹再点击反编译按钮哦！");
            tipsLabel.setTextFill(Paint.valueOf("#FF0000"));

        }


    }
}
