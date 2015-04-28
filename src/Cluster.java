/*
 * This class provides an interface to create clusters
 * A cluster is a collection of points scattered around a central point in the plane
 *
 */

import java.util.Random;
import java.util.ArrayList;

/**
 *
 * @author s106010
 */
public class Cluster {
    int stdDev;                         //stddev in both directions of the 2D gaussian distribution
    int[] origin = new int[2];          //2D origin (basically: 2 dimensional int of the means in x and y direction)
    Random random = new Random();
    ArrayList<Point> pointCollection = new ArrayList<>();
    
    /** 
    * Constructor
     * @param s
     * @param o
    */
    
    public Cluster(int s, int[] o){
        stdDev = s;
        origin = o;
        pointCollection.add(new Point(0,0));
    }

    /**
    * Getter for the standard deviation
    * @return 
    */
    public int getStdDev() {
        return stdDev;
    }
    
    /**
    * Getter for origin of the cluster
    * @return 
    */
    public int[] getOrigin() {
        return origin;
    }
    
    /**
    * Getter for the point collection
    * @return 
    */
    public ArrayList<Point> getPointCollection() {
        return pointCollection;
    }

    /**
    * 
    *
    */
    public void generatePoint() {
        double xCoord = random.nextGaussian() * stdDev + origin[0];
        double yCoord = random.nextGaussian() * stdDev + origin[1];
        pointCollection.add(new Point(xCoord, yCoord));
    }
    
}
