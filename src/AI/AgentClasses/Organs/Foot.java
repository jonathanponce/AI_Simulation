/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AI.AgentClasses.Organs;

import AI.AgentClasses.Action;
import AI.AgentClasses.Organ;
import AI.AgentClasses.Actions.*;

/**
 *
 * @author matthieugallet
 */
public class Foot extends Organ {

    public Foot(int size) {
        super();
        /* The size of the foot cannot be negativ.
         * I chose to limitate the size to 10.
         * The distance we walk per turn depends of the size of the foot.
        */ 
        int sizeCorrect= size<0? 0 : size;
        sizeCorrect= sizeCorrect>10? 10 : sizeCorrect;
        this.addCharacteristic("size", sizeCorrect);
        Walk walk= new Walk();
        
        Integer temp1[] = {this.getCharacteristic("size")};
        walk.addCondition("distance", temp1);
        Integer temp2[] = {0};
        walk.addCondition("food", temp2);
        walk.addCondition("agent", temp2);
        this.addAction(walk);
    }

    @Override
    public String getOrganName() {
        return "foot";
    }
    
    
    
}
