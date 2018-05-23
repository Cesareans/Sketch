package cesare.GUIComponents;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;

public class SketchMenuBar extends JMenuBar {
    private JMenu[] menus = {
            new JMenu("File"){
                {
                    add(new JMenuItem("New"){
                        {
                            setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
                            addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    SketchCanvasPane.getInstance().newCanvas(640,480);
                                }
                            });
                        }
                    });
                    add(new JMenuItem("Open"){
                        {
                            setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
                            addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    SketchCanvasPane.getInstance().newCanvasFromFile();
                                }
                            });
                        }
                    });
                    add(new JMenuItem("Save"){
                        {
                            setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
                            addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    SketchCanvasPane.getInstance().saveToFile();
                                }
                            });
                        }
                    });
                    add(new JMenuItem("Save As"){
                        {
                            setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK|InputEvent.SHIFT_DOWN_MASK));
                            addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    SketchCanvasPane.getInstance().saveToAnotherFile();
                                }
                            });
                        }
                    });
                    add(new JMenuItem("Close"){
                        {
                            setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_DOWN_MASK));
                            addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    SketchCanvasPane.getInstance().closeFile();
                                }
                            });
                        }
                    });
                }
            },
            new JMenu("Edit"){
                {
                    add(new JMenuItem("Revoke"){
                        {
                            addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK));
                                    SketchCanvasPane.getInstance().revokeOperation();
                                }
                            });
                        }
                    });
                    add(new JMenuItem("Retrieve"){
                        {
                            addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK));
                                    SketchCanvasPane.getInstance().retrieveOperation();
                                }
                            });
                        }
                    });
                }
            },
            new JMenu("View"){
                {
                    add(new JMenuItem("Toolbar"));
                }
            }
    };

    private static SketchMenuBar sketchMenuBar = new SketchMenuBar();
    public static SketchMenuBar getInstance(){
        return sketchMenuBar;
    }
    public SketchMenuBar(){
        for(JMenu menu : menus){
            add(menu);
        }
    }

}
