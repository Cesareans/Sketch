package cesare.operation.special;

import cesare.GUIComponents.SketchCanvasPane;
import cesare.operation.Operation;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Clip extends Operation {
    private int x1, y1, x2, y2;
    private boolean isClip;
    int dashedLength = 3;

    public static Clip clearClip(){
        Clip clip = new Clip(0,0,0,0);
        clip.isClip = false;
        return clip;
    }
    public Clip(int x1, int y1, int x2, int y2){
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        isClip = true;
    }
    public void setSecondPoint(int x, int y) {
        x2 = x;
        y2 = y;
        SketchCanvasPane.getInstance().setSelectRegion(x1,y1,x2,y2);
    }

    @Override
    public void operate(Graphics g) {
        g.setClip(null);
        if(isClip)
            ((Graphics2D) g).clip(new Rectangle2D.Double(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x2 - x1), Math.abs(y2 - y1)));
    }
}
