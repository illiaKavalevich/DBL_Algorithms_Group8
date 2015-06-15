
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author s131061
 */
public abstract class Algorithm {

    //stores amount of labels placed
    int numLabels = 0;
    ArrayList<Point> points = new ArrayList<>();
    int w;
    int h;
    ConflictDetector cD;
    Timer timer;
    String model;

    public Algorithm() {
        //needed for extending classes
    }

    public void addNumLabels(int i) {
        numLabels += i;
    }

    public void setParameters(int w, int h, ArrayList<Point> points, ConflictDetector cD, Timer timer, String model) {
        this.w = w;
        this.h = h;
        this.points = points;
        this.cD = cD;
        this.timer = timer;
        this.model = model;
    }

    public void stopRunning() {
    }

    /**
     * determines the positions of the labels
     *
     * @result \result = max amount of labels placed (using point.setLabelPos)
     * without overlap
     */
    public abstract void determineLabels();

    public void removeOverlap() {
        //System.out.println("Started to remove overlap");
        List<Point> copyPoints = new ArrayList<>(points);

        for (Point p : points) {
            if ("1slider".equals(model)) {
                p.actOverlap = cD.getActDegree(p.getActiveLabelSlider());
                //System.out.println(p.actOverlap);
                if (p.actOverlap == 0) {
                    copyPoints.remove(p.getActiveLabelSlider());
                }
            } else {
                for (Label l : p.possibleLabels) {
                    if (l.active == true) {
                        p.actOverlap = cD.getActDegree(l);
                        if (p.actOverlap == 0) {
                            copyPoints.remove(l);
                        }
                        break;
                    }
                }
            }
        }

        while (true) {
            
            Label worstLabel = null;
            int worstDegree = 0;
            boolean noMoreOverlap = true;
            for (Point p : copyPoints) {
                Label label = null;
                if ("1slider".equals(model)) {
                    label = p.getActiveLabelSlider();
                } else {
                    for (Label curLabel : p.possibleLabels) {
                        PosLabel curLabel2 = (PosLabel) curLabel;
                        if (curLabel2.active == true) {
                            label = curLabel2;
                        }

                    }
                }
                if (label != null) {

                    int labelDegree = p.actOverlap;
                    //System.out.println(p.actOverlap);
//                    System.out.println(label.p.possibleLabels);
                    //System.out.println(cD.getActConflictLabels(label));
//                    System.out.println(labelDegree);
                    if (labelDegree > worstDegree) {
                        //System.out.println("sup");
                        worstLabel = label;
                        worstDegree = labelDegree;
                        noMoreOverlap = false;
                        //System.out.println("removeOverlap 1");
                    }
                }
                //System.out.println("worstdegree: " + worstDegree);

            }
            if (!noMoreOverlap) {
                //System.out.println("hier komtie");
                Set<Label> labelsToBeUpdated = new HashSet<>();
                labelsToBeUpdated.addAll(cD.getActConflictLabels(worstLabel));
                for (Label l : labelsToBeUpdated) {
                    
                    l.p.actOverlap--;
                    if (l.p.actOverlap == 0) {
                        copyPoints.remove(l.p);
                    }
                }
                if ("1slider".equals(model)) {
                    //System.out.println("removeOverlap 2");
                    SliderLabel worstSliderLabel = (SliderLabel) worstLabel;
                    worstSliderLabel.deactivate();
                    cD.removePoint(worstSliderLabel.p);
                    copyPoints.remove(worstSliderLabel.p);
                } else {
                    worstLabel.active = false;
                    cD.removeLabel(worstLabel);
                }
                numLabels--;
            } else {
                break;
            }

        }
    }

    public int getNumLabels() {
        return numLabels;
    }

}
