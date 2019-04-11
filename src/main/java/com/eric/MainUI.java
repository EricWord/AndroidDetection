package com.eric;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/*
 *@description:应用程序主界面
 *@author:cuigs
 *@Email:cuigs@ti-net.com.cn
 *@Version:1.0
 *@Date:2019/4/8
 */
public class MainUI extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        //绝对布局
        AnchorPane an = new AnchorPane();
        //给布局设置一个背景颜色
        an.setStyle("-fx-background-color: #FFEFDB");
        Scene scene = new Scene(an);
        //将场景添加到舞台
        stage.setScene(scene);
        //设置舞台的宽高
        stage.setHeight(780);
        stage.setWidth(1400);
        //设置标题
        stage.setTitle("基于在线学习的恶意Android应用检测系统");
        //设置左上角的图标
        stage.getIcons().add(new Image("file:D:\\cgs\\Projects\\AndroidDetection\\src\\main\\java\\images\\detectIcon.png"));
        TabPane tabPane = new TabPane();
        tabPane.setPrefHeight(300);
        Tab reverseEngineeringTab = new Tab("逆向工程");
        Tab StaticFeatureExtractionTab = new Tab("静态特征提取");
        Tab modelTrainingTab = new Tab("模型训练");
        Tab applicationDetectionTab = new Tab("应用检测");
        Tab ModelUpdatingTab = new Tab("模型更新");
        tabPane.getTabs().addAll(reverseEngineeringTab, StaticFeatureExtractionTab, modelTrainingTab, applicationDetectionTab, ModelUpdatingTab);

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
        //左侧部分的内容
        VBox reverseEngineeringLeftVBox = new VBox();
        //选择单个APK文件按钮
        Button chooseOneApkButton = new Button("选择单个Apk文件");
        //设置反编译结果存放文件夹按钮
        Button setDecompileSaveDirectoryButton = new Button("设置反编译结果存放文件夹");
        //开始反编译按钮
        Button startDecompileButton = new Button("开始反编译");
        //将上述按钮添加到VBox
        reverseEngineeringLeftVBox.getChildren().addAll(chooseOneApkButton,setDecompileSaveDirectoryButton,startDecompileButton);


        //右侧部分
        VBox reverseEngineeringRightVBox =new VBox();
        //右侧表头
        Label rightDecompileInfoLabel = new Label("反编译日志信息");
        //右侧文本域
        TextArea rightDecompileInfoTextArea = new TextArea("反编译信息");
        reverseEngineeringRightVBox.getChildren().addAll(rightDecompileInfoLabel,rightDecompileInfoTextArea);

        //将左侧内容添加到布局
        reverseEngineeringBorderPane.setLeft(reverseEngineeringLeftVBox);
        //将右侧内容添加到布局
        reverseEngineeringBorderPane.setRight(reverseEngineeringRightVBox);
        BorderPane.setMargin(reverseEngineeringLeftVBox,new Insets(30,200,500,30));
        BorderPane.setMargin(reverseEngineeringRightVBox,new Insets(30,30,500,200));



        reverseEngineeringTab.setContent(reverseEngineeringBorderPane);
        StaticFeatureExtractionTab.setContent(new Button("tab2上的按钮"));
        modelTrainingTab.setContent(new Button("tab3上的按钮"));


//----------------------------------设置各个Tab的内容结束----------------------------------------------------


//显示舞台
        stage.show();
        //设置不可变
        stage.setResizable(false);
        tabPane.setPrefWidth(an.getWidth());
        //设置选中哪个
        tabPane.getSelectionModel().select(StaticFeatureExtractionTab);
        //设置tabPane的背景颜色
        tabPane.setStyle("-fx-background-color: #F5FFFA");


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

        //如何动态添加tab
      /*  an.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                tabPane.getTabs().add(new Tab("hello"));
            }
        });*/

        tabPane.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                tabPane.setPrefHeight(an.getWidth());

            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
