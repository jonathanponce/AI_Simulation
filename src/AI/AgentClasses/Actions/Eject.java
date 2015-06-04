/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AI.AgentClasses.Actions;
import AI.AgentClasses.Action;
import AI.AgentClasses.Agent;
import AI.Element;
import AI.Square;
import AI.World;
/**
 *
 * @author Haly et Florent
 */
public class Eject extends Action{

    private int actionRadius;
    
    public Eject(int actionRadius) {
        this.actionRadius = actionRadius;
    }
    @Override
    public String getName() {
        return "eject"; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int doAction(World world, int x, int y, int xnext, int ynext) {
        try {
            Square target = world.getSquare(xnext, ynext);
            if (target == null) {
                return 0;
            }
            if (x != xnext || y != ynext) {
                for (int i=-(this.actionRadius); i < this.actionRadius; i++) {
                    for (int j = -(this.actionRadius); j < this.actionRadius; j++) {
                        Square s = world.getSquare(xnext+i, ynext+j);
                        if (s != null) {
                            int light = s.getVariable("light");
                            // TODO : CHANGE LIGHT VARIABLE
                        }
                    }
                }
            }
            return 1;
        } catch (Exception e) {
            System.out.println(e);
            return 0;
        }
    }

    @Override
    public int evaluateAction(World world, int x, int y, int xnext, int ynext) {
        int nbOfAgents = 0;
         for (int i=-(this.actionRadius); i < this.actionRadius; i++) {
                    for (int j = -(this.actionRadius); j < this.actionRadius; j++) {
                        Square s = world.getSquare(xnext+i, ynext+j);
                        if (s != null) {
                            if (s.getElement().isAgent()){
                               nbOfAgents++; 
                            }
                        }
                    }
                }
         return nbOfAgents;
    }

    @Override
    public void cancelAction(World world, int xprevious, int yprevious, int xnext, int ynext) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
