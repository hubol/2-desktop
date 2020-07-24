package world.event;

import graphics.Sprite;
import world.control.Global;
import world.control.Shake;
import world.control.SpriteLoader;
import main.Calc;
import main.Entity;

public class FieldWaterfall extends Entity{
	public Shake s;

	public FieldWaterfall(double x, double y) {
		super(x, y);
		new SpriteLoader("sFieldRWaterfall_4", "sFieldLWaterfall_4", "sFieldCWaterfall_4");
		sprite = Sprite.get("sFieldRWaterfall");
		orientation = Sprite.NORTH;
		imageSpeed = 4.6 / 30.0;
		
		s = new Shake(.3);
		
		setDepth(Integer.MAX_VALUE-21);
		setCollisionGroup(Global.DEACTIVATEME);
	}
	
	public void step(){
		super.step();
	}
	
	public void render(){
		Sprite.get("sFieldLWaterfall").render(imageSingle, Sprite.NORTHEAST, x + s.x + Calc.rangedRandom(.3), y + s.y + Calc.rangedRandom(.3), 1, 1, 0, 1, 1, 1, 1);
		Sprite.get("sFieldRWaterfall").render((imageSingle + 2) % 4, Sprite.NORTHWEST, x + s.x + Calc.rangedRandom(.3), y + s.y + Calc.rangedRandom(.3), 1, 1, 0, 1, 1, 1, 1);
		Sprite.get("sFieldCWaterfall").render(imageSingle, Sprite.NORTH, x + s.x + Calc.rangedRandom(.3), y + s.y + Calc.rangedRandom(.3) - 4, 1, 1, 0, 1, 1, 1, 1);
		Sprite.get("sFieldCWaterfall").render((imageSingle + 2) % 4, Sprite.NORTH, x + s.y + Calc.rangedRandom(.3), y + s.x + Calc.rangedRandom(.3), -1, 1, 0, .2, 1, 1, 1);
	}

}
