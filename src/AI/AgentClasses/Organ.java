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
public class Organ {
    private String organName;
    private ArrayList<Action> actions;
    private HashMap<String, Integer> characteristics;
    public Organ(String n,Action a){
        organName=n;
        actions=new ArrayList();
        characteristics= new HashMap<String, Integer>();
        actions.add(a);
    }
    public Organ(String n){
        organName=n;
        actions=new ArrayList();
        characteristics= new HashMap<String, Integer>();
    };

    public String getOrganName(){
        return organName;
    }
    public void setOrganName(String organName) {
        this.organName = organName;
    }

    public ArrayList<Action> getActions() {
        return actions;
    }

    public void addAction(Action a) {
        this.actions.add(a);
    }
    
    public void addCharacteristic(String name, int value){
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

    
    

    
}
