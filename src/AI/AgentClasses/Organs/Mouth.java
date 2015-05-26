/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AI.AgentClasses.Organs;

import AI.AgentClasses.Actions.*;
import AI.AgentClasses.Organ;

/**
 *
 * @author matthieugallet
 */
public class Mouth extends Organ {
    
    public Mouth() {
        super();
        Eat eat= new Eat();
        Integer temp[]={1};
        eat.addCondition("food", temp);
        eat.addCondition("distance", temp);
        this.addAction(eat);
    }

    @Override
    public String getOrganName() {
        return "mouth";
    }
    
}
