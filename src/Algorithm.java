
import java.util.ArrayList;

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
    ConflictList cL;
    Timer timer;
    String model;

    public Algorithm() {
        //needed for extending classes
    }

    public void setParameters(int w, int h, ArrayList<Point> points, ConflictList cL, Timer timer, String model) {
        this.w = w;
        this.h = h;
        this.points = points;
        this.cL = cL;
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
            Label worstLabel = cL.activeLabels.get(0);
            int worstDegree = 0;
            boolean noMoreOverlap = true;
            for (Label label : cL.activeLabels) {
                int labelDegree = cL.getActDegree(label);
                if (labelDegree > worstDegree) {
                    worstLabel = label;
                    worstDegree = labelDegree;
                    noMoreOverlap = false;
                }
            }
            if (!noMoreOverlap) {
                cL.removeActiveLabel(worstLabel);
                if ("1slider".equals(model)) {
                    SliderLabel worstSliderLabel = (SliderLabel) worstLabel;
                    worstSliderLabel.deactivate();
                } else {
                    worstLabel.getPoint().setActiveLabelPos(new PosLabel(w, h, worstLabel.getPoint(), 0));
                }
            } else {
                break;
            }
        }
    }

    public int getNumLabels() {
        return numLabels;
    }

}
