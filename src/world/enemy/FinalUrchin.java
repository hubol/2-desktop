package world.enemy;

import graphics.Sprite;
import world.control.Global;
import world.control.Shake;
import world.control.SpriteLoader;
import world.player.Player;
import main.Calc;
import main.Entity;

public class FinalUrchin extends Entity{
	private int repeat = 2;
	private double angle, speed;
	private boolean drawStart = true;
	
	private Shake[] s;
	
	public FinalUrchin(double x, double y, int repeat, int yes, double angle, double speed){
		this(x, y, repeat, angle, speed);
		if (yes <= 0)
			drawStart = false;
	}

	public FinalUrchin(double x, double y, int repeat, double angle, double speed) {
		super(x, y);
		this.repeat = repeat;
		this.angle = angle;
		this.speed = speed;
		
		s = new Shake[repeat];
		for (int i=0; i<repeat; i++)
			s[i] = new Shake(.3);
		
		setDepth(Integer.MIN_VALUE+14);
		setCollisionGroup(Global.DEACTIVATEME, Global.DEACTIVATEENEMY);
		if (!Sprite.exists("sFinalUrchin"))
			new SpriteLoader("sFinalUrchin_2");
		sprite = Sprite.get("sFinalUrchin");
		imageSingle = (int)Math.round(((x + y) % 69.0) / 69.0);
	}
	
	public void step(){
		angle += speed;
		for (int i=0; i<repeat; i++){
			if (Calc.pointDistance(x + Calc.dirX(i * 32, angle), y + Calc.dirY(i * 32, angle), Player.me.x, Player.me.y) < 30)
				Player.hurtPlayer(300);
		}
	}
	
	public void render(){
		int img = (int)imageSingle;
		for (int i=0; i<repeat; i++){
			if (!(!drawStart && i == 0))
				sprite.render(img, Sprite.CENTERED, x + Calc.dirX(i * 32, angle) + s[i].x, y + Calc.dirY(i * 32, angle) + s[i].y, 1, 1, 0, 1, 1, 1, 1);
			if (img == 0)
				img = 1;
			else
				img = 0;
		}
	}

}
