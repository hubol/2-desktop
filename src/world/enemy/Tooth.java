package world.enemy;

import java.awt.Point;

import world.control.Global;
import world.control.Sound;
import world.player.Player;

import graphics.Sprite;
import main.Calc;

public class Tooth extends BasicEnemy {
	public double health;
	public boolean dead;
	
	public int mode; //0 = appear 1 = fall 2 = walk 3 = return
	public int wait;
	public int timer;
	public double img, posTimer, xsc, ysc;
	public Point myPos, crackPos;

	public Tooth(double x, double y, int w) {
		super(x, y);
		orientation = Sprite.SOUTH;
		sprite = Sprite.get("sTooth");
		//mask = Sprite.get("sUrchinMask").mask;
		mask = Sprite.get("sBlock").mask;
		imageSpeed = 0;
		
		setDepth(-1);
		setCollisionGroup(Global.BASICENEMY);
		
		health = 5;
		dead = false;
		
		mode = 0;
		wait = w;
		
		timer = -1;
		img = 0;
		
		myPos = new Point(0,0);
		crackPos = new Point(0,0);
		
		xsc = 1;
		ysc = 1;
		
		resetPos();
	}
	
	public void step(){
		//sitting up yonder
		if (mode == 0){
			angle = 180;
			img = 0;
			wait -= 1;
			if (wait == 0){
				timer = 15;
			}
			else if (wait < 0){
				timer -= 1;
				if (y < ystart + 32)
					y += 2;
				if (y >= ystart + 32 && timer <= 0)
					changeMode(1);
			}
		}
		
		//falling like a muhfucka
		else if (mode == 1){
			img = 1;
			setCollisionGroup(Global.BASICENEMY);
			vspeed += .6;
			angle += 6 * (vspeed/4);
			
			if (Calc.pointDistance(x, y + 16, Player.me.x, Player.me.y) <= 28)
				Player.hurtPlayer(35);
			
			if (y + vspeed >= 416){
				y = 416;
				vspeed = 0;
				changeMode(2);
			}
		}
		
		//walking AIMLESSLY
		else if (mode == 2){
			angle = 0;
			vspeed = 0;
			if (hspeed == 0)
				hspeed = (2.6 + Calc.rangedRandom(.4) + (5 - health)/3) * (Double)Calc.choose(1.0, -1.0);
			else
				turning();
			
			if (hspeed != 0)
				xsc = Math.signum(hspeed);
			
			if (playerCollisionDamagesMe() == 0)
					Player.hurtPlayer(35);
			
			img += Math.abs(hspeed) / 11.0;
			if (img >= 2)
				img -= 2;
			
			timer -= 1;
			if (timer <= 0)
				changeMode(3);
		}
		
		//return to upper position
		else if (mode == 3){
			img = 1;
			setCollisionGroup(0);
			hspeed = 0;
			vspeed = 0;
			x = Calc.approach(x, xstart, 8);
			y = Calc.approach(y, ystart + 32, 8);
			
			angle = Calc.approach(angle, 900, 6);
			
			double a = Calc.pointDistance(x, y, xstart, ystart + 32);
			
			//angle -= 8.0 + (a/16.0);
			
			if (a < 8){
				x = xstart;
				y = ystart + 32;
				changeMode(0);
			}
		}
		
		imageSingle = img + (2 * Calc.fRandom(4.0));
		super.step();
		
		posTimer -= 1;
		myPos.setLocation(myPos.getX()+Calc.rangedRandom(.25),myPos.getY()+Calc.rangedRandom(.25));
		crackPos.setLocation(crackPos.getX()+Calc.rangedRandom(.25),crackPos.getY()+Calc.rangedRandom(.25));
		if (posTimer <= 0)
			resetPos();
	}
	
	public void hurtPlayer(){
		Player.hurtPlayer(35);
	}
	
	public void changeMode(int m){
		angle = 0;
		if (m == 0){
			wait = 1;
		}
		if (m == 2){
			timer = 90;
			img = Calc.random(2);
		}
		mode = m;
	}
	
	public void landDamage(){
		Player.invincible = 1;
		Player.me.jump();
		Sound.play("sLandOnEnemy");
		harm();
	}
	
	public void gunDamage(){
		harm();
	}
	
	public void bombDamage(){
		harm();
	}
	
	public void harm(){
		health -= 1;
		
		if (health == 0){
			if (!dead)
				die();
		}
		else
			changeMode(3);
	}
	
	public void die(){
		dead = true;
		new ToothShard(x, y, 0);
		new ToothShard(x, y, 1);
		new ToothGhost(x, y - 16);
		
		destroy();
	}
	
	public void resetPos(){
		posTimer = 1 + Calc.random(12);
		myPos.setLocation(0.0,0.0);
		crackPos.setLocation(0.0,0.0);
	}
	
	public void render(){
		sprite.render((int)imageSingle, Sprite.CENTERED, x + myPos.getX(), y + myPos.getY() - 16, xsc, ysc, angle, 1, "ffffff");
		int i = Math.max(0, (int)health - 1);
		if (health < 5)
			Sprite.get("sToothCrack").render(i, Sprite.CENTERED, x + crackPos.getX(), y + crackPos.getY() - 16, xsc, ysc, angle, 1, "ffffff");
	}

}
