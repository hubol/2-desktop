package world.gameplay;

import java.util.ArrayList;

import world.boss.jelly.JellyBubble;
import world.boss.jelly.JellyEnemy;
import world.boss.jelly.JellyRender;
import world.control.Global;
import world.control.Sound;
import world.event.NoOccupy;
import graphics.Sprite;
import main.Calc;
import main.Scene;

public class JellyBlock extends JellyEnemy{
	public final static Sprite BACK = Sprite.get("sJellyBlockBack"), BLOCK = Sprite.get("sJellyBlock"), SHEEN = Sprite.get("sJellyBlockSheen");
	
	public double timer;
	public boolean sheen;

	public JellyBlock(double x, double y) {
		super(x, y);
		orientation=Sprite.NORTHWEST;
		setDepth(-10);
		sprite=Global.sBLOCK;
		mask=sprite.mask;
		setCollisionGroup(Global.SOLIDBLOCK, Global.BLOCK, Global.BASICENEMY, Global.TEMPSOLID, Global.JELLYBLOCK, Global.DEACTIVATEME); //solid block group
		
		for (int i=0; i<3; i++)
			spawn();
		
		if (Scene.getEntityList(JellyRender.class).size() == 0)
			new JellyRender(0, 0);
		
		sheen = false;
		
		timer = Calc.random(7);
		determineSheen();
	}
	
	public void gunDamage(){
		killMe();
	}
	
	public void bombDamage(){
		killMe();
	}
	
	public void determineSheen(){
		sheen = false;
		
		if (!Scene.collision(this, x, y - 31, Global.JELLYBLOCK) && !Scene.collision(this, x + 31, y, Global.JELLYBLOCK))
			sheen = true;
	}
	
	public void killMe(){
		Sound.popPlay();
		
		for (int i=0; i<8; i++)
			new JellyBubble(x + Calc.random(32), y + Calc.random(32), 8);
		
		new NoOccupy(x, y);
		
		destroy();
		
		ArrayList<JellyBlock> list = Scene.getEntityList(JellyBlock.class);
		for (int i=0; i<list.size(); i++)
			list.get(i).determineSheen();
	}
	
	public void step(){
		timer -= 1;
		if (timer <= 0){
			timer = 13 + Calc.random(3);
			spawn();
		}
	}
	
	public void spawn(){
		new JellyBubble(x + Calc.random(32), y + Calc.random(32), 1);
	}
	
	public void render(){
		
	}
	
	public void renderBack(){
		BACK.render((int)imageSingle, Sprite.CENTERED, x + 16 + Calc.rangedRandom(.6), y + 16 + Calc.rangedRandom(.6), xscale, yscale, angle, alpha, colR, colG, colB);
	}
	
	public void renderFront(){
		BLOCK.render((int)imageSingle, Sprite.CENTERED, x + 16 + Calc.rangedRandom(.6), y + 16 + Calc.rangedRandom(.6), xscale, yscale, angle, alpha, colR, colG, colB);
			if (sheen)
				SHEEN.render(1, Sprite.CENTERED, x + 16 + Calc.rangedRandom(.6), y + 16 + Calc.rangedRandom(.6), xscale, yscale, angle, alpha, colR, colG, colB);
	}

}
