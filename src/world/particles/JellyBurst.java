package world.particles;

import audio.Audio;
import world.boss.jelly.JellyBubble;
import world.control.Global;
import world.control.Sound;
import world.event.JellySalesmanControl;
import world.player.Player;
import main.Calc;
import main.Entity;

public class JellyBurst extends Entity{
	public double dir = 0, dist = 480;
	public JellySalesmanControl mother;
	public double time, ack;
	
	public JellyBurst(JellySalesmanControl mom) {
		super(Player.me.x, Player.me.y);
		visible = false;
		mother = mom;
		
		time = 25;
		ack = 1;
		
		setCollisionGroup(Global.DEACTIVATEME);
		
		resetPosition();
	}
	
	public void step(){
		dir += 4;
		dir *= 1.00075;
		
		dist = Calc.approach(dist, 20, 64);
		dist -= .5;
		if (dist <= 24){
			mother.jellyBurst();
			destroy();
		}
		
		ack -= 1;
		if (ack <= 0){
			Audio.get("sJellySalesmanBell").setPitch(1.0 + Math.abs(Calc.dirY(.4, dir)));
			Sound.play("sJellySalesmanBell");
			
			if (time > 5){
				time -= 3;
				time *= .9;
				if (time < 5)
					time = 5;
			}
			ack = time;
		}
		
		resetPosition();
		JellyBubble i = new JellyBubble(x, y, .5);
		i.vis = true;
	}
	
	public void resetPosition(){
		x = Player.me.x + Calc.dirX(dist, dir) + Calc.rangedRandom(.5);
		y = Player.me.y + Calc.dirY(dist, dir) + Calc.rangedRandom(.5);
	}
	
	public void render(){
		
	}

}
