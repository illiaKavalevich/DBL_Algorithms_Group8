import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author s106010
 * This algorithm will give optimal solutions for the label placement problem
 * It makes use of the optimization algorithm "Simplex"
 */
public class BranchAndBound extends Algorithm {

    int numberOfLabels;
    int numberOfLabelsPerPoint;
    int numberOfPoints;
    int[][] currentVarValues;
    double[][] t; //will contain the tableau in array form
    double[] b; //will contain the constraints values in array form
    double[] c; //will contain the target function constants in array form
    int nextLabel = 0; //the label that will be looked at next
    double currentRelaxationValue; //contains the highest solution so far
    ArrayList<double[]> tableau = new ArrayList<>(); //tableau builder ArrayList 
    ArrayList<Integer> cValues = new ArrayList<>(); //constraints values builder  ArrayList
    ArrayList<Integer> targetValues = new ArrayList<>(); //target function values builder ArrayList
    HashMap<Integer, Integer> labelPositions = new HashMap<>(); //used to obtain unique identifiers for labels
    HashMap<Integer, Integer> pointPositions = new HashMap<>(); //used to obtain unique identifiers for points
    ArrayList<Label> labelList = new ArrayList(); //list of all labels
    HashMap<Integer, Boolean> alreadyConstrained = new HashMap<>(); //this hashmap contains pairs of already constrained variables
    SimplexOri simplex; // placeholder for the simplex algorithm object
    Boolean finished = false; // state of the algorithm
    Quadtree2 quadTree;
    
    public BranchAndBound() {
        super();
    }
    
    @Override
    public void determineLabels() {
        setNumberOfLabels();
        initCurVarValues();
        buildInitialTableau();
        solve();
        setActiveLabels();
    }
    
    /*
     * This method solves the integer program recursively
     */
    public final void solve() {
        
        while(!finished()) {
            addNewEqualityConstraint(nextLabel, 0);
            rebuildTableau();
            simplex = new SimplexOri(t, b, c);
            double[] relaxationLeft = simplex.getPrimalResult();
            boolean feasibleOne = fathomTest(relaxationLeft);

            addNewEqualityConstraint(nextLabel, 1);
            rebuildTableau();
            simplex = new SimplexOri(t, b, c);
            double[] relaxationRight = simplex.getPrimalResult();
            boolean feasibleTwo = fathomTest(relaxationRight);
            
            if(relaxationLeft[relaxationLeft.length - 1] >= relaxationRight[relaxationRight.length - 1]) {
                setVariable(labelList.get(nextLabel), 0);
                setCurrentRelaxationValue(relaxationLeft[relaxationLeft.length - 1]);
            } else {
                setVariable(labelList.get(nextLabel), 1);
                setCurrentRelaxationValue(relaxationRight[relaxationRight.length - 1]);
            }
        }
    }
    
  /*
     * This method solves the integer program recursively
     */
    public final void setActiveLabels() {
        
        for(int pointIndex = 0; pointIndex < currentVarValues.length; pointIndex++) {
            for(int labelIndex = 0; labelIndex < currentVarValues[pointIndex].length - 1; labelIndex++) {
                int labelPosition = pointIndex * numberOfLabelsPerPoint + labelIndex;
                Label label = labelList.get(labelPosition);
                label.active = true;
            }
        }
    }
    
    /*
     * This method intializes the currentVarValues variable
     * All var values are set to 5. 5 is the default value and means a variable is unset (since we have an integer array)
     * The default value of an int array element is 0, but this doesn't work out for us because 0 is a feasible value too
     */
    public final void initCurVarValues() {
        currentVarValues = new int[numberOfPoints][numberOfLabelsPerPoint];
        Arrays.fill(currentVarValues, 5);
    }
    
    /*
     * This method uniquely combines three integers
     */
    public int cantorPair(int a, int b, int c) {
        //Cantor pairing function f:(NxN)->N:(k1,k2)->f(k1,k2) = 1/2(k1+k2)(k1+k2+1)+k2 
        //We apply this function twice to uniquely combine the three integers above
        int hashCode = (1/2) * (a + b) * (a + b + 1) + b; //first
        hashCode = (1/2) * (hashCode + c) * (hashCode + c + 1) + c; //second
        return hashCode;
    }
    
        /*
     * This method uniquely combines two integers
     */
    public int cantorPair(int a, int b) {
        //Cantor pairing function f:(NxN)->N:(k1,k2)->f(k1,k2) = 1/2(k1+k2)(k1+k2+1)+k2 
        int hashCode = (1/2) * (a + b) * (a + b + 1) + b;
        return hashCode;
    }
    
    /*
     * This method builds initial tableau (restrictions), looking at all actual conflicts
     */
    public void createLabelList() {
        int labelIndex = 0;
        int pointIndex = 0;
        for (Point p : points) { //iterate over all points
            pointIndex++;
            int xCoord = p.getxCoord();
            int yCoord = p.getyCoord();
            int hashCode = cantorPair(xCoord, yCoord);
            pointPositions.put(hashCode, pointIndex); //add the label position t the hashset
            for (Label l : p.possibleLabels) { //iterate over labels within that point
                labelIndex++;    
                //we use quadrant and (x,y) as unique identifiers of a certain label
                int quadrant = l.getQuadrant();
                hashCode = cantorPair(quadrant, xCoord, yCoord);
                labelPositions.put(hashCode, labelIndex); //add the label position t the hashset
                labelList.add(l); //add the label itself to the labellist
            }
        }
    }
    
    /*
     * This method will return the position of a label in the global label list
     */
    public int getLabelPosition(Label l, Boolean pointRelative) {
        int quadrant = l.getQuadrant();
        int x = l.getPoint().getxCoord();
        int y = l.getPoint().getyCoord();
        int hashCode = cantorPair(quadrant, x, y);
        int pos = labelPositions.get(hashCode);
        if(!pointRelative) {
            return pos;
        } else {
            pos = pos % numberOfLabelsPerPoint + 1;
            return pos;
        }
    }
    
    /*
     * This method will return the position of a label in the global point list
     */
    public int getPointPosition(Point p) {
        int x = p.getxCoord();
        int y = p.getyCoord();
        int hashCode = cantorPair(x, y);
        return labelPositions.get(hashCode);
    }
    
    /*
     * This method sets a label to a certain value
     * Note that when we set a certain variable to one, the rest of the labels of that point can be set to zero
     */
    public void setVariable(Label label, int value) {
        Point point = label.getPoint();
        int pointPosition = getPointPosition(point);
        int labelPosition = getLabelPosition(label, true);
        if(value == 1) {
            currentVarValues[pointPosition][labelPosition] = 1;
            nextLabel = (pointPosition + 1) * numberOfLabelsPerPoint;
            for(int index = 0; index < numberOfLabelsPerPoint - 1; index++) {
                if(index != labelPosition) {
                    currentVarValues[pointPosition][index] = 0;
                }
            }
        } else {
            currentVarValues[pointPosition][labelPosition] = 0;
            nextLabel = getLabelPosition(label, false) + 1;
        }
        if(nextLabel + 1 >= numberOfLabels) {
            finish();
        }
    }
    
    /*
     * This method flushes the current tableau builder vars
     */
    public void flushTableau() {
        tableau.clear();
        cValues.clear();
        targetValues.clear();
    }
    
    /*
     * This method builds initial tableau (constraints), looking at all actual conflicts
     */
    public final void buildInitialTableau() {
        
        int variableIndex = 0; //this index will be used to build the tableau of constraints
        
        quadTree = new Quadtree2();
        
        // Build quadtree
        for (Point p : points) {
            for (Label l : p.possibleLabels) {
                quadTree.insertLabel(l);
            }
        }
        
        // Iterate over points/labels again, this time to create constraints primarily based on conflicts
        
        for(Point p : points) {
            
            ArrayList<Integer> pointLabels = new ArrayList<>(); //will be used to store labels per point (list in more convenient)
            
            for (Label l : p.getPossibleLabels()) { //iterate over labels within that point
                variableIndex++; //update the variable position
                int tempLabelPos = getLabelPosition(l, false);
                Set<Label> conflicts = quadTree.getActConflict(l);
                int[] tempPositions = new int[conflicts.size()];
                if(conflicts.size() > 0) { //we have conflicts for labels in this point
                    int conflictIndex = 0;
                    for(Label conflictLabel : conflicts) { //iterate over those conflicting labels
                       int tempConfLabelPos = getLabelPosition(conflictLabel, false);
                       tempPositions[conflictIndex] = getLabelPosition(conflictLabel, false); //add to list of temporary positions, used to create a constraint ruel
                       int hashCode = cantorPair(tempLabelPos, tempConfLabelPos); //create the hashcode for the label and the conflicting label
                       alreadyConstrained.put(hashCode, true); //put the already linked labels in a hashmap so we don't create duplicates
                       conflictIndex++;
                    }
                }

                // Here we will build the actual constraints for conflicting labels
                
                 for(int pos : tempPositions) {
                    if(!alreadyConstrained.containsKey(cantorPair(tempLabelPos, pos))) { //first check if we don't already have the same constraint
                        ArrayList<Integer> labelPosPair = new ArrayList<>();
                        labelPosPair.add(tempLabelPos);
                        labelPosPair.add(pos);
                        addNewConstraint(labelPosPair); //add the actual constraint
                    }
                 }
                // We also have to keep track of the labels per point
                // Every point needs a constraint such that only label can be placed
                 
                pointLabels.add(tempLabelPos);
                
                // Add target function constant too
                cValues.add(1);
                
                // At last, we add non-zero constraints for every variable
                // Since we can only have <= constraints (that's the way Simplex works),
                // we add the equivalent constraint -x <= 0
                
                addNewNonZeroConstraint(tempLabelPos);
            }
            
            // Create a constraint per point to force maximum one placed label
            addNewConstraint(pointLabels);
        }
        
        t = convertTableauToArray(tableau);
        b = convertToArray(cValues);
        c = convertToArray(targetValues);
    }
    
       /*
     * This method rebuilds the tableau (constraints)
     */
    public final void rebuildTableau() {
        
        flushTableau(); //flush all previous tableau vars
        
        int variableIndex = 0; //this index will be used to build the tableau of constraints
        
        for(Point p : points) {
            
            ArrayList<Integer> pointLabels = new ArrayList<>(); //will be used to store labels per point (list in more convenient)
            
            for (Label l : p.getPossibleLabels()) { //iterate over labels within that point
                variableIndex++; //update the variable position
                int tempLabelPos = getLabelPosition(l, false);
                Set<Label> conflicts = quadTree.getActConflict(l);
                int[] tempPositions = new int[conflicts.size()];
                if(conflicts.size() > 0) { //we have conflicts for labels in this point
                    int conflictIndex = 0;
                    for(Label conflictLabel : conflicts) { //iterate over those conflicting labels
                       int tempConfLabelPos = getLabelPosition(conflictLabel, false);
                       tempPositions[conflictIndex] = getLabelPosition(conflictLabel, false); //add to list of temporary positions, used to create a constraint ruel
                       int hashCode = cantorPair(tempLabelPos, tempConfLabelPos); //create the hashcode for the label and the conflicting label
                       alreadyConstrained.put(hashCode, true); //put the already linked labels in a hashmap so we don't create duplicates
                       conflictIndex++;
                    }
                }

                // Here we will build the actual constraints for conflicting labels
                
                 for(int pos : tempPositions) {
                    if(!alreadyConstrained.containsKey(cantorPair(tempLabelPos, pos))) { //first check if we don't already have the same constraint
                        ArrayList<Integer> labelPosPair = new ArrayList<>();
                        labelPosPair.add(tempLabelPos);
                        labelPosPair.add(pos);
                        addNewConstraint(labelPosPair); //add the actual constraint
                    }
                 }
                // We also have to keep track of the labels per point
                // Every point needs a constraint such that only label can be placed
                 
                pointLabels.add(tempLabelPos);
                
                // Add target function constant too
                cValues.add(1);
                
                // We add non-zero constraints for every variable
                // Since we can only have <= constraints (that's the way Simplex works),
                // we add the equivalent constraint -x <= 0
                
                addNewNonZeroConstraint(tempLabelPos);
                
                // As extra step in the tableau rebuilding, we set equality constraints
                for(int pIndex = 0; pIndex < currentVarValues.length; pIndex++) {
                    for(int vIndex = 0; vIndex < currentVarValues[pIndex].length; vIndex++) {
                        if(currentVarValues[pIndex][vIndex] != 5) { // A variable has been set
                            addNewEqualityConstraint(pIndex * numberOfLabelsPerPoint + vIndex, currentVarValues[pIndex][vIndex]); //add equality constraint with the found value
                        }
                    }
                }
            }
            
            // Create a constraint per point to force maximum one placed label
            addNewConstraint(pointLabels);
        }
        
        t = convertTableauToArray(tableau);
        b = convertToArray(cValues);
        c = convertToArray(targetValues);
    }
    
    protected final void setNumberOfLabels() {
        numberOfPoints = points.size();
        numberOfLabelsPerPoint = points.get(0).possibleLabels.size();
        numberOfLabels = numberOfPoints * numberOfLabelsPerPoint;
    }
    
    protected final void setNumberOfLabels(int num) {
        numberOfLabels = num;
    }
    
    public int getNumberOfLabels() {
        return numberOfLabels;
    }
    
    /*
     * This method adds constraints based on positions of labels in the global label list
     */
    public void addNewConstraint(ArrayList<Integer> labelPosPair) {
        double[] tempLine = new double[getNumberOfLabels()]; //store new temp constraint rule
        for(int varIndex = 0; varIndex < getNumberOfLabels(); varIndex++) {
            tempLine[varIndex] = labelPosPair.contains(varIndex) ? 1 : 0;
        }
        tableau.add(tempLine); //add the constraint rule to the tableau
        cValues.add(1); //add the constraint value too
    }

    /*
     * This method adds constraints based on positions of labels in the global label list, with a configurable constraint value
     */
    public void addNewConstraint(ArrayList<Integer> labelPosPair, int value) {
        double[] tempLine = new double[getNumberOfLabels()]; //store new temp constraint rule
        for(int varIndex = 0; varIndex < getNumberOfLabels(); varIndex++) {
            tempLine[varIndex] = labelPosPair.contains(varIndex) ? 1 : 0;
        }
        tableau.add(tempLine); //add the constraint rule to the tableau
        cValues.add(value); //add the constraint value too
    }
    
     /*
     * This method adds a non zero constraint
     */
    public void addNewNonZeroConstraint(int labelPos) {
        double[] tempLine = new double[getNumberOfLabels()]; //store new temp constraint rule
        for(int varIndex = 0; varIndex < getNumberOfLabels(); varIndex++) {
            tempLine[varIndex] = (varIndex == labelPos) ? -1 : 0;
        }
        tableau.add(tempLine); //add the constraint rule to the tableau
        cValues.add(0); //add the constraint value too
    }

    /*
     * This method adds a equality constraint
     * Simplex only allows <= constraints
     * Therefore, if we want x to be exactly equal to a,
     * we can add the two constraints x <= a and -x <= -a (-x <= -a is equiv to x >= a)
     */
    public void addNewEqualityConstraint(int labelPos, int value) {
        double[] tempLineOne = new double[getNumberOfLabels()]; //store new temp constraint rule
        double[] tempLineTwo = new double[getNumberOfLabels()]; //store new temp constraint rule
        for(int varIndex = 0; varIndex < getNumberOfLabels(); varIndex++) {
            tempLineOne[varIndex] = (varIndex == labelPos) ? 1 : 0;
            tempLineTwo[varIndex] = (varIndex == labelPos) ? -1 : 0;
        }
        tableau.add(tempLineOne); //add the constraint rule to the tableau
        tableau.add(tempLineTwo); //add the constraint rule to the tableau
        cValues.add(value); //add the constraint value too
        cValues.add(-value); //add the constraint value too
    }
    
    /*
     * Set the value of the current IP relaxation
     */
    public void setCurrentRelaxationValue(double relax) {
        currentRelaxationValue = relax;
    }
    
    /*
     * Test to determine whether or not solution should be fathomed 
     */
    public boolean fathomTest(double[] solution) {
        return arrayIsInteger(solution) && passesTreshold(solution);
    }
    
    /*
     * Array integer test
     */
    public boolean arrayIsInteger(double[] array) {
        for(double elm : array) {
            if(elm != (int) elm) {
                return false;
            }
        }
        return true;
    }
    
    /*
     * Relaxation result test
     */
    public boolean passesTreshold(double[] array) {
        if(array[array.length - 1] <= currentRelaxationValue) {
            return false;
        }
        return true;
    }

    public double[][] convertTableauToArray(ArrayList<double[]> al) {
         return (double[][]) al.toArray(new double[al.size()][al.get(0).length]); //convert our tableau to a real array
    }
    
    public double[] convertToArray(ArrayList<Integer> al) {
         double[] output = new double[al.size()];
         for(int index = 0; index < al.size(); index++) {
             output[index] = al.get(index);
         }
         return output;
    }
    
    private void finish() {
        finished = true;
    }
    
    private boolean finished() {
        return finished;
    }
}