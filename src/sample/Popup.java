package sample;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Popup {
    public static void display(String winner)
    {
        Stage popUpWindow=new Stage();
        popUpWindow.initModality(Modality.APPLICATION_MODAL);
        popUpWindow.setTitle("This is a pop up window");
        Label label1= new Label(winner);
        Button button1= new Button("Close this pop up window");
        button1.setOnAction(e -> popUpWindow.close());
        VBox layout= new VBox(10);
        layout.getChildren().addAll(label1, button1);
        layout.setAlignment(Pos.CENTER);
        Scene scene1= new Scene(layout, 300, 250);
        popUpWindow.setScene(scene1);
        popUpWindow.showAndWait();
    }

}
