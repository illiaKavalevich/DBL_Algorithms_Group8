/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
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

    String model;
    int w;
    int h;
    int numPoints;
    int numLabels;
    ArrayList<Point> points = new ArrayList<>();
    Iterator<Point> iter;
    Algorithm alg;
    ConflictList cL;

    public void readInput() {
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
        giveOutput();
    }

    public void processOutput() {
        if (model.equals("2pos")) {
            alg = new TwoPos(w, h, points, cL);
        } else if (model.equals("4pos")) {
            alg = new FourPos(w, h, points, cL);
        } else if (model.equals("1slider")) {
            alg = new OneSlider(w, h, points, cL);
        } else {
            System.out.println(model + " is not a valid model");
        }
        alg.determineLabels();
        numLabels = alg.getNumLabels();

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

    public void plotLabels() {
        //just for testing purposes
        model = "2pos";
        points.add(new PosPoint(12, 4, "2pos", 1, 1));
        points.add(new PosPoint(1, 16, "2pos", 1, 1));
        points.add(new PosPoint(7, 2, "2pos", 1, 1));
        points.add(new PosPoint(4, 11, "2pos", 1, 1));
        points.add(new PosPoint(5, 9, "2pos", 1, 1));
        
        int count = 1;
        XYDataset label;
        
        XYDataset plotPoints = createPointDataSet();
        XYLineAndShapeRenderer rendererP = new XYLineAndShapeRenderer(false, true);
        ValueAxis domain = new NumberAxis("");
        ValueAxis range = new NumberAxis("");
        XYPlot plot = new XYPlot(plotPoints, domain, range, rendererP);
        
        for (Point point : points) {
            if (model.equals("1slider")) {
                label = createSliderLabelDataSet(point.getActiveLabelSlider());
            } else {
                point.getActiveLabelPos().setLabel(1);
                label = createPosLabelDataSet(point.getActiveLabelPos());
            }
            
            XYLineAndShapeRenderer rendererL = new XYLineAndShapeRenderer(true, false);
            rendererL.setSeriesPaint(0, Color.BLACK);
            rendererL.setSeriesVisibleInLegend(Boolean.FALSE);
            plot.setDataset(count, label);
            plot.setRenderer(count, rendererL);
            count++;
        }
        
        JFreeChart chart = new JFreeChart("Label Placement", JFreeChart.DEFAULT_TITLE_FONT, plot, true);
        ChartFrame frame = new ChartFrame("DBL Algorithms", chart);
        frame.pack();
        frame.setVisible(true);
    }

    private XYDataset createPointDataSet() {
        XYSeriesCollection plotPoints = new XYSeriesCollection();
        XYSeries series = new XYSeries("Points");
        for (Point point : points) {
            series.add(point.getxCoord(), point.getyCoord());
        }
        plotPoints.addSeries(series);
        return plotPoints;
    }

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
    
    private XYDataset createSliderLabelDataSet(SliderLabel label) {
        XYSeriesCollection currentLabel = new XYSeriesCollection();
        XYSeries series = new XYSeries("", false, true);
        double minX = label.miniX;
        double maxX = label.maxiX;
        series.add(minX, label.minY);
        series.add(minX, label.maxY);
        series.add(maxX, label.maxY);
        series.add(maxX, label.minY);
        series.add(minX, label.minY);
        currentLabel.addSeries(series);
        return currentLabel;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        MainFrame mainFrame = new MainFrame();
        //mainFrame.readInput();
        mainFrame.plotLabels();
    }

}
