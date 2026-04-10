package com.footballmanager.footballmanager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Scanner;

public class ClubMenuController implements Initializable {
    @FXML
    private Label NotifyBadge;
    @FXML
    private Label OfferNotifyBadge;
    @FXML
    private Label teamName;
    @FXML
    private Label budgetLabel;
    @FXML
    private ImageView teamCrest;
    @FXML
    private Label NotificBagde;
    @FXML
    private Button Players;
    @FXML
    private Button TransferMarket;
    @FXML
    private Button scoutPlayers;
    @FXML
    private Button incomingOffers;
    @FXML
    private Button notificButton;
    @FXML
    private Button Logout;
    @FXML
    private Button musicButton;
    @FXML
    private Button account;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String[] teams = {"FC Barcelona", "Arsenal FC" , "Chelsea FC", "Manchester United",
                "Real Madrid CF", "FC Bayern München", "Paris Saint-Germain", "Manchester City"};

        String[] crests = {"Barcelona.png","Arsenal.png","Chelsea.png","ManchesterUnited.png",
                "RealMadrid.png","Bayern.png","PSG.png","ManchesterCity.png"};
        File file = new File("src/main/resources/Squads/SquadBudgets.txt");
        Scanner scanner;
        ArrayList<String> budgets = new ArrayList<>();
        {
            try {
                scanner = new Scanner(file);
                while (scanner.hasNextLine()){
                    budgets.add(scanner.nextLine());
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        int idx = SelectedClub.clubIndex;

        teamName.setText(teams[idx]);

        Image image = new Image(getClass().getResourceAsStream("/Clubs/" + crests[idx]));
        teamCrest.setImage(image);

        budgetLabel.setText("Budget: €" + budgets.get(idx) + "M");

        updateNotifBadge();
        updateTransferBadge();
        updateOfferBadge();
        addSoundEffects(Players);
        addSoundEffects(TransferMarket);
        addSoundEffects(scoutPlayers);
        addSoundEffects(incomingOffers);
        addSoundEffects(notificButton);
        addSoundEffects(Logout);
        addSoundEffects(musicButton);
        addSoundEffects(account);
    }

    private void updateTransferBadge(){
        File file = new File("src/main/resources/Squads/TransferList.txt");
        int count = 0;
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()){
                scanner.nextLine();
                count++;
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        if(count > 0){
            NotifyBadge.setVisible(true);
            NotifyBadge.setText(String.valueOf(count));
        }
        else{
            NotifyBadge.setVisible(false);
        }
    }

    private void updateOfferBadge(){
        File file = new File("src/main/resources/Squads/TransferReq.txt");
        int count = 0;
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()){
                String[] reqInfo = scanner.nextLine().split(",");
                int reqTo = Integer.parseInt(reqInfo[6]);
                if(reqTo == SelectedClub.clubIndex) {
                    count++;
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        if(count > 0){
            OfferNotifyBadge.setVisible(true);
            OfferNotifyBadge.setText(String.valueOf(count));
        }
        else{
            OfferNotifyBadge.setVisible(false);
        }
    }

    private void updateNotifBadge() {
        File file = new File("src/main/resources/Squads/Notifications.txt");
        int idx = SelectedClub.clubIndex;
        int total = 0;

        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String[] info = scanner.nextLine().split(",");
                int buyer  = Integer.parseInt(info[1]);
                int seller = Integer.parseInt(info[2]);
                String opt = info[4];
                if (!opt.equals("R") && idx == buyer || idx == seller) total++;
                else if(opt.equals("R") && idx == buyer){
                    total++;
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        int unseen = total - NotificNumUpdate.updateCount[idx];

        if (unseen > 0) {
            NotificBagde.setVisible(true);
            NotificBagde.setText(String.valueOf(unseen));
        } else {
            NotificBagde.setVisible(false);
        }
    }

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void GoToMainMenu (ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("mainmenu.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        logout(stage);
    }

    public void GoToPlayers(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("PlayerList.fxml"));
        root = loader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        //scene = new Scene(root);
        scene = new Scene(root, stage.getScene().getWidth(), stage.getScene().getHeight());
        scene.getStylesheets().add(getClass().getResource("/Stylings/ListStyle.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    public void GoToTransferHub(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TransferHub.fxml"));
        root = loader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        //scene = new Scene(root);
        scene = new Scene(root, stage.getScene().getWidth(), stage.getScene().getHeight());
        scene.getStylesheets().add(getClass().getResource("/Stylings/TransferHub.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    public void GoToScoutplayers(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ScoutTeams.fxml"));
        root = loader.load();
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root, stage.getScene().getWidth(), stage.getScene().getHeight());
        scene.getStylesheets().add(getClass().getResource("/Stylings/ScoutTeams.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    public void GoToIncomingOffers(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("IncomingOffers.fxml"));
        root = loader.load();
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root, stage.getScene().getWidth(), stage.getScene().getHeight());
        scene.getStylesheets().add(getClass().getResource("/Stylings/IncomingOffers.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    public void GoToNotificationHub(ActionEvent event) throws IOException{
        File file = new File("src/main/resources/Squads/Notifications.txt");
        int idx = SelectedClub.clubIndex;
        int total = 0;
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String[] info = scanner.nextLine().split(",");
                int buyer  = Integer.parseInt(info[1]);
                int seller = Integer.parseInt(info[2]);
                String opt = info[4];
                if (!opt.equals("R") && idx == buyer || idx == seller) total++;
                else if(opt.equals("R") && idx == buyer){
                    total++;
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        NotificNumUpdate.updateCount[idx] = total;

        try{
            PrintWriter printWriter = new PrintWriter(new File("src/main/resources/Squads/NotificationSeen.txt"));
            for(int seeNum : NotificNumUpdate.updateCount){
                printWriter.println(seeNum);
            }
            printWriter.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }


        FXMLLoader loader = new FXMLLoader(getClass().getResource("NotificationHub.fxml"));
        root = loader.load();
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root, stage.getScene().getWidth(), stage.getScene().getHeight());
        scene.getStylesheets().add(getClass().getResource("/Stylings/NotificationHub.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    public void musicControl(){

        stage = (Stage) musicButton.getScene().getWindow();

        double oldSfx = volume.sfxVol;
        double oldMusic = volume.musicVol;

        Stage warningBox = new Stage();
        warningBox.initStyle(StageStyle.UNDECORATED);
        warningBox.initModality(Modality.APPLICATION_MODAL);

        Label message = new Label("Settings");
        message.getStyleClass().add("warning-message");

        Button confirm = new Button("Confirm");
        Button cancel = new Button("Cancel");

        addSoundEffects(confirm);
        addSoundEffects(cancel);

        confirm.getStyleClass().add("warning-button");
        cancel.getStyleClass().add("warning-button");

        Slider sfx = new Slider(0, 1, volume.sfxVol);
        Slider music = new Slider(0, 1, volume.musicVol);

        sfx.setMaxWidth(220);
        music.setMaxWidth(220);

        Label sfxLabel = new Label("SFX Volume");
        Label musicLabel = new Label("Music Volume");

        sfxLabel.getStyleClass().add("warning-message");
        musicLabel.getStyleClass().add("warning-message");

        sfx.valueProperty().addListener((obs, oldVal, newVal) -> {
            volume.sfxVol = newVal.doubleValue();
            MusicPlayer.refreshVolume();
        });

        music.valueProperty().addListener((obs, oldVal, newVal) -> {
            volume.musicVol = newVal.doubleValue();
            MusicPlayer.refreshVolume();
        });


        confirm.setOnAction(event -> {
            warningBox.close();
        });

        cancel.setOnAction(event -> {
            warningBox.close();

            volume.sfxVol = oldSfx;
            volume.musicVol = oldMusic;

            MusicPlayer.refreshVolume();
        });

        VBox sfxBox = new VBox(10, sfxLabel, sfx);
        sfxBox.setAlignment(Pos.CENTER);

        VBox musicBox = new VBox(10, musicLabel, music);
        musicBox.setAlignment(Pos.CENTER);

        HBox buttons = new HBox(25, confirm, cancel);
        buttons.setAlignment(Pos.CENTER);

        VBox root = new VBox(25, message, sfxBox, musicBox, buttons);
        root.setAlignment(Pos.CENTER);

        root.getStyleClass().add("warning-pane");

        Scene scene = new Scene(root, 600, 350);
        scene.getStylesheets().add(
                getClass().getResource("/Stylings/AlertStyle2.css").toExternalForm()
        );

        warningBox.setScene(scene);

        warningBox.setX(stage.getX() + (stage.getWidth()  - 600) / 2);
        warningBox.setY(stage.getY() + (stage.getHeight() - 350) / 2);

        warningBox.showAndWait();
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

    public void logout(Stage stage){
        Stage warningBox = new Stage();
        warningBox.initStyle(StageStyle.UNDECORATED); //removes default top bar
        warningBox.initModality(Modality.APPLICATION_MODAL); //can't access other stuffs

        Label message = new Label("Are you sure you want to logout?");
        message.getStyleClass().add("warning-message");

        Button yes = new Button("Yes");
        Button no = new Button("No");

        addSoundEffects(yes);
        addSoundEffects(no);

        yes.getStyleClass().add("warning-button");
        no.getStyleClass().add("warning-button");

        yes.setOnAction(event -> {
            warningBox.close();
            scene = new Scene(root);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/Stylings/Styles.css")).toExternalForm());
            stage.setScene(scene);
            stage.show();
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
        scene.getStylesheets().add(getClass().getResource("/Stylings/AlertStyle2.css").toExternalForm());

        warningBox.setScene(scene);

        warningBox.setX(stage.getX() + (stage.getWidth()  - 600) / 2);
        warningBox.setY(stage.getY() + (stage.getHeight() - 350) / 2);

        warningBox.showAndWait();
    }

    public void GotoAccount (ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Account.fxml"));
        root = loader.load();

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/Stylings/Styles2.css")).toExternalForm());

        stage.setScene(scene);
        stage.show();
    }
}