package softcodedgameproject;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JWindow;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;

public class SoftCodedGameProject {

    
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable(){
            @Override
            public void run() {
                
                JFrame Mainframe = new Frame("Game of Life");
                
                Mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                Mainframe.pack();
                Mainframe.setVisible(true);
            }
            
        });
    }
    
}

//FRAME BEGINS
//========================================================================================================================================================
//========================================================================================================================================================
class Frame extends JFrame{
    
    //Variable for living cells
    //---------------------------------------------------------------------------------------
    ArrayList<intCell> Livecells = new ArrayList<>();
    /**
     * This Array (Livecells) behaves as the model for the simulation; since the program is fairly simple,
     * the necessity for implementing a class as a model was not felt.
     */


    //=====================================================================================================================================================
    //**IMPORTANT INSTRUCTIONS**
    //=====================================================================================================================================================
    /**
     * Please edit in accordance with the following instructions to avoid abrupt behavior of program.:
     * 
     * 1. DO NOT change the initial values of any variable, other than 'GRID_SIDE'.
     * 2. 'GRID_SIDE 'should preferably by an even number, and should be at-least 180 for optimum behavior.
     * 3. For debugging, follow the comments for critical parts of the program.
     * 4. Be informed, that the performance of the program might be affected due to debugging outputs (example: might perform significantly slow).
     * 
     */
    //=====================================================================================================================================================


    //Variables for Grid Location
    //---------------------------------------------------------------------------------------
    int x = 0, y = 0, x2 = 0, y2 = 0;       //initial and ending points of drag
    boolean move = false;                   //true only if grid dragged
    int prevX = x2 - x; int prevY = y2 - y; //facilitates in locating the first cell in the grid(i.e. top-left-most cell)

    //int left_dist_travelled, right_dist_travelled, top_dist_travelled, bottom_dist_travelled;

    //Variables for Grid size
    //---------------------------------------------------------------------------------------
    int GRID_SIDE = 100;                    //number of squares/side of grid
    int HALF_GRID = (int)GRID_SIDE/2;       //half of grid side (since grid starts at (-half_grid))
    int sq_side_length = 10;                //side of each square (illusion of zoom in and out)

    //Variables for Simulation of Game
    //---------------------------------------------------------------------------------------
    int start_speed = 350;                  //the speed of simulation when start clicked
    int generation = 0;                     //to print generation
    boolean halt = true;                    //if the next click should stop the simulation
    boolean firstStart = true;              //first time start is pressed
    boolean manualSim = true;               //if manually editing is permitted
    
    
    //==========================================================================================================
    //--MAIN WINDOW
    //==========================================================================================================
    Frame(String title){
        
        setTitle(title);
        
        //MAIN GAME GRID
        //----------------------------------------------------------------------
        //creating grid
        final JPanel gamegrid = new gridPanel();
        //gamegrid.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        
        //----------------------------------------------------------------------
        
        //ADDING MENU BAR
        //=====================================================================================================
        JMenuBar hiddenmenu = new JMenuBar(); hiddenmenu.setVisible(false);
        hiddenmenu.setBackground(Color.black); hiddenmenu.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        hiddenmenu.setForeground(Color.red);
        //FILE MENU----------------------------------------------
        JMenu filemenu = new JMenu("File", false); filemenu.setForeground(Color.red); hiddenmenu.add(filemenu);
        //filemenu.add("Open").addActionListener(new Opening Action());
        //EDIT MENU----------------------------------------------
        JMenu editmenu = new JMenu("Edit", false); editmenu.setForeground(Color.red); hiddenmenu.add(editmenu, Color.red);
        editmenu.add("Editing").addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                if(firstStart || !halt){ manualSim = !manualSim;}
            }
            
        });
        //SETTINGS MENU----------------------------------------------
        JMenu settingsmenu = new JMenu("Settings", false); settingsmenu.setForeground(Color.red); hiddenmenu.add(settingsmenu);
        //=====================================================================================================


        
        //ADDING POP MENU
        //=====================================================================================================
        JPopupMenu rclick = new JPopupMenu();
        //rclick.setPreferredSize(new Dimension(70, 40));
        gamegrid.setComponentPopupMenu(rclick);
        rclick.add("Edit").addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                if(firstStart || !halt){ manualSim = !manualSim;}
            }
        });
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new FileNameExtensionFilter("txt Files","txt"));
    
 
        //ADDING MOUSE MOTION LISTENER
        //=====================================================================================================
        gamegrid.addMouseMotionListener(new MouseMotionAdapter(){
            @Override
            public void mouseDragged(MouseEvent e) {
                x2 = e.getX(); y2 = e.getY();
                move = true;
                repaint();
            }
            
            @Override
            public void mouseMoved(MouseEvent e){
                if(e.getY() < 40) hiddenmenu.setVisible(true);
                else hiddenmenu.setVisible(false);
            }
        });
        
        //ADDING MOUSE LISTENER
        //=====================================================================================================
        gamegrid.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                if(manualSim){
                    if(e.getButton() == MouseEvent.BUTTON1){
                      //System.out.println("Clicked");          //FOR DEBUGGING
                        int xx = e.getX(); int yy = e.getY();   //get location of mouse
                        int count = 0;                          //to know the count number of the box selected
                        //looping through the grid, to get the box
                        for(int j = -HALF_GRID; j < HALF_GRID; j++){
                            for(int i = -HALF_GRID; i < HALF_GRID; i++){
                                /**
                                 * Box Selection.
                                 * If the mouse location falls between the left side of a particular box and the left side of its successive box. This narrows the search to a column.
                                 * If the mouse location falls between the top side of a particular box and the top side of its successive box. This narrows the search to a row.
                                 * Intersection of the row and column would result in the selection of ONE unique box.
                                 */
                                if((xx > (prevX + i*sq_side_length) && xx < (prevX + (i + 1)*sq_side_length)) && (yy > (prevY + j*sq_side_length) && yy < (prevY + (j + 1)*sq_side_length))){
                                    boolean notfound = true;                            //to check if box is to selected /or/ deselected (resurrect or kill)
                                        for(int k = 0; k < Livecells.size(); k++){
                                            if(Livecells.get(k).getCounter() == count) {
                                                Livecells.remove(k);                    //killing the cell            
                                                notfound = false;                       //cell was killed
                                            }
                                        }
                                    if(notfound) {Livecells.add(new intCell(count));}   /*cell was resurrected*/
                                    System.out.println("count: " + count);              //FOR DEBUGGING
                                }
                                count++;
                            }
                        }
                        repaint();
                    }
                    else if(e.getButton() == MouseEvent.BUTTON3){
                        //ADD POP-DOWN MENU
                        rclick.setVisible(true);
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                //System.out.println("Pressed");        //FOR DEBUGGING
                setCursor(Cursor.HAND_CURSOR);
                x = e.getX(); y = e.getY();             //set initial position of drag
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                setCursor(Cursor.DEFAULT_CURSOR);
                if(move){                               //changes the location of the grid only if the grid is ACTUALLY dragged
                    prevX += x2 - x; prevY += y2 - y;   //sets location of first cell relative to the ORIGIN of PANEL
                }
                move = false;                           //never changes the location of the grid, if not ACTUALLY dragged
                x2 = e.getX(); y2 = e.getY();
            }
            
        });
        
        //----------------------------------------------------------------------
        
        
        //MENUBAR OPTIONS
        //----------------------------------------------------------------------
        //creating menubar and setting flowlayout
        JPanel menubar = new JPanel();
        menubar.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        menubar.setBackground(Color.lightGray);
        menubar.setForeground(Color.red);
        menubar.setLayout(new FlowLayout(FlowLayout.LEFT));
        //----------------------------------------------------------------------
        
        //ADDING GENERATION DISPLAY
        //=====================================================================================================
        JLabel gen_display = new JLabel();
        gen_display.setForeground(Color.red);
        gen_display.setText("Generation: " + generation);        
        
        //-------------------------------------------------------------------------------------------------------------------------------------
        //GAME LOGIC LISTENER
        //-------------------------------------------------------------------------------------------------------------------------------------
        //=====================================================================================================================================
        class logic implements ActionListener{

            @Override
            public void actionPerformed(ActionEvent ae) {
                //System.out.println("============================================================================================================");
                ArrayList<intCell> killCells = new ArrayList<>();
                ArrayList<intCell> createCells = new ArrayList<>();
                
                //-------------------------------------------------------------------------------------------------------------------------------
                //-------------------------------------------------------------------------------------------------------------------------------
                for(int i = 0; i < Livecells.size(); i++){
                    int count = Livecells.get(i).getCounter();
                    int[] neighbours = new int[8]; int AliveNeighbours = 0;

                    int top = count - GRID_SIDE, bottom = count + GRID_SIDE, left = count - 1, right = count + 1;
                    int topleft = count - (GRID_SIDE + 1), topright = count - (GRID_SIDE - 1), bottomleft = count + (GRID_SIDE - 1), bottomright = count + (GRID_SIDE + 1);
                    
                    //(!(count < GRID_SIDE)) -> for squares on the topmost row
                    //(count%GRID_SIDE != 0) -> for squares on the leftmost column
                    //(count < GRID_SIDE*(GRID_SIDE - 1)) -> for squares on the bottommost row
                    //((count + 1)%GRID_SIDE != 0) -> for squares on the rightmost column
                    //--------------------------------------------------------------------------------
                    if (!(count < GRID_SIDE)) neighbours[0] = top; else neighbours[0] = (GRID_SIDE*GRID_SIDE - (GRID_SIDE - count));
                    //--------------------------------------------------------------------------------
                    if (count < GRID_SIDE*(GRID_SIDE - 1)) neighbours[1] = bottom; else neighbours[1] = ((GRID_SIDE) - (GRID_SIDE*GRID_SIDE - count));
                    //--------------------------------------------------------------------------------
                    if (count%GRID_SIDE != 0) neighbours[2] = left; else neighbours[2] = count + (GRID_SIDE - 1);
                    //--------------------------------------------------------------------------------
                    if ((count + 1)%GRID_SIDE != 0) neighbours[3] = right; else neighbours[3] = (count + 1) - GRID_SIDE;
                    //--------------------------------------------------------------------------------
                    if (!(count < GRID_SIDE) && (count%GRID_SIDE != 0)) neighbours[4] = topleft; 
                    else {
                        //---------------------------------------
                        if ((count < GRID_SIDE) && (count%GRID_SIDE == 0)) neighbours[4] = (GRID_SIDE*GRID_SIDE - 1); ////
                        //---------------------------------------
                        else if (count < GRID_SIDE) neighbours[4] = (GRID_SIDE*GRID_SIDE - (GRID_SIDE - count)) - 1; ////
                        //---------------------------------------
                        else if (count%GRID_SIDE == 0) neighbours[4] = count - 1; ////
                        //---------------------------------------
                    }
                    //--------------------------------------------------------------------------------
                    if (!(count < GRID_SIDE) && ((count + 1)%GRID_SIDE != 0)) neighbours[5] = topright; 
                    else {
                        //---------------------------------------
                        if((count < GRID_SIDE) && ((count + 1)%GRID_SIDE == 0)) neighbours[5] = (GRID_SIDE*GRID_SIDE - GRID_SIDE); ////
                        //---------------------------------------
                        else if (count < GRID_SIDE) neighbours[5] = (GRID_SIDE*GRID_SIDE - (GRID_SIDE - count)) + 1; ////
                        //---------------------------------------
                        else if ((count + 1)%GRID_SIDE == 0) neighbours[5] = (count + 1) - 2*GRID_SIDE; ////
                        //---------------------------------------
                    }
                    //--------------------------------------------------------------------------------
                    if (count%GRID_SIDE != 0 && (count < GRID_SIDE*(GRID_SIDE - 1))) neighbours[6] = bottomleft; 
                    else {
                        //---------------------------------------
                        if (count%GRID_SIDE == 0 && !(count < GRID_SIDE*(GRID_SIDE - 1))) neighbours[6] = GRID_SIDE - 1; ////
                        //---------------------------------------
                        else if (count%GRID_SIDE == 0) neighbours[6] = count + 2*GRID_SIDE - 1;
                        //---------------------------------------
                        else if (!(count < GRID_SIDE*(GRID_SIDE - 1))) neighbours[6] = ((GRID_SIDE) - (GRID_SIDE*GRID_SIDE - count)) - 1;
                        //---------------------------------------
                    }
                    //--------------------------------------------------------------------------------
                    if (count < GRID_SIDE*(GRID_SIDE - 1) && ((count + 1)%GRID_SIDE != 0)) neighbours[7] = bottomright; 
                    else {
                        //---------------------------------------
                        if(!(count < GRID_SIDE*(GRID_SIDE - 1)) && ((count + 1)%GRID_SIDE == 0)) neighbours[7] = 0; ////
                        //---------------------------------------
                        else if (!(count < GRID_SIDE*(GRID_SIDE - 1))) neighbours[7] = ((GRID_SIDE) - (GRID_SIDE*GRID_SIDE - count)) + 1;
                        //---------------------------------------
                        else if ((count + 1)%GRID_SIDE == 0) neighbours[7] = count + 1; ////
                        //---------------------------------------
                    }
                    //--------------------------------------------------------------------------------
                    //System.out.println("For box: " + Livecells.get(i).getCounter());
                    for(int k = 0; k < neighbours.length; k++){
                        //System.out.println("neighbour at " + k + ": " + neighbours[k]);
                        for(int l = 0; l < Livecells.size(); l++){
                            if(Livecells.get(l).getCounter() == (neighbours[k])) AliveNeighbours++;
                        }
                    }
                    //System.out.println("Livecells: "); for(int m = 0; m < Livecells.size(); m++) System.out.println(Livecells.get(m).getCounter() + " ");
                    //System.out.println("Alive: " + AliveNeighbours);
                    if(AliveNeighbours > 3 || AliveNeighbours < 2) killCells.add(new intCell(count));
                }                
                
                //-------------------------------------------------------------------------------------------------------------------------------
                //-------------------------------------------------------------------------------------------------------------------------------
                for(int count = 0; count < GRID_SIDE*GRID_SIDE; count++){
                    int[] neighbours = new int[8]; int AliveNeighbours = 0;

                    int top = count - GRID_SIDE, bottom = count + GRID_SIDE, left = count - 1, right = count + 1;
                    int topleft = count - (GRID_SIDE + 1), topright = count - (GRID_SIDE - 1), bottomleft = count + (GRID_SIDE - 1), bottomright = count + (GRID_SIDE + 1);
                    
                    //(!(count < GRID_SIDE)) -> for squares on the topmost row
                    //(count%GRID_SIDE != 0) -> for squares on the leftmost column
                    //(count < GRID_SIDE*(GRID_SIDE - 1)) -> for squares on the bottommost row
                    //((count + 1)%GRID_SIDE != 0) -> for squares on the rightmost column
                    //--------------------------------------------------------------------------------
                    if (!(count < GRID_SIDE)) neighbours[0] = top; else neighbours[0] = (GRID_SIDE*GRID_SIDE - (GRID_SIDE - count));
                    //--------------------------------------------------------------------------------
                    if (count < GRID_SIDE*(GRID_SIDE - 1)) neighbours[1] = bottom; else neighbours[1] = ((GRID_SIDE) - (GRID_SIDE*GRID_SIDE - count));
                    //--------------------------------------------------------------------------------
                    if (count%GRID_SIDE != 0) neighbours[2] = left; else neighbours[2] = count + (GRID_SIDE - 1);
                    //--------------------------------------------------------------------------------
                    if ((count + 1)%GRID_SIDE != 0) neighbours[3] = right; else neighbours[3] = (count + 1) - GRID_SIDE;
                    //--------------------------------------------------------------------------------
                    if (!(count < GRID_SIDE) && (count%GRID_SIDE != 0)) neighbours[4] = topleft; 
                    else {
                        //---------------------------------------
                        if ((count < GRID_SIDE) && (count%GRID_SIDE == 0)) neighbours[4] = (GRID_SIDE*GRID_SIDE - 1);
                        //---------------------------------------
                        else if (count < GRID_SIDE) neighbours[4] = (GRID_SIDE*GRID_SIDE - (GRID_SIDE - count)) - 1;
                        //---------------------------------------
                        else if (count%GRID_SIDE == 0) neighbours[4] = count - 1;
                        //---------------------------------------
                    }
                    //--------------------------------------------------------------------------------
                    if (!(count < GRID_SIDE) && ((count + 1)%GRID_SIDE != 0)) neighbours[5] = topright; 
                    else {
                        //---------------------------------------
                        if((count < GRID_SIDE) && ((count + 1)%GRID_SIDE == 0)) neighbours[5] = (GRID_SIDE*GRID_SIDE - GRID_SIDE);
                        //---------------------------------------
                        else if (count < GRID_SIDE) neighbours[5] = (GRID_SIDE*GRID_SIDE - (GRID_SIDE - count)) + 1;
                        //---------------------------------------
                        else if ((count + 1)%GRID_SIDE == 0) neighbours[5] = (count + 1) - 2*GRID_SIDE;
                        //---------------------------------------
                    }
                    //--------------------------------------------------------------------------------
                    if (count%GRID_SIDE != 0 && (count < GRID_SIDE*(GRID_SIDE - 1))) neighbours[6] = bottomleft; 
                    else {
                        //---------------------------------------
                        if (count%GRID_SIDE == 0 && !(count < GRID_SIDE*(GRID_SIDE - 1))) neighbours[6] = GRID_SIDE - 1;
                        //---------------------------------------
                        else if (count%GRID_SIDE == 0) neighbours[6] = count + 2*GRID_SIDE - 1;
                        //---------------------------------------
                        else if (!(count < GRID_SIDE*(GRID_SIDE - 1))) neighbours[6] = ((GRID_SIDE) - (GRID_SIDE*GRID_SIDE - count)) - 1;
                        //---------------------------------------
                    }
                    //--------------------------------------------------------------------------------
                    if (count < GRID_SIDE*(GRID_SIDE - 1) && ((count + 1)%GRID_SIDE != 0)) neighbours[7] = bottomright; 
                    else {
                        //---------------------------------------
                        if(!(count < GRID_SIDE*(GRID_SIDE - 1)) && ((count + 1)%GRID_SIDE == 0)) neighbours[7] = 0;
                        //---------------------------------------
                        else if (!(count < GRID_SIDE*(GRID_SIDE - 1))) neighbours[7] = ((GRID_SIDE) - (GRID_SIDE*GRID_SIDE - count)) + 1;
                        //---------------------------------------
                        else if ((count + 1)%GRID_SIDE == 0) neighbours[7] = count + 1;
                        //---------------------------------------
                    }
                    //--------------------------------------------------------------------------------
//                    System.out.println("For box: " + count);
                    for(int k = 0; k < neighbours.length; k++){
//                        System.out.println("neighbour at " + k + ": " + neighbours[k]);
                        for(int l = 0; l < Livecells.size(); l++){
                            if(Livecells.get(l).getCounter() == (neighbours[k])) {
                                AliveNeighbours++;
                                //System.out.println("Livecells count (" + Livecells.get(l).getCounter() + ") has neighbour " + neighbours[k]);
                            }
                        }
                    }
                    
//                    System.out.println("Livecells: "); for(int m = 0; m < Livecells.size(); m++) System.out.println(Livecells.get(m).getCounter() + " ");
//                    System.out.println("Alive: " + AliveNeighbours);
                    if(AliveNeighbours == 3) {
                        //System.out.println("count: " + count);
                        createCells.add(new intCell(count));
                    }
                    
                }
                
                //-------------------------------------------------------------------------------------------------------------------------------
                //-------------------------------------------------------------------------------------------------------------------------------
                //System.out.println("Kill size: " + killCells.size());
                //kill cells
                for(int i = 0; i < killCells.size(); i++){
                    int whichcount = killCells.get(i).getCounter();
                    //System.out.print(whichcount + " ");
                    for(int j = 0; j < Livecells.size(); j++){
                        if(Livecells.get(j).getCounter() == whichcount) Livecells.remove(j);
                    }
                }
                for(int i = 0; i < createCells.size(); i++){
                    boolean contains = false;
                    for(int l = 0; l < Livecells.size(); l++){
                            if(Livecells.get(l).getCounter() == (createCells.get(i).getCounter())) {
                                contains = true;
                            }
                        }
                    if(!contains) Livecells.add(new intCell(createCells.get(i).getCounter()));
                }
                
                generation++;
                gen_display.setText("Generation: " + generation);
                repaint();
            }
        
        }
        //====================================================================================================
        //----------------------------------------------------------------------------------------------------
        
        
        //ADDING MANUAL SIMULATION
        //=====================================================================================================
        JButton next = new JButton("Next");
        next.addActionListener(new logic());
        
        //ADDING AUTOMATIC SIMULATION
        //=====================================================================================================
        JButton start = new JButton("Start");
        JComboBox speed = new JComboBox(); speed.setPreferredSize(new Dimension(90, 24));
        
        Timer simulation = new Timer(start_speed, new logic());
        start.addActionListener(new ActionListener(){
        
            @Override
            public void actionPerformed(ActionEvent ae) {
                //STARTING FIRST TIME
                if(firstStart) {
                    simulation.start(); start.setText("Stop"); firstStart = false; next.setEnabled(false); speed.setEnabled(false); manualSim = false;
                }
                //PROGRESSIVE STARTS AND STOPS
                else {
                    if(halt){simulation.stop();}
                    else {simulation.start();}
                    if(halt){
                        start.setText("Start");
                        halt = false; //System.out.println("should stop");
                        next.setEnabled(true);
                        speed.setEnabled(true);
                        manualSim = true;
                    }
                    else{
                        start.setText("Stop");
                        halt = true; //System.out.println("should start");
                        next.setEnabled(false);
                        speed.setEnabled(false);
                        manualSim = false;
                    }
                }
            }
            
        });

        
        //ADDING CONFIGURATION
        //=====================================================================================================
        JComboBox config = new JComboBox(); config.setPreferredSize(new Dimension(180, 24));
        config.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        config.addItem("Clear"); config.addItem("10 cell row"); config.addItem("Small Exploder");
        config.addItem("Exploder"); config.addItem("Glider"); config.addItem("Lightweight Spaceship");
        config.addItem("Tumbler"); config.addItem("Gosper Glider Gun"); config.addItem("R-Pentamino");
        config.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ie) {
                String whichconfig = (String) config.getSelectedItem();
                if(whichconfig.equals("Clear")){
                    Livecells.clear(); generation = 0; repaint(); gen_display.setText("Generation: " + generation);
                    //JOptionPane.showMessageDialog(null, "Grid cleared!");
                }
                else if(whichconfig.equals("R-Pentamino")){
                    Livecells.clear();
                    int count = HALF_GRID*GRID_SIDE - (HALF_GRID);
                    Livecells.add(new intCell(count - GRID_SIDE));
                    Livecells.add(new intCell(count- (GRID_SIDE -1)));
                    Livecells.add(new intCell(count + GRID_SIDE));
                    Livecells.add(new intCell(count -1));
                    Livecells.add(new intCell(count));
                     generation = 0; repaint(); gen_display.setText("Generation: " + generation);
                }
                else if(whichconfig.equals("10 cell row")){
                    Livecells.clear();
                    int count = HALF_GRID*GRID_SIDE - (HALF_GRID + 5);
                    for(int i = 0; i < 10; i++) Livecells.add(new intCell(count++));
                    generation = 0; repaint(); gen_display.setText("Generation: " + generation);
                    //JOptionPane.showMessageDialog(null, whichconfig + " configuration has been added to the center of the grid.\nPlease move to the center to view the simulation");
                }
                else if(whichconfig.equals("Small Exploder")){
                    Livecells.clear();
                    int count = HALF_GRID*GRID_SIDE - (HALF_GRID);
                    for(int i = -2; i < 2; i++) if(i != 0)Livecells.add(new intCell(count + i*GRID_SIDE));
                    Livecells.add(new intCell(count - 1)); Livecells.add(new intCell(count + 1));
                    Livecells.add(new intCell(count - (GRID_SIDE + 1))); Livecells.add(new intCell(count - GRID_SIDE + 1));
                    generation = 0; repaint(); gen_display.setText("Generation: " + generation);
                    //JOptionPane.showMessageDialog(null, whichconfig + " configuration has been added to the center of the grid.\nPlease move to the center to view the simulation");
                }
                else if(whichconfig.equals("Exploder")){
                    Livecells.clear();
                    int count = HALF_GRID*GRID_SIDE - (HALF_GRID + 2);
                    for(int i = -2; i < 3; i++) Livecells.add(new intCell(count + i*GRID_SIDE));
                    count += 4; for(int i = -2; i < 3; i++) Livecells.add(new intCell(count + i*GRID_SIDE));
                    count -= (2*GRID_SIDE + 2); Livecells.add(new intCell(count)); count += (4*GRID_SIDE); Livecells.add(new intCell(count));
                    generation = 0; repaint(); gen_display.setText("Generation: " + generation);
                    //JOptionPane.showMessageDialog(null, whichconfig + " configuration has been added to the center of the grid.\nPlease move to the center to view the simulation");
                }
                else if(whichconfig.equals("Glider")){
                    Livecells.clear();
                    int count = HALF_GRID*GRID_SIDE - (HALF_GRID);
                    for(int i = -1; i < 2; i++) if(i != 0)Livecells.add(new intCell(count + i*GRID_SIDE));
                    Livecells.add(new intCell(count + 1)); Livecells.add(new intCell(count + GRID_SIDE - 1)); Livecells.add(new intCell(count + (GRID_SIDE + 1)));
                    generation = 0; repaint(); gen_display.setText("Generation: " + generation);
                    //JOptionPane.showMessageDialog(null, whichconfig + " configuration has been added to the center of the grid.\nPlease move to the center to view the simulation");
                }
                else if(whichconfig.equals("Lightweight Spaceship")){
                    Livecells.clear();
                    int count = HALF_GRID*GRID_SIDE - (HALF_GRID);
                    Livecells.add(new intCell(count - (2*GRID_SIDE + 1))); /*-*/ Livecells.add(new intCell(count - (2*GRID_SIDE)));
                    Livecells.add(new intCell(count - (2*GRID_SIDE) + 1)); Livecells.add(new intCell(count - (2*GRID_SIDE - 2)));
                    Livecells.add(new intCell(count - (GRID_SIDE + 2))); Livecells.add(new intCell(count - GRID_SIDE + 2));
                    Livecells.add(new intCell(count + 2)); Livecells.add(new intCell(count + (GRID_SIDE - 2)));
                    Livecells.add(new intCell(count + (GRID_SIDE + 1)));
                    generation = 0; repaint(); gen_display.setText("Generation: " + generation);
                    //JOptionPane.showMessageDialog(null, whichconfig + " configuration has been added to the center of the grid.\nPlease move to the center to view the simulation");
                }
                else if(whichconfig.equals("Tumbler")){
                    Livecells.clear();
                    int count = HALF_GRID*GRID_SIDE - (HALF_GRID);
                    count -= (3*GRID_SIDE);
                    for(int i = 0; i < 5; i++) {Livecells.add(new intCell(count - 1 + i*GRID_SIDE)); Livecells.add(new intCell(count + 1 + i*GRID_SIDE));}
                    Livecells.add(new intCell(count - 2)); Livecells.add(new intCell(count + 2));
                    Livecells.add(new intCell(count + (GRID_SIDE - 2))); Livecells.add(new intCell(count + (GRID_SIDE + 2)));
                    count += 4*GRID_SIDE;
                    Livecells.add(new intCell(count - (GRID_SIDE + 3))); Livecells.add(new intCell(count - (GRID_SIDE - 3)));
                    Livecells.add(new intCell(count - 3)); Livecells.add(new intCell(count + 3));
                    Livecells.add(new intCell(count + (GRID_SIDE - 3))); Livecells.add(new intCell(count + (GRID_SIDE - 2)));
                    Livecells.add(new intCell(count + (GRID_SIDE + 2))); Livecells.add(new intCell(count + (GRID_SIDE + 3)));
                    generation = 0; repaint(); gen_display.setText("Generation: " + generation);
                    //JOptionPane.showMessageDialog(null, whichconfig + " configuration has been added to the center of the grid.\nPlease move to the center to view the simulation");
                }
                else if(whichconfig.equals("Gosper Glider Gun")){
                    Livecells.clear();
                    int count = HALF_GRID*GRID_SIDE - (HALF_GRID);
                    count -= 3*GRID_SIDE;
                    //EACH FIGURE IS NUMBERED BASED ON INITIAL POSITION FROM THE LEFT
                    //FIGURE 3:
                    Livecells.add(new intCell(count)); Livecells.add(new intCell(count - 2));
                    Livecells.add(new intCell(count - (GRID_SIDE + 1))); Livecells.add(new intCell(count - (GRID_SIDE + 2)));
                    Livecells.add(new intCell(count + (GRID_SIDE - 2)));
                    //FIGURE 2:
                    count -= (2*GRID_SIDE + 8);
                    Livecells.add(new intCell(count)); Livecells.add(new intCell(count - 2));
                    Livecells.add(new intCell(count - GRID_SIDE)); Livecells.add(new intCell(count - (GRID_SIDE + 1)));
                    Livecells.add(new intCell(count + (GRID_SIDE - 2))); Livecells.add(new intCell(count + (GRID_SIDE - 1)));
                    //FIGURE 1:
                    count -= 9;
                    Livecells.add(new intCell(count)); Livecells.add(new intCell(count - 1));
                    Livecells.add(new intCell(count - GRID_SIDE)); Livecells.add(new intCell(count - (GRID_SIDE + 1)));
                    //FIGURE 4:
                    count += 23; count -= 2*GRID_SIDE;
                    Livecells.add(new intCell(count)); Livecells.add(new intCell(count - 2));
                    Livecells.add(new intCell(count - GRID_SIDE)); Livecells.add(new intCell(count - (GRID_SIDE + 1)));
                    Livecells.add(new intCell(count + (GRID_SIDE - 2))); Livecells.add(new intCell(count + (GRID_SIDE - 1)));
                    //FIGURE 5:
                    count += 11*GRID_SIDE;
                    Livecells.add(new intCell(count)); Livecells.add(new intCell(count + 1));
                    Livecells.add(new intCell(count + 2)); Livecells.add(new intCell(count + GRID_SIDE));
                    Livecells.add(new intCell(count + (2*GRID_SIDE + 1)));
                    //FIGURE 7
                    count += 13; count -= 4*GRID_SIDE;
                    Livecells.add(new intCell(count)); Livecells.add(new intCell(count - 2));
                    Livecells.add(new intCell(count - (GRID_SIDE + 1))); Livecells.add(new intCell(count - (GRID_SIDE + 2)));
                    Livecells.add(new intCell(count + (GRID_SIDE - 2)));
                    //FIGURE 6
                    count -= (7*GRID_SIDE + 2);
                    Livecells.add(new intCell(count)); Livecells.add(new intCell(count - 1));
                    Livecells.add(new intCell(count - GRID_SIDE)); Livecells.add(new intCell(count - (GRID_SIDE + 1)));
                    generation = 0; repaint(); gen_display.setText("Generation: " + generation);
                    
                }
                    JOptionPane.showMessageDialog(null, whichconfig + " configuration has been added to the center of the grid.\nPlease move to the center to view the simulation");
            }
            
        });

        //ADDING SIMULATION SPEEDS
        //=====================================================================================================
        speed.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        speed.addItem("Very Slow"); speed.addItem("Slow"); speed.addItem("Medium"); speed.addItem("Fast"); speed.addItem("Very Fast"); 
        speed.setSelectedItem("Medium");
        speed.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent ie) {
                String whichspeed = (String) speed.getSelectedItem();
                if(whichspeed.equals("Very Slow")){ start_speed = 1000; simulation.setDelay(start_speed); }
                else if(whichspeed.equals("Slow")){ start_speed = 500; simulation.setDelay(start_speed); }
                else if(whichspeed.equals("Medium")){ start_speed = 200; simulation.setDelay(start_speed); }
                else if(whichspeed.equals("Fast")){ start_speed = 80; simulation.setDelay(start_speed); }
                else if(whichspeed.equals("Very Fast")){ start_speed = 10; simulation.setDelay(start_speed); }
            }
            
        });

        //ADDING ZOOM SIZES
        //=====================================================================================================
        JComboBox size = new JComboBox(); size.setPreferredSize(new Dimension(90, 24));
        size.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        size.addItem("Small"); size.addItem("Medium"); size.addItem("Big");
        size.setSelectedItem("Medium");
        size.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent ie) {
                String whichsize = (String) size.getSelectedItem();
                if(whichsize.equals("Small")){ sq_side_length = 5; repaint(); }
                else if(whichsize.equals("Medium")){ sq_side_length = 10; repaint(); }
                else if(whichsize.equals("Big")){ sq_side_length = 15; repaint(); }
            }
            
        });        
        
        // (CONT ...) ADDING POP MENU
        //=====================================================================================================
        class SettingsAction implements ActionListener{
           
          
            @Override
            public void actionPerformed(ActionEvent ae) {
                //--MAIN SETTINGS PANEL--------------------------------------------------------------------
                //------------------------------------------------------------------------------------------
                JPanel settings = new JPanel();
                settings.setLayout(new BorderLayout());
                settings.setPreferredSize(new Dimension(400, 250));
                
                //--Zoom and Speed Panel--------------------------------------------------------------------
                //------------------------------------------------------------------------------------------
                JPanel north = new JPanel();
                north.setLayout(new GridLayout(1, 2));

                //--Zoom--------------------------------------------------------------------
                JPanel ZOOM = new JPanel();
                ButtonGroup zoomgroup = new ButtonGroup();
                JRadioButton smallzoom = new JRadioButton("Small");
                smallzoom.addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        sq_side_length = 5; repaint();
                        size.setSelectedItem("Small");
                    }
                });
                JRadioButton mediumzoom = new JRadioButton("Medium");
                mediumzoom.addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        sq_side_length = 10; repaint();
                        size.setSelectedItem("Medium");
                    }
                });
                JRadioButton bigzoom = new JRadioButton("Big");
                bigzoom.addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        sq_side_length = 15; repaint();
                        size.setSelectedItem("Big");
                    }
                });
                zoomgroup.add(smallzoom); zoomgroup.add(mediumzoom); zoomgroup.add(bigzoom);
                switch((String)size.getSelectedItem()){
                    case"Big": bigzoom.setSelected(true);
                    break;
                    case"Medium":mediumzoom.setSelected(true);
                    break;
                    case"Small":smallzoom.setSelected(true);
                    break;
                }
                ZOOM.setLayout(new BoxLayout(ZOOM, BoxLayout.Y_AXIS));
                ZOOM.add(smallzoom); ZOOM.add(mediumzoom); ZOOM.add(bigzoom);
                north.add(ZOOM);
                ZOOM.setBorder(BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED), "Zoom"));

                //--Speed--------------------------------------------------------------------
                JPanel SPEED = new JPanel();
                ButtonGroup speedgroup = new ButtonGroup();
                JRadioButton veryslowspeed = new JRadioButton("Very Slow");
                veryslowspeed.addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        start_speed = 1000; simulation.setDelay(start_speed);
                        speed.setSelectedItem("Very Slow");
                    }
                });
                JRadioButton slowspeed = new JRadioButton("Slow");
                slowspeed.addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        start_speed = 500; simulation.setDelay(start_speed);
                        speed.setSelectedItem("Slow");
                    }
                });
                JRadioButton mediumspeed = new JRadioButton("Medium");
                mediumspeed.addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        start_speed = 200; simulation.setDelay(start_speed);
                        speed.setSelectedItem("Medium");
                    }
                });
                JRadioButton fastspeed = new JRadioButton("Fast");
                fastspeed.addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        start_speed = 80; simulation.setDelay(start_speed);
                        speed.setSelectedItem("Fast");
                    }
                });
                JRadioButton veryfastspeed = new JRadioButton("Very Fast");
                veryfastspeed.addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        start_speed = 10; simulation.setDelay(start_speed);
                        speed.setSelectedItem("Very Fast");
                    }
                });
                speedgroup.add(veryslowspeed); speedgroup.add(slowspeed); speedgroup.add(mediumspeed); speedgroup.add(fastspeed); speedgroup.add(veryfastspeed);
                SPEED.setLayout(new BoxLayout(SPEED, BoxLayout.Y_AXIS));
                SPEED.add(veryslowspeed); SPEED.add(slowspeed); SPEED.add(mediumspeed); SPEED.add(fastspeed); SPEED.add(veryfastspeed);
                switch((String)speed.getSelectedItem()){
                    case"Slow":slowspeed.setSelected(true);
                    break;
                    case"Very Slow":veryslowspeed.setSelected(true);
                    break;
                    case"Medium":mediumspeed.setSelected(true);
                    break;
                    case"Fast":fastspeed.setSelected(true);
                    break;
                    case"Very Fast":veryfastspeed.setSelected(true);
                    break;
                }
                north.add(SPEED);
                //String currentspeed = (String) speed.getSelectedItem();
                SPEED.setBorder(BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED), "Speed"));
                
                //--Edit and Config Panel--------------------------------------------------------------------
                //------------------------------------------------------------------------------------------
                JPanel south = new JPanel();
                south.setLayout(new BorderLayout());
                
                //--Edit--------------------------------------------------------------------
                JPanel EDIT = new JPanel();
                EDIT.setBorder(BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED), "Edit"));
                EDIT.setLayout(new FlowLayout(FlowLayout.LEFT));
                EDIT.add(new JLabel("Edit: "));
                ButtonGroup editgroup = new ButtonGroup();
                JRadioButton enable = new JRadioButton("Enable");
                enable.addActionListener(new ActionListener(){
                    @Override
                        public void actionPerformed(ActionEvent ae) {
                            if(!manualSim){ manualSim = !manualSim;}
                        }
                    });
                JRadioButton disable = new JRadioButton("Disable");
                disable.addActionListener(new ActionListener(){
                    @Override
                        public void actionPerformed(ActionEvent ae) {
                            if(manualSim){ manualSim = !manualSim;}
                        }
                    });
                editgroup.add(enable);
                editgroup.add(disable);
                if(manualSim)
                    enable.setSelected(true);
                else
                    disable.setSelected(true);
                EDIT.add(enable); EDIT.add(disable);
                south.add(EDIT, BorderLayout.NORTH);
                
                
                //--Config--------------------------------------------------------------------
                JPanel CONFIG = new JPanel();
                CONFIG.setLayout(new FlowLayout(FlowLayout.LEFT));
                //--------------------CHECK REWRITTEN CODE--------------------------------------------
                JComboBox config2 = new JComboBox(); config2.setPreferredSize(new Dimension(180, 24));
                config2.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
                config2.addItem("Clear"); config2.addItem("10 cell row"); config2.addItem("Small Exploder");
                config2.addItem("Exploder"); config2.addItem("Glider"); config2.addItem("Lightweight Spaceship");
                config2.addItem("Tumbler"); config2.addItem("Gosper Glider Gun");config2.addItem("R-Pentamino");
                config2.setSelectedItem(config.getSelectedItem());
                config2.addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent ie) {
                        String whichconfig = (String) config2.getSelectedItem();
                        if(whichconfig.equals("Clear")){
                            Livecells.clear(); generation = 0; repaint(); gen_display.setText("Generation: " + generation);
                           // JOptionPane.showMessageDialog(null, "Grid cleared!");
                            config.setSelectedItem("Clear");
                        }
                        else if(whichconfig.equals("R-Pentamino")){
                    Livecells.clear();
                    int count = HALF_GRID*GRID_SIDE - (HALF_GRID);
                    Livecells.add(new intCell(count - GRID_SIDE));
                    Livecells.add(new intCell(count- (GRID_SIDE -1)));
                    Livecells.add(new intCell(count + GRID_SIDE));
                    Livecells.add(new intCell(count -1));
                    Livecells.add(new intCell(count));
                     generation = 0; repaint(); gen_display.setText("Generation: " + generation);
                     config.setSelectedItem("R-Pentamino");
                }
                        else if(whichconfig.equals("10 cell row")){
                            Livecells.clear();
                            int count = HALF_GRID*GRID_SIDE - (HALF_GRID + 5);
                            for(int i = 0; i < 10; i++) Livecells.add(new intCell(count++));
                            generation = 0; repaint(); gen_display.setText("Generation: " + generation);
                          //  JOptionPane.showMessageDialog(null, whichconfig + " configuration has been added to the center of the grid.\nPlease move to the center to view the simulation");
                            config.setSelectedItem("10 cell row");
                        }
                        else if(whichconfig.equals("Small Exploder")){
                            Livecells.clear();
                            int count = HALF_GRID*GRID_SIDE - (HALF_GRID);
                            for(int i = -2; i < 2; i++) if(i != 0)Livecells.add(new intCell(count + i*GRID_SIDE));
                            Livecells.add(new intCell(count - 1)); Livecells.add(new intCell(count + 1));
                            Livecells.add(new intCell(count - (GRID_SIDE + 1))); Livecells.add(new intCell(count - GRID_SIDE + 1));
                            generation = 0; repaint(); gen_display.setText("Generation: " + generation);
                          //  JOptionPane.showMessageDialog(null, whichconfig + " configuration has been added to the center of the grid.\nPlease move to the center to view the simulation");
                            config.setSelectedItem("Small Exploder");
                        }
                        else if(whichconfig.equals("Exploder")){
                            Livecells.clear();
                            int count = HALF_GRID*GRID_SIDE - (HALF_GRID + 2);
                            for(int i = -2; i < 3; i++) Livecells.add(new intCell(count + i*GRID_SIDE));
                            count += 4; for(int i = -2; i < 3; i++) Livecells.add(new intCell(count + i*GRID_SIDE));
                            count -= (2*GRID_SIDE + 2); Livecells.add(new intCell(count)); count += (4*GRID_SIDE); Livecells.add(new intCell(count));
                            generation = 0; repaint(); gen_display.setText("Generation: " + generation);
                          //  JOptionPane.showMessageDialog(null, whichconfig + " configuration has been added to the center of the grid.\nPlease move to the center to view the simulation");
                            config.setSelectedItem("Exploder");
                        }
                        else if(whichconfig.equals("Glider")){
                            Livecells.clear();
                            int count = HALF_GRID*GRID_SIDE - (HALF_GRID);
                            for(int i = -1; i < 2; i++) if(i != 0)Livecells.add(new intCell(count + i*GRID_SIDE));
                            Livecells.add(new intCell(count + 1)); Livecells.add(new intCell(count + GRID_SIDE - 1)); Livecells.add(new intCell(count + (GRID_SIDE + 1)));
                            generation = 0; repaint(); gen_display.setText("Generation: " + generation);
                          //  JOptionPane.showMessageDialog(null, whichconfig + " configuration has been added to the center of the grid.\nPlease move to the center to view the simulation");
                            config.setSelectedItem("Glider");
                        }
                        else if(whichconfig.equals("Lightweight Spaceship")){
                            Livecells.clear();
                            int count = HALF_GRID*GRID_SIDE - (HALF_GRID);
                            Livecells.add(new intCell(count - (2*GRID_SIDE + 1))); /*-*/ Livecells.add(new intCell(count - (2*GRID_SIDE)));
                            Livecells.add(new intCell(count - (2*GRID_SIDE) + 1)); Livecells.add(new intCell(count - (2*GRID_SIDE - 2)));
                            Livecells.add(new intCell(count - (GRID_SIDE + 2))); Livecells.add(new intCell(count - GRID_SIDE + 2));
                            Livecells.add(new intCell(count + 2)); Livecells.add(new intCell(count + (GRID_SIDE - 2)));
                            Livecells.add(new intCell(count + (GRID_SIDE + 1)));
                            generation = 0; repaint(); gen_display.setText("Generation: " + generation);
                        //    JOptionPane.showMessageDialog(null, whichconfig + " configuration has been added to the center of the grid.\nPlease move to the center to view the simulation");
                            config.setSelectedItem("Lightweight Spaceship");
                        }
                        else if(whichconfig.equals("Tumbler")){
                            Livecells.clear();
                            int count = HALF_GRID*GRID_SIDE - (HALF_GRID);
                            count -= (3*GRID_SIDE);
                            for(int i = 0; i < 5; i++) {Livecells.add(new intCell(count - 1 + i*GRID_SIDE)); Livecells.add(new intCell(count + 1 + i*GRID_SIDE));}
                            Livecells.add(new intCell(count - 2)); Livecells.add(new intCell(count + 2));
                            Livecells.add(new intCell(count + (GRID_SIDE - 2))); Livecells.add(new intCell(count + (GRID_SIDE + 2)));
                            count += 4*GRID_SIDE;
                            Livecells.add(new intCell(count - (GRID_SIDE + 3))); Livecells.add(new intCell(count - (GRID_SIDE - 3)));
                            Livecells.add(new intCell(count - 3)); Livecells.add(new intCell(count + 3));
                            Livecells.add(new intCell(count + (GRID_SIDE - 3))); Livecells.add(new intCell(count + (GRID_SIDE - 2)));
                            Livecells.add(new intCell(count + (GRID_SIDE + 2))); Livecells.add(new intCell(count + (GRID_SIDE + 3)));
                            generation = 0; repaint(); gen_display.setText("Generation: " + generation);
                        //    JOptionPane.showMessageDialog(null, whichconfig + " configuration has been added to the center of the grid.\nPlease move to the center to view the simulation");
                            config.setSelectedItem("Tumbler");
                         }
                        else if(whichconfig.equals("Gosper Glider Gun")){
                            Livecells.clear();
                            int count = HALF_GRID*GRID_SIDE - (HALF_GRID);
                            count -= 3*GRID_SIDE;
                            //EACH FIGURE IS NUMBERED BASED ON INITIAL POSITION FROM THE LEFT
                            //FIGURE 3:
                            Livecells.add(new intCell(count)); Livecells.add(new intCell(count - 2));
                            Livecells.add(new intCell(count - (GRID_SIDE + 1))); Livecells.add(new intCell(count - (GRID_SIDE + 2)));
                            Livecells.add(new intCell(count + (GRID_SIDE - 2)));
                            //FIGURE 2:
                            count -= (2*GRID_SIDE + 8);
                            Livecells.add(new intCell(count)); Livecells.add(new intCell(count - 2));
                            Livecells.add(new intCell(count - GRID_SIDE)); Livecells.add(new intCell(count - (GRID_SIDE + 1)));
                            Livecells.add(new intCell(count + (GRID_SIDE - 2))); Livecells.add(new intCell(count + (GRID_SIDE - 1)));
                            //FIGURE 1:
                            count -= 9;
                            Livecells.add(new intCell(count)); Livecells.add(new intCell(count - 1));
                            Livecells.add(new intCell(count - GRID_SIDE)); Livecells.add(new intCell(count - (GRID_SIDE + 1)));
                            //FIGURE 4:
                            count += 23; count -= 2*GRID_SIDE;
                            Livecells.add(new intCell(count)); Livecells.add(new intCell(count - 2));
                            Livecells.add(new intCell(count - GRID_SIDE)); Livecells.add(new intCell(count - (GRID_SIDE + 1)));
                            Livecells.add(new intCell(count + (GRID_SIDE - 2))); Livecells.add(new intCell(count + (GRID_SIDE - 1)));
                            //FIGURE 5:
                            count += 11*GRID_SIDE;
                            Livecells.add(new intCell(count)); Livecells.add(new intCell(count + 1));
                            Livecells.add(new intCell(count + 2)); Livecells.add(new intCell(count + GRID_SIDE));
                            Livecells.add(new intCell(count + (2*GRID_SIDE + 1)));
                            //FIGURE 7
                            count += 13; count -= 4*GRID_SIDE;
                            Livecells.add(new intCell(count)); Livecells.add(new intCell(count - 2));
                            Livecells.add(new intCell(count - (GRID_SIDE + 1))); Livecells.add(new intCell(count - (GRID_SIDE + 2)));
                            Livecells.add(new intCell(count + (GRID_SIDE - 2)));
                            //FIGURE 6
                            count -= (7*GRID_SIDE + 2);
                            Livecells.add(new intCell(count)); Livecells.add(new intCell(count - 1));
                            Livecells.add(new intCell(count - GRID_SIDE)); Livecells.add(new intCell(count - (GRID_SIDE + 1)));
                            generation = 0; repaint(); gen_display.setText("Generation: " + generation);
                         //   JOptionPane.showMessageDialog(null, whichconfig + " configuration has been added to the center of the grid.\nPlease move to the center to view the simulation");
                            config.setSelectedItem("Gosper Glider Gun");
                        }
                    }

                });
                CONFIG.add(config2);
                south.add(CONFIG);
                
                //--------------------------------------------------------------------
                settings.add(north, BorderLayout.NORTH);
                settings.add(south, BorderLayout.SOUTH);
                JOptionPane.showMessageDialog(null, settings, "Settings", JOptionPane.INFORMATION_MESSAGE);
                //settings.setPreferredSize(new Dimension(200, 200));
            }
            
        }
        rclick.add("Settings").addActionListener(new SettingsAction());
        settingsmenu.add("More Settings").addActionListener(new SettingsAction());
        
               rclick.add("Save").addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                try{
                int ok = fc.showSaveDialog(null);
                if(ok == JFileChooser.APPROVE_OPTION){
                    File f = fc.getSelectedFile();
                    try (BufferedWriter output = new BufferedWriter(new FileWriter(f))) {
                        String y = (String)config.getSelectedItem();
                        output.write(y);
                        output.newLine();
                        output.write(Integer.toString(Livecells.size()));
                        output.newLine();
                        for(int i =0;i<Livecells.size();i++){
                            System.out.println(Livecells.get(i));
                            String x = Livecells.get(i).toString();
                            output.write(x);
                            output.newLine();
                        }
                       // output.write("x");output.newLine();
                        output.write(Integer.toString(generation));
                        output.newLine();
                        String z = (String)speed.getSelectedItem();
                        output.write(z);
                        output.newLine();
                        String a = (String)size.getSelectedItem();
                        output.write(a);
                        output.newLine();
                        output.close();
                    }
                }
            }catch(Exception ex){
                ex.printStackTrace();
            }
            }
            
        });
     
                   rclick.add("Open").addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                          JFileChooser fc2 = new JFileChooser();
        fc2.setFileFilter(new FileNameExtensionFilter("txt Files","txt"));
                     try{
                int ok = fc2.showOpenDialog(null);
                if(ok == JFileChooser.APPROVE_OPTION){
                    //Livecells.clear();
                    File f = fc2.getSelectedFile();
                    Scanner infile = new Scanner(f);
                        config.setSelectedItem(infile.nextLine());
                        int x = Integer.parseInt(infile.nextLine());
                        for(int i =1;i<=x;i++){                                                       
                                Livecells.add(new intCell(Integer.parseInt(infile.nextLine())));
                                repaint();
                        }
                        
                        generation = Integer.parseInt(infile.nextLine());
                        speed.setSelectedItem(infile.nextLine());                                               
                        size.setSelectedItem(infile.nextLine());
                             infile.close();
                        
                        
                      //  input.readLine();
                       
                    
                }
            }catch(Exception ex){
                ex.printStackTrace();
            }
            }
        });
        //CREATING MENUBAR
        //-----------------------------------------------------------------------------------------------------
        menubar.add(config);
        menubar.add(next);
        menubar.add(start);
        menubar.add(speed);
        menubar.add(size);
        menubar.add(gen_display);
        //-----------------------------------------------------------------------------------------------------
        
        //AN ADDITIONAL BUTTON TO PRINT THE COUNTS OF ALL ALIVE CELLS (MAINLY FOR DEBUGGING PURPOSES...)
        //-----------------------------------------------------------------------------------------------------
//        JButton print = new JButton("Print");
//        print.addActionListener(new ActionListener(){
//            @Override
//            public void actionPerformed(ActionEvent ae) {
//                System.out.print("Livecells: ");
//                    for(int i = 0; i < Livecells.size(); i++){
//                        System.out.print(Livecells.get(i).getCounter() + " ");
//                    }
//            }
//            
//        });
//        menubar.add(print);
        //----------------------------------------------------------------------
        
        
        //----------------------------------------------------------------------        
        this.add(gamegrid, BorderLayout.CENTER);
        this.add(menubar, BorderLayout.SOUTH);
        this.setJMenuBar(hiddenmenu);
    }
    
    //CELLS THAT ARE ALIVE (For Array - to differenciate between object and index)
    class intCell{
        private final int counter;
        intCell (int i){counter = i;}
        int getCounter(){return counter;}
        public String toString(){
            String c = Integer.toString(counter);
            return c;
        }
    }
    
    
    //GRID OF LIFE
    //========================================================================================
    //========================================================================================
    
    class gridPanel extends JPanel{
    
            gridPanel(){
                
            }
    
            @Override
            public void paintComponent(Graphics g){
                super.paintComponent(g);
                setBackground(Color.darkGray);
                Graphics2D g2d = (Graphics2D) g;
                if(move){
                    int count = 0;
                    for(int j = -HALF_GRID; j < HALF_GRID; j++){
                        for(int i = -HALF_GRID; i < HALF_GRID; i++){
    //                        intCell temp = new intCell(count);
                            for(int k = 0; k < Livecells.size(); k++){                          
                                if(Livecells.get(k).getCounter() == count){                                      
                                        g2d.setColor(Color.red);
                                    g2d.fillRect(prevX + i*sq_side_length + (x2 - x), prevY + j*sq_side_length + (y2 - y), sq_side_length, sq_side_length);
                                }
                            }
                            g2d.setColor(Color.black);
                            g2d.drawRect(prevX + i*sq_side_length + (x2 - x), prevY + j*sq_side_length + (y2 - y), sq_side_length, sq_side_length);
                            count++;
                        }
                    }
                }
                else{
                    int count = 0;
                    for(int j = -HALF_GRID; j < HALF_GRID; j++){
                        for(int i = -HALF_GRID; i < HALF_GRID; i++){
    //                        intCell temp = new intCell(count);
                            for(int k = 0; k < Livecells.size(); k++){
                              
                                if(Livecells.get(k).getCounter() == count){
//                                          while(Livecells.get(k+1)!=null &&Livecells.get(k+1).getCounter() == (count+1)){
//                                    if(Livecells.get(k) == Livecells.get(k+1))
//                                        g2d.setColor(Color.blue);
//                                    else                                 
                              
                                        g2d.setColor(Color.red);
                                       // g2d.setColor(Color.red);
                               // }
                                 //   g2d.setColor(Color.red);
                                    g2d.fillRect(prevX + i*sq_side_length, prevY + j*sq_side_length, sq_side_length, sq_side_length);
                                }
                            }
                            g2d.setColor(Color.black);
                            g2d.drawRect(prevX + i*sq_side_length, prevY + j*sq_side_length, sq_side_length, sq_side_length);
                            count++;
                        }
                    }
                }
                
                
                //right_dist_travelled, top_dist_travelled, bottom_dist_travelled
                //g2d.draw(new Line2D.Double(0, 250, prevX + (x2 - x), 250));

            }
            
            @Override
            public Dimension getPreferredSize(){
                return new Dimension(600, 500);
            }
    //------------------------------------------------------------------------------------------------
    //ENDING PANEL
    }
    
//------------------------------------------------------------------------------------------------
//ENDING FRAME
}