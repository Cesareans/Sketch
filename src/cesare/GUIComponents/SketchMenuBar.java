package cesare.GUIComponents;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class SketchMenuBar extends JMenuBar {
    private JMenu[] menus = {
            new JMenu("File"){
                {
                    add(new JMenuItem("New"){
                        {
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
                            addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    SketchCanvasPane.getInstance().saveToAnotherFile();
                                }
                            });
                        }
                    });
                    add(new JMenuItem("Close"));
                }
            },
            new JMenu("Edit"){
                {
                    add(new JMenuItem("Revoke"){
                        {
                            addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
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
