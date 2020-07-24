package world.event;

import graphics.Sprite;
import world.control.Global;
import world.control.Shake;
import world.control.Sound;
import world.gameplay.Upblock;
import world.particles.JumpDreamPart;
import main.Calc;
import main.Entity;

public class Brick extends Entity{
	public boolean fall, fell;
	public double toY = -1, a = 0;
	public double mult = 1;
	public Shake s;
	
	public String color;
	public Sprite back;
	
	public int image, bmage;
	
	public boolean arf = false;

	public Brick(double x, double y, String c) {
		super(x, y);
		fall = false;
		fell = true;
		toY = y;
		upblock();
		
		s = new Shake(.12);
		
		color = c;
		image = (int)((Math.abs(x + y * (x - y)) % 80) / 16);
		bmage = (int)((Math.abs(y + x * (y - x)) % 70) / 14);
		
		setCollisionGroup(Global.DEACTIVATEME);
		setDepth(9);
	}
	
	public Brick(double x, double y, boolean f, String c){
		super(x, y - 480);
		toY = y;
		fall = true;
		fell = false;
		
		s = new Shake(.12);
		
		color = c;
		image = (int)((Math.abs(x + y * (x - y)) % 80) / 16);
		bmage = (int)((Math.abs(y + x * (y - x)) % 70) / 14);
		
		setCollisionGroup(Global.DEACTIVATEME);
		setDepth(9);
	}
	
	public void upblock(){
		new Upblock(x, toY);
	}
	
	public void step(){
		if (fall)
			vspeed += .82;
		
		super.step();
		angle = a * mult;
		mult *= -1;
		a *= .5;
		
		if (fell && !fall)
			y = Calc.approach(y, toY, 2);
		
		if (fall && !fell && vspeed > 0 && y >= toY){
			fell = true;
			fall = false;
			vspeed = 0;
			a = 0;
			
			upblock();
			
			for (int i=0; i<10; i++){
				JumpDreamPart j = new JumpDreamPart(x + Calc.random(32), y + 8 + Calc.rangedRandom(1), "008FF7");
				j.setDepth(5);
				j.size = 3.2 + Calc.random(3.8);
				j.setDirSpeed(Calc.random(180), .8 + Calc.random(.9));
			}

			y = toY;
			Sound.playPitched("sBrickLand", .1);
		}
	}
	
	public void render(){
		if (!arf){
			sprite = Sprite.get("sBrickLine");
			back = Sprite.get("sBrickSolid");
			arf = true;
		}
		
		double x = this.x + s.x + Calc.rangedRandom(.05) + 16, y = this.y + s.y + Calc.rangedRandom(.05) + 8;
		sprite.render(image, orientation, x, y, 1, 1, angle, 1, 1, 1, 1);
		back.render(bmage, orientation, x, y, 1, 1, angle, 1, color);
	}

}
