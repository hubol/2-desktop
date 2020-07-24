package world.junk;

public class StupidGhost {
	public double x, y;
	public double size, angle;
	
	public StupidGhost(double x, double y, double a){
		this.x = x;
		this.y = y;
		angle = a;
		size = 1;
	}
	
	public void update(){
		size *= .95;
		size -= .002;
	}

}