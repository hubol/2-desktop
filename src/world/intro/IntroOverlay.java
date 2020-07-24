package world.intro;

import graphics.BlendMode;
import graphics.Sprite;
import world.control.Global;
import world.control.SpriteLoader;
import main.Calc;
import main.Entity;

public class IntroOverlay extends Entity{
	public int a;

	public IntroOverlay(int a) {
		super(0, 0);
		setDepth(-69420);
		
		this.a = a;
		Global.menuOverlay = a;
		
		new SpriteLoader("overlays",false,".jpg","overlay"+a);
	}
	
	public void step(){
		
	}
	
	public void render(){
		BlendMode.SUBTRACT.set();
		Sprite.get("overlay"+a).render(0,Sprite.NORTHWEST, 0, 0, 1+Calc.random(.001), 1+Calc.random(.001), 0, .22, 1, 1, 1);
		BlendMode.NORMAL.set();
	}

}
