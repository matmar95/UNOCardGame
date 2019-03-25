package game.controller;

import game.model.StatusRegistry;
import game.network.NodeHealthCheckService;
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
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
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
    Button readyButton;
    @FXML
    Rectangle loadingGif;
    @FXML
    VBox vBoxLabels;

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
            new Thread(new NodeHealthCheckService()).start();
            String ipAddress = NetworkManager.getInstance().getMyNode().getIpAddress();
            int portNum = NetworkManager.getInstance().getMyNode().getPort();
            createPane.setVisible(true);
            Label userLabel = new Label();
            userLabel.setText("Ip Address: " + ipAddress + "\tPort: " + portNum);
            userLabel.setFont(new Font("Arial Rounded MT Bold",14));
            userLabel.setTextFill(Color.rgb(242,234,234,1));
            vBoxLabels.getChildren().add(userLabel);
            createButton.setDisable(true);
            joinButton.setDisable(true);
            userField.setDisable(true);

        }else{
            alertUsername();
        }
    }

    public void joinButtonPress(ActionEvent event){
        if (!userField.getText().isEmpty()){
            this.username = userField.getText().trim();
            createPane.setVisible(false);
            joinPane.setVisible(true);
            joinButton.setDisable(true);
            userField.setDisable(true);
            NetworkManager.getInstance().initialize(username);
            new Thread(new NodeHealthCheckService()).start();
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

            if((!NetworkManager.getInstance().getMyNode().getIpAddress().equals(this.ip) && NetworkManager.getInstance().getMyNode().getPort()==this.port)||
                    (NetworkManager.getInstance().getMyNode().getIpAddress().equals(this.ip) && NetworkManager.getInstance().getMyNode().getPort()!=this.port) ||
                    (!NetworkManager.getInstance().getMyNode().getIpAddress().equals(this.ip) && NetworkManager.getInstance().getMyNode().getPort()!=this.port)) {
                if(new NetworkClusterServices().canJoinCluster(new PlayerNode(this.ip,this.port))) {
                    if (new NetworkClusterServices().joinTheCluster(new PlayerNode(this.ip, this.port))) {
                        this.sourcePanel = (Node) event.getSource();
                        createButton.setDisable(true);
                        joinButton.setDisable(true);
                        loadingGif.setVisible(true);
                        readyButton.setDisable(true);
                        ipField.setDisable(true);
                        portField.setDisable(true);
                        loadingGif.setFill(new ImagePattern(new Image("/image_assets/loading.gif")));
                    }
                } else {
                    showDialogError("Max player numbers reached for this game");
                }
            } else {
                showDialogError("You can't connect to yourself");
            }
        }else{
            alertUsername();
        }
    }

    public void startGame(ActionEvent event) throws RemoteException {
        this.sourcePanel = (Node) event.getSource();
        StatusRegistry.getInstance().setFirst(true);
        (new GameController()).startNewGame(NetworkManager.getInstance().getMyNode(), new Random().nextLong());
    }

    public void addPlayerToLabel(PlayerNode player){
        Platform.runLater(()-> {
            Label label = new Label();
            label.setText("Player " + player.getUsername() + " has joined the cluster");
            label.setFont(new Font("Arial Rounded MT Bold",14));
            label.setTextFill(Color.rgb(242,234,234,1));
            vBoxLabels.getChildren().add(label);
        });
    }

    public void showDialogError(String msg){
        Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.CLOSE);
        alert.setTitle("");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.CLOSE) {
            alert.close();
        }
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

            } catch (
                    IOException e) {
                e.printStackTrace();
            }
        });
    }
}
