package com.footballmanager.footballmanager;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainMenuController implements Initializable {
    @FXML
    private Button exitButton;
    @FXML
    private AnchorPane MainScene;
    @FXML
    private Button musicButton;

    private Stage stage;
    private Scene scene;
    private Parent root;
    private String passwordPath = "src/main/resources/Passwords/password.txt";

    public void LoginScreen (ActionEvent event) throws IOException {
        File file = new File(passwordPath);
        if (file.length() == 0) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SignUp.fxml"));
            root = loader.load();

            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/Stylings/Styles.css")).toExternalForm());

            stage.setScene(scene);
            stage.show();
        }
        else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            root = loader.load();

            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/Stylings/Styles.css")).toExternalForm());

            stage.setScene(scene);
            stage.show();
        }
    }

    public void musicControl(ActionEvent event) throws IOException{
        if(MusicPlayer.isPlay){
            MusicPlayer.pauseMusic();
            MusicPlayer.isPlay = false;
            musicButton.setText("Music Off");
        }
        else{
            MusicPlayer.playMusic();
            MusicPlayer.isPlay = true;
            musicButton.setText("Music On");
        }
    }

    public void ExitApplication(){
        stage = (Stage) exitButton.getScene().getWindow();
        Stage warningBox = new Stage();
        warningBox.initStyle(StageStyle.UNDECORATED); //removes default top bar
        warningBox.initModality(Modality.APPLICATION_MODAL); //can't access other stuffs

        Label message = new Label("Are you sure you want to exit? :(");
        message.getStyleClass().add("warning-message");

        Button yes = new Button("Yes");
        Button no = new Button("No");

        yes.getStyleClass().add("warning-button");
        no.getStyleClass().add("warning-button");

        yes.setOnAction(event -> {
            warningBox.close();
            stage.close();
        });
        no.setOnAction(event -> {
            warningBox.close();
        });

        HBox buttons = new HBox(25, yes, no);
        buttons.setAlignment(Pos.CENTER);

        VBox root = new VBox(40,message,buttons);
        root.setAlignment(Pos.CENTER);

        root.getStyleClass().add("warning-pane");

        Scene scene = new Scene(root, 600, 350);
        scene.getStylesheets().add(getClass().getResource("/Stylings/AlertStyle.css").toExternalForm());

        warningBox.setScene(scene);

        warningBox.setX(stage.getX() + (stage.getWidth()  - 600) / 2);
        warningBox.setY(stage.getY() + (stage.getHeight() - 350) / 2);

        warningBox.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(MusicPlayer.isPlay){
            musicButton.setText("Music On");
        }
        else{
            musicButton.setText("Music Off");
        }
    }
}