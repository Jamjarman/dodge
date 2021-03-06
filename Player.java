
public class Player extends Actor {

	private int health;
	private int score;
	private boolean reset;
	
	/**
	 * generate a new player character based on actor super class adding health and score int fields
	 * @param x
	 * @param y
	 * @param h
	 * @param s
	 */
	public Player(double x, double y, double scX, double scY, double minX, double minY, int h, int s) {
		super(x, y, scX, scY, minX, minY);
		health=h;
		score=s;
		reset=false;
	}
	
	/**
	 * return score
	 * @return
	 */
	public int getScore(){
		return score;
	}
	
	/**
	 * reset score to an inputed int
	 * @param s
	 */
	public void setScore(int s){
		score=s;
	}
	
	/**
	 * increment score by 1, useful for missiles leaving screen
	 */
	public void incScore(){
		score++;
	}
	
	/**
	 * return health 
	 * @return
	 */
	public int getHealth(){
		return health;
	}
	
	/**
	 * set health to input int h
	 * @param h
	 */
	public void setHealth(int h){
		health=h;
	}
	
	/**
	 * decrement players health by inputed amount
	 * @param damage
	 */
	public void damagePlayer(int damage){
		health-=damage;
	}

	public boolean getReset() {
		return reset;
	}

	public void setReset(boolean reset) {
		this.reset = reset;
	}
	
	
}
