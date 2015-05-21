
/**
 * This class uses the decorator pattern to create subsets of the problem and
 * let the algorithm solve them independently.
 * 
 */

public class SubProblemDecorator extends AlgorithmDecorator {
    
    public SubProblemDecorator(Algorithm decoratedAlgorithm) {
        super(decoratedAlgorithm);
    }
    
    @Override
    public void determineLabels() {
        super.determineLabels();
    }
    
    @Override
    public int getNumLabels() {
        return super.getNumLabels();
    }  
}
