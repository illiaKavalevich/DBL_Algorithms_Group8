/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
//import org.jfree.chart.*;
//import org.jfree.chart.ChartFactory;
//import org.jfree.chart.JFreeChart;
//import org.jfree.chart.axis.DateAxis;
//import org.jfree.chart.axis.DateTickMarkPosition;
//import org.jfree.chart.axis.NumberAxis;
//import org.jfree.chart.axis.ValueAxis;
//import org.jfree.chart.plot.PlotOrientation;
//import org.jfree.chart.plot.XYPlot;
//import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
//import org.jfree.data.xy.DefaultXYDataset;
//import org.jfree.data.xy.XYDataset;
//import org.jfree.data.xy.XYSeries;
//import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author s131061
 */
public class MainFrame {

    String model;
    int w;
    int h;
    int numPoints;
    int numLabels;
    ArrayList<Point> points = new ArrayList<>();
    Iterator<Point> iter;
    Algorithm alg;
    ConflictList cL;
    Timer timer;

    public void readInput() {
//        model = "2pos";
//        w = 2;
//        h = 2;
//        Point point1 = new PosPoint(5, 4, model, w, h);
//        Point point2 = new PosPoint(2, 3, model, w, h);
//        Point point3 = new PosPoint(1, 6, model, w, h);
//        Point point4 = new PosPoint(10, 12, model, w, h);
//        points.add(point1);
//        points.add(point2);
//        points.add(point3);
//        points.add(point4);
//        
        timer = new Timer(250);
        int firstPoint;
        int secondPoint;
        Scanner input = new Scanner(System.in);
        //pattern used to skip input "...: "
        String template = "\\n*[a-zA-Z\\s]*[^\\w\\s]\\s*";

        input.skip(template);
        model = input.next();
        input.skip(template);
        w = input.nextInt();
        input.skip(template);
        h = input.nextInt();
        input.skip(template);
        numPoints = input.nextInt();
        if (model.equals("2pos") || model.equals("4pos")) {
            while (input.hasNext()) {
                firstPoint = input.nextInt();
                secondPoint = input.nextInt();
                points.add(new PosPoint(firstPoint, secondPoint, model, w, h));
            }
        } else if (model.equals("1slider")) {
            while (input.hasNext()) {
                firstPoint = input.nextInt();
                secondPoint = input.nextInt();
                points.add(new SliderPoint(firstPoint, secondPoint, model, w, h));
            }
        } else {
            System.out.println("MainFrame.readInput: no valid model provided");
        }
        cL = new ConflictList(points, model);
        processOutput();
    }

    public void processOutput() {
        if (model.equals("2pos")) {
            alg = new Falp();
        } else if (model.equals("4pos")) {
            alg = new Falp();
        } else if (model.equals("1slider")) {
            alg = new Falp();
        } else {
            System.out.println(model + " is not a valid model");
        }
        alg.setParameters(w, h, points, cL, timer);
        alg.determineLabels();
        numLabels = alg.getNumLabels();
        giveOutput();

    }

    public void giveOutput() {

        //stores the current point being visited by the iterator
        Point curPoint;

        //print the required information
        System.out.println("placement model: " + model);
        System.out.println("width: " + w);
        System.out.println("height: " + h);
        System.out.println("number of points: " + numPoints);
        System.out.println("number of labels: " + numLabels);

        //creates new iterator to iterate over all points
        iter = points.iterator();

        //loop over all points to print their values
        if (model.equals("2pos") || model.equals("4pos")) {
            while (iter.hasNext()) {
                curPoint = iter.next();
                System.out.println(curPoint.getxCoord() + " " + curPoint.getyCoord()
                        + " " + curPoint.getActiveLabelPos().getPlacement());
            }
        } else if (model.equals("1slider")) {
            while (iter.hasNext()) {
                curPoint = iter.next();
                System.out.println(curPoint.getxCoord() + " " + curPoint.getyCoord()
                        + " " + curPoint.getActiveLabelSlider().getPlacement());
            }
        } else {
            System.out.println("MainFrame.giveOutput: no valid model provided");
        }

    }

    /*
     Used for data visualization
     COMMENT BEFORE SUBMITTING TO PEACH
     */
//    public void plotLabels() {
//        int count = 1;
//        XYDataset label;
//
//        //sets basics for graph display and plot all points
//        XYDataset plotPoints = createPointDataSet();
//        XYLineAndShapeRenderer rendererP = new XYLineAndShapeRenderer(false, true);
//        ValueAxis domain = new NumberAxis("");
//        ValueAxis range = new NumberAxis("");
//        XYPlot plot = new XYPlot(plotPoints, domain, range, rendererP);
//
//        for (Point point : points) {
//            if (model.equals("1slider")) {
//                label = createSliderLabelDataSet(point.getActiveLabelSlider());
//            } else {
//                label = createPosLabelDataSet(point.getActiveLabelPos());
//            }
//
//            XYLineAndShapeRenderer rendererL = new XYLineAndShapeRenderer(true, false);
//            if (cL.actConflict.get(label) == null) {
//                System.out.println("no conflict");
//                System.out.println(cL.actConflict.get(label));
//                rendererL.setSeriesPaint(0, Color.BLACK);
//            } else {
//                System.out.println("conflict");
//                rendererL.setSeriesPaint(0, Color.RED);
//            }
//            rendererL.setSeriesVisibleInLegend(Boolean.FALSE);
//            plot.setDataset(count, label);
//            plot.setRenderer(count, rendererL);
//            count++;
//        }
//
//        /* for each point plot the active label
//         if it has a conflict with >=1 other active label(s) make the label red
//         otherwise black
//         */
//        for (Point point : points) {
//            if (model.equals("1slider")) {
//                label = createSliderLabelDataSet(point.getActiveLabelSlider());
//                XYLineAndShapeRenderer rendererL = new XYLineAndShapeRenderer(true, false);
//                if (cL.actConflict.containsKey(point.getActiveLabelSlider())) {
//                    rendererL.setSeriesPaint(0, Color.RED);
//                } else {
//                    rendererL.setSeriesPaint(0, Color.BLACK);
//                }
//                rendererL.setSeriesVisibleInLegend(Boolean.FALSE);
//                plot.setDataset(count, label);
//                plot.setRenderer(count, rendererL);
//            } else {
//                label = createPosLabelDataSet(point.getActiveLabelPos());
//                XYLineAndShapeRenderer rendererL = new XYLineAndShapeRenderer(true, false);
//                if (cL.actConflict.containsKey(point.getActiveLabelPos())) {
//                    rendererL.setSeriesPaint(0, Color.RED);
//                } else {
//                    rendererL.setSeriesPaint(0, Color.BLACK);
//                }
//                rendererL.setSeriesVisibleInLegend(Boolean.FALSE);
//                plot.setDataset(count, label);
//                plot.setRenderer(count, rendererL);
//            }
//            count++;
//        }
//
//        //draw the graph
//        JFreeChart chart = new JFreeChart("Label Placement", JFreeChart.DEFAULT_TITLE_FONT, plot, true);
//        ChartFrame frame = new ChartFrame("DBL Algorithms", chart);
//        frame.pack();
//        frame.setVisible(true);
//    }
//
//    //create a data set containing all the points
//    private XYDataset createPointDataSet() {
//        XYSeriesCollection plotPoints = new XYSeriesCollection();
//        XYSeries series = new XYSeries("Points");
//        for (Point point : points) {
//            series.add(point.getxCoord(), point.getyCoord());
//        }
//        plotPoints.addSeries(series);
//        return plotPoints;
//    }
//
//    //create a data set containing the four points of the PosLabel
//    private XYDataset createPosLabelDataSet(PosLabel label) {
//        XYSeriesCollection currentLabel = new XYSeriesCollection();
//        XYSeries series = new XYSeries("", false, true);
//        series.add(label.minX, label.minY);
//        series.add(label.minX, label.maxY);
//        series.add(label.maxX, label.maxY);
//        series.add(label.maxX, label.minY);
//        series.add(label.minX, label.minY);
//        currentLabel.addSeries(series);
//        return currentLabel;
//    }
//
//    //create a data set containing the four points of the SliderLabel
//    private XYDataset createSliderLabelDataSet(SliderLabel label) {
//        XYSeriesCollection currentLabel = new XYSeriesCollection();
//        XYSeries series = new XYSeries("", false, true);
//        double minX = label.miniX;
//        double maxX = label.maxiX;
//        series.add(minX, label.minY);
//        series.add(minX, label.maxY);
//        series.add(maxX, label.maxY);
//        series.add(maxX, label.minY);
//        series.add(minX, label.minY);
//        currentLabel.addSeries(series);
//        return currentLabel;
//    }
    /**
     * @param args the command line arguments 
     *
     */
    public static void main(String[] args) {
        MainFrame mainFrame = new MainFrame();
        mainFrame.readInput();
//       
    }

}
