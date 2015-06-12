import java.util.ArrayList;
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
public abstract class BranchAndBound extends Algorithm implements ConflictDetectorInterface {
    ArrayList<double[]> t = new ArrayList<>(); //tableau builder ArrayList
    ArrayList<Double> b = new ArrayList<Double>();
    ArrayList<Double> c = new ArrayList<Double>();
    int numberOfLabels;
    int numberOfLabelsPerPoint;
    double[] currentVarValues;
    HashMap<Integer, Integer> labelPositions = new HashMap<>();
    ArrayList<Label> labelList = new ArrayList();
    HashMap<Integer, Boolean> alreadyConstrained = new HashMap<>();
    
    public BranchAndBound() {
        setNumberOfLabels();
        buildTableau();
    }
    
    /*
     * This method solves the integer program recursively
     */
    public void solve() {
        
  
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
        for (Point p : points) { //iterate over all points
            for (Label l : p.possibleLabels) { //iterate over labels within that point
                labelIndex++;    
                //we use quadrant and (x,y) as unique identifiers of a certain label
                int quadrant = l.getQuadrant();
                int xCoord = l.getPoint().getxCoord();
                int yCoord = l.getPoint().getyCoord();
                int hashCode = cantorPair(quadrant, xCoord, yCoord);
                labelPositions.put(hashCode, labelIndex); //add the label position t the hashset
                labelList.add(l); //add the label itself to the labellist
            }
        }
    }
    
    /*
     * This method returns the position of a label in the global label list
     */
    public int getLabelPosition(Label l) {
        int quadrant = l.getQuadrant();
        int xCoord = l.getPoint().getxCoord();
        int yCoord = l.getPoint().getyCoord();
        int hashCode = cantorPair(quadrant, xCoord, yCoord);
        return labelPositions.get(hashCode);   
    }
    
    /*
     * This method builds initial tableau (restrictions), looking at all actual conflicts
     */
    protected final void buildTableau() {
        int variableIndex = 0; //this index will be used to build the tableau of restrictions
        for(Point p : points) {
            
            ArrayList<Integer> pointLabels = new ArrayList<>(); //will be used to store labels per point (list in more convenient)
            
            for (Label l : p.getPossibleLabels()) { //iterate over labels within that point
                variableIndex++; //update the variable position
                int tempLabelPos = getLabelPosition(l);
                Set<Label> conflicts = getPosConflictLabels(l);
                int[] tempPositions = new int[conflicts.size()];
                if(conflicts.size() > 0) { //we have conflicts for labels in this point
                    int conflictIndex = 0;
                    for(Label conflictLabel : conflicts) { //iterate over those conflicting labels
                       int tempConfLabelPos = getLabelPosition(conflictLabel);
                       tempPositions[conflictIndex] = getLabelPosition(conflictLabel); //add to list of temporary positions, used to create a constraint ruel
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
            }
            
            // Create a constraint per point to force maximum one placed label
            addNewConstraint(pointLabels);
        }
    }
    
    public void rebuildTableau() {
        int variableIndex = 0; //this index will be used to build the tableau of restrictions
        
        for(Point p : points) {
            
            ArrayList<Integer> pointLabels = new ArrayList<>(); //will be used to store labels per point (list in more convenient)
            
            for (Label l : p.getPossibleLabels()) { //iterate over labels within that point
                
                
                
                variableIndex++; //update the variable position
                int tempLabelPos = getLabelPosition(l);
                Set<Label> conflicts = getPosConflictLabels(l);
                int[] tempPositions = new int[conflicts.size()];
                if(conflicts.size() > 0) { //we have conflicts for labels in this point
                    int conflictIndex = 0;
                    for(Label conflictLabel : conflicts) { //iterate over those conflicting labels
                       int tempConfLabelPos = getLabelPosition(conflictLabel);
                       tempPositions[conflictIndex] = getLabelPosition(conflictLabel); //add to list of temporary positions, used to create a constraint ruel
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
            }
            
            // Create a constraint per point to force maximum one placed label
            addNewConstraint(pointLabels);
        }
    }
    
    protected final void setNumberOfLabels() {
        int numberOfPoints = points.size();
        numberOfLabelsPerPoint = points.get(0).possibleLabels.size();
        numberOfLabels = numberOfPoints * numberOfLabelsPerPoint;
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
        t.add(tempLine); //add the constraint rule to the tableau
        c.add((double) 1); //add the constraint value too
    }

    public void addNewConstraint(ArrayList<Integer> labelPosPair, int value) {
        double[] tempLine = new double[getNumberOfLabels()]; //store new temp constraint rule
        for(int varIndex = 0; varIndex < getNumberOfLabels(); varIndex++) {
            if(labelPosPair.contains(varIndex)) {
                tempLine[varIndex] = 1;
            } else {
                tempLine[varIndex] = 0;
            }
        }
        t.add(tempLine); //add the constraint rule to the tableau
        c.add((double) value); //add the constraint value too
    }


    
    public double[][] convertTableauToArray(ArrayList<double[]> al) {
         return (double[][]) al.toArray(new double[al.size()][al.get(0).length]); //convert our tableau to a real array
    }
}
