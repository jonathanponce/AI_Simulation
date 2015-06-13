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

public abstract class Sense implements Cloneable{
    private HashMap<String, Integer[]> condition= new HashMap<String, Integer[]>();
    private int elementRange;
    private int variableRange;
    private boolean canSenseFood;
    private boolean canSenseAgent;
    
    /**
     * Use this function to copy this sense.
     * @return
     * @throws CloneNotSupportedException 
     */
    public Sense copy() throws CloneNotSupportedException{
        Sense newsense= (Sense) this.clone();
        newsense.condition= new HashMap<String, Integer[]>(this.getCondition());
        newsense.canSenseAgent=this.canSenseAgent;
        newsense.canSenseFood= this.canSenseFood;
        newsense.elementRange= this.elementRange;
        newsense.variableRange= this.variableRange;
        return newsense;
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
