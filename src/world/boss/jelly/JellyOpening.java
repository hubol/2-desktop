package world.boss.jelly;

import java.util.ArrayList;

import world.Overlay;
import world.boss.BossAnnounce;
import world.control.Global;
import world.control.Hud;
import world.control.Music;
import world.control.Sound;
import world.control.SoundLoader;
import world.interact.BasicNpc;
import world.interact.Message;
import world.player.Player;

import audio.Audio;

import main.Calc;
import main.Scene;

public class JellyOpening extends BasicNpc{
	public boolean spawning, did, messagePhase;
	public double dir, dist, funk;
	
	public double xx = 400, yy = 300, lastX = -1, lastY = -1;
	public boolean should, is;
	
	public int msgTime;
	
	public SoundLoader mine;
	public JellyMan jelly;
	
	public JellyOpening(double x, double y) {
		super(x, y);
		mine = new SoundLoader("sJellyCry");
		
		new SoundLoader("sJellyMessage","sBossTextJelly","sJellyCry1","sJellyCry2","sJellyGlassLand","sJellyGlassShatter","sJellyLandPop","sJellyBurstBegin","sJellyHurtCry","sJellyHitCry","sJellyWarp","sJellySplit","sJellyReturn","sJellyExclamation1","sJellyExclamation2","sJellyExclamation3");
		
		requireInteract = false;
		
		spawning = true;
		dir = 90;
		dist = yy + 32;
		
		should = true;
		is = false;
		did = false;
		
		messagePhase = false;
		msgTime = 0;
		
		funk = 0;
		
		movePlayer = false;
		
		setConversation(new Message("why doesn't anybody love me?"), new Message("wah! wah wah! wah wah wah! wah wah wah wah! wah wah wah wah wah!"), new Message("i'm a [big baby] who needs constant attention or else i get sad."), new Message("sniff! sniff!"), new Message("will you love me?"));
		
		bubbleY = -84;
		setCollisionGroup(Global.DEACTIVATEME);
	}
	
	public void showMessageEvent(int id){
		Sound.playPitched("sJellyMessage",.08);
	}
	
	public void hideMessageEvent(int id){
		jelly.vspeed = 0;
		lastX = jelly.x;
		lastY = jelly.y;
		
		if (id == 0){
			jelly.goTo(176, 256);
			jelly.crying = true;
		}
		else if (id == 1){
			jelly.goTo(432, 240);
		}
		else if (id == 2){
			jelly.goTo(320, 288);
			jelly.crying = false;
			jelly.imageSingle = 1;
		}
		else if (id == 3){
			jelly.goTo(192, 224);
		}
		else if (id == 4){
			jelly.goTo(464, 272);
		}
		
		Sound.playPitched("sJellyWarp",.05);
		
		jelly.jellyLine(lastX, lastY, jelly.x, jelly.y);
		
		for (int i=0; i<6; i++){
			JellyBubble j = new JellyBubble(jelly.x + Calc.rangedRandom(8), jelly.y + Calc.rangedRandom(8),8);
			j.life /= 2;
			j = new JellyBubble(jelly.x + Calc.rangedRandom(8), jelly.y + Calc.rangedRandom(8),6);
			j.life /= 2;
			j = new JellyBubble(jelly.x + Calc.rangedRandom(8), jelly.y + Calc.rangedRandom(8),4);
			j.life /= 2;
			j = new JellyBubble(jelly.x + Calc.rangedRandom(8), jelly.y + Calc.rangedRandom(8),2);
			j.life /= 2;
			new JellyBubbleSmall(jelly.x + Calc.rangedRandom(4), jelly.y + Calc.rangedRandom(4),6);
			new JellyBubbleSmall(jelly.x + Calc.rangedRandom(4), jelly.y + Calc.rangedRandom(4),5);
			new JellyBubbleSmall(jelly.x + Calc.rangedRandom(4), jelly.y + Calc.rangedRandom(4),4);
		}
		
		x = jelly.x;
		y = jelly.y;
		
		if (id == 4)
			endConversation();
	}
	
	public void endConversationEvent(){
		Music.changeMusic("musBoss01");
		Global.addTweet("ok hella creepy jelly boy");
		Audio.get("sJellyCry").stop();
		mine.destroy();
		
		jelly.active = true;
		jelly.phase = 1;
		jelly.goTo(jelly.x, jelly.y);
		jelly.vspeed = 0;
		
		new BossAnnounce("JELLYBOY","FF8B10","sBossTextJelly");
	}
	
	public void step(){
		dir += 4;
		dist -= .06125;
		dist = Calc.approach(dist, 0, 110);
		dist -= .06125;
		dist -= funk;
		
		funk += .00125;
		
		//my god this is the biggest piece of shit ive ever written
		if (spawning){
			Hud.hideAll();
			
			whistlePitch();
			JellyRender.me.intensity += .02;
			JellyRender.me.intensity *= 1.01;
			double xxx = xx + Calc.dirX(dist, dir);
			new JellyBubble(xxx, yy + Calc.dirY(dist, dir), (232-dist)/76);
			Player.me.face(xxx > Player.me.x);
			
			if (dist < 17){
				JellyRender.me.intensity += .5;
				JellyRender.me.visible = !JellyRender.me.visible;
				if (!JellyRender.me.visible)
					Sound.playPitched("sJellyGlitch",.2);
			}
			if (dist < 6){
				should = false;
				JellyRender.me.visible = false;
			}
			if (dist < 5 && !did){
				Audio.get("sJellyCry").setLooping(true);
				mine.playOnceLoaded("sJellyCry");
				Global.addTweet("crying jelly boy alert alert");
				did = true;
				Music.changeMusic("musThisJelly");
			}
			if (dist < 3){
				messagePhase = true;
				msgTime = 0;
				
				JellyRender.me.visible = true;
				spawning = false;
				JellyRender.me.intensity = 1;
				ArrayList<JellyBubble> list = Scene.getEntityList(JellyBubble.class);
				for (int i=0; i<list.size(); i++)
					list.get(i).destroy();
				for (int i=0; i<12; i++)
					new JellyBubble(xx, yy, 8);
				for (int i=0; i<12; i++)
					new JellyBubble(xx, yy, 16);

				jelly = new JellyMan(xx, yy);
				should = false;
				
				Overlay.fadeOut(1, 1, 1, 1, 5);
				
				x = xx;
				y = yy;
			}
		}
		else if (messagePhase){
			msgTime += 1;
			if (msgTime == 5)
				initConversation();
			
			messageStep();
		}
		
		
	}
	
	public void whistlePitch(){
		Audio.get("sJellyWhistle").setPitch(1 + ((Calc.dirY(dist, dir)/((yy + 32)*8))));
	}
	
	public void render(){
		if (!stepActive)
			should = false;
		
		if (is!=should){
			if (should){
				whistlePitch();
				Audio.get("sJellyWhistle").setLooping(true);
				Sound.play("sJellyWhistle");
			}
			else
				Audio.get("sJellyWhistle").stop();
			is = should;
		}
	}

}
