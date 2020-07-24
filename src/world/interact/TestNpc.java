package world.interact;

import world.player.Player;
import graphics.Sprite;

public class TestNpc extends BasicNpc{

	public TestNpc(double x, double y) {
		super(x, y);
		orientation = Sprite.CENTERED;
		sprite = Sprite.get("sJar");
		mask = sprite.mask;
		imageSingle = 0;
		imageSpeed = 0;
		talkingDistance = 32;
		bubbleX = 0;
		bubbleY = -72;
		
		setConversation(new Message("hey what's%your name?", 1, 4), new Message("oh \"yes\"%that's a pretty%name"),new Message("piss off,%yes"),new Message("bye now"), new Message("nice name dude"));
		//setConversation(new Message("hi there my%name is hubol"), new Message("i like it in%my butte"), new Message("ok thanks"));
		
		adjustXscale();
	}
	
	public void adjustXscale(){
		if (Player.me.x > x)
			xscale = 1;
		else if (Player.me.x < x)
			xscale = -1;
	}
	
	public void hideMessageEvent(int id){
		if (id == 3 || id == 4)
			endConversation();
	}
	
	public void roomDestroy(){
		clearConversation();
		super.roomDestroy();
	}
	
	public void step(){
		messageStep();
	}

}
