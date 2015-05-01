
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
    ArrayList<Pair> pairs = new ArrayList<>();
    int w;
    int h;

    /**
     * Creates a new Algorithm object
     *
     * @param w width of the labels
     * @param h height of the labels
     * @param points arraylist with all the points
     */
    public Algorithm(int w, int h, ArrayList<Point> points) {
        this.w = w;
        this.h = h;
        this.points = points;
        setPairs();
    }

    /**
     * determines the positions of the labels
     *
     * @result \result = max amount of labels placed (using point.setLabelPos)
     * without overlap
     */
    public void determineLabels() {
        //place labels...
        //then final check for correctness
        if (!checkCorrect()) {
            System.out.println("WRONG! overlap in labels found!");
        }
    }

    /**
     * checks if there is no overlap between labels
     *
     * @return true if there are no labels that overlap
     */
    public boolean checkCorrect() {
        //check for all pairs if their labels overlap
        for (Pair pair : pairs) {
            //if one has no label, jump to next pair
            if (hasNil(pair.p1.getLabelPos(), pair.p2.getLabelPos())) {
                continue;
                //if both labelPos' are same
            } else if (sameLabelPos(pair.p1.getLabelPos(), pair.p2.getLabelPos())) {
                if (Math.abs(pair.p1.getxCoord() - pair.p2.getxCoord()) >= w) {
                } else if (Math.abs(pair.p1.getyCoord() - pair.p2.getyCoord()) >= h) {
                } else {
                    return false;
                }
                //if both labelPos' are on the same horizontal side (north or south)    
            } else if (sameHorSide(pair.p1.getLabelPos(), pair.p2.getLabelPos())) {
                if (Math.abs(pair.p1.getyCoord() - pair.p2.getyCoord()) >= h) {
                } else if (Math.abs(pair.p1.getxCoord() - pair.p2.getxCoord()) >= 2*w) {
                } else if (pair.p1.getxCoord() > pair.p2.getxCoord()) {
                    if (pair.p1.getLabelPos().charAt(1) != 'E') {
                        return false;
                    }
                } else if (pair.p1.getxCoord() < pair.p2.getxCoord()) {
                    if (pair.p1.getLabelPos().charAt(1) != 'W') {
                        return false;
                    }
                }
                //if both labelPos' are on the same vertical side (west or east)
            } else if (sameVerSide(pair.p1.getLabelPos(), pair.p2.getLabelPos())) {
                if (Math.abs(pair.p1.getxCoord() - pair.p2.getxCoord()) >= w) {
                } else if (Math.abs(pair.p1.getyCoord() - pair.p2.getyCoord()) >= 2*h) {
                } else if (pair.p1.getyCoord() > pair.p2.getyCoord()) {
                    if (pair.p1.getLabelPos().charAt(0) != 'N') {
                        return false;
                    }
                } else if (pair.p1.getyCoord() < pair.p2.getyCoord()) {
                    if (pair.p1.getLabelPos().charAt(0) != 'S') {
                        return false;
                    }
                }
                //if both labelPos' are diagonally placed    
            } else if (areDiag(pair.p1.getLabelPos(), pair.p2.getLabelPos())) {
                if (Math.abs(pair.p1.getxCoord() - pair.p2.getxCoord()) >= 2*w) {
                } else if (Math.abs(pair.p1.getyCoord() - pair.p2.getyCoord()) >= 2*h) {
                }//to continue..
            
            }

        }
        return true;
    }

    /**
     * Creates all possible pairs out of all of the points
     */
    public void setPairs() {
        boolean isNew = true;

        //add all possible points
        for (Point point1 : points) {
            for (Point point2 : points) {
                /**
                 * for each pair check if it does not already exist the other
                 * way around (i.e pair(p1, p2) is same as (pair(p2, p1)) if
                 * not, add to pairs
                 */
                for (Pair pair : pairs) {
                    if (pair.p1.equals(point2) && pair.p2.equals(point1)) {
                        isNew = false;
                        break;
                    }
                }
                if (isNew) {
                    pairs.add(new Pair(point1, point2));
                }
            }
        }
    }

    public int getNumLabels() {
        return numLabels;
    }

    /**
     * Checks if either labelPos is NIL
     *
     * @param pos1 first labelPos
     * @param pos2 second labelPos
     * @return true if at least one labelPos is NIL
     */
    public boolean hasNil(String pos1, String pos2) {
        return pos1.equals("NIL") || pos2.equals("NIL");
    }

    /**
     * Checks if both labelPos are the same
     *
     * @param pos1 first labelPos
     * @param pos2 second labelPos
     *
     * @return true if both labelPos are the same
     */
    public boolean sameLabelPos(String pos1, String pos2) {
        return pos1.equals(pos2);
    }

    /**
     * Checks if both labelPos are north or both are south
     *
     * @param pos1 first labelPos
     * @param pos2 second labelPos
     *
     * @return true if both are "N." or both are "S."
     */
    public boolean sameHorSide(String pos1, String pos2) {
        return (pos1.startsWith("N") && pos1.startsWith("N"))
                || (pos1.startsWith("S") && pos1.startsWith("S"));
    }

    /**
     * Checks if both labelPos are west or both are east
     *
     * @param pos1 first labelPos
     * @param pos2 second labelPos
     *
     * @return true if both are ".W" or both are ".E"
     */
    public boolean sameVerSide(String pos1, String pos2) {
        return (pos1.endsWith("W") && pos1.endsWith("W"))
                || (pos1.endsWith("E") && pos1.endsWith("E"));
    }

    /**
     * Checks if labelPos are diagonally placed
     *
     * @param pos1 first labelPos
     * @param pos2 second labelPos
     *
     * @return true for combinations (NW SE) and (NE SW)
     */
    public boolean areDiag(String pos1, String pos2) {
        return (pos1.charAt(0) != (pos2.charAt(0)))
                && (pos1.charAt(1) != (pos2.charAt(1)));
    }
}
