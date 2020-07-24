package world.enemy;

import graphics.Sprite;
import main.Calc;
import world.control.EV;
import world.control.Global;
import world.control.Sound;
import world.enemy.BasicEnemy;
import world.event.NoOccupy;
import world.player.Player;
import main.Scene;

public class Urchin extends BasicEnemy {
	public boolean dead, suicide;
	public double intense, worth;
	public int suicideTimer;
	public int damage;
	public Sprite death;

	public final static Sprite URCHIN = Sprite.get("sUrchin"), MASK = Sprite.get("sUrchinMask"), DEATH = Sprite.get("sUrchinDie"), SUICIDE = Sprite.get("sSuicidalUrchin"), SUICIDE_DEATH = Sprite.get("sSuicidalUrchinDie"), CLOSE = Sprite.get("sSuicidalUrchinClose"), DUMB = Sprite.get("sSuicideUrchin");
	
	public Urchin(double x, double y) {
		super(x, y);
		orientation=Sprite.CENTERED;
		dead=false;
		
		suicide = false;
		
		landCheckNecessary = false;
		
		sprite=URCHIN;
		mask=MASK.mask;
		death = DEATH;
		
		worth = 5;
		
		intense=0;
		imageSingle = Calc.random(2);
		imageSpeed=Calc.random(.4);
		
		angle = Calc.rangedRandom(15);
		
		suicideTimer = -1;
		
		damage();
		
		if (Global.room(8, 5))
			worth = 2;
		
		resetCollision();
	}
	
	public void damage(){
		damage = 30;
		if (Global.currentArea.equals("CAVE"))
			damage = 50;
		else if (Global.currentArea.equals("STONE"))
			damage = 60;
		else if (Global.currentArea.equals("PLANT"))
			damage = 80;
		else if (Global.currentArea.equals("KISS")||Global.currentArea.equals("URCHIN"))
			damage = 90;
	}
	
	public Urchin(double x, double y, int pos) {
		super(x, y);
		orientation=Sprite.CENTERED;
		dead=false;
		
		suicide = true;
		
		landCheckNecessary = false;
		
		sprite = SUICIDE;
		mask=MASK.mask;
		
		death = SUICIDE_DEATH;
		
		//suicideTimer = -1;
		
		//if (Global.gotStench)
			suicideTimer = 900000;
		
			damage();	
			
		worth = 5;
		
		intense=0;
		imageSingle = Calc.random(2);
		imageSpeed=Calc.random(.4);
		
		angle = Calc.rangedRandom(15);
		
		if (Global.room(8, 5))
			worth = 2;
		
		resetCollision();
	}
	
	public void resetCollision(){
		if (Scene.collision(this, x, y, Global.PLAYER))
			setCollisionGroup(Global.BASICENEMY, Global.DEACTIVATEME);
		else
			setCollisionGroup(Global.BASICENEMY, Global.SOLIDBLOCK, Global.TEMPSOLID, Global.DEACTIVATEME);
	}
	
	public void step(){
		if (dead){
			if (hspeed == 0)
				hspeed = Calc.rangedRandom(1);
			vspeed-=.1+Calc.random(.3);
			vspeed*=1.05+Calc.random(.015);
			intense+=.02;
			intense*=1.4;
			if (intense>=1.5){
				Global.dropRandomLoot(xstart, ystart, worth);
				Global.squareParticle(x, y, 5 + (int)Calc.random(5), "FF1864", 3 + Calc.random(3));
				Global.squareParticle(x, y, 3 + (int)Calc.random(3), "FF1864", 5 + Calc.random(5));
				Sound.explodePlay();
				super.destroy();
				Global.refreshRain();
			}
		}
		else{
			if (suicideTimer > 13){
				boolean can = Global.gotStench;
				if (Global.room(4, 4) && can)
					can = (Global.event[EV.PAPA_DEFEAT] > 0);
				
				if (Calc.pointDistance(x,y,Player.me.x,Player.me.y) <= 180 && can)
					sprite = CLOSE;
				else
					sprite = SUICIDE;
			}
			
			if (Calc.pointDistance(x,y,Player.me.x,Player.me.y) < 96 && suicideTimer > 12 && Global.gotStench)
				suicideTimer = 13;
			
			if (Global.gotStench)
				suicideTimer -= 1;
			if (suicideTimer == 12 && Global.gotStench){
				Sound.playPitched("sUrchinSuicideBegin",.15);
				sprite = DUMB;
			}
			if (suicideTimer == 0 && Global.gotStench){
				Sound.playPitched((String)Calc.choose("sUrchinSuicide","sUrchinSuicideScream"),.09);
				dead = true;
			}
		}
		
		if ((Scene.collision(this, x-1, y, Global.PLAYER)||Scene.collision(this, x+1, y, Global.PLAYER)||Scene.collision(this, x, y-1, Global.PLAYER)||Scene.collision(this, x, y+1, Global.PLAYER)) && !dead && !(Global.gotStench && suicide))
				Player.hurtPlayer(damage);
		
		/*if (Scene.collision(this, x-1, y, Global.PLAYER)){
			if (!Scene.collision(this, x-1, y, Global.BLOCK,Global.BOMBROCK,Global.BOSSBLOCK))
				Player.hurtPlayer(30);
		}
		else if (Scene.collision(this, x+1, y, Global.PLAYER)){
			if (!Scene.collision(this, x+1, y, Global.BLOCK,Global.BOMBROCK,Global.BOSSBLOCK))
				Player.hurtPlayer(30);
		}
		else if (Scene.collision(this, x, y-1, Global.PLAYER)){
			if (!Scene.collision(this, x, y-1, Global.BLOCK,Global.BOMBROCK,Global.BOSSBLOCK))
				Player.hurtPlayer(30);
		}
		else if (Scene.collision(this, x, y+1, Global.PLAYER)){
			if (!Scene.collision(this, x, y+1, Global.BLOCK,Global.BOMBROCK,Global.BOSSBLOCK))
				Player.hurtPlayer(30);
		}*/
		
		resetCollision();
		
		super.step();
	}
	
	public void bombDamage(){
		killMe();
	}
	
	public void gunDamage(){
		if (!dead){
		worth *= .7;
		
		for (int i=0; i<4; i++)
			Global.squareParticle(x + Calc.rangedRandom(16), y + Calc.rangedRandom(16), 3 + (int)Calc.random(5), "FF1E56", 1.5 + Calc.random(5));
		}
		killMe();
	}
	
	public void killMe(){
		if (!dead){
			new NoOccupy(x - 16, y - 16);
			
			dead=true;
			Sound.playPitched("sUrchinKill");
		}
	}
	
	public void render(){
		double ax, ay;
		ax = Calc.rangedRandom(1);
		ay = Calc.rangedRandom(1);
		
		if (suicideTimer < 13 && suicideTimer >= 0){
			ax = Calc.rangedRandom(14 - suicideTimer);
			ay = Calc.rangedRandom(14 - suicideTimer);
		}
		
		if (!dead)
			sprite.render((int)imageSingle, orientation, x+ax, y+ay, xscale, 1, angle + Calc.rangedRandom(1), 1, 1, 1, 1);
		else
			death.render((int)Calc.random(4), orientation, x+((-8+Calc.random(16))*intense), y+((-8+Calc.random(16))*intense), xscale*Math.max(1, intense), Math.max(1, intense), (-8+Calc.random(16))*intense, 1, 1, 1, 1);
	}

}
