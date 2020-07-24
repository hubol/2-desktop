package world.boss.slick;

import audio.Audio;
import graphics.Sprite;
import world.Fg;
import world.boss.BossAnnounce;
import world.control.Global;
import world.control.Music;
import world.control.NewGen;
import world.control.Shake;
import world.control.Sound;
import world.enemy.BasicEnemy;
import world.particles.JumpDreamPart;
import world.player.Player;
import main.Calc;
import main.Mask;
import main.Scene;

public class Slick extends BasicEnemy{
	public double[] xx, yy, aa;
	public SlickAnimation crouch, jump, divebomb, walk, stun, idle, dance, cuteIdle, animation, stab, groundVuln, groundHit, dead;
	public double approach;
	public boolean reverseLoop, cycle;
	
	public boolean smallMask;
	
	public double timer;
	public boolean a;
	
	public Shake s;
	
	public double eyelidImage;
	public boolean pupil;
	public boolean eyelidOpen;
	
	public final double maxHealth = 6; //gun opens eye, bomb -1.5, jump -1
	public double health = maxHealth;
	
	public int phase;
	
	public NewGen g;
	
	public int jumps;
	public int hurt;
	public double eyeDir, eyeDist, hurtDir;
	
	public double damageMultiplier;
	
	public double[] stabX = new double[6];
	public int stabVuln;
	public int stabShow, stabAt;
	
	public boolean stabbing, vuln;
	
	public boolean deadened, splode;
	public double sVsp;
	
	public double sin, deadMult;
	
	public final String[] stupid = new String[]{"4784FF", "F8FF20", "FFEB48"};
	
	public boolean not;
	
	public double ass;

	public Slick(double x, double y)  {
		super(x, y);
		
		ass = 0;
		
		not = true;
		
		sin = 0;
		deadMult = 1;
		
		deadened = false;
		splode = false;
		sVsp = 0;
		
		hurt = 0;
		eyeDir = 0;
		eyeDist = 0;
		hurtDir = 0;
		
		vuln = false;
		
		damageMultiplier = .4;
		
		stabbing = false;
		
		g = new NewGen(3.53719,4.14,6.375,5.29,1.12,654.7297);
		
		timer = Calc.random(60);
		a = true;
		
		mask = getMask(false);
		
		crouch = new SlickAnimation(new Integer[]{0,0,0}, new Integer[]{0,3,4}, new Integer[]{0,16,25}, new Integer[]{0,0,0}, new Integer[]{0,2,6}, new Integer[]{0,-15,-27}, new Integer[]{0,0,0}, new Integer[]{0,1,3}, new Integer[]{0,0,0}, new Integer[]{0,-3,-6}, new Integer[]{0,0,0}, new Integer[]{0,0,-1}, new Integer[]{0,4,7}, new Integer[]{0,0,0}, new Integer[]{0,0,0});
		jump = new SlickAnimation(new Integer[]{0,0,0}, new Integer[]{0,2,2}, new Integer[]{10,39,39}, new Integer[]{0,0,0}, new Integer[]{0,1,1}, new Integer[]{-11,-37,-37}, new Integer[]{0,0,0}, new Integer[]{0,-3,0}, new Integer[]{0,0,0}, new Integer[]{-15,-10,-10}, new Integer[]{-6,-4,-2}, new Integer[]{-38,-28,-28}, new Integer[]{16,13,10}, new Integer[]{-7,-5,-3}, new Integer[]{39,27,27});
		divebomb = new SlickAnimation(new Integer[]{0,0,0,0}, new Integer[]{3,3,2,2}, new Integer[]{19,22,9,4}, new Integer[]{0,0,0,0}, new Integer[]{4,4,2,2}, new Integer[]{-21,-25,-12,-7}, new Integer[]{0,0,0,0}, new Integer[]{1,1,-1,-1}, new Integer[]{0,0,0,0}, new Integer[]{9,5,3,0}, new Integer[]{4,4,3,3}, new Integer[]{32,19,9,1}, new Integer[]{-9,-5,-3,-2}, new Integer[]{4,4,3,3}, new Integer[]{-37,-23,-9,-3});
		walk = new SlickAnimation(new Integer[]{0,0,0,0,0}, new Integer[]{0,0,0,3,0}, new Integer[]{0,0,7,4,4}, new Integer[]{0,0,0,0,0}, new Integer[]{0,0,0,4,0}, new Integer[]{0,-7,-7,-7,-7}, new Integer[]{0,0,0,-2,0}, new Integer[]{0,-2,0,1,0}, new Integer[]{0,0,0,0,0}, new Integer[]{0,-3,-7,-9,-2}, new Integer[]{0,0,-3,0,0}, new Integer[]{0,-4,-8,0,0}, new Integer[]{4,7,7,3,3}, new Integer[]{-2,-2,0,0,0}, new Integer[]{8,5,0,0,0});
		stun = new SlickAnimation(new Integer[]{0,0,0,0,0}, new Integer[]{0,0,0,0,3}, new Integer[]{0,-10,0,20,39}, new Integer[]{0,0,0,0,0}, new Integer[]{0,0,0,0,4}, new Integer[]{-18,-33,-53,-37,-53}, new Integer[]{0,0,0,0,0}, new Integer[]{0,-2,-2,1,1}, new Integer[]{0,0,0,0,0}, new Integer[]{-8,-14,-14,-13,-5}, new Integer[]{-3,-5,-5,1,4}, new Integer[]{-18,-31,-31,-16,-4}, new Integer[]{0,-1,-1,6,13}, new Integer[]{0,3,3,2,-2}, new Integer[]{0,7,7,14,23});
		idle = new SlickAnimation(new Integer[]{0,0,0}, new Integer[]{0,2,4}, new Integer[]{0,7,15}, new Integer[]{0,0,0}, new Integer[]{0,4,4}, new Integer[]{0,-5,-12}, new Integer[]{0,0,0}, new Integer[]{0,2,2}, new Integer[]{0,0,0}, new Integer[]{0,-3,-5}, new Integer[]{0,0,0}, new Integer[]{0,0,0}, new Integer[]{0,3,5}, new Integer[]{0,0,0}, new Integer[]{0,0,0});
		dance = new SlickAnimation(new Integer[]{0,0,0,0}, new Integer[]{0,-3,-3,-6}, new Integer[]{0,9,31,41}, new Integer[]{0,0,0,0}, new Integer[]{0,-4,-4,-7}, new Integer[]{0,-8,-26,-41}, new Integer[]{0,0,0,0}, new Integer[]{0,-3,-4,-8}, new Integer[]{0,3,2,0}, new Integer[]{0,-3,-7,-11}, new Integer[]{0,0,-3,-10}, new Integer[]{0,0,-5,-18}, new Integer[]{0,6,12,19}, new Integer[]{0,-3,-7,-15}, new Integer[]{0,8,18,37});
		cuteIdle = new SlickAnimation(new Integer[]{0,0,0,0,0}, new Integer[]{0,0,2,2,8}, new Integer[]{0,7,12,19,29}, new Integer[]{0,0,0,0,0}, new Integer[]{0,0,2,2,8}, new Integer[]{0,-8,-11,-17,-28}, new Integer[]{0,0,0,0,0}, new Integer[]{0,0,2,2,5}, new Integer[]{0,0,0,0,0}, new Integer[]{0,-3,-6,-8,-8}, new Integer[]{0,0,0,0,0}, new Integer[]{0,0,0,0,0}, new Integer[]{0,2,4,8,9}, new Integer[]{0,0,0,0,0}, new Integer[]{0,0,0,0,0});
		stab = new SlickAnimation(new Integer[]{0,0,0}, new Integer[]{-9,-5,-3}, new Integer[]{0,11,30}, new Integer[]{0,0,0}, new Integer[]{-9,-5,-2}, new Integer[]{0,-9,-28}, new Integer[]{0,0,0}, new Integer[]{-10,-8,-5}, new Integer[]{0,0,0}, new Integer[]{9,9,6}, new Integer[]{-6,-6,-6}, new Integer[]{32,32,24}, new Integer[]{-9,-8,-9}, new Integer[]{-5,-5,-5}, new Integer[]{-28,-24,-28});
		groundVuln = new SlickAnimation(new Integer[]{32,32,32,32}, new Integer[]{32,32,32,32}, new Integer[]{-178,-178,-178,-178}, new Integer[]{-31,-31,-31,-31}, new Integer[]{32,32,32,32}, new Integer[]{-184,-184,-184,-184}, new Integer[]{0,0,0,0}, new Integer[]{0,0,0,0}, new Integer[]{-178,-178,-178,-178}, new Integer[]{43,48,42,34}, new Integer[]{-67,-63,-65,-68}, new Integer[]{-195,-210,-198,-183}, new Integer[]{-38,-33,-41,-47}, new Integer[]{-67,-69,-66,-62}, new Integer[]{186,172,191,205});
		groundHit = new SlickAnimation(new Integer[]{31,31,31}, new Integer[]{35,35,35}, new Integer[]{183,183,183}, new Integer[]{-31,-31,-31}, new Integer[]{35,35,35}, new Integer[]{-182,-182,-182}, new Integer[]{0,0,0}, new Integer[]{0,0,0}, new Integer[]{-178,-167,-188}, new Integer[]{49,55,42}, new Integer[]{-64,-49,-64}, new Integer[]{-205,-228,-203}, new Integer[]{-47,-33,-55}, new Integer[]{-63,-72,-48}, new Integer[]{199,180,229});
		dead = new SlickAnimation(new Integer[]{0,0,0,0,0}, new Integer[]{0,0,0,0,0}, new Integer[]{12,12,12,7,7}, new Integer[]{0,0,0,0,0}, new Integer[]{0,0,0,0,0}, new Integer[]{-16,-16,-16,-8,-8}, new Integer[]{0,0,0,0,0}, new Integer[]{0,0,0,0,0}, new Integer[]{0,-4,0,4,4}, new Integer[]{10,10,10,10,10}, new Integer[]{0,0,0,0,0}, new Integer[]{29,29,29,29,29}, new Integer[]{17,17,20,17,17}, new Integer[]{-7,-7,-14,-7,-7}, new Integer[]{31,31,44,31,31});
		//l mandible, r mandible, body, l foot, r foot
		
		animation = idle;
		
		stabShow = 0;
		stabAt = 0;
		
		setCollisionGroup(Global.DEACTIVATEME, Global.BASICENEMY);
		
		xx = new double[5];
		yy = new double[5];
		aa = new double[5];
		
		for (int i=0; i<5; i++){
			xx[i] = 0;
			yy[i] = 0;
			aa[1] = 0;
		}
		
		aa[0] = 15;
		aa[1] = -15;
		
		approach = 3.7;
		imageSingle = 0;
		imageSpeed = .15;
		reverseLoop = true;
		cycle = false;
		
		eyelidImage = 19;
		pupil = false;
		
		phase = -5;
		eyelidOpen = false;
		
		jumps = 0;
		
		s = new Shake(.1);
		
		alarmInitialize(10);
		setDepth(-1);
	}
	
	public void determineStabs(){
		stabVuln = 1 + (int)g.random(4);
		int pX = (int)g.random(5);
		for (int i=0; i<5; i++){
			if (i != pX)
				stabX[i] = 84 + g.random(456);
			else
				stabX[i] = Player.me.x;
		}
		stabX[5] = 320;
		stabShow = 0;
		stabAt = 0;
	}
	
	public void destroy(){
		s.destroy();
		super.destroy();
	}
	
	public double health(){
		return (health / maxHealth);
	}
	
	public double invHealth(){
		return 1 - health();
	}
	
	public void alarmEvent(int i){
		if (i == 0){
			Sound.playPitched("sSlickJump", .05);
			setPhase(1);
			vspeed = -16;
			animation = jump;
			imageSingle = 1;
			imageSpeed = 0;
			reverseLoop = false;
			cycle = false;
			
			jumps += 1;
			if (jumps == 3){
				damageMultiplier = 1;
				eyeOpen();
			}
			else
				damageMultiplier = .4;
		}
		else if (i == 1){
			setPhase(0);
		}
		else if (i == 2){ //what a stupid alarm
			approach = 3.7;
		}
		else if (i == 3){ //end of grund
			setPhase(4);
			Audio.fade("sSkullQuake", 0, .08);
		}
		else if (i == 4){
			setPhase(3);
		}
		else if (i == 5){
			imageSpeed = 0;
			imageSingle = 0;
			String c = "D2A0FF";
			if (stabShow != stabVuln)
				c = "F8FF20";
			
			new SlickIndicator(stabX[stabShow], 320, ""+(stabShow + 1), c, 60 + (int)(45 * health()));
			stabShow += 1;
			
			if (stabShow < 6){
				alarm[5] = 10 + (int)(10 * health());
			}
			else{
				alarm[6] = 5 + (int)(25 * health());
			}
		}
		else if (i == 6){
			approach = 1;
			alarm[2] = 2;
			
			if (phase != 5)
				setPhase(5);
			
			if (stabAt < 5){
				mask = getMask(true);
				
				x = stabX[stabAt];
				
				for (int j=0; j<8; j++)
					new SlickRockDebris(x + Calc.rangedRandom(20), 384 + (j * 3.75));
				
				stabbing = true;
				
				if (stabAt != stabVuln){
					vuln = false;
					animation = stab;
					imageSpeed = 0;
					cycle = false;
					reverseLoop = false;
					imageSingle = 0;
					
					damageMultiplier = .4;
					
					animationSetInstant(animation);
					
					eyeClose();
					
					Sound.playPitched("sSlickStab", .05);
					alarm[7] = 7 + (int)(10 * health());
				}
				else{
					vuln = true;
					
					animation = groundVuln;
					imageSpeed = .4;
					cycle = true;
					reverseLoop = true;
					imageSingle = 0;
					
					damageMultiplier = 1;
					
					animationSetInstant(animation);
					
					Sound.playPitched("sSlickVulnerable", .05);
					eyeOpen();
					mask = getMask(false);
					alarm[7] = 45 + (int)(30 * health());
				}
				stabAt += 1;
			}
			else{
				vuln = false;
				damageMultiplier = .4;
				eyeClose();
				jumps = 0;
				alarm[8] = 5 + (int)(10 * health());
			}
		}
		else if (i == 7){
			Sound.playPitched("sSlickWithdraw", .05);
			stabbing = false;
			if (!vuln)
				imageSingle = 2;
			
			alarm[6] = 10 + (int)(7 * health());
			if (vuln)
				alarm[6] = 22;
		}
		else if (i == 8){
			Sound.playPitched("sSlickJump", .05);
			Sound.playPitched("sSlickBigJump", .05);
			setPhase(1);
			vspeed = -24;
			animation = jump;
			imageSingle = 1;
			imageSpeed = 0;
			reverseLoop = false;
			cycle = false;
			
			x = 320;
			
			Fg.me.shakeTimer = 20;
			damageMultiplier = .4;
			
			mask = getMask(true);
		}
		else if (i == 9){
			splode = true;
			Audio.fade("sSlickOutro", 0, .005);
			new SlickLoot(320, 240, 120, 80);
			
			for (int y = 0; y<18; y++){
				for (int z=0; z<3; z++){
					JumpDreamPart j = new JumpDreamPart(x + Calc.rangedRandom(15), this.y + Calc.rangedRandom(15) + (8 * Math.sin(sin / 15.0)), stupid[z]);
					j.setDepth(0);
					j.setDirSpeed(Calc.random(360), Calc.random(6));
				}
			}
			Sound.playPitched("sSlickFinal", .05);
			
			Sound.explodePlay();
			Sound.explodePlay();
			
			Global.heal();
			
			destroy();
			
			final double f = 8 * Math.sin(sin / 15.0);
			final double fuckY = -15;
			new SlickBrokenExplode(x + xx[0] - 16, y + f + yy[0] - 16 + fuckY, aa[0], 4); //mand 0
			new SlickBrokenExplode(x + xx[1] + 16, y + f + yy[1] - 16 + fuckY, aa[1], 1); //mand 1
			new SlickBrokenExplode(x + xx[2], y + f + yy[2] + fuckY, aa[2], 0); //bod 2
			new SlickBrokenExplode(x + xx[3] - 16, y + f + yy[3] + 32 + fuckY, aa[3], 3); //foot 3
			new SlickBrokenExplode(x + xx[4] + 16, y + f + yy[4] + 32 + fuckY, aa[4], 2); //ft 4
			
			Fg.me.shakeTimer = 8;
			
			Audio.get("sSlickSqwak").stop();
		}
	}
	
	public void eyeOpen(){
		if (!eyelidOpen){
			Sound.playPitched("sSlickEyeOpen", .05);
			eyelidOpen = true;
		}
	}
	
	public void eyeClose(){
		if (eyelidOpen){
			Sound.playPitched("sSlickEyeClose", .05);
			eyelidOpen = false;
		}
	}
	
	public void reset(){
		String s = "sSlickSpark"+(Calc.boolToInt(a) + 1);
		if (Audio.soundExists(s)){
			final double g = .4;
			Audio.get(s).setGain(g);
			Audio.fade(s, g, 1);
			Audio.get(s).setPitch(.9 + Calc.random(.75));
			Sound.play(s);
		}
		int id = (int)Calc.random(5);
		if (vuln)
			id = 2;
		double amt = 1 + Calc.random(5);
		for (int i=0; i<amt; i++){
			double dir = Calc.random(360), dist = Calc.random(16);
			new SlickSpark(xx[id] + x + Calc.dirX(dist, dir), yy[id] + y + Calc.dirY(dist, dir));
		}
		timer = 8 + Calc.random(22);
		a = !a;
	}
	
	public void setPhase(int i){
		if (i == 0){
			Sound.playPitched("sSlickCrouch", .05);
			alarm[0] = 45 - (int)(15 * invHealth());
		}
		else if (i == 1){
			//this might have something to do with jumping??
		}
		else if (i == 2){
			
		}
		else if (i == 3){
			//return to grund
			eyeClose();
			
			Audio.fade("sSkullQuake", 1, 1);
			Audio.get("sSkullQuake").setGain(1);
			Audio.get("sSkullQuake").setLooping(true);
			Sound.play("sSkullQuake");
			
			animation = stab;
			cycle = true;
			reverseLoop = true;
			imageSpeed = .35;
			
			mask = getMask(true); //stab mask
			vspeed = 1.9;
			alarm[3] = 60;
		}
		else if (i == 4){
			//stab prep (numbers)
			vspeed = 0;
			eyeClose();
			
			determineStabs();
			alarm[5] = 30;
		}
		else if (i == 5){
			//stab!!
		}
		phase = i;
	}
	
	public void step(){
		if (Audio.soundExists("sSlickSqwak"))
			Audio.get("sSlickSqwak").setPitch(1 + Calc.rangedRandom(.25));
		
		if (!eyelidOpen){
			if (phase == -5)
				eyelidImage -= .6;
			else
				eyelidImage -= 1.1;
			if (eyelidImage < 0){
				eyelidImage = 0;
				if (phase == -5){
					Music.changeMusic("musBoss04");
					Global.addTweet("golden robot crab fight wtf!!!!");

					Audio.fade("sSlickIntro",0,.05);
					
					new BossAnnounce("SLICK","F8FF20","sBossTextSlick");
					Player.control = true;
					Player.me.cutMove = false;
					
					pupil = true;
					phase = -4;
					alarm[1] = 35;
				}
			}
		}
		else{
			eyelidImage += .8;
			if (eyelidImage > 19)
				eyelidImage = 19;
		}
		
		if (phase == 0){
			if (animation != crouch){
				animation = crouch;
				imageSingle = 0;
				imageSpeed = .2 + (.15 * invHealth());
				reverseLoop = false;
				cycle = false;
			}
		}
		
		if (vspeed < -18 && !deadened){
			for (int i=0; i<4; i++)
				new SlickRockDebris(x + Calc.rangedRandom(20), Math.max(384, y));
		}
		
		timer -= 1;
		if (timer <= 0)
			reset();
		
		animation();
		
		if (deadened && !splode){
			for (int i=0; i<9; i++)
				alarm[i] = -1;
			
			sin += 1;
			vspeed = -Math.max(0, deadMult);
			deadMult -= .01;
			
			if (alarm[9] > -1 && alarm[9] < 20)
				visible = !visible;
			else
				visible = true;
			
			ass -= 1;
			if (ass <= 0){
				Fg.me.shakeTimer = 4;
				new SlickPop(x + Calc.rangedRandom(18), y + Calc.rangedRandom(18));
				ass = Calc.random(5 + (25 * Math.max(0, deadMult)));
			}
			
			for (int i=0; i<3; i++){
				JumpDreamPart j = new JumpDreamPart(x + Calc.rangedRandom(15), y + Calc.rangedRandom(15) + (8 * Math.sin(sin / 15.0)), stupid[i]);
				j.setDepth(0);
			}
			
			if (deadMult <= 0 && not){
				alarm[9] = 110;
				not = false;
			}
		}
		
		alarmStep();
		
		if (phase == 1){
			vspeed += .8;
			
			if (vspeed > 0 && jumps == 0 && !smallMask)
				mask = getMask(false);
			
			if (vspeed > 0 && hurt <= 0){
				imageSingle = 0;
			}
			if (y >= 352 && vspeed > 0){
				vspeed = 0;
				y = 352;
				setPhase(2);
				
				approach = 2;
				alarm[2] = 5;
				
				animation = idle;
				imageSingle = 0;
				imageSpeed = .15;
				reverseLoop = true;
				cycle = false;
				
				Fg.me.shakeTimer = 15;
				Sound.playPitched("sSlickGroundCrash", .05);
				
				boolean[] rock = new boolean[8];
				double h = health();
				if (h > .7){ //above player
					double i = ((Player.me.x - 80) / 64.0);
					rock[(int)i] = true;
					if (h < .8){
						int a = (int)Math.round(i);
						if (a > 7)
							a = 6;
						rock[a] = true;
					}
				}
				else{ //patterned
					boolean a = (g.random(1) > .5);
					for (int i=0; i<8; i++){
						rock[i] = a;
						a = !a;
					}
				}
				/*else{ //patterned + entire player side
					boolean a = (g.random(1) > .5);
					for (int i=0; i<8; i++){
						rock[i] = a;
						a = !a;
					}
					int b = 0;
					if (Player.me.x > x)
						b = 4;
					for (int i=0; i<4; i++)
						rock[b + i] = true;
				}*/
				
				for (int i=0; i<8; i++){
					if (rock[i])
						new SlickBoulder(96 + (i * 64), this);
				}
				
				if (jumps < 4)
					setPhase(0);
				else{
					//setPhase(3);
					phase = 400;
					alarm[4] = 60;
				}
			}
			
			if (Player.me.vspeed < 0 && Scene.collision(this, x, y + vspeed, Global.PLAYER) && eyelidOpen)
				hurt(1);
		}
		
		x += hspeed;
		y += vspeed;
		
		if (vuln && Player.me.vspeed > 0 && Scene.collision(this, x, y - 1, Global.PLAYER) && eyelidOpen && !deadened){
			alarm[7] = 45;
			hurt(1);
			Player.invincible = 1;
			Player.me.jump();
			Player.me.vspeed -= .8;
			Sound.play("sLandOnEnemy");
		}
		
		if (Scene.collision(this, x, y, Global.PLAYER) && hurt <= 0 && !vuln && ((phase == 5 && y < 410)||phase != 5) && !deadened)
			Player.hurtPlayer(75);
		
		if (phase == 3){
			for (int i=0; i<2; i++)
				new SlickRockDebris(x + Calc.rangedRandom(20), 384);
		}
		
		if (phase == 5){
			if (stabbing)
				y = Calc.approach(y, 368, 2);
			else{
				if (!vuln)
					y = Calc.approach(y, 466, 3);
				else
					y = Calc.approach(y, 466, 6);
			}
		}
		
		if (!deadened && vspeed < 0){
			JumpDreamPart j;
			for (int i=0; i<3; i++){
				j = new JumpDreamPart(x + Calc.rangedRandom(15), this.y + Calc.rangedRandom(15) + (8 * Math.sin(sin / 15.0)), stupid[0]);
				j.size = 2 + Calc.random(8);
			}
		}
	}
	
	public Mask getMask(boolean stab){
		smallMask = !stab;
		
		if (!stab)
			return new Mask(36,36,18,18);
		return new Mask(38,94,19,75);
	}
	
	public void animation(){
		imageSingle += imageSpeed;
		if (imageSingle >= animation.size){
			if (reverseLoop){
				imageSingle = animation.size - 1 - (imageSingle % animation.size);
				if (imageSpeed > 0)
					imageSpeed *= -1;
			}
			else{
				if (cycle)
					imageSingle = imageSingle % animation.size;
				else
					imageSingle = animation.size - 1;
			}
		}
		else if (imageSingle < 0){
			if (reverseLoop){
				if (imageSpeed < 0)
					imageSpeed *= -1;
				imageSingle = Math.abs(imageSingle) % animation.size;
			}
			else{
				if (cycle){
					while (imageSingle < 0)
						imageSingle += animation.size;
				}
				else
					imageSingle = 0;
			}
		}
		
		if (!splode){
			for (int i=0; i<5; i++){
				xx[i] = Calc.approach(xx[i], animation.get(i * 3, (int)imageSingle), approach);
				yy[i] = Calc.approach(yy[i], animation.get((i * 3) + 1, (int)imageSingle), approach);
				aa[i] = Calc.approach(aa[i], animation.get((i * 3) + 2, (int)imageSingle), approach);
			}
		}
		else{
			for (int i=0; i<5; i++){
				xx[i] += (2 - i + Calc.rangedRandom(.2)) * 4;
				yy[i] += (1 + (i / 25.0)) * sVsp;
				aa[i] += (2 - i + Calc.rangedRandom(.2)) * 4 * sVsp;
			}
			sVsp += .8;
		}
		
		if (hurt == 60 || hurt == 55 || hurt == 45)
			new SlickPop(x + Calc.rangedRandom(18), y + Calc.rangedRandom(18));
		
		hurt -= 1;
		if (hurt > 0 || deadened){
			if (!deadened)
				visible = !visible;
			hurtDir += 60;

			eyeDir = Calc.approach(eyeDir, hurtDir, 2);
			eyeDist = Calc.approach(eyeDist, 6.5, 3);
		}
		else{
			visible = true;
			hurtDir = 0;
			
			eyeDir = Calc.pointDirection(x + xx[2], y + yy[2], Player.me.x, Player.me.y);
			eyeDist = Calc.approach(eyeDist, Math.min(7.7, 2 + (Calc.pointDistance(x + xx[2], y + yy[2], Player.me.x, Player.me.y) / 40.0)), 7);
		}
		
		
	}
	
	public void animationSetInstant(SlickAnimation s){
		for (int i=0; i<5; i++){
			xx[i] = Calc.approach(xx[i], s.get(i * 3, (int)imageSingle), approach);
			yy[i] = Calc.approach(yy[i], s.get((i * 3) + 1, (int)imageSingle), approach);
			aa[i] = Calc.approach(aa[i], s.get((i * 3) + 2, (int)imageSingle), approach);
		}
	}
	
	public void render(){
		//l mandible, r mandible, body, l foot, r foot
		double xxx = s.x + Calc.rangedRandom(.125), yyy = s.y + Calc.rangedRandom(.125);
		
		if (deadened && !splode){
			yyy += 8 * Math.sin(sin / 15.0);
		}
		
		//feet
		Sprite.get("sSlickFoot").render(0, Sprite.SOUTH, x + xxx + 16 + xx[4] + Calc.rangedRandom(.125), y + yyy + 32 + Calc.rangedRandom(.125) + yy[4], 1, 1, angle + aa[4], 1, 1, 1, 1);
		Sprite.get("sSlickFoot").render(0, Sprite.SOUTH, x + xxx - 16 + xx[3] + Calc.rangedRandom(.125), y + yyy + 32 + Calc.rangedRandom(.125) + yy[3], -1, 1, angle + aa[3], 1, 1, 1, 1);
		
		//mandible
		Sprite.get("sSlickMandible").render(0, Sprite.SOUTH, x + xxx + xx[0] - 16 + Calc.rangedRandom(.125), y + yyy + yy[0] - 16 + Calc.rangedRandom(.125), -1, 1, angle + aa[0], 1, 1, 1, 1);
		Sprite.get("sSlickMandible").render(0, Sprite.SOUTH, x + xxx + xx[1] + 16 + Calc.rangedRandom(.125), y + yyy + yy[1] - 16 + Calc.rangedRandom(.125), 1, 1, angle + aa[1], 1, 1, 1, 1);
		
		xxx += Calc.rangedRandom(.125);
		//bod
		Sprite.get("sSlickBody").render(0, orientation, x + xxx + xx[2], y + yyy + yy[2], 1, 1, angle + aa[2], 1, 1, 1, 1);
		if (pupil){
			//initial point -2, +3.5
			Sprite.get("sSlickPupil").render(0, orientation, x + xxx + xx[2] - 2 + Calc.dirX(eyeDist, eyeDir), y + yyy + yy[2] + 3.5 + Calc.dirY(eyeDist, eyeDir), 1, 1, 0, 1, 1, 1, 1);
			Sprite.get("sSlickPupil").render(1, orientation, x + xxx + xx[2] - 2 + Calc.dirX(eyeDist, eyeDir), y + yyy + yy[2] + 3.5 + Calc.dirY(eyeDist, eyeDir), 1, 1, 0, health(), 1, 1, 1);
		}
		Sprite.get("sSlickEyelid").render(eyelidImage, orientation, x + xxx + xx[2], y + yyy + yy[2], 1, 1, angle + aa[2], 1, 1, 1, 1);
		Sprite.get("sSlickBody").render(1, orientation, x + xxx + xx[2], y + yyy + yy[2], 1, 1, angle + aa[2], 1, 1, 1, 1);
	}
	
	public int playerCollisionDamagesMe(){
		return 1;
	}
	
	public void gunDamage(){
		if (hurt <= 0 && phase == 1 && !smallMask)
			mask = getMask(false);
		if (hurt <= 0 && smallMask)
			eyeOpen();
	}
	
	public void bombDamage(){
		hurt(1.6);
	}
	
	public void kill(){
		if (!deadened){
			for (int i=0; i<9; i++)
				alarm[i] = -1;
			phase = 42069;
			
			deadened = true;
			
			Music.changeMusic("");
			
			vspeed = -1;
			
			animation = dead;
			imageSpeed = .3;
			imageSingle = 0;
			approach = 3;
			
			eyeOpen();
			
			Audio.get("sSlickOutro").setLooping(true);
			Sound.play("sSlickOutro");
			Audio.get("sSlickSqwak").setLooping(true);
			Sound.playPitched("sSlickSqwak", .05);
		}
	}
	
	public void hurt(double dmg){
		if (eyelidOpen && hurt <= 0 && smallMask && !deadened){
			Sound.playPitched("sSlickHurtBase", .05);
			Audio.get("sSlickHurt").setPitch(1.0 + invHealth());
			Sound.play("sSlickHurt");
			
			health -= dmg * damageMultiplier;
			hurt = 60;
			if (health <= 0){
				health = 0;
				kill();
			}
			else{
				eyeClose();
				
				approach = 2.3;
				if (!vuln){
					animation = stun;
					imageSpeed = .4;
				}
				else{
					animation = groundHit;
					imageSpeed = .8;
				}
				imageSingle = 0;
				cycle = true;
				reverseLoop = true;
				
				Fg.me.shakeTimer = 7;
				
				if (phase == 1){
					vspeed = -10;
				}
			}
			
			Global.squareParticle(x, y, 6, "4784FF", 3);
			Global.squareParticle(x, y + 4, 5, "4784FF", 4);
			Global.squareParticle(x, y + 8, 4, "4784FF", 5);
		}
	}

}
