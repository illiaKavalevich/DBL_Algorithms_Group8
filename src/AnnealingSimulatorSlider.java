
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

    public AnnealingSimulatorSlider(Falp alg) {
        super(alg);
    }

    @Override
    public void moveLabelRandomly(SliderPoint p) {
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
            if(cD.getActDegree(l) == 1) numLabelsCovered++;
        }
        
        if(oldLocalE == 0 && newLocalE > 0) {
            changeSelf = -1;
        } else if(oldLocalE > 0 && newLocalE == 0) {
            changeSelf = 1;
        } else {
            changeSelf = 0;
        }
        deltaE = - numLabelsCovered + numLabelsCleared + changeSelf;
    }

    @Override
    protected void activateDeactivate(SliderPoint p) {
        lastPoint = p;
        int numLabelsCleared = 0;
        int numLabelsCovered = 0;
        int changeSelf;
        if(p.getActiveLabelSlider().active == true) {
            
            Set<Label> oldConflicts = new HashSet(cD.getActConflictLabels(p.activeLabel));
            int oldLocalE = oldConflicts.size();
            
            for(Label l : oldConflicts) {
                if(cD.getActDegree(l) == 1) numLabelsCleared++;
            }
            
            if(oldLocalE == 0) {
                changeSelf = -1;
            } else {
                changeSelf = 0;
            }
            p.getActiveLabelSlider().active = false;
            activePoints.remove(lastPoint);
            numLabels--;
        } else {
            p.getActiveLabelSlider().active = true;
            activePoints.add(lastPoint);
            Set<Label> newConflicts = new HashSet(cD.getActConflictLabels(p.activeLabel));
            int newLocalE = newConflicts.size();
            
            if(newLocalE == 0) {
                changeSelf = 1;
            } else {
                changeSelf = 0;
            }
            
            for(Label l : newConflicts) {
                if(cD.getActDegree(l) == 1) numLabelsCovered++;
            }
            numLabels++;
        }
        
        deltaE = - numLabelsCovered + numLabelsCleared + changeSelf;
    }
    
    @Override
    protected void computeInitialScore() {
        int tempE = 0;
        for (Point p : points) {
            if (cD.getActDegree(p.getActiveLabelSlider()) == 0) {
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
    protected void undoLastActivationDeactvation() {
        if(lastPoint.getActiveLabelSlider().active == true) {
            lastPoint.getActiveLabelSlider().active = false;
            activePoints.remove(lastPoint);
            numLabels--;
        } else{
            lastPoint.getActiveLabelSlider().active = true;
            activePoints.add(lastPoint);
            numLabels++;
        }
    }

    @Override
    protected void doInitialPlacement() {
        if(useDiscreteAlg) {
            discreteAlg.setParameters(w, h, points, cD, timer, "1slider");
            //discreteAlg.removeConflicts();
            cD.setModel("2pos");
            discreteAlg.removeConflicts();
            cD.setModel("1slider");
            for(Point p : points) {
                for(Label l : p.getPossibleLabels()) {
                    if(l.active = true) {
                        if(l.quadrant == 1) {
                            p.getActiveLabelSlider().setPlacement(1.0f);
                        } else {
                            p.getActiveLabelSlider().setPlacement(0.0f);
                        }
                    }
                }
            }
        } else {
            for (Point point : points) {
                point.getActiveLabelSlider().setPlacement(rand.nextFloat());
            }
        }
    }
}
