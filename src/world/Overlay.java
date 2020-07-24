package world;

import java.text.DecimalFormat;
import java.util.ArrayList;

import world.control.Global;
import world.control.Music;
import world.control.Pause;
import world.player.Player;
import world.water.WaterControl;
import gameMain.GameMain;
import graphics.BlendMode;
import graphics.Graphics;
import graphics.Shape;
import graphics.Sprite;
import graphics.Text;
import main.Calc;
import main.Entity;
import main.Input;
import main.Scene;

public class Overlay extends Entity {
	private long lastTime;
	public static double r,g,b,a;
	public static double timer;
	
	public int triangles=0;
	public static Overlay me;
	
	public boolean fadeIn;
	public double ang, xx, yy, xsc, ysc, aniTimer, alp;
	
	public boolean fade = false;
	
	public Overlay(double x, double y) {
		super(x, y);
		setDepth(Integer.MIN_VALUE);
		sprite=Sprite.get("sOver");
		persistent=true;
		
		me = this;
		
		r=1;
		g=1;
		b=1;
		a=0;
		
		fadeIn = false;
		alp = 0;
		
		timer=30;
		
		resetTimer();
		ang = 0;
		xx = 0;
		yy = 0;
	}
	
	public void resetTimer(){
		aniTimer = 5 + Calc.random(6);
		xsc = (Double)Calc.choose(1.0,-1.0);
		ysc = (Double)Calc.choose(1.0,-1.0);
	}
	
	/**
	 * make a rectangle of the color fade out for the given amount of time
	 * @param red
	 * @param green
	 * @param blue
	 * @param alp alpha to start at
	 * @param time time to fade out
	 */
	public static void fadeOut(double red, double green, double blue, double alp, double time){
		r=red;
		g=green;
		b=blue;
		a=alp;
		timer=time;
	}
	
	public void step(){
		if (a>0)
			a-=1.0/timer;
		if (a<0)
			a=0;
		
		triangles = Scene.trianglesDrawn;
		
		//we're doing this bro we're drawing the sleepy overlay
		aniTimer -= 1;
		if (aniTimer <= 0)
			resetTimer();
		
		ang += 4;
		if (ang >= 360)
			ang -= 360;
		
		if (fadeIn)
			alp += .2;
		else
			alp -= .3;
		alp = Math.max(0, Math.min(1, alp));
		
		fadeIn = false;
		
		if (Scene.getEntityList(Player.class).size() > 0){
			xx = Calc.approach(xx, Player.me.x + Calc.rangedRandom(1) + Calc.dirX(12, ang), 1.2);
			yy = Calc.approach(yy, Player.me.y + Calc.rangedRandom(1) + Calc.dirY(12, ang), 1.2);
		}
		
		if (fade && alpha > 0)
			alpha -= 1.0 / 350.0;
	}
	
	public void destroy(){
		me = null;
		super.destroy();
	}
	
	public String imgName(){
		String s = "overlay"+Calc.getGridPosition(Global.roomX, Global.roomY, 16);
		if (Global.heroMode)
			s = "overlay"+(128 + Calc.getGridPosition(Global.roomX, Global.roomY, 16));
		if (Global.dream)
			s = "z"+Global.currentDream;
		else if (Global.room(16,1))
			s = "whatisthis";
		
		return s;
	}
	
	public void delete(){
		String s = imgName();
		if (Sprite.exists(s))
			Sprite.get(s).remove();
	}
	
	public void render(){
		if (Global.mainActive && !GameMain.SCREENSHOT){
			if (Fg.me.ovVis && !Pause.me.paused){
				BlendMode.SUBTRACT.set();
				String s = imgName();
				
				if (Sprite.exists(s))
					Sprite.get(s).render(0,Sprite.NORTHWEST, 0, 0, 1+Calc.random(.001), 1+Calc.random(.001), 0, .22 * alpha, 1, 1, 1);
	
				BlendMode.NORMAL.set();
			}
			Graphics.setColor(r,g,b);
			Graphics.setAlpha(a);
			Shape.drawRectangle(0,0,640,480);
			Graphics.setAlpha(1);
			BlendMode.SUBTRACT.set();
			sprite.render(0,Sprite.CENTERED,319+Calc.random(2),239+Calc.random(2),1.02+Calc.random(01),1.02+Calc.random(01),0,.03 * alpha,1,1,1);
			BlendMode.NORMAL.set();
		}
		
		long time = System.currentTimeMillis();
		
		if (time != lastTime && Global.debug){
			Graphics.setColor(0,0,0);
			Graphics.setAlpha(.4);
			Shape.drawRectangle(0, 0, 640, 103);
			
			Graphics.setColor(1,1,1);
			Graphics.setAlpha(1);
			Text.setFont(Global.FONT);
			Text.orientation = Text.NORTHWEST;
			
			ArrayList<WaterControl> list = Scene.getEntityList(WaterControl.class);
			String str = "%[NO WATER]";
			if (list.size() > 0){
				str = "%WATER IMG: ["+(int)list.get(0).image+"]";
			}
			
			String p = "[NO PLAYER]";
			if (Player.me != null)
				p = "PLAYER: ["+Player.me.x+", "+Player.me.y+"]"+str;
			
			int add = 0;
			if (Global.heroMode)
				add += 128;
			
			Text.drawTextExt(4,4,("FPS: ["+(int)(1000/(time-lastTime))+"]%ROOM: ["+(Calc.formatNumberString(""+Global.roomX, 2))+", "+(Calc.formatNumberString(""+Global.roomY, 2))+", "+(Calc.getGridPosition(Global.roomX, Global.roomY, 16) + add)+"]%["+Scene.getInstances().length+"] ENTITIES"+"%["+Scene.entityChecks+"] ENTITY LIST CHECKS%["+triangles+"] TRIANGLES DRAWN PREVIOUS STEP%DREAM: ["+Global.currentDream+"]%MUSIC: ["+Music.music+"], PREV: ["+Music.musicPrev+"], KEPT: ["+Music.musicKept+"]%LOADED: ["+Music.loaded+"], UNLOADED: ["+Music.unloaded).toUpperCase()+"]%TIME PLAYED: ["+time(Global.framesPlayed)+"], TIME PAUSED: ["+time(Global.framesPaused)+"], TOTAL: ["+time(Global.framesPlayed + Global.framesPaused)+"]%MOUSE: ["+Input.mouseX +", "+Input.mouseY+"]%"+p,.25,.25,0,"21CBFF");
			
			Graphics.setColor("21CBFF");
			Shape.drawLine(Input.mouseX + 2, Input.mouseY + 1, Input.mouseX + 6, Input.mouseY + 1, 1);
			Shape.drawLine(Input.mouseX, Input.mouseY + 1, Input.mouseX - 4, Input.mouseY + 1, 1);
			Shape.drawLine(Input.mouseX + 1, Input.mouseY + 2, Input.mouseX + 1, Input.mouseY + 6, 1);
			Shape.drawLine(Input.mouseX + 1, Input.mouseY, Input.mouseX + 1, Input.mouseY - 4, 1);
			Graphics.setColor(1,1,1);
			Shape.drawLine(Input.mouseX + 1, Input.mouseY, Input.mouseX + 5, Input.mouseY, 1);
			Shape.drawLine(Input.mouseX - 1, Input.mouseY, Input.mouseX - 5, Input.mouseY, 1);
			Shape.drawLine(Input.mouseX, Input.mouseY + 1, Input.mouseX, Input.mouseY + 5, 1);
			Shape.drawLine(Input.mouseX, Input.mouseY - 1, Input.mouseX, Input.mouseY - 5, 1);
		}
		lastTime = time;
		
		/*double fuck[] = {16, 24, 36, 69, 42, 35};
		Shape.drawPieChart(320, 240, 120, 100, 0, fuck, "0094CF", "FF5C54", "FFF849", "66FFBF", "A582FF", "FF6DA5");*/
	}
	
	public String time(long t){
		DecimalFormat f = new DecimalFormat();
		f.setMaximumIntegerDigits(0);
		f.setMinimumFractionDigits(2);
		f.setMaximumFractionDigits(2);
		
		return Calc.formatTime((int)(t / 30)) + "" + f.format((double)((t % 30.0) / 30.0));
	}
	
}
