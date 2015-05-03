
import java.util.*;
/*
problems:
original conflict list will be gone after first step and is needed in later steps
conflictList needs method remove label rather than point 
    (when the author used 'node', it apparently referred to a label and not to a point...)
*/
public class Falp extends Algorithm{
    HashSet<Point> noActiveLabelPoints = new HashSet<>();                       //all the points that do not have an active label
    HashSet<Point> noOverlapPoints = new HashSet<>();                           //all the points that have an active label that doesn't overlap
    
    public Falp(int w, int h, ArrayList<Point> points, ConflictList cL) {
        super(w, h, points, cL);
    }
    
    //first step of the FALP algorithm: 
    //give a point an active label that has no overlap with if possible
    //otherwise take the label with the least overlap and remove the labels it has overlap with
    public void removeConflicts(){
        HashSet<Point> activePointSet = new HashSet<>();                        //all points that still need to be examined by the algorithm
        for(Label label : cL.possibleLabels){
            noActiveLabelPoints.add(label.getPoint());
            activePointSet.add(label.getPoint());            
        }
        while(!(activePointSet.isEmpty())){
            
        }
    }
}
