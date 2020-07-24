package world.boss.duke;

import main.Calc;
import graphics.Sprite;
import world.Fg;
import world.control.Shake;
import world.control.Sound;
import world.enemy.BasicEnemy;
import world.player.Player;

public class Duke extends BasicEnemy{
	public Shake s = new Shake(.7);
	public int a = 0;
	public boolean last = false;
	
	public Duke(double x, double y) {
		super(x, y);
		
		sprite = Sprite.get("sDuke");
		mask = Sprite.get("sPukeMask").mask;
		setDepth(-3);
		alarmInitialize(2);
		
		xscale = 0;
		
		img();
		peep();
	}
	
	public void destroy(){
		s.destroy();
		super.destroy();
	}
	
	public void kill(){
		Fg.me.shakeTimer = 6;
		
		Sound.deep();
		Sound.play("sLandOnEnemy");
		Sound.playPitched("sBossTextPuke", .3);
		
		if (!last)
			new DukeDeath(x, y, false);
		else
			new DukeDeath(x, y);
		destroy();
	}
	
	public void img(){
		imageSpeed = .15 + Calc.random(.03);
		alarm[0] = 1 + (int)Calc.random(30);
	}
	
	public void peep(){
		alarm[1] = 60 + (int)Calc.random(180);
	}
	
	public void step(){
		int i = (int)imageSingle;
		imageSingle += imageSpeed;
		
		xscale = Calc.approach(xscale, 1, 2.5);
		
		a += 1;
		if (a >= 60)
			a -= 60;
		
		while (imageSingle >= 6)
			imageSingle -= 6;
		
		if ((i == 2 || i == 5) && (int)imageSingle == i - 1)
			Sound.flap();
		
		alarmStep();
		
		playerCollisionDamagesMe();
	}
	
	public void bombDamage(){
		kill();
	}
	
	public void gunDamage(){
		kill();
	}
	
	public void landDamage(){
		Player.invincible = 1;
		Player.me.jump();
		Player.me.vspeed -= .8;
		Sound.play("sLandOnEnemy");
		kill();
	}
	
	public void hurtPlayer(){
		Player.hurtPlayer(140);
	}
	
	public void alarmEvent(int i){
		if (i == 0)
			img();
		else if (i == 1){
			Sound.deep();
			peep();
		}
	}
	
	public void render(){
		int img = (int)imageSingle;

		double yy = 0;
		if (img == 2 || img == 5)
			yy = -5;
		sprite.render(img, orientation, x + s.x + Calc.rangedRandom(.05), y + s.y + Calc.rangedRandom(.05) + ( 2 * Math.sin(a / 15.0)) + yy, xscale, xscale, 0, 1, 1, 1, 1);
	}

}
