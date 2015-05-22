
import java.util.ArrayList;

/**
 * This class uses the decorator pattern to create subsets of the problem and
 * let the algorithm solve them independently.
 * 
 */

public class AlgorithmDecorator extends Algorithm {
    Algorithm decoratedAlgorithm;
    ConflictList cL;
    
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
    public void setParameters(int w, int h, ArrayList<Point> points, ConflictList cL, Timer timer) {
        decoratedAlgorithm.setParameters(w, h, points, cL, timer);
    }
        
}