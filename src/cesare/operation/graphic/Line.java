package cesare.operation.graphic;

import java.awt.*;
import java.awt.geom.Line2D;

public class Line extends SketchShape {
    private int x1,y1,x2,y2;

    public Line(int x1,int y1,int x2,int y2){
        this.x1=x1;
        this.y1=y1;
        this.x2=x2;
        this.y2=y2;
    }
    public void setSecondPoint(int x , int y){
        x2 = x ; y2 = y;
    }
    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = ((Graphics2D) g);
        if(isGradient){
            GradientPaint gp = new GradientPaint(x1,y1,color,x2,y2,secondColor);
            g2d.setPaint(gp);
        }
        g2d.draw(new Line2D.Double(x1,y1,x2,y2));
    }
}
