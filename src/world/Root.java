package world;

import graphics.Sprite;

import java.io.File;
import java.util.ArrayList;

import javax.media.opengl.GL2;

import world.control.Debug;
import world.control.EV;
import world.control.Global;
import world.control.Hud;
import world.control.IO;
import world.control.Music;
import world.control.Pause;
import world.control.Performer;
//import world.control.Recorder;
import world.control.SaveFile;
import world.control.Sound;
import world.control.SoundLoader;
import world.control.SpriteLoader;
import world.death.Death;
import world.event.BedScene;
import world.event.FuckOff;
import world.event.NewRain;
import world.event.Teleport;
import world.gameplay.JellyBlock;
import world.intro.IntroWait;
import world.menu.Menu;
import world.menu.MenuBg;
import world.menu.MenuMusic;
import world.player.Player;

import audio.Audio;

import main.Calc;
import main.Entity;
import main.Main;
import main.Scene;

public class Root extends Entity {
	public static String room;
	public static Root me;
	public static double amount=1.0;
	//background shit
	public static Sprite bg, fg;
	
	public static int musicWait = 4;
	public static String storedSong = "";
	
	public static boolean spawned = false;

	public Root(double x, double y) {
		super(x, y);
		
		Main.animator.setUpdateFPSFrames(16, null);
		
		setCollisionGroup(Global.CONTROLLER);
		setDepth(Integer.MAX_VALUE);
		GL2 gl = Scene.gl; 
		gl.glDisable(GL2.GL_CULL_FACE);
		gl.glEnable (GL2.GL_BLEND); 
        gl.glBlendFunc (GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
       
        persistent=true;
        
        if (!new File(Main.DIRECTORY, "res/data/tags.hema").exists())
        	SaveFile.tagInit();
        
        //new MusicController(0,0);
        changeRoom("intro"); //TODO
        //changeRoom("prep");
	}
	
	/**spawn controller objects when entering any room for the first time!!!!!!*/
	public static void controllers(){
		if (!spawned){
			new Global(0,0);
			
			if (Global.canDebug)
				new Debug(0,0);
			
			new Music(0,0);
			new Sound(0,0);
			
			new Bg(0,0);
			new Fg(0,0);
			
			new Hud(0,0);
			new Pause(0,0);
			
			new Overlay(0,0);
			
			spawned = true;
		}
	}
	
	public static void perform(int l){
		if (l != -1){
			//Recorder.me.destroy();
			IO.playback = true;
			new Performer(l);
		}
	}
	
	public static void changeRoom(String rm){
		ArrayList<Entity> list = Scene.getCollisionGroupList(Global.KILLATLEAVE);
		for (int i=0; i<list.size(); i++)
			list.get(i).destroy();
		
		if (Scene.console)
			System.out.println(rm + "???!?!?!?!?!?!?!?!?!?!?!!");
		
		//Main.animator.setUpdateFPSFrames(1, null);
		controllers();
		
		room=rm;
		
		Global.lastX=Global.roomX;
		Global.lastY=Global.roomY;
		
		Global.roomX=-1;
		Global.roomY=-1;
		
		Global.deactivate();
		
		killEveryone();
		if (rm.equals("intro")){
			new IntroWait();
		}
		else if (rm.equals("a")){
			Hud.me.currentMoney = 0;
			new FuckOff();
		}
		else if (rm.equals("b")){
			new FuckOff(Global.saveFileNumber);
		}
		else if (rm.equals("c")){
			new FuckOff(true);
		}
		else if (rm.equals("d")){
			new FuckOff("menu");
		}
		else if (rm.equals("prep")){ //prepare game!!!!
			
			Global.initVars(Global.storedSaveFile, Global.storedHeroine);
			
			//TODO!!!! this overrides gameplay and instead plays a .RUN file with the provided id!!!
			//!!!!!!!!!!!!!!!!!!!!!!!!!!!!TODO!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			perform(-1);
			//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!TODO: PERFORMER STUFF!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			
			new Player(96,368);
			changeRoom(Global.roomX,Global.roomY);
			Global.toDream(16);
			Overlay.fadeOut(0, 0, 0, 1, 60);
			Player.release();
			Global.saveGame();
			
			SaveFile.tagWrite(Global.storedSaveFile - 1, "BEDROOM DREAM - 0%");
		}
		else if (rm.equals("BedSceneA")){
			new BedScene(0,0);
		}
		else if (rm.equals("menutest")){
			new MenuMusic();
		}
		else if (rm.equals("menu")){
			//TODO menustuff
			new SoundLoader(false, "sMenuGrow", "sMenuTwo", "sMenuFart", "sMenuUp", "sMenuDown", "sMenuConfirm","sDishError","sMenuKeyReceive", "sMenuTest", "sMenuControlsDisplay");
			new SpriteLoader("overlays",false,".jpg","overlay"+Global.menuOverlay);
			new SpriteLoader(/*"sTitleCrazd_2", */"sTitleTwo_10", "sMenuFlowersBack_5", "sMenuFlowersLine_5", "sMenuCloudsBack_4", "sMenuCloudsLine_4", "sMenuButton_2", "menufont_49");
			new MenuBg();
			new Menu();
		}
		else if (rm.equals("death")){
			new SoundLoader(false, "sDeathShow", "sMenuUp", "sMenuDown", "sMenuConfirm");
			new SpriteLoader("sDeathBack_3", "sDeathLine_3", "menufont_49", "sMenuButton_2");
			new SpriteLoader("overlays",false,".jpg","overlay"+Global.menuOverlay);
			new Death();
		}
		
		Main.animator.resetFPSCounter();
		//Main.animator.setUpdateFPSFrames(1, null);
	}
	
	public static void changeRoom(int rX, int rY){
		changeRoom(rX, rY, false);
	}
	
	/**d: dream???*/
	public static void changeRoom(int rX, int rY, boolean d){
		if (Scene.console)
			System.out.println("ahhhh " +rX+", "+rY + "???!?!?!?!?!?!?!?!?!?!?!!");
		
		//Main.animator.setUpdateFPSFrames(1, null);
		
		controllers();
		
		Fg.me.shakeMultiplier = 1.0;
		
		//HAHA OH GOD
		Bg.me.delete();
		Fg.me.delete();
		Overlay.me.delete();
		
		room=rX+"_"+rY;
		
		//fuuck! stop all looping sounds!
		Audio.get("sBigUrchinSing").stop();
		
		//these values are changed in the player class anyway
		Global.roomX=rX;
		Global.roomY=rY;
		
		Global.dream = d;
		//buh
		
		Global.reactivate();
		
		killEveryone();
		
		Music.firstStep = true;
		
		String[] meta=Scene.readEntities("res/rooms/rm"+room+".dongs");
		
		/*
		 * you can return these two lines if the floating text is really necessary
		for (int i=0; i<30; i++)
			new FloatText(Calc.random(640),Calc.random(480));
		 */
		
		if (Global.teleport){
			ArrayList<Teleport> list = Scene.getEntityList(Teleport.class);
			for (int i=0; i<list.size(); i++){
				Global.playerX = list.get(i).x + 16;
				Global.playerY = list.get(i).y + 16;
				Player.me.x = Global.playerX;
				Player.me.y = Global.playerY;
			}
			Global.teleport = false;
		}
		
		//handle metadata
		if (Global.roomX <= 15)
			Global.setHexMap(Global.roomX, Global.roomY, meta[0]);
		
		if (!Global.dream)
			Global.roomColor=meta[0];
		
		if (!Global.getAreaName(meta[0]).equals(Global.currentArea))
			Hud.showArea();
		Global.currentArea = Global.getAreaName(meta[0]);
		
		String changed = changeMusicForRoom(Global.roomX, Global.roomY, meta[0]);
		
		if (!changed.equals("dongs"))
			meta[1] = changed;
		
		if (!Global.dream){
			if (musicWait <= 0){
				if (Music.music.equals(""))
					Music.changeMusic(meta[1]);
				else
					Music.fadeMusic(meta[1],true);
			}
			else{
				storedSong = meta[1];
			}
		}
		
		Global.roomMusic=meta[1];
		
		Global.stepSound=meta[2];
		
		Fg.setIntensity(Calc.parseDouble(meta[3]));
		//end of metadata handling
		
		if (Global.roomX <= 15){
			//lets make some keys
			Global.createRoomDropKeys();
			
			//lets make some player doors
			Global.createPlayerDoors();
		}
		
		//lets set the map stuff now that everything SHOULD BE created
		Global.refreshIconMap();
		
		//delete stuff
		deleteLoads(Global.lastX,Global.lastY);
		
		//load fg/bg
		Fg.touchSecret = false;
		if (!Scene.collision(Player.me, Player.me.x, Player.me.y, Global.SECRET))
			Fg.me.secretAlpha = 1;
		Bg.me.bgVis=(loadBackground("bg"+Global.roomX+","+Global.roomY+".png",(int)Math.ceil(Calc.random(6))));
		Bg.me.frontBgVis=(loadBackground("bg"+Global.roomX+","+Global.roomY+"FRONT.png",(int)Math.ceil(Calc.random(6))));
		Fg.me.fgVis=(loadBackground("fg"+Global.roomX+","+Global.roomY+".png",(int)Math.ceil(Calc.random(6))));
		if (Fg.me.fgVis)
			Fg.hasSecret=(loadBackground("fg"+Global.roomX+","+Global.roomY+"s.png",Sprite.get("fg"+Global.roomX+","+Global.roomY).textures.length));
		else
			Fg.hasSecret = false;
		
		//load pretty overlay :>
		boolean output;
		
		if (!Global.room(16, 1)){
			if (!Global.dream){
				int i = Calc.getGridPosition(Global.roomX, Global.roomY, 16);
				if (Global.heroMode) //fantastic
					i += 128;
				File u = new File(Main.DIRECTORY, "res/overlays/overlay"+i+".jpg");
				output=u.exists();
				if (output)
					Sprite.loadFile("overlay"+i+".jpg","res/overlays/",0);
			}
			else{
				File u = new File(Main.DIRECTORY, "res/overlays/z"+Global.currentDream+".jpg");
				output=u.exists();
				if (output)
					Sprite.loadFile("z"+Global.currentDream+".jpg","res/overlays/",0);
			}
		}
		else{
			output = true;
			Sprite.loadFile("whatisthis.jpg","res/overlays/",0);
		}
		
		Fg.me.ovVis=output;
		
		Global.gemMilestones = 50 + (int)Calc.random(50);
		
		ArrayList<JellyBlock> list = Scene.getEntityList(JellyBlock.class);
		for (int i=0; i<list.size(); i++)
			list.get(i).determineSheen();
		
		Global.roomInitEvents();
		
		Player.me.roomStart();
		
		if (Global.dream){
			if (musicWait <= 0){
				if (Music.music.equals(""))
					Music.changeMusic(meta[1], false);
				else
					Music.fadeMusic(meta[1],true, false);
			}
			else{
				storedSong = meta[1];
			}
		}
		
		ArrayList<NewRain> bist = Scene.getEntityList(NewRain.class);
		for (int i=0; i<bist.size(); i++)
			bist.get(i).refresh();
		
		Main.animator.resetFPSCounter();
		 
		if (Global.gotGun){
			if (Global.selectedWeapon == 0 && Global.playerBullets != Global.playerMaxBullets){
				Sound.playPitched("sGunReload",.08);
				Hud.showWeapon();
				Hud.randomizeColor();
			}
			
			Global.playerBullets = Global.playerMaxBullets;
		}
		
		//new Feelings(0,0);
		
		//new Turkey(Player.me.x, Player.me.y, "sAxeLadyFace", "sAxeLadyFeather", "D4FF2B", "sBalloonPop", 1.0).setConversation(new Message("test"));
		//new ReadBook(new SpriteLoader(true, ".jpg", "bookA", "bookB", "bookC", "bookD", "bookE", "bookF", "bookG"));
		//new BossAnnounce("HOLD MY DICK","FF3557","sMouseKill");
		//Global.toTweetScene("kill me please just fucking kill me already fuck fuck fuck fuck shit shit shit shit zzzz");
	}
	
	/** this is for changing music based on which direction you entered a room i guess/any other criteria why is this here lol
	 * @param meta */
	public static String changeMusicForRoom(int x, int y, String meta){
		if (x == 3 && y == 3 && Global.gotMapDoor)
			return "musSoldOut";
		else if (x == 10 && y == 5 && Global.event[EV.DISH_PUZZLE] == 0)
			return "musChimey";
		else if (Calc.compareColor("FFD808", meta) && Global.event[EV.SLICK_DEFEAT] > 0)
			return "musTup";
		return "dongs";
	}

	/**Call when you need to delete the loaded bg, fg, and overlay at the given position*/
	public static void deleteLoads(int ax, int ay){
		//delete stuff
		deleteSprite("add"+ax+","+ay);
		deleteSprite("bg"+ax+","+ay);
		deleteSprite("bg"+ax+","+ay+"FRONT");
		deleteSprite("cracked"+ax+","+ay);
		deleteSprite("fg"+ax+","+ay);
		deleteSprite("fg"+ax+","+ay+"s");
		deleteSprite("glow"+ax+","+ay);
		deleteSprite("glow"+ax+","+ay+"OUT");
		deleteSprite("overlay"+Calc.getGridPosition(ax, ay, 16));
		deleteSprite("z"+(ax - 16));
	}
	
	public static void deleteSprite(String s){
		if (Sprite.exists(s))
			Sprite.get(s).remove();
	}
	
	public static boolean loadBackground(String f, int split){
		File u = new File(Main.DIRECTORY, "res/backgrounds/"+f);
		boolean output=u.exists();
		if (output)
			Sprite.loadFile(f,"res/backgrounds/",split);
		
		return output;
	}
	
	public static void killEveryone(){
		Entity[] instances = Scene.getInstances();
		
		for(int i=0; i<instances.length; i++)
		{
			Entity e = instances[i];
			if (!e.persistent)
			e.roomDestroy();
		}
	}

	@Override
	public void step(){
		musicWait -= 1;
		if (musicWait == 0 && !storedSong.equals(""))
			Music.changeMusic(storedSong);
		
		super.step();
	}
	@Override
	public void render(){
		
		GL2 gl = Scene.gl;
		
		//Set camera
		gl.glViewport(Scene.viewportX, Scene.viewportY, Scene.viewportWidth, Scene.viewportHeight);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrtho(0, Scene.projectionWidth, Scene.projectionHeight, 0, -1,1);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		
		//Clear background
		//gl.glClearColor(0.75f,0.75f,0.75f,1);
		gl.glClearColor(0,0,0,1);
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT|GL2.GL_DEPTH_BUFFER_BIT);
	}
	
	public void startMusic(String mus){
		Audio.get(mus).setLooping(true);
		Audio.get(mus).setGain(0);
		Audio.fade(mus, 0, amount/100);
		Audio.get(mus).play();
	}
	public static void playMusic(String mus){
		Audio.fade(mus, 1, amount/100);
	}
	public static void stopMusic(String mus){
		Audio.fade(mus, 0, amount/100);
	}
}
