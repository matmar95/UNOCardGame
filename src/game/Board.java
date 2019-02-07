package game;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Board extends Application {

    public void start(Stage boardStage){
        StackPane root = new StackPane();
        root.setId("boardpane");
        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().addAll(this.getClass().getResource("view/style.css").toExternalForm());
        Region background = new Region();
        background.setPrefSize(800,600);
        boardStage.setScene(scene);
        boardStage.setTitle("UNO");
        boardStage.setResizable(false);
        boardStage.show();
    }
/*
    private Parent createContent(){
        Pane root = new Pane();
        root.setPrefSize(800,600);
        Region background =  new Region();
        background.setPrefSize(800,600);
        background.setStyle();

    }*/
}
