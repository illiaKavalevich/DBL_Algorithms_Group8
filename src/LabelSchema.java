
import java.util.ArrayList;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author s106010
 */

/**
 * A label schema is a sequence of label positions for all points
 * In case a label has not been placed, its position is the string "#"
*/

public class LabelSchema {
    
    protected List<PosLabel> Schema = new ArrayList();
    
    public LabelSchema() {}
    
    public LabelSchema(ArrayList<PosLabel> s) {
        Schema = s;
    }
    
    protected void addToSchema(PosLabel pp) {
        Schema.add(pp);
    }
}
