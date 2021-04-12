package ru.maxaleksey.ui;

import ru.maxaleksey.logic.GameField;
import ru.maxaleksey.logic.Obstacle;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;

public class UI {


    public static boolean choise(Point[] way_player, Point[] way_op){
        return way_player.length <= way_op.length;
    }

    public static Point[] shortestWay(Hashtable<Point,ArrayList<Point>> graph, Point position, int win_pos){
        Hashtable<Point,Integer> bypass = new Hashtable<>();
        HashSet<Point> set = new HashSet<>();
        Point last_pos = null;
        int d = 0;
        int n = 0;
        bypass.put(position,0);
        boolean flag = true;
        while(flag){
            ArrayList<Point> mas_p = new ArrayList<>();
            for(Point p: bypass.keySet()){
                if(bypass.get(p) == d){
                    if(p.x == win_pos){
                        n = d;
                        last_pos = p;
                        flag = false;
                        break;
                    }
                    set.add(p);
                    for(Point loc: graph.get(p)){
                        if(!set.contains(loc)) {
                            mas_p.add(loc);
                        }
                    }
                }
            }
            for(Point p: mas_p){
                bypass.put(p,d+1);
            }
            d += 1;
        }
        Point this_p = last_pos;
        Point[] mas_point = new Point[n+1];
        mas_point[n] = last_pos;
        for(int i = n-1; i>=0; i--){
            for(Point p: graph.get(this_p)){
                if(contain(bypass,p)){
                    if (bypass.get(p) == i) {
                        mas_point[i] = p;
                        this_p = p;
                        break;
                    }
                }
            }
        }
        return mas_point;
    }

    public static Point move(Point[] way){
        return way[1];
    }

    private static Obstacle obst(Point one, Point two, int i,Point size){
        if(one.x == two.x){
            if(one.x+i < size.x) {
                Point three_point = new Point(one.x + i, one.y);
                Point end_point = new Point(three_point.x, two.y);
                return new Obstacle(one, two, three_point, end_point);
            }else{
                Point three_point = new Point(one.x - i, one.y);
                Point end_point = new Point(three_point.x, two.y);
                return new Obstacle(one, two, three_point, end_point);
            }
        }else{
            if(one.y + i < size.y) {
                Point three_point = new Point(one.x, one.y + i);
                Point end_point = new Point(two.x, one.y + i);
                return new Obstacle(one, two, three_point, end_point);
            }else{
                Point three_point = new Point(one.x, one.y - i);
                Point end_point = new Point(two.x, one.y - i);
                return new Obstacle(one, two, three_point, end_point);
            }
        }
    }

    public static void putObstacle(Point[] way_op, GameField gameField, Point player, Point op) throws Exception{
        for(int j = 1;j<way_op.length;j++) {
            if (gameField.addObstacle(obst(way_op[j - 1], way_op[j], 1, gameField.getSize()), player, op)) {
                return;
            }
        }
        throw new Exception("Поставить препятствие нельзя");
    }

    private static boolean contain(Hashtable<Point,Integer> map, Point p){
        for(Point point: map.keySet()) {
            if(p.x == point.x && p.y == point.y){
                return true;
            }
        }
        return false;
    }
}
