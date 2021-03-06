/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author s131061
 */
public class PosLabel extends Label {
    String placement = "NIL";
    
    public PosLabel(int w, int h, Point p) {
        super(w, h, p);
    }
    

    public PosLabel(int w, int h, Point p, int quadrant) {
        super(w, h, p, quadrant);
        if (quadrant == 1) {
            placement = "NE";
        } else if (quadrant == 2) {
            placement = "NW";
        } else if (quadrant == 3) {
            placement = "SW";
        } else if (quadrant == 4) {
            placement = "SE";
        } else if (quadrant == 0) {
            placement = "#"; //needed for exhaustive search
        } 
    }
    
    public void setLabel(int quadrant) {
        this.quadrant = quadrant;
        switch (quadrant) {
            case 0:
                placement = "#";
                break;
            case 1:
                minX = p.getxCoord();
                minY = p.getyCoord();
                maxX = p.getxCoord() + w;
                maxY = p.getyCoord() + h;
                placement = "NE";
                break;
            case 2:
                minX = p.getxCoord() - w;
                minY = p.getyCoord();
                maxX = p.getxCoord();
                maxY = p.getyCoord() + h;
                placement = "NW";
                break;
            case 3:
                minX = p.getxCoord() - w;
                minY = p.getyCoord() - h;
                maxX = p.getxCoord();
                maxY = p.getyCoord();
                placement = "SW";
                break;
            case 4:
                minX = p.getxCoord();
                minY = p.getyCoord() - h;
                maxX = p.getxCoord() + w;
                maxY = p.getyCoord();
                placement = "SE";
                break;

        }
    }
            
    public String getPlacement() {
        return placement;
    }
    
    
}
