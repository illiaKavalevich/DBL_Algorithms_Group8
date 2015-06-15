
import java.util.HashSet;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author s131061
 */
public class PosPoint extends Point {
    /**
     * Holds the place of the actual placed label. In case of 2pos or 4 pos,
     * this label is one of the labels contained in possibleLabels.
     */
    PosLabel activeLabel;

    
    
    public PosPoint(int x, int y, String model, int w, int h) {
        super(x, y, model, w, h);
        if (model.equals("2pos")) {
            possibleLabels.add(new PosLabel(w, h, this, 1));
            possibleLabels.add(new PosLabel(w, h, this, 2));
            activeLabel = new PosLabel(w, h, this);
        } else if (model.equals("4pos")) {
            possibleLabels.add(new PosLabel(w, h, this, 1));
            possibleLabels.add(new PosLabel(w, h, this, 2));
            possibleLabels.add(new PosLabel(w, h, this, 3));
            possibleLabels.add(new PosLabel(w, h, this, 4));
            activeLabel = new PosLabel(w, h, this);
        } else {
            System.out.println("PosPoint.PosPoint: no valid model provided");
        }
    }
    
    //Copy constructor
    public PosPoint(Point p){
        super(p);
        this.possibleLabels = new HashSet<>();
        for(Label l : p.possibleLabels){
            Label label = new PosLabel(l.w, l.h, this, l.quadrant);
            label.active = l.active;            
            this.possibleLabels.add(label);
        }
    }
    
    @Override
    public PosLabel getActiveLabelPos() {
        return activeLabel;
    }
    @Override
    public void setActiveLabelPos(PosLabel l){
        activeLabel = l;
    }

    @Override
    public SliderLabel getActiveLabelSlider() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
