
/**
 * This class uses the decorator pattern to claim 'free' labels, labels that are
 * easy to place without affecting the possibility of an optimal solution.
 * 
 */

public class ClaimFreeDecorator extends AlgorithmDecorator {
    int numLabelsClaimed;
    
    public ClaimFreeDecorator(Algorithm decoratedAlgorithm) {
        super(decoratedAlgorithm);
    }
    
    @Override
    public void determineLabels() {
        numLabelsClaimed = 1; //1 so loop does not terminate immediately
        while(numLabelsClaimed > 0) {
            numLabelsClaimed = 0;
            applyRule1();
            applyRule2();
        }
        super.determineLabels();
    }
    
    @Override
    public int getNumLabels() {
        return super.getNumLabels();
    }  
    
    /* Gives points a label if there is a label position that has 0 overlap with 
     other possible label positions.
     */
    private void applyRule1() {
//        for(point : points) {
//            
//        }
    }
    
    /* Gives two points a label if they both have 2 labels that only have overlap
    with the other point.
    */
    private void applyRule2() {
//      for(point : points) {
//            
//      }
    }
}