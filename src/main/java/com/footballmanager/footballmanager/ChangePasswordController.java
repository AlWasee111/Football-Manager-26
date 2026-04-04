package com.footballmanager.footballmanager;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChangePasswordController {
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