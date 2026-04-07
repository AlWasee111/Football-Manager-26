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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
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

public class InComingOffersController implements Initializable {
    @FXML
    private ListView<String> OfferList;
    @FXML
    private Label playerLabel;
    @FXML
    private Button acceptButton;
    @FXML
    private ImageView playerCard;
    @FXML
    private ComboBox<String> ratingList;
    @FXML
    private ComboBox<String> posList;
    @FXML
    private ComboBox<String> nationList;
    @FXML
    private ComboBox<String> salaryList;

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
    String[] ratings = {"60+","70+","80+","85+","90+"};
    String[] nations = {"ARG", "AUS", "BEL", "BRA", "CAM", "COL", "CRO", "DEN", "ECU", "EGY", "ENG", "ESP", "FRA", "GER", "GHA", "GOR", "ITA", "IVO", "JAP", "KOR", "MOR", "NED", "NOR", "POL", "POR", "RUS", "SEN", "SLO", "SWE", "TUR", "UKR", "URU", "UZB"};
    String[] positions = {"GK","CB","RB","LB","CDM","CM","CAM","RM","LM","LW","RW","ST"};
    String[] salaries = {"30000+", "40000+", "50000+", "60000+", "70000+", "80000+", "90000+", "100000+", "110000+", "120000+", "130000+", "140000+", "150000+", "160000+", "170000+", "180000+", "190000+", "200000+", "210000+", "220000+", "230000+", "240000+", "250000+", "260000+", "270000+", "280000+", "290000+", "300000+", "310000+", "320000+", "330000+", "340000+", "350000+", "360000+", "370000+", "380000+", "390000+", "400000+"};

    File file = new File("src/main/resources/Squads/TransferReq.txt");
    Scanner scanner;

    ArrayList<Player> players = new ArrayList<>();

    {
        try {
            scanner = new Scanner(file);
            while (scanner.hasNextLine()){
                String[] splitInfo = scanner.nextLine().split(",");
                Player playerInfo = new Player(splitInfo[0],splitInfo[1],Integer.parseInt(splitInfo[2]),Double.parseDouble(splitInfo[3]),splitInfo[4],splitInfo[5]);
                playerInfo.setSeller(Integer.parseInt(splitInfo[6]));
                playerInfo.setBuyer(Integer.parseInt(splitInfo[7]));
                playerInfo.setFee(Double.parseDouble(splitInfo[8]));
                if(playerInfo.seller == idx){
                    players.add(playerInfo);
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ratingList.getItems().addAll(ratings);
        posList.getItems().addAll(positions);
        nationList.getItems().addAll(nations);
        salaryList.getItems().addAll(salaries);

        for (int i = players.size() - 1; i >= 0; i--){
            String name = players.get(i).name;
            int buyer = players.get(i).buyer;
            double fee = players.get(i).fee;
            OfferList.getItems().add(name + " - " + teams[buyer] + " - €" + fee + "M");
        }

        OfferList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if (t1 == null) return;
                currentOffer = OfferList.getSelectionModel().getSelectedItem();
                if(currentOffer == null) return;

                acceptButton.setVisible(true);

                String[] splitCurrentOffer = currentOffer.split(" - ");
                currentPlayer = splitCurrentOffer[0];
                curBuyer = splitCurrentOffer[1];
                curFee = Double.parseDouble(splitCurrentOffer[2].replace("€", "").replace("M", "").trim());
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

    public void backToMenu(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("ClubMenu.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/Stylings/Styles2.css")).toExternalForm());

        stage.setScene(scene);
        stage.show();
    }

    public void filter(ActionEvent event) throws IOException{
        String selectedPos = posList.getValue();
        String selectedNation = nationList.getValue();
        String selectedRating = ratingList.getValue();
        String selectedSalary = salaryList.getValue();

        OfferList.getItems().clear();

        for(Player playerInfo : players){
            if(selectedPos != null && !playerInfo.pos.equals(selectedPos)){
                continue;
            }
            if(selectedNation != null && !playerInfo.nation.equals(selectedNation)){
                continue;
            }
            if(selectedRating != null){
                int minRating = Integer.parseInt(selectedRating.replace("+",""));
                if(playerInfo.rating < minRating){
                    continue;
                }
            }
            if(selectedSalary != null){
                double minSalary = Double.parseDouble(selectedSalary.replace("+",""));
                if(playerInfo.salary < minSalary){
                    continue;
                }
            }

            OfferList.getItems().add(playerInfo.name + " - " + teams[playerInfo.buyer] + " - €" + playerInfo.fee + "M");
        }
    }

    public void resetFilter(){
        posList.setValue(null);
        nationList.setValue(null);
        ratingList.setValue(null);
        salaryList.setValue(null);

        OfferList.getItems().clear();

        for (int i = players.size() - 1; i >= 0; i--){
            String name = players.get(i).name;
            int buyer = players.get(i).buyer;
            double fee = players.get(i).fee;
            OfferList.getItems().add(name + " - " + teams[buyer] + " - €" + fee + "M");
        }
    }

    public void sellPlayer(){
        stage = (Stage) acceptButton.getScene().getWindow();
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
                Player curPlayer = getPlayer(currentPlayer);
                PlayerClient.sendCommand("RS",curPlayer);
                OfferList.getItems().remove(currentOffer);
                players.remove(curPlayer);
            });
            reject.setOnAction(event -> {
                warningBox.close();
                Player curPlayer = getPlayer(currentPlayer);
                PlayerClient.sendCommand("RR",curPlayer);
                OfferList.getItems().remove(currentOffer);
                players.remove(curPlayer);
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

            warningBox.setX(stage.getX() + (stage.getWidth()  - 600) / 2);
            warningBox.setY(stage.getY() + (stage.getHeight() - 350) / 2);

            warningBox.showAndWait();
        }
    }
}
