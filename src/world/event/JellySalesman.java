package world.event;

import main.Calc;
import graphics.Sprite;
import world.boss.jelly.JellyBubble;
import world.boss.jelly.JellyBubbleSmall;
import world.boss.jelly.JellyEnemy;
import world.boss.jelly.JellyRender;
import world.control.Global;
import world.control.Sound;
import world.control.SpriteLoader;

public class JellySalesman extends JellyEnemy{
	public double counter;
	public int hurt, img;
	public static JellySalesman me;

	public JellySalesman(double x, double y) {
		super(x, y);
		
		new SpriteLoader("sJellySalesmanHurtBack_12", "sJellySalesman_6", "sJellySalesmanBack_6", "sJellySalesmanHurt_12");
		
		sprite = Sprite.get("sJellySalesman");
		backSprite = Sprite.get("sJellySalesmanBack");
		
		img = -1;
		
		me = this;
		
		setCollisionGroup(Global.DEACTIVATEME, Global.BASICENEMY);
		
		mask = sprite.mask;
		hurt = 0;
		
		counter = 0;
		imageSingle = 0;
		imageSpeed = 0;
		imageSpeed = 5.2369 / 45.0;
		
		if (Global.gotMapDoor){
			visible = false;
			destroy();
		}
	}
	
	public void destroy(){
		me = null;
		super.destroy();
	}
	
	public void roomDestroy(){
		destroy();
	}
	
	public void step(){
		sprite = Sprite.get("sJellySalesman");
		backSprite = Sprite.get("sJellySalesmanBack");
		imageSpeed = 5.2369 / 25.0;
		
		hurt -= 1;
		if (hurt > 0){
			sprite = Sprite.get("sJellySalesmanHurt");
			backSprite = Sprite.get("sJellySalesmanHurtBack");
			imageSpeed = (hurt * .67) / 30.0;
			visible = true;
			if (Calc.random(1) < .2)
				visible = false;
			new JellyBubble(x + Calc.rangedRandom(8), y - 4 - Calc.random(20), Math.max(4, hurt));
		}
		else
			visible = true;
		
		JellyRender.me.intensity = Calc.approach(JellyRender.me.intensity, 1.0, 12);
		
		counter += 1 + Math.max(0, hurt);
		double div = 30.0;
		y = ystart + (5 * Math.sin(counter / div));
		
		/*if (y > prevY && imageSingle > 0)
			imageSingle -= 5.2369 / 120.0;
		if (y < prevY && imageSingle < 3.9)
			imageSingle += 5.2369 / 120.0;*/
		
		if (counter % 5 == 0)
			new JellyBubble(x + Calc.rangedRandom(8), y - 4 - Calc.random(20),4);
		
		super.step();
		
		if (img != -1){
			sprite = Sprite.get("sJellySalesmanHurt");
			backSprite = Sprite.get("sJellySalesmanHurtBack");
			if (img == 0)
				imageSingle = (Integer)Calc.choose(0, 6);
			else if (img == 1)
				imageSingle = (Integer)Calc.choose(1, 5, 7, 11);
			else if (img == 2)
				imageSingle = (Integer)Calc.choose(2, 4, 8, 10);
			else if (img == 3)
				imageSingle = (Integer)Calc.choose(3, 9);
		}
	}
	
	public void gunDamage(){
		hurt();
	}
	
	public void bombDamage(){
		hurt();
	}
	
	public void hurt(){
		Sound.popPlay();
		if (hurt <= 0){
			JellyRender.me.intensity = 32.0;
			hurt = 30;
			Sound.playPitched("sJellySalesmanHitCry",.08);

			for (int i=0; i<16; i++){
				new JellyBubbleSmall(x + Calc.rangedRandom(8), y - 4 - Calc.random(20), 12);
				new JellyBubble(x + Calc.rangedRandom(8), y - 4 - Calc.random(20), 10);
				new JellyBubble(x + Calc.rangedRandom(8), y - 4 - Calc.random(20), 6);
			}
			
			new SmallMessage(this, 0, -34, (String)Calc.choose("wow!", "okay then!", "eat shit!", "cool!", "aah!"), 16);
		}
	}
	
	public void render(){
		
	}

}
