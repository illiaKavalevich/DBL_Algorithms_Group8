import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.lang.Math;

/**
 * This class represents an MX quadtree. 
 * 
 */
public class Quadtree {
    
    private Node root;
    
    //quadtree reaches from -size to size on x and y axis
    private int size = 16384;
    private int maxDepth = 15;
    
    Quadtree() {
        root = new Node(0, 0, 0);
    }
    
    //a class that helps to define rectangle in which to search
    private class Rectangle {
        //coordinates defining the rectangle
        double minX;
        double maxX;
        double minY;
        double maxY;
        
        Rectangle(double minX, double maxX, double minY, double maxY) {
            this.minX = minX;
            this.maxX = maxX;
            this.minY = minY;
            this.maxY = maxY;
        }
        
        //returns true if a given coordinate is contained in the rectangle
        public boolean contains(double x, double y) {
            return minX <= x && x <= maxX && minY <= y && y <= maxY;
        }
    }
    
    public class Node {
        double x, y; //middle point of node
        Node NW, NE, SE, SW; //subtrees
        List<Label> labels; //labels in the node
        int depth;
        double nodeSize;
        boolean hasChildren = false;

        Node(double x, double y, int depth) {
            this.x = x;
            this.y = y;
            this.depth = depth;
            this.nodeSize = size / Math.pow(2, depth);
        }
        
        public void addLabel(Label l) {
            if(l == null) labels = new ArrayList<>();
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
    
    
    private void insert(Node n, Label l, Rectangle rectLabel) {
        if (n.depth == maxDepth) {
            n.labels.add(l);
            return;
        }
        
        if(!n.hasChildren) {
            n.NW = new Node(n.x + (n.nodeSize / 2), n.y + (n.nodeSize / 2), n.depth + 1);
            n.NE = new Node(n.x - (n.nodeSize / 2), n.y + (n.nodeSize / 2), n.depth + 1);
            n.SE = new Node(n.x - (n.nodeSize / 2), n.y - (n.nodeSize / 2), n.depth + 1);
            n.SW = new Node(n.x + (n.nodeSize / 2), n.y - (n.nodeSize / 2), n.depth + 1);
        }
        
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
        remove(root, l, rectLabel);
    }
    
    private void remove(Node n, Label l, Rectangle rectLabel) {
        if (n.depth == maxDepth) {
            n.labels.remove(l);
            return;
        }
        
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
     * Used to query for labels that (partially overlap a given rectangle
     * 
     * @return a Set of labels that overlap a given area
     */
    public Set<Label> query(int minX, int maxX, int minY, int maxY) {
        HashSet<Label> result = new HashSet<>();
        Rectangle rectangle = new Rectangle(minX, maxX, minY, maxY);
        query(root, rectangle, result);
        return result;
    }

    private void query(Node n, Rectangle rectangle, HashSet<Label> result) {
        if (n == null) return;
        if (n.depth == maxDepth && rectangle.contains(n.x, n.y)){
            for (Label l : n.labels){
                result.add(l);
            }
            return;
        }
        
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
