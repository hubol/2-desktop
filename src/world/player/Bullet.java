package world.player;

import java.util.ArrayList;

import world.control.Global;
import world.control.Sound;
import world.dream.DreamHeartParticles;
import world.enemy.BasicEnemy;
import world.gameplay.Jar;
import world.interact.BasicNpc;
import world.particles.BulletPop;

import graphics.Sprite;
import main.Calc;
import main.Entity;
import main.Mask;
import main.Scene;

public class Bullet extends Entity{
	public int trail, toAdd, timer, start;
	public boolean used;
	
	public static final Sprite BULLET = Sprite.get("sGunShot"), OVER = Sprite.get("sGunShotOver");

	public Bullet(double x, double y, double hsp) {
		super(x, y);
		hspeed = hsp;
		xscale = Math.signum(hsp)/4;
		yscale = .3;
		orientation = Sprite.EAST;
		sprite = BULLET;
		mask = sprite.mask;
		
		timer = 16;
		start = timer;
		
		trail = 0;
		toAdd = 0;
		
		used = false;
		
		interact();
		
		x += Math.signum(hsp) * 16;
		
		setCollisionGroup(Global.DEACTIVATEME, Global.BULLET);
		setDepth(-2);
	}
	
	public void step(){
		yscale = Calc.approach(yscale, .6, 3);
		
		if (timer > (start / 2.0))
			xscale = Calc.approach(xscale, Math.signum(hspeed), 8);
		else
			xscale = Calc.approach(xscale, Math.signum(hspeed)/5, 12);
		
		toAdd += 1;
		if (toAdd == 3){
			toAdd = 0;
			if (trail < 10)
				trail += 1;
		}
		
		double hSign = Math.signum(hspeed);
		
		hspeed += .6 * hSign;
		hspeed *= 1.0125;
		timer -= 1;
		
		interact();
		
		x += hspeed;
		
		if (Player.me.hLoop){
			if (x > 640)
				x -= 640;
			else if (x < 0)
				x += 640;
		}
	}
	
	public void interact(){
		double hSign = Math.signum(hspeed);
		
		if (Global.roomX == 21)
			new DreamHeartParticles(x - hspeed, y + Calc.rangedRandom(8), getDirection() + 180 + Calc.rangedRandom(25), -15);
		
		if ((Scene.collision(this, Math.round(x)+hspeed+hSign, y, Global.BASICENEMY, Global.SOLIDBLOCK, Global.JAR, Global.BASICNPC))){
			if (!used){
				if (Scene.collision(this, Math.round(x)+hspeed+hSign, y, Global.BASICENEMY)) //if ur gonna touch an enemy
				{
					//gun damage the basic enemy'ies!!!!!!!!!!!
					ArrayList<BasicEnemy> list = Scene.getCollidedEntities(this, Math.round(x)+hspeed+hSign, y, Global.BASICENEMY);
					
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
					
					hspeed=0;
					x = Math.round(x);
					
					int ugh = 0;
					
					while(!Scene.collision(this, x+hSign, y, Global.BASICENEMY) && ugh < 10){
						x+=hSign;
						ugh += 1;
					}
					
					destroy();
				}
				else if (Scene.collision(this, Math.round(x)+hspeed+hSign, y, Global.JAR)) //if ur gonna touch a jar
				{
					//gun damage the basic enemy'ies!!!!!!!!!!!
					ArrayList<Jar> list = Scene.getCollidedEntities(this, Math.round(x)+hspeed+hSign, y, Global.JAR);

					Jar mine = null;
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
						mine.shatter();
					
					hspeed=0;
					
					destroy();
				}
				else if (Scene.collision(this, Math.round(x)+hspeed+hSign, y, Global.BASICNPC)) //if ur gonna touch an npc
				{
					//gun damage the basic enemy'ies!!!!!!!!!!!
					ArrayList<BasicNpc> list = Scene.getCollidedEntities(this, Math.round(x)+hspeed+hSign, y, Global.BASICNPC);
					
					BasicNpc mine = null;
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
					
					hspeed=0;
					x = Math.round(x);
					
					int ugh = 0;
					
					while(!Scene.collision(this, x+hSign, y, Global.BASICNPC) && ugh < 10){
						x+=hSign;
						ugh += 1;
					}

					destroy();
				}
				else if (Scene.collision(this, Math.round(x)+hspeed+hSign, y, Global.SOLIDBLOCK)) //if ur gonna touch a solid block
				{
					hspeed=0;
					x = Math.round(x);
					while(!Scene.collision(this, x+hSign, y, Global.SOLIDBLOCK))
						x+=hSign;
					destroy();
				}
			}
		}
		else if (timer <= 0){ //if ur gonna end ur animation
			destroy();
		}
	}
	
	public void destroy(){
		used = true;
		if (imageSpeed != 0){
			imageSpeed = 0;
			imageSingle = 6.9;
			super.destroy();
			new BulletPop(x, y);
			Sound.playPitched("sGunPop");
			
			if (Global.roomX == 21){
				for (int i=0; i<16; i++)
					new DreamHeartParticles(x, y, Calc.random(360), Integer.MIN_VALUE + 12);
			}
		}
	}
	
	public void render(){
		draw(x, y);
		if (Player.me.hLoop){
			draw(x - 640, y);
			draw(x + 640, y);
		}
	}
	
	public void draw(double x, double y){
		for (int i=0; i < trail; i++){
			sprite.render((int)imageSingle, orientation, x - ((((i + 1)*16)+((i + 3)*(i)))*xscale) + Calc.rangedRandom((i + 1)*.4), y + Calc.rangedRandom((i + 1)*.4), xscale, (Integer)Calc.choose(1,-1) * yscale, 0, .6 / (i + 1), "ffffff");
		}
		sprite.render((int)imageSingle, orientation, x + Calc.rangedRandom(.2), y + Calc.rangedRandom(.2), xscale, yscale, 0, 1, "ffffff");
		OVER.render((int)imageSingle, orientation, x + Calc.rangedRandom(.2), y + Calc.rangedRandom(.2), xscale, yscale, 0, 1, "ffffff");
	}

}
