package com.footballmanager.footballmanager;

import java.io.IOException;
import java.util.Objects;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Controller {
    @FXML
    private Button exitButton;
    @FXML
    private AnchorPane MainScene;

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void LoginScreen (ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        root = loader.load();

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/Styles.css")).toExternalForm());

        stage.setScene(scene);
        stage.show();
    }

    public void ExitApplication (ActionEvent event) {
        stage = (Stage) MainScene.getScene().getWindow();
        stage.close();
    }
}