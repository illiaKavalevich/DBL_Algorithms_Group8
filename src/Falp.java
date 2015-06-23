
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
    boolean changed = false;

    public Falp() {

    }

    //After the timer stops, determineLables will only stop after a iteration of localsearch has finished, since it cannot be interrupted
    //During the local search
    @Override
    public void determineLabels() {
        removeConflicts();
        if (!stop && points.size()<10000) {
            giveActiveLabel();
            int totaldegree = 0;
            for (Point p : points) {
                for (Label l : p.possibleLabels) {
                    if (l.active) {
                        totaldegree += cD.getActDegree(l);
                    }
                }
            }
            if (totaldegree < 100000) {

                while (!stop && changed) {
                    localSearch();
                }
                removeOverlap();
                selectBestResult();
            } else {
                points.clear();
                for (Point p : firstPoints) {
                    points.add(p);
                }
                numLabels = firstNumlabels;
            }
        }
    }

    @Override
    public void stopRunning() {
        stop = true;
    }

    //select the best result: either the final result or the result of the first step
    public void selectBestResult() {
        if (numLabels < firstNumlabels) {
            points.clear();
            for (Point p : firstPoints) {
                points.add(p);
            }
            numLabels = firstNumlabels;
        }
    }

    //first step of the FALP algorithm: 
    //give a point an active label that has no overlap with if possible
    //otherwise take the label with the least overlap and remove the labels it has overlap with
    //the conflictlist that is used will be empty after this method, so a copy is needed
    public void removeConflicts() {
        Set<Label> posLabelSet = new HashSet<>();
        for (Point p : points) {
            for (Label l : p.possibleLabels) {
                posLabelSet.add(l);
            }
        }
        ConflictDetector cdCopy = null;
        if(model.equals("1slider")){
            cdCopy = new ConflictDetector(points, "2pos", new Quadtree2());
        } else{
            cdCopy = new ConflictDetector(points, model, new Quadtree2());
        }
        
        firstNumlabels = 0;
        numLabels = 0;
        for (Point point : points) {
            noActiveLabelPoints.add(point);
        }
        while (!posLabelSet.isEmpty() && !stop) {
            Label bestLabel = null;
            int lowestDegree = Integer.MAX_VALUE;

            for (Label l : posLabelSet) {
                int labelDegree = cdCopy.getPosDegree(l);
                if (labelDegree < lowestDegree) {
                    bestLabel = l;
                    lowestDegree = labelDegree;
                    if(labelDegree == 0){
                        break;
                    }
                }
            }
            for (Label l : cdCopy.getPosConflictLabels(bestLabel)) {
                if (l != bestLabel) {
                    cdCopy.removeLabel(l);
                }
                posLabelSet.remove(l);
            }
            for (Label l : bestLabel.getPoint().getPossibleLabels()) {
                if (l != bestLabel) {
                    cdCopy.removeLabel(l);
                }
                posLabelSet.remove(l);
            }
            Point p = bestLabel.getPoint();
            noActiveLabelPoints.remove(p);
            activeLabelPoints.add(p);
            bestLabel.active = true;
            firstNumlabels++;
            numLabels++;
        }
        for (Point p : points) {
            if(model.equals("1slider")){
                firstPoints.add(new SliderPoint(p));
            }
            else{
                firstPoints.add(new PosPoint(p));
            }
            
        }
    }

    //step 2 of FALP:
    //Give all points that did not yet have a label an active label
    //this label may have conflicts with other labels
    public void giveActiveLabel() {
        for (Point point : noActiveLabelPoints) {
            Label bestLabel = null;
            int bestDegree = Integer.MAX_VALUE;
            for (Label label : point.possibleLabels) {
                int labelDegree = cD.getActConflictLabels(label).size();
                if (labelDegree < bestDegree) {
                    bestDegree = labelDegree;
                    bestLabel = label;
                }
            }
            bestLabel.active = true;
            activeLabelPoints.add(point);
        }

    }

    //step 3 of FALP: the local search
    //Check for each point if it has a label with less overlap than the current active one.
    public void localSearch() {
        changed = false;
        for (Point point : activeLabelPoints) {
            Label bestLabel = null;
            int bestDegree = Integer.MAX_VALUE;
            for (Label label : point.getPossibleLabels()) {

                int labelDegree = cD.getActConflictLabels(label).size();
                if (labelDegree < bestDegree) {
                    bestLabel = label;                                      //update bestlabel if there is a label with lower degree
                    bestDegree = labelDegree;
                }
            }
            if(bestLabel.active == false){
                changed = true;
            }
            for (Label l : point.possibleLabels) {
                l.active = false;
            }
            bestLabel.active = true;
        }
    }

}
