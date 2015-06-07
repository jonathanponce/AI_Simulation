/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AI.AgentClasses;

import AI.Element;
import AI.World;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Vaferdolosa
 */
public class Agent extends Element {

    private static ArrayList<String> agentCharacteristics = new ArrayList<String>();
    private HashMap<String, Integer> characteristics;
    private HashMap<Integer, Element> sensedElement;
    private HashMap<Integer, Integer> sensedVariable;
    private ArrayList<Organ> organs;
    private ArrayList<Sense> senses;
    private ArrayList<Action> actions;
    private World world;
    private int bestx, besty;
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

    public void moveTo(int xNew, int yNew) {
        setPosx(xNew);
        setPosy(yNew);
    }

    public Agent(World w, int x, int y) {
        organs = new ArrayList<Organ>();
        senses = new ArrayList<Sense>();
        actions = new ArrayList<Action>();
        characteristics = new HashMap<String, Integer>();
        sensedElement = new HashMap<>();
        sensedVariable = new HashMap<>();      
        for (int i = 0; i < agentCharacteristics.size(); i++) {
            Integer temp[] = {0};
            try {
                this.characteristics.put(agentCharacteristics.get(i), temp[0]);
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
    
    public void die(){
        dead = true;
        world.removeElement(posx, posy);
    }

    @Override
    public String getName() {
        return "agent";
    }
    
    public Element senseElement(int x, int y) {
        if(sensedElement.containsKey(world.getCoordHash(x, y))){
            return sensedElement.get(world.getCoordHash(x, y));
        }
        
        HashMap<Element, Integer> detected = new HashMap<>();//number of sensor detecting an element
        
        int bestOccurrence=0;//select most likely element
        Element likelyElement = null;
        
        for (Sense sensor : senses) {
            int elementRange = sensor.getElementRange();
            int elementRange2 = elementRange*elementRange;
            int distance2 = (int)(Math.pow(x-posx,2)+Math.pow(y-posy,2));
            
            if(distance2<elementRange2){
                Element e = sensor.senseElement(world, posx, posy, x, y);
                if(e != null && e.getName() == "nonElement"){
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
        
        return likelyElement;
    }
    
    public Integer senseVariable(int x, int y, String name) {
        if(sensedVariable.containsKey(world.getCoordHash(x, y))){
            return sensedVariable.get(world.getCoordHash(x, y));
        }
        
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
    
    public void act() throws Exception {
        sensedElement = new HashMap<>();//reset computional memory about map
        sensedVariable = new HashMap<>(); 
       
        Action toDo = chooseAction();
        
        //System.out.println(toDo);
        if (toDo != null) {
            //System.out.print(",");
            //System.out.print(bestx);
            //System.out.print(",");
            //System.out.println(besty);
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

    public static void addCharacteristic(String name) {
        agentCharacteristics.add(name);
    }

    public int getCharacteristic(String name) {
        return characteristics.containsKey(name) ? characteristics.get(name) : -1;
    }

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

    public void addOrgan(Organ o) {
        organs.add(o);
        for(Sense sensor: o.getSenses()){
            senses.add(sensor);
        }
        for(Action action: o.getActions()){
            actions.add(action);
            action.setAgent(this);
        }
    }

    public Action chooseAction() throws Exception {
        //choose what to do

        Action best = null;
        int bestValue = -1000000;
        int xcoord = -1, ycoord = -1;
        for (int organNum = 0; organNum < organs.size(); organNum++) {
            for (int actionNum = 0; actionNum < organs.get(organNum).getActions().size(); actionNum++) {
                Action temp = organs.get(organNum).getActions().get(actionNum);
                int limit = 0;
                if (temp.getCondition().get("distance") != null) {
                    limit = temp.getCondition().get("distance")[0];
                }
                for (int k = -limit; k < limit + 1; k++) {
                    for (int l = -(limit - Math.abs(k)); l < (limit - Math.abs(k)) + 1; l++) {
                        int xresult = (this.posx + k < 0 ? this.posx + k + world.getSize()[0] : this.posx + k) % world.getSize()[0];
                        int yresult = (this.posy + l < 0 ? this.posy + l + world.getSize()[1] : this.posy + l) % world.getSize()[1];
                        int current = this.evaluationFunction(xresult, yresult, temp);
                        int xprevious= this.posx;
                        int yprevious = this.posy;
                        if (current > -1000000) {
                            temp.doAction(world, this.posx, this.posy, xresult, yresult);
                            current = current + this.soloMax(world, this, this.getDepthMax())/2;
                            if (current > bestValue) {
                                bestValue = current;
                                best = temp;
                                xcoord = xresult;
                                ycoord = yresult;
                            }
                            temp.cancelAction(world, xprevious, yprevious, xresult, yresult);
                        }
                    }
                }
            }
        }
        bestx = xcoord;
        besty = ycoord;
        return best;
    }

    public int soloMax(World w, Agent thisAgent, int depthLeft) {
        int bestValue = -1000000;//min value
        if (depthLeft < 1) {
            return 0;
        } else {
            try {
                for (int organNum = 0; organNum < thisAgent.organs.size(); organNum++) {
                    for (int actionNum = 0; actionNum < thisAgent.organs.get(organNum).getActions().size(); actionNum++) {
                        Action temp = thisAgent.organs.get(organNum).getActions().get(actionNum);
                        int limit = 0;
                        if (temp.getCondition().get("distance") != null) {
                            limit = temp.getCondition().get("distance")[0];
                        }
                        for (int k = -limit; k < limit + 1; k++) {
                            for (int l = -(limit - Math.abs(k)); l < (limit - Math.abs(k)) + 1; l++) {
                                //World world = w.copy(); // TODO partial copy
                                //Agent this = (Agent) thisAgent.clone();
                                //world.setElement(this, this.posx, this.posy);
                                int xresult = (this.posx + k < 0 ? this.posx + k + world.getSize()[0] : this.posx + k) % world.getSize()[0];
                                int yresult = (this.posy + l < 0 ? this.posy + l + world.getSize()[1] : this.posy + l) % world.getSize()[1];
                                /*if (world.getElement(xresult, yresult) != null) {
                                    Element target = world.getElement(xresult, yresult).copy();
                                    world.setElement(target, xresult, yresult);
                                }*/
                                int xprevious = this.posx;
                                int yprevious = this.posy;
                                int current = this.evaluationFunction(xresult, yresult, temp);
                                if (current > -1000000) {
                                    temp.doAction(world, this.posx, this.posy, xresult, yresult);
                                    bestValue = Math.max(bestValue, current + this.soloMax(world, this, depthLeft- 1)/2);
                                    temp.cancelAction(world, xprevious, yprevious, xresult, yresult);
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

    public int evaluationFunction(int x, int y, Action a) throws Exception {

        try {
            // We first verify that this action is possible on this square.
            Iterator it = a.getCondition().entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                // if the condition is about a max distance (e.g. movement)
                if (pair.getKey().equals("distance")) {
                    int xdist = Math.min(Math.abs(x - posx), Math.abs(posx + world.getSize()[0] - x));
                    // x-posx<0 ? x-posx+world.getSize()[0] : x-posx;
                    int ydist = Math.min(Math.abs(y - posy), Math.abs(posy + world.getSize()[1] - y));
                    //y-posy<0 ? y-posy+world.getSize()[1] : y-posy;
                    if (((Integer[]) (pair.getValue()))[0] >= xdist + ydist) {
                        continue;
                    } else {
                        return -1000000;
                    }
                }
                // For the conditions on the terrain (e.g. max temperature).
                if (((Integer[]) (pair.getValue())).length == 2) {
                    try {
                        if (world.getVariable((String) pair.getKey(), x, y) >= ((Integer[]) pair.getValue())[0]
                                && world.getVariable((String) pair.getKey(), x, y) <= ((Integer[]) pair.getValue())[1]) {
                            continue;
                        } else {
                            return -1000000;
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(Agent.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                // For the condition about an object on this square (e.g. eat).
                if (((Integer[]) (pair.getValue())).length == 1) {
                    try {
                        if (((Integer[]) pair.getValue())[0] == 1) {
                            if (world.getElement(x, y) != null && world.getElement(x, y).getName().equals(pair.getKey())) {
                                continue;
                            } else {
                                return -1000000;
                            }
                        } else { // we have: ((Integer[])pair.getValue())[0]==0
                            if (world.getElement(x, y) != null && world.getElement(x, y).getName().equals(pair.getKey())) {
                                return -1000000;
                            } else {
                                continue;
                            }
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(Agent.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                //System.out.println(pair.getKey() + " = " + pair.getValue());
                it.remove(); // avoids a ConcurrentModificationException
                return -1000000;
            }

            //Now we evaluate the action.
            return a.evaluateAction(world, posx, posy, x, y);
        } catch (Exception ex) {
            Logger.getLogger(Agent.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }
}
