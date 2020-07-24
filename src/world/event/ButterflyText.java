package world.event;

import graphics.Graphics;
import graphics.Text;
import world.control.Global;
import world.control.Shake;
import main.Calc;
import main.Entity;

public class ButterflyText extends Entity{
	public String txt;
	public Shake s;

	public ButterflyText(double x, double y) {
		super(x, y);
		int a = Global.butterfliesCaught();
		if (a % 10 == 0)
			Global.addTweet(a + " BUTTERFLIES "+(String)Calc.choose("FOUND", "CAUGHT", "CAPTURED")+(String)Calc.choose(".","!","","","!!"));
		
		txt = a+"!";
		vspeed = -8;
		
		imageSpeed = 0;
		
		alarmInitialize(1);
		alarm[0] = 60;
		
		s = new Shake(.3);
		
		setCollisionGroup(Global.DEACTIVATEME);
		setDepth(Integer.MIN_VALUE + 18);
	}
	
	public void alarmEvent(int i){
		destroy();
	}
	
	public void step(){
		super.step();
		vspeed *= .8;
	}
	
	public void destroy(){
		s.destroy();
		super.destroy();
	}
	
	public void render(){
		Graphics.setColor("0095CF");
		Graphics.setAlpha(1);
		Text.orientation = Text.CENTERED;
		Text.setFont(Global.FONT);
		Text.drawTextExt(x + s.x + Calc.rangedRandom(.3) + 8, y + s.y + Calc.rangedRandom(.3), txt, .5, .5, 0);
		Text.drawTextExt(x + s.x + Calc.rangedRandom(.3) + 8, y + s.y + Calc.rangedRandom(.3) + 480, txt, .5, .5, 0);
		Text.drawTextExt(x + s.x + Calc.rangedRandom(.3) + 8, y + s.y + Calc.rangedRandom(.3) - 480, txt, .5, .5, 0);
	}

}
