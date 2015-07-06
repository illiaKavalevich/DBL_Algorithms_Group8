
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.System.exit;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author s106010
 */
public class TestFrame {
    
    final static Charset        ENCODING = StandardCharsets.UTF_8;
    ArrayList<Point>            points = new ArrayList<>();
    Algorithm alg;
    Quadtree2 q;
    ConflictDetector cD;
    Timer timer;
    
    public void readDatafile(String fileName, String model, int w, int h) {

        Path path;
        path = Paths.get(fileName);
        int i = 0;
        try (Scanner scanner =  new Scanner(path, ENCODING.name())){
            while (scanner.hasNextLine()){
              String currentLine = scanner.nextLine();
              String[] components = currentLine.split(" ");
              int x = (int) Float.parseFloat(components[0]);
              int y = (int) Float.parseFloat(components[1]);
               if (model.equals("1slider")) {
                    Point point = new SliderPoint(x, y, model, w, h);
                    points.add(point);
                } else {
                    Point point = new PosPoint(x, y, model, w, h);
                    points.add(point);
                }
              i++;
            }      
        } catch(IOException e) {
           System.err.println("Caught IO-Exception: " + e.getMessage());
           exit(0);
        }

    }
    
    public void processData(String model, int w, int h) {
         int numPoints = points.size();
        
        if (model.equals("2pos")) {
            alg = numPoints <= 15 ? new ExhaustiveSearch(): new Falp();
        } else if (model.equals("4pos")) {
            alg = numPoints <= 15 ? new ExhaustiveSearch(): new Falp();
        } else if (model.equals("1slider")) {
            if (numPoints >= 250) {
                alg = new AnnealingSimulatorSlider(null);
            } else {
                alg = new ClaimFreeDecorator(new AnnealingSimulatorSlider(new Falp()));
            }

        } else {
            System.out.println(model + " is not a valid model");
        }

        //timer.start();

        q = new Quadtree2();
        cD = new ConflictDetector(points, model, q);
        
        timer = new Timer(50000, alg);

        //alg = new AnnealingSimulatorSlider();
        alg.setParameters(w, h, points, cD, timer, model);
        alg.determineLabels();
        //System.out.println("--" + alg.getNumLabels() + "--");
        
    }

    public void processOutput(String outputFile, long timeElapsed) throws IOException {
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(outputFile, true)));
        alg.determineLabels();
        int numLabels = alg.getNumLabels();
        long runningTime = timeElapsed;
        out.println(alg.model + " " + numLabels + " " + alg.points.size() + " " + alg.w + " " + alg.h + " " + runningTime + "ms");
        points.clear();
        out.close();
    }
    
    public void processDatasets() throws IOException {
        
        //process all datasets with 2 pos models
        int datasetIndex = 5;
        while(datasetIndex <= 10000) {
            
            int w = 3;
            int h = 1;
            

            for(int index = 0; index < 3; index++) {
            
                String filename = "D:\\Cloud\\2IO90 - DBL Algorithms\\Datasets\\generated\\" + datasetIndex + " points\\set" + "_" + index + ".txt";
                readDatafile(filename, "2pos", w, h);
                System.out.println(points.size());
                long start = System.currentTimeMillis(); 
                processData("2pos", w, h);
                long elapsedTime = System.currentTimeMillis() - start;
                processOutput("D:\\\\Cloud\\\\2IO90 - DBL Algorithms\\\\Datasets\\\\output\\2pos_output.txt", elapsedTime);
                w++;
                h++;
   
            }
           
            
            if(datasetIndex < 100) {
                datasetIndex = datasetIndex + 5;
            } else if(datasetIndex >= 100 && datasetIndex < 500) {
                datasetIndex = datasetIndex + 100;
            } else if(datasetIndex >= 500 && datasetIndex < 3000) {
                datasetIndex = datasetIndex + 500;
            } else if(datasetIndex >= 3000 && datasetIndex < 5000) {
                datasetIndex = datasetIndex + 1000;
            } else {
                datasetIndex = datasetIndex + 5000;
            }
        }
        
        //process all datasets with 4 pos models
        datasetIndex = 5;
        while(datasetIndex <= 10000) {
            
            int w = 3;
            int h = 1;
            

            for(int index = 0; index < 3; index++) {
            
                String filename = "D:\\Cloud\\2IO90 - DBL Algorithms\\Datasets\\generated\\" + datasetIndex + " points\\set" + "_" + index + ".txt";
                readDatafile(filename, "4pos", w, h);
                long start = System.currentTimeMillis();  
                processData("4pos", w, h);
                long elapsedTime = System.currentTimeMillis() - start;
                processOutput("D:\\Cloud\\2IO90 - DBL Algorithms\\Datasets\\output\\4pos_output.txt", elapsedTime);
                w++;
                h++;
   
            }
           
            
            if(datasetIndex < 100) {
                datasetIndex = datasetIndex + 5;
            } else if(datasetIndex >= 100 && datasetIndex < 500) {
                datasetIndex = datasetIndex + 100;
            } else if(datasetIndex >= 500 && datasetIndex < 3000) {
                datasetIndex = datasetIndex + 500;
            } else if(datasetIndex >= 3000 && datasetIndex < 5000) {
                datasetIndex = datasetIndex + 1000;
            } else {
                datasetIndex = datasetIndex + 5000;
            }
        }
        
        //process all datasets with slider models
        datasetIndex = 5;
        while(datasetIndex <= 10000) {
            
            int w = 3;
            int h = 1;
            

            for(int index = 0; index < 3; index++) {
            
                String filename = "D:\\Cloud\\2IO90 - DBL Algorithms\\Datasets\\generated\\" + datasetIndex + " points\\set" + "_" + index + ".txt";
                readDatafile(filename, "1slider", w, h);
                long start = System.nanoTime(); 
                processData("1slider", w, h);
                long elapsedTime = System.currentTimeMillis() - start;
                processOutput("D:\\Cloud\\2IO90 - DBL Algorithms\\Datasets\\output\\1slider_output.txt", elapsedTime);
                w++;
                h++;
   
            }
           
            
            if(datasetIndex < 100) {
                datasetIndex = datasetIndex + 5;
            } else if(datasetIndex >= 100 && datasetIndex < 500) {
                datasetIndex = datasetIndex + 100;
            } else if(datasetIndex >= 500 && datasetIndex < 3000) {
                datasetIndex = datasetIndex + 500;
            } else if(datasetIndex >= 3000 && datasetIndex < 5000) {
                datasetIndex = datasetIndex + 1000;
            } else {
                datasetIndex = datasetIndex + 5000;
            }
        }


    }
    
    public static void main(String args[]) throws IOException {
        TestFrame testframe = new TestFrame();
        testframe.processDatasets();
    }
    
}
