/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AI.AgentClasses;

import java.util.ArrayList;

/**
 *
 * @author jonathan
 */
public class Organ {
    private String organName;
    private ArrayList<Action> actions;
    public Organ(String n,Action a){
        organName=n;
        actions=new ArrayList();
        actions.add(a);
    }
    public Organ(String n){
        organName=n;
        actions=new ArrayList();
    }

    public String getOrganName() {
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

    
}
