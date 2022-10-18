package Models;

import java.util.ArrayList;
import java.util.List;

public class Polygon {
    private List<Point> points;
    public Polygon(){
        this.points  = new ArrayList<>();
    }
    public void addPoint(Point point){
        points.add(point);
    }

    public Point getPoint(int idx){
        return points.get(idx);
    }

    public int getCount(){
        return points.size();
    }
}
