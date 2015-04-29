
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
public class Point {
    
    int xCoord;
    int yCoord;
    String labelPos = "NIL";
    

    /** 
    * Constructor
    * @param x x-coordinate of the point
    * @param y y-coordinate of the point
    */
    public Point(int x, int y){
        xCoord = x;
        yCoord = y;
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
    
    /**
    * Getter for label position of the point
    */
    public String getLabelPos() {
        return labelPos;
    }

    /**
    * Setter for label position of the point
    */
    public void setLabelPos(String labelPos) {
        this.labelPos = labelPos;
    }
    
    
}
