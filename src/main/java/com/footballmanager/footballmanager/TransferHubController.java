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
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

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

    File file = new File("src/main/resources/Squads/TransferList.txt");
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
            TransHubList.getItems().add(player);
        }

        TransHubList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                buyButton.setVisible(true);
                currentPlayer = TransHubList.getSelectionModel().getSelectedItem();
                playerLabel.setText(currentPlayer);
            }
        });
    }

    public void buyPlayer(ActionEvent event) throws IOException{
        if(!currentPlayer.isEmpty()){
            players.remove(currentPlayer);

            PrintWriter printWriter = new PrintWriter(file);
            for(String player : players){
                printWriter.println(player);
            }
            printWriter.close();

            int idx = SelectedClub.clubIndex;
            String[] squads = {"Squads/FCBsquad.txt", "Squads/ARSsquad.txt", "Squads/CHEsquad.txt", "Squads/MUsquad.txt",
                    "Squads/RMsquad.txt", "Squads/BMsquad.txt", "Squads/PSGsquad.txt", "Squads/MCsquad.txt"};

            File file = new File("src/main/resources/" + squads[idx]);
            FileWriter fileWriter = new FileWriter(file,true);
            fileWriter.write(currentPlayer + "\n");
            fileWriter.close();

            TransHubList.getItems().remove(currentPlayer);
        }
    }
}
