package world.intro;

import java.util.ArrayList;

import world.control.Shake;
import world.control.Sound;
import graphics.Sprite;
import main.Entity;
import main.Scene;

public class Snake extends Entity{
	public final int timer = 10;
	public int fed = 0;
	public Shake s;

	public Snake(double x, double y) {
		super(x, y);
		setColor(Intro.me.LINE);
		imageSingle = 0;
		imageSpeed = 0;
		
		orientation = Sprite.NORTHWEST;
		sprite = Sprite.get("sIntroSnake");
		mask = sprite.mask;
		
		s = new Shake(.5);
		
		alarmInitialize(2);
		alarm[0] = 60;
	}
	
	public void alarmEvent(int i){
		if (i == 0){
			x += 64;
			Sound.playPitched("sIntroSnakeMove",.03);
			
			if (Scene.instanceNumber(SnakeFollow.class) < fed)
				new SnakeFollow(x - 64, y, timer + 1);
			else{
				ArrayList<SnakeFollow> list = Scene.getEntityList(SnakeFollow.class);
				for (int j=0; j<list.size(); j++){
					list.get(j).x += 64;
					list.get(j).alarm[0] += timer;
				}
			}
			
			if (x < 960)
				alarm[0] = timer;
			else
				alarm[1] = 60;
		}
		else
			Intro.me.complete();
	}
	
	public void collect(){
		fed += 1;
		Sound.playPitched("sIntroSnakeGet",.03);
		ArrayList<SnakeFollow> list = Scene.getEntityList(SnakeFollow.class);
		for (int i=0; i<list.size(); i++)
			list.get(i).alarm[0] += timer;
	}
	
	public void step(){
		if (Scene.collision(this, x, y, 69420)){
			collect();
			ArrayList<Entity> arf = Scene.getCollidedEntities(this, x, y, 69420);
			arf.get(0).destroy();
		}
		
		alarmStep();
	}
	
	public void render(){
		sprite.render(imageSingle, orientation, x + s.x, y + s.y, xscale, yscale, angle, alpha, colR, colG, colB);
	}

}
