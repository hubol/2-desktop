package world.interact;

import graphics.Graphics;
import graphics.Sprite;
import graphics.Text;

import java.util.ArrayList;

import world.control.Global;
import world.control.IO;
import world.control.Sound;
import world.player.Player;

import main.Calc;
import main.Entity;
import main.Scene;

public class BasicNpc extends Entity {
	public double talkingDistance = 0, bubbleX, bubbleY;
	public boolean showBubble = false, active = false, object = false;
	public String text = "", color="#48a0ff", font="crazdFont";
	public ArrayList<Message> conversation;
	
	public boolean requireInteract = true;
	
	public int nextMessage = -1;
	public int currentMessage = -1;
	public int messageTimer = 0, showTimer = 0, nextTimer = 0, speakTimer = 0;
	public boolean input = true, await = false, interact = false, canSpeak = true, movePlayer = true, cancel = false;
	
	public double bubbleSpeed = 1.0/6.0, bubbleImage = Calc.random(3);

	public BasicNpc(double x, double y) {
		super(x, y);
		conversation = new ArrayList<Message>();
		
		setCollisionGroup(Global.INTERACTABLE, Global.DEACTIVATEME, Global.BASICNPC);
	}
	
	public void bombDamage(){
		
	}
	
	public void gunDamage(){
		
	}
	
	/**call in child step*/
	public void messageStep(){
		bubbleImage += bubbleSpeed;
		if (bubbleImage >= 3)
			bubbleImage -= 3;
		
		interact = false;
		
		if (active){
			showTimer -= 1;
			if ((((Calc.pointDistance(Player.me.x, Player.me.y, x, y) >= talkingDistance - 4.0) || !movePlayer) && nextMessage == 0 && currentMessage == -1 && !showBubble)||(currentMessage >= 0 && nextMessage >= 0 && showTimer == 0 && !showBubble))
				initMessage();
			
			if (showBubble && conversation.get(currentMessage).question){
				if (IO.checkFrameKey(Global.LEFT) || IO.checkFrameKey(Global.RIGHT)){
					input = !input;
					if (input)
						Sound.playPitched("sSelectUp");
					else
						Sound.playPitched("sSelectDown");
				}
			}
			
			messageTimer -= 1;
			if (showBubble && messageTimer <= 0 && (IO.checkFrameKey(Global.DOWN) || IO.checkFrameKey(Global.INTERACT))){
				pressMessage();
			}
		}
		else if (requireInteract){
			speakTimer -= 1;
			
			if (Scene.collision(this, x, y, Global.PLAYER) && Player.control && !Global.paused && !Player.me.cutMove && speakTimer <= 0 && canSpeak){
				interact = true;
				if (Player.canDownInteract()){
					initConversation();
				}
			}
			
			if (interact)
				Global.drawDown(x + bubbleX, y + bubbleY + 48);
		}
		
		if (active){
			if (movePlayer){
				Player.me.cutMove = true;
				
				xscale = 1;
				Player.me.cutRight = true;
				Player.me.cutToX = x + talkingDistance;
				
				if (Player.me.x < x){
					xscale = -1;
					Player.me.cutRight = false;
					Player.me.cutToX = x - talkingDistance;
				}
			}
		}
	}
	
	public void initMessage(){	
		currentMessage = nextMessage;
		if (conversation.get(currentMessage) != null){
			messageTimer = 5;
			input = true;
			showMessage(currentMessage);
		}
	}
	
	/**this shows a message/question/what have you*/
	public void showMessage(int id){
		showBubble = true;
		text = Text.widthLimit(conversation.get(id).text, 21);
		showMessageEvent(id);
	}
	
	/**this shows a message/question/what have you*/
	public void hideMessage(int id){
		hideMessageEvent(id);
		showBubble = false;
	}
	
	/**this occurs when the interact key is pressed with a message onscreen*/
	public void pressMessage(){
		if (conversation.get(currentMessage).question){
			Sound.playPitched("sConfirm", .025);
			nextMessage = conversation.get(currentMessage).noId;
			if (input)
				nextMessage = conversation.get(currentMessage).yesId;
		}
		else{
			Sound.playPitched("sMessageHide", .025);
			if (conversation.size() > currentMessage + 1){
				if (conversation.get(currentMessage + 1) != null)
					nextMessage = currentMessage + 1;
				else
					endConversation();
			}
			else
				endConversation();
		}
		
		hideMessage(currentMessage);
		
		showTimer = 5;
	}
	
	/**use this method to perform events at the showing of certain text boxes*/
	public void showMessageEvent(int id){
		
	}
	
	/**use this method to perform events at the hiding of certain text boxes*/
	public void hideMessageEvent(int id){
		
	}
	
	/**use this method to perform events at the start of a conversation, note that there is time between the start of a conversation and the first showBubble*/
	public void initConversationEvent(){
		
	}
	
	/**use this method to perform events at the end of a conversation*/
	public void endConversationEvent(){
		
	}
	
	/**wipe the conversation entirely!*/
	public void clearConversation(){
		conversation.clear();
	}
	
	/**reset the entire conversation to new input*/
	public void setConversation(Message... input){
		clearConversation();
		for (int i=0; i<input.length; i++){
			addToConversation(input[i]);
		}
	}
	
	/**add a new entry to the conversation*/
	public void addToConversation(Message input){
		conversation.add(input);
	}
	
	/**initiate conversation*/
	public void initConversation(){
		initConversationEvent();
		
		if (!cancel){
			Global.glassesEnabled = false;
			if (Global.gotGlasses && !object){
				Sound.playPitched("sGlassesOn",.05);
				Global.glassesEnabled = true;
			}
			
			text = "";
			active = true;
			nextMessage = 0;
			currentMessage = -1;
			
			Player.control = false;
				
			if (movePlayer){
				Player.me.cutMove = true;
				
				xscale = 1;
				Player.me.cutRight = true;
				Player.me.cutToX = x + talkingDistance;
				
				if (Player.me.x < x){
					xscale = -1;
					Player.me.cutRight = false;
					Player.me.cutToX = x - talkingDistance;
				}
			}
		}
	}
	
	/**end conversation*/
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
		Player.control = true;
		Player.me.cutMove = false;
		Player.me.downTimer = 4;
		
		speakTimer = 10;
	}
	
	/**draw teh bubbol*/
	public void drawBubble(){
		if (showBubble){
			double ax, ay;
			
			ax = Calc.rangedRandom(.75);
			ay = Calc.rangedRandom(.75);
			
			double xxx, yyy;
			xxx = x + bubbleX + ax;
			yyy = y + bubbleY + ay;
			
			double flip = 1.0, add = 0;
			if (yyy < 64){
				yyy += (bubbleY * 2) + 196;
				flip = -1.0;
				add = 1.0;
			}
			
			double qY = 0;
			/*if (conversation.get(currentMessage).question)
				qY = -8;*/
			
			Sprite.get("sBigBubble").render((int)bubbleImage, Sprite.CENTERED, xxx, yyy - 8, 1, flip, 0, alpha, "#ffffff");
			Graphics.setColor(color);
			Graphics.setAlpha(alpha);
			Text.setSpacing(1, 1.25);
			Text.setFont(font);
			Text.orientation=Text.CENTERED;
			Text.randomize(.01);
			Text.drawTextExt(xxx + 4, yyy + 4 + (8 * add) + qY - 8, text.toUpperCase(), .27, .29, 0, "FF2658");
			
			if (currentMessage > -1){
				if (conversation.get(currentMessage).question){
					Sprite s = Sprite.get("sBubbleAnswer");
					int hX = -80, hXs = -1, yU = -6, nU = 0;
					if (!input){
						nU = yU;
						yU = 0;
						hX *=-1;
						hXs *=-1;
					}
					int adtnl = 12;
					yU += adtnl; nU += adtnl;
					
					s.render((int)bubbleImage, Sprite.CENTERED, xxx-48, yyy+(48.0*flip)+yU+(112.0*add), 1, 1, 0, alpha, "#ffffff");
					s.render((int)bubbleImage, Sprite.CENTERED, xxx+48, yyy+(48.0*flip)+nU+(112.0*add), 1, 1, 0, alpha, "#ffffff");
					Text.drawTextExt(xxx-44, yyy+(56.0*flip)+yU+(128.0*add), "YES", .3, .3, 0);
					Text.drawTextExt(xxx+54, yyy+(56.0*flip)+nU+(128.0*add), "NO", .3, .3, 0);
					Sprite.get("sBubbleHand").render((int)bubbleImage, Sprite.CENTERED, xxx +hX, yyy+(42.0*flip)+(100.0*add) + 12, hXs, 1, 0, alpha, "#ffffff");
				}
			}
			
			Text.randomize(0);
			Text.setSpacing(1, 1);
			Graphics.setAlpha(1);
		}
	}

}
