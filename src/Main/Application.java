package Main;

import Main.Figures.*;
import java.awt.Color;
import java.util.ArrayList;

class Application {
    //Constants for the application
    final int FRAME_WIDTH = 1550;
    final int FRAME_HEIGHT = 800;
    final Color PURPLE = new Color(76 , 0 , 176);
    final int SMALL_WIDTH = 3;
    final int MEDIUM_WIDTH = 5;
    final int LARGE_WIDTH = 8;


    //Enum for the paint mode
    enum PaintMode {
        LINE,
        RECTANGLE,
        OVAL,
        PENCIL ,
        ERASER ,
        NONE             //This is whenever Clear or Undo is clicked
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
    int currentStrokeWidth;
    Color currentColor;
    boolean isSolid;
    boolean isDotted;
    boolean hasBeenDragged;
    ArrayList<Drawable> drawables;

    private static Application ref = null;
    private Application(){
        x1 = x2 = y1 = y2 = startX = startY = finalX = finalY = 0;
        currentPaintMode = PaintMode.LINE;
        currentStrokeWidth = MEDIUM_WIDTH;
        currentColor = Color.BLACK;
        isSolid = false;
        isDotted = false;
        hasBeenDragged = false;
        drawables = new ArrayList<>();
    }

    public static Application create(){
        if (ref == null)
            ref = new Application();
        return ref;
    }

}
