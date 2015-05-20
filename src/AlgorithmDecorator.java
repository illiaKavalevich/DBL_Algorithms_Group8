
import java.util.ArrayList;

/**
 * This class uses the decorator pattern to create subsets of the problem and
 * let the algorithm solve them independently.
 * 
 */

public class AlgorithmDecorator extends Algorithm {
    Algorithm decoratedAlgorithm;
    ConflictList cL;
    
    public AlgorithmDecorator(Algorithm decoratedAlgorithm, ConflictList cL) {
        this.decoratedAlgorithm = decoratedAlgorithm;
        this.cL = cL;
    }
    
    @Override
    public void determineLabels() {
        decoratedAlgorithm.determineLabels();
    }

    @Override
    public int getNumLabels() {
        return decoratedAlgorithm.getNumLabels();
    } 
        
}
