import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.lang.Math;

/**
 * This class represents an MX quadtree. Labels are only stored in the nodes.
 * 
 */
public class Quadtree {
    
    private Node root; //the root with depth 0
    
    //quadtree reaches from -size to size on x and y axis
    private int size = 16384;
    private int maxDepth = 15; //maxdepth is chosen so that the smallest square is 1x1
    
    //constructor that generates an empty tree with a single root node
    public Quadtree() {
        root = new Node(0, 0, 0);
    }
    
    //a class that helps to define rectangle in which to search
    private class Rectangle {
        //coordinates defining the rectangle
        double minX;
        double maxX;
        double minY;
        double maxY;
        
        //constructor for the rectangle
        Rectangle(double minX, double maxX, double minY, double maxY) {
            this.minX = minX;
            this.maxX = maxX;
            this.minY = minY;
            this.maxY = maxY;
        }
        
        //returns true if a given coordinate is contained in the rectangle (or on the border)
        public boolean contains(double x, double y) {
            return minX <= x && x <= maxX && minY <= y && y <= maxY;
        }
    }
    
    //subclass defining a node in the tree
    public class Node {
        double x, y; //middle point of node
        Node NW, NE, SE, SW; //subtrees
        List<Label> labels; //labels in the node
        int depth; //depth in the tree
        double nodeSize; //size of square
        boolean hasChildren;

        Node(double x, double y, int depth) {
            this.x = x;
            this.y = y;
            this.depth = depth;
            this.nodeSize = size / Math.pow(2, depth); //calculate size
            this.hasChildren = false;
            this.labels = new ArrayList<>(); //initialize to avoid null pointer exceptions
        }
        
        //adds a label to the node
        public void addLabel(Label l) {
            labels.add(l);
        }
    }
    
    /**
     * This method adds a Label to the data structure
     * 
     * @param l the Label to be inserted
     */
    public void insertLabel(Label l) {
        Rectangle rectLabel = new Rectangle(l.getMinX(), l.getMaxX(), 
                l.getMinY(), l.getMaxY());
        insert(root, l, rectLabel);
    }
    
    //propagates the insertion through the depth of the tree
    private void insert(Node n, Label l, Rectangle rectLabel) {
        //if we are in a leaf add the label to the leaf
        if (n.depth == maxDepth) {
            n.labels.add(l);
            return; //stop the propagation
        }
        
        //create children if non existent
        if(!n.hasChildren) {
            n.NW = new Node(n.x + (n.nodeSize / 2), n.y + (n.nodeSize / 2), n.depth + 1);
            n.NE = new Node(n.x - (n.nodeSize / 2), n.y + (n.nodeSize / 2), n.depth + 1);
            n.SE = new Node(n.x - (n.nodeSize / 2), n.y - (n.nodeSize / 2), n.depth + 1);
            n.SW = new Node(n.x + (n.nodeSize / 2), n.y - (n.nodeSize / 2), n.depth + 1);
            n.hasChildren = true;
        }
        
        //propagate the insertion to the right subtree(s)
        if (n.x < l.getMaxX() && n.y < l.getMaxY()) {
            insert(n.NW, l, rectLabel);
        }
        else if (n.x < l.getMaxX() && n.y > l.getMinY()) {
            insert(n.SW, l, rectLabel);
        }
        else if (n.x > l.getMinX() && n.y > l.getMinY()) {
            insert(n.SE, l, rectLabel);
        }
        else if (n.x > l.getMinX() && n.y < l.getMaxY()) {
            insert(n.NE, l, rectLabel);
        }

    }
    
    /**
     * This method simply removes a Label
     * 
     * @param l label to be removed
     */
    public void removeLabel(Label l) {
        Rectangle rectLabel = new Rectangle(l.getMinX(), l.getMaxX(), 
                l.getMinY(), l.getMaxY());
        //Note: rectLabel not used a the moment. May be used to stop propagation if entire node is covered by label
        remove(root, l, rectLabel);
    }
    
    /* removes a label from the datastructure by propagating deletion through the
    tree. At the leaf nodes the label is deleted. Empty nodes are not deleted. 
    Very similar to insertion.*/
    private void remove(Node n, Label l, Rectangle rectLabel) { //Note: rectLabel not used a the moment
        //remove from leaf
        if (n.depth == maxDepth) {
            n.labels.remove(l);
            return; //stop propagation downwards
        }
        
        //propagate call to subtree(s)
        if (n.x < l.getMaxX() && n.y < l.getMaxY()) {
            remove(n.NW, l, rectLabel);
        }
        else if (n.x < l.getMaxX() && n.y > l.getMinY()) {
            remove(n.SW, l, rectLabel);
        }
        else if (n.x > l.getMinX() && n.y > l.getMinY()) {
            remove(n.SE, l, rectLabel);
        }
        else if (n.x > l.getMinX() && n.y < l.getMaxY()) {
            remove(n.NE, l, rectLabel);
        }
    }
    
    /**
     * Used to query for labels that (partially) overlap a given rectangle
     * 
     * @params coordinates defining a rectangle
     * @return a Set of labels that overlap a given area
     */
    public Set<Label> query(int minX, int maxX, int minY, int maxY) {
        HashSet<Label> result = new HashSet<>(); //result set passed to query
        Rectangle rectangle = new Rectangle(minX, maxX, minY, maxY); //construct rectangle
        query(root, rectangle, result);
        return result;
    }
    
    //query is propagated to subnodes until leaves are reached.
    private void query(Node n, Rectangle rectangle, HashSet<Label> result) {
        if (n == null) return; //non existent node, always doesnt contain any label
        
        //if the node is 'covered' by the rectangle add associated labels to result
        if (n.depth == maxDepth && rectangle.contains(n.x, n.y)){
            for (Label l : n.labels){
                result.add(l); //IMPORTANT: maybe a check needs to be done that l is not in result already, depends on how add is handled by java
            }
            return; //stop propagation
        }
        
        //propagate query to subtree(s)
        if (n.x < rectangle.maxX && n.y < rectangle.maxY) {
            query(n.NW, rectangle, result);
        }
        if (n.x > rectangle.minX && n.y > rectangle.minY) {
            query(n.SE, rectangle, result);
        }
        if (n.x < rectangle.maxX && n.y > rectangle.minY) {
            query(n.SW, rectangle, result);
        }
        if (n.x > rectangle.minX && n.y < rectangle.maxY) {
            query(n.NE, rectangle, result);
        }
    }
}
