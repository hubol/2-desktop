package world.boss.skull;

import graphics.Graphics;
import graphics.Shape;
import graphics.Sprite;
import world.control.Global;
import world.player.Player;
import main.Calc;
import main.Entity;

public class SkullFlower extends Entity{
	public boolean hit;
	public double mult, bluh, deathMult, initMult;
	
	public final Sprite petal = Sprite.get("sSkullPetalSmall");
	
	public final int petals = 7;
	public double[] petalAlpha, petalAngle, petalX, petalY, petalSub, petalAdd, petalDir;
	public boolean dead;
	
	public double centerX, centerY, centerHsp, centerVsp, dir;
	
	public double popDir = 0;
	public double timer;

	public SkullFlower(double x, double y) {
		super(x, y);
		
		setCollisionGroup(Global.DEACTIVATEME);
		
		dir = Calc.pointDirection(x, y, Player.me.x, Player.me.y);
		
		centerX = 0;
		centerY = 0;
		centerHsp = Calc.rangedRandom(3);
		centerVsp = -Calc.random(3);
		
		timer = 15 + (Skull.me.invHealthPercent() * 30);
		
		initMult = 0;
		setDepth(-4);
		
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
	}
	
	public void step(){
		if (initMult < 1.0){
			initMult = Calc.approach(initMult, 1.0, 6 - (2.1 * Skull.me.invHealthPercent()));
			initMult += .00975;
		}
		else{
			if (getSpeed() == 0)
				setDirSpeed(dir, 8 + (6 * Skull.me.invHealthPercent()));
			initMult = 1.0;
		}
		
		if (!dead){
			double dir = Calc.random(360), dist = (6 + Calc.random(42)) * initMult;
			new PetalParticle(x + Calc.dirX(dist, dir), y + Calc.dirY(dist, dir));
			setSpeed(getSpeed()*1.01);
			angle += (4 + (getSpeed() * 1.6)) * mult;
		}
		
		super.step();
		
		if (!dead){
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
		Graphics.setColor("007CFF");
		Shape.drawLine(x, y, x + Calc.dirX(960, dir), y + Calc.dirY(960, dir), 7 * initMult);
		Graphics.setColor("FF1053");
		Shape.drawLine(x, y, x + Calc.dirX(960, dir), y + Calc.dirY(960, dir), 2 * initMult);
		
		double dir = angle;
		for (int i=0; i<petals; i++){
			petal.render(0, Sprite.WEST, x + petalX[i] + Calc.rangedRandom(.3), y + petalY[i] + Calc.rangedRandom(.3), .8 * initMult, .77 * initMult, petalAngle[i] + dir, petalAlpha[i], 1, 1, 1);
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
