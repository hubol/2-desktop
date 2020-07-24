package world.junk;

import graphics.Sprite;
import world.control.Global;
import world.control.Shake;
import world.enemy.BasicEnemy;
import world.player.Player;
import main.Calc;
import main.Scene;

public class Fruit extends BasicEnemy{
	public boolean done;
	public String s;
	public int type;
	
	public Shake g = new Shake(.6);
	
	public FruitControl mom;
	
	public final static String[] colors = new String[]{"FF1843", "FFFB18"};

	public Fruit(double x, double y, int i) {
		super(x, y);
		done = false;
		s = "sStrawberry";
		if (i == 1)
			s = "sLemon";
		
		xscale = .65;
		yscale = xscale;
		
		type = i;
		
		mom = Scene.getEntityList(FruitControl.class).get(0);
		
		setDepth(-1);
		setCollisionGroup(Global.DEACTIVATEME, Global.BASICENEMY);
	}
	
	public void pop(){
		for (int i=0; i<13; i++)
			new FruitExplode(x + Calc.rangedRandom(5), y + Calc.rangedRandom(5), type);
	}
	
	public void touch(){
		pop();
		mom.collect(type);
		destroy();
	}
	
	public void step(){
		angle += 4;
		
		if (Calc.pointDistance(x, y, Player.me.x, Player.me.y) < 30)
			touch();
	}
	
	public void render(){
		if (!done){
			if (Sprite.exists(s)){
				sprite = Sprite.get(s);
				mask = sprite.mask;
				done = true;
			}
		}
		if (done)
			sprite.render(0, orientation, x + Calc.rangedRandom(.2) + g.x, y + Calc.rangedRandom(.2) + g.y, xscale, xscale, angle, 1, 1, 1, 1);
	}
	
	public int playerCollisionDamagesMe(){
		return 1;
	}
	
	public void gunDamage(){
		touch();
	}

}
