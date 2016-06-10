package com.davidflex.supermarket.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * ShopAgent gui.
 */
public class ShopAgentGui extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/shopAgentGui.fxml"));
        Parent root = loader.load();

        primaryStage.setTitle("Shop Agent");
        primaryStage.setResizable(false);

        final Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.centerOnScreen();

        primaryStage.show();
    }
}
