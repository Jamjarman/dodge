
public class Player extends Actor {

	private int health;
	private int score;
	
	/**
	 * generate a new player character based on actor super class adding health and score int fields
	 * @param x
	 * @param y
	 * @param h
	 * @param s
	 */
	public Player(int x, int y, int sc, int h, int s) {
		super(x, y, sc);
		health=h;
		score=s;
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
	
	public int getHealth(){
		return health;
	}
	
	public void setHealth(int h){
		health=h;
	}
	
	public void damagePlayer(int damage){
		health-=damage;
	}
	
	/**
	 * moves player position using x and y as a change variable rather than a hard position
	 */
	@Override
	public void move(int x, int y){
		if(this.getX()+x>this.getScreenMax()||this.getX()+x<0){
			this.setX(x);
		}
	}
	
	public void move(char direction){
		if(direction=='q'){
			
		}
	}
	
}
