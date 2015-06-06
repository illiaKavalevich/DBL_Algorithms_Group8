/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author s106010
 * The Simplex algorithm is used for optimization of some cost function
 * given a number of restrictions on the variables in that cost function
 * It's used as a helper algorithm in the exhaustive branch-and-bound algorithm
 */
public class Simplex {
    
    private double[][] t; //tableaux
    private int con; //number of constraints
    private int v; //number of variables
    private int[] basis; //print out solution
    private int[] b;
    private int[] c;
    
    //setting up simplex tableaux (matrix)
    // b contains the values of the constraints
    // c contains the function to maximize
    public Simplex(int[] c, int[][] A, int[] b) {
        this.b = b;
        this.c = c;
        con = b.length;
        v = c.length;
        t = new double[1+con+1][1+con+v+1]; //con+v is the original variables + slack variables
                                        // + 1 is for the not basisvariables 
        for(int i = 0; i < con; i++){
            for (int j = 0; j < v; j++){
              //copy values of A into t
              t[i+1][j+1] = A[i][j]; 
            } 
        }
        
       addSlackVariables(t);
       enumerateRowsColumns(t, this.b, this.c);
       /*
       basis = new int[con];
       for(int i = 0; i < con; i++){
           basis[i] = v + i;
       }
       */ //possibly not needed
       compute();
       
    }
    
    private void addSlackVariables(double t[][]) {
         this.t = t;
         for(int i = 0; i < con; i++){
            t[i+1][1+v+i] = 1.0;
        }
    }
    private void enumerateRowsColumns(double t[][], int b[], int c[]){
           //add on the right side of the matrix the values of the constraints
        this.t = t;
        for(int i = 0; i < con; i++){
            t[i+1][v+con+1] = b[i];
          
        }
        //add the function to maximize in the lowest row
        for(int i = 0; i < v; i++){
            t[con+1][i+1] = c[i];
        }
        
        //add in the first row the reference of each possible label
        int labelNr = 0;
        for(int i = 0; i < v; i++){
            t[0][i+1] = labelNr;
            labelNr++;
            
        }
        //add the slackvariable references
        int slackNr = 0;
        for(int i = 0; i < con; i++){
            t[0][1+v+i] = slackNr; 
            slackNr--; //to avoid confusion with variable references
        }
    }
    
    private void compute(){
        while(true){
         
            //find lowest value of last row (most negative value, derived from function to maximize
            int hP = getLowest();//horizontal pivot
            if (hP == -1) break; //optimal solution, done with computing
            //find pivot row which has the lowest ratio value of the column of getLowest()
            int vP = lowestRatio(hP);//vertical pivot
            //Gaussen-Jordan elimination
            gje(hP,vP);
        }
    }
    //initially non optimal solutions contain one or more -1s
    private int getLowest(){
        int currentLowest = 0; 
        for(int i = 0; i < v; i++){
            if(t[con+1][i+1] < 0){
                return i;
                
            }
            
        }
        return -1; //done no more negative values
    }
    //find the vertical index in the column of hP which gives the lowest ConstraintValue/vertical index
    private int lowestRatio(int c){
        double currentLowestRatio = -1;
        int index = -1;
        
        for(int i = 0; i < con; i++){
            if(currentLowestRatio == -1){
                currentLowestRatio =  t[i+1][con+v+1]/t[i+1][c+1]; //first input will be currentLowestRatio, assmued matrix t is not empty
                index = i;
                continue;
            }
            else if(t[i+1][c+1] <= 0){
            continue;
        }
            else if(t[i+1][con+v+1]/t[i+1][c+1] < currentLowestRatio){
               currentLowestRatio =  t[i+1][con+v+1]/t[i+1][c+1];
               index = i;
            } 
       
        }
        
        return index;
    }
    
    private void gje(int p, int q){ //Gauss-Jordan elimination with pivot hP and vP
        
        
    }
    
    void initialFeasibleSolution() {
        
    }
}
