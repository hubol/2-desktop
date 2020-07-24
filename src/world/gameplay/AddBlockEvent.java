package world.gameplay;

import java.util.ArrayList;

import world.Fg;
import world.Root;
import world.control.Global;
import world.control.Sound;
import world.control.SoundLoader;
import graphics.Sprite;
import main.Calc;
import main.Entity;
import main.Scene;

public class AddBlockEvent extends Entity{
	public Sprite me;
	public String showSound, hideSound;
	public boolean show = false;
	private boolean shake = false;
	
	public int event, value;

	public AddBlockEvent(double x, double y, int event, int value, String showSound, String hideSound) {
		super(x, y);
		show = Global.event[event] == value;
		
		this.event = event;
		this.value = value;
		
		this.showSound = showSound;
		this.hideSound = hideSound;
		if (ok(showSound) && !ok(hideSound))
			new SoundLoader(showSound);
		else if (!ok(showSound) && ok(hideSound))
			new SoundLoader(hideSound);
		else if (ok(showSound) && ok(hideSound))
			new SoundLoader(showSound, hideSound);
		
		Root.loadBackground("add"+Global.roomX+","+Global.roomY+".png",(int)Math.ceil(Calc.random(6)));
		me = Sprite.get("add"+Global.roomX+","+Global.roomY);
		
		change(show);
	}
	
	public void change(boolean s){
		ArrayList<AddBlock> list = Scene.getEntityList(AddBlock.class);
		if (s){
			for (int i=0; i<list.size(); i++)
				list.get(i).active();
		}
		else{
			for (int i=0; i<list.size(); i++)
				list.get(i).inactive();
		}
	}
	
	public boolean ok(String s){
		return !s.equals("sNoSound");
	}
	
	public void step(){
		if (Global.event[event] == value != show){
			show = (Global.event[event] == value);
			
			if(show && ok(showSound))
				Sound.playPitched(showSound, .02);
			else if(!show && ok(hideSound))
				Sound.playPitched(hideSound, .02);
			
			change(show);
			
			if (shake)
				Fg.me.shakeTimer = 10;
		}
		
		shake = true;
	}
	
	public void draw(){
		if (show)
			Fg.me.fgRender(me, 1);
	}

}
