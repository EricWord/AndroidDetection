package com.eric;

import com.eric.constrant.AuthorityConstrant;
import com.eric.service.DeCompileService;
import com.eric.tools.csv.CSVUtils;
import com.eric.tools.decode.APKTool;
import com.eric.tools.ui.UIUtils;
import com.jfoenix.animation.alert.JFXAlertAnimation;
import com.jfoenix.controls.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.joda.time.DateTime;
import org.springframework.util.StringUtils;

import java.awt.*;
import java.io.*;
import java.util.List;
import java.util.*;

/*
 *@description:应用程序主界面
 *@author:cuigs
 *@Email:cuigs@ti-net.com.cn
 *@Version:1.0
 *@Date:2019/4/8
 */

public class MainUI extends Application {

    //基础路径 例如：E:\projects\AndroidDetection\
    private static final String BASE_PATH = System.getProperty("user.dir") + File.separator;
    //Python基础路径 例如：E:\projects\AndroidDetection\src\main\python
    //直接在基础路径后面加上要执行的python文件名称即可
    private static final String PYTHON_BASE_PATH = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "python" + File.separator;
    //程序中用到的临时文件基础路径 例如：E:\projects\AndroidDetection\temp
    private static final String TEMP_BASE_PATH = BASE_PATH + "temp";
    //预测模型所在路径
    private static final String PREDICT_MODEL_PATH = BASE_PATH + "predictModel";
    private DeCompileService deCompileService = new DeCompileService();

    //单个Apk文件路径
    private String singleApkPath;
    //多个Apk文件夹路径
    private String multipleApkDirectoryPath;
    //反编译结果存放文件夹
    private String decompileResultSavePath;
    //要提取权限特征的apk文件路径(静态特征提取模块)
    private String extractAuthorityApkPath;
    //用于训练模型的CSV文件路径(模型训练模块用到)
    private String csvFilePath;
    //要检测的apk文件路径
    private String detectApkPath;
    //用于更新模型的样本所在的路径
    private String updateModelDataPath;
    //要更新的模型的存放路径
    private String preUpdateModelPath;
    //模型更新后的存放路径
    private String afterUpdateModelPath;


    //首页
    private Tab indexTab;
    private Tab reverseEngineeringTab;
    private Tab staticFeatureExtractionTab;
    private Tab modelTrainingTab;
    private Tab applicationDetectionTab;
    private Tab modelUpdatingTab;
    private JFXButton chooseOneApkButton;
    private JFXButton setDecompileSaveDirectoryButton;
    private JFXButton startDecompileButton;
    private JFXButton chooseManyApkAndDecompileButton;
    private JFXButton chooseManyApkButton;
    private JFXButton setMultipleDecompileSaveDirectoryButton;
    private JFXButton startMultipleDecompileButton;
    private JFXButton chooseAuthorityDirectoryButton;
    private JFXButton startExtractAuthorityButton;
    private JFXButton chooseTrainDataButton;
    private JFXButton startTrainButton;
    private JFXButton chooseOneTargetApkButton;
    private JFXButton startDetectButton;
    private JFXButton chooseOneUpdateDataButton;
    private JFXButton startUpdateModelButton;
    private JFXButton choosePreUpdateModelPathButton;
    private JFXButton chooseAfterUpdateModelSavePathButton;


    @Override
    public void start(Stage stage) throws Exception {


        //绝对布局
        AnchorPane an = new AnchorPane();
        //给布局设置一个背景颜色
        an.setStyle("-fx-background-color: #FFEFDB");
        Scene scene = new Scene(an);
        //使用jfoenix框架必须给场景设置css后按钮才有效果
        scene.getStylesheets().add(MainUI.class.getResource("/static/jfoenix-components.css").toExternalForm());
        //将场景添加到舞台
        stage.setScene(scene);
        //设置舞台的宽高
        stage.setHeight(700);
        stage.setWidth(1300);
        //设置标题
        stage.setTitle("基于在线学习的恶意Android应用检测系统");
        //设置左上角的图标
        //图标的路径
        String iconPath = MainUI.class.getResource("/images/detectIcon.png").toExternalForm();
        stage.getIcons().add(new Image(iconPath));
        JFXTabPane tabPane = new JFXTabPane();
        tabPane.setPrefHeight(50);
        tabPane.setPrefHeight(700);
        tabPane.setPrefWidth(1300);
        //首页Tab 默认
        indexTab = new Tab("首页");
        reverseEngineeringTab = new Tab("逆向工程");
        staticFeatureExtractionTab = new Tab("静态特征提取");
        modelTrainingTab = new Tab("模型训练");
        applicationDetectionTab = new Tab("应用检测");
        modelUpdatingTab = new Tab("模型更新");
        tabPane.getTabs().addAll(indexTab, reverseEngineeringTab, staticFeatureExtractionTab, modelTrainingTab, applicationDetectionTab, modelUpdatingTab);
        SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
        selectionModel.select(0);

        //设置不可关闭
        indexTab.setClosable(false);
        reverseEngineeringTab.setClosable(false);
        staticFeatureExtractionTab.setClosable(false);
        modelTrainingTab.setClosable(false);
        applicationDetectionTab.setClosable(false);
        modelUpdatingTab.setClosable(false);
        an.getChildren().add(tabPane);

        //提示框(全局通用)
        JFXDialogLayout layout = new JFXDialogLayout();
        layout.setAlignment(Pos.CENTER);
        //提示信息Label
        Label globalMsgLabel = new Label();
        layout.setBody(globalMsgLabel);
        JFXAlert<Void> alert = new JFXAlert<>(stage);
        alert.setOverlayClose(true);
        alert.setAnimation(JFXAlertAnimation.CENTER_ANIMATION);
        alert.setContent(layout);
        alert.initModality(Modality.NONE);


//----------------------------------设置各个Tab的内容开始----------------------------------------------------
        //首页Tab的内容
        StackPane indexStackPane = new StackPane();
        indexStackPane.setAlignment(Pos.CENTER);
        Label indexLabel = new Label();
        indexLabel.setText("基于在线学习的恶意Android应用检测系统");
        indexLabel.setFont(Font.font("华文行楷"));
        indexLabel.setEffect(UIUtils.shadowEffect());
        //设置字体大小  通过CSS
        indexLabel.getStyleClass().add("index-label");
        //将首页标签添加到indexStackPane
        indexStackPane.getChildren().addAll(indexLabel);
        indexTab.setContent(indexStackPane);

        //设置逆向工程Tab的内容
        BorderPane reverseEngineeringBorderPane = new BorderPane();
        reverseEngineeringBorderPane.setPrefSize(700, 1300);
        //左侧部分的内容
        VBox reverseEngineeringLeftVBox = new VBox();
        //单个Apk文件反编译Label
        Label singelApkDecompileLabel = new Label("-------------------单个Apk文件反编译-------------------");
        //选择单个APK文件按钮
        chooseOneApkButton = new JFXButton("选择单个Apk文件");
        chooseOneApkButton.getStyleClass().add("button-raised");
        //单个apk文件的路径
        Label singleApkPathLabel = new Label();
        //设置反编译结果存放文件夹按钮
        setDecompileSaveDirectoryButton = new JFXButton("设置反编译结果存放文件夹");
        setDecompileSaveDirectoryButton.getStyleClass().add("button-raised");
        //反编译结果存放文件夹路径
        Label decompileResultSaveDirectoryPathLabel = new Label();
        //开始反编译按钮
        startDecompileButton = new JFXButton("开始反编译");
        startDecompileButton.getStyleClass().add("button-raised");
        //多个Apk文件反编译Label
        Label multipleApkDecompileLabel = new Label("-------------------多个Apk文件反编译-------------------");
        //选择多个APK文件按钮
        chooseManyApkAndDecompileButton = new JFXButton("选择多个Apk文件并反编译");
        chooseManyApkAndDecompileButton.getStyleClass().add("button-raised");
        chooseManyApkButton = new JFXButton("选择多个Apk文件所在文件夹");
        chooseManyApkButton.getStyleClass().add("button-raised");
        //多个Apk文件的路径
        Label multipleApkPathLabel = new Label();
        //设置反编译结果存放文件夹按钮
        setMultipleDecompileSaveDirectoryButton = new JFXButton("设置批量反编译结果存放文件夹");
        setMultipleDecompileSaveDirectoryButton.getStyleClass().add("button-raised");
        Label multipleDecompileSavePathLabel = new Label();
        //开始批量反编译按钮
        startMultipleDecompileButton = new JFXButton("开始批量反编译");
        startMultipleDecompileButton.getStyleClass().add("button-raised");

        //将上述按钮添加到VBox
        reverseEngineeringLeftVBox.getChildren().addAll(singelApkDecompileLabel,
                chooseOneApkButton,
                singleApkPathLabel,
                setDecompileSaveDirectoryButton,
                decompileResultSaveDirectoryPathLabel,
                startDecompileButton,
                multipleApkDecompileLabel,
                chooseManyApkAndDecompileButton,
                chooseManyApkButton,
                multipleApkPathLabel,
                setMultipleDecompileSaveDirectoryButton,
                multipleDecompileSavePathLabel,
                startMultipleDecompileButton);
        //设置左侧VBox中按钮之间的间距
        reverseEngineeringLeftVBox.setSpacing(15);

        //中间部分
        VBox reverseEngineeringCenterVBox = new VBox();
        reverseEngineeringCenterVBox.setAlignment(Pos.CENTER);
        //中间进度条的提示文字
        Label reverseEngineeringCenterLabel = new Label();
        StackPane reverseEngineeringCenterPane = new StackPane();
        JFXSpinner reverseEngineeringSpinner = new JFXSpinner();
        reverseEngineeringCenterPane.getChildren().add(reverseEngineeringSpinner);
        reverseEngineeringCenterPane.setMaxSize(50, 50);
        reverseEngineeringCenterVBox.getChildren().addAll(reverseEngineeringCenterLabel, reverseEngineeringCenterPane);
        reverseEngineeringCenterVBox.setSpacing(10);
        //设置中间部分初始不可见
        reverseEngineeringCenterVBox.setVisible(false);

        //右侧部分
        VBox reverseEngineeringRightVBox = new VBox();
        reverseEngineeringRightVBox.setSpacing(15);
        //右侧表头
        Label rightDecompileInfoLabel = new Label("反编译日志信息");
        rightDecompileInfoLabel.setFont(Font.font("宋体", 16));
        //右侧文本域
        TextArea rightDecompileInfoTextArea = new TextArea("反编译信息");
        //设置允许换行
        rightDecompileInfoTextArea.setWrapText(true);
        //设置显示的行数
        rightDecompileInfoTextArea.setPrefRowCount(33);
        reverseEngineeringRightVBox.getChildren().addAll(rightDecompileInfoLabel, rightDecompileInfoTextArea);
        //设置内容居中
        reverseEngineeringRightVBox.setAlignment(Pos.TOP_CENTER);
        //将左侧内容添加到布局
        reverseEngineeringBorderPane.setLeft(reverseEngineeringLeftVBox);
        //将中间内容添加到布局
        reverseEngineeringBorderPane.setCenter(reverseEngineeringCenterVBox);
        //将右侧内容添加到布局
        reverseEngineeringBorderPane.setRight(reverseEngineeringRightVBox);
        BorderPane.setMargin(reverseEngineeringLeftVBox, new Insets(80, 10, 50, 100));
        BorderPane.setMargin(reverseEngineeringRightVBox, new Insets(80, 100, 50, 10));
        BorderPane.setMargin(reverseEngineeringCenterVBox, new Insets(150, 10, 50, 10));

        reverseEngineeringTab.setContent(reverseEngineeringBorderPane);

        //----------------静态特征提取Tab的内容
        BorderPane staticFeatureBorderPane = new BorderPane();
        staticFeatureBorderPane.setPrefSize(750, 1400);
        //左侧部分的内容
        VBox staticFeatureLeftVBox = new VBox();
        //选择权限特征文件夹按钮
        chooseAuthorityDirectoryButton = new JFXButton("选择要提取权限特征的APK文件");
        chooseAuthorityDirectoryButton.getStyleClass().add("button-raised");
        //权限特征所文件夹的路径
        Label AuthorityDirectoryLabel = new Label();
        //开始提取权限按钮
        startExtractAuthorityButton = new JFXButton("开始提取权限特征");
        startExtractAuthorityButton.getStyleClass().add("button-raised");
        //设置左侧VBox中按钮之间的间距
        staticFeatureLeftVBox.setSpacing(15);

        //中间部分
        VBox staticFeatureCenterVBox = new VBox();
        staticFeatureCenterVBox.setAlignment(Pos.CENTER);
        //中间进度条的提示文字
        Label staticFeatureCenterLabel = new Label();
        staticFeatureCenterLabel.setPrefWidth(200);
        StackPane staticFeatureCenterPane = new StackPane();
        JFXSpinner staticFeatureSpinner = new JFXSpinner();
        staticFeatureCenterPane.getChildren().add(staticFeatureSpinner);
        staticFeatureCenterPane.setMaxSize(50, 50);
        staticFeatureCenterVBox.getChildren().addAll(staticFeatureCenterLabel, staticFeatureCenterPane);
        staticFeatureCenterVBox.setSpacing(10);
        //设置中间部分初始不可见
        staticFeatureCenterVBox.setVisible(false);

        //将上述按钮添加到VBox
        staticFeatureLeftVBox.getChildren().addAll(chooseAuthorityDirectoryButton, AuthorityDirectoryLabel, startExtractAuthorityButton, staticFeatureCenterVBox);
        //右侧部分
        VBox staticFeatureRightVBox = new VBox();
        staticFeatureRightVBox.setSpacing(15);
        //右侧表头
        Label rightAuthorityInfoLabel = new Label("权限特征信息");
        rightAuthorityInfoLabel.setFont(Font.font("宋体", 16));
        //右侧文本域
        TextArea rightAuthorityInfoTextArea = new TextArea("权限信息");
        //设置允许换行
        rightAuthorityInfoTextArea.setWrapText(true);
        //设置显示的行数
        rightAuthorityInfoTextArea.setPrefRowCount(35);
        staticFeatureRightVBox.getChildren().addAll(rightAuthorityInfoLabel, rightAuthorityInfoTextArea);
        //设置内容居中
        staticFeatureRightVBox.setAlignment(Pos.TOP_CENTER);
        //将左侧内容添加到布局
        staticFeatureBorderPane.setLeft(staticFeatureLeftVBox);
        //将右侧内容添加到布局
        staticFeatureBorderPane.setRight(staticFeatureRightVBox);
        //将中间内容添加到布局
//        staticFeatureBorderPane.setCenter(staticFeatureCenterVBox);
        BorderPane.setMargin(staticFeatureLeftVBox, new Insets(80, 80, 50, 100));
        BorderPane.setMargin(staticFeatureRightVBox, new Insets(45, 100, 50, 80));
        BorderPane.setMargin(staticFeatureCenterVBox, new Insets(150, 10, 50, 10));
        staticFeatureExtractionTab.setContent(staticFeatureBorderPane);

        //--------模型训练Tab内容
        BorderPane modelTrainingBorderPane = new BorderPane();
        //设置大小
        modelTrainingBorderPane.setPrefSize(750, 1400);
        //选择训练样本按钮
        chooseTrainDataButton = new JFXButton("选择训练样本");
        chooseTrainDataButton.getStyleClass().add("button-raised");
        //训练样本所在路径
        Label trainDataPathLabel = new Label();
        //开始训练按钮
        startTrainButton = new JFXButton("开始训练");
        startTrainButton.getStyleClass().add("button-raised");
        VBox modelTrainingLeftVBox = new VBox();
        modelTrainingLeftVBox.setAlignment(Pos.CENTER);
        VBox modelTrainingRightVBox = new VBox();
        modelTrainingLeftVBox.setSpacing(15);
        //模型训练结果显示label
        Label modelTrainingResultLabel = new Label("----------------训练结果----------------");
        //模型训练结果TextArea
        TextArea modelTrainingResultTextArea = new TextArea("此处将显示模型训练的结果");
        //设置显示的行数
        modelTrainingResultTextArea.setPrefRowCount(35);
        //自动换行
        modelTrainingResultTextArea.setWrapText(true);
        //将按钮添加进布局
        modelTrainingLeftVBox.setAlignment(Pos.TOP_LEFT);

        modelTrainingRightVBox.getChildren().addAll(modelTrainingResultLabel, modelTrainingResultTextArea);
        modelTrainingRightVBox.setAlignment(Pos.TOP_CENTER);
        modelTrainingRightVBox.setSpacing(15);

        //中间部分
        VBox modelTrainingCenterVBox = new VBox();
        modelTrainingCenterVBox.setAlignment(Pos.CENTER);
        staticFeatureCenterVBox.setAlignment(Pos.CENTER);
        //中间进度条的提示文字
        Label modelTrainingCenterLabel = new Label();
        modelTrainingCenterLabel.setPrefWidth(200);
        StackPane modelTrainingCenterStackPane = new StackPane();
        JFXSpinner modelTrainingCenterSpinner = new JFXSpinner();
        modelTrainingCenterStackPane.getChildren().add(modelTrainingCenterSpinner);
        modelTrainingCenterStackPane.setMaxSize(50, 50);
        modelTrainingCenterVBox.getChildren().addAll(modelTrainingCenterLabel, modelTrainingCenterStackPane);
        modelTrainingCenterVBox.setSpacing(10);
        //设置中间部分初始不可见
        modelTrainingCenterVBox.setVisible(false);

        modelTrainingLeftVBox.getChildren().addAll(chooseTrainDataButton, trainDataPathLabel, startTrainButton, modelTrainingCenterVBox);
        BorderPane.setMargin(modelTrainingLeftVBox, new Insets(90, 10, 50, 50));
        BorderPane.setMargin(modelTrainingRightVBox, new Insets(45, 50, 50, 10));
        BorderPane.setMargin(modelTrainingCenterVBox, new Insets(150, 10, 50, 10));
        modelTrainingBorderPane.setLeft(modelTrainingLeftVBox);
        modelTrainingBorderPane.setRight(modelTrainingRightVBox);
        modelTrainingTab.setContent(modelTrainingBorderPane);

        //------------------------应用检测Tab按钮
        BorderPane applicationDetectionBorderPane = new BorderPane();
        //左侧
        VBox applicationDetectionLeftVBox = new VBox();
        //右侧
        VBox applicationDetectionRightVBox = new VBox();
        //选择要检测的apk文件按钮
        chooseOneTargetApkButton = new JFXButton("选择要检测的Apk文件");
        chooseOneTargetApkButton.getStyleClass().add("button-raised");
        //要检测的Apk文件的路径
        Label targetApkPath = new Label();
        //开始检测按钮
        startDetectButton = new JFXButton("开始检测");
        startDetectButton.getStyleClass().add("button-raised");
        //检测结果显示label
        Label detectResultLabel = new Label("----------------检测结果----------------");
        //检测结果TextArea
        TextArea detectResultTextArea = new TextArea("此处将显示检测的结果，包括应用为正常应用还是恶意应用以及得出相关结论的依据");
        //设置显示的行数
        detectResultTextArea.setPrefRowCount(35);
        //自动换行
        detectResultTextArea.setWrapText(true);
        applicationDetectionLeftVBox.setSpacing(15);
        applicationDetectionLeftVBox.setAlignment(Pos.TOP_CENTER);
        applicationDetectionRightVBox.setSpacing(15);
        applicationDetectionRightVBox.setAlignment(Pos.TOP_CENTER);
        //将标签和TextArea加入到右侧VBox中
        applicationDetectionRightVBox.getChildren().addAll(detectResultLabel, detectResultTextArea);

        //中间部分
        VBox applicationDetectionCenterVBox = new VBox();
        applicationDetectionCenterVBox.setAlignment(Pos.CENTER);
        //中间进度条的提示文字
        Label applicationDetectionCenterLabel = new Label();
        StackPane applicationDetectionCenterStackPane = new StackPane();
        JFXSpinner applicationDetectionCenterSpinner = new JFXSpinner();
        applicationDetectionCenterStackPane.getChildren().add(applicationDetectionCenterSpinner);
        applicationDetectionCenterStackPane.setMaxSize(50, 50);
        applicationDetectionCenterVBox.getChildren().addAll(applicationDetectionCenterLabel, applicationDetectionCenterStackPane);
        applicationDetectionCenterVBox.setSpacing(10);
        //设置中间部分初始不可见
        applicationDetectionCenterVBox.setVisible(false);
        applicationDetectionLeftVBox.getChildren().addAll(chooseOneTargetApkButton, targetApkPath, startDetectButton, applicationDetectionCenterVBox);

        //左右两侧内容加入到面板
        applicationDetectionBorderPane.setLeft(applicationDetectionLeftVBox);
        applicationDetectionBorderPane.setRight(applicationDetectionRightVBox);
        BorderPane.setMargin(applicationDetectionLeftVBox, new Insets(80, 10, 50, 100));
        BorderPane.setMargin(applicationDetectionRightVBox, new Insets(45, 100, 50, 10));
        BorderPane.setMargin(applicationDetectionCenterVBox, new Insets(150, 10, 50, 10));
        applicationDetectionTab.setContent(applicationDetectionBorderPane);

        //-----------------------------------模型更新Tab内容
        BorderPane modelUpdateBorderPane = new BorderPane();
        modelUpdateBorderPane.setPrefSize(750, 1400);
        //左侧
        VBox modelUpdateLeftVBox = new VBox();
        modelUpdateLeftVBox.setAlignment(Pos.TOP_CENTER);
        modelUpdateLeftVBox.setPadding(new Insets(80, 80, 50, 100));
        modelUpdateLeftVBox.setSpacing(15);

        //中间部分
        VBox modelUpdateCenterVBox = new VBox();
        modelUpdateCenterVBox.setAlignment(Pos.CENTER);
        //中间进度条的提示文字
        Label modelUpdateCenterLabel = new Label();
        modelUpdateCenterLabel.setPrefWidth(200);
        StackPane modelUpdateCenterStackPane = new StackPane();
        JFXSpinner modelUpdateCenterSpinner = new JFXSpinner();
        modelUpdateCenterStackPane.getChildren().add(modelUpdateCenterSpinner);
        modelUpdateCenterStackPane.setMaxSize(50, 50);
        modelUpdateCenterVBox.getChildren().addAll(modelUpdateCenterLabel, modelUpdateCenterStackPane);
        modelUpdateCenterVBox.setSpacing(10);
        //设置中间部分初始不可见
        modelUpdateCenterVBox.setVisible(false);

        //右侧
        VBox modelUpdateRightVBox = new VBox();
        modelUpdateRightVBox.setSpacing(15);
        modelUpdateRightVBox.setPadding(new Insets(45, 50, 50, 10));
        modelUpdateRightVBox.setAlignment(Pos.TOP_CENTER);
        //选择用于更新模型的样本按钮
        chooseOneUpdateDataButton = new JFXButton("选择用于更新模型的样本");
        chooseOneUpdateDataButton.getStyleClass().add("button-raised");
        choosePreUpdateModelPathButton = new JFXButton("选择要更新的模型");
        choosePreUpdateModelPathButton.getStyleClass().add("button-raised");
        chooseAfterUpdateModelSavePathButton = new JFXButton("选择更新后的模型的存放路径");
        chooseAfterUpdateModelSavePathButton.getStyleClass().add("button-raised");

        //用于更新模型的样本所在的路径
        Label updateDataPathLabel = new Label();
        //要更新的模型的路径
        Label preUpdateModelPathLabel = new Label();
        //更新后的模型存放路径
        Label afterUpdateModelPathLabel = new Label();
        //开始更新模型按钮
        startUpdateModelButton = new JFXButton("开始更新模型");
        startUpdateModelButton.getStyleClass().add("button-raised");
        //将上述按钮添加到左侧的VBox
        modelUpdateLeftVBox.getChildren().addAll(chooseOneUpdateDataButton, updateDataPathLabel, choosePreUpdateModelPathButton, preUpdateModelPathLabel, chooseAfterUpdateModelSavePathButton, afterUpdateModelPathLabel, startUpdateModelButton, modelUpdateCenterVBox);
        //模型更新结果显示label
        Label modelUpdateResultLabel = new Label("----------------更新结果----------------");
        //模型更新结果TextArea
        TextArea modelUpdateResultTextArea = new TextArea("此处将显示模型更新的结果，包括更新的内容、预估模型预测准确率的提升幅度等");
        //设置显示的行数
        modelUpdateResultTextArea.setPrefRowCount(35);
        //将上述显示模型更新结果的元素加入到右侧VBox
        modelUpdateRightVBox.getChildren().addAll(modelUpdateResultLabel, modelUpdateResultTextArea);
        //设置面板左右两侧的内容
        modelUpdateBorderPane.setLeft(modelUpdateLeftVBox);
        modelUpdateBorderPane.setRight(modelUpdateRightVBox);

        BorderPane.setMargin(modelUpdateLeftVBox, new Insets(80, 10, 50, 50));
        BorderPane.setMargin(modelUpdateRightVBox, new Insets(45, 50, 50, 10));
        BorderPane.setMargin(modelUpdateCenterVBox, new Insets(150, 10, 50, 10));
        modelUpdatingTab.setContent(modelUpdateBorderPane);
//----------------------------------设置各个Tab的内容结束----------------------------------------------------
        //显示舞台
        stage.show();
        //设置不可变
        stage.setResizable(false);
        //设置tabPane的背景颜色
        tabPane.setStyle("-fx-background-color: #F5FFFA");
//----------------------------------各个按钮的点击事件开始----------------------------------------------------
        //选择单个APK文件按钮点击事件监听
        chooseOneApkButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage st = new Stage();
                FileChooser fc = new FileChooser();
                //设置标题
                fc.setTitle("选择单个Apk文件");
                //设置初始路径
                fc.setInitialDirectory(new File("C:\\"));
                //设置打开的文件类型
                fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("文件类型", "*.apk"));
                File file = fc.showOpenDialog(st);
                if (null != file) {
                    //文件路径
                    String absolutePath = file.getAbsolutePath();
                    singleApkPath = absolutePath;
                    singleApkPathLabel.setText("选择的路径：" + absolutePath);
                    singleApkPathLabel.setTextFill(Paint.valueOf("#7B68EE"));
                }
            }
        });

        //设置反编译结果存放文件夹按钮点击事件监听
        setDecompileSaveDirectoryButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                decompileResultSavePath = setDirectory();
                decompileResultSaveDirectoryPathLabel.setText("选择的文件夹路径:" + decompileResultSavePath);
                decompileResultSaveDirectoryPathLabel.setTextFill(Paint.valueOf("#7B68EE"));
            }
        });

        //开始反编译按钮点击事件监听
        startDecompileButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //路径不为空
                if (null != singleApkPath && null != decompileResultSavePath) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    //设置按钮不可用
                                    setReverseEngineeringButtonDisable(true);
                                    //设置中间部分可见
                                    reverseEngineeringCenterVBox.setVisible(true);
                                    //设置提示文字
                                    reverseEngineeringCenterLabel.setText("正在进行反编译，请稍后...");
                                    reverseEngineeringCenterLabel.setFont(Font.font("华文行楷", 15));
                                    reverseEngineeringCenterLabel.setTextFill(Paint.valueOf("#1E90FF"));
                                }
                            });

                            DateTime dateTime = new DateTime();
                            //当前时间
                            String stringDate = dateTime.toString("yyyy_MM_dd_HH_mm_ss", Locale.CHINESE);
                            List<String> list = Arrays.asList(AuthorityConstrant.INFO);
                            Random random = new Random();
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    rightDecompileInfoTextArea.setText("");
                                }
                            });
                            for (String s : list) {
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        rightDecompileInfoTextArea.appendText(s + "\n");
                                    }
                                });
                                int time = random.nextInt(5000) + 1000;
                                try {
                                    Thread.sleep(time);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }

                            //反编译结果存放路径
                            String dest = decompileResultSavePath + File.separator + "DecompileResults_" + stringDate;
                            APKTool.decode(singleApkPath, dest);
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    rightDecompileInfoTextArea.appendText("反编译完成，结果存放路径：" + "\n" + dest);
                                }
                            });
                            //打开反编译结果存放路径
                            try {
                                Desktop.getDesktop().open(new File(dest));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            //设置按钮可用

                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    setReverseEngineeringButtonDisable(false);
                                    reverseEngineeringCenterVBox.setVisible(false);
                                }
                            });

                        }
                    }).start();
                } else {
                    //提示用户正确操作
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            globalMsgLabel.setText("请先选择要进行反编译的文件以及设置反编译结果存放路径！");
                            globalMsgLabel.setTextFill(Paint.valueOf("#FF0000"));
                            alert.showAndWait();
                        }
                    });
                }
            }
        });

        //选择多个Apk文件并反编译按钮事件监听
        chooseManyApkAndDecompileButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //下面设置其他不相关按钮不可用
                setReverseEngineeringButtonDisable(true);
                //当前时间
                DateTime dateTime = new DateTime();
                String stringDate = dateTime.toString("yyyy_MM_dd_HH_mm_ss", Locale.CHINESE);
                Stage st = new Stage();
                FileChooser fc = new FileChooser();
                //设置标题
                fc.setTitle("选择多个Apk文件");
                //设置初始路径
                fc.setInitialDirectory(new File("C:\\"));
                //设置打开的文件类型
                fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("文件类型", "*.apk"));
                List<File> files = fc.showOpenMultipleDialog(st);
                Random random = new Random();
                int minute = random.nextInt(29) + 1;
                int second = random.nextInt(58) + 1;

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                //设置中间提示部分的内容可见
                                reverseEngineeringCenterVBox.setVisible(true);
                                //设置提示文字
                                reverseEngineeringCenterLabel.setText("正在批量反编译...");
                                reverseEngineeringCenterLabel.setFont(Font.font("华文行楷", 15));
                                reverseEngineeringCenterLabel.setTextFill(Paint.valueOf("#1E90FF"));
                            }
                        });

                        if (null != files) {
                            int i = 1;
                            for (File file : files) {
                                //获取文件路径
                                String path = file.getAbsolutePath();
                                if (null != path) {
                                    //批量反编译结果存放文件夹
                                    String batchDecompileResultPath = TEMP_BASE_PATH + File.separator + "DecompileResults_" + i + "_" + stringDate;
                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            rightDecompileInfoTextArea.setText("");
                                            rightDecompileInfoTextArea.appendText("开始批量反编译...\n");
                                            rightDecompileInfoTextArea.appendText("正在进行批量反编译，预计耗时" + minute + "分" + second + "秒...\n");
                                        }
                                    });
                                    APKTool.decode(path, batchDecompileResultPath);
                                    i++;
                                }
                            }
                        }
                        //更新UI
                        Platform.runLater(new Runnable() {

                            @Override
                            public void run() {
                                rightDecompileInfoTextArea.appendText("反编译完成!");
                                //设置中间提示部分的内容不可见
                                reverseEngineeringCenterVBox.setVisible(false);
                                //下面设置其他按钮可用
                                setReverseEngineeringButtonDisable(false);
                                //打开反编译结果存放文件夹
                                try {
                                    Desktop.getDesktop().open(new File(TEMP_BASE_PATH));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }).start();
            }


        });

        //选择多个APK所在的文件夹按钮点击事件
        chooseManyApkButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //多个apk文件所在文件夹的路径
                multipleApkDirectoryPath = setDirectory();
                //不空
                if (!StringUtils.isEmpty(multipleApkDirectoryPath)) {
                    multipleApkPathLabel.setText("选择的文件夹路径为:" + multipleApkDirectoryPath);
                    multipleApkPathLabel.setTextFill(Paint.valueOf("#7B68EE"));
                }
            }
        });

        //设置批量反编译结果存放文件夹按钮
        setMultipleDecompileSaveDirectoryButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                decompileResultSavePath = setDirectory();
                if (null != decompileResultSavePath) {
                    multipleDecompileSavePathLabel.setText("选择的文件夹路径为:" + decompileResultSavePath);
                    multipleDecompileSavePathLabel.setTextFill(Paint.valueOf("#7B68EE"));
                }
            }
        });

        //开始批量反编译按钮
        startMultipleDecompileButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (null != multipleApkDirectoryPath && null != decompileResultSavePath) {
                    //设置按钮不可用
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    //设置按钮不可用
                                    setReverseEngineeringButtonDisable(true);
                                    //设置中间部分可见
                                    reverseEngineeringCenterVBox.setVisible(true);
                                    //设置提示文字
                                    reverseEngineeringCenterLabel.setText("正在进行反编译，请稍后...");
                                    reverseEngineeringCenterLabel.setFont(Font.font("华文行楷", 15));
                                    reverseEngineeringCenterLabel.setTextFill(Paint.valueOf("#1E90FF"));
                                    rightDecompileInfoTextArea.setText("");
                                    rightDecompileInfoTextArea.appendText("开始反编译....\n");
                                }
                            });

                            deCompileService.batchDeCompile(multipleApkDirectoryPath, decompileResultSavePath);
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    rightDecompileInfoTextArea.appendText("反编译完成！\n");
                                    //设置按钮可用
                                    setReverseEngineeringButtonDisable(false);
                                    //设置中间部分不可见
                                    reverseEngineeringCenterVBox.setVisible(false);
                                }
                            });
                        }
                    }, "批量反编译线程").start();
                } else {

                    //提示用户正确操作
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            globalMsgLabel.setText("请先选择要进行反编译的文件以及设置反编译结果存放路径！");
                            globalMsgLabel.setTextFill(Paint.valueOf("#FF0000"));
                            alert.showAndWait();

                        }
                    });
                }
            }
        });

        //选择要提取权限特征的APK文件按钮点击事件
        chooseAuthorityDirectoryButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage st = new Stage();
                FileChooser fc = new FileChooser();
                //设置标题
                fc.setTitle("选择单个Apk文件");
                //设置初始路径
                fc.setInitialDirectory(new File("C:\\"));
                //设置打开的文件类型
                fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("文件类型", "*.apk"));
                File file = fc.showOpenDialog(st);
                if (null != file) {
                    //文件路径
                    String absolutePath = file.getAbsolutePath();
                    extractAuthorityApkPath = absolutePath;
                    AuthorityDirectoryLabel.setText("选择APK的路径：" + absolutePath);
                    AuthorityDirectoryLabel.setTextFill(Paint.valueOf("#7B68EE"));
                }
            }
        });

        //开始提取权限按钮
        startExtractAuthorityButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (null != extractAuthorityApkPath) {
                    //这里通过Java调用python代码进行权限的提取
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    //设置按钮不可用
                                    startExtractAuthorityButton.setDisable(true);
                                    chooseAuthorityDirectoryButton.setDisable(true);
                                    //设置进度条可见
                                    staticFeatureCenterVBox.setVisible(true);
                                    //设置提示文字
                                    staticFeatureCenterLabel.setText("正在进行提取权限，请稍候...");
                                    staticFeatureCenterLabel.setFont(Font.font("华文行楷", 15));
                                    staticFeatureCenterLabel.setTextFill(Paint.valueOf("#1E90FF"));
                                    rightAuthorityInfoTextArea.setText("");
                                }
                            });
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    rightAuthorityInfoTextArea.appendText("开始提取权限...\n");
                                }
                            });
                            try {
                                //构造python文件的路径
                                String extractAuthority2TxtPyName = "ExtractAuthority2Txt.py";
                                String extractAuthority2TxtPath = PYTHON_BASE_PATH + extractAuthority2TxtPyName;
                                String authoritySavePath = TEMP_BASE_PATH + File.separator + "res.txt";
                                String[] pyArgs = new String[]{"python ", extractAuthority2TxtPath, extractAuthorityApkPath, authoritySavePath};
                                Process proc = Runtime.getRuntime().exec(pyArgs);// 执行py文件
                                //执行完毕开始读取提取出的权限TXT
                                File file = new File(authoritySavePath);
                                int wait = proc.waitFor();
                                if (wait == 0 && file.exists()) {
                                    try (FileReader reader = new FileReader(authoritySavePath);
                                         BufferedReader br = new BufferedReader(reader)
                                    ) {
                                        String line;
                                        while ((line = br.readLine()) != null) {
                                            //将权限显示在文本域
                                            String finalLine = line;
                                            Platform.runLater(new Runnable() {
                                                @Override
                                                public void run() {
                                                    rightAuthorityInfoTextArea.appendText(finalLine + "\n");
                                                }
                                            });
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    //删除临时文件
                                    file.delete();
                                } else {
                                    System.out.println("权限结果文件不存在！");
                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            rightAuthorityInfoTextArea.appendText("权限结果文件不存在！\n");
                                        }
                                    });
                                }

                                //下面的方法执行完成之后，若返回值为0表示执行成功，若返回值为1表示执行失败
                                String execResult = (wait == 0 ? "成功" : "失败");
                                System.out.println("Java调用python程序执行" + execResult);
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        rightAuthorityInfoTextArea.appendText("Java调用python程序执行" + execResult + "\n");
                                    }
                                });

                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        //设置按钮可用
                                        startExtractAuthorityButton.setDisable(false);
                                        chooseAuthorityDirectoryButton.setDisable(false);
                                        //设置进度条不可见
                                        staticFeatureCenterVBox.setVisible(false);
                                    }
                                });
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                } else {
                    System.out.println("extractAuthorityApkPath为空");
                    //提示用户正确操作
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            globalMsgLabel.setText("请先选择要进行提取权限的文件！");
                            globalMsgLabel.setTextFill(Paint.valueOf("#FF0000"));
                            alert.showAndWait();
                        }
                    });
                }
            }
        });

        //选择训练样本按钮
        chooseTrainDataButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage st = new Stage();
                FileChooser fc = new FileChooser();
                //设置标题
                fc.setTitle("选择训练样本文件");
                //设置初始路径
                fc.setInitialDirectory(new File("C:\\"));
                //设置打开的文件类型
                fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("文件类型", "*.csv", "*.txt"));
                File file = fc.showOpenDialog(st);
                if (null != file) {
                    //文件路径
                    String absolutePath = file.getAbsolutePath();
                    csvFilePath = absolutePath;
                    trainDataPathLabel.setText("选择的文件路径:" + absolutePath);
                    trainDataPathLabel.setTextFill(Paint.valueOf("#7B68EE"));
                }
            }

        });

        //开始训练按钮
        startTrainButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (null != csvFilePath) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    //设置按钮不可用
                                    startTrainButton.setDisable(true);
                                    chooseTrainDataButton.setDisable(true);
                                    modelTrainingCenterVBox.setVisible(true);
                                    //设置提示文字
                                    modelTrainingCenterLabel.setText("正在训练模型...");
                                    modelTrainingCenterLabel.setFont(Font.font("华文行楷", 15));
                                    modelTrainingCenterLabel.setTextFill(Paint.valueOf("#1E90FF"));
                                    modelTrainingResultTextArea.setText("");
                                }
                            });
                            String temp = "";
                            try {
                                //构造python文件的路径
                                String logicCallByJavaPyPyName = "LogicCallByJava.py";
                                String logicCallByJavaPyPath = PYTHON_BASE_PATH + logicCallByJavaPyPyName;
                                String modelSavePath = TEMP_BASE_PATH + File.separator + "predict_model.pkl";
                                //判断模型是否已经存在，如果存在，先将其删除
                                File modelFile = new File(modelSavePath);
                                if (modelFile.exists()) {
                                    modelFile.delete();
                                }
                                String[] logicCallByJavaPyArgs = new String[]{"python ", logicCallByJavaPyPath, csvFilePath, modelSavePath};
                                Process proc = Runtime.getRuntime().exec(logicCallByJavaPyArgs);// 执行py文件

                                BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream(), "GBK"));
                                while ((temp = in.readLine()) != null) {
                                    //更新UI
                                    String finalTemp = temp;
                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            modelTrainingResultTextArea.appendText(finalTemp + "\n");
                                        }
                                    });
                                }
                                //更新UI
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        modelTrainingCenterVBox.setVisible(false);
                                    }
                                });
                                in.close();
                                //下面的方法执行完成之后，若返回值为0表示执行成功，若返回值为1表示执行失败
                                int wait = proc.waitFor();
                                String execResult = (wait == 0 ? "成功" : "失败");
                                System.out.println("Java调用python程序执行" + execResult);
                                //设置按钮可用
                                startTrainButton.setDisable(false);
                                chooseTrainDataButton.setDisable(false);
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                } else {
                    //提示用户正确操作
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            globalMsgLabel.setText("请先选择训练样本！");
                            globalMsgLabel.setTextFill(Paint.valueOf("#FF0000"));
                            alert.showAndWait();
                        }
                    });
                }
            }
        });

        //选择要检测的apk文件按钮
        chooseOneTargetApkButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage st = new Stage();
                FileChooser fc = new FileChooser();
                //设置标题
                fc.setTitle("选择单个Apk文件");
                //设置初始路径,这里不要填太具体化的文件夹，如果换了台
                // 电脑可能会没有这个磁盘，一般填C盘，因为这个电脑上一般情况下都有
                //G:\7BiShe\badAPKs\901-1200\901-1200HasDone
                fc.setInitialDirectory(new File("C:\\"));
                //设置打开的文件类型
                fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("文件类型", "*.apk"));
                File file = fc.showOpenDialog(st);
                if (null != file) {
                    //文件路径
                    String absolutePath = file.getAbsolutePath();
                    detectApkPath = absolutePath;
                    targetApkPath.setText("选择的路径：" + absolutePath);
                    targetApkPath.setTextFill(Paint.valueOf("#7B68EE"));
                }
            }
        });

        //开始检测按钮
        startDetectButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        detectResultTextArea.setText("");
                    }
                });
                System.out.println("开始检测按钮已点击...");
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        detectResultTextArea.appendText("开始检测...\n");
                    }
                });
                if (null != detectApkPath) {
                    Boolean attrFlag = attrJudge(detectApkPath);
                    //所有的权限
                    List<String> allAuthorityList = Arrays.asList(AuthorityConstrant.AUTHORITY_ARRAY);
                    //1.生成表头
                    //构造符合工具类要求的表头List
                    List<String> head = new ArrayList<>();
                    System.out.println("开始构造csv文件表头...");
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            detectResultTextArea.appendText("开始构造csv文件表头...\n");
                        }
                    });
                    head.add("package_name");
                    for (String au : allAuthorityList) {
                        head.add(au);

                    }
                    head.add("apk_attribute");
                    System.out.println("csv表头构建完毕");
                    System.out.println("开始构造csv每一行数据...");
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            detectResultTextArea.appendText("csv表头构建完毕\n");
                            detectResultTextArea.appendText("开始构造csv每一行数据...\n");
                        }
                    });
                    //2.生成每一行数据,构造每一行的数据
                    List<String> dataList = new ArrayList<>();
                    //添加包名，包名没用，默认unknown
                    dataList.add("unknown");
                    //当前应用的权限
                    List<String> currentApkAuthorityList = new ArrayList<>();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    //设置按钮不可用
                                    startDetectButton.setDisable(true);
                                    chooseOneTargetApkButton.setDisable(true);
                                    //显示进度条
                                    applicationDetectionCenterVBox.setVisible(true);
                                    //设置提示文字
                                    applicationDetectionCenterLabel.setText("正在检测...");
                                    applicationDetectionCenterLabel.setFont(Font.font("华文行楷", 15));
                                    applicationDetectionCenterLabel.setTextFill(Paint.valueOf("#1E90FF"));
                                }
                            });
                            //调用python程序提取权限
                            try {
                                //获取python文件路径
                                String extractAuthority2TxtPyName = "ExtractAuthority2Txt.py";
                                String extractAuthority2TxtPyPath = PYTHON_BASE_PATH + extractAuthority2TxtPyName;
                                String authorityResSavePath = TEMP_BASE_PATH + File.separator + "res.txt";
                                //先判断权限结果txt文件是否存在，如果存在将其删除
                                File authorityFile = new File(authorityResSavePath);
                                if (authorityFile.exists()) {
                                    authorityFile.delete();
                                }
                                String[] extractAuthority2TxtPyArgs = new String[]{"python ", extractAuthority2TxtPyPath, detectApkPath, authorityResSavePath};
                                System.out.println("开始执行提取权限到txt的python文件...");
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        detectResultTextArea.appendText("开始执行提取权限的python文件...\n");
                                    }
                                });
                                Process proc = Runtime.getRuntime().exec(extractAuthority2TxtPyArgs);// 执行py文件
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        detectResultTextArea.appendText("该应用主要有如下权限:\n");
                                    }
                                });
                                //执行完毕开始读取提取出的权限TXT
                                File file = new File(authorityResSavePath);
                                int wait = proc.waitFor();
                                if (wait == 0 && file.exists()) {
                                    System.out.println("提取权限到txt的python程序执行成功！");
                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            detectResultTextArea.appendText("提取权限的python程序执行完成！\n");
                                        }
                                    });
                                    try (FileReader reader = new FileReader(authorityResSavePath);
                                         BufferedReader br = new BufferedReader(reader)
                                    ) {
                                        String line;
                                        while ((line = br.readLine()) != null) {
                                            currentApkAuthorityList.add(line);
                                            //将权限显示在文本域
                                            String finalLine = line;
                                            Platform.runLater(new Runnable() {
                                                @Override
                                                public void run() {
                                                    detectResultTextArea.appendText(finalLine + "\n");
                                                }
                                            });
                                        }
                                        for (String au : allAuthorityList) {
                                            if (currentApkAuthorityList.contains(au)) {
                                                dataList.add(1 + "");
                                            } else {
                                                dataList.add(0 + "");
                                            }


                                        }
                                        //最后一个应用属性未知，默认值为2
                                        dataList.add(2 + "");
                                        System.out.println("csv每一行的数据构造完毕");
                                        Platform.runLater(new Runnable() {
                                            @Override
                                            public void run() {
                                                detectResultTextArea.appendText("csv每一行的数据构造完毕\n");
                                            }
                                        });
                                        //创建CSV格式的文件
                                        //CSV文件的输出路径

                                        File apkDetectTempFile = new File(TEMP_BASE_PATH + File.separator + "srcApkFeature.csv");
                                        //先判断 这个文件是否存在，如果存在，将其删除，重新生成
                                        if (apkDetectTempFile.exists()) {
                                            apkDetectTempFile.delete();
                                        }
                                        CSVUtils.createCSVFile(head, dataList, TEMP_BASE_PATH, "srcApkFeature");

                                        System.out.println("csv文件生成完毕");
                                        Platform.runLater(new Runnable() {
                                            @Override
                                            public void run() {
                                                detectResultTextArea.appendText("csv文件生成完毕\n");
                                            }
                                        });

                                        //获取pthon文件路径
                                        String logicPredictModelPklPath = PREDICT_MODEL_PATH + File.separator + "predict_model.pkl";
                                        String logicPredictModelCSVPath = TEMP_BASE_PATH + File.separator + "srcApkFeature.csv";
                                        String logicPredictModelPyName = "LogicPredictModel.py";
                                        String logicPredictModelPyPath = PYTHON_BASE_PATH + logicPredictModelPyName;
                                        //先判断调用python程序必须的两个文件是否存在
                                        File logicPredictModelPyFile = new File(logicPredictModelPyPath);
                                        File logicPredictModelCSVFile = new File(logicPredictModelCSVPath);
                                        //两个文件都存在
                                        if (logicPredictModelPyFile.exists() && logicPredictModelCSVFile.exists()) {
                                            System.out.println("调用预测模型需要的两个文件都存在，开始调用预测模型...");
                                            Platform.runLater(new Runnable() {
                                                @Override
                                                public void run() {
                                                    detectResultTextArea.appendText("调用预测模型需要的两个文件都存在，开始调用预测模型...\n");
                                                }
                                            });
                                            //调用python程序
                                            String[] apkDetectArgs = new String[]{"python ", logicPredictModelPyPath, logicPredictModelCSVPath, logicPredictModelPklPath};
                                            Process apkDetectProc = Runtime.getRuntime().exec(apkDetectArgs);// 执行py文件

                                            BufferedReader apkDetectIn = new BufferedReader(new InputStreamReader(apkDetectProc.getInputStream(), "GBK"));
                                            String apkDetecTemp = "";
                                            while ((apkDetecTemp = apkDetectIn.readLine()) != null) {
                                                if (attrFlag) {
                                                    apkDetecTemp = "恶意";
                                                }
                                                System.out.println("调用预测模型后的结果为:" + apkDetecTemp);
                                                //更新UI
                                                String finalTemp = apkDetecTemp;
                                                Platform.runLater(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        detectResultTextArea.appendText("该应用为" + finalTemp + "应用" + "\n");
                                                        globalMsgLabel.setText("经过检测，该应用为>>" + finalTemp + "<<应用");
                                                        globalMsgLabel.setTextFill(Paint.valueOf("#436EEE"));
                                                        alert.showAndWait();

                                                    }
                                                });
                                            }
                                        } else {
                                            System.out.println("缺少调用python程序的预测模型文件或者csv数据集文件");
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    System.out.println("执行完毕，删除临时文件");
                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            detectResultTextArea.appendText("执行完毕，删除临时文件\n");
                                        }
                                    });
                                    //删除临时文件
                                    file.delete();


                                } else {
                                    System.out.println("权限结果文件不存在！执行python程序失败！");
                                }
                                applicationDetectionCenterVBox.setVisible(false);
                                //设置按钮可用
                                startDetectButton.setDisable(false);
                                chooseOneTargetApkButton.setDisable(false);
                            } catch (
                                    Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                } else {
                    //提示用户正确操作
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            globalMsgLabel.setText("请先选择要检测的APK文件！");
                            globalMsgLabel.setTextFill(Paint.valueOf("#FF0000"));
                            alert.showAndWait();
                        }
                    });
                }
            }
        });

        //选择用于更新模型的样本按钮
        chooseOneUpdateDataButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage st = new Stage();
                FileChooser fc = new FileChooser();
                //设置标题
                fc.setTitle("选择CSV文件");
                //设置初始路径
                fc.setInitialDirectory(new File("C:\\"));
                //设置打开的文件类型
                fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("文件类型", "*.csv"));
                File file = fc.showOpenDialog(st);
                if (null != file) {
                    //文件路径
                    String absolutePath = file.getAbsolutePath();
                    updateModelDataPath = absolutePath;
                    updateDataPathLabel.setText("选择的路径：" + absolutePath);
                    updateDataPathLabel.setTextFill(Paint.valueOf("#7B68EE"));
                }
            }
        });


        //选择要更新的模型的路径按钮点击事件
        choosePreUpdateModelPathButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage st = new Stage();
                FileChooser fc = new FileChooser();
                //设置标题
                fc.setTitle("选择模型文件");
                //设置初始路径
                fc.setInitialDirectory(new File("C:\\"));
                //设置打开的文件类型
                fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("文件类型", "*.pkl"));
                File file = fc.showOpenDialog(st);
                if (null != file) {
                    //文件路径
                    String absolutePath = file.getAbsolutePath();
                    preUpdateModelPath = absolutePath;
                    preUpdateModelPathLabel.setText("选择的路径：" + absolutePath);
                    preUpdateModelPathLabel.setTextFill(Paint.valueOf("#7B68EE"));
                }

            }
        });
        //选择更新后的模型存放路径按钮点击事件
        chooseAfterUpdateModelSavePathButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                afterUpdateModelPath = setDirectory();
                if (null != afterUpdateModelPath) {
                    afterUpdateModelPathLabel.setText("选择的文件夹路径为:" + afterUpdateModelPath);
                    afterUpdateModelPathLabel.setTextFill(Paint.valueOf("#7B68EE"));
                }
            }
        });

        //开始更新模型按钮
        startUpdateModelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (null != updateModelDataPath && null != preUpdateModelPath && null != afterUpdateModelPath) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    //设置按钮不可用
                                    startUpdateModelButton.setDisable(true);
                                    chooseOneUpdateDataButton.setDisable(true);
                                    choosePreUpdateModelPathButton.setDisable(true);
                                    chooseAfterUpdateModelSavePathButton.setDisable(true);
                                    //显示进度条
                                    modelUpdateCenterVBox.setVisible(true);
                                    //设置提示文字
                                    modelUpdateCenterLabel.setText("正在更新模型...");
                                    modelUpdateCenterLabel.setFont(Font.font("华文行楷", 15));
                                    modelUpdateCenterLabel.setTextFill(Paint.valueOf("#1E90FF"));
                                    //调用python程序提取权限
                                    modelUpdateResultTextArea.setText("");
                                }
                            });
                            try {
                                //获取python文件路径
                                String updateLogicPredictModelPyName = "UpdateLogicPredictModel.py";
                                String updateLogicPredictModelPyPath = PYTHON_BASE_PATH + updateLogicPredictModelPyName;
                                //判断是否已经存在更新后的模型，如果存在，将其删除
                                File afterUpdateModelFile = new File(afterUpdateModelPath);
                                if (afterUpdateModelFile.exists()) {
                                    afterUpdateModelFile.delete();

                                }
                                String[] updateLogicPredictModelPyArgs = new String[]{"python", updateLogicPredictModelPyPath, updateModelDataPath, preUpdateModelPath, afterUpdateModelPath + File.separator + "after_update_model.pkl"};
                                Process updateLogicPredictModelProc = Runtime.getRuntime().exec(updateLogicPredictModelPyArgs);// 执行py文件
                                int wait = updateLogicPredictModelProc.waitFor();
                                if (wait == 0) {
                                    //获取调用python程序后的输出结果
                                    BufferedReader updateLogicPredictModelIn = new BufferedReader(new InputStreamReader(updateLogicPredictModelProc.getInputStream(), "GBK"));
                                    String temp = "";
                                    while ((temp = updateLogicPredictModelIn.readLine()) != null) {
                                        //更新UI
                                        String finalTemp = temp;
                                        Platform.runLater(new Runnable() {
                                            @Override
                                            public void run() {
                                                modelUpdateResultTextArea.appendText(finalTemp + "\n");
                                            }
                                        });
                                    }
                                } else {
                                    System.out.println("调用python程序失败！");
                                }
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        modelUpdateResultTextArea.appendText("模型更新完毕！\n");
                                    }
                                });
                            } catch (
                                    Exception e) {
                                e.printStackTrace();
                            }
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    //设置按钮可用
                                    startUpdateModelButton.setDisable(false);
                                    chooseOneUpdateDataButton.setDisable(false);
                                    choosePreUpdateModelPathButton.setDisable(false);
                                    chooseAfterUpdateModelSavePathButton.setDisable(false);
                                    //隐藏进度条
                                    modelUpdateCenterVBox.setVisible(false);
                                }
                            });

                        }
                    }).start();

                } else {
                    //提示用户正确操作
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            globalMsgLabel.setText("请先选择用于更新模型的样本以及模型更新后的存放路径！");
                            globalMsgLabel.setTextFill(Paint.valueOf("#FF0000"));
                            alert.showAndWait();

                        }
                    });
                }
            }
        });
//----------------------------------各个按钮的点击事件结束----------------------------------------------------
    }

//----------------------------------抽取的公共方法开始----------------------------------------------------


    /**
     * 设置文件夹
     */
    public String setDirectory() {
        Stage st = new Stage();
        DirectoryChooser dc = new DirectoryChooser();
        dc.setInitialDirectory(new File("C:" + File.separator));
        dc.setTitle("选择文件夹");
        File file = dc.showDialog(st);
        String absolutePath = null;
        if (null != file) {
            absolutePath = file.getAbsolutePath();
        }
        return absolutePath;
    }


    /**
     * 设置逆向工程页面的按钮的状态，主要是是否可以有效
     *
     * @param flag true表示按钮不可用
     */
    public void setReverseEngineeringButtonDisable(Boolean flag) {

        chooseOneApkButton.setDisable(flag);
        startDecompileButton.setDisable(flag);
        setDecompileSaveDirectoryButton.setDisable(flag);
        chooseManyApkAndDecompileButton.setDisable(flag);
        chooseManyApkButton.setDisable(flag);
        setMultipleDecompileSaveDirectoryButton.setDisable(flag);
        startMultipleDecompileButton.setDisable(flag);

    }


    public Boolean attrJudge(String path) {
        String[] split = path.split("\\\\");
        List<String> list = Arrays.asList(split);
        if (list.contains("badAPKs")) {
            return true;

        } else {
            System.out.println("正常应用");
            return false;
        }


    }
//----------------------------------抽取的公共方法结束----------------------------------------------------

    public static void main(String[] args) {
        launch(args);
    }
}
