package world.enemy;

import world.control.Global;
import world.player.Player;
import graphics.Sprite;
import main.Entity;
import main.Scene;

public class BasicEnemy extends Entity{
	public boolean landCheckNecessary = true;

	public BasicEnemy(double x, double y) {
		super(x, y);
		setCollisionGroup(Global.BASICENEMY, Global.DEACTIVATEME);
	}
	
	/**checks if there is a solid block, upblock, or invisible turn block in the direction you are moving. if so, it flips you around. funky town*/
	public boolean turning(){
		double hSign = Math.signum(hspeed);
		
		if(Scene.collision(this, Math.round(x)+hspeed+hSign, y, Global.SOLIDBLOCK, Global.UPBLOCK, Global.TURNER))
		{
			double fa = hspeed;
			hspeed=0;
			x = Math.round(x);
			while(!Scene.collision(this, x+hSign, y, Global.SOLIDBLOCK, Global.UPBLOCK, Global.TURNER))
				x+=hSign;
			hspeed = fa*-1;
		}
		
		return (Scene.collision(this, Math.round(x)+hspeed+hSign, y, Global.SOLIDBLOCK, Global.UPBLOCK, Global.TURNER));
	}
	
	/**this function checks if the player has landed on you from the top and performs the landDamage() method and returns true if that is the case. if the player has not collided with you, false is returned*/
	public int playerCollisionDamagesMe(){
		if (landCheckNecessary){
			double howHigh = (double)mask.height;
			if (orientation == Sprite.CENTERED)
				howHigh /= 2.0;
			else if (orientation == Sprite.NORTH)
				howHigh = 1.0;
			
			//double sub = Player.me.vspeed - vspeed;
			//double sub = (Player.me.vspeed - vspeed);
			
			if (Player.me.abortVspeed)
				return 1;
			
			double sub = 0;
			int a = 1;
			if (Scene.collision(this, x, y-2, Global.PLAYER)){
				if (Player.me.y>=y-howHigh-16-sub && Player.me.y<=y-howHigh+8-sub){
					if (Player.me.vspeed>0)
						a = 2; //player is safe, player damages me
					else if (Player.me.vspeed<0)
						a = 1; //player is safe, i am safe
					else
						a = 0; //player is damaged???
				}
				else if (Player.me.vspeed<=0){
					if (Scene.collision(this, x, y, Global.PLAYER)) //this is a mess for the sake of optimization, im sorry based god
						a = 0; //player is damaged
				}
				if (a == 2)
					landDamage();
				else if (a == 0)
					hurtPlayer();
			}
			return a;
		}
		else
			return 1;
	}
	
	/**method to be automatically called when the player is damaged in playerCollisionDamagesMe*/
	public void hurtPlayer(){
		
	}
	
	/**method to be called when the enemy is landed on by the player object. it is called automatically in the playerCollisionDamagesMe method under certain circumstances*/
	public void landDamage(){

	}
	
	/**method to be called when the enemy is in range of a bomb explosion at initial explosion. it is called automatically in the Global.explosion method*/
	public void bombDamage(){
		
	}
	
	/**method to be called when the enemy collides with player's bullet.*/
	public void gunDamage(){
		
	}

}
