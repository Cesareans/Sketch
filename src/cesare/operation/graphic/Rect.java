package cesare.operation.graphic;


import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Rect extends SketchShape {
    private int x1, y1, x2, y2;
    public Rect(int x1, int y1, int x2, int y2){
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public void setFirstPoint(int x, int y) {
        x1 = x;
        y1 = y;
    }
    public void setSecondPoint(int x, int y) {
        x2 = x;
        y2 = y;
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = ((Graphics2D) g);
        if (isGradient) {
            GradientPaint gp = new GradientPaint(x1, y1, color, x2, y2, secondColor);
            g2d.setPaint(gp);
        }
        //这里可以将值存好，来用空间换时间
        Rectangle2D rectangle = new Rectangle2D.Double(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x2 - x1), Math.abs(y2 - y1));
        if (isFilled)
            g2d.fill(rectangle);
        else
            g2d.draw(rectangle);
    }
}
