package world.end;

import world.control.Shake;
import graphics.Sprite;
import main.Entity;

public class Two extends Entity{
	public int a;
	public double sin;
	private Shake s = new Shake(.4);

	public Two(double vsp) {
		super(128, 640);
		vspeed = vsp;
		sprite = Sprite.get("sCreditsTitle");
	}
	
	public void step(){
		a += 1;
		while (a > 59)
			a -= 60;
		
		angle = sin * 10;
		sin = Math.sin((a / 30.0) * Math.PI);
		
		if (y < -200)
			destroy();
		
		y += vspeed;
	}
	
	public void render(){
		sprite.render(imageSingle, Sprite.CENTERED, x + s.x, y + s.y, xscale, yscale, angle, 1, 1, 1, 1);
	}

}
