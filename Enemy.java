
public class Enemy extends Actor {
	
	private boolean offScreen;
	private int damage=10;
	
	public Enemy(double x, double y, double scX, double scY, double minX, double minY) {
		super(x, y, scX, scY, minX, minY);
		// TODO Auto-generated constructor stub
		offScreen=false;
		this.reset();
	}
	
	/**
	 * overrides move method
	 * update to change offscreen var if moves offscreen. only activates if missile is set and on screen.
	 */
	@Override
	public void move(){
		if(!offScreen){
			if(this.getX()+this.getVel().getXVel()>this.getScreenMinX()&&this.getX()+this.getVel().getXVel()<this.getScreenMaxX())
				this.setX(this.getX()+this.getVel().getXVel());
			else
				offScreen=true;
			if(this.getY()+this.getVel().getYVel()>this.getScreenMinY()&&this.getY()+this.getVel().getYVel()<this.getScreenMaxY())
				this.setY(this.getY()+this.getVel().getYVel());
			else
				offScreen=true;
		}
	}
	
	/**
	 * resets (also used to set due to laziness) enemys position and rerandomizes it's velocity
	 * position is set based on a basic analysis of the components of velocity
	 * larger of x or y absolute value is set to an extreme side of the screen
	 * other of x or y is randomized based on it's positive or negativeness
	 */
	public void reset(){
		this.getVel().randomize();
		if(Math.abs(this.getVel().getXVel())>=Math.abs(this.getVel().getYVel())){
			if(this.getVel().getXVel()<0){
				this.setX(this.getScreenMaxX());
				if (this.getVel().getYVel()>0) {
					this.setY(Math.random()*(this.getScreenMaxY()/2)+(this.getScreenMaxY()/2));
				}
				else{
					this.setY(Math.random()*(this.getScreenMaxY()/2));
				}
			}
			else{
				this.setX(this.getScreenMinX());
				if (this.getVel().getYVel()>0) {
					this.setY(Math.random()*(this.getScreenMaxY()/2)+(this.getScreenMaxX()/2));
				}
				else{
					this.setY(Math.random()*(this.getScreenMaxY()/2));
				}
			}
		}
		else{
			if(this.getVel().getYVel()<0){
				this.setY(this.getScreenMaxY());
				if (this.getVel().getXVel()>0) {
					this.setX(Math.random()*(this.getScreenMaxX()/2));
				}
				else{
					this.setX(Math.random()*(this.getScreenMaxX()/2)+(this.getScreenMaxX()/2));
				}
			}
			else{
				this.setY(this.getScreenMinY());
				if (this.getVel().getXVel()>0) {
					this.setX(Math.random()*(this.getScreenMaxX()/2));
				}
				else{
					this.setX(Math.random()*(this.getScreenMaxX()/2)+(this.getScreenMaxX()/2));
				}
			}
		}
		offScreen=false;
	}

	public boolean isOffScreen() {
		return offScreen;
	}

	public void setOffScreen(boolean offScreen) {
		this.offScreen = offScreen;
	}

	public int getDamage() {
		// TODO Auto-generated method stub
		return this.damage;
	}

}
