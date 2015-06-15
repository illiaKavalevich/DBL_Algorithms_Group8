/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author s125203
 */
  /*   x1 x2 x3 s1 s2 value
   s1 2  5  7  1  0 1000
   s2 5  4  3  0  1  800
   p -2 -3 -5  0  0    0
    
    2 5 7 
    5 4 3 is what gets passed on in A[][]
    
    c is the function p = 2 3 5 that is to p -2 -3 -5 
    s1, s2 are the slackvariables, each constraint has a slackvariable */
//A the matrix that is given
//b the initial values of each constraint
//c the values of function in the last row
public class SimplexNew {
    
    double[][] d;
    int[] b;
    int[] c;

   
    //create matrix and put everything in 
    public SimplexNew(int[][] A, int[] b, int[] c){
        d = new double[1+b.length+1][1+ b.length + c.length + 1]; 
       // addValuesconstraints();
        for(int i = 0; i < b.length; i++){
            for(int j = 0; j < c.length; j++){
                d[i+1][j+1] = A[i][j];
            }
        }
        //addVariabletypes();
        int labelnumber = 1;
        for(int i = 0; i < c.length; i++){
                d[0][1+i] = labelnumber;
                labelnumber++;
        }
        //addConstraintvalues();
        for(int i = 0; i < b.length; i++){
                d[i+1][1+b.length+c.length] = b[i];
        }
        //addSlackvariables();
        for(int i = 0; i < b.length; i++){
                d[1+i][1+c.length + i] = 1;
        }
        //addSlackvariabletypes
        int slackvariable = -1;
        for(int i = 0; i < b.length; i++){
                d[0][1 + c.length + i ] = slackvariable;
                d[i+1][0] = slackvariable;
                slackvariable--;
        }
        //addValuesfunction();
        for(int i = 0; i < c.length; i++){
                d[1 + b.length][1+i] = -c[i];
        }
        //setInitialfunctionvalue();
        d[1 + b.length][1 + b.length + c.length] = 0;
        
        this.b = b;
        this.c = c;
        compute();
        // used to print matrix initially
        for(int i = 0; i < 1+b.length+1; i++){
           for(int j = 0; j < 1 + b.length + c.length + 1; j++){
               System.out.print(d[i][j] + "\t");
             
           }
             System.out.println("\n");
    } 
        System.out.println(findpivotColumn());
        System.out.println(findpivotRow(findpivotColumn())); 
    }
    private void compute(){
        int pRow;
        int pCol;
        while(true){
            pCol = findpivotColumn();
            if(pCol!=0){
            pRow = findpivotRow(pCol);
            }
            else{
               break;
            }
            pivotting(pRow,pCol);
        }
    }
    
    private int findpivotColumn(){ //find most negative index column in the last row
        int lowestIndex = 0;
        double lowestValue = 0;
         for(int j = 0; j < this.c.length; j++){
              if((d[this.b.length+1][j+1] < lowestIndex) && d[this.b.length+1][j+1] < 0 ){
                lowestIndex = j+1;
                lowestValue = d[this.b.length+1][j+1];
            }
             
         }
         //will return 0 when all values are non negative
         return lowestIndex; 

    }
    private int findpivotRow(int x){
        int lowestIndex = 0;
        double lowestDivision = 0;
        for(int i = 0; i < this.b.length; i++){ 
            if(lowestDivision == 0){
                lowestDivision = d[i+1][1+this.b.length+this.c.length]/d[i+1][x];//first row will be first lowestDivision
                lowestIndex = i+1;
            }//cannot divide by 0 or use negative values to divide with
            else if(d[i+1][1+this.b.length+this.c.length]/d[i+1][x] < lowestDivision && d[i+1][x] > 0){
                lowestDivision = d[i+1][x];
                lowestIndex = i+1;
        }
        
    }
    return lowestIndex;
 }   
    private void pivotting(int u, int v){
        double x;
        
      //scale the pivot row
      for(int  j = 0; j < b.length+c.length+1;j++){
              if(j+1!=v){
          
                   d[u][j+1] = d[u][j+1]/d[u][v];
                  
              }
           
           
       }
     d[u][v] = 1.0; 
    
       //obtain zeros in the rest of the pivot column by row operations
        for(int i = 0; i < b.length +1; i++){//also the last row will be operated on
            if(i+1!=u){ //when not pivot row
           x = d[i+1][v]/d[u][v]; //get the value x for which holds d[i+1][v] - d[u][v]x = 0
            
            for(int j = 0; j < b.length + c.length + 1; j++ ){
                d[i+1][j+1] = d[i+1][j+1] - d[u][j+1]*x;
                }
            }
            
        } 
        d[u][0] = d[0][v]; 
 
      
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
        
        SimplexNew x = new SimplexNew(A, b, c);
       // Simplex x = new Simplex(A, b, c); //used for testing matrix
    }    
     public static void test2() {
        int[][] A = {
            { 2,  3,  2 },
            {  1,  1,  2 }
            
        };
        int[] c = { 7, 8, 10 };
        int[] b = { 1000, 800};
        
        SimplexNew x = new SimplexNew(A, b, c);
       // Simplex x = new Simplex(A, b, c); //used for testing matrix
    }    
      public static void test3() {
        int[][] A = {
            { 2,  1,  1 },
            {  1,  3,  2 },
            {2 , 1 , 2}
            
        };
        int[] c = { 6, 5, 4 };
        int[] b = { 180, 300,240};
        
        SimplexNew x = new SimplexNew(A, b, c);
       // Simplex x = new Simplex(A, b, c); //used for testing matrix
    }    
       public static void test4() {
        int[][] A = {
            { 2,  1,  1 },
            {  2,  3,  0 }
            
        };
        int[] c = { 3, 1};
        int[] b = { 8, 12};
        
        SimplexNew x = new SimplexNew(A, b, c);
       // Simplex x = new Simplex(A, b, c); //used for testing matrix
    }    
         public static void test5() {
        int[][] A = {
            { 2,  1},
            {  4,  3}
            
        };
        int[] c = { 7, 5};
        int[] b = { 100, 240};
        
        SimplexNew x = new SimplexNew(A, b, c);
       // Simplex x = new Simplex(A, b, c); //used for testing matrix
    }    
          public static void test6() {
        int[][] A = {
            { 2,  1,1},
            {  4, 2, 3},
            {2,5,5}
            
        };
        int[] c = { 1, 2,-1};
        int[] b = { 14, 28,30};
        
        SimplexNew x = new SimplexNew(A, b, c);
       // Simplex x = new Simplex(A, b, c); //used for testing matrix
    }    
            public static void test7() {
        int[][] A = {
            { 2,  1},
            { 2, 3},
            {3,1}
            
        };
        int[] c = { 3,2};
        int[] b = { 18, 42,24};
        
        SimplexNew x = new SimplexNew(A, b, c);
       // Simplex x = new Simplex(A, b, c); //used for testing matrix
    }    
              public static void test8() {
        int[][] A = {
            { 2,3},
            {  -3,2},
            {0,2},
            {2,0},
       
            
        };
        int[] c = { 4,3};
        int[] b = { 6,3,5,4};
        
        SimplexNew x = new SimplexNew(A, b, c);
       // Simplex x = new Simplex(A, b, c); //used for testing matrix
    }    
                        public static void test9() {
        int[][] A = {
            { 1,2},
            {  3,2},
       
        };
        int[] c = { 20,30};
        int[] b = { 10,18};
        
        SimplexNew x = new SimplexNew(A, b, c);
       // Simplex x = new Simplex(A, b, c); //used for testing matrix
    }    
                                  public static void test10() {
        int[][] A = {
            { 1,1,1},
            {  2,1,0},
            
       
        };
        int[] c = { 3,4};
        int[] b = { 4,5};
        
        SimplexNew x = new SimplexNew(A, b, c);
       // Simplex x = new Simplex(A, b, c); //used for testing matrix
    }    
       
     public static void main(String[] args) {

        test1();
         //test2();
         //test3();
         //test3();
     }
    
}
