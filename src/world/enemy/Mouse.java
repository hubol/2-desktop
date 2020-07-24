package world.enemy;

import graphics.Sprite;
import main.Calc;
import world.control.Global;
import world.control.Sound;
import world.enemy.BasicEnemy;
import world.player.Player;

public class Mouse extends BasicEnemy {
	public boolean dead;
	public double intense;
	
	public final static Sprite RUN = Sprite.get("sMouseRun"), MASK = Sprite.get("sMouseMask"), DEAD = Sprite.get("sMouseDie");

	public Mouse(double x, double y, double h) {
		super(x, y);
		hspeed=h;
		
		orientation=Sprite.SOUTH;
		dead=false;
		
		sprite= RUN;
		mask=MASK.mask;
		
		intense=0;
		imageSpeed=0;
		
		setCollisionGroup(Global.BASICENEMY, Global.DEACTIVATEME, Global.DEACTIVATEENEMY);
		setDepth(-1);
	}
	
	public void step(){
		if (dead){
			hspeed=0;
			vspeed-=.4;
			vspeed*=1.02;
			intense+=.02;
			intense*=1.4;
			if (intense>=1.5){
				if (!Global.room(5,6))
					Global.dropRandomLoot(x, y, 4.4);
				Sound.explodePlay();
				super.destroy();
			}
		}
		else{
			//movement
			super.turning();
			
			imageSingle+=Math.abs(hspeed)/25;
			if (imageSingle>=2)
				imageSingle-=2;
			xscale=Math.signum(hspeed);
			//collide with player
			//System.out.println(Scene.collision(this, x, y-2, Global.PLAYER) && Player.me.y>=y-36 && Player.me.y<y-31 && Player.me.vspeed>=0);
			//System.out.println(Scene.collision(this, x, y-2, Global.PLAYER) && Player.me.y>=y-mask.height-4 && Player.me.y<y-mask.height+1 && Player.me.vspeed>=0);
			//System.out.println(playerCollisionDamagesMe());
			if (playerCollisionDamagesMe()==0)
				Player.hurtPlayer(20);
		}
		super.step();
	}
	
	public void hurtPlayer(){
		Player.hurtPlayer(20);
	}
	
	public void landDamage(){
		if (!dead){
			Player.invincible = 1;
			Player.me.jump();
			Player.me.vspeed -= .8;
			Sound.play("sLandOnEnemy");
			killMe();
		}
	}
	
	public void bombDamage(){
		killMe();
	}
	
	public void gunDamage(){
		for (int i=0; i<4; i++)
		Global.squareParticle(x + Calc.rangedRandom(16), y + Calc.rangedRandom(16), 3 + (int)Calc.random(5), "FF1E56", 1.5 + Calc.random(5));
		killMe();
	}
	
	public void killMe(){
		if (!dead){
			dead=true;
			Sound.playPitched("sMouseKill",.05);
			
			if (Global.room(5, 6))
				setDepth(Integer.MIN_VALUE + 8);
		}
	}
	
	public void render(){
		if (!dead)
			RUN.render((int)imageSingle, Sprite.SOUTH, x-1+Calc.random(2), y-1+Calc.random(2), xscale, 1, 0, 1, 1, 1, 1);
		else
			DEAD.render((int)Calc.random(4), Sprite.SOUTH, x+((-8+Calc.random(16))*intense), y+((-8+Calc.random(16))*intense), xscale*Math.max(1, intense), Math.max(1, intense), (-8+Calc.random(16))*intense, 1, 1, 1, 1);
	}

}
