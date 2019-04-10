package com.eric;

import com.eric.fxmlController.MainUIController;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/*
 *@description:应用程序主界面
 *@author:cuigs
 *@Email:cuigs@ti-net.com.cn
 *@Version:1.0
 *@Date:2019/4/8
 */
public class MainUI extends Application implements EventHandler {

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader();
        URL url = loader.getClassLoader().getResource("fxml/MainUI.fxml");
//        Parent root = loader.load(getClass().getClassLoader().getResource("fxml/MainUI.fxml"));
        loader.setLocation(url);
        BorderPane root = (BorderPane) loader.load();
        //获得控制器
        MainUIController mainUIController = (MainUIController) loader.getController();
        //获取单一APK文件反编译按钮
        Button singleApkDecompileButton = mainUIController.getSingleApkDecompile();
        //获取批量反编译APK按钮
        Button multipleApkDecompileButton = mainUIController.getMultipleApkDecompile();
        //获取权限特征提取按钮
        Button authorityExtractButton = mainUIController.getAuthorityExtract();
        //获取API特征提取按钮
        Button apiExtractButton = mainUIController.getApiExtract();
        //获取在线学习算法按钮
        Button onlineLearningAlgorithmButton = mainUIController.getOnlineLearningAlgorithm();
        //获取其他算法按钮
        Button otherAlgorithmButton = mainUIController.getOtherAlgorithm();
        //获取单一应用检测按钮
        Button singleApkDetectionButton = mainUIController.getSingleApkDetection();
        //获取批量应用检测按钮
        Button multipleApkDetectionButton = mainUIController.getMultipleApkDetection();
        //获取在线更新按钮
        Button onlineUpdateButton = mainUIController.getOnlineUpdate();
        //获取数据对比按钮
        Button compareDataButton = mainUIController.getCompareData();


        Scene scene = new Scene(root, 1200, 750);
        stage.setTitle("基于在线学习的恶意Android应用检测系统");
        //设置icon
        stage.getIcons().add(new Image("file:D:\\cgs\\Projects\\AndroidDetection\\src\\main\\java\\images\\detectIcon.png"));


        stage.setScene(scene);
        stage.show();

        //获取中间的标签
        Label mainContainerLabel = mainUIController.getMainContainerLabel();


        //-----------------下面是按钮的点击事件---------------------
        //单一APK文件反编译按钮事件监听
        singleApkDecompileButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FXMLLoader singleApkDecompileLoader = new FXMLLoader();
                URL url = singleApkDecompileLoader.getClassLoader().getResource("fxml/singleApkDecompile.fxml");
                singleApkDecompileLoader.setLocation(url);
                BorderPane singleApkDecompileRoot = null;
                try {
                    singleApkDecompileRoot = (BorderPane) singleApkDecompileLoader.load();
                    Scene singleApkDecompileScene = new Scene(singleApkDecompileRoot);
                    Node node = (Node) event.getSource();
                    Stage mainStage = (Stage) node.getScene().getWindow();
                    mainStage.setScene(singleApkDecompileScene);
                    mainStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        //批量APK文件反编译按钮事件监听
        multipleApkDecompileButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FXMLLoader multipleApkDecompileLoader = new FXMLLoader();
                URL url = multipleApkDecompileLoader.getClassLoader().getResource("fxml/multipleApkDecompile.fxml");
                multipleApkDecompileLoader.setLocation(url);
                BorderPane multipleApkDecompileRoot = null;
                try {
                    multipleApkDecompileRoot = (BorderPane) multipleApkDecompileLoader.load();
                    Scene multipleApkDecompileScene = new Scene(multipleApkDecompileRoot);
                    Node node = (Node) event.getSource();
                    Stage mainStage = (Stage) node.getScene().getWindow();
                    mainStage.setScene(multipleApkDecompileScene);
                    mainStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        //权限特征提取按钮事件监听
        authorityExtractButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FXMLLoader authorityExtractLoader = new FXMLLoader();
                URL url = authorityExtractLoader.getClassLoader().getResource("fxml/AuthorityExtract.fxml");
                authorityExtractLoader.setLocation(url);
                BorderPane authorityExtractRoot = null;
                try {
                    authorityExtractRoot = (BorderPane) authorityExtractLoader.load();
                    Scene authorityExtractScene = new Scene(authorityExtractRoot);
                    Node node = (Node) event.getSource();
                    Stage mainStage = (Stage) node.getScene().getWindow();
                    mainStage.setScene(authorityExtractScene);
                    mainStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        //API特征提取按钮事件监听
        apiExtractButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mainContainerLabel.setText("API特征提取");

            }
        });

        //在线学习算法按钮事件监听
        onlineLearningAlgorithmButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mainContainerLabel.setText("在线学习算法");

            }
        });

        //其他算法按钮事件监听
        otherAlgorithmButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mainContainerLabel.setText("其他学习算法");

            }
        });

        //单一应用检测按钮事件监听
        singleApkDetectionButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mainContainerLabel.setText("单一应用检测");


            }
        });

        //批量应用检测按钮事件监听
        multipleApkDetectionButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mainContainerLabel.setText("批量应用检测");

            }
        });

        //在线更新按钮事件监听
        onlineUpdateButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mainContainerLabel.setText("在线更新模型");


            }
        });

        //数据对比按钮设置事件监听
        compareDataButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mainContainerLabel.setText("数据对比");

            }
        });


    }

 /*   public void sigleDecomplie(){
        System.out.println("单线程反编译按钮被点击了...");
        Stage stage = new Stage();
        BorderPane borderPane = new BorderPane();

        Text text = new Text("这是测试文本");
        borderPane.getChildren().add(text);
        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        stage.show();


    }*/
/*    public void mulltiDecomplie(){
        System.out.println("多线程反编译按钮被点击了...");

    }*/


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void handle(Event event) {
        Button bt = (Button) event.getSource();
        String text = bt.getText();
        System.out.println(text);


    }
}
