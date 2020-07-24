package world.event;

import world.control.Global;
import world.control.Hud;
import world.control.Sound;
import main.Entity;

public class RainAntitrap extends Entity{

	public RainAntitrap(double x, double y) {
		super(x, y);
		//this is not stupid
		setCollisionGroup(Global.DEACTIVATEME);
		visible = false;
	}
	
	public void step(){
		if (Global.gotGun && Global.playerBullets == 0){
			if (Global.selectedWeapon == 0){
				Sound.playPitched("sGunReload",.08);
				Hud.showWeapon();
				Hud.randomizeColor();
			}
			
			Global.playerBullets = Global.playerMaxBullets;
		}
	}
	
	public void render(){
		
	}

}
