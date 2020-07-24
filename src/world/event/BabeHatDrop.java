package world.event;

import graphics.Sprite;
import world.Fg;
import world.control.Global;
import world.control.Shake;
import world.control.Sound;
import world.gameplay.DropKey;
import world.player.Player;
import main.Calc;
import main.Entity;

public class BabeHatDrop extends Entity{
	public double addY = 0;
	public int timer = 33;
	
	public boolean key = false;
	public Shake s = new Shake(.4);
	
	public int dist = 80;

	public BabeHatDrop(double x, double y) {
		super(x, y);
		alarmInitialize(1);
		alarm[0] = 1;
	}
	
	public void alarmEvent(int i){
		if (dist < 640){
			for (int j=0; j<6; j++){
				Global.explosionEffect(x + Calc.dirX(dist, j * 60), y + Calc.dirY(dist, j * 60), .85);
				Global.explosionEffect(x + Calc.dirX(dist, j * 60), y + Calc.dirY(dist, j * 60), 1.2);
			}
			Sound.explodePlay();
			Sound.explodePlay();
			
			Fg.me.shakeTimer = 8;
			
			alarm[0] = 4;
			dist += 80;
		}
	}
	
	public void step(){
		timer -= 1;
		
		addY = Math.min(22, Calc.approach(addY, 22, 4) + .2);
		
		Sound.playPitched("sBabeBlink",.5);
		
		key = true;
		visible = true;
		
		if (timer < 0){
				Player.control = true;
				int a = Global.getFirstEmptyDrop();
				Global.dropRmX[a] = Global.roomX;
				Global.dropRmY[a] = Global.roomY;
				Global.dropX[a] = x - 16;
				Global.dropY[a] = y - 32;
				Global.dropColor[a] = "D7FF18";
				new DropKey(x - 16, y - 32, a, "D7FF18");
				destroy();
			}
		
		alarmStep();
	}
	
	public void render(){
		final double x = this.x + s.x + Calc.rangedRandom(.15), y = this.y + s.y + Calc.rangedRandom(.15);
		if (!key)
			Sprite.get("sBabeBody").render(0, Sprite.SOUTH, x, y + addY, 1, 1, 0, 1, 1, 1, 1);
		else
			Sprite.get("sBabeKey").render(0, Sprite.SOUTH, x, y, 1, 1, 0, 1, 1, 1, 1);
	}

}
