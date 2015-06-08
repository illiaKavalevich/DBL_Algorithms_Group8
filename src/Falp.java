
import java.util.*;

public class Falp extends Algorithm {

    HashSet<Point> noActiveLabelPoints = new HashSet<>();                       //all the points that do not have an active label
    HashSet<Point> activeLabelPoints = new HashSet<>();                           //all the points that have an active label that doesn't overlap
    Runnable Run;
    int firstNumlabels;
    ArrayList<Point> firstPoints = new ArrayList<>();
    ArrayList<Integer> firstQuadrants = new ArrayList<>();
    Thread thread;
    boolean stop = false;

    public Falp() {

    }

    //After the timer stops, determineLables will only stop after a iteration of localsearch has finished, since it cannot be interrupted
    //During the local search
    @Override
    public void determineLabels() {
        removeConflicts();
        giveActiveLabel();
        while (!stop) {
            localSearch();
        }
        removeOverlap();
        selectBestResult();
    }

    @Override
    public void stopRunning() {
        stop = true;
    }

    //select the best result: either the final result or the result of the first step
    public void selectBestResult() {
        System.out.println("Selecting the best result");
        if (numLabels < firstNumlabels) {
            System.out.println("The result of the first step was better");
            int i = 0;
            points.clear();
            for(Point p:firstPoints){
                points.add(p);
            }
        }
    }

    //first step of the FALP algorithm: 
    //give a point an active label that has no overlap with if possible
    //otherwise take the label with the least overlap and remove the labels it has overlap with
    //the conflictlist that is used will be empty after this method, so a copy is needed
    public void removeConflicts() {
        firstNumlabels = points.size();
        numLabels = points.size();
        ConflictList clCopy = new ConflictList(cL);
        System.out.println("Started to remove conflicts");
        for (Point point : points) {
            noActiveLabelPoints.add(point);
        }
        while (!(clCopy.possibleLabels.isEmpty())) {
            Label bestLabel = clCopy.possibleLabels.get(0);                     //find the label with the lowest degree
//            System.out.println(clCopy.getPosConflictLabels(bestLabel).size());
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
            activeLabelPoints.add(p);
            PosLabel newActLabel = p.getActiveLabelPos();
            newActLabel.setLabel(bestLabel.getQuadrant());
            //added
            cL.updateActConflicts(newActLabel);
            firstNumlabels--;
        }
        for(Point p:points){
            Point point = new PosPoint(p);
            firstPoints.add(point);
        }
    }

    //step 2 of FALP:
    //Give all points that did not yet have a label an active label
    //this label may have conflicts with other labels
    public void giveActiveLabel() {
        System.out.println("Started to give active labels");
        for (Point point : noActiveLabelPoints) {
            Label bestLabel = cL.possibleLabels.get(0);
            int bestDegree = Integer.MAX_VALUE;
            for (Label label : point.possibleLabels) {
                int labelDegree = cL.getPosActDegree(label);
                //System.out.println(labelDegree);
                if (labelDegree < bestDegree) {
                    bestDegree = labelDegree;
                    bestLabel = label;
                }
            }
            point.getActiveLabelPos().setLabel(bestLabel.getQuadrant());
            cL.updateActConflicts(point.getActiveLabelPos());
            activeLabelPoints.add(point);
        }

    }

    //step 3 of FALP: the local search
    //Check for each point if it has a label with less overlap than the current active one.
    public void localSearch() {
        System.out.println("local search");
        boolean changed = false;                                            //indicates if for any point its active label was changed
        for (Point point : activeLabelPoints) {
            Label bestLabel = cL.possibleLabels.get(0);                     //this will be the best label of a point
            for (Label posLabel : point.getPossibleLabels()) {
                bestLabel = posLabel;                                       //initialize bestLabel to some random possible label of that point
            }
            int bestDegree = Integer.MAX_VALUE;
            for (Label label : point.getPossibleLabels()) {
                int labelDegree = cL.getPosActDegree(label);
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
            }
        }
        //Stop the local search if none of the active labels have overlap, or if none of the active labels got changed in the last iteration
            /*if (cL.actConflict.isEmpty() || !changed) {
         break;
         }*/
    }

}
