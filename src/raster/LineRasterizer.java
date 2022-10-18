package raster;

import Models.Line;

public abstract class LineRasterizer {
    protected Raster raster;

    public LineRasterizer(Raster raster){
        this.raster = raster;
    }

    public void rasterize(Line line,int color, boolean use_dashed){
        if (use_dashed) {
            drawLine(line.getX1(), line.getY1(), line.getX2(), line.getY2(), color, true);
        }else {
            drawLine(line.getX1(), line.getY1(), line.getX2(), line.getY2(), color, false);
        }
    }
    public void clearLine(Line line){
        drawLine(line.getX1(), line.getY1(), line.getX2(), line.getY2(), 0x000000, false);
    }

    protected void drawLine(int x1, int y1, int x2, int y2, int color, boolean is_dashed) {

    }
}
