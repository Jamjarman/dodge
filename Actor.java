
public class Actor {

	private double xPos;
	private double yPos;
	private double screenMax;
	private Direction vel;
	
	public Actor(double x, double y, double sc){
		xPos=x;
		yPos=y;
		screenMax=sc;
		vel=new Direction(0,0);
	}
	
	public Direction getVel()
	
	public double getScreenMax(){
		return screenMax;
	}
	
	public void setScreenMax(double sc){
		screenMax=sc;
	}
	
	public double getX(){
		return xPos;
	}
	
	public double getY(){
		return yPos;
	}
	
	public void move(double x, double y){
		xPos=xPos;
		yPos=yPos;
	}
	
	public void setX(double x){
		xPos=x;
	}
	
	public void setY(double y){
		yPos=y;
	}
	
}
