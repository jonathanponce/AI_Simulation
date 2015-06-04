/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AI.AgentClasses.Actions;

import AI.AgentClasses.Action;
import AI.World;

/**
 *
 * @author matthieugallet
 */
public class Reproduce extends Action {

    @Override
    public String getName() {
        return "reproduce";
    }

    @Override
    public int doAction(World world, int x, int y, int xnext, int ynext) {
        // We first have to choose an empty space for the child.
        
        
        // then we have to create the child.
        
        // Now we have to remove 5 points (arbitrary value) of food from each parent.
        
        return 0;
    }

    @Override
    public int evaluateAction(World world, int x, int y, int xnext, int ynext) {
        // The evaluation is an arbitrary value for now.
        return 150;
    }

    @Override
    public void cancelAction(World world, int xprevious, int yprevious, int xnext, int ynext) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
