import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author s133524
 */
public class AnnealingSimulatorSlider extends AnnealingSimulator {
    
    public AnnealingSimulatorSlider(int w, int h, ArrayList<Point> points, 
            ConflictList cL) {
        super(w, h, points, cL);
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
