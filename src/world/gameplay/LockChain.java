package world.gameplay;

import world.control.Global;
import world.control.Sound;
import graphics.Sprite;
import main.Calc;
import main.Entity;
import main.Scene;

public class LockChain extends Entity {
	public final static Sprite LOCK = Sprite.get("sLock"), CHAIN = Sprite.get("sLockChain");
	
	public double timer;
	public boolean killed, played, arf;
	
	public double rattle, sattle;
	
	public LockChain(double x, double y, boolean t, String c) {
		super(x, y);
		if (t){
			sprite=LOCK;
			setDepth(5);
		}
		else{
			sprite=CHAIN;
			setDepth((Integer)Calc.choose(6,7));
			angle=Calc.random(360);
		}
		
		mask=sprite.mask;
		
		setColor(c);
		orientation=Sprite.CENTERED;
		timer=Calc.random(7)+1;
		
		arf = false;
		played=false;
		killed=false;
		
		setCollisionGroup(Global.DEACTIVATEME);
		
		rattle=0;
		sattle=0;
	}
	
	public void step(){
		boolean a = true;
		while(a || (arf && vspeed != 0)){
			if (killed){
				if (sprite == Sprite.get("sLock"))
					sprite = Sprite.get("sUnlock");
				
				hspeed*=.9;
				
				angle-=hspeed;
				
				double hSign = Math.signum(hspeed);
				
				if(Scene.collision(this, Math.round(x)+hspeed+hSign, y, Global.SOLIDBLOCK, Global.UPBLOCK))
				{
					hspeed=0;
					x = Math.round(x);
					while(!Scene.collision(this, x+hSign, y, Global.SOLIDBLOCK, Global.UPBLOCK))
						x+=hSign;
				}
	
				double vSign = (vspeed<0?-1:1);
				if(Scene.collision(this, x+hspeed,Math.round(y)+vspeed+vSign, Global.SOLIDBLOCK, Global.UPBLOCK))
				{
					vspeed=0;
					
					if (!played){
						played=true;
						Sound.chainPlay();
					}
					
					y = Math.round(y);
					while(!Scene.collision(this, x+hspeed,y+vSign, Global.SOLIDBLOCK, Global.UPBLOCK)){
						y+=vSign;
					}
				}
				else
				{
					vspeed+=.79;
				}
			}
			else{
				x+=-.5+Calc.random(1);
				y+=-.5+Calc.random(1);
				
				if (rattle > 0){
					x+=Calc.rangedRandom(1.3)*(rattle/sattle);
					y+=Calc.rangedRandom(1.3)*(rattle/sattle);
				}
				
				rattle-=1;
				
				timer-=1;
				if (timer<=0){
					timer=Calc.random(7)+1;
					x=xstart;
					y=ystart;
				}
			}
			super.step();
			a = false;
			if (y > 480)
				arf = false;
		}
	}
	
	public void rattle(){
		rattle=9+Calc.random(15);
		sattle=rattle;
	}
	
	public void kill(){
		killed=true;
		vspeed=-.25+Calc.random(1.25);
		hspeed=-3+Calc.random(6);
	}
	
	public void render(){
		double addX=Calc.rangedRandom(.15), addY=Calc.rangedRandom(.15);
		
		if (rattle>0){
			addX=Calc.rangedRandom(4.7)*(rattle/sattle);
			addY=Calc.rangedRandom(4.7)*(rattle/sattle);
		}
		
		sprite.render(0,orientation,x+addX,y+addY,1,1,angle,1,1,1,1);
		sprite.render(1,orientation,x+addX,y+addY,1,1,angle,1,colR,colG,colB);
	}

}
