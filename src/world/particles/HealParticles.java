package world.particles;

import world.control.Global;
import world.player.Player;
import main.Calc;
import main.Entity;

public class HealParticles extends Entity{
	public double desiredX, desiredY, ang;
	public double sin, time;
	
	public double spd;
	
	public boolean fade = false;
	public double mult = 0;

	public HealParticles(double spd) {
		super(0, 0);
		setCollisionGroup(Global.DEACTIVATEME);
		setDepth(Integer.MIN_VALUE + 18);
		time = 10 + Calc.random(65);
		sin = Calc.random(time);
		alarmInitialize(1);
		alarm[0] = 90 + (int)Calc.random(30);
		
		this.spd = spd;
		
		desired();
		x = desiredX;
		y = desiredY;
		
		animation();
		
		mult = 0;
		
		sprite = Global.sHEART;
		imageSingle = Calc.random(2);
		imageSpeed = Calc.random(.3);
	}
	
	public void alarmEvent(int i){
		fade = true;
	}
	
	public void desired(){
		desiredX = Player.me.x + Calc.dirX(32, ang);
		desiredY = Player.me.y + Calc.dirY(32, ang);
	}
	
	public void animation(){
		sin += 1;
		xscale = .5 + Math.abs(.18 * Math.sin(sin / (time / 2.0)));
		yscale = xscale;
		while (sin > time)
			sin -= time;
	}
	
	public void step(){
		if (fade){
			alpha -= .06;
			mult = Calc.approach(mult, 2, 25);
		}
		else
			mult = Calc.approach(mult, 1, 2.5);
		if (alpha <= 0)
			destroy();
		
		desired();
		ang += spd;
		x = Calc.approach(x, desiredX, 2);
		y = Calc.approach(y, desiredY, 2);
		
		x += Calc.rangedRandom(.6);
		y += Calc.rangedRandom(.6);
		
		super.step();
		animation();
	}
	
	public void render(){
		int i = (int)imageSingle;
		if (i == 0)
			i = (Integer)Calc.choose(0,2,4,6);
		else
			i = (Integer)Calc.choose(1,3,5,7);
		
		sprite.render(i, orientation, x + Calc.rangedRandom(Calc.random(1)), y + Calc.rangedRandom(Calc.random(1)), xscale * mult, yscale * mult, ang - 90, alpha, "ffffff");
	}

}
