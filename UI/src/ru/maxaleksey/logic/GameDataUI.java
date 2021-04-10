package ru.maxaleksey.logic;

import ru.maxaleksey.ui.UI;

import java.awt.*;
import java.util.ArrayList;

public class GameDataUI {
    private GameField gameField = null;
    private Player opponent = null;
    private Player player = null;
    private boolean isFirst = false;
    private isWIN is_win = isWIN.NONE;

    public GameDataUI(Point size,String name_op, int player_count_bar){
        gameField = new GameField(size);
        player = new Player("max",player_count_bar);
        opponent = new Player(name_op, player_count_bar);
    }

    public void setPositions(boolean move, Point pos_player, Point pos_opp, ArrayList<Obstacle> barriers){
        isFirst = move;
        player.setPosition(pos_player);
        opponent.setPosition(pos_opp);
        for(Obstacle b: barriers){
            gameField.addObstacle(b,player.getPosition(), opponent.getPosition());
        }
        if(isFirst){
            makeMove();
        }
    }

    public void setStep(Point pos_op, ArrayList<Obstacle> barriers){
        opponent.setPosition(pos_op);
        ArrayList<Obstacle> current_bariers = gameField.getBarriers();
        for(Obstacle b: barriers){
            if(!current_bariers.contains(b)){
                gameField.addObstacle(b,player.getPosition(),pos_op);
            }
        }
        makeMove();
    }

    public Point getSize(){
        return gameField.getSize();
    }

    public Point getPositionPlayer(){
        return player.getPosition();
    }

    public Point getOpponentPosition(){
        return opponent.getPosition();
    }

    public ArrayList<Obstacle> getBarriers(){
        return gameField.getBarriers();
    }

    public void setResult(String result, Point op_pos, ArrayList<Obstacle> barriers){
        opponent.setPosition(op_pos);
        ArrayList<Obstacle> current_bariers = gameField.getBarriers();
        for(Obstacle b: barriers){
            if(!current_bariers.contains(b)){
                gameField.addObstacle(b,player.getPosition(),op_pos);
            }
        }
        if(result.contains("win")){
            is_win = isWIN.WIN;
        }else{
            if(result.contains("lose")){
                is_win = isWIN.LOSE;
            }else{
                if(result.contains("draw")){
                    is_win = isWIN.DRAW;
                }
            }
        }

    }

    private void makeMove(){
        Point[] way_player = UI.shortestWay(gameField.getGraph(),player.getPosition(),player.getWin_pos());
        Point[] way_opponent = UI.shortestWay(gameField.getGraph(), opponent.getPosition(), opponent.getWin_pos());
        if(UI.choise(way_player,way_opponent)){
            player.setPosition(UI.move(way_player));
        }else{
            try{
                UI.putObstacle(way_opponent,gameField, player.getPosition(),opponent.getPosition());
            }catch (Exception e){
                if(e.getMessage().equals("Поставить препятствие нельзя")){
                    player.setPosition(UI.move(way_player));
                }else{
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}

enum isWIN{
    WIN,
    LOSE,
    DRAW,
    NONE
}
