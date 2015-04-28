import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import org.jfree.chart.ChartPanel; 
import org.jfree.chart.JFreeChart; 
import org.jfree.data.xy.XYSeries; 
import java.util.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeriesCollection;

public class PlotExample extends JPanel {
    public PlotExample() {
        
        super();
        
        XYSeries series = new XYSeries("Planned"); //create chart dataset

        int[] origin = {2000,2000}; //set the origin of the cluster in the plane
        Cluster cluster = new Cluster(100000,origin); //create the cluster

        int i = 0;
        //generate Gaussian points (see: Cluster.java)
        while(i < 1000) {
            cluster.generatePoint(); 
            i++;
        }

        ArrayList<Point> pointCollection = cluster.getPointCollection(); //fetch points 

        pointCollection.stream().forEach((point) -> {
            series.add(point.getxCoord(), point.getyCoord()); //add points to chart series
        });  

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        JFreeChart chart = ChartFactory.createScatterPlot("Gaussian scatterplot", "X","Y", dataset);
        XYPlot plot = (XYPlot)chart.getPlot();
        ChartPanel chartPanel = new ChartPanel(chart);

        add(chartPanel);
    }

    public static void main(String[] args)
    {
    JFrame frame = new JFrame("Scatterplot");

    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    frame.getContentPane().add(new PlotExample(), BorderLayout.CENTER);
    frame.pack();
    frame.setVisible(true);
    }
}