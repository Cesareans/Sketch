package cesare.operationUtil.graphicUtil;

import cesare.operation.Operation;
import cesare.operation.graphic.Rect;

import java.awt.*;

public class RectUtil extends SketchShapeUtil {
    public RectUtil(){
        operationType = OperationType.MultiTwoPointType;
    }

    @Override
    public void setStart(Operation[] curOperation, int x, int y) {
        curOperation[0] = new Rect(x,y,x,y);
        ((Rect)curOperation[0]).setColor(color).setGradient(isGradient).setSecondColor(secondColor).setLineWidth(lineWidth).setDashedLength(dashedLength).setFilled(isFilled);
    }

    @Override
    public void setProcess(Operation[] curOperation, int x, int y) {
        ((Rect)curOperation[0]).setSecondPoint(x,y);
    }

    @Override
    public void setTerminal(Operation[] curOperation, int x, int y) {
        setProcess(curOperation,x,y);
        end();
    }
}
