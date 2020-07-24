package world.menu;

import java.awt.event.KeyEvent;

import graphics.Graphics;
import graphics.Shape;
import graphics.Text;

import audio.Audio;

import world.control.Global;
import world.control.IO;
import world.control.SetControls;
import world.control.Shake;
import world.control.Sound;
import main.Calc;
import main.Entity;
import main.Input;
import main.Main;
import main.Scene;

public class MenuControls extends Entity{
	public Menu mom;
	public boolean escape;
	public SetControls controls;
	public final String[] names = new String[]{"LEFT", "RIGHT", "UP", "DOWN", "JUMP", "PAUSE", "POWER", "INTERACT"}, adtnl = new String[]{"DEFAULT: LEFT ARROW", "DEFAULT: RIGHT ARROW", "DEFAULT: UP ARROW", "DEFAULT: DOWN ARROW", "DEFAULT: UP ARROW%%NOTE: CAN BE SAME AS UP KEY", "DEFAULT: SPACEBAR", "DEFAULT: Z", "DEFAULT: X"};
	public final Double[] xx = new Double[]{64.0,160.0,112.0,112.0,112.0,288.0,448.0,448.0}, yy = new Double[]{256.0,256.0,224.0,288.0,160.0,256.0,224.0,288.0};
	
	public final Double[] scale = new Double[]{246.94, 277.18, 311.13, 329.63, 369.99, 415.3, 466.16, 493.88};
	
	public boolean testing = false;
	public boolean[] tested = new boolean[]{false,false,false,false,false,false,false,false};
	
	public double approachTo, approachSpeed, atX;
	
	public boolean active = false, await = false;
	public int at = -1;
	public int done = 0;
	
	public boolean textVisible = true;
	
	public Shake s;
	public int error = 0;
	
	public static MenuControls me;

	public MenuControls(Menu mommy) {
		super(0, 0);
		mom = mommy;
		
		setDepth(-20);
		
		me = this;
		
		controls = new SetControls();
		Main.canvas.addKeyListener(controls);
		
		s = new Shake();
		
		atX = -320;
		approachTo = 960;
		approachSpeed = 20;
		
		escape = Scene.killOnEscape;
		Scene.killOnEscape = false;
		
		alarmInitialize(2);
		alarm[0] = 20;
	}
	
	public void alarmEvent(int i){
		if (i == 0)
			active = true;
		else if (i == 1){
			active = true;
			textVisible = true;
			await = false;
		}
	}
	
	public void clear(int i){
		if (!tested[i]){
			Audio.get("sMenuTest").setPitch(scale[done] / 246.94);
			Sound.play("sMenuTest");
			done += 1;
			tested[i] = true;
		}
	}
	
	public void step(){
		approachTo = Math.max(0, approachTo - 96);
		approachSpeed = Math.max(2, approachSpeed - .5);
		if (approachTo <= 128)
			approachSpeed -= .8;
		atX = Calc.approach(atX, approachTo, approachSpeed);
		
		if (Input.checkKey(KeyEvent.VK_ESCAPE))
			cancel();
		else if (active){
			if (!await && !Global.awaitKey){
				Sound.playPitched("sMenuControlsDisplay", .06);
				if (at < 7){
					await = true;
					at += 1;
					Global.which = at;
					Global.awaitKey = true;
				}
				else{
					testing = true;
					active = false;
				}
			}
			else if (await && !Global.awaitKey){
				active = false;
				textVisible = false;
				alarm[1] = 5;
			}
		}
		
		if (testing){
			if (IO.checkFrameKey(Global.LEFT))
				clear(0);
			if (IO.checkFrameKey(Global.RIGHT))
				clear(1);
			if (IO.checkFrameKey(Global.UP))
				clear(2);
			if (IO.checkFrameKey(Global.DOWN))
				clear(3);
			if (IO.checkFrameKey(Global.JUMP))
				clear(4);
			if (IO.checkFrameKey(Global.PAUSE))
				clear(5);
			if (IO.checkFrameKey(Global.ACTION))
				clear(6);
			if (IO.checkFrameKey(Global.INTERACT))
				clear(7);
			
			if (tested())
				complete();
		}
		
		alarmStep();
		
		if (error > 0)
			error -= 1;
	}
	
	public boolean tested(){
		for (int i=0; i<8; i++){
			if (!tested[i])
				return false;
		}
		return true;
	}
	
	/*if (which==0)
	LEFT = k;
	else if (which==1)
		RIGHT = k;
	else if (which==2)
		UP = k;
	else if (which==3)
		DOWN = k;
	else if (which==4)
		JUMP = k;
	else if (which==5)
		PAUSE = k;
	else if (which==6)
		ACTION = k;
	else if (which==7)
		INTERACT = k;
	
	awaitKey = false;
	*/
	
	public void cancel(){
		Global.readKeys();
		mom.error = 6;
		Sound.playPitched("sDishError", .05);
		returnToMommy();
	}
	
	public void complete(){
		//Sound.playPitched("sMenuControlComplete", .06);
		Global.writeKeys();
		//mom.confirmSound();
		returnToMommy();
	}
	
	public void returnToMommy(){
		Scene.killOnEscape = escape;
		Main.canvas.removeKeyListener(controls);
		Global.awaitKey = false;
		Global.which = 0;
		mom.active = true;
		mom.alarm[2] = 30;
		me = null;
		destroy();
	}
	
	public void render(){
		double z = 0;
		if (error > 0)
			z = -9 + (18 * (error % 2));
		
		Text.idiot = false;
		Graphics.setColor(Global.menuLineColor);
		Shape.drawRectangle(atX + 48 + s.x + z, 48 + s.y, atX + (640 - 48) + s.x + z, 480 - 48 + s.y);
		Graphics.setColor(Global.menuBackgroundColor);
		
		Text.setFont(Global.FONT);
		Text.orientation = Text.CENTERED;
		
		String heading = "", note = "";
		String below = "PRESS ESCAPE TO CANCEL!!!";
		
		if (active){
			if (await){
				heading = "PRESS "+names[at]+" KEY";
				note = adtnl[at];
			}
		}
		else if (testing){
			heading = "TEST YOUR KEY CONFIGURATION";
			for (int i=0; i<8; i++){
				if (!tested[i]){
					final double aY = -32;
					double w = (names[i].length() * 8.0) + 4.0;
					Graphics.setColor(Global.menuBackgroundColor);
					Shape.drawRectangle(xx[i] + s.x - w + atX + 48 + z, yy[i] + s.y - 12 + 48 + aY, xx[i] + s.x + w + atX + 48 + z, yy[i] + s.y + 10 + 48 + aY);
					Graphics.setColor(Global.menuLineColor);
					Text.drawTextExt(xx[i] + s.x + atX + 48 + z, yy[i] + s.y + 48 + aY, names[i], .5, .5, 0);
				}
			}
		}
		
		Graphics.setColor(Global.menuBackgroundColor);
		Shape.drawRectangle(atX + 48 + s.x + z, 96 + s.y - 16, atX + (640 - 48) + s.x + z, 96 + s.y + 16);
		Graphics.setColor(Global.menuLineColor);
		Text.drawTextExt(320 + s.x + atX + z, 98 + s.y, heading, .5, .5, 0);
		Graphics.setColor(Global.menuBackgroundColor);
		Text.drawTextExt(320 + s.x + atX + z, 112 + 48 + s.y, note, .5, .5, 0);
		Text.drawTextExt(320 + s.x + atX + z, 352 + 48 + s.y, below, .5, .5, 0);
		Text.idiot = true;
	}

}
