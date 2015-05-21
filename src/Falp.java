
import java.util.*;

public class Falp extends Algorithm {

    HashSet<Point> noActiveLabelPoints = new HashSet<>();                       //all the points that do not have an active label
    HashSet<Point> noOverlapPoints = new HashSet<>();                           //all the points that have an active label that doesn't overlap
    ConflictList clCopy = new ConflictList(cL);

    public Falp() {

    }

    @Override
    public void determineLabels() {
        removeConflicts();
        giveActiveLabel();
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
            clCopy.updateActConflicts(newActLabel);

        }
        //System.out.println("points without label: " + noActiveLabelPoints.size());

    }

    //step 2 of FALP:
    //Give all points that did not yet have a label an active label
    //this label may have conflicts with other labels
    public void giveActiveLabel() {
        for (Point point : noActiveLabelPoints) {
            for (Label label : point.possibleLabels) {
                if (clCopy.possibleLabels.contains(label)) {
                    point.getActiveLabelPos().setLabel(label.getQuadrant());
                    numLabels++;
                    break;
                }
            }
        }

    }

}
