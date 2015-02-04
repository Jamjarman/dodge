
public class Actor {

	private double xPos;
	private double yPos;
	private double screenMaxX;
	private double screenMaxY;
	private double screenMinX;
	private double screenMinY;
	private Direction vel;
	
	public Actor(double x, double y, double scX, double scY, double minX, double minY){
		xPos=x;
		yPos=y;
		screenMaxX=scX;
		screenMaxY=scY;
		screenMinX=minX;
		screenMinY=minY;
		vel=new Direction(0,0);
	}
	
	public Direction getVel(){
		return vel;
	}
	
	public void setVel(Direction dir){
		vel=dir;
	}
	
	public double getScreenMaxX(){
		return screenMaxX;
	}
	
	public void setScreenMaxX(double sc){
		screenMaxX=sc;
	}
	
	public double getScreenMaxY(){
		return screenMaxY;
	}
	
	public void setScreenMaxY(double sc){
		screenMaxY=sc;
	}
	
	public double getX(){
		return xPos;
	}
	
	public double getY(){
		return yPos;
	}
	
	public void move(){
		if(xPos+this.vel.getXVel()>this.screenMinX&&xPos+this.vel.getXVel()<this.screenMaxX)
			xPos=xPos+this.vel.getXVel();
		if(yPos+this.vel.getYVel()>this.screenMinY&&yPos+this.vel.getYVel()<this.screenMaxY)
			yPos=yPos+this.vel.getYVel();
	}
	
	public void setX(double x){
		xPos=x;
	}
	
	public void setY(double y){
		yPos=y;
	}

	public double getScreenMinX() {
		return screenMinX;
	}

	public void setScreenMinX(double screenMinX) {
		this.screenMinX = screenMinX;
	}

	public double getScreenMinY() {
		return screenMinY;
	}

	public void setScreenMinY(double screenMinY) {
		this.screenMinY = screenMinY;
	}
	
}
