package sample;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.text.DecimalFormat;

public class BattleshipGame {
    protected Parent createContent() {
        BorderPane root = new BorderPane();
        root.setPrefSize(600, 800);

//      Start Game
        Moves game = new Moves();

        Board playerBoard=game.getPlayerBoard();
        Board enemyBoard=game.getEnemyBoard();



        HBox vbox = new HBox(50,playerBoard ,enemyBoard);
        game.running = true;
        vbox.setAlignment(Pos.CENTER);
        root.setCenter(vbox);
        int num1=game.getEnemyShips();
        root.setTop(getStats(game));

        EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                root.setTop(getStats(game));
            }
        };
        root.addEventFilter( MouseEvent.ANY, eventHandler);

        return root;
    }

    public VBox getStats(Moves game){

        double gridWidth = 150;
        double gridHeight = 50;

        DecimalFormat dc = new DecimalFormat("0.00");
        Color infoColor=Color.BLUE;
        Color dataColor=Color.WHITE;
        MyNode info0= new MyNode( "Game Stats", gridWidth*1.4, gridHeight,infoColor);
        MyNode info1 = new MyNode( "Active Ships", gridWidth, gridHeight,infoColor);
        MyNode info2 = new MyNode( "Points" ,gridWidth, gridHeight,infoColor);
        MyNode info3 = new MyNode( "Hit acc",gridWidth, gridHeight,infoColor);

        MyNode player1 = new MyNode( "Player", gridWidth*1.4, gridHeight,infoColor);
        MyNode player2 = new MyNode(String.valueOf(game.getPlayerShips()) , gridWidth, gridHeight,dataColor);
        MyNode player3 = new MyNode(String.valueOf(game.getPlayerPoints()) ,gridWidth, gridHeight,dataColor);
        MyNode player4 = new MyNode(String.valueOf(dc.format(game.getPlayerAcc())) ,gridWidth, gridHeight,dataColor);

        MyNode computer1 = new MyNode( "Computer", gridWidth*1.4, gridHeight,infoColor);
        MyNode computer2 = new MyNode( String.valueOf(game.getEnemyShips()) , gridWidth, gridHeight,dataColor);
        MyNode computer3 = new MyNode( String.valueOf(game.getEnemyPoints()) ,gridWidth, gridHeight,dataColor);
        MyNode computer4 = new MyNode( String.valueOf(dc.format(game.getEnemyAcc())) ,gridWidth, gridHeight,dataColor);

        HBox info = new HBox( info0,info1,info2,info3);
        HBox player = new HBox( player1,player2,player3,player4);
        HBox computer = new HBox( computer1,computer2,computer3,computer4);
        VBox VBoxStats = new VBox(info,player,computer);

        return VBoxStats;
    }

    public static class MyNode extends StackPane {
        public MyNode(){

        }

        public MyNode( String name,double width, double height,Color color ) {

            // create rectangle
            Rectangle rectangle = new Rectangle( width, height);
            rectangle.setStyle("-fx-background-color: white; -fx-border-style: solid; -fx-stroke: black; -fx-stroke-width: 2; -fx-stroke-height: 2; -fx-border-width: 30; -fx-border-color: black; -fx-min-width: 20; -fx-min-height:20; -fx-max-width:20; -fx-max-height: 20;");
            rectangle.setFill(color);

            // create label
            Label label = new Label( name);
            label.setFont(new Font("Arial", 18));
            label.setStyle("-fx-font-weight: bold;");
            getChildren().addAll( rectangle, label);

        }
    }


}
