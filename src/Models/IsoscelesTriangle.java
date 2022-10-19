package Models;

import static java.lang.Math.abs;

public class IsoscelesTriangle {
    private final Polygon triangle;
    public IsoscelesTriangle(){
        triangle = new Polygon();
    }
    public Polygon getPolygon(){
        return triangle;
    }
    public int getCount(){
        return triangle.getCount();
    }
    public void addPoint(Point point){
        switch (getCount()){
            case(0):
            case(1):
                addBasePoint(point);
                break;
            default:
                addTopPoint(point);
                break;
        }
    }
    public Point getLastPoint(){
        return triangle.getLastPoint();
    }

    private void addBasePoint(Point point){
        if (triangle.getCount() < 3){
            triangle.addPoint(point);
        }else{
            System.out.println("Trying to add point to already existing base");
        }
    }
    private void addTopPoint(Point mouse_point){
        if (triangle.getCount() < 2) {
            System.out.println("Base not made yet");
        } else if (triangle.getCount() > 3) {
            System.out.println("Adding more points than possible for a triangle");
        } else {
            int x1 = triangle.getPoint(0).getX();
            int y1 = triangle.getPoint(0).getY();
            int x2 = triangle.getPoint(1).getX();
            int y2 = triangle.getPoint(1).getY();
            Function base;
            if ((x2 - x1) == 0){
                base = new Function(0, y1);
            }else {
                float temp_k = ((float)(y2 - y1)/(float)(x2 - x1));
                base = new Function(temp_k, y1 - temp_k * x1);
            }
            float temp_distance_x = abs(x1 - x2);
            float temp_distance_y = abs(y1 - y2);
            float midpoint_x;
            if (x1 < x2){
                midpoint_x = (float)x1 + temp_distance_x/2.f;
            }else {
                midpoint_x = (float)x2 + temp_distance_x/2.f;
            }
            float midpoint_y;
            if (y1 < y2){
                midpoint_y = (float)y1 + temp_distance_y/2.f;
            }else {
                midpoint_y = (float)y2 + temp_distance_y/2.f;
            }
            Function parallel = new Function(base.getK(), mouse_point.getY() - base.getK() * mouse_point.getX());
            //midpoint_y = 1/(-base.getK())*midpointx + q
            //q = midpoint_y - 1/(-base.getK())*midpoint_x
            float ppdc_q = midpoint_y - ((1/(-base.getK())) * midpoint_x);
            Function perpendicular = new Function(1/(-base.getK()), ppdc_q);

            if (triangle.getCount() == 3){
                editPoint(2, parallel.getIntersection(perpendicular));
            } else { triangle.addPoint(parallel.getIntersection(perpendicular)); }
            //TODO top point calculation
        }

    }
    public void editPoint(int idx, Point point_to_change){
        triangle.setPointAt(idx, point_to_change);
    }
    

}
