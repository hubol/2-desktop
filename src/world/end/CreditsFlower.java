package world.end;

import world.control.Shake;
import graphics.Sprite;
import main.Entity;

public class CreditsFlower extends Entity{
	private Shake s = new Shake(.4);

	public CreditsFlower(double x, double y, Sprite s, double img, double spd) {
		super(x, y);
		sprite = s;
		imageSingle = img;
		imageSpeed = 0;
		vspeed = spd;
		
		setDepth(6);
	}
	
	public void step(){
		super.step();
		if (y < -32)
			destroy();
	}
	
	public void render(){
		sprite.render(imageSingle, Sprite.CENTERED, x + s.x, y + s.y, xscale, yscale, angle, 1, 1, 1, 1);
	}

}
