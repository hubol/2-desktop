package world.intro;

import graphics.Sprite;
import main.Calc;
import main.Entity;

public class DassBlock extends Entity{
	public double rand;
	public DassBlockMake mom;
	public char myChar;
	public int ch;
	public boolean vis;

	public DassBlock(double x, double y, DassBlockMake mom, char c, boolean v) {
		super(x, y);
		rand = 8;
		this.mom = mom;
		
		vis = v;
		
		myChar = c;
		ch = -1;
		if (c != ' ')
			ch = mom.myFont.fetchSymbolId(c);
	}
	
	public void step(){
		x = xstart + Calc.rangedRandom(rand);
		y = ystart + Calc.rangedRandom(rand);
		rand = Calc.approach(rand, 0, 8);
	}
	
	public void render(){
		Sprite.get("sIntroDassBlock").render(0, Sprite.NORTHWEST, x + mom.addX, y + mom.addY, .25, .25, 0, 1, Intro.me.LINE);
		if (ch != -1 && vis)
			Sprite.get("font").render(ch, Sprite.CENTERED, x + mom.addX + 18, y + mom.addY + 18, .8, .8, 0, 1, Intro.me.BACK);
	}

}
