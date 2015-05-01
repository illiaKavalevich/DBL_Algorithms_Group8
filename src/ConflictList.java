
import java.util.*;

/**
 *
 * @author s130604
 */
public final class ConflictList {
    /*
     * Maps a label to all labels it has a conflict with
     * labels are only added if they overlap, NOT if they are just adjacent
     */

    HashMap<Label, Set<Label>> posConflict = new HashMap<>();      //lists all possible conflicts     
    HashMap<Label, Set<Label>> actConflict = new HashMap<>();      //lists all actual conflicts
    ArrayList<Label> possibleLabels = new ArrayList<>();           //List of all possible labels
    ArrayList<Label> activeLabels = new ArrayList<>();             //List of all active labels
    ArrayList<LabelPair> posLpairs = new ArrayList<>();            //List of all pairs of possible labels
    ArrayList<LabelPair> actLpairs = new ArrayList<>();            //List of all pairs of active labels

    //width and height refers to the width and height of the label
    //In the constructor the adjacency list is filled
    public ConflictList(List<Point> points, String model) {
        for (Point p : points) {
            for (Label label : p.possibleLabels) {
                posConflict.put(label, new HashSet());
                possibleLabels.add(label);
            }
            if (model.equals("1slider")) {
                activeLabels.add(p.getActiveLabelSlider());
            } else {
                activeLabels.add(p.getActiveLabelPos());
            }
        }
        setPairsPos(points);
        setPairsAct(points);
        for (LabelPair pair : posLpairs) {
            if (pair.l1.overlaps(pair.l2)) {
                Set labelSet = posConflict.get(pair.l1);
                labelSet.add(pair.l2);
                posConflict.put(pair.l1, labelSet);
                Set labelSet2 = posConflict.get(pair.l2);
                labelSet2.add(pair.l1);
                posConflict.put(pair.l2, labelSet2);
            }
        }
        for (LabelPair pair : actLpairs) {
            if (pair.l1.overlaps(pair.l2)) {
                Set labelSet = actConflict.get(pair.l1);
                labelSet.add(pair.l2);
                actConflict.put(pair.l1, labelSet);
                Set labelSet2 = actConflict.get(pair.l2);
                labelSet2.add(pair.l1);
                actConflict.put(pair.l2, labelSet2);
            }
        }
    }

    public void setPairsPos(List<Point> points) {
        boolean isNew = true;

        //add all possible labels
        for (Label label1 : possibleLabels) {
            for (Label label2 : possibleLabels) {
                /**
                 * for each pair check if it does not already exist the other
                 * way around (i.e pair(p1, p2) is same as (pair(p2, p1)) and
                 * check if the labels do not belong to the same point. If not,
                 * add to pairs
                 */
                for (LabelPair pair : posLpairs) {
                    if (pair.l1.equals(label2) && pair.l2.equals(label1)) {
                        isNew = false;
                        break;
                    }
                    if (label1.p == label2.p) {
                        isNew = false;
                        break;
                    }
                }
                if (isNew) {
                    posLpairs.add(new LabelPair(label1, label2));
                }
            }
        }
    }
    
    public void setPairsAct(List<Point> points) {
        boolean isNew = true;

        //add all possible labels
        for (Label label1 : activeLabels) {
            for (Label label2 : activeLabels) {
                /**
                 * for each pair check if it does not already exist the other
                 * way around (i.e pair(p1, p2) is same as (pair(p2, p1)) if not,
                 * add to pairs
                 */
                for (LabelPair pair : actLpairs) {
                    if (pair.l1.equals(label2) && pair.l2.equals(label1)) {
                        isNew = false;
                        break;
                    }
                    
                }
                if (isNew) {
                    actLpairs.add(new LabelPair(label1, label2));
                }
            }
        }
    }
}
