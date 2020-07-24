package world.junk;

public class FlapperGhost {
	public double x, y;
	public double size;
	
	public FlapperGhost(double x, double y){
		this.x = x;
		this.y = y;
		size = 1;
	}
	
	public void update(){
		size *= .98;
		size -= .001;
	}

}