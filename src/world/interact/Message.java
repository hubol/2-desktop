package world.interact;

public class Message {
	public String text;
	public boolean question;
	public int yesId, noId;

	public Message(String s){
		text = s;
		question = false;
		yesId = -1;
		noId = -1;
	}
	
	public Message(String s, int y, int n){
		text = s;
		question = true;
		yesId = y;
		noId = n;
	}
}
