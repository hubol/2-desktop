package world.boss.skull;

import audio.Audio;
import world.Fg;
import world.control.Global;
import world.control.Sound;
import world.enemy.BasicEnemy;
import world.player.Player;
import graphics.Sprite;
import main.Calc;
import main.Scene;

public class Seed extends BasicEnemy{
	public Skull mother;
	public boolean held, sprout;
	public double addY, increment, timer, pitch;

	public Seed(Skull mom, double time, boolean sprout) {
		super(0, 0);
		
		this.sprout = sprout;
		
		setDepth(-4);
		mother = mom;
		
		adjustPosition();
		
		held = true;
		timer = time;
		
		addY = 0;
		
		setCollisionGroup(Global.DEACTIVATEME);
		
		increment = 16.0 / time;
		
		sprite = Sprite.get("sSkullSeed");
		orientation = Sprite.SOUTH;
		mask = sprite.mask;
		
		xscale = .8;
		yscale = .8;
	}
	
	public void adjustPosition(){
		x = mother.x + 2;
		y = mother.y + 16 + addY;
		
		if (timer < 10 && timer >= 0 && held){
			x += Calc.rangedRandom(3);
			y += Calc.rangedRandom(3);
		}
	}
	
	public void claimSound(){
		Audio.get("sSkullDropWhistle").setLooping(true);
		Audio.get("sSkullDropWhistle").setGain(0);
		Audio.get("sSkullDropWhistle").setPitch(3);
		Audio.get("sSkullDropWhistle").setGain(.18);
		Audio.fade("sSkullDropWhistle", 1.0, .18);
		Sound.play("sSkullDropWhistle");
		pitch = 3.0;
		mother.seedClaimedWhistle = this;
	}
	
	public boolean claimedSound(){
		return mother.seedClaimedWhistle == this;
	}
	
	public void returnSound(){
		if (claimedSound()){
			Audio.get("sSkullDropWhistle").stop();
			Audio.get("sSkullDropWhistle").setGain(0);
			Audio.get("sSkullDropWhistle").setPitch(3);
			Audio.get("sSkullDropWhistle").setGain(.18);
			mother.seedClaimedWhistle = null;
		}
	}
	
	public void step(){
		if (claimedSound()){
			Audio.fade("sSkullDropWhistle", 1.0, .18);
			Audio.get("sSkullDropWhistle").setPitch(pitch);
			pitch = Calc.approach(pitch, 1, 20);
		}
		
		timer -= 1;
		
		if (!held){
			if (vspeed == 0)
				claimSound();
			vspeed += .9;
		}
		else{
			adjustPosition();
			addY += increment;
		}
		
		if (Scene.collision(this, x, y, Global.PLAYER))
			Player.hurtPlayer(35);
		
		super.step();
		
		if (y >= 416){
			Sound.playPitched("sSkullSeedLand", .07);
			Fg.me.shakeTimer = 8;
			
			if (sprout)
				new Sprout(x, mother);
			
			for (int i=0; i<16; i++)
				new SeedParticle(x + Calc.rangedRandom(5), 416 - Calc.random(8));
			
			for (int i=0; i<22; i++)
				new GroundParticle(x + Calc.rangedRandom(9), 416);
			
			returnSound(); //this is not actually necessary but w/e
			
			destroy();
		}
	}
	
	public void render(){
		if (!stepActive && claimedSound())
			Audio.get("sSkullDropWhistle").setGain(0);
		super.render();
	}

}
