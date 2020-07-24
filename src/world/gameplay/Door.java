package world.gameplay;

import world.Root;
import world.control.Global;
import world.control.Sound;
import world.player.Player;
import graphics.Graphics;
import graphics.Sprite;
import graphics.Text;
import main.Calc;
import main.Entity;
import main.Scene;

public class Door extends Entity {
	public int trX, trY, tpX, tpY;
	public Sprite door, hole;
	public boolean opened, action, player, lastError;
	public String color;
	
	public int phase, timer, id, normalId;
	
	//basic door
	public Door(double x, double y, int rX, int rY, int pX, int pY) {
		super(x, y);
		setCollisionGroup(Global.DOOR);
		addCollisionGroup(Global.DEACTIVATEME);
		addCollisionGroup(Global.INTERACTABLE);
		setDepth(7);
		
		trX=rX;
		trY=rY;
		tpX=pX;
		tpY=pY;
		
		normalId = -9001;
		
		phase=0;
		timer=0;
		
		opened=false;
		action=false;
		lastError = false;
		
		orientation=Sprite.NORTHWEST;
		door= Global.sDOOR;
		hole= Global.sDOORHOLE;
		
		mask=door.mask;
		
		player = false;
		id = -1;
	}
	
	//player-placed door
	public Door(double x, double y, int rX, int rY, int pX, int pY, String col, int identity, int norm){
		super(x,y);
		setCollisionGroup(Global.DOOR);
		addCollisionGroup(Global.DEACTIVATEME);
		addCollisionGroup(Global.INTERACTABLE);
		setDepth(7);
		
		normalId = norm;
		
		trX=rX;
		trY=rY;
		tpX=pX;
		tpY=pY;
		id = identity;
		
		phase=0;
		timer=0;
		
		opened=false;
		action=false;
		
		color = col;
		
		orientation=Sprite.NORTHWEST;
		door=Sprite.get("sDoorPlayer");
		hole=Sprite.get("sDoorHole");
		
		mask=door.mask;
		
		player = true;
	}
	
	public void changeRoom(){
		Global.lastX=Global.roomX;
		Global.lastY=Global.roomY;
		
		Global.roomX=trX;
		Global.roomY=trY;
		
		Global.addMapDoor(Global.lastX, Global.lastY, Global.roomX, Global.roomY);
		
		Global.playerX=tpX;
		Global.playerY=tpY;
		Player.me.x=tpX;
		Player.me.y=tpY;
		
		if (!(Global.lastX == Global.roomX && Global.lastY == Global.roomY)){
			Global.preventAudioCrash();
			Root.changeRoom(Global.roomX,Global.roomY);
		}
	}
	
	public void step(){
		if (phase>0){
			timer-=1;
			if (timer<=0){
				if (phase==1){
					Sound.play("doorOpen");
					opened=true;
					phase=2;
					timer=21;
				}
				else if (phase==2){
					Sound.play("doorClose");
					opened=false;
					phase=3;
					timer=11;
				}
				else if (phase==3){
					changeRoom();
					phase = 0;
				}
				else if (phase==4){
					Sound.play("doorOpen");
					opened=true;
					phase=5;
					timer=11;
				}
				else if (phase==5){
					Player.me.visible=true;
					phase=6;
					timer=11;
				}
				else if (phase==6){
					Sound.play("doorClose");
					opened=false;
					phase=0;
					timer=0;
					Player.control=true;
					Player.me.myDoor=null;
					Player.doorPhase=0;
					Player.doorTime=0;
				}
			}
		}
		else{
			if (!Player.canDownInteract())
				lastError = false;
			
			action=false;
			if (Player.control && Player.me.vspeed == 0 && Player.cantDoor <= 0 && Player.me.y == y + 48 && Player.doorPhase==0 && phase==0 && Scene.collision(this,x,y,Global.PLAYER) && !Scene.collision(Player.me, Player.me.x, Player.me.y, Global.KEY) && !Scene.collision(this,x,y,Global.LOCKED) && Scene.collision(Player.me, Player.me.x, Player.me.y + 1, Global.SOLIDBLOCK)){
				action=true;
				Player.canChangeWeapon = false;
			}
			if (action && Player.canDownInteract() && Player.me.target == this){
				if (!player || (player && id != -1))
					enterDoor();
				else if (!lastError){
					Sound.errorPlay();
					lastError = true;
				}
			}
		}
	}
	
	public void enterDoor(){
		action=false;
		phase=1;
		timer=1;
		Player.enterDoor();
	}
	
	public void render(){
		hole.render((Integer)Calc.choose(0,1,2), orientation, x, y, 1, 1, 0, 1, 1, 1, 1);
		
		int xsc = 1;
		if (opened)
			xsc = -1;
		
		if (!player)
			door.render(0, orientation, x + Calc.rangedRandom(.4), y + Calc.rangedRandom(.4), xsc, 1, 0, 1, 1, 1, 1);
		else{
			door.render(1, orientation, x + Calc.rangedRandom(.4), y + Calc.rangedRandom(.4), xsc, 1, 0, 1, color);
			door.render(0, orientation, x + Calc.rangedRandom(.4), y + Calc.rangedRandom(.4), xsc, 1, 0, 1, "ffffff");
		}
		if (action && Player.control && !Global.paused){
			Player.me.target = this;
			if (!player || (player && id != -1))
				Global.drawDown(x+16, y-6);
		}
	}
	
	public void drawBubble(){
		if (player && id == -1 && action){
			double ax, ay;
			ax = Calc.rangedRandom(.75);
			ay = Calc.rangedRandom(.75);
			
			Sprite.get("sBubble").render((int)imageSingle, Sprite.CENTERED, x+16+ax, y-68+ay, 1, 1, 0, alpha, "#ffffff");
			Graphics.setColor("#48a0ff");
			Graphics.setAlpha(alpha);
			Text.setSpacing(1, 1.25);
			Text.setFont(Global.CRAZDFONT);
			Text.orientation=Text.CENTERED;
			Text.randomize(.01);
			Text.drawTextExt(x+24+ax, y-64+ay, "YOU NEED TO%PLACE ANOTHER%DOOR", .3, .3, 0);
			Text.randomize(0);
			Text.setSpacing(1, 1);
			Graphics.setAlpha(1);
		}
	}

}
