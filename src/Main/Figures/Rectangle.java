package Main.Figures;

import Main.AppManager;

import java.awt.*;

public class Rectangle extends Shape {
    public Rectangle(int x1, int y1, int x2, int y2, Color color, int strokeWidth, AppManager.PaintStyle paintStyle) {
        super(x1, y1, x2, y2, color, strokeWidth, paintStyle);
    }

    @Override
    public void draw(Graphics2D g2D) {
        g2D.setColor(color);
        switch (paintStyle) {
            case NORMAL:
                g2D.setStroke(new BasicStroke(strokeWidth));
                g2D.drawRect(x1, y1, x2 - x1, y2 - y1);
                break;
            case FILL:
                g2D.fillRect(x1, y1, x2 - x1, y2 - y1);
                break;
            case DOTTED:
                float[] dashPattern = {5f, 5f}; // 5 pixels on, 5 pixels off
                g2D.setStroke(new BasicStroke(strokeWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, dashPattern, 0));
                g2D.drawRect(x1, y1, x2 - x1, y2 - y1);
                break;
        }
    }
}
