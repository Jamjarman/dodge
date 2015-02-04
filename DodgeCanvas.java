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
	private int bubble=40;
	private int actorSize=20;
	private boolean dead=false;
	
	// display fields
	private int screenX, screenY;			// actual screen dimensions
	private int playX, playY;				// margins for playable screen
	private int[] colorOffsets;
	private int minX, minY, maxX, maxY;		// minimum and maximum boundaries for actors
	
	// final fields
	private final int LEFT_MARGIN = 180;	// dimensions of bottom margin
	private final int OTHER_MARGINS = 40; 	// dimensions of left, up, right margins
	private final int GRID_DIM = 20; 		// width and height of a single cell
	
	public DodgeCanvas(int frameWidth, int frameHeight, int initHealth, int initScore, int colRad, int numE, double initX, double initY) {
		//System.out.println("initializing canvas");
		screenX = frameWidth;
		screenY = frameHeight;
		playX = (int)((double)(screenX-LEFT_MARGIN)*(1-initX)/2.0);
		playY = (int)((double)screenY*(1-initY)/2.0);
		setMinMaxes();
		System.out.println("PlayY = "+playY);
		System.out.println("MaxY = "+maxY);
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
		setP(new Player(screenX/2, screenY/2, screenX, screenY, health, score, minX, maxX));
		genEnemies();
		
		colorOffsets = new int[]{255, 255, 255};
		repaintReminder = new Thread(this);
		repaintReminder.start();
	} // end of constructor
	
	private void setMinMaxes() {
		minX = LEFT_MARGIN+playX;
		minY = playY;
		maxX = screenX-playX;
		maxY = screenY-(playY*2);
	}

	
	
	/**
	 * generate a set of enemies
	 */
	private void genEnemies() {
		this.eArr=new Enemy[this.numEnemies];
		for(int i=0; i<numEnemies; i++){
			this.eArr[i]=new Enemy(Math.random()*screenX, Math.random()*screenY, screenX, screenY, minX, maxX);
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
	private boolean inArea(Actor a1, Actor a2){
		double dist=Math.sqrt(Math.pow(a1.getX()+a2.getX(), 2)+Math.pow(a1.getY()+a2.getY(), 2));
		if(dist<=bubble){
			return true;
		}
		return false;
	}
	
	/**
	 * commit actions for turn and reset enemies as needed. Increment score and health for player as required
	 */
	public void cycleTurn(){
		player.move();
		if(player.getHealth()<=0){
			dead=true;
		}
		for(int i=0; i<eArr.length; i++){
			eArr[i].move();
			if(eArr[i].isOffScreen()){
				player.incScore();
				eArr[i].reset();
			}
			else if(inArea(player, eArr[i])&&inArea(player, eArr[i])){
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
				newArr[i]=new Enemy(Math.random()*screenX, Math.random()*screenY, screenX, screenY, minX, maxX);
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
		g2d.setColor(new Color(colorOffsets[0], colorOffsets[1], colorOffsets[2]));
		g2d.setStroke(bs);
		g2d.drawRect(minX, minY, maxX-minX, maxY-minY);
				
		// Draws outer border
		bs = new BasicStroke(6);
		g2d.setStroke(bs);
		g2d.drawRect(minX-10, minY-10, maxX-minX+20, maxY-minY+20);
		
		/*
		 * Displays the colored border for the statistics
		 */
		g2d.setColor(new Color(colorOffsets[0], colorOffsets[1], colorOffsets[2]));
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
		g2.setColor(new Color(0f, 1f, 0f));
		g2.drawRect((int)player.getX()-(actorSize/2), (int)player.getY()-(actorSize/2), actorSize, actorSize);
		g2.setColor(new Color(1f, 0f, 0f));
		for(int i=0; i<numEnemies; i++){
			g2.drawRect((int)eArr[i].getX()-(actorSize/2), (int)eArr[i].getY()-(actorSize/2), actorSize, actorSize);
		}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (repaintReminder == Thread.currentThread()) {
			System.out.println(player.getHealth()+", "+player.getScore());
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
