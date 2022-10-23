package Models;

public final class Utils {
    private Utils(){}
    public static Point IntersectionPoint(float k1, float q1, float k2, float q2){// calculates intersection of two lines
        float intersect_x = (q2 - q1)/(k1 - k2);
        if(intersect_x < 0){
            intersect_x = (q1 - q2)/(k2 - k1);
        }
        float intersect_y = k1 * intersect_x + q1;
        return new Point((int)intersect_x,(int)intersect_y);
    }
}
