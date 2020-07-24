package world.event;

import audio.Audio;
import graphics.Sprite;
import world.control.EV;
import world.control.Global;
import world.control.Shake;
import world.control.Sound;
import world.player.Player;
import main.Calc;
import main.Entity;

public class Butterfly extends Entity{
	public int id;
	public double a, yAt, xAt;
	public Shake s = new Shake();
	
	public double thing, dingus;

	public Butterfly(double x, double y, int id) {
		super(x, y);
		this.id = id;
		
		setCollisionGroup(Global.DEACTIVATEME);
		setDepth(-1);
		
		a = random(30);
		
		thing = 12 + random(8);
		dingus = 4 + random(4);
		
		if (Global.butterfly[id]||Global.event[EV.TRUCK_UNLOCKED]==0){
			visible = false;
			if (Global.room(9,5))
				new ButterflyMarker(x, y, id);
			destroy();
		}
		
		sprite = Sprite.get("sButterfly");
		
		xAt = 0;
		yAt = 0;
		
		imageSingle = random(2);
		
		alarmInitialize(1);
		imageSpeed = .2 + Calc.random(.35);
		alarm[0] = 1 + (int)Calc.random(30);
	}
	
	public void destroy(){
		s.destroy();
		super.destroy();
	}
	
	public double random(double a){
		return ((Math.abs(((xstart * -.2) + (ystart * 1.1) + ((double)Global.roomX * 2.95) - (a * 2.3))) % 1.1) / 1.1) * a;
	}
	
	public void alarmEvent(int i){
		if (i == 0){
			imageSpeed = .2 + Calc.random(.35);
			alarm[0] = 1 + (int)Calc.random(30);
		}
	}
	
	public void step(){
		xAt = Calc.approach(xAt, 0, 2);
		x = xstart + xAt + (s.x / 3.0);
		yAt = Calc.approach(yAt, (dingus * Math.sin(a / thing)), 2);
		y = ystart + yAt + (s.y / 3.0);
		a += 1;
		
		if (Calc.pointDistance(x, y, Player.me.x, Player.me.y) < 25 && visible){
			Global.butterfly[id] = true;
			Audio.get("sGetButterfly").setPitch(1 + (.25 * ((double)Global.butterfliesCaught() / 50.0)) + Calc.rangedRandom(.005));
			Audio.get("sGetButterfly2").setPitch(1 + (.25 * ((double)Global.butterfliesCaught() / 50.0)) + Calc.rangedRandom(.005));
			Sound.play("sGetButterfly");
			Sound.play("sGetButterfly2");
			new ButterflyText(x, y);
			
			Global.squareParticle(x, y, 6, "007CFF", 4);
			
			if (Global.butterfliesCaught() == 50)
				Global.eventItemGet(2);
			
			destroy();
		}
		
		super.step();
	}
	
	public void render(){
		sprite.render(imageSingle, Sprite.CENTERED, x + Calc.rangedRandom(.2), y + Calc.rangedRandom(.2), 1, 1, 0, 1, 1, 1, 1);
		sprite.render(imageSingle, Sprite.CENTERED, x + Calc.rangedRandom(.2), y + Calc.rangedRandom(.2) + 480, 1, 1, 0, 1, 1, 1, 1);
		sprite.render(imageSingle, Sprite.CENTERED, x + Calc.rangedRandom(.2), y + Calc.rangedRandom(.2) - 480, 1, 1, 0, 1, 1, 1, 1);
	}

}
