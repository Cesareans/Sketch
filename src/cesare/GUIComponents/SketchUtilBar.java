package cesare.GUIComponents;

import cesare.operationUtil.OperationUtil;
import cesare.operationUtil.graphicUtil.*;
import cesare.operationUtil.specialUtil.*;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SketchUtilBar extends JToolBar {
    private interface NonParaNonRet {
        public void operation();
    }

    private class OperationButton extends JButton {
        OperationButton(NonParaNonRet operation, Icon icon) {
            super(icon);
            setMaximumSize(new Dimension(buttonSide, buttonSide));
            setMinimumSize(new Dimension(buttonSide, buttonSide));
            addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    operation.operation();
                }
            });
        }
    }

    private class UtilButton extends JButton {
        private OperationUtil operationUtil;

        UtilButton(OperationUtil operationUtil, Icon icon) {
            super(icon);
            setMaximumSize(new Dimension(buttonSide, buttonSide));
            setMinimumSize(new Dimension(buttonSide, buttonSide));
            this.operationUtil = operationUtil;
            addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (operationUtil instanceof SketchShapeUtil) {
                        ((SketchShapeUtil) operationUtil).setColor(onUsingColorButton.getColor());
                    }
                    currentOperationUtil = operationUtil;
                }
            });
        }
    }

    private class ColorButton extends JButton {
        private Color buttonColor;
        private Icon icon = new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                g.setColor(buttonColor);
                g.fillRect(0, 0, getWidth(), getHeight());
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

        private void chooseThis() {
            if (onUsingColorButton == this) {
                Color color = JColorChooser.showDialog(SketchUtilBar.this, "Chooser a Color", buttonColor);
                if (color != null)
                    buttonColor = color;
            } else {
                JButton preButton = onUsingColorButton;
                onUsingColorButton = this;
                setBorder(new EtchedBorder());
                preButton.setBorder(null);
            }
            if (currentOperationUtil instanceof SketchShapeUtil) {
                ((SketchShapeUtil) currentOperationUtil).setColor(onUsingColorButton.getColor());
            }
        }

        private Color getColor() {
            return buttonColor;
        }

        private void setColor(Color color) {
            buttonColor = color;
            updateUI();
        }
    }

    private OperationUtil currentOperationUtil;
    private int buttonSide = 36;
    private ColorButton onUsingColorButton;

    public void updateButtonState() {
        undoButton.setEnabled(SketchCanvasPane.getInstance().canUndo());
        redoButton.setEnabled(SketchCanvasPane.getInstance().canRedo());
    }

    public void setOnUsingColor(Color color) {
        onUsingColorButton.setColor(color);
    }

    public OperationUtil getOperationUtilRef() {
        SketchUtilAttributePanel attrRef = SketchUtilAttributePanel.getInstance();
        if (attrRef != null) {
            if (currentOperationUtil instanceof SketchShapeUtil) {
                ((SketchShapeUtil) currentOperationUtil).setFilled(attrRef.isFilled);
                ((SketchShapeUtil) currentOperationUtil).setGradient(attrRef.isGradient);
                ((SketchShapeUtil) currentOperationUtil).setSecondColor(attrRef.secondColor);
                ((SketchShapeUtil) currentOperationUtil).setLineWidth(attrRef.lineWidth);
                ((SketchShapeUtil) currentOperationUtil).setDashedLength(attrRef.isDashed ? attrRef.dashedLength : 0);
            }
        }
        return currentOperationUtil;
    }

    OperationButton undoButton = new OperationButton(() -> SketchCanvasPane.getInstance().undoOperation(), new ImageIcon("res/icon/revoke.png"));
    OperationButton redoButton = new OperationButton(() -> SketchCanvasPane.getInstance().redoOperation(), new ImageIcon("res/icon/retrieve.png"));
    ColorButton firstColorButton = new ColorButton();
    ColorButton secondColorButton = new ColorButton();

    private static SketchUtilBar sketchUtilBar = new SketchUtilBar();

    public static SketchUtilBar getInstance() {
        return sketchUtilBar;
    }

    private SketchUtilBar() {
        setFloatable(false);
        add(new OperationButton(() -> SketchCanvasPane.getInstance().newCanvas(640, 480), new ImageIcon("res/icon/newFile.png")));
        add(new OperationButton(() -> SketchCanvasPane.getInstance().newCanvasFromFile(), new ImageIcon("res/icon/openFile.png")));
        add(new OperationButton(() -> SketchCanvasPane.getInstance().saveToFile(), new ImageIcon("res/icon/saveFile.png")));
        add(new OperationButton(() -> SketchCanvasPane.getInstance().saveToAnotherFile(), new ImageIcon("res/icon/saveToAnotherFile.png")));
        add(new OperationButton(() -> SketchCanvasPane.getInstance().closeFile(), new ImageIcon("res/icon/quit.png")));
        addSeparator();
        add(undoButton);
        add(redoButton);
        addSeparator();
        add(new UtilButton(null, new ImageIcon("res/icon/cursor.png")));
        add(new UtilButton(new AreaUtil(false), new ImageIcon("res/icon/cut.png")));
        add(new UtilButton(new AreaUtil(true), new ImageIcon("res/icon/copy.png")));
        add(new UtilButton(new ClipUtil(), new ImageIcon("res/icon/clip.png")));
        addSeparator();
        add(new UtilButton(new LineUtil(), new ImageIcon("res/icon/line.png")));
        add(new UtilButton(new RectUtil(), new ImageIcon("res/icon/rect.png")));
        add(new UtilButton(new OvalUtil(), new ImageIcon("res/icon/oval.png")));
        add(new UtilButton(new PolygonUtil(), new ImageIcon("res/icon/polygon.png")));
        add(new UtilButton(new EraserUtil(), new ImageIcon("res/icon/rubber.png")));
        add(new UtilButton(new TextUtil(), new ImageIcon("res/icon/word.png")));
        addSeparator();
        onUsingColorButton = firstColorButton;
        firstColorButton.setBorder(new EtchedBorder());
        add(firstColorButton);
        add(Box.createHorizontalStrut(4));
        add(secondColorButton);
        add(Box.createHorizontalStrut(4));
        add(new UtilButton(new DropperUtil(), new ImageIcon("res/icon/dropper.png")));
        updateButtonState();
    }
}
