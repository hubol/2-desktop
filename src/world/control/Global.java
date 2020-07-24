package world.control;

import graphics.Font;
import graphics.Sprite;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import audio.Audio;
import world.Bg;
import world.Fg;
import world.Root;
import world.boss.BossBlock;
import world.boss.BossCollision;
import world.boss.banaan.Banaan;
import world.boss.duke.DukeFirst;
import world.boss.duke.Duketangle;
import world.boss.jelly.JellyBigBubble;
import world.boss.jelly.JellyExplosion;
import world.boss.jelly.JellyOpening;
import world.boss.jelly.JellyRender;
import world.boss.papa.Papa;
import world.boss.puke.PukeFirst;
import world.boss.skull.Skull;
import world.boss.slick.SlickBrokenControl;
import world.dream.DreamHeartParticles;
import world.enemy.BasicEnemy;
import world.enemy.BombBaby;
import world.enemy.Urchin;
import world.event.CellPhone;
import world.event.DassKey;
import world.event.Digit;
import world.event.DreamMusicControl;
import world.event.EventTicket;
import world.event.HandsomePrince;
import world.event.Heal;
import world.event.Monster;
import world.event.NewRain;
import world.event.SunlightBottomDrip;
import world.event.SunlightBottomDrop;
import world.gameplay.Bed;
import world.gameplay.BombRock;
import world.gameplay.Door;
import world.gameplay.DropKey;
import world.gameplay.Gem;
import world.gameplay.Jar;
import world.gameplay.TextSign;
import world.interact.BasicNpc;
import world.junk.AnalBead;
import world.menu.MenuControls;
import world.particles.Explosion;
import world.particles.PieceParticle;
import world.particles.Spark;
import world.particles.SquareParticle;
import world.player.Player;
import main.Calc;
import main.Entity;
import main.Main;
import main.Scene;

public class Global extends Entity{
	public final static Font FONT = new Font("font"), CRAZDFONT = new Font("crazdFont");
	public final static Sprite sHEART = Sprite.get("sHeart"), sBLOCK = Sprite.get("sBlock"), sBALLOON = Sprite.get("sBalloon"), sBOMBROCK = Sprite.get("sBombRock"), sDOOR = Sprite.get("sDoor"), sDOORHOLE = Sprite.get("sDoorHole"), sUPBLOCK = Sprite.get("sUpblock"), sEXPLODE = Sprite.get("sExplode");
	//version control
	public final static int VERSION=1;
	public final static double REAL_VERSION = 1.12; //LAST UPLOADED 1.12
	//god damn
	public static int identifier;
	//room co-ordinates
	public static int roomX, roomY, lastX, lastY;
	//dream stuff
	public static int dreams = 40, currentDream;
	public static boolean dream;
	public static boolean[] dreamCleared;
	public static double returnX, returnY;
	public static int returnRX, returnRY;
	//debug
	public static final boolean canDebug = false;
	public static boolean teleport=false, debug = false; //teleport is not just a debug feature!!!! used in game now!!! (though i cant think of where??)
	//player co-ordinates
	public static double playerX, playerY;
	//player stats
	public static boolean heroMode;
	public static double jumpSpeed, runSpeed, luck, tempMultLuck;
	public static int maxWeapons = 3;
	public static boolean gotDuck, gotWallSlide, gotGun, gotBombs, gotMapDoor, gotMapIcons, gotGlasses, gotBoots, gotStench, gotClover; //things you got
	public static boolean glassesEnabled; //enabled stuff
	public static int playerHealth, playerMaxHealth, playerBombs, playerMaxBombs, playerBullets, playerMaxBullets, playerBulletRefillTimer, money, selectedWeapon;
	//various shit being recorded??
	public static int totalSaves, totalDeaths, gemMilestones;
	public static long framesPlayed;
	public static long framesPaused;
	//player tweets lol
	public static String[] tweetText, tweetTimestamp;
	//player placeable doors
	public static boolean gotDoors;
	public static int playerDoors;
	public static String playerDoorColor;
	//HI!!!!!! SELECTED WEAPON!!!!! 0 = GUN!!!! 1 = BOMB!!!!!!!!!! 2= DOORS 3 = GLASSES
	//PAUSE STUFF
	public static boolean paused;
	//options
	public static boolean alwaysShowHud;
	//keyboard controls
	public static int LEFT, RIGHT, UP, DOWN, JUMP, PAUSE, ACTION, INTERACT;
	public static String controls = "res/data/controls.dongs";
	//volume control
	public static boolean mute;
	//blood
	public static String bloodColor = "FF1443", bloodOutlineColor = "4784FF";
	//reassigning keyboard control stuff
	public static int which;
	public static boolean awaitKey;
	//eat pee
	public static SoundLoader soundLoadClaimed;
	public static boolean lastDream;
	//jesus
	public static String menuBackgroundColor = "#FFFFFF", menuLineColor = "#000000";
	public static int menuOverlay, menuSong;
	//stored
	public static int storedSaveFile;
	public static boolean storedHeroine;
	
	public static boolean playerDead;
	
	//this iS SO FUCKING STUPID
	public static double storedA = 0;
	
	//collision group shit!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	public final static int
	CONTROLLER=-1,
	PLAYER=5,
	RAINBOW = 6,
	DEACTIVATEME = 7,
	BED = 8,
	BOSSCOLLISION = 9,
	SOLIDBLOCK=10,
	UPBLOCK=11,
	SECRET = 12,
	HEART = 13,
	ITEM = 14,
	INTERACTABLE = 15,
	TEMPSOLID = 16,
	BLOCK = 17,
	BOSSBLOCK = 18,
	BOMBROCK = 19,
	JELLYBLOCK = 20,
	BULLET = 21,
	STONEBELLON = 22,
	NOOCCUPY = 23,
	BASICNPC = 24,
	TRUCKWINDOW = 25,
	MONSTER = 26,
	DEACTIVATEENEMY = 27,
	WATER = 28,
	WATERSOLID = 29,
	KILLATLEAVE = 30,
	DINGUS = 31,
	STUPIDORB = 32,
	//SCENERY=42,
	KEY=50,
	LOCKED=51,
	UNLOCKED=52,
	TURNER=66,
	DOOR=69,
	JAR=100,
	BASICENEMY=101;
	//map stuff
	public static String[] hexMap; //keep track of colors of map stuff in hex string
	public static int[] symMap; //keep track of icons on map
	//drawing an INTERACT symbol
	public static Sprite action;
	public static double actionImg, actionX, actionY;
	public static boolean actionVis;
	//drawing a down symbol
	public static Sprite down;
	public static double downX, downY;
	public static boolean downVis;
	//whether regular gamestuff is active:
	public static boolean mainActive;
	//room stuff
	public static String currentArea="";
	public static String stepSound;
	public static String roomMusic, roomColor="#FFFFFF";
	//key/lock stuff
	public static int keyAmount=50;
	//holding key stuff
	public static String heldKey;
	public static boolean[] pickedUp;
	public static boolean[] unlocked;
	//dropped keys
	public static int[] dropRmX, dropRmY;
	public static double[] dropX, dropY;
	public static String[] dropColor;
	//money left laying around
	public static int gemAmount=300;
	public static boolean[] gemGot;
	//events
	public static final int eventAmount = 100;
	public static int[] event; //0 = jellyboss defeat, 1 = complete adventure game once, 2 = music dream, 3 = skull defeat, 4 = axe + tree prog
	
	public static final int eventItemAmount = 16;
	public static int[] eventItem;
	public static final String[] eventItemNames = {"AXE","DASSKEY","BUTTERFLIES","DINOFOOTPRINT","DINOFEMUR","DINOCLAW #DINOTOOTH #DINOHORN???","EROR","ERROR","ERO","CUM","","","","","",""};
	//butterflies!!
	public static boolean[] butterfly;
	//doors placed by player
	public static int[] doorRmX, doorRmY, doorToRmX, doorToRmY;
	public static double[] doorX, doorY, doorToX, doorToY;
	public static String[] doorColor;
	public static int doorSetsPlaced, doorsPlaced, doorSetsMax = 25, doorSetsPurchased;
	//hearts
	public static int heartAmount = 72;
	public static boolean[] heartGot;
	//map lines
	public static int[] mapDoorX, mapDoorY, mapDoorToX, mapDoorToY;
	public static int mapDoorAmount = 256;
	//area names based on color strings
	public static HashMap<String, String> areaNames;
	//colors
	public final static String BLUELINE = "#20a1ff";
	//various
	public static boolean isHalloween = false;
	//cutscene shit
	public static String cutsceneTweet;
	//shit
	public static int screenMode;
	//i hate my life
	public final static int totalBosses = 7;
	public final static int totalChests = 11;
	public static int saveFileNumber = 1; //TODO fuvk!!!!!!
	public static double storedPitch;

	public static boolean iLoveYou;
	
	public static Shake rain;

	public Global(double x, double y) {
		super(x, y);
		
		Scene.console = false;
		
		rain = new Shake(.3);
		rain.persistent = true;
		
		iLoveYou = false;
		
		playerDead = false;
		soundLoadClaimed = null;
		
		Scene.killOnEscape = false;
		
		lastDream = false;
		
		screenMode = 1;
		screenMode(screenMode);
		
		areaNames = new HashMap<String, String>();
		addAreaName("FF70C7","BEDROOM");
		addAreaName("00C773","FIELD");
		addAreaName("DBFF30","SUNLIGHT");
		addAreaName("FF7787","STONE");
		addAreaName("FF8B10","CAVE");
		addAreaName("AF60FF","TOWN");
		addAreaName("35FF75","$$$$$$");
		addAreaName("59FF74","DOT");
		addAreaName("FF164D","SHRINE");
		addAreaName("FFD808","BATH");
		addAreaName("18C9FF", "BRICK");
		addAreaName("D768FF", "RAIN");
		addAreaName("9FFFCE", "PLANT");
		addAreaName("FC9FFF", "KISS");
		addAreaName("00C77D", "URCHIN");
		addAreaName("FF563F", "FINAL");
		
		initTweets();
		
		action = Sprite.get("sInteract");
		down = Sprite.get("sDown");
		
		paused = false;
		
		actionVis = false;
		actionX = -69;
		actionY = -69;
		
		downVis = false;
		downX = -69;
		downY = -69;
		
		visible = true;
		persistent = true;
		setCollisionGroup(CONTROLLER);
		
		mainActive = false;
		
		isHalloween = checkDate("10", "31");
		
		dream = false;
		currentDream = -1;
		dreamCleared = new boolean[dreams];
		returnX = -69;
		returnY = -69;
		returnRX = 0;
		returnRY = 0;
		
		for (int i=0; i<dreams; i++)
			dreamCleared[i] = false;
		
		//butterfly
		butterfly = new boolean[50];
		
		//map init
		hexMap = new String[256];
		symMap = new int[256];
		
		//pickin up some keys
		heldKey = "";
		pickedUp = new boolean[keyAmount];
		unlocked = new boolean[keyAmount];
		
		//dropped keys stuff
		dropRmX = new int[keyAmount];
		dropRmY = new int[keyAmount];
		dropX = new double[keyAmount];
		dropY = new double[keyAmount];
		dropColor = new String[keyAmount];
		
		//placed doors stuff
		doorColor = new String[doorSetsMax*2];
		doorRmX = new int[doorSetsMax*2];
		doorRmY = new int[doorSetsMax*2];
		doorX = new double[doorSetsMax*2];
		doorY = new double[doorSetsMax*2];
		doorToRmX = new int[doorSetsMax*2];
		doorToRmY = new int[doorSetsMax*2];
		doorToX = new double[doorSetsMax*2];
		doorToY = new double[doorSetsMax*2];
		initPlayerDoors();
		
		setDepth(Integer.MIN_VALUE+5);
		
		for (int i=0; i<keyAmount; i++){
			pickedUp[i] = false;
			unlocked[i] = false;
			clearDropEntry(i);
		}
		
		gemGot = new boolean[gemAmount];
		for (int i=0; i<gemAmount; i++){
			gemGot[i] = false;
		}
		
		mapDoorX = new int[mapDoorAmount];
		mapDoorY = new int[mapDoorAmount];
		mapDoorToX = new int[mapDoorAmount];
		mapDoorToY = new int[mapDoorAmount];
		for (int i=0; i<mapDoorAmount; i++){
			mapDoorX[i] = -1;
			mapDoorY[i] = -1;
			mapDoorToX[i] = -1;
			mapDoorToY[i] = -1;
		}
		
		heartGot = new boolean[heartAmount];
		for (int i=0; i<heartAmount; i++){
			heartGot[i] = false;
		}
		
		event = new int[eventAmount];
		for (int i=0; i<eventAmount; i++){
			event[i] = 0;
		}
		
		eventItem = new int[eventItemAmount];
		for (int i=0; i<eventItemAmount; i++){
			eventItem[i] = 0;
		}
		
		File f = new File(Main.DIRECTORY, controls);
		if (!f.exists()){
			LEFT = KeyEvent.VK_LEFT;
			RIGHT = KeyEvent.VK_RIGHT;
			UP = KeyEvent.VK_UP;
			DOWN = KeyEvent.VK_DOWN;
			JUMP = KeyEvent.VK_UP;
			PAUSE = KeyEvent.VK_SPACE;
			ACTION = (int)'Z';
			INTERACT = (int)'X';
			writeKeys();
		}
		else
			readKeys();
	}
	
	public static int heartsGot(){
		int output = 0;
		for (int i=0; i<heartAmount; i++)
			output += Calc.boolToInt(Global.heartGot[i]);
		return output;
	}
	
	public static int chestsOpened(){
		return Global.event[EV.BOMBUPGRADE] + Calc.boolToInt(Global.gotBombs) + Calc.boolToInt(Global.gotGun) + Calc.boolToInt(Global.gotWallSlide) + Calc.boolToInt(Global.gotClover) + Calc.boolToInt(Global.gotBoots) + Calc.boolToInt(Global.gotMapIcons) + Calc.boolToInt(Global.gotMapDoor) + Calc.boolToInt(Global.gotStench) + Global.event[EV.GOT_SNORKEL] + Global.event[EV.BULB];
	}
	
	public void step(){
		if (!paused){
			actionImg+=.5;
			if (actionImg>=8)
				actionImg-=8;
		}
		
		//LUCK!
		tempMultLuck = 1.0;
		/*if (isHalloween)
			tempMultLuck += .5;*/
		//NOT REPRODUCABLE IN ALL RUNS->REDACTED
	}
	
	public static void eventItemGet(int id){
		eventItem[id] = 1;
		new EventTicket(id, true);
		String s = "";
		if (Calc.random(1) < .5){
			s += (String)Calc.choose("GOT","OBTAINED","ACQUIRED");
			s += (String)Calc.choose(" THE "," ");
		}
		s += "#" + eventItemNames[id] + (String)Calc.choose("!","!!","",".");
		if (id >= 3 && id <= 5)
			s += " #DINOBONER SWAG";
		addTweet(s);
	}
	
	public static void eventItemUse(int id){
		eventItem[id] = 2;
		new EventTicket(id, false);
	}
	
	/**im lazy*/
	public static boolean room(int x, int y){
		return (roomX == x && roomY == y);
	}
	
	/**im still lazy*/
	public static boolean last(int x, int y){
		return (lastX == x && lastY == y);
	}
	

	public static void clearDream(){
		if (!dreamCleared[currentDream]){
			dreamCleared[currentDream] = true;
			
			//Sound.playPitched("sDreamClear",.07);
			
			ArrayList<Bed> list = Scene.getEntityList(Bed.class);
			for (int i=0; i<list.size(); i++){
				list.get(i).intensity = 2.0;
				list.get(i).cleared = true;
			}
		}
	}
	
	/**lets go to the dream world (does not take care of player things!!!)*/
	public static void toDream(int ax){
		returnX = Player.me.x;
		returnY = Player.me.y;
		returnRX = roomX;
		returnRY = roomY;
		
		lastX = roomX;
		lastY = roomY;
		
		dream = true;
		teleport = true;
		
		currentDream = ax - 16;
		
		//Overlay.fadeOut(0, 0, 0, 1, 60);
		Root.changeRoom(ax, 0, true);
		
		Player.control = false;
		Player.me.returnPiecesInstant();
	}
	
	/**lets leave the dream world (does not take care of player things!!!)*/
	public static void fromDream(){
		lastDream = true;
		
		Player.me.x = returnX;
		Player.me.y = returnY;
		
		lastX = roomX;
		lastY = roomY;
		
		currentDream = -1;
		
		dream = false;
		
		//Overlay.fadeOut(0, 0, 0, 1, 60);
		Root.changeRoom(returnRX, returnRY);
		
		ArrayList<Bed> list = Scene.getEntityList(Bed.class);
		for (int i=0; i<list.size(); i++)
			list.get(i).save();
		
		Player.me.returnPiecesInstant();
	}
	
	/**save the muthafucking game*/
	public static void saveGame(){
		SaveFile.save();
	}
	
	/**lets have a cutscene party*/
	public static void toTweetScene(String s){
		cutsceneTweet = s.toUpperCase();
		new CellPhone(0,0);
	}
	
	/**change screen mode! 0 = windowed! 1 = fullscreen w/ aspect! 2 = fullscreen (fill entire screen)*/
	public static void screenMode(int m){
		//Scene.keepViewport = false;
		if (m == 0){
			Scene.keepAspectRatio = true;
			//Scene.viewportWidth = 800;
			//Scene.viewportHeight = 600;
			//Scene.canvasWidth = 800;
			//Scene.canvasHeight = 600;
			//Scene.width = 800;
			//Scene.height = 600;
			Main.canvas.setPreferredSize(new Dimension(800,600));
			Main.setFullScreen(false);
		}
		else{
		    if (m == 1)
				Scene.keepAspectRatio = true;
		    else
				Scene.keepAspectRatio = false;
		    
		    Main.setFullScreen(true);
		}
	}
	
	public static int butterfliesCaught(){
		int a = 0;
		for (int i=0; i<50; i++){
			if (butterfly[i])
				a += 1;
		}
		return a;
	}
	
	public static void shiftScreenMode(){
		if (screenMode == 0)
			screenMode = 1;
		else
			screenMode = 0;
		screenMode(screenMode);
	}
	
	public static void roomInitEvents(){
		//TODO room init stuff
		if (roomX == 9 && roomY == 1 && event[EV.SKULL_DEFEAT] == 0){
			new SpriteLoader("sSkullShadow", "sSkullFlowerCenterShadow", "sSkullPetalShadow", "sSkull_8","sSkullEyebrow","sSkullFlowerCenter","sSkullFlowerExplosion_2","sSkullHole_4","sSkullMouth_2","sSkullParticle","sSkullPetal","sSkullPetalParticle","sSkullPetalSmall","sSkullSeed","sSkullSeedBaby_2","sSkullSeedBabyKill_7","sSkullShatter_8","sSkullSprout_13","sSkullStem", "sJetpackSmoke", "sJetpack");
			new SoundLoader("sSkullQuake","sSkullRise", "sSkullBlink", "sSkullVineGrow","sSkullFlowerBloom","sSkullFlowerBloom2","sSkullShriek","sBossTextSkull", "sSkullSeedLand", "sSkullPoop", "sSkullDropWhistle", "sSkullFlowerAppear1", "sSkullFlowerAppear2", "sSkullFlowerAppear3", "sSkullFlowerAppear4", "sSkullFlowerDestroy1", "sSkullFlowerDestroy2", "sSkullFlowerDestroy3", "sSkullFlowerDestroy4", "sJetpackExhaust", "sJetpackUpExhaust", "sJetpackFallWhistle", "sSkullBabyKill", "sSkullBabyLand", "sSkullBabyJump", "sSkullBabyDash", "sJetpackExplode", "sSkullHit", "sSkullSqueel", "sSkullFall", "sSkullShatter", "sSkullSigh");
		}
		else if (roomX == 18 && roomY == 0 && event[EV.SUNLIGHT_DREAM] == 0){
			new SoundLoader(false, "sSundHeartUp", "sSundHeartGo", "sSundHeartAttach", "sDreamHeartAppear");
			new SpriteLoader("sSunScribbles_5");
		}
		else if (roomX == 14 && roomY == 1)
			new SoundLoader("sBlindSpeak", "sWeirdSpeak", "sMouthSpeak");
		else if (roomX == 15 && roomY == 1)
			new SoundLoader("sCuteBoySpeak", "sGalSpeak");
		else if (roomX == 15 && roomY == 2 && event[EV.BANAAN_DEFEAT] == 0){
			new SoundLoader("sBanaanKopiAvOw", "sSkullQuake", "sBanaanKopiEnd", "sBanaanHitScream", "sBanaanHitChime", "sBanaanScorch", "sBossTextBanaan", "sBanaanPukeDie", "sBanaanFck", "sBanaanDed");
			new Banaan(224, 168);
		}
		else if (room(16,1)){
			dream = false;
			currentDream = -1;
		}
		/*else if (room(1,2) && !gotWallSlide && !gotGun)
			new TextSign(224, 288,"you're not ready%for the%lime green key!","crazdFont","48a0ff");*/
		else if (room(10,5)){
			if (event[EV.DISH_PUZZLE] == 0){
				//new AmbientLoop(0, 0, "loopChimey");
				new SpriteLoader("sWeed_2");
				new SoundLoader("sDishBlock", "sDishError", "sDishWin", "sEpicWeedJoke", "sDishClang", "sTakstein", "sDishFjank", "sDishSplode");
			}
			else{
				ArrayList<Digit> list = Scene.getEntityList(Digit.class);
				for (int i=0; i<list.size(); i++)
					list.get(i).destroy();
				
				if (eventItem[1] == 0){
					//no dasskey
					new SoundLoader("sDishFjank");
					new DassKey(288, 112, false);
				}
			}
		}
		else if (room(9,5) && Global.event[EV.TRUCK_UNLOCKED] == 0){
			new SoundLoader("sTruckUnlock", "sTruckCant", "sTruckEscape");
		}
		else if (room(12, 4) && Player.me.y > 470){
			Player.me.y = 479;
			Player.me.x = 320;
			Monster i = new Monster(320, 512);
			i.enterRoomFlying();
		}
		else if (room(22,0)){
			new SoundLoader(false, "sDreamJumpChime", "sDishError", "sFjaak0", "sFjaak1", "sJumpDream");
		}
		else if (room(21,0))
			new SpriteLoader("sHeartScribbles_5");
		else if (room(23,0)){
			new SpriteLoader("sJarScribbles_5");
			new SoundLoader(false,"sDreamHeartAppear");
		}
		else if (room(1,1)){
			new SpriteLoader("sBloodFountain_4");
			
		}
		else if (room(25,0)){
			new SoundLoader(false, "sPetStartle","sPetCall","sPetCancel","sPetConfirm","sPetSelect","sPetHappy","sPetFail","sPetNeedle","sPetWin","sDreamHeartAppear");
		}
		else if (room(27,0)){
			//ghost
			new SpriteLoader("sGhostBack_4", "sGhostOutline_4", "sGhostFace_4");
		}
		else if (room(28,0)){
			new SpriteLoader("sMusicHeartUnearth", "sMusicBlock", "sMusicBoxIcons_5");
			new DreamMusicControl(0,0);
		}
		else if (room(13,4)){
			new SoundLoader(false, "sSlickSpark1", "sSlickSpark2", "sSlickIntro");
			new SpriteLoader("sSlickBody_2", "sSlickEyelid_20", "sSlickFoot", "sSlickMandible", "sSlickPupil_2", "sSlickSpark_5", "sSlickBoulder_2", "sSlickDebris_2", "sSlickBoulderMask", "sSlickPop_3", "sSlickRockDebris_4");
			new SoundLoader("sSlickForm", "sSlickAttach", "sBossTextSlick", "sSlickCrouch", "sSlickGroundCrash", "sSlickJump", "sSlickEyeOpen", "sSlickBoulderLand", "sSlickHurt", "sSlickHurtBase", "sSlickEyeClose", "sSlickPop","sSkullQuake", "sSlickDebrisLand", "sSlickIndicate", "sSlickStab", "sSlickWithdraw", "sSlickVulnerable", "sSlickBigJump", "sSlickOutro", "sSlickSqwak", "sSlickFart", "sSlickFinal");
		}
		else if (room(7,4)){
			new SpriteLoader("sWaterSparks_6", "sWaterSparksBack_6", "sWaterSparksMask");
			new SoundLoader(false, "sSparkSputter0", "sSparkSputter1", "sSparkTune0", "sSparkTune1", "sSparkTune2", "sSparkTune3", "sSparkTune4");
		}
		else if (room(7,3)){
			new SpriteLoader("sBathShit");
		}
		else if (room(24,0))
			new SpriteLoader("sAmwayEye");
		else if (room(5, 3))
			new SpriteLoader("sJellyBigBubbleBreak_7","sJellyBigBubbleBreakGlow_5","sJellyBlob","sJellyBlobBack","sJellyBlobSheen","sJellyBombBlob","sJellyBombBubble","sJellyBombPop","sJellyMan_3","sJellyManBack_3","sJellyManBreak_11","sJellyWarn_4","sJellyWarnBack_4");
		else if (room(19, 0))
			new SpriteLoader("sSemenMask", "sPenisMeterDassFace", "sPhallus_8", "sSemen_8", "sSemenBack_8");
		else if (room(14, 5) || room(14, 4)){
			if (room(14, 5)){
				new SpriteLoader("sBrickLine_5", "sBrickSolid_5");
				new SoundLoader(false, "sBrickLand");
			}
			
			if (!Scene.instanceExists(HandsomePrince.class))
				new HandsomePrince(0, 0);
			else 
				HandsomePrince.me.alarm[0] = -1;
			
		}
		else if (room(6, 5) || room(6, 6)){ //this is very silly
			if (room(6, 5))
				new SpriteLoader("sSunlightBottomDrip_11");
			else
				new SpriteLoader("sSunlightBottomDrip_11", "sStupidSplash_7");
			
			for (int j=0; j<120; j++){
				ArrayList<SunlightBottomDrip> list = Scene.getEntityList(SunlightBottomDrip.class);
				for (int i=0; i<list.size(); i++)
						list.get(i).step();
					
				ArrayList<SunlightBottomDrop> bist = Scene.getEntityList(SunlightBottomDrop.class);
				for (int i=0; i<bist.size(); i++){
						bist.get(i).play = false;
						bist.get(i).step();
				}
			}
			
			ArrayList<SunlightBottomDrop> bist = Scene.getEntityList(SunlightBottomDrop.class);
			for (int i=0; i<bist.size(); i++)
					bist.get(i).play = true;
		}
		else if (room(15, 7))
			new SoundLoader(false, "sUnderSecret");
		else if (room(14, 6)){
			/*if (Audio.soundExists("musCute"))
				Audio.get("musCute").setPitch(1);*/
		}
		else if (room(13, 7) && last(14, 7)){
			Scene.destroy(BombRock.class);
		}
		else if (room(11, 7))
			new SpriteLoader("sDassHidden_8");
		else if (room(0, 3))
			Global.event[EV.ROOMOFDOORS] = 1;
		else if (room(9,7))
			new SpriteLoader("sStrawberry", "sLemon");
		else if (room(5,5)){
			new SpriteLoader("sAnalBeads_3", "sAnalTexture");
			new SpriteLoader(true, ".jpg", "sSoundGen_512");
			ArrayList<AnalBead> list = Scene.getEntityList(AnalBead.class);
			for (int j=0; j<270; j++){
				for (int i=0; i<list.size(); i++)
					list.get(i).step();
			}
			new SoundLoader("sAnalBeadBreak");
			//musAnalBeads is slightly over the top. should be reserved for some quiet location
			//new AmbientLoop(0,0,"musAnalBeads");
		}
		else if (room(4, 6)){
			new SpriteLoader("sKissFace_3", "sKissDrool_2");
			new SoundLoader(false, "sAddLeft", "sAddRight", "sKissCongrats", "sKissDrool");
		}
		else if (room(4, 5)){
			Global.event[EV.ABOVEADD] = 0;
			new SpriteLoader("sKissFace_3", "sKissDrool_2");
			new SoundLoader(false, "sShriekLeft", "sShriekRight", "sKissCongrats", "sKissDrool");
			Fg.me.shakeTimer = 0;
		}
		else if (room(2, 5)){
			Global.event[EV.ONOFF] = 1;
			new SpriteLoader("sKissFace_3", "sKissDrool_2");
			new SoundLoader(false, "sShrook", "sZOn", "sZOff", "sKissCongrats", "sKissDrool");
			Fg.me.shakeTimer = 0;
		}
		else if (room(3,4) && Player.me.x > 320)
			Scene.destroy(Urchin.class);
		else if (room(4, 4) && Global.event[EV.PAPA_DEFEAT] == 0){
			new SoundLoader("sPapaSpikeIn", "sPapaJuice", "sPapaDeath", "sPapaDeathBegin", "sPapaRecharge", "sPapaReform", "sBossTextPapa", "sPapaPutter", "sPapaSniffle", "sPapaBigSniffle", "sPapaSpikeBreak", "sPapaSpikeLaunch", "sPapaSpikeLaunchCount", "sPapaBoink", "sPapaFinalLaunchPrepare", "sPapaLaunchScreech", "sPapaOrbHurt", "sPapaOrbBreak"); 
			new SpriteLoader("sPapaSpikeShard", "sPapaOrbShard", "sPapaDamage_3", "sPapaOuterDamage_6");
		}
		else if (room(0,4)){
			new SpriteLoader("sKissFace_3", "sKissDrool_2");
			new SoundLoader(false, "sOrbGroan", "sOrbChange", "sStupidOrbBoink", "sStupidOrbRespawn", "sKissCongrats", "sKissDrool");
		}
		else if (room(0, 6)){
			new SoundLoader(false, "sFinalOpen");
		}
		else if (room(0, 7)){
			new SpriteLoader("sStrawberry", "sLemon");
		}
		else if (room(4, 7)){
			if (Audio.soundExists("musGoodNews")){
				Audio.get("musGoodNews").setPitch(1);
				Audio.fade("musGoodNews", 1, .025);
			}
		}
		else if (room(29,0)){
			new SpriteLoader("menufont_49", "sCreditsTitle", "sCreditsFlower_3", "sCreditsDot");
		}
		else if (room(12, 1) && Global.gotBombs){
			new BombBaby(288, 320);
			new BombBaby(320, 320);
		}
		else if (room(9, 0))
			new SoundLoader("sChopBar", "sChopTreeHit", "sChopTreeTone", "sChopArrowChange", "sChopExplode","sChopGiggle");
	}
	
	public static void pieceParticles(double x, double y, String s, double img, double imgSpd, int hPieces, int vPieces, int depth){
		double hMult = (double)Sprite.get(s).imageWidth / (double)hPieces, vMult = (double)Sprite.get(s).imageHeight / (double)vPieces;
		
		for (int i=0; i<hPieces; i++){
			for (int j=0; j<vPieces; j++){
				new PieceParticle(x, y, s, img, imgSpd, i * hMult, j * vMult, hMult, vMult, depth, 90 + Calc.rangedRandom(35), 5 + Calc.random(5), .8 + Calc.rangedRandom(.075), 80);
				//               (x, y, s, img, imgSpd, left,      top,       width, height, depth, direction,                 speed, gravity, life)
			}
		}
	}
	
	public static int getRunId(){
		int a = 0;
		
		File f = new File(Main.DIRECTORY, "res/data/runs.ily");
		try {
			if (f.exists()){
				FileReader r;
				r = new FileReader(f);
				
				BufferedReader p = new BufferedReader(r);
				a = Calc.parseInt(p.readLine());
				p.close();
				r.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return a;
	}
	
	/**this fabulously-named function causes all SoundLoader objects (including Music) to complete their threaded loading!!! as a side-effect
	 * this may cause visible "freezing".
	 * 
	 * this prevents nullpointers referencing unloaded or partially-loaded sounds on shitty and/or old hardware. workarounds for this issue have existed
	 * prior to the inclusion of this function (7 January 2014), producing console output when Sound.play() is called for an absent sound. This function, however,
	 * prevents crashes related to Audio.get() and Audio.fade(). The use of the Audio functions is rare in comparison to Sound.play(), and usually only
	 * applied in boss fights. it is therefore only necessary to use this function in situations wherein the aforementioned Audio functions are incorporated in tandem
	 * with a threaded SoundLoader.
	 * @throws InterruptedException
	 */
	public static void preventAudioCrash(){
		ArrayList<SoundLoader> sound = Scene.getEntityList(SoundLoader.class);
		for (int i=0; i<sound.size(); i++){
			try {
				sound.get(i).join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	//ROOM SPECIFIC EVENTS THAT ARE UNFORTUNATELY HARD-CODED HERE
	public static void bossCollision() throws InterruptedException{
		preventAudioCrash();
		
		//TODO boss collisions
		
		if (Scene.instanceExists(BossCollision.class)){
			ArrayList<BossCollision> list = Scene.getEntityList(BossCollision.class);
			for (int i=0; i<list.size(); i++){
				if (!list.get(i).isDestroyed())
					list.get(i).destroy();
			}
			
			if (roomX == 9 && roomY == 1 && event[EV.SKULL_DEFEAT] == 0){
				/*int amt = 6;
				for (int i = 0; i<amt; i++){
					new Tooth(80.0 + (i * (512.0/amt)), 64.0, i * 30);
				}*/
				
				//new FireRender(0, 0);
				//new FireBurster(200, 200);
				//new FireBurster(320, 240);
				
				blockFade(true);
				
				new Skull(448,424);
				
				Player.control = false;
				Player.me.cutMove = true;
				Player.me.cutRight = false;
				Player.me.cutToX = Player.me.x + 1;
				
				Music.fadeMusic("", false);
				
				/*
				
				
				
				new EggBossEnter(672, 416);*/
			}
			else if (roomX == 5 && roomY == 3){
				if (event[0] == 0){
					Music.fadeMusic("",false);
					new JellyRender(0,0);
					blockFade(true);
					
					new JellyOpening(400, -16);
					Player.control = false;
					Player.me.cutMove = true;
					Player.me.cutRight = false;
					Player.me.cutToX = Player.me.x + 1;
				}
			}
			else if (roomX == 15 && roomY == 2 && event[EV.BANAAN_DEFEAT] == 0){
				Player.control = false;
				Player.me.cutMove = true;
				Player.me.cutRight = false;
				Player.me.cutToX = Player.me.x + 1;
				
				blockFade(true);
				Music.fadeMusic("", false);
				
				Sound.playPitched("sBanaanKopiAvOw", .02);
				Banaan.me.alarm[0] = 45;
				Banaan.me.y -= 4;
				Banaan.me.surprise = 45;
			}
			else if (room(13,4) && event[EV.SLICK_DEFEAT] == 0){
				blockFade(true);
				Music.fadeMusic("", false);
				
				Player.control = false;
				Player.me.cutMove = true;
				Player.me.cutRight = false;
				Player.me.cutToX = Player.me.x + 1;
				
				SlickBrokenControl.me.alarm[0] = 60;
			}
			else if (room(7, 7) && event[EV.PUKE_DEFEAT] == 0){
				blockFade(true);
				Music.fadeMusic("", false);
				
				Player.control = false;
				Player.me.cutMove = true;
				Player.me.cutRight = false;
				Player.me.cutToX = Player.me.x + 1;
				
				addTweet("PUKE FIGHT!!!!!!! #GROSS");
				
				Sound.play("sDrama");
				new PukeFirst(320, -32);
			}
			else if (room(3, 5) && event[EV.DUKE_DEFEAT] == 0){
				blockFade(true);
				Music.fadeMusic("", false);
				
				Player.control = false;
				Player.me.cutMove = true;
				Player.me.cutRight = false;
				Player.me.cutToX = 96;
				
				addTweet("I AM FIGHTING THE #DUKE!!!!!!!");
				
				Sound.play("sDukeDrama");
				new DukeFirst(448, -32);
				new Duketangle(256, 64, 384, 416);
			}
			else if (room(4, 4) && event[EV.PAPA_DEFEAT] == 0){
				blockFade(true);
				Music.fadeMusic("", false);
				
				Player.control = false;
				Player.me.cutMove = true;
				Player.me.cutRight = false;
				Player.me.cutToX = Player.me.x + 1;
				
				addTweet("PAPA URCHIN PAPA URCHIN #PAPA #URCHIN!!!!!!!");
				new SpriteLoader("sLazy", "sPapaFace_2", "sRoundRect", "sPapaFurrow_2", "sPapaAnger_9", "sPapaAngerBack_9");
				new SoundLoader("musBoss07", "musBoss07Layer");
				new Papa(320, 160);
			}
		}
	}
	
	//RECORDING DOOR STUFF!!!
	/**record a door, usually when the door is entered*/
	public static void addMapDoor(int x, int y, int toX, int toY){
		if (!(x == toX && y == toY)){
			for (int i=0; i<mapDoorAmount; i++){
				if ((mapDoorX[i] == x && mapDoorY[i] == y && mapDoorToX[i] == toX && mapDoorToY[i] == toY)||(mapDoorX[i] == toX && mapDoorY[i] == toY && mapDoorToX[i] == x && mapDoorToY[i] == y))
					break;
				if (mapDoorX[i] == -1){
					mapDoorX[i] = x;
					mapDoorY[i] = y;
					mapDoorToX[i] = toX;
					mapDoorToY[i] = toY;
					break;
				}
			}
		}
	}
	
	//PLAYER-PLACED DOORS!!!
	
	/**initialize doors*/
	public static void initPlayerDoors(){
		for(int i=0; i<doorSetsMax*2; i++){
			clearDoorEntry(i);
		}
	}
	
	/**clear door entry*/
	public static void clearDoorEntry(int i){
		doorColor[i] = "";
		doorRmX[i] = -420;
		doorRmY[i] = -420;
		doorX[i] = -1;
		doorY[i] = -1;
		doorToRmX[i] = -420;
		doorToRmY[i] = -420;
		doorToX[i] = -1;
		doorToY[i] = -1;
	}
	
	/**add door entry*/
	public static void addDoorEntry(double x, double y){
		if (gotDoors && playerDoors > 0){
			//same for first and second door
			doorColor[doorsPlaced] = playerDoorColor;
			doorRmX[doorsPlaced] = roomX;
			doorRmY[doorsPlaced] = roomY;
			doorX[doorsPlaced] = x;
			doorY[doorsPlaced] = y;
			
			//this isnt set for the first door because it has nothing to set to until the second door is placed
			if (playerDoors == 1){
				doorToRmX[doorsPlaced] = doorRmX[doorsPlaced - 1];
				doorToRmY[doorsPlaced] = doorRmY[doorsPlaced - 1];
				doorToX[doorsPlaced] = doorX[doorsPlaced - 1] + 16;
				doorToY[doorsPlaced] = doorY[doorsPlaced - 1] + 48;
				
				doorToRmX[doorsPlaced - 1] = doorRmX[doorsPlaced];
				doorToRmY[doorsPlaced - 1] = doorRmY[doorsPlaced];
				doorToX[doorsPlaced - 1] = doorX[doorsPlaced] + 16;
				doorToY[doorsPlaced - 1] = doorY[doorsPlaced] + 48;
				
				addMapDoor(doorRmX[doorsPlaced], doorRmY[doorsPlaced], doorToRmX[doorsPlaced], doorToRmY[doorsPlaced]);
			}
			
			//if the jackass player places two doors in the same room for some reason lmao
			ArrayList<Door> biss = Scene.getEntityList(Door.class);
			for (int i=0; i<biss.size(); i++){
				if (biss.get(i).normalId == doorsPlaced - 1){
					biss.get(i).id = doorsPlaced - 1;
					biss.get(i).trX=doorToRmX[doorsPlaced - 1];
					biss.get(i).trY=doorToRmY[doorsPlaced - 1];
					biss.get(i).tpX=(int)doorToX[doorsPlaced - 1];
					biss.get(i).tpY=(int)doorToY[doorsPlaced - 1];
				}
			}
			
			createDoor(doorsPlaced);
			
			doorsPlaced += 1;
			playerDoors -= 1;
			if (playerDoors == 0){
				doorSetsPlaced += 1;
				gotDoors = false;
				if (getWeapons() > 0)
					changeWeapon(1);
			}
		}
	}
	
	/**create the player doors for this room*/
	public static void createPlayerDoors(){
		for(int i=0; i<doorSetsMax*2; i++){
			createDoor(i);
		}
	}
	
	/**check if door entry exists*/
	public static boolean doorEntryExists(int id){
		return !doorColor[id].equals("");
	}
	
	/**create instance of door entry*/
	public static void createDoor(int id){
		if (roomX == doorRmX[id] && roomY == doorRmY[id] && doorEntryExists(id)){
			int i = -1;
			if (id % 2 == 0 && doorEntryExists(id + 1) || id % 2 == 1)
				i = id;
			new Door(doorX[id], doorY[id], doorToRmX[id], doorToRmY[id], (int)doorToX[id], (int)doorToY[id], doorColor[id], i, id);
		}
	}
	
	//WOW WTF IS THIS DOING HERE LMAO
	
	/**check date (for various easter eggs)*/
	public static boolean checkDate(String m, String d){
		DateFormat dateFormat = new SimpleDateFormat("MM/dd");
		Calendar cal = Calendar.getInstance();
		
		String[] check = dateFormat.format(cal.getTime()).split("/");
		if (check[0].equals(Calc.formatNumberString(m, 2)) && check[1].equals(Calc.formatNumberString(d, 2))){
			if (Scene.console)
				System.out.println("yes! it is "+m+"/"+d);
			return true;
		}
		else{
			if (Scene.console)
				System.out.println("no! it is not "+m+"/"+d);
			return false;
		}
	}
	
	/**get the current timestamp in string form!!!*/
	public static String getTime(String regex){
		DateFormat dateFormat = new SimpleDateFormat("MM"+regex+"dd"+regex+"yy h:mm a");
		Calendar cal = Calendar.getInstance();
		return dateFormat.format(cal.getTime());
	}
	
	public static String getTime(){
		return getTime("/");
	}
	
	/**damn ur cute*/
	public static void initTweets(){
		tweetText = new String[1];
		tweetTimestamp = new String[1];
		
		tweetText[0] = "I'M ALIVE";
		tweetTimestamp[0] = getTime(".");
	}
	
	/**add a "tweet" entry thing*/
	public static void addTweet(String s){
		String[] oldText = tweetText, oldTimestamp = tweetTimestamp;
		
		int l = oldText.length;
		
		tweetText = new String[l + 1];
		tweetTimestamp = new String[l + 1];
		for (int i=0; i<l; i++){
			tweetText[i] = oldText[i];
			tweetTimestamp[i] = oldTimestamp[i];
		}
		tweetText[l] = s.toUpperCase();
		tweetTimestamp[l] = getTime(".");
		
		if (Scene.console)
			System.out.println(tweetText[l]+", "+tweetTimestamp[l]);
	}
	
	//KEY CHECKING!!! KEY PICKUPS!!! KEY DROPS!!!
	
	/**check if you are holding the correct colored key*/
	public static boolean checkHoldingKey(String s){
		if (!heldKey.equals(""))
			return (Calc.formatColor(heldKey).equals(Calc.formatColor(s)));
		return false;
	}
	
	/**lose the key you are holding*/
	public static void deleteKey(){
		heldKey = "";
	}
	
	/**lose the key you are holding*/
	public static void deleteKey(String s){
		heldKey = "";
	}
	
	/**create a key from an entry*/
	public static void createKey(int i){
		if (!dropColor[i].equals("") && roomX==dropRmX[i] && roomY==dropRmY[i]){
			new DropKey(dropX[i],dropY[i],i,dropColor[i]);
		}
	}
	
	/**pick up a key of specified color*/
	public static void pickupKey(String s, double xx, double yy){
		if (!heldKey.equals("")){
			Sound.playPitched("sKeyDrop");
			createKey(addDropEntry(roomX,roomY,xx,yy,heldKey));
			heldKey = s;
		}
		else{
			refreshIconMap();
			heldKey = s;
		}
	}
	
	/**clear drop entry*/
	public static void clearDropEntry(int i){
		dropRmX[i] = -420;
		dropRmY[i] = -420;
		dropX[i] = -1;
		dropY[i] = -1;
		dropColor[i] = "";
	}
	
	/**add drop entry*/
	public static int addDropEntry(int rmx, int rmy, double xx, double yy, String c){
		if (c=="")
			c = "#ffffff";
		
		int a = getFirstEmptyDrop();
		
		//System.out.println("FIRST EMPTY DROP!!!!: "+a);
		
		dropRmX[a] = rmx;
		dropRmY[a] = rmy;
		dropX[a] = xx;
		dropY[a] = yy;
		dropColor[a] = c;
		
		return a;
	}
	
	/**get first empty entry of drop key array*/
	public static int getFirstEmptyDrop(){
		int a = 0;
		
		while (/*dropColor[a].length() > 5 */!dropColor[a].equals("") && a<keyAmount-1)
			a+=1;
		
		//System.out.println("AAAA!@!!!!! "+a+", "+dropColor[a]);
		
		return a;
	}
	
	/**create the keys for this room*/
	public static void createRoomDropKeys(){
		for(int i=0; i<keyAmount; i++){
			createKey(i);
		}
	}
	
	//LOOT STUFF!!!!!!!!!
	
	public static double lootRandom(double seed, double x){
		return ((seed % 13.69) / 13.69) * x;
	}
	
	/**drop random amounts of loot*/
	public static void dropRandomLoot(double x, double y, double worth){
		int um = (int)lootRandom(((x + 1) * (y + 1)) + Player.me.seed + (Player.me.seed * ((x + 1)/(y + 1))), worth*luck*tempMultLuck*Math.min(1.0, (Math.max(.50, ((double)playerHealth/(double)playerMaxHealth)))*1.2))-1;
		intToGem(x,y,um);
	}
	
	/**drop somewhat-exact loot (still factors in luck)*/
	public static void dropLoot(double x, double y, double worth){
		int um = (int)Math.ceil(worth*(Math.max(1, tempMultLuck * .8))*luck);
		intToGem(x,y,um);
	}
	
	/**drop truly exact loot*/
	public static void dropExactLoot(double x, double y, double worth){
		intToGem(x,y,(int)worth);
	}
	
	//WEAPON STUFF!!!!!!!
	
	/**return how many weapons you have*/
	public static int getWeapons(){
		int weapons = 0;
		if (gotGun)
			weapons += 1;
		if (gotBombs)
			weapons += 1;
		if (gotDoors)
			weapons += 1;

		return weapons;
	}
	
	/**change weapon*/
	public static void changeWeapon(int add){
		if (getWeapons() > 1){
			int uh = selectedWeapon;
			uh += add;
			if (uh < 0)
				uh = maxWeapons - 1;
			if (uh > maxWeapons - 1)
				uh = 0;
			
			boolean[] i = new boolean[maxWeapons];
			i[0] = gotGun;
			i[1] = gotBombs;
			i[2] = gotDoors;
			
			//THIS HAS POTENTIAL TO CAUSE SERIOUS ISSUES!!!!!!
			while (!i[uh]){
				uh += add;
				if (uh < 0)
					uh = maxWeapons - 1;
				if (uh > maxWeapons - 1)
					uh = 0;
			}
			
			selectedWeapon = uh;
		}
	}
	
	/**unlock weapon*/
	public static void unlockWeapon(int id){
		if (id != 3)
			selectedWeapon = id;
		if (id == 0){
			gotGun = true;
			playerBullets = 20;
			playerMaxBullets = playerBullets;
		}
		else if (id == 1){
			gotBombs = true;
			playerBombs = 3;
			playerMaxBombs = playerBombs;
		}
		else if (id == 2){
			gotDoors = true;
			playerDoors = 2;
			playerDoorColor = Calc.makeHexColor(Calc.getColorHSV((int)Calc.random(256), 150, 255));
		}
		else if (id == 3){
			gotGlasses = true;
			glassesEnabled = false;
		}
	}
	
	/**lock weapon (for the sake of screenshots)*/
	public static void lockWeapon(int id){
		selectedWeapon = 0;
		if (id == 0){
			gotGun = false;
			playerBullets = 0;
			playerMaxBullets = playerBullets;
		}
		else if (id == 1){
			gotBombs = false;
			playerBombs = 0;
			playerMaxBombs = playerBombs;
		}
	}
	
	/**square particle circle*/
	public static void squareParticle(double x, double y, int number, String color, double spd){
		double f = Calc.random(360);
		for(int i=0; i<number; i++){
			SquareParticle j = new SquareParticle(x, y);
			j.setSpeed(spd);
			j.setDirection(f+(i*(360.0/number)));
			j.setColor(color);
		}
	}
	
	/**convert integer to a bunch of gems on screen at given position lol*/
	public static void intToGem(double x, double y, int um){
		if (um > 0){
			while (um > 50){
				um -= 10;
				new Gem(x,y,10,1,0);
			}
			while (um > 15){
				um -= 5;
				new Gem(x,y,5,1,0);
			}
			while (um > 0){
				um -= 1;
				new Gem(x,y,1,1,0);
			}
		}
	}
	
	/**heal player, play sound, create particle burster, show health hud*/
	public static void heal(){
		Global.playerHealth = Global.playerMaxHealth;
		Hud.showHealth();
		Sound.playPitched("sHeal", .035);
		new Heal();
	}
	
	/*retrieve the players name based on hero mode
	public static String getName(){
		if (heroMode)
			return "Loon";
		return "115";
	}*/
	
	public static void createStupidFuckshitFile(int s){
		try {
			File f = new File(Main.DIRECTORY, "res/data/crazd2fuckshitfile"+s+".FUCK");
			FileWriter w;
		
			w = new FileWriter(f);
			
			PrintWriter p = new PrintWriter(w);
			
			p.println("farts loudly");
			
			p.close();
			w.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**call this method only when starting a new game*/
	public static void initVars(int saveFile, boolean heroine){
		//TODO initvars!!
		//boolean doIt = (framesPlayed < 2);
		
		saveFileNumber = saveFile;
		heroMode = heroine;
		
		initTweets();
		
		dream = false;
		
		mute = false;
		lastX = -1;
		lastY = -1;
		roomX = 0;
		roomY = 0;
		playerX = 96;
		playerY = 368;
		
		totalSaves = 0;
		gemMilestones = 100; //DO NOT SAVE/LOAD!!!!!!!!!!!!!!!!
		totalDeaths = 0; //FOR FUCKS SAKE DO NOT INCLUDE THIS IN SAVING OR LOADING REGULAR GAME FILES
		framesPlayed = 0; //FOR FUCKS SAKE DO NOT INCLUDE THIS IN SAVING OR LOADING REGULAR GAME FILES
		framesPaused = 0; //FOR FUCKS SAKE DO NOT INCLUDE THIS IN SAVING OR LOADING REGULAR GAME FILES
		
		alwaysShowHud = false;
		
		gotDuck = false;
		gotWallSlide = false;
		
		gotGun = false;
		gotBombs = false;
		gotDoors = false;
		gotMapDoor = false;
		gotMapIcons = false;
		gotGlasses = false;
		gotBoots = false;
		gotStench = false;
		gotClover = false;
		
		//gotBoots = true;
		
		runSpeed = 6; //initial 6, increase by 1
		jumpSpeed = 5.31; //5.31 gets up 1 block, 7.41 gets up 2 blocks, 9.1 gets up 3, 10.5 up 4, 11.8 up 5, 12.9 up 6
		
		selectedWeapon = 0;
		
		playerMaxBullets = 0;
		playerBullets = playerMaxBullets;
		playerBulletRefillTimer = 20;
		
		playerMaxBombs = 0;
		playerBombs = playerMaxBombs;
		
		playerMaxHealth = 100;
		playerHealth = playerMaxHealth;
		
		glassesEnabled = false;
		
		playerDoors = 0;
		playerDoorColor = "";
		doorSetsPlaced = 0;
		doorsPlaced = 0;
		doorSetsPurchased = 0;
		
		if (playerMaxBullets > 0)
			gotGun = true;
		
		if (playerMaxBombs > 0)
			gotBombs = true;
		
		money = 0;
		luck = 1;
		
		stepSound = "sStep";
		
		heldKey = "";
		for (int i=0; i<keyAmount; i++){
			pickedUp[i] = false;
			unlocked[i] = false;
			clearDropEntry(i);
		}
		
		initPlayerDoors();

		for (int i=0; i<gemAmount; i++){
			gemGot[i] = false;
		}

		for (int i=0; i<mapDoorAmount; i++){
			mapDoorX[i] = -1;
			mapDoorY[i] = -1;
			mapDoorToX[i] = -1;
			mapDoorToY[i] = -1;
		}
		
		for (int i=0; i<dreams; i++)
			dreamCleared[i] = false;
		
		for (int i=0; i<50; i++)
			butterfly[i] = false;
		
		for (int i=0; i<heartAmount; i++){
			heartGot[i] = false;
		}
		
		for (int i=0; i<eventAmount; i++){
			event[i] = 0;
		}
		
		for (int i=0; i<eventItemAmount; i++){
			eventItem[i] = 0;
		}
		
		for(int a=0; a<256; a++){
			hexMap[a] = "000000";
			symMap[a] = 0;
		}
		
		identifier = (int)Calc.random(Integer.MAX_VALUE);
		//new Recorder();
		
		/*doorSetsPlaced = 24;
		doorSetsPurchased = 24;*/
		
		//gotWallSlide = true;//
		//unlockWeapon(0);//
		//unlockWeapon(1);//
		//unlockWeapon(2);
		//unlockWeapon(3);
		//gotBoots = true;//
		//gotStench = true;//
		//gotMapIcons = true;
		//gotMapDoor = true;
		//event[EV.GOT_SNORKEL] = 1; //
		//event[EV.BOMBUPGRADE] = 1;//
		
		//dreamCleared[0] = true;
		
		/*if (doIt)
			SaveFile.save();*/
		
		//default
		Global.event[EV.ONOFF] = 1;
	}
	
	public static double getProgress(){
		return heartsGot() + chestsOpened() + bossesKilled();
	}
	
	public static double getMaxProgress(){
		return (double)(heartAmount + totalChests + totalBosses);
	}
	
	public static String getPercentage(){
		double d = getProgress() / getMaxProgress();
		return (int)(d * 100) + "%";
	}
	
	/**suicidal urchins???*/
	public static boolean suicidalUrchinsExist(){
		ArrayList<Urchin> list = Scene.getEntityList(Urchin.class);
		for (int i=0; i<list.size(); i++){
			if (list.get(i).suicideTimer > 0)
				return true;
		}
		return false;
	}
	
	/**add an area name to the color key*/
	public static void addAreaName(String c, String n){
		areaNames.put(Calc.formatColor(c), n);
	}
	
	/**return an area name based on given color key*/
	public static String getAreaName(String c){
		String out = areaNames.get(Calc.formatColor(c));
		if (out != null)
			return out;
		return "Unknown Area";
	}
	
	/**returning to the game world after some sort of interlude/cutscene*/
	public static void reactivate(){
		if (!mainActive){
			mainActive = true;
		}
	}
	
	/**prepare to move to some sort of interlude/cutscne*/
	public static void deactivate(){
		if (mainActive){
			mainActive = false;
			Bg.me.visible = false;
			Fg.me.fgVis = false;
			Fg.me.ovVis = false;
			Root.deleteLoads(lastX,lastY);
		}
	}
	
	/**store hex color on the map grid*/
	public static void setHexMap(int ax, int ay, String hex){
		if (ax <= 15)
			hexMap[Calc.getGridPosition(ax, ay, 16)] = Calc.formatColor(hex);
	}
	/**get hex color from map grid*/
	public static String getHexMap(int ax, int ay){
		if (roomX <= 15)
			return hexMap[Calc.getGridPosition(ax, ay, 16)];
		else
			return randomColor();
	} 
	
	/**store icon on the map grid*/
	public static void setIconMap(int ax, int ay, int icon){
		if (ax <= 15){
			if (getIconMap(ax, ay) < icon || icon == 0)
				symMap[Calc.getGridPosition(ax, ay, 16)] = icon;
		}
	}
	/**get icon from map grid*/
	public static int getIconMap(int ax, int ay){
		if (roomX <= 15)
			return symMap[Calc.getGridPosition(ax, ay, 16)];
		else
			return (int)Calc.random(8);
	}
	/**this will refresh the map icon for the current room!*/
	public static void refreshIconMap(){
		setIconMap(roomX, roomY, 0);
		ArrayList<MapIconInfluence> list = Scene.getEntityList(MapIconInfluence.class);
		for (int i=0; i<list.size(); i++){
			list.get(i).refreshIcon();
		}
	}
	
	public static boolean passesKeyTest(int k){
		int[] keys = new int[]{LEFT, RIGHT, UP, DOWN, JUMP, PAUSE, ACTION, INTERACT};
		for (int i=0; i<which; i++){
			if (k == keys[i] && !(which == 4 && i == 2))
				return false;
		}
		return true;
	}
	
	/**set a control key to an input key*/
	public static void keyReceive(int k){
		if (passesKeyTest(k)){
			Sound.playPitched("sMenuKeyReceive", .05);
			
			if (which==0)
				LEFT = k;
			else if (which==1)
				RIGHT = k;
			else if (which==2)
				UP = k;
			else if (which==3)
				DOWN = k;
			else if (which==4)
				JUMP = k;
			else if (which==5)
				PAUSE = k;
			else if (which==6)
				ACTION = k;
			else if (which==7)
				INTERACT = k;
			
			awaitKey = false;
		}
		else{
			Sound.playPitched("sDishError", .05);
			MenuControls.me.error = 6;
		}
	}
	
	/**write the controls to a stupid file*/
	public static void writeKeys(){
		try {
			File f = new File(Main.DIRECTORY, controls);
			FileWriter w;
		
			w = new FileWriter(f);
			PrintWriter p = new PrintWriter(w);
			
			p.println(LEFT);
			p.println(RIGHT);
			p.println(UP);
			p.println(DOWN);
			p.println(JUMP);
			p.println(PAUSE);
			p.println(ACTION);
			p.println(INTERACT);
			
			p.close();
			w.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**read the controls from a stupid file*/
	public static void readKeys(){
		try {
			File f = new File(Main.DIRECTORY, controls);
			FileReader w;
		
			w = new FileReader(f);
			BufferedReader p = new BufferedReader(w);
			
			String s = p.readLine();
			for(int a=0; a<8; a++){
				int b = Calc.parseInt(s);
				
				if (a==0)
					LEFT = b;
				else if (a==1)
					RIGHT = b;
				else if (a==2)
					UP = b;
				else if (a==3)
					DOWN = b;
				else if (a==4)
					JUMP = b;
				else if (a==5)
					PAUSE = b;
				else if (a==6)
					ACTION = b;
				else if (a==7)
					INTERACT = b;
				
				s = p.readLine();
			}
				
			
			p.close();
			w.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**whether to fade in or fade out boss blocks*/
	public static void blockFade(boolean b){
		ArrayList<BossBlock> list = Scene.getEntityList(BossBlock.class);
		for (int i=0; i<list.size(); i++){
			list.get(i).fadeIn = b;
		}
	}
	
	/**refill all stats*/
	public static void recharge(){
		playerHealth = playerMaxHealth;
		playerBombs = playerMaxBombs;
		playerBullets = playerMaxBullets;
		Hud.randomizeColor();
	}
	
	/**sets location to draw an interact symbol on-screen to given ax and ay values*/
	public static void drawInteract(double ax, double ay){
		actionVis = true;
		actionX = ax;
		actionY = ay;
	}
	
	/**sets location to draw a down symbol on-screen to given ax and ay values*/
	public static void drawDown(double ax, double ay){
		downVis = true;
		downX = ax;
		downY = ay;
	}
	
	/**return a random color in string format*/
	public static String randomColor(){
		return Calc.makeHexColor(Calc.random(1),Calc.random(1),Calc.random(1));
	}
	
	/**visual explosion at ax, ay coordinates with scale of s*/
	public static void explosionEffect(double ax, double ay, double s){
		if (roomX != 21){	
			//small
			for (int i=0; i<12; i++){
				Explosion j = new Explosion(ax,ay);
				j.xscale *= .5 * s;
				j.yscale = j.xscale;
				j.imageSpeed *= 1.2;
				j.setSpeed(j.getSpeed()*2);
			}
			//big
			for (int i=0; i<3; i++){
				Explosion j = new Explosion(ax,ay);
				j.xscale = s;
				j.yscale = s;
			}
		}
		else{
			for (int i=0; i<6; i++){
				DreamHeartParticles j = new DreamHeartParticles(ax, ay, Calc.random(360), Integer.MIN_VALUE + 10);
				j.xscale *= 1.75;
				j.yscale = j.xscale;
				j.mult = .85;
				j.setSpeed(j.getSpeed() * 1.5);
			}
			for (int i=0; i<24; i++){
				DreamHeartParticles j = new DreamHeartParticles(ax, ay, Calc.random(360), Integer.MIN_VALUE + 10);
				j.xscale *= 1.2;
				j.yscale = j.xscale;
				j.mult = .75;
				j.setSpeed(j.getSpeed() * 1.2);
			}
		}
	}
	
	public static double bossesKilled(){
		return Calc.boolToInt(Global.event[EV.BANAAN_DEFEAT] > 0) + Calc.boolToInt(Global.event[EV.JELLYBOSS] > 0) + Calc.boolToInt(Global.event[EV.SKULL_DEFEAT] > 0) + Calc.boolToInt(Global.event[EV.SLICK_DEFEAT] > 0) + Calc.boolToInt(Global.event[EV.PUKE_DEFEAT] > 0) + Calc.boolToInt(Global.event[EV.DUKE_DEFEAT] > 0) + Calc.boolToInt(Global.event[EV.PAPA_DEFEAT] > 0);
	}
	
	/**visual explosion at ax, ay coordinates with scale of s*/
	public static void jellyExplosionEffect(double ax, double ay, double s){
		jellyExplosionEffect(ax, ay, s, false);
	}
	
	/**visual explosion at ax, ay coordinates with scale of s*/
	public static void jellyExplosionEffect(double ax, double ay, double s, boolean p){
		//small
		for (int i=0; i<12; i++){
			JellyExplosion j = new JellyExplosion(ax,ay,p);
			j.xscale *= .5 * s;
			j.yscale = j.xscale;
			j.imageSpeed *= 1.2;
			j.setSpeed(j.getSpeed()*2);
		}
		//big
		for (int i=0; i<3; i++){
			JellyExplosion j = new JellyExplosion(ax,ay,p);
			j.xscale = s;
			j.yscale = s;
		}
	}
	
	/**visual spark at ax, ay coordinates with scale of s*/
	public static void sparkEffect(double ax, double ay, double s){
		//small
		for (int i=0; i<12; i++){
			Spark j = new Spark(ax,ay);
			j.xscale *= .75 * s;
			j.yscale = j.xscale;
			j.imageSpeed *= 1.2;
			j.setSpeed(j.getSpeed()*1.3);
		}
		//big
		for (int i=0; i<3; i++){
			Spark j = new Spark(ax,ay);
			j.xscale = s * .5;
			j.yscale = s * .5;
		}
	}
	
	public static void explosion(double ax, double ay){
		explosion(ax, ay, 50);
	}
	
	/**make an explosion that damages enemies, hurts players, and breaks blocks and jars*/
	public static void explosion(double ax, double ay, double dmg){
		explosionEffect(ax, ay, 1);
		
		//destroy any rocks nearby
		ArrayList<BombRock> list = Scene.getEntityList(BombRock.class);
		for (int i=0; i<list.size(); i++){
			if (Calc.pointDistance(list.get(i).x+16,list.get(i).y+16,ax,ay) < 96){
				list.get(i).shatter();
			}
		}
		//destroy any jars nearby
		ArrayList<Jar> lizt = Scene.getEntityList(Jar.class);
		for (int i=0; i<lizt.size(); i++){
			if (Calc.pointDistance(lizt.get(i).x+16,lizt.get(i).y+16,ax,ay) < 96){
				lizt.get(i).shatter();
			}
		}
		//damage any enemies nearby
		ArrayList<BasicEnemy> liat = Scene.getEntityList(BasicEnemy.class);
		for (int i=0; i<liat.size(); i++){
			BasicEnemy me = liat.get(i);
			
			double xx = 0, yy = 0;
			Sprite s = me.sprite;
			int o = me.orientation;
			
			if (o == Sprite.WEST || o == Sprite.NORTHWEST || o == Sprite.SOUTHWEST)
				xx = s.imageWidth / 2.0;
			else if (o == Sprite.EAST || o == Sprite.NORTHEAST || o == Sprite.SOUTHEAST)
				xx = -s.imageWidth / 2.0;
			
			if (o == Sprite.NORTH || o == Sprite.NORTHWEST || o == Sprite.NORTH)
				yy = s.imageWidth / 2.0;
			else if (o == Sprite.SOUTH || o == Sprite.SOUTHWEST || o == Sprite.SOUTHEAST)
				yy = -s.imageWidth / 2.0;
			
			if (Calc.pointDistance(me.x+xx,me.y+yy,ax,ay) < 96){
				me.bombDamage();
			}
		}
		
		//damage any npcs nearby
		ArrayList<BasicNpc> lipt = Scene.getEntityList(BasicNpc.class);
		for (int i=0; i<lipt.size(); i++){
			BasicNpc me = lipt.get(i);
			
			double xx = 0, yy = 0;
			Sprite s = me.sprite;
			int o = me.orientation;
			
			if (o == Sprite.WEST || o == Sprite.NORTHWEST || o == Sprite.SOUTHWEST)
				xx = s.imageWidth / 2.0;
			else if (o == Sprite.EAST || o == Sprite.NORTHEAST || o == Sprite.SOUTHEAST)
				xx = -s.imageWidth / 2.0;
			
			if (o == Sprite.NORTH || o == Sprite.NORTHWEST || o == Sprite.NORTH)
				yy = s.imageWidth / 2.0;
			else if (o == Sprite.SOUTH || o == Sprite.SOUTHWEST || o == Sprite.SOUTHEAST)
				yy = -s.imageWidth / 2.0;
			
			if ((Calc.pointDistance(me.x+xx,me.y+yy,ax,ay) < 96) || ((roomX == 9 && roomY == 0 && Calc.pointDistance(me.x, me.y, ax, ay) < 128)))
				me.bombDamage();
		}
		
		//destroy any rocks nearby
		ArrayList<JellyBigBubble> liyt = Scene.getEntityList(JellyBigBubble.class);
		for (int i=0; i<liyt.size(); i++){
			if (Calc.pointDistance(liyt.get(i).x,liyt.get(i).y,ax,ay) < 96){
				liyt.get(i).shatter();
			}
		}
		//hurt player if too close
		if (Calc.pointDistance(Player.me.x,Player.me.y,ax,ay) < 96)
			Player.hurtPlayer((int)dmg);
	}
	
	public static void refreshRain(){
		//adjust rain (for rocks)
		ArrayList<NewRain> bist = Scene.getEntityList(NewRain.class);
		for (int i=0; i<bist.size(); i++)
			bist.get(i).refresh();
	}
	
	/**deactivate game objects*/
	public static void deactivateGameplayObjects(){
		ArrayList<Entity> list = Scene.getCollisionGroupList(DEACTIVATEME);
		for (int i=0; i<list.size(); i++){
			list.get(i).stepActive = false;
		}
	}
	
	/**activate game objects*/
	public static void activateGameplayObjects(){
		ArrayList<Entity> list = Scene.getCollisionGroupList(DEACTIVATEME);
		for (int i=0; i<list.size(); i++){
			list.get(i).stepActive = true;
		}
	}
	
	public static void toDeath(){
		//Main.animator.setUpdateFPSFrames(1, null);
		Main.animator.resetFPSCounter();
		Global.mainActive = false;
		Global.deactivate();
		Player.me.destroy();
		Global.playerDead = false;
		Root.changeRoom("c");
	}
	
	public void render(){
		if (mainActive)
			subRender();
	}
	
	public void subRender(){
		Player.me.drawAir();
		
		ArrayList<TextSign> list = Scene.getEntityList(TextSign.class);
		for (int i=0; i<list.size(); i++)
			list.get(i).drawBubble();
		
		ArrayList<Door> biss = Scene.getEntityList(Door.class);
		for (int i=0; i<biss.size(); i++)
			biss.get(i).drawBubble();
		
		ArrayList<BasicNpc> boss = Scene.getEntityList(BasicNpc.class);
		for (int i=0; i<boss.size(); i++)
			boss.get(i).drawBubble();
		
		if (actionVis)
			action.render((int)actionImg,Sprite.SOUTH,actionX-1+Calc.random(2),actionY-1+Calc.random(2),1,1,0,1,1,1,1);
		actionVis = false;
		
		if (downVis)
			down.render((int)actionImg,Sprite.SOUTH,downX-1+Calc.random(2),downY-1+Calc.random(2),1,1,0,1,1,1,1);
		downVis = false;
	}

}
