import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.Scanner;


public class DodgeCanvas extends Canvas implements Runnable {

	// animation fields
	private Thread repaintReminder; 	// for animation
	private BufferedImage buffer;
	
	// actor fields
	private Direction newDir;
	private Player player;
	private Enemy[] eArr;
	private int numEnemies;
	private int health;
	private int score;
	private int bubble=100;
	private int actorSize=20;
	private boolean dead=false;
	private boolean gameStarted=false;
	public int initNumEnemies;
	public int initHealth;
	public int initScore;
	
	// display fields
	private int screenX;
	private int screenY;
	private int playX;
	private int playY;
	private int[] colorOffsets;
	
	// final fields
	private final int LEFT_MARGIN = 180;	// dimensions of bottom margin
	private final int OTHER_MARGINS = 40; 	// dimensions of left, up, right margins
	private final int GRID_DIM = 20; 		// width and height of a single cell
	
	public DodgeCanvas(int frameWidth, int frameHeight, int initHealthN, int initScoreN, int colRad, int numE) {
		//System.out.println("initializing canvas");	
		screenX = frameWidth;
		screenY = frameHeight;
//		playX = (int)((double)(screenX-LEFT_MARGIN)*initX/2.0);
//		playY = (int)((double)(screenX-LEFT_MARGIN)*initY/2.0);
		health = initHealthN;
		score = initScoreN;
		this.initHealth=initHealthN;
		this.initScore=initScoreN;
		bubble = colRad;
		numEnemies=numE;
		initNumEnemies=numE;
		// Key listener
		addKeyListener(new KeyListener() {			
				@Override
				public void keyPressed(KeyEvent e) {
					//System.out.println(e.getKeyChar() + "\t" + e.getKeyCode() + "\n");
					if (e.getKeyChar() == 'w') { player.setVel(new Direction(0,-1)); }	// 'up'
					if (e.getKeyChar() == 'd') { player.setVel(new Direction(1, 0)); }	// 'right'
					if (e.getKeyChar() == 's') { player.setVel(new Direction(0,1)); }	// 'down'
					if (e.getKeyChar() == 'a') { player.setVel(new Direction(-1,0)); }	// 'left'
					if (e.getKeyChar() == 'q') { player.setVel(new Direction(-1,-1)); }	// 'NW'
					if (e.getKeyChar() == 'e') { player.setVel(new Direction(1,-1)); }	// 'NE'
					if (e.getKeyChar() == 'c') { player.setVel(new Direction(1, 1)); }	// 'SE'
					if (e.getKeyChar() == 'z') { player.setVel(new Direction(-1,1)); }	// 'SW'
					if (e.getKeyChar() == 'x' || e.getKeyChar() == ' ') { player.setVel(new Direction(0, 0)); }	// 'stop'
					if (e.getKeyChar() == 'r'){
						if(player.getHealth()<=0){
							player.setReset(true);
						}
					}
				}

				@Override
				public void keyReleased(KeyEvent e) {
					
				}
				public void keyTyped(KeyEvent e) {

			}
					
		}); // end of key listener
		addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
				gameStarted=true;
				
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		//setEnemyNum(numEnemies);
		setP(new Player(screenX/2, screenY/2, screenX-OTHER_MARGINS, screenY-OTHER_MARGINS-LEFT_MARGIN, (double)(LEFT_MARGIN+OTHER_MARGINS), OTHER_MARGINS, health, score));
		genEnemies();
		
		colorOffsets = chooseColorScheme();
		repaintReminder = new Thread(this);
		repaintReminder.start();
	}

	/**
	 * generate a set of enemies
	 */
	private void genEnemies() {
		this.eArr=new Enemy[this.numEnemies];
		for(int i=0; i<numEnemies; i++){
			this.eArr[i]=new Enemy(Math.random()*screenX, Math.random()*screenY, screenX-OTHER_MARGINS, screenY-OTHER_MARGINS-LEFT_MARGIN, (double)(LEFT_MARGIN+OTHER_MARGINS), OTHER_MARGINS);
			this.eArr[i].getVel().randomize();
			this.eArr[i].reset();
		}
	}
	
	/**
	 * returns true if x1 and x2 are within BUBBLE of each other
	 * used for hit detection
	 * @param x1
	 * @param x2
	 * @return
	 */
	private boolean inArea(Actor a1, Actor a2){
		
		//double dist=Math.sqrt(Math.pow(a1.getX()+a2.getX(), 2)+Math.pow(a1.getY()+a2.getY(), 2));
		//System.out.println("checking closeness "+dist+" a1:"+a1.getX()+", "+a1.getY()+" a2 "+a2.getX()+", "+a2.getY());
		//System.out.println(dist);
		if(Math.abs(a1.getX()-a2.getX())<=bubble&&Math.abs(a1.getY()-a2.getY())<=bubble){
			return true;
			
		}
		return false;
	}
	
	/**
	 * commit actions for turn and reset enemies as needed. Increment score and health for player as required
	 */
	public void cycleTurn(){
		if(player.getHealth()>0&&gameStarted){
			player.move();
			if(player.getHealth()<=0){
				this.dead=true;
			}
			for(int i=0; i<eArr.length; i++){
				eArr[i].move();
				if(eArr[i].isOffScreen()){
					player.incScore();
					eArr[i].reset();
				}
				else if(inArea(player, eArr[i])){
					System.out.println("player hit!");
					player.damagePlayer(eArr[i].getDamage());
					eArr[i].reset();
				}
			}
			if(player.getScore()>0&&player.getScore()%100==0){
				setEnemyNum(numEnemies+5);
			}
		}
		else if(player.getReset()){
			player.setHealth(this.initHealth);
			player.setScore(this.initScore);
			player.setReset(false);
			this.numEnemies=this.initNumEnemies;
			player.setX(this.screenX/2);
			player.setY(this.screenY/2);
			eArr=null;
			genEnemies();
			this.dead=false;
			this.gameStarted=false;
		}
	}

	/**
	 * return array of enemies
	 * @return
	 */
	public Enemy[] getEArr() {
		return eArr;
	}

	/**
	 * introduce a new enemy array
	 * @param eArr
	 */
	public void setEArr(Enemy[] eArr) {
		this.eArr = eArr;
	}

	/**
	 * return the player
	 * @return
	 */
	public Player getP() {
		return player;
	}

	/**
	 * introduce new player
	 * @param p
	 */
	public void setP(Player newPlayer) {
		this.player = newPlayer;
	}

	/**
	 * get number of enemies
	 * @return
	 */
	public int getEnemyNum() {
		return numEnemies;
	}

	/**
	 * set new number of enemies and adds new enemies as needed
	 * @param numEnemies
	 */
	public void setEnemyNum(int enemyNumNew) {
		Enemy[] newArr=new Enemy[enemyNumNew];
		if(this.numEnemies<enemyNumNew){
			for(int i=0; i<this.numEnemies; i++){
				newArr[i]=eArr[i];
			}
			for(int i=this.numEnemies; i<enemyNumNew; i++){
				newArr[i]=new Enemy(Math.random()*screenX, Math.random()*screenY, screenX-OTHER_MARGINS, screenY-OTHER_MARGINS-LEFT_MARGIN, (double)(LEFT_MARGIN+OTHER_MARGINS), OTHER_MARGINS);
				newArr[i].reset();
			}
		}
		else{
			for(int i=0; i<enemyNumNew; i++){
				newArr[i]=this.eArr[i];
			}
		}
		this.numEnemies=enemyNumNew;
		eArr=newArr;
	} // end method setEnemyNum
	
	public void paintScreen(Graphics g){
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.BLACK);
		g2d.drawRect(0, 0, getWidth(), getHeight());
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // for smoother display
		
		// Draws inner border
		BasicStroke bs = new BasicStroke(3);
		g2d.setColor(new Color(colorOffsets[0]+100, colorOffsets[1]+100, colorOffsets[2]+100));
		g2d.setStroke(bs);
		g2d.drawRect(LEFT_MARGIN+OTHER_MARGINS-4, OTHER_MARGINS-4, (screenX-2*OTHER_MARGINS-LEFT_MARGIN)+8, (screenY-(6*OTHER_MARGINS))+8);
				
		// Draws outer border
		bs = new BasicStroke(6);
		g2d.setStroke(bs);
		g2d.drawRect(LEFT_MARGIN+OTHER_MARGINS-10, OTHER_MARGINS-10, (screenX-2*OTHER_MARGINS-LEFT_MARGIN)+21, (screenY-(6*OTHER_MARGINS))+21);
		
		/*
		 * Displays the colored border for the statistics
		 */
		g2d.setColor(new Color(colorOffsets[0]+100, colorOffsets[1]+100, colorOffsets[2]+100));
		// smaller lines
		bs = new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		g2d.setStroke(bs);
		g2d.drawLine(20, OTHER_MARGINS+20, 20, OTHER_MARGINS+(25*9));
		g2d.drawLine(LEFT_MARGIN+10, OTHER_MARGINS+20, LEFT_MARGIN+10, OTHER_MARGINS+(25*9));
		// strings
		String ins1 = "Instructions:";
		String ins2 = "Click to begin";
		String ins3 = "movement. Use";
		String ins4 = "wasd-qezc to";
		String ins5 = "avoid enemies.";
		String label = "Health/Score";
		String rest="Press r to restart";
		g2d.setFont(new Font("Times New Roman", Font.PLAIN, 22));
		g2d.drawString(ins1, (int)((LEFT_MARGIN+20)/2-((g2d.getFontMetrics().stringWidth(label))/2)), OTHER_MARGINS+50+(0)*g2d.getFontMetrics().getHeight());
		g2d.drawString(ins2, (int)((LEFT_MARGIN+20)/2-((g2d.getFontMetrics().stringWidth(label))/2)), OTHER_MARGINS+50+(1)*g2d.getFontMetrics().getHeight());
		g2d.drawString(ins3, (int)((LEFT_MARGIN+20)/2-((g2d.getFontMetrics().stringWidth(label))/2)), OTHER_MARGINS+50+(2)*g2d.getFontMetrics().getHeight());
		g2d.drawString(ins4, (int)((LEFT_MARGIN+20)/2-((g2d.getFontMetrics().stringWidth(label))/2)), OTHER_MARGINS+50+(3)*g2d.getFontMetrics().getHeight());
		g2d.drawString(ins5, (int)((LEFT_MARGIN+20)/2-((g2d.getFontMetrics().stringWidth(label))/2)), OTHER_MARGINS+50+(4)*g2d.getFontMetrics().getHeight());
		g2d.drawString(label, (int)((LEFT_MARGIN+30)/2-((g2d.getFontMetrics().stringWidth(label))/2)), OTHER_MARGINS+50+(6)*g2d.getFontMetrics().getHeight());
		
		String nums=player.getHealth()+"/"+player.getScore();
		g2d.drawString(nums, (int)((LEFT_MARGIN+30)/2-((g2d.getFontMetrics().stringWidth(nums))/2)), OTHER_MARGINS+50+(7)*g2d.getFontMetrics().getHeight());
		if(player.getHealth()<=0){
			String dead="You are dead!";
			g2d.drawString(dead, (int)((LEFT_MARGIN+30)/2-((g2d.getFontMetrics().stringWidth(dead))/2)), (int)(OTHER_MARGINS+50+(9)*g2d.getFontMetrics().getHeight()));
			g2d.drawString(rest, (int)((LEFT_MARGIN+30)/2-((g2d.getFontMetrics().stringWidth(rest))/2)), OTHER_MARGINS+50+(10)*g2d.getFontMetrics().getHeight());
		}
		// bigger lines
		bs = new BasicStroke(6, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		g2d.setStroke(bs);
		g2d.drawLine(20+8, OTHER_MARGINS+20, 20+8, OTHER_MARGINS+(25*9));
		g2d.drawLine(LEFT_MARGIN+10-8, OTHER_MARGINS+20, LEFT_MARGIN+10-8, OTHER_MARGINS+(25*9));
	}
	
	public void update(Graphics g){
		paint(g);
	}

	public void paint(Graphics g) {
		//System.out.println("in paint method");
		//if(buffer==null || buffer.getWidth(null)!=getWidth()||buffer.getHeight(null)!=getHeight()){
			buffer=(BufferedImage) createImage(getWidth(), getHeight());
		//}
		paintScreen(buffer.getGraphics());
		Graphics2D g2=(Graphics2D)g;
		BufferedImage withActors=buffer;
		paintActors(withActors.getGraphics());
		
		g2.drawImage(withActors, 0, 0, null);
	}
	
	private void paintActors(Graphics graphics) {
		Graphics2D g2=(Graphics2D)graphics;
		g2.setColor(new Color(255, 255, 255));
		Ellipse2D.Double circle = new Ellipse2D.Double((int)player.getX()-(actorSize/2), (int)player.getY()-(actorSize/2), actorSize*1.2, actorSize*1.2);
		g2.fill(circle);
//		g2.drawRect((int)player.getX()-(actorSize/2), (int)player.getY()-(actorSize/2), actorSize, actorSize);
		g2.setColor(new Color(colorOffsets[0]+100, colorOffsets[1]+100, colorOffsets[2]+100));
		BasicStroke bs = new BasicStroke(3);
		g2.setStroke(bs);
		if(gameStarted){
			for(int i=0; i<numEnemies; i++){
				g2.drawRect((int)eArr[i].getX()-(actorSize/2), (int)eArr[i].getY()-(actorSize/2), actorSize, actorSize);
			}
		}
		
	}

	/**
	 * Method to randomly choose a color scheme for the screen borders
	 * (not important, just had it left over)
	 * @return
	 */
	public int[] chooseColorScheme() {
		int num = (int)(Math.random()*7)+1;
		int[] offsets = new int[3];
		switch (num) {
		case 1:
			offsets = new int[]{55, 55, 55}; 	// grayscale
			break;
		case 2:
			offsets = new int[]{0, 55, 55};		// teal
			break;
		case 3:
			offsets = new int[]{55, 0, 55};		// magenta
			break;
		case 4:
			offsets = new int[]{55, 55, 0};		// yellow/tan
			break;
		case 5:
			offsets = new int[]{0, 0, 55};		// blue
			break;
		case 6:
			offsets = new int[]{55, 0, 0};		// red
			break;
		case 7:
			offsets = new int[]{0, 55, 0};		// green
			break;
		}
		return offsets;
	} // end method chooseColorScheme
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (repaintReminder == Thread.currentThread()) {
			//System.out.println(player.getHealth()+", "+player.getScore()+", "+numEnemies);
			repaint();
			cycleTurn();
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	} // end run()
	
}
