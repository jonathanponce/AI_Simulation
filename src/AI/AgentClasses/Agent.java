/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AI.AgentClasses;

import AI.Element;
import AI.World;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author Vaferdolosa
 */
public class Agent extends Element {

    private HashMap<String, Integer> characteristics;
    private ArrayList<Organ> organs;
    private World world;
    private int bestx, besty;
    private int posx,posy;

    public Agent(World w,int x,int y) {
        organs=new ArrayList<Organ>();
        characteristics=new HashMap<String, Integer>() ;
        world = w;
        posx=x;
        posy=y;
    }

    public boolean isAgent() {
        return true;
    }

    public void act() {

        chooseAction();
        //modify the world accordingly
    }

    private void addCharacteristic(String name, int value) {

        characteristics.put(name, value);

    }

    public void addOrgan(Organ o) {

        organs.add(o);
    }

    public Action chooseAction() {
        //choose what to do buttttt 

        Action best = null;
        int bestValue = -1;
        int xcoord = -1, ycoord = -1;
        for (int i = 0; i < organs.size(); i++) {
            for (int j = 0; j < organs.get(i).getActions().size(); j++) {
                Action temp = organs.get(i).getActions().get(j);
                for (int k = 0; k < world.getSize()[0]; k++) {
                    for (int l = 0; l < world.getSize()[1]; l++) {
                        int tempValue = evaluationFunction(k, l, temp);
                        if (tempValue > bestValue) {
                            bestValue = tempValue;
                            best = temp;
                            xcoord = k;
                            ycoord = l;
                        }
                    }
                }
                //see the world and the advantages every action offers
            }

        }
        bestx = xcoord;
        besty = ycoord;
        return best;
    }

    public int evaluationFunction(int x, int y, Action a) {
        String terrInfo = world.getTerrainInfo(x, y);
        String objInfo = world.getObjectInfo(x, y);
        int flag = 0;
        Iterator it = a.getCondition().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            if(pair.getKey().equals("Distance")){
                if(((Integer[])(pair.getValue()))[0]<Math.abs(x-posx)+Math.abs(y-posy)){
                    continue;
                }else{
                return 0;
                }
            }
            for (int j = 0; j < terrInfo.split("\n").length; j++) {
                if (terrInfo.split("\n")[j].split(":")[0].equals(pair.getKey()) ) {
                    if(Integer.parseInt(terrInfo.split("\n")[j].split(":")[1])>=((Integer[])pair.getValue())[0] && Integer.parseInt(terrInfo.split("\n")[j].split(":")[1])<=((Integer[])pair.getValue())[1]){
                    flag = 1;
                    break;
                    }else{
                        return 0;
                    }
                }
            }
            if(flag==1){
                flag=0;
                continue;
            }
            for (int j = 0; j < objInfo.split("\n").length; j++) {
                if (objInfo.split("\n")[j].split(":")[0].equals(pair.getKey()) ) {
                    if(Integer.parseInt(objInfo.split("\n")[j].split(":")[1])>=((Integer[])pair.getValue())[0] && Integer.parseInt(objInfo.split("\n")[j].split(":")[1])<=((Integer[])pair.getValue())[1]){
                    flag = 1;
                    break;
                    }else{
                        return 0;
                    }
                }
            }
            if(flag==1){
                flag=0;
                
                continue;
            }
            //System.out.println(pair.getKey() + " = " + pair.getValue());
            it.remove(); // avoids a ConcurrentModificationException
            return 0;
        }

        return 0;
    }
}
