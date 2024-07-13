package Main;
import Main.Figures.*;
import javax.swing.*;
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
    private JToggleButton lineBtn;
    private JToggleButton rectBtn;
    private JToggleButton ovalBtn;
    private JToggleButton pencilBtn;
    private JToggleButton eraserBtn;
    private JCheckBox solidCheckBox;
    private JCheckBox dottedCheckBox;
    private JToggleButton smallBtn;
    private JToggleButton mediumBtn;
    private JToggleButton largeBtn;
    private JToggleButton blackBtn;
    private JToggleButton redBtn;
    private JToggleButton greenBtn;
    private JToggleButton blueBtn;
    private JToggleButton purpleBtn;
    private JToggleButton yellowBtn;

    //Penicl and Eraser references
    private Pencil pencil;
    private Eraser eraser;

    //Ref to Application class (Singleton)
    private Application myApp;

    //Ref to the drawing panel
    private DrawPanel drawingPanel;


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
                    pencil = new Pencil(myApp.currentColor , myApp.currentStrokeWidth , myApp.isDotted);
                    myApp.drawables.add(pencil);
                    pencil.addPoint(p);
                    break;
                case ERASER:
                    eraser = new Eraser(myApp.currentStrokeWidth);
                    myApp.drawables.add(eraser);
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
                        Line l = new Line(myApp.x1, myApp.y1, myApp.x2, myApp.y2, myApp.currentColor, myApp.currentStrokeWidth , myApp.isDotted);
                        myApp.drawables.add(l);
                    }
                    break;

                case RECTANGLE:
                    if (myApp.hasBeenDragged) {
                        Rectangle r = new Rectangle(myApp.startX, myApp.startY, myApp.finalX, myApp.finalY, myApp.currentColor, myApp.currentStrokeWidth,  myApp.isDotted , myApp.isSolid);
                        myApp.drawables.add(r);
                    }
                    break;
                case OVAL:
                    if (myApp.hasBeenDragged) {
                        Oval o = new Oval(myApp.startX, myApp.startY, myApp.finalX, myApp.finalY, myApp.currentColor, myApp.currentStrokeWidth , myApp.isDotted , myApp.isSolid);
                        myApp.drawables.add(o);
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
            if (e.getSource() == yellowBtn)
                myApp.currentColor = Color.YELLOW;
            else if (e.getSource() == purpleBtn)
                myApp.currentColor = myApp.PURPLE;
            else if (e.getSource() == blueBtn)
                myApp.currentColor = Color.BLUE;
            else if (e.getSource() == greenBtn)
                myApp.currentColor = Color.GREEN;
            else if (e.getSource() == redBtn)
                myApp.currentColor = Color.RED;
            else if (e.getSource() == blackBtn)
                myApp.currentColor = Color.BLACK;
        }
    }
    class PaintModeListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            if (e.getSource() == eraserBtn)
                myApp.currentPaintMode = Application.PaintMode.ERASER;
            else if (e.getSource() == pencilBtn)
                myApp.currentPaintMode = Application.PaintMode.PENCIL;
            else if (e.getSource() == ovalBtn)
                myApp.currentPaintMode = Application.PaintMode.OVAL;
            else if (e.getSource() == rectBtn)
                myApp.currentPaintMode = Application.PaintMode.RECTANGLE;
            else if (e.getSource() == lineBtn)
                myApp.currentPaintMode = Application.PaintMode.LINE;

        }
    }
    class StrokeWidthListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            if (e.getSource() == smallBtn)
                myApp.currentStrokeWidth = myApp.SMALL_WIDTH;
            else if (e.getSource() == mediumBtn)
                myApp.currentStrokeWidth = myApp.MEDIUM_WIDTH;
            else if (e.getSource() == largeBtn)
                myApp.currentStrokeWidth = myApp.LARGE_WIDTH;
        }
    }
    class FunctionsListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e){
            if (e.getSource() == clearBtn){
                Rectangle r = new Rectangle(0 , 0 , getWidth() , getHeight(), Color.WHITE , myApp.currentStrokeWidth, false , true);
                myApp.drawables.add(r);
            }
            else if (e.getSource() == undoBtn){
                if (!myApp.drawables.isEmpty())
                    myApp.drawables.remove(myApp.drawables.size() - 1);
            }
            Application.PaintMode tmpPaintMode = myApp.currentPaintMode;
            myApp.currentPaintMode = Application.PaintMode.NONE;
            repaint();
            myApp.currentPaintMode = tmpPaintMode;
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

            if (myApp.isDotted){
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
                        if (myApp.isSolid)
                            g2D.fillRect(myApp.startX, myApp.startY, myApp.finalX - myApp.startX, myApp.finalY - myApp.startY);
                        else
                            g2D.drawRect(myApp.startX, myApp.startY, myApp.finalX - myApp.startX, myApp.finalY - myApp.startY);
                    break;
                case OVAL:
                    if (myApp.hasBeenDragged)
                        if (myApp.isSolid)
                            g2D.fillOval(myApp.startX, myApp.startY, myApp.finalX - myApp.startX, myApp.finalY - myApp.startY);
                        else
                            g2D.drawOval(myApp.startX, myApp.startY, myApp.finalX - myApp.startX, myApp.finalY - myApp.startY);
                    break;
                case PENCIL:
                    g2D.fillRect(myApp.x2 , myApp.y2 , myApp.currentStrokeWidth , myApp.currentStrokeWidth);
                    break;
                case ERASER:
                    g2D.setColor(Color.WHITE);
                    g2D.fillRect(myApp.x2 , myApp.y2 , myApp.currentStrokeWidth , myApp.currentStrokeWidth);
                    break;

            }
            g.drawImage(buffer, 0, 0, null);
            g2D.dispose();
        }
    }


    //Constructor
    public PaintBrush() {
        myApp = Application.create();
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
        StrokeWidthListener strokeWidthListener = new StrokeWidthListener();
        FunctionsListener functionsListener = new FunctionsListener();

        //Labels
        JLabel functionsLabel = new JLabel("Functions:");
        JLabel paintModeLabel = new JLabel("Paint Mode:");
        JLabel sizeLabel = new JLabel("Size:");
        JLabel colorsLabel = new JLabel("Colors:");


        //Functions
        clearBtn = new JButton("Clear");
        clearBtn.addActionListener(functionsListener);
        undoBtn = new JButton("Undo");
        undoBtn.addActionListener(functionsListener);


        //Figures
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


        //CheckBoxes
        solidCheckBox = new JCheckBox("Solid");
        solidCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED)
                    myApp.isSolid = true;
                else if (e.getStateChange() == ItemEvent.DESELECTED)
                    myApp.isSolid = false;
            }
        });

        dottedCheckBox = new JCheckBox("Dotted");
        dottedCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED)
                    myApp.isDotted = true;
                else if (e.getStateChange() == ItemEvent.DESELECTED)
                    myApp.isDotted = false;
            }
        });


        //Stroke Width buttons
        smallBtn = new JToggleButton("Small");
        smallBtn.addActionListener(strokeWidthListener);


        mediumBtn = new JToggleButton("Medium");
        mediumBtn.addActionListener(strokeWidthListener);
        mediumBtn.setSelected(true);

        largeBtn = new JToggleButton("Large");
        largeBtn.addActionListener(strokeWidthListener);


        ButtonGroup sizeGroup = new ButtonGroup();
        sizeGroup.add(smallBtn);
        sizeGroup.add(mediumBtn);
        sizeGroup.add(largeBtn);

        //Colors
        blackBtn = new JToggleButton("Black");
        blackBtn.setBackground(Color.BLACK);
        blackBtn.setForeground(Color.WHITE);
        blackBtn.addActionListener(colorListener);
        blackBtn.setSelected(true);

        redBtn = new JToggleButton("Red");
        redBtn.setBackground(Color.RED);
        redBtn.setForeground(Color.WHITE);
        redBtn.addActionListener(colorListener);

        greenBtn = new JToggleButton("Green");
        greenBtn.setBackground(Color.GREEN);
        greenBtn.setForeground(Color.BLACK);
        greenBtn.addActionListener(colorListener);

        blueBtn = new JToggleButton("Blue");
        blueBtn.setBackground(Color.BLUE);
        blueBtn.setForeground(Color.WHITE);
        blueBtn.addActionListener(colorListener);

        purpleBtn = new JToggleButton("Purple");
        purpleBtn.setBackground(myApp.PURPLE);
        purpleBtn.setForeground(Color.WHITE);
        purpleBtn.addActionListener(colorListener);

        yellowBtn = new JToggleButton("Yellow");
        yellowBtn.setBackground(Color.YELLOW);
        yellowBtn.setForeground(Color.BLACK);
        yellowBtn.addActionListener(colorListener);

        ButtonGroup colorsGroup = new ButtonGroup();
        colorsGroup.add(blackBtn);
        colorsGroup.add(redBtn);
        colorsGroup.add(greenBtn);
        colorsGroup.add(blueBtn);
        colorsGroup.add(purpleBtn);
        colorsGroup.add(yellowBtn);

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
                                .addGap(10, 10, 10)
                                .addComponent(functionsLabel)
                                .addGap(10, 10, 10)
                                .addComponent(clearBtn)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(undoBtn)
                                .addGap(22, 22, 22)
                                .addComponent(paintModeLabel)
                                .addGap(10, 10, 10)
                                .addComponent(lineBtn)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rectBtn)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ovalBtn)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pencilBtn)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(eraserBtn)
                                .addGap(22, 22, 22)
                                .addComponent(solidCheckBox)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(dottedCheckBox)
                                .addGap(22, 22, 22)
                                .addComponent(sizeLabel)
                                .addGap(10, 10, 10)
                                .addComponent(smallBtn)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(mediumBtn)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(largeBtn)
                                .addGap(22, 22, 22)
                                .addComponent(colorsLabel)
                                .addGap(10, 10, 10)
                                .addComponent(blackBtn)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(redBtn)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(greenBtn)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(blueBtn)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(purpleBtn)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(yellowBtn))
                        .addComponent(drawingPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(blueBtn)
                                        .addComponent(greenBtn)
                                        .addComponent(redBtn)
                                        .addComponent(blackBtn)
                                        .addComponent(purpleBtn)
                                        .addComponent(yellowBtn)
                                        .addComponent(eraserBtn)
                                        .addComponent(pencilBtn)
                                        .addComponent(ovalBtn)
                                        .addComponent(rectBtn)
                                        .addComponent(lineBtn)
                                        .addComponent(dottedCheckBox)
                                        .addComponent(solidCheckBox)
                                        .addComponent(clearBtn)
                                        .addComponent(undoBtn)
                                        .addComponent(functionsLabel)
                                        .addComponent(paintModeLabel)
                                        .addComponent(colorsLabel)
                                        .addComponent(sizeLabel)
                                        .addComponent(smallBtn)
                                        .addComponent(mediumBtn)
                                        .addComponent(largeBtn))
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
