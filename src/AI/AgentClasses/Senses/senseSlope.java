/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AI.AgentClasses.Senses;

import AI.AgentClasses.Organ;
import AI.AgentClasses.Sense;
import AI.Element;
import AI.NonElement;
import AI.World;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Halyna
 */
public class senseSlope extends Sense{

    public senseSlope(int elementRange, int variableRange,Organ a) {
        super(0, variableRange);// can only sense a variable
        organ=a;
        organ.addSens(this);
    }

    @Override
    public String getName() {
        return "senseSlope";
    }

    @Override
    public int senseVariable(World world, int agentX, int agentY, int squareX, int squareY, String name) {
        if (name == "slope")
            try {
                return world.getVariable("slope", squareX, squareY);
        } catch (Exception ex) {
            Logger.getLogger(senseSlope.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    @Override
    public Element senseElement(World world, int agentX, int agentY, int squareX, int squareY) {
        return new NonElement();
       
    }
    
}
