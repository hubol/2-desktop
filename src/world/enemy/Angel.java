package world.enemy;

import world.control.EV;
import world.control.Global;
import world.control.Sound;
import world.dream.HeartPiece;
import world.particles.SquareParticle;
import world.player.Player;
import graphics.Sprite;
import main.Calc;
import main.Scene;

public class Angel extends BasicEnemy{
	//angle/movement shit
	public double dist, ang, add;
	
	//death shit
	public boolean dead;
	
	//halo shit
	public double haloX, haloY, haloH, haloV, haloA, haloYAdd = -31, haloMath, haloGlow;
	
	//animation shit
	public int timer, next;
	public double[] imgAlpha;
	
	public double xStart, yStart;
	
	//sounds
	public int[] playAt;
	public boolean lastPlayed;
	public int singTimer;
	
	public int express;
	public int blinkTimer;
	public boolean blink;
	
	
	public Angel(double x, double y, double d, double s, double a) {
		super(x, y);
		
		addCollisionGroup(Global.DEACTIVATEENEMY);
		
		dist = d;
		ang = s;
		add = a;
		
		xStart = x;
		yStart = y;
		
		resetPosition();
		haloX = this.x;
		haloY = this.y + haloYAdd;
		haloA = 0;
		
		haloGlow = 0;
		haloMath = 0;
		
		orientation = Sprite.CENTERED;
		mask = Sprite.get("sAngelMask").mask;
		setDepth(-1);
		
		timer = 0;
		next = 1;
		imgAlpha = new double[2];
		imgAlpha[0] = 1;
		imgAlpha[1] = 0;
		
		playAt = new int[4];
		
		int b = (int)xStart + (int)yStart + (Global.roomX * 640) + (Global.roomY * 480);
		for (int i=0; i<4; i++){
			playAt[i] = b % 360; 
			b += 90;
		}
		lastPlayed = false;
		
		blink = false;
		singTimer = 0;
		blinkTimer();
	}
	
	public void blinkTimer(){
		blinkTimer = (int)Calc.random(90);
	}
	
	public void resetPosition(){
		x = xStart + Calc.dirX(dist, ang);
		y = yStart + Calc.dirY(dist, ang);
	}
	
	public void step(){
		//adjust position
		ang += add;
		while (ang >= 360)
			ang -= 360;
		
		if (!lastPlayed){
			for (int i=0; i<4; i++){
				if (!dead && ang >= playAt[i] && ang < playAt[i] + add && !lastPlayed){
					lastPlayed = true;
					Sound.angelPlay();
					singTimer = 10;
				}
			}
		}
		else
			lastPlayed = false;
		
		if (!dead){
			resetPosition();
			playerCollisionDamagesMe();
			
			if (Scene.collision(this, x, y, Global.PLAYER) && Player.me.vspeed > 0)
				hurtPlayer();
		}
		else{
			super.step();
			setSpeed(getSpeed() * .98);
			
			vspeed += 1.1;
			
			angle += add;
			xscale += .005;
			yscale = xscale;
			alpha -= .005;
			
			if (alpha <= 0 && haloY >= 540)
				destroy();
		}
		
		//halo animation
		if (!dead){
			haloMath += 1;
			haloGlow = Math.abs(Math.sin(haloMath / 15.0));
			
			haloX = Calc.approach(haloX, x, 1.1);
			haloY = y + haloYAdd + (2.0 * Math.sin(haloMath / 7.0));
		}
		else{
			haloGlow = Calc.approach(haloGlow, 0, 12);
			
			haloX += haloH;
			haloY += haloV;
			haloA += haloV * Math.signum(haloH);
			
			haloH *= .97;
			haloV += .8;
		}
		
		if (!dead){
			//flippin
			if (Player.me.x < x)
				xscale = 1;
			else
				xscale = -1;
			
			//animation
			timer += 1;
			if (timer >= 7){
				timer = 0;
				if (next == 0)
					next = 1;
				else
					next = 0;
			}
			for (int i=0; i<imgAlpha.length; i++){
				if (next == i)
					imgAlpha[i] = Calc.approach(imgAlpha[i], 1, 1.5);
				else
					imgAlpha[i] = Calc.approach(imgAlpha[i], 0, 2);
				if (imgAlpha[i] > .95 && next == i)
					imgAlpha[i] = 1;
			}
		}
		
		if (alpha > 0){
			SquareParticle i = new SquareParticle(x, y);
			i.setColor(Global.roomColor);
			i.setDepth(0);
		}
		
		blinkTimer -= 1;
		if (blinkTimer <= 0){
			blink = !blink;
			if (blink)
				blinkTimer = 8;
			else
				blinkTimer();
		}
		
		express = 0;
		if (singTimer > 0)
			express = 2;
		else if (blink)
			express = 1;
		
		singTimer -= 1;
		
	}
	
	public void hurtPlayer(){
		Player.hurtPlayer(50);
	}
	
	public void landDamage(){
		Player.me.jump();
		Player.me.vspeed -= .8;
		Sound.play("sLandOnEnemy");
		die();
	}
	
	public void bombDamage(){
		die();
	}
	
	public void gunDamage(){
		die();
	}
	
	public void die(){
		if (!dead){
			dead = true;
			haloH = Calc.rangedRandom(5);
			haloV = -1 - Calc.random(5);
			landCheckNecessary = false;
			
			add /= 4.0 * (Double)Calc.choose(1.0, -1.0);
			xscale = 1;
			yscale = 1;
			
			y += 24;
			
			if (Global.roomX == 18 && Global.roomY == 0 && Global.event[EV.SUNLIGHT_DREAM] == 0)
				new HeartPiece(x, y);
			
			Global.dropRandomLoot(x, y , 7);
			
			setDirSpeed(Calc.random(360),2 + Calc.random(8));
			Sound.playPitched("sAngelDie",.1);
		}
	}
	
	public final static Sprite ANGEL = Sprite.get("sAngel"), EXPRESSION = Sprite.get("sAngelExpress"), DEAD = Sprite.get("sAngelDead"), HALO = Sprite.get("sHalo");
	
	public void render(){
		if (!dead){
			for (int i=0; i<imgAlpha.length; i++)
				ANGEL.render(i, Sprite.CENTERED, x + Calc.rangedRandom(.6), y - 3 + Calc.rangedRandom(.6), xscale, 1, 0, imgAlpha[i], 1, 1, 1);
			EXPRESSION.render((express*2)+next, Sprite.CENTERED, x + Calc.rangedRandom(.6), y - 3 + Calc.rangedRandom(.6), xscale, 1, 0, 1, 1, 1, 1);
		}
		else{
			DEAD.render(Calc.random(5), Sprite.CENTERED, x + Calc.rangedRandom(.6 * (1 - alpha)), y - 3 + Calc.rangedRandom(.6 * (1 - alpha)), xscale, yscale, angle, alpha, "ffffff");
		}
		//draw a mf halo
		HALO.render(0, Sprite.CENTERED, haloX + Calc.rangedRandom(.6), haloY + Calc.rangedRandom(.6), 1, 1, haloA, 1, "ffffff");
		HALO.render(1, Sprite.CENTERED, haloX + Calc.rangedRandom(.6), haloY + Calc.rangedRandom(.6), 1, 1, haloA, haloGlow, "ffffff");
	}

}
