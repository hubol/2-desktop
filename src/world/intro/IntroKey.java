package world.intro;

import graphics.Graphics;
import graphics.Shape;
import graphics.Text;
import world.control.Shake;
import world.control.Sound;
import main.Entity;
import main.Input;

public class IntroKey extends Entity{
	private int key;
	private String title;
	private int width, snd;
	private final Shake[] s = new Shake[]{new Shake(.3), new Shake(.3)};
	private final IntroTutorial mom;

	public IntroKey(double x, double y, int key, String title, int width, int snd, IntroTutorial mom) {
		super(x, y);
		this.key = key;
		this.title = title;
		this.width = width;
		this.snd = snd;
		this.mom = mom;
	}
	
	public void step(){
		if (Input.checkFrameKey(key)){
			destroy();
			Sound.playPitched("sIntro"+snd);
		}
	}
	
	public void render(){
		Graphics.setAlpha(1);
		Graphics.setColor(mom.LINE);
		Shape.drawRectangle(x - (width / 2) + s[0].x, y - 17 + s[0].y, x + (width / 2) + s[0].x, y + 17 + s[0].y);
		Graphics.setColor(mom.BACK);
		Text.idiot = false;
		Text.orientation = Text.CENTERED;
		Text.setFont(mom.font);
		Text.drawTextExt(x + s[1].x, y + s[1].y, title, .64, .64, 0);
		Text.idiot = true;
	}

}
