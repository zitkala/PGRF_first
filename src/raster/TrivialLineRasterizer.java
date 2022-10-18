package raster;

import static java.lang.Math.abs;

public class TrivialLineRasterizer extends LineRasterizer{

    public TrivialLineRasterizer(Raster raster) {
        super(raster);
    }

    @Override
    protected void drawLine(int x1, int y1, int x2, int y2, int color) {
        float k;
        float q;
        if ((x2 - x1) == 0){
            k = -1;
            q = y1;// - x1;
        }else {
            k = (float)(y2 - y1)/ (float)(x2 - x1);
            q = y1 - k * x1;
        }

        if (abs(x1 - x2) > abs(y1 - y2)) {
            if (x1 <= x2) {
                setPixelCalculationX(x1, x2, k, q, color);
            } else {
                setPixelCalculationX(x2, x1, k, q, color);
            }
        } else {
            if (y1 <= y2) {
                setPixelCalculationY(x1, y1, y2, k, q, color);
            } else {
                setPixelCalculationY(x1, y2, y1, k, q, color);
            }
        }
    }
    private void setPixelCalculationX(int x1, int x2, float k, float q, int color){
        for (int idx_x = x1; idx_x <= x2; idx_x++) {
            int temp_y = (int) (k * idx_x + q);
            raster.setPixel(idx_x, temp_y, color);
        }
    }
    private void setPixelCalculationY(int x, int y1, int y2, float k, float q, int color){
        for (int idx_y = y1; idx_y <= y2; idx_y++) {
            int temp_x;
            if (k == -1){
                temp_x = x;
            }else {
                temp_x = (int) ((idx_y - q)/k);
            }
            raster.setPixel(temp_x, idx_y, color);
        }
    }
}
