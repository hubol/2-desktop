package world.gameplay;

import world.control.Global;
import world.control.Hud;
import world.control.Sound;
import world.particles.Sparkle;
import world.player.Player;
import graphics.Sprite;
import main.Calc;
import main.Entity;
import main.Scene;

public class Gem extends Entity{
	public int worth, type, id, wait;
	public boolean played;
	public double sparkleTimer;
	
	public final static Sprite GEM_1 = Sprite.get("sGem1"), GEM_5 = Sprite.get("sGem5"), GEM_10 = Sprite.get("sGem10");

	//wut!!!!! 0=give away(found in rooms) 1=spoils/loot!(dropped by enemy)
	public Gem(double x, double y, int num, int wut, int a){
		super(x, y);
		worth=num;
		type=wut;
		id=a;
		played=false;
		
		wait = 10;
		
		setDepth(-(1+(int)Calc.random(4)));
		
		if (worth == 1)
			sprite = GEM_1;
		else if (worth == 5)
			sprite = GEM_5;
		else
			sprite = GEM_10;
		
		orientation=Sprite.CENTERED;
		mask=sprite.mask;
		angle=Calc.random(360);
		imageSingle=(int)Calc.random(3);
		imageSpeed=0;
		
		setCollisionGroup(Global.DEACTIVATEME);
		
		if (type==1){
			vspeed=-2-Player.me.random(4);
			hspeed=-6+Player.me.random(12);
			
			resetSparkle();
		}
		else if (Global.gemGot[id]){
			visible=false;
			destroy();
		}
		else
			resetSparkle();
	}
	
	public void resetSparkle(){
		sparkleTimer = 5 + Calc.random(15 - worth);
		new Sparkle(x + Calc.rangedRandom(11), y + Calc.rangedRandom(11));
	}
	
	public void step(){
		wait -= 1;
		
		angle-=hspeed;
		if (vspeed>0)
			hspeed*=.85;
		else if (vspeed<0)
			hspeed*=.95;
		else
			hspeed*=.7;
		
		double hSign = Math.signum(hspeed);
		
		if (hspeed != 0){ //optimization
			if(Scene.collision(this, Math.round(x)+hspeed+hSign, y, Global.SOLIDBLOCK, Global.UPBLOCK))
			{
				hspeed=0;
				x = Math.round(x);
				while(!Scene.collision(this, x+hSign, y, Global.SOLIDBLOCK, Global.UPBLOCK))
					x+=hSign;
			}
		}

		double vSign = (vspeed<0?-1:1);
		if (vspeed != 0){ //optimization
			if(Scene.collision(this, x+hspeed,Math.round(y)+vspeed+vSign, Global.SOLIDBLOCK, Global.UPBLOCK))
			{
				
				if (!played && type==1 && vSign > 0){
					played=true;
					Sound.gemPlay();
				}
				
				y = Math.round(y);
				while(!Scene.collision(this, x+hspeed,y+vSign, Global.SOLIDBLOCK, Global.UPBLOCK)){
					y+=vSign;
				}
				
				if (vSign > 0)
					vspeed=0;
				else if (vSign < 0){
					vspeed = -vspeed;
					y += vspeed;
				}
			}
			else
			{
				vspeed+=.79;
			}
		}
		else{
			if (!Scene.collision(this, x+hspeed,y+1,Global.SOLIDBLOCK, Global.UPBLOCK))
				vspeed = .1;
		}
		
		if (wait <= 0){ //"optimization"
			if (Calc.pointDistance(x, y, Player.me.x, Player.me.y) < 24){
				collect();
			}
		}
		
		if (vspeed > 12)
			vspeed = 12;
		
		super.step();
		
		if (Player.me.vLoop && vspeed != 0){
			while (y < 0)
				y += 480;
			while (y > 480)
				y-= 480;
		}
		
		sparkleTimer -= 1;
		if (sparkleTimer <= 0)
			resetSparkle();
		
		if (y >= 540)
			destroy();
	}
	
	public void collect(){
		Sound.playPitched("sGemGet"+worth);
		Global.money+=worth;
		Global.gemMilestones -= worth;
		if (Global.gemMilestones <= 0){
			Global.gemMilestones += 100;
			
			String t = (String)Calc.choose("gem flow","cash flow","team cash","team gems","gems gems everywhere","look at all these fuckin gems","holy shit","gems for days!!!","cute money","helly money","hella gems obtained","hella cash acquired","initialize money dance");
			if (Calc.random(1)>.5){
				String[] a = t.split(" ");
				t = "#";
				for (int i=0; i<a.length; i++)
					t += a[i];
			}
			t += (String)Calc.choose("","","","!","!!!","!!!!!","","","","");
			Global.addTweet(t);
			
		}
		if (type==0)
			Global.gemGot[id]=true;
		
		Hud.showMoney();
		
		//PARTICLES!!!!!!!!!!!
		String c = "DF87FF";
		if (worth==5)
			c = "00D786";
		else if (worth==10)
			c = "FF688B";
		Global.squareParticle(x, y, Math.max(5,worth), c, 4);
		//END PARTICLES!!!!!!!!
		
		destroy();
	}
	
	public void render(){
		double ax=x, ay=y,a=angle;
		x+=-1+Calc.random(2);
		y+=-1+Calc.random(2);
		angle+=-2+Calc.random(4);
		super.render();
		if (Player.me.vLoop){
			y -= 480;
			super.render();
			y += 960;
			super.render();
		}
		x=ax;
		y=ay;
		angle=a;
	}

}
