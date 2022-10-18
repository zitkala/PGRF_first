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
    private final Polygon polygon = new Polygon();
    private Point start = null;
    private final PolygonRasterizer polygonRasterizer;

    private int mode = -1;
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
                            mode = 1;
                        } else { mode = 0; }
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

                start = new Point(e.getX(), e.getY());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
            }
        });
        panel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                //System.out.println("x: " + start.getX() + "y: " + start.getY());
                ((RasterBufferedImage)raster).clear();
                lineRasterizer.rasterize(new Line(start.getX(), start.getY(), e.getX(), e.getY()), 0x00ff00, false);
                panel.repaint();
            }
        });


        /*panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                //super.mouseReleased(e);
                Point point = new Point(e.getX(), e.getY());
                polygon.addPoint(point);
                polygonRasterizer.rasterize(polygon);
                panel.repaint();
            }
        });*/


    }
}
//TODO rovnoramm. trojuh. y = kx + q   // k2=1/k1
//TODO k3 = k1