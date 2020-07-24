package world.junk;

import world.control.Global;
import world.control.Hud;
import world.control.Sound;
import world.control.SpriteLoader;
import world.end.EndControl;
import world.gameplay.Heart;
import world.particles.HeartParticles;
import world.player.Player;
import graphics.Sprite;
import main.Calc;

public class FalseHeart extends Heart{
	public int me = 71;
	public double ang, img, ax;
	
	public double sparkleTimer;

	public FalseHeart(double x, double y) {
		super(x, y);
		
		new SpriteLoader("sShadowHeart_8");
		setCollisionGroup(Global.HEART);
		addCollisionGroup(Global.ITEM, Global.DEACTIVATEME);
		setDepth( -2 );
		orientation = Sprite.CENTERED;
		sprite = Sprite.get("sShadowHeart");
		mask = sprite.mask;
		ax = 0;
		
		resetSparkle();
	}
	
	public void resetSparkle(){
		sparkleTimer = 4 + Calc.random(8);
		//new Sparkle(x + Calc.rangedRandom(12), y + Calc.rangedRandom(12));
	}
	
	public void step(){
		img += 4.0 / 30.0;
		if (img >= 2)
			img -= 2;
		ang += 1;
		
		ax += 1;
		y = ystart + (4 * Math.sin(ax/15));
		
		sparkleTimer -= 1;
		if (sparkleTimer <= 0)
			resetSparkle();
		
		if (Calc.pointDistance(x, y, Player.me.x, Player.me.y) < 28)
			collect();
	}
	
	public void collect(){
			Player.control = false;
			Player.me.cutMove = false;
			Player.me.cutRight = false;
			Player.me.cutToX = Player.me.x + 1;
			new EndControl();
			
			Hud.showHealth();
			Sound.heartPlay();

			for (int i=0; i<16; i++){
				new HeartParticles(x,y);
			}
			Global.squareParticle(x, y, 16, "#ff2879", 4);
			destroy();
			Global.refreshIconMap();
	}
	
	public void refreshIcon(){
		if (!isDestroyed())
			Global.setIconMap(Global.roomX, Global.roomY, 4);
	}
	
	public void shabip(double xx, double yy){
		for (int i=0; i<4; i++)
			Sprite.get("sItemGlow").render((int)Calc.loopBounds(img + i, 0, 2), Sprite.WEST, x + Calc.rangedRandom(Calc.random(1)) + xx, y + Calc.rangedRandom(Calc.random(1)) + yy, .25, .8, ang + (i * 90), 1, "#ff2879");
	}
	
	public void drawGlow(){
		//if (stepActive){
			shabip(0, 0);
			if (Player.me.vLoop && !Global.room(18, 0)){
				shabip(0, 480);
				shabip(0, -480);
			}
			if (Player.me.hLoop){
				shabip(640, 0);
				shabip(-640, 0);
			}
		//}
	}
	
	public void bippity(int i, double xx, double yy){
		sprite.render(i, orientation, x + Calc.rangedRandom(Calc.random(1)) + xx, y + Calc.rangedRandom(Calc.random(1)) + yy, 1, 1, 0, 1, "ffffff");
	}
	
	public void render(){
		int i = (int)imageSingle;
		if (i == 0)
			i = (Integer)Calc.choose(0,2,4,6);
		else
			i = (Integer)Calc.choose(1,3,5,7);
		
		bippity(i, 0, 0);
		if (Player.me.vLoop && !Global.room(18, 0)){
			bippity(i, 0, 480);
			bippity(i, 0, -480);
		}
		if (Player.me.hLoop){
			bippity(i, 640, 0);
			bippity(i, -640, 0);
		}
	}

}
