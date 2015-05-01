/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author s131061
 */
public class SliderPoint extends Point {
    /**
     * Holds the place of the actual placed label. In case of
     * 1slider, this label starts with the position of the one label in
     * possibleLabels, but can be moved over de X-as of the possibleLabel
     *
     */
    SliderLabel activeLabel;

    public SliderPoint(int x, int y, String model, int w, int h) {
        super(x, y, model, w, h);
        if (model.equals("1slider")) {
            possibleLabels.add(new PosLabel(w, h, this, 2));
            activeLabel = new SliderLabel(w, h, this, 2);
        } else {
            System.out.println("Point.Point: no valid model provided");
        }
    }
    
    @Override
    public PosLabel getActiveLabelPos() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SliderLabel getActiveLabelSlider() {
        return activeLabel;
    }
    
    
}
