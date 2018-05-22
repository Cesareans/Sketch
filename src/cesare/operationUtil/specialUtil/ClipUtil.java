package cesare.operationUtil.specialUtil;

import cesare.GUIComponents.SketchCanvasPane;
import cesare.operation.Operation;
import cesare.operation.special.Clip;
import cesare.operationUtil.OperationUtil;

public class ClipUtil extends OperationUtil{
    public ClipUtil(){
        operationType = OperationType.MultiTwoPointType;
    }

    @Override
    public void setStart(Operation[] curOperation, int x, int y) {
        curOperation[0] = new Clip(x,y,x,y);
    }

    @Override
    public void setProcess(Operation[] curOperation, int x, int y) {
        ((Clip)curOperation[0]).setSecondPoint(x,y);
    }

    @Override
    public void setTerminal(Operation[] curOperation, int x, int y) {
        setProcess(curOperation,x,y);
        end();
    }
}
