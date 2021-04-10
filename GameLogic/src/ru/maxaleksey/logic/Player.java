package ru.maxaleksey.logic;

import java.awt.*;

// класс игрока
public class Player {
    private Point position;
    private String name;
    private int count_barriers;
    private int win_pos;

    public Player(String name, int count){
        this.name = name;
        count_barriers = count;
    }

    public Point getPosition(){
        return new Point(position.x,position.y);
    }

    public void setPosition(Point value){
        position = new Point(value.x,value.y);
    }

    public String getName(){
        return name;
    }

    public void setCount_barriers(int count_barriers) {
        this.count_barriers = count_barriers;
    }

    public int getCount_barriers(){
        return count_barriers;
    }

    public void setWin_pos(int win_pos) {
        this.win_pos = win_pos;
    }

    public int getWin_pos() {
        return win_pos;
    }

    public void setName(String name) {
        this.name = name;
    }
}
