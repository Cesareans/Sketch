package cesare;

import cesare.GUIComponents.SketchMainFrame;

import javax.swing.*;

public class Sketch {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        SketchMainFrame mainFrame = new SketchMainFrame();
    }
}
