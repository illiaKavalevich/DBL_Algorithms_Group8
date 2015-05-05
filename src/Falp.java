
import java.util.*;

public class Falp extends Algorithm{
    HashSet<Point> noActiveLabelPoints = new HashSet<>();                       //all the points that do not have an active label
    HashSet<Point> noOverlapPoints = new HashSet<>();                           //all the points that have an active label that doesn't overlap
    
    public Falp(int w, int h, ArrayList<Point> points, ConflictList cL) {
        super(w, h, points, cL);
    }
    
    //first step of the FALP algorithm: 
    //give a point an active label that has no overlap with if possible
    //otherwise take the label with the least overlap and remove the labels it has overlap with
    //the conflictlist that is used will be empty after this method, so a copy is needed
    public void removeConflicts(){
        ConflictList clCopy = new ConflictList(cL);
        HashSet<Point> activePointSet = new HashSet<>();                        //all points that still need to be examined by the algorithm
        for(Label label : clCopy.possibleLabels){
            noActiveLabelPoints.add(label.getPoint());
            activePointSet.add(label.getPoint());            
        }
        while(!(activePointSet.isEmpty())){
            for(Label l : clCopy.possibleLabels){
                if(clCopy.getPosDegree(l) == 0){
                    Point p = l.getPoint();
                    clCopy.removePoint(p);
                    activePointSet.remove(p);
                    noOverlapPoints.add(p);
                    //give p an active label (p needs to be a PosPoint for that)
                    
                }
            }
        }
    }
}
