package cesare.GUIComponents;

import javax.swing.*;

public class SketchMenuBar extends JMenuBar {
    private JMenu[] menus = {new JMenu("File")};

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
