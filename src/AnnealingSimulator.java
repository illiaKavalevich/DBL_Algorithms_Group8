
import java.util.ArrayList;
import java.lang.Math;
import java.util.Random;

/**
 * Date last modified: 30-4-2015
 * @author Mark Bouwman
 * 
 * Simulated Annealing is used in this class to solve the Point Feature Label
 * Placement problem using heuristics. In the first step of the algorithm all
 * labels are placed at random (or an initial labeling is given). Then it will 
 * continue by repositioning labels at random and seeing what the effect is. 
 * The annealing temperature is reduced gradually to 'settle' the solution.
 */
public abstract class AnnealingSimulator extends Algorithm {
    
    protected double T; //temperature
    protected double E; //score of the current itteration
    protected double deltaE; //the change in score
    protected int iterations;
    protected Random rand;

    public AnnealingSimulator() {
        this.rand = new Random(56467984);
        this.T = 2.50;
    }
    
    public void setTemperature(double temperature) {
        this.T = temperature;
    }
    
    public void determineLabels(int w, int h, ArrayList<Point> points) {
        Point point; //current point being altered
        doInitialPlacement(points);
        while(true) { //TO DO: determine when to stop treshold or time
            point = points.get(rand.nextInt(points.size()));
            moveLabelRandomly(point);
            computeDeltaE();
            if(deltaE < 0) {
                if(rand.nextDouble() <= (1 - Math.pow(Math.E, -(deltaE/T)))) {
                //undo
                }
            }
            //TO DO: lower temperature systematically
        }
    }
    
    protected void doInitialPlacement(ArrayList<Point> points){
        for(int i = 0; i < points.size(); i++) {
            if(points.get(i).getActiveLabelSlider() == null) { //NEEDS UPDATE
                moveLabelRandomly(points.get(i));
            }
        }
    }
    
    protected abstract void moveLabelRandomly(Point p);
    protected abstract void computeDeltaE();

}
