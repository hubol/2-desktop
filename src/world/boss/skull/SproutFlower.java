package world.boss.skull;

import graphics.Sprite;
import world.control.Global;
import world.control.Sound;
import world.player.Player;
import main.Calc;
import main.Entity;

public class SproutFlower extends Entity{
	public double scale, life, initLife;
	public double add;
	
	public Skull mother;
	
	public SproutFlower child;
	public boolean spawned;
	
	public double vineImage, vineXscale;
	
	public Sprite sprout, petal;

	public SproutFlower(double x, double y, double timer, Skull mom) {
		super(x, y);
		
		Sound.soundIndexedPlay("sSkullFlowerAppear", 4);
		
		setDepth(-6);
		
		sprout = mom.sprout;
		petal = mom.smallPetal;
		
		child = null;
		spawned = false;
		
		vineImage = -1;
		vineXscale = (Double)Calc.choose(1.0, -1.0);
		
		scale = 0;
		life = timer;
		initLife = life;
		
		mother = mom;
		
		add = Calc.rangedRandom(8);
		
		setCollisionGroup(Global.DEACTIVATEME);
	}
	
	public void step(){
		scale = Calc.approach(scale, .8, 3);
		
		if (Calc.pointDistance(x, y, Player.me.x, Player.me.y) < 40.8 * scale)
			Player.hurtPlayer(40);
		
		angle += add;
		
		life -= 1;
		
		if (life <= (initLife * .925) && !spawned && y > 64){
			if (vineImage < 0)
				vineImage = 0;
			vineImage += .65;
			vineImage *= 1.05;
			if (vineImage >= 13){
				spawned = true;
				vineImage = 12.9;
				new SproutFlower(x, y - 64, Math.max(5, initLife - 8), mother);
			}
		}
		
		if (life <= 0)
			destroy();
	}
	
	public void destroy(){
		Sound.soundIndexedPlay("sSkullFlowerDestroy", 4);
		
		for (int i=0; i<9; i++){
			double dir = Calc.random(360), dist = Calc.random(16);
			new FlowerParticle(x + Calc.dirX(dist, dir), y + Calc.dirY(dist, dir), 90 + Calc.rangedRandom(70));
		}
		super.destroy();
	}
	
	public void render(){
		if (vineImage >= 0)
			sprout.render(vineImage, Sprite.SOUTH, x, y, 1.3 * vineXscale, 2.5, 0, 1, 1, 1, 1);
		
		final int petals = 7;
		final double mult = 360.0 / (double)petals;
		for (int i=0; i<petals; i++)
			petal.render(0, Sprite.WEST, x + Calc.rangedRandom(.5), y + Calc.rangedRandom(.5), .9 * scale, .95 * scale, angle + (i * mult) + Calc.rangedRandom(.5), 1, 1, 1, 1);
		Sprite.get("sSkullFlowerCenter").render(0, Sprite.CENTERED, x + Calc.rangedRandom(.5), y + Calc.rangedRandom(.5), .9 * scale, .9 * scale, -angle, 1, 1, 1, 1);
	}

}
