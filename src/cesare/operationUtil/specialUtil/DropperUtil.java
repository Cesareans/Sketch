package cesare.operationUtil.specialUtil;

import cesare.GUIComponents.SketchMainFrame;
import cesare.GUIComponents.SketchUtilBar;
import cesare.operation.Operation;
import cesare.operationUtil.OperationUtil;

import javax.swing.*;
import java.awt.*;

public class DropperUtil extends OperationUtil{
    Robot robot;
    boolean canUse;
    public DropperUtil(){
        operationType = OperationType.MultiOnePointType;
        try {
            robot = new Robot();
            canUse = true;
        }catch (Exception ex){
            ex.printStackTrace();
            canUse = false;
        }
    }
    @Override
    public void setStart(Operation[] curOperation, int x, int y) {
        SketchUtilBar.getInstance().setOnUsingColor(robot.getPixelColor(x,y));
    }

    @Override
    public void setProcess(Operation[] curOperation, int x, int y) {
        SketchUtilBar.getInstance().setOnUsingColor(robot.getPixelColor(x,y));
    }

    @Override
    public void setTerminal(Operation[] curOperation, int x, int y) {
        SketchUtilBar.getInstance().setOnUsingColor(robot.getPixelColor(x,y));
        end();
    }
}
