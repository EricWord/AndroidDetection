package com.eric.test.javaFX;

import com.jfoenix.controls.JFXSpinner;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * @ClassName: SpinnerDemo
 * @Description: SpinnerDemo
 * @Author: Eric
 * @Date: 2019/4/22 0022
 * @Email: xiao_cui_vip@163.com
 */
public class SpinnerDemo extends Application {

    @Override
    public void start(final Stage stage) throws Exception {

        StackPane pane = new StackPane();

        JFXSpinner root = new JFXSpinner();

        pane.getChildren().add(root);

        final Scene scene = new Scene(pane, 50, 50);
        scene.getStylesheets().add(SpinnerDemo.class.getResource("/static/jfoenix-components.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("JFX Spinner Demo");
        stage.show();
    }

    public static void main(final String[] arguments) {
        Application.launch(arguments);
    }
}