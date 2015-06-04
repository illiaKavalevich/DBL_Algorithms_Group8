
import java.util.HashSet;
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
        if (model.equals("1slider")) return getActConflictLabelsSlider((SliderLabel) l).size();
        else return getActConflictLabelsDiscrete(l).size();
    }
    
    @Override
    public Set<Label> getActConflictLabels(Label l) {
        if (model.equals("1slider")) return getActConflictLabelsSlider((SliderLabel) l);
        else return getActConflictLabelsDiscrete(l);
    }
    
    private Set<Label> getActConflictLabelsSlider(SliderLabel l) {
        Set<Label> result = new HashSet<Label>();
        Set<Label> posLabels = new HashSet<Label>(l.getPoint().getPossibleLabels());
        Set<Label> posConflicts = new HashSet<Label>(); //pos conflicts of PosLabels
        
        //get all the labels the Slider might intersect with
        for (Label label : posLabels) {
            posConflicts.addAll(getPosConflictLabels(l));
        }
        
        //get all the associated Slider Labels
        Set<SliderLabel> posConflictSlider = new HashSet<SliderLabel>(); //pos conflicts SliderLabels
        for(Label label : posConflicts) {
            posConflictSlider.add(label.getPoint().getActiveLabelSlider());
        }
        
        //check if they indeed overlap and add to result
        for(Label label: posConflictSlider) {
            if(label.overlaps(l)) {
                result.add(label);
            }
        }
        return result;
    }
    
    private Set<Label> getActConflictLabelsDiscrete(Label l) {
        Set<Label> result = new HashSet<Label>();
        Set<Label> posConflicts = getPosConflictLabels(l); //labels conflicting with given label
        for(Label posLabel : posConflicts) { //for all the conflicting labels...
            if(posLabel.equals(posLabel.getPoint().getActiveLabelPos())) { //...check if label is active
                result.add(posLabel); //add to result
            }
        }
        return result;
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