package world.junk;

import graphics.Sprite;
import world.control.Global;
import world.control.Shake;
import world.control.SpriteLoader;
import main.Calc;
import main.Entity;

public class PlantEye extends Entity{
	public double fSpd, fimg, bimg;
	public int wait;
	
	public Shake s = new Shake(.12);

	public PlantEye(double x, double y) {
		super(x, y);
		setDepth(69);
		setCollisionGroup(Global.DEACTIVATEME);
		
		new SpriteLoader("sPlantEye_6", "sPlantEyeBack_3");
		
		fSpd = 1.0/3.0;
		wait = -1;
		
		int j = 30 + (int)Calc.random(200);
		for (int i=0; i<j; i++)
			step();
	}
	
	public void step(){
		int ni = (int)fimg;
		
		if (wait <= 0){
			fimg += fSpd;
			if (fimg >= 6){
				fimg = 5.99;
				wait = 30;
			}
			else if (fimg <= 0){
				fimg = 0;
				wait = 30;
			}
		}
		else{
			wait -= 1;
			if (wait <= 0){
				wait = -1;
				fSpd *= -1;
			}
		}
		
		if ((int)fimg != ni)
			bimg += 1;
		while (bimg >= 3)
			bimg -= 3;
	}
	
	public void render(){
		Sprite.get("sPlantEyeBack").render(bimg, Sprite.NORTHWEST, x + Calc.rangedRandom(.02) + s.x - 5, y + Calc.rangedRandom(.02) + s.y - 6, .97, .98, 0, 1, 1, 1, 1);
		Sprite.get("sPlantEye").render(fimg, Sprite.NORTHWEST, x + Calc.rangedRandom(.02) + s.x, y + Calc.rangedRandom(.02) + s.y, 1, 1, 0, 1, 1, 1, 1);
	}

}
