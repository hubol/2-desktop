package world.event;

import audio.Audio;
import graphics.Sprite;
import world.Fg;
import world.control.EV;
import world.control.Global;
import world.control.Sound;
import world.gameplay.Block;
import world.player.Player;
import main.Calc;
import main.Entity;
import main.Scene;

public class RainWeirdBlock extends Entity{
	public Block mine;

	public RainWeirdBlock(double x, double y) {
		super(x, y);
		adjust();
		visible = false;
		
		orientation = Sprite.CENTERED;
		sprite = Global.sBLOCK;
		mask = sprite.mask;
		
		setCollisionGroup(Global.SOLIDBLOCK, Global.DEACTIVATEME);
		
		if (Global.event[EV.RAINWEIRD] == 0)
			mine = new Block(96, 352);
	}
	
	public void destroy(){
		super.destroy();
	}
	
	public void roomDestroy(){
		destroy();
	}
	
	public void adjust(){
		if (Global.event[EV.RAINWEIRD] == 0){
			Fg.me.secretAlpha = 1;
			Fg.touchSecret = false;
		}
		else{
			Fg.me.secretAlpha = 0;
			Fg.touchSecret = true;
		}
	}
	
	public void step(){
		if (Player.me.vspeed <= 0 && Scene.collision(this, x, y + 1, Global.PLAYER) && Fg.me.shakeTimer <= 0){
			Fg.me.shakeTimer = 8;
			Sound.playPitched("sUnderSecret", .04);
			if (Global.event[EV.RAINWEIRD] == 1){
				Global.event[EV.RAINWEIRD] = 0;
				mine = new Block(96, 352);
			}
			else{
				Global.event[EV.RAINWEIRD] = 1;
				mine.destroy();
			}
		}
		
		adjust();
		
		if (Audio.soundExists("musCute")){
			Audio.get("musCute").setPitch(Calc.approach(Audio.get("musCute").getPitch(), .05, 120));
		}
	}
	
	public void render(){
		
	}

}
