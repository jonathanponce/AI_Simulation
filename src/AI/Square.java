/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AI;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
/**
 *
 * @author Vaferdolosa
 */
public class Square {
    private World world;
    private int LTVar[];
    private int LVar[];
    private List<Integer> fixed;
    private HashMap<Integer,Integer> cooldownFixedVar;
    
    private Element element;//TOCHANGE
    
    private int x;
    private int y;
    
    public Square(int x, int y, World world){
        this.x = x;
        this.y = y;
        LTVar = new int[world.getLTVariableNumber()];
        LVar = new int[world.getLVariableNumber()];
        this.world = world;
        element = null;
        
        fixed = new LinkedList();
        cooldownFixedVar = new HashMap<>();
    }
    
    public void setElement(Element object){
        this.element = object; 
    }
    
    public void removeElement(){
        this.element = null; 
    }
    
    public Element getElement(){
        return this.element; 
    }
    
    public void setLTFixed(int index,int delay){
        cooldownFixedVar.put(index,delay);
    }
    
    public void setLTUnfixed(int index){
        if(!cooldownFixedVar.containsKey(index)){
            cooldownFixedVar.remove(index);
        }
    }
    
    public void setLTValue(int index, int value){
        LTVar[index] = value;
    }
    
    public void setLTFixed(int index){
        setLTFixed(index,0);
    }
    
    public boolean isFixed(int index){
        
        if(cooldownFixedVar.containsKey(index)){
            int delay = cooldownFixedVar.get(index);
            if(delay!=0){
                cooldownFixedVar.put(index,delay-1);
                if(delay-1==0){
                    cooldownFixedVar.remove(index);
                    return false;
                }
            }
            return true;
        }
        else{
            return false;
        }
    }
    
    public void setLTVariable(int index, int variable){
        if(!isFixed(index)){
            LTVar[index] = variable;
        }
    }
    
    public void setLVariable(int index, int variable){
        LVar[index] = variable;
    }
    
    
    public int getLTVariable(int index){
        return LTVar[index];
    }
    
    public int getLVariable(int index){
        return LVar[index];
    }
    
    public int getVariable(String varName) throws Exception{
        Variable variable = world.getVariable(varName);
        if(variable==null){
            if(world.isTimeless(varName)){
                return LVar[world.getVariableIndex(varName)];
            }
            else{
                return LTVar[world.getVariableIndex(varName)];
            }
        }
        return variable.Update(x, y, world.getTime());
    }
}
