package world.boss.jelly;

import world.Fg;
import world.control.Global;
import world.control.Sound;
import world.enemy.BombBaby;
import world.player.Player;
import audio.Audio;
import main.Calc;
import graphics.Graphics;
import graphics.Shape;
import graphics.Sprite;

public class JellyBigBubble extends JellyEnemy{
	public boolean bombBaby, hurt, split;
	public double timer;
	
	public int phase;
	public int mult;
	
	public double addAng;
	public int aidTimer, aids, reset = 5;
	
	public int bombAwaitTimer;
	
	public int exclamation;

	public JellyBigBubble(double x, double y, double t, boolean b) {
		super(x, y);
		
		setDepth(-4);
		
		exclamation = (Integer)Calc.choose(1,2,3);
		
		aidTimer = 9000000;
		aids = 0;
		
		phase = 0;
		
		angle = Calc.random(360);
		mult = (Integer)Calc.choose(1,-1);
		addAng = 0;
		
		timer = t;
		bombBaby = b;
		hurt = false;
		
		split = false;
		if (!b && ((JellyMan.me.health <= 4 && JellyMan.me.count <= 8) || JellyMan.me.count % 2 == 0))
			split = true;
		
		orientation = Sprite.CENTERED;
		
		if (b)
			sprite = Sprite.get("sJellyBombBlob");
		else
			sprite = Sprite.get("sJellyBlob");
		backSprite = Sprite.get("sJellyBlobBack");
		
		setCollisionGroup(Global.BASICENEMY, Global.DEACTIVATEME);
		
		xscale = 0;
		yscale = 0;
	}
	
	public void step(){
		if (phase == 0){
			if (xscale < .75){
				angle += (2 + addAng) * (double)mult;
				addAng += .3;
				xscale += .75 / timer;
				yscale = xscale;
				
				if (xscale >= .375 && aidTimer > 100 && split)
					aidTimer = reset;
			}
			else{
				phase = 1;
				hurt = true;
				vspeed = 1.6;
			}
		}
		else if (phase == 1 || phase == 3){
			vspeed += .8;
			angle += vspeed * (double)mult;
		}
		else if (phase == 2){
			angle = Calc.approach(angle, 0, 1.2);
			
			double ay = yscale;
			yscale -= .025;
			
			if (yscale > .45)
				yscale = Calc.approach(yscale, 0, 10);
			yscale = Calc.approach(yscale, 0, 8);
			
			y += (ay - yscale + .02) * 64;
			
			xscale += .013;
			xscale = Calc.approach(xscale, 1.3, 7);
			if (yscale <= 0 || xscale <= 0)
				destroy();
		}
		
		if (phase == 3){
			if (imageSingle > 6)
				imageSingle = 6;
			
			if (y >= JellyMan.me.y)
				JellyMan.me.goTo(x, y);
			
			if (y + vspeed>= 368){
				Fg.me.shakeTimer = 15;
				
				Sound.explodePlay();
				Sound.explodePlay();
				
				JellyMan.me.goTo(x, 400);
				
				phase = 4;
				bombAwaitTimer = 165;
				
				Sound.playPitched("sJellyGlassLand",.05);
				
				for (int i=0; i<6; i++){
					new JellyBubbleSmall(x + Calc.rangedRandom(4), y + Calc.rangedRandom(4),6);
					new JellyBubbleSmall(x + Calc.rangedRandom(4), y + Calc.rangedRandom(4),5);
					new JellyBubbleSmall(x + Calc.rangedRandom(4), y + Calc.rangedRandom(4),4);
				}
				
				y = 368;
				vspeed = 0;
				sprite = Sprite.get("sJellyBigBubbleBreakGlow");
				imageSingle = 0;
				imageSpeed = .7;
			}
			
			if (phase == 4){
				imageSpeed += .004975;
			}
		}
		
		if (split){
			aidTimer -= 1;
			if (aidTimer <= 0){
				aids += 1;
				String s = "sJellyExclamation" + exclamation;
				Audio.get(s).setPitch(1 + ((double)aids / 20.0));
				Sound.play(s);
				new JellyExclamation(x + (aids * 36), 412);
				new JellyExclamation(x - (aids * 36), 412);
				aidTimer = reset;
			}
		}
		
		//crush teh fuckin boss
		if (phase == 1 && JellyMan.me.awaitBubble && y + 48 > JellyMan.me.y - 16){
			phase = 3;
			JellyMan.me.awaitBubble = false;
			JellyMan.me.phase = 3;
			JellyMan.me.imageSingle = 1;
			JellyMan.me.visible = false;
			sprite = Sprite.get("sJellyBigBubbleBreak");
			imageSingle = 1;
			imageSpeed = .3;
			Sound.playPitched("sJellyHurtCry",.05);
		}
		
		if (angle > 360)
			angle -= 360;
		if (angle < 0)
			angle += 360;
		
		super.step();
		
		if (phase == 4){
			bombAwaitTimer -= 1;
			if (bombAwaitTimer <= 0)
				shatter();
		}
		
		if (y >= 368 && phase == 1){
			Fg.me.shakeTimer = 5;
			
			Sound.explodePlay();
			Sound.explodePlay();
			
			Global.jellyExplosionEffect(x, 416, 1.1, bombBaby);
			Global.jellyExplosionEffect(x, 416, 1.3, bombBaby);
			
			if (Calc.pointDistance(Player.me.x,Player.me.y,x,y) < 96)
				Player.hurtPlayer(60);
			
			Sound.playPitched("sJellyLandPop",.05);
			y = 368;
			
			phase = 2;
			vspeed = 0;
			hurt = false;
			
			if (bombBaby){
				Sound.babyPlay();
				new BombBaby(x, 416);
			}
			else if (split){
				double spd = 7 + (3 * ((10 - JellyMan.me.health)/10.0));
				aids = 0;
				aidTimer = 900000000;
				Sound.playPitched("sJellySplit",.05);
				new JellyHorizontalBubble(x, y, .18 + (.06 * ((10 - JellyMan.me.health)/10.0)), spd);
				new JellyHorizontalBubble(x, y, .18 + (.06 * ((10 - JellyMan.me.health)/10.0)), -spd);
			}
		}
		
		if (phase != 4){
			if (hurt && Calc.pointDistance(x,y,Player.me.x,Player.me.y) < (64 * xscale) + 16)
					Player.hurtPlayer(50);
		}
		/*else if (Calc.pointDistance(x,y,Player.me.x,Player.me.y) < (64 * xscale) + 1)
			Player.hurtPlayer(50);*/
	}
	
	public void electricCircle(double in){
		Graphics.setColor("FF8B10");
		Graphics.setAlpha(1);
		int amt = 12 + (int)Calc.random(12);
		double dist = (49.0 * xscale) + Calc.random(40*in), dir = 0;
		double lastX = Calc.dirX(dist, dir), lastY = Calc.dirY(dist, dir);
		for (int i=0; i<=amt; i++){
			dist = (49.0 * xscale) + Calc.random(20*in);
			if (Calc.random(1) > .8)
				dist += Calc.random(30*in);
			dir += 360.0 / (double)amt;
			Shape.drawLine(x + lastX, y + lastY, x + Calc.dirX(dist, dir), y + Calc.dirY(dist, dir), .85 + Calc.random(.7));
			lastX = Calc.dirX(dist, dir);
			lastY = Calc.dirY(dist, dir);
		}
	}
	
	/**to my current knowledge, this occurs when either timer runs out for shield thing or bomb hapen*/
	public void shatter(){
		if (phase == 4){
			if (bombAwaitTimer <= 0){
				Sound.playPitched("sJellyReturn",.05);
				JellyMan.me.phase = 1;
				split();
			}
			else{
				Fg.me.shakeTimer = 15;
				Sound.playPitched("sJellyGlassShatter",.05);
				Sound.playPitched("sJellyHitCry",.05);
				JellyMan.me.phase = 4;
				JellyMan.me.stunTimer = 190;
				JellyMan.me.horizontalJump();
				JellyMan.me.imageSingle = 0;
				split();
			}
		}
	}
	
	public void split(){
		for (int i=0; i<16; i++){
			for (int j=1; j<5; j++){
				new JellyBubbleSmall(x + Calc.dirX(j * 14, (double)i * 22.5) + Calc.rangedRandom(4), y + Calc.dirY(j * 14, (double)i * 22.5) + Calc.rangedRandom(4),10);
				new JellyBubble(x + Calc.dirX(j * 14, (double)i * 22.5) + Calc.rangedRandom(4), y + Calc.dirY(j * 14, (double)i * 22.5) + Calc.rangedRandom(4),6);
				new JellyBubble(x + Calc.dirX(j * 14, (double)i * 22.5) + Calc.rangedRandom(4), y + Calc.dirY(j * 14, (double)i * 22.5) + Calc.rangedRandom(4),8);
			}
		}
		JellyMan.me.visible = true;
		destroy();
	}
	
	public void render(){
		
	}
	
	public void renderBack(){
		double i = imageSingle;
		imageSingle = 0;
		super.renderBack();
		imageSingle = i;
	}
	
	public void renderFront(){
		super.renderFront();
		
		double i = 1 + ((10 - JellyMan.me.health)/10);
		
		if (phase == 3){
			electricCircle(i);
			electricCircle(i);
		}
		if (phase == 4){
			electricCircle(i);
			electricCircle(i);
			if (bombAwaitTimer < 60)
				electricCircle(i);
		}
		
		Sprite.get("sJellyBlobSheen").render(0, orientation, x, y, xscale, yscale, 0, alpha, 1, 1, 1);
	}

}
