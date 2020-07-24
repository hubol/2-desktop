package world.event;

import world.control.Global;
import graphics.Sprite;
import main.Entity;
import main.Scene;

public class NewRain extends Entity{
	public Sprite brite;
	public int amount;

	public NewRain(double x, double y) {
		super(x, y);
		sprite = Sprite.get("sRainSplash");
		mask = sprite.mask;
		brite = Sprite.get("sRainFall");
		orientation = Sprite.NORTHWEST;
		setCollisionGroup(Global.DEACTIVATEME);
		setDepth(-15);
		
		imageSpeed = .3;
	}
	
	public void refresh(){
		amount = 0;
		
		double atY = 0;
		boolean go = true;
		if (!Scene.collision(this, x, y, Global.SOLIDBLOCK, Global.UPBLOCK)){
			while (go){
				if (Scene.collision(this, x, y + atY + 32, Global.SOLIDBLOCK, Global.UPBLOCK))
					go = false;
				
				amount += 1;
				atY += 32;
				
				if (y + atY > 512)
					go = false;
			}
		}
	}
	
	public void step(){
		super.step();
	}
	
	public void render(){
		double atY = 0;
		for (int i=0; i<amount; i++){
			if (i < amount - 1)
				brite.render(imageSingle, orientation, x + Global.rain.x, y + Global.rain.y + atY, 1, 1, 0, 1, 1, 1, 1);
			else
				sprite.render(imageSingle, orientation, x + Global.rain.x, y + Global.rain.y + atY, 1, 1, 0, 1, 1, 1, 1);
			
			atY += 32;
		}
	}

}
