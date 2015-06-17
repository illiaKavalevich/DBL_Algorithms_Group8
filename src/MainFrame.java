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

//COMMENT ALL JFREE STUFF BEFORE SUBMITTING TO PEACH
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

    int numLabels;
    ArrayList<Point> points = new ArrayList<>();
    Iterator<Point> iter;
    Algorithm alg;
    Quadtree2 q;
    ConflictDetector cD;
    Timer timer;

    //COMMENT BEFORE SUBMITTING TO PEACH
    int n = 10;
    int maxGrid = 20;
    //SET EMPTY BEFORE SUBMITTING TO PEACH, aka remove '= "..."'
    String model = "4pos";
    int numPoints = n;
    int w = 40;
    int h = 3;

    public void readInput() {
        //Set<Thread> threadSet = Thread.getAllStackTraces().keySet();

        //only used for testing purposes to create random points
        //COMMENT BEFORE SUBMITTING TO PEACH
        int pointsPlaced = 0;
        while (pointsPlaced < n) {
            Random rand = new Random();
            int x = rand.nextInt(maxGrid + 1);
            Random rand2 = new Random();
            int y = rand2.nextInt(maxGrid + 1);
            boolean alreadyExists = false;
            for (Point p : points) {
                if (p.xCoord == x && p.yCoord == y) {
                    alreadyExists = true;
                    break;
                }
            }
            if (!alreadyExists) {
                pointsPlaced++;
                if (model.equals("1slider")) {
                    Point point = new SliderPoint(x, y, model, w, h);
                    points.add(point);
                } else {
                    Point point = new PosPoint(x, y, model, w, h);
                    points.add(point);
                }
            }
        }
//        for(int i = 0;i<n;i++){
//            for(int j=0;j<n;j++){
//                Random rand = new Random();
//            int x = rand.nextInt(n);
//            Random rand2 = new Random();
//            int y = rand2.nextInt(n);
//                Point point = new PosPoint(5*i, 5*j, model, w, h);
//                points.add(point);
//            }
//        }
        // used for PEACH, UN-COMMENT BEFORE SUBMITTING TO PEACH
//        int firstPoint;
//        int secondPoint;
//        Scanner input = new Scanner(System.in);
//        //pattern used to skip input "...: "
//        String template = "\\n*[a-zA-Z\\s]*[^\\w\\s]\\s*";
//
//        input.skip(template);
//        model = input.next();
//        input.skip(template);
//        w = input.nextInt();
//        input.skip(template);
//        h = input.nextInt();
//        input.skip(template);
//        numPoints = input.nextInt();
//        if (model.equals("2pos") || model.equals("4pos")) {
//            while (input.hasNext()) {
//                firstPoint = input.nextInt();
//                secondPoint = input.nextInt();
//                points.add(new PosPoint(firstPoint, secondPoint, model, w, h));
//            }
//        } else if (model.equals("1slider")) {
//            while (input.hasNext()) {
//                firstPoint = input.nextInt();
//                secondPoint = input.nextInt();
//                points.add(new SliderPoint(firstPoint, secondPoint, model, w, h));
//            }
//        } else {
//            System.out.println("MainFrame.readInput: no valid model provided");
//        }
        Algorithm timerAlg = new Falp();
        if (model.equals("2pos")) {
            alg = numPoints <= 10 ? new ExhaustiveSearch(): timerAlg;
            if (numPoints == 10000) {
                timer = new Timer(160, timerAlg);
            } else {
                timer = new Timer(200, timerAlg);
            }

        } else if (model.equals("4pos")) {
            alg = numPoints <= 10 ? new ExhaustiveSearch(): timerAlg;
            if (numPoints == 10000) {
                timer = new Timer(125, timerAlg);
            } else {
                timer = new Timer(170, timerAlg);
            }

        } else if (model.equals("1slider")) {
            if (numPoints >= 250) {
                alg = new AnnealingSimulatorSlider(null);
            } else {
                alg = new ClaimFreeDecorator(new AnnealingSimulatorSlider(new Falp()));
            }
            timer = new Timer(170, timerAlg);
            timerAlg = alg;
        } else {
            System.out.println(model + " is not a valid model");
        }

        timer.start();

        q = new Quadtree2();
        cD = new ConflictDetector(points, model, q);
//        for (Point p : points) {
//            for (Label l : p.possibleLabels) {
//                q.insertLabel(l);
//            }
//        }
        //alg = new AnnealingSimulatorSlider();
        alg.setParameters(w, h, points, cD, timer, model);
//        alg.moveLabelRandomly(point1);
//        System.out.println(cD.getActDegree(point1.getActiveLabelSlider()));
//        System.out.println(cD.getActDegree(point2.getActiveLabelSlider()));

        processOutput();

        //COMMENT BEFORE SUBMITTING TO PEACH
        plotLabels();
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
                boolean hasActive = false;
                curPoint = iter.next();
                for (Label label : curPoint.possibleLabels) {
                    PosLabel label2 = (PosLabel) label;
                    if (label2.active == true) {
                        hasActive = true;
                        System.out.println(curPoint.getxCoord() + " " + curPoint.getyCoord()
                                + " " + label2.getPlacement());
                        break;
                    }
                }
                if (hasActive == false) {
                    System.out.println(curPoint.getxCoord() + " " + curPoint.getyCoord()
                            + " NIL");
                }
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
    }

    /*
     Used for data visualization
     COMMENT BEFORE SUBMITTING TO PEACH
     */
    public void plotLabels() {
        int count = 1;
        XYDataset label = null;
        Label l = null;
        boolean hasActive = false;

        //sets basics for graph display and plot all points
        XYDataset plotPoints = createPointDataSet();
        XYLineAndShapeRenderer rendererP = new XYLineAndShapeRenderer(false, true);
        ValueAxis domain = new NumberAxis("");
        ValueAxis range = new NumberAxis("");
        XYPlot plot = new XYPlot(plotPoints, domain, range, rendererP);

        /* for each point plot the active label
         if it has a conflict with >=1 other active label(s) make the label red
         otherwise black
         */
        for (Point point : points) {
            if (model.equals("1slider")) {
                hasActive = true;
                label = createSliderLabelDataSet(point.getActiveLabelSlider());
                l = point.getActiveLabelSlider();
            } else {
                for (Label curLabel : point.possibleLabels) {
                    PosLabel curLabel2 = (PosLabel) curLabel;
                    if (curLabel2.active == true) {
                        hasActive = true;
                        label = createPosLabelDataSet(curLabel2);
                        l = curLabel2;
                    }
                }

            }
            if (hasActive == true) {
                XYLineAndShapeRenderer rendererL = new XYLineAndShapeRenderer(true, false);
                if (cD.getActDegree(l) == 0) {
                    rendererL.setSeriesPaint(0, Color.BLACK);
                } else {
                    rendererL.setSeriesPaint(0, Color.RED);
                }
                rendererL.setSeriesVisibleInLegend(Boolean.FALSE);
                plot.setDataset(count, label);
                plot.setRenderer(count, rendererL);
                count++;
            }
        }
        //draw the graph
        JFreeChart chart = new JFreeChart("Label Placement", JFreeChart.DEFAULT_TITLE_FONT, plot, true);
        ChartFrame frame = new ChartFrame("DBL Algorithms", chart);
        frame.pack();
        frame.setVisible(true);
    }

    /*
     Used for data visualization
     COMMENT BEFORE SUBMITTING TO PEACH
     */
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

    /*
     Used for data visualization
     COMMENT BEFORE SUBMITTING TO PEACH
     */
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

    /*
     Used for data visualization
     COMMENT BEFORE SUBMITTING TO PEACH
     */
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
