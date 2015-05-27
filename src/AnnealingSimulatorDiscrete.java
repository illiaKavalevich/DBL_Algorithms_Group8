import java.util.ArrayList;
import java.util.Random;

public abstract class AnnealingSimulatorDiscrete extends AnnealingSimulator {
    int lastPosition;
    
    public AnnealingSimulatorDiscrete() {

    }
    
    protected void moveLabelRandomly(Point p){};
    
    @Override
    protected void computeInitialScore() {
        int tempE = 0;
        for(Point p: points) {
            if(cL.getActDegree(p.getActiveLabelPos()) > 0) {
                tempE ++;
            }
        }
        E = tempE;
    }
    
    @Override 
    protected void undoLastPlacement() {
        //TO DO
    }
    
    @Override
    protected void doInitialPlacement(){
        for (Point point : points) {
            if (point.getActiveLabelPos() == null) {
                //NEEDS UPDATE to make it general
                moveLabelRandomly(point);
            }
        }
    }
}
