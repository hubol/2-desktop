package world.event;

import graphics.Sprite;
import world.control.Global;
import main.Calc;
import main.Entity;

public class Penis extends Entity{
	public double seed;
	public boolean pubes, balls;
	public String[] colors;
	
	public double timer;
	public boolean ejaculating;
	
	public double a;
	public double ang, ysc, xsc, addX, addY;
	public double restAng, restYsc, restXsc, ejacAng, ejacYsc, ejacXsc;
	
	public double mult;
	
	public int sound;

	public Penis(double x, double y, double angle) {
		super(x, y);
		
		setCollisionGroup(Global.DEACTIVATEME);
		setDepth(-5);
		
		this.angle = angle;
		a = 0;
		
		mult = 1;
		
		seed = ((((y * y * y * y * y) % 15469789.1) * ((x * angle) % 913.2)) + (x * x * (x % 489.9) * (x % 432.2) * x) - ((y - 420.69) * (x + 69.42)) - ((angle * y) / (x * y)));
		
		if (angle == 180)
			yscale = -1;
		
		double add = 5;
		addX = 0; addY =0;
		if (angle == 0)
			addX = -add;
		else if (angle == 90)
			addY = add;
		else if (angle == 180)
			addX = add;
		else
			addY = -add;
		
		pubes = random(1) < .5;
		balls = random(1) < .5;
		colors = new String[3];
		for (int i=0; i<3; i++)
			colors[i] = Calc.makeHexColor(Calc.getColorHSV((int)random(256), 150, 255));
		
		restAng = -5 - random(10);
		restYsc = .7 + random(.2);
		restXsc = .5 + random(.4);
		if (angle != 90)
			ejacAng = restAng + random(40);
		else
			ejacAng = restAng + 15 + random(40);
		ejacYsc = .925 + random(.2);
		ejacXsc = (restXsc * 1.1) + random(.3);
		
		ejaculating = random(1) < .1;
		timer = 60 + random(260);
		ang = restAng; xsc = restYsc; ysc = restYsc;
		if (ejaculating){
			timer = random(60);
			ang = ejacAng; xsc = ejacYsc; ysc = ejacYsc;
		}
		
		sound = (int)random(3) + 1;
	}
	
	public void step(){
		a += 1;
		
		timer -= 1;
		if (timer <= 0){
			ejaculating = !ejaculating;
			timer = 60 + random(260);
			if (ejaculating){
				timer = 60;
				mult = 1.5;
			}
		}
		
		double aAng = restAng, aXsc = restXsc, aYsc = restYsc, aprch = 24;
		if (ejaculating){
			aAng = ejacAng + rangedRandom(7) + (8 * Math.sin(a / 4.0));
			aXsc = ejacXsc + rangedRandom(.1);
			aYsc = ejacYsc + rangedRandom(.1) + (.2 * Math.sin(a / 3.0));
			aprch = 12;
		}
		ang = Calc.approach(ang, aAng, aprch);
		xsc = Calc.approach(xsc, aXsc, aprch);
		ysc = Calc.approach(ysc, aYsc, aprch);
		
		mult = Calc.approach(mult, 1.0, 10);
		
		if (ejaculating){
			double dist = 72 * xsc, dir = angle + ((ang + 10) * yscale);
			Semen i = new Semen(x + addX + Calc.dirX(dist,  dir), y + addY + Calc.dirY(dist, dir), dir, sound);
			i.mult *= mult;
			i.setSpeed(i.getSpeed() * mult);
		}
	}
	
	public double random(double x){
		double output = Math.abs(seed / 421.0) % 1;
		seed += (this.x * 69.35) - (y * 70.7) + (angle * 72.35) + ((this.x * this.x * this.x) * (y * y * y)) + (angle * y * this.x) - 69.72 + x;
		return output * x;
	}
	
	public double rangedRandom(double x){
		return -x + random(x * 2.0);
	}
	
	public void render(){
		Sprite s = Sprite.get("sPhallus");
		
		//balls
		s.render(4, Sprite.WEST, x + rangedRandom(.4) + addX, y + rangedRandom(.4) + addY, Math.abs(Calc.approach(ysc, 1, 6)), yscale * Calc.approach(ysc, 1, 6), angle, 1, colors[0]);
		
		int j = 0;
		if (balls)
			j = 1;
		s.render(2 + j, Sprite.WEST, x + rangedRandom(.4) + addX, y + rangedRandom(.4) + addY, Math.abs(Calc.approach(ysc, 1, 6)), yscale * Calc.approach(ysc, 1, 6), angle, 1, 1, 1, 1);
		
		//back of phallus UuU
		for (int i=0; i < 2; i++)
			s.render(5 + i, Sprite.WEST, x + rangedRandom(.4) + addX, y + rangedRandom(.4) + addY, xsc, ysc * yscale, angle + (ang * yscale), 1, colors[1 + i]);
		
		//outlines
		s.render(7, Sprite.WEST, x + rangedRandom(.4) + addX, y + rangedRandom(.4) + addY, xsc, ysc * yscale, angle + (ang * yscale), 1, colors[0]);
		s.render(1, Sprite.WEST, x + rangedRandom(.4) + addX, y + rangedRandom(.4) + addY, xsc, ysc * yscale, angle + (ang * yscale), 1, 1, 1, 1);
		if (pubes)
			s.render(0, Sprite.WEST, x + rangedRandom(.4) + addX, y + rangedRandom(.4) + addY, xsc, ysc * yscale, angle + ((ang * yscale) / 16.0), 1, 1, 1, 1);
	}

}
