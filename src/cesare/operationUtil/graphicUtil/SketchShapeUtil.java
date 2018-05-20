package cesare.operationUtil.graphicUtil;

import cesare.operationUtil.OperationUtil;

import java.awt.*;

public abstract class SketchShapeUtil extends OperationUtil {
    protected Color color = Color.black;

    protected boolean isFilled = false;
    protected int lineWidth = 1;
    protected int dashedLength = 0;

    protected boolean isGradient = false;
    protected Color secondColor = null;

    public void setColor(Color color) {
        this.color = color;
    }
    public void setFilled(boolean filled){
        isFilled = filled;
    }
    public void setGradient(boolean gradient){
        isGradient = gradient;
    }
    public void setSecondColor(Color color){
        secondColor = color;
    }
    public void setLineWidth(int lineWidth){
        this.lineWidth = lineWidth;
    }
    public void setDashedLength(int dashedLength){
        this.dashedLength = dashedLength;
    }
}
