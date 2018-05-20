package cesare.GUIComponents;

import cesare.operationUtil.OperationUtil;
import cesare.operationUtil.graphicUtil.*;
import cesare.operationUtil.specialUtil.ClearAreaUtil;
import cesare.operationUtil.specialUtil.TextUtil;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SketchUtilBar extends JToolBar {
    private OperationUtil currentOperationUtil;
    private int buttonSide = 32;
    private ColorButton onUsingColorButton;
    private enum OperationActionType{REDO,UNDO}

    private class OperationAction extends AbstractAction{
        OperationActionType type;
        public OperationAction(OperationActionType type , Icon icon){
            super(type.name(),icon);
            this.type = type;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            switch (type) {
                case REDO:
                    for (Component component : getParent().getComponents()) {
                        if (component instanceof SketchCanvas) {
                            ((SketchCanvas) component).retrieveOperation();
                        }
                    }
                    break;
                case UNDO:
                    for (Component component : getParent().getComponents()) {
                        if (component instanceof SketchCanvas) {
                            ((SketchCanvas) component).revokeOperation();
                        }
                    }
                    break;
            }
        }
    }
    private class UtilButton extends JButton{
        private OperationUtil operationUtil;
        UtilButton(OperationUtil operationUtil , Icon icon){
            super(icon);
            this.operationUtil = operationUtil;
            addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(operationUtil instanceof SketchShapeUtil){
                        ((SketchShapeUtil) operationUtil).setColor(onUsingColorButton.getColor());
                    }
                    currentOperationUtil = operationUtil;
                }
            });

        }
    }
    private class ColorButton extends JButton{
        private Color buttonColor;
        private Icon icon = new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                g.setColor(buttonColor);
                g.fillRect(0,0,getWidth(),getHeight());
            }

            @Override
            public int getIconWidth() {
                return 0;
            }

            @Override
            public int getIconHeight() {
                return 0;
            }
        };
        ColorButton() {
            setMaximumSize(new Dimension(buttonSide, buttonSide));
            setMinimumSize(new Dimension(buttonSide, buttonSide));
            setIcon(icon);
            buttonColor = Color.black;
            onUsingColorButton = this;
            addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    chooseThis();
                }
            });
        }
        private void chooseThis(){
            if(onUsingColorButton == this) {
                Color color = JColorChooser.showDialog(SketchUtilBar.this, "Select a Color", buttonColor);
                if(color != null)
                    buttonColor = color;
            }
            else {
                JButton preButton = onUsingColorButton;
                onUsingColorButton = this;
                setBorder(new EtchedBorder());
                preButton.setBorder(null);
            }
            if(currentOperationUtil instanceof SketchShapeUtil){
                ((SketchShapeUtil) currentOperationUtil).setColor(onUsingColorButton.getColor());
            }
        }
        private Color getColor(){
            return buttonColor;
        }
    }

    public OperationUtil getOperationUtilRef() {
        SketchUtilAttributePanel attrRef = null;
        for(Component c : getParent().getComponents()){
            if(c instanceof SketchUtilAttributePanel){
                attrRef = ((SketchUtilAttributePanel) c);
            }
        }
        if(attrRef!=null){
            if(currentOperationUtil instanceof SketchShapeUtil){
                ((SketchShapeUtil) currentOperationUtil).setFilled(attrRef.isFilled);
                ((SketchShapeUtil) currentOperationUtil).setGradient(attrRef.isGradient);
                ((SketchShapeUtil) currentOperationUtil).setSecondColor(attrRef.secondColor);
                ((SketchShapeUtil) currentOperationUtil).setLineWidth(attrRef.lineWidth);
                ((SketchShapeUtil) currentOperationUtil).setDashedLength(attrRef.isDashed?attrRef.dashedLength:0);
            }
        }
        return currentOperationUtil;
    }

    private static SketchUtilBar sketchUtilBar = new SketchUtilBar();
    public static SketchUtilBar getSketchUtilBar() {
        return sketchUtilBar;
    }

    private SketchUtilBar() {
        setFloatable(false);
        add(new OperationAction(OperationActionType.UNDO, new ImageIcon("res/icon/revoke.png")));
        add(new OperationAction(OperationActionType.REDO, new ImageIcon("res/icon/retrieve.png")));
        addSeparator();
        add(new UtilButton(null, new ImageIcon("res/icon/cursor.png")));
        add(new UtilButton(new LineUtil(), new ImageIcon("res/icon/line.png")));
        add(new UtilButton(new RectUtil(), new ImageIcon("res/icon/rect.png")));
        add(new UtilButton(new OvalUtil(), new ImageIcon("res/icon/oval.png")));
        add(new UtilButton(new PolygonUtil(), new ImageIcon("res/icon/polygon.png")));
        add(new UtilButton(new ClearAreaUtil(new Color(255, 255, 255)), new ImageIcon("res/icon/rectRubber.png")));
        add(new UtilButton(new TextUtil(),new ImageIcon("res/icon/word.png")));
        addSeparator();
        add(new ColorButton());
        add(Box.createHorizontalStrut(4));
        add(new ColorButton());
    }
}
