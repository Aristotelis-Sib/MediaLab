package sample;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;

import static sample.Popup.displayResults;

//maybe class static or functions/vars
public class Moves {
    protected  Deque<Board.Cell> enemyStack= new ArrayDeque<>();
    protected  Deque<Board.Cell> playerStack= new ArrayDeque<>();
    protected boolean running = false;
    protected static Board enemyBoard, playerBoard;
    protected boolean enemyTurn = false;
    protected Random random = new Random();
    private PropertyChangeSupport support= new PropertyChangeSupport(this);
    private int numOfMoves=5;
    public static String id;
    public int getPlayerShips(){
        return playerBoard.ships;
    }
    public int getEnemyShips(){
        return enemyBoard.ships;
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

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public void observer() {
        support.firePropertyChange("0", this.running,"0");
    }

    public void setRandomPlayer(){
//        0--> player starts 1--> enemy starts
        int player= random.nextInt(2);
        if (player==0){
            enemyTurn=false;
            Popup.whoPlays("You start the game!");
        }
        else{
            enemyTurn=true;
            Popup.whoPlays("Enemy starts the game!");
            enemyMove();
        }
    }
    public void createEnemyBoard() {
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
                displayResults("YOU WON",getPlayerPoints(),getEnemyPoints());
                initBoard(enemyBoard,"1");
                initBoard(playerBoard,"1");
                reset();
                setRandomPlayer();
            }
            if (playerStack.size() == numOfMoves) {
                displayResults(getPlayerPoints() > getEnemyPoints() ? "YOU WON" : "YOU LOST",getPlayerPoints(),getEnemyPoints());
                initBoard(enemyBoard,"1");
                initBoard(playerBoard,"1");
                reset();
                setRandomPlayer();
            }
            observer();
            if (enemyTurn) {
                enemyMove();
                enemyTurn = false;
            }
        });
    }

    public void createPlayerBoard(){
        playerBoard = new Board(false, event -> {});
    }
    public void initBoard(Board board,String id){
        board.reset(true);
        ReadFile readFile=new ReadFile();
        String[][] strArray=readFile.readFile2Array(board.enemy, id);
        int[] count=new int[5];
        for(int i=0;i<5;i++) {
            board.placeShip(new Ship(strArray[i][0],strArray[i][3].equals("2")),Integer.parseInt(strArray[i][2]),Integer.parseInt(strArray[i][1]));
            count[Integer.parseInt(strArray[i][0])-1]=1;
        }
        if (Arrays.stream(count).sum()!=5){
//      InvalidCountException
            System.out.println("InvalidCountException");
        }
    }

    public void reset(){
        enemyStack= new ArrayDeque<>();
        playerStack= new ArrayDeque<>();
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
            observer();
            if (enemyStack.size()==numOfMoves){
                displayResults(getPlayerPoints() > getEnemyPoints() ? "YOU WON" : "YOU LOST",getPlayerPoints(),getEnemyPoints());
                initBoard(enemyBoard,"1");
                initBoard(playerBoard,"1");
                reset();
                setRandomPlayer();
            }
            enemyTurn = false;

            if (playerBoard.ships == 0) {
                displayResults("YOU LOST",getPlayerPoints(),getEnemyPoints());
                initBoard(enemyBoard,"1");
                initBoard(playerBoard,"1");
                reset();
                setRandomPlayer();
            }
        }

    }

}
