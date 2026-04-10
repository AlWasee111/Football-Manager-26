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
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
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
    @FXML
    private ComboBox<String> ratingList;
    @FXML
    private ComboBox<String> posList;
    @FXML
    private ComboBox<String> nationList;
    @FXML
    private ComboBox<String> salaryList;
    @FXML
    private Button salaryButton;
    @FXML
    private Button resetFilter;
    @FXML
    private Button filterButton;
    @FXML
    private Button backButton;

    private Stage stage;
    private Scene scene;
    private Parent root;

    private String currentPlayer;

    int idx = SelectedClub.clubIndex;

    String[] squads = {"Squads/FCBsquad.txt", "Squads/ARSsquad.txt", "Squads/CHEsquad.txt", "Squads/MUsquad.txt",
            "Squads/RMsquad.txt", "Squads/BMsquad.txt", "Squads/PSGsquad.txt", "Squads/MCsquad.txt"};

    String[] ratings = {"60+","70+","80+","85+","90+"};
    String[] nations = {"ARG", "AUS", "BEL", "BRA", "CAM", "COL", "CRO", "DEN", "ECU", "EGY", "ENG", "ESP", "FRA", "GER", "GHA", "GOR", "ITA", "IVO", "JAP", "KOR", "MOR", "NED", "NOR", "POL", "POR", "RUS", "SEN", "SLO", "SWE", "TUR", "UKR", "URU", "UZB"};
    String[] positions = {"GK","CB","RB","LB","CDM","CM","CAM","RM","LM","LW","RW","ST"};
    String[] salaries = {"30000+", "40000+", "50000+", "60000+", "70000+", "80000+", "90000+", "100000+", "110000+", "120000+", "130000+", "140000+", "150000+", "160000+", "170000+", "180000+", "190000+", "200000+", "210000+", "220000+", "230000+", "240000+", "250000+", "260000+", "270000+", "280000+", "290000+", "300000+", "310000+", "320000+", "330000+", "340000+", "350000+", "360000+", "370000+", "380000+", "390000+", "400000+"};

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
        ratingList.getItems().addAll(ratings);
        posList.getItems().addAll(positions);
        nationList.getItems().addAll(nations);
        salaryList.getItems().addAll(salaries);

        for (Player playerInfo : playerInfos){
            PlayerList.getItems().add(playerInfo.name);
        }

        PlayerList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                currentPlayer = PlayerList.getSelectionModel().getSelectedItem();
                if (currentPlayer == null) return;

                sellButton.setVisible(true);
                salaryButton.setVisible(true);
                playerLabel.setText(currentPlayer);
                String cardPath = getPlayer(currentPlayer).cardPath;
                Image image = new Image(getClass().getResourceAsStream("/playerCards/" + cardPath + ".png"));
                playerCard.setImage(image);
            }
        });
        addSoundEffects(backButton);
        addSoundEffects(salaryButton);
        addSoundEffects(filterButton);
        addSoundEffects(resetFilter);
        addSoundEffects(sellButton);
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

    public void filter(ActionEvent event) throws IOException{
        String selectedPos = posList.getValue();
        String selectedNation = nationList.getValue();
        String selectedRating = ratingList.getValue();
        String selectedSalary = salaryList.getValue();

        PlayerList.getItems().clear();

        for(Player playerInfo : playerInfos){
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

            PlayerList.getItems().add(playerInfo.name);
        }
    }

    public void resetFilter(){
        posList.setValue(null);
        nationList.setValue(null);
        ratingList.setValue(null);
        salaryList.setValue(null);

        PlayerList.getItems().clear();

        for(Player playerInfo : playerInfos){
            PlayerList.getItems().add(playerInfo.name);
        }
    }

    private void addSoundEffects(Button button) {

        AudioClip hoverSound = new AudioClip(
                getClass().getResource("/music/hovering.mp3").toExternalForm()
        );

        AudioClip clickSound = new AudioClip(
                getClass().getResource("/music/clicking.mp3").toExternalForm()
        );

        hoverSound.setVolume(2.0 * volume.sfxVol);
        clickSound.setVolume(1.6 * volume.sfxVol);

        button.setOnMouseEntered(e -> {
            hoverSound.play();
        });

        button.addEventHandler(ActionEvent.ACTION, e -> {
            clickSound.play();
        });
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
            priceField.setMaxWidth(300);

            Label errorMessage = new Label("");
            errorMessage.getStyleClass().add("error-text");

            Button confirm = new Button("Confirm");
            Button cancel = new Button("Cancel");

            addSoundEffects(confirm);
            addSoundEffects(cancel);

            confirm.getStyleClass().add("warning-button");
            cancel.getStyleClass().add("warning-button");

            confirm.setOnAction(event -> {
                String input = priceField.getText().trim();
                double fee;
                try{
                    fee = Double.parseDouble(input);
                    if(fee < 0 || input == null){
                        errorMessage.setText("Invalid Transfer Fee! Enter a positive number.");
                    }
                    else {
                        warningBox.close();
                        Player curPlayerInfo = getPlayer(currentPlayer);
                        curPlayerInfo.setSeller(SelectedClub.clubIndex);
                        curPlayerInfo.setFee(fee);
                        PlayerClient.sendCommand("S",curPlayerInfo);
                        PlayerList.getItems().remove(currentPlayer);
                        playerInfos.remove(curPlayerInfo);
                    }
                }
                catch (Exception e){
                    errorMessage.setText("Invalid Transfer Fee! Enter a positive number.");
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

    public void editSalary(){
        stage = (Stage) sellButton.getScene().getWindow();
        if(!currentPlayer.isEmpty()){
            Stage warningBox = new Stage();
            warningBox.initStyle(StageStyle.UNDECORATED); //removes default top bar
            warningBox.initModality(Modality.APPLICATION_MODAL); //can't access other stuffs

            Player curPlayerInfo = getPlayer(currentPlayer);

            Label message = new Label("Set new salary for " + currentPlayer);
            message.getStyleClass().add("warning-message");

            Label currentSalaryLabel = new Label("Current Salary: €" + String.format("%,.0f", curPlayerInfo.salary));
            currentSalaryLabel.getStyleClass().add("warning-message");

            TextField priceField = new TextField();
            //priceField.setPrefHeight(45);
            //priceField.setPrefWidth(250);
            priceField.setPromptText("Enter Salary");
            priceField.getStyleClass().add("text-field");
            priceField.setMaxWidth(300);

            Label errorMessage = new Label("");
            errorMessage.getStyleClass().add("error-text");

            Button confirm = new Button("Confirm");
            Button cancel = new Button("Cancel");

            addSoundEffects(confirm);
            addSoundEffects(cancel);

            confirm.getStyleClass().add("warning-button");
            cancel.getStyleClass().add("warning-button");

            confirm.setOnAction(event -> {
                String input = priceField.getText().trim();
                try {
                    double newSalary = Double.parseDouble(input);

                    if (newSalary < 0) {
                        errorMessage.setText("Invalid salary! Enter a positive number.");
                    } else {
                        warningBox.close();
                        curPlayerInfo.salary = newSalary;
                        curPlayerInfo.setSeller(SelectedClub.clubIndex);

                        try {
                            FileWriter writer = new FileWriter(file, false);
                            for (Player p : playerInfos) {
                                writer.write(p.name + "," + p.pos + "," + p.rating + ","
                                        + p.salary + "," + p.nation + "," + p.cardPath + "\n");
                                //Kai Havertz,ST,82,220000,GER,Havertz
                            }
                            writer.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (NumberFormatException e) {
                    errorMessage.setText("Invalid input! Enter a numeric value.");
                }
            });
            cancel.setOnAction(event -> {
                warningBox.close();
            });

            cancel.setOnAction(event -> warningBox.close());

            HBox buttons = new HBox(25, confirm, cancel);
            buttons.setAlignment(Pos.CENTER);

            VBox root = new VBox(40, message, currentSalaryLabel, priceField, errorMessage, buttons);
            root.setAlignment(Pos.CENTER);
            root.getStyleClass().add("warning-pane");

            Scene scene = new Scene(root, 600, 400);
            scene.getStylesheets().add(getClass().getResource("/Stylings/AlertStyle3.css").toExternalForm());

            warningBox.setScene(scene);
            warningBox.setX(stage.getX() + (stage.getWidth()  - 600) / 2);
            warningBox.setY(stage.getY() + (stage.getHeight() - 400) / 2);
            warningBox.showAndWait();
        }
    }
}
