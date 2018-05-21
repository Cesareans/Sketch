package cesare.GUIComponents;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class SketchMenuBar extends JMenuBar {
    JFileChooser fileChooser = new JFileChooser();
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
                                    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                                    int value = fileChooser.showOpenDialog(SketchMainFrame.getInstance());
                                    if(value == JFileChooser.APPROVE_OPTION){
                                        String selectPath = fileChooser.getSelectedFile().getPath();
                                        System.out.println ( "你选择的目录是：" + selectPath );
                                        fileChooser.setVisible(false);
                                    }
                                }
                            });
                        }
                    });
                    add(new JMenuItem("Save"){
                        {
                            addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                                    int value = fileChooser.showSaveDialog(SketchMainFrame.getInstance());
                                    if(value == JFileChooser.APPROVE_OPTION){
                                        String selectPath = fileChooser.getSelectedFile().getPath();
                                        System.out.println ( "你选择的目录是：" + selectPath );
                                        fileChooser.hide();
                                    }
                                }
                            });
                        }
                    });
                    add(new JMenuItem("Close"));
                }
            },
            new JMenu("Edit"){
                {
                    add(new JMenuItem("Revoke"));
                    add(new JMenuItem("Retrieve"));
                }
            },
            new JMenu("View"){
                {
                    add(new JMenuItem("Toolbar"));
                }
            }
    };

    private static SketchMenuBar sketchMenuBar = new SketchMenuBar();
    public static SketchMenuBar getSketchMenuBar(){
        return sketchMenuBar;
    }
    public SketchMenuBar(){
        for(JMenu menu : menus){
            add(menu);
        }
    }

}
