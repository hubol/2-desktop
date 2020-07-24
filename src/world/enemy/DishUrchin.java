package world.enemy;

import graphics.Sprite;
import world.control.Global;
import world.control.Shake;
import world.player.Player;
import main.Calc;
import main.Entity;

public class DishUrchin extends Entity{
	public Shake s;

	public DishUrchin(double x, double y) {
		super(x, y);
		initialize(16);
	}
	
	public DishUrchin(double x, double y, double v) {
		super(x, y);
		initialize(v);
	}
	
	public final static Sprite URCHIN = Sprite.get("sDishUrchin");
	
	public void initialize(double v){
		vspeed = v;
		
		sprite = URCHIN;
		orientation = Sprite.CENTERED;
		
		setDepth(-1);
		setCollisionGroup(Global.DEACTIVATEME, Global.DEACTIVATEENEMY);
		
		s = new Shake(.2);
		
		//angle = x + y;
	}
	
	public void destroy(){
		s.destroy();
		super.destroy();
	}
	
	public void step(){
		super.step();
		
		//angle += vspeed / 2;
		
		while (y > 480)
			y -= 480;
		while (y < 0)
			y += 480;
		
		if (Calc.pointDistance(x, y, Player.me.x, Player.me.y) < 26)
			Player.hurtPlayer(50);
	}
	
	public void render(){
		sprite.render(0, orientation, x + s.x + Calc.rangedRandom(.4), y + s.y + Calc.rangedRandom(.4), 1, 1, angle, 1, 1, 1, 1);
		sprite.render(0, orientation, x + s.x + Calc.rangedRandom(.4), y + s.y + Calc.rangedRandom(.4) - 480, 1, 1, angle, 1, 1, 1, 1);
		sprite.render(0, orientation, x + s.x + Calc.rangedRandom(.4), y + s.y + Calc.rangedRandom(.4) + 480, 1, 1, angle, 1, 1, 1, 1);
	}

}
