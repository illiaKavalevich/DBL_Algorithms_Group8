
import java.util.List;
import java.util.Set;

public class ConflictDetector implements ConflictDetectorInterface {
    String model;
    List<Point> points;
    Quadtree quadtree;
    
    public ConflictDetector(List<Point> points, String model, Quadtree quadtree) {
        this.model = model;
        this.points = points;
        this.quadtree = quadtree;
        addAllPoints();
    }
    
    //adds all the labels of all the points to the quadtree
    private void addAllPoints() {
        for(Point p : points) {
            for(Label l : p.getPossibleLabels()) {
                quadtree.insertLabel(l);
            }
        }
    }

    @Override
    public int getActDegree(Label l) {
        if (model.equals("1slider")) return getActConflictLabelsSlider(l).size();
        else return getActConflictLabelsDiscrete(l).size();
    }
    
    @Override
    public Set<Label> getActConflictLabels(Label l) {
        if (model.equals("1slider")) return getActConflictLabelsSlider(l);
        else return getActConflictLabelsDiscrete(l);
    }
    
    private Set<Label> getActConflictLabelsSlider(Label l) {
        Set<Label> posConflics = getPosConflictLabels(l);
        //TO DO: compute the actually conflicting labels
        return posConflics;
    }
    
    private Set<Label> getActConflictLabelsDiscrete(Label l) {
        Set<Label> posConflics = getPosConflictLabels(l);
        //TO DO: determine which labels are active
        return posConflics;
    }

    @Override
    public void removePoint(Point p) {
        for(Label l : p.getPossibleLabels()) {
            quadtree.removeLabel(l);
        }
    }

    @Override
    public int getPosDegree(Label l) {
        return getPosConflictLabels(l).size();
    }

    @Override
    public Set<Label> getPosConflictLabels(Label l) {
        return quadtree.query(l.getMinX(), l.getMaxX(), l.getMinY(), l.getMaxY());
    }
}