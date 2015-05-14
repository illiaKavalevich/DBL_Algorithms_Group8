
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
 */
public abstract class ExhaustiveSearch extends Algorithm {
    
    protected List<PosLabel> ActiveLabelPositions = new ArrayList(); //set of active label positions
    
    public ExhaustiveSearch() {
       
        
    }
    
    protected int computeNC() {
        //compute number of labels with conflicts in ActiveLabelPositions
        return 0;
    }
    
    protected int computeNSC() {
        //compute number of no
        return 0;
    }
    
    protected void determineLabels(int w, int h, ArrayList<Point> points) {
        
        
    }
}
