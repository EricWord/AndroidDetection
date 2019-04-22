package com.eric.test.javaFX;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.validation.RequiredFieldValidator;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * @ClassName: TextAreaDemo
 * @Description: jfoenix框架TextAreaDemo
 * @Author: Eric
 * @Date: 2019/4/20 0020
 * @Email: xiao_cui_vip@163.com
 */
public class TextAreaDemo extends Application {

    @Override
    public void start(Stage stage) {

        VBox main = new VBox();
        main.setSpacing(50);

        TextArea javafxTextArea = new TextArea();
        javafxTextArea.setPromptText("JavaFX Text Area");
        main.getChildren().add(javafxTextArea);
        JFXTextArea jfxTextArea = new JFXTextArea();
        jfxTextArea.setPromptText("JFoenix Text Area :D");
        jfxTextArea.setLabelFloat(true);

        RequiredFieldValidator validator = new RequiredFieldValidator();
        // NOTE adding error class to text area is causing the cursor to disapper
        validator.setMessage("Please type something!");
       /* validator.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class)
                .glyph(FontAwesomeIcon.WARNING)
                .size("1em")
                .styleClass("error")
                .build());*/
        jfxTextArea.getValidators().add(validator);
        jfxTextArea.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) {
                jfxTextArea.validate();
            }
        });


        main.getChildren().add(jfxTextArea);

        StackPane pane = new StackPane();
        pane.getChildren().add(main);
        StackPane.setMargin(main, new Insets(100));
        pane.setStyle("-fx-background-color:WHITE");

        final Scene scene = new Scene(pane, 800, 600);
        scene.getStylesheets()
                .add(ButtonDemo.class.getResource("/static/jfoenix-components.css").toExternalForm());
        stage.setTitle("JFX Button Demo");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

}
