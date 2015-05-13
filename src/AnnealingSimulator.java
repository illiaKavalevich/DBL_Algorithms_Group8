
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
    protected int iterationsSinceTempChange;
    protected Random rand;

    public AnnealingSimulator(int w, int h, ArrayList<Point> points, 
            ConflictList cL) {
        super(w, h, points, cL);
        this.rand = new Random(56467984);
        this.T = 2.50;
    }
    
    public void setTemperature(double temperature) {
        this.T = temperature;
    }
    
    @Override
    public void determineLabels() {
        Point point; //current point being altered
        iterationsSinceTempChange = 0;
        doInitialPlacement();
        
        //algorithm that optimizes the number of labels
        while(T > 0) {
            point = points.get(rand.nextInt(points.size()));
            moveLabelRandomly(point);
            //update adjancancy matrix
            computeScore();
            if(deltaE < 0) {
                if(rand.nextDouble() <= (1 - Math.pow(Math.E, -(deltaE/T)))) {
                //undo
                }
            }
            iterationsSinceTempChange ++;
            updateTemperature();
        }
        
        removeOverlap();
    }
    
    protected void doInitialPlacement(){
        for(int i = 0; i < points.size(); i++) {
            if(points.get(i).labelPos == "") { //NEEDS UPDATE
                moveLabelRandomly(points.get(i));
            }
        }
    }
    
    //Moves a label randomly to a new position that is not the old position
    protected void moveLabelRandomly(SliderPoint p){};
    protected void moveLabelRandomly(Point p){};
    
    //calculates the difference in score and stores it in deltaE
    protected abstract void computeScore();

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
}
