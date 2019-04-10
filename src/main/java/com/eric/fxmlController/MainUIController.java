package com.eric.fxmlController;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

/*
 *@description:主界面FXML控制器
 *@author:cuigs
 *@Email:cuigs@ti-net.com.cn
 *@Version:1.0
 *@Date:2019/4/10
 */
public class MainUIController {

    @FXML
    private BorderPane maxMainBorderPane;
    //单个APK文件反编译按钮
    @FXML
    private Button singleApkDecompile;
    //多个APK文件反编译
    @FXML
    private Button multipleApkDecompile;

    //权限特征提取
    @FXML
    private Button authorityExtract;
    //API特征提取
    @FXML
    private Button apiExtract;

    //在线学习算法
    @FXML
    private Button onlineLearningAlgorithm;

    //其它算法
    @FXML
    private Button otherAlgorithm;
    //单一应用检测

    @FXML
    private Button singleApkDetection;
    //批量应用检测
    @FXML
    private Button multipleApkDetection;
    //在线更新
    @FXML
    private Button onlineUpdate;
    //数据对比
    @FXML
    private Button compareData;

    //中间内容面板
    @FXML
    private StackPane mainContainer;
    //中间内容面板label
    @FXML
    private Label mainContainerLabel;

    @FXML
    private SplitPane centerSplitPane;

    public MainUIController() {
    }

    @FXML
    private void initialize() {
        System.out.println("initialize()");

    }

    public Button getSingleApkDecompile() {
        return singleApkDecompile;
    }

    public StackPane getMainContainer() {
        return mainContainer;
    }

    public Label getMainContainerLabel() {
        return mainContainerLabel;
    }

    public BorderPane getMaxMainBorderPane() {
        return maxMainBorderPane;
    }

    public SplitPane getCenterSplitPane() {
        return centerSplitPane;
    }

    public Button getMultipleApkDecompile() {
        return multipleApkDecompile;
    }

    public Button getAuthorityExtract() {
        return authorityExtract;
    }

    public Button getApiExtract() {
        return apiExtract;
    }

    public Button getOnlineLearningAlgorithm() {
        return onlineLearningAlgorithm;
    }

    public Button getOtherAlgorithm() {
        return otherAlgorithm;
    }

    public Button getSingleApkDetection() {
        return singleApkDetection;
    }

    public Button getMultipleApkDetection() {
        return multipleApkDetection;
    }

    public Button getOnlineUpdate() {
        return onlineUpdate;
    }

    public Button getCompareData() {
        return compareData;
    }
}
