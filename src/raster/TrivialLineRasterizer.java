package raster;

import static java.lang.Math.abs;

public class TrivialLineRasterizer extends LineRasterizer{
//Trivial line rasterizer
//+ easy to make
//+ easy to edit
//- does not have good-looking lines

    public TrivialLineRasterizer(Raster raster) {
        super(raster);
    }

    @Override
    protected void drawLine(int x1, int y1, int x2, int y2, int color, boolean is_dashed) {
        final int granularity = 15;//sets to how many sections will the line split
        float k;
        float q;
        int abs_x = abs(x1 - x2);
        int abs_y = abs(y1 - y2);
        if ((abs_x) == 0){//handles situation where x values of points are same
            int spacing_offset = abs_y / granularity;
            if (spacing_offset == 0){ spacing_offset = 1; }
            if (is_dashed) { setDashedPixelCalculationYStraight(y1, y2, x1, color, spacing_offset); }
            else { setPixelCalculationYStraight(y1, y2, x1, color); }
            return;

        } else if (abs_y == 0){//handles situation where y values of points are same
            int spacing_offset = abs_x / granularity;
            if (spacing_offset == 0){ spacing_offset = 1; }
            if (is_dashed) { setDashedPixelCalculationXStraight(x1, x2, y1, color, spacing_offset); }
            else { setPixelCalculationXStraight(x1, x2, y1, color); }
            return;
        } else {//handles normal run of rasterizer
            k = (float)(y2 - y1)/ (float)(x2 - x1);
            q = y1 - k * x1;
        }

        if ( abs_x > abs_y) {//decides on what values will rasterizing depend
            int spacing_offset = abs_x / granularity;
            if (spacing_offset == 0){ spacing_offset = 1; }
            if (is_dashed) { setDashedPixelsCalculationX(x1, x2, k, q, color, spacing_offset); }
            else{ setPixelsCalculationX(x1, x2, k, q, color); }
        } else {
            int spacing_offset = abs_y / granularity;
            if (spacing_offset == 0){ spacing_offset = 1; }
            if (is_dashed) { setDashedPixelCalculationY(y1, y2, k, q, color, spacing_offset); }
            else { setPixelsCalculationY(y1, y2, k, q, color); }
        }
    }

    private void setPixelCalculationXStraight(int x1, int x2, int y, int color) { //draws line where y doesn't change
        if (x1 > x2){
            int temp = x1;
            x1 = x2;
            x2 = temp;
        }

        for (int i = x1; i <= x2; i++){
            raster.setPixel(i, y, color);
        }
    }

    private void setPixelCalculationYStraight(int y1, int y2, int x, int color) {//draws line where x doesn't change
        if (y1 > y2){
            int temp = y1;
            y1 = y2;
            y2 = temp;
        }

        for (int i = y1; i <= y2; i++){
            raster.setPixel(x, i, color);
        }
    }

    private void setDashedPixelCalculationXStraight (int x1, int x2, int y, int color, int spacing_offset) { //draws dashed line where y doesn't change
        if (x1 > x2){
            int temp = x1;
            x1 = x2;
            x2 = temp;
        }

        for (int idx_x = x1; idx_x <= x2; idx_x += (spacing_offset * 2)) {
            int temp_idx_limit;
            if (idx_x + spacing_offset > x2){ temp_idx_limit = x2; }
            else { temp_idx_limit = idx_x + spacing_offset; }

            for (int idx_temp_x = idx_x;  idx_temp_x < temp_idx_limit; idx_temp_x++){
                raster.setPixel(idx_temp_x, y, color);
            }
        }
    }

    private void setDashedPixelCalculationYStraight (int y1, int y2, int x, int color, int spacing_offset) { //draws dashed line where x doesn't change
        if (y1 > y2){
            int temp = y1;
            y1 = y2;
            y2 = temp;
        }
        for (int idx_y = y1; idx_y <= y2; idx_y += (spacing_offset * 2)) {
            int temp_idx_limit;
            if (idx_y + spacing_offset > y2){ temp_idx_limit = y2; }
            else { temp_idx_limit = idx_y + spacing_offset; }
            for (int idx_temp_y = idx_y; idx_temp_y < temp_idx_limit; idx_temp_y++){
                raster.setPixel(x, idx_temp_y, color);
            }
        }
    }

    private void setPixelsCalculationX(int x1, int x2, float k, float q, int color){//draws line with length in x values
        if (x1 > x2){
            int temp = x1;
            x1 = x2;
            x2 = temp;
        }
        for (int idx_x = x1; idx_x <= x2; idx_x++) {
            int temp_y = (int) (k * idx_x + q);
            raster.setPixel(idx_x, temp_y, color);
        }
    }
    private void setPixelsCalculationY(int y1, int y2, float k, float q, int color){//draws line with length in y values
        if (y1 > y2){
            int temp = y1;
            y1 = y2;
            y2 = temp;
        }
        for (int idx_y = y1; idx_y <= y2; idx_y++) {
            int temp_x = (int) ((idx_y - q)/k);
            raster.setPixel(temp_x, idx_y, color);
        }
    }
    private void setDashedPixelsCalculationX(int x1, int x2, float k, float q, int color, int spacing_offset){//draws dashed line with length in x values
        if (x1 > x2){
            int temp = x1;
            x1 = x2;
            x2 = temp;
        }
        for (int idx_x = x1; idx_x <= x2; idx_x += (spacing_offset * 2)) {
            int temp_idx_limit;
            if (idx_x + spacing_offset > x2){ temp_idx_limit = x2; }
            else { temp_idx_limit = idx_x + spacing_offset; }

            for (int idx_temp_x = idx_x;  idx_temp_x < temp_idx_limit; idx_temp_x++){
                int temp_y = (int) (k * idx_temp_x + q);
                raster.setPixel(idx_temp_x, temp_y, color);
            }
        }
    }

    private void setDashedPixelCalculationY(int y1, int y2, float k, float q, int color, int spacing_offset){//draws dashed line with length in y values
        if (y1 > y2){
            int temp = y1;
            y1 = y2;
            y2 = temp;
        }
        for (int idx_y = y1; idx_y <= y2; idx_y += (spacing_offset * 2)) {
            int temp_idx_limit;
            if (idx_y + spacing_offset > y2){ temp_idx_limit = y2; }
            else { temp_idx_limit = idx_y + spacing_offset; }
            for (int idx_temp_y = idx_y; idx_temp_y < temp_idx_limit; idx_temp_y++){

                int temp_x = (int) ((idx_temp_y - q)/k);
                raster.setPixel(temp_x, idx_temp_y, color);

            }
        }
    }
}
