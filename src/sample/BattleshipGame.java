package sample;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.*;

import sample.Board.Cell;

public class BattleshipGame {
//    protected enemyLastMove lastMove =new enemyLastMove();
//    protected enemyLastMove preLastMove=new enemyLastMove();

    protected Deque<Cell> enemyStack= new ArrayDeque<>();
    protected Deque<Cell> playerStack= new ArrayDeque<>();

    protected boolean running = false;
    protected Board enemyBoard, playerBoard;

    protected int shipsToPlace = 5;

    protected boolean enemyTurn = false;

    protected Random random = new Random();

    protected Parent createContent() {
        BorderPane root = new BorderPane();
        root.setPrefSize(600, 800);

        root.setRight(new Text("RIGHT SIDEBAR - CONTROLS"));

        enemyBoard = new Board(true, event -> {
            if (!running)
                return;

            Board.Cell cell = (Board.Cell) event.getSource();
            if (cell.wasShot)
                return;

            cell.shoot();
            enemyTurn = true;
            if (enemyBoard.ships == 0) {
                System.out.println("YOU WIN");
                System.exit(0);
            }

            if (enemyTurn) {
                enemyMove();
                enemyTurn = false;
            }

        });

        playerBoard = new Board(false, event -> {
        });

//      Text box to choose layouts

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

        strArray=readFile.readFile2Array(true,1);
        count=new int[5];
        for(int i=0;i<5;i++) {
            enemyBoard.placeShip(new Ship(strArray[i][0],strArray[i][3].equals("2")),Integer.parseInt(strArray[i][2]),Integer.parseInt(strArray[i][1]));
            count[Integer.parseInt(strArray[i][0])-1]=1;
        }
        if (Arrays.stream(count).sum()<5){
//      InvalidCountException
            System.out.println("InvalidCountException");
        }

//      Start Game
        running = true;

        VBox vbox = new VBox(50, enemyBoard, playerBoard);
        vbox.setAlignment(Pos.CENTER);

        root.setCenter(vbox);

        return root;
    }
// Fix this to be more smart
    private void enemyMove() {
        boolean lastShotHit,first=true;
//        int remainShips=playerBoard.ships;
        Iterator<Cell> itr = enemyStack.iterator();
        Cell preLastMove=null;
        Cell lastMove = null;
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
                    for (Cell neighbor : playerBoard.getNeighbors(lastMove.x,lastMove.y)) {
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

            enemyTurn = false;

            if (playerBoard.ships == 0) {
                System.out.println("YOU LOSE");
                System.exit(0);
            }
        }
    }
//    public class enemyLastMove{
//        public int x;
//        public int y;
//        public boolean shot;
//        public String shipType;
//
//        public enemyLastMove(){
//           this.x=0;
//           this.y=0;
//           this.shot=false;
////           this.shipType="0";
//        }
//        public enemyLastMove(int x,int y,boolean shot){   //,String type){
//            this.x=x;
//            this.y=y;
//            this.shot=shot;
////            this.shipType=type;
//        }
//    }
}
