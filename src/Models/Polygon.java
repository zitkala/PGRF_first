package Models;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

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

    public int getClosestPointIdx(Point point) {//returns index of the closest point
        int closest_idx = -1;
        int closest_dist = -1;
        for (int i = 0; i < getCount(); i++){
            int distance_idx = abs(point.getX() - points.get(i).getX()) + abs(point.getY() - points.get(i).getY());
            if (closest_dist == -1){
                closest_dist = distance_idx;
                closest_idx = i;
            } else if (distance_idx < closest_dist) {
                closest_idx = i;
                closest_dist = distance_idx;
            }
        }
        return closest_idx;

    }

    public Point getPoint(int idx){//is made to get point at index or cycle around  up to one element on each edge of the array
        if (idx == -1) {
            return getLastPoint();
        } else if (idx == getCount()) {
            return getFirstPoint();
        } else {
            return points.get(idx);
        }
    }

    public Point getLastPoint(){ return getPoint(getCount() - 1); }
    public Point getFirstPoint(){ return getPoint(0); }


    public int getCount(){
        return points.size();
    }
}
