
/**
 * This class uses the decorator pattern to create subsets of the problem and
 * let the algorithm solve them independently.
 * 
 */

public class SubProblemDecorator extends Algorithm {
    Algorithm decoratedAlgorithm;

    
    public SubProblemDecorator(Algorithm decoratedAlgorithm, ConflictList cL) {
        this.decoratedAlgorithm = decoratedAlgorithm;
        this.cL = cL;
    }
    
    @Override
    public void determineLabels() {
        super.determineLabels();
    }
    
        @Override
    public int getNumLabels() {
        return decoratedAlgorithm.getNumLabels();
    } 
    
}
