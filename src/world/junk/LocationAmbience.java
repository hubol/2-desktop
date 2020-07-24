package world.junk;

import audio.Audio;
import world.control.Global;
import world.control.Sound;
import world.control.SoundLoader;
import world.player.Player;
import main.Entity;

public class LocationAmbience extends Entity{
	public SoundLoader loader;
	public String sound;
	public double width, height;
	public boolean done = false;

	public LocationAmbience(double x, double y, String sound, double width, double height) {
		super(x, y);
		this.width = width;
		this.height = height;
		this.sound = sound;
		
		setCollisionGroup(Global.DEACTIVATEME);
		
		if (!Global.room(29, 0))
			loader = new SoundLoader(sound);
		else
			loader = new SoundLoader(false, "musAnalBeads", "sEndTune", "sEndDrum", "sEndBrass", "sEndGrow0", "sEndGrow1", "sEndBroken", "sEndProgress", "sEndHorn", "sEndAdvance");
	}
	
	public void step(){
		if (loader.loaded){
			if (!done){
				done = true;
				Audio.get(sound).setLooping(true);
				Audio.get(sound).setGain(0.0);
				Audio.fade(sound, 0.0, .1);
				Sound.play(sound);
			}
			else{
				if (inBounds(Player.me.x - 15, Player.me.y - 16) && inBounds(Player.me.x + 15, Player.me.y + 16))
					Audio.fade(sound, 1.0, .01);
				else
					Audio.fade(sound, 0, .005);
			}
		}
	}
	
	public boolean inBounds(double x, double y){
		return (x >= this.x && y >= this.y && x <= this.x + width && y <= this.y + height);
	}
	
	public void render(){
		if (!stepActive && done)
			Audio.fade(sound, 0, 1.0);
	}

}
