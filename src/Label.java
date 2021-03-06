/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author s130604
 */
public abstract class Label {

    int minX, minY, maxX, maxY;
    int w;
    int h;
    Point p;    //the point this label labels
    int quadrant;
    boolean active = false;

    
    public Label(int w, int h, Point p) {
        this.w = w;
        this.h = h;
        this.p = p;
    }

    public Label(int w, int h, Point p, int quadrant) {
        this.w = w;
        this.h = h;
        this.p = p;
        this.quadrant = quadrant;
        switch (quadrant) {
            case 1:
                minX = p.getxCoord();
                minY = p.getyCoord();
                maxX = p.getxCoord() + w;
                maxY = p.getyCoord() + h;
                break;
            case 2:
                minX = p.getxCoord() - w;
                minY = p.getyCoord();
                maxX = p.getxCoord();
                maxY = p.getyCoord() + h;
                break;
            case 3:
                minX = p.getxCoord() - w;
                minY = p.getyCoord() - h;
                maxX = p.getxCoord();
                maxY = p.getyCoord();
                break;
            case 4:
                minX = p.getxCoord();
                minY = p.getyCoord() - h;
                maxX = p.getxCoord() + w;
                maxY = p.getyCoord();
                break;

        }
    }
    
    

    public int getMinX() {
        return minX;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMinY() {
        return minY;
    }

    public int getMaxY() {
        return maxY;
    }

    public Point getPoint() {
        return p;
    }

    public boolean overlaps(Label l) {
        return minX < l.getMaxX() && maxX > l.getMinX() && minY < l.getMaxY() && maxY > l.getMinY();
    }

    public int getQuadrant() {
        return quadrant;
    }

}
