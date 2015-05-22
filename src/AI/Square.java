/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AI;


/**
 *
 * @author Vaferdolosa
 */
public class Square {
    private World world;
    private int LTVar[];
    private int LVar[];
    
    private Element element;//TOCHANGE
    private int x;
    private int y;
    
    public Square(int x, int y, World world){
        this.x = x;
        this.y = y;
        LTVar = new int[world.getLTVariableNumber()];
        LVar = new int[world.getLVariableNumber()];
        this.world = world;
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
    
    public void setLTVariable(int index, int variable){
        LTVar[index] = variable;
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
