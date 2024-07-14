package Main.Figures;
import Main.AppManager;

import java.awt.*;
import java.util.ArrayList;

public class Pencil implements Drawable{
    private Color color;
    private int strokeWidth;
    private AppManager.PaintStyle paintStyle;
    private ArrayList<Point> points;

    public Pencil(Color color , int strokeWidth , AppManager.PaintStyle paintStyle){
        points = new ArrayList<>();
        this.color = color;
        this.strokeWidth = strokeWidth;
        this.paintStyle = paintStyle;
    }

    public void addPoint(Point p){
        points.add(p);
    }

    @Override
    public void draw(Graphics2D g2D){
        g2D.setColor(color);
        if (paintStyle == AppManager.PaintStyle.DOTTED) {
            float[] dashPattern = {3f, 3f}; // 5 pixels on, 5 pixels off
            g2D.setStroke(new BasicStroke(strokeWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, dashPattern, 0));
        }
        else
            g2D.setStroke(new BasicStroke(strokeWidth));

        for (int i = 0; i < points.size() - 1; i++)
        {
            g2D.drawLine(points.get(i).x , points.get(i).y , points.get(i+1).x , points.get(i+1).y );
        }
    }
}
