package com.footballmanager.footballmanager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private TextField Password;
    @FXML
    private Label errorMessage;
    @FXML
    private ComboBox<String> myCombobox;

    private Stage stage;
    private Scene scene;
    private Parent root;

    private final String[] clubs = {"Barcelona", "Arsenal", "Chelsea", "Manchester United",
            "Real Madrid", "Bayern", "PSG", "Manchester City"};
    int idx = -1;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        myCombobox.getItems().addAll(clubs);
    }

    // Returns to main menu if back button is clicked
    public void ReturnToMenu(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("mainmenu.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/Styles.css")).toExternalForm());

        stage.setScene(scene);
        stage.show();
    }

    public void EnterPage(ActionEvent event) throws IOException {
        String clubName = myCombobox.getValue();

        String pass = Password.getText();
        // Validate if club exists
        if (clubName == null) {
            errorMessage.setText("Club name cannot be empty!");
            errorMessage.setVisible(true);
        }
        // If club name is empty
        else if (!ClubExists(clubName)) {
            errorMessage.setText("Club not availabe!");
            errorMessage.setVisible(true);
        }
        // If password is empty
        else if (pass.isEmpty()) {
            errorMessage.setText("Password cannot be empty!");
            errorMessage.setVisible(true);
        } else {
            idx = getClubIndex(clubName);
            SelectedClub.clubIndex = idx;

            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("ClubMenu.fxml")));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/Styles2.css")).toExternalForm());

            stage.setScene(scene);
            stage.show();
        }
    }

    // Method to check if a club exists
    private boolean ClubExists(String clubName) {
        if (clubName.equals("Barcelona") || clubName.equals("Real Madrid") ||
                clubName.equals("Arsenal") || clubName.equals("Bayern") ||
                clubName.equals("Chelsea") || clubName.equals("PSG") ||
                clubName.equals("Manchester United") || clubName.equals("Manchester City")) {
            return true;
        }

        return false;
    }

    private int getClubIndex(String clubName){
        for(int i = 0; i < clubs.length; i++){
            if(clubName.equals(clubs[i])){
                return i;
            }
        }
        return -1;
    }

    public int getIdx(){
        return idx;
    }
}