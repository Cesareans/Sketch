package cesare.GUIComponents;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class SketchMenuBar extends JMenuBar {
    int auxKeyMask = System.getProperties().getProperty("os.name").equals("Mac OS X") ? InputEvent.META_DOWN_MASK : InputEvent.CTRL_DOWN_MASK;
    //File menu items
    JMenuItem newItem = new JMenuItem("New") {
        {
            setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, auxKeyMask));
            addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    SketchCanvasPane.getInstance().newCanvas(640, 480);
                }
            });
        }
    };
    JMenuItem openItem = new JMenuItem("Open") {
        {
            setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, auxKeyMask));
            addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    SketchCanvasPane.getInstance().newCanvasFromFile();
                }
            });
        }
    };
    JMenuItem saveItem = new JMenuItem("Save") {
        {
            setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, auxKeyMask));
            addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    SketchCanvasPane.getInstance().saveToFile();
                }
            });
        }
    };
    JMenuItem saveAsItem = new JMenuItem("Save As") {
        {
            setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, auxKeyMask | InputEvent.SHIFT_DOWN_MASK));
            addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    SketchCanvasPane.getInstance().saveToAnotherFile();
                }
            });
        }
    };
    JMenuItem closeItem = new JMenuItem("Close") {
        {
            setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, auxKeyMask));
            addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    SketchCanvasPane.getInstance().closeFile();
                }
            });
        }
    };
    //Edit menu items
    JMenuItem undoItem = new JMenuItem("Undo") {
        {
            setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, auxKeyMask));
            addActionListener(e -> {
                SketchCanvasPane.getInstance().undoOperation();
            });
        }
    };
    JMenuItem redoItem = new JMenuItem("Redo") {
        {
            setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, auxKeyMask));
            addActionListener(e -> {
                SketchCanvasPane.getInstance().redoOperation();
            });
        }
    };

    private JMenu[] menus = {
            new JMenu("File") {
                {
                    add(newItem);
                    add(openItem);
                    add(saveItem);
                    add(saveAsItem);
                    add(closeItem);
                }
            },
            new JMenu("Edit") {
                {
                    add(undoItem);
                    add(redoItem);
                }
            },
            new JMenu("View") {
                {
                    add(new JMenuItem("Toolbar"));
                }
            }
    };

    public void updateMenuItemState() {
        undoItem.setEnabled(SketchCanvasPane.getInstance().canUndo());
        redoItem.setEnabled(SketchCanvasPane.getInstance().canRedo());
    }

    private static SketchMenuBar sketchMenuBar = new SketchMenuBar();

    public static SketchMenuBar getInstance() {
        return sketchMenuBar;
    }

    private SketchMenuBar() {
        for (JMenu menu : menus) {
            add(menu);
        }
        updateMenuItemState();
    }
}
