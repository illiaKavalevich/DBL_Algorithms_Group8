import java.util.ArrayList;
import java.util.Random;

public class AnnealingSimulator2Pos extends AnnealingSimulatorDiscrete{
    int lastPosition;
    
    public AnnealingSimulator2Pos() {

    }
    
    @Override
    protected void moveLabelRandomly(Point p) {
        //needs update to take into account 
        int oldLocalE = cL.getActDegree(p.getActiveLabelPos());
        //if(p.)
        //p.getActiveLabelPos().
        deltaE = oldLocalE - cL.getActDegree(p.getActiveLabelPos());
    }
}