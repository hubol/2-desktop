package world.gameplay;

import world.control.Global;
import world.control.Sound;
import world.event.NoOccupy;
import world.particles.Debris;
import world.player.Player;
import graphics.Sprite;
import main.Calc;
import main.Entity;
import main.Scene;

public class Jar extends Entity{
	public int gems;
	public boolean falling;
	public double timer, mult;
	
	public final static Sprite JAR = Sprite.get("sJar");

	public Jar(double x, double y, double g) {
		super(x, y);
		sprite = JAR;
		
		setDepth(22);
		orientation = Sprite.NORTHWEST;
		setCollisionGroup(Global.JAR, Global.DEACTIVATEME);
		mask = sprite.mask;
		
		falling = false;
		imageSpeed = 0;
		
		imageSingle = 0;
		g *= Global.luck;
		
		double a = Player.me.random(1);
		if (a > .97){
			imageSingle = 2;
			g += 1;
			g *= 2;
		}
		else if (a > .9){
			imageSingle = 1;
			g += .5;
			g *= 1.5;
		}
		
		gems = (int)Math.max(0, Player.me.random(g));
		
		reset();
	}
	
	public void step(){
		if (!falling){
			if (!Scene.collision(this,x,y+1,Global.SOLIDBLOCK, Global.UPBLOCK, Global.JAR)){
				falling = true;
				vspeed = 2;
				gems += 1; //:)
			}
		}
		else{
			vspeed += .8;
			if (Scene.collision(this,x,y+vspeed,Global.JAR, Global.SOLIDBLOCK, Global.UPBLOCK))
				shatter();
		}
		super.step();
		
		timer -= 1;
		if (timer <= 0){
			reset();
		}
	}
	
	public void reset(){
		timer = Calc.random(30);
		mult = Calc.random(2);
	}
	
	public final static Sprite PARTS = Sprite.get("sJarParts");
	
	public void shatter(){
		Sound.jarBreakPlay();
		Global.dropLoot(x+16, y+16, gems);
		for (int i=0; i<9; i++){
			new Debris(x + 4 + Calc.random(20), y + 4 + Calc.random(20), PARTS);
		}
		new NoOccupy(x, y);
		super.destroy();
	}
	
	public void render(){
		double zx = x, zy = y;
		x += (-.5 + Calc.random(1)) * mult;
		y += (-.5 + Calc.random(1)) * mult;
		super.render();
		x = zx;
		y = zy;
	}

}
