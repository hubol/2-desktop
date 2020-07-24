package world.boss.skull;

import graphics.Sprite;
import world.control.Global;
import world.player.Player;
import main.Calc;
import main.Entity;

public class SmallFlower extends Entity{
	public boolean hurter, hit;
	public double mult, bluh, deathMult, initMult;
	
	public final int petals = 7;
	public double[] petalAlpha, petalAngle, petalX, petalY, petalSub, petalAdd, petalDir;
	public boolean dead;
	
	public double centerX, centerY, centerHsp, centerVsp;
	
	public double popDir = 0;

	public SmallFlower(double x, double y, boolean a) {
		super(x, y);
		
		setCollisionGroup(Global.DEACTIVATEME);
		
		centerX = 0;
		centerY = 0;
		centerHsp = Calc.rangedRandom(3);
		centerVsp = -Calc.random(3);
		
		initMult = 0;
		setDepth(-4);
		hurter = a;
		
		mult = (Double)Calc.choose(1.0, -1.0);
		bluh = 0;
		hit = false;
		
		deathMult = 4.0;
		
		petalAlpha = new double[petals];
		petalAngle = new double[petals];
		petalX = new double[petals];
		petalY = new double[petals];
		petalSub = new double[petals];
		petalAdd = new double[petals];
		petalDir = new double[petals];
		for (int i=0; i<petals; i++){
			petalAlpha[i] = 1.0;
			petalAngle[i] = 0.0;
			
			petalX[i] = 0.0;
			petalY[i] = 0.0;
			
			petalSub[i] = -.01 - Calc.random(.04);
			petalAdd[i] = 1 + Calc.random(4);
			petalDir[i] = Calc.random(360);
		}
		
		if (a){
			setDirSpeed(Calc.pointDirection(x, y, Player.me.x, Player.me.y), 8 + (3 * Skull.me.invHealthPercent()));
		}
		else{
			bluh = Calc.rangedRandom(5.0);
		}
	}
	
	public void step(){
		if (initMult < 1.0){
			initMult = Calc.approach(initMult, 1.0, 4);
			initMult += .00975;
		}
		else
			initMult = 1.0;
		
		if (!dead){
			if (hurter){
				double dir = Calc.random(360), dist = 6 + Calc.random(42);
				new PetalParticle(x + Calc.dirX(dist, dir), y + Calc.dirY(dist, dir));
				setSpeed(getSpeed()*1.01);
				angle += getSpeed() * 1.8 * mult;
			}
			else{
				angle += bluh;
			}
		}
		
		/*if (dead){
			boolean shit = true;
			for (int i=0; i<petals; i++){
				petalAlpha[i] += petalSub[i];
				petalAngle[i] += petalAdd[i];
				petalX[i] += Calc.dirX(petalAdd[i] * (1.5 - petalSub[i]), petalDir[i]) * deathMult;
				petalY[i] += Calc.dirY(petalAdd[i] * (1.5 - petalSub[i]), petalDir[i]) * deathMult;
				petalDir[i] += petalAdd[i] * mult * 1.2;
				if (petalAlpha[i] <= 0)
					petalAlpha[i] = 0;
				else
					shit = false;
			}
			
			deathMult -= .1;
			deathMult *= .9;
			if (deathMult < 1.0)
				deathMult = 1.0;
			
			centerX += centerHsp;
			centerY += centerVsp;
			centerHsp *= .98;
			centerVsp += .81;
			
			if (shit && centerY >= 500)
				destroy();
		}*/
		
		super.step();
		
		if (hurter && !dead){
			if (x <= 64 || y <= 64 || x >= 576 || y >= 416){
				if (x <= 64)
					popDir = 0;
				else if (y >= 416)
					popDir = 90;
				else if (x >= 576)
					popDir = 180;
				else if (y <= 64)
					popDir = 270;
				kill();
			}
			else if (Calc.pointDistance(x, y, Player.me.x, Player.me.y) < 32){
				hit = true;
				Player.hurtPlayer(25);
				kill();
			}
		}
	}
	
	public void render(){
		double dir = angle;
		for (int i=0; i<petals; i++){
			Sprite.get("sSkullPetalSmall").render(0, Sprite.WEST, x + petalX[i] + Calc.rangedRandom(.3), y + petalY[i] + Calc.rangedRandom(.3), .8 * initMult, .77 * initMult, petalAngle[i] + dir, petalAlpha[i], 1, 1, 1);
			dir += (360.0 / (double)petals);
		}
		Sprite.get("sSkullFlowerCenter").render(0, Sprite.CENTERED, x + centerX + Calc.rangedRandom(.3), y + centerY + Calc.rangedRandom(.3), .75 * initMult, .75 * initMult, angle * .5, petalAlpha[0], 1, 1, 1);
	}
	
	public void kill(){
		for (int i=0; i<14; i++){
			double d = popDir + Calc.rangedRandom(90);
			if (hit)
				d = Calc.random(360);
			new FlowerParticle(x, y, d);
		}
		
		/*dead = true;
		setSpeed(0);*/
		destroy();
	}

}
