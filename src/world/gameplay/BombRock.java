package world.gameplay;

import world.control.Global;
import world.control.Sound;
import world.event.NoOccupy;
import world.particles.Debris;
import world.player.Player;
import graphics.Sprite;
import main.Calc;
import main.Entity;

public class BombRock extends Entity{

	public BombRock(double x, double y) {
		super(x, y);
		sprite = Global.sBOMBROCK;
		mask = sprite.mask;
		orientation = Sprite.NORTHWEST;
		setCollisionGroup(Global.SOLIDBLOCK, Global.BOMBROCK);
		addCollisionGroup(Global.TEMPSOLID);
		imageSingle = Calc.random(2);
		imageSpeed = 7.0 / 30.0;
		
		setDepth(-11);
	}
	
	public void step(){
		super.step();
	}
	
	public final static Sprite PARTS = Sprite.get("sBombRockParts");
	
	public void shatter(){
		Sound.playPitched("sRockBreak");
		if (Player.me.random(1) > .6)
			Global.dropRandomLoot(x+16, y+16, 6);
		for (int i=0; i<9; i++){
			new Debris(x + 4 + Calc.random(20), y + 4 + Calc.random(20), PARTS);
		}
		new NoOccupy(x, y);
		
		super.destroy();
	}
	
	public void render(){
		double zx = x, zy = y;
		x += -.5 + Calc.random(1);
		y += -.5 + Calc.random(1);
		super.render();
		x = zx;
		y = zy;
	}

}
