package world.gameplay;

import java.util.ArrayList;

import world.control.Global;
import world.control.MapIconInfluence;
import world.control.PseudoGenerator;
import world.control.Sound;
import world.player.Player;

import graphics.Sprite;
import main.Calc;
import main.Scene;

public class Lock extends MapIconInfluence {
	public LockChain[] mine;
	public boolean can, action, killed;
	public int timer, timerB, fuck;
	public String color;
	public PseudoGenerator myGen;

	public Lock(double x, double y, int id, String color) {
		super(x, y);
		orientation=Sprite.NORTHWEST;
		
		sprite=Global.sDOOR;
		mask=sprite.mask;
		
		myGen = new PseudoGenerator(.9 + ((Global.roomX + 1) / 48.0), .9 + ((Global.roomY + 1) / 49.0), (x + y + id) / 640.0, .69, .42, 1, 0);
		
		fuck=id;
		
		can=true;
		timer=0;
		action=false;
		killed=false;
		timerB=0;
		
		this.color=color;
		setColor(color);
		
		if (!Global.unlocked[id]){
			mine = new LockChain[18];
			mine[0] = new LockChain(x+1,y+2,false,color);
			mine[1] = new LockChain(x+5,y+11,false,color);
			mine[2] = new LockChain(x+7,y+19,false,color);
			mine[3] = new LockChain(x+12,y+28,false,color);
			mine[4] = new LockChain(x+17,y+34,false,color);
			mine[5] = new LockChain(x+22,y+42,false,color);
			mine[6] = new LockChain(x+26,y+52,false,color);
			mine[7] = new LockChain(x+30,y+61,false,color);
			mine[8] = new LockChain(x+-1,y+64,false,color);
			mine[9] = new LockChain(x+3,y+59,false,color);
			mine[10] = new LockChain(x+6,y+53,false,color);
			mine[11] = new LockChain(x+9,y+42,false,color);
			mine[12] = new LockChain(x+12,y+30,false,color);
			mine[13] = new LockChain(x+19,y+23,false,color);
			mine[14] = new LockChain(x+24,y+15,false,color);
			mine[15] = new LockChain(x+27,y+7,false,color);
			mine[16] = new LockChain(x+30,y+2,false,color);
			mine[17] = new LockChain(x+14,y+31,true,color);
			
			setCollisionGroup(Global.LOCKED);
			addCollisionGroup(Global.DEACTIVATEME);
			addCollisionGroup(Global.INTERACTABLE);
		}
		else{
			for (int i =0; i<17; i++){
				LockChain j = new LockChain(x + 16 + myGen.rangedRandom(34), y + 52 + myGen.random(2), false, color);
				j.killed = true;
				j.played = true;
				j.arf = true;
				j.angle = myGen.random(360);
				j.vspeed = .01;
			}
			
			LockChain j = new LockChain(x + 16 + myGen.rangedRandom(4), y + 52 + myGen.random(2), true, color);
			j.killed = true;
			j.played = true;
			j.arf = true;
			j.angle = myGen.random(360);
			j.vspeed = .01;
			
			killed = true;
			setCollisionGroup(Global.UNLOCKED);
			super.destroy(); //meh
		}
	}
	
	public void step(){
		if (isOfCollisionGroup(Global.LOCKED)){
			action=false;
			if (Scene.collision(this,x,y,Global.PLAYER) && can && !killed && Player.me.vspeed == 0 && Player.me.y == y + 48 && Scene.collision(Player.me, Player.me.x, Player.me.y + 1, Global.SOLIDBLOCK)){
				action=true;
				Player.canChangeWeapon = false;
			}
			
			if (action && Player.canDownInteract()){
				Sound.play("sUnlock");
				
				if (Global.checkHoldingKey(color) || Global.checkHoldingKey("FFFFFF")){
					Global.refreshIconMap();
					Global.deleteKey(color);
					Global.unlocked[fuck] = true;
					Sound.play("sUnlock2");
					killed=true;
					unlock();
					timerB=13;
					action=false;
					can=false;
					timer=90000;
					
					//if two of the same id locked doors are in the same room
					ArrayList<Lock> list = Scene.getEntityList(Lock.class);
					for (int i=0; i<list.size(); i++){
						if (list.get(i)!=this && list.get(i).fuck == fuck){
							list.get(i).unlock();
							list.get(i).killed=true;
							list.get(i).timerB=13;
							list.get(i).action=false;
							list.get(i).can=false;
							list.get(i).timer=90000;
						}
					}
				}
				else{
					Sound.playPitched("sUnlockFail",.07);
					
					action=false;
					can=false;

					for (int i=0; i<mine.length; i++)
						mine[i].rattle();
					timer=12;
				}
			}
			
			timer-=1;
			if (timer<=0)
				can=true;
		}
		
		timerB-=1;
		if (timerB<=0 && killed){
			setCollisionGroup(Global.UNLOCKED);
			super.destroy(); //meh
		}
	}
	
	public void refreshIcon(){
		if (!killed && !isDestroyed())
			Global.setIconMap(Global.roomX, Global.roomY, 3);
	}
	
	public void unlock(){
		String t = "";
		if (Calc.random(1) < .5){
			t = (String)Calc.choose("","","","#");
			t += (String)Calc.choose("unlockage","lockflow","keyflow","keys","keysfordays");
		}
		else{
			t = (String)Calc.choose("unlockage","oh shit just unlokked a dorr","damn im good with keys","hell yeah door opened","RT FOLLOW BACK FOR DOOR UNLOCKAGE","i got keys for days","i got mf keys for days","2 many keys 4 u");
			t += (String)Calc.choose("","","","","","!!!","!","!!!!!!!");
		}
		Global.addTweet(t);
		for (int i=0; i<mine.length; i++)
			mine[i].kill();
	}
	
	/**note: no keys involved*/
	public void forcedUnlock(){
		Sound.play("sUnlock2");
		
		Global.addTweet("unlockage!!");
		
		killed=true;
		timerB=13;
		action=false;
		can=false;
		timer=90000;
		
		for (int i=0; i<mine.length; i++)
			mine[i].kill();
	}
	
	public void render(){
		if (action && Player.control && !Global.paused)
			Global.drawDown(x+16, y-6);
	}

}
