package raster;

import Models.Line;
import Models.Point;
import Models.Polygon;

public class PolygonRasterizer {
    private LineRasterizer lineRasterizer;
    public PolygonRasterizer(LineRasterizer lineRasterizer){
        this.lineRasterizer = lineRasterizer;
    }
    public void rasterize(Polygon polygon, int color, boolean use_dashed){
        for (int i = 0; i < polygon.getCount(); i++) {
            if (i == polygon.getCount() - 1) {
                lineRasterizer.rasterize(new Line(
                                polygon.getPoint(i).getX(),
                                polygon.getPoint(i).getY(),
                                polygon.getPoint(0).getX(),
                                polygon.getPoint(0).getY()),
                                color, use_dashed);

            } else {
                lineRasterizer.rasterize(new Line(
                                polygon.getPoint(i).getX(),
                                polygon.getPoint(i).getY(),
                                polygon.getPoint(i + 1).getX(),
                                polygon.getPoint(i + 1).getY()),
                                color, use_dashed);
            }
        }
    }
}
