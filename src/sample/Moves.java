package sample;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;
import customExceptions.*;


import static sample.Popup.displayResults;

public class Moves {
    protected  Deque<Board.Cell> enemyStack= new ArrayDeque<>();
    protected  Deque<Board.Cell> playerStack= new ArrayDeque<>();
    protected  HashMap<String,int[]> dict = new HashMap<>();
    protected  int biggestShipNotSinked =5;
    protected  int contHits=0;
    protected boolean size3ship=true;
    protected boolean running = false;
    protected static Board enemyBoard, playerBoard;
    protected boolean enemyTurn = false;
    protected Random random = new Random();
    private PropertyChangeSupport support= new PropertyChangeSupport(this);
    protected int numOfMoves=40;
    boolean enemyStarts=false;
    public static String id;

    //Getters
    public int getPlayerShips(){
        return playerBoard.ships;
    }
    public int getEnemyShips(){
        return enemyBoard.ships;
    }
    public int getPlayerRemMoves(){
        return numOfMoves-playerStack.size();
    }
    public int getEnemyRemMoves(){
        return numOfMoves-enemyStack.size();
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
    //observable object which notifies observers about the changes in its state
    public void observer() {
        support.firePropertyChange("0", this.running,"0");
    }

    public void setRandomPlayer(){
//        0--> player starts 1--> enemy starts
        int player= random.nextInt(2);
        if (player==0){
            enemyStarts=false;
            enemyTurn=false;
            Popup.whoPlays("You start the game!");
        }
        else{
            enemyStarts=true;
            enemyTurn=true;
            Popup.whoPlays("Enemy starts the game!");
            enemyMove();
        }
    }
    public void createEnemyBoard() {
        enemyBoard = new Board(true, event -> {
            //Event handler
            if (!running)
                return;

            Board.Cell cell = (Board.Cell) event.getSource();
            if (cell.hasBeenShot())
                return;

            cell.shoot();
            shotSequence(cell);
        });
    }

    public void shotSequence(Board.Cell cell){
        enemyTurn = true;
        //Keeping on stack each shot
        playerStack.addFirst(cell);
        //If enemy has no more ships
        if (enemyBoard.ships == 0) {
            displayResults("YOU WON","All enemy ships have sunk",getPlayerPoints(),getEnemyPoints());
            initBoard(enemyBoard,"1");
            initBoard(playerBoard,"1");
            reset();
            setRandomPlayer();
        }
        // If limit of moves reached (if player started +1 so enemy can play its 40th shot and then stop game)
        if (playerStack.size() == numOfMoves + (enemyStarts?0:1) ) {
            displayResults(getPlayerPoints() > getEnemyPoints() ? "YOU WON" : "YOU LOST","By points, 40 moves reached",getPlayerPoints(),getEnemyPoints());
            initBoard(enemyBoard,"1");
            initBoard(playerBoard,"1");
            reset();
            setRandomPlayer();
        }
        observer();
        //Enemy turn
        if (enemyTurn) {
            enemyMove();
            enemyTurn = false;
        }
    }
    public void createPlayerBoard(){
        playerBoard = new Board(false, event -> {});
    }

    public void initBoard(Board board,String id) {
        board.reset(true);
        ReadFile readFile=new ReadFile();
        //Reading file and getting results in an array
        String[][] strArray=readFile.readFile2Array(board.enemy, id);
        int[] count=new int[5];
        //Check and place each ship
        //Τry Catch to handle exceptions, but should never occur because the input file is checked from before
        try{
            for(int i=0;i<5;i++) {
                board.placeShip(new Ship(strArray[i][0],strArray[i][3].equals("2")),Integer.parseInt(strArray[i][2]),Integer.parseInt(strArray[i][1]),id);
                count[Integer.parseInt(strArray[i][0])-1]=1;
            }
            if (Arrays.stream(count).sum()!=5){
                //InvalidCountException
                throw new InvalidCountExeception(board.enemy?"enemy_SCENARIO-":"player_SCENARIO-"+ id+".txt\n throws InvalidCountException");
            }
        }catch (Exception e){
            Popup.eMessage(e.getMessage());
        }
    }
    //Same as init board,but for a dummy board to check if exceptions arise
    public static void checkBoard(String id) throws OverlapTilesException, AdjacentTilesException, OversizeException, InvalidCountExeception {
        boolean enemy=false;
        for (int j=0;j<2;j++){
            Board board=new Board(enemy, event -> {});
            board.reset(true);
            ReadFile readFile=new ReadFile();
            String[][] strArray=readFile.readFile2Array(board.enemy, id);
            int[] count=new int[5];
            for(int i=0;i<5;i++) {
                board.placeShip(new Ship(strArray[i][0],strArray[i][3].equals("2")),Integer.parseInt(strArray[i][2]),Integer.parseInt(strArray[i][1]),id);
                count[Integer.parseInt(strArray[i][0])-1]=1;
            }
            if (Arrays.stream(count).sum()!=5){
                //InvalidCountException
                throw new InvalidCountExeception(board.enemy?"enemy_SCENARIO-":"player_SCENARIO-"+ id+".txt\nthrows InvalidCountException");
            }
            enemy=true;
        }
    }
    //Reset, empty stack and empty hashmap
    public void reset(){
        enemyStack= new ArrayDeque<>();
        playerStack= new ArrayDeque<>();
        dict = new HashMap<>();
        dict.put("u", null);
        dict.put("d", null);
        dict.put("l", null );
        dict.put("r", null);
    }

    protected void enemyMove() {
        while (enemyTurn) {
            Board.Cell cell=null;
            boolean shot=false;
            boolean randomHit=true;
            String key= null;
            String keyToZero1= null;
            String keyToZero2= null;
            int[] nextPoint= new int[2];
            //Check if any value (not null) in dictionary (HashMap)
            //if none hit randomly
            for (Map.Entry<String,int[]> entry : dict.entrySet()){
                 if(entry.getValue()!=null){
                    randomHit=false;
                    break;
                }
            }
            if (!randomHit){
                if(dict.get("u")!=null){
                    cell = playerBoard.getCell(dict.get("u")[0],dict.get("u")[1]);
                    key="u";
                    //if hit and then hit neighbor above we have vertical ship
                    keyToZero1="l";
                    keyToZero2="r";
                    //Next point in same "direction"
                    nextPoint[0]=dict.get("u")[0];
                    nextPoint[1]=dict.get("u")[1]-1;
                }
                else if(dict.get("d")!=null){
                    cell = playerBoard.getCell(dict.get("d")[0],dict.get("d")[1]);
                    key="d";
                    //if hit and then hit neighbor bellow we have vertical ship
                    keyToZero1="l";
                    keyToZero2="r";
                    nextPoint[0]=dict.get("d")[0];
                    nextPoint[1]=dict.get("d")[1]+1;
                }
                else if(dict.get("l")!=null){
                    cell = playerBoard.getCell(dict.get("l")[0],dict.get("l")[1]);
                    key="l";
                    //if hit and then hit neighbor left we have horizontal ship
                    keyToZero1="u";
                    keyToZero2="d";
                    nextPoint[0]=dict.get("l")[0]-1;
                    nextPoint[1]=dict.get("l")[1];
                }
                else if(dict.get("r")!=null){
                    cell = playerBoard.getCell(dict.get("r")[0],dict.get("r")[1]);
                    key="r";
                    //if hit and then hit neighbor right we have horizontal ship
                    keyToZero1="u";
                    keyToZero2="d";
                    nextPoint[0]=dict.get("r")[0]+1;
                    nextPoint[1]=dict.get("r")[1];
                }

                shot=cell.shoot();
                enemyStack.addFirst(cell);

                if (!shot) {
                    //if no hit no more shots in this "direction"
                    dict.put(key, null);
                }else{
                    //Count how many hits we have on particular ship (zeros when we hit randomly again), when we hit
                    //a ship we shoot specifically until it is sinked and then randomly again
                    contHits++;
                    //Zero keys on wrong orientation of ship
                    dict.put(keyToZero1, null);
                    dict.put(keyToZero2, null);
                    //Check if next point in direction of the one that hit now is valid
                    if (playerBoard.isValidPoint(nextPoint[0],nextPoint[1])){
                        dict.put(key,nextPoint);
                    }
                    else{
                        dict.put(key,null);
                    }
                }
                //Knowing biggest ship possible we can stop hitting earlier
                if(contHits== biggestShipNotSinked){
                    dict.put("u", null);
                    dict.put("d", null);
                    dict.put("l", null );
                    dict.put("r", null);
                    //if biggest ship sank we then know maximum hits needed based on second biggest ship
                    biggestShipNotSinked--;
                    //special case for size/health 3, we have 2 ships with this size/health
                    if(size3ship && biggestShipNotSinked ==2){
                        biggestShipNotSinked++;
                        size3ship=false;
                    }
                }
            }else{
                contHits=0;
                boolean hit=false;
                boolean skip;
                while(!hit){
                    skip=false;
                    //pick random coordinates
                    int x = random.nextInt(10);
                    int y = random.nextInt(10);

                    //check is cell already has been shot
                    cell = playerBoard.getCell(x, y);
                    if (cell.hasBeenShot())
                        continue;
                    //Check if we have shot a ship next to this point,if yes pick new point, ships do not overlap
                    for (Board.Cell neighbor : playerBoard.getNeighbors(x,y)) {
                        if (enemyStack.contains(neighbor) && neighbor.hasShip()==1) {
                            skip=true;
                            break;
                        }
                    }
                    if (skip){
                        continue;
                    }
                    shot=cell.shoot();
                    enemyStack.addFirst(cell);
                    hit=true;
                }
                if (shot){
                    contHits++;
                    //if hit keep neighbors that have not been shot in dictionary and search
                    dict= playerBoard.getNeighborsDict(cell.x,cell.y);
                }
            }
//            System.out.println(dict);
            observer();
            if (enemyStack.size()==numOfMoves + (enemyStarts?1:0)){
                displayResults(getPlayerPoints() > getEnemyPoints() ? "YOU WON" : "YOU LOST","By points, 40 moves reached",getPlayerPoints(),getEnemyPoints());
                initBoard(enemyBoard,"1");
                initBoard(playerBoard,"1");
                reset();
                setRandomPlayer();
            }
            enemyTurn = false;

            if (playerBoard.ships == 0) {
                displayResults("YOU LOST","All of your ships are sunk",getPlayerPoints(),getEnemyPoints());
                initBoard(enemyBoard,"1");
                initBoard(playerBoard,"1");
                reset();
                setRandomPlayer();
            }
        }

    }

}
