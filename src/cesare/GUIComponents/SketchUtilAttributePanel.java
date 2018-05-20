package cesare.GUIComponents;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import static java.awt.GridBagConstraints.BOTH;
import static java.awt.GridBagConstraints.WEST;


public class SketchUtilAttributePanel extends JPanel {
    boolean isFilled = false;
    boolean isGradient = false;
    Color secondColor;
    int lineWidth;
    boolean isDashed = false;
    int dashedLength;

    JCheckBox filledCheckBox = new JCheckBox("Filled");
    JCheckBox gradientCheckBox = new JCheckBox("Use gradient");
    JButton secondColorButton = new JButton("2nd Color");

    JLabel lineWidthLabel = new JLabel("Line Width:");
    JTextField lineWidthTextField = new JTextField();

    JCheckBox dashedCheckBox = new JCheckBox("Dashed");
    JLabel dashLengthLabel = new JLabel("Dash length:");
    JTextField dashLengthTextField = new JTextField();

    GridBagLayout layout = new GridBagLayout();
    private static SketchUtilAttributePanel sketchUtilAttributePanel = new SketchUtilAttributePanel();
    public static SketchUtilAttributePanel getSketchUtilAttributePanel(){
        return sketchUtilAttributePanel;
    }
    public SketchUtilAttributePanel() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = WEST;
        constraints.fill = BOTH;
        setBorder(new EtchedBorder(EtchedBorder.LOWERED));

        filledCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isFilled = !isFilled;
            }
        });
        add(filledCheckBox);
        constraints.gridx = 0;constraints.gridy = 0;constraints.gridheight = 1;constraints.gridwidth = 4;
        layout.setConstraints(filledCheckBox,constraints);


        gradientCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isGradient = !isGradient;
                secondColorButton.setEnabled(isGradient);
            }
        });
        add(gradientCheckBox);
        constraints.gridx = 0;constraints.gridy = 1;constraints.gridheight = 1;constraints.gridwidth = 4;
        layout.setConstraints(gradientCheckBox,constraints);


        secondColorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color color = JColorChooser.showDialog(getParent(), "Color", secondColor);
                if(color != null)
                    SketchUtilAttributePanel.this.secondColor = color;
            }
        });
        secondColorButton.setEnabled(isGradient);
        add(secondColorButton);
        constraints.gridx = 0;constraints.gridy = 3;constraints.gridheight = 1;constraints.gridwidth = 4;
        layout.setConstraints(secondColorButton,constraints);

        add(lineWidthLabel);
        constraints.gridx = 0;constraints.gridy = 4;constraints.gridheight = 1;constraints.gridwidth = 1;
        layout.setConstraints(lineWidthLabel,constraints);

        lineWidthTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                char key = e.getKeyChar();
                String s = lineWidthTextField.getText();
                if(Character.isDigit(key))
                    s += key;
                else if(key == 8)
                    s = s.substring(0,s.length());

                lineWidthTextField.setText(s);
                if (s.length() > 0)
                    lineWidth = Integer.parseInt(s);
                else
                    lineWidth = 1;
                e.setKeyChar('\0');
            }
        });
        add(lineWidthTextField);
        constraints.gridx = 1;constraints.gridy = 4;constraints.gridheight = 1;constraints.gridwidth = 3;
        layout.setConstraints(lineWidthTextField,constraints);



        add(dashLengthLabel);
        constraints.gridx = 0;constraints.gridy = 5;constraints.gridheight = 1;constraints.gridwidth = 1;
        layout.setConstraints(dashLengthLabel,constraints);
        dashLengthTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                char key = e.getKeyChar();
                String s = dashLengthTextField.getText();
                if(Character.isDigit(key))
                    s += key;
                else if(key == 8)
                    s = s.substring(0,s.length());

                dashLengthTextField.setText(s);
                if (s.length() > 0)
                    dashedLength = Integer.parseInt(s);
                else
                    dashedLength = 0;
                e.setKeyChar('\0');
            }
        });

        dashLengthTextField.setEnabled(isDashed);
        add(dashLengthTextField);
        constraints.gridx = 1;constraints.gridy = 5;constraints.gridheight = 1;constraints.gridwidth = 3;
        layout.setConstraints(dashLengthTextField,constraints);
        setLayout(layout);

        dashedCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isDashed = !isDashed;
                dashLengthTextField.setEnabled(isDashed);
            }
        });
        add(dashedCheckBox);
        constraints.gridx = 0;constraints.gridy = 6;constraints.gridheight = 1;constraints.gridwidth = 4;
        layout.setConstraints(dashedCheckBox,constraints);
    }

}