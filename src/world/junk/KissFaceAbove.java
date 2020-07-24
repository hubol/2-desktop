package world.junk;

import java.util.ArrayList;

import main.Calc;
import main.Scene;
import audio.Audio;
import graphics.Sprite;
import world.control.EV;
import world.control.Global;
import world.control.Shake;
import world.control.Sound;
import world.enemy.BasicEnemy;

public class KissFaceAbove extends BasicEnemy{
	public boolean done;
	
	public double faceImg = 0, droolImg = 0;
	
	public boolean prep = false, play = false;
	public String sound = "sAddRight";
	
	public Shake s = new Shake(.5);
	public boolean a = false;
	
	public boolean can = !(Global.room(0, 4));
	
	public int event;

	public KissFaceAbove(double x, double y, String s) {
		this(x, y, EV.ABOVEADD, s);
	}
	
	public KissFaceAbove(double x, double y, int ev, String s){
		super(x, y);
		event = ev;
		done = met();
		
		if (!done)
			faceImg = 2;
		
		droolImg = (int)Calc.random(2);
		
		sound = s;
		
		mask = Global.sBLOCK.mask;
		setDepth(20);
		alarmInitialize(1);
		alarm[0] = 18 + (int)Calc.random(14);
	}
	
	public boolean met(){
		return Global.event[event] == 1;
	}
	
	public void alarmEvent(int i){
		if (!done)
			Sound.playPitched("sKissDrool", .2);
		
		if (droolImg == 0)
			droolImg = 1;
		else
			droolImg = 0;
		alarm[0] = 18 + (int)Calc.random(14);
	}
	
	public void step(){
		a = !a;
		
		if (!prep && Audio.soundExists(sound)){
			Audio.get(sound).setGain(0);
			Audio.fade(sound, 1, .03);
			Audio.get(sound).setLooping(true);
			prep = true;
		}
		
		if (done)
			faceImg -= .35;
		else
			faceImg += .35;
		
		faceImg = Math.max(0, Math.min(faceImg, 2));
		
		if ((int)faceImg == 1)
			droolImg = 0;
		
		if (prep && done && !play && (int)faceImg == 0){
			play = true;
			Sound.play(sound);
		}
		
		alarmStep();
		
		if (Global.room(0, 4)){
			if (Scene.collision(this, x, y, Global.STUPIDORB)){
				ArrayList<StupidOrb> list = Scene.getEntityList(StupidOrb.class);
				list.get(0).destroy();
				can = true;
				gunDamage();
			}
		}
	}
	
	public void gunDamage(){
		if (can){
			Sound.playPitched("sKissCongrats", .02);
			
			if (Global.event[event] == 1)
				Global.event[event] = 0;
			else
				Global.event[event] = 1;
			
			ArrayList<KissFaceAbove> list = Scene.getEntityList(KissFaceAbove.class);
			for (int i=0; i<list.size(); i++){
				KissFaceAbove me = list.get(i);
				if (me.play){
					me.play = false;
					Audio.get(me.sound).stop();
				}
				
				me.done = me.met();
			}
			
			if (Global.room(0, 4))
				can = false;
		}
	}
	
	public void render(){
		final double scale = 32.0 / 48.0;
		final double dX = 19 - 16, dY = 24 - 16;
		double addX = 0;
		
		if ((int)faceImg == 0){
			addX = -1;
			if (a)
				addX = 1;
		}
		
		Sprite.get("sKissFace").render(faceImg, Sprite.CENTERED, x + s.x + Calc.rangedRandom(.2) + addX, y + s.y + Calc.rangedRandom(.2), scale, scale, 0, 1, 1, 1, 1);
		if ((int)faceImg > 0)
			Sprite.get("sKissDrool").render(droolImg, Sprite.NORTH, x + s.x + dX + Calc.rangedRandom(.1), y + s.y + dY + Calc.rangedRandom(.1), scale *.5, scale *.5, 0, 1, 1, 1, 1);
	}

}
