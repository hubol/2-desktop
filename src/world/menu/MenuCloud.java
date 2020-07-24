package world.menu;

import world.control.Global;
import world.control.Shake;
import graphics.Sprite;
import main.Calc;
import main.Entity;

public class MenuCloud extends Entity{
	public Shake s;

	public MenuCloud(double x, double y, double hsp, int img, double xsc, double ysc) {
		super(x, y);
		
		hspeed = hsp;
		imageSingle = img;
		orientation = Sprite.CENTERED;
		
		xscale = xsc;
		yscale = ysc;
		
		s = new Shake(1.5);
		
		setDepth(400);
	}
	
	public void step(){
		x += hspeed;
		while (x < 0)
			x += 960;
		while (x > 960)
			x -= 960;
	}
	
	public void render(){
		double x = this.x + s.x + Calc.rangedRandom(.25), y = this.y + s.y + Calc.rangedRandom(.25);
		draw(x, y);
		draw(x + 960, y);
		draw(x - 960, y);
	}
	
	public void draw(double x, double y){
		Sprite.get("sMenuCloudsBack").render(imageSingle, orientation, x, y, xscale, yscale, 0, 1, Global.menuLineColor);
		Sprite.get("sMenuCloudsLine").render(imageSingle, orientation, x, y, xscale, yscale, 0, 1, Global.menuBackgroundColor);
	}

}
