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

    private static ArrayList<String> agentCharacteristics=new ArrayList<String>();;
    private HashMap<String, Integer> characteristics;
    private ArrayList<Organ> organs;
    private World world;
    private int bestx, besty;
    private int posx,posy;
    

    public int getPosx() {
        return posx;
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

    public Agent(World w,int x,int y) {
        organs=new ArrayList<Organ>();
        characteristics=new HashMap<String, Integer>();
        for (int i=0; i<agentCharacteristics.size(); i++){
            Integer temp[]={0};
            try {
                this.characteristics.put(agentCharacteristics.get(i), temp[0]);
            } catch (Exception ex) {
                Logger.getLogger(Agent.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        world = w;
        posx=x;
        posy=y;
    }

    public boolean isAgent() {
        return true;
    }
    public String getName() {
        return "agent";
    }

    public void act() {

        Action toDo= chooseAction();
        if (toDo != null){
            toDo.doAction(world, posx, posy, bestx, besty);
        }
        //Consume fat.
        if (characteristics.containsKey("fat")){
            characteristics.replace("fat", characteristics.get("fat"), characteristics.get("fat")-1);
            //System.out.println(characteristics.get("fat"));
            if (characteristics.get("fat")<0){
                world.removeElement(posx, posy);
            }
        }
    }

    public static void addCharacteristic(String name) {
        agentCharacteristics.add(name);
    }
    
    public int getCharacteristic(String name){
        return characteristics.containsKey(name)? characteristics.get(name) : -1;
    }
    
    public void setCharacteristic(String name, int value) throws Exception{
        /* it the characteristic already exists, it modify it.
         * else it throws an exception.
        */
        if (characteristics.containsKey(name)) {
            characteristics.replace(name, characteristics.get(name), value);
        } else {
            throw new Exception("Characteristic "+name+" doesn't exist.");
        }     
    }

    public void addOrgan(Organ o) {
        organs.add(o);
    }

    public Action chooseAction() {
        //choose what to do buttttt 

        Action best = null;
        int bestValue = -1;
        int xcoord = -1, ycoord = -1;
        for (int i = 0; i < organs.size(); i++) {
            for (int j = 0; j < organs.get(i).getActions().size(); j++) {
                Action temp = organs.get(i).getActions().get(j);
                for (int k = 0; k < world.getSize()[0]; k++) {
                    for (int l = 0; l < world.getSize()[1]; l++) {
                        int tempValue = evaluationFunction(k, l, temp);
                        if (tempValue > bestValue) {
                            bestValue = tempValue;
                            best = temp;
                            xcoord = k;
                            ycoord = l;
                        }
                    }
                }
                //see the world and the advantages every action offers
            }

        }
        bestx = xcoord;
        besty = ycoord;
        return best;
    }

    public int evaluationFunction(int x, int y, Action a) {
        
        try {
            // We first verify that this action is possible on this square.
            Iterator it = a.getCondition().entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                // if the condition is about a max distance (e.g. movement)
                if(pair.getKey().equals("distance")){
                    int xdist = Math.min(Math.abs(x-posx), Math.abs(posx+world.getSize()[0]-x)); 
                           // x-posx<0 ? x-posx+world.getSize()[0] : x-posx;
                    int ydist = Math.min(Math.abs(y-posy), Math.abs(posy+world.getSize()[1]-y));  
                            //y-posy<0 ? y-posy+world.getSize()[1] : y-posy;
                    if(((Integer[])(pair.getValue()))[0]>=xdist+ydist){
                        continue;
                    }else{
                        return -1;
                    }
                }
                // For the conditions on the terrain (e.g. max temperature).
                if (((Integer[])(pair.getValue())).length==2){
                    try {
                        if (world.getVariable((String) pair.getKey(), x, y)>=((Integer[])pair.getValue())[0] &&
                                world.getVariable((String) pair.getKey(), x, y)<=((Integer[])pair.getValue())[1]){
                            continue;
                        } else {
                            return -1;
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(Agent.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
                // For the condition about an object on this square (e.g. eat).
                if (((Integer[])(pair.getValue())).length==1){
                    try {
                        if (((Integer[])pair.getValue())[0]==1) {
                            if (world.getElement(x, y)!=null && world.getElement(x, y).getName().equals(pair.getKey())) {
                                continue;
                            } else {
                                return -1;
                            }
                        } else { // we have: ((Integer[])pair.getValue())[0]==0
                            if (world.getElement(x, y)!=null && world.getElement(x, y).getName().equals(pair.getKey())) {
                                return -1;
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
                return -1;
            }
            
            //Now we evaluate the action.
            
            return a.evaluateAction(world, posx, posy, x, y);
        } catch (Exception ex) {
            Logger.getLogger(Agent.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }
}
