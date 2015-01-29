
public class Actor {

	private double xPos;
	private double yPos;
	private double screenMaxX;
	private double screenMaxY;
	private Direction vel;
	
	public Actor(double x, double y, double scX, double scY){
		xPos=x;
		yPos=y;
		screenMaxX=scX;
		screenMaxY=scY;
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
		if(xPos+this.vel.getXVel()>0&&xPos+this.vel.getXVel()<this.screenMaxX)
			xPos=xPos+this.vel.getXVel();
		if(yPos+this.vel.getYVel()>0&&yPos+this.vel.getYVel()<this.screenMaxY)
			yPos=yPos+this.vel.getYVel();
	}
	
	public void setX(double x){
		xPos=x;
	}
	
	public void setY(double y){
		yPos=y;
	}
	
}
