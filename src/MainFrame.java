/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;


/**
 *
 * @author s131061
 */
public class MainFrame {

    String model;
    int w;
    int h;
    int numPoints;
    int numLabels;
    ArrayList<Point> points = new ArrayList<>();
    Iterator<Point> iter;
    Algorithm alg;

    public void readInput() {
        int firstPoint;
        int secondPoint;
        Scanner input = new Scanner(System.in);
        //pattern used to skip input "...: "
        String template = "\\n*[a-zA-Z\\s]*[^\\w\\s]\\s*";
        
        input.skip(template);
        model = input.next();
        input.skip(template);
        w = input.nextInt();
        input.skip(template);
        h = input.nextInt();
        input.skip(template);
        numPoints = input.nextInt();
        while (input.hasNext()) {
            firstPoint = input.nextInt();
            secondPoint = input.nextInt();
            points.add(new Point(firstPoint, secondPoint));
        }
    }
    public void processOutput() {
        if(model.equals("2pos")) {
            alg = new TwoPos(w, h, points);
        } else if (model.equals("4pos")) {
            alg = new FourPos(w, h, points);
        } else if (model.equals("1slider")) {
            alg = new OneSlider(w, h, points);
        } else {
            System.out.println(model + " is not a valid model");
        }
        alg.determineLabels();
        numLabels = alg.getNumLabels();
     
    }

    public void giveOutput() {
        //stores the current point being visited by the iterator
        Point curPoint;

        //print the required information
        System.out.println("placement model: " + model);
        System.out.println("width: " + w);
        System.out.println("height: " + h);
        System.out.println("number of points: " + numPoints);
        System.out.println("number of labels: " + numLabels);

        //creates new iterator to iterate over all points
        iter = points.iterator();

        //loop over all points to print their values
        while (iter.hasNext()) {
            curPoint = iter.next();
            System.out.println(curPoint.getxCoord() + " " + curPoint.getyCoord()
                    + " " + curPoint.getLabelPos());
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        MainFrame mainFrame = new MainFrame();
        mainFrame.readInput();
        mainFrame.processOutput();
        mainFrame.giveOutput();
    }

}
