/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author s106010
 * This class is independent of Point
 * It is necessary to work with both cartesian and spherical coordinates used in datasets
 */
public final class GeoPoint {
      
    double longitude, latitude; //spherical coordinates (decimal representation)
    int x, y;                   //cartesian coordinates (integer approximations)
    int R = 6371;               //earths radius
    int value;
    
    /**
     *
     * @param lon
     * @param lat
     */
    public GeoPoint(double lon, double lat) {
        longitude = lon;
        latitude = lat;
        convertToCartesian();
    }
    
    /**
     *
     * @param xCoord
     * @param yCoord
     */
    public GeoPoint(int xCoord, int yCoord) {
        x = xCoord;
        y = yCoord;
    }

    /**
     *
     * @return
     */
    public double getLon() {
        return longitude;
    }
    
    /**
     *
     * @return
     */
    public double getLat() {
        return latitude;
    }
    
    /**
     *
     * @param lon
     */
    public void setLon(double lon) {
        longitude = lon;
    }
    
    /**
     *
     * @param lat
     */
    public void setLat(double lat) {
        latitude = lat;
    }
   
    /**
     *
     * @param xCoord
     */
    public void setxCoord(int xCoord) {
        x = xCoord;
    }
    
    /**
     *
     * @param yCoord
     */
    public void setyCoord(int yCoord) {
        y = yCoord;
    }
    
    /**
     *
     * @return
     */
    public double getxCoord() {
        return x;
    }
    
    /**
     *
     * @return
     */
    public double getyCoord() {
        return y;
    }
    
    /**
     *
     */
    public void convertToCartesian() {
        /**
         * Conversion according
         * x = R * cos(lat) * cos(lon)
         * y = R * cos(lat) * sin(lon)
         * z = R * sin(lat)
         */
        
        double lonRadians = longitude * 180 / Math.PI; //convert to rad
        double latRadians = latitude * 180 / Math.PI; //idem
        int xCoord = (int) (R * Math.cos(latRadians) * Math.cos(lonRadians)); //approximate x coord
        int yCoord = (int) (R * Math.cos(latRadians) * Math.sin(lonRadians)); //approximate y coord
        setxCoord(xCoord); //set the x coord
        setyCoord(yCoord); //set the y coord
        
    }

    /*Methods below are necessary for a custom GeoPoint hashmap*/
    
    @Override
    public int hashCode() {
        //Cantor pairing function f:(NxN)->N:(k1,k2)->f(k1,k2) = 1/2(k1+k2)(k1+k2+1)+k2 
        return (1/2) * (x + y) * (x + y + 1) + y;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof GeoPoint) {   
            GeoPoint newPoint = (GeoPoint) obj;
            return x == newPoint.getxCoord() && y == newPoint.getyCoord();
        } else {
            return false;
        }
    }
}
