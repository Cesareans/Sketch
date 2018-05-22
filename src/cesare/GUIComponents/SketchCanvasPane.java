package cesare.GUIComponents;

import cesare.operation.Operation;
import cesare.operation.special.Clip;
import cesare.operation.special.CopyArea;
import cesare.operationUtil.OperationUtil;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class SketchCanvasPane extends JDesktopPane {
    public class SketchCanvas extends JInternalFrame {
        private class Canvas extends JPanel {
            private class SelectRegion{
                int x1,y1,x2,y2;
                boolean isClip;
                final int dashedLength = 3;
                SelectRegion(int x1, int y1 , int x2 , int y2 , boolean isClip) {
                    this.x1 = x1;
                    this.y1 = y1;
                    this.x2 = x2;
                    this.y2 = y2;
                    this.isClip = isClip;
                }
                void setRegion(int x1, int y1 , int x2 , int y2 , boolean isClip){
                    this.x1 = x1;
                    this.y1 = y1;
                    this.x2 = x2;
                    this.y2 = y2;
                    this.isClip = isClip;
                }

                void draw(Graphics g){
                    Graphics2D g2d = ((Graphics2D) g);
                    g2d.setColor(Color.black);
                    g2d.setStroke(new BasicStroke(1,BasicStroke.CAP_BUTT,0,BasicStroke.JOIN_BEVEL,new float[]{dashedLength} , 0));
                    g2d.draw(new Rectangle2D.Double(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x2 - x1), Math.abs(y2 - y1)));
                }
            }
            private class mouseListener extends MouseAdapter {
                @Override
                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);
                    currentOperationUtilRef = SketchUtilBar.getInstance().getOperationUtilRef();
                    if (currentOperationUtilRef != null) {
                        if (e.getButton() == MouseEvent.BUTTON1) {
                            if (currentOperationUtilRef.isEnd()) {
                                currentOperationUtilRef.reStart();
                            }
                            currentOperationUtilRef.setStart(currentOperation, e.getX(), e.getY());
                        } else if (e.getButton() == MouseEvent.BUTTON3) {
                            currentOperationUtilRef.end();
                            confirmOperation();
                        }
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    super.mouseReleased(e);
                    if(e.getButton() == MouseEvent.BUTTON3)
                        return;
                    if (currentOperationUtilRef != null) {
                        switch (currentOperationUtilRef.getOperationType()) {
                            case MultiTwoPointType:
                                currentOperationUtilRef.setTerminal(currentOperation, e.getX(), e.getY());
                                if (currentOperationUtilRef.isEnd()) //不管左键右键
                                    confirmOperation();
                                break;
                        }
                    }
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    super.mouseEntered(e);
                    for (Component component : getParent().getComponents()) {
                        if (component instanceof SketchInfoBar) {
                            ((SketchInfoBar) component).setMouseState(true);
                        }
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    super.mouseExited(e);
                    for (Component component : getParent().getComponents()) {
                        if (component instanceof SketchInfoBar) {
                            ((SketchInfoBar) component).setMouseState(false);
                        }
                    }
                }

            }
            private class motionListener extends MouseMotionAdapter {
                @Override
                public void mouseDragged(MouseEvent e) {
                    super.mouseDragged(e);
                    if(currentOperationUtilRef == null || currentOperationUtilRef.getOperationType() != OperationUtil.OperationType.MultiTwoPointType)
                        return;
                    if (currentOperation[0] != null) {
                        if (!currentOperationUtilRef.isEnd())
                            currentOperationUtilRef.setProcess(currentOperation, e.getX(), e.getY());
                    }
                    SketchInfoBar.getInstance().setMouseInfo(e.getX(), e.getY());
                    updateUI();
                }

                @Override
                public void mouseMoved(MouseEvent e) {
                    super.mouseMoved(e);
                    if(currentOperationUtilRef == null || currentOperationUtilRef.getOperationType() != OperationUtil.OperationType.MultiOnePointType)
                        return;
                    if (currentOperation[0] != null) {

                        if (!currentOperationUtilRef.isEnd())
                            currentOperationUtilRef.setProcess(currentOperation, e.getX(), e.getY());
                    }
                    SketchInfoBar.getInstance().setMouseInfo(e.getX(), e.getY());
                    updateUI();
                }

            }
            private class keyListener extends KeyAdapter{
                @Override
                public void keyReleased(KeyEvent e) {
                    super.keyReleased(e);
                    if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
                        if(selectRegion != null){
                            cancelSelectRegion();
                        }
                    }
                }
            }

            //Size
            private int canvasWidth;
            private int canvasHeight;
            Canvas(int width, int height) {
                setCanvasSize(width, height);
                addMouseListener(new mouseListener());
                addMouseMotionListener(new motionListener());
                addKeyListener(new keyListener());
                setFocusable(true);
            }
            void setCanvasSize(int width, int height) {
                canvasWidth = width;
                canvasHeight = height;
                setPreferredSize(new Dimension(width, height));
            }

            //Operation
            OperationUtil currentOperationUtilRef;
            Operation currentOperation[] = new Operation[1];
            ArrayList<Operation> operations = new ArrayList<>();
            ArrayList<Operation> revokedOperations = new ArrayList<>();
            void confirmOperation() {
                if (currentOperation[0] != null) {
                    operations.add(currentOperation[0]);
                    revokedOperations.clear();
                    currentOperation[0] = null;
                }
                setEditState(true);
                updateUI();
            }
            void revokeOperation() {
                if (operations.size() > 0) {
                    revokedOperations.add(operations.remove(operations.size() - 1));
                    updateUI();
                }
                if(operations.size() == savedOperationIndex)
                    setEditState(false);
                else
                    setEditState(true);
            }
            void retrieveOperation() {
                if (revokedOperations.size() > 0) {
                    operations.add((revokedOperations.remove(revokedOperations.size() - 1)));
                    updateUI();
                }
                if(operations.size() == savedOperationIndex)
                    setEditState(false);
                else
                    setEditState(true);
            }
            void operate(Graphics g) {
                 if (backImage == null) {
                    g.setColor(Color.white);
                    g.fillRect(0, 0, canvasWidth, canvasHeight);
                 } else {
                    g.drawImage(backImage, 0, 0, canvasWidth, canvasHeight, null);
                 }

                Graphics2D g2d = ((Graphics2D) g);
                Stroke preStroke = g2d.getStroke();

                for (Operation operation : operations) {
                    operation.operate(g);
                }
                if (currentOperation[0] != null) {
                    currentOperation[0].operate(g);
                }
                if (selectRegion != null)
                    selectRegion.draw(g);

                g2d.setStroke(preStroke);
            }

            //State
            private SelectRegion selectRegion;
            private boolean isEdited = false;
            private int savedOperationIndex = 0;
            private Image backImage = null;
            void setSelectRegion(int x1,int y1,int x2,int y2,boolean isClip){
                if(selectRegion == null)
                    selectRegion = new SelectRegion(x1,y1,x2,y2,isClip);
                else
                    selectRegion.setRegion(x1,y1,x2,y2,isClip);
            }
            void cancelSelectRegion(){
                if(selectRegion != null && selectRegion.isClip)
                    operations.add(Clip.clearClip());
                selectRegion = null;
                updateUI();
            }
            void setEditState(boolean isEdited){
                this.isEdited = isEdited;
                String title = SketchCanvas.this.getTitle();
                int length = title.length();
                if(isEdited && title.charAt(length - 1) != '*') {
                    SketchCanvas.this.setTitle(title + '*');
                }else if(!isEdited){
                    savedOperationIndex = operations.size();
                    if(title.charAt(length - 1) == '*')
                        SketchCanvas.this.setTitle(title.substring(0,length - 1));
                }
            }
            boolean getEditState() {
                return isEdited;
            }
            void setBackImage(Image image){
                backImage = image;
            }

            //BufferedImage
            BufferedImage convertToImage(){
                BufferedImage image = new BufferedImage(canvasWidth,canvasHeight,BufferedImage.TYPE_INT_RGB);
                operate(image.createGraphics());
                return image;
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                operate(g);
            }
        }

        private File imageFile = null;
        private String imageFormat;
        private int windowHeight;
        private int windowWidth;
        public Canvas canvas;
        private SpringLayout layout = new SpringLayout();

        void cancelSelectRegion(){
            canvas.cancelSelectRegion();
        }
        void setSelectRegion(int x1,int y1,int x2,int y2,boolean isClip) {
            canvas.setSelectRegion(x1,y1,x2,y2,isClip);
        }

        public int getCanvasWidth(){
            return canvas.canvasWidth;
        }
        public int getCanvasHeight(){
            return canvas.canvasHeight;
        }
        //File
        private void saveToFile(){
            if(imageFile == null){
                exportToFile("png");
            }else{
                try {
                    ImageIO.write(canvas.convertToImage(), imageFormat, imageFile);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            canvas.setEditState(false);
        }
        private void saveToAnotherFile(){
            exportToFile("png");
            canvas.setEditState(false);
        }
        private void exportToFile(String fileFormat){
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int value = fileChooser.showSaveDialog(SketchMainFrame.getInstance());
            if(value == JFileChooser.APPROVE_OPTION) {
                String selectedPath = fileChooser.getSelectedFile().getPath();
                fileChooser.setVisible(false);
                File imageFile = new File(selectedPath);

                if (imageFile.exists()) {
                    int choice = JOptionPane.showConfirmDialog(SketchMainFrame.getInstance(), "File overwriting. Please confirm.", "Confirm", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
                    if (choice == JOptionPane.CANCEL_OPTION) {
                        JOptionPane.showMessageDialog(SketchMainFrame.getInstance(), "File overwriting cancelled.", "Cancel", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                }else{
                    if (!selectedPath.substring(selectedPath.length() - fileFormat.length(), selectedPath.length() - 1).equals(fileFormat))
                        selectedPath += ("." + fileFormat) ;
                    imageFile = new File(selectedPath);
                }

                try {
                    ImageIO.write(canvas.convertToImage(), fileFormat, imageFile);
                    imageFormat = fileFormat;
                    setFile(imageFile);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        }
        private void setFile(File file) {
            imageFile = file;
            setTitle(imageFile.getName());
        }

        private void resized(){
            SpringLayout.Constraints constraints;
            constraints = layout.getConstraints(canvas);
            constraints.setX(Spring.constant(0));
            constraints.setY(Spring.constant(0));
            constraints.setWidth(Spring.constant(windowWidth));
            constraints.setHeight(Spring.constant(windowHeight));

        }
        private void partialInit(){
            JPanel contentPanel = new JPanel();
            contentPanel.setPreferredSize(new Dimension(windowWidth,windowHeight));
            setContentPane(contentPanel);

            setLayout(layout);
            add(canvas);
            resized();

            pack();
            setVisible(true);
            addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    super.componentResized(e);
                    resized();
                }
            });
        }
        SketchCanvas(File file){
            super(file.getName(),true,true,true,true);
            try {
                setFile(file);
                BufferedImage image = ImageIO.read(file);
                windowWidth = image.getWidth();
                windowHeight = image.getHeight();

                canvas = new Canvas(windowWidth,windowHeight);
                canvas.setBackImage(image);
                partialInit();
            }catch (Exception ex){
                ex.printStackTrace();
                JOptionPane.showMessageDialog(SketchMainFrame.getInstance(),"File Open Failed!","Warning!",JOptionPane.INFORMATION_MESSAGE);
            }
        }
        SketchCanvas(int width , int height){
            super("Untitled*", true, true, true, true);
            windowWidth = width;
            windowHeight = height;

            canvas = new Canvas(windowWidth, windowHeight);
            partialInit();
        }

        private void revokeOperation() {
            canvas.revokeOperation();
        }
        private void retrieveOperation() {
            canvas.retrieveOperation();
        }
        private BufferedImage getCanvasImage(){
            return canvas.convertToImage();
        }
        private BufferedImage getCanvasImageInOperations(){
            return null;
        }
    }

    private static SketchCanvasPane ourInstance = new SketchCanvasPane();
    public static SketchCanvasPane getInstance() {
        return ourInstance;
    }

    private SketchCanvasPane() {
        setBackground(Color.white);
    }

    public void cancelSelectRegion() {
        if (getSelectedFrame() != null)
            ((SketchCanvas) getSelectedFrame()).cancelSelectRegion();
    }
    public void setSelectRegion(int x1,int y1,int x2,int y2,boolean isClip) {
        if (getSelectedFrame() != null)
            ((SketchCanvas) getSelectedFrame()).setSelectRegion(x1, y1, x2, y2, isClip);
    }
    public void revokeOperation() {
        if (getSelectedFrame() != null)
            ((SketchCanvas) getSelectedFrame()).revokeOperation();
    }
    public void retrieveOperation() {
        if (getSelectedFrame() != null)
            ((SketchCanvas) getSelectedFrame()).retrieveOperation();
    }
    public BufferedImage getCanvasImage() {
        if (getSelectedFrame() != null)
            return ((SketchCanvas) getSelectedFrame()).getCanvasImage();
        else
            return null;
    }
    public BufferedImage getCanvasImageInOperations(){
        if (getSelectedFrame() != null)
            return ((SketchCanvas) getSelectedFrame()).getCanvasImageInOperations();
        else
            return null;
    }
    public void saveToFile() {
        if (getSelectedFrame() != null)
            ((SketchCanvas) getSelectedFrame()).saveToFile();
    }
    public void saveToAnotherFile() {
        if (getSelectedFrame() != null)
            ((SketchCanvas) getSelectedFrame()).saveToAnotherFile();
    }

    public void newCanvasFromFile(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int value = fileChooser.showOpenDialog(SketchMainFrame.getInstance());
        if(value == JFileChooser.APPROVE_OPTION){
            String selectedPath = fileChooser.getSelectedFile().getPath();
            fileChooser.setVisible(false);
            File imageFile = new File(selectedPath);
            if(imageFile.exists()){
                SketchCanvas canvas = new SketchCanvas(imageFile);
                add(canvas);
            }else{
                JOptionPane.showMessageDialog(SketchMainFrame.getInstance(),"File not existed!" , "Warning" , JOptionPane.WARNING_MESSAGE);
            }
        }
    }
    public void newCanvas(int width , int height){
        SketchCanvas canvas = new SketchCanvas(width,height);
        add(canvas);
    }
}
