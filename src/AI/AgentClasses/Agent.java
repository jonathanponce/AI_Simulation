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
        characteristics=new HashMap<String, Integer>() ;
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
            toDo.getAct(world, posx, posy, bestx, besty);
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

    public void addCharacteristic(String name, int value) {
        characteristics.put(name, value);
    }
    
    public int getCharacteristic(String name){
        return characteristics.containsKey(name)? characteristics.get(name) : -1;
    }
    
    public void setCharacteristic(String name, int value){
        /* it the characteristic already exists, it modify it.
         * else it adds a new one with the value.
        */
        if (characteristics.containsKey(name)) {
            characteristics.replace(name, characteristics.get(name), value);
        } else {
            addCharacteristic(name, value);
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
        
        // We first verify that this action is possible on this square.
        Iterator it = a.getCondition().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            // if the condition is about a max distance (e.g. movement)
            if(pair.getKey().equals("Distance")){
                if(((Integer[])(pair.getValue()))[0]>=Math.abs(x-posx)+Math.abs(y-posy)){
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
        
        return a.evaluateAct(world, posx, posy, x, y);
    }
}
