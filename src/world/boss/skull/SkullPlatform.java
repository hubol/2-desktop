package world.boss.skull;

import graphics.Sprite;
import world.control.Global;
import world.gameplay.Upblock;
import world.player.Player;
import main.Calc;
import main.Entity;
import main.Scene;

public class SkullPlatform extends Entity{
	public double a, b;
	public double platX, platY;
	
	public Upblock myblock;
	public SmallFlower[] flowers;
	
	public int amount;
	public int spawned;
	public double draw;
	
	public boolean active;
	
	public double withdraw = 5 + Calc.random(2.2);

	public SkullPlatform(double x, double y, double a, double b) {
		super(x, y);
		this.a = a;
		this.b = b;
		
		active = true;
		spawned = 0;
		
		setDepth(-3);
		
		draw = 0;
		amount = (int)(Calc.pointDistance(x, y, a, b) / 32);
		flowers = new SmallFlower[amount];
		
		for (int i=0; i<amount; i++)
			flowers[i] = null;
		
		refreshPlatform();
		myblock = new Upblock(platX, platY);
	}
	
	public void destroy(){
		myblock.destroy();
		super.destroy();
	}
	
	public void step(){
		if (active){
			draw = Calc.approach(draw, (double)amount, 20);
			draw += .2;
			
			while (Math.floor(draw) > spawned){
				double dir = Calc.pointDirection(x, y, a, b);
				double dist = (Calc.pointDistance(x, y, a, b) / ((double)amount/* - 1.0*/)) * draw;
				flowers[spawned] = new SmallFlower(x + Calc.dirX(dist, dir), y + Calc.dirY(dist, dir), false);
				spawned += 1;
			}
			
			if (draw > amount)
				draw = amount;
		}
		else{
			draw = Calc.approach(draw, 0, withdraw);
			draw -= .4;
			
			while (Math.floor(draw) < spawned && spawned > 0){
				flowers[spawned - 1].kill();
				spawned -= 1;
			}
			
			if (draw <= 0)
				destroy();
		}
		
		boolean attached = false;
		if (Player.me.x >= x && Player.me.x <= a && Player.me.vspeed >= 0){
			if (Scene.collision(myblock, platX, platY - 1, Global.PLAYER) && Player.me.y > platY - 17 && Player.me.y < platY - 15)
				attached = true;
		}
		if (Player.me.vspeed >= 0)
			refreshPlatform();
		myblock.x = platX; myblock.y = platY;
		if (attached){
			Player.me.y = platY - 16;
			Player.me.vspeed = 0;
		}
	}
	
	public void kill(){
		if (active){
			active = false;
		}
	}
	
	public void render(){
		int fuck = 12;
		int amt = (int)Math.ceil(draw) * fuck;
		double scale = 0;
		double dir = Calc.pointDirection(x, y, a, b), xx = 0, yy = 0, dist = (Calc.pointDistance(x, y, a, b) * (draw / ((double)amount * fuck))) / draw, xsc = (draw / (double)amount) / draw;
		for (int i=0; i<amt; i++){
			if (i < (double)amt / 2.0)
				scale = Calc.approach(scale, 1.0, 4);
			else
				scale = Calc.approach(scale, 0, 2);
			Sprite.get("sSkullStem").render(0, Sprite.WEST, x + xx, y + yy, xsc, scale, dir, 1, 1, 1, 1);
			xx += Calc.dirX(dist, dir);
			yy += Calc.dirY(dist, dir);
		}
	}
	
	public void refreshPlatform(){
		double playerX = Calc.keepInBounds(Player.me.x, x, a);
		platX = playerX - 16;
		platY = (((b - y) / (a - x)) * playerX) + (((a * y)-(x * b))/(a - x));
	}

}
