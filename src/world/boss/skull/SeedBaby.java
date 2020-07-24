package world.boss.skull;

import world.Fg;
import world.control.Global;
import world.control.Sound;
import world.player.Player;
import audio.Audio;
import main.Calc;
import main.Scene;
import graphics.Sprite;

public class SeedBaby extends Seed{
	public boolean animate;
	public int count, phase;
	public double timer;
	public double mult;
	
	public boolean dead;

	public SeedBaby(Skull mom, double time) {
		super(mom, time, false);
		sprite = Sprite.get("sSkullSeedBaby");
		mask = sprite.mask;
		imageSingle = 0;
		imageSpeed = 0;
		
		setDepth(-9);
		
		mult = 0;
		
		angle = 0;
		animate = false;
		count = 0;
		
		phase = 0;
		
		timer = 0;
		
		calcXscale();
		yscale = 1;
		
		dead = false;
	}
	
	public void landDamage(){
		if (!dead){
			dead = true;
			Player.invincible = 1;
			Player.me.jump();
			Player.me.vspeed -= .7;
			Sound.play("sLandOnEnemy");
			Sound.playPitched("sSkullBabyKill", .1);
			sprite = Sprite.get("sSkullSeedBabyKill");
			imageSingle = Calc.random(7);
		}
	}
	
	public void calcXscale(){
		xscale = 1.0;
		if (x < Player.me.x)
			xscale = -1.0;
	}
	
	public void step(){
		if (!dead){
			mult = Calc.approach(mult, 1, 5);
			
			if (claimedSound()){
				Audio.fade("sSkullDropWhistle", 1.0, .18);
				Audio.get("sSkullDropWhistle").setPitch(pitch);
				pitch = Calc.approach(pitch, 1, 20);
			}
			
			count += 1;
			if (animate){
				if (count % 4 == 0)
					angle += 90;
			}
			
			if (phase == 0){
				if (!held){
					imageSingle = 1;
					if (vspeed == 0)
						claimSound();
					vspeed += .9;
					animate = true;
				}
				else{
					adjustPosition();
					addY += increment;
				}
			}
			
			if (phase == 1 || phase == 2){
				if (playerCollisionDamagesMe()==0)
					Player.hurtPlayer(30);
			}
			else if (Scene.collision(this, x, y, Global.PLAYER))
				Player.hurtPlayer(30);
			
			if (phase == 1){
				timer -= 1;
				if (timer <= 0){
					Sound.playPitched("sSkullBabyJump", .1);
					for (int i=0; i<10; i++)
						new GroundParticle(x + Calc.rangedRandom(9), 416);
					vspeed = -8;
					phase = 2;
				}
			}
			else if (phase == 2){
				vspeed += 1;
				if (vspeed == 0){
					Sound.playPitched("sSkullBabyDash", .1);
					imageSingle = 1;
					animate = true;
					hspeed = 12 * -xscale;
					phase = 3;
				}
			}
			else if (phase == 3){
				vspeed += .5;
			}
			
			x += hspeed;
			y += vspeed;
			
			if (phase == 3){
				SeedParticle j = new SeedParticle(x, y - 12);
				j.xscale *= .6;
				j.yscale = j.xscale;
				
				if (x < 76 && hspeed < 0){
					x = 76;
					hspeed *= -1;
				}
				else if (x > 564 && hspeed > 0){
					x = 564;
					hspeed *= -1;
					Sound.playPitched("sSkullBabyLand", .1);
				}
				if (y >= 416){
					y = 416;
					vspeed = 0;
					calcXscale();
					timer = 30 + Calc.random(30);
					Sound.playPitched("sSkullBabyLand", .1);
					for (int i=0; i<10; i++)
						new GroundParticle(x + Calc.rangedRandom(9), 416);
					imageSingle = 0;
					animate = false;
					hspeed = 0;
					phase = 1;
					angle = 0;
				}
			}
			
			if (y >= 416 && phase == 0){
				imageSingle = 0;
				
				calcXscale();
				
				animate = false;
				angle = 0;
				Sound.playPitched("sSkullSeedLand", .07);

				Fg.me.shakeTimer = 8;
				phase = 1;
				vspeed = 0;
				
				y = 416;
				
				/*for (int i=0; i<16; i++)
					new SeedParticle(x + Calc.rangedRandom(5), 416 - Calc.random(8));*/
				
				for (int i=0; i<22; i++)
					new GroundParticle(x + Calc.rangedRandom(9), 416);
				
				returnSound(); //this is not actually necessary but w/e
				
				timer = 30 + Calc.random(30);
			}
		}
		else{
			count += 1;
			if (count % 2 == 0){
				imageSingle = Calc.random(7);
				angle += 90;
			}
			
			x+=Calc.rangedRandom(4);
			y+=Calc.rangedRandom(4);
			y *= .9925;
			imageSingle = 0;
			mult = Calc.approach(mult, 2, 30);
			mult *= 1.04;
			if (mult > 1.53){
				Sound.explodePlay();
				for (int i=0; i<16; i++)
					new GroundParticle(x + Calc.rangedRandom(9 * mult), y - (3 * mult) - Calc.random(18 * mult));
				destroy();
			}
		}
	}
	
	public void render(){
		if (!stepActive && claimedSound())
			Audio.get("sSkullDropWhistle").setGain(0);
		sprite.render(imageSingle, Sprite.CENTERED, x + Calc.rangedRandom(.5), y - 12 + Calc.rangedRandom(.5), xscale * mult, yscale * mult, angle, 1, 1, 1, 1);
	}

}
