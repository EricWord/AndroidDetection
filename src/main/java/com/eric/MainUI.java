package com.eric;

import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/*
 *@description:应用程序主界面
 *@author:cuigs
 *@Email:cuigs@ti-net.com.cn
 *@Version:1.0
 *@Date:2019/4/8
 */
public class MainUI extends Application  implements EventHandler {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/MainUI.fxml"));

        Scene scene = new Scene(root, 1200, 750);
        stage.setTitle("基于在线学习的恶意Android应用检测系统");
        //设置icon
        stage.getIcons().add(new Image("file:D:\\cgs\\Projects\\AndroidDetection\\src\\main\\java\\images\\detectIcon.png"));


        stage.setScene(scene);
        stage.show();


    }

    public void sigleDecomplie(){
        System.out.println("单线程反编译按钮被点击了...");
        Stage stage = new Stage();
        BorderPane borderPane = new BorderPane();

        Text text = new Text("这是测试文本");
        borderPane.getChildren().add(text);
        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        stage.show();


    }
    public void mulltiDecomplie(){
        System.out.println("多线程反编译按钮被点击了...");

    }


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void handle(Event event) {
        Button bt = (Button)event.getSource();
        String text = bt.getText();
        System.out.println(text);


    }
}
