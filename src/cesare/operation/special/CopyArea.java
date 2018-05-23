package cesare.operation.special;

import cesare.GUIComponents.SketchCanvasPane;
import cesare.operation.Operation;

import java.awt.*;
import java.awt.image.BufferedImage;

public class CopyArea extends Operation{
    int x1,y1,x2,y2;
    int sdx, sdy;
    int dx,dy;
    boolean retain;
    BufferedImage image;


    public CopyArea(int x1, int y1, int x2, int y2 , boolean retain) {
        setFirstPoint(x1, y1);
        setSecondPoint(x2, y2);
        sdx = sdy = dx = dy = 0;
        this.retain = retain;
    }
    public void setFirstPoint(int x , int y){
        if(x < 0)
            x = 0;
        else if(x > SketchCanvasPane.getInstance().getCanvasWidth())
            x = SketchCanvasPane.getInstance().getCanvasWidth();
        if(y< 0)
            y=0;
        else if(y > SketchCanvasPane.getInstance().getCanvasHeight())
            y=SketchCanvasPane.getInstance().getCanvasHeight();
        x1 = x;
        y1 = y;
    }
    public void setSecondPoint(int x, int y) {
        if(x < 0)
            x = 0;
        else if(x > SketchCanvasPane.getInstance().getCanvasWidth())
            x = SketchCanvasPane.getInstance().getCanvasWidth();
        if(y< 0)
            y=0;
        else if(y > SketchCanvasPane.getInstance().getCanvasHeight())
            y=SketchCanvasPane.getInstance().getCanvasHeight();
        x2 = x;
        y2 = y;
        SketchCanvasPane.getInstance().setSelectRegion(x1,y1,x2,y2,false);
    }
    public void setDistance(int dx , int dy){
        this.dx=dx;
        this.dy=dy;
        SketchCanvasPane.getInstance().setSelectRegion(x1 + dx + sdx, y1 + dy + sdy, x2 + dx + sdx, y2 +dy+ sdy,false);
    }
    public void confirmMove() {
        sdx +=dx;
        sdy +=dy;
        setDistance(0,0);
    }
    public boolean isInArea(int x , int y){
        return x>Math.min(x1+dx+ sdx,x2+dx+ sdx)&&x<Math.max(x1+dx+ sdx,x2+dx+ sdx)&&y>Math.min(y1+dy+ sdy,y2+dy+ sdy)&&y<Math.max(y1+dy+ sdy,y2+dy+ sdy);
    }


    @Override
    public void operate(Graphics g) {
        if(!(Math.abs(x2 - x1) > 0 && Math.abs(y2 - y1) > 0))
            return;
        image = SketchCanvasPane.getInstance().getCanvasImageInOperations().getSubimage(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x2 - x1), Math.abs(y2 - y1));


        if (!retain) {
            g.setColor(Color.white);
            g.fillRect(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x2 - x1), Math.abs(y2 - y1));
        }
        g.drawImage(image, Math.min(x1, x2) + dx + sdx, Math.min(y1, y2) + dy + sdy, null);
    }
}
