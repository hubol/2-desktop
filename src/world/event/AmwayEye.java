package world.event;

import graphics.Sprite;
import world.player.Player;
import main.Calc;
import main.Entity;

public class AmwayEye extends Entity{

	public AmwayEye(double x, double y) {
		super(x, y);
		resetPos();
	}
	
	public void render(){
		Sprite.get("sAmwayEye").render(0, orientation, x, y, xscale, yscale, angle, alpha, colR, colG, colB);
	}
	
	public void resetPos(){
		double dir = Calc.pointDirection(x, y, Player.me.x, Player.me.y);
		x = xstart + Calc.dirX(18, dir) + Calc.rangedRandom(1.5);
		y = ystart + Calc.dirY(13, dir) + Calc.rangedRandom(1.5);
	}
	
	public void step(){
		resetPos();
	}

}
