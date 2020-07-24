package world.minigame;

import graphics.Graphics;
import graphics.Sprite;
import world.control.Global;
import world.control.Music;
import world.control.SpriteLoader;
import world.interact.BigTree;
import main.Calc;
import main.Entity;

public class ChopControl extends Entity{
	public int steps, hits;
	public double scale, ang;
	public BigTree mother;

	public ChopControl(BigTree m) {
		super(0, 0);
		Music.changeMusic("musAxe");
		steps = 0;
		
		mother = m;
		
		hits = 0;
		
		scale = 0;
		ang = 480;
		
		Global.preventAudioCrash();
		
		setDepth(Integer.MIN_VALUE+17);
	}
	
	public void step(){
		steps += 1;
		if (steps == 2)
			new SpriteLoader("sTreeExplosionA_3", "sTreeExplosionB_3", "sTreeArrow_4","sTreeArrowAttention_2","sTreeDebris_2","sTreeClouds_4","sTreeMiniBackground","sTreeMiniBackgroundLines","sTreeMiniAxe_3", "sTreeMiniChopped_3", "sTreeMiniGem_3", "sTreeMiniGirl_3", "sTreeMiniTarget_3", "sTreeMiniTree_3");
		
		if (steps > 2){
			ang = Calc.approach(ang, 0, 10);
			scale = Calc.approach(scale, 1, 7);
		}
		
		scale = 1;
		ang = 0;
		
		if (steps == 30){
			for (int i = 0; i<40; i++)
				new ChopClouds(Calc.random(640), 120 + Calc.random(240));
			new ChopPlayer(320, 400, this);
		}
	}
	
	public void render(){
		if (steps > 30){
			Graphics.setColor("0C91FF");
			//Shape.drawCircle(640 - 64, 480 - 64, 58);
		}
		
		if (steps > 2){
			Sprite.get("sTreeMiniBackgroundLines").render(0, Sprite.CENTERED, 320 + Calc.rangedRandom(.5), 240 + Calc.rangedRandom(.5), scale, scale, ang, 1, 1, 1, 1);
			if (steps > 30){
				Graphics.setColor("32A6FF");
				//Shape.drawCircle(640 - 64, 480 - 64, 55);
			}
			Sprite.get("sTreeMiniBackground").render(0, Sprite.CENTERED, 320 + Calc.rangedRandom(.5), 240 + Calc.rangedRandom(.5), scale, scale, ang, 1, 1, 1, 1);
		}
	}

}
