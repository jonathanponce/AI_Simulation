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
public class Roll extends Action {

    @Override
    public String getName() {
        return "roll";
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
    public int evaluateAction(World world, int x, int y, int xnext, int ynext) throws Exception {
        int dx = Math.abs(xnext - x);
        int dy = Math.abs(ynext -y);
        int vx = (int)Math.signum(dx);
        int vy = (int)Math.signum(dx);
        int X = x;
        int Y = y;
        int currentSlope = world.getSquare(X, Y).getVariable("slope");
        int v = 0;
        while(X != xnext && Y != ynext) {
            dx = Math.abs(xnext - X);
            dy = Math.abs(ynext - Y);
            if (dx>dy){
                X+=vx;
            }
            else {
                Y+=vy;
            }
            Square s = world.getSquare(X, Y);
            v+=(currentSlope - s.getVariable("slope"));
            if (v<0) {
                return 0;
            }
            currentSlope = s.getVariable("slope");
        }
        return v;
    }

    @Override
    public void cancelAction(World world, int xprevious, int yprevious, int xnext, int ynext) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
