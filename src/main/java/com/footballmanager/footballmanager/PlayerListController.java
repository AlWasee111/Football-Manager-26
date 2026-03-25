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

    private Stage stage;
    private Scene scene;
    private Parent root;

    private String currentPlayer;

    int idx = SelectedClub.clubIndex;

    String[] squads = {"Squads/FCBsquad.txt", "Squads/ARSsquad.txt", "Squads/CHEsquad.txt", "Squads/MUsquad.txt",
            "Squads/RMsquad.txt", "Squads/BMsquad.txt", "Squads/PSGsquad.txt", "Squads/MCsquad.txt"};

    File file = new File("src/main/resources/" + squads[idx]);
    Scanner scanner;

    ArrayList<String> players = new ArrayList<>();

    {
        try {
            scanner = new Scanner(file);
            while (scanner.hasNextLine()){
                players.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for (String player : players){
            PlayerList.getItems().add(player);
        }

        PlayerList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                sellButton.setVisible(true);
                currentPlayer = PlayerList.getSelectionModel().getSelectedItem();
                playerLabel.setText(currentPlayer);
            }
        });
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
                    PlayerClient.sendCommand("S",currentPlayer, SelectedClub.clubIndex, ScoutTeamsController.scoutIDX, fee);
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
            warningBox.showAndWait();
        }
    }
}
