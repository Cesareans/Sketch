package cesare.operation.graphic;

import cesare.operation.Operation;

import java.awt.*;

//This SketchShape class is based on all that shape can be confirmed by two point.
public abstract class SketchShape extends Operation {
    protected Color color;

    protected boolean isFilled = false;
    protected int lineWidth = 1;
    protected int dashedLength = 0;

    protected boolean isGradient = false;
    protected Color secondColor = null;


    public SketchShape setColor(Color color) {
        this.color = color;
        return this;
    }
    public SketchShape setFilled(boolean filled){
        isFilled = filled;
        return this;
    }
    public SketchShape setLineWidth(int lineWidth){
        this.lineWidth = lineWidth;
        return this;
    }
    public SketchShape setDashedLength(int dashedLength){
        this.dashedLength = dashedLength;
        return this;
    }
    public SketchShape setGradient(boolean gradient){
        isGradient = gradient;
        return this;
    }
    public SketchShape setSecondColor(Color color){
        this.secondColor = color;
        return this;
    }

    public abstract void draw(Graphics g);
    @Override
    public void operate(Graphics g) {
        Graphics2D g2d = ((Graphics2D) g);
        g2d.setColor(color);
        if(dashedLength!=0){
            BasicStroke bs = new BasicStroke(lineWidth , BasicStroke.CAP_BUTT , 0 , BasicStroke.JOIN_BEVEL,new float[]{dashedLength} , 0);
            g2d.setStroke(bs);
        }else{
            BasicStroke bs = new BasicStroke(lineWidth , BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL);
            g2d.setStroke(bs);
        }
        draw(g);
    }
}
