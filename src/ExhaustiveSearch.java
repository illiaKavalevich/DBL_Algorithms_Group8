
import java.util.*;

public class ExhaustiveSearch extends Algorithm {

    HashSet<Point> noActiveLabelPoints = new HashSet<>();                       //all the points that do not have an active label
    HashSet<Point> activeLabelPoints = new HashSet<>();                           //all the points that have an active label that doesn't overlap
    Runnable Run;
    int firstNumlabels;
    ArrayList<Point> firstPoints = new ArrayList<>();
    ArrayList<Integer> firstQuadrants = new ArrayList<>();
    Thread thread;
    boolean stop = false;
    boolean changed = false;

    public ExhaustiveSearch() {

    }

    @Override
    public void determineLabels() {
         createSolutions(0);
    }
    
    public void createSolutions(int n) {
        for (Label label : points.get(n).possibleLabels) {
            
        }
    }

    @Override
    public void stopRunning() {
        stop = true;
    }

    public void selectBestResult() {
        
    }


}
