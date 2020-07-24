package world.boss.duke;

import main.Calc;
import main.Scene;
import graphics.Sprite;
import world.Fg;
import world.boss.BossAnnounce;
import world.control.Global;
import world.control.Music;
import world.control.Shake;
import world.control.Sound;
import world.enemy.BasicEnemy;
import world.particles.JumpDreamPart;
import world.player.Player;

public class DukeFirst extends BasicEnemy{
	public Shake s = new Shake(.7);
	public boolean ready = false;
	public int a = 0, invuln = 0;
	
	public double face = 1, health;
	public boolean begin = true;
	
	public Sprite puke = Sprite.get("sDuke"), hurt = Sprite.get("sDukeHurt");
	
	public DukeFirst(double x, double y) {
		super(x, y);
		
		health = 7;
		
		sprite = puke;
		mask = Sprite.get("sPukeMask").mask;
		
		vspeed = 9;
		
		setDepth(Integer.MIN_VALUE+19);
		alarmInitialize(3);
		
		img();
		peep();
		alarm[2] = 60;
	}
	
	public void destroy(){
		s.destroy();
		super.destroy();
	}
	
	public void kill(){
		Fg.me.shakeTimer = 6;
		
		Sound.deep();
		Sound.playPitched("sDukeScream");
		Sound.play("sLandOnEnemy");
		Sound.playPitched("sBossTextPuke", .3);
		
		if (health <= 0){
			DukeDeath i = new DukeDeath(x, y, true);
			i.alarm[0] = 20;
			destroy();
		}
		else{
			for (int k=0; k<24; k++){
				JumpDreamPart j = new JumpDreamPart(x + Calc.rangedRandom(48.8), y + Calc.rangedRandom(32.8), "FAFF58");
				j.setDepth(-3);
				j.size = 2.9 + Calc.random(12);
				j.setDirSpeed(Calc.random(360), 2 + Calc.random(2));
				j.alarm[0] = 5 + (int)Calc.random(10);
			}
		}
	}
	
	public void img(){
		imageSpeed = .15 + Calc.random(.03);
		alarm[0] = 1 + (int)Calc.random(30);
	}
	
	public void peep(){
		alarm[1] = 60 + (int)Calc.random(180);
	}
	
	public void step(){
		if (invuln > 0)
			invuln -= 1;
		
		int i = (int)imageSingle;
		imageSingle += imageSpeed;
		
		a += 1;
		if (a >= 60)
			a -= 60;
		
		while (imageSingle >= 6)
			imageSingle -= 6;
		
		if ((i == 2 || i == 5) && (int)imageSingle == i - 1)
			Sound.flap();
		
		alarmStep();
		
		if (ready)
			vTurning();
		
		if (begin)
			vspeed *= .97;
		else{
			vspeed = Calc.approach(vspeed, Math.signum(vspeed) * 2, 4);
			hspeed *= .97;
		}
		
		x += hspeed;
		y += vspeed;
		
		if (hspeed < 0)
			face = -1;
		else if (hspeed > 0)
			face = 1;
		
		playerCollisionDamagesMe();
	}
	
	/**checks if there is a solid block, upblock, or invisible turn block in the direction you are moving. if so, it flips you around. funky town*/
	public boolean vTurning(){
		double vSign = Math.signum(vspeed);
		
		if(Scene.collision(this, x, Math.round(y) + vspeed+vSign, Global.SOLIDBLOCK, Global.UPBLOCK, Global.TURNER))
		{
			double fa = vspeed;
			vspeed=0;
			y = Math.round(y);
			while(!Scene.collision(this, x, y + vSign, Global.SOLIDBLOCK, Global.UPBLOCK, Global.TURNER))
				y+=vSign;
			vspeed = fa*-1;
		}
		
		return (Scene.collision(this, x, Math.round(y)+vspeed+vSign, Global.SOLIDBLOCK, Global.UPBLOCK, Global.TURNER));
	}
	
	public void bombDamage(){
		if (invuln <= 0){
			health -= 7;
			invuln = 15;
		}
		kill();
	}
	
	public void gunDamage(){
		if (invuln <= 0){
			health -= 1.4;
			invuln = 15;
			
			vspeed *= 7.75;
			hspeed = .3;
		}
		kill();
	}
	
	public void landDamage(){
		Player.invincible = 1;
		Player.me.jump();
		Player.me.vspeed -= .8;
		Sound.play("sLandOnEnemy");
		
		if (invuln <= 0){
			health -= 1;
			invuln = 15;
			
			vspeed *= 7.75;
			hspeed = .3;
		}
		
		kill();
	}
	
	public void hurtPlayer(){
		Player.hurtPlayer(80);
	}
	
	public void alarmEvent(int i){
		if (i == 0)
			img();
		else if (i == 1){
			Sound.deep();
			peep();
		}
		else if (i == 2){
			begin = false;
			setDepth(-3);
			new BossAnnounce("DUKE",Global.roomColor,"sBossTextDuke");
			new DukeMusic();
			
			Music.fadeMusic("musBoss06Bg", true);
			
			Player.control= true;
			Player.me.cutMove = false;
			
			vspeed = 2;
			ready = true;
		}
	}
	
	public void render(){
		
		if ((invuln <= 0 || invuln % 2 == 0) || true){
			int img = (int)imageSingle;
			if (invuln > 0){
				sprite = hurt;
				img = 0;
			}
			else
				sprite = puke;
			
			double yy = 0;
			if (img == 2 || img == 5)
				yy = -5;
			sprite.render(img, orientation, x + s.x + Calc.rangedRandom(.05), y + s.y + Calc.rangedRandom(.05) + ( 2 * Math.sin(a / 15.0)) + yy, -face, 1, 0, 1, 1, 1, 1);
		}
	}

}
