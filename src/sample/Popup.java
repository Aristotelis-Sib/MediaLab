package sample;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import customExceptions.OversizeException;
import customExceptions.AdjacentTilesException;
import customExceptions.InvalidCountExeception;
import customExceptions.OverlapTilesException;


public class Popup {
    protected static String userInput="1";
    //userInput setter
    public static void setUserInput(String string){
        userInput=string;
    }

    public static void displayEnemyShips(Board board)
    {
        Stage popUpWindow=new Stage();
        popUpWindow.initModality(Modality.APPLICATION_MODAL);
        popUpWindow.initOwner(BattleshipGame.root.getScene().getWindow());
        popUpWindow.setMinHeight(550);
        popUpWindow.setMinWidth(550);
        popUpWindow.setMaxHeight(900);
        popUpWindow.setMaxWidth(900);
        popUpWindow.setTitle("Enemy Ships");
        Label label= new Label("Enemy Ships Status is:");
        label.setFont(new Font("Arial", 20));
        label.setStyle("-fx-font-weight: bold;");
        VBox layout = new VBox(40,label,getShips(board));
        layout.setAlignment(Pos.CENTER);
        Scene scene1= new Scene(layout, 300, 250);
        popUpWindow.setScene(scene1);
        popUpWindow.showAndWait();
    }


    public static VBox getShips(Board board){
        double gridWidth = 150;
        double gridHeight = 50;

        Color infoColor=Color.CYAN;
        Color dataColor=Color.WHITE;
        Color hitColor=Color.ORANGE;
        Color sinkColor=Color.GREEN;
        BattleshipGame.MyNode info0= new BattleshipGame.MyNode( "Ship Type", gridWidth*1.4, gridHeight,infoColor);
        BattleshipGame.MyNode info1 = new BattleshipGame.MyNode( "Hits/Size", gridWidth, gridHeight,infoColor);

        BattleshipGame.MyNode carrierName = new BattleshipGame.MyNode( "Carrier", gridWidth*1.4, gridHeight,infoColor);
        BattleshipGame.MyNode carrier= new BattleshipGame.MyNode(5-board.shipArray[0].getHealth()+"/5" , gridWidth, gridHeight,board.shipArray[0].getHealth()==0?sinkColor:(board.shipArray[0].getHealth()==5?dataColor:hitColor));

        BattleshipGame.MyNode battleshipName = new BattleshipGame.MyNode( "Battleship", gridWidth*1.4, gridHeight,infoColor);
        BattleshipGame.MyNode battleship = new BattleshipGame.MyNode( 4-board.shipArray[1].getHealth()+"/4" , gridWidth, gridHeight,board.shipArray[1].getHealth()==0?sinkColor:(board.shipArray[1].getHealth()==4?dataColor:hitColor));

        BattleshipGame.MyNode cruiserName = new BattleshipGame.MyNode( "Cruiser", gridWidth*1.4, gridHeight,infoColor);
        BattleshipGame.MyNode cruiser = new BattleshipGame.MyNode( 3-board.shipArray[2].getHealth()+"/3"  , gridWidth, gridHeight,board.shipArray[2].getHealth()==0?sinkColor:(board.shipArray[2].getHealth()==3?dataColor:hitColor));

        BattleshipGame.MyNode submarineName = new BattleshipGame.MyNode( "Submarine", gridWidth*1.4, gridHeight,infoColor);
        BattleshipGame.MyNode submarine = new BattleshipGame.MyNode(3-board.shipArray[3].getHealth()+"/3"  , gridWidth, gridHeight,board.shipArray[3].getHealth()==0?sinkColor:(board.shipArray[3].getHealth()==3?dataColor:hitColor));

        BattleshipGame.MyNode destroyerName = new BattleshipGame.MyNode( "Destroyer", gridWidth*1.4, gridHeight,infoColor);
        BattleshipGame.MyNode destroyer = new BattleshipGame.MyNode( 2-board.shipArray[4].getHealth()+"/2" , gridWidth, gridHeight,board.shipArray[4].getHealth()==0?sinkColor:(board.shipArray[4].getHealth()==2?dataColor:hitColor));

        BattleshipGame.MyNode healthy = new BattleshipGame.MyNode( "Not hit", gridWidth*0.797, gridHeight,dataColor);
        BattleshipGame.MyNode hit = new BattleshipGame.MyNode( "Hit" , gridWidth*0.797, gridHeight,hitColor);
        BattleshipGame.MyNode sunk = new BattleshipGame.MyNode( "Sunk" , gridWidth*0.797, gridHeight,sinkColor);

        VBox column1 = new VBox(info0,carrierName,battleshipName,cruiserName,submarineName,destroyerName);
        column1.setAlignment(Pos.CENTER);
        VBox column2 = new VBox( info1,carrier,battleship,cruiser,submarine,destroyer);
        column2.setAlignment(Pos.CENTER);
        HBox shipConditionColors=new HBox(-1,healthy,hit,sunk);
        shipConditionColors.setAlignment(Pos.CENTER);

        HBox shipsStats = new HBox(column1,column2);
        shipsStats .setAlignment(Pos.CENTER);

        VBox shipShotsTable=new VBox(shipsStats,shipConditionColors);
        shipShotsTable.setAlignment(Pos.CENTER);
        return shipShotsTable;

//        return shipsStats;
    }

    public static void displayShots(Deque<Board.Cell> stack,boolean isPlayer)
    {
        Stage popUpWindow=new Stage();
        popUpWindow.initModality(Modality.APPLICATION_MODAL);
        popUpWindow.initOwner(BattleshipGame.root.getScene().getWindow());
        popUpWindow.setMinHeight(600);
        popUpWindow.setMinWidth(800);
        popUpWindow.setMaxHeight(800);
        popUpWindow.setMaxWidth(1200);
        popUpWindow.setTitle("Last Shots Info");
        int numOfShots= Math.min(stack.size(), 5);
        Label label= new Label(isPlayer?"Your last "+ numOfShots+" Shots":"Enemies last "+numOfShots+" Shots");
        label.setFont(new Font("Arial", 20));
        label.setStyle("-fx-font-weight: bold;");
        VBox layout = new VBox(40,label,getShots(stack));
        layout.setAlignment(Pos.CENTER);
        Scene scene1= new Scene(layout, 300, 250);
        popUpWindow.setScene(scene1);
        popUpWindow.showAndWait();
    }

        public static void whoPlays(String toPlay)
    {
        Stage popUpWindow=new Stage();
        popUpWindow.setMinHeight(100);
        popUpWindow.setMinWidth(200);
        popUpWindow.setTitle("Who plays?");
        Label label= new Label(toPlay);
        label.setFont(new Font("Arial", 20));
        label.setStyle("-fx-font-weight: bold;");
        label.setAlignment(Pos.CENTER);

        Label labelId= new Label("Scenario id: "+Moves.id);
        labelId.setFont(new Font("Arial", 15));
        labelId.setStyle("-fx-font-weight: bold;");
        labelId.setAlignment(Pos.CENTER);

        Button button= new Button("  Ok  ");
        button.setOnAction(e->popUpWindow.close());
        button.setAlignment(Pos.CENTER);
        button.setDefaultButton(true);
        VBox layout = new VBox(10,label,labelId,button);
        layout.setAlignment(Pos.CENTER);
        Scene scene1= new Scene(layout, 300, 250);
        popUpWindow.setScene(scene1);
        popUpWindow.showAndWait();

    }


    public static VBox getShots(Deque<Board.Cell> stack){
        double gridWidth = 150;
        double gridHeight = 50;
        //HashMap as dictionary creating key-value pairs ,matching each key-ship type with is name
        Map<String, String> map = new HashMap<>();
        map.put("1", "Carrier");
        map.put("2", "Battleship");
        map.put("3", "Cruiser");
        map.put("4", "Submarine");
        map.put("5", "Destroyer");

        Color infoColor=Color.CYAN;
        Color dataColor=Color.WHITE;
        Color hitColor=Color.RED;
        Color waterColor= Color.AQUA;
        //Header row
        BattleshipGame.MyNode info0= new BattleshipGame.MyNode( "Last shots", gridWidth, gridHeight,infoColor);
        BattleshipGame.MyNode info1 = new BattleshipGame.MyNode( "Location", gridWidth, gridHeight,infoColor);
        BattleshipGame.MyNode info2 = new BattleshipGame.MyNode( "Hit/Miss", gridWidth, gridHeight,infoColor);
        BattleshipGame.MyNode info3 = new BattleshipGame.MyNode( "Ship Type", gridWidth, gridHeight,infoColor);

        Iterator<Board.Cell> itr = stack.iterator();
        int i=0;
        BattleshipGame.MyNode[] name= new BattleshipGame.MyNode[5];
        BattleshipGame.MyNode[] pos=new BattleshipGame.MyNode[5];
        BattleshipGame.MyNode[] hit=new BattleshipGame.MyNode[5];
        BattleshipGame.MyNode[] type=new BattleshipGame.MyNode[5];
        Board.Cell tmp;
        //Getting only 5 last shots
        while(itr.hasNext() && i<5){
            tmp=itr.next();
            name[i]= new BattleshipGame.MyNode( String.valueOf(i+1), gridWidth, gridHeight,infoColor);
            pos[i]= new BattleshipGame.MyNode("x: "+tmp.x+" y: "+tmp.y, gridWidth, gridHeight,dataColor);
            hit[i]= new BattleshipGame.MyNode(tmp.points>0?"Hit":"Miss", gridWidth, gridHeight,tmp.points>0?hitColor:dataColor);
            type[i]= new BattleshipGame.MyNode(tmp.points>0?map.get(tmp.getShipType()):"Water", gridWidth, gridHeight,tmp.points>0?dataColor:waterColor);
            i++;
        }
        i--;
        if(i>=0) {
            HBox rowInfo = new HBox(info0, info1, info2, info3);
            rowInfo.setAlignment(Pos.CENTER);
            HBox[] rows = new HBox[i+1];
            for (int j = i; j >= 0; j--) {
                rows[j] = new HBox(name[j], pos[j], hit[j], type[j]);
                rows[j].setAlignment(Pos.CENTER);
            }
            //Cases if total shot (and the 5 last shots) are less than 5
            VBox vBox = null;
            switch (i) {
                case 0:
                    vBox = new VBox(rowInfo, rows[0]);
                    break;
                case 1:
                    vBox = new VBox(rowInfo, rows[0], rows[1]);
                    break;
                case 2:
                    vBox = new VBox(rowInfo, rows[0], rows[1], rows[2]);
                    break;
                case 3:
                    vBox = new VBox(rowInfo, rows[0], rows[1], rows[2], rows[3]);
                    break;
                case 4:
                    vBox = new VBox(rowInfo, rows[0], rows[1], rows[2], rows[3], rows[4]);
                    break;
            }
            return vBox;
        }
        else{
            VBox vBox;
            BattleshipGame.MyNode noResults= new BattleshipGame.MyNode( "No Shots have been done", 2*gridWidth, gridHeight,infoColor);
            vBox = new VBox(noResults);
            return vBox;
        }
    }

    public static void displayResults(String winner,int playerPoints,int enemyPoints){
        displayResults( winner, "", playerPoints, enemyPoints);
    }

    public static void displayResults(String winner,String extramessage,int playerPoints,int enemyPoints)
    {
        //Display who is winner from message, and how he won (by points or ships)
        Stage popUpWindow=new Stage();
        popUpWindow.initModality(Modality.WINDOW_MODAL);
        popUpWindow.initOwner(BattleshipGame.root.getScene().getWindow());
        popUpWindow.setMinHeight(200);
        popUpWindow.setMinWidth(400);
        popUpWindow.setTitle("Game Over");
        Label label= new Label(winner);
        label.setFont(new Font("Arial", 20));
        label.setStyle("-fx-font-weight: bold;");
        Label labelExtra= new Label(extramessage);
        if (!extramessage.equals("")){
            labelExtra.setFont(new Font("Arial", 15));
            labelExtra.setStyle("-fx-font-weight: bold;");
        }

        Label player= new Label("Your points where: "+ playerPoints);
        player.setFont(new Font("Arial", 15));
        player.setStyle("-fx-font-weight: bold;");
        player.setAlignment(Pos.CENTER);

        Label enemy= new Label("Enemy points where: "+ enemyPoints);
        enemy.setFont(new Font("Arial", 15));
        enemy.setStyle("-fx-font-weight: bold;");
        enemy.setAlignment(Pos.CENTER);

        //Green color for winner
        if (playerPoints > enemyPoints) {
            player.setTextFill(Color.GREEN);
            enemy.setTextFill(Color.RED);
        } else {
            enemy.setTextFill(Color.GREEN);
            player.setTextFill(Color.RED); }
        VBox points=new VBox(20,player,enemy);
        points.setAlignment(Pos.CENTER);
        Button restartGame= new Button("Restart Game");
        restartGame.setOnAction(e->popUpWindow.close());
        restartGame.setDefaultButton(true);
        Button closeGame= new Button("Close Game");
        closeGame.setOnAction(e -> System.exit(0));
        HBox buttons=new HBox(10,restartGame,closeGame);
        buttons.setAlignment(Pos.CENTER);
        VBox layout= new VBox(25);
        if (!extramessage.equals("")) {
            layout.getChildren().addAll(label,labelExtra,points, buttons);
        }else{
            layout.getChildren().addAll(label,points, buttons);
        }
        layout.setAlignment(Pos.CENTER);
        Scene scene1= new Scene(layout, 300, 250);
        popUpWindow.setScene(scene1);
        popUpWindow.showAndWait();
    }

    public static void displayLoad(Button loadaction)
    {
        //Load scenario popup
        Stage popUpWindow=new Stage();
        popUpWindow.initModality(Modality.WINDOW_MODAL);
        popUpWindow.initOwner(BattleshipGame.root.getScene().getWindow());
        popUpWindow.setTitle("Load new game");
        Label label1= new Label("Choose Scenario id for next game:");
        Label label2= new Label("(only numbers are accepted)");
        Label label3= new Label();
        //"Textfield" that accepts only numbers
        NumFieldFX textField=new NumFieldFX();
        textField.setMaxWidth(60);
        Button submit= new Button("Load");
        submit.setOnAction(actionEvent -> {
            //Start button checks if an id is given
            if (!(textField.getText() != null && !textField.getText().isEmpty())) {
                label3.setTextFill(Color.FIREBRICK);
                label3.setText("You have not specified an id");
                setUserInput(Moves.id);
            }
            else{
                boolean noException=true;
                label3.setTextFill(Color.GREEN);
                boolean tmp;
                //Check if there is a file with such id
                tmp=ReadFile.checkForFile(textField.getText());
                if (tmp){
                    label3.setText("You gave id:" +textField.getText());
                    try{
                        //Check if the file has an arrangement of ships that do not throw any of the custom exceptions
                        Moves.checkBoard(textField.getText());
                    }catch (AdjacentTilesException e){
                        label3.setTextFill(Color.FIREBRICK);
                        label3.setText("Id " +textField.getText()+" throws exception");
                        Popup.eMessage(e.getMessage(),"Ships can not touch each other");
                        noException=false;
                    }catch (OverlapTilesException e){
                        label3.setTextFill(Color.FIREBRICK);
                        label3.setText("Id " +textField.getText()+" throws exception");
                        Popup.eMessage(e.getMessage(),"Ships can not overlap each other");
                        noException=false;
                    }catch (InvalidCountExeception e){
                        label3.setTextFill(Color.FIREBRICK);
                        label3.setText("Id " +textField.getText()+" throws exception");
                        Popup.eMessage(e.getMessage(),"Only one ship of each type");
                        noException=false;
                    }catch (OversizeException e){
                        label3.setTextFill(Color.FIREBRICK);
                        label3.setText("Id " +textField.getText()+" throws exception");
                        Popup.eMessage(e.getMessage(),"Ships can not exceed board limits");
                        noException=false;
                    }
                    if(noException){
                        //If the above are checked proceed to load and start game with new id
                        setUserInput(textField.getText());
                        loadaction.fire();
                        popUpWindow.close();
                    }
                }
                else{
                    label3.setTextFill(Color.FIREBRICK);
                    label3.setText("Non file with id: " +textField.getText());
                }

            }
        });
        submit.setDefaultButton(true);
        Button close= new Button("Close");
        close.setOnAction(actionEvent -> popUpWindow.close());
        HBox buttons= new HBox(10,submit,close);
        buttons.setAlignment(Pos.CENTER);
        VBox layout= new VBox(10);
        layout.getChildren().addAll(label1,label2,textField,buttons,label3);
        layout.setAlignment(Pos.CENTER);
        Scene scene1= new Scene(layout, 300, 250);
        popUpWindow.setScene(scene1);
        popUpWindow.showAndWait();
    }

    public static class NumFieldFX extends TextField {
        public NumFieldFX() {
            this.addEventFilter(KeyEvent.KEY_TYPED, t -> {
                char[] ar = t.getCharacter().toCharArray();
                char ch = ar[t.getCharacter().toCharArray().length - 1];
                if (!(ch >= '0' && ch <= '9')) {
                    System.out.println("The char you entered is not a number");
                    t.consume();
                }
            });
        }
    }
    public static void eMessage(String errorMessage){
        //Exception popup message
        Stage popUpWindow=new Stage();
        popUpWindow.setMinHeight(100);
        popUpWindow.setMinWidth(200);
        popUpWindow.setTitle("Exception");
        Label label= new Label(errorMessage);
        label.setFont(new Font("Arial", 15));
        label.setStyle("-fx-font-weight: bold;");
        label.setAlignment(Pos.CENTER);

        Button button= new Button("  Ok  ");
        button.setOnAction(e->popUpWindow.close());
        button.setAlignment(Pos.CENTER);
        button.setDefaultButton(true);
        VBox layout = new VBox(10,label,button);
        layout.setAlignment(Pos.CENTER);
        Scene scene1= new Scene(layout, 300, 250);
        popUpWindow.setScene(scene1);
        popUpWindow.showAndWait();
    }


    public static void eMessage(String errorMessage,String extraMessage)
    {
        //Exception popup message
        Stage popUpWindow=new Stage();
        popUpWindow.setMinHeight(100);
        popUpWindow.setMinWidth(200);
        popUpWindow.setTitle("Exception");
        Label label= new Label(errorMessage);
        label.setFont(new Font("Arial", 15));
        label.setStyle("-fx-font-weight: bold;");
        label.setAlignment(Pos.CENTER);

        Label labelExtra= new Label(extraMessage);
        labelExtra.setFont(new Font("Arial", 12));
        labelExtra.setStyle("-fx-font-weight: bold;");
        labelExtra.setAlignment(Pos.CENTER);

        Label labelOtherId= new Label("Choose other scenario id");
        labelOtherId.setFont(new Font("Arial", 12));
        labelOtherId.setStyle("-fx-font-weight: bold;");
        labelOtherId.setAlignment(Pos.CENTER);

        Button button= new Button("  Ok  ");
        button.setOnAction(e->popUpWindow.close());
        button.setAlignment(Pos.CENTER);
        button.setDefaultButton(true);
        VBox layout = new VBox(10,label,labelExtra,labelOtherId,button);
        layout.setAlignment(Pos.CENTER);
        Scene scene1= new Scene(layout, 300, 250);
        popUpWindow.setScene(scene1);
        popUpWindow.showAndWait();

    }
}
