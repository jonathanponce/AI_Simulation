/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AI.AgentClasses.Actions;

import AI.AgentClasses.Action;
import AI.AgentClasses.Agent;
import AI.AgentClasses.Organ;
import AI.Element;
import AI.World;
/**
 *
 * @author Florent et Haly
 */
public class SharpAttack extends Action{
    
    private int damage;
    
    public SharpAttack(int damage,Organ a) {
        this.damage = damage;
        organ=a;
        organ.addAction(this);
    }
    
    @Override
    public String getName() {
        return "sharpAttack";
    }

    @Override
    public int doAction(World world, int x, int y, int xnext, int ynext) {
        // TODO: add defense mechanisms ex. if theres a shell, no points are taken from the attacked agent
        try {
            Element attacked = world.getElement(xnext, ynext);
            if (attacked == null) {
                return 0;
            }
            if (x != xnext || y != ynext) {
                int lifePoints = ((Agent) attacked).getCharacteristic("lifePoints");
                ((Agent) attacked).setCharacteristic("lifePoints", lifePoints-damage); // TODO: + defense
            }
            return 1;
        } catch (Exception e) {
            System.out.println(e);
            return 0;
        }
    
    }

    @Override
    public int evaluateAction(World world, int x, int y, int xnext, int ynext) {
        return damage; // TODO : EVALUATE WITH DEFENSE
    }

    @Override
    public void cancelAction(World world, int xprevious, int yprevious, int xnext, int ynext) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Action copy() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
