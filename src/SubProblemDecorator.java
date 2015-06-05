
import java.util.ArrayList;


/**
 * This class uses the decorator pattern to create subsets of the problem and
 * let the algorithm solve them independently.
 * 
 */

public class SubProblemDecorator extends AlgorithmDecorator {
    ArrayList<Point> independentSubSet;
    ArrayList<Point> pointsSubSet; //points left to be processed
    int w;
    int h;
    Timer timer;
    
    public SubProblemDecorator(Algorithm decoratedAlgorithm) {
        super(decoratedAlgorithm);
    }
    
    @Override
    public void determineLabels() {
        while(!pointsSubSet.isEmpty()) {
            determineIndependentSet();
            decoratedAlgorithm.setParameters(w, h, independentSubSet, cL, timer, model);
            super.determineLabels();
        }
    }
    
    @Override
    public void setParameters(int w, int h, ArrayList<Point> points, ConflictList cL, Timer timer, String model) {
        this.cL = cL;
        this.points = points;
        this.w = w;
        this.h = h;
        this.timer = timer;
        this.model = model;
        decoratedAlgorithm.setParameters(w, h, independentSubSet, cL, timer, model);
    }

    private void determineIndependentSet() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
