/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AI.AgentClasses;

import java.util.ArrayList;
import java.util.HashMap;

public class Organ {
    /**
     * The name of an organ identify it.
     */
    private String organName;
    private ArrayList<Action> actions;
    private ArrayList<Sense> sensors;
    private HashMap<String, Integer> characteristics;
    
    
    public Organ(String n,Action a){
        organName=n;
        actions=new ArrayList();
        sensors=new ArrayList();
        characteristics= new HashMap<String, Integer>();
        actions.add(a);
    }
    
    public Organ(String n,Sense s){
        organName=n;
        actions=new ArrayList();
        sensors=new ArrayList();
        characteristics= new HashMap<String, Integer>();
        sensors.add(s);
    }
        
    public Organ(String n){
        organName=n;
        actions=new ArrayList();
        sensors=new ArrayList();
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
    public ArrayList<Sense> getSenses() {
        return sensors;
    }
    public void addSens(Sense a) {
        this.sensors.add(a);
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
    
    /**
     * Use this function to copy this organ.
     * One organ belong two only one agent.
     * You have two copy it (with this function) to give it to another agent.
     * This function also copy the actions associated.
     * @return a copy of this organ
     * @throws CloneNotSupportedException 
     */
    public Organ copy() throws CloneNotSupportedException{
        Organ neworgan= new Organ(this.getOrganName());
        neworgan.actions=new ArrayList();
        for(Action a: this.getActions()){
            neworgan.addAction(a.copy());
        }
        neworgan.sensors=new ArrayList();
        for(Sense s: this.getSenses()){
            neworgan.addSens(s.copy());
        }
        neworgan.characteristics= (HashMap<String, Integer>) this.characteristics.clone();
        
        return neworgan;
    }

}
