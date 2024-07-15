package Main.Figures;
import java.awt.*;
import java.util.ArrayList;
public class Eraser implements Drawable {
    private int strokeWidth;
    private ArrayList<Point> points;

    public Eraser(int strokeWidth){
        points = new ArrayList<>();
        this.strokeWidth = strokeWidth;
    }

    public void addPoint(Point p){
        points.add(p);
    }

    @Override
    public void draw(Graphics2D g2D){
        g2D.setColor(Color.WHITE);
        g2D.setStroke(new BasicStroke(strokeWidth));
        for (int i = 0; i < points.size() - 1; i++)
        {
            g2D.fillOval((points.get(i).x)-strokeWidth/2 , (points.get(i).y)-strokeWidth/2 , strokeWidth, strokeWidth);;
        }
    }
}
