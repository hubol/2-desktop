package world.turkey;

import world.control.EV;
import world.control.Global;
import world.control.Hud;
import world.control.SpriteLoader;
import world.interact.Message;
import graphics.Sprite;

public class AxeSaleswoman extends Turkey{
	public int convo;

	public AxeSaleswoman(double x, double y) {
		super(x, y);
		
		new SpriteLoader("sAxeLadyFace_3", "sAxeLadyFeather");
		face = Sprite.get("sAxeLadyFace");
		feather = Sprite.get("sAxeLadyFeather");
		col = "B5F700";
		speech = "sAxeSaleswoman";
		speed = 1.0;
		
		initialize();
		
		setConvo();
		setYelps("hey!","watch it!","wtf!","yowzas!!");
	}
	
	public void step(){
		if (active && Global.event[EV.TREE_PROG] == 0 && currentMessage == 0)
			Hud.showMoney();
		
		super.step();
	}
	
	public void setConvo(){
		convo = Global.event[EV.TREE_PROG];
		
		if (Global.event[EV.TREE_PROG] == 0){
			if(Global.money >= 130)
				setConversation(new Message("such magnificence! is the purchase of an [axe] for [$130] in order?", 2, 1), new Message("she claims refusal of the [axe] offer, yet i await her imminent purchase!"), new Message("a successful purchase! truly a splendor!"));
			else{
				setConversation(new Message("such magnificence! is the purchase of an [axe] for [$130] in order?", 2, 1), new Message("she claims refusal of the [axe] offer, yet i await her imminent purchase!"), new Message("a gem counter does not meet the standard of [$130]! a travesty!"));
				convo = 3;
			}
		}
		else if (Global.event[EV.TREE_PROG] == 1)
			setConversation(new Message("an [axe] cuts the [tree]!"), new Message("...but alas, a [tree]'s whereabouts i lack knowledge of."));
		else
			setConversation(new Message("a successful use of a certain [axe] i see!"));
	}
	
	public void initConversationEvent(){
		super.initConversationEvent();
		setConvo();
	}
	
	public void hideMessageEvent(int id){
		if ((convo == 0 || convo == 3) && id == 1)
			endConversation();
	}
	
	public void showMessageEvent(int id){
		if (id == 2){
			if (convo == 0){
				Global.money -= 130;
				emotion = HAPPY;
			}
			else
				emotion = SAD;
		}
		else if ((convo == 1 || convo == 2) && id == 0)
			emotion = HAPPY;
		else if (convo == 1 && id == 1)
			emotion = SAD;
	}
	
	public void endConversationEvent(){
		super.endConversationEvent();
		
		if (currentMessage == 2 && convo == 0){
			Global.eventItemGet(0);
			Global.event[EV.TREE_PROG] = 1;
		}
		
		emotion = NEUTRAL;
		speakTimer = 10;
	}

}
