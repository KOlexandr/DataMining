package ua.edu.cdu.appliedMathematics.modeling.spring;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PlotSimpleGraphic {

    private JFrame jFrame;
    private static final int OFFSET = 5;
    private static final int FRAME_WIDTH = 600;
    private static final int FRAME_HEIGHT = 500;
    private static final int MAX_GRAPHIC_WIDTH = 600;
    private static final int MAX_GRAPHIC_HEIGHT = 300;

    private JTextField mass;
    private JTextField step;
    private JTextField allSteps;
    private JTextField initialDeviation;
    private JTextField coefficientOfStiffnessOfSpring;

    /**
     * Creates main JFrame of program
     */
    public void view() {
        if(jFrame == null){
            jFrame = new JFrame("Spring Euler");
            jFrame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
            jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            jFrame.add(initializeFields(), BorderLayout.NORTH);
            final JPanel jPanel = new JPanel();
            jPanel.add(createButtons(), BorderLayout.NORTH);
            jFrame.add(jPanel);

            jFrame.setVisible(true);
            jFrame.setResizable(false);
        }
    }

    /**
     * Creates JPanel with JTextField`s for values of variables
     */
    private JPanel initializeFields(){
        final JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(6, 2));

        initialDeviation = new JTextField(5);
        jPanel.add(new JLabel("Initial Deviation ="));
        jPanel.add(initialDeviation);

        coefficientOfStiffnessOfSpring = new JTextField(5);
        jPanel.add(new JLabel("Coefficient of Stiffness of Spring = "));
        jPanel.add(coefficientOfStiffnessOfSpring);

        mass = new JTextField(5);
        jPanel.add(new JLabel("Mass = "));
        jPanel.add(mass);

        step = new JTextField(5);
        jPanel.add(new JLabel("Step (dt) = "));
        jPanel.add(step);

        allSteps = new JTextField(5);
        jPanel.add(new JLabel("All Steps (count) = "));
        jPanel.add(allSteps);
        return jPanel;
    }

    /**
     * Creates button which initializes or change values of variables
     */
    private JPanel createButtons(){
        final JPanel createButton = new JPanel();
        createButton.setLayout(new GridLayout(1, 1));
        final JButton setValuesButton = new JButton("Calculate result");
        final JButton clearButton = new JButton("Clear");
        setValuesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                runSpringCalculate();
            }
        });
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                plotBackground(jFrame.getGraphics());
            }
        });
        createButton.add(setValuesButton);
        createButton.add(clearButton);
        return createButton;
    }

    private void runSpringCalculate() {
        final double initialDeviationVal = setDoubleValue(initialDeviation.getText());
        final double coeff = setDoubleValue(coefficientOfStiffnessOfSpring.getText());
        final double massVal = setDoubleValue(mass.getText());
        final double stepVal = setDoubleValue(step.getText());
        final int allStepsVal = setIntValue(allSteps.getText());
        if(initialDeviationVal == Double.MIN_VALUE || coeff <= 0 ||
                massVal <= 0 || stepVal <= 0 || allStepsVal <= 0){
            JOptionPane.showMessageDialog(null, "Wrong input data, please change.\nProgram will run " +
                    "with default data!!!", "ERROR!!!", JOptionPane.WARNING_MESSAGE);
            Spring.springEuler(6, 5, 10, 0.001, 40000);
        } else {
            Spring.springEuler(initialDeviationVal, coeff, massVal, stepVal, allStepsVal);
        }
        plot(jFrame.getGraphics(), Spring.getCoordinates(), Spring.getTime());
    }

    /**
     * transform data to integer values for plot in JFrame
     * @param graphics
     * @param coordinates - coordinates of ua.edu.cdu.appliedMathematics.dm.ua.edu.cdu.appliedMathematics.modeling.spring
     * @param time - time of moving ua.edu.cdu.appliedMathematics.dm.ua.edu.cdu.appliedMathematics.modeling.spring
     */
    public void plot(final Graphics graphics, final double[] coordinates, final double[] time){
        final int size = coordinates.length;
        final int coordinatesInt[] = new int[size];
        final int timeInt[] = new int[size];
        for(int i = 0; i < size; i++){
            coordinatesInt[i] = (int)(coordinates[i]*100);
            timeInt[i] = (int)(time[i]*100);
        }
        final int minCoordinates = min(coordinatesInt);
        final int minTime = min(timeInt);
        final int maxCoordinates = max(coordinatesInt);
        final int maxTime = max(timeInt);
        if(minCoordinates < 0){
            for(int i = 0; i < size; i++){
                coordinatesInt[i] = (coordinatesInt[i] + Math.abs(minCoordinates));
            }
        }
        if(minTime < 0){
            for(int i = 0; i < size; i++){
                timeInt[i] = (timeInt[i] + Math.abs(minTime));
            }
        }
        final int xMove = FRAME_HEIGHT - OFFSET;
        final double tmpTime = (double)Math.abs(minTime - maxTime) / MAX_GRAPHIC_WIDTH;
        final double tmpCoordinates = (double)Math.abs(minCoordinates - maxCoordinates) / MAX_GRAPHIC_HEIGHT;
        for(int i = 0; i < size; i++){
            timeInt[i] = (int)(timeInt[i]/tmpTime);
            coordinatesInt[i] = xMove - (int)(coordinatesInt[i]/tmpCoordinates);
        }

        plotBackground(graphics);

        graphics.setColor(Color.red);
        for(int i = 0; i < size-1; i++){
            graphics.drawLine(timeInt[i], coordinatesInt[i], timeInt[i + 1], coordinatesInt[i + 1]);
        }
    }

    /**
     * finding min value in array
     * @param array
     * @return
     */
    private static int min(final int[] array){
        int min = Integer.MAX_VALUE;
        for (final int anArray : array) {
            if (min > anArray) {
                min = anArray;
            }
        }
        return min;
    }

    /**
     * finding max value in array
     * @param array
     * @return
     */
    private static int max(final int[] array){
        int max = Integer.MIN_VALUE;
        for (final int anArray : array) {
            if (max < anArray) {
                max = anArray;
            }
        }
        return max;
    }

    /**
     * Method gets text from TextField and parse it as double value
     * @return double number
     */
    private static double setDoubleValue(final String str){
        double value = Double.MIN_VALUE;
        try{
            value = Double.parseDouble(str);
        } catch(NumberFormatException ignored){}
        return value;
    }

    /**
     * Method gets text from TextField and parse it as integer value
     * @return integer number
     */
    private static int setIntValue(final String str){
        int value = Integer.MIN_VALUE;
        try{
            value = Integer.parseInt(str);
        } catch(NumberFormatException ignored){}
        return value;
    }

    /**
     * plot background for some chart
     * @param graphics
     */
    private static void plotBackground(final Graphics graphics){
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, FRAME_HEIGHT - MAX_GRAPHIC_HEIGHT - OFFSET, FRAME_WIDTH, FRAME_HEIGHT);

        graphics.setColor(Color.BLACK);
        final int timeStepForPlotAxisVertical = FRAME_WIDTH / 8;
        for(int i = 0; i < FRAME_WIDTH; i+= timeStepForPlotAxisVertical){
            graphics.drawLine(i, FRAME_HEIGHT-MAX_GRAPHIC_HEIGHT-OFFSET, i, FRAME_HEIGHT);
        }
        final int timeStepForPlotAxisHorizontal = (MAX_GRAPHIC_HEIGHT+OFFSET) / 8;
        for(int i = FRAME_HEIGHT-MAX_GRAPHIC_HEIGHT-OFFSET; i < FRAME_HEIGHT; i+= timeStepForPlotAxisHorizontal){
            graphics.drawLine(0, i, FRAME_WIDTH, i);
        }
    }
}