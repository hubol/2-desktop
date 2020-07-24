package world.boss.papa;

import audio.Audio;

public class PapaMusic{
	private final String main = "musBoss07", layer = "musBoss07Layer";
	
	public PapaMusic(){
		Audio.get(main).setLooping(true);
		Audio.get(layer).setLooping(true);
		Audio.get(main).setGain(1);
		Audio.get(layer).setGain(0);
		Audio.get(main).setPitch(1);
		Audio.get(layer).setPitch(1);
		
		Audio.fade(main, 1, 1);
		Audio.fade(layer, 0, 1);
		
		Audio.get(main).play();
		Audio.get(layer).play();
	}
	
	public void main(){
		Audio.fade(main, 1, .05);
		Audio.fade(layer, 0, .05);
	}
	
	public void expose(){
		Audio.fade(layer, 1, .05);
		Audio.fade(main, 0, .05);
	}

}
