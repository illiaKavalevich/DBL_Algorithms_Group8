/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author s131061
 */
public class SliderLabel extends Label {

    Float placement = (float)0; //returns float number in range [0, 1]
    Float miniX, maxiX;

    //sets the xcoordinates of the label to float for sliding purposes
    public SliderLabel(int w, int h, Point p, int quadrant) {
        super(w, h, p, quadrant);
        miniX = (float) minX;
        maxiX = (float) maxX;
        active = true;
    }

    public Float getPlacement() {
        return placement;
    }

    public void deactivate() {
        active = false;
    }
    
    //changes xcoordinates of label when sliding
    public void setPlacement(Float placement) {
            this.placement = placement;
            miniX = p.xCoord - w + placement * w;
            maxiX = p.xCoord + placement * w;
    }
    
    public float getMaxiX() {
        return maxiX;
    }
    
    public float getMiniX() {
        return miniX;
    }
    
    public boolean overlaps(SliderLabel l) {
        return miniX < l.getMaxiX() && maxiX > l.getMiniX() && minY < l.getMaxY() && maxY > l.getMinY();
    }

}
