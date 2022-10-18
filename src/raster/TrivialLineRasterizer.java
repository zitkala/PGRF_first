package raster;

import static java.lang.Math.abs;

public class TrivialLineRasterizer extends LineRasterizer{

    public TrivialLineRasterizer(Raster raster) {
        super(raster);
    }

    @Override
    protected void drawLine(int x1, int y1, int x2, int y2, int color, boolean is_dashed) {
        final int granularity = 15;
        float k;
        float q;
        if ((x2 - x1) == 0){
            k = -1;
            q = y1;// - x1;
        }else {
            k = (float)(y2 - y1)/ (float)(x2 - x1);
            q = y1 - k * x1;
        }
        int abs_x = abs(x1 - x2);
        int abs_y = abs(y1 - y2);
        if ( abs_x > abs_y) {
            int spacing_offset = abs_x / granularity;
            if (spacing_offset == 0){ spacing_offset = 1; }
            if (x1 <= x2) {
                if (is_dashed) { setDashedPixelsCalculationX(x1, x2, k, q, color, spacing_offset); }
                else{ setPixelsCalculationX(x1, x2, k, q, color); }
            } else {
                if (is_dashed){ setDashedPixelsCalculationX(x2, x1, k, q, color, spacing_offset); }
                setPixelsCalculationX(x2, x1, k, q, color);
            }
        } else {
            int spacing_offset = abs_y / granularity;
            if (spacing_offset == 0){ spacing_offset = 1; }
            if (y1 <= y2) {
                if (is_dashed) { setDashedPixelCalculationY(x1, y1, y2, k, q, color, spacing_offset); }
                else { setPixelsCalculationY(x1, y1, y2, k, q, color); }
            } else {
                if (is_dashed) { setDashedPixelCalculationY(x1, y2, y1, k, q, color, spacing_offset); }
                else { setPixelsCalculationY(x1, y2, y1, k, q, color); }
            }
        }
    }

    private void setPixelsCalculationX(int x1, int x2, float k, float q, int color){
        for (int idx_x = x1; idx_x <= x2; idx_x++) {
            int temp_y = (int) (k * idx_x + q);
            raster.setPixel(idx_x, temp_y, color);
        }
    }
    private void setPixelsCalculationY(int x, int y1, int y2, float k, float q, int color){
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
    private void setDashedPixelsCalculationX(int x1, int x2, float k, float q, int color, int spacing_offset){
        for (int idx_x = x1; idx_x <= x2; idx_x += (spacing_offset*2)) {
            int temp_idx_limit;
            if (idx_x + spacing_offset > x2){ temp_idx_limit = x2; }
            else { temp_idx_limit = idx_x + spacing_offset; }
            for (int idx_temp_x = idx_x;  idx_temp_x < temp_idx_limit; idx_temp_x++){
                int temp_y = (int) (k * idx_temp_x + q);
                raster.setPixel(idx_temp_x, temp_y, color);
            }
        }
    }

    private void setDashedPixelCalculationY(int x, int y1, int y2, float k, float q, int color, int spacing_offset){
        for (int idx_y = y1; idx_y <= y2; idx_y += (spacing_offset*2)) {
            int temp_x;
            int temp_idx_limit;
            if (idx_y + spacing_offset > y2){ temp_idx_limit = y2; }
            else { temp_idx_limit = idx_y + spacing_offset; }
            for (int idx_temp_y = idx_y; idx_temp_y < temp_idx_limit; idx_temp_y++){
                if (k == -1){
                    temp_x = x;
                }else {
                    temp_x = (int) ((idx_temp_y - q)/k);
                }
                raster.setPixel(temp_x, idx_temp_y, color);
            }
        }
    }
}
