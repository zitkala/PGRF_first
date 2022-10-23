package Models;

import raster.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


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

    private int edit = -1;

    private int mode = 0;
    public Canvas(int width, int height) {
        //Setup
        frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.setTitle("PGRF");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        raster = new RasterBufferedImage(width, height);
        lineRasterizer = new TrivialLineRasterizer(raster);
        polygonRasterizer = new PolygonRasterizer(lineRasterizer);

        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ((RasterBufferedImage)raster).present(g);
            }
        };
        raster.clear();
        panel.setPreferredSize(new Dimension(width, height));

        frame.add(panel, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);

        panel.requestFocus();
        panel.requestFocusInWindow();
        panel.repaint();
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()){
                    case KeyEvent.VK_T://mode switching button T
                        if(mode == 0){//polygon mode
                            triangle = new IsoscelesTriangle();
                            mode = 1;
                        } else {//triangle mode
                            polygon = new Polygon();
                            mode = 0;

                        }
                        break;
                    case KeyEvent.VK_C://clear button C
                        raster.clear();
                        polygon = new Polygon();
                        triangle = new IsoscelesTriangle();
                        panel.repaint();
                        break;
                }
            }
        });
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {//mouse pressed event
                switch (e.getButton()) {
                    case (MouseEvent.BUTTON1) -> {//left button pressed
                        int temp_count;
                        //uses polygon or triangle depending on mode
                        if (mode == 0) {
                            temp_count = polygon.getCount();
                        } else {
                            temp_count = triangle.getCount();
                        }
                        if (temp_count < 1) {//checks whether the polygon or triangle has any point already
                            start = new Point(e.getX(), e.getY());
                        } else {
                            start = null;
                            end = new Point(e.getX(), e.getY());
                        }
                    }
                    case (MouseEvent.BUTTON3) -> {//right mouse button pressed
                        end = new Point(e.getX(), e.getY());
                        switch (mode) {//sets edit value to index of the closest point
                            case (0) -> edit = polygon.getClosestPointIdx(end);
                            case (1) -> edit = triangle.getClosestPointIdx(end);
                        }
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {//mouse released event
                raster.clear();//clears raster to prepare for new rasterization
                switch (e.getButton()) {
                    case (MouseEvent.BUTTON1)://left button route
                        switch (mode) {
                            case (0) -> {//ads points if any were set and rasterizes polygon
                                if (start != null) {
                                    polygon.addPoint(start);
                                }
                                if (end != null) {
                                    polygon.addPoint(end);
                                }
                                polygonRasterizer.rasterize(polygon, 0x00ff00, false);
                            }
                            case (1) -> {//does the same for triangle
                                if (start != null) {
                                    triangle.addPoint(start);
                                }
                                if (end != null) {
                                    triangle.addPoint(end);
                                }
                                polygonRasterizer.rasterize(triangle, 0x00ff00, false);
                            }
                        }
                        break;
                    case (MouseEvent.BUTTON3)://right mouse button route
                        if (edit != -1 && end != null) {//checks if edit value is valid
                            switch (mode) {
                                case (0) -> {
                                    polygon.setPointAt(edit, end);//changes Point at specified index
                                    polygonRasterizer.rasterize(polygon, 0x00ff00, false);
                                }
                                case (1) -> {
                                    triangle.setPointAt(edit, end);
                                    polygonRasterizer.rasterize(triangle, 0x00ff00, false);
                                }
                            }
                        }
                        break;
                }//resets values for next time
                edit = -1;
                start = null;
                end = null;
                panel.repaint();//paints current raster
            }
        });
        panel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {//mouse motion event
                end = new Point(e.getX(), e.getY());
                raster.clear();
                fixRange(end, width, height);// checks if the point is in valid range
                if(SwingUtilities.isLeftMouseButton(e)) {//left button route
                    switch (mode) {
                        case (0):
                            if (polygon.getCount() < 1) {//draws line from mouse pressed point to current mouse location
                                lineRasterizer.rasterize(new Line(start.getX(), start.getY(), end.getX(), end.getY()), 0x00ff00, true);
                            } else {//draws polygon, then draws two dashed lines connecting to current mouse location from first and last points
                                polygonRasterizer.rasterize(polygon, 0x00ff00, false);
                                lineRasterizer.rasterize(new Line(polygon.getLastPoint(), end), 0x00ff00, true);
                                lineRasterizer.rasterize(new Line(polygon.getFirstPoint(), end), 0x00ff00, true);
                            }
                            break;
                        case (1):
                            switch (triangle.getCount()) {//similar to polygon but calculates last point in inside Triangle class
                                case (0) -> lineRasterizer.rasterize(new Line(start, end), 0x00ff00, true);
                                case (1) -> lineRasterizer.rasterize(new Line(triangle.getLastPoint(), end), 0x00ff00, true);
                                default -> {
                                    triangle.addPoint(end);
                                    polygonRasterizer.rasterize(triangle, 0x00ff00, false);
                                }
                            }
                            break;
                        }
                    } else if (SwingUtilities.isRightMouseButton(e)) {//right button route
                        switch (mode){
                            case(0):
                                if (edit != -1) {
                                    //checks for valid edit index value, draws polygon,
                                    //then draws two lines from next to the point that gets edited to mouse location
                                    polygonRasterizer.rasterize(polygon, 0x00ff00, false);
                                    Point left = polygon.getPoint(edit - 1);
                                    Point right = polygon.getPoint( edit + 1);
                                    lineRasterizer.rasterize(new Line(left, end), 0x00ff00, true);
                                    lineRasterizer.rasterize(new Line(right, end), 0x00ff00, true);
                                }
                                break;

                            case(1):
                                if (edit != -1) {//same as polygon
                                    polygonRasterizer.rasterize(triangle, 0x00ff00, false);
                                    Point left = triangle.getPoint(edit - 1);
                                    Point right = triangle.getPoint( edit + 1);
                                    lineRasterizer.rasterize(new Line(left, end), 0x00ff00, true);
                                    lineRasterizer.rasterize(new Line(right, end), 0x00ff00, true);
                                }
                                break;
                        }
                    }
                panel.repaint();
            }

        });



    }
    private void fixRange(Point point_to_check, int width, int height){//in case of invalid values sets input Point to values inside an acceptable range
        if(point_to_check.getX() < 0) { point_to_check.setX(0); }
        if(point_to_check.getX() >= width) { point_to_check.setX(width - 1); }
        if(point_to_check.getY() < 0) { point_to_check.setY(0); }
        if(point_to_check.getY() >= height) { point_to_check.setY(height -1 ); }
    }

}
