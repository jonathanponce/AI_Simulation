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
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author matthieugallet
 */
public class Reproduce extends Action {

    private Agent child;

    @Override
    public String getName() {
        return "reproduce";
    }

    @Override
    public int doAction(World world, int x, int y, int xnext, int ynext) {
        // We first have to choose an empty space for the child.
        int xchild = x - 1;
        int ychild = y - 1;
        try {
            Element parentx = world.getElement(x, y);
            if (parentx == null || parentx.getClass() != Agent.class) {
                return 0;
            }
            Element parenty = world.getElement(xnext, ynext);
            if (parenty == null || parenty.getClass() != Agent.class) {
                return 0;
            }

            while (world.getElement(xchild, ychild) != null) {
                xchild++;
                if (xchild > x + 1) {
                    xchild = x - 1;
                    ychild++;
                }
                if (ychild > y + 1) {
                    return 0;//no empty spaces sorrounding
                }
            }
            Agent ax = (Agent) parentx;
            Agent ay = (Agent) parenty;
            child = new Agent(world, xchild, ychild);
            Iterator it = ax.getCharacteristics().entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                //System.out.println(pair.getKey() + " = " + pair.getValue());
                child.setCharacteristic((String)pair.getKey(), 10);//maybe we should average this value?
                it.remove(); // avoids a ConcurrentModificationException
            }
            Iterator it2 = ay.getCharacteristics().entrySet().iterator();
            while (it2.hasNext()) {
                Map.Entry pair = (Map.Entry) it2.next();
                //System.out.println(pair.getKey() + " = " + pair.getValue());
                child.setCharacteristic((String)pair.getKey(), 10);//maybe we should average this value?
                it2.remove(); // avoids a ConcurrentModificationException
            }
            Random r=new Random(System.currentTimeMillis());
            for (int i = 0; i < ax.getOrgans().size(); i++) {
                if(r.nextFloat()>0.5){
                    child.addOrgan(ax.getOrgans().get(i));
                }
            }
            for (int i = 0; i < ay.getOrgans().size(); i++) {
                if(r.nextFloat()>0.5){
                    child.addOrgan(ay.getOrgans().get(i));
                }
            }
            

            world.setElement(child, 1, 1);
            ((Agent) world.getElement(x, y)).setCharacteristic("fat", ((Agent) world.getElement(x, y)).getCharacteristic("fat") - 5);
            ((Agent) world.getElement(xnext, ynext)).setCharacteristic("fat", ((Agent) world.getElement(xnext, ynext)).getCharacteristic("fat") - 5);
            return 1;
        } catch (Exception ex) {
            Logger.getLogger(Reproduce.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
        //return 0;
    }

    @Override
    public int evaluateAction(World world, int x, int y, int xnext, int ynext) {
        // The evaluation is an arbitrary value for now.
        return 150;
    }

    @Override
    public void cancelAction(World world, int xprevious, int yprevious, int xnext, int ynext) throws Exception {
        ((Agent) world.getElement(xprevious, yprevious)).setCharacteristic("fat", ((Agent) world.getElement(xprevious, yprevious)).getCharacteristic("fat") + 5);
        ((Agent) world.getElement(xnext, ynext)).setCharacteristic("fat", ((Agent) world.getElement(xnext, ynext)).getCharacteristic("fat") + 5);
        world.removeElement(child.getPosx(), child.getPosy());
    }

}
