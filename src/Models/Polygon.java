package Models;

import java.util.ArrayList;
import java.util.List;

public class Polygon {
    protected List<Point> points;
    public Polygon(){
        this.points  = new ArrayList<>();
    }
    public void addPoint(Point point){
        points.add(point);
    }

    public void setPointAt(int idx, Point point){
        points.set(idx, point);
    }

    public Point getPoint(int idx){
        return points.get(idx);
    }

    public Point getLastPoint(){ return getPoint(getCount() - 1); }
    public Point getFirstPoint(){ return getPoint(0); }


    public int getCount(){
        return points.size();
    }
}
