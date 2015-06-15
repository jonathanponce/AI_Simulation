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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Reproduce extends Action {

    //private Agent child;
    /**
     * This stack is a LIFO: it's the list of all the children of the agent.
     */
    private Stack<Agent> childs = new Stack<Agent>();

    @Override
    public String getName() {
        return "reproduce";
    }

    public Reproduce(Organ a) {
        this.addCondition("distance", new Integer[]{1});
        this.addCondition("agent", new Integer[]{1});
        organ = a;
        organ.addAction(this);
    }

    /**
     * This function verify if the both parents exist, have enough fat and if
     * there is an empty square for the child.
     *
     * @param world
     * @param x
     * @param y
     * @param xnext
     * @param ynext
     * @return true or false.
     */
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
                int xtarget = (xchild < 0 ? xchild + world.getSize()[0] : xchild) % world.getSize()[0];
                int ytarget = (ychild < 0 ? ychild + world.getSize()[1] : ychild) % world.getSize()[1];
                while (world.getElement(xtarget, ytarget) != null) {
                    xchild++;
                    if (xchild > x + 1) {
                        xchild = x - 1;
                        ychild++;
                    }
                    xtarget = (xchild < 0 ? xchild + world.getSize()[0] : xchild) % world.getSize()[0];
                    ytarget = (ychild < 0 ? ychild + world.getSize()[1] : ychild) % world.getSize()[1];
                    if (ychild > y + 1) {
                        return false;//no empty spaces sorrounding
                    }
                }
                if (((Agent) parentx).getCharacteristic("fat") <= 6 || ((Agent) parenty).getCharacteristic("fat") <= 6) {
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

    /**
     * Execute the reproduction including cross the parents and create a child.
     *
     * @param world
     * @param x
     * @param y
     * @param xnext
     * @param ynext
     * @return nothing important.
     */
    @Override
    public int doAction(World world, int x, int y, int xnext, int ynext) {
        // We first have to choose an empty space for the child.
        int xchild = x - 1;
        int ychild = y - 1;
        int xtarget = (xchild < 0 ? xchild + world.getSize()[0] : xchild) % world.getSize()[0];
        int ytarget = (ychild < 0 ? ychild + world.getSize()[1] : ychild) % world.getSize()[1];
        try {
            Element parentx = world.getElement(x, y);
            Element parenty = world.getElement(xnext, ynext);

            while (world.getElement(xtarget, ytarget) != null) {
                xchild++;
                if (xchild > x + 1) {
                    xchild = x - 1;
                    ychild++;
                }
                xtarget = (xchild < 0 ? xchild + world.getSize()[0] : xchild) % world.getSize()[0];
                ytarget = (ychild < 0 ? ychild + world.getSize()[1] : ychild) % world.getSize()[1];
                if (ychild > y + 1) {
                    // This case won't occur because it's already verify by the isPossibleAction.
                    return 0;//no empty spaces sorrounding
                }
            }
            Agent ax = (Agent) parentx;
            Agent ay = (Agent) parenty;
            Agent child = new Agent(world, xtarget, ytarget);
            childs.push(child);

            // Creation of the characteristics of the child
            for (String charName : Agent.agentCharacteristics) {
                //System.out.println(charName + " = " + 10);
                child.setCharacteristic(charName, 5);//maybe we should average this value?
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
                            child.addOrgan(ax.getOrgans().get(i).combine(ay.getOrgans().get(j)));

                        }
                    }
                }
                if (flag == false) {
                    if (r.nextFloat() > 0.5) {
                        child.addOrgan(ax.getOrgans().get(i).combine(null));
                    }
                } else {
                    flag = false;
                }
            }
            for (int i = 0; i < ay.getOrgans().size(); i++) {
                if (seen[i] == false && r.nextFloat() > 0.5) {
                    child.addOrgan(ay.getOrgans().get(i).combine(null));
                }
            }
            /*if (r.nextFloat() > 0.9) {
             child.addOrgan(new Organ("empty organ"));
             }*/

            /*System.out.print("child pos: ");
             System.out.print(child.getPosx());
             System.out.print(child.getPosy());
             System.out.print("-- target= ");
             System.out.print(xtarget);
             System.out.println(ytarget);*/
//            System.out.println("REPRODUCTION");
//            System.out.println("-------CHILD");
//            System.out.println("AGENT CHARACTERISTICS");
//            Iterator it =( (HashMap<String,Integer>)(child.getCharacteristics().clone())).entrySet().iterator();
//            while (it.hasNext()) {
//                Map.Entry pair = (Map.Entry) it.next();
//                System.out.println(pair.getKey() + " = " + pair.getValue());
//                it.remove(); // avoids a ConcurrentModificationException
//            }
//            for (int i = 0; i < child.getOrgans().size(); i++) {
//                System.out.println("ORGAN CHARACTERISTICS "+child.getOrgans().get(i).getOrganName());
//               // it = child.getOrgans().get(i).getCharacteristics().entrySet().iterator();
//                it =( (HashMap<String,Integer>)(child.getOrgans().get(i).getCharacteristics().clone())).entrySet().iterator();
//                while (it.hasNext()) {
//                    Map.Entry pair = (Map.Entry) it.next();
//                    System.out.println(pair.getKey() + " = " + pair.getValue());
//                    it.remove(); // avoids a ConcurrentModificationException
//                }
//                System.out.println("ACTION CHARACTERISTICS");
//                for (int j = 0; j < child.getOrgans().get(i).getActions().size(); j++) {
//                    //it = child.getOrgans().get(i).getActions().get(j).getCondition().entrySet().iterator();
//                    it =( (HashMap<String,Integer[]>)(child.getOrgans().get(i).getActions().get(j).getCondition().clone())).entrySet().iterator();
//                    while (it.hasNext()) {
//                        Map.Entry pair = (Map.Entry) it.next();
//                        System.out.println(pair.getKey() + " = " + pair.getValue());
//                        it.remove(); // avoids a ConcurrentModificationException
//                    }
//                }
//                System.out.println("SENSES CHARACTERISTICS");
//                for (int j = 0; j < child.getOrgans().get(i).getSenses().size(); j++) {
//                    //it = child.getOrgans().get(i).getSenses().get(j).getCondition().entrySet().iterator();
//                    it =( (HashMap<String,Integer[]>)(child.getOrgans().get(i).getSenses().get(j).getCondition().clone())).entrySet().iterator();
//                    while (it.hasNext()) {
//                        Map.Entry pair = (Map.Entry) it.next();
//                        System.out.println(pair.getKey() + " = " + pair.getValue());
//                        it.remove(); // avoids a ConcurrentModificationException
//                    }
//                }
//
//            }
//            System.out.println("-----------PARENT 1");
//            System.out.println("AGENT CHARACTERISTICS");
//             it =( (HashMap<String,Integer>)(ax.getCharacteristics().clone())).entrySet().iterator();
//            while (it.hasNext()) {
//                Map.Entry pair = (Map.Entry) it.next();
//                System.out.println(pair.getKey() + " = " + pair.getValue());
//                it.remove(); // avoids a ConcurrentModificationException
//            }
//            for (int i = 0; i < ax.getOrgans().size(); i++) {
//                System.out.println("ORGAN CHARACTERISTICS "+ax.getOrgans().get(i).getOrganName());
//               // it = ax.getOrgans().get(i).getCharacteristics().entrySet().iterator();
//                it =( (HashMap<String,Integer>)(ax.getOrgans().get(i).getCharacteristics().clone())).entrySet().iterator();
//                while (it.hasNext()) {
//                    Map.Entry pair = (Map.Entry) it.next();
//                    System.out.println(pair.getKey() + " = " + pair.getValue());
//                    it.remove(); // avoids a ConcurrentModificationException
//                }
//                System.out.println("ACTION CHARACTERISTICS");
//                for (int j = 0; j < ax.getOrgans().get(i).getActions().size(); j++) {
//                    //it = ax.getOrgans().get(i).getActions().get(j).getCondition().entrySet().iterator();
//                    it =( (HashMap<String,Integer[]>)(ax.getOrgans().get(i).getActions().get(j).getCondition().clone())).entrySet().iterator();
//                    while (it.hasNext()) {
//                        Map.Entry pair = (Map.Entry) it.next();
//                        System.out.println(pair.getKey() + " = " + pair.getValue());
//                        it.remove(); // avoids a ConcurrentModificationException
//                    }
//                }
//                System.out.println("SENSES CHARACTERISTICS");
//                for (int j = 0; j < ax.getOrgans().get(i).getSenses().size(); j++) {
//                    //it = ax.getOrgans().get(i).getSenses().get(j).getCondition().entrySet().iterator();
//                    it =( (HashMap<String,Integer[]>)(ax.getOrgans().get(i).getSenses().get(j).getCondition().clone())).entrySet().iterator();
//                    while (it.hasNext()) {
//                        Map.Entry pair = (Map.Entry) it.next();
//                        System.out.println(pair.getKey() + " = " + pair.getValue());
//                        it.remove(); // avoids a ConcurrentModificationException
//                    }
//                }
//
//            }
//            System.out.println("PARENT 2");
//            System.out.println("AGENT CHARACTERISTICS");
//           it =( (HashMap<String,Integer>)(ay.getCharacteristics().clone())).entrySet().iterator();
//            while (it.hasNext()) {
//                Map.Entry pair = (Map.Entry) it.next();
//                System.out.println(pair.getKey() + " = " + pair.getValue());
//                it.remove(); // avoids a ConcurrentModificationException
//            }
//            for (int i = 0; i < ay.getOrgans().size(); i++) {
//                System.out.println("ORGAN CHARACTERISTICS "+ay.getOrgans().get(i).getOrganName());
//               // it = ay.getOrgans().get(i).getCharacteristics().entrySet().iterator();
//                it =( (HashMap<String,Integer>)(ay.getOrgans().get(i).getCharacteristics().clone())).entrySet().iterator();
//                while (it.hasNext()) {
//                    Map.Entry pair = (Map.Entry) it.next();
//                    System.out.println(pair.getKey() + " = " + pair.getValue());
//                    it.remove(); // avoids a ConcurrentModificationException
//                }
//                System.out.println("ACTION CHARACTERISTICS");
//                for (int j = 0; j < ay.getOrgans().get(i).getActions().size(); j++) {
//                    //it = ay.getOrgans().get(i).getActions().get(j).getCondition().entrySet().iterator();
//                    it =( (HashMap<String,Integer[]>)(ay.getOrgans().get(i).getActions().get(j).getCondition().clone())).entrySet().iterator();
//                    while (it.hasNext()) {
//                        Map.Entry pair = (Map.Entry) it.next();
//                        System.out.println(pair.getKey() + " = " + pair.getValue());
//                        it.remove(); // avoids a ConcurrentModificationException
//                    }
//                }
//                System.out.println("SENSES CHARACTERISTICS");
//                for (int j = 0; j < ay.getOrgans().get(i).getSenses().size(); j++) {
//                    //it = ay.getOrgans().get(i).getSenses().get(j).getCondition().entrySet().iterator();
//                    it =( (HashMap<String,Integer[]>)(ay.getOrgans().get(i).getSenses().get(j).getCondition().clone())).entrySet().iterator();
//                    while (it.hasNext()) {
//                        Map.Entry pair = (Map.Entry) it.next();
//                        System.out.println(pair.getKey() + " = " + pair.getValue());
//                        it.remove(); // avoids a ConcurrentModificationException
//                    }
//                }
//
//            }
            world.setElement(child, xtarget, ytarget);
            ax.setCharacteristic("fat", ax.getCharacteristic("fat") - 5);
            ay.setCharacteristic("fat", ay.getCharacteristic("fat") - 5);
            return 1;
        } catch (Exception ex) {
            Logger.getLogger(Reproduce.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
        //return 0;
    }

    /**
     * Arbitrary evaluation. It's the happyness of the agent to have a child.
     *
     * @param world
     * @param x
     * @param y
     * @param xnext
     * @param ynext
     * @return 150
     */
    @Override
    public int evaluateAction(World world, int x, int y, int xnext, int ynext) {
        // The evaluation is an arbitrary value for now.
        try {
            Element target = ((Agent) world.getElement(x, y)).senseElement(xnext, ynext);
            if (target != null && target.getName().equals("agent")) {
                return 150;
            }
        } catch (Exception ex) {
            Logger.getLogger(Eat.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    /**
     * Kill the child which is on the top of the stack and give the fat back to
     * the parents.
     *
     * @param world
     * @param xprevious
     * @param yprevious
     * @param xnext
     * @param ynext
     * @throws Exception
     */
    @Override
    public void cancelAction(World world, int xprevious, int yprevious, int xnext, int ynext) throws Exception {
        ((Agent) world.getElement(xprevious, yprevious)).setCharacteristic("fat", ((Agent) world.getElement(xprevious, yprevious)).getCharacteristic("fat") + 5);
        ((Agent) world.getElement(xnext, ynext)).setCharacteristic("fat", ((Agent) world.getElement(xnext, ynext)).getCharacteristic("fat") + 5);

        /*System.out.print("cancelReproduce :");
         System.out.print(world.getElement(xprevious, yprevious));
         System.out.print(" = ");
         System.out.print(xnext);
         System.out.println(ynext);*/
        if (childs.empty()) {
            System.err.println("Warning: lifo of child empty!");
            System.out.println("Warning: lifo of child empty!");
        }
        Agent child = childs.pop();
        child.die();
        //world.removeElement(child.getPosx(), child.getPosy());
    }

    /*@Override
     public Action copy() {
     throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
     }*/
}
