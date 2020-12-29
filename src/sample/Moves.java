package sample;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.*;

import static sample.Popup.display;

//maybe class static or functions/vars
public class Moves {
    protected  Deque<Board.Cell> enemyStack= new ArrayDeque<>();
    protected  Deque<Board.Cell> playerStack= new ArrayDeque<>();
    protected boolean running = false;
    protected  Board enemyBoard, playerBoard;
    protected boolean enemyTurn = false;
    protected Random random = new Random();

    public int getPlayerShips(){
        return playerBoard.ships;
    }
    public int getEnemyShips(){
        return enemyBoard.ships;
    }

    public Deque<Board.Cell> getEnemyStack(){
        return enemyStack;
    }
    public Deque<Board.Cell> getPlayerStack(){
        return playerStack;
    }

    public int getPlayerPoints(){
        Iterator<Board.Cell> itr = playerStack.iterator();
        int totalPoints=0;
        while(itr.hasNext()){
            totalPoints+=itr.next().points;
        }
        return totalPoints;
    }

    public int getEnemyPoints(){
        Iterator<Board.Cell> itr = enemyStack.iterator();
        int totalPoints=0;
        while(itr.hasNext()){
            totalPoints+=itr.next().points;
        }
        return totalPoints;
    }

    public double getPlayerAcc(){
        Iterator<Board.Cell> itr = playerStack.iterator();
        double count=0;
        while(itr.hasNext()){
            if(!(itr.next().points==0)){
                count+=1.0;
            }
        }
        if (playerStack.size()==0) return 0.0;
        else return count/playerStack.size();
    }
    public double getEnemyAcc(){
        Iterator<Board.Cell> itr = enemyStack.iterator();
        double count=0;
        while(itr.hasNext()){
            if(!(itr.next().points==0)){
                count+=1;
            }
        }
        if (enemyStack.size()==0) return 0.0;
        else return count/enemyStack.size();

    }
    public Board getEnemyBoard(){
//      id parameter for reading proper txt
        enemyBoard = new Board(true, event -> {
            if (!running)
                return;

            Board.Cell cell = (Board.Cell) event.getSource();
            if (cell.wasShot)
                return;

            cell.shoot();
            enemyTurn = true;
            playerStack.addFirst(cell);
            if (enemyBoard.ships == 0) {
                display("YOU WON");
                System.exit(0);
            }
            if (playerStack.size()==40){
                display(getPlayerPoints()>getEnemyPoints()?"YOU WON":"YOU LOST");
                System.exit(0);
            }

            if (enemyTurn) {
                enemyMove();
                enemyTurn = false;
            }

        });

        ReadFile readFile=new ReadFile();
        String[][] strArray=readFile.readFile2Array(true,1);
        int[] count=new int[5];
        for(int i=0;i<5;i++) {
            enemyBoard.placeShip(new Ship(strArray[i][0],strArray[i][3].equals("2")),Integer.parseInt(strArray[i][2]),Integer.parseInt(strArray[i][1]));
            count[Integer.parseInt(strArray[i][0])-1]=1;
        }
        if (Arrays.stream(count).sum()<5){
//      InvalidCountException
            System.out.println("InvalidCountException");
        }

        return enemyBoard;
    }

    public Board getPlayerBoard(){
        playerBoard = new Board(false, event -> {});

        ReadFile readFile=new ReadFile();
        String[][] strArray=readFile.readFile2Array(false,1);
        int[] count=new int[5];
        for(int i=0;i<5;i++) {
            playerBoard.placeShip(new Ship(strArray[i][0],strArray[i][3].equals("2")),Integer.parseInt(strArray[i][2]),Integer.parseInt(strArray[i][1]));
            count[Integer.parseInt(strArray[i][0])-1]=1;
        }
        if (Arrays.stream(count).sum()<5){
//      InvalidCountException
            System.out.println("InvalidCountException");
        }
        return  playerBoard;
    }

    private void enemyMove() {
        boolean lastShotHit,first=true;
//        int remainShips=playerBoard.ships;
        Iterator<Board.Cell> itr = enemyStack.iterator();
        Board.Cell preLastMove=null;
        Board.Cell lastMove = null;
        if(!itr.hasNext()) {
            lastShotHit = false;
        }
        else {
            lastMove=itr.next();
            lastShotHit = !(lastMove.points == 0);
            if(itr.hasNext()) {
                preLastMove = itr.next();
            }
        }

        while (enemyTurn) {
            if (lastShotHit){
                if(preLastMove!=null && !(preLastMove.points==0)){
                    Board.Cell cell=null;
                    if (preLastMove.x==lastMove.x){
                        if (first) {
                            first = false;
                            if(playerBoard.isValidPoint(preLastMove.x, Math.max(preLastMove.y, lastMove.y) + 1))
                                cell = playerBoard.getCell(preLastMove.x, Math.max(preLastMove.y, lastMove.y) + 1);
                            else continue;

                        }
                        else{
                            lastShotHit=false;
                            if(playerBoard.isValidPoint(preLastMove.x, Math.min(preLastMove.y, lastMove.y) - 1))
                                cell = playerBoard.getCell(preLastMove.x, Math.min(preLastMove.y, lastMove.y) - 1);
                            else continue;
                        }
                    }
                    else if (preLastMove.y==lastMove.y){
                        if (first) {
                            first = false;
                            if(playerBoard.isValidPoint(Math.max(preLastMove.x, lastMove.x) + 1, preLastMove.y))
                                cell = playerBoard.getCell(Math.max(preLastMove.x, lastMove.x) + 1, preLastMove.y);
                            else continue;

                        }
                        else{
                            lastShotHit = false;
                            if(playerBoard.isValidPoint(Math.min(preLastMove.x, lastMove.x) - 1, preLastMove.y))
                                cell = playerBoard.getCell(Math.min(preLastMove.x, lastMove.x) - 1, preLastMove.y);
                            else continue;
                        }
                    }
                    if (cell==null) {
                        lastShotHit=false;
                        continue;
                    }
                    if (cell.wasShot)
                        continue;
//
                    cell.shoot();
                    enemyStack.addFirst(cell);
                    break;
                }
                else{
                    for (Board.Cell neighbor : playerBoard.getNeighbors(lastMove.x,lastMove.y)) {
                        if (!neighbor.wasShot) {
                            neighbor.shoot();
                            enemyStack.addFirst(neighbor);
                            break;
                        }
                    }
                    lastShotHit=false;
                }
            }
            else {
                int x = random.nextInt(10);
                int y = random.nextInt(10);

                Board.Cell cell = playerBoard.getCell(x, y);
                if (cell.wasShot)
                    continue;

                cell.shoot();
                enemyStack.addFirst(cell);
            }

            if (enemyStack.size()==40){
                display(getPlayerPoints()>getEnemyPoints()?"YOU WON":"YOU LOST");
                System.exit(0);
            }
            enemyTurn = false;

            if (playerBoard.ships == 0) {
                display("YOU LOST");
                System.exit(0);
            }
        }

    }

}
