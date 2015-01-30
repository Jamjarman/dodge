
public class Game {
	
	private Player p;
	private Enemy[] eArr;
	private int enemyNum;
	private int screenX;
	private int screenY;
	private static double BUBBLE=3;
	
	/**
	 * take vars to create enemies and player
	 * @param xS
	 * @param yS
	 * @param h
	 * @param s
	 * @param e
	 */
	public Game(int xS, int yS, int h, int s, int e){
		screenX=xS;
		screenY=yS;
		setEnemyNum(e);
		setP(new Player(xS/2, yS/2, screenX, screenY, h, s));
		generateE();
	}

	/**
	 * generate a set of enemies
	 */
	private void generateE() {
		this.eArr=new Enemy[this.enemyNum];
		for(int i=0; i<enemyNum; i++){
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
		if(Math.abs(x1-x2)<=BUBBLE){
			return true;
		}
		return false;
	}
	
	/**
	 * commit actions for turn and reset enemies as needed. Increment score and health for player as required
	 */
	public void cycleTurn(){
		p.move();
		for(int i=0; i<eArr.length; i++){
			eArr[i].move();
			if(eArr[i].isOffScreen()){
				p.incScore();
				eArr[i].reset();
			}
			else if(inArea(eArr[i].getX(), p.getX())&&inArea(eArr[i].getY(), p.getY())){
				p.damagePlayer(eArr[i].getDamage());
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
		return p;
	}

	/**
	 * introduce new player
	 * @param p
	 */
	public void setP(Player p) {
		this.p = p;
	}

	/**
	 * get number of enemies
	 * @return
	 */
	public int getEnemyNum() {
		return enemyNum;
	}

	/**
	 * set new number of enemies and adds new enemies as needed
	 * @param enemyNum
	 */
	public void setEnemyNum(int enemyNumNew) {
		Enemy[] newArr=new Enemy[enemyNumNew];
		if(this.enemyNum<enemyNumNew){
			for(int i=0; i<this.enemyNum; i++){
				newArr[i]=eArr[i];
			}
			for(int i=this.enemyNum; i<enemyNumNew; i++){
				newArr[i]=new Enemy(Math.random()*screenX, Math.random()*screenY, screenX, screenY);
				newArr[i].getVel().randomize();
			}
		}
		else{
			for(int i=0; i<enemyNumNew; i++){
				newArr[i]=this.eArr[i];
			}
		}
		
	}

}
