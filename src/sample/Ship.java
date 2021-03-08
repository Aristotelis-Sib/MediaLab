package sample;

import javafx.scene.Parent;

public class Ship extends Parent {
    public boolean vertical;
    public int hitPoints;
    public int sinkPoints;
    public int length;
    public  String type;
    private int health;

    public Ship(String type, boolean vertical) {
        this.type=type;
        this.vertical = vertical;
        switch(type) {
            case "1":
                this.health=5;
                this.hitPoints=350;
                this.sinkPoints=1000;
                break;
            case "2":
                this.health=4;
                this.hitPoints=250;
                this.sinkPoints=500;
                break;
            case "3":
                this.health=3;
                this.hitPoints=100;
                this.sinkPoints=250;
                break;
            case "4":
                this.health=3;
                this.hitPoints=100;
                this.sinkPoints=0;
                break;
            case "5":
                this.health=2;
                this.hitPoints=50;
                this.sinkPoints=0;
                break;
        }
        this.length=this.health;
    }

    /**
     * Hits ship and subtracts remaining health by 1.If ship has health left returns points corresponding for hit
     * if no more health left (ship sank) return points corresponding for hit plus points for sinking this type of ship
     *
     * @return int Points corresponding to hit (depends on ship type), if hit sinks the ship returns points of hit plus
     *              points of sinking this ship type.
     */
    public int hit() {
        health--;
        if (health>0){
            return this.hitPoints;
        }
        else{
            return this.hitPoints + this.sinkPoints;
        }
    }

    /**
     * Returns true if health of ship is not zero (floating ship) else returns false
     *
     * @return boolean true if ship has not been sank else returns false
     */
    public boolean isAlive() {
        return health > 0;
    }

    /**
     * Returns the remaining health of the ship. That is the number of additional hits the ship can withstand.
     *
     * @return int remaining health of ship
     */
    public int getHealth(){
        return health;
    }

}