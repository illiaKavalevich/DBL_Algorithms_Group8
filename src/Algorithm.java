


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
    
    public Algorithm() {
        //needed for extending classes
    }

    /**
     * Creates a new Algorithm object
     *
     * @param w width of the labels
     * @param h height of the labels
     * @param points arraylist with all the points
     */
    public Algorithm(int w, int h, ArrayList<Point> points, ConflictList cL) {
        this.w = w;
        this.h = h;
        this.points = points;
        this.cL = cL;
    }

    /**
     * determines the positions of the labels
     *
     * @result \result = max amount of labels placed (using point.setLabelPos)
     * without overlap
     */
    public void determineLabels() {
        //place labels...
        
    }

    public int getNumLabels() {
        return numLabels;
    }

    
}
