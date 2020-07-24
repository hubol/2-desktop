package world.enemy;

import graphics.Sprite;
import main.Calc;
import world.control.Global;
import world.control.Sound;
import world.player.Player;
import main.Scene;

public class Flower extends BasicEnemy {
	public boolean dead;
	public double intense, worth, scale = 1.25, rotate = Calc.random(360), add = Calc.rangedRandom(4);
	public int markings = (Global.roomX + Global.roomY) % 3, stem = (int)Calc.random(2), face = 2, ang= -1, ysc = (Integer)Calc.choose(1, - 1);

	public Flower(double x, double y) {
		super(x, y);
		orientation=Sprite.CENTERED;
		dead=false;
		
		sprite=Sprite.get("sFlower");
		mask=Sprite.get("sUrchinMask").mask;
		
		worth = 6.1;
		
		intense=0;
		imageSingle = Calc.random(2);
		imageSpeed=Calc.random(.4);
		
		angle = Calc.rangedRandom(15);
		
		if (Scene.collision(this, x - 32, y, Global.BLOCK, Global.UPBLOCK))
			ang = 0;
		else if (Scene.collision(this, x, y +32, Global.BLOCK, Global.UPBLOCK))
			ang = 90;
		else if (Scene.collision(this, x + 32, y, Global.BLOCK, Global.UPBLOCK))
			ang = 180;
		else if (Scene.collision(this, x, y - 32, Global.BLOCK, Global.UPBLOCK))
			ang = 270;
	}
	
	public void step(){
		rotate += add*1.2;
		
		//adjust how u look based on distance to player
		double d = Calc.pointDistance(x, y, Player.me.x, Player.me.y);
		if (d < 108){
			face = 0;
			scale = Calc.approach(scale, 1, 2);
		}
		else if (d < 180){
			face = 1;
			scale = Calc.approach(scale, 1.125, 2);
		}
		else{
			face = 2;
			scale = Calc.approach(scale, 1.25, 2);
		}
		
		if (dead){
			face = 0;
			if (getSpeed()==0){
				d = Calc.random(360);
				if (ang != -1)
					d = ang + Calc.rangedRandom(15);
				setDirSpeed(d, 2 + Calc.random(1));
			}
			setSpeed(getSpeed()*1.01);
			intense+=.02;
			intense*=1.4;
			if (intense>=1.5){
				Global.dropRandomLoot(x, y - 16, worth);
				Sound.explodePlay();
				super.destroy();
			}
		}
		
		if (Scene.collision(this, x, y, Global.PLAYER)){
			//if (Player.hurtTime <= 0){
			//	Player.me.jump();
			//	Player.me.hspeed *= -1;
			//}
			//speed = speed - 2*normalize(position-urchinPosition)*sign(abs(dotProduct(position-urchinPosition,speed)));
			//V3D playerPos = new V3D(Player.me.x, Player.me.y,0);
			//V3D playerSpd = new V3D(Player.me.hspeed, Player.me.vspeed,0);
			
			Player.hurtPlayer(40);
		}
		
		super.step();
	}
	
	public void bombDamage(){
		killMe();
	}
	
	public void gunDamage(){
		if (!dead){
		worth *= .7;
		
		for (int i=0; i<4; i++)
			Global.squareParticle(x + Calc.rangedRandom(16), y + Calc.rangedRandom(16), 3 + (int)Calc.random(5), "FF1E56", 1.5 + Calc.random(5));
		}
		killMe();
	}
	
	public void killMe(){
		if (!dead){
			dead=true;
			Sound.playPitched("sUrchinKill");
		}
	}
	
	public void render(){
		if (ang != -1)
			Sprite.get("sFlowerRoot").render(stem, Sprite.EAST, x+Calc.rangedRandom(1),y+Calc.rangedRandom(1), 1, ysc, ang, 1, "ffffff");
		if (!dead){
			if (face == 2){
				sprite.render(6, orientation, x-1+Calc.random(2), y-1+Calc.random(2), scale, scale, angle + Calc.rangedRandom(1) + rotate, 1, Global.roomColor);
				sprite.render(3 + markings, orientation, x-1+Calc.random(2), y-1+Calc.random(2), scale, scale, angle + Calc.rangedRandom(1) + rotate, 1, 1, 1, 1);
			}
			sprite.render((int)face, orientation, x-1+Calc.random(2), y-1+Calc.random(2), scale, scale, angle + Calc.rangedRandom(1) + rotate, 1, 1, 1, 1);
			sprite.render(7, orientation, x-1+Calc.random(2), y-1+Calc.random(2), scale, scale, angle + Calc.rangedRandom(1) + rotate, 1, 1, 1, 1);
		}
		else
			Sprite.get("sFlowerDie").render((int)Calc.random(5), orientation, x+((-8+Calc.random(16))*intense), y+((-8+Calc.random(16))*intense), 1.2*Math.max(1, intense), 1.2*Math.max(1, intense), (-8+Calc.random(16))*intense, 1, 1, 1, 1);
	}

}
