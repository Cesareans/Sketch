package cesare.operationUtil.specialUtil;

import cesare.GUIComponents.SketchCanvas;
import cesare.operation.Operation;
import cesare.operation.special.Text;
import cesare.operationUtil.OperationUtil;

import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class TextUtil extends OperationUtil {
    public TextUtil(){
        operationType = OperationType.MultiOnePointType;
    }
    private Text curText;
    private int curPos;
    keyListener listener = new keyListener();
    private class keyListener extends KeyAdapter{
        @Override
        public void keyTyped(KeyEvent e) {
            super.keyTyped(e);
            if(curText == null)
                return;
            int ch = e.getKeyCode();
            switch (ch){
                case KeyEvent.VK_ESCAPE:
                    end();
                    break;
                case KeyEvent.VK_BACK_SPACE:
                    curText.text = curText.text.substring(0,curText.text.length()-1);
                    --curPos;
                    break;
                default:
                    curText.text += e.getKeyChar();
                    break;
                /*case KeyEvent.VK_LEFT:
                    --curPos;
                    if(curPos < 0)
                        curPos = 0;
                    break;
                case KeyEvent.VK_RIGHT:
                    ++curPos;
                    if(curPos > curText.text.length())
                        curPos = curText.text.length();*/

            }
        }
    }
    @Override
    public void setStart(Operation[] curOperation, int x, int y) {
        curOperation[0] = new Text();
        curText = ((Text) curOperation[0]);
        curText.setAnchor(x,y);
        curPos = 0;
        SketchCanvas.getSketchCanvas().addKeyListener(listener);
    }

    @Override
    public void setProcess(Operation[] curOperation, int x, int y) {
    }

    @Override
    public void setTerminal(Operation[] curOperation, int x, int y) {

    }

}
