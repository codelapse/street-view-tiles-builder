package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = loader.load();
        sample s = (sample)loader.getController();
        s.stage = primaryStage;

        Scene scene = new Scene(root, 600, 450);
        scene.getStylesheets().add("sample/style.css");

        primaryStage.setTitle("StreetView Tiles Builder");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
