/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.Int;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import static java.lang.System.exit;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author s106010
 */
public class DataHandler {
    
    String                  fileName, delimiter;
    int                     lonPos, latPos;
    double                  scale; //scaling factor     
    ArrayList<GeoPoint>     pointCol = new ArrayList();  //pointcollection
    Map                     pointMap = new HashMap();   //point hashmap
    final static Charset    ENCODING = StandardCharsets.UTF_8;
    
    public DataHandler(String f, String d, int lonp, int latp) {
        fileName = f;  //filename of the dataset
        delimiter = d; //the delimiter used in the original dataset
        lonPos = lonp; //position at which the longitude finds itself in the delimited line
        latPos = latp; //idem
    }

    /**
     * Create point object from GPS coords of city
     * Convert GPS coords into cartesian coords
     */
    private void readLine(String nextLine) {
        String[] lineArray = nextLine.split(delimiter); //split the current line beased on the delimiter
        double longitude = Double.parseDouble(lineArray[lonPos - 1]); //convert the longitude (degrees in string) to double
        double latitude = Double.parseDouble(lineArray[latPos - 1]); //idem
        GeoPoint point = new GeoPoint(longitude, latitude); //create point object based on lon,lat
        pointMap.put(point.hashCode(), point);
    }
    
    /** 
     * Walks around in a spiral starting from a center point
     * @param max
     * @return 
     */
    void spiralWalk(int maxPoints) {
        int counter;            //counts the number of points found so far
        int heighestY = 0;      //heighest y so far
        int lowestY = 0;        //lowest y so far
        int rightMostX = 0;     //right most x so far
        int leftMostX = 0;      //left most x so far
        int upwards,rightwards,downwards,leftwards; //local loop indices
        GeoPoint newGeoPoint; //holds a local GeoPoint object
        
        /**
         * we start from (0,0) => go up => go right => go down => go left => repeat
         * thus, we have 4 cycles
         * we keep track of bounds on (lower, upper) x, and (lower,upper) y
         */
        
        counter = 1; //start the counter
        
        while(counter < maxPoints) {

            //start upwards motion
            for(upwards=lowestY+1; upwards<=heighestY+1; upwards++) {
                //System.out.println("x=" + leftMostX + " y=" + upwards);
                newGeoPoint = new GeoPoint(leftMostX, upwards);
                //two things are done below
                //first, a coinflip is done, deciding wether or not to include a certain point with a probability of .5 (to get randomized datasets)
                //second, we check if the dataset actually contains the point (which was stored earlier in a HashMap
                if(flipCoin() == 1 && pointMap.containsKey(newGeoPoint.hashCode())) {
                    pointCol.add(newGeoPoint);
                    counter++;
                }
                if(counter > maxPoints) {
                    break;
                }
            }
            heighestY++;

            //start rightwards motion
            for(rightwards=leftMostX + 1; rightwards<=rightMostX + 1; rightwards++) {
                //System.out.println("x=" + rightwards + " y=" + heighestY);
                newGeoPoint = new GeoPoint(rightwards, heighestY);
                if(flipCoin() == 1 && pointMap.containsKey(newGeoPoint.hashCode())) {
                    pointCol.add(newGeoPoint);
                    counter++;
                }
                if(counter > maxPoints) {
                    break;
                }
            }
            rightMostX++;

            //start downwards motion
            for(downwards=heighestY - 1; downwards>=lowestY - 1; downwards--) {
                //System.out.println("x=" + rightMostX + " y=" + downwards);
                newGeoPoint = new GeoPoint(rightMostX, downwards);
                if(flipCoin() == 1 && pointMap.containsKey(newGeoPoint.hashCode())) {
                    pointCol.add(newGeoPoint);
                    counter++;
                }
                if(counter > maxPoints) {
                    break;
                }
            }
            lowestY--;

            //start leftwards motion
            for(leftwards=rightMostX - 1; leftwards>=leftMostX - 1; leftwards--) {
                //System.out.println("x=" + leftwards + " y=" + lowestY);
                newGeoPoint = new GeoPoint(leftwards, lowestY);
                if(flipCoin() == 1 && pointMap.containsKey(newGeoPoint.hashCode())) {
                    pointCol.add(newGeoPoint);
                    counter++;
                }
                if(counter > maxPoints) {
                    break;
                }
            }
            leftMostX--;
        }
    }
    
    

    /**
     * Read the datafile with a limit on the number of points
     */
    void readDataFile(int limit) {
       Path path;
       path = Paths.get(fileName);
       int i = 0;
       try (Scanner scanner =  new Scanner(path, ENCODING.name())){
            while (scanner.hasNextLine() && i < limit){
              readLine(scanner.nextLine());
              i++;
            }      
       } catch(IOException e) {
           System.err.println("Caught IO-Exception: " + e.getMessage());
           exit(0);
       }
     }
    
    /**
     * Write current pointcollection to file, with a limit on the number of points
     */
    void writeToFile(String fn, int limit) {
        BufferedWriter writer;
        try {
            File file = new File(fn);
            if(!file.exists()) {
                file.createNewFile();
            } 
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"));
            int i = 0;
            for(Iterator<GeoPoint> p = pointCol.iterator(); i < limit || p.hasNext();) {
                GeoPoint point = p.next();
                if(i > 0) {
                    writer.newLine();
                }
                writer.write(point.getxCoord()+" "+point.getyCoord());
                i++;
            }  
            writer.close();
        } catch (IOException e) {
            System.err.println("Caught IOException: " + e.getMessage());
            exit(0);
        } finally {
        }
    }
    
    /**
    * Write current pointcollection to screen, with a limit on the number of points
    */
    void writeToScreen(int limit) {
        int i = 0;
        for(Iterator<GeoPoint> p = pointCol.iterator(); i < limit - 1;) {
            GeoPoint point = p.next();
            System.out.println(point.getxCoord()+" "+point.getyCoord());
            i++;
        }  
    }
   
    /**
     * Write current pointcollection to file, with a limit on the number of points
     * @return 
     */
    public ArrayList<GeoPoint> getPointCol() {
        return pointCol;
    }
    
    /**
     * Shuffle the point collection so that we get a different dataset each time 
     */
    public void ShufflePointCollection() {
        long seed = System.nanoTime();
        Collections.shuffle(pointCol, new Random(seed));
    }
    
     /**
     * Flush
     */
    public void flush() {
        pointCol = new ArrayList();
    }
    
    /**
     * Coin flipper, used as dataset randomizer
     * @return
     */
    public int flipCoin() {
        Random random = new Random();
        return random.nextInt(2);
    }
    
    /**
     *
     * @param args
     */
    
    /*public static void main(String args[]) {
        //ask for input file
        Scanner scanner = new Scanner(System.in);
        System.out.println("Give input filename (including path):");
        String inputFile = scanner.nextLine();
        System.out.println("Give a CSV delimiter string (leaving blank will default to \\t)");
        String separator = scanner.nextLine();
        if(separator.equals("")) {
            separator = "\t";
        }
        System.out.println("Give the position (counting from 1) of the longitude in the file line: (leaving blank will default to 5)");
        int lonPos = scanner.nextInt();
        if(lonPos == 0) {
           lonPos = 5;
        }
        System.out.println("Give the position (counting from 1) of the lattitude in the file line: (leaving blank will default to 6)");
        int latPos = scanner.nextInt();
        if(latPos == 0) {
           latPos = 6;
        }
        DataHandler dataObject = new DataHandler(inputFile, separator, lonPos, latPos);
        
        System.out.println("What's the maximum number of points you want to convert?");
        int numPoints = scanner.nextInt();

        dataObject.readDataFile(numPoints);

        //String inputFile = "D:\\\\Cloud\\\\2IO90 - DBL Algorithms\\\\Datasets\\\\cities5000\\cities5000.txt";
        //String outputFile = "D:\\\\Cloud\\\\2IO90 - DBL Algorithms\\\\Datasets\\test.txt";
        
        System.out.println("Do you want to print the output to screen (Y/N)? If your answer is no, you will be asked for an output filename.");
        scanner.nextLine(); //fire empty line
        String inputType = scanner.nextLine();  
        int numSets = 0;
        String opFile = "";
        if(inputType.equals("Y")) {
            dataObject.spiralWalk(numPoints);
            dataObject.writeToScreen(numPoints);
        } else if(inputType.equals("N")) {
            System.out.println("Give output filename including path. Please also provide an extension at the end of the filename.");
            opFile = scanner.nextLine(); 
            System.out.println("How many datasets do you want? (In case you want multiple sets, multiple files will written with an identifier.");
            numSets = scanner.nextInt();
            
        } else {
            dataObject.spiralWalk(numPoints);
            dataObject.writeToScreen(numPoints);
        }
        
        if(numSets > 1) {
            System.out.println("output file" + opFile);
            String[] fileArray = opFile.split("\\.");
            int numberOfSplits = fileArray.length;
            System.out.println("number of splits: " + numberOfSplits);
            String baseFilename = "";
            for(int filenameIndex = 0; filenameIndex < numberOfSplits - 1; filenameIndex++) {
                if(filenameIndex > 0) {
                    baseFilename += ".";
                }
                baseFilename += fileArray[filenameIndex];
            }
            
            for(int numSetIndex = 0; numSetIndex < numSets; numSetIndex++) {
                dataObject.spiralWalk(numPoints);
                String numerator = Integer.toString(numSetIndex);
                String filename = baseFilename + "_" + numerator + "." + fileArray[numberOfSplits - 1];
                dataObject.writeToFile(filename, numPoints);
                dataObject.flush();
            }
        } else {
            dataObject.spiralWalk(numPoints);
            dataObject.writeToFile(opFile, numPoints);
        }

        //dataObject.Plot(10000);
    }*/
    
}
