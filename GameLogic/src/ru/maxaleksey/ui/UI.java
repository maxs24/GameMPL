package ru.maxaleksey.ui;

import ru.maxaleksey.logic.GameDataUI;
import ru.maxaleksey.logic.GameField;
import ru.maxaleksey.logic.Obstacle;
import ru.maxaleksey.logic.Player;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;

public class UI {


    public static boolean choise(Point[] way_player, Point[] way_op){
        return way_player.length < way_op.length;
    }

    public static Point[] shortestWay(Hashtable<Point,ArrayList<Point>> graph, Point position, int win_pos){
        Hashtable<Point,Integer> bypass = new Hashtable<>();
        LinkedList<Point> queue = new LinkedList<>();
        HashSet<Point> set = new HashSet<>();
        Point last_pos = null;
        int d = 0;
        queue.addLast(position);
        while(queue.size() != 0){
            Point this_p = queue.poll();
            set.add(this_p);
            bypass.put(this_p,d);
            if(this_p.x == win_pos){
                last_pos = this_p;
                break;
            }
            for(Point p:graph.get(this_p)){
                if(!set.contains(p)){
                    queue.addLast(p);
                }
            }
            d += 1;
        }
        if(last_pos != null){
            Point this_p = last_pos;
            Point[] mas_point = new Point[d+1];
            mas_point[d] = last_pos;
            for(int i = d-1; i>=0; i--){
                for(Point p: graph.get(this_p)){
                    if(bypass.get(p) == i){
                        mas_point[i] = p;
                        this_p = p;
                        break;
                    }
                }
            }
            return mas_point;
        }
        return null;
    }

    public static Point move(Point[] way){
        return way[1];
    }

    private static Obstacle obst(Point one, Point two, int i){
        if(one.x == two.x){
            Point three_point = new Point(one.x+i,one.y);
            Point end_point = new Point(three_point.x,two.y);
            return new Obstacle(one,two,three_point,end_point);
        }else{
            Point three_point = new Point(one.x,one.y+i);
            Point end_point = new Point(three_point.x,two.y);
            return new Obstacle(one,two,three_point,end_point);
        }
    }

    public static void putObstacle(Point[] way_op, GameField gameField, Point player, Point op) throws Exception{
        for(int j = 1;j<way_op.length;j++){
            if(gameField.addObstacle(obst(way_op[j-1], way_op[j],1),player, op)){
                return;
            }else{
                if(gameField.addObstacle(obst(way_op[j-1], way_op[j],-1),player, op)){
                    return;
                }
            }
        }
        throw new Exception("Поставить препятствие нельзя");
    }
}
