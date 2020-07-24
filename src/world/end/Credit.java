package world.end;

import world.control.Shake;
import graphics.Graphics;
import graphics.Text;
import main.Entity;

public class Credit extends Entity{
	private String text;
	private Credits mom;
	private Shake s = new Shake(.4);

	public Credit(String cred, Credits mother, double spd) {
		super(16, 512);
		text = cred;
		mom = mother;
		vspeed = spd;
	}
	
	public void step(){
		y += vspeed;
		if (y < -32)
			destroy();
	}
	
	public void render(){
		final double xx = .5, yy = .65;
		Text.idiot = false;
		Text.setFont(mom.font);
		Text.orientation = Text.NORTHWEST;
		Graphics.setAlpha(1);
		Graphics.setColor("0081CF");
		Text.drawTextExt(x + s.x + 4, y + s.y, text, xx, yy, 0);
		Text.drawTextExt(x + s.x - 4, y + s.y, text, xx, yy, 0);
		Text.drawTextExt(x + s.x, y + s.y + 4, text, xx, yy, 0);
		Text.drawTextExt(x + s.x, y + s.y - 4, text, xx, yy, 0);
		Graphics.setColor(1, 1, 1);
		Text.drawTextExt(x + s.x, y + s.y, text, xx, yy, 0);
		Text.idiot = true;
	}

}
