package ru.maxaleksey.logic;

import java.awt.*;

// класс препятствия
public class Obstacle {
    private final Point[] one = new Point[2];
    private final Point[] two = new Point[2];

    public Obstacle(Point p11, Point p12, Point p21, Point p22){
        one[0] = p11;
        one[1] = p12;
        two[0] = p21;
        two[1] = p22;
    }

    public boolean isObstacle(Point p11, Point p12, Point p21, Point p22){
        return d(p11,p12) && d(p11,p21) && d(p12,p22) && d(p12,p22);
    }

    private boolean d(Point p1, Point p2){
        return Math.sqrt(Math.pow(p1.x-p2.x,2) + Math.pow(p1.y - p2.y,2))==1;
    }

    public Point[] getOne(){
        Point[] value = new Point[2];
        value[0] = one[0];
        value[1] = one[1];
        return value;
    }

    public Point[] getTwo(){
        Point[] value = new Point[2];
        value[0] = two[0];
        value[1] = two[1];
        return value;
    }
    public boolean equals(Obstacle obj) {
        if(this.one[0].x == obj.one[0].x && this.one[0].y == obj.one[0].y
                && this.one[1].x == obj.one[1].x && this.one[1].y == obj.one[1].y
                && this.two[0].x == obj.two[0].x && this.two[0].y == obj.two[0].y
                && this.two[1].x == obj.two[1].x && this.two[1].y == obj.two[1].y){
            return true;
        }
        return false;
    }
}
