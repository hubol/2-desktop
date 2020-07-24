package world.boss.papa;

import java.util.ArrayList;

import javax.media.opengl.GL2;

import audio.Audio;

import world.Fg;
import world.boss.BossAnnounce;
import world.control.Global;
import world.control.Music;
import world.control.Shake;
import world.control.Sound;
import world.player.Bullet;
import world.player.Player;
import graphics.Graphics;
import graphics.Shape;
import graphics.Sprite;
import main.Calc;
import main.Entity;
import main.Scene;

public class Papa extends Entity{
	public final double URCHINMULT = 1.06, ORBMULT = 1.3;
	
	public final double side = 48.0, apothem = 24.0 / Math.tan(22.5 * Calc.toRad), radius = 24.0 / Math.sin(22.5 * Calc.toRad), edge = 48.0 / Math.sqrt(2.0);
	
	private Shake s = new Shake();
	private Sprite damage = Sprite.get("sPapaDamage"), brek = Sprite.get("sPapaOuterDamage");
	
	public ArrayList<PapaSpike> spikes = new ArrayList<PapaSpike>();
	public double[] fx = new double[8];
	
	public double intensity = 1;
	
	public double urchinHealth = 100, atUrchin = 100, orbHealth = 100, atOrb = 100, showOrb = 100, whineAt = 97, launchAt = 85;
	public boolean diag = false;
	
	public boolean ORBFORM = false;
	public double faceScale = 1;
	
	private int spikeAt = 0;
	
	private double toX, toY, approach = 20;
	
	private boolean opening = true, doneOpen = false;
	private double toGo = 420;
	
	private int stunTimer = -1;
	
	private boolean furrow = false, angerAdvance = false;
	private double anger = 0, furrowImage = 0, fuck = 0;
	
	private double followDir = 90;
	private boolean bounceCapable = false;
	
	private int orbHurt = -1;
	
	private PapaMusic music;
	private boolean FUCKYOU = false;
	
	private ArrayList<Break> breaks = new ArrayList<Break>();
	
	private boolean fuckWithMusic = false;
	public double squeeze = 1, showSqueeze = 1;
	private int squeezes = 0;
	
	private double endToX, endToY;

	public Papa(double x, double y) {
		super(x + 420, y);
		toX = x + 420;
		toY = y;
		
		for (int i=0; i<8; i++)
			fx[i] = i * 45.0;
		
		setDepth(-14);
		alarmInitialize(20);
		
		Audio.get("sPapaPutter").setGain(1.0);
		Audio.fade("sPapaPutter", 0, .005);
		Audio.get("sPapaPutter").setLooping(true);
		Sound.play("sPapaPutter");
		
		alarm[0] = 60;
		alarm[1] = 100;
		alarm[2] = 15;
		alarm[3] = 10;
		alarm[4] = 80;
		alarm[7] = 40;
		
		setCollisionGroup(Global.DEACTIVATEME);
	}
	
	public void reset(){
		FUCKYOU = false;
		
		Sound.playPitched("sPapaReform", .01);
		
		music.main();
		ORBFORM = false;
		whineAt = 97;
		launchAt = 85;
		urchinHealth = 100;
		showOrb = 0;
		toX = x;
		toY = y;
		followDir = 90;
		
		for (int i=0; i<8; i++)
			stream(i * 45, "FF1864");
		
		spikeAt = 0;
		alarm[0] = 10;
		alarm[4] = 10;
		Fg.me.shakeTimer = 10;
	}
	
	public void addBreak(Break b){
		if(breaks.size() > 300)
			breaks.remove(0);
		breaks.add(b);
	}
	
	public void becomeOrb(){
		furrow = false;
		ORBFORM = true;
		showOrb = orbHealth;
	}
	
	public void stun(int timer){
		if (stunTimer <= 0)
			stunTimer = timer;
	}
	
	public void stream(double dir){
		stream(dir, "FF1864");
	}
	
	public void stream(double dir, String color){
		stream(dir, radius, color);
	}
	
	public void stream(double dir, double dist, String color){
		for (int i=0; i<8; i++){
			final double d = Calc.random(360), l = Calc.random(5);
			new Streamer(this, Calc.pointDistance(0, 0, Calc.dirX(dist, dir) + Calc.dirX(l, d), Calc.dirY(dist, dir) + Calc.dirY(l, d)), Calc.pointDirection(0, 0, Calc.dirX(dist, dir) + Calc.dirX(l, d), Calc.dirY(dist, dir) + Calc.dirY(l, d)), angle, x + Calc.dirX(dist, dir) + Calc.dirX(l, d), y + Calc.dirY(dist, dir) + Calc.dirY(l, d), dir + Calc.rangedRandom(10), color, 4 + (int)Calc.random(10));
		}
	}
	
	public void fancy(double dir){
		for (int i=0; i<5; i++){
			final double d = Calc.random(360), l = Calc.random(9);
			new OrbShard(x + Calc.dirX(radius, dir) + Calc.dirX(l, d), y + Calc.dirY(radius, dir) + Calc.dirY(l, d));
		}
		stream(dir);
		brk(dir);
	}
	
	public void brk(double dir){
		addBreak(new Break(dir - normalize(angle)));
	}
	
	public void orbHurt(double dmg, double dir){
		if (orbHurt <= 0){
			orbHealth -= dmg * ORBMULT;
			orbHurt = 15;
			
			fancy(dir);
			
			Sound.playPitched("sPapaOrbBreak");
			Sound.playPitched("sPapaOrbHurt");
		}
	}
	
	public void bombDetect(double x, double y){
		if (!fuckWithMusic){
			if (!ORBFORM){
				for (int i=0; i<spikes.size(); i++){
					PapaSpike p = spikes.get(i);
					if (Calc.pointDistance(x, y, p.x + p.b.x, p.y + p.b.y) < 100)
						p.bigBreak();
				}
			}
			if (Calc.pointDistance(x, y, this.x, this.y) < radius + 70){
				if (!ORBFORM)
					stun(50);
				final double dir = Calc.pointDirection(x, y, this.x, this.y);
				final double spd = 30;
				hspeed = Calc.dirX(spd, dir);
				vspeed = Calc.dirY(spd, dir);
				if (ORBFORM){
					orbHealth -= 12 * ORBMULT;
					orbHurt = 15;
					
					fancy(dir);
					
					Sound.playPitched("sPapaOrbBreak");
					Sound.playPitched("sPapaOrbHurt");
				}
			}
		}
	}
	
	public void bulletHandle(){
		if (!ORBFORM){
			for (int i=0; i<spikes.size(); i++)
				spikes.get(i).bulletHandle();
		}
		
		ArrayList<Bullet> list = Scene.getEntityList(Bullet.class);
		for (int i=0; i<list.size(); i++){
			Bullet b = list.get(i);
			if (Calc.pointDistance(x, y, b.x, b.y) < radius + 4){
				b.destroy();
				if (!ORBFORM)
					stun(25);
				final double dir = Calc.pointDirection(b.x, b.y, x, y);
				final double spd = 8;
				hspeed = Calc.dirX(spd, dir);
				vspeed = Calc.dirY(spd, dir);
				if (ORBFORM)
					orbHurt(3, dir + 180);
			}
		}
	}
	
	public void alarmEvent(int i){
		if (i == 0)
			addSpike();
		else if (i == 1){
			Global.preventAudioCrash();
			Music.changeMusic("musBoss07Bg");
			music = new PapaMusic();
			new BossAnnounce("PAPA",Global.roomColor,"sBossTextPapa");
			Player.control = true;
			Player.me.cutMove = false;
		}
		else if (i == 2){
			if (furrowImage == 0)
				furrowImage = 1;
			else
				furrowImage = 0;
			alarm[2] = 15;
		}
		else if (i == 3){
			if (!angerAdvance){
				if (anger == 0)
					anger = 1;
				else
					anger = 1;
				alarm[3] = 10;
			}
		}
		else if (i == 4){
			angerAdvance = true;
			alarm[4] = 90 + (int)Calc.random(120);
			alarm[3] = -1;
			alarm[5] = 3;
		}
		else if (i == 5){
			if (!ORBFORM)
				Sound.play("sPapaBigSniffle");
			fuck = -.15;
			alarm[6] = 10; 
		}
		else if (i == 6)
			fuck = 0;
		else if (i == 8 && alarm[10] <= 0){
			reloadDiag(true);
		}
		else if (i == 9 && alarm[10] <= 0){
			reloadDiag(false);
		}
		else if (i == 10){
			Sound.playPitched("sPapaLaunchScreech", .01);
			music.expose();
			
			ORBFORM = true;
			if (x < 320)
				hspeed = 4;
			else
				hspeed = -4;
			
			vspeed = -12;
		}
		else if (i == 11){
			launchDiag(true, 3);
			launchDiag(false, 3);
			
			alarm[10] = alarm[8] / 2;
			Fg.me.shakeTimer = alarm[10];
			
			alarm[8] = -8;
			alarm[9] = -8;
		}
		else if (i == 12){
			reset(); //TODO
		}
		else if (i == 13){
			Sound.playPitched("sPapaRecharge", .01);
			new CircleStreamer(this, radius + 16, 0, "00C77D");
			new CircleStreamer(this, radius + 16, 90, "FF1864");
			new CircleStreamer(this, radius + 16, 180, "00C77D");
			new CircleStreamer(this, radius + 16, 270, "FF1864");
		}
		else if (i == 17){
			final int goTo = 15;
			Audio.get("sPapaJuice").setPitch((double)squeezes / (double)goTo);
			Sound.play("sPapaJuice");
			Sound.playPitched("sPapaOrbHurt");
			Fg.me.shakeTimer = 5;
			//setDirSpeed(Calc.pointDirection(x, y, 320, 240), 6);
			endToX = Calc.approach(endToX, 320, 4);
			endToY = Calc.approach(endToY, 176, 4);
			
			stream(Calc.random(360), (40 + Calc.random(radius - 40)) * showSqueeze, "FF1864");
			stream(Calc.random(360), (40 + Calc.random(radius - 40)) * showSqueeze, "FF1864");
			stream(Calc.random(360), (40 + Calc.random(radius - 40)) * showSqueeze, "00C77D");
			
			squeeze = .7;
			squeezes++;
			if (squeezes < goTo)
				alarm[17] = 12 - (int)(squeezes * .3);
			else{
				alarm[18] = 15;
			}
		}
		else if (i == 18){ //TODO BREAK
			Audio.get("sPapaOrbBreak").setPitch(1);
			Audio.get("sPapaSpikeBreak").setPitch(1);
			Sound.play("sPapaDeath");
			Sound.play("sPapaOrbBreak");
			Sound.play("sPapaSpikeBreak");
			
			PapaGhost p = new PapaGhost(x, y, showSqueeze, angle);
			for (int l=0; l<8; l++)
				p.stream(l * 45, "FF1864");
			
			final double dist = radius * showSqueeze;
			final double rot = 20, rpt = 11;
			
			OrbShard o = new OrbShard(x, y, true);
			o.setDepth(Integer.MIN_VALUE+17);
			for (double j=0; j<rot; j++){
				for (double k=1; k<=rpt; k++){
					o = new OrbShard(x + Calc.dirX(dist * (k / rpt), j * (360.0 / rot)), y + Calc.dirY(dist * (k / rpt), j * (360.0 / rot)), true);
					o.setDepth(Integer.MIN_VALUE+17);
				}
			}
			
			Global.addTweet("I HAVE DEFEATED #PAPA!!!!!!");
			
			Player.control = true;
			Player.me.cutMove = false;
			
			Scene.destroy(Streamer.class);
			
			destroy();
		}
	}
	
	public double normalize(double angle){
		while (angle < 0)
			angle += 360;
		return angle % 360.0;
	}
	
	public void addSpike(){
		if (spikeAt < 8){
			spikes.add(new PapaSpike(false, spikeAt, this));
			spikeAt ++;
			alarm[0] = 10;
		}
	}
	
	public void step(){
		if (!ORBFORM){
			if (alarm[11] > 0){
				alarm[8] = -8;
				alarm[9] = -8;
			}
			
			for (int i=0; i<8; i++){
				fx[i] += intensity;
				while ((fx[i] / 15.0) * Calc.toRad >= Math.PI * 2)
					fx[i] = 0;
			}
			
			if (opening){
				if (toGo > 0){
					toX -= 4;
					toGo -= 4;
					toY = ystart + (70 * Math.sin(toGo / 64.0));
					Audio.get("sPapaPutter").setPitch(1 + (.3 * Math.sin(toGo / 64.0)));
				}
				else{
					toY = ystart;
					opening = false;
					doneOpen = true;
					Audio.get("sPapaPutter").stop();
				}
			}
			
			if (angerAdvance && anger < 9){
				anger += .33 + fuck;
				if (anger >= 9){
					anger = 0;
					angerAdvance = false;
				}
			}
			
			x = Calc.approach(x, toX, approach) + hspeed;
			y = Calc.approach(y, toY, approach) + vspeed;
			
			hspeed *= .5;
			vspeed *= .5;
			
			atUrchin = Calc.approach(atUrchin, urchinHealth, 20);
			alarmStep();
			
			if (alarm[7] > 0)
				atUrchin = 0;
			
			if (stunTimer > 0)
				stunTimer -= 1;
			else if (doneOpen && alarm[10] <= 0){
				final double goaldir = Calc.pointDirection(x,y,Player.me.x,Player.me.y);
				if (Calc.dirY(1, followDir-goaldir)>0)
					followDir+=5;
				if (Calc.dirY(1, followDir-goaldir)<0)
					followDir-=5;
				
				final double spd = 1.65  - (.35 * (orbHealth / 100.0)) - (.1 * (urchinHealth / 100.0));
				toX += Calc.dirX(spd, followDir);
				toY += Calc.dirY(spd, followDir);
			}
			
			bulletHandle();
			
			if (Calc.pointDistance(x, y, Player.me.x, Player.me.y) < 71){
				Player.hurtPlayer(110);
			}
			else if (Calc.pointDistance(x, y, Player.me.x, Player.me.y) < 150){
				furrow = true;
				urchinHealth -= .1025 * URCHINMULT;
				if (Calc.pointDistance(x, y, Player.me.x, Player.me.y) < 130)
					urchinHealth -= .07025 * URCHINMULT;
				if (Calc.pointDistance(x, y, Player.me.x, Player.me.y) < 110)
					urchinHealth -= .06025 * URCHINMULT;
				
				if (urchinHealth < whineAt){
					Sound.playPitched("sSuicideNear");
					whineAt -= 1.5;
				}
				
				if (alarm[10] <= 0 && !FUCKYOU){
					if (urchinHealth > 0){
						if (urchinHealth < launchAt){
							diag = !diag;
							launchDiag(diag);
							launchAt -= 15;
							if (launchAt <= 0)
								launchAt = -9000;
						}
					}
					else{ //TODO orb transformation
						/*for (int i=0; i<spikes.size(); i++){
							PapaSpike s = spikes.get(i);
							if (s.attached){
								s.bigBreak();
								i--;
							}
						}*/
						FUCKYOU = true;
						
						Sound.playPitched("sPapaFinalLaunchPrepare", .04);
						reloadDiag(true);
						reloadDiag(false);
						
						alarm[10] = 9000;
						alarm[11] = 10;
						alarm[12] = 390;
						alarm[13] = 330;
					}
				}
			}
			else
				furrow = false;
			
			faceScale = Calc.approach(faceScale, 1, 2);
			showOrb = 100;
		}
		else{
			alarmStep();
			
			faceScale = Calc.approach(faceScale, 0, 13);
			showOrb = orbHealth;
			
			angle += hspeed * .25;
			
			if (!fuckWithMusic)
				vspeed += 1;
			else{
				x = Calc.approach(x, endToX, 12);
				y = Calc.approach(y, endToY, 12);
			}
			
			if (bounceCapable && !fuckWithMusic){
				if (y + radius + vspeed >= 352){
					y = 352 - radius;
					hspeed *= .9; 
					if (vspeed < 4)
						vspeed = 0;
					else{
						vspeed *= -.6;
						boink();
						fancy(270);
					}
				}
				if (x + radius > 608 || x - radius < 32){
					if (x + radius > 608){
						x = 607.9 - radius;
						fancy(0);
					}
					else{
						x = 32.1 + radius;
						fancy(180);
					}
					hspeed *= -.8;
					boink();
				}
				if (y - radius < 32){
					y = 32 + radius;
					vspeed = Math.abs(vspeed);
					hspeed *= .9; 
					boink();
					fancy(90);
				}
			}
			else if (allInside() && !fuckWithMusic){
				bounceCapable = true;
			}
			
			x += hspeed;
			y += vspeed;
			
			if (fuckWithMusic){
				squeeze = Calc.approach(squeeze, 1, 2);
				showSqueeze = Calc.approach(showSqueeze, squeeze, 2);
				hspeed *= .95;
				vspeed *= .95;
			}
			
			if (!fuckWithMusic)
				bulletHandle();
			
			if (orbHurt <= 0 && Calc.pointDistance(Player.me.x, Player.me.y, x, y) < radius + 19 && orbHurt <= 0/* && Player.me.getSpeed() > 0*/ && !fuckWithMusic){
				final double dir = Calc.pointDirection(Player.me.x, Player.me.y, x, y);
				final double spd = Math.max(10, Math.min(25, Player.me.getSpeed() * 4));
				
				while(Calc.pointDistance(Player.me.x, Player.me.y, x, y) < radius + 19){
					x += Calc.dirX(1, dir);
					y += Calc.dirY(1, dir);
				}
				
				hspeed = Calc.dirX(spd, dir);
				vspeed = Calc.dirY(spd, dir);
				if (ORBFORM)
					orbHurt(2.5, dir + 180);
			}
			
			if (orbHealth <= 0 && !fuckWithMusic){
				for (int i=0; i<15; i++)
					alarm[i] = 90000; //TODO death sequence
				
				Sound.play("sPapaDeathBegin");
				
				endToX = x;
				endToY = y;
				
				alarmEvent(17);
				
				Audio.fade("musBoss07Layer", 0, .0006);
				fuckWithMusic = true;
				
				orbHealth = 0;
				hspeed = 0;
				vspeed = 0;
				
				Player.control = false;
				Player.me.cutMove = true;
				Player.me.cutRight = false;
				Player.me.cutToX = 96;
			}
		}
		
		atOrb = Calc.approach(atOrb, showOrb, 5);
		if (orbHurt > 0){
			//visible = !visible;
			orbHurt -= 1;
		}
		else
			visible = true;
		
		if (fuckWithMusic)
			Audio.get("musBoss07Layer").setPitch(Math.max(.1, Audio.get("musBoss07Layer").getPitch() - .0005));
		
		//System.out.println(Scene.getEntityList(PapaSpike.class).size() + " papaspikes, "+ Scene.getEntityList(Streamer.class).size() + " streamers, "+ Scene.getEntityList(OrbShard.class).size() + " orbshards, "+ Scene.getEntityList(SpikeShard.class).size() + " spikeshards");
	}
	
	public void boink(){
		Sound.playPitched("sPapaBoink");
		Fg.me.shakeTimer = (int)getSpeed() + 2;
		
		Sound.playPitched("sPapaOrbBreak");
		Sound.playPitched("sPapaOrbHurt");
		orbHealth -= .25 * ORBMULT;
	}
	
	public boolean anyOutside(){
		return (!pointInside(x, y - radius) || !pointInside(x, y + radius) || !pointInside(x - radius, y) || !pointInside(x + radius, y));
	}
	
	public boolean allInside(){
		return (pointInside(x, y - radius) && pointInside(x, y + radius) && pointInside(x - radius, y) && pointInside(x + radius, y));
	}
	
	public boolean pointInside(double x, double y){
		return (x >= 32 && y >= 32 && x <= 608 && y <= 352);
	}
	
	public void launchDiag(boolean diag){
		launchDiag(diag, 1);
	}
	
	public void launchDiag(boolean diag, int timerMultiplier){
		final int timer = (int)(20 + (5 * (orbHealth / 100.0)) + (5 * (urchinHealth / 100.0))) * timerMultiplier;
		final double spd = (11 - (2.5 * (orbHealth / 100.0)) - (1.5 * (urchinHealth / 100.0)));
		for (int i=0; i<spikes.size(); i++){
			PapaSpike s = spikes.get(i);
			if (s.attached && s.alarm[1] <= 0 && ((diag && (s.angle == 45 || s.angle == 135 || s.angle == 225 || s.angle == 315)) || (!diag && (s.angle == 0 || s.angle == 90 || s.angle == 180 || s.angle == 270))))
				s.launch(timer, spd);
		}
		
		if (diag)
			alarm[8] = timer * 2;
		else
			alarm[9] = timer * 2;
	}
	
	public void reloadDiag(boolean diag){
		boolean[] needed = new boolean[]{false, true, false, true, false, true, false, true};
		if (!diag)
			needed = new boolean[]{true, false, true, false, true, false, true, false};
		for (int i=0; i<spikes.size(); i++){
			PapaSpike s = spikes.get(i);
			if (s.attached)
				needed[s.id] = false;
		}
		
		for (int i=0; i<8; i++){
			if (needed[i])
				spikes.add(new PapaSpike(false, i, this));
		}
	}
	
	public void damage(double scale, int img){
		final double invert = 1 - (atOrb / 100.0);
		final double multiplier = Math.min(1, Math.max(0, (invert - ((double)img / 3.0)) * 3.0));
		
		Scene.gl.glEnable(GL2.GL_ALPHA_TEST);
		
		Scene.gl.glClearStencil(0);
		Scene.gl.glClear( GL2.GL_STENCIL_BUFFER_BIT );
		Scene.gl.glEnable( GL2.GL_STENCIL_TEST );
		
		Scene.gl.glStencilFunc( GL2.GL_ALWAYS, 1, 0xFF );
		Scene.gl.glStencilOp( GL2.GL_KEEP, GL2.GL_KEEP, GL2.GL_REPLACE );
		
		//cut holes in a metaphorical board
		Graphics.setAlpha(1.0 / 256.0);
		Graphics.setColor("FFFFFF");
		Shape.drawCircle(x + s.x, y + s.y, multiplier * radius, 40);
		Graphics.setAlpha(1.0);
		
		Scene.gl.glStencilFunc( GL2.GL_EQUAL, 1, 0xFF );
		Scene.gl.glStencilOp( GL2.GL_KEEP, GL2.GL_KEEP, GL2.GL_KEEP );
		 
		Scene.gl.glDisable(GL2.GL_ALPHA_TEST);
		
		//in essence, spray paint the metaphorical board with the holes
		damage.render(img, Sprite.CENTERED, x + s.x, y + s.y, scale, scale, angle, 1, 1, 1, 1);
			
		Scene.gl.glDisable( GL2.GL_STENCIL_TEST );
	}
	
	public void render(){
		Graphics.setAlpha(1);
		Graphics.setColor("0078F7");
		Shape.drawCircle(s.x + x, s.y + y, (radius * Math.pow(1 - faceScale, 2) * showSqueeze) + 3.5, 40);
		ArrayList<PapaEntity> list = Scene.getEntityList(PapaEntity.class);
		for (int i=0; i<360; i+=15){
			shape(Calc.dirX(3.5, i), Calc.dirY(3.5, i)); 
			for (int j=0; j<list.size(); j++)
				list.get(j).back(Calc.dirX(3.5, i) + s.x, Calc.dirY(3.5, i) + s.y);
		}
		Graphics.setColor("00C77D");
		for (int j=0; j<list.size(); j++)
			list.get(j).front(s.x, s.y);
		Graphics.setColor("00C77D");
		Shape.drawCircle(s.x + x, s.y + y, radius * Math.pow(1 - faceScale, 2) * showSqueeze, 40);
		
		shape(0, 0);
		
		final double scl = (radius / 128.0) * Math.pow(1 - faceScale, 2) * showSqueeze;
		for(int i=0; i<breaks.size(); i++){
			Break b = breaks.get(i);
			double d = angle + b.addDirection, dis = radius * Math.pow(1 - faceScale, 2) * showSqueeze;
			brek.render(b.img, Sprite.EAST, x + s.x + Calc.dirX(dis, d), y + s.y + Calc.dirY(dis, d), scl * b.xsc, scl * b.ysc, d, 1, 1, 1, 1);
		}
		
		
		//time for orb damage
		final double scal = (((radius * 2) + 7) / 256.0) * Math.pow(1 - faceScale, 2) * showSqueeze;
		for (int i=0; i<3; i++)
			damage(scal, i);
		
		final double vShift = 4.5;
		
		Sprite.get("sPapaAngerBack").render(anger, Sprite.CENTERED, x + s.x, y + s.y + vShift, faceScale, faceScale, 0, 1, 1, 1, 1);
		
		Scene.gl.glEnable(GL2.GL_ALPHA_TEST);
		
		Scene.gl.glClearStencil(0);
		Scene.gl.glClear( GL2.GL_STENCIL_BUFFER_BIT );
		Scene.gl.glEnable( GL2.GL_STENCIL_TEST );
		
		Scene.gl.glStencilFunc( GL2.GL_ALWAYS, 1, 0xFF );
		Scene.gl.glStencilOp( GL2.GL_KEEP, GL2.GL_KEEP, GL2.GL_REPLACE );
		
		//cut holes in a metaphorical board
		Graphics.setAlpha(1.0 / 256.0);
		Graphics.setColor("FFFFFF");
		drawRectangle(x + s.x - 26, y + s.y + 34.0 - (68.0 * (atUrchin / 100.0)) + vShift, x + s.x + 26.0, y + s.y + 34.0 + vShift);
		Graphics.setAlpha(1.0);
		
		Scene.gl.glStencilFunc( GL2.GL_EQUAL, 1, 0xFF );
		Scene.gl.glStencilOp( GL2.GL_KEEP, GL2.GL_KEEP, GL2.GL_KEEP );
		 
		Scene.gl.glDisable(GL2.GL_ALPHA_TEST);
		
		//in essence, spray paint the metaphorical board with the holes
		Sprite.get("sPapaAnger").render(anger, Sprite.CENTERED, x + s.x, y + s.y + vShift, faceScale, faceScale, 0, 1, 1, 1, 1);
			
		Scene.gl.glDisable( GL2.GL_STENCIL_TEST );
		
		if (furrow)
			Sprite.get("sPapaFurrow").render(furrowImage, Sprite.CENTERED, x + s.x, y + s.y + vShift, faceScale, faceScale, 0, 1, 1, 1, 1);
	}
	
	public void shape(double x, double y){
		if (!fuckWithMusic){
			Scene.gl.glColor4d(Graphics.color[0], Graphics.color[1], Graphics.color[2], Graphics.alpha);
			Scene.gl.glBegin(GL2.GL_TRIANGLE_FAN);
			Scene.gl.glVertex2d(this.x + s.x + x, this.y + s.y + y);
			for (int i=0; i<9; i++){
				Scene.gl.glVertex2d(this.x + s.x + x + Calc.dirX(radius, (i * 45.0) - 22.5),this.y + s.y + y + Calc.dirY(radius, (i * 45.0) - 22.5));
				Scene.trianglesDrawn += 1;
			}
			Scene.gl.glEnd();
		}
	}
	
	public static void drawRectangle(double x1, double y1, double x2, double y2){
		Sprite.get("sRoundRect").render(0, Sprite.NORTHWEST, x1, y1, Math.abs(x2-x1)/64.0, Math.abs(y2-y1)/64.0, 0, Graphics.alpha, Graphics.color[0], Graphics.color[1], Graphics.color[2]);
	}

}
