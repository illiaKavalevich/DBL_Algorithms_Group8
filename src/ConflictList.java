
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
    String model;
    java.util.concurrent.locks.Lock lock = new java.util.concurrent.locks.ReentrantLock();

    //copy constructor
    public ConflictList(ConflictList cl) {
        this.actConflict = new HashMap<>(cl.actConflict);
        this.posConflict = new HashMap<>(cl.posConflict);
        this.actLpairs = new ArrayList<>(cl.actLpairs);
        this.posLpairs = new ArrayList<>(cl.posLpairs);
        this.activeLabels = new ArrayList<>(cl.activeLabels);
        this.possibleLabels = new ArrayList<>(cl.possibleLabels);
        this.model = cl.model;
    }

    //width and height refers to the width and height of the label
    //In the constructor the adjacency list is filled
    public ConflictList(List<Point> points, String model) {
        System.out.println("Started on conflictList");
        this.model = model;
        for (Point p : points) {
            for (Label label : p.possibleLabels) {
                //posConflict.put(label, new HashSet());
                possibleLabels.add(label);
            }
            if (model.equals("1slider")) {
                //System.out.println("label added to conflictlist");
                activeLabels.add(p.getActiveLabelSlider());
            } else {
                activeLabels.add(p.getActiveLabelPos());
            }
        }
        setPairsPos();
        setPairsAct();
        for (LabelPair pair : posLpairs) {
            if (pair.l1.overlaps(pair.l2)) {
                if (!posConflict.containsKey(pair.l1)) {
                    posConflict.put(pair.l1, new HashSet<Label>());
                }
                Set labelSet = posConflict.get(pair.l1);
                labelSet.add(pair.l2);
                posConflict.put(pair.l1, labelSet);
                if (!posConflict.containsKey(pair.l2)) {
                    posConflict.put(pair.l2, new HashSet<Label>());
                }
                Set labelSet2 = posConflict.get(pair.l2);
                labelSet2.add(pair.l1);
                posConflict.put(pair.l2, labelSet2);
            }
        }
        //System.out.println("number of labels in actLpairs: " + actLpairs.size());
        for (LabelPair pair : actLpairs) {

            if (pair.l1.overlaps(pair.l2)) {
                //System.out.println("we have overlap");
                if (!actConflict.containsKey(pair.l1)) {
                    //System.out.println("put label in hashset");
                    actConflict.put(pair.l1, new HashSet<Label>());
                }
                Set labelSet = actConflict.get(pair.l1);
                labelSet.add(pair.l2);
                actConflict.put(pair.l1, labelSet);
                if (!actConflict.containsKey(pair.l2)) {
                    //System.out.println("put label in hashset");
                    actConflict.put(pair.l2, new HashSet<Label>());
                }
                Set labelSet2 = actConflict.get(pair.l2);
                labelSet2.add(pair.l1);
                actConflict.put(pair.l2, labelSet2);
            }

        }
    }

    public void ActConflicts(Label l) {
        for (LabelPair pair : actLpairs) {
            if (pair.l1 == l || pair.l2 == l) {
                if (pair.l1.overlaps(pair.l2)) {
                    if (!actConflict.containsKey(pair.l1)) {
                        actConflict.put(pair.l1, new HashSet<Label>());
                    }
                    Set labelSet = actConflict.get(pair.l1);
                    labelSet.add(pair.l2);
                    actConflict.put(pair.l1, labelSet);
                    if (!actConflict.containsKey(pair.l2)) {
                        actConflict.put(pair.l2, new HashSet<Label>());
                    }
                    Set labelSet2 = actConflict.get(pair.l2);
                    labelSet2.add(pair.l1);
                    actConflict.put(pair.l2, labelSet2);
                }

            }
        }
    }

    public void updateActConflicts(Label l) {
        //lock.lock();
        for (LabelPair pair : actLpairs) {
            if (pair.l1 == l || pair.l2 == l) {
                if (pair.l1.overlaps(pair.l2)) {
                    if (!actConflict.containsKey(pair.l1)) {
                        actConflict.put(pair.l1, new HashSet<Label>());
                    }
                    Set labelSet = actConflict.get(pair.l1);
                    labelSet.add(pair.l2);
                    actConflict.put(pair.l1, labelSet);
                    if (!actConflict.containsKey(pair.l2)) {
                        actConflict.put(pair.l2, new HashSet<Label>());
                    }
                    Set labelSet2 = actConflict.get(pair.l2);
                    labelSet2.add(pair.l1);
                    actConflict.put(pair.l2, labelSet2);
                }

            }
        }
    }

    public void setPairsPos() {
        //add all possible labels
        for (int i = 0; i < possibleLabels.size(); i++) {
            for (int j = i + 1; j < possibleLabels.size(); j++) {
                posLpairs.add(new LabelPair(possibleLabels.get(i), possibleLabels.get(j)));

            }
        }
    }

    public void setPairsAct() {

        for (int i = 0; i < activeLabels.size(); i++) {
            for (int j = i + 1; j < activeLabels.size(); j++) {
                //System.out.println("added label pair");
                actLpairs.add(new LabelPair(activeLabels.get(i), activeLabels.get(j)));
            }
        }
    }

    public Set<Label> getPosConflictLabels(Label l) {
        return posConflict.get(l);
    }

    public int getPosDegree(Label l) {
        if (hasPosConflicts(l)) {
            return posConflict.get(l).size();
        }
        return 0;
    }

    public boolean hasPosConflicts(Label l) {
        return posConflict.containsKey(l);
    }

    public Set<Label> getActConflictLabels(Label l) {
        return actConflict.get(l);
    }

    public int getActDegree(Label l) {
        if (hasActConflicts(l)) {
            return actConflict.get(l).size();
        }
        return 0;
    }

    public boolean hasActConflicts(Label l) {
        return actConflict.containsKey(l);
    }

    //get all active labels Label l overlaps with
    //l may also be a possible label
    public Set<Label> getPosActConflictLabels(Label l) {
        Set<Label> labelSet = posConflict.get(l);
        Set<Label> resultSet = new HashSet<>();
        for (Label label : labelSet) {
            if (label.minX == label.getPoint().getActiveLabelPos().minX && label.minY == label.getPoint().getActiveLabelPos().minY) {
                resultSet.add(label);
            }
        }
        return resultSet;
    }

    public int getPosActDegree(Label l) {
        if (hasPosConflicts(l)) {
            Set<Label> labelSet = posConflict.get(l);
            Set<Label> resultSet = new HashSet<>();
            for (Label label : labelSet) {
                if (label.minX == label.getPoint().getActiveLabelPos().minX && label.minY == label.getPoint().getActiveLabelPos().minY) {
                    resultSet.add(label);
                }
            }
            return resultSet.size();
        }
        return 0;
    }

    public void addPoint(Point p) {
        ArrayList<LabelPair> posNLpairs = new ArrayList<>();            //List of all pairs of possible labels with new labels
        for (Label label : p.possibleLabels) {
            posConflict.put(label, new HashSet());
            possibleLabels.add(label);
        }
        if (model.equals("1slider")) {
            activeLabels.add(p.getActiveLabelSlider());
        } else {
            activeLabels.add(p.getActiveLabelPos());
        }

        boolean isNew = true;                                           //true if a new pair is found

        //add all possible labels
        for (Label label1 : p.possibleLabels) {
            for (Label label2 : possibleLabels) {
                /**
                 * for each pair check if it does not already exist the other
                 * way around (i.e pair(p1, p2) is same as (pair(p2, p1)) and
                 * check if the labels do not belong to the same point. If not,
                 * add to pairs
                 */
                for (LabelPair pair : posNLpairs) {
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
                    posNLpairs.add(new LabelPair(label1, label2));
                }
            }
        }
        //Add active label conflicts
        Label label1;
        if (model.equals("1slider")) {
            label1 = p.getActiveLabelSlider();
        } else {
            label1 = p.getActiveLabelPos();
        }
        for (Label label : activeLabels) {
            if (label.overlaps(label1)) {
                Set labelSet = actConflict.get(label);
                labelSet.add(label1);
                actConflict.put(label, labelSet);
                Set labelSet2 = actConflict.get(label1);
                labelSet2.add(label1);
                actConflict.put(label1, labelSet2);
            }
        }

        //add possible label conflicts
        for (LabelPair pair : posNLpairs) {
            if (pair.l1.overlaps(pair.l2)) {
                Set labelSet = posConflict.get(pair.l1);
                labelSet.add(pair.l2);
                posConflict.put(pair.l1, labelSet);
                Set labelSet2 = posConflict.get(pair.l2);
                labelSet2.add(pair.l1);
                posConflict.put(pair.l2, labelSet2);
            }
        }
    }

    public void removePoint(Point p) {
        //remove the possible labels (and pairs) that belong to this point
        for (Label removeLabel : p.getPossibleLabels()) {
            posConflict.remove(removeLabel);
            possibleLabels.remove(removeLabel);
            for (Label label : possibleLabels) {
                posConflict.remove(label, removeLabel);
            }
            HashSet<LabelPair> removeSet = new HashSet<>();
            for (LabelPair pair : posLpairs) {
                if (pair.l1.equals(removeLabel) || pair.l2.equals(removeLabel)) {
                    removeSet.add(pair);
                }
            }
            for (LabelPair removePair : removeSet) {
                posLpairs.remove(removePair);
            }
        }
        //remove the active labels (and pairs) that belong to this point
        Label removeLabel;
        if (model.equals("1slider")) {
            removeLabel = p.getActiveLabelSlider();
        } else {
            removeLabel = p.getActiveLabelPos();
        }
        actConflict.remove(removeLabel);
        activeLabels.remove(removeLabel);
        for (Label label : activeLabels) {
            actConflict.remove(label, removeLabel);
        }
        HashSet<LabelPair> removeSet = new HashSet<>();
        for (LabelPair pair : actLpairs) {
            if (pair.l1.equals(removeLabel) || pair.l2.equals(removeLabel)) {
                removeSet.add(pair);
            }
        }
        for (LabelPair removePair : removeSet) {
            actLpairs.remove(removePair);
        }
    }

    void removeLabel(Label l) {
        posConflict.remove(l);
        possibleLabels.remove(l);
        for (Label label : possibleLabels) {
            posConflict.remove(label, l);
        }
        HashSet<LabelPair> removeSet = new HashSet<>();
        for (LabelPair pair : posLpairs) {
            if (pair.l1.equals(l) || pair.l2.equals(l)) {
                removeSet.add(pair);
            }
        }
        for (LabelPair removePair : removeSet) {
            posLpairs.remove(removePair);
        }

        actConflict.remove(l);
        activeLabels.remove(l);
        for (Label label : activeLabels) {
            actConflict.remove(label, l);
        }
        HashSet<LabelPair> removeSet2 = new HashSet<>();
        for (LabelPair pair : actLpairs) {
            if (pair.l1.equals(l) || pair.l2.equals(l)) {
                removeSet2.add(pair);
            }
        }
        for (LabelPair removePair : removeSet2) {
            actLpairs.remove(removePair);
        }
    }

    public void removeActiveLabel(Label l) {
        actConflict.remove(l);
        activeLabels.remove(l);
        for (Label label : actConflict.keySet()) {
            Set labelSet = actConflict.get(label);
            labelSet.remove(l);
            actConflict.put(label, labelSet);
        }
        HashSet<LabelPair> removeSet = new HashSet<>();
        for (LabelPair pair : actLpairs) {
            if (pair.l1 == l || pair.l2 == l) {
                removeSet.add(pair);
            }
        }
        for (LabelPair removePair : removeSet) {
            actLpairs.remove(removePair);
        }
    }

}
