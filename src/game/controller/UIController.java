package game.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import utils.NetworkUtils;

import java.io.File;
import java.io.IOException;

public class UIController {

    @FXML
    TextField userField;
    @FXML
    TextField ipField;
    @FXML
    TextField portField;
    @FXML
    Pane createPane;
    @FXML
    Pane joinPane;
    @FXML
    Button joinButton;
    @FXML
    Button createButton;
    @FXML
    Label createLabel;

    @FXML
    public void initialize(){
    }

    String username = null;

    private void alertUsername(){
        Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid Username", ButtonType.OK);
        alert.setTitle("Error");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            alert.close();
        }
    }
    public void createButtonPress(ActionEvent event){
        if (!userField.getText().isEmpty()){
            username = userField.getText().trim();
            final String ipAddress = NetworkUtils.getIpAddress();
            createLabel.setText("Your Ip Address is: " + ipAddress);
            createPane.setVisible(true);
            joinPane.setVisible(false);
            //createButton.setDisable(true);
            //joinButton.setDisable(true);

        }else{
            alertUsername();
        }
    }

    public void joinButtonPress(ActionEvent event){
        if (!userField.getText().isEmpty()){
            username = userField.getText().trim();
            createPane.setVisible(false);
            joinPane.setVisible(true);
            //createButton.setDisable(true);
            //joinButton.setDisable(true);
        }else{
            alertUsername();
        }
    }
    public void startGame(ActionEvent event){
        Button b = (Button)event.getSource();

        if (!userField.getText().isEmpty()){
            username = userField.getText().trim();

            /*Parent game;
            try {
                game = FXMLLoader.load(getClass().getClassLoader().getResource("game/view/Game.fxml"));
                Stage stage = new Stage();
                stage.setTitle("UNO - Game mode");
                stage.setScene(new Scene(game));
                stage.show();
                ((Node)(event.getSource())).getScene().getWindow().hide();
            }
            catch (IOException e) {
                e.printStackTrace();
            }*/
        }else{
            alertUsername();
        }
        String ip = ipField.getText().trim();
        String port = portField.getText().trim();

    }
}
