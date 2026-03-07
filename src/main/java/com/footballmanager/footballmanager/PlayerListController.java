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

    String[] squads = {"FCBsquad.txt", "ARSsquad.txt", "CHEsquad.txt", "MUsquad.txt",
            "RMsquad.txt", "BMsquad.txt", "PSGsquad.txt", "MCsquad.txt"};

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
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/Styles2.css")).toExternalForm());

        stage.setScene(scene);
        stage.show();
    }

    public void sellPlayer(ActionEvent event) throws IOException{
        if(!currentPlayer.isEmpty()){
            players.remove(currentPlayer);

            PrintWriter printWriter = new PrintWriter(file);
            for(String player : players){
                printWriter.println(player);
            }
            printWriter.close();

            File file1 = new File("src/main/resources/TransferList.txt");
            FileWriter fileWriter = new FileWriter(file1,true);
            fileWriter.write(currentPlayer + "\n");
            fileWriter.close();
        }
    }
}
