package world.event;

import java.util.ArrayList;

import graphics.Sprite;
import audio.Audio;
import world.control.Global;
import world.control.Sound;
import world.player.Player;
import main.Calc;
import main.Entity;
import main.Scene;

public class EventTicket extends Entity{
	public boolean get, show;
	public int id;
	
	public int phase, timer;
	public double pitch;
	
	public double a, b;

	public EventTicket(int id, boolean get) {
		super(0, 0);
		this.id = id;
		this.get = get;
		
		Player.control = false;
		Player.me.stepActive = false;
		
		phase = 0;
		timer = 0;
		
		setDepth(Integer.MIN_VALUE + 4);
		setCollisionGroup(Global.DEACTIVATEME);
		
		Audio.get("sEventItemLoop").setLooping(true);
		Audio.get("sEventItemLoop").setGain(1);
		Audio.fade("sEventItemLoop",0,.005);
		Audio.get("sEventItemLoop").setPitch(1);
		Sound.play("sEventItemLoop");
		
		pitch = 1;
		
		y = 240;
		
		Sound.play("sEventItemIn");
		
		ArrayList<Entity> list = Scene.getCollisionGroupList(Global.DEACTIVATEENEMY, Global.BASICNPC);
		for (int i=0; i<list.size(); i++)
			list.get(i).stepActive = false;
		
		if (!get){
			x = 320;
			timer = 15;
			show = true;
			x = 800;
		}
		else{
			x = -160;
			timer = 15;
			show = false;
		}
		
		a = 0;
	}
	
	public void step(){
		if (visible){
			Player.control = false;
			Player.me.stepActive = false;
		}
		
		a += 1;
		b = Math.sin(a / 15);
		
		timer -= 1;
		
		x = Calc.approach(x, 320, 2);
		
		if (timer == 0){
			if (get){
				if (phase == 0){
					Sound.playPitched("sEventItemGet");
					timer = 30;
					show = true;
				}
				else{
					if (visible){
						Player.control = true;
						Player.me.stepActive = true;
						ArrayList<Entity> list = Scene.getCollisionGroupList(Global.DEACTIVATEENEMY, Global.BASICNPC);
						for (int i=0; i<list.size(); i++)
							list.get(i).stepActive = true;
					}
					visible = false;
				}
			}
			else{
				if (phase == 0){
					Sound.playPitched("sEventItemUse");
					timer = 30;
					show = true;
				}
				else{
					if (visible){
						Player.control = true;
						Player.me.stepActive = true;
						ArrayList<Entity> list = Scene.getCollisionGroupList(Global.DEACTIVATEENEMY, Global.BASICNPC);
						for (int i=0; i<list.size(); i++)
							list.get(i).stepActive = true;
					}
					
					visible = false;
				}
			}
			phase += 1;
		}
		
		pitch += .0025;
		Audio.get("sEventItemLoop").setPitch(pitch);
	}
	
	public void render(){
		Sprite.get("sEventItemTicket").render(0, Sprite.CENTERED, x + Calc.rangedRandom(.4), y + Calc.rangedRandom(.4) + (8 * b), 1, 1, 0, 1, 1, 1, 1);
		Sprite.get("sStarburst").render(0, Sprite.CENTERED, x + Calc.rangedRandom(.4), y + Calc.rangedRandom(.4) + (8 * b), .45, .45, 0, 1, "0095EF");
		
		if (show)
			Sprite.get("sEventItem").render(id, Sprite.CENTERED, x + Calc.rangedRandom(.4), y + Calc.rangedRandom(.4) + (8 * b), .7, .7, 0, 1, 1, 1, 1);
		
		if (!get && phase >= 1)
			Sprite.get("sEventItemCross").render(0, Sprite.CENTERED, x + Calc.rangedRandom(4), y + Calc.rangedRandom(4) + (8 * b), .75, .75, 0, 1, 1, 1, 1);
	}

}
