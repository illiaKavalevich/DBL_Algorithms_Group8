
import java.util.ArrayList;
import java.lang.Math;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Date last modified: 20-5-2015
 * @author Mark Bouwman
 * 
 * Simulated Annealing is used in this class to solve the Point Feature Label
 * Placement problem using heuristics. In the first step of the algorithm all
 * labels are placed at random (or an initial labeling is given). Then it will 
 * continue by repositioning labels at random and seeing what the effect is. 
 * The annealing temperature is reduced gradually to 'settle' the solution.
 */
public abstract class AnnealingSimulator extends Algorithm {
    
    protected Point lastPoint;
    Set<Label> labelsAffectedLastChange = new HashSet();
    protected double T; //temperature
    protected int oldE; 
    protected int E; //score of the current itteration
    protected int deltaE; //the change in score
    protected int iterationsSinceTempChange;
    protected Random rand;
    public boolean complicatedScoring = true;

    public AnnealingSimulator(int w, int h, ArrayList<Point> points, 
            ConflictList cL) {
        super(w, h, points, cL);
        this.rand = new Random(56467984);
        this.T = 2.0;
    }
    
    public void setTemperature(double temperature) {
        this.T = temperature;
    }
    
    @Override
    public void determineLabels() {
        Point point; //current point being altered
        iterationsSinceTempChange = 0;
        doInitialPlacement();
        computeInitialScore();
        
        //algorithm that optimizes the number of labels
        while(T > 0) {
            point = points.get(rand.nextInt(points.size()));
            moveLabelRandomly(point);
            if(deltaE < 0) {
                if(rand.nextDouble() <= (1 - Math.pow(Math.E, -(deltaE/T)))) {
                undoLastPlacement();
                }
            }
            iterationsSinceTempChange ++;
            updateTemperature();
        }
        
        removeOverlap();
    }
    
    protected void doInitialPlacement(){
        for (Point point : points) {
            if (point.getActiveLabelSlider() == null) {
                //NEEDS UPDATE to make it general
                moveLabelRandomly(point);
            }
        }
    }
    
    //Moves a label randomly to a new position that is not the old position
    protected void moveLabelRandomly(SliderPoint p){};
    protected void moveLabelRandomly(Point p){};
    
    //calculates the score and stores it in E
    protected abstract void computeInitialScore();

    protected void removeOverlap() {
        
    }
    
    protected void updateTemperature() {
        if(iterationsSinceTempChange == (50 * numLabels)) {
            if(T < 0.2) {
                T -= 0.05;
            } else {
                T = 0.9 * T;
            }
            iterationsSinceTempChange = 0;
        }
    }
    
    abstract protected void undoLastPlacement();
}
