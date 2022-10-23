package Models;

import static java.lang.Math.abs;

public class IsoscelesTriangle extends Polygon{

    public IsoscelesTriangle(){
    }
    @Override
    public void addPoint(Point point){
        switch (getCount()) {
            case (0), (1) -> addBasePoint(point);
            default -> addTopPoint(point);
        }
    }

    private void addBasePoint(Point point){
        if (getCount() < 3){
            super.addPoint(point);
        }else{
            System.out.println("Trying to add point to already existing base");
        }
    }
    private void addTopPoint(Point mouse_point){
        if (getCount() < 2) {
            System.out.println("Base not made yet");
        } else if (getCount() > 3) {
            System.out.println("Adding more points than possible for a triangle");
        } else {

            int x1 = getPoint(0).getX();
            int y1 = getPoint(0).getY();
            int x2 = getPoint(1).getX();
            int y2 = getPoint(1).getY();

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

            float base_k;

            if ((temp_distance_x) == 0) {
                decideAdd(new Point(mouse_point.getX(), (int) midpoint_y));
                return;
            } else if (temp_distance_y == 0) {
                decideAdd(new Point((int)midpoint_x, mouse_point.getY()));
                return;

            } else {
                base_k = ((float)(y2 - y1)/(float)(x2 - x1));
            }

            float prl_q = mouse_point.getY() - base_k * mouse_point.getX();
            float ppdc_q = midpoint_y - ((1/(-base_k)) * midpoint_x);

            decideAdd(Utils.IntersectionPoint(base_k, prl_q, (1/(-base_k)),ppdc_q));
        }

    }
    private void decideAdd(Point point_to_add) {
        if (getCount() == 3){
            setPointAt(2, point_to_add);
        } else { super.addPoint(point_to_add); }
    }
}
