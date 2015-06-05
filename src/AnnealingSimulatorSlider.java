
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
        lastPosition = p.activeLabel.getPlacement();
        int oldLocalE = cL.getActDegree(p.activeLabel);
        if(oldLocalE > 0) {
            oldLocalE ++;
        }
        int newLocalE = 0;
        if(cL.getActConflictLabels(p.activeLabel) == null) {
            System.out.println("get null");
        }
        Set<Label> oldConflicts = new HashSet(cL.getActConflictLabels(p.activeLabel));
        Set<Label> newConflicts;
        
        if(complicatedScoring) {
            float random = rand.nextFloat();
            p.activeLabel.setPlacement(random);
            
            cL.updateActConflicts(p.activeLabel);
            newConflicts = new HashSet(cL.getActConflictLabels(p.activeLabel));
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
        } else {
            float random = rand.nextFloat();
            p.activeLabel.setPlacement(random);
            System.out.println(p.xCoord + " placement: " + random);

            newConflicts = new HashSet(cL.getActConflictLabels(p.activeLabel));
            newConflicts.addAll(oldConflicts); //now holds all labels affected
            for(Label label: newConflicts) {
                cL.updateActConflicts(label);
            }
            cL.updateActConflicts(p.activeLabel);
            newLocalE = cL.getActDegree(p.activeLabel);
            if(newLocalE > 0) {
                newLocalE ++;
            }
        }
        
        labelsAffectedLastChange.clear();
        labelsAffectedLastChange.addAll(newConflicts);
        labelsAffectedLastChange.addAll(oldConflicts);
        deltaE = oldLocalE - newLocalE;
        System.out.println(newLocalE);
    }
    
    @Override
    protected void computeInitialScore() {
        int tempE = 0;
        for(Point p: points) {
            if(cL.getActDegree(p.getActiveLabelSlider()) > 0) {
                tempE ++;
            }
        }
        System.out.println(tempE);
        E = tempE;
    }
    
    @Override 
    protected void undoLastPlacement() {
        lastPoint.getActiveLabelSlider().setPlacement(lastPosition);
        for(Label label : labelsAffectedLastChange) {
            cL.updateActConflicts(label);
        }
    }
    
    @Override
    protected void doInitialPlacement(){
        for (Point point : points) {
            if (point.getActiveLabelSlider() == null) {
                moveLabelRandomly((SliderPoint) point);
            }
        }
    }
}