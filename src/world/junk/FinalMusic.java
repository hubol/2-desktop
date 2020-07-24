package world.junk;

import world.player.Player;
import audio.Audio;
import main.Calc;
import main.Entity;

public class FinalMusic extends Entity{
	public FinalMusic(double x, double y) {
		super(x, y);
		visible = false;
	}
	
	public void step(){
		if (Audio.soundExists("musGoodNews")){
			double toVol = 1 - Math.min(1, Math.max(.01, (Player.me.x / 480.0) * 1.1));
			double toPitch = Math.max(.05, 1 - (Player.me.x / 470.0));
			Audio.fade("musGoodNews", toVol, .1);
			Audio.get("musGoodNews").setPitch(Calc.approach(Audio.get("musGoodNews").getPitch(), toPitch, 16));
		}
	}
	
	public void render(){
		
	}

}
