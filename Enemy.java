
public class Enemy extends Actor {
	
	private boolean offScreen;

	public Enemy(double x, double y, double scX, double scY) {
		super(x, y, scX, scY);
		// TODO Auto-generated constructor stub
		offScreen=false;
		this.getVel().randomize();
	}
	
	/**
	 * overrides move method
	 * update to change offscreen var if moves offscreen. only activates if missile is set and on screen.
	 */
	@Override
	public void move(){
		if(!offScreen){
			if(this.getX()+this.getVel().getXVel()>0&&this.getX()+this.getVel().getXVel()<this.getScreenMaxX())
				this.setX(this.getX()+this.getVel().getXVel());
			else
				offScreen=true;
			if(this.getY()+this.getVel().getYVel()>0&&this.getY()+this.getVel().getYVel()<this.getScreenMaxY())
				this.setY(this.getY()+this.getVel().getYVel());
			else
				offScreen=false;
		}
	}
	
	public void reset(){
		this.getVel().randomize();
		this.setX(Math.random()*this.getScreenMaxX());
		this.setY(Math.random()*this.getScreenMaxY());
		offScreen=false;
	}

	public boolean isOffScreen() {
		return offScreen;
	}

	public void setOffScreen(boolean offScreen) {
		this.offScreen = offScreen;
	}

}
