package world.intro;

import world.control.Shake;
import graphics.Sprite;
import main.Entity;

public class SnakeFollow extends Entity{
	public Shake s;
	
	public SnakeFollow(double x, double y, int timer) {
		super(x, y);
		setColor(Intro.me.LINE);
		imageSingle = 1;
		imageSpeed = 0;
		
		orientation = Sprite.NORTHWEST;
		sprite = Sprite.get("sIntroSnake");
		mask = sprite.mask;
		
		s = new Shake(.5);
		
		alarmInitialize(1);
		alarm[0] = timer;
	}
	
	public void alarmEvent(int i){
		destroy();
	}
	
	public void render(){
		sprite.render(imageSingle, orientation, x + s.x, y + s.y, xscale, yscale, angle, alpha, colR, colG, colB);
	}

}
