package world.event;

import graphics.Graphics;
import graphics.Shape;
import world.Overlay;
import world.Root;
import world.control.Global;
import world.control.Hud;
import world.control.Music;
import world.control.SaveFile;
import world.control.Sound;
import world.control.SoundLoader;
import main.Entity;
import main.Main;

public class FuckOff extends Entity{
	public int timer;
	public int load;
	public boolean FUCK = false, FUCKTWO = false;
	public String s;

	public FuckOff() {
		super(0, 0);
		stupid();
		load = -1;
	}
	
	public FuckOff(int a) {
		super(0, 0);
		stupid();
		load = a;
	}
	
	public FuckOff(String s){
		super(0,0);
		stupid();
		load = -1;
		FUCKTWO = true;
		this.s = s;
	}
	
	public void stupid(){
		setDepth(Integer.MIN_VALUE);
		sound();
		timer = 60;
	}
	
	public FuckOff(boolean a){
		super(0,0);
		stupid();
		load = -1;
		FUCK = true;
	}
	
	public void sound(){
		new SoundLoader(false, "sMenuLove", "sIntroCover");
		if (Global.menuSong != 0 && Global.menuOverlay > 126 && !Global.iLoveYou){
			Global.iLoveYou = true;
			Sound.play("sMenuLove");
		}
		Sound.play("sIntroCover");
	}
	
	public void step(){
		timer -= 1;
		if (timer == 0){
			if (!FUCKTWO){
				if (load == -1){
					Hud.me.currentMoney = 0;
					if (!FUCK)
						Root.changeRoom("prep");
					else{
						Music.changeMusic("musDeath", false);
						Main.animator.resetFPSCounter();
						Root.changeRoom("death");
					}
				}
				else{
					SaveFile.load(load);
					Overlay.fadeOut(0, 0, 0, 1, 60);
				}
			}
			else{
				Main.animator.resetFPSCounter();
				Root.changeRoom(s);
			}
				
		}
	}
	
	public void render(){
		Graphics.setColor("000000");
		Graphics.setAlpha(1);
		Shape.drawRectangle(0, 0, 640, 480);
	}

}
