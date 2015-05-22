/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AI;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Arc2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author jonathan
 */
class MyCanvas extends JPanel implements ActionListener {

    private int width;
    private int height;
    private int totalWidth;
    private int totalHeight;
    private Timer timer;
    private int w = 1200;
    private int h = 1200;
    private int size = 25;
    private int xcoord = 0;
    private int ycoord = 0;
    private int x, y;
    private String[][] objectInfo;
    private String[][] terrainInfo;
    private int[][] objects;
    private Color[][] colors;
    private int displacedX = 0, displacedY = 0;
    private boolean pressed = false;

    public void adjust(int ww, int hh) {
        this.w = (int) (ww);
        this.h = (int) (hh);
        width=w;
        height=h;
        if (x > y) {
            size = height / y;
        } else {
            size = width / x;
        }
        this.setSize(ww, hh);
    }

    public void moveMouse(int ww, int hh) {
        xcoord = ww;
        ycoord = hh;
    }

    public String getObjectInfo(int x, int y) {
        return objectInfo[x][y];
    }

    public String getTerrainInfo(int x, int y) {
        return terrainInfo[x][y];
    }

    public int[][] getObjects() {
        return objects;
    }

    public MyCanvas(int width2, int height2, int sizex, int sizey) {
        //setBackground(Color.getHSBColor(0.3f, 0.4f, 0.6f));
        setBackground(Color.getHSBColor(0.0f, 0.0f, 0.0f));
        this.width = width2;
        this.height = height2;
        setDoubleBuffered(true);

        timer = new Timer(10, this);
        timer.setInitialDelay(10);
        timer.start();
        //x = (int) Math.ceil(sizex / size);
        //y = (int) Math.ceil(sizey / size);
        x = sizex;
        y = sizey;
        if (sizex > sizey) {
            size = height / y;
        } else {
            size = width / x;
        }
        totalWidth = sizex * size;
        totalHeight = sizey * size;
        objectInfo = new String[sizex][sizey];
        terrainInfo = new String[sizex][sizey];
        objects = new int[sizex][sizey];
        colors = new Color[sizex][sizey];
        for (int i = 0; i < sizex; i++) {
            objectInfo[i] = new String[sizey];
            terrainInfo[i] = new String[sizey];
            objects[i] = new int[sizey];
            colors[i] = new Color[sizey];
            for (int j = 0; j < sizey; j++) {
                objectInfo[i][j] = "";
                terrainInfo[i][j] = "";
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        paint2(g);
    }

    public void paint2(Graphics g) {
        Graphics2D g2;
        g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setStroke(new BasicStroke(1));
        g2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        g2.setColor(Color.getHSBColor((float) ((15.0) / 360.0), 0.8f, 0.8f));
        g2.fillRect(0, 0, width, height);
        int tempX, tempY;
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                tempX = i * size - displacedX;
                tempY = j * size - displacedY;
                while (tempX > totalWidth) {
                    tempX -= totalWidth;
                }
                while (tempY > totalHeight) {
                    tempY -= totalHeight;
                }
                while (tempX < width - totalWidth) {
                    tempX += totalWidth;
                }
                while (tempY < height - totalHeight) {
                    tempY += totalHeight;
                }
                g2.setColor(Color.getHSBColor((float) ((20.0) / 360.0), 0.8f, 0.8f));
                g2.fillRect(tempX, tempY, size, size);
                g2.setColor(Color.black);
                g2.drawRect(tempX, tempY, size, size);
                if (objects[i][j] != 0) {
                    g2.setColor(colors[i][j]);
                    switch( objects[i][j]){
                        case 1:
                            g2.fill(new Arc2D.Double(tempX, tempY, size, size, 0, 360, Arc2D.PIE));
                            break;
                        case 2:
                            g2.fill(new Arc2D.Double(tempX, tempY, size/2, size/2, 0, 360, Arc2D.PIE));
                            g2.fill(new Arc2D.Double(tempX+size/2, tempY+size/2, size/2, size/2, 0, 360, Arc2D.PIE));
                            break;
                        case 3:
                            
                            g2.fillRect(tempX, tempY, size/2,size/2);
                            break;
                        
                    }
                }
            }
        }
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                tempX = i * size - displacedX;
                tempY = j * size - displacedY;
                while (tempX > totalWidth) {
                    tempX -= totalWidth;
                }
                while (tempY > totalHeight) {
                    tempY -= totalHeight;
                }
                while (tempX < width - totalWidth) {
                    tempX += totalWidth;
                }
                while (tempY < height - totalHeight) {
                    tempY += totalHeight;
                }
                if (pressed == false && (!objectInfo[i][j].equals("") || !terrainInfo[i][j].equals("")) && xcoord > tempX && xcoord < tempX + size && ycoord > tempY && ycoord < tempY + size) {
                    int linesOfText = objectInfo[i][j].split(",").length + terrainInfo[i][j].split(",").length;
                    g2.setColor(Color.getHSBColor((float) ((160.0) / 360.0), 0.8f, 0.8f));
                    g2.fillRoundRect(xcoord, ycoord - 30 * (linesOfText + 1), 200, 30 * (linesOfText + 1), 30, 30);
                    g2.setColor(Color.getHSBColor((float) ((230.0) / 360.0), 0.8f, 0.8f));
                    if (!objectInfo[i][j].equals("")) {
                        for (int k = 0; k < objectInfo[i][j].split(",").length; k++) {
                            g2.drawString(objectInfo[i][j].split(",")[k], xcoord + 20, ycoord - 30 * (linesOfText));
                            linesOfText--;
                        }
                    }
                    if (!terrainInfo[i][j].equals("")) {
                        for (int k = 0; k < terrainInfo[i][j].split(",").length; k++) {
                            g2.drawString(terrainInfo[i][j].split(",")[k], xcoord + 20, ycoord - 30 * (linesOfText));
                            linesOfText--;
                        }
                    }

                }
            }
        }

    }

    public double dist(int x1, int x2, int y1, int y2) {
        return (Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2)));
    }

    public void setObject(int posx, int posy, float hue, float bri, float sat,int type) {
        objects[posx][posy] = type;
        colors[posx][posy] = Color.getHSBColor(hue, bri, sat);
    }

    public void setObjectInfo(int posx, int posy, String info) {
        objectInfo[posx][posy] = info;
    }

    public void setTerrainInfo(int posx, int posy, String info) {
        terrainInfo[posx][posy] = info;
    }

    public void actionPerformed(ActionEvent e) {
        this.repaint();
    }

    public void moveWorld(int x, int y) {
        displacedX -= x;
        displacedY -= y;
    }

    public void setPressed(boolean p) {
        pressed = p;
    }

    public void setZoom(double z) {
        if (z < 0) {
            size = (int) (size * (1 / z * -1));
            displacedX=(int) (displacedX * (1 / z * -1));
            displacedY=(int) (displacedY * (1 / z * -1));
            if (x > y) {
                if(size < height / y){
                    size = height / y;
                }
            } else {
                if(size <width / x){
                    size = width / x;
                }
            }
        } else {
            size *= z;
            displacedX*=z;
            displacedY*=z;
            if(size >height){
                size=height-100;
            }
            if(size>width){
                size=width-100;
            }
            
        }
        totalWidth = x * size;
            totalHeight = y * size;
    }
    public void emptyElements(){
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                
            objectInfo[i][j]="";
            terrainInfo[i][j]="";
            objects[i][j]=0;
            }
        }
    
    }

}
