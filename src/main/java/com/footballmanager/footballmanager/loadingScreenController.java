package com.footballmanager.footballmanager;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Objects;

public class loadingScreenController {

    @FXML
    private ProgressBar progressBar;

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void initialize() {

        MusicPlayer.playMusic();
        // Start from 0
        progressBar.setProgress(0);

        // Create smooth 3-second animation
        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.seconds(3),
                        new KeyValue(
                                progressBar.progressProperty(),
                                1,
                                Interpolator.EASE_BOTH   // smooth animation
                        )
                )
        );

        //When progress bar is FULL
        timeline.setOnFinished(e -> {
            if (progressBar.getProgress() >= 1.0) {
                try {
                    root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("ClubMenu.fxml")));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                stage = (Stage) progressBar.getScene().getWindow();
                scene = new Scene(root);
                scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/Stylings/Styles2.css")).toExternalForm());

                stage.setScene(scene);
                stage.show();
            }
        });

        // Start animation
        timeline.play();
    }
}