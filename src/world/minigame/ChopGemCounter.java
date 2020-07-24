package world.minigame;

import world.control.Global;
import graphics.Graphics;
import graphics.Text;
import main.Calc;
import main.Entity;

public class ChopGemCounter extends Entity{
	public ChopPlayer mother;
	public int last;

	public ChopGemCounter(double x, double y, ChopPlayer mom) {
		super(x, y);
		mother = mom;
		last = 0;
		
		setDepth(Integer.MIN_VALUE+8);
	}
	
	public void step(){
		
	}
	
	public void render(){
		Text.setFont(Global.CRAZDFONT);
		Text.orientation = Text.SOUTHEAST;
		
		final double ax = 40, ay = 48, xsc = .4, ysc = .3;
		
		Text.randomize(.4);
		if (last != mother.earned)
			Text.randomize(5);
		
		Graphics.setColor("006187");
		Text.drawTextExt(x + Calc.rangedRandom(.5) + 2 + ax, y + Calc.rangedRandom(.5) + 2 + ay, ""+mother.earned, xsc, ysc, 0);
		Graphics.setColor("DF87FF");
		Text.drawTextExt(x + Calc.rangedRandom(.5) + ax, y + Calc.rangedRandom(.5) + ay, ""+mother.earned, xsc, ysc, 0);
		
		Text.randomize(0);
		
		last = mother.earned;
	}

}
