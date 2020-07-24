package world.boss.papa;

import main.Calc;
import main.Entity;
import audio.Audio;
import world.control.EV;
import world.control.Global;
import world.control.Music;

public class PapaGhost extends Entity{
	public double showSqueeze;
	public final double radius = 24.0 / Math.sin(22.5 * Calc.toRad);

	public PapaGhost(double x, double y, double scale, double angle) {
		super(x, y);
		Audio.fade("musBoss07Layer", 0, .05);
		showSqueeze = scale;
		this.angle = angle;
		alarmInitialize(2);
		alarm[0] = 60;
		alarm[1] = 30;
	}
	
	public void stream(double dir){
		stream(dir, "FF1864");
	}
	
	public void stream(double dir, String color){
		stream(dir, radius, color);
	}
	
	public void stream(double dir, double dist, String color){
		for (int i=0; i<8; i++){
			final double d = Calc.random(360), l = Calc.random(5);
			new StreamerLol(this, Calc.pointDistance(0, 0, Calc.dirX(dist, dir) + Calc.dirX(l, d), Calc.dirY(dist, dir) + Calc.dirY(l, d)), Calc.pointDirection(0, 0, Calc.dirX(dist, dir) + Calc.dirX(l, d), Calc.dirY(dist, dir) + Calc.dirY(l, d)), angle, x + Calc.dirX(dist, dir) + Calc.dirX(l, d), y + Calc.dirY(dist, dir) + Calc.dirY(l, d), dir + Calc.rangedRandom(10), color, 4 + (int)Calc.random(10));
		}
	}
	
	public void alarmEvent(int i){
		if (i == 0){
			Global.event[EV.PAPA_DEFEAT] = 1;
			Music.fadeMusic(Global.roomMusic, true);
			Global.blockFade(false);
			Global.heal();
			Global.refreshIconMap();
		}
		else if (i == 1){
			Global.dropExactLoot(x, y, 150);
			if (Global.playerHealth == Global.playerMaxHealth)
				Global.dropExactLoot(x, y, 150);
		}
	}

	public void step(){
		Audio.get("musBoss07Layer").setPitch(Math.max(.1, Audio.get("musBoss07Layer").getPitch() - .0005));
		alarmStep();
	}
	
	public void render(){
		
	}

}
