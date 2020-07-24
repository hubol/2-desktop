package world.gameplay;

import world.control.Global;
import world.control.MapIconInfluence;
import world.player.Player;
import graphics.Sprite;
import main.Calc;
import main.Scene;

public class DropKey extends MapIconInfluence{
	public int id, time;
	public boolean action;
	public String color;

	public DropKey(double x, double y, int identity, String c) {
		super(x, y);
		id = identity;
		setColor(c);
		color=c;
		
		setDepth(7);
		
		orientation=Sprite.NORTHWEST;
		
		action=false;
		sprite=Sprite.get("sKey");
		
		/*if (Scene.collision(this, x, y, Global.KEY)){
			visible = false;
			stepActive = false;
			destroy();
		}*/
		
		setCollisionGroup(Global.KEY, Global.INTERACTABLE);
		time=10;
	}
	
	public void step(){
		action=false;
		if (Scene.collision(this,x,y,Global.PLAYER) && Player.me.vspeed == 0 && Player.me.y == y + 16 && time<=0 && Player.downTime <= 0 && !Global.paused){
			//System.out.println(y + ", " + Player.me.y);
			action=true;
			Player.canChangeWeapon = false;
		}
		
		if (action && Player.canDownInteract()){
			pickUp();
		}
		
		time-=1;
		if (time<0)
			time=0;
	}
	
	public void pickUp(){
		super.destroy();
		Global.clearDropEntry(id);
		Global.pickupKey(color,x,y);
		Player.me.pickupKey(x+16, y+16);
		Player.downTime = 10;
		Player.cantDoor = 5;
	}
	
	public void refreshIcon(){
		if (!isDestroyed())
			Global.setIconMap(Global.roomX, Global.roomY, 2);
	}
	
	public void render(){
		double addX=Calc.rangedRandom(.45), addY=Calc.rangedRandom(.45);
		
		sprite.render(0,orientation,x+addX+Calc.rangedRandom(.325),y+addY+Calc.rangedRandom(.325),1,1,angle,1,1,1,1);
		sprite.render(1,orientation,x+addX+Calc.rangedRandom(.325),y+addY+Calc.rangedRandom(.325),1,1,angle,1,colR,colG,colB);
		
		if (action && Player.control)
			Global.drawDown(x+16, y-6);
	}

}
