package cesare.operationUtil.graphicUtil;

import cesare.operation.Operation;
import cesare.operation.graphic.Oval;

import java.awt.*;

public class OvalUtil extends SketchShapeUtil {
    public OvalUtil(){
        operationType = OperationType.MultiTwoPointType;
    }

    @Override
    public void setStart(Operation[] curOperation, int x, int y) {
        curOperation[0] = new Oval(x,y,x,y);
        ((Oval)curOperation[0]).setColor(color).setGradient(isGradient).setSecondColor(secondColor).setLineWidth(lineWidth).setDashedLength(dashedLength).setFilled(isFilled);
    }

    @Override
    public void setProcess(Operation[] curOperation, int x, int y) {
        ((Oval) curOperation[0]).setSecondPoint(x,y);
    }

    @Override
    public void setTerminal(Operation[] curOperation, int x, int y) {
        setProcess(curOperation,x,y);
        end();
    }
}
