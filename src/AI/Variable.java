/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AI;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 *
 * @author Vaferdolosa
 */
public class Variable {
    
    final private String name;
    final World world;
    private Main.LTfunction update;
    private boolean constant = false;
    private boolean global = false;
    private boolean timeless = false;
    private boolean fixed = false;
    private int cooldown=0;
    
    public int value;
    

    public Variable(World world, String Name, Main.LTfunction UpdateFunction){
        name = Name;
        this.world=world;
        update = UpdateFunction;
    }
    
    public Variable(World world, String Name, Main.Lfunction UpdateFunction){
        name = Name;
        this.world=world;
        update = (World w, int x,int y,long t) -> {return UpdateFunction.compute(w,x,y);};
        timeless = true;
    }
    
    public Variable(World world, String Name, Main.Tfunction UpdateFunction){
        name = Name;
        this.world=world;
        update = (World w, int x,int y,long t) -> {return UpdateFunction.compute(w,t);};
        global = true;
    }
    
    public Variable(World world, String Name, int value){
        name = Name;
        this.world=world;
        update = (World w, int x,int y,long t) -> {return value;};
        constant = true;
    }
    
    public boolean isTimeless(){
        return timeless;
    }
    
    public boolean isGlobal(){
        return global;
    }
    
    public boolean isConstant(){
        return constant;
    }
    
    public void setFixed(){
        fixed = true;
    }
    
    public void setFixed(int x, int y, int delay) throws Exception{
        Square s = world.getSquare(x, y);
        s.setLTFixed(world.getVariableIndex(name),delay);
    }
    
    public void setFixed(int x, int y) throws Exception{
        Square s = world.getSquare(x, y);
        s.setLTFixed(world.getVariableIndex(name));
    }
    
    public void setUnfixed(int index, int x, int y){
        Square s = world.getSquare(x, y);
        s.setLTUnfixed(index);
    }
    
    
    public void setFixed(int delay){
        cooldown = delay;
        fixed = true;
    }
    
    public boolean isFixed(){
        
        if(cooldown!=0){
            cooldown--;
            if(cooldown==0){
                fixed = false;
            }
        }
        
        
        return fixed;
    }
    
    public void setUnfixed(){
        cooldown = 0;
        fixed = false;
    }
    
    public int Update(int x, int y, long t){
        if(isFixed()){
            return value;
        }
        else{
            return update.compute(world, x,y,t);
        }
    }
    
    public int Update(int x, int y) throws Exception{
        if(isFixed()){
            return value;
        }
        else{
            if(isTimeless()){
                return update.compute(world, x,y,0);
            }
            else{
                throw new Exception("Variable "+name+" is not timeless");
            }
        }
    }
    
    public int Update(long t) throws Exception{
        if(isFixed()){
            return value;
        }
        else{
            if(isGlobal()){
                return update.compute(world, 0,0,t);
            }
            else{
                throw new Exception("Variable "+name+" is not global");
            }
        }
    }
    
    public int Update() throws Exception{
        if(isFixed()){
            return value;
        }
        else{
            if(isConstant()){
                return update.compute(world, 0,0,0);
            }
            else{
                throw new Exception("Variable "+name+" is not constant");
            }
        }
    }
}
