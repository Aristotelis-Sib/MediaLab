package sample;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

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
        //Initialize game
        game.createPlayerBoard();
        game.createEnemyBoard();
        game.initBoard(Moves.playerBoard,Popup.userInput);  //initiate player and enemy board
        game.initBoard(Moves.enemyBoard,Popup.userInput);
        game.reset();
        game.addPropertyChangeListener(new listener());  //Listener for updating table with stats and remaining shots
        Moves.id=Popup.userInput;
        //Create and add top menu bar and table with statistics in top part of window
        menuBar = new MenuBar();
        menuBar.getMenus().addAll(applicationMenu(),detailsMenu());
        VBox topVBox =new VBox(40,menuBar,getStats(game));
        root.setTop(topVBox);
        //Create and add boards in center of window
        HBox vbox = new HBox(40,addTitle2Board(Moves.playerBoard,"Your Board") ,addTitle2Board(Moves.enemyBoard,"Enemy Board"));
        vbox.setAlignment(Pos.CENTER);
        root.setCenter(vbox);
        //Add Start button and Remaining shots counter in bottom of window
        VBox bottom=new VBox(10,start(),remainingShot(game) );
        bottom.setAlignment(Pos.CENTER);
        root.setBottom(bottom);
        return root;
    }
    public VBox addTitle2Board(Board board,String Title){
        //Add title above boards
        Label label= new Label(Title);
        label.setFont(new Font("Arial", 20));
        label.setStyle("-fx-font-weight: bold;");
        label.setAlignment(Pos.CENTER);
        VBox titledBoard= new VBox(15,label,board);
        titledBoard.setAlignment(Pos.CENTER);
        return titledBoard;
    }
    public VBox getStats(Moves game){
        //Creates array of MyNode cells (rectangles) with statistics of player and enemy (computer)
        double gridWidth = 150;
        double gridHeight = 50;

        DecimalFormat dc = new DecimalFormat("0.00");
        Color infoColor=Color.CYAN;
        Color dataColor=Color.WHITE;
        //Info bar ,first row
        MyNode info0= new MyNode( "Game Stats", gridWidth*1.4, gridHeight,infoColor);
        MyNode info1 = new MyNode( "Active Ships", gridWidth, gridHeight,infoColor);
        MyNode info2 = new MyNode( "Points" ,gridWidth, gridHeight,infoColor);
        MyNode info3 = new MyNode( "    Hit   \n accuracy (%)",gridWidth, gridHeight,infoColor);
        //Player row with stats
        MyNode player1 = new MyNode( "Player", gridWidth*1.4, gridHeight,infoColor);
        MyNode player2 = new MyNode(String.valueOf(game.getPlayerShips()) , gridWidth, gridHeight,dataColor);
        MyNode player3 = new MyNode(String.valueOf(game.getPlayerPoints()) ,gridWidth, gridHeight,dataColor);
        MyNode player4 = new MyNode(String.valueOf(dc.format(game.getPlayerAcc())) ,gridWidth, gridHeight,dataColor);
        //Enemy (Computer) row with stats
        MyNode computer1 = new MyNode( "Computer", gridWidth*1.4, gridHeight,infoColor);
        MyNode computer2 = new MyNode( String.valueOf(game.getEnemyShips()) , gridWidth, gridHeight,dataColor);
        MyNode computer3 = new MyNode( String.valueOf(game.getEnemyPoints()) ,gridWidth, gridHeight,dataColor);
        MyNode computer4 = new MyNode( String.valueOf(dc.format(game.getEnemyAcc())) ,gridWidth, gridHeight,dataColor);
        
        //Stacking each "cell" side by side for each row
        HBox info = new HBox( info0,info1,info2,info3);
        info.setAlignment(Pos.CENTER);
        HBox player = new HBox( player1,player2,player3,player4);
        player.setAlignment(Pos.CENTER);
        HBox computer = new HBox( computer1,computer2,computer3,computer4);
        computer.setAlignment(Pos.CENTER);
        //Return all rows stacked one above the other
        return new VBox(info,player,computer);
    }

    public static class MyNode extends StackPane {
        //Custom class for creating cells/rectangles with text to then create tables
        public MyNode( String text,double width, double height,Color color ) {
            this(text,width,height,color,2);
        }
        public MyNode( String text,double width, double height,Color color,int strokeWidth ) {

            // create rectangle
            Rectangle rectangle = new Rectangle( width, height);
            rectangle.setStyle("-fx-border-style: solid; -fx-stroke: black; -fx-stroke-width:"+strokeWidth+"; -fx-stroke-height: 2; -fx-border-width: 3; -fx-border-height: 3; -fx-border-color: black; -fx-min-width: 20; -fx-min-height:20; -fx-max-width:20; -fx-max-height: 20;");
            rectangle.setFill(color);

            // create label
            Label label = new Label( text);
            label.setFont(Font.font("Helvetica", 18));
            label.setStyle("-fx-font-weight: bold; -fx-font-color: black;");
            getChildren().addAll( rectangle, label);

        }
    }

    public VBox remainingShot(Moves game){
        //Creates table that shows remaining shots for
        double gridWidth = 150;
        double gridHeight = 50;
        Color infoColor=Color.CYAN;
//        Color dataColor=Color.WHITE;
        MyNode remaining_shots= new MyNode( "Remaining Shots", gridWidth*1.4, gridHeight,infoColor);
        MyNode numOfShots = new MyNode( String.valueOf(game.getPlayerRemMoves()), gridWidth*1.4, gridHeight,infoColor);
        return new VBox(remaining_shots,numOfShots);
    }
    
    //Observe  when there is a change in the state by the observable object
    public class listener implements PropertyChangeListener {
        public void propertyChange(PropertyChangeEvent evt) {
            //Update Stats table and remaining shots   (boards update by themselves)
            VBox topVBox =new VBox(40,menuBar,getStats(game));
            root.setTop(topVBox);
            VBox bottom=new VBox(10,start(),remainingShot(game) );
            bottom.setAlignment(Pos.CENTER);
            root.setBottom(bottom);
        }

    }
    //Create start button
    private Button start(){
        Button start= new Button("  Start  ");
        start.setOnAction(actionEvent -> {
            game.reset();
            //The .txt given at initBoard stage are already checked when loading new scenario
            //but for extra caution we implement a try catch.
            try {
                //Initiate boards based on user input
                game.initBoard(Moves.enemyBoard, userInput);
                game.initBoard(Moves.playerBoard, userInput);
                Moves.id= userInput;
            }catch (Exception e){
                //If exception is found "load" scenario with id 1 as default scenario
                Moves.id= "1";
                game.initBoard(Moves.enemyBoard, userInput);
                game.initBoard(Moves.playerBoard, userInput);
            }
            game.running=true;
            game.observer();
            game.setRandomPlayer();
        });
        start.setPrefWidth(160);
        start.setPrefHeight(50);
        Font font = Font.font("Courier New", FontWeight.BOLD, 20);
        start.setFont(font);
        start.setAlignment(Pos.CENTER);
        return start;
    }

    private Menu applicationMenu() {
        //Top bar menu
        Menu fileMenu = new Menu("Application");
//        fileMenu.setStyle("-fx-font-weight: bold; -fx-border-style: solid;-fx-border-width: 3; -fx-border-color: black");
        //Start button same as start function above
        MenuItem start = new MenuItem("Start");
        start.setOnAction(actionEvent -> {
            game.reset();
            try {
                game.initBoard(Moves.enemyBoard, Popup.userInput);
                game.initBoard(Moves.playerBoard, Popup.userInput);
                Moves.id= userInput;
            }catch (Exception e){
                Moves.id= "1";
                game.initBoard(Moves.enemyBoard, Popup.userInput);
                game.initBoard(Moves.playerBoard, Popup.userInput);
            }
            game.running=true;
            game.observer();
            game.setRandomPlayer();
        });
        //Popup Load Window
        MenuItem load = new MenuItem("Load");
        load.setOnAction(actionEvent -> displayLoad(start));

        //Exit game, close window
        MenuItem exitMenuItem = new MenuItem("Exit");
        exitMenuItem.setOnAction(actionEvent -> Platform.exit());

        fileMenu.getItems().addAll(start, load,
                new SeparatorMenuItem(), exitMenuItem);
        return fileMenu;
    }

    private Menu detailsMenu() {
        Menu details = new Menu("Details");
//        details.setStyle("-fx-font-weight: bold; -fx-border-style: solid;-fx-border-width: 3; -fx-border-color: black");
        //Popup windows with corresponding tables showing information asked
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
