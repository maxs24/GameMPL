package ru.maxaleksey.logic;

import ru.maxaleksey.ui.UI;

import java.awt.*;
import java.util.ArrayList;
import java.util.function.Consumer;

public class GameDataUI {
    private static GameDataUI gameDataUI = null;
    private GameField gameField = null;
    private Player opponent = null;
    private Player player = null;
    private boolean isFirst = false;
    private isWIN is_win = isWIN.NONE;
    private ArrayList<Consumer<String>> sendDataListeners = new ArrayList<>();

    public void addSendDataListeners(Consumer<String> value){
        sendDataListeners.add(value);
    }

    public void removeSendDataListeners(Consumer<String> value){
        sendDataListeners.remove(value);
    }

    public static GameDataUI getInstance(){
        if(gameDataUI == null){
            return new GameDataUI();
        }
        return gameDataUI;
    }

    private GameDataUI(){
        gameField = new GameField(new Point(5,5));
        player = new Player("max",10);
        opponent = new Player("opponent", 10);
    }

    public void setPositions(boolean move, Point pos_player, Point pos_opp, ArrayList<Obstacle> barriers){
        isFirst = move;
        player.setPosition(pos_player);
        opponent.setPosition(pos_opp);
        if(pos_player.x == 0){
            player.setWin_pos(getSize().x-1);
            opponent.setWin_pos(0);
        }else{
            player.setWin_pos(0);
            opponent.setWin_pos(getSize().x-1);
        }
        for(Obstacle b: barriers){
            gameField.addObstacle(b,player.getPosition(), opponent.getPosition());
        }
        if(isFirst){
            makeMove();
            String data = toString();
            System.out.println(data);
            for(Consumer<String> it: sendDataListeners){
                it.accept(data);
            }
        }
    }

    public void setStep(Point pos_op, ArrayList<Obstacle> barriers){
        opponent.setPosition(pos_op);
        ArrayList<Obstacle> current_bariers = gameField.getBarriers();
        for(Obstacle b: barriers){
            boolean flag = false;
            for(Obstacle c_b: current_bariers){
                if(c_b.equals(b)){
                    flag = true;
                }
            }
            if(!flag){
                gameField.addObstacle(b,player.getPosition(),pos_op);
            }
        }
        makeMove();
        String data = toString();
        System.out.println(data);
        for(Consumer<String> it: sendDataListeners){
            it.accept(data);
        }
    }

    public Point getSize(){
        return gameField.getSize();
    }

    public void setSize(Point value){
        gameField.setSize(value);
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
            boolean flag = false;
            for(Obstacle c_b: current_bariers){
                if(c_b.equals(b)){
                    flag = true;
                }
            }
            if(!flag){
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

    public String getIs_win(){
        if(is_win == isWIN.WIN){
            return "win";
        }
        if(is_win == isWIN.LOSE){
            return "lose";
        }
        if(is_win == isWIN.DRAW){
            return "draw";
        }
        return "none";
    }

    public void setPlayerBarriersCount(int pbc){
        opponent.setCount_barriers(pbc);
    }

    public void setNameOpponent(String name){
        opponent.setName(name);
    }

    public GameField getGameField() {
        return gameField;
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
                System.out.println("Препятствие не поставлено");
                player.setPosition(UI.move(way_player));
            }
        }
    }

    public String toString(){
        StringBuilder data = new StringBuilder("SOCKET STEP ");
        data.append("{\"width\": ");
        data.append(this.getSize().y);
        data.append(",\"height\": ");
        data.append(this.getSize().x);
        data.append(", \"position\':[");
        data.append(this.getPositionPlayer().x);
        data.append(",");
        data.append(this.getPositionPlayer().y);
        data.append("], \"opponentPosition:[");
        data.append(this.getOpponentPosition().x);
        data.append(",");
        data.append(this.getOpponentPosition().y);
        data.append("], \"barriers\": [");
        ArrayList<Obstacle> mas_barriers = this.getBarriers();
        for(Obstacle ob: mas_barriers){
            data.append("[");
            Point[] mas = ob.getOne();
            Point[] mas_2 = ob.getTwo();
            data.append("[");
            data.append(mas[0].x);
            data.append(",");
            data.append(mas[0].y);
            data.append("],[");
            data.append(mas[1].x);
            data.append(",");
            data.append(mas[1].y);
            data.append("],[");
            data.append(mas_2[0].x);
            data.append(",");
            data.append(mas_2[0].y);
            data.append("],[");
            data.append(mas_2[1].x);
            data.append(",");
            data.append(mas_2[1].y);
            data.append("]]");
            data.append(",");
        }
        data.deleteCharAt(data.length()-1);
        data.append("}");
        return data.toString();
    }

}

enum isWIN{
    WIN,
    LOSE,
    DRAW,
    NONE
}
