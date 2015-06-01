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
 * @author User
 */
public class See extends Sense{

    public See(int range) {
        super(range);
    }

    @Override
    public String getName() {
        return "see";
    }

    @Override
    public Element sense(World world, int agentX, int agentY, int squareX, int squareY) {
        int dx = Math.abs(squareX - agentX);
        int dy = Math.abs(squareY -agentY);
        int X = agentX;
        int Y = agentY;
        int v = 0;
        while(X != squareX && Y != squareY) { // check if theres an obstacle first
            dx = Math.abs(squareX - X);
            dy = Math.abs(squareY - Y);
            if (dx>dy){
                Y++;
            }
            else {
                X++;
            }
            
            Square s = world.getSquare(X, Y);
            if (s.getElement() != null) // there is an obstacle 
            {
                return null; // we should return an error code so to not confuse this with an empty square
            }
        }
        return world.getSquare(squareX, squareY).getElement();
    
    }

    
}
