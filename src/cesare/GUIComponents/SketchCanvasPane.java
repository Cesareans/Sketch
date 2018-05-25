package cesare.GUIComponents;

import cesare.operation.Operation;
import cesare.operation.special.Clip;
import cesare.operationUtil.OperationUtil;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.util.ArrayList;

public class SketchCanvasPane extends JDesktopPane {
    class Filter extends FileFilter {
        String[] suffixes;
        Filter(String... suffixes) {
            super();
            this.suffixes = suffixes;
        }
        boolean matchSuffix(String path){
            for(String suffix:suffixes){
                if(path.toLowerCase().endsWith('.' + suffix.toLowerCase()))
                    return true;
            }
            return false;
        }


        @Override
        public boolean accept(File file) {
            if(file.isDirectory())
                return true;
            for(String suffix:suffixes){
                if(file.getName().toLowerCase().endsWith('.' + suffix.toLowerCase()))
                    return true;
            }
            return false;
        }

        @Override
        public String getDescription() {
            StringBuilder description = new StringBuilder();
            for(int i = 0 ; i < suffixes.length - 1; ++i){
                description.append("*.").append(suffixes[i]).append('/');
            }
            description.append("*.").append(suffixes[suffixes.length - 1]);
            return description.toString();
        }
    }
    class SketchCanvas extends JInternalFrame {
        class Canvas extends JPanel {
            class SelectRegion{
                int x1,y1,x2,y2;
                final int dashedLength = 3;
                SelectRegion(int x1, int y1 , int x2 , int y2) {
                    setRegion(x1, y1, x2, y2);
                }
                void setRegion(int x1, int y1 , int x2 , int y2){
                    this.x1 = x1;
                    this.y1 = y1;
                    this.x2 = x2;
                    this.y2 = y2;
                }
                boolean inRegion(int x, int y) {
                    return x > Math.min(x1, x2) && x < Math.max(x1, x2) && y > Math.min(y1, y2) && y < Math.max(y1, y2);
                }
                void draw(Graphics g){
                    Graphics2D g2d = ((Graphics2D) g);
                    g2d.setColor(Color.black);
                    g2d.setStroke(new BasicStroke(1,BasicStroke.CAP_BUTT,0,BasicStroke.JOIN_BEVEL,new float[]{dashedLength} , 0));
                    g2d.draw(new Rectangle2D.Double(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x2 - x1), Math.abs(y2 - y1)));
                }
            }
            class mouseListener extends MouseAdapter {
                @Override
                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);
                    currentOperationUtilRef = SketchUtilBar.getInstance().getOperationUtilRef();
                    if(selectRegion !=null && !selectRegion.inRegion(e.getX() , e.getY())){
                        cancelSelectRegion();
                        if(isClipped){
                            operations.add(Clip.clearClip());
                            isClipped = false;
                        }
                    }
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
            class motionListener extends MouseMotionAdapter {
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
            class keyListener extends KeyAdapter{
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
            int canvasWidth;
            int canvasHeight;
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

            //BufferedImage
            BufferedImage bufferedImage;
            BufferedImage imageInOperations(){
                ColorModel cm = bufferedImage.getColorModel();
                boolean isAlpha = cm.isAlphaPremultiplied();
                WritableRaster raster = bufferedImage.copyData(null);
                return new BufferedImage(cm, raster, isAlpha, null);
            }
            BufferedImage convertToImage(){
                bufferedImage = new BufferedImage(canvasWidth, canvasHeight, BufferedImage.TYPE_INT_RGB);
                operate(bufferedImage);
                return bufferedImage;
            }

            //Operation
            boolean isEdited = false;
            int savedOperationIndex = 0;
            OperationUtil currentOperationUtilRef;
            Operation currentOperation[] = new Operation[1];
            ArrayList<Operation> operations = new ArrayList<>();
            ArrayList<Operation> revokedOperations = new ArrayList<>();
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
            void confirmOperation() {
                if (currentOperation[0] != null) {
                    operations.add(currentOperation[0]);
                    if(currentOperation[0] instanceof Clip)
                        isClipped = true;
                    revokedOperations.clear();
                    currentOperation[0] = null;
                }
                setEditState(true);
                updateUI();
                SketchUtilBar.getInstance().updateButtonState();
                SketchMenuBar.getInstance().updateMenuItemState();
            }
            void undoOperation() {
                if (operations.size() > 0) {
                    revokedOperations.add(operations.remove(operations.size() - 1));
                    updateUI();
                }
                if(operations.size() == savedOperationIndex)
                    setEditState(false);
                else
                    setEditState(true);
                SketchUtilBar.getInstance().updateButtonState();
                SketchMenuBar.getInstance().updateMenuItemState();
            }
            void redoOperation() {
                if (revokedOperations.size() > 0) {
                    operations.add((revokedOperations.remove(revokedOperations.size() - 1)));
                    updateUI();
                }
                if(operations.size() == savedOperationIndex)
                    setEditState(false);
                else
                    setEditState(true);
                SketchUtilBar.getInstance().updateButtonState();
                SketchMenuBar.getInstance().updateMenuItemState();
            }
            void operate(BufferedImage image) {
                Graphics2D g = image.createGraphics();
                if (backImage == null) {
                    g.setColor(Color.white);
                    g.fillRect(0, 0, canvasWidth, canvasHeight);
                } else {
                    g.drawImage(backImage, 0, 0, canvasWidth, canvasHeight, null);
                }

                Stroke preStroke = g.getStroke();

                for (Operation operation : operations) {
                    operation.operate(g);
                }
                if (currentOperation[0] != null)
                    currentOperation[0].operate(g);

                g.setStroke(preStroke);
            }

            //State
            private boolean isClipped = false;
            private SelectRegion selectRegion;
            private Image backImage = null;
            void setSelectRegion(int x1,int y1,int x2,int y2){
                if(selectRegion == null)
                    selectRegion = new SelectRegion(x1,y1,x2,y2);
                else
                    selectRegion.setRegion(x1,y1,x2,y2);
            }
            void cancelSelectRegion(){
                selectRegion = null;
                updateUI();
            }
            void setBackImage(Image image){
                backImage = image;
            }


            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                bufferedImage = new BufferedImage(canvasWidth, canvasHeight, BufferedImage.TYPE_INT_RGB);
                operate(bufferedImage);
                g.drawImage(bufferedImage,0,0,canvasWidth,canvasHeight,null);
                if (selectRegion != null) {
                    selectRegion.draw(g);
                }
            }
        }

        private File imageFile = null;
        private String imageFormat;
        private int windowHeight;
        private int windowWidth;
        public Canvas canvas;
        private SpringLayout layout = new SpringLayout();

        public int getCanvasWidth(){
            return canvas.canvasWidth;
        }
        public int getCanvasHeight(){
            return canvas.canvasHeight;
        }

        private BufferedImage getCanvasImage(){
            return canvas.convertToImage();
        }
        private BufferedImage getCanvasImageInOperations(){
            return canvas.imageInOperations();
        }
        void cancelSelectRegion(){
            canvas.cancelSelectRegion();
        }
        void setSelectRegion(int x1,int y1,int x2,int y2) {
            canvas.setSelectRegion(x1,y1,x2,y2);
        }
        //File
        private void saveToFile(){
            if(imageFile == null){
                exportToFile();
            }else{
                try {
                    ImageIO.write(canvas.convertToImage(), imageFormat, imageFile);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                canvas.setEditState(false);
            }
        }
        private void saveToAnotherFile(){
            exportToFile();
        }
        private void exportToFile(){
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.setFileFilter(new Filter("jpg","jpeg"));
            fileChooser.addChoosableFileFilter(new Filter("png"));
            fileChooser.addChoosableFileFilter(new Filter("bmp"));
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int value = fileChooser.showSaveDialog(SketchMainFrame.getInstance());
            if(value == JFileChooser.APPROVE_OPTION) {
                String selectedPath = fileChooser.getSelectedFile().getPath();
                if(!((Filter) fileChooser.getFileFilter()).matchSuffix(selectedPath)){
                    int i = selectedPath.lastIndexOf('.');
                    if(i >= 0)
                        selectedPath = selectedPath.substring(0 , i) + '.' + ((Filter) fileChooser.getFileFilter()).suffixes[0];
                    else
                        selectedPath = selectedPath + '.' + ((Filter) fileChooser.getFileFilter()).suffixes[0];
                }
                fileChooser.setVisible(false);
                File imageFile = new File(selectedPath);
                if (imageFile.exists()) {
                    int choice = JOptionPane.showConfirmDialog(SketchMainFrame.getInstance(), "File overwriting. Please confirm.", "Confirm", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
                    if (choice == JOptionPane.CANCEL_OPTION) {
                        JOptionPane.showMessageDialog(SketchMainFrame.getInstance(), "File overwriting cancelled.", "Cancel", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                }

                try {
                    setFile(new File(selectedPath));
                    ImageIO.write(canvas.convertToImage(), imageFormat, imageFile);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                canvas.setEditState(false);
            }
        }
        private void setFile(File file) {
            imageFile = file;
            String name = file.getName();
            imageFormat = name.substring(name.lastIndexOf('.') + 1);
            setTitle(name);
        }
        //Operations
        private boolean canUndo(){
            return canvas.operations.size() > 0;
        }
        private boolean canRedo(){
            return canvas.revokedOperations.size() > 0;
        }
        private void undoOperation() {
            canvas.undoOperation();
        }
        private void redoOperation() {
            canvas.redoOperation();
        }

        //Frame
        private void resized(){
            SpringLayout.Constraints constraints;
            constraints = layout.getConstraints(canvas);
            constraints.setX(Spring.constant(0));
            constraints.setY(Spring.constant(0));
            constraints.setWidth(Spring.constant(windowWidth));
            constraints.setHeight(Spring.constant(windowHeight));

        }
        private void onClosing() {
            if (canvas.isEdited) {
                int choice = JOptionPane.showInternalOptionDialog(this, "File not saved, confirm closing?", "Info", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[]{"Cancel", "OK", "Save"}, "Save");
                switch (choice) {
                    case 0://Save
                        saveToFile();
                        break;
                    case 1://OK
                        dispose();
                        break;
                    case 2://Cancel
                        break;
                }
            } else
                dispose();
        }
        private void partialInit(){
            JPanel contentPanel = new JPanel();
            contentPanel.setPreferredSize(new Dimension(windowWidth,windowHeight));
            setContentPane(contentPanel);

            setLayout(layout);
            add(canvas);
            resized();

            pack();
            setDefaultCloseOperation(JInternalFrame.DO_NOTHING_ON_CLOSE);
            setVisible(true);
            addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    super.componentResized(e);
                    resized();
                }
            });
            addInternalFrameListener(new InternalFrameAdapter() {
                @Override
                public void internalFrameClosing(InternalFrameEvent e) {
                    super.internalFrameClosing(e);
                    onClosing();
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
                JOptionPane.showMessageDialog(SketchMainFrame.getInstance(),"File Open Failed!","Warning!",JOptionPane.INFORMATION_MESSAGE);
            }

        }
        SketchCanvas(int width , int height){
            super("Untitled", true, true, true, true);
            windowWidth = width;
            windowHeight = height;

            canvas = new Canvas(windowWidth, windowHeight);
            partialInit();
        }
    }

    private static SketchCanvasPane ourInstance = new SketchCanvasPane();
    public static SketchCanvasPane getInstance() {
        return ourInstance;
    }

    private SketchCanvasPane() {
        setBackground(Color.white);
    }

    public int getCanvasWidth(){
        if(getSelectedFrame()!=null)
            return ((SketchCanvas) getSelectedFrame()).getCanvasWidth();
        else
            return 0;
    }
    public int getCanvasHeight(){
        if(getSelectedFrame() != null)
            return ((SketchCanvas) getSelectedFrame()).getCanvasHeight();
        else
            return 0;
    }

    public void cancelSelectRegion() {
        if (getSelectedFrame() != null)
            ((SketchCanvas) getSelectedFrame()).cancelSelectRegion();
    }
    public void setSelectRegion(int x1,int y1,int x2,int y2) {
        if (getSelectedFrame() != null)
            ((SketchCanvas) getSelectedFrame()).setSelectRegion(x1, y1, x2, y2);
    }
    public boolean canUndo(){
        if (getSelectedFrame() != null)
            return ((SketchCanvas) getSelectedFrame()).canUndo();
        else
            return false;
    }
    public boolean canRedo(){
        if (getSelectedFrame() != null)
            return ((SketchCanvas) getSelectedFrame()).canRedo();
        else
            return false;
    }
    public void undoOperation() {
        if (getSelectedFrame() != null)
            ((SketchCanvas) getSelectedFrame()).undoOperation();
    }
    public void redoOperation() {
        if (getSelectedFrame() != null)
            ((SketchCanvas) getSelectedFrame()).redoOperation();
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

    public void closeFile(){
        if(getSelectedFrame()!=null)
            getSelectedFrame().doDefaultCloseAction();
    }

    public void newCanvasFromFile(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(true);
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileFilter(new Filter("jpg","jpeg"));
        fileChooser.addChoosableFileFilter(new Filter("png"));
        fileChooser.addChoosableFileFilter(new Filter("bmp"));
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int value = fileChooser.showOpenDialog(SketchMainFrame.getInstance());
        if(value == JFileChooser.APPROVE_OPTION){
            fileChooser.setVisible(false);
            for(File selectedFile : fileChooser.getSelectedFiles()) {
                String selectedPath = selectedFile.getPath();
                File imageFile = new File(selectedPath);
                if (imageFile.exists()) {
                    SketchCanvas canvas = new SketchCanvas(imageFile);
                    addFrame(canvas);
                } else {
                    JOptionPane.showMessageDialog(SketchMainFrame.getInstance(), "File not existed!", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        }
    }
    public void newCanvas(int width , int height){
        SketchCanvas canvas = new SketchCanvas(width,height);
        addFrame(canvas);
    }
    private void addFrame(JInternalFrame frame){
        add(frame);
        this.setSelectedFrame(frame);
        if(getSelectedFrame() != null){
            JInternalFrame preFrame = getSelectedFrame();
            frame.setLocation(preFrame.getX() + 5 , preFrame.getY() + 5);
        }
    }
}
