
import java.util.ArrayList;
import java.util.HashSet;
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
        System.out.println("Started to remove overlap");
        while (true) {
            System.out.println("removeOverlap loop");
            Label worstLabel = null;
            int worstDegree = 0;
            boolean noMoreOverlap = true;
            for (Point p : points) {
                Label label = p.getActiveLabelPos();
                
                int labelDegree = cD.getActConflictLabels(label).size();
                System.out.println(labelDegree);
                if (labelDegree > worstDegree) {
                    worstLabel = label;
                    worstDegree = labelDegree;
                    noMoreOverlap = false;
                }
            }

            if (!noMoreOverlap) {
                cD.removeLabel(worstLabel);
                if ("1slider".equals(model)) {
                    SliderLabel worstSliderLabel = (SliderLabel) worstLabel;
                    worstSliderLabel.deactivate();
                } else {
                    PosLabel worstPosLabel = (PosLabel) worstLabel;
                    worstPosLabel.setLabel(0);
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
