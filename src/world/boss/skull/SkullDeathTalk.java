package world.boss.skull;

import world.control.Global;
import world.control.Sound;
import world.interact.BasicNpc;
import world.interact.Message;

public class SkullDeathTalk extends BasicNpc{
	public Skull mother;

	public SkullDeathTalk(double x, double y, Skull mom) {
		super(x, y);
		mother = mom;
		
		visible = false;
		
		bubbleY = -70;
		setConversation(new Message("is this the part where we shake hands?"));
		movePlayer = false;
		requireInteract = false;
		talkingDistance = 420;
		
		initConversation();
		Sound.play("sSkullSigh");
	}
	
	public void step(){
		messageStep();
	}
	
	public void endConversation(){
		endConversationEvent();
		
		if (Global.glassesEnabled)
			Sound.playPitched("sGlassesOff",.05);
		Global.glassesEnabled = false;
		
		nextMessage = -1;
		currentMessage = -1;
		showBubble = false;
		active = false;
		text = "";

		speakTimer = 10;
		
		mother.phase = 420;
		mother.timer = 15;
		destroy();
	}

}
