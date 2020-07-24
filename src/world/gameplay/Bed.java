package world.gameplay;

import java.io.File;
import java.util.HashMap;

import audio.Audio;
import world.Overlay;
import world.control.Global;
import world.control.Hud;
//import world.control.IO;
import world.control.MapIconInfluence;
//import world.control.Recorder;
import world.control.Sound;
//import world.control.SpriteLoader;
//import world.dream.DreamShatter;
import world.event.DreamWarp;
import world.particles.HeartParticles;
import world.particles.WackyText;
import world.player.Player;
import graphics.Sprite;
import main.Calc;
import main.Main;
import main.Scene;

public class Bed extends MapIconInfluence {
	public Sprite bed, sheet;
	public int sImg, timer, timerThing, timerThingB, timerThingC, create, explode;
	public boolean show, capable;
	
	public double intensity, flashTimer, bluhTimer;
	public boolean cleared;
	
	public boolean fade = false;
	
	public void setSheet(){
		int i = (int)Calc.random(sheet.textures.length);
		
		if (!Global.dream){
			HashMap<String, Integer> s = new HashMap<String, Integer>();
			s.put("BEDROOM", 0);
			s.put("FIELD", 1);
			s.put("SUNLIGHT", 2);
			s.put("SHRINE", 3);
			s.put("FINAL", 4);
			s.put("BRICK", 5);
			s.put("DOT", 6);
			s.put("PLANT", 7);
			s.put("STONE", 8);
			s.put("RAIN", 9);
			s.put("BATH", 10);
			s.put("KISS", 11);
			s.put("CAVE", 12);
			
			if (s.containsKey(Global.currentArea))
				i = s.get(Global.currentArea);
			else
				i = (int)Calc.random(sheet.textures.length);
		}
		else
			i = Global.currentDream;
		
		sImg=i;
		
		if (Global.room(29, 0))
			sImg = 4;
	}

	public Bed(double x, double y) {
		super(x, y);
		
		intensity = 1.0;
		bluhTimer = 0;
		resetFlash();
		
		orientation = Sprite.SOUTH;
		bed = Sprite.get("sGameBed");
		sheet = Sprite.get("sBedSheets");
		mask = Sprite.get("sBedMask").mask;
		setCollisionGroup(Global.SOLIDBLOCK);
		addCollisionGroup(Global.BED);
		addCollisionGroup(Global.INTERACTABLE, Global.DEACTIVATEME);
		
		show = false;
		timer = 0;
		
		capable = true;
		timerThing = -1;
		timerThingB = -1;
		timerThingC = -1;
		create = -1;
		explode = -1;
		
		setDepth(-25);
		
		/*if (Global.dream)
			new SpriteLoader("sDreamShatter_2");*/
		
		setSheet();
	}
	
	public void resetFlash(){
		flashTimer = Calc.random(14);
		if (cleared){
			bluhTimer = Math.min(flashTimer - 1, Calc.random(5));
			intensity = 3.0 + Calc.random(12);
			//Global.sparkEffect(x + Calc.rangedRandom(32), y - Calc.random(32), Calc.random(.25));
			if (!Global.room(29, 0))
				Sound.playPitched("sJellyGlitch",1.0);
		}
	}
	
	public void step(){
		if (visible){
			flashTimer -= 1;
			bluhTimer -= 1;
			if (cleared && bluhTimer <= 0)
				intensity = 2.0;
			if (flashTimer <= 0)
				resetFlash();
		}
		
		if (timer <= 0)
			capable = true;
		
		show = false;
		if (Math.round(Player.me.y)==y-48 && Player.me.vspeed==0 && Scene.collision(this, x, y-1, Global.PLAYER) && timer<=0 && capable && timerThing < 0 && !Global.paused){
			show = true;
			Player.canChangeWeapon = false;
			
			Overlay.me.fadeIn = true;
		}
		
		create -= 1;
		if (create == 0)
			new DreamWarp(0,0,true);
		
		explode -= 1;
		if (explode == 0){
			//TDO clangk sound
			//new DreamShatter();
			
			Global.explosionEffect(x - 16, y - 16, .25);
			Global.explosionEffect(x + 16, y - 16, .25);
			Global.explosionEffect(x, y - 16, .5);
			Global.explosionEffect(x, y - 16, 1.5);
			Global.explosionEffect(x, y - 16, 1.1);
			Sound.play("sBombBlast");
			
			Audio.get("sDreamWind").stop();
			DreamWarp.me.destroy();
			
			visible = false;
		}
		
		setSheet();
		
		if (show){
			Global.drawDown(x, y-70);
			if (Player.canDownInteract()){
				capable = false;
				timer = 90000;
				
				if (!Global.dream){
					if (Global.dreamCleared[sImg] && !Global.room(5, 7)){
						Player.control = false;
						Player.me.hspeed = 0;
						Player.me.fallApart(false);
						
						timerThing = 15;
					}
					else
						new BedMessage(x, y - 112, this);
				}
				else{
					Hud.hideAll();
					
					Player.control = false;
					Player.me.fallApart(true);
					//TODO return to overworld stuff
					timerThing = 77;
					
					if (Global.dreamCleared[sImg]){
						timerThing += 60;
						explode = 85;
					}
					create = 30;
				}
			}
		}
		
		if (Scene.getEntityList(BedMessage.class).size() > 0 || timerThing > 0 || timerThingB > 0 || timerThingC > 0)
			Overlay.me.fadeIn = true;
		
		if (capable)
			timer-=1;
		
		timerThing -= 1;
		
		if ((timerThing >= 0 && timerThing <= 2 && Global.dream)||(timerThingC >= 0 && timerThingC <= 2))
			Overlay.fadeOut(0, 0, 0, 1, 62);//Overlay.fadeOut(0, 0.58203125, 0.80859375, 1, 60);
		
		if (timerThing == 0){
			if (Global.dream){
				Audio.get("sDreamWind").stop();
				Global.fromDream();
				System.out.println("from dream??");
			}
			else{
				save();
				Player.me.returnPieces();
				System.out.println("return pieces!!");
			}
			
			capable = true;
			timer = 150;
		}
		else if (timerThing > 0 && Global.dream)
			Hud.hideAll();
		
		if (!capable){
			timerThingB -= 1;
			if (timerThingB == 0){
				save();
				new DreamWarp(0,0,false);
				timerThingC = 47;
			}
			
			timerThingC -= 1;
			if (timerThingC == 0){
				Audio.get("sDreamWind").stop();
				if (!Global.room(5, 7))
					Global.toDream(16 + sImg);
				else
					Global.toDream(29); //TODO end
			}
		}
		
		if (fade && alpha > 0)
			alpha -= 1.0 / 475.0;
	}
	
	public void save(){
		File f = new File(Main.DIRECTORY, "res/data/crazd2fuckshitfile"+Global.saveFileNumber+".FUCK");
		if (f.exists())
			f.delete();
		
		for (int i=0; i<16; i++){
			new HeartParticles(Player.me.x,Player.me.y);
		}
		Global.squareParticle(Player.me.x, Player.me.y, 16, "#ff2879", 4);
		
		new WackyText(Player.me.x,Player.me.y - 24,"GAME SAVED");
		
		show = false;
		String s = "sSave"+(1+(int)Calc.random(4));
		Sound.play(s);
		
		Global.recharge();
		
		Hud.showAll();
		
		if (Global.totalSaves % 3 == 0){
			String t = "", n = (String)Calc.choose("save", "sleep");
			int route = (int)Calc.random(4);
			if (route == 0){
				if (Calc.random(1) > .5){
					if (Calc.random(1) > .5)
						t = "#";
					if (Calc.random(1) > .5)
						t += n + "flow";
					else
						t += n + " flow";
				}
				else
					t = n + (String)Calc.choose("","","!","!!!","!!!!!!");
			}
			else if (route == 1){
				t = (String)Calc.choose("got my ", "get my ", "gettin my ", "gettin my ", "gettin my ", "getting my ", "got my ");
				t += (String)Calc.choose("","motherfuckin ","motherfucking ","god damn ", "", "");
				t += n + " on";
			}
			else if (route == 2){
				t = (String)Calc.choose("im ","i'm ","i am ","is ", "i ");
				t += (String)Calc.choose("sleeping","make sleep","make sleeps","make sleepy town","making sleep","making sleeps", "making sleepy town", "asleep");
				t += (String)Calc.choose("","","","","!");
			}
			else if (route == 3){
				t = (String)Calc.choose("","","#");
				t += (String)Calc.choose("napflow","teamnap","naptown","dreamingaboutboys","ily","goodnight","nightynight");
			}
			
			Global.addTweet(t);
		}
		
		Global.totalSaves += 1;
		
		Global.playerX = Player.me.x;
		Global.playerY = Player.me.y;
		Global.saveGame();
		/*if (!IO.playback)
			Recorder.me.save();*/
		
		//TODO particles
		timer = 150;
	}
	
	public void endingExplosion(){
		Global.explosionEffect(x - 16, y - 16, .25);
		Global.explosionEffect(x + 16, y - 16, .25);
		Global.explosionEffect(x, y - 16, .5);
		Global.explosionEffect(x, y - 16, 1.5);
		Global.explosionEffect(x, y - 16, 1.1);
		Sound.play("sBombBlast");
		
		visible = false;
	}
	
	public void refreshIcon(){
		if (!isDestroyed() && !Global.dream)
			Global.setIconMap(Global.roomX, Global.roomY, 5);
	}
	
	public void render(){
		int o = orientation;
		sheet.render(sImg, o, x+Calc.rangedRandom(.75*intensity), y+Calc.rangedRandom(.75*intensity)+2, 1, 1, 0, alpha, "#ffffff");
		bed.render(2, o, x+Calc.rangedRandom(.75*intensity), y+Calc.rangedRandom(.75*intensity)+2, 1, 1, 0, alpha, "#ffffff");
		bed.render((Integer)Calc.choose(0,1), o, x+Calc.rangedRandom(.75*intensity), y+Calc.rangedRandom(.75*intensity)+2, 1, 1, 0, alpha, "#ffffff");
	}

}
