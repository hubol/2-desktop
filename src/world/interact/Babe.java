package world.interact;

import audio.Audio;
import graphics.Sprite;
import world.Fg;
import world.control.EV;
import world.control.Global;
import world.control.Shake;
import world.control.Sound;
import world.control.SoundLoader;
import world.control.SpriteLoader;
import world.event.BabeHatDrop;
import world.event.SmallMessage;
import world.particles.GlitchBlood;
import world.player.Player;
import main.Calc;
import main.Scene;

public class Babe extends BasicNpc{
	public int eye = 0, mouth = 0;
	public int leg = 0;
	public boolean speak = false;
	
	public int hurt = 0;
	
	public double legAnimate = 0;
	public Shake s = new Shake(.35), t = new Shake(.1);
	
	public double desH = 0;
	public boolean set = false;
	public boolean viz = true;
	
	public int conversation;
	
	public boolean can = true, dying = false;

	public Babe(double x, double y){
		super(x, y);
		
		new SoundLoader(false, "sBabeStep", "sBabeSpeak", "sBabeBlink", "sBabeOw");
		new SoundLoader("musBabeRain","musBabe1", "musBabe2", "musBabe3", "musBabe4");
		new SpriteLoader("sBabeBody_5", "sBabeLegs_3", "sBabeSymbol_2", "sBabeMask", "sBabeKey");
		
		orientation = Sprite.SOUTH;
		sprite = Sprite.get("sBabeMask");
		mask = sprite.mask;
		
		bubbleX = 0;
		bubbleY = -96;
		
		alarmInitialize(5);
		alarm[0] = 1 + (int)Calc.random(90);
		alarm[1] = 11;
		move();
		
		talkingDistance = 32;
		
		setDepth(3);
		refresh();
		
		if (Global.event[EV.BABEPROG] > 1){ //dode
			canSpeak = false;
			can = false;
			viz = false;
			setCollisionGroup(Global.DEACTIVATEME);
		}
	}
	
	public void refresh(){
		conversation = 0;
		if (Global.event[EV.BABEPROG] == 0)
			setConversation(new Message("i'm on a big search for those [boners]. [dino boners]. [three] of those. those [three dino boners]."), new Message("should that [boner] be unearthed, would you present that to me?"), new Message("that [weapon] is the reward!"));
		else if (canReceive()){
			conversation = toReceive() + 1;
			
			String num = "first";
			if (progress() == 1)
				num = "second";
			else if (progress() == 2)
				num = "third";
			
			String boner = "footprint";
			if (toReceive() == 1)
				boner = "femur";
			else if (toReceive() == 2)
				boner = "tooth";
			
			String apprec = "that [reward] is closer!";
			if (progress() == 2)
				apprec = "that [reward] is yours!";
			
			setConversation(new Message("aha! that ["+num+" boner]: the ["+boner+"]!"), new Message("much appreciation! "+apprec), new Message("hema"));
		}
		else if (Global.event[EV.BABEPROG] == 1){
			if (progress() == 0)
				setConversation(new Message((String)Calc.choose("keep your eyes peeled for any of those [three boners]!", "i'm a big fan of those [three dino boners]!", "seen any of those [three fossilized boners] lately?")));
			else if (progress() == 1)
				setConversation(new Message((String)Calc.choose("keep your eyes peeled for those [two boners]!", "any trace of those [two boners]?", "a [reward] depends on the [two dino boners]!")));
			else if (progress() == 2)
				setConversation(new Message("there remains that [single dino boner]!"));
		}
		else
			setConversation(new Message("if you are reading this this is a bug"));
	}
	
	public boolean canReceive(){
		for (int i=0; i<3; i++){
			if (Global.eventItem[3 + i] == 1)
				return true;
		}
		return false;
	}
	
	public int toReceive(){
		if (canReceive()){
			for (int i=0; i<3; i++){
				if (Global.eventItem[3 + i] == 1)
					return i;
			}
		}
		return -1;
	}
	
	public void sound(String s, double p){
		if (Fg.me.secretAlpha == 0)
			Sound.playPitched(s, p);
	}
	
	public void move(){
		desH = (Double)Player.me.choose(1.0,0.0,-1.0);
		alarm[2] = 1 + (int)Player.me.random(120);
	}
	
	public int progress(){
		int i = 0;
		for (int j=0; j<3; j++){
			if (Global.eventItem[3 + j] == 2)
				i ++;
		}
		return i;
	}
	
	public void step(){
		if (can)
			turning();
		
		if (!set && Audio.soundExists("musBabe4")){
			for (int i=1; i<5; i++){
				Audio.get("musBabe"+i).setLooping(true);
				Audio.get("musBabe"+i).setGain(0);
				Audio.fade("musBabe"+i, 0, 0);
			}
			for (int i=1; i<5; i++)
				Sound.play("musBabe"+i);
			
			Audio.get("musBabeRain").setLooping(true);
			Audio.get("musBabeRain").setGain(0);
			Audio.fade("musBabeRain", 0, 0);
			Sound.play("musBabeRain");
			
			set = true;
		}
		
		if (Audio.soundExists("musCute")){
			if (Fg.me.secretAlpha == 0){
				Audio.fade("musCute", .01, .05);
				if (set){
					Audio.fade("musBabeRain", 1.0, .069);
					for (int i=1; i<5; i++){
						if (progress() >= i - 1)
							Audio.fade("musBabe"+i, 1.0, .06);
					}
				}
			}
			else{
				Audio.fade("musCute", 1, .01);
				if (set){
					Audio.fade("musBabeRain", 0.0, .04);
					for (int i=1; i<5; i++)
							Audio.fade("musBabe"+i, 0.0, .07);
				}
			}
		}
		
		if (can){
			hspeed = Calc.approach(hspeed, desH, 4.8);
			super.step();
			animation();
			messageStep();
		}
	}
	
	public void animation(){
		if (!speak) //nospeak mouth
			mouth = 0;
		if (y != ystart) //jumpleg
			leg = 2;
		else{ //walk/stand leg
			if (desH == 0){
				leg = 0;
				legAnimate = 0;
			}
			else{
				xscale = Math.signum(desH);
				final int oldLeg = leg;
				legAnimate = (legAnimate + (Math.abs(desH) / 12.0)) % 2;
				leg = (int)legAnimate;
				if (leg == 0 && oldLeg == 1){
					sound("sBabeStep", .09);
				}
			}
		}
		
		if (hurt > 0){
			hurt -= 1;
			viz = !viz;
		}
		else
			viz = true;
	}
	
	public void speak(){
		if (!speak){
			sound("sBabeSpeak", .09);
			speak = true;
			mouth = 1;
			alarm[1] = 11;
		}
	}
	
	public void alarmEvent(int i){
		//eye control
		if (i == 0){
			if (eye == 0){
				eye = 1;
				sound("sBabeBlink", .1);
				alarm[0] = 3 + (int)Calc.random(3);
			}
			else{
				eye = 0;
				alarm[0] = 1 + (int)Calc.random(90);
			}
		}
		//mouth speak
		else if (i == 1){
			if (speak){
				if (mouth == 0)
					mouth = 1;
				else
					mouth = 0;
			}
			alarm[1] = 9;
		}
		//movement
		else if (i == 2){
			if (!dying)
				move();
		}
		//death animation initialize
		else if (i == 3){
			dying = true;
			Player.control = false;
			speak();
			Global.event[EV.BABEPROG] = 2;
			new SmallMessage(this, 0, -38, "THANK!", 10);
			alarm[4] = 19;
		}
		//explode
		else if (i == 4){
			can = false;
			viz = false;
			setCollisionGroup(Global.DEACTIVATEME);
			
			Fg.me.shakeTimer = 7;
			
			//Player.control = true;
			Global.explosionEffect(x, y - 15, .85);
			Global.explosionEffect(x, y - 15, 1.2);
			Sound.explodePlay();
			Sound.explodePlay();
			Sound.playPitched("sBombBlast",.05);
			Sound.playPitched("sBabeOw", .05);
			
			new BabeHatDrop(x,y);
			//TODO hat
		}
	}
	
	public void initConversationEvent(){
		desH = 0;
		hspeed = 0;
		refresh();
		alarm[2] = -1;
	}
	
	public void endConversationEvent(){
		if (Global.event[EV.BABEPROG] < 1)
			Global.event[EV.BABEPROG] = 1;
		alarm[2] = 1 + (int)Calc.random(90);
	}
	
	public void showMessage(int id){
		if (conversation == 0 || (conversation > 0 && id != 2)){
			speak();
			super.showMessage(id);
		}
		else if (conversation > 0 && id == 2){
			hideMessage(id);
			endConversation();
			if (progress() == 2){
				canSpeak = false;
				alarm[3] = 1;
			}
			Global.eventItemUse(3 + toReceive());
		}
	}
	
	public void hideMessage(int id){
		speak = false;
		super.hideMessage(id);
	}
	
	public void render(){
		if (viz && can){
			double x = this.x + s.x + Calc.rangedRandom(.08), y = this.y + s.y + Calc.rangedRandom(.08);
			double extY = 0, hatY = 0;
			if (mouth == 1){
				extY = -1;
				hatY -= 3;
			}
			if (leg > 0){
				y -= 4.5;
				hatY -= 1;
			}
			if (leg == 2)
				hatY -= 1.8;
			if (hurt > 0)
				hatY -= 10;
			
			Sprite.get("sBabeLegs").render(leg, Sprite.NORTH, x, y - 18 + extY, xscale, 1, 0, 1, 1, 1, 1);
			Sprite.get("sBabeBody").render(3 + mouth, Sprite.CENTERED, x, y - 17, xscale, 1, 0, 1, 1, 1, 1);
			Sprite.get("sBabeBody").render(1 + eye, Sprite.CENTERED, x, y - 17, xscale, 1, 0, 1, 1, 1, 1);
			Sprite.get("sBabeBody").render(0, Sprite.CENTERED, x + t.x, y - 17 + t.y + hatY, xscale, 1, 0, 1, 1, 1, 1);
		}
		
		if (Global.paused && Fg.me.secretAlpha == 0){
			Audio.get("musCute").setGain(.01);
			Audio.fade("musCute", .01, 1);
		}
	}
	
	/**checks if there is a solid block, upblock, or inviz turn block in the direction you are moving. if so, it flips you around. funky town*/
	public void turning(){
		double hSign = Math.signum(hspeed);
		
		if(Scene.collision(this, Math.round(x)+hspeed+hSign, y, Global.SOLIDBLOCK, Global.UPBLOCK, Global.TURNER))
		{
			double fa = hspeed;
			hspeed=0;
			x = Math.round(x);
			while(!Scene.collision(this, x+hSign, y, Global.SOLIDBLOCK, Global.UPBLOCK, Global.TURNER))
				x+=hSign;
			hspeed = fa*-1;
			desH = hspeed;
		}
	}
	
	public void gunDamage(){
		if (can)
			pain();
	}
	
	public void bombDamage(){
		if (can)
			pain();
	}
	
	public void pain(){
		if (hurt <= 0){
			sound("sBabeOw", .05);
			
			new SmallMessage(this, 0, -38, "OW!", 10);
			hurt = 20;
			
			for (int i=0; i<6; i++)
				new GlitchBlood(x + Calc.rangedRandom(8), y - 22);
			
			for (int i=0; i<4; i++)
				Global.squareParticle(x + Calc.rangedRandom(16), y - 22 + Calc.rangedRandom(16), 3 + (int)Calc.random(5), "FF1E56", 1.5 + Calc.random(5));
		}
	}

}
