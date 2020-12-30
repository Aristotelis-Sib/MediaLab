package sample;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;

import static sample.Popup.*;

public class BattleshipGame {

    protected static BorderPane root = new BorderPane();
    protected Moves game=new Moves();
    protected MenuBar menuBar;
    protected Parent createContent() {

        root.setPrefSize(800, 800);
        game.createPlayerBoard();
        game.createEnemyBoard();
        game.initBoard(Moves.playerBoard,Popup.userInput);
        game.initBoard(Moves.enemyBoard,Popup.userInput);
        Moves.id=Popup.userInput;
        HBox vbox = new HBox(50,Moves.playerBoard ,Moves.enemyBoard);
        menuBar = new MenuBar();
        menuBar.getMenus().addAll(applicationMenu(),detailsMenu());
        VBox topVBox =new VBox(40,menuBar,getStats(game));
        root.setTop(topVBox);
        vbox.setAlignment(Pos.CENTER);
        root.setCenter(vbox);
        game.addPropertyChangeListener(new listener());
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
        MyNode info3 = new MyNode( "    Hit   \n accuracy (%)",gridWidth, gridHeight,infoColor);

        MyNode player1 = new MyNode( "Player", gridWidth*1.4, gridHeight,infoColor);
        MyNode player2 = new MyNode(String.valueOf(game.getPlayerShips()) , gridWidth, gridHeight,dataColor);
        MyNode player3 = new MyNode(String.valueOf(game.getPlayerPoints()) ,gridWidth, gridHeight,dataColor);
        MyNode player4 = new MyNode(String.valueOf(dc.format(game.getPlayerAcc())) ,gridWidth, gridHeight,dataColor);

        MyNode computer1 = new MyNode( "Computer", gridWidth*1.4, gridHeight,infoColor);
        MyNode computer2 = new MyNode( String.valueOf(game.getEnemyShips()) , gridWidth, gridHeight,dataColor);
        MyNode computer3 = new MyNode( String.valueOf(game.getEnemyPoints()) ,gridWidth, gridHeight,dataColor);
        MyNode computer4 = new MyNode( String.valueOf(dc.format(game.getEnemyAcc())) ,gridWidth, gridHeight,dataColor);

        HBox info = new HBox( info0,info1,info2,info3);
        info.setAlignment(Pos.CENTER);
        HBox player = new HBox( player1,player2,player3,player4);
        player.setAlignment(Pos.CENTER);
        HBox computer = new HBox( computer1,computer2,computer3,computer4);
        computer.setAlignment(Pos.CENTER);
        return new VBox(info,player,computer);
    }

    public static class MyNode extends StackPane {
        public MyNode( String text,double width, double height,Color color ) {

            // create rectangle
            Rectangle rectangle = new Rectangle( width, height);
            rectangle.setStyle("-fx-border-style: solid; -fx-stroke: black; -fx-stroke-width: 2; -fx-stroke-height: 2; -fx-border-width: 3; -fx-border-height: 3; -fx-border-color: black; -fx-min-width: 20; -fx-min-height:20; -fx-max-width:20; -fx-max-height: 20;");
            rectangle.setFill(color);

            // create label
            Label label = new Label( text);
            label.setFont(Font.font("Helvetica", 18));
            label.setStyle("-fx-font-weight: bold; -fx-font-color: black;");
            getChildren().addAll( rectangle, label);

        }
    }
    public class listener implements PropertyChangeListener {
        public void propertyChange(PropertyChangeEvent evt) {
            VBox topVBox =new VBox(40,menuBar,getStats(game));
            root.setTop(topVBox);
        }
    }

    private MenuItem exitMenuItem() {
        MenuItem exitMenuItem = new MenuItem("Exit");
        exitMenuItem.setOnAction(actionEvent -> Platform.exit());
        return exitMenuItem;
    }

    private Menu applicationMenu() {
        Menu fileMenu = new Menu("Application");
        fileMenu.setStyle("-fx-font-weight: bold; -fx-border-style: solid;-fx-border-width: 3; -fx-border-color: black");
        MenuItem start = new MenuItem("Start");
        start.setOnAction(actionEvent -> {
            game.reset();
            game.initBoard(Moves.enemyBoard, Popup.userInput);
            game.initBoard(Moves.playerBoard, Popup.userInput);
            game.running=true;
            game.observer();
            game.setRandomPlayer();
        });

        MenuItem load = new MenuItem("Load");
        load.setOnAction(actionEvent -> displayLoad(start));

        fileMenu.getItems().addAll(start, load,
                new SeparatorMenuItem(), exitMenuItem());
        return fileMenu;
    }

    private Menu detailsMenu() {
        Menu details = new Menu("Details");
        details.setStyle("-fx-font-weight: bold; -fx-border-style: solid;-fx-border-width: 3; -fx-border-color: black");

        MenuItem enemyShips = new MenuItem("Enemy Ships");
        enemyShips.setOnAction(actionEvent -> displayEnemyShips(Moves.enemyBoard));

        MenuItem playerShots = new MenuItem("Player Shots");
        playerShots.setOnAction(actionEvent -> displayShots(game.playerStack,true));

        MenuItem enemyShots = new MenuItem("Enemy Shots");
        enemyShots.setOnAction(actionEvent -> displayShots(game.enemyStack,false));

        details.getItems().addAll(enemyShips, playerShots,enemyShots);

        return details;
    }


}
