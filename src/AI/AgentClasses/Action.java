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

/**
 *
 * @author jonathan
 */
public abstract class Action {

    private HashMap<String, Integer[]> condition= new HashMap<String, Integer[]>();
    protected Agent agent;
    
    public HashMap<String, Integer[]> getCondition() {
        return condition;
    }
    
    public void addCondition(String s, Integer[] i){
        condition.put(s, i);
    }
    
    public void setAgent(Agent agent){
        this.agent = agent;
    }
    
    public abstract String getName();
    public abstract int doAction(World world, int x,int y, int xnext, int ynext);
    public abstract void cancelAction(World world, int xprevious, int yprevious, int xnext, int ynext) throws Exception;
    public abstract int evaluateAction(World world, int x,int y, int xnext, int ynext) throws Exception;
    
}
