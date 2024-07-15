package Main;
import Main.Figures.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Point;
import java.awt.BasicStroke;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class PaintBrush extends JFrame {
    //Layout Buttons
    private JButton clearBtn;
    private JButton undoBtn;
    private JButton redoBtn;
    private JToggleButton lineBtn;
    private JToggleButton rectBtn;
    private JToggleButton ovalBtn;
    private JToggleButton pencilBtn;
    private JToggleButton eraserBtn;
    private JRadioButton normalRadioBtn;
    private JRadioButton fillRadioBtn;
    private JRadioButton dottedRadioBtn;
    private JSlider strokeWidthSlider;
    private JButton blackBtn;
    private JButton whiteBtn;
    private JButton redBtn;
    private JButton greenBtn;
    private JButton blueBtn;
    private JButton purpleBtn;
    private JButton yellowBtn;

    //Pencil and Eraser references
    private Pencil pencil;
    private Eraser eraser;

    //Ref to AppManager class (Singleton)
    private final AppManager myApp;

    //Ref to the drawing panel
    private final DrawPanel drawingPanel;


    //Helper Classes
    class PressListener implements MouseListener{

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {
            Point p = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), drawingPanel);
            myApp.x1 = p.x;
            myApp.y1 = p.y;
            switch (myApp.currentPaintMode){
                case PENCIL:
                    pencil = new Pencil(myApp.currentColor , myApp.currentStrokeWidth , myApp.currentPaintStyle);
                    myApp.drawables.push(pencil);
                    pencil.addPoint(p);
                    break;
                case ERASER:
                    eraser = new Eraser(myApp.currentStrokeWidth);
                    myApp.drawables.push(eraser);
                    eraser.addPoint(p);
                    break;

            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            Point p = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), drawingPanel);
            myApp.x2 = p.x;
            myApp.y2 = p.y;
            calculate();
            switch (myApp.currentPaintMode) {
                case LINE:
                    if (myApp.hasBeenDragged) {
                        Line l = new Line(myApp.x1, myApp.y1, myApp.x2, myApp.y2, myApp.currentColor, myApp.currentStrokeWidth , myApp.currentPaintStyle);
                        myApp.drawables.push(l);
                    }
                    break;

                case RECTANGLE:
                    if (myApp.hasBeenDragged) {
                        Rectangle r = new Rectangle(myApp.startX, myApp.startY, myApp.finalX, myApp.finalY, myApp.currentColor, myApp.currentStrokeWidth,  myApp.currentPaintStyle);
                        myApp.drawables.push(r);
                    }
                    break;
                case OVAL:
                    if (myApp.hasBeenDragged) {
                        Oval o = new Oval(myApp.startX, myApp.startY, myApp.finalX, myApp.finalY, myApp.currentColor, myApp.currentStrokeWidth ,myApp.currentPaintStyle);
                        myApp.drawables.push(o);
                    }
                    break;
                case PENCIL:
                    pencil.addPoint(p);
                    break;
                case ERASER:
                    eraser.addPoint(p);
                    break;
            }
            myApp.hasBeenDragged = false;
            drawingPanel.repaint();

            //Deleting the Redo history whenever a new shape is drawn
            myApp.redoStack.removeAllElements();

            redoBtn.setEnabled(false);
            undoBtn.setEnabled(true);
        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {}
    }
    class DragListener implements MouseMotionListener {
        @Override
        public void mouseDragged(MouseEvent e) {
            myApp.hasBeenDragged = true;
            Point p = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), drawingPanel);
            myApp.x2 = p.x;
            myApp.y2 = p.y;
            switch(myApp.currentPaintMode){
                case PENCIL:
                    pencil.addPoint(p);
                    break;
                case ERASER:
                    eraser.addPoint(p);
                    break;
            }
            drawingPanel.repaint();
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            Point p = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), drawingPanel);
            myApp.x2 = p.x;
            myApp.y2 = p.y;
            drawingPanel.repaint();
        }
    }
    class ColorListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == blackBtn)
                myApp.currentColor = Color.BLACK;
            else if (e.getSource() == whiteBtn)
                myApp.currentColor = Color.WHITE;
            else if (e.getSource() == redBtn)
                myApp.currentColor = Color.RED;
            else if (e.getSource() == greenBtn)
                myApp.currentColor = Color.GREEN;
            else if (e.getSource() == blueBtn)
                myApp.currentColor = Color.BLUE;
            else if (e.getSource() == yellowBtn)
                myApp.currentColor = Color.YELLOW;
            else if (e.getSource() == purpleBtn)
                myApp.currentColor = myApp.PURPLE;
        }
    }
    class PaintModeListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            if (e.getSource() == lineBtn)
                myApp.currentPaintMode = AppManager.PaintMode.LINE;
            else if (e.getSource() == rectBtn)
                myApp.currentPaintMode = AppManager.PaintMode.RECTANGLE;
            else if (e.getSource() == ovalBtn)
                myApp.currentPaintMode = AppManager.PaintMode.OVAL;
            else if (e.getSource() == pencilBtn)
                myApp.currentPaintMode = AppManager.PaintMode.PENCIL;
             else if (e.getSource() == eraserBtn)
                myApp.currentPaintMode = AppManager.PaintMode.ERASER;

        }
    }
    class FunctionsListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e){
            if (e.getSource() == clearBtn){
                myApp.drawables.removeAllElements();
                myApp.redoStack.removeAllElements();
                redoBtn.setEnabled(false);
                undoBtn.setEnabled(false);
            }
            else if (e.getSource() == undoBtn){
                    myApp.redoStack.push(myApp.drawables.pop());
                    if (myApp.drawables.isEmpty())
                        undoBtn.setEnabled(false);
                    redoBtn.setEnabled(true);
                }
            else if (e.getSource() == redoBtn) {
                myApp.drawables.push(myApp.redoStack.pop());
                if (myApp.redoStack.isEmpty())
                    redoBtn.setEnabled(false);
                undoBtn.setEnabled(true);
            }
            AppManager.PaintMode tmpPaintMode = myApp.currentPaintMode;
            myApp.currentPaintMode = AppManager.PaintMode.NONE;
            drawingPanel.repaint();
            myApp.currentPaintMode = tmpPaintMode;
        }
    }
    class RadioButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == normalRadioBtn)
                myApp.currentPaintStyle = AppManager.PaintStyle.NORMAL;
            else if (e.getSource() == fillRadioBtn)
                myApp.currentPaintStyle = AppManager.PaintStyle.FILL;
            else if (e.getSource() == dottedRadioBtn)
                myApp.currentPaintStyle = AppManager.PaintStyle.DOTTED;
        }
    }

    //This class enables the application to have smoother graphics using the buffered image
    class DrawPanel extends JPanel{
        private BufferedImage buffer;

        public DrawPanel() {
            buffer = new BufferedImage(myApp.FRAME_WIDTH, myApp.FRAME_HEIGHT, BufferedImage.TYPE_INT_RGB);
            setBackground(Color.WHITE);

            //Resizes the buffer image whenever the window is resized
            this.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e){
                    BufferedImage newBuffer = new BufferedImage(PaintBrush.this.getWidth(), PaintBrush.this.getWidth(), BufferedImage.TYPE_INT_RGB);
                    Graphics2D g2D = newBuffer.createGraphics();
                    g2D.drawImage(buffer, 0, 0, null);
                    g2D.dispose();
                    buffer = newBuffer;
                }
            });
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2D = buffer.createGraphics();
            g2D.setColor(this.getBackground());
            g2D.clearRect(0, 0, buffer.getWidth(), buffer.getHeight());
            g2D.fillRect(0, 0, buffer.getWidth(), buffer.getHeight());
            for (Drawable drawable : myApp.drawables)
                drawable.draw(g2D);

            g2D.setColor(myApp.currentColor);
            if (myApp.currentPaintStyle == AppManager.PaintStyle.DOTTED) {
                float[] dashPattern = {5f, 5f}; // 5 pixels on, 5 pixels off
                g2D.setStroke(new BasicStroke(myApp.currentStrokeWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, dashPattern, 0));
            }
            else
                g2D.setStroke(new BasicStroke(myApp.currentStrokeWidth));

            calculate();
            switch(myApp.currentPaintMode) {
                case LINE:
                    if (myApp.hasBeenDragged)
                        g2D.drawLine(myApp.x1, myApp.y1, myApp.x2, myApp.y2);
                    break;
                case RECTANGLE:
                    if (myApp.hasBeenDragged)
                        if (myApp.currentPaintStyle == AppManager.PaintStyle.FILL)
                            g2D.fillRect(myApp.startX, myApp.startY, myApp.finalX - myApp.startX, myApp.finalY - myApp.startY);
                        else
                            g2D.drawRect(myApp.startX, myApp.startY, myApp.finalX - myApp.startX, myApp.finalY - myApp.startY);
                    break;
                case OVAL:
                    if (myApp.hasBeenDragged)
                        if (myApp.currentPaintStyle == AppManager.PaintStyle.FILL)
                            g2D.fillOval(myApp.startX, myApp.startY, myApp.finalX - myApp.startX, myApp.finalY - myApp.startY);
                        else
                            g2D.drawOval(myApp.startX, myApp.startY, myApp.finalX - myApp.startX, myApp.finalY - myApp.startY);
                    break;
                case PENCIL:
                    if (myApp.y2 > 15)
                        g2D.fillOval(myApp.x2 - myApp.currentStrokeWidth/2 , myApp.y2 - myApp.currentStrokeWidth/2 , myApp.currentStrokeWidth , myApp.currentStrokeWidth);
                    break;
                case ERASER:
                    g2D.setColor(Color.WHITE);
                    g2D.fillOval(myApp.x2 - myApp.currentStrokeWidth/2 , myApp.y2 - myApp.currentStrokeWidth/2 , myApp.currentStrokeWidth , myApp.currentStrokeWidth);
                    break;

            }
            g.drawImage(buffer, 0, 0, null);
            g2D.dispose();
        }
    }


    //Constructor
    public PaintBrush() {
        myApp = AppManager.create();
        drawingPanel = new DrawPanel();
        initComponents();
    }


    //Initializing Components
    @SuppressWarnings("unchecked")
    private void initComponents() {
        drawingPanel.addMouseListener(new PressListener());
        drawingPanel.addMouseMotionListener(new DragListener());

        ColorListener colorListener = new ColorListener();
        PaintModeListener paintModeListener = new PaintModeListener();
        FunctionsListener functionsListener = new FunctionsListener();
        RadioButtonListener radioButtonListener = new RadioButtonListener();


        //Functions
        JLabel functionsLabel = new JLabel("Functions:");
        clearBtn = new JButton("Clear");
        clearBtn.addActionListener(functionsListener);

        undoBtn = new JButton("Undo");
        undoBtn.setEnabled(false);
        undoBtn.addActionListener(functionsListener);

        redoBtn = new JButton("Redo");
        redoBtn.setEnabled(false);
        redoBtn.addActionListener(functionsListener);


        //Figures
        JLabel paintModeLabel = new JLabel("Paint Mode:");
        lineBtn = new JToggleButton("Line");
        lineBtn.addActionListener(paintModeListener);
        lineBtn.setSelected(true);

        rectBtn = new JToggleButton("Rectangle");
        rectBtn.addActionListener(paintModeListener);

        ovalBtn = new JToggleButton("Oval");
        ovalBtn.addActionListener(paintModeListener);

        pencilBtn = new JToggleButton("Pencil");
        pencilBtn.addActionListener(paintModeListener);

        eraserBtn = new JToggleButton("Eraser");
        eraserBtn.addActionListener(paintModeListener);

        ButtonGroup figuresGroup = new ButtonGroup();
        figuresGroup.add(lineBtn);
        figuresGroup.add(rectBtn);
        figuresGroup.add(ovalBtn);
        figuresGroup.add(pencilBtn);
        figuresGroup.add(eraserBtn);


        //Radio Buttons
        JLabel styleLabel = new JLabel("Style:");
        normalRadioBtn = new JRadioButton("Normal");
        normalRadioBtn.addActionListener(radioButtonListener);
        normalRadioBtn.setSelected(true);

        fillRadioBtn = new JRadioButton("Fill");
        fillRadioBtn.addActionListener(radioButtonListener);

        dottedRadioBtn = new JRadioButton("Dotted");
        dottedRadioBtn.addActionListener(radioButtonListener);

        ButtonGroup radioButtonsGroup = new ButtonGroup();
        radioButtonsGroup.add(normalRadioBtn);
        radioButtonsGroup.add(fillRadioBtn);
        radioButtonsGroup.add(dottedRadioBtn);

        //Stroke Width buttons
        strokeWidthSlider = new JSlider();
        strokeWidthSlider.setMinimum(myApp.minStrokeWidth);
        strokeWidthSlider.setMaximum(myApp.maxStrokeWidth);
        strokeWidthSlider.setValue(myApp.initialStrokeWidth);
        JLabel sliderLabel = new JLabel("Size:  " + strokeWidthSlider.getValue());
        strokeWidthSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                sliderLabel.setText("Size:  " + strokeWidthSlider.getValue());
                myApp.currentStrokeWidth = strokeWidthSlider.getValue();
            }
        });

        //Colors
        JLabel colorsLabel = new JLabel("Colors:");
        blackBtn = new JButton();
        blackBtn.setBackground(Color.BLACK);
        blackBtn.addActionListener(colorListener);

        whiteBtn = new JButton();
        whiteBtn.setBackground(Color.WHITE);
        whiteBtn.addActionListener(colorListener);

        redBtn = new JButton();
        redBtn.setBackground(Color.RED);
        redBtn.addActionListener(colorListener);

        greenBtn = new JButton();
        greenBtn.setBackground(Color.GREEN);
        greenBtn.addActionListener(colorListener);

        blueBtn = new JButton();
        blueBtn.setBackground(Color.BLUE);
        blueBtn.addActionListener(colorListener);

        purpleBtn = new JButton();
        purpleBtn.setBackground(myApp.PURPLE);
        purpleBtn.addActionListener(colorListener);

        yellowBtn = new JButton();
        yellowBtn.setBackground(Color.YELLOW);
        yellowBtn.addActionListener(colorListener);

        //Layout
        getContentPane().setBackground(Color.WHITE);
        setTitle("PaintBrush");
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(myApp.FRAME_WIDTH,myApp.FRAME_HEIGHT);
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(10)
                                .addComponent(functionsLabel)
                                .addGap(10)
                                .addComponent(clearBtn)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(undoBtn)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(redoBtn)
                                .addGap(22)
                                .addComponent(paintModeLabel)
                                .addGap(10)
                                .addComponent(lineBtn)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rectBtn)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ovalBtn)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pencilBtn)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(eraserBtn)
                                .addGap(22)
                                .addComponent(styleLabel)
                                .addGap(10)
                                .addComponent(normalRadioBtn)
                                .addGap(5)
                                .addComponent(fillRadioBtn)
                                .addGap(5)
                                .addComponent(dottedRadioBtn)
                                .addGap(22)
                                .addComponent(sliderLabel)
                                .addGap(10)
                                .addComponent(strokeWidthSlider, 120,120,120)
                                .addGap(22)
                                .addComponent(colorsLabel)
                                .addGap(10)
                                .addComponent(blackBtn, 25 ,  25, 25)
                                .addComponent(whiteBtn, 25 ,  25, 25)
                                .addComponent(redBtn, 25 ,  25, 25)
                                .addComponent(greenBtn, 25 ,  25, 25)
                                .addComponent(blueBtn, 25 ,  25, 25)
                                .addComponent(purpleBtn, 25 ,  25, 25)
                                .addComponent(yellowBtn, 25 ,  25, 25))
                        .addComponent(drawingPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(functionsLabel)
                                        .addComponent(clearBtn)
                                        .addComponent(undoBtn)
                                        .addComponent(redoBtn)
                                        .addComponent(paintModeLabel)
                                        .addComponent(lineBtn)
                                        .addComponent(rectBtn)
                                        .addComponent(ovalBtn)
                                        .addComponent(pencilBtn)
                                        .addComponent(eraserBtn)
                                        .addComponent(styleLabel)
                                        .addComponent(normalRadioBtn)
                                        .addComponent(fillRadioBtn)
                                        .addComponent(dottedRadioBtn)
                                        .addComponent(sliderLabel)
                                        .addComponent(strokeWidthSlider, 30, 30, 30)
                                        .addComponent(colorsLabel)
                                        .addComponent(blackBtn, 25 ,  25, 25)
                                        .addComponent(whiteBtn, 25 ,  25, 25)
                                        .addComponent(redBtn, 25 ,  25, 25)
                                        .addComponent(greenBtn, 25 ,  25, 25)
                                        .addComponent(blueBtn, 25 ,  25, 25)
                                        .addComponent(purpleBtn, 25 ,  25, 25)
                                        .addComponent(yellowBtn, 25 ,  25, 25))
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(drawingPanel, GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE))
        );
        setLocationRelativeTo(null);
    }


    //Function to calculate the start/end x and y values
    private void calculate(){
        myApp.startX = Math.min(myApp.x1,myApp.x2);
        myApp.startY = Math.min(myApp.y1,myApp.y2);

        myApp.finalX = Math.max(myApp.x1 , myApp.x2);
        myApp.finalY = Math.max(myApp.y1 , myApp.y2);
    }


    //main function
    public static void main(String[] args) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PaintBrush.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PaintBrush.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PaintBrush.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PaintBrush.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PaintBrush().setVisible(true);
            }
        });

    }


}
