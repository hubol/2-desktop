package world.event;

import world.control.Global;
import main.Entity;

public class BrickControl extends Entity{
	public int row, height, total, timer = -1;
	public final String[] colors = new String[]{"#18c9ffF", "#ff3060"};

	public BrickControl(double x, double y) {
		super(x, y);
		row = 0;
		height = 0;
		total = 0;

		for (int i=0; i<need(); i++)
			brick(false);
		
		visible = false;
		setCollisionGroup(Global.DEACTIVATEME);
	}
	
	public void step(){
		if (timer > -1)
			timer -= 1;
		else if (need() != total){
			brick(true);
			timer = 6;
		}
	}
	
	public int need(){
		return (int)(55 * Math.min(1, ((double)Global.heartsGot() / 25.0)));
	}
	
	public void brick(boolean fall){
		if (fall)
			new Brick(160 + (height * 16) + (row * 32), 368 - (height * 16), true, colors[(height + row) % 2]);
		else
			new Brick(160 + (height * 16) + (row * 32), 368 - (height * 16), colors[(height + row) % 2]);
		
		row += 1;
		if (row > 9 - height){
			row = 0;
			height += 1;
		}
		total += 1;
	}

}
