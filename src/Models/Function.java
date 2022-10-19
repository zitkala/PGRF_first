package Models;

public class Function {
    private final float k;
    private final float q;
    public Function(float k, float q){
        this.k = k;
        this.q = q;
    }
    public float getK(){
        return k;
    }
    public float getQ(){
        return q;
    }
    public Point getIntersection(Function to_intersect){
        //k1x - (-1/k2)*x = q2/(-k2) - q1
        //((-1/k2) - k1)x = (q2/(-k2) - q1)/((-1/k2) * k1)
        //x = ((q2/(-k2) - q1)/(-1/k2) * k1))/((-1/k2) - k1)
        //float intersect_x = (((to_intersect.getQ()/(-to_intersect.getK())) - q)/((-1/ to_intersect.getK())*k)/((-1/to_intersect.getK())-k));
        //k1x + q1 = k2x + q2
        float intersect_x = (to_intersect.getQ() - q)/(k - to_intersect.getK());
        if(intersect_x < 0){
            intersect_x = (q - to_intersect.getQ())/(to_intersect.getK() - k);
        }
        float intersect_y = k * intersect_x + q;
        return new Point((int)intersect_x,(int)intersect_y);
    }
}
