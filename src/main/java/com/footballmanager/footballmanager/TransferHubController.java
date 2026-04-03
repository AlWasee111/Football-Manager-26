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

public class TransferHubController implements Initializable {
    @FXML
    private ListView<String> TransHubList;
    @FXML
    private Label playerLabel;
    @FXML
    private Button buyButton;
    @FXML
    private ImageView playerCard;

    private Stage stage;
    private Scene scene;
    private Parent root;

    String currentPlayer;

    public void backToMenu(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("ClubMenu.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/Stylings/Styles2.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    String curName;
    double curFee;
    String curSeller;
    int curSellerID;

    String[] teams = {"FC Barcelona", "Arsenal FC" , "Chelsea FC", "Manchester United",
            "Real Madrid CF", "FC Bayern München", "Paris Saint-Germain", "Manchester City"};

    File file = new File("src/main/resources/Squads/TransferList.txt");
    Scanner scanner;

    ArrayList<Player> players = new ArrayList<>();

    {
        try {
            scanner = new Scanner(file);
            while (scanner.hasNextLine()){
                String[] splitInfo = scanner.nextLine().split(",");
                Player player = new Player(splitInfo[0],splitInfo[1],Integer.parseInt(splitInfo[2]),Double.parseDouble(splitInfo[3]),splitInfo[4],splitInfo[5]);
                player.setSeller(Integer.parseInt(splitInfo[6]));
                player.setFee(Double.parseDouble(splitInfo[7]));
                players.add(player);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for (int i = players.size() - 1; i >= 0; i--){
            String name = players.get(i).name;
            int seller = players.get(i).seller;
            double fee = players.get(i).fee;
            TransHubList.getItems().add(name + " - " + teams[seller] + " - €" + fee + "M");
        }

        TransHubList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                buyButton.setVisible(true);
                currentPlayer = TransHubList.getSelectionModel().getSelectedItem();
                if(currentPlayer == null) return;

                String[] curPlayerInfo = currentPlayer.split(" - ");
                curName = curPlayerInfo[0];
                curSeller = curPlayerInfo[1];
                curFee = Double.parseDouble(curPlayerInfo[2].replace("€", "").replace("M", "").trim());
                curSellerID = getPlayer(curName).seller;
                String cardPath = getPlayer(curName).cardPath;
                Image image = new Image(getClass().getResourceAsStream("/playerCards/" + cardPath + ".png"));
                playerCard.setImage(image);
                playerLabel.setText(curName);
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

    public void buyPlayer(){
        if(!curName.isEmpty()){
            Stage warningBox = new Stage();
            warningBox.initStyle(StageStyle.UNDECORATED); //removes default top bar
            warningBox.initModality(Modality.APPLICATION_MODAL); //can't access other stuffs

            Label message = new Label("");
            if(SelectedClub.clubIndex != curSellerID){
                message = new Label("Name: " + curName + "\n" + "Club: " + teams[curSellerID] + "\n" + "Price: €" + curFee + "M");
            }
            else{
                message = new Label("Are you sure you want to remove\n" + curName + " from transferlist?");
            }
            message.getStyleClass().add("warning-message");

            Label errorMessage = new Label("");
            errorMessage.getStyleClass().add("error-text");

            Button yes = new Button("Yes");
            Button no = new Button("No");

            yes.getStyleClass().add("warning-button");
            no.getStyleClass().add("warning-button");

            yes.setOnAction(event -> {
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

                if(curFee <= buyerBudget || SelectedClub.clubIndex == curSellerID){
                    warningBox.close();
                    Player curPlayer = getPlayer(curName);
                    curPlayer.setBuyer(SelectedClub.clubIndex);
                    PlayerClient.sendCommand("B",curPlayer);
                    TransHubList.getItems().remove(curPlayer.name + " - " + teams[curPlayer.seller] + " - €" + curPlayer.fee + "M");
                }
                else{
                    errorMessage.setText("You do not have enough budget!");
                }
            });
            no.setOnAction(event -> {
                warningBox.close();
            });

            HBox buttons = new HBox(25, yes, no);
            buttons.setAlignment(Pos.CENTER);

            VBox root = new VBox(40,message,errorMessage,buttons);
            root.setAlignment(Pos.CENTER);

            root.getStyleClass().add("warning-pane");

            Scene scene = new Scene(root, 600, 350);
            scene.getStylesheets().add(getClass().getResource("/Stylings/AlertStyle3.css").toExternalForm());

            warningBox.setScene(scene);
            warningBox.showAndWait();
        }
    }
}
