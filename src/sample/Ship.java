package sample;

import javafx.scene.Parent;

public class Ship extends Parent {
    public boolean vertical;
    public int hitPoints;
    public int sinkPoints;
    public int length;
    public  String type;
    private int health;

    public int getHealth(){
        return health;
    }

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

    public int hit() {
        health--;
        if (health>0){
            return this.hitPoints;
        }
        else{
            return this.hitPoints+this.sinkPoints;
        }
    }

    public boolean isAlive() {
        return health > 0;
    }
}