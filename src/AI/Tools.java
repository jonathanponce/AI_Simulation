/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AI;

import java.util.HashMap;

/**
 *
 * @author matthieugallet
 */
public class Tools {
    
    public static HashMap<String, Integer[]> copyStringIntegerHashmap(HashMap<String, Integer[]> h){
        HashMap<String, Integer[]> newhashmap= new HashMap<String, Integer[]>(h);
        return newhashmap;
    }
}
