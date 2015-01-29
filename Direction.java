
public class Direction {
	
	double xVel;
	double yVel;
	
	public Direction(double x, double y){
		xVel=x;
		yVel=y;
	}
	
	public double getXVel(){
		return xVel;
	}
	
	public double getYVel(){
		return yVel;
	}
	
	public void setXVel(double x){
		xVel=x;
	}
	
	public void setYVel(double y){
		yVel=y;
	}
	
	public void randomize() {
		// TODO Auto-generated method stub
		double angle=Math.random()*Math.PI*2;
		xVel=(double) Math.cos(angle);
		yVel=(double) Math.sin(angle);
	}

}
