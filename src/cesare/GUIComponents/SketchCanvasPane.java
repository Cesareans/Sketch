package cesare.GUIComponents;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class SketchCanvasPane extends JDesktopPane {
    private static SketchCanvasPane ourInstance = new SketchCanvasPane();

    public static SketchCanvasPane getInstance() {
        return ourInstance;
    }

    //private HashMap<Integer , SketchCanvas> canvasMap = new HashMap<>();
    private ArrayList<SketchCanvas> canvasList = new ArrayList<>();


    public void revokeOperation() {
        ((SketchCanvas) getSelectedFrame()).revokeOperation();
    }
    public void retrieveOperation() {
        ((SketchCanvas) getSelectedFrame()).retrieveOperation();
    }

    private SketchCanvasPane() {
        setBackground(Color.white);
    }

    public void newCanvas(int width , int height){
        SketchCanvas canvas = new SketchCanvas(width,height);
        canvasList.add(canvas);
        add(canvasList.get(canvasList.size() - 1));
    }
}
