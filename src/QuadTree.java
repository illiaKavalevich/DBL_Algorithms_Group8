
import java.util.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author s130604
 */
public class QuadTree implements ConflictDetectorInterface {

    int minX = Integer.MAX_VALUE;
    int minY = Integer.MIN_VALUE;
    int maxX = Integer.MAX_VALUE;
    int maxY = Integer.MIN_VALUE;
    String model;
    int level;

    public QuadTree(List<Point> points, String model, int w, int h) {
        this.model=model;
        for (Point p : points) {
            if (p.xCoord - w < minX) {
                minX = p.xCoord - w;
            }
            if (p.xCoord + w > maxX) {
                maxX = p.xCoord + w;
            }
            if (p.yCoord - h < minY) {
                minY = p.yCoord - h;
            }
            if (p.yCoord + h > maxY) {
                maxY = p.yCoord + h;
            }
        }
    }

    public QuadTree(int minX, int maxX, int minY, int maxY, int level){
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
        this.level = level;
    }
    //copy constructor
    public QuadTree(QuadTree q) {

    }

    @Override
    public int getActDegree(Label l) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set<Label> getActConflictLabels(Label l) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removePoint(Point p) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateNotifyPre(Point p) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateNotifyPost() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
