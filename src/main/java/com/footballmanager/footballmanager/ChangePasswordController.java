package com.footballmanager.footballmanager;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class ChangePasswordController implements Initializable {
    @FXML
    private PasswordField oldPass;
    @FXML
    private PasswordField newPass;
    @FXML
    private Label errorMessage;

    private String club;
    private String passwordPath = "src/main/resources/Passwords/password.txt";
    private final String[] clubs = {"Barcelona", "Arsenal", "Chelsea", "Manchester United",
            "Real Madrid", "Bayern", "PSG", "Manchester City"};

    @FXML
    private javafx.scene.control.Button confirm;
    @FXML
    private javafx.scene.control.Button backButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addSoundEffects(confirm);
        addSoundEffects(backButton);
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

    public void InvokePassChange(ActionEvent event) throws IOException {
        String currentClub;
        String password = null;
        int clubIndex = SelectedClub.clubIndex;
        String targetClubName = clubs[clubIndex];

        if (oldPass.getText().isEmpty()) {
            errorMessage.setText("Please give your old password!");
            errorMessage.setVisible(true);
            fadeOutWarning(errorMessage);
        }
        else if (newPass.getText().isEmpty()) {
            errorMessage.setText("New password cannot be empty!");
            errorMessage.setVisible(true);
            fadeOutWarning(errorMessage);
        }
        else {
            BufferedReader reader = new BufferedReader(new FileReader(passwordPath));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("_Password_");
                if (parts[0].equals(targetClubName)) {
                    club = line;
                    break;
                }
            }
            reader.close();

            String[] clubAndPassword;
            clubAndPassword = club.split("_Password_");
            currentClub = clubAndPassword[0];
            password = clubAndPassword[1];

            if (!oldPass.getText().equals(password)) {
                errorMessage.setText("Wrong password entered!");
                errorMessage.setVisible(true);
                fadeOutWarning(errorMessage);
            }
            else {
                ArrayList<String> passwords = new ArrayList<>();
                reader = new BufferedReader(new FileReader(passwordPath));

                while ((line = reader.readLine()) != null) {
                    String[] parsed = line.split("_Password_");

                    if (currentClub.equals(parsed[0]))
                        passwords.add(currentClub + "_Password_" + newPass.getText());
                    else
                        passwords.add(line);
                }

                reader.close();

                try (PrintWriter writer = new PrintWriter(new FileWriter(passwordPath))) {
                    for (String lines : passwords) {
                        writer.println(lines);
                    }
                }

                errorMessage.setText("Password changed successfully!");
                errorMessage.setVisible(true);
                fadeOutWarning(errorMessage);
            }
        }
    }

    public void backToAccount(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Account.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/Stylings/Styles2.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    public void fadeOutWarning(Label warningField) {
        FadeTransition fade = new FadeTransition(Duration.millis(6500), warningField);

        fade.setFromValue(1.0);
        fade.setToValue(0.0);

        fade.play();
    }
}