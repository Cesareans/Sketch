package cesare.GUIComponents;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class SketchMainFrame extends JFrame {
    private final int utilBarHeight = 40;
    private final int infoBarHeight = 20;
    private final int attrWidth = 120;
    private int CanvasPaneHeight = 640;
    private int CanvasPaneWidth = 960;


    private SpringLayout layout = new SpringLayout();

    private SketchMenuBar sketchMenuBar = SketchMenuBar.getInstance();
    private SketchUtilBar sketchUtilBar = SketchUtilBar.getInstance();
    private SketchUtilAttributePanel sketchUtilAttributePanel = SketchUtilAttributePanel.getInstance();
    private SketchInfoBar sketchInfoBar = SketchInfoBar.getInstance();
    private SketchCanvasPane sketchCanvasPane = SketchCanvasPane.getInstance();

    private static SketchMainFrame sketchMainFrame = new SketchMainFrame();
    public static SketchMainFrame getInstance(){return sketchMainFrame;}
    private SketchMainFrame(){
        JPanel contentPanel = new JPanel();
        contentPanel.setPreferredSize(new Dimension(CanvasPaneWidth, CanvasPaneHeight));
        setContentPane(contentPanel);

        setJMenuBar(sketchMenuBar);
        setLayout(layout);
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

        addComponentListener(new ComponentAdapter(){
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                CanvasPaneHeight = getContentPane().getHeight();
                CanvasPaneWidth = getContentPane().getWidth();
                resized();
            }
        });
    }

    private void resized(){
        SpringLayout.Constraints constraints;

        //sketchUtilBar
        constraints = layout.getConstraints(sketchUtilBar);
        constraints.setX(Spring.constant(0));
        constraints.setY(Spring.constant(0));
        constraints.setWidth(Spring.constant(CanvasPaneWidth));
        constraints.setHeight(Spring.constant(utilBarHeight));

        //sketchUtilAttributePanel
        constraints = layout.getConstraints(sketchUtilAttributePanel);
        constraints.setX(Spring.constant(0));
        constraints.setY(Spring.constant(layout.getConstraints(sketchUtilBar).getConstraint(SpringLayout.SOUTH).getValue()));
        constraints.setWidth(Spring.constant(attrWidth));
        constraints.setHeight(Spring.constant(CanvasPaneHeight - infoBarHeight - layout.getConstraints(sketchUtilBar).getConstraint(SpringLayout.SOUTH).getValue()));

        //sketchCanvas
        constraints = layout.getConstraints(sketchCanvasPane);
        constraints.setX(Spring.constant(layout.getConstraints(sketchUtilAttributePanel).getConstraint(SpringLayout.EAST).getValue()));
        constraints.setY(Spring.constant(utilBarHeight));
        constraints.setWidth(Spring.constant(CanvasPaneWidth - constraints.getX().getValue() ));
        constraints.setHeight(Spring.constant(CanvasPaneHeight - constraints.getY().getValue() - infoBarHeight));


        //sketchInfoBar
        constraints = layout.getConstraints(sketchInfoBar);
        constraints.setX(Spring.constant(0));
        constraints.setY(Spring.constant(CanvasPaneHeight - infoBarHeight));
        constraints.setWidth(Spring.constant(CanvasPaneWidth));
        constraints.setHeight(Spring.constant(infoBarHeight));
    }
}
