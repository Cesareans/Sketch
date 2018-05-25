package cesare.operationUtil.specialUtil;

import cesare.GUIComponents.SketchCanvasPane;
import cesare.GUIComponents.SketchUtilBar;
import cesare.operation.Operation;
import cesare.operationUtil.OperationUtil;

import java.awt.*;


public class DropperUtil extends OperationUtil{
    public DropperUtil(){
        operationType = OperationType.MultiOnePointType;
    }
    @Override
    public void setStart(Operation[] curOperation, int x, int y) {
        SketchUtilBar.getInstance().setOnUsingColor(new Color(SketchCanvasPane.getInstance().getCanvasImageInOperations().getRGB(x,y) , true));
    }

    @Override
    public void setProcess(Operation[] curOperation, int x, int y) {
        SketchUtilBar.getInstance().setOnUsingColor(new Color(SketchCanvasPane.getInstance().getCanvasImageInOperations().getRGB(x,y) , true));
    }

    @Override
    public void setTerminal(Operation[] curOperation, int x, int y) {
        SketchUtilBar.getInstance().setOnUsingColor(new Color(SketchCanvasPane.getInstance().getCanvasImageInOperations().getRGB(x,y) , true));
        end();
    }
}
