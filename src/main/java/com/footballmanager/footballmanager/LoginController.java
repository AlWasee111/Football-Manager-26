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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.net.URL;
import java.util.*;

public class LoginController implements Initializable {

    @FXML
    private TextField Password;
    @FXML
    private Label errorMessage;
    @FXML
    private Label myLabel;
    @FXML
    private ComboBox<String> myCombobox;

    private Stage stage;
    private Scene scene;
    private Parent root;
    private String passwordPath = "src/main/resources/Passwords/password.txt";
    private final String[] clubs = {"Barcelona", "Arsenal", "Chelsea", "Manchester United",
            "Real Madrid", "Bayern", "PSG", "Manchester City"};

    List<String> signedUp = new ArrayList<>();
    List<String> nonSignedUp = new ArrayList<>();

    int idx = -1;

    @FXML
    private Button EnterButton;
    @FXML
    private Button signUp;
    @FXML
    private Button backButton;

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

        addSoundEffects(EnterButton);
        addSoundEffects(signUp);
        addSoundEffects(backButton);
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
            fadeOutWarning(errorMessage);
        }
        //If club doesn't exist
        else if (!ClubExists(clubName)) {
            errorMessage.setText("Club not availabe!");
            errorMessage.setVisible(true);
            fadeOutWarning(errorMessage);
        }
        // If password is empty
        else if (pass.isEmpty()) {
            errorMessage.setText("Password cannot be empty!");
            errorMessage.setVisible(true);
            fadeOutWarning(errorMessage);
        }
        //Login with existing club
        else {
            BufferedReader reader = new BufferedReader(new FileReader(passwordPath));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parsedStrings = line.split("_Password_");
                String parsedClubName = parsedStrings[0];
                String parsedPassword = parsedStrings[1];
                if (parsedClubName.equals(clubName) && !parsedPassword.equals(pass)) {
                    errorMessage.setText("Wrong password!");
                    errorMessage.setVisible(true);
                    fadeOutWarning(errorMessage);
                    reader.close();
                    break;
                } else if (parsedClubName.equals(clubName) && parsedPassword.equals(pass)) {
                    reader.close();
                    EnterClubmenu(clubName, event);
                    break;
                }
            }
        }
    }

    private void EnterClubmenu (String clubName, ActionEvent event) throws IOException{
        idx = getClubIndex(clubName);
        SelectedClub.clubIndex = idx;

        File file = new File("src/main/resources/Squads/NotificationSeen.txt");
        int i = 0;
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                NotificNumUpdate.updateCount[i++] = Integer.parseInt(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("LoadingScreen.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/Stylings/loadingScreen.css")).toExternalForm());

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

    public void GotoSignupPage(ActionEvent event) throws IOException {
        if (clubs.length == myCombobox.getItems().size()) {
            errorMessage.setText("No clubs available for signup!");
            errorMessage.setVisible(true);
            fadeOutWarning(errorMessage);
        }
        else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SignUp.fxml"));
            root = loader.load();

            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/Stylings/Styles.css")).toExternalForm());

            stage.setScene(scene);
            stage.show();
        }
    }

    public void fadeOutWarning(Label warningField) {
        FadeTransition fade = new FadeTransition(Duration.millis(6500), warningField);

        fade.setFromValue(1.0);
        fade.setToValue(0.0);

        fade.play();
    }
}