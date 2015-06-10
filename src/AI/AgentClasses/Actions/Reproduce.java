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
    public boolean isActionPossible(World world, int x, int y, int xnext, int ynext) {
        if (x != xnext || y != ynext) {
            try {
                Element parentx = world.getElement(x, y);
                if (parentx == null || parentx.getClass() != Agent.class) {
                    return false;
                }
                Element parenty = world.getElement(xnext, ynext);
                if (parenty == null || parenty.getClass() != Agent.class) {
                    return false;
                }

                int xchild = x - 1;
                int ychild = y - 1;
                while (world.getElement(xchild, ychild) != null) {
                    xchild++;
                    if (xchild > x + 1) {
                        xchild = x - 1;
                        ychild++;
                    }
                    if (ychild > y + 1) {
                        return false;//no empty spaces sorrounding
                    }
                }
                if (((Agent) parentx).getCharacteristic("fat") <= 5 || ((Agent) parenty).getCharacteristic("fat") <= 5) {
                    return false;
                }
                return true;
            } catch (Exception ex) {
                Logger.getLogger(Reproduce.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public int doAction(World world, int x, int y, int xnext, int ynext) {
        // We first have to choose an empty space for the child.
        int xchild = x - 1;
        int ychild = y - 1;
        try {
            Element parentx = world.getElement(x, y);
            Element parenty = world.getElement(xnext, ynext);

            while (world.getElement(xchild, ychild) != null) {
                xchild++;
                if (xchild > x + 1) {
                    xchild = x - 1;
                    ychild++;
                }
                if (ychild > y + 1) {
                    // This case won't occur because it's already verify by the isPossibleAction.
                    return 0;//no empty spaces sorrounding
                }
            }
            Agent ax = (Agent) parentx;
            Agent ay = (Agent) parenty;
            child = new Agent(world, xchild, ychild);

            // Creation of the characteristics of the child
            for (String charName : Agent.agentCharacteristics) {
                //System.out.println(charName + " = " + 10);
                child.setCharacteristic(charName, 10);//maybe we should average this value?
            }
            /*Iterator it = ax.getCharacteristics().entrySet().iterator();
             while (it.hasNext()) {
             Map.Entry pair = (Map.Entry) it.next();
             System.out.println(pair.getKey() + " = " + pair.getValue());
             child.setCharacteristic((String) pair.getKey(), 10);//maybe we should average this value?
             it.remove(); // avoids a ConcurrentModificationException
             }
             Iterator it2 = ay.getCharacteristics().entrySet().iterator();
             while (it2.hasNext()) {
             Map.Entry pair = (Map.Entry) it2.next();
             //System.out.println(pair.getKey() + " = " + pair.getValue());
             child.setCharacteristic((String) pair.getKey(), 10);//maybe we should average this value?
             it2.remove(); // avoids a ConcurrentModificationException
             }*/
            Random r = new Random(System.currentTimeMillis());
            boolean flag = false;
            boolean seen[] = new boolean[ay.getOrgans().size()];
            for (int i = 0; i < ax.getOrgans().size(); i++) {
                for (int j = 0; j < ay.getOrgans().size(); j++) {
                    if (ax.getOrgans().get(i).getOrganName().equals(ay.getOrgans().get(j).getOrganName())) {
                        flag = true;
                        seen[j] = true;
                        if (r.nextFloat() > 0.1) {
                            child.addOrgan(ax.getOrgans().get(i).copy());
                        }
                    }
                }
                if (flag == false) {
                    if (r.nextFloat() > 0.5) {
                        child.addOrgan(ax.getOrgans().get(i).copy());
                    }
                } else {
                    flag = false;
                }
            }
            for (int i = 0; i < ay.getOrgans().size(); i++) {
                if (seen[i] == false && r.nextFloat() > 0.5) {
                    child.addOrgan(ay.getOrgans().get(i).copy());
                }
            }
            /*if (r.nextFloat() > 0.9) {
             child.addOrgan(new Organ("empty organ"));
             }*/

            world.setElement(child, xchild, ychild);
            ax.setCharacteristic("fat", ((Agent) world.getElement(x, y)).getCharacteristic("fat") - 5);
            ay.setCharacteristic("fat", ((Agent) world.getElement(xnext, ynext)).getCharacteristic("fat") - 5);
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
        /*
         * This function doesn't work. Probably because the child was modify.
         */
        world.removeElement(child.getPosx(), child.getPosy());
    }

}
