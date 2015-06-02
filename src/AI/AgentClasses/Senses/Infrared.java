/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AI.AgentClasses.Senses;

import AI.AgentClasses.Sense;
import AI.AgentClasses.Agent;
import AI.Main;
import AI.Element;
import AI.Square;
import AI.World;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Halyna
 */
public class Infrared extends Sense{

    public Infrared(int range) {
        super(range);
    }

    @Override
    public String getName() {
        return "infrared";
    }

    @Override
    public Element sense(World world, int agentX, int agentY, int squareX, int squareY) {
        //can only sense food, even if theres an obstacle
        Element e = world.getSquare(squareX, squareY).getElement();
        if (!e.isAgent())
            return e;
        return null;
    }
    
}
