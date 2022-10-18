package raster;

import java.awt.*;
import java.awt.image.BufferedImage;

public class GraphicsLineRasterizer extends LineRasterizer{
    public GraphicsLineRasterizer(Raster raster) {
        super(raster);
    }

    @Override
    protected void drawLine(int x1, int y1, int x2, int y2, int color, boolean is_dashed) {
        Graphics g = ((RasterBufferedImage)raster).getBufferedImage().getGraphics();
        g.setColor(Color.decode(Integer.toHexString(color)));
        if(is_dashed) {
            Graphics2D g2d = (Graphics2D) g;
            float[] dash1 = {2f, 0f, 2f};
            Stroke stroke = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 1.0f, dash1, 2f);
            g2d.setStroke(stroke);
            g2d.drawLine(x1, y1, x2, y2);
        } else {
            g.drawLine(x1, y1, x2, y2);
        }
    }
}
