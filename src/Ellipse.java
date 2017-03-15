/**
 * Created by xiu on 3/15/17.
 */
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

public class Ellipse extends JFrame {
    DrawingCanvas canvas;

    JLabel location;

    public Ellipse() {
        super();
        Container container = getContentPane();

        canvas = new DrawingCanvas();
        container.add(canvas);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 2));
        panel.add(new JLabel("x,y: ", JLabel.RIGHT));
        location = new JLabel("");
        panel.add(location);

        container.add(panel, BorderLayout.SOUTH);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        setSize(600,300);
        setVisible(true);
    }

    public static void main(String arg[]) {
        new Ellipse();
    }

    class DrawingCanvas extends JPanel {
        double x, y, w, h;
        double mx, my, mw, mh;

        int x1, y1, x2, y2;

        Ellipse2D ellipse;
        Ellipse2D mellipse;

        Ellipse2D selectedShape;

        Rectangle2D boundingRec;

        Cursor curCursor;
        public DrawingCanvas() {
            x = 20;
            y = 20;
            w = 100;
            h = 100;


            mx = 150;
            my = 20;
            mw = 100;
            mh = 100;

            setBackground(Color.white);
            addMouseListener(new MyMouseListener());
            addMouseMotionListener(new MyMouseMotionListener());
        }
        public void paint(Graphics g) {
            Graphics2D g2D = (Graphics2D) g;
            ellipse = new Ellipse2D.Double(x, y, w, h);
            mellipse = new Ellipse2D.Double(mx, my, mw, mh);
            g2D.draw(ellipse);
            g2D.draw(mellipse);
            drawArrow(g,x+100,y+50,mx,my+50);

            if (boundingRec != null) {
                drawHighlightSquares(g2D, boundingRec);
            }
            if (curCursor != null)
                setCursor(curCursor);
        }
        public void drawHighlightSquares(Graphics2D g2D, Rectangle2D r) {
            double x = r.getX();
            double y = r.getY();
            double w = r.getWidth();
            double h = r.getHeight();
            g2D.setColor(Color.black);

            g2D.fill(new Rectangle.Double(x - 3.0, y - 3.0, 6.0, 6.0));
            g2D
                    .fill(new Rectangle.Double(x + w * 0.5 - 3.0, y - 3.0, 6.0,
                            6.0));
            g2D.fill(new Rectangle.Double(x + w - 3.0, y - 3.0, 6.0, 6.0));
            g2D
                    .fill(new Rectangle.Double(x - 3.0, y + h * 0.5 - 3.0, 6.0,
                            6.0));
            g2D.fill(new Rectangle.Double(x + w - 3.0, y + h * 0.5 - 3.0, 6.0,
                    6.0));
            g2D.fill(new Rectangle.Double(x - 3.0, y + h - 3.0, 6.0, 6.0));
            g2D.fill(new Rectangle.Double(x + w * 0.5 - 3.0, y + h - 3.0, 6.0,
                    6.0));
            g2D.fill(new Rectangle.Double(x + w - 3.0, y + h - 3.0, 6.0, 6.0));
        }

        class MyMouseListener extends MouseAdapter {
            public void mousePressed(MouseEvent e) {
                if (ellipse.contains(e.getX(), e.getY())) {
                    selectedShape = ellipse;
                    if (boundingRec != null)
                        boundingRec = ellipse.getBounds2D();
                    displayParameters(selectedShape);
                } else {
                    boundingRec = null;
                    location.setText("");
                }
                canvas.repaint();
                x1 = e.getX();
                y1 = e.getY();
            }
            public void mouseReleased(MouseEvent e) {
                if (ellipse.contains(e.getX(), e.getY())) {
                    boundingRec = ellipse.getBounds2D();
                    selectedShape = ellipse;

                    displayParameters(selectedShape);
                }

                canvas.repaint();
            }

            public void mouseClicked(MouseEvent e) {
                if (ellipse.contains(e.getX(), e.getY())) {
                    selectedShape = ellipse;
                    boundingRec = ellipse.getBounds2D();

                    displayParameters(selectedShape);
                } else {
                    if (boundingRec != null)
                        boundingRec = null;
                    location.setText("");
                }
                canvas.repaint();
            }
        }
        class MyMouseMotionListener extends MouseMotionAdapter {
            public void mouseDragged(MouseEvent e) {
                if (ellipse.contains(e.getX(), e.getY())) {
                    boundingRec = null;
                    selectedShape = ellipse;
                    x2 = e.getX();
                    y2 = e.getY();
                    x = x + x2 - x1;
                    y = y + y2 - y1;
                    x1 = x2;
                    y1 = y2;
                }
                if (selectedShape != null)
                    displayParameters(selectedShape);
                canvas.repaint();
            }

            public void mouseMoved(MouseEvent e) {
                if (ellipse != null) {
                    if (ellipse.contains(e.getX(), e.getY())) {
                        curCursor = Cursor
                                .getPredefinedCursor(Cursor.HAND_CURSOR);
                    } else {
                        curCursor = Cursor.getDefaultCursor();
                    }
                }
                canvas.repaint();
            }
        }

        public void displayParameters(Shape shape) {
            double x = selectedShape.getX();
            double y = selectedShape.getY();
            double w = selectedShape.getWidth();
            double h = selectedShape.getHeight();
            String locString = "(" + Double.toString(x) + ","
                    + Double.toString(y) + ")";
            String sizeString = "(" + Double.toString(w) + ","
                    + Double.toString(h) + ")";
            location.setText(locString);
        }


        void drawArrow(Graphics g1, double x1, double y1, double x2, double y2) {
            final int ARR_SIZE = 4;
            Graphics2D g = (Graphics2D) g1.create();

            double dx = x2 - x1, dy = y2 - y1;
            double angle = Math.atan2(dy, dx);
            int len = (int) Math.sqrt(dx*dx + dy*dy);
            AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
            at.concatenate(AffineTransform.getRotateInstance(angle));
            g.transform(at);
            g.setColor(Color.blue);
            // Draw horizontal arrow starting in (0, 0)
            g.drawLine(0, 0, len, 0);

            g.fillPolygon(new int[] {len, len-ARR_SIZE, len-ARR_SIZE, len},new int[] {0, -ARR_SIZE, ARR_SIZE, 0}, 4);
        }
    }
}


