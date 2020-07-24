package world.junk;

import world.Fg;
import world.control.EV;
import world.control.Global;
import world.control.Sound;
import world.control.SoundLoader;
import world.control.SpriteLoader;
import world.enemy.Mouse;
import world.gameplay.Spring;
import main.Entity;
import main.Scene;

public class KissSpring extends Entity{
	public boolean kill = false;

	public KissSpring(double x, double y) {
		super(x, y);
		new SpriteLoader("sStupidSpring");
		
		if (Global.event[EV.DINGUS] == 1){
			spawn(1);
			Scene.destroy(Mouse.class);
			destroy();
		}
		else{
			new SoundLoader("sSpringAppearIn", "sSpringAppear");
		}
		
		setCollisionGroup(Global.DEACTIVATEME);
		alarmInitialize(2);
		visible = false;
	}
	
	public void alarmEvent(int i){
		if (i == 0){
			Sound.play("sSpringAppear");
			destroy();
		}
		else{
			Global.explosionEffect(x, y, .75);
			Global.explosionEffect(x, y, 1.5);
			Sound.playPitched("sBombBlast",.05);
			
			Fg.me.shakeTimer = 10;
			spawn(20);
			alarm[0] = 15;
		}
	}
	
	public void step(){
		if (!kill){
			if (!Scene.instanceExists(Mouse.class)){
				kill = true;
				Global.event[EV.DINGUS] = 1;
				
				Sound.play("sSpringAppearIn");
				alarm[1] = 20;
			}
		}
		
		alarmStep();
	}
	
	public void spawn(int a){
		Spring s = new Spring(x, y, 4, true);
		s.alarm[0] = a;
		kill = true;
	}
	
	public void render(){
		
	}
}

