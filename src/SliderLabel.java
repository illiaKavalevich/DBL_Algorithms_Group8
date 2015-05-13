

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

    Float placement; //returns float number in range [0, 1]
    Float miniX, maxiX;

    //sets the xcoordinates of the label to float for sliding purposes
    public SliderLabel(int w, int h, Point p, int quadrant) {
        super(w, h, p, quadrant);
        miniX = (float) minX;
        maxiX = (float) maxX;
    }

    public Float getPlacement() {
        return placement;
    }

    //changes xcoordinates of label when sliding
    public void setPlacement(Float placement) {
        if (miniX + placement > maxX || maxiX + placement < minX) {
            System.out.println("SliderLabel.setPlacement: invalid placement provided");
        } else {
            this.placement = placement;
            miniX = miniX + placement;
            maxiX = maxiX + placement;
        }

    }

}
