
import java.util.ArrayList;import java.util.HashSet;
import java.util.Set;
;

/**
 * This class uses the decorator pattern to claim 'free' labels, labels that are
 * easy to place without affecting the possibility of an optimal solution.
 * 
 */

public class ClaimFreeDecorator extends AlgorithmDecorator {
    int numLabelsClaimed;
    int numLabelsClaimedFinal;
    ArrayList<Point> pointsSubSet = new ArrayList<>();
    
    public ClaimFreeDecorator(Algorithm decoratedAlgorithm) {
        super(decoratedAlgorithm);
    }
    
    @Override
    public void determineLabels() {
        pointsSubSet.addAll(points);
        numLabelsClaimed = 1; //1 so loop does not terminate immediately
        numLabelsClaimedFinal = 0;
        while(numLabelsClaimed > 0) {
            numLabelsClaimed = 0;
            applyRule1();
            applyRule2();
            numLabelsClaimedFinal += numLabelsClaimed;
        }
        super.determineLabels();
        super.addNumLabels(numLabelsClaimedFinal);
        //System.out.println("Claimed " + numLabelsClaimedFinal);
    }
    
   
    
    @Override
    public void setParameters(int w, int h, ArrayList<Point> points, ConflictDetector cD, Timer timer, String model) {
        this.cD = cD;
        this.points = points;
        this.model = model;
        decoratedAlgorithm.setParameters(w, h, pointsSubSet, cD, timer, model);
    }
    
    /* Gives points a label if there is a label position that has 0 overlap with 
     other possible label positions.
     */
    private void applyRule1() {
        Set<Point> pointsToBeRemoved = new HashSet<>(); 
        for(Point point : pointsSubSet) { //loop all points
            for(Label label : point.getPossibleLabels()) { //loop all labels
                if(cD.getPosDegree(label) == 0) {
                    activateLabel(label);
                    cD.removePoint(point);
                    pointsToBeRemoved.add(point);
                    numLabelsClaimed ++;
                    break;
                }
            }
        }
        
        for(Point p : pointsToBeRemoved) {
            pointsSubSet.remove(p);
        }
    }
    
    /* Gives two points a label if they both have 2 labels that only have overlap
    with the other point.
    */
    private void applyRule2() {
        Set<Point> pointsToBeRemoved = new HashSet<>(); 
        
        for(Point point : pointsSubSet) { //check rule for every point
            
            //My apologies for the extremely nested statements but this is actually efficient
            for(Label label : point.getPossibleLabels()) { //loop all labels
                if(cD.getPosDegree(label) == 1) {
                    //loops over all the label in conflict with the label from outer loop
                    for(Label label2 : cD.getPosConflictLabels(label)) { 
                        Point nearPoint = label2.getPoint(); //a point that has a label conflicting with the point under inspection
                        for(Label labelNearPoint : nearPoint.getPossibleLabels()) { //loop over label nearpoint
                            if(cD.getPosDegree(labelNearPoint) == 1) { //if degree of label of nearby point is 1
                                for(Label label3 : cD.getPosConflictLabels(labelNearPoint)) { //loop over labels conflicting the label of nearby point
                                    //if this label conflicts with a label of the starting point but is not 'label' we've got a winner
                                    if(label3.getPoint().equals(point) && !label3.equals(label)) {
                                        //point.setActiveLabelPos((PosLabel) label);
                                        //nearPoint.setActiveLabelPos((PosLabel) labelNearPoint);
                                        cD.removePoint(point);
                                        cD.removePoint(nearPoint);
                                        pointsToBeRemoved.add(point);
                                        pointsToBeRemoved.add(nearPoint);
                                        activateLabel(labelNearPoint);
                                        activateLabel(label);
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
        
        for(Point p : pointsToBeRemoved) {
            pointsSubSet.remove(p);
        }
    }
    
    private void activateLabel(Label l) {
        if(model.equals("1slider")) {
            if(l.quadrant == 1) {
                l.p.getActiveLabelSlider().setPlacement(1.0f);
            } else {
                l.p.getActiveLabelSlider().setPlacement(0.0f);
            }
        } else {
            l.active = true;
        }
    }
}