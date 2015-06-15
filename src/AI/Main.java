/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AI;

import AI.AgentClasses.*;
import AI.AgentClasses.Actions.*;
import AI.AgentClasses.Senses.See;
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
    /*
    @FunctionalInterface
    public static interface Actionfunction{
        int compute(World world, int xagent,int yagent, int xobject, int yobject);
    }
    @FunctionalInterface
    public static interface Evaluatefunction{
        int compute(World world, int xagent,int yagent, int xobject, int yobject);
    }
    */
    
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
        /*w.addRule((World world, int x, int y, long t) -> {

            Element e;
            try {
                e = world.getElement(x, y);
            } catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                return 0;
            }

            if (e == null) {

                int mapSize = world.getSize()[0] * world.getSize()[1];

                //System.out.println(5.0/mapSize);
                if (world.getRandom() < 5.0/mapSize){
                    world.setElement(new Food(), x, y);
                }
            }

            return 0;
        });*/

        w.buildWorld();
        Integer[] zero={0};
        Integer[] one={1};
        Integer[] two={2};
       
        Agent.addCharacteristic("fat");
        Agent.addCharacteristic("lifePoints");
        
        for (int numAgent=0; numAgent<2; numAgent++){
            float r = w.getRandom();
            int x = (int)(w.getSize()[0]*r);
            
            Agent nouvAgent=new Agent(w,4*numAgent,4*numAgent);
            nouvAgent.setCharacteristic("fat", 10000);
            nouvAgent.setCharacteristic("lifePoints", 10);
            
            
            for (int i = 0; i < 100; i++) {
               // System.out.println("traingular "+walk.triangular(4, 6, 5));
            }
            
            Organ foot= new Organ("foot");
            foot.addCharacteristic("size",2);
            Walk walk= new Walk(foot);
            //foot.addAction(walk); no need!
            
            
            
            Organ mouth = new Organ("mouth");
            Eat eat= new Eat(mouth);
            //mouth.addAction(eat);
            
            Organ sexe = new Organ("sexe");
            Reproduce reproduce = new Reproduce(sexe);
            //sexe.addAction(reproduce);
            
            Organ eyes = new Organ("eye");
            eyes.addCharacteristic("range", 6);
            See see = new See(eyes);
           // eyes.addSens(see);
             
            
            nouvAgent.addOrgan(sexe);
            nouvAgent.addOrgan(mouth);
            nouvAgent.addOrgan(foot);
            nouvAgent.addOrgan(eyes);
            w.setElement(nouvAgent,4*numAgent,4*numAgent);
            
        }
        
        //w.setElement(new Food(), 1, 1);
        //w.setElement(new Food(), 3, 3);
        
        /*w.setElement(new Obstacle(), 3, 3);
        w.setElement(new Obstacle(), 4, 3);
        w.setElement(new Obstacle(), 5, 3);
        w.setElement(new Obstacle(), 3, 4);
        w.setElement(new Obstacle(), 3, 5);
        //w.setElement(new Obstacle(), 4, 4);
        
        w.setElement(new Obstacle(), 5, 4);
        w.setElement(new Obstacle(), 5, 5);
        w.setElement(new Obstacle(), 3, 6);
        w.setElement(new Obstacle(), 5, 6);
        w.setElement(new Obstacle(), 3, 7);
        w.setElement(new Obstacle(), 4, 7);
       w.setElement(new Obstacle(), 3, 8);
        w.setElement(new Obstacle(), 5, 8);
        w.setElement(new Obstacle(), 3, 9);
        w.setElement(new Obstacle(), 5, 9);
        w.setElement(new Obstacle(), 3, 0);
        w.setElement(new Obstacle(), 5, 0);   */           
        w.run();  
        /*
        
        
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
                if(world.getRandom()<1){//5.0/mapSize){
                    world.setElement(new Food(), x, y);
                }
            }
            
            return 0;});
        
        w.buildWorld();
        
        Integer[] zero={0};
        Integer[] one={1};
        Integer[] two={2};
       
        Agent.addCharacteristic("lifePoints");
        Agent.addCharacteristic("fat");
        
        for (int numAgent=0; numAgent<5; numAgent++){
            float r = w.getRandom();
            int x = (int)(w.getSize()[0]*r);
            
            Agent nouvAgent=new Agent(w,x,numAgent);
            //System.out.println(agent1.isAgent());
            nouvAgent.setCharacteristic("fat", 10);
            nouvAgent.setCharacteristic("lifePoints", 10);
            
            Walk walk= new Walk();
            walk.addCondition("distance", two);
            walk.addCondition("*", zero);
            //walk.addCondition("agent", zero);
            Organ foot= new Organ("foot", walk);
            foot.addCharacteristic("size", 2);
            
            
            Eat eat= new Eat();
            eat.addCondition("food", one);
            eat.addCondition("distance", one);
            Organ mouth = new Organ("mouth", eat);

            Reproduce reproduce = new Reproduce();
            reproduce.addCondition("distance", one);
            reproduce.addCondition("agent", one);

            Organ sexe = new Organ("sexe", reproduce);

            See see = new See(6);
            Organ eyes = new Organ("eye", see);
            
            nouvAgent.addOrgan(sexe);
            nouvAgent.addOrgan(foot);
            nouvAgent.addOrgan(mouth);
            nouvAgent.addOrgan(eyes);
            w.setElement(nouvAgent, x, numAgent);
            
        }
        
        //w.setElement(new Food(), 4, 4);
        //w.setElement(new Food(), 4, 5);
        //w.setElement(new Food(), 4, 6);
        //w.setElement(new Food(), 9, 9);
              
        w.run();        
        */
    }
}
