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
public class feelTemperature extends Sense{

    public feelTemperature() {
        super(1); // can only feel the temperature in the square around him
    }

    @Override
    public String getName() {
        return "feelTemperature";
    }

    @Override
    public Integer sense(World world, int agentX, int agentY, int squareX, int squareY) {
        try {
            return world.getSquare(squareX, squareY).getVariable("temperature");
        } catch (Exception ex) {
            Logger.getLogger(feelTemperature.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
}
