/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AI.AgentClasses;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author jonathan
 */
public abstract class  Organ {
    
    private ArrayList<Action> actions;
    private HashMap<String, Integer> characteristics;
    
    public Organ() {
        actions=new ArrayList();
        characteristics= new HashMap<String, Integer>();
    };

    public abstract String getOrganName();

    public ArrayList<Action> getActions() {
        return actions;
    }

    public void addAction(Action a) {
        this.actions.add(a);
    }
    
    protected void addCharacteristic(String name, int value){
        characteristics.put(name, value);
    }
    
    protected int getCharacteristic(String name){
        return characteristics.containsKey(name)? characteristics.get(name) : -1;
    }
    
    protected void setCharacteristic(String name, int value){
        /* it the characteristic already exists, it modify it.
         * else it adds a new one with the value.
        */
        if (characteristics.containsKey(name)) {
            characteristics.replace(name, characteristics.get(name), value);
        } else {
            addCharacteristic(name, value);
        }     
    }

    
    

    
}
