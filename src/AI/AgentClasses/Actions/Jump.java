/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AI.AgentClasses.Actions;

import AI.AgentClasses.Action;
import AI.AgentClasses.Agent;
import AI.Element;
import AI.World;


/**
 *
 * @author Haly et Florent
 */
public class Jump extends Action {

    @Override
    public String getName() {
        return "fly";
    }

    @Override
    public int doAction(World world, int x, int y, int xnext, int ynext) {
        
        try {
            Element thisElement = world.getElement(x, y);
            if (thisElement == null) {
                return 0;
            }
            if (x != xnext || y != ynext) {
                ((Agent) thisElement).moveTo(xnext, ynext);
                world.setElement(thisElement, xnext, ynext);
                world.removeElement(x, y);
            }
            return 1;
        } catch (Exception e) {
            System.out.println(e);
            return 0;
        }
    }

    @Override
    public int evaluateAction(World world, int x, int y, int xnext, int ynext) {
        return Math.abs(ynext-y) + Math.abs(xnext-x); // manhattan distance
    }
    
}
