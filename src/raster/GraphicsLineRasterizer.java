package raster;

import java.awt.*;
import java.awt.image.BufferedImage;

public class GraphicsLineRasterizer extends LineRasterizer{
    public GraphicsLineRasterizer(Raster raster) {
        super(raster);
    }

    @Override
    protected void drawLine(int x1, int y1, int x2, int y2, int color) {
        Graphics g = ((RasterBufferedImage)raster).getBufferedImage().getGraphics();
        g.setColor(Color.decode(Integer.toHexString(color)));
        g.drawLine(x1, y1, x2, y2);
    }
    @Override
    protected void drawDashedLine(int x1, int y1, int x2, int y2, int color) {
        Graphics g = ((RasterBufferedImage)raster).getBufferedImage().getGraphics();
        //Stroke stroke = new BasicStroke(1, BasicStroke.CAP_BUTT,BasicStroke.JOIN_ROUND,1.0f,new float[2f,0f,2f], 2f);
    }
}
