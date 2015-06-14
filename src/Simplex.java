/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author s106010 & s125203
 * The Simplex algorithm is used for optimization of some cost function
 * given a number of restrictions on the variables in that cost function
 * It's used as a helper algorithm in the exhaustive branch-and-bound algorithm
 */

public class Simplex {
    private static final double EPSILON = 1.0E-10;
    private double[][] t; //tableaux
    private int con; //number of constraints
    private int v; //number of variables
    private int[] b;
    private int[] c;
    private double[] result;
    private int[] basis;
    
    //setting up simplex tableaux (matrix)
    // b contains the values of the constraints
    // c contains the function to maximize
    /*
     x1 x2 x3 s1 s2 value
   s1 2  5  7  1  0 1000
   s2 5  4  3  0  1  800
   p -2 -3 -5  0  0    0
    
    2 5 7 
    5 4 3 is what gets passed on in A[][]
    
    c is the function p = 2 3 5 that is to p -2 -3 -5 
    s1, s2 are the slackvariables, each constraint has a slackvariable
    */
    public Simplex(int[][] A, int[] b, int[] c) {
        this.b = b;
        this.c = c; 
        con = b.length;
        v = c.length;
        result = new double[v+1];
        t = new double[1+con+1][1+con+v+1]; //con+v is the original variables + slack variables
                                        // + 1 is for the not basisvariables 
        for(int i = 0; i < con; i++){
            for (int j = 0; j < v; j++){
              //copy values of A into t
              t[i+1][j+1] = A[i][j]; 
            } 
        }
        
       t = addSlackVariables(t);
       enumerateRowsColumns(t, this.b, this.c); 
       /* //used for testing matrix
       for(int i = 0; i < 1+con+1; i++){
           for(int j = 0; j < 1 + con + v + 1; j++){
               System.out.print(t[i][j] + " ");
             
           }
             System.out.println("\n");
       } */
       
       basis = new int[con];
       for (int i = 0; i < con; i++){
           //basis[i] = v + i; //no idea why
           basis[i] = v+i;
       }
     
     compute(); 
       
       //makeResult();
       assert check(A,b,c); 
    }

  
    
    private double[][] addSlackVariables(double[][] t) {
       this.t = t;
         for(int i = 0; i < con; i++){
           this.t[i+1][1+v+i] = 1;
        }
         return this.t;
        
    }
    private double[][] enumerateRowsColumns(double[][] t, int[] b, int[] c){
       
           //add on the right side of the matrix the values of the constraints
        this.t = t;
        for(int i = 0; i < con; i++){
            this.t[i+1][v+con+1] = b[i];
          
        }
        //add the function to maximize in the lowest row
        for(int i = 0; i < v; i++){
            this.t[con+1][i+1] = -c[i];
        }
         //set initial value of the function to be 0
        this.t[1+con][1+con+v] = 0;
        
        //add in the first row the reference of each possible label
        double labelNr = 1;
        for(int i = 0; i < v; i++){
            this.t[0][i+1] = labelNr;
            labelNr++;
            
        }
        //add the slackvariable references
        int slackNr = -1;
        for(int i = 0; i < con; i++){
            this.t[0][1+v+i] = slackNr; 
            slackNr--; //to avoid confusion with variable references
        }
        return this.t;
    }
    
    private void compute(){
        while(true){
         
           // System.out.println("-");
            //find lowest value of last row (most negative value, derived from function to maximize
            int hP = getLowest();//horizontal pivot
            if (hP == -1) break; //optimal solution: there are no more negative values in the last row. Done with computing
            //find pivot row which has the lowest ratio value of the column of getLowest()
            int vP = lowestRatio(hP);//vertical pivot
            //Gaussen-Jordan elimination
            gje(vP,hP);
            
            //
            basis[vP-1] = hP-1; //undo +1 from getLowest and lowestRatio
        
        }
    } 
    //initially non optimal solutions contain one or more -1s
    private int getLowest(){
         /* int currentLowest = 0; 
        for(int i = 0; i < v; i++){
            if(t[con+1][i+1] < 0){
                return i+1;
                
            }
            
        } */
        int currentLowest = 0;
        for(int i = 0; i < v; i++){
            if((t[con+1][i+1] < currentLowest) && t[con+1][i+1] < 0 ){
                currentLowest = i+1;
            }
            
        }
        if(currentLowest == 0){ //done no more negative values
                return -1;
            }
            else{return currentLowest;} 
    }
    //find the vertical index in the column of hP which gives the lowest ConstraintValue/vertical index
    private int lowestRatio(int c){
        double currentLowestRatio = -1;
        int index = -1;
        
        for(int i = 0; i < con; i++){ //can't divide by 0
            if(t[i+1][c] <= 0){
            continue;//next iteration
            }
            else if(currentLowestRatio == -1){
                currentLowestRatio =  t[i+1][con+v+1]/t[i+1][c]; //first input will be currentLowestRatio, assmued matrix t is not empty
                index = i+1;
                continue;//next iteration
            }
            
        
            else if(t[i+1][con+v+1]/t[i+1][c] < currentLowestRatio){
               currentLowestRatio =  t[i+1][con+v+1]/t[i+1][c];
               index = i+1;
            } 
       
        }
        
        return index;
    }
    
    private void gje(int p, int q){ //Gauss-Jordan elimination with pivot hP and vP to get zeros in one column only
        double x = 0;
        double y = 0;
        for(int i = 0; i < con+1; i++){
            if(i+1!= p) { // if its not the pivot row
                x = t[i+1][q]/t[p][q]; // t[i+1][q]-t[p][q]x = 0
                for(int j = 0; j < v+con+1; j++) {
                    t[i+1][j+1] = t[i+1][j+1] - t[p][j+1]*x; //subtract row [p]x times from row i+1
                }
            } 
        }
        //if(t[p][q] != 0){ //scale the pivot row
            for(int n = 0; n < v+con+1; n++){
                t[p][n+1] = t[p][n+1]/t[p][q];
               
            }
       // }
        t[p][0] = t[0][q]; //replace (slack)variable with the variable in t[0][q] in which column the elemination was done
    }
    
    public double valueFunction(){
        
        return t[1+con][1+con+v];
    }
    
    
    public double[] primalSol(){ // return the values of each variable in the last column
       /* double[] a = new double[v];
        for (int i = 0; i < con; i++){
            if(basis[i] < v){
                a[basis[i]] = t[i][1+con+v];
            }
        }
        return a; */
        
        double[] a = new double[v];
        for (int i = 0; i < v; i++){
            a[(int) t[1+i][0]] = t[i+1][1+con+v]; //
        }
        return a; 
    }
    
    public double[] dual(){ //return the values of the function in the last row
        double[] l = new double[con];
        for(int i = 0; i < con; i++){
            l[i] = t[1+con][1+i];
        }
        return l;
    }
    
     // is the solution primal feasible?
    private boolean isPrimalFeasible(int[][] A, int[] b) {
        double[] x = primalSol();

        // check that x >= 0
        for (int j = 0; j < x.length; j++) {
            if (x[j] < 0.0) {
                System.out.println("x[" + j + "] = " + x[j] + " is negative");
                return false;
            }
        }

        // check that Ax <= b
        for (int i = 0; i < con; i++) {
            double sum = 0.0;
            for (int j = 0; j < v; j++) {
                sum += A[i][j] * x[j];
            }
            if (sum > b[i] + EPSILON) {
                System.out.println("not primal feasible");
                System.out.println("b[" + i + "] = " + b[i] + ", sum = " + sum);
                return false;
            }
        }
        return true;
    }
     // is the solution dual feasible?
    private boolean isDualFeasible(int[][] A, int[] c) {
        double[] y = dual();

        // check that y >= 0
        for (int i = 0; i < y.length; i++) {
            if (y[i] < 0.0) {
                System.out.println("y[" + i + "] = " + y[i] + " is negative");
                return false;
            }
        }

        // check that yA >= c
        for (int j = 0; j < v; j++) {
            double sum = 0.0;
            for (int i = 0; i < con; i++) {
                sum += A[i][j] * y[i];
            }
            if (sum < c[j] - EPSILON) {
                System.out.println("not dual feasible");
                System.out.println("c[" + j + "] = " + c[j] + ", sum = " + sum);
                return false;
            }
        }
        return true;
    }

    // check that optimal value = cx = yb
    private boolean isOptimal(int[] b, int[] c) {
        double[] x = primalSol();
        double[] y = dual();
        double value = valueFunction();

        // check that value = cx = yb
        double value1 = 0.0;
        for (int j = 0; j < x.length; j++)
            value1 += c[j] * x[j];
        double value2 = 0.0;
        for (int i = 0; i < y.length; i++)
            value2 += y[i] * b[i];
        if (Math.abs(value - value1) > EPSILON || Math.abs(value - value2) > EPSILON) {
            System.out.println("value = " + value + ", cx = " + value1 + ", yb = " + value2);
            return false;
        }

        return true;
    }
    
    
     private boolean check(int[][] A, int[] b, int[] c) {
        return isPrimalFeasible(A, b) && isDualFeasible(A, c) && isOptimal(b, c);
    }

     // print tableaux
    public void show() {
        System.out.println("con = " + con);
        System.out.println("v = " + v);
        for (int i = 0; i <= con; i++) {
            for (int j = 0; j <= con + v; j++) {
                System.out.printf("%7.2f ", t[i+1][j+1]);
            }
            System.out.println();
        }
        System.out.println("value = " + valueFunction());
        for (int i = 0; i < con; i++)
            if (basis[i] < v) System.out.println("x_" + basis[i] + " = " + t[i+1][1+con+v]);
        System.out.println();
    }
    
    public static void test(int[][] A, int[] b, int[] c) {
        Simplex lp = new Simplex(A, b, c);
        System.out.println("value = " + lp.valueFunction());
        double[] x = lp.primalSol();
        for (int i = 0; i < x.length; i++)
            System.out.println("x[" + i + "] = " + x[i]);
        double[] y = lp.dual();
        for (int j = 0; j < y.length; j++)
            System.out.println("y[" + j + "] = " + y[j]);
    }

    public static void test1() {
        int[][] A = {
            { -1,  1,  0 },
            {  1,  4,  0 },
            {  2,  1,  0 },
            {  3, -4,  0 },
            {  0,  0,  1 },
        };
        int[] c = { 1, 1, 1 };
        int[] b = { 5, 45, 27, 24, 4};
        
        test(A,b,c);
       // Simplex x = new Simplex(A, b, c); //used for testing matrix
    }
    
      // x0 = 12, x1 = 28, opt = 800
   /* public static void test2() {
        double[] c = {  13.0,  23.0 };
        double[] b = { 480.0, 160.0, 1190.0 };
        double[][] A = {
            {  5.0, 15.0 },
            {  4.0,  4.0 },
            { 35.0, 20.0 },
        };
        test(A, b, c);
    }
     // unbounded
    public static void test3() {
        double[] c = { 2.0, 3.0, -1.0, -12.0 };
        double[] b = {  3.0,   2.0 };
        double[][] A = {
            { -2.0, -9.0,  1.0,  9.0 },
            {  1.0,  1.0, -1.0, -2.0 },
        };
        test(A, b, c);
    }

    // degenerate - cycles if you choose most positive objective function coefficient
    public static void test4() {
        double[] c = { 10.0, -57.0, -9.0, -24.0 };
        double[] b = {  0.0,   0.0,  1.0 };
        double[][] A = {
            { 0.5, -5.5, -2.5, 9.0 },
            { 0.5, -1.5, -0.5, 1.0 },
            { 1.0,  0.0,  0.0, 0.0 },
        };
        test(A, b, c);
    } */
     
    public void makeResult(){

        for(int i = 0; i < con; i++){
            result[(int)t[i+1][0]] = t[i+1][1+con+v];
            
        }
        result[v] = this.t[1+con][1+con+v]; //add function value
        System.out.println("size of result: " + result.length + " v: " + v);
    }
    
   
    
   /* public static void main(String args[]) {
        int[] c = new int[4];
        c[0] = c[1] = c[2] = 0;
        c[3] = 5;
        int[] b = new int[1];
        b[0] = 4;
        int[][] t = new int[1][4];
        t[0][0] = 1;
        t[0][1] = 2;
        t[0][2] = 1;
        t[0][3] = 4;
        
        Simplex simplex = new Simplex(c, t, b);
        double[] result = simplex.returnResult();
        for(double res : result) {
            System.out.println(res + "-");
        }
    }
    */
     public static void main(String[] args) {

        test1();
         //test2();
           //test3();
         //test4();
    }
   
}
