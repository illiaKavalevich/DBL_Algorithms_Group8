
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConflictDetector implements ConflictDetectorInterface {
    String model; //model for point placement
    List<Point> points; //points that are considered
    Quadtree quadtree; //reference to the quadtree used
    
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
        Set<Label> result = new HashSet<Label>(); //result containing SliderLabels 
        Set<Label> posLabels = new HashSet<Label>(l.getPoint().getPossibleLabels()); //pos labels of point
        Set<Label> posConflicts = new HashSet<Label>(); //pos conflicts of PosLabels
        Set<SliderLabel> posConflictSlider = new HashSet<SliderLabel>(); //pos conflicts SliderLabels
        
        //get all the labels pos labels the possible labels of l intersect with
        for (Label label : posLabels) {
            posConflicts.addAll(getPosConflictLabels(l));
        }
        
        //get all the associated Slider Labels
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
        Set<Label> result = new HashSet<Label>(); //active labels conflicting with l
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
        for(Label l : p.getPossibleLabels()) { //loop over all labels...
            quadtree.removeLabel(l); //... and delete them
        }
    }
    
    @Override
    public void removeLabel(Label l) {
        quadtree.removeLabel(l); 
    }

    @Override
    public int getPosDegree(Label l) {
        return getPosConflictLabels(l).size();
    }

    @Override
    public Set<Label> getPosConflictLabels(Label l) {
        Set<Label> result = quadtree.query(l.getMinX(), l.getMaxX(), l.getMinY(), l.getMaxY()); //query the area of l
        result.remove(l); //remove the label itself from the result
        return result;
    }
}