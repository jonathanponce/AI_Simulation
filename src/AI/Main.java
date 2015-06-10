/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AI;

import AI.AgentClasses.*;
import AI.AgentClasses.Actions.*;
import AI.AgentClasses.Senses.See;

import java.lang.*;
import java.util.HashMap;
import java.util.Random;
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
        //w.addVariable("light",(World world,int x,int y,long t)->{return (int)(((x+y>5+5*Math.cos(t/6))? 1 : 0)*(50-50*(Math.cos(t/6))));});
        w.addVariable("light",(World world,int x,int y,long t)->{return 80;});
        w.addRule((World world)->{System.out.println("start world!");return 0;});
        w.addRule((World world,long t)->{System.out.println("next turn!"); if(world.getTime()==100){world.Stop();System.out.println("end turn!");}return 0;});
        //w.addRule((World world,int x,int y)->{if(x%2==0 && y%2==0){System.out.println(x); world.setElement(new Agent(w,x,y), x, y);}return 0;});//need to detive Element// need to check agents coordinates
        //w.addRule((World world,int x,int y, long t)->{world.removeElement((int)t%10, (int)t/10);return 0;});
        w.addRule((World world,int x,int y, long t)->{
            
            Element e;
            try {
                e = world.getElement(x, y);
            } catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                return 0;
            }
            
            if(e==null){
                
                int mapSize = world.getSize()[0]*world.getSize()[1];

                //System.out.println(5.0/mapSize);
                if(world.getRandom()<5.0/mapSize){
                    world.setElement(new Food(), x, y);
                }
            }
            
            return 0;});
        
        w.buildWorld();
        
        Integer[] zero={0};
        Integer[] un={1};
        Integer[] deux={2};
        
        /*Walk walk= new Walk();
        walk.addCondition("distance", deux);
        walk.addCondition("food", zero);
        walk.addCondition("agent", zero);
        
        Organ foot= new Organ("foot", walk);
        foot.addCharacteristic("size", 2);
        
        Walk walk1= new Walk();
        walk1.addCondition("distance", deux);
        walk1.addCondition("food", zero);
        walk1.addCondition("agent", zero);
        
        Organ foot1= new Organ("foot", walk1);
        foot1.addCharacteristic("size", 2);
        
        Eat eat= new Eat();
        
        eat.addCondition("food", un);
        eat.addCondition("distance", un);
        Organ mouth= new Organ("mouth", eat);
        
        Eat eat1= new Eat();
        eat1.addCondition("food", un);
        eat1.addCondition("distance", un);
        Organ mouth1= new Organ("mouth", eat1);
        
        Reproduce reproduce= new Reproduce();
        reproduce.addCondition("distance", un);
        reproduce.addCondition("agent", un);
        
        Organ sexe= new Organ("sexe", reproduce);*/
       
        Agent.addCharacteristic("fat");
        Agent.addCharacteristic("lifePoints");
        
        for (int numAgent=0; numAgent<2; numAgent++){
            float r = w.getRandom();
            int x = (int)(w.getSize()[0]*r);
            
            Agent nouvAgent=new Agent(w,x,numAgent);
            //System.out.println(agent1.isAgent());
            nouvAgent.setCharacteristic("fat", 10);
            nouvAgent.setCharacteristic("lifePoints", 10);
            
            Walk walk= new Walk();
            walk.addCondition("distance", deux);
            walk.addCondition("food", zero);
            walk.addCondition("agent", zero);
            Organ foot= new Organ("foot", walk);
            foot.addCharacteristic("size", 2);
            
            Eat eat= new Eat();
            eat.addCondition("food", un);
            eat.addCondition("distance", un);
            Organ mouth = new Organ("mouth", eat);

            Reproduce reproduce = new Reproduce();
            reproduce.addCondition("distance", un);
            reproduce.addCondition("agent", un);

            Organ sexe = new Organ("sexe", reproduce);
            //nouvAgent.addOrgan(sexe);
            
            See see = new See(6);
            Organ eyes = new Organ("eye", see);
            
            nouvAgent.addOrgan(foot);
            nouvAgent.addOrgan(mouth);
            nouvAgent.addOrgan(eyes);
            w.setElement(nouvAgent, x, numAgent);
            
        }
        
        //w.setElement(new Agent(w,3,3), 3, 3);
        /*Agent agent1=new Agent(w,1,1);
        //System.out.println(agent1.isAgent());
        agent1.setCharacteristic("fat", 5);
        agent1.setCharacteristic("lifePoints", 10);
        agent1.addOrgan(foot1);
        agent1.addOrgan(mouth1);
        w.setElement(agent1, 1, 1);*/
        
        /*Agent agent2=new Agent(w,2,2);
        agent2.setCharacteristic("fat", 20);
        agent2.setCharacteristic("lifePoints", 10);
        agent2.addOrgan(foot);
        agent2.addOrgan(mouth);
        w.setElement(agent2, 2, 2);*/
        
        //w.setElement(new Food(), 4, 4);
        //w.setElement(new Food(), 4, 5);
        //w.setElement(new Food(), 4, 6);
        //w.setElement(new Food(), 9, 9);
              
        w.run();        
        
    }
}
