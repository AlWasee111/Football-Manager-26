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

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ScoutTeamsController implements Initializable {
    @FXML
    private ListView<String> TeamList;
    @FXML
    private Label TeamLabel;
    @FXML
    private Button scoutButton;

    String selectedTeam;

    private Stage stage;
    private Scene scene;
    private Parent root;

    String[] teams = {"FC Barcelona", "Arsenal FC" , "Chelsea FC", "Manchester United",
            "Real Madrid CF", "FC Bayern München", "Paris Saint-Germain", "Manchester City"};

    int teamIncharge = SelectedClub.clubIndex;
    static int scoutIDX;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for(int i = 0; i < teams.length; i++){
            if(i != teamIncharge){
                TeamList.getItems().add(teams[i]);
            }
        }

        TeamList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                scoutButton.setVisible(true);
                selectedTeam = TeamList.getSelectionModel().getSelectedItem();
                TeamLabel.setText(selectedTeam);
                for(int i = 0; i < teams.length; i++){
                    if(selectedTeam.equals(teams[i])){
                        scoutIDX = i;
                        break;
                    }
                }
            }
        });
    }

    public void GoToScoutTeamPlayer(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ScoutTeamSquad.fxml"));
        root = loader.load();
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root, stage.getScene().getWidth(), stage.getScene().getHeight());
        scene.getStylesheets().add(getClass().getResource("/Stylings/ScoutTeamsSquad.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    public void backToMenu(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("ClubMenu.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/Stylings/Styles2.css")).toExternalForm());

        stage.setScene(scene);
        stage.show();
    }
}
