package sample;

import java.util.*;

import customExceptions.OversizeException;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import customExceptions.*;

public class Board extends Parent {
    private VBox rows = new VBox();
    protected boolean enemy;
    public int ships = 5;
    public Ship[] shipArray = new Ship[5];

    //Create board,that is an array of Cell type object with a specific Event handler
    public Board(boolean enemy, EventHandler<? super MouseEvent> handler) {
        this.enemy = enemy;
        HBox firstRow = new HBox();
        //Create first row with numbering of columns
        for(int i=0;i<10;i++){
            if(i==0) {
                BattleshipGame.MyNode rect= new BattleshipGame.MyNode("y\\x",30,30,Color.DARKGRAY,1);
                firstRow.getChildren().add(rect);
            }
            BattleshipGame.MyNode rect= new BattleshipGame.MyNode(String.valueOf(i),30,30,Color.DARKGRAY,1);
            firstRow.getChildren().add(rect);
        }
        rows.getChildren().add(firstRow);
        //Create next 10 rows,1 column has number of row and rest cells with mouse click handler.
        for (int y = 0; y < 10; y++) {
            HBox row = new HBox();
            BattleshipGame.MyNode rect= new BattleshipGame.MyNode(String.valueOf(y),30,30,Color.DARKGRAY,1);
            row.getChildren().add(rect);
            for (int x = 0; x < 10; x++) {
                Cell c = new Cell(x, y, this);
                c.setOnMouseClicked(handler);
                row.getChildren().add(c);
            }
            rows.getChildren().add(row);
        }

        getChildren().add(rows);
    }

    public boolean placeShip(Ship ship, int x, int y,String scenarioId) throws OverlapTilesException, AdjacentTilesException, OversizeException {
        //Check if ship can be placed in the given coordinates if not custom exception is thrown
        if (canPlaceShip(ship, x, y,scenarioId)) {
            int length = ship.length;
            //Assign ship to corresponding cells
            if (ship.vertical) {
                for (int i = y; i < y + length; i++) {
                    Cell cell = getCell(x, i);
                    cell.ship = ship;
                    if (!enemy) {
                        cell.setFill(Color.WHITE);
                        cell.setStroke(Color.GREEN);
                    }
                }
            } else {
                for (int i = x; i < x + length; i++) {
                    Cell cell = getCell(i, y);
                    cell.ship = ship;
                    if (!enemy) {
                        cell.setFill(Color.WHITE);
                        cell.setStroke(Color.GREEN);
                    }
                }
            }
            shipArray[Integer.parseInt(ship.type) - 1] = ship;
            return true;
        }

        return false;
    }
    //From given coordinates get Cell
    public Cell getCell(int x, int y) {
        return (Cell) ((HBox) rows.getChildren().get(y+1)).getChildren().get(x+1);
    }

    //Find neighbors of cell
    protected Cell[] getNeighbors(int x, int y) {
        Point2D[] points = new Point2D[]{
                new Point2D(x - 1, y),
                new Point2D(x + 1, y),
                new Point2D(x, y - 1),
                new Point2D(x, y + 1),
        };

        List<Cell> neighbors = new ArrayList<>();

        for (Point2D p : points) {
            if (isValidPoint(p)) {
                neighbors.add(getCell((int) p.getX(), (int) p.getY()));
            }
        }

        return neighbors.toArray(new Cell[0]);
    }
    //Find neighbors tha have not been shot and return in HashMap with corresponding values for keys "u" (neighbor above)
    // "d" (neighbor bellow),"l" (left neighbor) and "r" (right neighbor)
    protected HashMap<String,int[]> getNeighborsDict(int x, int y) {
        int[] point_u= {x , y-1};
        int[] point_d= {x , y+1};
        int[] point_l= {x-1 , y};
        int[] point_r= {x+1 , y};

        HashMap<String,int[]> dict = new HashMap<>();
        dict.put("u", null);
        dict.put("d", null);
        dict.put("l", null);
        dict.put("r", null);
        Board.Cell cell;
        if(isValidPoint(point_u[0],point_u[1])){
            cell=getCell(point_u[0],point_u[1]);
            if (!cell.hasBeenShot()){
                dict.put("u",point_u);
            }
        }
        if(isValidPoint(point_d[0],point_d[1])){
            cell=getCell(point_d[0],point_d[1]);
            if (!cell.hasBeenShot()){
                dict.put("d",point_d);
            }
        }
        if(isValidPoint(point_l[0],point_l[1])){
            cell=getCell(point_l[0],point_l[1]);
            if (!cell.hasBeenShot()){
                dict.put("l",point_l);
            }
        }
        if(isValidPoint(point_r[0],point_r[1])){
            cell=getCell(point_r[0],point_r[1]);
            if (!cell.hasBeenShot()){
                dict.put("r",point_r);
            }
        }
        return dict;
    }

    private boolean canPlaceShip(Ship ship, int x, int y,String scenarioID) throws OversizeException, OverlapTilesException, AdjacentTilesException {
        int length = ship.length;
//        String exceptionMessage=enemy?"enemy_SCENARIO-":"player_SCENARIO-"+scenarioID+".txt at ship-type(row) " + ship.type;
        String exceptionMessage=enemy?"enemy_SCENARIO-":"player_SCENARIO-"+scenarioID+".txt \n";
        //Checking if ship can be placed in given position and orientation
        if (ship.vertical) {
            for (int i = y; i < y + length; i++) {
                if (!isValidPoint(x, i)) {
//                    OversizeException
                    throw new OversizeException( exceptionMessage+ "throws OversizeException");
                }
                Cell cell = getCell(x, i);
                if (cell.ship != null) {
//                  OverlayException
                    throw new OverlapTilesException(exceptionMessage + "throws OverlapTilesException");
                }
                for (Cell neighbor : getNeighbors(x, i)) {
                    if (neighbor.ship != null) {
//                      AdjacentTilesException
                        throw new AdjacentTilesException(exceptionMessage + "throws AdjacentTilesException");
                    }
                }
            }
        } else {
            for (int i = x; i < x + length; i++) {
//                    OversizeException
                if (!isValidPoint(i, y)) {
                    throw new OversizeException(exceptionMessage + ship.type + "throws OversizeException");
                }

                Cell cell = getCell(i, y);
                if (cell.ship != null) {
//                  OverlayException
                    throw new OverlapTilesException(exceptionMessage+ "throws OverlapTilesException");
                }

                for (Cell neighbor : getNeighbors(i, y)) {
                    if (neighbor.ship != null) {
//                      AdjacentTilesException
                        throw new AdjacentTilesException(exceptionMessage + "throws AdjacentTilesException");
                    }
                }
            }
        }

        return true;
    }

    protected boolean isValidPoint(Point2D point) {
        return isValidPoint(point.getX(), point.getY());
    }
    //True if point is inside bounds of board
    protected boolean isValidPoint(double x, double y) {
        return x >= 0 && x < 10 && y >= 0 && y < 10;
    }
    //Reset all cells
    public void reset(boolean total) {
        this.ships = 5;
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                Cell c = this.getCell(x, y);
                c.setFill(Color.LIGHTGRAY);
                c.setStroke(Color.BLACK);
                c.wasShot = false;
                c.points = 0;
                if (c.ship != null && !this.enemy && !total) {
                    c.setFill(Color.WHITE);
                    c.setStroke(Color.GREEN);
                }
                if (total) {
                    c.ship = null;
                    c.setFill(Color.LIGHTGRAY);
                    c.setStroke(Color.BLACK);
                }
            }
        }
    }

    public class Cell extends Rectangle {
        public int x, y;
        private Ship ship = null;
        private boolean wasShot = false;
        public int points = 0;
        private Board board;

        /**
         * Square subarea or part of Board with specific coordinates (x,y) on the board
         * and default color set to gray.Where x is in horizontal direction and y in vertical.
         *
         * @param x         Specifies the x-coordinate of the board at which the cell corresponds. Measured in pixels
         * @param y         Specifies the y-coordinate of the board at which the cell corresponds. Measured in pixels.
         * @param board     The board in which the cell belongs
         */
        public Cell(int x, int y, Board board) {
            super(30, 30);
            this.x = x;
            this.y = y;
            this.board = board;
            setFill(Color.LIGHTGRAY);
            setStroke(Color.CYAN);
        }

        /**
         * Hit of cell.Change state of cell as shot.If no ship corresponds to this cell change of filling color to cyan and false is returned.If it corresponds to a ship filling color is changed to red,points are updated and true is returned.If ship can not
         * endure any more hits the board in which the cell bellongs to subtracts by 1 the number of ships tha have not been sinked.
         *
         * @return boolean true if a ship corresponds to this cell false otherwise
         */
        public boolean shoot() {
            wasShot = true;
            setFill(Color.CYAN);

            if (ship != null) {
                this.points = ship.hit();
                setFill(Color.RED);
                if (!ship.isAlive()) {
                    board.ships--;
                }
                return true;
            }
            return false;
        }

        /**
         * Returns if cell has been shot or not
         *
         * @return bool true if cell has been shot false if not
         */
        public boolean hasBeenShot() {
            return wasShot;
        }

        /**
         * Returns the type of ship that corresponds to this cell.If no such
         * ship the value is null.
         *
         * @return string ship type if cell corresponds to a ship else null
         */
        public String getShipType(){
            return ship.type;
        }

        /**
         * If we have shot the cell it returns us if it has a ship corresponding to its position or not.
         * If we have not shot the cell it gives us no information.
         *
         * @return int 0 if haven't shot this cell,1 if we have shot the cell and has a ship corresponding to its position
         * and -1 if we have shot the cell and no ship is corresponding to its position
         */
        public int hasShip(){
            if(!wasShot){
                return 0;  //We don't know
            } else{
                if(ship!=null){
                    return 1;
                } else{
                    return -1; }
            }
        }
    }
}