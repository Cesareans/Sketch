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

    private final int barHeight = 40;
    private final int attrWidth = 120;
    private int VisualRegionHeight = 640;
    private int VisualRegionWidth = 960;

    private int canvasWidth = 640;
    private int canvasHeight = 480;


    private SpringLayout layout = new SpringLayout();

    private SketchMenuBar sketchMenuBar = SketchMenuBar.getSketchMenuBar();
    private SketchUtilBar sketchUtilBar = SketchUtilBar.getSketchUtilBar();
    private SketchUtilAttributePanel sketchUtilAttributePanel = SketchUtilAttributePanel.getSketchUtilAttributePanel();
    private SketchInfoBar sketchInfoBar = SketchInfoBar.getSketchInfoBar();
    private SketchCanvas sketchCanvas = SketchCanvas.getSketchCanvas().setCanvasSize(canvasWidth,canvasHeight);

    public SketchMainFrame(){
        JPanel contentPanel = new JPanel();
        contentPanel.setPreferredSize(new Dimension(VisualRegionWidth,VisualRegionHeight));
        setContentPane(contentPanel);

        setLayout(layout);

        setJMenuBar(sketchMenuBar);
        add(sketchUtilBar);
        add(sketchUtilAttributePanel);
        add(sketchInfoBar);
        add(sketchCanvas);
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
        constraints.setHeight(Spring.constant(barHeight));

        //sketchUtilAttributePanel
        constraints = layout.getConstraints(sketchUtilAttributePanel);
        constraints.setX(Spring.constant(0));
        constraints.setY(Spring.constant(layout.getConstraints(sketchUtilBar).getConstraint(SpringLayout.SOUTH).getValue()));
        constraints.setWidth(Spring.constant(attrWidth));
        constraints.setHeight(Spring.constant(VisualRegionHeight - barHeight - layout.getConstraints(sketchUtilBar).getConstraint(SpringLayout.SOUTH).getValue()));

        //sketchCanvas
        constraints = layout.getConstraints(sketchCanvas);
        constraints.setX(Spring.constant(layout.getConstraints(sketchUtilAttributePanel).getConstraint(SpringLayout.EAST).getValue()));
        constraints.setY(Spring.constant(barHeight));
        constraints.setWidth(Spring.constant(canvasWidth));
        constraints.setHeight(Spring.constant(canvasHeight));


        //sketchInfoBar
        constraints = layout.getConstraints(sketchInfoBar);
        constraints.setX(Spring.constant(0));
        constraints.setY(Spring.constant(VisualRegionHeight - 20));
        constraints.setWidth(Spring.constant(VisualRegionWidth));
        constraints.setHeight(Spring.constant(barHeight));
    }
}
