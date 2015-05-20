import java.util.ArrayList;
import java.util.Random;

public abstract class AnnealingSimulatorDiscrete extends AnnealingSimulator {
    int lastPosition;
    
    public AnnealingSimulatorDiscrete(int w, int h, ArrayList<Point> points, 
            ConflictList cL, Timer timer) {
        super(w, h, points, cL, timer);
    }
    
    @Override
    protected abstract void moveLabelRandomly(Point p);
    
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
}
