package cesare.operation.graphic;

import java.awt.*;
import java.util.ArrayList;

public class Polygon extends SketchShape {
    int pointsNum = 0;
    ArrayList<Integer> xPoints = new ArrayList<>();
    ArrayList<Integer> yPoints = new ArrayList<>();
    public Polygon(int x , int y){
        addPoint(x,y);
    }
    public void addPoint(int x, int y){
        ++pointsNum;
        xPoints.add(x);
        yPoints.add(y);
    }
    public void setLastPoints(int x , int y){
        xPoints.set(pointsNum - 1 , x);
        yPoints.set(pointsNum - 1 , y);
    }
    @Override
    public void draw(Graphics g) {
        int [] xPts = new int[pointsNum];
        int [] yPts = new int[pointsNum];
        for(int i = 0 ; i < pointsNum ; ++i){
            xPts[i] = xPoints.get(i);
            yPts[i] = yPoints.get(i);
        }

        if(isFilled)
            g.fillPolygon(xPts, yPts,pointsNum);
        else
            g.drawPolygon(xPts, yPts,pointsNum);
    }
}
