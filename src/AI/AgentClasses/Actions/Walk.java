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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Walk extends Action {

    @Override
    public String getName() {
        return "walk";
    }

    /**
     * Move the agent if the start position is different to the target position.
     * @param world
     * @param x
     * @param y
     * @param xnext
     * @param ynext
     * @return 
     */
    @Override
    public int doAction(World world, int x, int y, int xnext, int ynext) {
        try {
            Element thisElement = world.getElement(x, y);
            if (thisElement == null) {
                System.err.println("Warning: Element doesn't exist!");
                System.out.println("Warning: Element doesn't exist!");
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

    /**
     * Walking brings nothing and cost nothing to the agent.
     * So it returns 0.
     * It could be interesting to improve this evaluation function so that the agent has interest in exploring the world 
     * and put small cost for each square he walks so that or agent will be as lazy as Florent :).
     * @param world
     * @param x
     * @param y
     * @param xnext
     * @param ynext
     * @return 0
     */
    @Override
    public int evaluateAction(World world, int x, int y, int xnext, int ynext) {
        int res = 0;
        /*int xresult, yresult;
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
        }*/
        return res;
    }

    /**
     * Move the agent back to the initial position.
     * @param world
     * @param xprevious
     * @param yprevious
     * @param xnext
     * @param ynext
     * @throws Exception 
     */
    @Override
    public void cancelAction(World world, int xprevious, int yprevious, int xnext, int ynext) throws Exception {
        
        Element thisElement = world.getElement(xnext, ynext);
        /*System.out.print("cancelWalk :");
        System.out.print(thisElement);
        System.out.print(" = ");
        System.out.print(xnext);
        System.out.println(ynext);*/
        if (thisElement==null){
            System.out.print(xprevious);
            System.out.println(yprevious);
            Thread.sleep(1000);
        }
        if (xprevious != xnext || yprevious != ynext) {
            /*System.out.print("(xpre, ypre, xnext, ynext)=");
            System.out.print(xprevious);
            System.out.print(yprevious);
            System.out.print(xnext);
            System.out.println(ynext);*/
            ((Agent) thisElement).moveTo(xprevious, yprevious);
            world.setElement(thisElement, xprevious, yprevious);
            world.removeElement(xnext, ynext);
        }
    }
    
    /**
     * This function verifiy if there is a path from (x,y) to (xnext,ynext).
     * It also verify if the length of this path is smaller than the distance an agent can walk in 1 turn.
     * An agent can walk only on empty squares: cannot walk on other agent or food...
     * @param world
     * @param x
     * @param y
     * @param xnext
     * @param ynext
     * @return true or false if it find a correct path or not.
     */
    @Override
    public boolean isActionPossible(World world, int x, int y, int xnext, int ynext) {
        boolean val = AstarSearch(new aStarNode(x, y), world, xnext, ynext);
        System.out.println("val "+val+" x "+x+" y "+y+" xn "+xnext+" yn "+ynext);
        return val;
        //return true;
    }

    public boolean AstarSearch(aStarNode source, World w, int xg, int yg) {
        Set<aStarNode> explored = new HashSet<aStarNode>();
        PriorityQueue<aStarNode> queue = new PriorityQueue<aStarNode>(11, new Comparator<aStarNode>() {
            //override compare method
            public int compare(aStarNode i, aStarNode j) {
                if (i.fValue > j.fValue) {
                    return 1;
                } else if (i.fValue < j.fValue) {
                    return -1;
                } else {
                    return 0;
                }
            }
        }
        );
        //cost from start
        source.gValue = 0;
        queue.add(source);
        boolean found = false;
        double cost, tempG, tempF, h;
        ArrayList<aStarNode> childs;
        while ((!queue.isEmpty()) && (!found)) {
            aStarNode current = queue.poll();
            if (current.fValue > this.getCondition().get("distance")[0]) {//need to know the limit of walking
                return false;
            }
            explored.add(current);
            childs = getChild(w, current.x, current.y);
            if (current.isGoal(w, xg, yg)) {//found solution
                found = true;
                /*System.out.println("PATH");
                 while (current.x != source.x && current.y != source.y) {
                            
                            
                 System.out.println("x "+current.x+" y "+current.y);
                 current = current.parent;
                 }
                 System.out.println("END PATH");
                 */
                return true;
            }
            //check every child of current node
            for (int i = 0; i < childs.size(); i++) {
                aStarNode child = childs.get(i);

                tempG = current.gValue + 1;

                if ((explored.contains(child))) {
                    continue;
                }
                tempF = tempG + Math.abs(xg - child.x) + Math.abs(yg - child.y);//there should be a method in world returning manhattan distance
                if(tempF>tempG+ Math.abs(xg+w.getSize()[0] - child.x) + Math.abs(yg - child.y)){
                    tempF = tempG + Math.abs(xg+w.getSize()[0] - child.x) + Math.abs(yg - child.y);
                }
                if(tempF>tempG+ Math.abs(xg-w.getSize()[0] - child.x) + Math.abs(yg - child.y)){
                    tempF = tempG + Math.abs(xg-w.getSize()[0] - child.x) + Math.abs(yg - child.y);
                }
                if(tempF>tempG+ Math.abs(xg - child.x) + Math.abs(yg-w.getSize()[1] - child.y)){
                    tempF = tempG + Math.abs(xg - child.x) + Math.abs(yg-w.getSize()[1] - child.y);
                }
                if(tempF>tempG+ Math.abs(xg - child.x) + Math.abs(yg+w.getSize()[1] - child.y)){
                    tempF = tempG + Math.abs(xg - child.x) + Math.abs(yg+w.getSize()[1] - child.y);
                }
                
                
                
                if(tempF>tempG+ Math.abs(xg+w.getSize()[0] - child.x) + Math.abs(yg+w.getSize()[1] - child.y)){
                    tempF = tempG + Math.abs(xg+w.getSize()[0] - child.x) + Math.abs(yg+w.getSize()[1] - child.y);
                }
                if(tempF>tempG+ Math.abs(xg-w.getSize()[0] - child.x) + Math.abs(yg+w.getSize()[1] - child.y)){
                    tempF = tempG + Math.abs(xg-w.getSize()[0] - child.x) + Math.abs(yg +w.getSize()[1]- child.y);
                }
                if(tempF>tempG+ Math.abs(xg +w.getSize()[0]- child.x) + Math.abs(yg-w.getSize()[1] - child.y)){
                    tempF = tempG + Math.abs(xg+w.getSize()[0] - child.x) + Math.abs(yg-w.getSize()[1] - child.y);
                }
                if(tempF>tempG+ Math.abs(xg-w.getSize()[0] - child.x) + Math.abs(yg-w.getSize()[1] - child.y)){
                    tempF = tempG + Math.abs(xg -w.getSize()[0] - child.x) + Math.abs(yg-w.getSize()[1] - child.y);
                }
                if ((!queue.contains(child)) || (tempF < child.fValue)) {// if child node is not in queue or newer fValue is lower
                    child.parent = current;
                    child.gValue = tempG;
                    child.fValue = tempF;
                    if (!queue.contains(child)) {
                        queue.add(child);
                    }
                }
            }
        }
        return false;
    }

    public ArrayList<aStarNode> getChild(World w, int x, int y) {
        ArrayList<aStarNode> child = new ArrayList<aStarNode>();
        try {
            if (w.getElement(x - 1, y) == null) {//world should take into consideration the limits
                child.add(new aStarNode(x - 1, y));
            }
            if (w.getElement(x, y - 1) == null) {
                child.add(new aStarNode(x, y - 1));
            }
            if (w.getElement(x + 1, y) == null) {
                child.add(new aStarNode(x + 1, y));
            }
            if (w.getElement(x, y + 1) == null) {
                child.add(new aStarNode(x, y + 1));
            }
        } catch (Exception ex) {
            Logger.getLogger(Walk.class.getName()).log(Level.SEVERE, null, ex);
        }
        return child;
    }

    class aStarNode {

        public double vertex;
        public double gValue;//cost from start to here
        public double hValue;//heuristic cost from here to goal
        public double fValue = 0;//total cost
        public aStarNode parent;
        public int x, y;

        public aStarNode(int xi, int xy) {
            x = xi;
            y = xy;

        }

        public double getVertex() {
            return vertex;
        }

        public boolean isGoal(World w, int xg, int yg) {
            if (xg == x && yg == y) {
                return true;
            }
            int xt = (x < 0 ? x + w.getSize()[0] : x) % w.getSize()[0];
            int yt = (y < 0 ? y + w.getSize()[1] : y) % w.getSize()[1];
            if (xg == xt && yg == yt) {
                return true;
            }
            return false;
        }

    }

    /*@Override
     public Action copy() {
     Action newaction= (Action) new Walk();
     newaction.condition= new HashMap<String, Integer[]>(this.getCondition());
     return newaction;
     }*/
}
