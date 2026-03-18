package com.footballmanager.footballmanager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
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

import java.io.*;
import java.net.URL;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

enum State {LOGIN, SIGNUP}
public class LoginController implements Initializable {

    @FXML
    private TextField Password;
    @FXML
    private Label errorMessage;
    @FXML
    private Label myLabel;
    @FXML
    private Button loginState;
    @FXML
    private ComboBox<String> myCombobox;

    private Stage stage;
    private Scene scene;
    private Parent root;
    private State state = State.LOGIN;
    private String passwordPath = "src/main/resources/Passwords/password.txt";
    private final String[] clubs = {"Barcelona", "Arsenal", "Chelsea", "Manchester United",
            "Real Madrid", "Bayern", "PSG", "Manchester City"};

    List<String> signedUp = new ArrayList<>();
    List<String> nonSignedUp = new ArrayList<>();

    int idx = -1;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try (BufferedReader reader = new BufferedReader(new FileReader(passwordPath));){
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parsedStrings = line.split("_Password_");
                if (ClubExists(parsedStrings[0]))
                    signedUp.add(parsedStrings[0]);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Password file not found!");
        } catch (IOException e) {
            System.out.println("Something went wrong!");
        }

        myCombobox.getItems().addAll(signedUp);

        for (String club : clubs) {
            if (!signedUp.contains(club))
                nonSignedUp.add(club);
        }
    }

    // Returns to main menu if back button is clicked
    public void ReturnToMenu(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("mainmenu.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/Stylings/Styles.css")).toExternalForm());

        stage.setScene(scene);
        stage.show();
    }

    public void EnterPage(ActionEvent event) throws IOException {
        String clubName = myCombobox.getValue();
        String pass = Password.getText();

        // If club name is empty
        if (clubName == null) {
            errorMessage.setText("Club name cannot be empty!");
            errorMessage.setVisible(true);
        }
        //If club doesn't exist
        else if (!ClubExists(clubName)) {
            errorMessage.setText("Club not availabe!");
            errorMessage.setVisible(true);
        }
        // If password is empty
        else if (pass.isEmpty()) {
            errorMessage.setText("Password cannot be empty!");
            errorMessage.setVisible(true);
        }
        //Login with existing club
        else if (state == State.LOGIN) {
            BufferedReader reader = new BufferedReader(new FileReader(passwordPath));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parsedStrings = line.split("_Password_");
                String parsedClubName = parsedStrings[0];
                String parsedPassword = parsedStrings[1];
                if (parsedClubName.equals(clubName) && !parsedPassword.equals(pass)) {
                    errorMessage.setText("Wrong password!");
                    errorMessage.setVisible(true);
                    reader.close();
                    break;
                } else if (parsedClubName.equals(clubName) && parsedPassword.equals(pass)) {
                    reader.close();
                    EnterClubmenu(clubName, event);
                    break;
                }
            }
        }
        //Login with new club
        else if (state == State.SIGNUP) {
            try (FileWriter fileWriter = new FileWriter(passwordPath, true);
                 PrintWriter printWriter = new PrintWriter(fileWriter)) {
                printWriter.println(clubName + "_Password_" + pass);
            }

            EnterClubmenu(clubName, event);
        }
    }

    private void EnterClubmenu (String clubName, ActionEvent event) throws IOException{
        idx = getClubIndex(clubName);
        SelectedClub.clubIndex = idx;

        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("ClubMenu.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/Stylings/Styles2.css")).toExternalForm());

        stage.setScene(scene);
        stage.show();
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

    public void ChangeState(ActionEvent e) {
        if (state == State.LOGIN) {
            state = State.SIGNUP;
            myLabel.setText("Choose Club    :");
            myCombobox.setPromptText("Select Club");
            myCombobox.setItems(FXCollections.observableArrayList(nonSignedUp));
            loginState.setText("Login");
        }
        else {
            state = State.LOGIN;
            myLabel.setText("Enter Your Club :");
            myCombobox.setPromptText("Select Club");
            myCombobox.setItems(FXCollections.observableArrayList(signedUp));
            loginState.setText("Signup");
        }
    }
}