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
public class feelTemperature extends Sense{

    public feelTemperature() {
        super(0, 1); // can only feel the temperature in the square around him. cannot sense elements
    }

    @Override
    public String getName() {
        return "feelTemperature";
    }

   

    @Override
    public int senseVariable(World world, int agentX, int agentY, int squareX, int squareY, String name) {
       if (name == "temperature") {
            try {
                return world.getVariable("temperature", squareX, squareY);
            } catch (Exception ex) {
                Logger.getLogger(feelTemperature.class.getName()).log(Level.SEVERE, null, ex);
            }
       }
        return 0;
    }

    @Override
    public Element senseElement(World world, int agentX, int agentY, int squareX, int squareY) {
        return new NonElement();
    }
    
}
