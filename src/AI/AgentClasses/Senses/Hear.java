/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AI.AgentClasses.Senses;

import AI.AgentClasses.Sense;
import AI.AgentClasses.Agent;
import AI.AgentClasses.Organ;
import AI.Main;
import AI.Element;
import AI.NonElement;
import AI.Square;
import AI.World;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Halyna
 */
public class Hear extends Sense{

    public Hear(int range,Organ a) {
        super(range, 0); // ELEMENTS ONLY
        organ=a;
        organ.addSens(this);
    }

    @Override
    public String getName() {
        return "hear"; //To change body of generated methods, choose Tools | Templates.
    }


    @Override
    public int senseVariable(World world, int agentX, int agentY, int squareX, int squareY, String name) {
        return 0;
    }

    @Override
    public Element senseElement(World world, int agentX, int agentY, int squareX, int squareY) {
        // can only hear other agents, no matter if theres an obstacle or not
        Element e = null;
        try {
            e = world.getElement(squareX, squareY);
        } catch (Exception ex) {
            Logger.getLogger(Hear.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (e.isAgent())
            return e;
        else
            return new NonElement(); // no agent but doesnt mean empty square
    }
    
}
