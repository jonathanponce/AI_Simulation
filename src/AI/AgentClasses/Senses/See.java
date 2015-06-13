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
import AI.NonElement;
import AI.Square;
import AI.World;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Halyna
 */
public class See extends Sense{

    int optimalLight = 50;
    public See(int range) {
        super(range, 0); // See can only SEE elements, not detect variables
    }

    @Override
    public String getName() {
        return "see";
    }

    @Override
    public int senseVariable(World world, int agentX, int agentY, int squareX, int squareY, String name) {
        // seeing cannot detect a variable
        return 0;
    }

    @Override
    public Element senseElement(World world, int agentX, int agentY, int squareX, int squareY) {
        int slopeX = squareX - agentX;
        int slopeY = squareY - agentY;
        int div = GCD(slopeX, slopeY);
        if (div==0) div =1;
        slopeX /= div;
        slopeY /= div; // get the smallest increment possible for the vector
        boolean obstacle = false;
        
        // lets check inbetween the two points without considering the frontier
        int x = agentX + slopeX;
        int y = agentY + slopeY;
        while (x != squareX && y != squareY) {
            try {
                if (world.getElement(x, y) != null) // there is an obstacle 
                {
                    obstacle = true;
                    break;
                }
            } catch (Exception ex) {
                Logger.getLogger(See.class.getName()).log(Level.SEVERE, null, ex);
            }
            x += slopeX;
            y += slopeY;
        }
        if (!obstacle)
            try {
                int light = world.getVariable("light", squareX, squareY);
                float random = world.getRandom();
                float quotient = light/optimalLight;
                if (quotient < random) {
                    return new NonElement();
                }                
                return world.getElement(squareX,squareY); // no need to check through the frontiere
            } catch (Exception ex) {
                Logger.getLogger(See.class.getName()).log(Level.SEVERE, null, ex);
            }
        // now lets check if the agent could see through the frontiere (if an obstacle was found in the direct path)
        x = squareX + slopeX;
        y = squareY + slopeY;
        int width = world.getSize()[0];
        int height = world.getSize()[1];
        while ( (x >=0 && y >= 0) && ( x < width && y < height) ) { // while x and y are inside the square
            try {
                if (world.getElement(x, y) != null) // there is an obstacle 
                {
                    obstacle = true;
                    break;
                }
            } catch (Exception ex) {
                Logger.getLogger(See.class.getName()).log(Level.SEVERE, null, ex);
            }
            x = squareX + slopeX;
            y = squareY + slopeY;
        
        }
        // lets check the other way
        x = agentX - slopeX;
        y = agentY - slopeY;
        while ( (x >=0 && y >= 0) && ( x < width && y < height) ) { // while x and y are inside the square
            try {
                if (world.getElement(x, y) != null) // there is an obstacle 
                {
                    obstacle = true;
                    break;
                }
            } catch (Exception ex) {
                Logger.getLogger(See.class.getName()).log(Level.SEVERE, null, ex);
            }
            x = agentX - slopeX;
            y = agentY - slopeY;
        
        }
        if (obstacle)
            return new NonElement(); // we couldnt see anything because of obstacles
        else
            try {
                int light = world.getVariable("light", squareX, squareY);
                float random = world.getRandom();
                float quotient = light/optimalLight;
                if (quotient < random) {
                   return new NonElement();
                }                
                return world.getElement(squareX, squareY); // no obstacles
            } 
            catch (Exception ex) {
                Logger.getLogger(See.class.getName()).log(Level.SEVERE, null, ex);
            }
        return new NonElement();
    }
    
    private int GCD(int a, int b) {
        if (b==0) return a;
        return GCD(b,a%b);
    }

    
}
