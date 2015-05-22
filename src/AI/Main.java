/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AI;

import java.lang.*;
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
    
    Main() throws Exception{
        //CODE HERE
        
        World w = new World(10,10);
        
        w.addVariable("Friction", 1);
        w.addVariable("tilt", (World world,int x,int y)->{return x+y;});
        w.addVariable("temperature",(World world,long t)->{return (int)(20+10*(Math.sin(t)));});
        w.addVariable("light",(World world,int x,int y,long t)->{return (int)(((x+y>5+5*Math.cos(t))? 1 : 0)*(50-50*(Math.cos(t))));});
        
        w.addRule((World world)->{System.out.println("start world!");return 0;});
        w.addRule((World world,long t)->{System.out.println("next turn!"); if(world.getTime()==10){world.Stop();System.out.println("end turn!");}return 0;});
        w.addRule((World world,int x,int y)->{if(x%2==0 && y%2==0){world.setElement(new Agent(), x, y);}return 0;});//need to detive Element
        w.addRule((World world,int x,int y, long t)->{world.removeElement((int)t%10, (int)t/10);return 0;});
        
        
        w.buildWorld();        
        w.run();        
        
    }
}
