package  sample;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage){
        BattleshipGame battleshipgame = new BattleshipGame();
        Scene scene = new Scene(battleshipgame.createContent());
        primaryStage.setTitle("MediaLab Battleship");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
