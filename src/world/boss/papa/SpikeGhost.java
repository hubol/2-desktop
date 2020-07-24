package world.boss.papa;

public class SpikeGhost {
	public double x, y;
	public double size;
	
	public SpikeGhost(double x, double y){
		this.x = x;
		this.y = y;
		size = 1;
	}
	
	public void update(){
		size *= .98;
		size -= .003;
	}

}