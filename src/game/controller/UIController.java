package game.controller;

import game.network.NetworkClusterServices;
import game.network.NetworkManager;
import game.network.PlayerNode;
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
import utils.Logger;
import utils.NetworkUtils;

import java.io.File;
import java.io.IOException;

public class UIController {

    Logger LOG = new Logger(UIController.class);
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
    Button readyButton;

    private String ip;
    private String username;
    private int port;

    @FXML
    public void initialize(){
    }

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
            this.username = userField.getText().trim();
            joinPane.setVisible(false);
            NetworkManager.getInstance().initialize(username);
            String ipAddress = NetworkManager.getInstance().getMyNode().getIpAddress();
            int portNum = NetworkManager.getInstance().getMyNode().getPort();
            createPane.setVisible(true);
            createLabel.setText("Ip Address: " + ipAddress + "\tPort: " + portNum);
            //LOG.info("Your Ip Address is: " + ipAddress + ":" + portNum);
            //createButton.setDisable(true);
            //joinButton.setDisable(true);

        }else{
            alertUsername();
        }
    }

    public void joinButtonPress(ActionEvent event){
        if (!userField.getText().isEmpty()){
            this.username = userField.getText().trim();
            createPane.setVisible(false);
            joinPane.setVisible(true);
            //createButton.setDisable(true);
            //joinButton.setDisable(true);
        }else{
            alertUsername();
        }
    }

    public void readyButtonPress(ActionEvent event){
        if (!ipField.getText().isEmpty() && !portField.getText().isEmpty()){
            this.ip = ipField.getText().trim();
            this.port = Integer.parseInt(portField.getText().trim());
            createPane.setVisible(false);
            joinPane.setVisible(true);
            NetworkManager.getInstance().initialize(username);
            new NetworkClusterServices().joinTheCluster(new PlayerNode(this.ip, this.port));
            //createButton.setDisable(true);
            //joinButton.setDisable(true);
        }else{
            alertUsername();
        }
    }

    public void startGame(ActionEvent event) {
    }
}
