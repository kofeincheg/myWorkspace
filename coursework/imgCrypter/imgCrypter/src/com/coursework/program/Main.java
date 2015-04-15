package com.coursework.program;

import com.coursework.utils.AppConst;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent rootNode = FXMLLoader.load(getClass().getResource(AppConst.MAIN_FORM_LOCATION));

        Scene scene = new Scene(rootNode);
        primaryStage.setTitle("Image Crypter");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
