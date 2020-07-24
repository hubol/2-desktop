package world.junk;

import graphics.Sprite;

import java.util.ArrayList;

import world.control.Global;
import world.control.Shake;
import world.control.Sound;
import world.enemy.BasicEnemy;
import world.particles.BulletPop;
import world.player.Player;
import main.Calc;
import main.Entity;
import main.Mask;
import main.Scene;

public class Sprak extends Entity{
	private Shake s = new Shake();

	public Sprak(double x, double y) {
		super(x, y);
		
		sprite = Sprite.get("sSprak");
		mask = sprite.mask;
		xscale = .25;
		yscale = .25;
		
		setCollisionGroup(Global.DEACTIVATEME);
		setDepth(-2); 
	}
	
	public void step(){
		final double v = -2.75;
		y += v;
		
		if (Scene.collision(this, x, Math.round(y) + v - 1, Global.BASICENEMY)){
			//gun damage the basic enemy'ies!!!!!!!!!!!
			ArrayList<BasicEnemy> list = Scene.getCollidedEntities(this, x, Math.round(y) + v - 1, Global.BASICENEMY);
			
			BasicEnemy mine = null;
			double dist = 9001;
			
			for (int i=0; i<list.size(); i++){
				Mask m = list.get(i).mask;
				double newDist = Calc.pointDistance(x, y, list.get(i).x - (m.width * (Sprite.get("test").getOrientationScale(list.get(i).orientation)[0] - .5)), list.get(i).y - (m.height * (Sprite.get("test").getOrientationScale(list.get(i).orientation)[1] - .5)));
				if (newDist < dist){
					dist = newDist;
					mine = list.get(i);
				}
			}
			if (mine != null)
				mine.gunDamage();
			
			y = Math.round(y);
			
			int ugh = 0;
			
			while(!Scene.collision(this, x, y - 1, Global.BASICENEMY) && ugh < 10){
				y -= 1;
				ugh += 1;
			}
			
			destroy();
		}
		else if (Calc.pointDistance(x, y, Player.me.x, Player.me.y) < 30){
			Player.hurtPlayer(100);
			destroy();
		}
	}
	
	public void destroy(){
		new BulletPop(x, y);
		Sound.playPitched("sGunPop");
		s.destroy();
		super.destroy();
	}
	
	public void render(){
		sprite.render(0, orientation, x + s.x + Calc.rangedRandom(.1), y + s.y + Calc.rangedRandom(.1), 1, 1, y, 1, 1, 1, 1); 
	}

}
