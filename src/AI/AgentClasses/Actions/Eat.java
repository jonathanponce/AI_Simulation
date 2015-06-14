/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AI.AgentClasses.Actions;

import AI.AgentClasses.Action;
import AI.AgentClasses.Agent;
import AI.Main;
import AI.Element;
import AI.Food;
import AI.World;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Eat extends Action {
    
    @Override
    public String getName() {
        return "eat";
    }

    @Override
    public int doAction(World world, int x, int y, int xnext, int ynext) {
        try {
            //agent.senseElement(x, y)
            if (world.getElement(xnext, ynext) != null && world.getElement(xnext, ynext).getName().equals("food")) {
                //System.out.print("-food");
                //System.out.print(xnext);
                //System.out.println(ynext);
                world.removeElement(xnext, ynext);
                ((Agent) world.getElement(x, y)).setCharacteristic("fat", ((Agent) world.getElement(x, y)).getCharacteristic("fat") + 10);
                return 1;
            }
    
            return 0;
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    @Override
    public int evaluateAction(World world, int x, int y, int xnext, int ynext) {
        try {
            Element target = ((Agent) world.getElement(x, y)).senseElement(xnext, ynext);
            if (target != null && target.getName().equals("food")) {
                return 100;
            }
        } catch (Exception ex) {
            Logger.getLogger(Eat.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return 0;
        //return 100;
    }

    @Override
    public void cancelAction(World world, int xprevious, int yprevious, int xnext, int ynext) throws Exception{
        /*System.out.print("cancelEat :");
        System.out.print(world.getElement(xprevious, yprevious));
        System.out.print(" = ");
        System.out.print(xnext);
        System.out.println(ynext);*/
        world.setElement(new Food(),xnext, ynext);
        ((Agent) world.getElement(xprevious, yprevious)).setCharacteristic("fat", ((Agent) world.getElement(xprevious, yprevious)).getCharacteristic("fat") - 10);
    }

    /*@Override
    public Action copy() {
        Action newaction= (Action) new Eat();
        newaction.condition= new HashMap<String, Integer[]>(this.getCondition());
        return newaction;
    }*/
    
}