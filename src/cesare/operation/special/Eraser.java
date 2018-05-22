package cesare.operation.special;

import cesare.operation.Operation;

import java.awt.*;
import java.time.chrono.Era;
import java.util.ArrayList;

public class Eraser extends Operation {
    class EraserPoint {
        int x;
        int y;
        EraserPoint(int x ,int y){
            this.x = x;
            this.y = y;
        }
        double distance(EraserPoint p) {
            double dx = x - p.x;
            double dy = y - p.y;
            return Math.sqrt(dx * dx + dy * dy);
        }
    }
    int radius = 10;
    ArrayList<EraserPoint> points = new ArrayList<>();
    Color color = Color.white;
    public Eraser(int x , int y){
        points.add(new EraserPoint(x , y));
    }
    public void addPoint(int x , int y) {
        EraserPoint point = new EraserPoint(x, y);
        points.add(point);
        EraserPoint lastPoint = points.get(points.size() - 1);
        double distance = point.distance(lastPoint);
        for (; distance > radius; distance -= radius / 2) {
        }
    }
    @Override
    public void operate(Graphics g) {
        g.setColor(color);
        for (EraserPoint point : points) {
            g.fillOval(point.x, point.y, radius, radius);
        }
    }
}
