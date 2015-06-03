import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author s133524
 */
public class Quadtree {
    
    private Node root;
    
    Quadtree() {

    }
    
    //a class that helps to define rectangle in which to search
    private class Rectangle {
        //coordinates defining the rectangle
        int minX;
        int maxX;
        int minY;
        int maxY;
        
        Rectangle(int minX, int maxX, int minY, int maxY) {
            this.minX = minX;
            this.maxX = maxX;
            this.minY = minY;
            this.maxY = maxY;
        }
        
        //returns true if a given coordinate is contained in the rectangle
        public boolean contains(int x, int y) {
            return minX < x && x < maxX && minY < y && y < maxY;
        }
    }
    
    public class Node {
        int x, y; //middle point of node
        Node NW, NE, SE, SW; //subtrees
        List<Label> labels; //labels in the node

        Node(int x, int y, Label label) {
            this.x = x;
            this.y = y;
            this.labels = new ArrayList<>();
            labels.add(label);
        }
    }
    
    /**
     * This method adds a Label to the data structure
     * 
     * @param l the Label to be inserted
     */
    public void insertLabel(Label l) {
        root = insert(root, l.getMinX(), l.getMinY(), l);
        root = insert(root, l.getMaxX(), l.getMaxY(), l);
        root = insert(root, l.getMinX(), l.getMaxY(), l);
        root = insert(root, l.getMaxX(), l.getMinY(), l);
    }
    
    
    private Node insert(Node n, int x, int y, Label l) {
        if (n == null) {
            return new Node(x, y, l);
        }
        if (x == n.x && y == n.y) {
            n.labels.add(l);
        } 
        else if (x <  n.x && y >= n.y) n.NW = insert(n.NW, x, y, l);
        else if (x <  n.x && y <  n.y) n.SW = insert(n.SW, x, y, l);
        else if (x >= n.x && y <  n.y) n.SE = insert(n.SE, x, y, l);
        else if (x >= n.x && y >= n.y) n.NE = insert(n.NE, x, y, l);
        return n;
    }
    
    public void removeLabel(Label l) {
        throw new UnsupportedOperationException("Not supported yet.");
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
        if (rectangle.contains(n.x, n.y)){
            for (Label l : n.labels){
                result.add(l);
            }
        }
        if (rectangle.minX < n.x && rectangle.maxY >= n.y) {
            query(n.NW, rectangle, result);
        }
        if (rectangle.maxX >= n.x && rectangle.minY < n.y) {
            query(n.SE, rectangle, result);
        }
        if (rectangle.minX < n.x && rectangle.minY < n.y) {
            query(n.SW, rectangle, result);
        }
        if (rectangle.maxX >= n.x && rectangle.maxY >= n.y) {
            query(n.NE, rectangle, result);
        }
    }
}
