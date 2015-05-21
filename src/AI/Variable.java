/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AI;

import java.util.HashMap;

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
    
    public void setUnfixed(){
        fixed = false;
    }
    
    public int Update(int x, int y, long t){
        if(fixed){
            return value;
        }
        else{
            return update.compute(world, x,y,t);
        }
    }
    
    public int Update(int x, int y) throws Exception{
        if(fixed){
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
        if(fixed){
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
        if(fixed){
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
