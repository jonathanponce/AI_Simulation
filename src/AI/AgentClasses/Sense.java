/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AI.AgentClasses;

import AI.Element;
import AI.Main;
import AI.World;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class Sense implements Cloneable{
    private HashMap<String, Integer[]> condition= new HashMap<String, Integer[]>();
    private int elementRange;
    private int variableRange;
    private boolean canSenseFood;
    private boolean canSenseAgent;
    public Organ organ;
    
    /**
     * Use this function to copy this sense.
     * @return
     * @throws CloneNotSupportedException 
     */
    public void setOrgan(Organ a){
        organ=a;
       }
    public Sense copy() throws CloneNotSupportedException{
        Sense newsense= (Sense) this.clone();
        newsense.condition= new HashMap<String, Integer[]>(this.getCondition());
        newsense.canSenseAgent=this.canSenseAgent;
        newsense.canSenseFood= this.canSenseFood;
        newsense.elementRange= this.elementRange;
        newsense.variableRange= this.variableRange;
        return newsense;
    }
    public Sense combine(Organ par) throws CloneNotSupportedException {
        Sense newsense= (Sense) this.clone();
        newsense.condition= (HashMap<String, Integer[]>) this.condition.clone();
        Iterator it = newsense.condition.entrySet().iterator();
        if (par == null) {
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                Integer[] t = (Integer[]) pair.getValue();
                Integer[] tempValue = new Integer[t.length];
                for (int i = 0; i < tempValue.length; i++) {
                    tempValue[i] = (int) (newsense.condition.get(pair.getKey())[i] * (0.95 + 0.1 * Math.random()));//a little mutation
                }
                newsense.condition.put(pair.getKey().toString(), tempValue);
                it.remove(); // avoids a ConcurrentModificationException
            }
            newsense.canSenseAgent=this.canSenseAgent;
        newsense.canSenseFood= this.canSenseFood;
        newsense.elementRange= this.elementRange;
        newsense.variableRange= this.variableRange;
            return newsense;
        }
        Sense other = null;
        for (Sense a : par.getSenses()) {
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
                tempValue[i] = (int) Math.round(triangular((newsense.condition.get(pair.getKey())[i]-1), other.condition.get(pair.getKey())[i]+1,  ((newsense.condition.get(pair.getKey())[i] + other.condition.get(pair.getKey())[i]) / 2)));
            }
            newsense.condition.put(pair.getKey().toString(), tempValue);
            it.remove(); // avoids a ConcurrentModificationException
        }
        newsense.canSenseAgent=this.canSenseAgent;
        newsense.canSenseFood= this.canSenseFood;
        newsense.elementRange= this.elementRange;
        newsense.variableRange= this.variableRange;
        return newsense;
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

    public abstract String getName();
    
    public int getElementRange() {
        return this.elementRange;
    };
    
    public int getVariableRange() {
        return this.variableRange;
    };
    
    public Sense(int elementRange, int variableRange) { // I assume each agent can have different range for its sense; but all senses have the same sens definition
        this.elementRange = elementRange;
        this.variableRange = variableRange;
    };
    
    public abstract int senseVariable(World world, int agentX,int agentY, int squareX, int squareY, String name);
    public abstract Element senseElement(World world, int agentX,int agentY, int squareX, int squareY);
}
