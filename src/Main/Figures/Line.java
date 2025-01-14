package Main.Figures;

import Main.AppManager;

import java.awt.*;

public class Line extends Shape {
    public Line(int x1 , int y1 , int x2 , int y2 , Color color , int strokeWidth , AppManager.PaintStyle paintStyle){
        super(x1,y1,x2,y2,color , strokeWidth , paintStyle);
    }
    @Override
    public void draw(Graphics2D g2D) {
        g2D.setColor(color);
        if (paintStyle == AppManager.PaintStyle.DOTTED) {
            float[] dashPattern = {5f, 5f}; // 5 pixels on, 5 pixels off
            g2D.setStroke(new BasicStroke(strokeWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, dashPattern, 0));
        }
        else
            g2D.setStroke(new BasicStroke(strokeWidth));

        g2D.drawLine(x1,y1,x2,y2);
    }
}
