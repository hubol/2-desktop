package world.intro;

import graphics.Font;
import graphics.Graphics;
import graphics.Shape;
import graphics.Text;

import java.awt.event.KeyEvent;

import world.Root;
import world.control.Global;
import world.control.NewGen;
import world.control.Shake;
import world.control.Sound;
import world.control.SoundLoader;
import main.Entity;
import main.Scene;

public class IntroTutorial extends Entity{
	private final String[] title = new String[]{" ARROW KEYS", " JUMP KEY", " PAUSE KEY", " INTERACT KEY", " POWER KEY"};
	private final String[] desc = new String[]{"USED FOR MENU NAVIGATION%AND MOVEMENT", "USED FOR JUMPING", "USED TO SHOW AND HIDE%THE PAUSE MENU", "USED FOR MENU CONFIRMATION%AND WEAPON SWITCHING", "USED FOR WEAPONS"};
	
	private final int[] key = new int[]{KeyEvent.VK_RIGHT, KeyEvent.VK_UP, KeyEvent.VK_LEFT, KeyEvent.VK_DOWN, KeyEvent.VK_UP, KeyEvent.VK_SPACE, KeyEvent.VK_X, KeyEvent.VK_Z};
	private final String[] name = new String[]{"RIGHT", "UP", "LEFT", "DOWN", "UP", "SPACE", "X", "Z"};
	
	private final int units = 30;
	private final int[] w = new int[]{5, 5, 5, 5, 5, 8, 2, 2};
	
	public final Font font = Global.FONT;
	private int at = 0;
	private boolean done = false;
	
	private Shake[] s = new Shake[12];
	
	public String BACK, LINE;
	
	public IntroTutorial() {
		super(0, 0);
		new SoundLoader(false, "sIntro0", "sIntro1", "sIntro2", "sIntro3", "sIntro4", "sIntro5", "sIntro6", "sIntro7", "sEndAdvance");
		for (int i=0; i<s.length; i++)
			s[i] = new Shake(.3);
		
		alarmInitialize(1);
		
		BACK = "#FFFFFF";
		LINE = "#FFFFFF";
		double a = 0;
		NewGen myGen = new NewGen(3.53719,2.14,6.375,3.29,.12,(double)a * (double)a * 6.7297);
		
		String dark = "#0094CF";//(String)myGen.choose("#007CFF", "#FF2339", "#FF2714", "#30BA7C", "#2BC9BA", "#A55BFF", "#FF70FF");
		
		if (myGen.random(1) > .5)
			LINE = dark;
		else
			BACK = dark;
		
		Global.menuBackgroundColor = LINE;
		Global.menuLineColor = BACK;
	}
	
	public void key(double x, double y, int i){
		new IntroKey(x, y, key[i], name[i], w[i] * units, i, this);
	}
	
	public void alarmEvent(int i){
		Root.changeRoom("d");
	}
	
	public void step(){
		if (!Scene.instanceExists(IntroKey.class) && !done){
			Sound.playPitched("sEndAdvance");
			
			if (at == 0){
				key(320 + 96, 240 + 60, 0);
				key(320, 200 + 60, 1);
				key(320 - 96, 240 + 60, 2);
				key(320, 280 + 60, 3);
			}
			else if (at < 5){
				key(320, 240 + 60, 3 + at);
			}
			else{
				done = true;
				alarm[0] = 60;
			}
			at++;
		}
		
		alarmStep();
	}
	
	public void render(){
		Text.idiot = false;
		Text.orientation = Text.CENTERED;
		Text.setFont(font);
		
		Graphics.setAlpha(1);
		Graphics.setColor(BACK);
		Shape.drawRectangle(0, 0, 640, 480);
		
		if (!done){
			Graphics.setColor(LINE);
			Text.drawTextExt(320 + s[0].x, 80 + s[0].y, "WELCOME!", 2, 2, 0);
			Shape.drawRectangle(0,160 + s[1].y, 640, 200 + s[1].y);
			Graphics.setColor(BACK);
			Text.drawTextExt(320 + s[2].x, 180 + s[2].y, "PLEASE PRESS THE "+title[at - 1]+"!", .6d, .65, 0);
			
			Graphics.setColor(LINE);
			Shape.drawRectangle(0, 380 + s[1].y, 640, 460 + s[1].y);
			Graphics.setColor(BACK);
			String t = desc[at - 1];
			String[] z = t.split("%");
			if (z.length == 1)
				Text.drawTextExt(320 + s[2].x, 420 + s[2].y, desc[at - 1], .65, .65, 0);
			else{
				Text.drawTextExt(320 + s[2].x, 420 + s[2].y - 16, z[0], .65, .65, 0);
				Text.drawTextExt(320 + s[2].x, 420 + s[2].y + 16, z[1], .65, .65, 0);
			}
		}
		else{
			Graphics.setColor(LINE);
			Text.drawTextExt(320 + s[0].x, 240 + s[0].y, "I AM SO PROUD OF YOU!", .8, .8, 0);
		}
			
		Text.idiot = true;
	}


}
