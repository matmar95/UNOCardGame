package game.controller;

import game.model.StatusRegistry;
import game.network.NetworkClusterServices;
import game.network.NetworkManager;
import game.network.PlayerNode;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import utils.Logger;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Random;

public class HomeUIController {

    Logger LOG = new Logger(HomeUIController.class);
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
    private Node sourcePanel;

    private static HomeUIController instance;

    public static HomeUIController getInstance(){
        return instance;
    }

    @FXML
    public void initialize(){
        instance = this;
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
            this.sourcePanel = (Node) event.getSource();
            //createButton.setDisable(true);
            //joinButton.setDisable(true);
        }else{
            alertUsername();
        }
    }

    public void startGame(ActionEvent event) throws RemoteException {
        this.sourcePanel = (Node) event.getSource();
        StatusRegistry.getInstance().setFirst(true);
        (new GameController()).startNewGame(NetworkManager.getInstance().getMyNode(), new Random().nextLong());
    }

    public void launchGameUI(){
        Platform.runLater(()-> {
            Parent root;
            try {
                sourcePanel.getScene().getWindow().hide();
                root = FXMLLoader.load(getClass().getClassLoader().getResource("game/view/Game.fxml"));
                Stage stage = new Stage();
                stage.setTitle("UNO-Game");
                stage.setScene(new Scene(root));
                stage.setResizable(false);
                stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

                    @Override
                    public void handle(WindowEvent event) {
                        Platform.runLater(new Runnable() {

                            @Override
                            public void run() {
                                System.out.println("Application Closed by click to Close Button(X)");
                                System.exit(0);
                            }
                        });
                    }
                });
                stage.show();
                // Hide this current window (if this is what you want)

            } catch (
                    IOException e) {
                e.printStackTrace();
            }
        });
    }
}