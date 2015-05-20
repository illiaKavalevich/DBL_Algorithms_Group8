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
