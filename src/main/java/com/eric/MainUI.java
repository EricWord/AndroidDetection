package com.eric;

import com.eric.service.AuthorityService;
import com.eric.service.DeCompileService;
import com.eric.tools.decode.APKTool;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTabPane;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.util.List;
import java.util.Locale;

/*
 *@description:应用程序主界面
 *@author:cuigs
 *@Email:cuigs@ti-net.com.cn
 *@Version:1.0
 *@Date:2019/4/8
 */
@Component
public class MainUI extends Application {
    @Resource
    private DeCompileService deCompileService;
    @Resource
    private AuthorityService authorityService;
    public static MainUI mainUI;

    @PostConstruct
    public void init() {
        mainUI = this;

    }

    //单个Apk文件路径
    private String singleApkPath;
    //多个Apk文件夹路径
    private String multipleApkDirectoryPath;
    //反编译结果存放文件夹
    private String decompileResultSavePath;
    //批量反编译结果存放文件夹
//    private String batchDecomplieResultSavePath;
    //权限提取选择的文件夹路径
    private String authorityDirectory;
    //要检测的Apk所在的路径
    private String detectApkPath;
    //用于更新模型的样本所在的路径
    private String updateModelDataPath;

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
        stage.getIcons().add(new Image("file:E:\\projects\\AndroidDetection\\src\\main\\java\\images\\detectIcon.png"));
//        TabPane tabPane = new TabPane();
        JFXTabPane tabPane = new JFXTabPane();
        tabPane.setPrefHeight(750);
        //首页Tab 默认
        Tab reverseEngineeringTab = new Tab("逆向工程");
        Tab StaticFeatureExtractionTab = new Tab("静态特征提取");
        Tab modelTrainingTab = new Tab("模型训练");
        Tab applicationDetectionTab = new Tab("应用检测");
        Tab ModelUpdatingTab = new Tab("模型更新");
        tabPane.getTabs().addAll(reverseEngineeringTab, StaticFeatureExtractionTab, modelTrainingTab, applicationDetectionTab, ModelUpdatingTab);
        SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
        selectionModel.select(0);

        //设置不可关闭
        reverseEngineeringTab.setClosable(false);
        StaticFeatureExtractionTab.setClosable(false);
        modelTrainingTab.setClosable(false);
        applicationDetectionTab.setClosable(false);
        ModelUpdatingTab.setClosable(false);
        //给tab设置图标
        an.getChildren().add(tabPane);


//----------------------------------设置各个Tab的内容开始----------------------------------------------------
        //设置逆向工程Tab的内容
        BorderPane reverseEngineeringBorderPane = new BorderPane();
        reverseEngineeringBorderPane.setPrefSize(700, 1300);
        //左侧部分的内容
        VBox reverseEngineeringLeftVBox = new VBox();
        //单个Apk文件反编译Label
        Label singelApkDecompileLabel = new Label("-------------------单个Apk文件反编译-------------------");
        //选择单个APK文件按钮
        JFXButton chooseOneApkButton = new JFXButton("选择单个Apk文件");
        chooseOneApkButton.getStyleClass().add("button-raised");
        //单个apk文件的路径
        Label singleApkPathLabel = new Label();
        //设置反编译结果存放文件夹按钮
        JFXButton setDecompileSaveDirectoryButton = new JFXButton("设置反编译结果存放文件夹");
        setDecompileSaveDirectoryButton.getStyleClass().add("button-raised");
        //反编译结果存放文件夹路径
        Label decompileResultSaveDirectoryPathLabel = new Label();
        //开始反编译按钮
        JFXButton startDecompileButton = new JFXButton("开始反编译");
        startDecompileButton.getStyleClass().add("button-raised");

        //多个Apk文件反编译Label
        Label multipleApkDecompileLabel = new Label("-------------------多个Apk文件反编译-------------------");


        //选择多个APK文件按钮
        JFXButton chooseManyApkAndDecompileButton = new JFXButton("选择多个Apk文件并反编译");
        chooseManyApkAndDecompileButton.getStyleClass().add("button-raised");
        JFXButton chooseManyApkButton = new JFXButton("选择多个Apk文件所在文件夹");
        chooseManyApkButton.getStyleClass().add("button-raised");
        //多个Apk文件的路径
        Label multipleApkPathLabel = new Label();
        //设置反编译结果存放文件夹按钮
        JFXButton setMultipleDecompileSaveDirectoryButton = new JFXButton("设置批量反编译结果存放文件夹");
        setMultipleDecompileSaveDirectoryButton.getStyleClass().add("button-raised");
        Label multipleDecompileSavePathLabel = new Label();
        //开始批量反编译按钮
        JFXButton startMultipleDecompileButton = new JFXButton("开始批量反编译");
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
        rightDecompileInfoTextArea.setPrefRowCount(35);
        reverseEngineeringRightVBox.getChildren().addAll(rightDecompileInfoLabel, rightDecompileInfoTextArea);
        //设置内容居中
        reverseEngineeringRightVBox.setAlignment(Pos.TOP_CENTER);
        //将左侧内容添加到布局
        reverseEngineeringBorderPane.setLeft(reverseEngineeringLeftVBox);
        //将右侧内容添加到布局
        reverseEngineeringBorderPane.setRight(reverseEngineeringRightVBox);
        BorderPane.setMargin(reverseEngineeringLeftVBox, new Insets(30, 80, 500, 100));
        BorderPane.setMargin(reverseEngineeringRightVBox, new Insets(45, 100, 500, 80));

        reverseEngineeringTab.setContent(reverseEngineeringBorderPane);


        //----------------静态特征提取Tab的内容
        BorderPane staticFeatureBorderPane = new BorderPane();
        staticFeatureBorderPane.setPrefSize(750, 1400);
        //左侧部分的内容
        VBox staticFeatureLeftVBox = new VBox();
        //选择权限特征文件夹按钮
        JFXButton chooseAuthorityDirectoryButton = new JFXButton("选择要提取权限特征的文件夹");
        chooseAuthorityDirectoryButton.getStyleClass().add("button-raised");
        //权限特征所文件夹的路径
        Label AuthorityDirectoryLabel = new Label();
        //开始提取权限按钮
        JFXButton startExtractAuthorityButton = new JFXButton("开始提取权限特征");
        startExtractAuthorityButton.getStyleClass().add("button-raised");
        //将上述按钮添加到VBox
        staticFeatureLeftVBox.getChildren().addAll(chooseAuthorityDirectoryButton, AuthorityDirectoryLabel, startExtractAuthorityButton);
        //设置左侧VBox中按钮之间的间距
        staticFeatureLeftVBox.setSpacing(15);

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
        BorderPane.setMargin(staticFeatureLeftVBox, new Insets(80, 80, 500, 100));
        BorderPane.setMargin(staticFeatureRightVBox, new Insets(45, 100, 500, 80));
        StaticFeatureExtractionTab.setContent(staticFeatureBorderPane);

        //--------模型训练Tab内容
        BorderPane modelTrainingBorderPane = new BorderPane();
        //设置大小
        modelTrainingBorderPane.setPrefSize(750, 1400);
        //选择训练样本按钮
        JFXButton chooseTrainDataButton = new JFXButton("选择训练样本");
        chooseTrainDataButton.getStyleClass().add("button-raised");
        //训练样本所在路径
        Label trainDataPathLabel = new Label();
        //开始训练按钮
        JFXButton startTrainButton = new JFXButton("开始训练");
        startTrainButton.getStyleClass().add("button-raised");
        VBox modelTrainingLeftVBox = new VBox();
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
        modelTrainingLeftVBox.getChildren().addAll(chooseTrainDataButton, trainDataPathLabel, startTrainButton);
        modelTrainingLeftVBox.setAlignment(Pos.TOP_LEFT);
//        modelTrainingLeftVBox.setPadding(new Insets(80, 80, 500, 100));


        modelTrainingRightVBox.getChildren().addAll(modelTrainingResultLabel, modelTrainingResultTextArea);
        modelTrainingRightVBox.setAlignment(Pos.TOP_CENTER);
        modelTrainingRightVBox.setSpacing(15);


//        modelTrainingBorderPane.getChildren().addAll(modelTrainingLeftVBox,modelTrainingRightVBox);
        BorderPane.setMargin(modelTrainingLeftVBox, new Insets(45, 100, 500, 80));
        BorderPane.setMargin(modelTrainingRightVBox, new Insets(45, 100, 500, 80));
        modelTrainingBorderPane.setLeft(modelTrainingLeftVBox);
        modelTrainingBorderPane.setRight(modelTrainingRightVBox);
        modelTrainingTab.setContent(modelTrainingBorderPane);


        //------------------------应用检测Tab按钮
        BorderPane applicationDetectionBorderPane = new BorderPane();
        applicationDetectionBorderPane.setPrefSize(750, 1400);
        //左侧
        VBox applicationDetectionLeftVBox = new VBox();

        //右侧
        VBox applicationDetectionRightVBox = new VBox();
        //选择要检测的apk文件按钮
        JFXButton chooseOneTargetApkButton = new JFXButton("选择要检测的Apk文件");
        chooseOneTargetApkButton.getStyleClass().add("button-raised");
        //要检测的Apk文件的路径
        Label targetApkPath = new Label();
        //开始检测按钮
        JFXButton startDetectButton = new JFXButton("开始检测");
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
        applicationDetectionLeftVBox.setAlignment(Pos.TOP_LEFT);
        //将上述按钮添加到左侧VBox
        applicationDetectionLeftVBox.getChildren().addAll(chooseOneTargetApkButton, targetApkPath, startDetectButton);
        applicationDetectionRightVBox.setSpacing(15);
        applicationDetectionRightVBox.setAlignment(Pos.TOP_CENTER);
        //将标签和TextArea加入到右侧VBox中
        applicationDetectionRightVBox.getChildren().addAll(detectResultLabel, detectResultTextArea);
        //左右两侧内容加入到面板
        applicationDetectionBorderPane.setLeft(applicationDetectionLeftVBox);
        applicationDetectionBorderPane.setRight(applicationDetectionRightVBox);
        BorderPane.setMargin(applicationDetectionLeftVBox, new Insets(80, 80, 500, 100));
        BorderPane.setMargin(applicationDetectionRightVBox, new Insets(45, 100, 500, 80));
        applicationDetectionTab.setContent(applicationDetectionBorderPane);

        //-----------------------------------模型更新Tab内容
        BorderPane modelUpdateBorderPane = new BorderPane();
        modelUpdateBorderPane.setPrefSize(750, 1400);
        //左侧
        VBox modelUpdateLeftVBox = new VBox();
        modelUpdateLeftVBox.setPadding(new Insets(80, 80, 500, 100));
        modelUpdateLeftVBox.setSpacing(15);
        //右侧
        VBox modelUpdateRightVBox = new VBox();
        modelUpdateRightVBox.setSpacing(15);
        modelUpdateRightVBox.setPadding(new Insets(45, 100, 500, 80));
        modelUpdateRightVBox.setAlignment(Pos.TOP_CENTER);
        //选择用于更新模型的样本按钮
        JFXButton chooseOneUpdateDataButton = new JFXButton("选择用于更新模型的样本");
        chooseOneUpdateDataButton.getStyleClass().add("button-raised");
        //用于更新模型的样本所在的路径
        Label updateDataPathLabel = new Label();
        //开始更新模型按钮
        JFXButton startUpdateModelButton = new JFXButton("开始更新模型");
        startUpdateModelButton.getStyleClass().add("button-raised");
        //将上述按钮添加到左侧的VBox
        modelUpdateLeftVBox.getChildren().addAll(chooseOneUpdateDataButton, updateDataPathLabel, startUpdateModelButton);
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
        ModelUpdatingTab.setContent(modelUpdateBorderPane);
//----------------------------------设置各个Tab的内容结束----------------------------------------------------
        //显示舞台
        stage.show();
        //设置不可变
        stage.setResizable(false);
        tabPane.setPrefWidth(an.getWidth());
        //设置选中哪个Tab 默认显示逆向工程Tab
        tabPane.getSelectionModel().select(reverseEngineeringTab);
        //设置tabPane的背景颜色
        tabPane.setStyle("-fx-background-color: #F5FFFA");

//----------------------------------变化监听开始----------------------------------------------------

        //监听选择了哪个tab
        tabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
                System.out.println(newValue.getText());
            }
        });
        //tab被选中时事件监听
        reverseEngineeringTab.setOnSelectionChanged(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                Tab t = (Tab) event.getSource();
                System.out.println("tab1的选择状态是：" + reverseEngineeringTab.isSelected());
            }

        });
        tabPane.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                tabPane.setPrefHeight(an.getWidth());

            }
        });
//----------------------------------变化监听结束----------------------------------------------------
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
                fc.setInitialDirectory(new File("E:\\BiSheData"));
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
                DateTime dateTime = new DateTime();
                //当前时间
                String stringDate = dateTime.toString("yyyy_MM_dd_HH_mm_ss", Locale.CHINESE);
                //路径不为空
                if (null != singleApkPath && null != decompileResultSavePath) {
                    rightDecompileInfoTextArea.setText("");
                    rightDecompileInfoTextArea.appendText("开始反编译.....\n");
                    rightDecompileInfoTextArea.appendText("正在反编译.....\n");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            APKTool.decode(singleApkPath, decompileResultSavePath + File.separator + "DecompileResults_" + stringDate);
                        }
                    }).start();

                } else if (null == singleApkPath && null != decompileResultSavePath) {
                   /* tipsLabel.setText("请先选择要反编译的Apk文件再点击反编译按钮哦！");
                    tipsLabel.setTextFill(Paint.valueOf("#FF0000"));*/

                } else if (null != singleApkPath && null == decompileResultSavePath) {
                   /* tipsLabel.setText("请先选择反编译结果存放文件夹再点击反编译按钮哦！");
                    tipsLabel.setTextFill(Paint.valueOf("#FF0000"));*/

                } else {
                    /*tipsLabel.setText("请先选择要反编译的Apk文件和反编译结果存放文件夹再点击反编译按钮哦！");
                    tipsLabel.setTextFill(Paint.valueOf("#FF0000"));*/

                }
            }
        });

        //选择多个Apk文件并反编译按钮事件监听
        chooseManyApkAndDecompileButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //下面设置其他不相关按钮不可用
                setButtonDisble(true, chooseOneApkButton, setDecompileSaveDirectoryButton, startDecompileButton, chooseManyApkAndDecompileButton, chooseManyApkButton, setMultipleDecompileSaveDirectoryButton, startMultipleDecompileButton);
                //当前时间
                DateTime dateTime = new DateTime();
                String stringDate = dateTime.toString("yyyy_MM_dd_HH_mm_ss", Locale.CHINESE);
                Stage st = new Stage();
                FileChooser fc = new FileChooser();
                //设置标题
                fc.setTitle("选择多个Apk文件");
                //设置初始路径
                fc.setInitialDirectory(new File("E:\\BiSheData"));
                //设置打开的文件类型
                fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("文件类型", "*.apk"));
                List<File> files = fc.showOpenMultipleDialog(st);
                if (null != files) {
                    for (File file : files) {
                        //获取文件路径
                        String path = file.getAbsolutePath();
                        if (null != path) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {


                                    rightDecompileInfoTextArea.setText("");
                                    rightDecompileInfoTextArea.appendText("开始批量反编译...\n");
                                    rightDecompileInfoTextArea.appendText("正在进行批量反编译，预计耗时36分11秒...\n");
                                    APKTool.decode(path, decompileResultSavePath + File.separator + "DecompileResults_" + stringDate);

                                }
                            }, "批量反编译Apk线程").start();
                        } else {
                            /*tipsLabel.setText("请先选择反编译结果存放文件夹再点击反编译按钮哦！");
                            tipsLabel.setTextFill(Paint.valueOf("#FF0000"));*/

                        }

                    }
                }
                rightDecompileInfoTextArea.appendText("反编译完成!");
                //下面设置其他按钮可用
                setButtonDisble(false, chooseOneApkButton, setDecompileSaveDirectoryButton, startDecompileButton, chooseManyApkAndDecompileButton, chooseManyApkButton, setMultipleDecompileSaveDirectoryButton, startMultipleDecompileButton);


            }

            public void setButtonDisble(boolean b, Button chooseOneApkButton, Button setDecompileSaveDirectoryButton, Button startDecompileButton, Button chooseManyApkAndDecompileButton, Button chooseManyApkButton, Button setMultipleDecompileSaveDirectoryButton, Button startMultipleDecompileButton) {
                chooseOneApkButton.setDisable(b);
                setDecompileSaveDirectoryButton.setDisable(b);
                startDecompileButton.setDisable(b);
                chooseManyApkAndDecompileButton.setDisable(b);
                chooseManyApkButton.setDisable(b);
                setMultipleDecompileSaveDirectoryButton.setDisable(b);
                startMultipleDecompileButton.setDisable(b);
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
                    decompileResultSaveDirectoryPathLabel.setTextFill(Paint.valueOf("#7B68EE"));
                }


            }
        });

        //设置批量反编译结果存放文件夹按钮
        setMultipleDecompileSaveDirectoryButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                    multipleDecompileSavePathLabel.setText("选择的文件夹路径为:" + setDirectory());
                    multipleDecompileSavePathLabel.setTextFill(Paint.valueOf("#7B68EE"));

            }
        });

        //开始批量反编译按钮
        startMultipleDecompileButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (null != multipleApkDirectoryPath && null != decompileResultSavePath) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            rightDecompileInfoTextArea.setText("");
                            rightDecompileInfoTextArea.appendText("开始反编译....\n");
                            rightDecompileInfoTextArea.appendText("正在反编译反编译，预计需要9分40秒....\n");
                            mainUI.deCompileService.batchDeCompile(multipleApkDirectoryPath, decompileResultSavePath);
                            rightDecompileInfoTextArea.appendText("反编译完成！\n");
                        }
                    }, "批量反编译线程").start();
                }
            }
        });

        //选择权限特征文件夹按钮
        chooseAuthorityDirectoryButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                authorityDirectory = setDirectory();
                if (null != authorityDirectory) {
                    AuthorityDirectoryLabel.setText("选择的路径：" + authorityDirectory);
                    AuthorityDirectoryLabel.setTextFill(Paint.valueOf("#7B68EE"));
                }

            }
        });

        //开始批量提取权限按钮
        startExtractAuthorityButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (null != authorityDirectory) {
                    rightAuthorityInfoTextArea.setText("");
                    rightAuthorityInfoTextArea.appendText("开始提取权限...\n");
                    rightAuthorityInfoTextArea.appendText("正在提取权限,预计耗时8分10秒...\n");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            mainUI.authorityService.saveAuthority(authorityDirectory, 0);

                            rightAuthorityInfoTextArea.appendText("权限提取完成！\n");
                        }
                    }, "提取权限线程").start();


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
                fc.setTitle("选择单个Apk文件");
                //设置初始路径
                fc.setInitialDirectory(new File("E:\\BiSheData"));
                //设置打开的文件类型
                fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("文件类型", "*.apk"));
                File file = fc.showOpenDialog(st);
                if (null != file) {
                    //文件路径
                    String absolutePath = file.getAbsolutePath();
                    singleApkPath = absolutePath;
                    trainDataPathLabel.setText(absolutePath);
                    trainDataPathLabel.setTextFill(Paint.valueOf("#7B68EE"));

                }
            }

        });

        //开始训练按钮
        startTrainButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("开始训练按钮");
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
                //设置初始路径
                fc.setInitialDirectory(new File("E:\\BiSheData"));
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
                System.out.println("开始检测按钮");
            }
        });

        //选择用于更新模型的样本按钮
        chooseOneUpdateDataButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage st = new Stage();
                FileChooser fc = new FileChooser();
                //设置标题
                fc.setTitle("选择单个Apk文件");
                //设置初始路径
                fc.setInitialDirectory(new File("E:\\BiSheData"));
                //设置打开的文件类型
                fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("文件类型", "*.apk"));
                File file = fc.showOpenDialog(st);
                if (null != file) {
                    //文件路径
                    String absolutePath = file.getAbsolutePath();
                    detectApkPath = absolutePath;
                    updateDataPathLabel.setText("选择的路径：" + absolutePath);
                    updateDataPathLabel.setTextFill(Paint.valueOf("#7B68EE"));

                }


            }
        });

        //开始更新模型按钮
        startUpdateModelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("开始更新模型按钮");
            }
        });


//----------------------------------各个按钮的点击事件结束----------------------------------------------------

    }

    /**
     * 设置文件夹
     */
    public String setDirectory() {
        Stage st = new Stage();
        DirectoryChooser dc = new DirectoryChooser();
        dc.setInitialDirectory(new File("E:" + File.separator+"BiSheData"));
        dc.setTitle("选择文件夹");
        File file = dc.showDialog(st);
        String absolutePath = null;
        if (null != file) {
            absolutePath = file.getAbsolutePath();
        }
        return absolutePath;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
