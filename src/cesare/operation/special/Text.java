package cesare.operation.special;

import cesare.operation.Operation;

import java.awt.*;

public class Text extends Operation {
    Font textFont;
    public String text;
    int x,y;
    public void setAnchor(int x , int y){
        this.x = x;
        this.y = y;
    }
    @Override
    public void operate(Graphics g) {
        Font temp = g.getFont();
        g.setFont(textFont);
        if(text != null)
            g.drawString(text,x,y);
        g.setFont(temp);
    }
}
