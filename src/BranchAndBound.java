
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author s106010
 * This algorithm will give optimal solutions for the label placement problem
 * It makes use of the optimization algorithm "Simplex"
 */
public abstract class BranchAndBound extends Algorithm {
    
    protected List<PosLabel> ActiveLabelPositions = new ArrayList(); //set of active label positions
    
    public BranchAndBound(int w, int h, ArrayList<Point> points, ConflictList cL, Timer timer) {
       super(w, h, points, cL, timer);
    }

    protected void defineConflictRestrictions() {
        
    }
}
