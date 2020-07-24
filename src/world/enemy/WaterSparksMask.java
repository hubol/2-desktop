package world.enemy;

import world.control.Global;
import world.particles.WaterSparksParts;
import world.player.Player;
import graphics.Sprite;
import main.Calc;
import main.Entity;
import main.Scene;

public class WaterSparksMask extends Entity{
	public int timer;

	public WaterSparksMask(double x, double y, int timer) { 
		super(x, y);
		sprite = Sprite.get("sWaterSparksMask");
		mask = sprite.mask;
		orientation = Sprite.WEST;
		
		yscale = .6;
		
		this.timer = timer;
		setCollisionGroup(Global.BASICENEMY, Global.DEACTIVATEME, Global.DEACTIVATEENEMY);
		spawn();
	}
	
	public void spawn(){
		double j = 12 + Calc.random(8);
		for (int i=0; i<j; i++)
			new WaterSparksParts(x, y);
	}
	
	public void step(){
		timer -= 1;
		if (timer <= 0)
			destroy();
		else{
			if (Scene.collision(this, x, y, Global.PLAYER))
				Player.hurtPlayer(95);
			spawn();
		}
	}
	
	public void render(){
		
	}

}
