/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AI;

/**
 *
 * @author Halyna
 */
public class NonElement extends Element {

    @Override
    public String getName() {
        return "nonElement";
    }

    @Override
    public boolean isAgent() {
        return false;
    }
    
}