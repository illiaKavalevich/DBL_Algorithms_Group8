import java.util.ArrayList;
import java.util.Random;

public class AnnealingSimulator4Pos extends AnnealingSimulatorDiscrete{
    int lastPosition;
    
    public AnnealingSimulator4Pos(int w, int h, ArrayList<Point> points, 
            ConflictList cL) {
        super(w, h, points, cL);
    }
    
    @Override
    protected void moveLabelRandomly(Point p) {
        //needs update to take into account 
        int oldLocalE = cL.getActDegree(p.getActiveLabelPos());
        float random = rand.nextFloat();
        //p.activeLabel.setPlacement(random);
        deltaE = oldLocalE - cL.getActDegree(p.getActiveLabelPos());
    }
}
