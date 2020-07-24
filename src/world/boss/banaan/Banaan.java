package world.boss.banaan;

import audio.Audio;
import world.Fg;
import world.boss.BossAnnounce;
import world.control.EV;
import world.control.Global;
import world.control.Music;
import world.control.PseudoGenerator;
import world.control.Sound;
import world.control.SpriteLoader;
import world.enemy.BasicEnemy;
import world.player.Player;
import graphics.Sprite;
import main.Calc;

public class Banaan extends BasicEnemy{
	//gameplay stuff
	public double health;
	public final int MAXHEALTH = 4;
	public boolean weakened;
	//visual
	public double intense,toXscale;
	public double[] dupeY, dupeXsc;
	public int[] dupeId;
	
	public int toId;
	public int surprise;
	public boolean controlled;
	//control
	public static Banaan me;
	public boolean pre;
	
	//attack stuff
	public double[] puke;
	public double pukeSpeed;
	public boolean[] pukeDupe;
	
	public double distance;
	public double requiredDistance;
	
	public int count;
	public int lastMove;
	
	public int invuln;
	
	public int scorchPhase;
	public int fuck;
	
	public final int BOTTOMROW = 368;
	
	public boolean lastRight;
	public boolean lastEleven;
	public int scorched;
	
	//god
	public PseudoGenerator myGen;
	
	public int lootCalc;
	public int times;
	
	public boolean death;

	public Banaan(double x, double y) {
		super(x, y);
		health = MAXHEALTH;
		
		alarmInitialize(10);
		
		new SpriteLoader("sBanaanPuk_10","sBanaan_2","sBanaanNotice","sBanaanOutline");
		myGen = new PseudoGenerator(2.5,4.12,.6459,13.0,25.1,1.4,69.0);
		
		pre = true;
		
		times = 0;
		
		me = this;
		
		invuln = 0;
		
		death = false;
		
		fuck = 0;
		
		lootCalc = 0;
		
		weakened = false;
		lastRight = true;
		lastEleven = false;
		
		surprise = 0;
		lastMove = -1;
		
		pukeSpeed = 0;
		distance = 0;
		requiredDistance = 0;
		
		scorchPhase = -1;
		scorched = 0;
		
		count = 0;
		toId = 0;
		controlled = false;
		
		puke = new double[3];
		pukeDupe = new boolean[3];
		for (int i=0; i<3; i++){
			puke[i] = 0;
			pukeDupe[i] = false;
		}
		
		dupeY = new double[2];
		dupeXsc = new double[2];
		dupeId = new int[2];
		for (int i=0; i<2; i++){
			dupeY[i] = -69;
			dupeXsc[i] = 0;
			dupeId[i] = -1;
		}
		
		imageSingle = 0;
		imageSpeed = 0;
		
		intense = 0;
		
		toXscale = 1;
		
		sprite = Sprite.get("sBanaan");
		orientation = Sprite.CENTERED;
		mask = sprite.mask;
		
		setCollisionGroup(Global.BASICENEMY, Global.DEACTIVATEME);
		setDepth(0);
	}
	
	public void calculateLoot(int split){
		lootCalc = (int)Math.ceil(((Global.luck * Player.me.random(25)) + (Calc.boolToInt(Global.playerHealth == Global.playerMaxHealth) * 150) + 120)/(double)split);
	}
	
	public void hurtScreech(){
		Audio.get("sBanaanHitChime").setPitch(1 + (invHealthPercent()*.4));
		Audio.get("sBanaanHitScream").setPitch(1 + (invHealthPercent()*.4));
		Sound.play("sBanaanHitChime");
		Sound.play("sBanaanHitScream");
	}
	
	public void gunDamage(){
		if (weakened && invuln <= 0){
			hurtScreech();
			health -= .1;
			invuln = 6;
			Fg.me.shakeTimer = 6;
		}
	}
	
	public void bombDamage(){
		//??
	}
	
	public void landDamage(){
		if (weakened){
			hurtScreech();
			health -= 1;
			invuln = -1;
			weakened = false;
			shatter(x, y, 1);
			Player.me.jump();
			Player.me.vspeed -= .7;
			Sound.play("sLandOnEnemy");
			visible = false;
			if (health > 0){
				x = 224;
				y = 168;
				xscale = 0;
				toId = -1;
				alarm[3] = 5 + (int)(25 * healthPercent());
				scorchPhase = 0;
			}
			else{
				death = true;
				Audio.get("sBanaanDed").setGain(.8);
				Audio.fade("sBanaanDed", .8, 1);
				Sound.play("sBanaanDed");
				calculateLoot(13);
				Global.dropExactLoot(x, y, lootCalc);
				deathJump();
				Music.fadeMusic("musWent", true);
				alarm[6] = 20;
			}
		}
	}
	
	public void deathJump(){
		setPos(80 + (Math.floor(myGen.random(16)) * 32), (Double)myGen.choose(288.0,320.0));
	}
	
	public void hurtPlayer(){
		if (!death)
			Player.hurtPlayer(69);
	}
	
	public void alarmEvent(int i){
		if (i == 0){
			shatter(x, y, 0);
			visible = false;
			toXscale = 0;
			xscale = 0;
			
			Audio.get("sSkullQuake").setGain(1);
			Audio.fade("sSkullQuake", 1, 1);
			Audio.get("sSkullQuake").setLooping(true);
			Sound.play("sSkullQuake");
			
			x = 560;
			y = BOTTOMROW;
			
			alarm[1] = 60;
			
			
			Fg.me.shakeTimer = 80;
		}
		else if (i == 1){
			pre = false;
			
			visible = true;
			toXscale = 1;
			
			Audio.fade("sSkullQuake",0,.02);
			
			Music.changeMusic("musBoss03");
			Global.addTweet("hem hem!!!!!!!!! xD");
			new BossAnnounce("BANAAN",Global.roomColor,"sBossTextBanaan");
			
			Player.control = true;
			Player.me.cutMove = false;
			
			alarm[2] = 30;
		}
		else if (i == 2){
			distance = 0;
			count = -1;
			complete();
		}
		else if (i == 3){
			invuln = 60;
			visible = true;
			xscale = 0;
			//this is for scorching
			scorchPhase = 1;
			alarm[4] = 5 + (int)(25 * healthPercent());
		}
		else if (i == 4){
			final double spdup = .21;
			
			if (scorched == 0){
				Audio.get("sSkullQuake").setGain(1);
				Audio.fade("sSkullQuake", 1, 1);
				Audio.get("sSkullQuake").setLooping(true);
				Sound.play("sSkullQuake");
				Audio.get("sBanaanScorch").setGain(.8);
				Audio.fade("sBanaanScorch", .8, 1);
				Audio.get("sBanaanScorch").setPitch(1 + (invHealthPercent() * spdup));
				Sound.play("sBanaanScorch");
				fuck = -4;
			}
			
			Fg.me.shakeTimer = 16;
			
			double xx = 592 - (32 * scorched);
			if (!lastRight)
				xx = 44 + (32 * scorched);
			
			double spd = 5.2 + (invHealthPercent() * 3.8) + ((double)scorched/8.0);
			
			new BanaanVPuker(xx, 240, spd, 128);
			int dist = -3 - (int)Math.ceil(health) + fuck;
			if (Math.abs(dist) > 1 && scorched + Math.abs(dist) < 17)
				new BanaanVPuker(xx + (dist * 32), 240, spd, 128);
			
			if (scorched == 0){
				for (int j=0; j<17 + dist; j++)
					new BanaanVPuker(44 + (j * 32), 240, spd, 128);
			}
			
			if (fuck < 0)
				fuck += 2;
			
			Sound.playPitched("sBanaanKopiEnd",.04);
			
			scorched += 1;
			lastEleven = !lastEleven;
			
			if (scorched < 17){
				alarm[4] = (int)(11 * (1 - (invHealthPercent() * spdup)));
				if (lastEleven)
					alarm[4] = (int)(12 * (1 - (invHealthPercent() * spdup)));
			}
			else{
				Audio.fade("sSkullQuake",0,.02);
				shatter(x,y,0);
				visible = false;
				alarm[5] = 25 + (int)(25 * healthPercent());
			}
		}
		else if (i == 5){
			scorched = 0;
			visible = true;
			xscale = 0;
			x = 560;
			y = BOTTOMROW;
			/*if (lastRight)
				x = 80;*/
			scorchPhase = -1;
			toId = 0;
			complete();
			complete();
			//lastRight = !lastRight;
		}
		else if (i == 6){
			visible = true;
			xscale = 0;
			alarm[7] = 10;
		}
		else if (i == 7){
			visible = false;
			times += 1;
			Global.dropExactLoot(x, y, lootCalc);
			shatter(x, y, 1);
			deathJump();
			
			if (times >= 13){
				Global.event[EV.BANAAN_DEFEAT] = 1;
				Global.refreshIconMap();
				Global.addTweet("#BANAAN defeated motherfucker!!!!!");
				alarm[9] = 30;
			}
			else
				alarm[6] = 20;
		}
		else if (i == 8){
			Global.blockFade(false);
			Music.fadeMusic(Global.roomMusic, true);
		}
		else if (i == 9){
			Global.heal();
			alarm[8] = 30;
		}
	}
	
	/**do a thing when distance meets requiredDistance*/
	public void complete(){
		//fuck off
		if (weakened) //wear the weaken off
			shatter(x, y, 1);
		
		weakened = false;
		
		visible = true;
		
		count += 1;
		double pukeLen = (60 + (invHealthPercent() * 17)), restLen = (112 + (invHealthPercent() * 5));
		
		pukeSpeed = 2.02 + (3.02 * invHealthPercent());
		
		double a = 0, b = 0, c = 0;
		int d = (int)myGen.random(3);
		if (lastMove == 2)
			d = (int)myGen.random(2);
		
		if (d == 0){
			a = 1;
			c = 1;
		}
		else if (d == 1){
			a = 1;
			b = 1;
		}
		else if (d == 2){
			c = 1;
			b = 1;
		}
		
		lastMove = d;
		
		if (count > 7 + (invHealthPercent() * 7)){
			count = -2;
			weakened = true;
			toId = 0;
			distance = 0;
			requiredDistance = 530 + (invHealthPercent() * 110);
			
			shatter(x, y, 0);
			Sound.playPitched("sBanaanFck",.05);
			//weaken sound
		}
		else if (count >= 0)
			puke(pukeLen + restLen, pukeLen * a, pukeLen * b, pukeLen * c);
	}
	
	public void destroy(){
		me = null;
		super.destroy();
	}
	
	public void roomDestroy(){
		me = null;
		super.destroy();
	}
	
	public void puke(double d, double... a){
		requiredDistance = d;
		distance = 0;
		
		if (a[0] > 0||a[1] > 0||a[2] > 0){
			Sound.playPitched("sBanaanKopiAvOw", .02);
			
			//this will alter the position of the primary banaan
			if (!controlled){
				int b = -1;
				while (b == -1 || a[b] <= 0)
					b = (int)Calc.random(3);
				toId = b;
				controlled = true;
			}
			
			//determine which slots need "dupe" banaans
			boolean[] need = new boolean[3];
			for (int i=0; i<3; i++)
				need[i] = true;
			need[toId] = false;
			
			for (int i=0; i<3; i++){
				if (a[i] <= 0)
				need[i] = false;
			}
			
			for (int i=0; i<2; i++){
				if (dupeId[i] != -1)
					need[dupeId[i]] = false;
			}
			//placing "dupe" banaans
			for (int i=0; i<3; i++){
				if (need[i]){
					int mine = 1;
					if (dupeId[0] == -1)
						mine = 0;
					dupeId[mine] = i;
					dupeXsc[mine] = 0;
					dupeY[mine] = BOTTOMROW - (i * 32);
					pukeDupe[i] = true;
				}
			}
			//tell the thing to actually puke lol
			for (int i=0; i<3; i++)
				puke[i] = a[i];
		}
	}
	
	public void step(){
		if (!pre){
			//pukeStuff
			
			if (scorchPhase == -1 && !death){
				distance += pukeSpeed;
				double hsp = pukeSpeed;
				if (x > 320)
					hsp *= -1;
				
				for (int i=0; i<3; i++){
					if (puke[i] > 0){
						new BanaanPuke(x, BOTTOMROW - (i * 32), hsp);
						
						puke[i] -= pukeSpeed;
						if (puke[i] <= 0){
							puke[i] = 0;
							if (pukeDupe[i]){
								int myDupe = 0;
								if (dupeId[1] == i)
									myDupe = 1;
								
								shatter(x, dupeY[myDupe], 0);
								
								dupeY[myDupe] = -69;
								dupeXsc[myDupe] = 0;
								dupeId[myDupe] = -1;
								
								pukeDupe[i] = false;
							}
							else
								controlled = false;
						}
					}
				}
				
				if (distance >= requiredDistance && requiredDistance > 0){
					complete();
				}
			}
			
			//dupeStuff
			for (int i=0; i<2; i++)
				dupeXsc[i] = Calc.approach(dupeXsc[i], (double)Calc.boolToInt(dupeId[i] != -1), 4);
			if (toId >= 0 && toId <= 2 && !death)
				y = Calc.approach(y, BOTTOMROW - (toId * 32.0), 2);
			
			if (invuln > -1)
				invuln -= 1;
			if (invuln == 0)
				visible = true;
			else if (invuln > 0)
				visible = !visible;
			
			playerCollisionDamagesMe();
		}
		
		if (surprise > 0)
			surprise -= 1;
		
		intense = (intense + .01) * 1.02;
		if (intense >= 1)
			intense = 0;
		
		xscale = Calc.approach(xscale, toXscale, 4);
		
		imageSingle = Calc.boolToInt(weakened);
		
		super.step();
		
		if (death)
			imageSingle = 1;
	}
	
	/**create shattering pieces of banaan at the centered origin place thign!!!!!!!!! wow!!!! this makes 0 sense!!!!!!!!*/
	public void shatter(double x, double y, int img){
		intense = 0;
		
		Sound.jarBreakPlay();
		
		Fg.me.shakeTimer = 6;
		
		for (int i=0; i<16; i++){
			for (int j=0; j<16; j++){
				new BanaanPieces(x - 16, y - 16, i * 2, j * 2, img);
			}
		}
	}
	
	public void render(){
		double addX = Calc.rangedRandom(intense), addY = Calc.rangedRandom(intense);
		
		for (int i=0; i<2; i++){
			addX = Calc.rangedRandom(intense);
			addY = Calc.rangedRandom(intense);
			if (dupeId[i] != -1){
				out(x + addX + 1, dupeY[i] + addY, dupeXsc[i]);
				out(x + addX - 1, dupeY[i] + addY, dupeXsc[i]);
				out(x + addX, dupeY[i] + addY + 1, dupeXsc[i]);
				out(x + addX, dupeY[i] + addY - 1, dupeXsc[i]);
				sprite.render(0, orientation, x + addX, dupeY[i] + addY, dupeXsc[i], 1, 0, 1, 1, 1, 1);
			}
		}
		
		addX = Calc.rangedRandom(intense);
		addY = Calc.rangedRandom(intense);
		
		out(x - 1 + addX, y + addY, xscale);
		out(x + 1 + addX, y + addY, xscale);
		out(x + addX, y - 1 + addY, xscale);
		out(x + addX, y + 1 + addY, xscale);
		sprite.render(imageSingle, orientation, x + addX, y + addY, xscale, yscale, angle, alpha, 1, 1, 1);
		
		if (surprise > 0)
			Sprite.get("sBanaanNotice").render(0, Sprite.CENTERED, x + addX, y + addY - 2.5, 1, 1, 0, 1, 1, 1, 1);
	}
	
	public void out(double x, double y, double xscale){
		Sprite.get("sBanaanOutline").render(0, orientation, x + Calc.rangedRandom(.05), y + Calc.rangedRandom(.05), xscale, yscale, angle, alpha, 1, 1, 1);
	}
	
	public double healthPercent(){
		return Math.max(0, health)/(double)MAXHEALTH;
	}
	
	public double invHealthPercent(){
		return 1 - healthPercent();
	}

}
