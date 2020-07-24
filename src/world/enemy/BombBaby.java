package world.enemy;

import world.control.Global;
import world.control.Hud;
import world.control.PseudoGenerator;
import world.control.Sound;
import world.player.Player;
import graphics.Sprite;
import main.Calc;
import main.Scene;

public class BombBaby extends BasicEnemy{
	public boolean dead;
	public double intense;
	public boolean landLast;
	public double launchTimer;
	public double worth;
	public boolean drop;
	
	public PseudoGenerator g;

	public BombBaby(double x, double y) {
		super(x, y);
		
		g = new PseudoGenerator(1.06 + (x / 1960.0),1.07 + (y / 1960.0), 1.1, 1626.2, 2.2, 1.2, x + y + (Global.roomX * 640) +  (Global.roomY * 480));
		
		worth = g.random(5) + 1;
		drop = true;
		if (Global.gotBombs)
			drop = false;
		if (g.random(1) > .9995){
			worth = 500;
			drop = true;
		}
		
		orientation=Sprite.SOUTH;
		dead=false;
		
		sprite=JUMP;
		mask=sprite.mask;
		
		intense=0;
		imageSpeed=0;
		
		landLast = true;
		
		launchTimer = g.random(60);
		
		setCollisionGroup(Global.BASICENEMY, Global.DEACTIVATEME);
		addCollisionGroup(Global.DEACTIVATEENEMY);
		setDepth(-1);
	}
	
	public void step(){
		if (dead){
			hspeed=0;
			vspeed-=.5;
			vspeed*=1.04;
			intense+=.04;
			intense*=1.3;
			if (intense>=1.8){
				
				Sound.babyPlay();
				
				if (Global.gotBombs){
					
					Sound.playPitched("sGetBomb",.02);
					if (Global.playerBombs < Global.playerMaxBombs){
						Global.playerBombs += 1;
					}
					
					Player.me.bombShow = 15;
					
					//new Damage(Player.me.x + 16, Player.me.y - 10, "BOMB!");
					
					if (Global.selectedWeapon == 1)
						Hud.showWeapon();
				}
				else
					Sound.explodePlay();
				
				Global.squareParticle(x, y - 12, 12, "FF4873", 4);
				
				if (drop)
				Global.dropRandomLoot(x, y, (int)worth);
				
				super.destroy();
			}
		}
		else{
			//movement
			super.turning();
			
			if (vspeed!=0)
				landLast=false;
			
			double vSign = (vspeed<0?-1:1);
			if(Scene.collision(this, x+hspeed,Math.round(y)+vspeed+vSign, Global.SOLIDBLOCK, Global.UPBLOCK))
			{
				if (!landLast)
				{
					landLast=true;

					if (vSign > 0){
						Sound.babyPlay();
						launchTimer = g.random(60);
					}
				}
				vspeed=0;
				y = Math.round(y);
				while(!Scene.collision(this, x+hspeed,y+vSign, Global.SOLIDBLOCK, Global.UPBLOCK)){
					y+=vSign;
				}
			}
			else if (!Scene.collision(this, x+hspeed,Math.round(y)+1, Global.SOLIDBLOCK, Global.UPBLOCK)){
				vspeed += .8;
			}
			
			if (vspeed != 0){
				imageSingle = 1;
			}
			else {
				imageSingle+=.1;
				if (imageSingle>=2)
					imageSingle-=2;
			}
			
			if (hspeed != 0)
				xscale=-Math.signum(hspeed);
			//collide with player
			//System.out.println(Scene.collision(this, x, y-2, Global.PLAYER) && Player.me.y>=y-36 && Player.me.y<y-31 && Player.me.vspeed>=0);
			//System.out.println(Scene.collision(this, x, y-2, Global.PLAYER) && Player.me.y>=y-mask.height-4 && Player.me.y<y-mask.height+1 && Player.me.vspeed>=0);
			//System.out.println(playerCollisionDamagesMe());
			if (playerCollisionDamagesMe()==0/* || Scene.collision(this, x, y-vspeed, Global.PLAYER)*/)
				Player.hurtPlayer(1);
		}
		super.step();
		
		hspeed *= .95;
		
		launchTimer -= 1;
		if (launchTimer <= 0 && vspeed == 0){
			hspeed = g.rangedRandom(4);
			vspeed = -3.25-g.rangedRandom(3.25);
			Sound.babyPlay();
			launchTimer = 90000000;
		}
	}
	
	public void landDamage(){
		if (!dead){
			Player.invincible = 1;
			Player.me.jump();
			Player.me.vspeed -= .7;
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
			Sound.playPitched("sBombBabyDie",.05);
			Sound.babyPlay();
		}
	}
	
	public final static Sprite JUMP = Sprite.get("sBombBabyJump"), DEAD = Sprite.get("sBombBabyDead");
	
	public void render(){
		if (!dead)
			JUMP.render((int)imageSingle, Sprite.SOUTH, x-1+Calc.random(2), y-1+Calc.random(2), xscale, 1, 0, 1, 1, 1, 1);
		else
			DEAD.render((int)Calc.random(5), Sprite.SOUTH, x+((-8+Calc.random(16))*intense), y+((-8+Calc.random(16))*intense), xscale*Math.max(1, intense), Math.max(1, intense), (-8+Calc.random(16))*intense, 1, 1, 1, 1);
	}

}
