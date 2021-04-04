package ru.maxaleksey.logic;

import java.awt.*;
import java.util.*;

// игровое поле
public class GameField {
    private Hashtable<Point, ArrayList<Point>> graph = new Hashtable<>();
    Point size;

    GameField(Point sizefield){
        size = sizefield;
        for(int i = 0;i<sizefield.x;i++){
            for(int j=0;j<sizefield.y;j++){
                if(i == sizefield.x-1 || i == 0){
                    if(j == 0 || j == sizefield.y-1){
                        setAngle(i,j);
                    }else{
                        setBorder(i,j);
                    }
                }else{
                    if(j == 0 || j == sizefield.y-1){
                        setBorder(i,j);
                    }else{
                        setMiddle(i,j);
                    }
                }
            }
        }
    }

    private void setAngle(int i, int j){
        Point p = new Point(i,j);
        ArrayList<Point> mas_p = new ArrayList<>();
        if(i == 0){
            if(j == 0){
                mas_p.add(new Point(i+1,j));
                mas_p.add(new Point(i,j+1));
            }else{
                mas_p.add(new Point(i,j-1));
                mas_p.add(new Point(i+1,j));
            }
        }else{
            if(j == 0){
                mas_p.add(new Point(i-1,j));
                mas_p.add(new Point(i,j+1));
            }else{
                mas_p.add(new Point(i-1,j));
                mas_p.add(new Point(i,j-1));
            }
        }
        graph.put(p,mas_p);
    }

    private void setBorder(int i, int j){
        Point p = new Point(i,j);
        ArrayList<Point> mas_p = new ArrayList<>();
        if(i == 0){
            mas_p.add(new Point(i,j+1));
            mas_p.add(new Point(i,j-1));
            mas_p.add(new Point(i+1,j));
        }else{
            if(i == size.x - 1){
                mas_p.add(new Point(i,j+1));
                mas_p.add(new Point(i,j-1));
                mas_p.add(new Point(i-1,j));
            }else{
                if(j == 0){
                    mas_p.add(new Point(i+1,j));
                    mas_p.add(new Point(i-1,j));
                    mas_p.add(new Point(i,j+1));
                }else{
                    mas_p.add(new Point(i+1,j));
                    mas_p.add(new Point(i-1,j));
                    mas_p.add(new Point(i,j-1));
                }
            }
        }
        graph.put(p,mas_p);
    }

    private void setMiddle(int i, int j){
        Point p = new Point(i,j);
        ArrayList<Point> mas_p = new ArrayList<>();
        mas_p.add(new Point(i,j+1));
        mas_p.add(new Point(i,j-1));
        mas_p.add(new Point(i-1,j));
        mas_p.add(new Point(i+1,j));
        graph.put(p,mas_p);
    }

    public void addObstacle(Obstacle obs, Point player_position_one, Point player_position_two){
        removeConn(obs.getOne()[0],obs.getOne()[1]);
        removeConn(obs.getTwo()[0],obs.getTwo()[1]);
        if(!(isConnected(player_position_one,true) && isConnected(player_position_two,false))){
            addConn(obs.getOne()[0],obs.getOne()[1]);
            addConn(obs.getTwo()[0],obs.getTwo()[1]);
        }
    }

    public Obstacle newObstalce(){

    }

    private void removeConn(Point p1, Point p2){
        graph.get(p1).remove(p2);
        graph.get(p2).remove(p1);
    }

    private void addConn(Point p1, Point p2){
        graph.get(p1).add(p2);
        graph.get(p2).add(p1);
    }

    private boolean isConnected(Point pp, boolean first){
        LinkedList<Point> queue = new LinkedList<>();
        queue.addLast(pp);
        HashSet<Point> set = new HashSet<>();
        while(queue.size() != 0){
            Point this_p = queue.poll();
            set.add(this_p);
            if(first){
                if(this_p.x == 0){
                    return true;
                }
            }else{
                if(this_p.x == size.x - 1){
                    return true;
                }
            }
            for (Point p:graph.get(this_p)) {
                if(!set.contains(p)) {
                    queue.addLast(p);
                }
            }
        }
        return false;
    }
}
