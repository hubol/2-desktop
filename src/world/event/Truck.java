package world.event;

import main.Calc;
import graphics.Sprite;
import world.Fg;
import world.control.EV;
import world.control.Global;
import world.control.PseudoGenerator;
import world.control.Sound;
import world.control.SpriteLoader;
import world.interact.BasicNpc;
import world.interact.Message;
import world.player.Player;

public class Truck extends BasicNpc{
	public boolean opened, can;
	public PseudoGenerator myGen;
	public int at;
	public boolean[] launched;
	
	public static final double addX = -8;
	
	//public TruckButterfly[] butters;

	public Truck(double x, double y) {
		super(x - addX, y);
		at = 0;
		
		new SpriteLoader("sTruckMask", "sTruck_6");
		
		launched = new boolean[50];
		for (int i=0; i<50; i++)
			launched[i] = false;
		
		opened = (Global.eventItem[1] == 2);
		can = (Global.eventItem[1] != 2);

		if (Global.eventItem[1] == 0)
			setConversation(new Message("it's locked."));
		
		sprite = Sprite.get("sTruck");
		orientation = Sprite.SOUTH;
		mask = Sprite.get("sTruckMask").mask;
		
		myGen = new PseudoGenerator(1.19,1.15,-.18,401.2,115,1.4,420);
		
		bubbleX = addX;
		bubbleY = -102;
		
		/*if (!opened){
			butters = new TruckButterfly[50];
			for (int i=0; i<50; i++){
				butters[i] = new TruckButterfly(x + 10 + myGen.random(14), y - 39 + myGen.random(5), myGen.random(360), myGen.random(5));
			}
		}*/
		
		setCollisionGroup(Global.TRUCKWINDOW);
		setDepth(2);
		
		alarmInitialize(2);
	}
	
	public void alarmEvent(int i){
		if (i == 0){
			Sound.playPitched("sTruckEscape",.1);
			Fg.me.shakeTimer = 7;
			
			int f = (int)myGen.random(50);
			while (launched[f]){
				f += 1;
				if (f > 49)
					f = 0;
			}
			
			launched[f] = true;
			
			new ButterflyLeave(x + 9, y - 21, f);
			at += 1;
			if (at >= 50){
				alarm[1] = 30;
			}
			else
				alarm[0] = 2;
		}
		else if (i == 1){
			Global.eventItemUse(1);
		}
	}
	
	public void step(){
		if (can)
			messageStep();
		
		alarmStep();
	}
	
	public void initConversationEvent(){
		if (Global.eventItem[1] == 1){
			cancel = true;
			can = false;
			Player.control = false;
			Player.me.cutMove = true;
			Player.me.cutRight = false;
			Player.me.cutToX = 272;
			opened = true;
			Sound.play("sTruckUnlock");
			Global.event[EV.TRUCK_UNLOCKED] = 1;
			alarm[0] = 15;
		}
		else{
			Sound.playPitched("sTruckCant",.05);
		}
	}
	
	public void render(){
		if (opened){
			double xx = x + Calc.rangedRandom(.54) + addX, yy = y + Calc.rangedRandom(.54);
			sprite.render(4, orientation, xx, yy, 1, 1, 0, 1, 1, 1, 1);
			sprite.render(3, orientation, xx, yy, 1, 1, 0, .5, 1, 1, 1);
			sprite.render(5, orientation, xx, yy, 1, 1, 0, .5, 1, 1, 1);
		}
		else{
			double xx = Calc.rangedRandom(1.2) + addX, yy = Calc.rangedRandom(1.2);
			sprite.render(0, orientation, x + xx, y + yy, 1, 1, 0, .5, 1, 1, 1);
			sprite.render(2, orientation, x + xx, y + yy, 1, 1, 0, 1, 1, 1, 1);
			/*for (int i=0; i<50; i++)
				butters[i].pseudoRender(xx, yy);*/
			sprite.render(1, orientation, x + xx, y + yy, 1, 1, 0, 1, 1, 1, 1);
			sprite.render(0, orientation, x + xx, y + yy, 1, 1, 0, .5, 1, 1, 1);
		}
	}

}
