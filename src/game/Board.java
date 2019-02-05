package game;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Board extends Application {

    public void start(Stage boardStage){
        StackPane root = new StackPane();
        root.setId("boardpane");
        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().addAll(this.getClass().getResource("view/style.css").toExternalForm());
        boardStage.setScene(scene);
        boardStage.setTitle("UNO");
        boardStage.show();
    }
}
