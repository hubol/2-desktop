package world.gameplay;

import graphics.Sprite;
import world.control.Global;
import world.control.Sound;
import world.control.SoundLoader;
import world.control.SpriteLoader;
import world.event.NoOccupy;

public class CrackedBlock extends BombRock{
	public Sprite me;
	public int myEvent;
	public String sound;

	public CrackedBlock(double x, double y, int event, String sound) {
		super(x, y);
		
		myEvent = event;
		
		if (Global.event[event] == 0){
			sprite = Global.sBLOCK;
			mask = sprite.mask;
			orientation = Sprite.NORTHWEST;
			setCollisionGroup(Global.SOLIDBLOCK, Global.TEMPSOLID);
			visible = false;
			
			this.sound = sound;
			if (!sound.equals("sNoSound"))
				new SoundLoader(sound);
			new SpriteLoader("backgrounds",true,".png","cracked"+Global.roomX+","+Global.roomY);
			me = Sprite.get("cracked"+Global.roomX+","+Global.roomY);
		}
		else
			destroy();
	}
	
	public void step(){
		//no
	}
	
	public void shatter(){
		Global.event[myEvent] = 1;
		
		if (!sound.equals("sNoSound"))
			Sound.play(sound);
		Sound.jarBreakPlay();
		Sound.playPitched("sRockBreak");
		new NoOccupy(x, y);
		
		super.destroy();
	}
	
	public void draw(double x, double y){
		me.render(0, orientation, x, y, 1, 1, 0, 1, 1, 1, 1);
	}
	
	public void render(){
		//no
	}

}
