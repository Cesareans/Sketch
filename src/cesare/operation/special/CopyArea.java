package cesare.operation.special;

import cesare.GUIComponents.SketchCanvasPane;
import cesare.operation.Operation;

import java.awt.*;
import java.awt.image.BufferedImage;

public class CopyArea extends Operation{
    int x1,y1,x2,y2;
    int dx0,dy0;
    int dx,dy;
    boolean retain;


    public CopyArea(int x1, int y1, int x2, int y2 , boolean retain) {
        setFirstPoint(x1, y1);
        setSecondPoint(x2, y2);
        dx0 = dy0 = dx = dy = 0;
        this.retain = retain;
    }
    public void setFirstPoint(int x , int y){
        x1 = x;
        y1 = y;
    }
    public void setSecondPoint(int x, int y) {
        x2 = x;
        y2 = y;
        SketchCanvasPane.getInstance().setSelectRegion(x1,y1,x2,y2,false);
    }
    public void setDistance(int dx , int dy){
        this.dx=dx;
        this.dy=dy;
        SketchCanvasPane.getInstance().setSelectRegion(x1 + dx + dx0, y1 + dy + dy0, x2 + dx + dx0, y2 +dy+dy0,false);
    }
    public void confirmMove() {
        dx0+=dx;
        dy0+=dy;
        dx=0;dy=0;
    }
    public boolean isInArea(int x , int y){
        return x>Math.min(x1+dx+dx0,x2+dx+dx0)&&x<Math.max(x1+dx+dx0,x2+dx+dx0)&&y>Math.min(y1+dy+dy0,y2+dy+dy0)&&y<Math.max(y1+dy+dy0,y2+dy+dy0);
    }


    @Override
    public void operate(Graphics g) {
        if(Math.abs(x2 - x1)<=0 || Math.abs(y2 - y1)<= 0)
            return;

        BufferedImage image = SketchCanvasPane.getInstance().getCanvasImageInOperations().getSubimage(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x2 - x1), Math.abs(y2 - y1));
        if (!retain) {
            g.setColor(Color.white);
            g.fillRect(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x2 - x1), Math.abs(y2 - y1));
            //g.clearRect(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x2 - x1), Math.abs(y2 - y1));
        }
        g.drawImage(image, Math.min(x1, x2) + dx + dx0, Math.min(y1, y2) + dy + dy0, null);
    }
}
