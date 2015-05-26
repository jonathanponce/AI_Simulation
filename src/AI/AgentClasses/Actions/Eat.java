/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AI.AgentClasses.Actions;

import AI.AgentClasses.Action;
import AI.AgentClasses.Agent;
import AI.Main;
import AI.Element;
import AI.World;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author matthieugallet
 */
public class Eat extends Action {

    @Override
    public String getName() {
        return "eat";
    }

    @Override
    public int doAction(World world, int x, int y, int xnext, int ynext) {
        try {
            if (world.getElement(xnext, ynext) != null && world.getElement(xnext, ynext).getName().equals("food")) {
                world.removeElement(xnext, ynext);
                ((Agent) world.getElement(x, y)).setCharacteristic("fat", ((Agent) world.getElement(x, y)).getCharacteristic("fat") + 10);
                return 1;
            }
            return 0;
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    @Override
    public int evaluateAction(World world, int x, int y, int xnext, int ynext) {
        return 99;
    }
    
}