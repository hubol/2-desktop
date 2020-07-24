package world.interact;

import world.control.EV;
import world.control.Global;
import world.control.Sound;
import world.control.SoundLoader;
import world.control.SpriteLoader;
import world.minigame.ChopControl;
import world.player.Player;
import main.Calc;
import main.Scene;
import graphics.Sprite;

public class BigTree extends BasicNpc{
	public boolean dead, animate;
	public double hurt, hurtX;

	public BigTree(double x, double y) {
		super(x, y);
		
		new SpriteLoader("sTreeMask","sTree_5","sTreeChopped","sTreeChoppedMask");
		
		orientation = Sprite.SOUTH;
		sprite = Sprite.get("sTree");
		setDepth(3);
		
		movePlayer = false;
		bubbleX = 0;
		bubbleY = -140;
		
		mask = Sprite.get("sTreeMask").mask;
		
		imageSingle = 0;
		addCollisionGroup(Global.SOLIDBLOCK);
		
		dead = false;
		
		animate = false;
		
		hurt = 0;
		hurtX = 0;
		
		if (Global.event[EV.TREE_PROG] > 1)
			kill();
		else if (Global.event[EV.TREE_PROG] == 0)
			setConversation(new Message("the life of an [obstacle]! it's-a-me!"), new Message("an [axe saleswoman]! find her! [circumvent me]! please and thank you!"));
		else
			setConversation(new Message("is that an [axe] that you have there!!!"), new Message("hooray!!!"), new Message("shit"));
	
		if (!dead)
			new SoundLoader("sTreeWhine", "sChopBells");
	}
	
	public void gunDamage(){
		hurt();
	}
	
	public void bombDamage(){
		hurt();
	}
	
	public void hurt(){
		//nvm
	}
	
	public void kill(){
		Global.event[EV.TREE_PROG] = 2;
		
		setCollisionGroup(Global.SOLIDBLOCK);
		mask = Sprite.get("sTreeChoppedMask").mask;
		sprite = Sprite.get("sTreeChopped");
		dead = true;
	}
	
	public void showMessageEvent(int id){
		animate = true;
		imageSingle = 1;
		
		if (id == 0)
			Sound.playPitched("sTreeWhine",.2);
		else if (id == 2){
			showBubble = false;
			animate = false;
			
			new ChopControl(this);
			//TODO minigame
		}
		//whining chirp sound??
	}
	
	public void hideMessageEvent(int id){
		animate = false;
	}
	
	public void step(){
		if (!dead){
			if (animate){
				imageSingle += .16;
				if (imageSingle >= 2)
					imageSingle -= 2;
			}
			else
				imageSingle = 0;
			
			
			hurt -= 1;
			hurtX = Calc.approach(hurtX, 0, 7);
			if (hurt > 0){
				visible = !visible;
				imageSingle = 1;
			}
			else{
				visible = true;
				if (hurt == 0)
					imageSingle = 0;
			}
			
			
			messageStep();
			
			if (requireInteract && hurt <= 0){
				speakTimer -= 1;
				
				if (Scene.collision(this, x - 4, y, Global.PLAYER) && Player.control && !Player.me.cutMove && speakTimer <= 0 && canSpeak){
					interact = true;
					if (Player.canDownInteract()){
						initConversation();
					}
				}
				
				if (interact)
					Global.drawDown(x + bubbleX, y + bubbleY + 48);
			}
		}
	}
	
	public void render(){
		if (!dead){
			double drawImage = imageSingle;
			if ((int)imageSingle == 0)
				drawImage = (Double)Calc.choose(0.0, 4.0);
			
			sprite.render(2, orientation, x + Calc.rangedRandom(.5), y + Calc.rangedRandom(.5), 1, 1, 0, 1, 1, 1, 1);
			sprite.render(drawImage, orientation, x + Calc.rangedRandom(.5) + hurtX, y + Calc.rangedRandom(.5), 1, 1, 0, 1, 1, 1, 1);
			sprite.render(3, orientation, x + Calc.rangedRandom(.5) + hurtX, y + Calc.rangedRandom(.5) + 32, 1, 1, 0, 1, 1, 1, 1);
		}
		else{
			sprite.render(0, orientation, x + Calc.rangedRandom(.5), y + Calc.rangedRandom(.5), 1, 1, 0, 1, 1, 1, 1);
		}
	}

}
