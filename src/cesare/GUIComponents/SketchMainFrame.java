package cesare.GUIComponents;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class SketchMainFrame extends JFrame {
    class FrameComponentAdapter extends ComponentAdapter{
        @Override
        public void componentResized(ComponentEvent e) {
            VisualRegionHeight = getContentPane().getHeight();
            VisualRegionWidth = getContentPane().getWidth();
            resized();
        }
    }

    private final int utilBarHeight = 40;
    private final int infoBarHeight = 20;
    private final int attrWidth = 120;
    private int VisualRegionHeight = 640;
    private int VisualRegionWidth = 960;

    /*private int canvasPaneWidth = 640;
    private int canvasPaneHeight = 480;*/


    private SpringLayout layout = new SpringLayout();

    private SketchMenuBar sketchMenuBar = SketchMenuBar.getSketchMenuBar();
    private SketchUtilBar sketchUtilBar = SketchUtilBar.getSketchUtilBar();
    private SketchUtilAttributePanel sketchUtilAttributePanel = SketchUtilAttributePanel.getSketchUtilAttributePanel();
    private SketchInfoBar sketchInfoBar = SketchInfoBar.getSketchInfoBar();
    private SketchCanvasPane sketchCanvasPane = SketchCanvasPane.getInstance();

    private static SketchMainFrame sketchMainFrame = new SketchMainFrame();
    public static SketchMainFrame getInstance(){return sketchMainFrame;}
    private SketchMainFrame(){
        JPanel contentPanel = new JPanel();
        contentPanel.setPreferredSize(new Dimension(VisualRegionWidth,VisualRegionHeight));
        setContentPane(contentPanel);

        setLayout(layout);

        setJMenuBar(sketchMenuBar);
        add(sketchUtilBar);
        add(sketchUtilAttributePanel);
        add(sketchInfoBar);
        add(sketchCanvasPane);
        resized();

        setTitle("Sketch");
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        addComponentListener(new FrameComponentAdapter());
    }

    private void resized(){
        SpringLayout.Constraints constraints;

        //sketchUtilBar
        constraints = layout.getConstraints(sketchUtilBar);
        constraints.setX(Spring.constant(0));
        constraints.setY(Spring.constant(0));
        constraints.setWidth(Spring.constant(VisualRegionWidth));
        constraints.setHeight(Spring.constant(utilBarHeight));

        //sketchUtilAttributePanel
        constraints = layout.getConstraints(sketchUtilAttributePanel);
        constraints.setX(Spring.constant(0));
        constraints.setY(Spring.constant(layout.getConstraints(sketchUtilBar).getConstraint(SpringLayout.SOUTH).getValue()));
        constraints.setWidth(Spring.constant(attrWidth));
        constraints.setHeight(Spring.constant(VisualRegionHeight - infoBarHeight - layout.getConstraints(sketchUtilBar).getConstraint(SpringLayout.SOUTH).getValue()));

        //sketchCanvas
        constraints = layout.getConstraints(sketchCanvasPane);
        constraints.setX(Spring.constant(layout.getConstraints(sketchUtilAttributePanel).getConstraint(SpringLayout.EAST).getValue()));
        constraints.setY(Spring.constant(utilBarHeight));
        constraints.setWidth(Spring.constant(VisualRegionWidth - constraints.getX().getValue() ));
        constraints.setHeight(Spring.constant(VisualRegionHeight - constraints.getY().getValue() - infoBarHeight));


        //sketchInfoBar
        constraints = layout.getConstraints(sketchInfoBar);
        constraints.setX(Spring.constant(0));
        constraints.setY(Spring.constant(VisualRegionHeight - infoBarHeight));
        constraints.setWidth(Spring.constant(VisualRegionWidth));
        constraints.setHeight(Spring.constant(infoBarHeight));
    }
}
