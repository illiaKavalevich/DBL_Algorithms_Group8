<<<<<<< HEAD
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author s133524
 */
public class AnnealingSimulatorSlider extends AnnealingSimulator {
    float lastPosition;
    
    public AnnealingSimulatorSlider(int w, int h, ArrayList<Point> points, 
            ConflictList cL) {
        super(w, h, points, cL);
    }
    
    @Override
    protected void moveLabelRandomly(SliderPoint p) {
        lastPosition = p.activeLabel.getPlacement();
        int oldLocalE = cL.getActDegree(p.activeLabel);
        if(oldLocalE > 0) {
            oldLocalE ++;
        }
        int newLocalE = 0;
        
        if(complicatedScoring) {
            Set<Label> oldConflicts = cL.getActConflictLabels(p.activeLabel);
            Set<Label> newConflicts;

            float random = rand.nextFloat();
            p.activeLabel.setPlacement(random);
            
            cL.updateActConflicts(p.activeLabel);
            newConflicts = cL.getActConflictLabels(p.activeLabel);
            for(Label label : oldConflicts) {
                cL.updateActConflicts(label);
                if(cL.getActDegree(label) > 0) {
                    newLocalE ++;
                }
                newConflicts.remove(label);
            }

            if(newConflicts.size() > 0) {
                newLocalE ++;
                for(Label label : newConflicts) {
                    cL.updateActConflicts(label); //Needed?
                    if(cL.getActDegree(label) == 1) {
                        newLocalE ++;
                    }
                }
            }
            labelsAffectedLastChange.clear();
            labelsAffectedLastChange.addAll(newConflicts);
            labelsAffectedLastChange.addAll(oldConflicts);
            deltaE = oldLocalE - newLocalE;
        } else {
            float random = rand.nextFloat();
            p.activeLabel.setPlacement(random);
            
            //still needs to update all label ofter change.
            cL.updateActConflicts(p.activeLabel);
            newLocalE = cL.getActDegree(p.activeLabel);
            if(newLocalE > 0) {
                newLocalE ++;
            }

            deltaE = oldLocalE - newLocalE;
        }
    }
    
    @Override
    protected void computeInitialScore() {
        int tempE = 0;
        for(Point p: points) {
            if(cL.getActDegree(p.getActiveLabelSlider()) > 0) {
                tempE ++;
            }
        }
        E = tempE;
    }
    
    @Override 
    protected void undoLastPlacement() {
        lastPoint.getActiveLabelSlider().setPlacement(lastPosition);
        for(Label label : labelsAffectedLastChange) {
            cL.updateActConflicts(label);
        }
    }
}
=======
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author s133524
 */
public class AnnealingSimulatorSlider extends AnnealingSimulator {
    
    public AnnealingSimulatorSlider(int w, int h, ArrayList<Point> points, 
            ConflictList cL, Timer timer) {
        super(w, h, points, cL, timer);
    }
    
    @Override
    protected void moveLabelRandomly(SliderPoint p) {
        float random = rand.nextFloat();
        p.activeLabel.setPlacement(random);
    }
    
    @Override
    protected void computeScore() {
        
    }
    
}
>>>>>>> origin/master
