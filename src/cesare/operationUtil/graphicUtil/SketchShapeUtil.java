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

    public SketchShapeUtil setColor(Color color) {
        this.color = color;
        return this;
    }
    public SketchShapeUtil setFilled(boolean filled){
        isFilled = filled;
        return this;
    }
    public SketchShapeUtil setGradient(boolean gradient){
        isGradient = gradient;
        return this;
    }
    public SketchShapeUtil setSecondColor(Color color){
        secondColor = color;
        return this;
    }
    public SketchShapeUtil setLineWidth(int lineWidth){
        this.lineWidth = lineWidth;
        return this;
    }
    public SketchShapeUtil setDashedLength(int dashedLength){
        this.dashedLength = dashedLength;
        return this;
    }
}
