package world.control;

import world.Root;
import graphics.Graphics;
import graphics.Shape;
import audio.Audio;
import main.Entity;
import main.Scene;

public class SoundLoader extends Entity{
	public boolean loaded = false;
	public String[] mySounds;
	public boolean[] loadedSounds, playOnceLoaded;
	public boolean killYourself = false;
	public boolean initialized, cease;
	public boolean threaded = true;
	public SoundLoader reference = this;
	
	public Thread t = null;
	
	public void join() throws InterruptedException{
		if (t != null)
			t.join();
	}
	
	/**call this bad boy to have the sounds loaded in a separate thread!!! dangerous if run on a slow-ass hdd!!!!*/
	public SoundLoader(String... sounds) {
		super(0, 0);
		init(sounds);
	}
	
	/**safest method for loading sounds!!!! no separate thread!!!! however, serious wait times may occur!!! pros and cons man!!!*/
	public SoundLoader(boolean thread, String... sounds) {
		super(0, 0);
		threaded = thread;
		init(sounds);
	}
	
	public SoundLoader(double x, double y){
		super(x, y);
	}
	
	public void init(String... sounds){
		killYourself = false;
		
		cease = false;
		
		visible = Global.debug;
		initialized = true;
		
		setDepth(Integer.MIN_VALUE);
		
		mySounds = sounds;
		loadedSounds = new boolean[mySounds.length];
		playOnceLoaded = new boolean[mySounds.length];
		for (int i=0; i<loadedSounds.length; i++){
			loadedSounds[i] = false;
			playOnceLoaded[i] = false;
		}
		
		loaded = false;
		
		if (((Music.loaded && !Music.firstStep)||Root.room.equals("heyhey")) && Global.soundLoadClaimed == null)
			load();
		else
			initialized = false;
	}
	
	public void step(){
		kill();
		
		if (initialized){
			if (!killYourself){ //precautionary measure
				for (int i=0; i<mySounds.length; i++){
					if (loadedSounds[i] && playOnceLoaded[i]){
						Sound.play(mySounds[i]);
						playOnceLoaded[i] = false;
					}
				}
			}
		}
		else if (Music.loaded && !Music.firstStep && Global.soundLoadClaimed == null)
			load();
	}
	
	public void load(){
		Global.soundLoadClaimed = this;
		initialized = true;
		
		if (threaded){ //load in a separate thread!!!!
			t = new Thread(){@Override
				public void run(){
				if (!cease){
					for (int i=0; i<mySounds.length; i++){
						if (!cease){
							if (Scene.console)
								System.out.println("temp sound loaded: "+mySounds[i]);
							Audio.load("flyAudio", mySounds[i] + ".ogg");
						}
						loadedSounds[i] = true;
					}
				}
				loaded = true;
				
				if (Global.soundLoadClaimed == reference) //wtf lame
					Global.soundLoadClaimed = null;
			}
			};
			
			t.setDaemon(true);
			t.start();
		}
		else{ //load in same thread!!! lagg!!!!
			for (int i=0; i<mySounds.length; i++){
				if (Scene.console)
					System.out.println("temp sound loaded: "+mySounds[i]);
				Audio.load("flyAudio", mySounds[i] + ".ogg");
				loadedSounds[i] = true;
			}
			loaded = true;
			if (Global.soundLoadClaimed == this)
				Global.soundLoadClaimed = null;
		}
	}
	
	public void kill(){
		if (initialized){
			if (killYourself && loaded){
				for (int i=0; i<mySounds.length; i++){
					if (Audio.soundExists(mySounds[i])){
						if (Scene.console)
							System.out.println("temp sound unloaded: "+mySounds[i]);
						Audio.delete(mySounds[i]);
					}
					loadedSounds[i] = false;
				}
				
				loaded = false;
				super.destroy();
			}
		}
		else if (killYourself){
			loaded = false;
			super.destroy();
		}
	}
	
	public void destroy(){
		if (Global.soundLoadClaimed == this)
			Global.soundLoadClaimed = null;
		
		cease = true;
		
		killYourself = true;
		kill();
	}
	
	public void roomDestroy(){
		destroy();
	}
	
	public void playOnceLoaded(String s){
		for (int i=0; i<mySounds.length; i++){
			if (mySounds[i].equals(s)){
				playOnceLoaded[i] = true;
				return;
			}
		}
	}
	
	public void render(){
		if (!loaded){
			double percent = 0;
			for (int i=0; i<mySounds.length; i++){
				if (loadedSounds[i])
					percent += (1.0 / (double)mySounds.length);
			}
			
			Graphics.setAlpha(1);
			Graphics.setColor("43C49E");
			Shape.drawRectangle(0, 0, 16, 480);
			Graphics.setColor("FE005A");
			Shape.drawRectangle(0, 0, 16, 480.0 * percent);
		}
	}

}
