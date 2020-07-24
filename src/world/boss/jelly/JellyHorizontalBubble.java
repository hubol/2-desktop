package world.boss.jelly;

import world.control.Global;
import world.control.Sound;
import world.player.Player;
import main.Calc;
import graphics.Sprite;

public class JellyHorizontalBubble extends JellyEnemy{
	public double addAng;
	public boolean hurt;
	public int health = 2;

	public JellyHorizontalBubble(double x, double y, double size, double speed) {
		super(x, 416 - (64*size));
		
		setDepth(-4);
		
		angle = Calc.random(360);
		addAng = Calc.rangedRandom(5);
		
		orientation = Sprite.CENTERED;

		sprite = Sprite.get("sJellyBlob");
		backSprite = Sprite.get("sJellyBlobBack");
		
		setCollisionGroup(Global.BASICENEMY, Global.DEACTIVATEME);
		
		xscale = size;
		yscale = size;
		
		hspeed = speed;
		
		hurt = true;
	}
	
	public void gunDamage(){
		health--;
		
		for (int i=0; i<2; i++){
			new JellyBubbleSmall(x + Calc.rangedRandom(4), y + Calc.rangedRandom(4),6);
			new JellyBubbleSmall(x + Calc.rangedRandom(4), y + Calc.rangedRandom(4),5);
			new JellyBubbleSmall(x + Calc.rangedRandom(4), y + Calc.rangedRandom(4),4);
		}
		
		if (health == 1){
			Sound.playPitched("sJellyWarp");
			y += (96 * xscale) - (128 * xscale);
			xscale *= .75;
			yscale = xscale;
		}
		else{
			Sound.playPitched("sJellyLandPop");
			destroy();
		}
	}
	
	public void step(){
		angle += addAng;

		if (angle > 360)
			angle -= 360;
		if (angle < 0)
			angle += 360;
		
		super.step();
		
		if (!hurt){
			double ax = xscale;
			xscale -= .025;
			
			if (xscale > .45)
				xscale = Calc.approach(xscale, 0, 10);
			xscale = Calc.approach(xscale, 0, 8);
			
			if (x < 320)
				x -= (ax - xscale + .005) * 64;
			else
				x += (ax - xscale + .005) * 64;
			
			if (xscale <= 0)
				destroy();
		}
		
		if ((x >= 576 - (64*xscale)||x <= 64 + (64*xscale)) && hurt){
			hurt = false;
			
			Sound.explodePlay();
			Sound.explodePlay();
			
			Global.jellyExplosionEffect(x, y, 1.1);
			Global.jellyExplosionEffect(x, y, 1.3);
			
			if (Calc.pointDistance(Player.me.x,Player.me.y,x,y) < 96)
				Player.hurtPlayer(50);

			hspeed = 0;
		}
		
		if (hurt && Calc.pointDistance(x,y,Player.me.x,Player.me.y) < (64 * xscale) + 16)
				Player.hurtPlayer(40);
	}
	
	public void render(){
		
	}
	
	public void renderBack(){
		double i = imageSingle;
		imageSingle = 0;
		super.renderBack();
		x += 3;
		super.renderBack();
		x -= 6;
		super.renderBack();
		x+= 3;
		y+= 3;
		super.renderBack();
		y-=6;
		super.renderBack();
		y+=4;
		x-=2; //-2,-2
		super.renderBack();
		x+=4; //2, -2
		super.renderBack();
		y+=4; //2, 2
		super.renderBack();
		x-=4;//-2,2
		super.renderBack();
		x+=2;
		y-=2;
		imageSingle = i;
	}
	
	public void renderFront(){
		super.renderFront();
		Sprite.get("sJellyBlobSheen").render(0, orientation, x, y, xscale, yscale, 0, alpha, 1, 1, 1);
	}

}
