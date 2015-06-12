
import java.util.ArrayList;

/**
 * This class uses the decorator pattern to create subsets of the problem and
 * let the algorithm solve them independently.
 * 
 */

public class AlgorithmDecorator extends Algorithm {
    Algorithm decoratedAlgorithm;
    ConflictDetector cD;
    
    public AlgorithmDecorator(Algorithm decoratedAlgorithm) {
        this.decoratedAlgorithm = decoratedAlgorithm;
    }
    
    @Override
    public void determineLabels() {
        decoratedAlgorithm.determineLabels();
    }

    @Override
    public int getNumLabels() {
        return decoratedAlgorithm.getNumLabels();
    } 
    
    @Override
    public void setParameters(int w, int h, ArrayList<Point> points, ConflictDetector cD, Timer timer, String model) {
        decoratedAlgorithm.setParameters(w, h, points, cD, timer, model);
    }
}
