package world.boss.banaan;

import world.control.Global;
import world.control.Sound;
import world.particles.PieceParticle;
import world.player.Player;
import graphics.Sprite;
import main.Calc;
import main.Entity;

public class BanaanVPuke extends Entity{

	public BanaanVPuke(double x, double y, double v) {
		super(x, y);
		
		orientation = Sprite.CENTERED;
		sprite = Sprite.get("sBanaanPuk");
		imageSpeed = .5;
		
		vspeed = v;
		setCollisionGroup(Global.DEACTIVATEME);
	}
	
	public void step(){
		super.step();
		
		if (y >= 368){
			Sound.playPitched("sBanaanPukeDie");
			destroy();
		}
		else if (Calc.pointDistance(x, y, Player.me.x, Player.me.y) < 20){
			Player.hurtPlayer(65);
			for(int i=0; i<5; i++)
				new PieceParticle(x + Calc.rangedRandom(16), y + Calc.rangedRandom(16), "sBanaanPuk", Calc.random(10), Calc.random(.5), Calc.random(28), Calc.random(28), 2.5 + Calc.random(1.5), 2.5 + Calc.random(1.5), -8, -45 + Calc.random(270), 9, 2, 5);
		}
		
		if (y + vspeed >= 368)
			y = 368;
	}

}
