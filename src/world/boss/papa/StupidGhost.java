package world.boss.papa;

public class StupidGhost extends SpikeGhost{
	public StupidGhost(double x, double y){
		super(x, y);
	}
	
	public void update(){
		size *= .95;
		size -= .00225;
	}

}