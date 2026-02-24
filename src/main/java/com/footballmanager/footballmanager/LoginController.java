package com.footballmanager.footballmanager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class LoginController {

    @FXML
    private TextField ClubName;
    @FXML
    private TextField Password;
    @FXML
    private Label errorMessage;

    private Stage stage;
    private Scene scene;
    private Parent root;

    // Returns to main menu if back button is clicked
    public void ReturnToMenu(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("mainmenu.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/Styles.css")).toExternalForm());

        stage.setScene(scene);
        stage.show();
    }

    public void EnterPage() {
        String clubName = ClubName.getText().trim();
        String pass = Password.getText();
        // Validate if club exists
        if (clubName.isEmpty()) {
            errorMessage.setText("Club name cannot be empty!");
            errorMessage.setVisible(true);
        }
        //If club name is empty
        else if (!ClubExists(clubName)) {
            errorMessage.setText("Club not availabe!");
            errorMessage.setVisible(true);
        }
        //If password is empty
        else if (pass.isEmpty()) {
            errorMessage.setText("Password cannot be empty!");
            errorMessage.setVisible(true);
        }
    }


    //Method to check if a club exists
    private boolean ClubExists(String clubName) {
        if (clubName.equalsIgnoreCase("Barcelona") || clubName.equalsIgnoreCase("Real Madrid") ||
                clubName.equalsIgnoreCase("Arsenal") || clubName.equalsIgnoreCase("Bayern") ||
                clubName.equalsIgnoreCase("Chelsea") || clubName.equalsIgnoreCase("PSG") ||
                clubName.equalsIgnoreCase("Manchester United") || clubName.equalsIgnoreCase("Manchester City")) {
            return true;
        }

        return false;
    }
}