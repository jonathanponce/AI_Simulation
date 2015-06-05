/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AI;

import AI.AgentClasses.*;
import AI.AgentClasses.Actions.*;

import java.lang.*;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Vaferdolosa
 */
public class Main {
    
    @FunctionalInterface
    public static interface LTfunction{
        int compute(World world, int x,int y,long t);
    } 
    
    @FunctionalInterface
    public static interface Lfunction{
        int compute(World world,int x,int y);
    }
    
    @FunctionalInterface
    public static interface Tfunction{
        int compute(World world,long t);
    }
    
    @FunctionalInterface
    public static interface Cfunction{
        int compute(World world);
    }
    
    @FunctionalInterface
    public static interface Actionfunction{
        int compute(World world, int xagent,int yagent, int xobject, int yobject);
    }
    @FunctionalInterface
    public static interface Evaluatefunction{
        int compute(World world, int xagent,int yagent, int xobject, int yobject);
    }

    
    Main() throws Exception{
        //CODE HERE
        World w = new World(10,10);
        
        w.addVariable("Friction", 1);
        w.addVariable("slope", (World world,int x,int y)->{return x+y;});
        w.addVariable("temperature",(World world,long t)->{return (int)(20+10*(Math.sin(t)));});
        w.addVariable("light",(World world,int x,int y,long t)->{return (int)(((x+y>5+5*Math.cos(t))? 1 : 0)*(50-50*(Math.cos(t))));});
        
        w.addRule((World world)->{System.out.println("start world!");return 0;});
        w.addRule((World world,long t)->{System.out.println("next turn!"); if(world.getTime()==20){world.Stop();System.out.println("end turn!");}return 0;});
        //w.addRule((World world,int x,int y)->{if(x%2==0 && y%2==0){System.out.println(x); world.setElement(new Agent(w,x,y), x, y);}return 0;});//need to detive Element// need to check agents coordinates
        //w.addRule((World world,int x,int y, long t)->{world.removeElement((int)t%10, (int)t/10);return 0;});
        
        w.buildWorld();  

        Walk walk= new Walk();
        Integer temp[] = {2};
        walk.addCondition("distance", temp);
        Integer temp2[] = {0};
        walk.addCondition("food", temp2);
        walk.addCondition("agent", temp2);
        
        Organ foot= new Organ("foot", walk);
        foot.addCharacteristic("size", 2);
        
        Walk walk1= new Walk();
        walk1.addCondition("distance", temp);
        walk1.addCondition("food", temp2);
        walk1.addCondition("agent", temp2);
        
        Organ foot1= new Organ("foot", walk1);
        foot1.addCharacteristic("size", 2);
        
        Eat eat= new Eat();
        Integer temp3[]={1};
        eat.addCondition("food", temp3);
        eat.addCondition("distance", temp3);
        Organ mouth= new Organ("mouth", eat);
        
        Eat eat1= new Eat();
        eat1.addCondition("food", temp3);
        eat1.addCondition("distance", temp3);
        Organ mouth1= new Organ("mouth", eat1);
       
        Agent.addCharacteristic("fat");
        Agent.addCharacteristic("lifePoints");
        
        //w.setElement(new Agent(w,3,3), 3, 3);
        Agent agent1=new Agent(w,1,1);
        //System.out.println(agent1.isAgent());
        agent1.setCharacteristic("fat", 5);
        agent1.setCharacteristic("lifePoints", 10);
        agent1.addOrgan(foot1);
        agent1.addOrgan(mouth1);
        w.setElement(agent1, 1, 1);
        
        Agent agent2=new Agent(w,2,2);
        agent2.setCharacteristic("fat", 20);
        agent2.setCharacteristic("lifePoints", 10);
        agent2.addOrgan(foot);
        agent2.addOrgan(mouth);
        w.setElement(agent2, 2, 2);
        
        w.setElement(new Food(), 4, 4);
        w.setElement(new Food(), 4, 5);
        w.setElement(new Food(), 4, 6);
        w.setElement(new Food(), 9, 9);
              
        w.run();        
        
    }
}
