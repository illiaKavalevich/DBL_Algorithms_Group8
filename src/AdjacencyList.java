
import java.util.*;

/**
 *
 * @author s130604
 */
public class AdjacencyList {
    /*
     * Maps a label to all labels it has a conflict with
     * labels are only added if they overlap, NOT if they are just adjacent
     */

    HashMap<Label, Set<Label>> map = new HashMap<>();
    ArrayList<Label> labels = new ArrayList<>();               //List of all labels
    ArrayList<LabelPair> pairs = new ArrayList<>();            //List of all pairs of labels

    //width and height refers to the width and height of the label
    //In the constructor the adjacency list is filled
    public AdjacencyList(List<Point> points, int width, int height) {
        for (Point p : points) {
            for (int i = 1; i <= 4; i++) {
                Label label = new Label(width, height, p, i);
                map.put(label, new HashSet());
                labels.add(label);
            }
        }
        setPairs();
        for (LabelPair pair : pairs) {
            if (pair.l1.overlaps(pair.l2)) {
                Set labelSet = map.get(pair.l1);
                labelSet.add(pair.l2);
                map.put(pair.l1, labelSet);
                Set labelSet2 = map.get(pair.l2);
                labelSet2.add(pair.l1);
                map.put(pair.l2, labelSet2);
            }
        }
    }

    public void setPairs() {
        boolean isNew = true;

        //add all possible points
        for (Label label1 : labels) {
            for (Label label2 : labels) {
                /**
                 * for each pair check if it does not already exist the other
                 * way around (i.e pair(p1, p2) is same as (pair(p2, p1)) if
                 * not, add to pairs
                 */
                for (LabelPair pair : pairs) {
                    if (pair.l1.equals(label2) && pair.l2.equals(label1)) {
                        isNew = false;
                        break;
                    }
                }
                if (isNew) {
                    pairs.add(new LabelPair(label1, label2));
                }
            }
        }
    }
}
