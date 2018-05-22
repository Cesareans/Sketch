package cesare.operationUtil;

import cesare.operation.Operation;

public abstract class OperationUtil {
    public enum OperationType{
        MultiTwoPointType,
        MultiOnePointType
    }
    protected OperationType operationType;
    final public OperationType getOperationType(){
        return operationType;
    }

    //为了多段多点操作
    private boolean isEnd = true;
    final public void end(){
        isEnd = true;
    }
    final public void reStart(){
        isEnd = false;
    }
    final public boolean isEnd(){
        return isEnd;
    }

    protected int initX;
    protected int initY;
    public abstract void setStart(Operation[] curOperation, int x, int y);
    public abstract void setProcess(Operation[] curOperation , int x , int y);
    public abstract void setTerminal(Operation[] curOperation, int x, int y);
}