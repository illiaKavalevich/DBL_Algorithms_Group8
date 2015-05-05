
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author s131061
 */
public abstract class Point {

    int xCoord;
    int yCoord;
    /**
     * Holds all possible places a label can be at for the 2pos or 4 pos model.
     * For 2pos model, this is the NW and NE place. For the 4pos model
     * this is the NW, NE, SW and SE place. However, for the 1slider model
     * only the NW place is added, because the actual label's bottomleft corner
     * will always be placed somewhere on this X-as.
     *
     */
    HashSet<Label> possibleLabels;

    /**
     * Constructor
     *
     * @param x x-coordinate of the point
     * @param y y-coordinate of the point
     */
    public Point(int x, int y, String model, int w, int h) {
        xCoord = x;
        yCoord = y;
        possibleLabels = new HashSet<>();
    }

    /**
     * Getter for x coordinate
     */
    public int getxCoord() {
        return xCoord;
    }

    /**
     * Getter for y coordinate
     */
    public int getyCoord() {
        return yCoord;
    }
    
    public HashSet<Label> getPossibleLabels() {
        return possibleLabels;
    }
    
    public abstract Label getActiveLabelPos();
    
    public abstract void setActiveLabelPos(Label l);
    
    public abstract SliderLabel getActiveLabelSlider();

}
