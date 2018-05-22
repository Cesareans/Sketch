package cesare.operationUtil.specialUtil;

import cesare.GUIComponents.SketchCanvasPane;
import cesare.operation.Operation;
import cesare.operation.special.CopyArea;
import cesare.operationUtil.OperationUtil;

public class CopyAreaUtil extends OperationUtil{
    private boolean onMove = false;
    private boolean retain;
    public CopyAreaUtil(boolean retain){
        operationType = OperationType.MultiTwoPointType;
        this.retain = retain;
    }
    int initX,initY;
    @Override
    public void setStart(Operation[] curOperation, int x, int y) {
        if(curOperation[0] == null) {
            curOperation[0] = new CopyArea(x, y, x, y ,retain);
            onMove = false;
        }
        else{
            if(((CopyArea) curOperation[0]).isInArea(x,y)) {
                onMove = true;
                initX = x;
                initY = y;
            }else {
                end();
                SketchCanvasPane.getInstance().cancelSelectRegion();
            }
        }
    }

    @Override
    public void setProcess(Operation[] curOperation, int x, int y) {
        if(!isEnd()) {
            if (onMove) {
                ((CopyArea) curOperation[0]).setDistance(x - initX, y - initY);
            } else {
                ((CopyArea) curOperation[0]).setSecondPoint(x, y);
            }
        }
    }

    @Override
    public void setTerminal(Operation[] curOperation, int x, int y) {
        if(!isEnd()) {
            if (onMove) {
                ((CopyArea) curOperation[0]).setDistance(x - initX, y - initY);
                ((CopyArea) curOperation[0]).confirmMove();
            } else {
                ((CopyArea) curOperation[0]).setSecondPoint(x, y);
                onMove = true;
            }
        }
    }
}
