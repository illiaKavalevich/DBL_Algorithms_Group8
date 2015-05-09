
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * 
 * @author s106010
 * 
 * Constructive Genetic Algorithm class
 * Provides an interface to calculate an estimative on the amount of possible labels without conflicts
 * 
 */
public abstract class CGA {

    protected int gMax;                     //g maximization upperbound
    protected double imParam;               //interval minimization parameter (d)
    protected double alpha;                 //treshold value selection
    protected List<LabelSchema> population = new ArrayList();      //population alpha of current structures and schemata (todo: define structure class, or edit point class
    int seed;
    int pointPosition;
    int quadrant;
    Random randomSeed; //will be used to generate random seed
    Random randomPoint; //will be used to generate random Point
    Random randomLabel; //will be used to generate random quadrant
    
    
    public CGA(int g, double d, double a) {
        gMax = g;
        imParam = d;
        alpha = a;
    }
    
    protected void ConstructiveGeneticAlgorithm() {
        
    }
    
    /**
     * A random population will be generate as a basis "gene pool"
     * A population is collection of schemata. 
     * In turn, schemata are sequences of labelpositions.
     * In case a point has no label, it's label position will be the string "#".
     * 
    */
    protected void GenerateInitialPopulation(ArrayList<Point> points, int numberOfSchemata, int numberOfPointFeatures) {
        int numberOfPoints = points.size();
        int i; //index for schemata generation
        for(i = 0; i < numberOfSchemata; i++) {
            population.add(new LabelSchema()); //add schema to population
            int j; //index for point feature label generation
            int[] labeledPoints;
            for(j = 0; j < numberOfPointFeatures; j++) { //randomly label points
                seed = randomSeed.nextInt(100000);
                pointPosition = randomPoint.nextInt(numberOfPoints);
                PosLabel posLabel = generateRandomPosLabel();
                population.get(i).addToSchema(posLabel);
                //todo: add point position to labeledpoints
            }
            
            //todo: add labelposition "#" to all labels except for the already labeled ones
        }
    }

    protected void Select() {
        
    }
    
    protected void Recombine() {
        
    }
    
    protected PosLabel generateRandomPosLabel() {
        return null;
        //todo: random position label
    }

}
