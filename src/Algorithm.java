
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author s131061
 */
public abstract class Algorithm {

    //stores amount of labels placed
    int numLabels = 0;
    ArrayList<Point> points = new ArrayList<>();
    int w;
    int h;
    ConflictList cL;
    Timer timer;
    
    public Algorithm() {
        //needed for extending classes
    }

    public void setParameters(int w, int h, ArrayList<Point> points, ConflictList cL, Timer timer) {
        this.w = w;
        this.h = h;
        this.points = points;
        this.cL = cL;
        this.timer = timer;
    }
    
    /**
     * determines the positions of the labels
     *
     * @result \result = max amount of labels placed (using point.setLabelPos)
     * without overlap
     */
    public abstract void determineLabels();
    
    public void removeOverlap(){
        System.out.println("Started to remove overlap");
        while(true){
        //System.out.println("actConflict size: "+cL.actConflict.size());
            int i = 0;
            for(Label l:cL.actConflict.keySet()){
                i+=cL.actConflict.get(l).size();
            }
            System.out.println("total overlap: "+i);
            Label worstLabel = cL.activeLabels.get(0);
            //System.out.println("nr of active labels: "+cL.activeLabels.size());
            int worstDegree = 0;
            boolean noMoreOverlap = true;
            for(Label label : cL.activeLabels){
                int labelDegree = cL.getActDegree(label);
                if(labelDegree > worstDegree){
                    worstLabel = label;
                    worstDegree = labelDegree;
                    noMoreOverlap = false;
                }
            }
            System.out.println("degree: "+worstDegree+" minX: "+worstLabel.minX+" minY: "+worstLabel.minY);
            if(!noMoreOverlap){
                cL.removeActiveLabel(worstLabel);
                worstLabel.getPoint().setActiveLabelPos(new PosLabel(w, h, worstLabel.getPoint()));                
            }
            else{
                break;
            }
        }
    }

    public int getNumLabels() {
        return numLabels;
    }
    
    

    
}
