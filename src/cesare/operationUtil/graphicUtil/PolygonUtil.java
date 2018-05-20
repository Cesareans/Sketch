package cesare.operationUtil.graphicUtil;

import cesare.operation.Operation;
import cesare.operation.graphic.Polygon;

import java.awt.*;

public class PolygonUtil extends SketchShapeUtil {
    public PolygonUtil(){
        operationType = OperationType.MultiOnePointType;
    }
    @Override
    public void setStart(Operation[] curOperation, int x, int y) {
        if(curOperation[0] == null){
            curOperation[0] = new Polygon(x,y);
            ((Polygon)curOperation[0]).setColor(color).setGradient(isGradient).setSecondColor(secondColor).setLineWidth(lineWidth).setDashedLength(dashedLength).setFilled(isFilled);
            ((Polygon)curOperation[0]).addPoint(x,y);
        }else
            ((Polygon)curOperation[0]).addPoint(x,y);
    }

    @Override
    public void setProcess(Operation[] curOperation, int x, int y) {
        if(curOperation[0] != null)
            ((Polygon)curOperation[0]).setLastPoints(x,y);
    }

    @Override
    public void setTerminal(Operation[] curOperation, int x, int y) {
        setProcess(curOperation , x , y);
    }
}
