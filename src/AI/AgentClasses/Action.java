/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AI.AgentClasses;

import AI.Main;
import AI.World;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class Action implements Cloneable{

    public HashMap<String, Integer[]> condition= new HashMap<String, Integer[]>();
    protected Agent agent;
    
    public Action copy() throws CloneNotSupportedException{
        Action newaction= (Action) this.clone();
        newaction.condition= new HashMap<String, Integer[]>(this.getCondition());
        return newaction;
    }
    public Action combine(Organ par) throws CloneNotSupportedException {
        Action newaction = (Action) this.clone();
        newaction.condition = new HashMap<String, Integer[]>(this.getCondition());
        Iterator it = newaction.condition.entrySet().iterator();
        if (par == null) {
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                Integer[] t = (Integer[]) pair.getValue();
                Integer[] tempValue = new Integer[t.length];
                for (int i = 0; i < tempValue.length; i++) {
                    tempValue[i] = (int) (newaction.condition.get(pair.getKey())[i] * (0.95 + 0.1 * Math.random()));//a little mutation
                }
                newaction.condition.put(pair.getKey().toString(), tempValue);
                it.remove(); // avoids a ConcurrentModificationException
            }
            return newaction;
        }
        Action other = null;
        for (Action a : par.getActions()) {
            if (a.getName().equals(this.getName())) {
                other = a;
            }
        }
        if(other==null){
            System.out.println("Error?");//just in case
        }
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            Integer[] t = (Integer[]) pair.getValue();
            Integer[] tempValue = new Integer[t.length];
            for (int i = 0; i < tempValue.length; i++) {
                tempValue[i] = (int) Math.round(triangular((newaction.condition.get(pair.getKey())[i]-1), other.condition.get(pair.getKey())[i]+1,  ((newaction.condition.get(pair.getKey())[i] + other.condition.get(pair.getKey())[i]) / 2)));
            }
            newaction.condition.put(pair.getKey().toString(), tempValue);
            it.remove(); // avoids a ConcurrentModificationException
        }
        return newaction;
    }

    public double triangular(double a, double b, double c) {
        double U = Math.random() / (double) 1.0;
        double F = (c - a) / (b - a);
        if (U <= F) {
            return a + Math.sqrt(U * (b - a) * (c - a));
        } else {
            return b - Math.sqrt((1 - U) * (b - a) * (b - c));
        }
    }
    public HashMap<String, Integer[]> getCondition() {
        return condition;
    }
    
    public void addCondition(String s, Integer[] i){
        condition.put(s, i);
    }
    
    public void setAgent(Agent agent){
        this.agent = agent;
    }
    
    public boolean isActionPossible(World world, int x,int y, int xnext, int ynext) throws Exception{
        return true;
    }
    
    //public abstract Action copy();
    public abstract String getName();
    /**
     * Execute the implemented action.
     * @param world
     * @param x
     * @param y
     * @param xnext
     * @param ynext
     * @return 
     */
    public abstract int doAction(World world, int x,int y, int xnext, int ynext);
    /**
     * Undo the action. It's the reverse of the function doAction().
     * @param world
     * @param xprevious
     * @param yprevious
     * @param xnext
     * @param ynext
     * @throws Exception 
     */
    public abstract void cancelAction(World world, int xprevious, int yprevious, int xnext, int ynext) throws Exception;
    /**
     * Evaluate the value of an action.
     * The value of an action is what it brings to the agent (fatness, happyness,knowledge,...).
     * @param world
     * @param x
     * @param y
     * @param xnext
     * @param ynext
     * @return
     * @throws Exception 
     */
    public abstract int evaluateAction(World world, int x,int y, int xnext, int ynext) throws Exception;
    
}
