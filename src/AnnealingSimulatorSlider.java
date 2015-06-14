
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
        int oldLocalE = cD.getActDegree(p.activeLabel);
        if(oldLocalE > 0) {
            oldLocalE ++;
        }
        int newLocalE = 0;
        if(cD.getActConflictLabels(p.activeLabel) == null) {
            //System.out.println("get null");
        }
        Set<Label> oldConflicts = new HashSet(cD.getActConflictLabels(p.activeLabel));
        Set<Label> newConflicts;
        
        if(complicatedScoring) {
            float random = rand.nextFloat();
            p.activeLabel.setPlacement(random);
            
            newConflicts = new HashSet(cD.getActConflictLabels(p.activeLabel));
            for(Label label : oldConflicts) {
                if(cD.getActDegree(label) > 0) {
                    newLocalE ++;
                }
                newConflicts.remove(label);
            }

            if(newConflicts.size() > 0) {
                newLocalE ++;
                for(Label label : newConflicts) {
                    if(cD.getActDegree(label) == 1) {
                        newLocalE ++;
                    }
                }
            }
        } else {
            float random = rand.nextFloat();
            p.activeLabel.setPlacement(random);
            //System.out.println(p.xCoord + " placement: " + random);

            newConflicts = new HashSet(cD.getActConflictLabels(p.activeLabel));
            newConflicts.addAll(oldConflicts); //now holds all labels affected
            newLocalE = cD.getActDegree(p.activeLabel);
            if(newLocalE > 0) {
                newLocalE ++;
            }
        }
        
        labelsAffectedLastChange.clear();
        labelsAffectedLastChange.addAll(newConflicts);
        labelsAffectedLastChange.addAll(oldConflicts);
        deltaE = oldLocalE - newLocalE;
        //System.out.println(newLocalE);
    }
    
    @Override
    protected void computeInitialScore() {
        int tempE = 0;
        for(Point p: points) {
            if(cD.getActDegree(p.getActiveLabelSlider()) > 0) {
                tempE ++;
            }
        }
        //System.out.println(tempE);
        E = tempE;
    }
    
    @Override 
    protected void undoLastPlacement() {
        lastPoint.getActiveLabelSlider().setPlacement(lastPosition);
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