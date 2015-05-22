package AI;

import AI.AgentClasses.Agent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import javax.swing.JPanel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Vaferdolosa
 */
public class World {

    private final int size[];
    private Square grid[][];

    private HashMap<String, Integer> LTVarName;
    private List<Variable> LTVar;

    private HashMap<String, Integer> LVarName;
    private List<Variable> LVar;

    private HashMap<String, Variable> TVar;
    private HashMap<String, Variable> CVar;

    private List<Main.Lfunction> LRule;
    private List<Main.Tfunction> TRule;
    private List<Main.LTfunction> LTRule;
    private List<Main.Cfunction> IRule;
    private List<Main.Cfunction> WRule;

    private List<Agent> agent;

    private long time;
    private boolean end;

    jFrame1 jframe;
    MyCanvas canvas;
    JPanel jpanel;

    public World(int weight, int height) {

        size = new int[2];
        size[0] = weight;
        size[1] = height;

        grid = new Square[weight][height];

        LTVarName = new HashMap<>();
        LTVar = new LinkedList();

        LVarName = new HashMap<>();
        LVar = new LinkedList();

        TVar = new HashMap<>();
        CVar = new HashMap<>();

        LRule = new LinkedList();
        TRule = new LinkedList();
        LTRule = new LinkedList();
        IRule = new LinkedList();
        WRule = new LinkedList();

        agent = new LinkedList();

        time = 0;
        end = false;

        jframe = new jFrame1(weight, height);

        jpanel = jframe.getJPanel();
        canvas = jframe.getCanvas();
        //canvas = new MyCanvas(jpanel.getWidth(), jpanel.getHeight(),weight,height);// JPanel not accessible ... /!\
        jframe.setVisible(true);
        //canvas.setSize(jpanel.getWidth(), jpanel.getHeight());
        //jpanel.add(canvas);
        //canvas.setVisible(true);

    }

    public int[] getSize() {
        return size;
    }
    public String getTerrainInfo(int x, int y){
        draw();
        return canvas.getTerrainInfo(x,y);
    }
     public String getObjectInfo(int x, int y){
        draw();
        return canvas.getObjectInfo(x,y);
    }

    private boolean addVariable(String name) throws Exception {

        if (LTVarName.containsKey(name)) {
            throw new Exception("Variable " + name + " already exist!");
        } else {
            return true;
        }
    }

    public void addVariable(String name, Main.LTfunction updateFunction) throws Exception {
        addVariable(name);
        LTVarName.put(name, LTVar.size());
        LTVar.add(new Variable(this, name, updateFunction));
    }

    public void addVariable(String name, Main.Lfunction updateFunction) throws Exception {
        addVariable(name);
        LVarName.put(name, LVar.size());
        LVar.add(new Variable(this, name, updateFunction));

    }

    public void addVariable(String name, Main.Tfunction updateFunction) throws Exception {
        addVariable(name);
        TVar.put(name, new Variable(this, name, updateFunction));
    }

    public void addVariable(String name, int value) throws Exception {
        addVariable(name);
        CVar.put(name, new Variable(this, name, value));
    }

    public void addRule(Main.LTfunction rule) {
        LTRule.add(rule);
    }

    public void addRule(Main.Lfunction rule) {
        LRule.add(rule);
    }

    public void addRule(Main.Tfunction rule) {
        TRule.add(rule);
    }

    public void addRule(Main.Cfunction rule) {
        IRule.add(rule);
    }

    public Variable getVariable(String name) throws Exception {
        if (LTVarName.containsKey(name) || LVarName.containsKey(name)) {
            return null;
        } else {
            if (TVar.containsKey(name)) {
                return TVar.get(name);
            } else if (CVar.containsKey(name)) {
                return CVar.get(name);
            } else {
                throw new Exception("Variable " + name + " doesn't exist!");
            }
        }
    }

    public int getVariableIndex(String name) throws Exception {
        if (LTVarName.containsKey(name)) {
            return LTVarName.get(name);
        } else if (LVarName.containsKey(name)) {
            return LVarName.get(name);
        } else {
            throw new Exception("Variable " + name + " doesn't exist!");
        }
    }

    public boolean isTimeless(String variable) {
        return LVarName.containsKey(variable);
    }

    public int getLTVariableNumber() {
        return LTVarName.size();
    }

    public int getLVariableNumber() {
        return LVarName.size();
    }

    private void addAgent(Agent agent) {
        this.agent.add(agent);
    }

    private void removeAgent(Agent agent) {
        this.agent.remove(agent);
    }

    public void setElement(Element element, int x, int y) {
        if (element.isAgent()) {
            addAgent((Agent) element);
        }
        grid[x][y].setElement(element);
    }

    public void removeElement(int x, int y) {
        if (grid[x][y].getElement() != null) {
            if (grid[x][y].getElement().isAgent()) {
                removeAgent(((Agent) grid[x][y].getElement()));
            }
            grid[x][y].removeElement();
        }
    }

    public long getTime() {
        return time;
    }

    public Square getSquare(int x, int y) {
        return grid[x][y];
    }

    public void Stop() {
        end = true;
    }

    public void buildWorld() throws Exception {

        for (int x = 0; x < size[0]; x++) {
            for (int y = 0; y < size[1]; y++) {
                grid[x][y] = new Square(x, y, this);

                for (String i : LVarName.keySet()) {
                    int index = LVarName.get(i);
                    Variable v = LVar.get(index);
                    grid[x][y].setLVariable(index, v.Update(x, y));
                }

                for (String i : LTVarName.keySet()) {
                    int index = LTVarName.get(i);
                    Variable v = LTVar.get(index);
                    grid[x][y].setLTVariable(index, v.Update(x, y, 0));
                }
            }
        }

        for (String i : TVar.keySet()) {
            TVar.get(i).Update(0);
        }

        for (int i = 0; i < IRule.size(); i++) {
            IRule.get(i).compute(this);
        }

        for (int i = 0; i < TRule.size(); i++) {
            TRule.get(i).compute(this, 0);
        }

        for (int x = 0; x < size[0]; x++) {
            for (int y = 0; y < size[1]; y++) {
                for (int i = 0; i < LRule.size(); i++) {
                    LRule.get(i).compute(this, x, y);
                }
                for (int i = 0; i < LTRule.size(); i++) {
                    LTRule.get(i).compute(this, x, y, 0);
                }
            }
        }
    }

    public void next() throws Exception {
        time += 1;

        for (String i : TVar.keySet()) {
            TVar.get(i).Update(time);
        }

        for (int x = 0; x < size[0]; x++) {
            for (int y = 0; y < size[1]; y++) {
                for (String i : LTVarName.keySet()) {
                    int index = LTVarName.get(i);
                    Variable v = LTVar.get(index);
                    grid[x][y].setLTVariable(index, v.Update(x, y, time));
                }
            }
        }

        for (int i = 0; i < WRule.size(); i++) {
            WRule.get(i).compute(this);
        }

        for (int i = 0; i < TRule.size(); i++) {
            TRule.get(i).compute(this, time);
        }

        for (int x = 0; x < size[0]; x++) {
            for (int y = 0; y < size[1]; y++) {
                for (int i = 0; i < LTRule.size(); i++) {
                    LTRule.get(i).compute(this, x, y, time);
                }
            }
        }

        for (Agent agt : agent) {
            agt.act();
        }
    }

    public void run() throws Exception {
        while (!end) {
            next();
            draw();

            Thread.sleep(300);
        }
    }

    public void drawVariableDEBUG(String var) throws Exception {
        for (int x = 0; x < size[0]; x++) {
            for (int y = 0; y < size[1]; y++) {
                System.out.print(grid[x][y].getVariable(var));
                System.out.print("|");
            }
            System.out.println();
        }

        System.out.println();
        System.out.println();
        System.out.println();
    }

    public void drawElementDEBUG() throws Exception {
        for (int x = 0; x < size[0]; x++) {
            for (int y = 0; y < size[1]; y++) {
                System.out.print(grid[x][y].getElement() == null ? "O" : "X");
            }
            System.out.println();
        }

        System.out.println();
        System.out.println();
        System.out.println();
    }

    public void draw() {
        //jpanel.remove(canvas);
        //canvas = new MyCanvas(jpanel.getWidth(), jpanel.getHeight(),size[0],size[1]);// JPanel not accessible ... /!\
        //jframe.setVisible(true);
        //canvas.setSize(jpanel.getWidth(), jpanel.getHeight());
        //jpanel.add(canvas);

        //canvas.setVisible(true);
        canvas.emptyElements();
        for (int x = 0; x < size[0]; x++) {
            for (int y = 0; y < size[1]; y++) {
                Square s = grid[x][y];
                if (s.getElement() != null) {
                    if (s.getElement().isAgent()) {
                        canvas.setObject(x, y, 100, 100, 100, 3);
                    } else {
                        canvas.setObject(x, y, 100, 100, 100, 1);//TODO color attribute for Element
                    }
                    //setObjectInfo(int posx, int posy, String info) TODO no info for now
                }
                String txt = "";
                for (String i : LTVarName.keySet()) {
                    int index = LTVarName.get(i);
                    Variable v = LTVar.get(index);
                    txt += i + ": " + (v.Update(x, y, time)) + "\n";
                    canvas.setTerrainInfo(x, y, txt);
                }
                for (String i : LVarName.keySet()) {
                    int index = LVarName.get(i);
                    Variable v = LVar.get(index);
                    txt += i + ": " + (v.Update(x, y, time)) + "\n";
                    canvas.setTerrainInfo(x, y, txt);
                }
            }
        }
    }
}
