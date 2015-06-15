
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author s133524
 */
public class AnnealingSimulatorSlider extends AnnealingSimulator {

    float lastPosition;

    public AnnealingSimulatorSlider() {

    }

    @Override
    protected void moveLabelRandomly(SliderPoint p) {
        int numLabelsCleared = 0;
        int numLabelsCovered = 0;
        int changeSelf;
        lastPosition = p.activeLabel.getPlacement();
        lastPoint = p;
        
        //book keeping old conflicts
        Set<Label> oldConflicts = new HashSet(cD.getActConflictLabels(p.activeLabel));
        Set<Label> oldConflictsCopy = new HashSet(oldConflicts);
        int oldLocalE = oldConflicts.size();
        
        //move label to a random position
        float random = rand.nextFloat();
        p.activeLabel.setPlacement(random);

        Set<Label> newConflicts = new HashSet(cD.getActConflictLabels(p.activeLabel));
        int newLocalE = newConflicts.size();
        
        //compute the nuber of labels cleared by change
        oldConflicts.removeAll(newConflicts);
        for(Label l : oldConflicts) {
            if(cD.getActDegree(l) == 0) numLabelsCleared++;
        }
        
        //compute the nuber of labels covered by change
        newConflicts.removeAll(oldConflictsCopy);
        for(Label l : newConflicts) {
            if(cD.getActDegree(l) == 0) numLabelsCovered++;
        }
        
        if(oldLocalE == 0 && newLocalE > 0) {
            changeSelf = 1;
        } else if(oldLocalE > 0 && newLocalE == 0) {
            changeSelf = -1;
        } else {
            changeSelf = 0;
        }
        
//        } else {
//            float random = rand.nextFloat();
//            p.activeLabel.setPlacement(random);
//            //System.out.println(p.xCoord + " placement: " + random);
//
//            newConflicts = new HashSet(cD.getActConflictLabels(p.activeLabel));
//            newConflicts.addAll(oldConflicts); //now holds all labels affected
//            newLocalE = cD.getActDegree(p.activeLabel);
//            if(newLocalE > 0) {
//                newLocalE ++;
//            }

        deltaE = numLabelsCovered - numLabelsCleared + changeSelf;
    }

    @Override
    protected void computeInitialScore() {
        int tempE = 0;
        for (Point p : points) {
            if (cD.getActDegree(p.getActiveLabelSlider()) > 0) {
                tempE++;
            }
        }
        E = tempE;
    }

    @Override
    protected void undoLastPlacement() {
        lastPoint.getActiveLabelSlider().setPlacement(lastPosition);
    }

    @Override
    protected void doInitialPlacement() {
        for (Point point : points) {
            if (point.getActiveLabelSlider() == null) {
                moveLabelRandomly((SliderPoint) point);
            }
        }
    }
}
