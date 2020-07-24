package world.boss.slick;

import graphics.Graphics;
import graphics.Text;
import world.control.Global;
import world.control.Shake;
import world.control.Sound;
import main.Entity;

public class SlickIndicator extends Entity{
	public String color, text;
	public Shake s;

	public SlickIndicator(double x, double y, String text, String color, int time) {
		super(x, y);
		Sound.playPitched("sSlickIndicate", .1);
		alarmInitialize(1);
		alarm[0] = time;
		
		s = new Shake(.4);
		
		this.text = text;
		this.color = color;
		
		setDepth(6);
		
		vspeed = -8;
	}
	
	public void alarmEvent(int i){
		destroy();
	}
	
	public void step(){
		if (alarm[0] < 10)
			visible = !visible;
		
		super.step();
		vspeed *= .5;
	}
	
	public void destroy(){
		s.destroy();
		super.destroy();
	}
	
	public void render(){
		Text.idiot = false;
		Text.orientation = Text.CENTERED;
		Text.setFont(Global.FONT);
		Graphics.setAlpha(1);
		Graphics.setColor("4784FF");
		Text.drawTextExt(x + s.x + 4, y + s.y, text, 1, 1, 0);
		Text.drawTextExt(x + s.x - 4, y + s.y, text, 1, 1, 0);
		Text.drawTextExt(x + s.x, y + s.y + 4, text, 1, 1, 0);
		Text.drawTextExt(x + s.x, y + s.y - 4, text, 1, 1, 0);
		Graphics.setColor(color);
		Text.drawTextExt(x + s.x, y + s.y, text, 1, 1, 0);
		Text.idiot = true;
	}

}
