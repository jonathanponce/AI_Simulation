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
import AI.Food;
import AI.Square;
import AI.World;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.lang.Math;
import java.util.Random;
/**
 *
 * @author Halyna
 */
public class Smell extends Sense {

    public Smell(int range) {
        super(range, 0);
    }

    @Override
    public String getName() {
        return "smell";
    }

    @Override
    public int senseVariable(World world, int agentX, int agentY, int squareX, int squareY, String name) {
        return 0;
    }

    @Override
    public Element senseElement(World world, int agentX, int agentY, int squareX, int squareY) {
        // the farther away we are from the agent, the less accuracy it will have.
        // this means it might return there is (or not) food, but it might be wrong.
        
        //can only sense food, even if theres an obstacle
        Element e = world.getSquare(squareX, squareY).getElement();
        if (e.isAgent()) return null; // there is no food but there might be an agent
        
        
        double distance = Math.sqrt(Math.pow(squareX - agentX, 2) + Math.pow(squareY - agentY, 2));
        int random = new Random().nextInt(this.getElementRange());
        
        if (random > distance) // it will sense INCORRECTLY
            if (e == null)
                return new Food(); // it thinks there's food but theres not
            else
                return null; // it thinks theres no food but there is
        else // it will sense CORRECTLY
            return e; // return whether theres food or not
    }
    
}
