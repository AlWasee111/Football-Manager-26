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

public class InComingOffersController implements Initializable {
    @FXML
    private ListView<String> OfferList;
    @FXML
    private Label playerLabel;
    @FXML
    private Button acceptButton;

    private Parent root;
    private Stage stage;
    private Scene scene;

    private String currentOffer;
    private String currentPlayer;
    double curFee;
    String curBuyer;
    //int curBuyerID;

    int idx = SelectedClub.clubIndex;
    int buyeridx;

    String[] teams = {"FC Barcelona", "Arsenal FC" , "Chelsea FC", "Manchester United",
            "Real Madrid CF", "FC Bayern München", "Paris Saint-Germain", "Manchester City"};

    File file = new File("src/main/resources/Squads/TransferReq.txt");
    Scanner scanner;

    ArrayList<String> offers = new ArrayList<>();

    {
        try {
            scanner = new Scanner(file);
            while (scanner.hasNextLine()){
                String[] reqInfo = scanner.nextLine().split(",");
                int reqTo = Integer.parseInt(reqInfo[2]);
                if(reqTo == idx){
                    String playerName = reqInfo[0];
                    String reqForm = teams[Integer.parseInt(reqInfo[1])];
                    double fee = Double.parseDouble(reqInfo[3]);
                    String offer = playerName + " - " + reqForm + " - €" + fee + "M";
                    offers.add(offer);
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for (String player : offers){
            OfferList.getItems().add(player);
        }

        OfferList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if (t1 == null) return;

                acceptButton.setVisible(true);
                currentOffer = OfferList.getSelectionModel().getSelectedItem();
                String[] splitCurrentOffer = currentOffer.split(" - ");
                currentPlayer = splitCurrentOffer[0];
                curBuyer = splitCurrentOffer[1];
                curFee = Double.parseDouble(splitCurrentOffer[2].replace("€", "").replace("M", "").trim());
                for(int i = 0; i < teams.length; i++){
                    if(curBuyer.equals(teams[i])){
                        buyeridx = i;
                        break;
                    }
                }
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
        if(!currentOffer.isEmpty()){
            Stage warningBox = new Stage();
            warningBox.initStyle(StageStyle.UNDECORATED); //removes default top bar
            warningBox.initModality(Modality.APPLICATION_MODAL); //can't access other stuffs

            Label message = new Label("Name: " + currentPlayer + "\n" + "Wanted by: " + curBuyer + "\n" + "Price: €" + curFee + "M");
            message.getStyleClass().add("warning-message");

            Button accept = new Button("Accept");
            Button reject = new Button("Reject");
            Button stall = new Button("Stall");

            accept.getStyleClass().add("warning-button");
            reject.getStyleClass().add("warning-button");
            stall.getStyleClass().add("warning-button");

            accept.setOnAction(event -> {
                warningBox.close();
                PlayerClient.sendCommand("RS",currentOffer, SelectedClub.clubIndex, buyeridx, curFee);
                OfferList.getItems().remove(currentOffer);
            });
            reject.setOnAction(event -> {
                warningBox.close();
                PlayerClient.sendCommand("RR",currentOffer, SelectedClub.clubIndex, buyeridx, curFee);
                OfferList.getItems().remove(currentOffer);
            });
            stall.setOnAction(event -> {
                warningBox.close();
            });

            HBox buttons = new HBox(25, accept, stall, reject);
            buttons.setAlignment(Pos.CENTER);

            VBox root = new VBox(40,message,buttons);
            root.setAlignment(Pos.CENTER);

            root.getStyleClass().add("warning-pane");

            Scene scene = new Scene(root, 600, 350);
            scene.getStylesheets().add(getClass().getResource("/Stylings/AlertStyle3.css").toExternalForm());

            warningBox.setScene(scene);
            warningBox.showAndWait();
        }
    }
}
