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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author matthieugallet
 */
public class Walk extends Action {

    @Override
    public String getName() {
        return "walk";
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
        int res = 0;
        int xresult, yresult;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                try {
                    xresult = (xnext + i) < 0 ? xnext + i + world.getSize()[0] : xnext + i;
                    yresult = (ynext + j) < 0 ? ynext + j + world.getSize()[1] : ynext + j;
                    Element objFutureSquare = world.getElement(xresult % world.getSize()[0], yresult % world.getSize()[1]);
                    if (objFutureSquare != null && objFutureSquare.getName().equals("food")) {
                        res++;
                    }
                    xresult = (x + i) < 0 ? x + i + world.getSize()[0] : x + i;
                    yresult = (y + j) < 0 ? y + j + world.getSize()[1] : y + j;
                    objFutureSquare = world.getElement(xresult % world.getSize()[0], yresult % world.getSize()[1]);
                    if (objFutureSquare != null && objFutureSquare.getName().equals("food")) {
                        res--;
                    }
                } catch (Exception ex) {
                    Logger.getLogger(Agent.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return res;
    }
    
}