/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import org.jfree.chart.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickMarkPosition;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author s131061
 */
public class MainFrame {

    int w;
    int h;
    int numPoints;
    int numLabels;
    ArrayList<Point> points = new ArrayList<>();
    Iterator<Point> iter;
    Algorithm alg;
    //ConflictList cL;
    Quadtree q;
    ConflictDetector cD;
    Timer timer;
    
    int n = 5;
    int maxGrid = 10;
    String model = "2pos";
    
    public void readInput() {
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        System.out.println("Number of threads that are still running: "+threadSet.size());
        //model = "4pos";
        w = 10;
        h = 10;
        
        for (int i = 0; i < n; i++) {
            Random rand = new Random();
            int x = rand.nextInt(maxGrid + 1);
            Random rand2 = new Random();
            int y = rand2.nextInt(maxGrid + 1);
            if (model.equals("1slider")) {
                Point point = new SliderPoint(x, y, model, w, h);
                points.add(point);
            } else {
                Point point = new PosPoint(x, y, model, w, h);
                points.add(point);
            }

        }
//        for (int i = 0; i < 23; i++) {
//            for (int j = 0; j < 23; j++) {
//                Random rand = new Random();
//                 int x =rand.nextInt(12);
//                 Random rand2 = new Random();
//                 int y =rand2.nextInt(12);
//                 
//                Point point = new PosPoint(i, j, model, w, h);
//                points.add(point);
//            }
//        }
        //Point point1 = new PosPoint(4, 4, model, w, h);
//            Point point2 = new PosPoint(3, 4, model, w, h);
//            Point point3 = new PosPoint(4, 6, model, w, h);
//            Point point4 = new PosPoint(4, 5, model, w, h);
        //Point point5 = new PosPoint(5, 5, model, w, h);
//        Point point1 = new PosPoint(5, 4, model, w, h);
//            Point point2 = new PosPoint(2, 3, model, w, h);
//            Point point3 = new PosPoint(1, 6, model, w, h);
//            Point point4 = new PosPoint(10, 12, model, w, h);
//        Point point5 = new PosPoint(4, 4, model, w, h);
//            Point point6 = new PosPoint(3, 3, model, w, h);
//            Point point7 = new PosPoint(2, 6, model, w, h);
//            Point point8 = new PosPoint(11, 12, model, w, h);
//            Point point9 = new PosPoint(2, 4, model, w, h);
//            Point point10 = new PosPoint(1, 5, model, w, h);

        //slider points
//            Point point1 = new SliderPoint(5, 4, model, w, h);
//            Point point2 = new SliderPoint(2, 3, model, w, h);
//            Point point3 = new SliderPoint(1, 6, model, w, h);
//            Point point4 = new SliderPoint(10, 12, model, w, h);
//            Point point5 = new SliderPoint(4, 4, model, w, h);
//            Point point6 = new SliderPoint(3, 3, model, w, h);
//            Point point7 = new SliderPoint(2, 6, model, w, h);
//            Point point8 = new SliderPoint(11, 12, model, w, h);
//            Point point9 = new SliderPoint(2, 4, model, w, h);
////            Point point10 = new SliderPoint(1, 5, model, w, h);
        //points.add(point1);
//        points.add(point2);
//        points.add(point3);
//        points.add(point4);
        //points.add(point5);
//            points.add(point6);
//            points.add(point7);
//            points.add(point8);
//            points.add(point9);
//            points.add(point10);
        if (model.equals("2pos")) {
            alg = new Falp();
        } else if (model.equals("4pos")) {
            alg = new Falp();
        } else if (model.equals("1slider")) {
            alg = new AnnealingSimulatorSlider();
        } else {
            System.out.println(model + " is not a valid model");
        }
        timer = new Timer(3, alg);
        timer.start();
        
        //cL = new ConflictList(points, model);
        q = new Quadtree();
        cD = new ConflictDetector(points, model, q);
//        for (Point p : points) {
//            for (Label l : p.possibleLabels) {
//                q.insertLabel(l);
//            }
//        }
        alg.setParameters(w, h, points, cD, timer, model);
        int firstPoint;
        int secondPoint;
        /*Scanner input = new Scanner(System.in);
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
         }*/
        processOutput();
    }

    public void processOutput() {
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
                boolean curPointActive = curPoint.getActiveLabelSlider().active;
                if (curPointActive) {
                    System.out.println(curPoint.getxCoord() + " " + curPoint.getyCoord()
                            + " " + curPoint.getActiveLabelSlider().getPlacement());
                } else {
                    System.out.println(curPoint.getxCoord() + " " + curPoint.getyCoord()
                            + " NIL");
                }
            }
        } else {
            System.out.println("MainFrame.giveOutput: no valid model provided");
        }
        plotLabels();
    }

    /*
     Used for data visualization
     COMMENT BEFORE SUBMITTING TO PEACH
     */
    public void plotLabels() {
        int count = 1;
        XYDataset label;
        Label l;

        //sets basics for graph display and plot all points
        XYDataset plotPoints = createPointDataSet();
        XYLineAndShapeRenderer rendererP = new XYLineAndShapeRenderer(false, true);
        ValueAxis domain = new NumberAxis("");
        ValueAxis range = new NumberAxis("");
        XYPlot plot = new XYPlot(plotPoints, domain, range, rendererP);

        for (Point point : points) {
            if (model.equals("1slider")) {
                label = createSliderLabelDataSet(point.getActiveLabelSlider());
                l = point.getActiveLabelSlider();
            } else {
                label = createPosLabelDataSet(point.getActiveLabelPos());
                l = point.getActiveLabelPos();
            }

            XYLineAndShapeRenderer rendererL = new XYLineAndShapeRenderer(true, false);
            if (cD.getActDegree(l) == 0) {
                System.out.println("no conflict");
                rendererL.setSeriesPaint(0, Color.BLACK);
            } else {
                System.out.println("conflict");
                rendererL.setSeriesPaint(0, Color.RED);
            }
            rendererL.setSeriesVisibleInLegend(Boolean.FALSE);
            plot.setDataset(count, label);
            plot.setRenderer(count, rendererL);
            count++;
        }

        /* for each point plot the active label
         if it has a conflict with >=1 other active label(s) make the label red
         otherwise black
         */
//        for (Point point : points) {
//            if (model.equals("1slider")) {
//                label = createSliderLabelDataSet(point.getActiveLabelSlider());
//                XYLineAndShapeRenderer rendererL = new XYLineAndShapeRenderer(true, false);
//                if (cD.getActConflictLabels.containsKey(point.getActiveLabelSlider())) {
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
//                if (cD.getActConflictLabels.containsKey(point.getActiveLabelPos())) {
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

        //draw the graph
        JFreeChart chart = new JFreeChart("Label Placement", JFreeChart.DEFAULT_TITLE_FONT, plot, true);
        ChartFrame frame = new ChartFrame("DBL Algorithms", chart);
        frame.pack();
        frame.setVisible(true);
    }

    //create a data set containing all the points
    private XYDataset createPointDataSet() {
        XYSeriesCollection plotPoints = new XYSeriesCollection();
        XYSeries series = new XYSeries("Points");
        for (Point point : points) {
            series.add(point.getxCoord(), point.getyCoord());
        }
        plotPoints.addSeries(series);
        return plotPoints;
    }

    //create a data set containing the four points of the PosLabel
    private XYDataset createPosLabelDataSet(PosLabel label) {
        XYSeriesCollection currentLabel = new XYSeriesCollection();
        XYSeries series = new XYSeries("", false, true);
        series.add(label.minX, label.minY);
        series.add(label.minX, label.maxY);
        series.add(label.maxX, label.maxY);
        series.add(label.maxX, label.minY);
        series.add(label.minX, label.minY);
        currentLabel.addSeries(series);
        return currentLabel;
    }

    //create a data set containing the four points of the SliderLabel
    private XYDataset createSliderLabelDataSet(SliderLabel label) {
        XYSeriesCollection currentLabel = new XYSeriesCollection();
        XYSeries series = new XYSeries("", false, true);
        double minX = label.miniX;
        double maxX = label.maxiX;
        if (label.active) {
            series.add(minX, label.minY);
            series.add(minX, label.maxY);
            series.add(maxX, label.maxY);
            series.add(maxX, label.minY);
            series.add(minX, label.minY);
        }
        currentLabel.addSeries(series);
        return currentLabel;
    }

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
