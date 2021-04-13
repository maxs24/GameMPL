package ru.maxaleksey.logic;

import java.awt.*;
import java.util.*;

// игровое поле
public class GameField {
    private Hashtable<Point, ArrayList<Point>> graph = new Hashtable<>();
    private ArrayList<Obstacle> obstacles = new ArrayList<>();
    private Point size;

    public GameField(Point sizefield){
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

    public boolean addObstacle(Obstacle obs, Point player_position_one, Point player_position_two){
        removeConn(obs.getOne()[0],obs.getOne()[1]);
        removeConn(obs.getTwo()[0],obs.getTwo()[1]);
        if(!(isConnected(player_position_one,true) && isConnected(player_position_two,false))){
            addConn(obs.getOne()[0],obs.getOne()[1]);
            addConn(obs.getTwo()[0],obs.getTwo()[1]);
            return false;
        }
        obstacles.add(obs);
        return true;
    }

    public ArrayList<Obstacle> getBarriers(){
        return obstacles;
    }

    private Obstacle newObstalce(){
        Random rnd = new Random();
        Point initial_point = new Point(rnd.nextInt(size.x),rnd.nextInt(size.y));
        int side;
        int i = 0;
        int j = 0;
        int k = 0;
        int t = 0;
        if(initial_point.x == 0){
            if(initial_point.y == 0){
                side = rnd.nextInt(2);
                if(side == 0){
                    i = 1;
                    j = 0;
                    k = 0;
                    t = 1;
                }else{
                    i = 0;
                    j = 1;
                    k = 1;
                    t = 0;
                }
            }else{
                if(initial_point.y == size.y - 1){
                    side = rnd.nextInt(2);
                    if(side == 0){
                        i = 1;
                        j = 0;
                        k = 0;
                        t = -1;
                    }else{
                        i = 0;
                        j = -1;
                        k = 1;
                        t = 0;
                    }
                }else{
                    side = rnd.nextInt(3);
                    switch (side){
                        case 0:
                            i = 0;
                            j = -1;
                            k = 1;
                            t = 0;
                        case 1:
                            i = 1;
                            j = 0;
                            k = 0;
                            int side_two = rnd.nextInt(2);
                            if(side_two == 0){
                                t = 1;
                            }else{
                                t = -1;
                            }
                        case 2:
                            i = 0;
                            j = 1;
                            k = 1;
                            t = 0;
                    }
                }
            }
        }else{
            if(initial_point.x == size.x - 1){
                if(initial_point.y == 0){
                    side = rnd.nextInt(2);
                    if(side == 0){
                        i = -1;
                        j = 0;
                        k = 0;
                        t = 1;
                    }else{
                        i = 0;
                        j = 1;
                        k = -1;
                        t = 0;
                    }
                }else{
                    if(initial_point.y == size.y - 1){
                        side = rnd.nextInt(2);
                        if(side == 0){
                            i = 0;
                            j = -1;
                            k = -1;
                            t = 0;
                        }else{
                            i = -1;
                            j = 0;
                            k = 0;
                            t = -1;
                        }
                    }else{
                        side = rnd.nextInt(3);
                        switch (side){
                            case 0:
                                i = 0;
                                j = -1;
                                k = -1;
                                t = 0;
                            case 1:
                                i = -1;
                                j = 0;
                                k = 0;
                                int new_side = rnd.nextInt(2);
                                if(new_side == 0){
                                    t = -1;
                                }else{
                                    t = 1;
                                }
                            case 2:
                                i = 0;
                                j = 1;
                                k = -1;
                                t = 0;
                        }
                    }
                }
            }else{
                if(initial_point.y == 0){
                    side = rnd.nextInt(3);
                    switch (side){
                        case 0:
                            i = -1;
                            j = 0;
                            k = 0;
                            t = 1;
                        case 1:
                            i = 0;
                            j = 1;
                            int new_side = rnd.nextInt(2);
                            if(new_side == 0){
                                k = -1;
                            }else{
                                k = 1;
                            }
                            t = 0;
                        case 2:
                            i = 1;
                            j = 0;
                            k = 0;
                            t = 1;
                    }
                }else{
                    if(initial_point.y == size.y - 1){
                        side = rnd.nextInt(3);
                        switch (side){
                            case 0:
                                i = -1;
                                j = 0;
                                k = 0;
                                t = -1;
                            case 1:
                                i = 0;
                                j = -1;
                                int new_side = rnd.nextInt(2);
                                if(new_side == 0){
                                    k = -1;
                                }else{
                                    k = 1;
                                }
                                t = 0;
                            case 2:
                                i = 1;
                                j = 0;
                                k = 0;
                                t = -1;
                        }
                    }else{
                        side = rnd.nextInt(4);
                        int new_side;
                        switch (side){
                            case 0:
                                i = 0;
                                j = -1;
                                new_side = rnd.nextInt(2);
                                if(new_side == 0){
                                    k = -1;
                                }else{
                                    k = 1;
                                }
                                t = 0;
                            case 1:
                                i = 1;
                                j = 0;
                                k = 0;
                                new_side = rnd.nextInt(2);
                                if(new_side == 0){
                                    t = -1;
                                }else{
                                    t = 1;
                                }
                            case 2:
                                i = -1;
                                j = 0;
                                k = 0;
                                new_side = rnd.nextInt(2);
                                if(new_side == 0){
                                    t = -1;
                                }else{
                                    t = 1;
                                }
                            case 3:
                                i = 0;
                                j = 1;
                                new_side = rnd.nextInt(2);
                                if(new_side == 0){
                                    k = -1;
                                }else{
                                    k = 1;
                                }
                                t = 0;
                        }
                    }
                }
            }
        }
        Point second_point = new Point(initial_point.x + i, initial_point.y + j);
        Point third_point = new Point(initial_point.x + k, initial_point.y + t);
        Point end_point = new Point(initial_point.x + i + k, initial_point.y + j + t);
        return new Obstacle(initial_point,second_point,third_point,end_point);
    }

    public void initilizationObstacles(int count_obstacles, Point player_one_position, Point player_two_position){
        int count = 0;
        while(count < count_obstacles){
            Obstacle new_obstacle = newObstalce();
            if(addObstacle(new_obstacle,player_one_position,player_two_position)){
                count++;
            }
        }
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

    public Point getSize(){
        return new Point(size.x,size.y);
    }

    public void setSize(Point size) {
        this.size = new Point(size.x,size.y);
        graph.clear();
        for(int i = 0;i<size.x;i++){
            for(int j=0;j<size.y;j++){
                if(i == size.x-1 || i == 0){
                    if(j == 0 || j == size.y-1){
                        setAngle(i,j);
                    }else{
                        setBorder(i,j);
                    }
                }else{
                    if(j == 0 || j == size.y-1){
                        setBorder(i,j);
                    }else{
                        setMiddle(i,j);
                    }
                }
            }
        }
    }

    public Hashtable<Point, ArrayList<Point>> getGraph() {
        return graph;
    }
}
