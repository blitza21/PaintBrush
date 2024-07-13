package Main.Figures;

import java.awt.*;

public class Oval extends Shape {
    private boolean isSolid;
    public Oval(int x1 , int y1 , int x2 , int y2 , Color color , int strokeWidth , boolean isDotted ,  boolean isSolid ){
        super(x1,y1,x2,y2,color ,strokeWidth, isDotted);
        this.isSolid = isSolid;
    }

    @Override
    public void draw(Graphics2D g2D) {
        g2D.setColor(color);
        if (isDotted){
            float[] dashPattern = {5f, 5f}; // 5 pixels on, 5 pixels off
            g2D.setStroke(new BasicStroke(strokeWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, dashPattern, 0));
        }
        else
            g2D.setStroke(new BasicStroke(strokeWidth));

        if (isSolid)
            g2D.fillOval(x1, y1, x2 -x1, y2 - y1);
        else
            g2D.drawOval(x1, y1, x2 -x1, y2 - y1);
    }
}
