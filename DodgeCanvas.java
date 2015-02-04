import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;


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
	private int bubble;
	
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
	
	public DodgeCanvas(int frameWidth, int frameHeight, int initHealth, int initScore, int colRad, int numE, double initX, double initY) {
		System.out.println("initializing canvas");
		screenX = frameWidth;
		screenY = frameHeight;
		playX = (int)((double)(screenX-LEFT_MARGIN)*initX/2.0);
		playY = (int)((double)(screenX-LEFT_MARGIN)*initY/2.0);
		health = initHealth;
		score = initScore;
		bubble = colRad;
		numEnemies=numE;
		// Key listener
		addKeyListener(new KeyListener() {			
				@Override
				public void keyPressed(KeyEvent e) {
					System.out.println(e.getKeyChar() + "\t" + e.getKeyCode() + "\n");
					if (e.getKeyChar() == 'w') { player.setVel(new Direction(0,-1)); }	// 'up'
					if (e.getKeyChar() == 'd') { player.setVel(new Direction(1, 0)); }	// 'right'
					if (e.getKeyChar() == 's') { player.setVel(new Direction(0,1)); }	// 'down'
					if (e.getKeyChar() == 'a') { player.setVel(new Direction(-1,0)); }	// 'left'
					if (e.getKeyChar() == 'q') { player.setVel(new Direction(-1,-1)); }	// 'NW'
					if (e.getKeyChar() == 'e') { player.setVel(new Direction(1,-1)); }	// 'NE'
					if (e.getKeyChar() == 'c') { player.setVel(new Direction(1, 1)); }	// 'SE'
					if (e.getKeyChar() == 'z') { player.setVel(new Direction(-1,1)); }	// 'SW'
					if (e.getKeyChar() == 'x' || e.getKeyChar() == ' ') { player.setVel(new Direction(0, 0)); }	// 'stop'
				}

				@Override
				public void keyReleased(KeyEvent e) {
					
				}
				public void keyTyped(KeyEvent e) {

			}
					
		}); // end of key listener
		
		//setEnemyNum(numEnemies);
		setP(new Player(screenX/2, screenY/2, screenX, screenY, health, score));
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
			this.eArr[i]=new Enemy(Math.random()*screenX, Math.random()*screenY, screenX, screenY);
			this.eArr[i].getVel().randomize();
		}
	}
	
	/**
	 * returns true if x1 and x2 are within BUBBLE of each other
	 * used for hit detection
	 * @param x1
	 * @param x2
	 * @return
	 */
	private boolean inArea(double x1, double x2){
		if(Math.abs(x1-x2) <= bubble){
			return true;
		}
		return false;
	}
	
	/**
	 * commit actions for turn and reset enemies as needed. Increment score and health for player as required
	 */
	public void cycleTurn(){
		player.move();
		for(int i=0; i<eArr.length; i++){
			eArr[i].move();
			if(eArr[i].isOffScreen()){
				player.incScore();
				eArr[i].reset();
			}
			else if(inArea(eArr[i].getX(), player.getX())&&inArea(eArr[i].getY(), player.getY())){
				player.damagePlayer(eArr[i].getDamage());
				eArr[i].reset();
			}
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
				newArr[i]=new Enemy(Math.random()*screenX, Math.random()*screenY, screenX, screenY);
				newArr[i].getVel().randomize();
			}
		}
		else{
			for(int i=0; i<enemyNumNew; i++){
				newArr[i]=this.eArr[i];
			}
		}
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
		bs = new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		g2d.setStroke(bs);
		g2d.drawLine(screenX/2-50, OTHER_MARGINS+50, screenX/2-50, OTHER_MARGINS+(25*5));
		g2d.drawLine(screenX/2+50, OTHER_MARGINS+50, screenX/2+50, OTHER_MARGINS+(25*5));
		bs = new BasicStroke(6, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		g2d.setStroke(bs);
		g2d.drawLine(screenX/2-60, OTHER_MARGINS+50, screenX/2-60, OTHER_MARGINS+(25*5));
		g2d.drawLine(screenX/2+60, OTHER_MARGINS+50, screenX/2+60, OTHER_MARGINS+(25*5));
	}
	
	public void update(Graphics g){
		paint(g);
	}

	public void paint(Graphics g) {
		System.out.println("in paint method");
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
		g2.setColor(new Color(0f, 1f, 0f));
		g2.drawRect((int)player.getX(), (int)player.getY(), 20, 20);
		g2.setColor(new Color(1f, 0f, 0f));
		for(int i=0; i<numEnemies; i++){
			g2.drawRect((int)eArr[i].getX(), (int)eArr[i].getY(), 20, 20);
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
			System.out.println("Running thread");
			repaint();
			cycleTurn();
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	} // end run()
	
}
