package world.boss.jelly;

import world.control.Global;
import world.player.Player;
import main.Entity;

public class JellyGhost extends Entity{

	public JellyGhost(double x, double y) {
		super(x, y);
		visible = false;
	}
	
	public void step(){
		if (Player.me.x >= 160){
			Global.blockFade(false);
			destroy();
		}
	}
	
	public void render(){
		
	}

}
