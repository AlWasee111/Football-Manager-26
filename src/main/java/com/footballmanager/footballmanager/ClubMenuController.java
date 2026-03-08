package com.footballmanager.footballmanager;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

public class ClubMenuController {
    private Stage stage;
    private Scene scene;
    private Parent root;
    public void GoToMainMenu (ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("mainmenu.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        logout(stage);
    }
    public void GoToPlayers(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("PlayerList.fxml"));
        root = loader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        //scene = new Scene(root);
        scene = new Scene(root, stage.getScene().getWidth(), stage.getScene().getHeight());
        scene.getStylesheets().add(getClass().getResource("/Stylings/ListStyle.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
    public void GoToTransferHub(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TransferHub.fxml"));
        root = loader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        //scene = new Scene(root);
        scene = new Scene(root, stage.getScene().getWidth(), stage.getScene().getHeight());
        scene.getStylesheets().add(getClass().getResource("/Stylings/TransferHub.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    public void logout(Stage stage){
        Stage warningBox = new Stage();
        warningBox.initStyle(StageStyle.UNDECORATED); //removes default top bar
        warningBox.initModality(Modality.APPLICATION_MODAL); //can't access other stuffs

        Label message = new Label("Are you sure you want to logout?");
        message.getStyleClass().add("warning-message");

        Button yes = new Button("Yes");
        Button no = new Button("No");

        yes.getStyleClass().add("warning-button");
        no.getStyleClass().add("warning-button");

        yes.setOnAction(event -> {
            warningBox.close();
            scene = new Scene(root);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/Stylings/Styles.css")).toExternalForm());
            stage.setScene(scene);
            stage.show();
        });
        no.setOnAction(event -> {
            warningBox.close();
        });

        HBox buttons = new HBox(25, yes, no);
        buttons.setAlignment(Pos.CENTER);

        VBox root = new VBox(40,message,buttons);
        root.setAlignment(Pos.CENTER);

        root.getStyleClass().add("warning-pane");

        Scene scene = new Scene(root, 600, 350);
        scene.getStylesheets().add(getClass().getResource("/Stylings/AlertStyle2.css").toExternalForm());

        warningBox.setScene(scene);
        warningBox.showAndWait();
    }
}
