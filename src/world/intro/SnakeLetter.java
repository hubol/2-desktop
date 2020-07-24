package world.intro;

import world.control.Shake;
import graphics.Sprite;
import main.Entity;

public class SnakeLetter extends Entity{
	public Shake s;

	public SnakeLetter(double x, double y, int i) {
		super(x, y);
		setColor(Intro.me.LINE);
		imageSingle = i;
		imageSpeed = 0;
		
		s = new Shake(.5);
		
		setCollisionGroup(69420);
		
		orientation = Sprite.NORTHWEST;
		sprite = Sprite.get("sIntroSnake");
		mask = sprite.mask;
	}
	
	public void render(){
		sprite.render(imageSingle, orientation, x + s.x, y + s.y, xscale, yscale, angle, alpha, colR, colG, colB);
	}

}
