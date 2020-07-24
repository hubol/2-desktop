package world.event;

import audio.Audio;
import graphics.Text;
import main.Calc;
import world.Fg;
import world.boss.jelly.JellyBubble;
import world.boss.jelly.JellyBubbleUp;
import world.boss.jelly.JellyRenderSpecial;
import world.control.Global;
import world.control.Hud;
import world.control.Music;
import world.control.Sound;
import world.control.SoundLoader;
import world.gameplay.TextSign;
import world.interact.BasicNpc;
import world.interact.Message;
import world.player.Player;

public class JellySalesmanControl extends BasicNpc{
	public boolean purchased, spoken;
	public int phase, price;
	public int timer, count;
	
	public int cutPhase, cutTimer;
	public SoundLoader sound;
	public boolean quake;
	
	public static JellySalesmanControl me;

	public JellySalesmanControl(double x, double y) {
		super(x, y);
		purchased = false;
		spoken = false;
		
		me = this;
		
		cutPhase = -1;
		cutTimer = -1;
		
		quake = false;
		
		new JellyRenderSpecial(0, 0);
		
		sound = new SoundLoader("sJellySalesmanEh","sJellySalesmanOh","sSkullQuake", "sJellySalesmanBell", "sJellyReturn");
		
		if (Global.gotDoors)
			purchased = true;
		
		timer = -1;
		count = 0;
		
		price = 185;
		resetConvo();
		
		bubbleY = -186;
		movePlayer = false;
		
		if (Global.gotMapDoor){
			new TextSign(x - 16, y - 16, "SOLD OUT.", "crazdFont", "#48a0ff");
			destroy();
		}
	}
	
	public void destroy(){
		me = null;
		super.destroy();
	}
	
	public void roomDestroy(){
		destroy();
	}
	
	public void finish(){
		cutPhase = -1;
		cutTimer = -1;
		spoken = true;
		endConversation();
		resetConvo();
	}
	
	public void step(){
		if (!purchased && currentMessage == 0 && active)
			Hud.showMoney();
		
		messageStep();
		
		count += 1;
		
		/*if (purchased && !Global.gotDoors && !Scene.instanceExists(Chest.class)){
			purchased = false;
			resetConvo();
		}*/
		
		if (cutPhase > -1){
			cutTimer -= 1;
			
			if (cutPhase == 0){
				Player.control = false;
				purchased = true;
				
				JellySalesman.me.img = 2;
				
				if (count % 2 == 0){
					Sound.popPlay();
				}
				
				if (cutTimer > 8){
					for (int i=0; i<4; i++)
						new JellyBubbleUp(272 + Calc.rangedRandom(32), 352 + Calc.random(12));
				}
				
				if (!quake){
					quake = true;
					Audio.get("sSkullQuake").setGain(1.0);
					Audio.fade("sSkullQuake",1,1);
					Audio.get("sSkullQuake").setLooping(true);
					Sound.play("sSkullQuake");
				}
				Fg.me.shakeTimer = 5;
			}
			
			if (cutPhase == 1){
				JellySalesman.me.img = 2;
			}
			
			if (cutTimer == 0){
				cutTimer = -1;
				if (cutPhase == 0){
					
					JellySalesman.me.img = -1;
					cutPhase = -1;
					quake = false;
					Fg.me.shakeTimer = 5;
					Audio.fade("sSkullQuake",0,.1);
					
					if (Global.gotMapDoor){
						cutPhase = 3;
						cutTimer = 30;
					}
					else{
						Player.control = true;
					}
				}
				else if (cutPhase == 3){
					double a = JellySalesman.me.x, b = JellySalesman.me.y;
					for (int i=0; i<8; i++){
						new JellyBubble(a + Calc.rangedRandom(16), b + Calc.rangedRandom(32), 3);
						new JellyBubble(a + Calc.rangedRandom(16), b + Calc.rangedRandom(32), 7);
						new JellyBubble(a + Calc.rangedRandom(16), b + Calc.rangedRandom(32), 12);
						new JellyBubble(a + Calc.rangedRandom(16), b + Calc.rangedRandom(32), 18);
					}
					
					Sound.playPitched("sBombBlast",.05);
					Sound.explodePlay();
					Sound.explodePlay();
					
					Global.jellyExplosionEffect(a, b, 1.1, false);
					Global.jellyExplosionEffect(a, b, 1.3, false);
					
					TextSign i = new TextSign(x - 16, y + 16, "SOLD OUT.", "crazdFont", "#48a0ff");
					i.moveUp = -32;
					
					JellySalesman.me.destroy();
					
					cutPhase = 4;
					cutTimer = 30;
					//TODO giggling sound
				}
				else if (cutPhase == 4){
					Player.control = true;
					Global.roomMusic = "musSoldOut";
					Music.fadeMusic(Global.roomMusic, true);
					destroy();
				}
			}
		}
		
		timer -= 1;
		if (timer == 0){
			finish();
		}
		
		if ((phase == 1 && currentMessage == 2) || (phase == 2 && currentMessage == 0)){
			Hud.showMoney();
		}
	}
	
	public void jellyBurst(){
		if (cutPhase == 1){
			Audio.fade(Global.roomMusic, 1, .05);
			//TODO poppin sound
			
			Sound.playPitched("sHammerGet", .05);
			Sound.playPitched("sJellyReturn",.05);
			cutPhase = 2;
			cutTimer = 45;
			double mult = (360.0 / 18.0);
			for (int i=0; i<18; i++){
				JellyBubble j = new JellyBubble(Player.me.x + Calc.dirX(23 + Calc.random(2), i * mult), Player.me.y + Calc.dirY(23 + Calc.random(2), i * mult), 16);
				j.vis = true;
				j = new JellyBubble(Player.me.x + Calc.dirX(23 + Calc.random(2), i * mult), Player.me.y + Calc.dirY(23 + Calc.random(2), i * mult), 12);
				j.vis = true;
				j = new JellyBubble(Player.me.x + Calc.dirX(23 + Calc.random(2), i * mult), Player.me.y + Calc.dirY(23 + Calc.random(2), i * mult), 8);
				j.vis = true;
				j = new JellyBubble(Player.me.x + Calc.dirX(23 + Calc.random(2), i * mult), Player.me.y + Calc.dirY(23 + Calc.random(2), i * mult), 4);
				j.vis = true;
			}
		}
	}
	
	public void showMessageEvent(int id){
		if ((phase >= 0 && phase <= 2 && id == 0) || (phase == 0 && id == 2) || (phase == 1 && id == 2)){
			Sound.playPitched("sJellySalesmanEh",.07);
		}
		
		if ((phase == 0 && id == 4)||(phase == 1 && id == 3)||(phase == 2 && id == 1)){
			if (Global.money >= price){
				purchased = true;
				Global.doorSetsPurchased += 1;
				
				Global.money -= price;
				showTimer = 4000;
				showBubble = false;
				
				//if (price == 0){
					timer = 111;
					new Chest(272, 400, 2);
					cutPhase = 0;
					cutTimer = 96;
					count = -1;
					//TODO purchase hammer shit
				/*}
				else{
					//TODO quick refill
					cutPhase = 1;
					cutTimer = 10000;
					showTimer = 40000;
					showBubble = false;
					new JellyBurst(this);
					Audio.fade(Global.roomMusic, .5, .05);
				}*/
			}
			else{
				Sound.playPitched("sJellySalesmanOh",.07);
				phase = 4;
				currentMessage = 0;
				clearConversation();
				if (!Global.heroMode)
					setConversation(new Message((String)Calc.choose("bro, that is not enough monies.","girl, where are the gems?","where the gems at, baby?","i require more money!","i demand more gems!")));
				else
					setConversation(new Message((String)Calc.choose("miss, that is not enough monies.","girl, where are the gems?","where the gems at, baby?","i require more money!","i demand more gems!")));
				text = Text.widthLimit(conversation.get(0).text, 18);
			}
		}
	}
	
	public void hideMessageEvent(int id){
		//end of conversation
		if ((phase == 0 && id == 3)||(phase == 1 && id == 4)||(phase == 2 && id == 2)||phase == 3||phase == 4){
			finish();
		}
	}
	
	public void initConversationEvent(){
		resetConvo();
	}
	
	public void resetConvo(){
		phase = -1;
		clearConversation();

		phase = 1;
		if (!purchased)
			setConversation(new Message("hi i have a [cool thing] for you!! would you like to buy it for [$"+price+"]?!", 3, 4), new Message("yay!!! it's a fabulous [map upgrade]! it lets you see a pretty [highway of doors]!"), new Message("isn't that a steal for [$"+price+"]?", 3, 4), new Message("dicks"), new Message((String)Calc.choose("alright.","whatever you say, girl.","whatever you say.","'scool.")));
		else
			setConversation(new Message((String)Calc.choose("thank you for your purchase!","thanks for your purchase!","thanks girl!","enjoy!")));
	}
	
	public void render(){
		
	}

}
