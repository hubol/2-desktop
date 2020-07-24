package world.gameplay;

import main.Scene;
import audio.Audio;
import world.control.Global;
import world.control.Hud;
import world.control.Sound;
import world.interact.BasicNpc;
import world.interact.Message;
import world.player.Player;

public class BedMessage extends BasicNpc{
	public Bed mother;

	public BedMessage(double x, double y, Bed mom) {
		super(x, y);
		
		Sound.playPitched("sDreamMessage",.07);
		Audio.get("sDreamWind").setGain(0);
		Audio.fade("sDreamWind", .1, .005);
		Sound.play("sDreamWind");
		Audio.get("sDreamWind").setLooping(true);
		Audio.get("sDreamWind").setPitch(1);
		
		visible = false;
		object = true;
		movePlayer = false;
		
		bubbleY = -16;
		
		mother = mom;
		
		String s = "enter%["+Global.currentArea+" dream]?";
		if (Global.room(5, 7))
			s = "enter%[the meadow]?";
		
		setConversation(new Message(s,1,2));
		initConversation();
	}
	
	public void step(){
		messageStep();
	}
	
	public void hideMessageEvent(int a){
		endConversation();
		
		System.out.println(nextMessage);
		
		if (input){
			Player.me.fallApart(true);
			if (Scene.console)
				System.out.println("go to dream???");
			Hud.hideAll();
			mother.timerThingB = 30;
		}
		else{
			Audio.fade("sDreamWind", 0, .2);
			
			Player.me.fallApart(false);
			//Player.me.returnPiecesInstant();
			if (Scene.console)
				System.out.println("just a good ol save");
			mother.timerThing = 15;
		}
		
		//mother.save();
		
		Player.control = false;
		
		destroy();
	}
	
	public void render(){
		
	}

}
