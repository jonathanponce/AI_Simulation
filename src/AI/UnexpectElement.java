/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AI;

/**
 *
 * @author matthieugallet
 */
public class UnexpectElement extends Element{

    @Override
    public String getName() {
        return "unexpectElement";
    }

    @Override
    public boolean isAgent() {
        return false;
    }
    
}
