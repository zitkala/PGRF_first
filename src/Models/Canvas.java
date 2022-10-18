package Models;

import Models.Polygon;
import raster.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.GenericArrayType;

public class Canvas {
    private final JFrame frame;
    private final JPanel panel;
    private final Raster raster;
    private final LineRasterizer lineRasterizer;
    private Polygon polygon = new Polygon();
    private IsoscelesTriangle triangle = new IsoscelesTriangle();
    private Point start = null;
    private Point end = null;
    private final PolygonRasterizer polygonRasterizer;

    private int mode = 0;
    public Canvas(int width, int height) {
        frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.setTitle("PGRF");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //x = width/2;
        //y = height/2;
        raster = new RasterBufferedImage(width, height);
        lineRasterizer = new TrivialLineRasterizer(raster);
        polygonRasterizer = new PolygonRasterizer(lineRasterizer);

        //raster.setPixel(x, y, 0xffffff);
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ((RasterBufferedImage)raster).present(g);
            }
        };
        panel.setPreferredSize(new Dimension(width, height));

        frame.add(panel, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
        
        panel.requestFocus();
        panel.requestFocusInWindow();
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()){
                    case KeyEvent.VK_T:
                        if(mode == 0){
                            triangle = new IsoscelesTriangle();
                            mode = 1;
                        } else {
                            polygon = new Polygon();
                            mode = 0;

                        }
                        break;
                    case KeyEvent.VK_C:
                        System.out.println("Clearing");
                        ((RasterBufferedImage)raster).clear();
                        panel.repaint();
                        break;
                }
            }
        });
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int temp_count;

                if (mode == 0){ temp_count = polygon.getCount(); }
                else { temp_count = triangle.getCount(); }

                if (temp_count < 1){
                    start = new Point(e.getX(), e.getY());
                } else {
                    start = null;
                    end = new Point(e.getX(),e.getY());
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                System.out.println("ms released");
                ((RasterBufferedImage) raster).clear();
                switch (mode) {
                    case (0):
                        if (start != null) { polygon.addPoint(start); }
                        if (end != null) {
                            polygon.addPoint(end);
                            polygonRasterizer.rasterize(polygon, 0x00ff00, false);
                        }
                        break;
                    case (1):
                        if (start != null) { triangle.addPoint(start); }
                        if (end != null) { triangle.addPoint(end); }
                        polygonRasterizer.rasterize(triangle.getPolygon(), 0x00ff00, false);
                        break;
                }
                start = null;
                end = null;
                panel.repaint();
            }
        });
        panel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                //System.out.println("x: " + start.getX() + "y: " + start.getY());
                end = new Point(e.getX(), e.getY());
                switch (mode) {
                    case (0):
                        ((RasterBufferedImage) raster).clear();
                        if (polygon.getCount() < 1) {
                            lineRasterizer.rasterize(new Line(start.getX(), start.getY(), end.getX(), end.getY()), 0x00ff00, true);
                        } else {
                            polygonRasterizer.rasterize(polygon, 0x00ff00, false);
                            lineRasterizer.rasterize(new Line(polygon.getLastPoint(), end),0x00ff00, true);
                            lineRasterizer.rasterize(new Line(polygon.getFirstPoint(), end), 0x00ff00, true);

                        }
                        panel.repaint();
                        break;
                    case (1):
                        switch (triangle.getCount()){
                            case(0):
                                lineRasterizer.rasterize(new Line(start, end), 0x00ff00, true);
                                break;
                            case(1):
                                lineRasterizer.rasterize(new Line(triangle.getLastPoint(), end),0x00ff00, true);
                                break;
                            default:
                                triangle.addPoint(end);
                                polygonRasterizer.rasterize(triangle.getPolygon(),0x00ff00, false);
                        }
                        break;

                }
            }
        });



    }
}
//TODO rovnoramm. trojuh. y = kx + q   // k2=1/k1
//TODO k3 = k1