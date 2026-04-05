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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Scanner;

public class ScoutTeamSquadController implements Initializable {
    @FXML
    private ListView<String> PlayerList;
    @FXML
    private Label playerLabel;
    @FXML
    private Button reqButton;
    @FXML
    private Label teamName;
    @FXML
    private ImageView playerCard;

    private Stage stage;
    private Scene scene;
    private Parent root;

    private String currentPlayer;

    int idx = ScoutTeamsController.scoutIDX;

    String[] squads = {"Squads/FCBsquad.txt", "Squads/ARSsquad.txt", "Squads/CHEsquad.txt", "Squads/MUsquad.txt",
            "Squads/RMsquad.txt", "Squads/BMsquad.txt", "Squads/PSGsquad.txt", "Squads/MCsquad.txt"};

    String[] teams = {"FC Barcelona", "Arsenal FC" , "Chelsea FC", "Manchester United",
            "Real Madrid CF", "FC Bayern München", "Paris Saint-Germain", "Manchester City"};


    File file = new File("src/main/resources/" + squads[idx]);
    Scanner scanner;

    ArrayList<Player> players = new ArrayList<>();

    {
        try {
            scanner = new Scanner(file);
            while (scanner.hasNextLine()){
                String[] splitPlayer = scanner.nextLine().split(",");
                players.add(new Player(splitPlayer[0],splitPlayer[1],Integer.parseInt(splitPlayer[2]),Double.parseDouble(splitPlayer[3]),splitPlayer[4],splitPlayer[5]));
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        teamName.setText(teams[idx]);

        for (Player player : players){
            PlayerList.getItems().add(player.name);
        }

        PlayerList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                reqButton.setVisible(true);
                currentPlayer = PlayerList.getSelectionModel().getSelectedItem();
                String cardPath = getPlayer(currentPlayer).cardPath;
                Image image = new Image(getClass().getResourceAsStream("/playerCards/" + cardPath + ".png"));
                playerCard.setImage(image);
                playerLabel.setText(currentPlayer);
            }
        });
    }

    private Player getPlayer(String name){
        for(Player player : players){
            if(name.equals(player.name)){
                return player;
            }
        }
        return null;
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

    public void reqTransfer(){
        stage = (Stage) reqButton.getScene().getWindow();
        if(!currentPlayer.isEmpty()){
            Stage warningBox = new Stage();
            warningBox.initStyle(StageStyle.UNDECORATED); //removes default top bar
            warningBox.initModality(Modality.APPLICATION_MODAL); //can't access other stuffs

            Label message = new Label("How much you are willing to pay for\n" + currentPlayer + " in €M?");
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

                File file3 = new File("src/main/resources/Squads/SquadBudgets.txt");

                ArrayList<String> budgets = new ArrayList<>();
                {
                    try {
                        scanner = new Scanner(file3);
                        while (scanner.hasNextLine()){
                            budgets.add(scanner.nextLine());
                        }
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }

                double buyerBudget = Double.parseDouble(budgets.get(SelectedClub.clubIndex));

                if(fee < 0){
                    errorMessage.setText("Invalid Transfer Fee! Enter a positive number.");
                }
                else if(fee > buyerBudget){
                    errorMessage.setText("You do not have enough budget!");
                }
                else {
                    warningBox.close();
                    Player player = getPlayer(currentPlayer);
                    player.setFee(fee);
                    player.setBuyer(SelectedClub.clubIndex);
                    player.setSeller(ScoutTeamsController.scoutIDX);
                    PlayerClient.sendCommand("R",player);
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
