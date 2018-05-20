package cesare.GUIComponents;

import cesare.operation.Operation;
import cesare.operation.graphic.Rect;
import cesare.operationUtil.OperationUtil;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;

public class SketchCanvas extends JPanel {
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
            if(currentOperationUtilRef != null) {
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
            if(currentOperationUtilRef != null) {
                switch (currentOperationUtilRef.getOperationType()) {
                    case MultiTwoPointType:
                        currentOperationUtilRef.setTerminal(currentOperation,e.getX(),e.getY());
                        if (currentOperationUtilRef.onEnd())//不管左键右键
                            confirmOperation();
                        break;
                }
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            super.mouseEntered(e);
            for(Component component:getParent().getComponents()){
                if(component instanceof SketchInfoBar){
                    ((SketchInfoBar) component).setMouseState(true);
                }
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            super.mouseExited(e);
            for(Component component:getParent().getComponents()){
                if(component instanceof SketchInfoBar){
                    ((SketchInfoBar) component).setMouseState(false);
                }
            }
        }

    }
    private class motionListener extends MouseMotionAdapter{
        @Override
        public void mouseDragged(MouseEvent e) {
            super.mouseDragged(e);
            if(currentOperationUtilRef != null && currentOperation[0] !=null){
                if(!currentOperationUtilRef.onEnd())
                    currentOperationUtilRef.setProcess(currentOperation, e.getX() , e.getY());
            }
            for(Component component:getParent().getComponents()){
                if(component instanceof SketchInfoBar){
                    ((SketchInfoBar) component).setMouseInfo(e.getX(),e.getY());
                }
            }
            updateUI();
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            super.mouseMoved(e);
            if(currentOperationUtilRef != null && currentOperation[0] !=null){
                if(!currentOperationUtilRef.onEnd())
                    currentOperationUtilRef.setProcess(currentOperation,e.getX(),e.getY());
            }
            for(Component component:getParent().getComponents()){
                if(component instanceof SketchInfoBar){
                    ((SketchInfoBar) component).setMouseInfo(e.getX(),e.getY());
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


    private static SketchCanvas sketchCanvas = new SketchCanvas();
    public static SketchCanvas getSketchCanvas(){
        return sketchCanvas;
    }
    public SketchCanvas setCanvasSize(int width , int height){
        canvasWidth = width;
        canvasHeight = height;
        setSize(width, height);
        return this;
    }
    private SketchCanvas() {
        setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        addMouseListener(new mouseListener());
        addMouseMotionListener(new motionListener());
    }
    public void revokeOperation(){
        if(operations.size() > 0) {
            revokedOperations.add(operations.remove(operations.size() - 1));
            updateUI();
        }
    }
    public void retrieveOperation(){
        if(revokedOperations.size() > 0) {
            operations.add((revokedOperations.remove(revokedOperations.size() - 1)));
            updateUI();
        }
    }


    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(Color.white);
        g.fillRect(0,0,canvasWidth,canvasHeight);

        Graphics2D g2d = ((Graphics2D) g);
        Stroke preStroke = g2d.getStroke();

        for(Operation operation : operations)
            operation.operate(g);

        if(currentOperation[0] != null)
            currentOperation[0].operate(g);

        g2d.setStroke(preStroke);
    }
}