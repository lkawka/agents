package utils;

import jade.content.Concept;

public class Position implements Concept {
    private Integer x,y;

    public Position(){

    }

    public Position(int x, int y){
        this.x = x;
        this.y = y;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }
}
