/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AI.AgentClasses.Actions;

import AI.AgentClasses.Action;
import AI.AgentClasses.Agent;
import AI.AgentClasses.Organ;
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
    public Jump(Organ a){
            organ=a;
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

    @Override
    public void cancelAction(World world, int xprevious, int yprevious, int xnext, int ynext) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Action copy() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
