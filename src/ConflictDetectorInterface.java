import java.util.Set;

/**
 * This class serves as an interface for a data structure that keeps track of the
 * overlap between labels
 **/

public interface ConflictDetectorInterface {
    
    /**
     * Computes the number of active labels a given label intersects with
     * 
     * @param l
     * @return The number of active labels l overlaps with
     */
    public int getActDegree(Label l);
      
    /**
     * Gets all the labels a given label conflicts with and returns them in a Set
     * 
     * @param l the Label of interest
     * @return A set of objects of type Label that are set active that l 
     * conflicts with.
     * @pre l is an active label
     */
    public Set<Label> getActConflictLabels(Label l);
    
    /**
     * Computes the number of possible labels a given label intersects with
     * 
     * @param l
     * @return The number of possible labels l overlaps with
     * @pre l is not a SliderLabel
     */
    public int getPosDegree(Label l);
    
    /**
     * Gets all the possible labels a given label conflicts with and returns them in a Set
     * 
     * @param l the Label of interest
     * @return A set of objects of type Label that l conflicts with.
     * @pre l is not a SliderLabel
     */
    public Set<Label> getPosConflictLabels(Label l);
    
    /**
     * Removes a given point and all its associated labels from the data structure
     * 
     * @param p the point to be removed
     */
    public void removePoint(Point p);
    
    /**
     * This method is to be called before a change is made to which label is active,
     * and updateNotifyPost is to be called after the update. This allows the data
     * structure to handle changes in an efficient manner.
     * 
     * @param p the point to be updated
     */
    //public void updateNotifyPre(Point p);
    
    /**
     * This method is to be called after a change is made to which label is active,
     * and updateNotifyPre must be called before the update. This allows the data
     * structure to handle changes in an efficient manner.
     * 
     * @pre updateNotifyPre is called first
     */
    //public void updateNotifyPost();
}
