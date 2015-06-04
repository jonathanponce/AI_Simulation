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
public abstract class Element implements Cloneable {
    public abstract String getName();
    public abstract boolean isAgent();
    
    public Element copy() throws CloneNotSupportedException{
        return (Element) this.clone();
    }
}
