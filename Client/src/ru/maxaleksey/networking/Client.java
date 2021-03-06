package ru.maxaleksey.networking;

import ru.maxaleksey.logic.Obstacle;
import ru.maxaleksey.logic.GameDataUI;


import java.awt.*;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class Client {
    private static GameDataUI gameData = GameDataUI.getInstance();
    private Socket s;
    private boolean stop = false;
    private int port = 5703;
    private Communicator cmn;
    private String serveradress = "chopin.skyseven.ru";

    public Client(){
        try {
            s = new Socket(serveradress, port);
            cmn = new Communicator(s);
            cmn.addDataReceievListeners(this::dataReceived);
            cmn.start();
            gameData.addSendDataListeners(this::sendStep);
            sendConnection();
        } catch (IOException e) {
            System.out.println("Подключение не удалось");
        }

    }

    public boolean isAlive(){
        return !stop && s.isConnected();
    }

    private void dataReceived(String data){
        if(data != null){
            System.out.println(data);
            if(data.contains("DATA")){
                getJoinLobby(data);
                return;
            }
            if(data.contains("LOGIN OK")){
                sendJoinLobby();
                return;
            }
            if(data.contains("LOGIN FAILED")){
                sendDisconect();
                return;
            }
            if(data.contains("move")){
                getElemPos(data);
                return;
            }
            if(data.contains("result")){
                getEnd(data);
                sendDisconect();
                return;
            }
            if(data.contains("BYE")){
                cmn.stop();
                return;
            }
            getStep(data);
        }
    }

    private void sendConnection(){
        if(cmn.isAlive()) {
            String data = "CONNECTION {\"LOGIN\":\"maxim\"}";
            cmn.sendData(data);
            System.out.println(data);
        }
    }

    private void sendJoinLobby(){
        if(cmn.isAlive()) {
            String data = "SOCKET JOINLOBBY {\"id\":null}";
            cmn.sendData(data);
            System.out.println(data);
        }
    }

    private static void getJoinLobby(String data){
        String[] data_mas = data.split("(\\{|\\})");
        String[] data_lobby = data_mas[2].split(",");
        int width = 0;
        int height = 0;
        int gameBarrierCount = 0;
        int playerBarrierCount = 0;
        String name = "";
        for(String s: data_lobby){
            if(s.contains("width")){
                String width_str = s.split(":")[1].split("\"")[0];
                width = Integer.parseInt(width_str);
            }
            if(s.contains("height")){
                String height_str = s.split(":")[1].split("\"")[0];
                height = Integer.parseInt(height_str);
            }
            if(s.contains("gameBarrierCount")){
                String BarrierCount_str = s.split(":")[1].split("\"")[0];
                gameBarrierCount = Integer.parseInt(BarrierCount_str);
            }
            if(s.contains("playerBarrierCount")){
                String BarrierCount_str = s.split(":")[1].split("\"")[0];
                playerBarrierCount = Integer.parseInt(BarrierCount_str);
            }
            if(s.contains("name")){
                name = s.split(":")[1];
            }
        }
        gameData.setSize(new Point(height,width));
        gameData.setPlayerBarriersCount(playerBarrierCount);
        gameData.setNameOpponent(name);
    }

    private static void getElemPos(String data){
        String[] data_mas = data.split("(\\{|\\})");
        String[] data_field = data_mas[1].split(":");
        Point position_player = null;
        Point position_opponent = null;
        ArrayList<Obstacle> obstacles = new ArrayList<>();
        boolean move = false;
        for(int i =0;i<data_field.length;i++){
            if(data_field[i].contains("position")){
                String[] position = data_field[i+1].split("\\[|\\]")[1].split(",");
                position_player = new Point(Integer.parseInt(position[0]),Integer.parseInt(position[1]));
            }
            if(data_field[i].contains("opponentPosition")){
                String[] opponent_position = data_field[i+1].split("\\[|\\]")[1].split(",");
                position_opponent = new Point(Integer.parseInt(opponent_position[0]),Integer.parseInt(opponent_position[1]));
            }
            if(data_field[i].contains("barriers")){
                String[] barriers = data_field[i+1].split("\\[|\\]");
                ArrayList<Point> mas_points = new ArrayList<>();
                for(String s: barriers){
                    if(!(s.equals(" ") || s.equals("")||s.equals(","))) {
                        String[] pair = s.split(",");
                        mas_points.add(new Point(Integer.parseInt(pair[0]), Integer.parseInt(pair[1])));
                    }
                }
                for(int j =0;j<mas_points.size()-3;j+=4){
                    obstacles.add(new Obstacle(mas_points.get(j),mas_points.get(j+1),mas_points.get(j+2),mas_points.get(j+3)));
                }
            }
            if(data_field[i].contains("move")){
                if(data_field[i+1].contains("true")) {
                    move = true;
                }else{
                    move = false;
                }
            }
        }
        gameData.setPositions(move, position_player, position_opponent, obstacles);
    }

    private static void getStep(String data){
        String[] data_mas = data.split("(\\{|\\})");
        String[] data_field = data_mas[1].split(":");
        Point position_opponent = null;
        ArrayList<Obstacle> obstacles = new ArrayList<>();
        for(int i =0;i<data_field.length;i++) {
            if (data_field[i].contains("opponentPosition")) {
                String[] opponent_position = data_field[i + 1].split("\\[|\\]")[1].split(",");
                position_opponent = new Point(Integer.parseInt(opponent_position[0]), Integer.parseInt(opponent_position[1]));
            }
            if (data_field[i].contains("barriers")) {
                String[] barriers = data_field[i + 1].split("\\[|\\]");
                ArrayList<Point> mas_points = new ArrayList<>();
                for (String s : barriers) {
                    if(!(s.equals(" ") || s.equals("")||s.equals(","))) {
                        String[] pair = s.split(",");
                        mas_points.add(new Point(Integer.parseInt(pair[0]), Integer.parseInt(pair[1])));
                    }
                }
                for (int j = 0; j < mas_points.size() - 3; j += 4) {
                    obstacles.add(new Obstacle(mas_points.get(j), mas_points.get(j + 1), mas_points.get(j + 2), mas_points.get(j + 3)));
                }
            }
        }
        gameData.setStep(position_opponent,obstacles);
    }

    private static void getEnd(String data){
        String[] data_mas = data.split("(\\{|\\})");
        String[] data_field = data_mas[1].split(":");
        Point position_opponent = null;
        ArrayList<Obstacle> obstacles = new ArrayList<>();
        String is_win = "";
        for(int i =0;i<data_field.length;i++) {
            if(data_field[i].contains("result")){
                if(data_field[i+1].contains("win")) {
                    is_win = "win";
                }else{
                    if(data_field[i+1].contains("lose")){
                        is_win = "lose";
                    }else{
                        is_win = "draw";
                    }
                }
                break;
            }
        }
        gameData.setResult(is_win);
    }

    private void sendStep(String data){
        cmn.sendData(data);
    }

    public void sendDisconect(){
        String data = "DISCONNECT {\"QUIT\":\"\"}";
        cmn.sendData(data);
        System.out.println(data);
    }

    public GameDataUI getGameData() {
        return gameData;
    }


}
