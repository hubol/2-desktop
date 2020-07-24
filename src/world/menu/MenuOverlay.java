package world.menu;

import graphics.BlendMode;
import graphics.Sprite;
import world.control.Global;
import main.Calc;
import main.Entity;

public class MenuOverlay extends Entity{

	public MenuOverlay() {
		super(0, 0);
		setDepth(-10000);
	}
	
	public void step(){
		
	}
	
	public void render(){
		BlendMode.SUBTRACT.set();
		Sprite.get("overlay"+Global.menuOverlay).render(0,Sprite.NORTHWEST, 0, 0, 1+Calc.random(.001), 1+Calc.random(.001), 0, .22, 1, 1, 1);
		BlendMode.NORMAL.set();
	}

}
