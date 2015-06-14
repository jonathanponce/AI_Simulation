/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AI.AgentClasses;

import AI.Element;
import AI.NonElement;
import AI.World;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Agent extends Element {

    /**
     * agentCharacteristics is the list of all the characteristics of the agents.
     * All the agents have the same characterisitcs.
     * It's impossible that one agent have different characteristics than another.
     */
    public static ArrayList<String> agentCharacteristics = new ArrayList<String>();
    /**
     * agentOrgans is the list of all the names of the existing organs.
     * We maybe have to modify it to include the range of the characteristics of this organ.
     */
    public static ArrayList<String> agentOrgans = new ArrayList<String>();
    private HashMap<String, Integer> characteristics;
    private HashMap<Integer, Element> sensedElement;
    private HashMap<Integer, Integer> sensedVariable;
    private ArrayList<Organ> organs;
    private ArrayList<Sense> senses;
    private ArrayList<Action> actions;
    private World world;
    private int bestx, besty;
    private HashMap<Agent, Stack<Integer>> evaluationMemory;
    private int posx, posy;
    private boolean dead;

    public int getPosx() {
        return posx;
    }
    private int getDepthMax(){
        return 2;
    }
    public void setPosx(int posx) {
        this.posx = posx;
    }
    public int getPosy() {
        return posy;
    }
    public void setPosy(int posy) {
        this.posy = posy;
    }
    /**
     * Move the agent the a new position.
     * @param xNew is x-coordinate of the new position.
     * @param yNew is y-coordinate of the new position.
     */
    public void moveTo(int xNew, int yNew) {
        setPosx(xNew);
        setPosy(yNew);
    }
    /** Constructor of the agent class:
     * @param w is the world in which the agent evolves
     * @param x is his x-coordinate.
     * @param y is his y-coordinate.
     */
    public Agent(World w, int x, int y) {
        organs = new ArrayList<Organ>();
        senses = new ArrayList<Sense>();
        actions = new ArrayList<Action>();
        characteristics = new HashMap<String, Integer>();
        sensedElement = new HashMap<>();
        sensedVariable = new HashMap<>();      
        for (String charName: agentCharacteristics) {
            Integer temp[] = {0};
            try {
                this.characteristics.put(charName, temp[0]);
            } catch (Exception ex) {
                Logger.getLogger(Agent.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        world = w;
        posx = x;
        posy = y;
        dead = false;
    }

    public boolean isAgent() {
        return true;
    }
    public boolean isDead(){
        return dead;
    }
    /** This function delete the agent and remove it from the world **/
    public void die(){
        dead = true;
        world.removeElement(posx, posy);
    }
    
    @Override
    public String getName() {
        return "agent";
    }
    
    /**
     * Call all the sensor of the agent to test if there is an element in the specified square.
     * @param x is the x-coordinate of the specified square.
     * @param y is the y-coordinate of the specified square.
     * @return the element it detects or a nonElement if it don't detect an element which exists.
     */
    public Element senseElement(int x, int y) {
        /*if(sensedElement.containsKey(world.getCoordHash(x, y))){
            return sensedElement.get(world.getCoordHash(x, y));
        }*/
        
        HashMap<Element, Integer> detected = new HashMap<>();//number of sensor detecting an element
        
        int bestOccurrence=0;//select most likely element
        Element likelyElement = null;
        
        for (Sense sensor : senses) {
            int elementRange = sensor.getElementRange();
            int elementRange2 = elementRange*elementRange;
            int distance2 = (int)(Math.pow(x-posx,2)+Math.pow(y-posy,2));
            
            if(distance2<elementRange2){
                Element e = sensor.senseElement(world, posx, posy, x, y);
                if(e != null && e.getName().equals("nonElement")){
                    e = null;
                }
                
                if(detected.containsKey(e)){
                    int occurrence = detected.get(e)+1;
                    detected.put(e, occurrence);
                    if(occurrence>bestOccurrence){
                        bestOccurrence=occurrence;
                        likelyElement = e;
                    }
                }
                else{
                    detected.put(e, 1);
                    if(1>bestOccurrence){
                        bestOccurrence=1;
                        likelyElement = e;
                    }
                }
                
            }
        }
        sensedElement.put(world.getCoordHash(x, y), likelyElement);
        if (bestOccurrence == 0) {
            if (likelyElement == null) {
                likelyElement = new NonElement();
            }
        }
        return likelyElement;
    }
    
    /**
     * Call all the sensor of the agent to discover the value of a variable in the specified square.
     * @param x is the x-coordinate of the specified square.
     * @param y is the y-coordinate of the specified square.
     * @param name is the name of the variable we are looking for.
     * @return the value it detects.
     */
    public Integer senseVariable(int x, int y, String name) {
        /*if(sensedVariable.containsKey(world.getCoordHash(x, y))){
            return sensedVariable.get(world.getCoordHash(x, y));
        }*/
        
        HashMap<Integer, Integer> detected = new HashMap<>();//number of sensor detecting an element
        
        int bestOccurrence=0;//select most likely element
        Integer likelyVariable = null;
        
        for (Sense sensor : senses) {
            int elementRange = sensor.getElementRange();
            int elementRange2 = elementRange*elementRange;
            int distance2 = (int)(Math.pow(x-posx,2)+Math.pow(y-posy,2));
            
            if(distance2<elementRange2){
                Integer var = sensor.senseVariable(world, posx, posy, x, y, name);
                
                if(detected.containsKey(var)){
                    int occurrence = detected.get(var)+1;
                    detected.put(var, occurrence);
                    if(occurrence>bestOccurrence){
                        bestOccurrence=occurrence;
                        likelyVariable = var;
                    }
                }
                else{
                    detected.put(var, 1);
                    if(1>bestOccurrence){
                        bestOccurrence=1;
                        likelyVariable = var;
                    }
                }
                
            }
        }
        sensedVariable.put(world.getCoordHash(x, y), likelyVariable);
        
        return likelyVariable;
    }
    
    /**
     * The agent will choose an action with his AI and execute it.
     * @throws Exception 
     */
    public void act() throws Exception {
        sensedElement = new HashMap<>();//reset computional memory about map
        sensedVariable = new HashMap<>(); 
       
        Action toDo = chooseActionMaxMax();
        
        //System.out.println(toDo);
        if (toDo != null) {
            toDo.doAction(world, posx, posy, bestx, besty);
        }

        //Consume fat.
        if (characteristics.containsKey("fat")) {
            characteristics.replace("fat", characteristics.get("fat"), characteristics.get("fat") - 1);
            //System.out.println(characteristics.get("fat"));
            if (characteristics.get("fat") < 0) {
                die();
            }
        }
    }

    /**
     * add a characteristic to all the agent.
     * You have to use it before creating any agent.
     * @param name is the name of the new characteristic.
     */
    public static void addCharacteristic(String name) {
        agentCharacteristics.add(name);
    }

    /**
     * get the value of the characteristic.
     * @param name is the name of the characteristic you are looking for.
     * @return the value of the characteristic.
     */
    public int getCharacteristic(String name) {
        return characteristics.containsKey(name) ? characteristics.get(name) : -1;
    }

    /**
     * Set the value of the characteristic.
     * @param name is the name of the characteristic you want to update.
     * @param value is the new value.
     * @throws Exception 
     */
    public void setCharacteristic(String name, int value) throws Exception {
        /* it the characteristic already exists, it modify it.
         * else it throws an exception.
         */
        if (characteristics.containsKey(name)) {
            characteristics.replace(name, characteristics.get(name), value);
        } else {
            throw new Exception("Characteristic " + name + " doesn't exist.");
        }
    }
    
    /**
     * add a new organ to the list of existing organs.
     * @param name the name of the new organ.
     */
    private static void addNewOrgan(String name) {
        agentOrgans.add(name);
    }

    /**
     * add an organ to the agent.
     * @param o is the new organ.
     */
    public void addOrgan(Organ o) {
        if (!agentOrgans.contains(o.getOrganName())){
           Agent.addNewOrgan(o.getOrganName());
        }
        organs.add(o);
        for(Sense sensor: o.getSenses()){
            senses.add(sensor);
        }
        for(Action action: o.getActions()){
            actions.add(action);
            action.setAgent(this);
        }
    }

    
    public HashMap<String, Integer> getCharacteristics() {
        return characteristics;
    }

    public ArrayList<Organ> getOrgans() {
        return organs;
    }
    
    /**
     * Test if an action is possible in a target square.
     * It will verify the conditions and call action.isActionPossible
     * @param x x-coordinate of the target square.
     * @param y y-coordinate of the target square.
     * @param a the action to test.
     * @return true if the given action is possible in the target square.
     * @throws Exception 
     */
    private boolean isPossibleAction(int x, int y, Action a) throws Exception {

        // We first verify that this action is possible on this square.
        Iterator it = a.getCondition().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            // if the condition is about a max distance (e.g. movement)
            if (pair.getKey().equals("distance")) {
                continue;
                /*int xdist = Math.min(Math.abs(x - posx), Math.abs(posx + world.getSize()[0] - x));
                // x-posx<0 ? x-posx+world.getSize()[0] : x-posx;
                int ydist = Math.min(Math.abs(y - posy), Math.abs(posy + world.getSize()[1] - y));
                //y-posy<0 ? y-posy+world.getSize()[1] : y-posy;
                if (((Integer[]) (pair.getValue()))[0] >= xdist + ydist) {
                    continue;
                } else {
                    return false;
                }*/
            }
            // For the conditions on the terrain (e.g. max temperature).
            if (((Integer[]) (pair.getValue())).length == 2) {
                if (world.getVariable((String) pair.getKey(), x, y) >= ((Integer[]) pair.getValue())[0]
                        && world.getVariable((String) pair.getKey(), x, y) <= ((Integer[]) pair.getValue())[1]) {
                    continue;
                } else {
                    return false;
                }
            }

            // For the condition about an object on this square (e.g. eat).
            if (((Integer[]) (pair.getValue())).length == 1) {
                
                if (((Integer[]) pair.getValue())[0] == 1) {
                    if (world.getElement(x, y) != null && (world.getElement(x, y).getName().equals(pair.getKey()) || pair.getKey().equals("*"))) {
                        continue;
                    } else {
                        return false;
                    }
                } else { // we have: ((Integer[])pair.getValue())[0]==0
                    if (world.getElement(x, y) != null && (world.getElement(x, y).getName().equals(pair.getKey())|| pair.getKey().equals("*"))) {
                        return false;
                    } else {
                        continue;
                    }
                }
            }
            
            /*if (((Integer[]) (pair.getValue())).length == 1) {
                Element senseEl= this.senseElement(x, y);
                System.out.print("agent l502: ");
                System.out.print(" (x,y)=");
                System.out.print(x);
                System.out.print(y);
                System.out.print(" annoncé= ");
                System.out.print(senseEl); 
                System.out.print(" realité= ");
                System.out.println(world.getElement(x, y));
                if (((Integer[]) pair.getValue())[0] == 1) {
                    
                    if ( senseEl!= null && !senseEl.getName().equals("nonElement") && (senseEl.getName().equals(pair.getKey()) || pair.getKey().equals("*"))) {
                        continue;
                    } else {
                        return false;
                    }
                } else { // we have: ((Integer[])pair.getValue())[0]==0
                    if (senseEl != null && !senseEl.getName().equals("nonElement") && (senseEl.getName().equals(pair.getKey())|| pair.getKey().equals("*"))) {
                        return false;
                    } else {
                        continue;
                    }
                }
            }*/

            //System.out.println(pair.getKey() + " = " + pair.getValue());
            it.remove(); // avoids a ConcurrentModificationException
            return false;
        }

        return a.isActionPossible(world, posx, posy, x, y);
    }

    /**
     * Evaluate the value of an action.
     * The value of an action is what it brings to the agent (fatness, happyness,...).
     * @param x
     * @param y
     * @param a
     * @return the value of an action.
     * @throws Exception 
     */
    private int evaluationFunction(int x, int y, Action a) throws Exception {
        return a.evaluateAction(world, posx, posy, x, y);
    }
    
    /**
     * It's the AI of the agent.
     * @return the more interesting action for this agent.
     * @throws Exception 
     */
    private Action chooseActionMaxMax() throws Exception {
        //choose what to do

        Action best = null;
        ArrayList<Action> bestActions = new ArrayList<>();
        ArrayList<Integer> bestxs = new ArrayList<>();
        ArrayList<Integer> bestys = new ArrayList<>();
                
        int bestValue = -1000000;
        int xcoord = -1, ycoord = -1;
        for (int organNum = 0; organNum < organs.size(); organNum++) {
            for (int actionNum = 0; actionNum < organs.get(organNum).getActions().size(); actionNum++) {
                Action currentAction = organs.get(organNum).getActions().get(actionNum);
                int limit = 0;
                if (currentAction.getCondition().get("distance") != null) {
                    limit = currentAction.getCondition().get("distance")[0];
                }
                for (int k = -limit; k < limit + 1; k++) {
                    for (int l = -(limit - Math.abs(k)); l < (limit - Math.abs(k)) + 1; l++) {
                        /*System.out.print("first: ");
                        System.out.print(k);
                        System.out.println(l);*/
                        int xtarget = world.toToricCoord(this.posx + k, 0);
                        int ytarget = world.toToricCoord(this.posy + l, 1);
                        
                        if (this.isPossibleAction(xtarget, ytarget, currentAction)) {
                            int current = this.evaluationFunction(xtarget, ytarget, currentAction);
                            System.out.print("first: ");
                            System.out.print(currentAction.getName());
                            System.out.print(k);
                            System.out.print(l);
                            System.out.print("-- pos= ");
                            System.out.print(this.posx);
                            System.out.print(this.posy);
                            System.out.print("-- target= ");
                            System.out.print(xtarget);
                            System.out.println(ytarget);
                            int xprevious = this.posx;
                            int yprevious = this.posy;
                            if (current > -1000000) {
                                currentAction.doAction(world, this.posx, this.posy, xtarget, ytarget);
                                /*System.out.print("newpos= ");
                                System.out.print(this.posx);
                                System.out.println(this.posy);*/
                                current = current + this.soloMax(world, this, this.getDepthMax()) / 2;
                                if (current > bestValue) {
                                    bestActions = new ArrayList<Action>();
                                    bestActions.add(currentAction);
                                    bestValue = current;
                                    best = currentAction;
                                    xcoord = xtarget;
                                    ycoord = ytarget;
                                    bestxs = new ArrayList<Integer>();
                                    bestxs.add(xtarget);
                                    bestys = new ArrayList<Integer>();
                                    bestys.add(ytarget);
                                } else if (current == bestValue) {
                                    bestxs.add(xtarget);
                                    bestys.add(ytarget);
                                    bestActions.add(currentAction);
                                }
                                currentAction.cancelAction(world, xprevious, yprevious, xtarget, ytarget);
                            }
                        }
                    }
                }
            }
        }
        
        if (bestActions.size()==0){
            //In this case no actions are possible.
            return null;
        }
        
        int r = (int)(world.getRandom()*bestActions.size());
        //System.out.print("r= ");
        //System.out.println(r);
        best = bestActions.get(r);
        
        bestx = xcoord;
        besty = ycoord;
        
        bestx = bestxs.get(r);
        besty = bestys.get(r);
        return best;
    }

    /**
     * AI of the agent.
     * It's a recursive function on the depth of exploration of the tree of possible actions.
     * It's used by the chooseAction() function.
     * @param w the world of the agent.
     * @param thisAgent
     * @param depthLeft the current depth.
     * @return the evaluation of this branch.
     */
    private int soloMax(World w, Agent thisAgent, int depthLeft) {
        int bestValue = -1000000;//min value
        if (depthLeft < 1) {
            return 0;
        } else {
            try {
                for (int organNum = 0; organNum < thisAgent.organs.size(); organNum++) {
                    for (int actionNum = 0; actionNum < thisAgent.organs.get(organNum).getActions().size(); actionNum++) {
                        Action currentAction = thisAgent.organs.get(organNum).getActions().get(actionNum);
                        int limit = 0;
                        if (currentAction.getCondition().get("distance") != null) {
                            limit = currentAction.getCondition().get("distance")[0];
                        }
                        for (int k = -limit; k < limit + 1; k++) {
                            for (int l = -(limit - Math.abs(k)); l < (limit - Math.abs(k)) + 1; l++) {
                                int xtarget = world.toToricCoord(this.posx + k, 0);
                                int ytarget = world.toToricCoord(this.posy + l, 1);
                                if (this.isPossibleAction(xtarget, ytarget, currentAction)) {
                                    System.out.print(depthLeft);
                                    System.out.print(" step: ");
                                    System.out.print(currentAction.getName());
                                    System.out.print("---- pos= ");
                                    System.out.print(posx);
                                    System.out.print(posy);
                                    System.out.print("-- target= ");
                                    System.out.print(xtarget);
                                    System.out.println(ytarget);
                                    int xprevious = this.posx;
                                    int yprevious = this.posy;
                                    int current = this.evaluationFunction(xtarget, ytarget, currentAction);
                                    if (current > -1000000) {
                                        
                                        currentAction.doAction(world, this.posx, this.posy, xtarget, ytarget);
                                        bestValue = Math.max(bestValue, current + this.soloMax(world, this, depthLeft - 1) / 2);
                                        /*System.out.print("bestvalue: ");
                                        System.out.println(bestValue);*/
                                        currentAction.cancelAction(world, xprevious, yprevious, xtarget, ytarget);
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                Logger.getLogger(Agent.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return bestValue;
    }
    
    private Action chooseActionMinMax() throws Exception {
        //choose what to do

        Action best = null;
        ArrayList<Action> bestActions = new ArrayList<>();
        ArrayList<Integer> bestxs = new ArrayList<>();
        ArrayList<Integer> bestys = new ArrayList<>();
                
        HashMap<Integer, Agent> seenAgent= new HashMap<Integer, Agent>();
        seenAgent.put(0, this);
        int index=1;
        for (Agent newagent: world.getAgents()){
            if (newagent != this && this.senseElement(newagent.posx, newagent.posy)!=null && this.senseElement(newagent.posx, newagent.posy).isAgent()) {
                seenAgent.put(index, newagent);
                index++;
            }
        }
        
        int bestValue = -1000000;
        for (Organ currentOrgan : organs) {
            for (Action currentAction : currentOrgan.getActions()) {
                int limit = 0;
                if (currentAction.getCondition().get("distance") != null) {
                    limit = currentAction.getCondition().get("distance")[0];
                }
                for (int k = -limit; k < limit + 1; k++) {
                    for (int l = -(limit - Math.abs(k)); l < (limit - Math.abs(k)) + 1; l++) {
                        int xtarget = world.toToricCoord(this.posx + k,0);
                        int ytarget = world.toToricCoord(this.posx + l,1);
                        /*System.out.print("first: ");
                        System.out.print(currentAction);
                        System.out.print("-- pos= ");
                        System.out.print(this.posx);
                        System.out.print(this.posy);
                        System.out.print("-- target= ");
                        System.out.print(xtarget);
                        System.out.println(ytarget);*/
                        if (this.isPossibleAction(xtarget, ytarget, currentAction)) {
                            int current = this.evaluationFunction(xtarget, ytarget, currentAction);
                            int xprevious = this.posx;
                            int yprevious = this.posy;
                            if (current > -1000000) {
                                currentAction.doAction(world, this.posx, this.posy, xtarget, ytarget);
                                this.evaluationMemory=new HashMap<Agent, Stack<Integer>>();
                                int next= seenAgent.size()==1 ? 0 : 1; 
                                HashMap<Agent, Integer> branchresult=this.chooseConcurrent(world, seenAgent, next, this.getDepthMax());
                                int add= branchresult.get(this)==null ? 0 : branchresult.get(this);
                                current = current + add / 2;
                                if (current > bestValue) {
                                    bestActions = new ArrayList<Action>();
                                    bestActions.add(currentAction);
                                    bestValue = current;
                                    bestxs = new ArrayList<Integer>();
                                    bestxs.add(xtarget);
                                    bestys = new ArrayList<Integer>();
                                    bestys.add(ytarget);
                                } else if (current == bestValue) {
                                    bestxs.add(xtarget);
                                    bestys.add(ytarget);
                                    bestActions.add(currentAction);
                                }
                                currentAction.cancelAction(world, xprevious, yprevious, xtarget, ytarget);
                            }
                        }
                    }
                }
            }
        }
        
        if (bestActions.size()==0){
            //In this case no actions are possible.
            return null;
        }
        
        int r = (int)(world.getRandom()*bestActions.size());
        best = bestActions.get(r);
        bestx = bestxs.get(r);
        besty = bestys.get(r);
        return best;
    }
    
    private HashMap<Agent, Integer> chooseConcurrent(World w, HashMap<Integer, Agent> order, int currentagentnum, int depthLeft) throws Exception {
        if (currentagentnum == 0) {
            depthLeft = depthLeft - 1;
        }
        if (depthLeft < 1) {
            return new HashMap<Agent, Integer>();
        } else {
            HashMap<Agent, Integer> res = new HashMap<Agent, Integer>();
            Agent currentAgent = order.get(currentagentnum);
            /*System.out.print("Agent l643, currentagent= ");
            System.out.print(currentAgent);
            System.out.print(" currentagentnum= ");
            System.out.print(currentagentnum);
            System.out.print(" order.size= ");
            System.out.println(order.size());*/
            //res.put(currentAgent, -1000000);
            int bestValue = -1000000;
            if (!currentAgent.isDead()) {
            for (Organ currentOrgan : organs) {
                for (Action currentAction : currentOrgan.getActions()) {
                    int limit = 0;
                    if (currentAction.getCondition().get("distance") != null) {
                        limit = currentAction.getCondition().get("distance")[0];
                    }
                    for (int k = -limit; k < limit + 1; k++) {
                        for (int l = -(limit - Math.abs(k)); l < (limit - Math.abs(k)) + 1; l++) {
                            int xtarget = world.toToricCoord(currentAgent.posx + k, 0);
                            int ytarget = world.toToricCoord(currentAgent.posx + l, 1);
                            /*System.out.print("first: ");
                             System.out.print(currentAction);
                             System.out.print("-- pos= ");
                             System.out.print(currentAgent.posx);
                             System.out.print(currentAgent.posy);
                             System.out.print("-- target= ");
                             System.out.print(xtarget);
                             System.out.println(ytarget);*/
                            if (currentAgent.isPossibleAction(xtarget, ytarget, currentAction)) {
                                int current = currentAgent.evaluationFunction(xtarget, ytarget, currentAction);
                                int xprevious = currentAgent.posx;
                                int yprevious = currentAgent.posy;
                                if (current > -1000000) {
                                    currentAction.doAction(world, currentAgent.posx, currentAgent.posy, xtarget, ytarget);
                                            //res.put(currentAgent, Math.max(res.get(currentAgent), current + this.chooseSolo(world, currentAgent, depthLeft - 1) / 2));
                                            /*System.out.print("bestvalue: ");
                                     System.out.println(bestValue);*/
                                    HashMap<Agent, Integer> branchresult = this.chooseConcurrent(world, order, (currentagentnum + 1) % order.size(), depthLeft);
                                    /*System.out.print("Agent l681: branchresult= ");
                                    System.out.print(branchresult);
                                    System.out.print("current= ");
                                    System.out.println(current);*/
                                    int add= branchresult.get(currentAgent)==null ? 0 : branchresult.get(currentAgent);
                                    current = current + add / 2;
                                    if (current > bestValue) {
                                        
                                        bestValue = current;
                                        res.putAll(branchresult);
                                        res.put(currentAgent, current);
                                    }
                                    currentAction.cancelAction(world, xprevious, yprevious, xtarget, ytarget);
                                }
                            }
                        }
                    }
                }
            }
            }
            /*System.out.print("Agent l698: res= ");
            System.out.println(res);*/
            return res;
        }

    }
    
 
}
