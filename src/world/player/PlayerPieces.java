package world.player;

import world.control.Sound;
import graphics.Sprite;
import main.Calc;
import main.Entity;

public class PlayerPieces extends Entity{
	public double iX, iY, targetX, targetY, div, count;
	public boolean out;
	public int timer, leaveTimer;
	public boolean shit, used;
	
	public final static Sprite STAND = Sprite.get("sStand");

	public PlayerPieces(double x, double y, double iX, double iY, boolean o, boolean ugh) {
		super(x, y);
		this.iX = iX;
		this.iY = iY;
		
		used = false;
		
		shit = ugh;
		if (shit)
			leaveTimer = 30 + calcTimer();
		else
			leaveTimer = 90000;
		
		setDepth(Integer.MIN_VALUE+10);
		
		targetX = Player.me.x - 16 + (iX/32);
		targetY = Player.me.y - 16 + (iY/32);
		
		out = o;
		count = 0;
		
		sprite = Sprite.get("sStand");
		
		imageSingle = 0;
		imageSpeed = .4 + Player.me.rangedRandom(.01);
		
		timer = 90000;
		
		if (out)
			setDirSpeed(Player.me.random(360),12 + Player.me.random(6));
		else{
			out = true;
			div = 12;
			timer = calcTimer();
		}
	}
	
	public int calcTimer(){
		return (int)(((((iY / 2.0) * 16) + (iX / 2.0)))/9.0);
	}
	
	public void step(){
		if (out){
			
			count += 1;
			
			x += Player.me.rangedRandom(1);
			y += Player.me.rangedRandom(1);
			setSpeed(getSpeed()*(.95 - Math.abs(.2 * Math.sin(count/10.0))));
			
			timer -= 1;
			if (timer <= 0){
				out = false;
				setSpeed(0);
			}
			
			leaveTimer -= 1;
			if (leaveTimer <= 0){
				out = false;
				setDirSpeed(90 + Player.me.rangedRandom(30),6 + Player.me.random(3));
			}
		}
		else{
			if (!shit){
				x = Calc.approach(x, targetX, div);
				y = Calc.approach(y, targetY, div);
				
				div -= .12512;
				div *= .87;
				if (div < 1){
					setDepth(0);
					
					div = 1;
					if (!used){
						used = true;
						Sound.playPitched("sDreamReattach",.2);
					}
				}
			}
			else{
				setSpeed((getSpeed() + .2) * 1.08);
				setDirection(Calc.approach(getDirection(), 90, 20));
				
				x = Calc.approach(x, Player.me.x, Math.max(1, 20 - getSpeed()));
			}
		}
		
		super.step();
	}
	
	public void toTarget(){
		div = 12;
		timer = calcTimer();
	}
	
	public void render(){
		if (out)
			STAND.renderPart((int)imageSingle, Sprite.NORTHWEST, x, y, iX, iY, 2, 2, 1, 1, Calc.rangedRandom(1), 1 + Calc.rangedRandom(.2), 1, 1, 1);
		else
			STAND.renderPart((int)imageSingle, Sprite.NORTHWEST, x, y, iX, iY, 2, 2, 1, 1, 0, 1 + Calc.rangedRandom(.2), 1, 1, 1);
	}

}
