package com.footballmanager.footballmanager;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.media.AudioClip;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.scene.paint.Color;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        PlayerClient.connect();

        MusicPlayer.initPlaylist();

        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("mainmenu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1440, 900);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/Stylings/Styles.css")).toExternalForm());

        Image icon = new Image(getClass().getResourceAsStream("/images/logo.jpg"));

        stage.setResizable(false);
        stage.getIcons().add(icon);
        stage.setTitle("Football Manager 26");
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(windowEvent -> {
            windowEvent.consume();
            logout(stage);
        });
    }

    public void logout(Stage stage){
        Stage warningBox = new Stage();
        warningBox.initStyle(StageStyle.UNDECORATED); //removes default top bar
        warningBox.initModality(Modality.APPLICATION_MODAL); //can't access other stuffs

        Label message = new Label("Are you sure you want to exit? :(");
        message.getStyleClass().add("warning-message");

        Button yes = new Button("Yes");
        Button no = new Button("No");

        addSoundEffects(yes);
        addSoundEffects(no);

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

    public static void main(String[] args) {
        launch(args);
    }

    private void addSoundEffects(Button button) {

        AudioClip hoverSound = new AudioClip(
                getClass().getResource("/music/hovering.mp3").toExternalForm()
        );

        AudioClip clickSound = new AudioClip(
                getClass().getResource("/music/clicking.mp3").toExternalForm()
        );

        button.setOnMouseEntered(e -> {
            hoverSound.setVolume(2.0 * volume.sfxVol);
            hoverSound.play();
        });

        button.addEventHandler(ActionEvent.ACTION, e -> {
            clickSound.setVolume(1.6 * volume.sfxVol);
            clickSound.play();
        });
    }
}