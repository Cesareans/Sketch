package cesare.operationUtil.specialUtil;

import cesare.operation.Operation;
import cesare.operation.special.Eraser;
import cesare.operationUtil.OperationUtil;

public class EraserUtil extends OperationUtil {
    public EraserUtil(){operationType = OperationType.MultiTwoPointType;}

    @Override
    public void setStart(Operation[] curOperation, int x, int y) {
        if(curOperation[0] == null)
            curOperation[0] = new Eraser(x , y);
    }

    @Override
    public void setProcess(Operation[] curOperation, int x, int y) {
        if(curOperation[0] != null)
            ((Eraser) curOperation[0]).addPoint(x,y);
    }

    @Override
    public void setTerminal(Operation[] curOperation, int x, int y) {
        setProcess(curOperation,x,y);
        end();
    }
}
