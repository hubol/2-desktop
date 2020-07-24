package world.water;

import graphics.Sprite;

public class WaterWave extends WaterMom{

	public WaterWave(double x, double y, String s, String o, double ang) {
		super(x, y);
		orientation = Sprite.SOUTHWEST;
		
		sprite = Sprite.get(s);
		outline = Sprite.get(o);
		
		angle = (int)ang;
		
		if (angle == 90 || angle == 180)
			this.x += 32;
		if (angle == 0 || angle == 90)
			this.y += 32;
	}
	
	public void renderBack(double x, double y, double img){
		double xx = 0, yy = 0;
		if (angle == 0)
			xx = -3;
		else if (angle == 90)
			yy = 3;
		else if (angle == 180)
			xx = 3;
		else if (angle == 270)
			yy = -3;
		super.renderBack(x + xx, y + yy, img);
	}

}
