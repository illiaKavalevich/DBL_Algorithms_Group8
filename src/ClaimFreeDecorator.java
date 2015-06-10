
import java.util.ArrayList;
import java.util.HashSet;


/**
 * This class uses the decorator pattern to claim 'free' labels, labels that are
 * easy to place without affecting the possibility of an optimal solution.
 * 
 */

public class ClaimFreeDecorator extends AlgorithmDecorator {
    int numLabelsClaimed;
    ArrayList<Point> pointsSubSet;
    
    public ClaimFreeDecorator(Algorithm decoratedAlgorithm) {
        super(decoratedAlgorithm);
    }
    
    @Override
    public void determineLabels() {
        pointsSubSet = new ArrayList<>(points);
        numLabelsClaimed = 1; //1 so loop does not terminate immediately
        while(numLabelsClaimed > 0) {
            numLabelsClaimed = 0;
            applyRule1();
            applyRule2();
        }
        super.determineLabels();
    }
    
    @Override
    public void setParameters(int w, int h, ArrayList<Point> points, ConflictList cL, Quadtree q, Timer timer, String model) {
        this.cL = cL;
        this.points = points;
        decoratedAlgorithm.setParameters(w, h, pointsSubSet, cL, q, timer, model);
    }
    
    /* Gives points a label if there is a label position that has 0 overlap with 
     other possible label positions.
     */
    private void applyRule1() {
        for(Point point : pointsSubSet) { //loop all points
            for(Label label : point.getPossibleLabels()) { //loop all labels
                if(cL.getPosDegree(label) == 0) {
                    point.setActiveLabelPos((PosLabel) label);
                    cL.removePoint(point);
                    pointsSubSet.remove(point);
                    numLabelsClaimed ++;
                    break;
                }
            }
        }
    }
    
    /* Gives two points a label if they both have 2 labels that only have overlap
    with the other point.
    */
    private void applyRule2() {
        for(Point point : pointsSubSet) { //check rule for every point
            
            //My apologies for the extremely nested statements but this is actually efficient
            for(Label label : point.getPossibleLabels()) { //loop all labels
                if(cL.getPosDegree(label) == 1) {
                    //loops over all the label in conflict with the label from outer loop
                    for(Label label2 : cL.getPosConflictLabels(label)) { 
                        Point nearPoint = label2.getPoint(); //a point that has a label conflicting with the point under inspection
                        for(Label labelNearPoint : nearPoint.getPossibleLabels()) { //loop over label nearpoint
                            if(cL.getPosDegree(labelNearPoint) == 1) { //if degree of label of nearby point is 1
                                for(Label label3 : cL.getPosConflictLabels(labelNearPoint)) { //loop over labels conflicting the label of nearby point
                                    //if this label conflicts with a label of the starting point but is not 'label' we've got a winner
                                    if(label3.getPoint().equals(point) && !label3.equals(label)) { 
                                        point.setActiveLabelPos((PosLabel) label);
                                        nearPoint.setActiveLabelPos((PosLabel) labelNearPoint);
                                        cL.removePoint(point);
                                        cL.removePoint(nearPoint);
                                        pointsSubSet.remove(point);
                                        pointsSubSet.remove(nearPoint);
                                        numLabelsClaimed += 2;
                                        //needs break
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}