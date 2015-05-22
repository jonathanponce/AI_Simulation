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
public class Action {

    private String name;
    private HashMap<String, Integer[]> condition;
    
    private Main.Actionfunction act;

    public Action(String n, Main.Actionfunction a, HashMap<String, Integer[]> c){
        name=n;
        condition=c;
        act=a;
    }
    
    public Action(String n, Main.Actionfunction a){
        name=n;
        condition= new HashMap<String, Integer[]>();
        act=a;
    }
    
    public String getName() {
        return name;
    }

    public HashMap<String, Integer[]> getCondition() {
        return condition;
    }

    public Main.Actionfunction getAct() {
        return act;
    }

}
