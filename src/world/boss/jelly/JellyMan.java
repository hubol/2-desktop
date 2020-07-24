package world.boss.jelly;

import java.awt.Point;

import world.control.Global;
import world.control.Hud;
import world.control.Music;
import world.control.Sound;
import world.event.SmallMessage;
import world.player.Player;

import audio.Audio;

import main.Calc;
import graphics.Sprite;

public class JellyMan extends JellyEnemy{
	public double xx, drawY;
	
	public int phase; //introduction, 1move up-right, 2burstin + bubble warp, 3 trapped in bubble, 4 help im on teh ground
	public int subphase; //lol why is this a thing
	
	public int count;
	public double timer;
	
	public double size;
	
	public double randomGen;
	
	public boolean active;
	
	public int health;
	
	public double moveX, moveY, moveDiv;
	
	public double dieSize;
	
	public int cryTimer, cry;
	
	public int aniTimer;
	public double stunTimer;
	public int hurt, hitsThisCycle;
	
	//circle burstin
	public boolean indicateBurst;
	public double indicateX, indicateY, indicateR, indicateA;
	
	public double dieCount;
	
	public static JellyMan me;
	
	public boolean awaitBubble;
	
	public boolean crying;
	public double cryImage;

	public JellyMan(double x, double y) {
		super(x, y);

		randomGen = 4235.6969;
		
		cryImage = 0;
		crying = false;
		me = this;
		awaitBubble = false;
		
		orientation = Sprite.CENTERED;
		setDepth(-3);
		setCollisionGroup(Global.DEACTIVATEME, Global.BASICENEMY);
		
		sprite = Sprite.get("sJellyMan");
		backSprite = Sprite.get("sJellyManBack");
		
		mask = Sprite.get("sJellyMask").mask;
		dieSize = 1;
		
		health = 10;
		dieCount = 0;
		
		size = 1;
		
		imageSingle = 0;
		imageSpeed = 0;
		
		cryTimer = 0;
		cry = 1;
		
		stunTimer = 0;
		aniTimer = 0;
		hurt = 0;
		
		hitsThisCycle = 0;
		
		Audio.get("sJellySpawnWhistle").setGain(0);
		Audio.fade("sJellySpawnWhistle", 0, 0);
		Audio.get("sJellySpawnWhistle").setLooping(true);
		Audio.get("sJellySpawnWhistle").setPitch(1.0);
		Audio.get("sJellySpawnWhistle").play();
		
		xx = 0;
		drawY = 0;
		
		vspeed = .2;
		phase = 0;
		
		count = 0;
		timer = 0;
		active = false;
		
		indicateBurst = false;
		indicateX = 320;
		indicateY = 240;
		indicateR = 48;
		indicateA = 0;
		
		moveDiv = 24;
	}
	
	public double pissRandom(double a){
		randomGen += (a * 7.7890123) + 99.42069;
		randomGen *= 1.1 - (a % .1);
		if (Math.abs(randomGen) > Double.MAX_VALUE - 1000)
			randomGen = .69;
		return ((randomGen % 69.5) / 69.5) * a;
	}
	
	public double pissRangedRandom(double a){
		return -a + pissRandom(a * 2.0);
	}
	
	public Object pissChoose(Object... entries){
		int i = (int)(pissRandom(1)*entries.length);
		return entries[i];
	}
	
	public void destroy(){
		dumpLeftovers();
		super.destroy();
	}
	
	public void roomDestroy(){
		dumpLeftovers();
		super.roomDestroy();
	}
	
	public void dumpLeftovers(){
		Audio.get("sJellySpawnWhistle").stop();
		Audio.get("sJellySpawnWhistle").setGain(1);
		Audio.fade("sJellySpawnWhistle", 1, 1);
		Audio.get("sJellySpawnWhistle").setLooping(false);
		Audio.get("sJellySpawnWhistle").setPitch(1.0);
		
		me = null;
	}
	
	public void setFloat(double y){
		drawY = Calc.approach(drawY, y, 3);
	}
	
	public void step(){
		if (active){
			cryTimer -= 1;
			if (cryTimer <= 0){
				Sound.playPitched("sJellyCry"+cry,.12);
				
				if (cry == 1)
					cry = 2;
				else
					cry = 1;
				cryTimer = 300 + (int)Calc.random(300);
				if (phase == 69)
					cryTimer /= 2;
			}
			
			if (phase == 1 || phase == 4){
				if (hurt > 0){
					new JellyBubble(x + Calc.rangedRandom(6), y + Calc.rangedRandom(6),hurt);
					visible = !visible;
					hurt -= 1;
				}
			}
			else{
				hurt = 0;
			}
			
			if (phase == 4){
				JellyRender.me.intensity = size * size;
			}
			
			if (phase == 69){
				dieCount += 1;
				dieSize -= 1.0/120.0;
				Audio.get("musBoss01").setPitch(1 - (dieCount/240.0));
				for (int i=0; i<3; i++){
					new JellyBubble(x,y,5 + Calc.random(5));
				}
				if (dieCount >= 120){
					Global.event[0] = 1;
					Global.refreshIconMap();
					
					Global.dropLoot(x, y, 60);
					if (Global.playerHealth == Global.playerMaxHealth)
						Global.dropLoot(x, y, 55);
					Global.dropRandomLoot(x, y, 15);
					
					for (int i=0; i<36; i++){
						JellyBubble j = new JellyBubble(x,y,12);
						j.setDirSpeed(i * 10, 12);
						j = new JellyBubble(x,y,12);
						j.setDirSpeed(i * 10, 8);
					}
					
					Sound.play("sJellyHitCry");
					Sound.play("sJellyHurtCry");
					Sound.play("sJellyReturn");
					
					Audio.get("musBoss01").setPitch(1);
					Music.changeMusic("musCave");
					
					Global.heal();
					
					new JellyGhost(320,240);
					
					destroy();
				}
			}
			else
				size = Calc.approach(size, 1, 16);
		}
		
		xx += 1;
		if (phase != 1 && phase != 3 && phase != 4)
			setFloat(5.0 * Math.sin(xx / 30.0));
		
		if (phase == 0 && !crying){
			int amt = (int)Calc.random(3);
			for (int i=0; i<amt; i++){
				new JellyBubble(x + Calc.rangedRandom(8), y + Calc.rangedRandom(8));
			}
			
			Hud.hideAll();
			if (y > 250)
				vspeed = Calc.approach(vspeed, 0, 24);
		}
		else if (phase == 1){
			visible = true;
			
			new JellyBubble(x + Calc.rangedRandom(8), y + Calc.rangedRandom(8),4);
			
			moveTo(512, 160, 18 + ((double)health / 4.0));
			if (Calc.pointDistance(x, y, 512, 160) < 8){
				hitsThisCycle = 0;
				stunTimer = 0;
				aniTimer = 0;
				phase = 2;
				subphase = 0;
				count = 0;
				timer = (double)health * 1.5;
				awaitBubble = false;
			}
		}
		else if (phase == 2){
			//new JellyBubble(x + Calc.rangedRandom(8), y + Calc.rangedRandom(8),2);
			
			JellyRender.me.intensity = Calc.approach(JellyRender.me.intensity, 1, 2);
			JellyRender.me.intensity -= .1;
			if (JellyRender.me.intensity < 1)
				JellyRender.me.intensity = 1;
			
			if (subphase == 1){
				setFloat(12.0 * Math.sin(xx / 10.0));
				imageSingle = 1;
			}
			else{
				setFloat(8.0 * Math.sin(xx / 20.0));
				imageSingle = 0;
			}
			
			timer -= 1;
			if (timer <= 0){
				if (subphase == 0){ //indicator
					Sound.playPitched("sJellyBurstBegin",.05);
					indicateBurst = true;
					Point a = calcRandomRoom(indicateR + 16, 150);
					
					if (count > 8){
						a.x = (Integer)pissChoose(70 + (int)indicateR, 570 - (int)indicateR);
					}
					else
						a.x = (int)Calc.approach(Player.me.x, a.x, Math.max(1, 1 + pissRandom(4) - ((10.0 - (double)health)/5.0)));
					
					setIndicate(a.x, a.y);
					
					subphase = 1;
					timer = 32 + ((double)health * 1.2);
					
					new JellyBigBubble(indicateX, indicateY, timer, (count == 0));
				}
				else if (subphase == 1){
					//Overlay.fadeOut(1, 1, 1, 1, 12 - ((double)health * .7));
					
					Sound.playPitched("sJellyWarp",.05);
					
					JellyRender.me.intensity = 50;
					JellyRender.me.glitchReset();
					
					indicateBurst = false;
					
					double lastX = x, lastY = y;
					
					Point a = calcRandomRoom(38, 150);
					while (Math.abs(a.x - indicateX) < indicateR + 28 || Calc.pointDistance(lastX, lastY, a.x, a.y) < 80)
						a = calcRandomRoom(38, 150);
					goTo(a.x, a.y);
					
					if (count > 8){
						goTo(indicateX, indicateY + indicateR + 56);
						awaitBubble = true;
						indicateBurst = false;
					}
					
					jellyLine(lastX, lastY, x, y);
					
					for (int i=0; i<6; i++){
						JellyBubble j = new JellyBubble(x + Calc.rangedRandom(8), y + Calc.rangedRandom(8),8);
						j.life /= 2;
						j = new JellyBubble(x + Calc.rangedRandom(8), y + Calc.rangedRandom(8),6);
						j.life /= 2;
						j = new JellyBubble(x + Calc.rangedRandom(8), y + Calc.rangedRandom(8),4);
						j.life /= 2;
						j = new JellyBubble(x + Calc.rangedRandom(8), y + Calc.rangedRandom(8),2);
						j.life /= 2;
						new JellyBubbleSmall(x + Calc.rangedRandom(4), y + Calc.rangedRandom(4),6);
						new JellyBubbleSmall(x + Calc.rangedRandom(4), y + Calc.rangedRandom(4),5);
						new JellyBubbleSmall(x + Calc.rangedRandom(4), y + Calc.rangedRandom(4),4);
					}
					
					count += 1;
					
					if (!awaitBubble)
						timer = 30 + ((double)health * 1.0);
					else
						timer = 100000;
					subphase = 0;
				}
			}
		}
		
		//drawing the indicator circle thing
		if (count % 2 == 0)
			indicateA += 20 - health;
		else
			indicateA -= 20 - health;
		
		Audio.get("sJellySpawnWhistle").setPitch(1.15 + Calc.dirY(.3, indicateA));
		
		if (indicateBurst){
			JellyBubble i =new JellyBubble(indicateX + Calc.dirX(indicateR, indicateA) + Calc.rangedRandom(4), indicateY + Calc.dirY(indicateR, indicateA) + Calc.rangedRandom(4),.2, (count == 0));
			i.life /= 2;
			i = new JellyBubble(indicateX + Calc.dirX(indicateR, indicateA + 180) + Calc.rangedRandom(4), indicateY + Calc.dirY(indicateR, indicateA + 180) + Calc.rangedRandom(4),.2, (count == 0));
			i.life /= 2;
			
			Audio.fade("sJellySpawnWhistle", .9, .3);
		}
		else{
			Audio.fade("sJellySpawnWhistle", 0, .1);
		}
		
		//moving to x and y
		if (phase != 0){
			x = Calc.approach(x, moveX, moveDiv);
			y = Calc.approach(y, moveY, moveDiv);
		}
		
		super.step();
		
		if (phase == 3 || phase == 4)
			setFloat(0);
		
		if (phase == 4){
			stunTimer -= 1;
			if (stunTimer <= 0){
				phase = 1;
				Sound.playPitched("sJellyReturn",.05);
			}
			
			if (hurt <= 0){
				aniTimer += 1;
				if (aniTimer > 7){
					if (imageSingle == 0)
						imageSingle = 1;
					else{
						imageSingle = 0;
						angle += 90;
					}
				}
			}
			else{
				imageSingle = 2;
				angle = 0;
			}
		}
		else
			angle = 0;
		
		if (phase == 4)
			playerCollisionDamagesMe();
		
		if (crying){
			cryImage += 4.5/30.0;
			if (cryImage >= 2)
				cryImage -= 2;
			
			imageSingle = Math.floor(cryImage) + 1;
			
			new JellyTear(x, y-8);
		}
	}
	
	public Point calcRandomRoom(double center, double heightSub){
		return new Point((int)(64 + center + pissRandom(512 - (center*2))), (int)(96 + center + pissRandom(320 - (center*2) - heightSub)));
	}
	
	public void hurtPlayer(){
		if (phase == 4){
			Player.hurtPlayer(50);
			stunTimer /= 2;
		}
	}
	
	public void landDamage(){
		if (phase == 4){
			Player.invincible = 1;
			Player.me.jump();
			Player.me.vspeed -= .7;
			getHurt();
		}
	}
	
	public void gunDamage(){
		if (phase == 4){
			getHurt();
		}
	}
	
	public void bombDamage(){
		if (phase == 4){
			getHurt();
		}
	}
	
	public void getHurt(){
		new SmallMessage(this, 0, -18, (String)Calc.choose("OW!", "ouch!", "oof!", "jeez!", "waah!"), 16);
		
		health -= 1;
		hitsThisCycle += 1;
		
		Sound.playPitched("sJellyGlassShatter",.05);
		Sound.playPitched("sJellyHitCry",.05);
		Sound.playPitched("sJellyReturn",.05);
		
		hurt = 10;
		
		if (hitsThisCycle >= 4){
			phase = 1;
		}
		else if (health > 0){
			//horizontalJumpLimit(90.0 + ((1 - ((double)health/10.0))*30.0));

			for (int i=0; i<16; i++){
				new JellyBubbleSmall(x + Calc.rangedRandom(4), y + Calc.rangedRandom(4), 12);
				new JellyBubble(x + Calc.rangedRandom(4), y + Calc.rangedRandom(4), 10);
				new JellyBubble(x + Calc.rangedRandom(4), y + Calc.rangedRandom(4), 6);
			}
			
			double lastX = x;
			//jesus christ im so lazy
			double add = (99.0 + ((1 - ((double)health/10.0))*(60.0 + pissRandom(25)))) * (Double)pissChoose(1.0,-1.0);
			x = lastX + add;
			if (x < 80 || x > 560){
				add *= -1;
				x = lastX + add;
			}
			
			goTo(x, y);
			
			jellyLine(lastX,y,x,y);
			
			stunTimer += 32;
		}
		else{
			phase = 69;
			
			moveTo(320,256,10);
			hurt = 0;
			
			cryTimer = 1;
			
			/*Player.control = false;
			Player.me.cutMove = true;
			Player.me.cutRight = false;
			Player.me.cutToX = Player.me.x + 1;*/
		}
	}
	
	public void horizontalJump(){
		size = 2;
		
		double lastX = x;
		//jesus christ im so lazy
		x = 80 + pissRandom(480);
		while (Calc.pointDistance(x,0,Player.me.x,0) < 48)
			x = 80 + Calc.random(480);
		
		goTo(x, y);
		
		jellyLine(lastX,y,x,y);
	}
	
	public void horizontalJumpLimit(double limit){
		size = 2;
		
		double lastX = x;
		//jesus christ im so lazy
		x = 80 + pissRandom(480);
		while (Calc.pointDistance(x,0,Player.me.x,0) < 48 || Calc.pointDistance(lastX, 0, x, 0) > limit || Calc.pointDistance(lastX, 0, x, 0) < limit*.7)
			x = 80 + Calc.random(480);
		
		goTo(x, y);
		
		jellyLine(lastX,y,x,y);
	}
	
	public void jellyLine(double lastX, double lastY, double x, double y){
		double dist = Calc.pointDistance(lastX, lastY, x, y), dir = Calc.pointDirection(lastX, lastY, x, y), gone = 0;
		while (gone < dist){
			JellyBubble j = new JellyBubble(lastX + Calc.rangedRandom(3) + Calc.dirX(gone, dir), lastY + Calc.rangedRandom(3) + Calc.dirY(gone, dir), .5);
			j.life *= .7;
			gone += 16;
		}
	}
	
	public void moveTo(double x, double y, double d){
		moveX = x;
		moveY = y;
		moveDiv = d;
	}
	
	public void goTo(double x, double y){
		this.x = x;
		this.y = y;
		moveX = x;
		moveY = y;
	}
	
	public void setIndicate(double x, double y){
		indicateX = x;
		indicateY = y;
	}
	
	public void setIndicate(double x, double y, double r){
		indicateX = x;
		indicateY = y;
		indicateR = r;
	}
	
	public double getDrawY(){
		return y + drawY;
	}
	
	public void renderBack(){
		xscale *= size; yscale *= size;
		backSprite.render(imageSingle, orientation, x, y + drawY, xscale*dieSize, yscale*dieSize, angle, alpha, 1, 1, 1);
		xscale = 1; yscale = 1;
	}
	
	public void renderFront(){
		xscale *= size; yscale *= size;
		sprite.render(imageSingle, orientation, x, y + drawY, xscale*dieSize, yscale*dieSize, angle, alpha, 1, 1, 1);
		Sprite.get("sJellyManBreak").render(Math.min(10, Math.max(health, 0)), orientation, x, y + drawY, xscale*dieSize, yscale*dieSize, angle, alpha, 1, 1, 1);
		xscale = 1; yscale = 1;
	}
	
	public void render(){
		if (!stepActive)
			Audio.fade("sJellySpawnWhistle", 0, .1);
	}

}
