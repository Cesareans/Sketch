package cesare.operationUtil.specialUtil;

import cesare.operation.Operation;
import cesare.operationUtil.OperationUtil;
import cesare.operationUtil.graphicUtil.RectUtil;

import java.awt.*;

public class ClearAreaUtil extends OperationUtil {
    private RectUtil clearAreaUtil = ((RectUtil) new RectUtil().setFilled(true));
    public ClearAreaUtil(Color backGroundColor){
        operationType = OperationType.MultiTwoPointType;
        clearAreaUtil.setColor(backGroundColor);
    }

    @Override
    public void setStart(Operation[] curOperation, int x, int y) {
        clearAreaUtil.setStart(curOperation,x,y);
    }

    @Override
    public void setProcess(Operation[] curOperation, int x, int y) {
        clearAreaUtil.setProcess(curOperation,x,y);
    }

    @Override
    public void setTerminal(Operation[] curOperation, int x, int y) {
        clearAreaUtil.setTerminal(curOperation,x,y);
        end();
    }
}
