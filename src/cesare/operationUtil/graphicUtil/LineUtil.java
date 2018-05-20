package cesare.operationUtil.graphicUtil;

import cesare.operation.Operation;
import cesare.operation.graphic.Line;

import java.awt.*;

public class LineUtil extends SketchShapeUtil {
    public LineUtil(){
        operationType = OperationType.MultiTwoPointType;
    }
    @Override
    public void setStart(Operation[] curOperation, int x, int y) {
        curOperation[0]= new Line(x,y,x,y);
        ((Line)curOperation[0]).setColor(color).setGradient(isGradient).setSecondColor(secondColor).setLineWidth(lineWidth).setDashedLength(dashedLength);
    }

    @Override
    public void setProcess(Operation[] curOperation, int x, int y) {
        ((Line)curOperation[0]).setSecondPoint(x,y);
    }

    @Override
    public void setTerminal(Operation[] curOperation, int x, int y) {
        setProcess(curOperation,x,y);
        end();
    }
}
