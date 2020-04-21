import java.awt.Graphics;
import java.awt.Color;
import java.awt.image.*;
import java.awt.Font;
import javax.swing.*;
import java.awt.event.*;
import java.awt.Polygon;
import java.awt.Graphics2D;
import java.io.*;
import java.util.Scanner;
import java.util.*;
import javax.imageio.ImageIO;
import javax.sound.sampled.*;
public class MazeProgram extends JPanel implements KeyListener,MouseListener
{
	//THREE ADDONS - GRADIENT WALLS, MUSIC, FLASHLIGHT
	JFrame frame;
	//declare an array to store the maze
	static int counter = 0;
	static Boolean won = false;
	static int x = 8;
	static int y = 0;
	static int a,b,c,d;
	static int checkStepsOne,checkStepsTwo,checkStepsThree,checkStepsZero,finalCheckSteps;
	static int direction = 1;
	static Boolean bool = false;
	static Boolean flashlightBool = false;
	static int xVar,yVar,gr,grr;
	static int stepsAhead = 2;
	Ceiling ceiling1;
	ArrayList<String[]> maze = new ArrayList<>();
	ArrayList<Ceiling> wallList = new ArrayList<>();
	ArrayList<Ceiling> trapList = new ArrayList<>();
	ArrayList<Ceiling> backwall = new ArrayList<>();
	BufferedImage navigateUp, navigateDown, navigateRight, navigateLeft; 
	public MazeProgram()
	{
		
		try
		{
			navigateUp = ImageIO.read(new File("images/navigateUp.gif"));
			navigateDown = ImageIO.read(new File("images/navigateDown.gif"));
			navigateRight = ImageIO.read(new File("images/navigateRight.gif"));
			navigateLeft = ImageIO.read(new File("images/navigateLeft.gif"));


		}catch(IOException e)
		{};
		setBoard();
		frame = new JFrame();
		frame.add(this);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1410,1000);
		frame.setVisible(true);
		frame.addKeyListener(this);
		music();
	}
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2=(Graphics2D)g;
		g.setColor(Color.BLACK);	//this will set the background color
		g.fillRect(0,0,2000,1000);

		for(Ceiling cg:wallList){
			g2.setPaint(cg.getPaint());
			g2.fill(cg.getPoly());
		}
		

		for(Ceiling tr:trapList){
			g2.setPaint(tr.getPaint());
			g2.fill(tr.getPoly());
			g2.setColor(Color.BLACK);
			g2.draw(tr.getPoly());
			
		}
		for(Ceiling bk:backwall){
			if(flashlightBool == false){
				g2.setColor(new Color(224,224,224));//GRAY
				g2.fill(bk.getRect());
			}
			else if(bk.getBool() == false){
				g2.setColor(new Color(224,224,224));//GRAY
				g2.fill(bk.getRect());

			}
			else{
				g2.setColor(new Color(0,76,153));//DARK BLUE
				g2.fill(bk.getRect());

			}
		}
		if(x == 5 && y>28){
			g2.setColor(Color.BLUE);
			g2.setFont(new Font("SnellRoundhand", Font.PLAIN, ((int)(y*1.2))) ); 
			g2.drawString("Finish Line!", 602, 300);
		}
		g2.setColor(Color.BLACK);
		g2.fillRect(0,0,360,150);


		


	
		for(int i = 0; i<maze.size(); i++){
			for(int j = 0; j<maze.get(i).length;j++){
		
				if (maze.get(i)[j].equals("*")){
					g.setColor(new Color(0,76,153));
					g.drawRect(j*10,i*10,10,10);
					g.fillRect(j*10,i*10,10,10);
				}
				if(maze.get(i)[j].equals("S")){
					g.setColor(Color.GREEN);
					g.drawRect(j*10,i*10,10,10);
					g.fillRect(j*10,i*10,10,10);
					
				}
				if(maze.get(i)[j].equals("F")){
					g.setColor(Color.RED);
					g.drawRect(j*10,i*10,10,10);
					g.fillRect(j*10,i*10,10,10);
				}
					

			}
		}

		g.setFont(new Font("SnellRoundhand", Font.PLAIN, 25)); 		
		g.setColor(Color.WHITE);
		g.drawString("COUNTER: "+ Integer.toString(counter),650,780);
		g.drawString("Ice Castle Maze by Jasmine: ",30,20);
		g.setFont(new Font("Bodoni", Font.PLAIN, 18));
		g.drawString("Crank up the volume for some hiphop jingles...",520,820);


		if(won == true){
		g.setFont(new Font("Bodoni", Font.PLAIN, 30)); 
		g.setColor(new Color(205,127,239));
		g.drawString("You Finally Got it! You were a little lost, but you made it out in " + Integer.toString(counter)+ " turns.",185,350);	
		g.drawString("To Play Again Press the Space Bar",500,450);	
		}

		
		if(direction == 0){
			g.drawImage(navigateUp,10*y,10*x,10,10,this);
		}
		else if(direction == 1){
			g.drawImage(navigateRight,10*y,10*x,10,10,this);
		}
		else if(direction == 2){
			g.drawImage(navigateDown,10*y,10*x,10,10,this);
		}
		else if(direction == 3){
			g.drawImage(navigateLeft,10*y,10*x,10,10,this);
		}

		if(flashlightBool == false){
		g.setColor(new Color(0,0,0,240));
		g.fillRect(0,0,2000,1000);
		g.setFont(new Font("TimesRoman", Font.PLAIN, 20)); 		
		g.setColor(Color.YELLOW);
		g.drawString("You have a flashlight in your pack. Toggle W and S key to Use.",440,800);

	}

	}
	public void setBoard()
	{
		//choose your maze design

		//pre-fill maze array here

		File name = new File("maze.txt");
		int r=0;
		try
		{
			
			BufferedReader inputt = new BufferedReader(new FileReader(name));
			String text;
			while( (text=inputt.readLine())!= null)
			{
				maze.add(text.split(""));
			}
						
		}
		catch (IOException io)
		{
			System.err.println("File error");
		}

		setWalls();
	}

	public void setWalls()
	{
		xVar = 50;
		yVar = 150;


		//Check Steps function based on direction
		if(direction == 0){ // Up Check Steps Zero
			checkStepsZero = 0;
			for(int a = 1; a<stepsAhead;a++){
				if(maze.get(x-a)[y].equals(" ")){
					checkStepsZero++;
				}
				else if(maze.get(x-a)[y].equals("*")){
					break;
				}

			}
		}
		else if(direction == 1){ // Right Check Steps One
			checkStepsOne = 0;
			for(int a = 1; a<stepsAhead;a++){
				if(maze.get(x)[y+a].equals(" ") || maze.get(x)[y+a].equals("F")){
					checkStepsOne++;
				}
				else if(maze.get(x)[y+a].equals("*")){
					break;
				}

			}
		}
		else if(direction == 2){ //Down Check Steps Two
			checkStepsTwo = 0;
			for(int a = 1; a<stepsAhead;a++){
				if(maze.get(x+a)[y].equals(" ")){
					checkStepsTwo++;
				}
				else if(maze.get(x+a)[y].equals("*")){
					break;
				}

			}
		}
		else if(direction == 3){ //Left Check Steps Three
			checkStepsThree = 0;
			for(int a = 1; a<stepsAhead;a++){
				if(maze.get(x)[y-a].equals(" ")){
					checkStepsThree++;
				}
				else if(maze.get(x)[y-a].equals("*")){
					break;
				}

			}
		}
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		finalCheckSteps = 0;
		if(direction == 0)
			finalCheckSteps = checkStepsZero;
		else if(direction == 1)
			finalCheckSteps = checkStepsOne;
		else if(direction == 2)
			finalCheckSteps = checkStepsTwo;
		else if(direction == 3)
			finalCheckSteps = checkStepsThree;

//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		for(int i = 0;i<finalCheckSteps+1;i++){ //LEFT WALL 
				
				int[] c1X={20,750,750,20};
				int[] c1Y={yVar,yVar,0,0};
				for(int j = 0; j<4;j++){
					if(j == 0 || j == 3){
						c1X[j]+=xVar*i;
						c1Y[j]+=yVar*i;

					}
					else if(j==1 || j ==2){
						c1X[j]-=xVar*i;
						c1Y[j]+=yVar*i;
					}
					

				}
				wallList.add(new Ceiling(c1X,c1Y,255,153,255));
				a = c1X[0];
				b = c1Y[0];
				c = c1X[1];
			}

		for(int i = 0;i<finalCheckSteps+1;i++){ //RIGHT WALL 
		
		int[] c1X={20,750,750,20};
		int[] c1Y={1400-yVar,1400-yVar,1400,1400};
		for(int j = 0; j<4;j++){
				if(j == 0 || j == 3){
					c1X[j]+=xVar*i;
					c1Y[j]-=yVar*i;

				}
				else if(j==1 || j ==2){
					c1X[j]-=xVar*i;
					c1Y[j]-=yVar*i;
				}
				

				
			}
		wallList.add(new Ceiling(c1X,c1Y,255,153,255));
		d = c1Y[0];
		}


		backwall = new ArrayList<>();
		if(finalCheckSteps == stepsAhead-1){
			backwall.add(new Ceiling(b,a,(d-b),(c-a),true));
		}
		else if(finalCheckSteps<stepsAhead-1){
			backwall.add(new Ceiling(b,a,(d-b),(c-a),false));
		}

		gr = 50;
		grr =25;
		for(int i = 0;i<finalCheckSteps+1;i++){ //LEFT WALL Trapezoids
			int[] c1X={20,750,800,-30};
			int[] c1Y={yVar,yVar,0,0};
			if(direction == 0){ //
				if(maze.get(x-i)[y-1].equals("*")){ // DIRECTION ZERO TRAPSSSS
					for(int j = 0; j<4;j++){
						if(j == 0 || j == 3){
							c1X[j]+=xVar*i;
							c1Y[j]+=yVar*i;

						}
						else if(j==1 || j == 2){
							c1X[j]-=xVar*i;
							c1Y[j]+=yVar*i;
						}
					}	
					trapList.add(new Ceiling(c1X,c1Y,204-(gr*i),229-(grr*i),255));
				}	
			}
			else if(direction == 1){ // 
				if(maze.get(x-1)[y+i].equals("*")){ // DIRECTION ONE TRAPSSSS
					for(int j = 0; j<4;j++){
						if(j == 0 || j == 3){
							c1X[j]+=xVar*i;
							c1Y[j]+=yVar*i;

						}
						else if(j==1 || j == 2){
							c1X[j]-=xVar*i;
							c1Y[j]+=yVar*i;
						}
					}	
					trapList.add(new Ceiling(c1X,c1Y,204-(gr*i),229-(grr*i),255));
				}	
			}
			else if(direction == 2){
				if(maze.get(x+i)[y+1].equals("*")){ // DIRECTION TWO TRAPSSSS
					for(int j = 0; j<4;j++){
						if(j == 0 || j == 3){
							c1X[j]+=xVar*i;
							c1Y[j]+=yVar*i;

						}
						else if(j==1 || j == 2){
							c1X[j]-=xVar*i;
							c1Y[j]+=yVar*i;
						}
					}	
					trapList.add(new Ceiling(c1X,c1Y,204-(gr*i),229-(grr*i),255));
				}	
			}
			else if(direction == 3){
				if(maze.get(x+1)[y-i].equals("*")){ // DIRECTION THREE TRAPSSSS
					for(int j = 0; j<4;j++){
						if(j == 0 || j == 3){
							c1X[j]+=xVar*i;
							c1Y[j]+=yVar*i;

						}
						else if(j==1 || j == 2){
							c1X[j]-=xVar*i;
							c1Y[j]+=yVar*i;
						}
					}	
					trapList.add(new Ceiling(c1X,c1Y,204-(gr*i),229-(grr*i),255));
				}	
			}
		}

//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		for(int i = 0;i<finalCheckSteps+1;i++){ //RIGHT WALL Trapezoids
			int[] c1X={20,750,800,-30};
			int[] c1Y={1400-yVar,1400-yVar,1400,1400};
			if(direction == 0){ //
				if(maze.get(x-i)[y+1].equals("*")){ // DIRECTION ZERO TRAPSSSS
					for(int j = 0; j<4;j++){
						if(j == 0 || j == 3){
							c1X[j]+=xVar*i;
							c1Y[j]-=yVar*i;

						}
						else if(j==1 || j == 2){
							c1X[j]-=xVar*i;
							c1Y[j]-=yVar*i;
						}
					}	
					trapList.add(new Ceiling(c1X,c1Y,204-(gr*i),229-(grr*i),255));
				}	
			}
			else if(direction == 1){ // 
				if(maze.get(x+1)[y+i].equals("*")){ // DIRECTION ONE TRAPSSSS
					for(int j = 0; j<4;j++){
						if(j == 0 || j == 3){
							c1X[j]+=xVar*i;
							c1Y[j]-=yVar*i;

						}
						else if(j==1 || j == 2){
							c1X[j]-=xVar*i;
							c1Y[j]-=yVar*i;
						}
					}	
					trapList.add(new Ceiling(c1X,c1Y,204-(gr*i),229-(grr*i),255));
				}	
			}
			else if(direction == 2){
				if(maze.get(x+i)[y-1].equals("*")){ // DIRECTION TWO TRAPSSSS
					for(int j = 0; j<4;j++){
						if(j == 0 || j == 3){
							c1X[j]+=xVar*i;
							c1Y[j]-=yVar*i;

						}
						else if(j==1 || j == 2){
							c1X[j]-=xVar*i;
							c1Y[j]-=yVar*i;
						}
					}	
					trapList.add(new Ceiling(c1X,c1Y,204-(gr*i),229-(grr*i),255));
				}	
			}
			else if(direction == 3){
				if(maze.get(x-1)[y-i].equals("*")){ // DIRECTION THREE TRAPSSSS
					for(int j = 0; j<4;j++){
						if(j == 0 || j == 3){
							c1X[j]+=xVar*i;
							c1Y[j]-=yVar*i;

						}
						else if(j==1 || j == 2){
							c1X[j]-=xVar*i;
							c1Y[j]-=yVar*i;
						}
					}	
					trapList.add(new Ceiling(c1X,c1Y,204-(gr*i),229-(grr*i),255));
				}	
			}
		}
	}
	public void music(){
		try{
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(MazeProgram.class.getResource("sound/song.wav"));
			Clip clip = AudioSystem.getClip();
			clip.open(audioIn);
			clip.start();
		}catch(Exception ex){
			System.out.println("Error with playing sound");
			ex.printStackTrace();
		}
		
	}


	public void keyPressed(KeyEvent e)
	{		
		if(won == false){

			if(e.getKeyCode() == (39)){ //right
				if(direction == 3){
					direction=0;
					counter++;
					setWalls();
					repaint();
				}
				else{
					direction++;
					counter++;
					setWalls();
					repaint();
				}
			}
			else if(e.getKeyCode() == (37)){//left
				if(direction == 0){
					direction=3;
					counter++;
					setWalls();
					repaint();
				}
				else{
					direction--;
					counter++;
					setWalls();
					repaint();
				}
			}
			if(e.getKeyCode() == (38)){ //up

				if(direction == 0 && maze.get(x-1)[y].equals(" ")){
					x--;
					counter++;
				}
				else if(direction == 1 && maze.get(x)[y+1].equals("F")){
					won = true;
					y++;
					counter++;		
				
				}
				else if(direction == 1 && maze.get(x)[y+1].equals(" ")){
					y++;
					counter++;
				}	
				else if(direction == 2 && maze.get(x+1)[y].equals(" ")){
					x++;
					counter++;
				}
				else if(direction == 3 && maze.get(x)[y-1].equals(" ")){
					y--;
					counter++;
				}
				trapList = new ArrayList<>();
				setWalls();
				repaint();
			}

			if(e.getKeyCode() == (87)){
				flashlightBool = true;
				stepsAhead = 4;
				setWalls();
				repaint();
			}
			if(e.getKeyCode() == (83)){
				flashlightBool = false;
				stepsAhead = 2;
				setWalls();
				repaint();
			}

		}
			else{
			if(e.getKeyCode() == (32)){ //space
				counter = 0;
				x = 8;
				y = 0;
				won = false;
				setWalls();
				repaint();
				}
			}
	}
	public void keyReleased(KeyEvent e)
	{

	}
	public void keyTyped(KeyEvent e)
	{
	}
	public void mouseClicked(MouseEvent e)
	{
	}
	public void mousePressed(MouseEvent e)
	{
	}
	public void mouseReleased(MouseEvent e)
	{
	}
	public void mouseEntered(MouseEvent e)
	{
	}
	public void mouseExited(MouseEvent e)
	{
	}
	
	public static void main(String args[])
	{
		MazeProgram app=new MazeProgram();
	}
}