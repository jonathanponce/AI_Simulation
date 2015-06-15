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
public class Infrared extends Sense{

    public Infrared(int range,Organ a) {
        super(0, range); // ELEMENTS ONLY
        organ=a;
    }

    @Override
    public String getName() {
        return "infrared";
    }


    @Override
    public int senseVariable(World world, int agentX, int agentY, int squareX, int squareY, String name) {
        return 0;
    }

    @Override
    public Element senseElement(World world, int agentX, int agentY, int squareX, int squareY) {
        //can only sense food, even if theres an obstacle
        Element e = null;
        try {
            e = world.getElement(squareX, squareY);
        } catch (Exception ex) {
            Logger.getLogger(Infrared.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (!e.isAgent())
            return e; // food
        return new NonElement(); // no food, but might be an agent
    }
    
}
