import static java.lang.System.exit;
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

    int numberOfLabels; //number of possible labels (only for internal use)
    int numberOfLabelsPerPoint;
    int numberOfPoints;
    int numLabels = 0; //the numLabels var used by the mainframe
    int[][] currentVarValues;
    double[][] t; //will contain the tableau in array form
    double[] b; //will contain the constraints values in array form
    double[] c; //will contain the target function constants in array form
    int nextLabel = 0; //the label that will be looked at next
    double currentRelaxationValue; //contains the highest solution so far
    double[] currentIncumbent; //holds the best integer solution so far
    ArrayList<double[]> tableau = new ArrayList<>(); //tableau builder ArrayList 
    ArrayList<Integer> cValues = new ArrayList<>(); //constraints values builder  ArrayList
    ArrayList<Integer> targetValues = new ArrayList<>(); //target function values builder ArrayList
    HashMap<Integer, Integer> labelPositions = new HashMap<>(); //used to obtain unique identifiers for labels
    HashMap<Integer, Integer> pointPositions = new HashMap<>(); //used to obtain unique identifiers for points
    ArrayList<Label> labelList = new ArrayList(); //list of all labels
    HashMap<Integer, Boolean> alreadyConstrained = new HashMap<>(); //this hashmap contains pairs of already constrained variables
    SimplexOri simplex; // placeholder for the simplex algorithm object
    boolean finished = false; // state of the algorithm
    Quadtree2 quadTree;
    
    public BranchAndBound() {
        super();
    }
    
//    @Override
//    public void determineLabels() {
//        setNumberOfLabels();
//        initCurVarValues();
//        buildInitialTableau();
//        solve();
//        setActiveLabels();
//    }
    
    /*
     * This method solves the integer program recursively
     */
    @Override
    public final void determineLabels() {
        
        setNumberOfLabels(); // Needed for numerous of things
        createLabelList(); // Labellist is made here, but also point- and label positions hashmaps
        initCurVarValues(); // Properly fill the initial current var values array
        buildInitialTableau(); // Build the initial tableau. Since CPU/memory intensive tasks are done here, it should only be done once
        
        while(!finished()) { // Run untill setVariable notifies us that we're finished
            
            /* DEBUGGING 
            for(int i = 0; i < t.length; i++){
               for(int j = 0; j < t[j].length; j++){
                  System.out.print(" " + t[i][j]);
               }
               System.out.print(" " +  b[i]);
               System.out.println();
            }
            /* /DEBUGGING */

            // alreadyConstrained.clear(); // Not needed after performance improvement, tableau is not completely rebuilt every time
            rebuildTableau(); // Rebuild the tableau. Performance improvement: only add equality constraints.
            addNewEqualityConstraint(nextLabel, 0); // This is the actual branching. We temporarily set one variable fixed, to bound: check the LP Relaxation value.
            convertTempTableauLists(); // Convert the working lists to Arrays
            simplex = new SimplexOri(t, b, c); // Initiate the actual Simplex algorithm

            double[] relaxationLeft = simplex.getPrimalResult(); // Get the left branch result

            removeLastVarConstraint(); // We have to remove the constraints set for the last branch, otherwise we will assign two values to a variable.
            convertTempTableauLists(); // Convert the working lists to Arrays because of the constraint removal
                            
            /* DEBUGGING */ 
            System.out.println("Left relaxation result: " + relaxationLeft[relaxationLeft.length - 1]);
            System.out.println("relaxation is integer: " + arrayIsInteger(relaxationLeft)); 

            
            for(int i = 0; i < relaxationLeft.length - 1; i++){
                System.out.println(" : " +  relaxationLeft[i]); 
                
            }
            /* /DEBUGGING */

            // alreadyConstrained.clear(); // Not needed anymore
            rebuildTableau(); // Re-rebuild the tableau
            addNewEqualityConstraint(nextLabel, 1); // Branch again
            convertTempTableauLists();

            simplex = new SimplexOri(t, b, c);

            double[] relaxationRight = simplex.getPrimalResult();
            
            System.out.println();
            
            for(int i = 0; i < relaxationRight.length - 1; i++){
                System.out.println(" : " +  relaxationRight[i]);  
            }

            System.out.println("Right relaxation result: " + relaxationRight[relaxationRight.length - 1]);
            System.out.println("relaxation is integer: " + arrayIsInteger(relaxationRight)); 
            
            boolean noLeftSubtree = false;
            boolean noRightSubtree = false;
            int labelToDoLeft = -1;
            int labelToDoRight = -1;

            if(!fathomTest(relaxationLeft)) {
                //removeLastVarConstraint();
                labelToDoLeft = 0;
                System.out.println("LEFT");
            } else {
                if(arrayIsInteger(relaxationLeft)) {
                    //we have found the incumbent
                    if(relaxationLeft[relaxationLeft.length - 1] > getCurrentIncumbentValue()) {
                        System.out.println("LEFT2");
                        setCurrentIncumbent(relaxationLeft);
                        setCurrentRelaxationValue(relaxationLeft[relaxationLeft.length - 1]);
                    } 
                }
                noLeftSubtree = true;
                labelToDoLeft = 1;
            }
            if(!hasActiveConflictingLabels(nextLabel)) { 
                if(!fathomTest(relaxationRight)) {
                    labelToDoRight = 1;
                    System.out.println("RIGHT");
                } else {
                    if(arrayIsInteger(relaxationRight)) {
                        //we have found the incumbent
                        if(relaxationRight[relaxationRight.length - 1] > getCurrentIncumbentValue()) {
                            System.out.println("RIGHT2");
                            setCurrentIncumbent(relaxationRight);
                            setCurrentRelaxationValue(relaxationRight[relaxationRight.length - 1]);
                        }
                    } 
                    noRightSubtree = true;
                    labelToDoRight = 0;
                }
            } else {
                labelToDoRight = 0;
            }
            
            if(noLeftSubtree && noRightSubtree) {
                finish();
                System.out.println("finish0");
                
            } else {
                if(labelToDoRight == labelToDoLeft) {// labelToDoLeft != -1) {
                    System.out.println("setlabel0");
                    setVariable(labelList.get(nextLabel), labelToDoLeft);
                } else {
                    
                    System.out.println("test - labeltodo: " + labelToDoLeft + " " + labelToDoRight );
                    
                    
                    if(noLeftSubtree && labelToDoRight == 1) {
                        System.out.println("setlabel21");
                        setVariable(labelList.get(nextLabel), 1);
                    }
                    
                    if(noRightSubtree && labelToDoLeft == 0) {
                        System.out.println("setlabel20");
                        setVariable(labelList.get(nextLabel), 0);
                    }
                    
                    if(noLeftSubtree && labelToDoRight == 0) {
                        if(!noRightSubtree && labelToDoLeft == 1) {
                            System.out.println("setlabel1");
                            setVariable(labelList.get(nextLabel), 1);
                        } else {
                            System.out.println("finish1");
                            finish();
                        }
                    }
                    
                    if(noRightSubtree && labelToDoLeft == 1) {
                        if(!noLeftSubtree && labelToDoRight == 0) {
                            System.out.println("setlabel2");
                            setVariable(labelList.get(nextLabel), 0);
                        } else {
                            System.out.println("finish2");
                            finish();
                        }
                    }
                    
                    if(!noLeftSubtree && labelToDoRight == 0 && !noRightSubtree && labelToDoLeft == 1) {
                        if(relaxationLeft[relaxationLeft.length - 1] > relaxationRight[relaxationRight.length - 1])  {
                            System.out.println("setlabel3");
                            setVariable(labelList.get(nextLabel), 1);
                        } else {
                            System.out.println("setlabel4");
                            setVariable(labelList.get(nextLabel), 0);
                        }
                    }
                    
                    if(!noLeftSubtree && labelToDoRight == 1 && !noRightSubtree && labelToDoLeft == 0) {
                        if(relaxationLeft[relaxationLeft.length - 1] > relaxationRight[relaxationRight.length - 1])  {
                            System.out.println("setlabel3");
                            setVariable(labelList.get(nextLabel), 0);
                        } else {
                            System.out.println("setlabel4");
                            setVariable(labelList.get(nextLabel), 1);
                        }
                    }
                }
            }
            
            System.out.println("noLeftSubtree: " + noLeftSubtree + " noRightSubtree: " + noRightSubtree);
            
            System.out.println("ENDLOOP");
        }
        
        setActiveLabels();
    }
    
    /*
     * This method sets labels to an active state if determined so by the last Simplex iteration
     */
    public final void setActiveLabels() {
        
        for(int labelIndex = 0; labelIndex < currentIncumbent.length - 1; labelIndex++) {
            Label label = labelList.get(labelIndex);
            label.active = currentIncumbent[labelIndex] == 1;
            numLabels += currentIncumbent[labelIndex] == 1 ? 1 : 0;
            System.out.println(currentIncumbent[labelIndex] == 1 ? "TRUE" : "FALSE");
        }
        
    }
    
    /*
     * This method intializes the currentVarValues variable
     * All var values are set to 5. 5 is the default value and means a variable is unset (since we have an integer array)
     * The default value of an int array element is 0, but this doesn't work out for us because 0 is a feasible value too
     */
    public final void initCurVarValues() {
        currentVarValues = new int[numberOfPoints][numberOfLabelsPerPoint];
        for(int rowIndex = 0; rowIndex < numberOfPoints; rowIndex++) {
            Arrays.fill(currentVarValues[rowIndex], 5);
        }
    }
    
    /*
     * This method combines three integers into a unique tag
     */
    public int combineIntegers(int a, int b, int c) {
        return (a << 20) | (b << 10) | c;
    }
    
     /*
     * This method uniquely two integers into a unique tag
     */
    public int combineIntegers(int a, int b) {
        return (a << 10) | b;
    }
    
    /*
     * This method builds initial tableau (restrictions), looking at all actual conflicts
     */
    public void createLabelList() {
        int labelIndex = 0;
        int pointIndex = 0;
        for (Point p : points) { //iterate over all points
            int xCoord = p.getxCoord();
            int yCoord = p.getyCoord();
            int hashCode = combineIntegers(xCoord, yCoord);
            pointPositions.put(hashCode, pointIndex); //add the label position t the hashset
            for (Label l : p.possibleLabels) { //iterate over labels within that point
                //we use quadrant and (x,y) as unique identifiers of a certain label
                int quadrant = l.getQuadrant();
                int labelHashCode = combineIntegers(quadrant, xCoord, yCoord);
                System.out.println(labelIndex + ". " + labelHashCode + " - quadrant: " + quadrant + " - x: " + xCoord + " - y: " + yCoord);
                labelPositions.put(labelHashCode, labelIndex); //add the label position t the hashset
                labelList.add(l); //add the label itself to the labellist
                labelIndex++;  
            }
            pointIndex++;
        }
        
    }
    
    /*
     * This method will return the position of a label in the global label list
     */
    public int getLabelPosition(Label l, Boolean pointRelative) {
        int quadrant = l.getQuadrant();
        int x = l.getPoint().getxCoord();
        int y = l.getPoint().getyCoord();
        int hashCode = combineIntegers(quadrant, x, y);
        int pos = labelPositions.get(hashCode);
        if(!pointRelative) {
            return pos;
        } else {
            pos = pos % numberOfLabelsPerPoint;
            return pos;
        }
    }
    
    /*
     * This method will return the position of a label in the global point list
     */
    public int getPointPosition(Point p) {
        int x = p.getxCoord();
        int y = p.getyCoord();
        int hashCode = combineIntegers(x, y);
        return pointPositions.get(hashCode);
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
            for(int index = 0; index < numberOfLabelsPerPoint; index++) {
                if(index != labelPosition) {
                    currentVarValues[pointPosition][index] = 0;
                }
            }
        } else {
            System.out.println("pointPosition: "  + pointPosition + " labelPosition: " + labelPosition +  " arraylist size: " + currentVarValues.length + "," + currentVarValues[0].length);
            currentVarValues[pointPosition][labelPosition] = 0;
            nextLabel = getLabelPosition(label, false) + 1;
        }
        if(nextLabel + 1 > numberOfLabels) {
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
        
        currentIncumbent = new double[numberOfLabels + 1];
        
        int variableIndex = 0; //this index will be used to build the tableau of constraints
        
        /*
        // Build quadtree
        for (Point p : points) {
            for (Label l : p.possibleLabels) {
                q.insertLabel(l);
            }
        }*/
        
        // Iterate over points/labels again, this time to create constraints primarily based on conflicts
        
        for(Point p : points) {
            
            ArrayList<Integer> pointLabels = new ArrayList<>(); //will be used to store labels per point (list in more convenient)
            
            for (Label l : p.getPossibleLabels()) { //iterate over labels within that point

                int tempLabelPos = getLabelPosition(l, false);
                Set<Label> conflicts = cD.getPosConflictLabels(l);
                System.out.println("cf size: " + conflicts.size());
                
                int[] tempPositions = new int[conflicts.size()];
                if(conflicts.size() > 0) { //we have conflicts for labels in this point
                    
                    int conflictIndex = 0;
                    for(Label conflictLabel : conflicts) { //iterate over those conflicting labels
                       int tempConfLabelPos = getLabelPosition(conflictLabel, false);
                       System.out.println("confl pos: " + tempConfLabelPos);
                       tempPositions[conflictIndex] = getLabelPosition(conflictLabel, false); //add to list of temporary positions, used to create a constraint ruel
                       int hashCode = combineIntegers(tempLabelPos, tempConfLabelPos); //create the hashcode for the label and the conflicting label
                       conflictIndex++;
                    }
                }

                // Here we will build the actual constraints for conflicting labels
                
                 for(int pos : tempPositions) {
                    if(!alreadyConstrained.containsKey(combineIntegers(tempLabelPos, pos))) { //first check if we don't already have the same constraint
                        ArrayList<Integer> labelPosPair = new ArrayList<>();
                        labelPosPair.add(tempLabelPos);
                        labelPosPair.add(pos);
                        addNewConstraint(labelPosPair); //add the actual constraint
                        alreadyConstrained.put(combineIntegers(tempLabelPos, pos), true); //put the already linked labels in a hashmap so we don't create duplicates
                    }
                 }
                // We also have to keep track of the labels per point
                // Every point needs a constraint such that only label can be placed
                 
                pointLabels.add(tempLabelPos);
                
                // Add constraint value too
                //cValues.add(1);
                // Add targetfunction value too
                targetValues.add(1);
                
                // At last, we add non-zero and max-one constraints for every variable
                // Since we can only have <= constraints (that's the way Simplex works),
                // we add the equivalent constraint -x <= 0
                
                addNewNonZeroConstraint(tempLabelPos);
                addNewMaxOneConstraint(tempLabelPos);
                
                variableIndex++; //update the variable position
            }
            
            // Create a constraint per point to force maximum one placed label
            addNewConstraint(pointLabels);
        }
        
        convertTempTableauLists();

    }
    
       /*
     * This method rebuilds the tableau (constraints)
     */
    public final void rebuildTableau() {
        
            // As extra step in the tableau rebuilding, we set equality constraints
            for(int pIndex = 0; pIndex < currentVarValues.length; pIndex++) {
                for(int vIndex = 0; vIndex < currentVarValues[pIndex].length; vIndex++) {
                    if(currentVarValues[pIndex][vIndex] != 5) { // A variable has been set
                        addNewEqualityConstraint(pIndex * numberOfLabelsPerPoint + vIndex, currentVarValues[pIndex][vIndex]); //add equality constraint with the found value
                    }
                }
            }
     
    }
    
    /*
     * Convert temporary tableau placeholders to real tableau
     */
    protected void convertTempTableauLists() {
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
        System.out.println("Adding new constraint for labels: ");
        for(int varIndex = 0; varIndex < getNumberOfLabels(); varIndex++) {
            tempLine[varIndex] = labelPosPair.contains(varIndex) ? 1 : 0;
            System.out.print(labelPosPair.contains(varIndex) ? " " + varIndex : "");
        }
        System.out.println();
       
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
     * This method adds a non zero constraint
     */
    public void addNewMaxOneConstraint(int labelPos) {
        double[] tempLine = new double[getNumberOfLabels()]; //store new temp constraint rule
        for(int varIndex = 0; varIndex < getNumberOfLabels(); varIndex++) {
            tempLine[varIndex] = (varIndex == labelPos) ? 1 : 0;
        }
        tableau.add(tempLine); //add the constraint rule to the tableau
        cValues.add(1); //add the constraint value too
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
     * Check if label has active conflicting labels
     */
    public boolean hasActiveConflictingLabels(int labelPos) {
        Label label = labelList.get(labelPos);
        Set<Label> conflicts = cD.getPosConflictLabels(label);
        if(conflicts.size() > 0) { //we have conflicts for labels in this point
            int conflictIndex = 0;
            for(Label conflictLabel : conflicts) { //iterate over those conflicting labels
               int tempConfLabelPos = getLabelPosition(conflictLabel, true);
               int tempConfPointPos = getPointPosition(conflictLabel.getPoint());
               if(currentVarValues[tempConfPointPos][tempConfLabelPos] == 1) {
                   return true;
               }
               conflictIndex++;
            }
        }
        
        return false;
    }
    
    /*
     * Remove the last set constraints
     */
    public void removeLastVarConstraint() {
        tableau.remove(tableau.size() - 1); // Remove constraint rule
        cValues.remove(tableau.size() - 1); // Remove constraint value
    }
    
    /*
     * Get the current incumbent
     */
    public double[] getCurrentIncumbent() {
        return currentIncumbent;
    }
    
    /*
     * Set the current incumbent
     */
    public void setCurrentIncumbent(double[] inc) {
        currentIncumbent = inc;
    }
    
    /*
     * Get the current incumbent value
     */
    public double getCurrentIncumbentValue() {
        return currentIncumbent[currentIncumbent.length - 1];
    }
    
     /*
     * Set the value of the current IP relaxation
     */
    public void setCurrentRelaxationValue(double relax) {
        currentRelaxationValue = relax;
    }
    
     /*
     * Get the value of the current IP relaxation
     */
    public double getCurrentRelaxationValue() {
        return currentRelaxationValue;
    }
    
    /*
     * Test to determine whether or not solution should be fathomed 
     */
    public boolean fathomTest(double[] solution) {
        return arrayIsInteger(solution); // || passesTreshold(solution);
    }
    
    /*
     * Array integer test
     */
    public boolean arrayIsInteger(double[] array) {
        int aIndex = 0;
        for(double elm : array) {
            if(aIndex < array.length - 1) {
                if(elm != (int) elm) {
                    return false;
                }
                
            }
            aIndex++;
        }
        return true;
    }
    
    /*
     * Relaxation result test
     */
    public boolean passesTreshold(double[] array) {
        if(array[array.length - 1] <= getCurrentRelaxationValue()) {
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
    
    private void test() {
        double[] array = new double[4];
        System.out.println(arrayIsInteger(array) ? "yes" : "no");
    }
    
    /*
    public static void main(String args[]) {
        BranchAndBound bb = new BranchAndBound();
        bb.test();
    }*/
    
}