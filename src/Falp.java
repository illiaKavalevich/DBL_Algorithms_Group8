
import java.util.*;

public class Falp extends Algorithm {

    HashSet<Point> noActiveLabelPoints = new HashSet<>();                       //all the points that do not have an active label
    HashSet<Point> noOverlapPoints = new HashSet<>();                           //all the points that have an active label that doesn't overlap
    ConflictList clCopy = new ConflictList(cL);

    public Falp(int w, int h, ArrayList<Point> points, ConflictList cL) {
        super(w, h, points, cL);

    }

    @Override
    public void determineLabels() {
        removeConflicts();
        giveActiveLabel();
        localSearch();
    }

    //first step of the FALP algorithm: 
    //give a point an active label that has no overlap with if possible
    //otherwise take the label with the least overlap and remove the labels it has overlap with
    //the conflictlist that is used will be empty after this method, so a copy is needed
    public void removeConflicts() {
        for (Point point : points) {
            noActiveLabelPoints.add(point);
        }
        while (!(clCopy.possibleLabels.isEmpty())) {
            Label bestLabel = clCopy.possibleLabels.get(0);                     //find the label with the lowest degree
            //System.out.println(clCopy.getPosConflictLabels(bestLabel).size());
            int lowestDegree = Integer.MAX_VALUE;

            for (Label l : clCopy.possibleLabels) {
                //System.out.println(clCopy.getPosDegree(l));
                if (clCopy.getPosDegree(l) < lowestDegree) {
                    bestLabel = l;
                    lowestDegree = clCopy.getPosDegree(l);
                }
            }
            //System.out.println("degree of the best label: "+clCopy.getPosDegree(bestLabel));
            if (clCopy.hasPosConflicts(bestLabel)) {
                for (Label l : clCopy.getPosConflictLabels(bestLabel)) {
                    clCopy.removeLabel(l);
                }
            }
            Point p = bestLabel.getPoint();
            clCopy.removePoint(p);
            noActiveLabelPoints.remove(p);
            noOverlapPoints.add(p);
            PosLabel newActLabel = p.getActiveLabelPos();
            newActLabel.setLabel(bestLabel.getQuadrant());
            //added
            numLabels++;
            cL.updateActConflicts(newActLabel);

        }
        //System.out.println("points without label: " + noActiveLabelPoints.size());

    }

    //step 2 of FALP:
    //Give all points that did not yet have a label an active label
    //this label may have conflicts with other labels
    public void giveActiveLabel() {
        for (Point point : noActiveLabelPoints) {
            for (Label label : point.possibleLabels) {
                if (cL.possibleLabels.contains(label)) {
                    point.getActiveLabelPos().setLabel(label.getQuadrant());
                    cL.updateActConflicts(point.getActiveLabelPos());
                    numLabels++;
                    noOverlapPoints.add(point);
                    break;
                }
            }
        }
    }

    //step 3 of FALP: the local search
    //Check for each point if it has a label with less overlap than the current active one.
    public void localSearch() {
        while (true) {
            boolean changed = false;                                            //indicates if for any point its active label was changed
            for (Point point : noOverlapPoints) {
                Label bestLabel = cL.possibleLabels.get(0);                     //this will be the best label of a point
                for(Label posLabel : point.getPossibleLabels()){
                    bestLabel = posLabel;                                       //initialize bestLabel to some random possible label of that point
                }
                int bestDegree = Integer.MAX_VALUE;
                for (Label label : point.getPossibleLabels()) {
                    int labelDegree = cL.getActDegree(label);
                    //System.out.println("degree of possible label: "+labelDegree);
                    if (labelDegree < bestDegree) {
                        bestLabel = label;                                      //update bestlabel if there is a label with lower degree
                        bestDegree = labelDegree;
                    }
                }
                if (bestLabel != point.getActiveLabelPos()) {
                    point.getActiveLabelPos().setLabel(bestLabel.getQuadrant());
                    cL.updateActConflicts(point.getActiveLabelPos());
                    changed = true;
                    /*System.out.println("Changed a label");
                    System.out.println("x point: "+point.xCoord);
                    System.out.println("y point: "+point.yCoord);
                    System.out.println("quadrant label: "+bestLabel.getQuadrant());*/
                }
            }
            System.out.println(cL.actConflict.size());
            //Stop the local search if none of the active labels have overlap, or if none of the active labels got changed in the last iteration
            /*if (cL.actConflict.isEmpty() || changed) {
                break;
            }*/
        }
    }

    

}
