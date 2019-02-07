import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends Application{
    Parent parent = null;
    public static final String UI_FORM = "game/view/Home.fxml";
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage){
        URL form = this.getClass().getClassLoader().getResource(UI_FORM);
        if (form == null) {
            Logger.getLogger("GameApp").log(Level.SEVERE, "Couldn't file FXML form " + UI_FORM);
            return;
        }
        //Parent root = FXMLLoader.load(getClass().getResource("game/Home.fxml"));
        try {
            parent = FXMLLoader.load(form);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Scene scene = new Scene(parent);
        primaryStage.setTitle("UNO");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

    }
}