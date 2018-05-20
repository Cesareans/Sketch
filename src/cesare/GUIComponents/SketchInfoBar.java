package cesare.GUIComponents;

import javax.swing.*;

public class SketchInfoBar extends JToolBar {
    private static SketchInfoBar sketchInfoBar = new SketchInfoBar();
    public static SketchInfoBar getSketchInfoBar(){
        return sketchInfoBar;
    }
    public SketchInfoBar(){
        add(mouseInfoLabel);
    }

    private JLabel mouseInfoLabel = new JLabel();
    private boolean isMouseInCanvas = false;
    public void setMouseState(boolean isMouseInCanvas){
        this.isMouseInCanvas = isMouseInCanvas;
    }
    public void setMouseInfo(int x , int y){
        if(isMouseInCanvas)
            mouseInfoLabel.setText("(" + x + "," + y + ")");
        else
            mouseInfoLabel.setText("Not in canvas.");
    }
}
