package com.footballmanager.footballmanager;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Scanner;

public class PlayerListController implements Initializable {
    @FXML
    private ListView<String> PlayerList;
    @FXML
    private Label playerLabel;
    @FXML
    private Button sellButton;
    @FXML
    private ImageView playerCard;

    private Stage stage;
    private Scene scene;
    private Parent root;

    private String currentPlayer;

    int idx = SelectedClub.clubIndex;

    String[] squads = {"Squads/FCBsquad.txt", "Squads/ARSsquad.txt", "Squads/CHEsquad.txt", "Squads/MUsquad.txt",
            "Squads/RMsquad.txt", "Squads/BMsquad.txt", "Squads/PSGsquad.txt", "Squads/MCsquad.txt"};

    File file = new File("src/main/resources/" + squads[idx]);
    Scanner scanner;

    ArrayList<Player> playerInfos = new ArrayList<>();
    ArrayList<String> players = new ArrayList<>();

    {
        try {
            scanner = new Scanner(file);
            while (scanner.hasNextLine()){
                String[] splitPlayer = scanner.nextLine().split(",");
                playerInfos.add(new Player(splitPlayer[0],splitPlayer[1],Integer.parseInt(splitPlayer[2]),Double.parseDouble(splitPlayer[3]),splitPlayer[4],splitPlayer[5]));
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for (Player playerInfo : playerInfos){
            PlayerList.getItems().add(playerInfo.name);
        }

        PlayerList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                sellButton.setVisible(true);
                currentPlayer = PlayerList.getSelectionModel().getSelectedItem();
                playerLabel.setText(currentPlayer);
                String cardPath = getPlayer(currentPlayer).cardPath;
                Image image = new Image(getClass().getResourceAsStream("/playerCards/" + cardPath + ".png"));
                playerCard.setImage(image);
            }
        });
    }

    private Player getPlayer(String name){
        for(Player playerinfo : playerInfos){
            if(name.equals(playerinfo.name)){
                return playerinfo;
            }
        }
        return null;
    }

    public void backToMenu(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("ClubMenu.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/Stylings/Styles2.css")).toExternalForm());

        stage.setScene(scene);
        stage.show();
    }

    public void sellPlayer(){
        stage = (Stage) sellButton.getScene().getWindow();
        if(!currentPlayer.isEmpty()){
            Stage warningBox = new Stage();
            warningBox.initStyle(StageStyle.UNDECORATED); //removes default top bar
            warningBox.initModality(Modality.APPLICATION_MODAL); //can't access other stuffs

            Label message = new Label("Set transfer fee for " + currentPlayer + " in €M");
            message.getStyleClass().add("warning-message");

            TextField priceField = new TextField();
            //priceField.setPrefHeight(45);
            //priceField.setPrefWidth(250);
            priceField.setPromptText("Enter Transfer Fee in €M");
            priceField.getStyleClass().add("text-field");

            Label errorMessage = new Label("");
            errorMessage.getStyleClass().add("error-text");

            Button confirm = new Button("Confirm");
            Button cancel = new Button("Cancel");

            confirm.getStyleClass().add("warning-button");
            cancel.getStyleClass().add("warning-button");

            confirm.setOnAction(event -> {
                String input = priceField.getText().trim();
                double fee = Double.parseDouble(input);

                if(fee < 0){
                    errorMessage.setText("Invalid Transfer Fee! Enter a positive number.");
                }
                else {
                    warningBox.close();
                    Player curPlayerInfo = getPlayer(currentPlayer);
                    curPlayerInfo.setSeller(SelectedClub.clubIndex);
                    curPlayerInfo.setFee(fee);
                    PlayerClient.sendCommand("S",curPlayerInfo);
                    PlayerList.getItems().remove(currentPlayer);
                }
            });
            cancel.setOnAction(event -> {
                warningBox.close();
            });

            HBox buttons = new HBox(25, confirm, cancel);
            buttons.setAlignment(Pos.CENTER);

            VBox root = new VBox(40,message,priceField,errorMessage,buttons);
            root.setAlignment(Pos.CENTER);

            root.getStyleClass().add("warning-pane");

            Scene scene = new Scene(root, 600, 350);
            scene.getStylesheets().add(getClass().getResource("/Stylings/AlertStyle3.css").toExternalForm());

            warningBox.setScene(scene);

            warningBox.setX(stage.getX() + (stage.getWidth()  - 600) / 2);
            warningBox.setY(stage.getY() + (stage.getHeight() - 350) / 2);

            warningBox.showAndWait();
        }
    }
}
