package world.control;

import main.Input;

public class IO {
	public static boolean playback = false;
	
	public static boolean checkKey(int keyCode){
		if (!playback)
			return Input.checkKey(keyCode);
		return Performer.me.check(keyCode);
	}
	
	public static boolean checkFrameKey(int keyCode){
		if (!playback)
			return Input.checkFrameKey(keyCode);
		return Performer.me.press(keyCode);
	}

}
