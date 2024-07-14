package Main;

import Main.Figures.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Vector;

public class AppManager {
    //Constants for the application
    final int FRAME_WIDTH = 1350;
    final int FRAME_HEIGHT = 800;
    final int initialStrokeWidth = 3;
    final Color PURPLE = new Color(76 , 0 , 176);

    //Enum for the paint mode
    enum PaintMode {
        LINE,
        RECTANGLE,
        OVAL,
        PENCIL ,
        ERASER ,
        NONE             //This is whenever Clear or Undo is clicked
    }

    public enum PaintStyle{
        NORMAL,
        FILL,
        DOTTED
    }

    //Application variables
    int x1;
    int x2;
    int y1;
    int y2;
    int startX;
    int startY;
    int finalX;
    int finalY;
    PaintMode currentPaintMode;
    PaintStyle currentPaintStyle;
    int currentStrokeWidth;
    Color currentColor;
    boolean hasBeenDragged;
    Stack<Drawable> drawables;


    private static AppManager ref = null;
    private AppManager(){
        x1 = x2 = y1 = y2 = startX = startY = finalX = finalY = 0;
        currentPaintMode = PaintMode.LINE;
        currentStrokeWidth = initialStrokeWidth;
        currentColor = Color.BLACK;
        currentPaintStyle = PaintStyle.NORMAL;
        hasBeenDragged = false;
        drawables = new Stack<>();
    }

    public static AppManager create(){
        if (ref == null)
            ref = new AppManager();
        return ref;
    }

}
