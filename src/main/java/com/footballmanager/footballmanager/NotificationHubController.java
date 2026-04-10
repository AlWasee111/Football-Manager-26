package com.footballmanager.footballmanager;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Scanner;

public class NotificationHubController implements Initializable {
    @FXML
    private ListView<String> NotificationList;
    @FXML
    private Button backButton;

    private Parent root;
    private Stage stage;
    private Scene scene;

    int idx = SelectedClub.clubIndex;
    int selleridx;
    int buyeridx;
    double fee;
    String optType;
    String playerName;

    int curSeller;
    int curBuyer;
    String currentPlayer;
    String currentNotific;

    String[] teams = {"FC Barcelona", "Arsenal FC" , "Chelsea FC", "Manchester United",
            "Real Madrid CF", "FC Bayern München", "Paris Saint-Germain", "Manchester City"};

    File file = new File("src/main/resources/Squads/Notifications.txt");
    Scanner scanner;

    ArrayList<String> notifics = new ArrayList<>();

    {
        try {
            scanner = new Scanner(file);
            while (scanner.hasNextLine()){
                String[] notificInfo = scanner.nextLine().split(",");
                playerName = notificInfo[0];
                buyeridx = Integer.parseInt(notificInfo[1]);
                selleridx = Integer.parseInt(notificInfo[2]);
                fee = Double.parseDouble(notificInfo[3]);
                optType = notificInfo[4];
                if(optType.equals("R") && idx == buyeridx){
                    String Notification = playerName + " transfer rejected by " + teams[selleridx];
                    notifics.add(Notification);
                }
                else if(!optType.equals("R") && idx == buyeridx && idx == selleridx){
                    String Notification = playerName + " retained from transferlist";
                    notifics.add(Notification);
                }
                else if(!optType.equals("R") && idx == buyeridx){
                    String notification = playerName + " bought from " + teams[selleridx] + " for €" + fee + "M";
                    notifics.add(notification);
                }
                else if(!optType.equals("R") && idx == selleridx){
                    String notification = playerName + " sold to " + teams[buyeridx] + " for €" + fee + "M";
                    notifics.add(notification);
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for(int i = notifics.size() - 1; i >= 0; i--){
            NotificationList.getItems().add(notifics.get(i));
        }
        addSoundEffects(backButton);
    }

    public void backToMenu(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("ClubMenu.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/Stylings/Styles2.css")).toExternalForm());

        stage.setScene(scene);
        stage.show();
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
