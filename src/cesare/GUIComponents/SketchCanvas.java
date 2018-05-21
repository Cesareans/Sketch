package cesare.GUIComponents;

import cesare.operation.Operation;
import cesare.operationUtil.OperationUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;


public class SketchCanvas extends JInternalFrame {
    private class Canvas extends JPanel {
        private OperationUtil currentOperationUtilRef;
        private Operation currentOperation[] = new Operation[1];
        private ArrayList<Operation> operations = new ArrayList<>();
        private ArrayList<Operation> revokedOperations = new ArrayList<>();

        private int canvasWidth;
        private int canvasHeight;

        private class mouseListener extends MouseAdapter {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                currentOperationUtilRef = SketchUtilBar.getSketchUtilBar().getOperationUtilRef();
                if (currentOperationUtilRef != null) {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        if (currentOperationUtilRef.onEnd())
                            currentOperationUtilRef.reStart();
                        currentOperationUtilRef.setStart(currentOperation, e.getX(), e.getY());
                    } else if (e.getButton() == MouseEvent.BUTTON3) {
                        currentOperationUtilRef.end();
                        confirmOperation();
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                if(e.getButton() == MouseEvent.BUTTON3)
                    return;
                if (currentOperationUtilRef != null) {
                    switch (currentOperationUtilRef.getOperationType()) {
                        case MultiTwoPointType:
                            currentOperationUtilRef.setTerminal(currentOperation, e.getX(), e.getY());
                            if (currentOperationUtilRef.onEnd())//不管左键右键
                                confirmOperation();
                            break;
                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                for (Component component : getParent().getComponents()) {
                    if (component instanceof SketchInfoBar) {
                        ((SketchInfoBar) component).setMouseState(true);
                    }
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                for (Component component : getParent().getComponents()) {
                    if (component instanceof SketchInfoBar) {
                        ((SketchInfoBar) component).setMouseState(false);
                    }
                }
            }

        }
        private class motionListener extends MouseMotionAdapter {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                if (currentOperationUtilRef != null && currentOperation[0] != null) {
                    if (!currentOperationUtilRef.onEnd())
                        currentOperationUtilRef.setProcess(currentOperation, e.getX(), e.getY());
                }
                for (Component component : getParent().getComponents()) {
                    if (component instanceof SketchInfoBar) {
                        ((SketchInfoBar) component).setMouseInfo(e.getX(), e.getY());
                    }
                }
                updateUI();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                if (currentOperationUtilRef != null && currentOperation[0] != null) {
                    if (!currentOperationUtilRef.onEnd())
                        currentOperationUtilRef.setProcess(currentOperation, e.getX(), e.getY());
                }
                for (Component component : getParent().getComponents()) {
                    if (component instanceof SketchInfoBar) {
                        ((SketchInfoBar) component).setMouseInfo(e.getX(), e.getY());
                    }
                }
                updateUI();
            }

        }
        private void confirmOperation() {
            if (currentOperation[0] != null) {
                operations.add(currentOperation[0]);
                revokedOperations.clear();
                currentOperation[0] = null;
            }
            updateUI();
        }

        void setCanvasSize(int width, int height) {
            canvasWidth = width;
            canvasHeight = height;
            setMinimumSize(new Dimension(width, height));
            setMaximumSize(new Dimension(width,height));
        }

        Canvas(int width, int height) {
            setCanvasSize(width, height);
            addMouseListener(new mouseListener());
            addMouseMotionListener(new motionListener());
        }

        void revokeOperation() {
            if (operations.size() > 0) {
                revokedOperations.add(operations.remove(operations.size() - 1));
                updateUI();
            }
        }
        void retrieveOperation() {
            if (revokedOperations.size() > 0) {
                operations.add((revokedOperations.remove(revokedOperations.size() - 1)));
                updateUI();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.white);
            g.fillRect(0, 0, canvasWidth, canvasHeight);

            Graphics2D g2d = ((Graphics2D) g);
            Stroke preStroke = g2d.getStroke();

            for (Operation operation : operations)
                operation.operate(g);

            if (currentOperation[0] != null)
                currentOperation[0].operate(g);

            g2d.setStroke(preStroke);
        }
    }

    private File file;
    private int windowHeight;
    private int windowWidth;
    Canvas canvas;
    private SpringLayout layout = new SpringLayout();
    public void revokeOperation() {
        canvas.revokeOperation();
    }
    public void retrieveOperation() {
        canvas.retrieveOperation();
    }
    public void importFile(File file){
        this.file = file;
    }

    SketchCanvas(int width , int height){
        super("Canvas", true, true, true, true);
        windowWidth = width;
        windowHeight = height;
        setLayout(layout);
        canvas = new Canvas(width, height);
        add(canvas);
        setSize(width,height);
        resized();
        setVisible(true);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                resized();
                //canvas.setCanvasSize(getContentPane().getWidth(),getContentPane().getHeight());
            }
        });
    }

    private void resized(){
        SpringLayout.Constraints constraints;

        //sketchUtilBar
        constraints = layout.getConstraints(canvas);
        constraints.setX(Spring.constant(0));
        constraints.setY(Spring.constant(0));
        constraints.setWidth(Spring.constant(windowWidth));
        constraints.setHeight(Spring.constant(windowHeight));

    }
}