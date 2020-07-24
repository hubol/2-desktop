package world.boss.slick;

import graphics.Sprite;
import world.Fg;
import world.control.Global;
import world.control.Shake;
import world.control.Sound;
import world.enemy.BasicEnemy;
import world.player.Player;
import main.Calc;

public class SlickBoulder extends BasicEnemy{
	public Shake s;
	public double timer;
	public Slick mom;
	
	public double img;

	public SlickBoulder(double x, Slick mommy) {
		super(x, -32);
		s = new Shake(.3);
		
		mom = mommy;
		
		sprite = Sprite.get("sSlickBoulder");
		mask = Sprite.get("sSlickBoulderMask").mask;
		
		reset();
		
		img = 0;
		
		imageSingle = 0;
		imageSpeed = 0;
		
		vspeed = mom.g.rangedRandom(1);
		
		//setDepth(-1);
		setDepth(Integer.MIN_VALUE+19);
		setCollisionGroup(Global.DEACTIVATEME, Global.BASICENEMY);
	}
	
	public void destroy(){
		s.destroy();
		super.destroy();
	}
	
	public void reset(){
		angle = Calc.random(360);
		xscale = (Integer)Calc.choose(1,-1);
		yscale = (Integer)Calc.choose(1,-1);
		timer = 1 + Calc.random(19);
	}
	
	public void step(){
		vspeed += .6;
		if (imageSingle == 1){
			vspeed *= 1.02;
			img += .25;
		}
		super.step();
		if (y >= 352 && imageSingle == 0){
			Fg.me.shakeTimer = 15;
			y = 352;
			imageSingle = 1;
			vspeed = -6 + mom.g.rangedRandom(.5);
			//setDepth(Integer.MIN_VALUE+19);
			Sound.playPitched("sSlickBoulderLand", .05);
		}
		
		if (Calc.pointDistance(x, y, Player.me.x, Player.me.y) < 44)
			Player.hurtPlayer(70);
		
		timer -= 1.0 + (imageSingle / 2.0);
		if (timer <= 0)
			reset();
		
		if (y > 524)
			destroy();
	}
	
	public void render(){
		double x = this.x, y = this.y;
		this.x += s.x + Calc.rangedRandom(.1);
		this.y += s.y + Calc.rangedRandom(.1);
		super.render();
		this.x = x;
		this.y = y;
		
		if (img < 2 && imageSingle == 1)
			Sprite.get("sSlickDebris").render(Math.min(1, 2 - img), Sprite.SOUTH, x + s.x + Calc.rangedRandom(.1), 384 + s.y + Calc.rangedRandom(.1), 1.1, 1.1, 0, 1, 1, 1, 1);
	}
	
	public int playerCollisionDamagesMe(){
		return 1;
	}
	
	public void gunDamage(){
		
	}

}
