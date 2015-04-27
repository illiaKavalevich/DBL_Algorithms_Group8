
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

    /**
     * determines the positions of the labels
     *
     * @param w width of the labels
     * @param h height of the labels
     * @param points arraylist with all the points
     * @result \result = max amount of labels placed (using point.setLabelPos)
     * without overlap
     */
    public void determineLabels(int w, int h, ArrayList<Point> points) {

    }

    public int getNumLabels() {
        return numLabels;
    }
}
