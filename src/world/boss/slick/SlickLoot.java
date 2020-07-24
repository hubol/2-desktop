package world.boss.slick;

import audio.Audio;
import world.control.EV;
import world.control.Global;
import world.control.Music;
import world.control.Sound;
import main.Calc;
import main.Entity;

public class SlickLoot extends Entity{
	public final int number = 20;
	public double dist = 0;
	public double dir = 0;
	
	public int loot, bonus, wait = 0;

	public SlickLoot(double x, double y, int loot, int bonus) {
		super(x, y);
		setCollisionGroup(Global.DEACTIVATEME);
		visible = false;
		
		this.loot = loot;
		this.bonus = bonus * Calc.boolToInt(Global.playerHealth == Global.playerMaxHealth);
		
		Sound.playPitched("sSlickFart", .05);
		
		alarmInitialize(1);
	}
	
	public void alarmEvent(int i){
		Audio.get("sSlickFart").setPitch(2);
		Sound.play("sSlickFart");
		
		Global.addTweet("the motherfucker is dead");
		
		Global.blockFade(false);
		Global.dropExactLoot(x, y, bonus);
		Global.dropLoot(x, y, 20);
		Music.fadeMusic("musTup", true);
		Global.event[EV.SLICK_DEFEAT] = 1;
		
		Global.refreshIconMap();
		
		destroy();
	}
	
	public void step(){
		wait -= 1;
		
		if (wait <= 0){
			if (dir < 360)
				Global.dropExactLoot(x + Calc.dirX(dist, dir), y + Calc.dirY(dist, dir), (int)((double)loot / (double)number));
			dir += 360.0 / (double)number;
			dist += 3.1;
			if (dir >= 360 && alarm[0] == -1)
				alarm[0] = 10;
			alarmStep();
		}
	}

}
