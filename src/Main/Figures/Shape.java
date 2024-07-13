package Main.Figures;

import java.awt.*;


public abstract class Shape implements Drawable{
    protected int x1;
    protected int x2;
    protected int y1;
    protected int y2;
    protected Color color;
    protected int strokeWidth;
    protected boolean isDotted;

    public Shape(int x1 , int y1 , int x2 , int y2 , Color color , int strokeWidth , boolean isDotted){
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        this.color = color;
        this.strokeWidth = strokeWidth;
        this.isDotted = isDotted;
    }
}
