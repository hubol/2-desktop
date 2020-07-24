package world.boss.skull;

import java.util.ArrayList;

import audio.Audio;
import world.Fg;
import world.control.Global;
import world.control.Sound;
import world.event.SmallMessage;
import world.player.Player;
import graphics.Sprite;
import main.Calc;
import main.Entity;
import main.Scene;

public class Jetpack extends Entity{
	public int phase, phaseTimer;
	public double targetX;
	
	public boolean smoke;
	
	public Skull mother;
	
	public double exhaustPitch, whistlePitch;
	
	public int count;
	public boolean arf;
	public boolean blewup = false;

	public Jetpack(double x, double y, Skull mom) {
		super(x, y);
		
		arf = true;
		
		mother = mom;
		
		count = 0;
		
		exhaustPitch = 1.0;
		whistlePitch = 3.0;
		
		this.y = 512;
		this.x = mother.pissRandom(96, 544);
		while (Math.abs(this.x - Player.me.x) < 64)
			this.x = Calc.random(96, 544);
		
		xstart = this.x;
		
		targetX = mother.pissRandom(96, 544);
		while (Math.abs(targetX - Player.me.x) < 48 && Math.abs(targetX - this.x) < 48)
			targetX = Calc.random(96, 544);
		
		sprite = Sprite.get("sJetpack");
		mask = sprite.mask;
		orientation = Sprite.CENTERED;
		setDepth(Integer.MIN_VALUE + 16);
		setCollisionGroup(Global.DEACTIVATEME);
		
		phase = 0;
		phaseTimer = -1;
		
		smoke = false;
		
		vspeed = -2;
		
		hspeed = 5;
		if (this.x > targetX)
			hspeed = -5;
		
		new SmallMessage(this, 0, -16, (String)Calc.choose("I'M HERE TO HELP YOU!","I'M HERE TO SAVE YOU!","HERE I COME TO SAVE THE DAY!","NEED SOME ASSISTANCE?"), 60);
	}
	
	public void step(){
		if (phase == 0){
			if (vspeed > -8)
				vspeed -= .12;
			
			if (y < 180)
				phase = 1;
		}
		else if (phase == 1){
			vspeed += .8;
			if (vspeed >= 0 && phaseTimer <= 0){
				vspeed = 0;
				new SmallMessage(this, 0, -16, "CATCH ME!", 40);
				Global.explosionEffect(x,  y, 1);
				Sound.play("sBombBlast");
				phaseTimer = 7;
				smoke = true;
			}
			
			phaseTimer -= 1;
			if (phaseTimer == 0){
				phase = 2;
				Audio.get("sJetpackFallWhistle").setGain(.7);
				Audio.get("sJetpackFallWhistle").setPitch(whistlePitch);
				Audio.get("sJetpackFallWhistle").setLooping(true);
				Audio.get("sJetpackFallWhistle").play();
				Audio.fade("sJetpackFallWhistle", 1.0, .2);
			}
		}
		else if (phase == 2){
			vspeed += .37;
			
			whistlePitch = Math.max(1.0, Calc.approach(whistlePitch - .05, 1, 20));
			Audio.get("sJetpackFallWhistle").setPitch(whistlePitch);
			
			//fuck you
			if (xstart < targetX){
				angle -= 3.2;
				angle *= 1.125;
				if (angle < -180)
					angle = -180;
			}
			else if (xstart > targetX){
				angle += 3.2;
				angle *= 1.125;
				if (angle > 180)
					angle = 180;
			}
		}
		
		if (phase <= 1){
			angle = Calc.approach(angle, -hspeed * 1.5, 10);
			
			hspeed *= .988;
			if (((xstart < targetX && x < targetX) || (xstart > targetX && x > targetX)) && vspeed < 0)
				x = Calc.approach(x, targetX, 30);
			else
				hspeed *= .6;
		}
		
		if (phase == 0 || phase == 1){
			if (count % 4 == 0){
				exhaustPitch += .025;
				Audio.get("sJetpackExhaust").setPitch(exhaustPitch + Calc.rangedRandom(.125));
				Sound.play("sJetpackExhaust");
			}
			count += 1;
		}
		else if (phase == 3 || phase == 4){
			exhaustPitch += .01245125;
			Audio.get("sJetpackUpExhaust").setPitch(exhaustPitch);
		}
		
		Audio.get("sJetpackUpExhaust").setGain(1);
		
		super.step();
		
		if (phase == 3){
			vspeed -= .06;
			
			Player.me.vspeed = 0;
			Player.me.x = x;
			Player.me.y = y;
			
			if (Player.me.y <= 80){
				Player.me.y = 80;
				phase = 4;
				Player.me.setDepth(0);
				Player.control = true;
				Player.me.jetpack = false;
			}
		}
		
		if (phase == 4){
			vspeed -= .2;
			exhaustPitch += .025;
			if (y <= -64)
				destroy();
		}
		
		if (phase == 3 || phase == 4){
			angle = Calc.approach(angle, 0, 5);
			if (angle != 0){
				if (angle > 0){
					angle -= 4;
					if (angle < 0)
						angle = 0;
				}
				else{
					angle += 4;
					if (angle > 0)
						angle = 0;
				}
			}
			
			x = Calc.approach(x, targetX, 16);
			if (x != targetX){
				if (x > targetX){
					x -= 3;
					if (x < targetX)
						x = targetX;
				}
				else{
					x += 3;
					if (x > targetX)
						x = targetX;
				}
			}
			
		}
		
		if (arf){
			double[] point = Calc.transformPointByAngle(24, 14, angle);
			if (!smoke){
				for (int j=0; j<2; j++){
					FlowerParticle i = new FlowerParticle(x + point[0], y + point[1], angle - 90 + Calc.rangedRandom(5));
					i.xscale *= .5;
					i.yscale = i.xscale;
				}
			}
			else{
				for (int i=0; i <7; i++)
					new JetpackSmoke(x + point[0], y + point[1]);
			}
		}
		else{
			double[] point = Calc.transformPointByAngle(-26, 14, angle);
			if (!smoke){
				for (int j=0; j<2; j++){
					FlowerParticle i = new FlowerParticle(x + point[0], y + point[1], angle - 90 + Calc.rangedRandom(5));
					i.xscale *= .5;
					i.yscale = i.xscale;
				}
			}
			else{
				for (int i=0; i <7; i++)
					new JetpackSmoke(x + point[0], y + point[1]);
			}
		}
		
		arf = !arf;
		
		if (phase < 3 && !blewup && y < 416){
			if (Calc.pointDistance(x, y, Player.me.x, Player.me.y) <= 34){
				ArrayList<SproutFlower> list = Scene.getEntityList(SproutFlower.class);
				for (int i=0; i<list.size(); i++)
					list.get(i).spawned = true;
				
				Audio.get("sJetpackFallWhistle").stop();
				
				mother.phase = 4;
				mother.buildShadows();
				Player.invincible = 30;
				
				x = (x + Player.me.x) / 2.0;
				y = (y + Player.me.y) / 2.0;
				Player.me.x = x;
				Player.me.y = y;
				
				Player.me.vspeed = 0;
				
				phase = 3;
				
				targetX = mother.targetX;
				
				Audio.get("sJetpackUpExhaust").setPitch(1.0);
				Audio.get("sJetpackUpExhaust").setLooping(true);
				Sound.play("sJetpackUpExhaust");
				
				Player.control = false;
				Player.me.jetpack = true;
				Player.me.cutMove = false;
				
				vspeed = -2;
				
				smoke = false;
				
				Player.me.setDepth(Integer.MIN_VALUE + 15);
				
				
				exhaustPitch = 1.0;
			}
		}
		
		if (phase == 2){
			if (y >= 480){
				Fg.me.shakeTimer = 20;
				Audio.get("sJetpackFallWhistle").stop();
				y = 480;
				Global.explosionEffect(x,  y, 1);
				Sound.playPitched("sJetpackExplode", .05);
				blewup = true;
				destroy();
			}
		}
	}
	
	public void destroy(){
		Audio.get("sJetpackUpExhaust").stop();
		Audio.get("sJetpackUpExhaust").setPitch(1.0);
		super.destroy();
	}
	
	public void render(){
		if (!stepActive){
			Audio.get("sJetpackFallWhistle").setGain(0);
			Audio.get("sJetpackUpExhaust").setGain(0);
		}
		sprite.render(imageSingle, orientation, x + Calc.rangedRandom(.7), y + Calc.rangedRandom(.7), xscale, yscale, angle, 1, 1, 1, 1);
	}

}
