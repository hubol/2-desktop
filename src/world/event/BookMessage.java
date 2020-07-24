package world.event;

import world.control.Sound;
import world.control.SpriteLoader;
import world.interact.BasicNpc;
import world.interact.Message;
import world.player.Player;

public class BookMessage extends BasicNpc{
	public Book mother;

	public BookMessage(double x, double y, Book mom) {
		super(x, y);
		
		Sound.playPitched("sReadMessage",.07);
		
		visible = false;
		object = true;
		movePlayer = false;
		
		bubbleY = -64;
		
		mother = mom;
		
		setConversation(new Message(mom.messageText,1,2));
		initConversation();
	}
	
	public void step(){
		messageStep();
	}
	
	public void hideMessageEvent(int a){
		endConversation();
		
		if (input){
			//System.out.println("open book");
			mother.timer = 30;
			Player.control = false;
			
			String[] s = new String[mother.spriteNames.size()];
			for (int i=0; i<mother.spriteNames.size(); i++)
				s[i] = mother.spriteNames.get(i);
			
			new ReadBook(new SpriteLoader(true, mother.extension, s));
		}
		else{
			//System.out.println("do not open book");
			mother.timer = 15;
			Player.control = true;
		}
		
		destroy();
	}
	
	public void render(){
		
	}

}
