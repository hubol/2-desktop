package world.boss.papa;

public class StreamerGhost extends SpikeGhost{
	public StreamerGhost(double x, double y){
		super(x, y);
	}
	
	public void update(){
		size *= .9;
		size -= .0045;
	}

}