package world.boss.puke;

import world.control.Global;
import world.control.Sound;
import world.enemy.BasicEnemy;
import world.player.Player;
import graphics.Sprite;
import main.Calc;
import main.Scene;

public class PukeBalloon extends BasicEnemy {
	public double ax, az, timer;
	public int sS, sI, fI;
	
	public double drawX, drawY;
	public boolean popped;
	
	public PukeSpawn mom;

	public PukeBalloon(double zx, double zy, PukeSpawn mommy) {
		super(zx, zy);
		
		mom = mommy;
		
		for (int i=0; i<4; i++)
			Global.squareParticle(x + Calc.rangedRandom(6), y + Calc.rangedRandom(6), 3 + (int)Calc.random(5), Global.roomColor, 1.5 + Calc.random(5));
		
		orientation = Sprite.CENTERED;
		sprite = Sprite.get("sBalloon");
		mask = sprite.mask;
		ax = Calc.random(60);
		az = Calc.random(60);
		
		drawX = xstart + (6 * Math.sin(ax / 25));
		drawY = ystart + (6 * Math.sin(az / 28));
		
		popped = false;
		
		setString();
		
		fI = 5 + (int)(Global.roomX % 2) + (int)((Global.roomY % 2) * 2);
		
		timer = Calc.random(30.0 / 4.0);
	}
	
	public void step(){
		ax += 1;
		az += 1;
		drawX = xstart + (6 * Math.sin(ax / 25));
		drawY = ystart + (6 * Math.sin(az / 18));
		
		playerCollisionDamagesMe();
		
		if (!popped){
			if (Scene.collision(this, x, y, Global.PLAYER) && Player.me.y < y - 12 && Player.me.vspeed > 0)
				landDamage();
		}
		
		timer -= 1;
		if (timer <= 0){
			setString();
			timer = 30.0 / 4.0;
		}
	}
	
	public void setString(){
		sS = (Integer)Calc.choose(1, -1);
		sI = 2 + (int)Calc.random(3);
	}
	
	public void landDamage(){
		Player.me.jump();
		Player.me.vspeed -= .8;
		Sound.play("sLandOnEnemy");
		pop();
	}
	
	public void bombDamage(){
		pop();
	}
	
	public void gunDamage(){
		pop();
	}
	
	public void pop(){
		if (!popped){
			popped = true;
			
			for (int i=0; i<2; i++)
				Global.squareParticle(drawX + Calc.rangedRandom(6), drawY + Calc.rangedRandom(6), 2 + (int)Calc.random(4), "007FEF", .9 + Calc.random(3.3));
			for (int i=0; i<4; i++)
				Global.squareParticle(drawX + Calc.rangedRandom(16), drawY + Calc.rangedRandom(16), 3 + (int)Calc.random(5), Global.roomColor, 1.5 + Calc.random(5));
			
			Sound.playPitched("sBalloonPop");
			
			mom.alarm[0] = 60;
			
			destroy();
		}
	}
	
	public void render(){
		balloonDraw(sI, drawX - 1.5, drawY + 20, sS, "ffffff");
		balloonDraw(0, drawX, drawY, 1, Global.roomColor);
		balloonDraw(fI, drawX, drawY, 1, "ffffff");
		balloonDraw(1, drawX, drawY, 1, "ffffff");
	}
	
	public void balloonDraw(int i, double axx, double ayy, double xsc, String col){
		sprite.render(i, Sprite.CENTERED, axx + Calc.rangedRandom(.25), ayy + Calc.rangedRandom(.25), xsc, 1, Calc.rangedRandom(.25), 1, col);
	}

}
