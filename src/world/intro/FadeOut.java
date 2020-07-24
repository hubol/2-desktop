package world.intro;

import graphics.Graphics;
import graphics.Shape;
import main.Entity;

public class FadeOut extends Entity{
	public String color;

	public FadeOut(String color) {
		super(0, 0);
		this.color = color;
		alpha = 0;
		setDepth(-50);
	}
	
	public void step(){
		alpha += 1.0 / 60.0;
	}
	
	public void render(){
		Graphics.setAlpha(alpha);
		Graphics.setColor(color);
		Shape.drawRectangle(0,0,640,480);
		Graphics.setAlpha(1);
	}

}
