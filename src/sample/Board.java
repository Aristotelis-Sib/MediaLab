package sample;

import java.util.ArrayList;
import java.util.List;

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

    public Board(boolean enemy, EventHandler<? super MouseEvent> handler) {
        this.enemy = enemy;
        HBox firstRow = new HBox();
        for(int i=0;i<10;i++){
            if(i==0) {
                BattleshipGame.MyNode rect= new BattleshipGame.MyNode("",30,30,Color.DARKGRAY,1);
                firstRow.getChildren().add(rect);
            }
            BattleshipGame.MyNode rect= new BattleshipGame.MyNode(String.valueOf(i),30,30,Color.DARKGRAY,1);
            firstRow.getChildren().add(rect);
        }
        rows.getChildren().add(firstRow);

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

    public boolean placeShip(Ship ship, int x, int y,String scenarioId) {
        if (canPlaceShip(ship, x, y,scenarioId)) {
            int length = ship.length;

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

    public Cell getCell(int x, int y) {
        return (Cell) ((HBox) rows.getChildren().get(y+1)).getChildren().get(x+1);
    }

    protected Cell[] getNeighbors(int x, int y) {
        Point2D[] points = new Point2D[]{
                new Point2D(x - 1, y),
                new Point2D(x + 1, y),
                new Point2D(x, y - 1),
                new Point2D(x, y + 1),
//              Not sure if needed
//                new Point2D(x-1, y-1),
//                new Point2D(x+1, y - 1),
//                new Point2D(x-1, y + 1),
//                new Point2D(x+1, y + 1)

        };

        List<Cell> neighbors = new ArrayList<>();

        for (Point2D p : points) {
            if (isValidPoint(p)) {
                neighbors.add(getCell((int) p.getX(), (int) p.getY()));
            }
        }

        return neighbors.toArray(new Cell[0]);
    }

    private boolean canPlaceShip(Ship ship, int x, int y,String scenarioID) {
        int length = ship.length;

        String exceptionMessage=enemy?"enemy_" : "player_"+scenarioID+".txt at ship-type(row) " + ship.type;
        if (ship.vertical) {
            for (int i = y; i < y + length; i++) {
                if (!isValidPoint(x, i)) {
//                    OversizeException
                    throw new OversizeException( exceptionMessage+ " throws OversizeException");
                }
                Cell cell = getCell(x, i);
                if (cell.ship != null) {
//                  OverlayException
                    throw new OverlapTilesException(exceptionMessage + " throws OverlapTilesException");
                }
                for (Cell neighbor : getNeighbors(x, i)) {
                    if (neighbor.ship != null) {
//                      AdjacentTilesException
                        throw new AdjacentTilesException(exceptionMessage + " throws AdjacentTilesException");
                    }
                }
            }
        } else {
            for (int i = x; i < x + length; i++) {
//                    OversizeException
                if (!isValidPoint(i, y)) {
                    throw new OversizeException(exceptionMessage + ship.type + " throws OversizeException");
                }

                Cell cell = getCell(i, y);
                if (cell.ship != null) {
//                  OverlayException
                    throw new OverlapTilesException(exceptionMessage+ " throws OverlapTilesException");
                }

                for (Cell neighbor : getNeighbors(i, y)) {
                    if (neighbor.ship != null) {
//                      AdjacentTilesException
                        throw new AdjacentTilesException(exceptionMessage + " throws AdjacentTilesException");
                    }
                }
            }
        }

        return true;
    }

    protected boolean isValidPoint(Point2D point) {
        return isValidPoint(point.getX(), point.getY());
    }

    protected boolean isValidPoint(double x, double y) {
        return x >= 0 && x < 10 && y >= 0 && y < 10;
    }

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
        public Ship ship = null;
        public boolean wasShot = false;
        public int points = 0;
        private Board board;

        public Cell(int x, int y, Board board) {
            super(30, 30);
            this.x = x;
            this.y = y;
            this.board = board;
            setFill(Color.LIGHTGRAY);
            setStroke(Color.BLACK);
        }

        public boolean shoot() {
            wasShot = true;
            setFill(Color.BLACK);

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
    }
}