package world.control;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import audio.Audio;

import world.Root;
import world.player.Player;

import main.Calc;
import main.Main;
import main.Scene;

public class SaveFile{
	/**this will save the game file using all of the fantastic information found in the Global class*/
	public static void save(){
		Player.me.seed = 1;
		
		try {
			//write the main save file!
			File f = new File(Main.DIRECTORY, "res/data/crazd2save"+Global.saveFileNumber+".piss");
			FileWriter w;
		
			w = new FileWriter(f);
			PrintWriter p = new PrintWriter(w);
			p.println(Global.identifier);
			p.println(Player.me.seed);
			p.println(Global.heroMode);
			p.println(Calc.arrayToString(Global.tweetText,"="));
			p.println(Calc.arrayToString(Global.tweetTimestamp,"="));
			p.println(Global.dream);
			p.println(Global.mute);
			p.println(Global.screenMode);
			p.println(Global.lastX);
			p.println(Global.lastY);
			p.println(Global.roomX);
			p.println(Global.roomY);
			p.println(Global.playerX);
			p.println(Global.playerY);
			p.println(Global.totalSaves);
			p.println(Global.alwaysShowHud);
			p.println(Global.gotDuck);
			p.println(Global.gotWallSlide);
			p.println(Global.gotGun);
			p.println(Global.gotBombs);
			p.println(Global.gotDoors);
			p.println(Global.gotMapDoor);
			p.println(Global.gotMapIcons);
			p.println(Global.gotGlasses);
			p.println(Global.gotBoots);
			p.println(Global.gotStench);
			p.println(Global.gotClover);
			p.println(Global.runSpeed);
			p.println(Global.jumpSpeed);
			p.println(Global.selectedWeapon);
			p.println(Global.playerMaxBullets);
			p.println(Global.playerBullets);
			p.println(Global.playerBulletRefillTimer);
			p.println(Global.playerMaxBombs);
			p.println(Global.playerBombs);
			p.println(Global.playerMaxHealth);
			p.println(Global.playerHealth);
			p.println(Global.glassesEnabled);
			p.println(Global.playerDoors);
			p.println(Global.playerDoorColor);
			p.println(Global.doorSetsPlaced);
			p.println(Global.doorsPlaced);
			p.println(Global.doorSetsPurchased);
			p.println(Global.money);
			p.println(Global.luck);
			p.println(Global.stepSound);
			p.println(Global.heldKey);
			//KEY PICKUPS!!!! DOOR UNLOCKS!!!! KEY DROPS!!!!
			p.println(Calc.arrayToString(Global.pickedUp));
			p.println(Calc.arrayToString(Global.unlocked));
			p.println(Calc.arrayToString(Global.dropRmX,"YIFF"));
			p.println(Calc.arrayToString(Global.dropRmY,"IN"));
			p.println(Calc.arrayToString(Global.dropX,"HELL"));
			p.println(Calc.arrayToString(Global.dropY,"KILL"));
			p.println(Calc.arrayToString(Global.dropColor,"="));
			//PLAYER DOORS!!!!!! (MAY BE TOTALLY REMOVED IN FINAL VERSION!!!!! WHO CARES!!!! LETS CONSIDER THIS BONUS CONTENT FOR HACKERS!!!!)
			p.println(Calc.arrayToString(Global.doorColor,"="));
			p.println(Calc.arrayToString(Global.doorRmX,"sAtAn"));
			p.println(Calc.arrayToString(Global.doorRmY,"i"));
			p.println(Calc.arrayToString(Global.doorX,"w"));
			p.println(Calc.arrayToString(Global.doorY,"a"));
			p.println(Calc.arrayToString(Global.doorToRmX,"n"));
			p.println(Calc.arrayToString(Global.doorToRmY,"t"));
			p.println(Calc.arrayToString(Global.doorToX,"ki"));
			p.println(Calc.arrayToString(Global.doorToY,"ss"));
			//I DONT EVEN CARE!!!! BUT THESE ARE A BUNCH OF FUCKING ARRAYS (HEARTS, GEMS, EVENTS, EVENT ITEMS, MAP DOOR LINES, MAP COLORS, MAP SYMBOLS)
			p.println(Calc.arrayToString(Global.gemGot));
			p.println(Calc.arrayToString(Global.dreamCleared));
			p.println(Calc.arrayToString(Global.mapDoorX,"d"));
			p.println(Calc.arrayToString(Global.mapDoorY,"a"));
			p.println(Calc.arrayToString(Global.mapDoorToX,"s"));
			p.println(Calc.arrayToString(Global.mapDoorToY,"s"));
			p.println(Calc.arrayToString(Global.heartGot));
			p.println(Calc.arrayToString(Global.butterfly));
			p.println(Calc.arrayToString(Global.event,"m"));
			p.println(Calc.arrayToString(Global.eventItem,"a"));
			p.println(Calc.arrayToString(Global.hexMap,"="));
			p.println(Calc.arrayToString(Global.symMap,"n"));
			//GREAT
			p.println(Global.currentDream);
			p.println(Global.teleport);
			
			p.close();
			w.close();
			
			/**-------------------------------------------------------------------------
			OKAY LETS WRITE THE RUNSETTINGS (TO BE USED UPON RUNNING OF THE GAME [WOW!])
			--------------------------------------------------------------------------*/
			f = new File(Main.DIRECTORY, "res/data/runsettings.ass");
			w = new FileWriter(f);
			p = new PrintWriter(w);
			
			p.println("MUTE:");
			p.println(Global.mute);
			p.println("SCREEN MODE (0 = WINDOWED, 1 = FULLSCREEN):");
			p.println(Global.screenMode);
			
			p.close();
			w.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		tagWrite(Global.saveFileNumber - 1, Global.currentArea.toUpperCase() +" - " + Global.getPercentage());
		saveStats();
	}
	
	public static String tagRead(int i){
		File f = new File(Main.DIRECTORY, "res/data/tags.hema");
		String s = "";
		
		try {
			FileReader z = new FileReader(f);
			BufferedReader r = new BufferedReader(z);
			
			for (int j=0; j<i; j++)
				r.readLine();
			
			s = r.readLine();

			z.close();
			r.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return s;
	}
	
	public static void tagInit(){
		File f = new File(Main.DIRECTORY, "res/data/tags.hema");
		
		try {
			FileWriter b = new FileWriter(f);
			PrintWriter w = new PrintWriter(b);
			
			for (int i=0; i<3; i++)
				w.println("");
			
			b.close();
			w.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void tagWrite(int i, String s){
		File f = new File(Main.DIRECTORY, "res/data/tags.hema");
		
		String[] fck = new String[3];
		fck[0] = tagRead(0);
		fck[1] = tagRead(1);
		fck[2] = tagRead(2);
		
		try {
			FileWriter b = new FileWriter(f);
			PrintWriter w = new PrintWriter(b);
			
			for (int j=0; j<3; j++){
				if (j != i)
					w.println(fck[j]);
				else
					w.println(s);
			}
			
			b.close();
			w.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**this will call the Global.initVars() method to wipe all existing game data, load a specific game file with the supplied arguments, and go to the indicated room in the save file*/
	public static void load(int i){
		Global.saveFileNumber = i;
		
		int oldScreen = Global.screenMode;
		boolean oldMute = Global.mute;
		
		Global.initVars(i, false);
		
		boolean a = new File(Main.DIRECTORY, "res/data/crazd2fuckshitfile"+i+".FUCK").exists();
		
		if (a){
			Global.roomX = 0;
			Global.roomY = 0;
			Global.lastX = -1;
			Global.lastY = -1;
			Global.playerX = 96;
			Global.playerY = 368;
			
			try {
				File f = new File(Main.DIRECTORY, "res/data/crazd2save"+Global.saveFileNumber+".piss");
				FileReader r = new FileReader(f);
				BufferedReader p = new BufferedReader(r);
				p.readLine();
				p.readLine();
				Global.heroMode = Calc.parseBoolean(p.readLine());
				p.close();
				r.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
			if (Scene.getEntityList(Player.class).size() == 0)
				new Player(0,0);
			
			try {
				File f = new File(Main.DIRECTORY, "res/data/crazd2save"+Global.saveFileNumber+".piss");
				FileReader r = new FileReader(f);
				BufferedReader p = new BufferedReader(r);
				//LOADING BEGINS!!!!!!!!!!!!
				Global.identifier = Calc.parseInt(p.readLine());
				Player.me.seed = Calc.parseDouble(p.readLine());
				Global.heroMode = Calc.parseBoolean(p.readLine());
				Global.tweetText = Calc.stringToStringArray(p.readLine(), "=");
				Global.tweetTimestamp = Calc.stringToStringArray(p.readLine(), "=");
				Global.dream = Calc.parseBoolean(p.readLine());
				Global.mute = Calc.parseBoolean(p.readLine());
				Global.screenMode = Calc.parseInt(p.readLine());
				Global.lastX = Calc.parseInt(p.readLine());
				Global.lastY = Calc.parseInt(p.readLine());
				Global.roomX = Calc.parseInt(p.readLine());
				Global.roomY = Calc.parseInt(p.readLine());
				Global.playerX = Calc.parseDouble(p.readLine());
				Global.playerY = Calc.parseDouble(p.readLine());
				Global.totalSaves = Calc.parseInt(p.readLine());
				Global.alwaysShowHud = Calc.parseBoolean(p.readLine());
				Global.gotDuck = Calc.parseBoolean(p.readLine());
				Global.gotWallSlide = Calc.parseBoolean(p.readLine());
				Global.gotGun = Calc.parseBoolean(p.readLine());
				Global.gotBombs = Calc.parseBoolean(p.readLine());
				Global.gotDoors = Calc.parseBoolean(p.readLine());
				Global.gotMapDoor = Calc.parseBoolean(p.readLine());
				Global.gotMapIcons = Calc.parseBoolean(p.readLine());
				Global.gotGlasses = Calc.parseBoolean(p.readLine());
				Global.gotBoots = Calc.parseBoolean(p.readLine());
				Global.gotStench = Calc.parseBoolean(p.readLine());
				Global.gotClover = Calc.parseBoolean(p.readLine());
				Global.runSpeed = Calc.parseDouble(p.readLine());
				Global.jumpSpeed = Calc.parseDouble(p.readLine());
				Global.selectedWeapon = Calc.parseInt(p.readLine());
				Global.playerMaxBullets = Calc.parseInt(p.readLine());
				Global.playerBullets = Calc.parseInt(p.readLine());
				Global.playerBulletRefillTimer = Calc.parseInt(p.readLine());
				Global.playerMaxBombs = Calc.parseInt(p.readLine());
				Global.playerBombs = Calc.parseInt(p.readLine());
				Global.playerMaxHealth = Calc.parseInt(p.readLine());
				Global.playerHealth = Calc.parseInt(p.readLine());
				Global.glassesEnabled = Calc.parseBoolean(p.readLine());
				Global.playerDoors = Calc.parseInt(p.readLine());
				Global.playerDoorColor = p.readLine();
				Global.doorSetsPlaced = Calc.parseInt(p.readLine());
				Global.doorsPlaced = Calc.parseInt(p.readLine());
				Global.doorSetsPurchased = Calc.parseInt(p.readLine());
				Global.money = Calc.parseInt(p.readLine());
				Global.luck = Calc.parseDouble(p.readLine());
				Global.stepSound = p.readLine();
				Global.heldKey = p.readLine();
				Global.pickedUp = Calc.stringToBooleanArray(p.readLine());
				Global.unlocked = Calc.stringToBooleanArray(p.readLine());
				Global.dropRmX = Calc.stringToIntArray(p.readLine(), "YIFF");
				Global.dropRmY = Calc.stringToIntArray(p.readLine(), "IN");
				Global.dropX = Calc.stringToDoubleArray(p.readLine(), "HELL");
				Global.dropY = Calc.stringToDoubleArray(p.readLine(), "KILL");
				Global.dropColor = Calc.stringToStringArray(p.readLine(), "=");
				Global.doorColor = Calc.stringToStringArray(p.readLine(), "=");
				Global.doorRmX = Calc.stringToIntArray(p.readLine(), "sAtAn");
				Global.doorRmY = Calc.stringToIntArray(p.readLine(), "i");
				Global.doorX = Calc.stringToDoubleArray(p.readLine(), "w");
				Global.doorY = Calc.stringToDoubleArray(p.readLine(), "a");
				Global.doorToRmX = Calc.stringToIntArray(p.readLine(), "n");
				Global.doorToRmY = Calc.stringToIntArray(p.readLine(), "t");
				Global.doorToX = Calc.stringToDoubleArray(p.readLine(), "ki");
				Global.doorToY = Calc.stringToDoubleArray(p.readLine(), "ss");
				Global.gemGot = Calc.stringToBooleanArray(p.readLine());
				Global.dreamCleared = Calc.stringToBooleanArray(p.readLine());
				Global.mapDoorX = Calc.stringToIntArray(p.readLine(), "d");
				Global.mapDoorY = Calc.stringToIntArray(p.readLine(), "a");
				Global.mapDoorToX = Calc.stringToIntArray(p.readLine(), "s");
				Global.mapDoorToY = Calc.stringToIntArray(p.readLine(), "s");
				Global.heartGot = Calc.stringToBooleanArray(p.readLine());
				Global.butterfly = Calc.stringToBooleanArray(p.readLine());
				Global.event = Calc.stringToIntArray(p.readLine(), "m");
				Global.eventItem = Calc.stringToIntArray(p.readLine(), "a");
				Global.hexMap = Calc.stringToStringArray(p.readLine(), "=");
				Global.symMap = Calc.stringToIntArray(p.readLine(), "n");
				Global.currentDream = Calc.parseInt(p.readLine());
				Global.teleport = Calc.parseBoolean(p.readLine());
				
				p.close();
				r.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		Hud.me.currentMoney = Global.money;
		
		//mute if the settings changed upon loading the file!
		if (oldMute != Global.mute){
			if (Global.mute)
				Audio.setGlobalGain(0);
			else
				Audio.setGlobalGain(1);
		}
		//change full/window if the settings changed upon loading the file!
		if (oldScreen != Global.screenMode)
			Global.screenMode(Global.screenMode);
		
		if (!a){
			loadStats(Global.saveFileNumber);
			Hud.me.currentMoney = Global.money;
			
			Player.me.x = Global.playerX;
			Player.me.y = Global.playerY;
			Root.changeRoom(Global.roomX,Global.roomY);
			Player.release();
		}
		else{
			Root.changeRoom("prep");
		}
		//POST!!!!!!
	}
	
	/**this updates the hearts in a save file. used for ending*/
	public static void saveHearts(){
		rewriteLine(new File(Main.DIRECTORY, "res/data/crazd2save"+Global.saveFileNumber+".piss"), 35, ""+Global.playerMaxHealth);
		rewriteLine(new File(Main.DIRECTORY, "res/data/crazd2save"+Global.saveFileNumber+".piss"), 36, ""+Global.playerHealth);
		rewriteLine(new File(Main.DIRECTORY, "res/data/crazd2save"+Global.saveFileNumber+".piss"), 69, Calc.arrayToString(Global.heartGot));
	}
	
	public static void rewriteLine(File f, int line, String newLine){
		ArrayList<String> lines = new ArrayList<String>();
		FileReader r;
		try {
			r = new FileReader(f);
			BufferedReader p = new BufferedReader(r);
			
			String s;
			while((s = p.readLine()) != null)
				lines.add(s);
			
			r.close();
			p.close();
			
			FileWriter z = new FileWriter(f);
			PrintWriter w = new PrintWriter(z);
			
			lines.set(line, newLine);
			for (int i=0; i<lines.size(); i++)
				w.println(lines.get(i));
			
			z.close();
			w.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**this will save statistics that should be kept regardless of deaths or restarts (e.g. number of deaths, play time)*/
	public static void saveStats(){
		try {
			File f = new File(Main.DIRECTORY, "res/data/crazd2stats"+Global.saveFileNumber+".cock");
			FileWriter w;
		
			w = new FileWriter(f);
			PrintWriter p = new PrintWriter(w);
			
			p.println(Global.totalDeaths);
			p.println(Global.framesPlayed);
			p.println(Global.framesPaused);
			
			p.close();
			w.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**this will load statistic that should be kept regardless of deaths or restarts (e.g. number of deaths, play time)*/
	public static void loadStats(int i){
		try {
			File f = new File(Main.DIRECTORY, "res/data/crazd2stats"+i+".cock");
			FileReader r = new FileReader(f);
			BufferedReader p = new BufferedReader(r);

			Global.totalDeaths = Calc.parseInt(p.readLine());
			Global.framesPlayed = Calc.parseLong(p.readLine());
			Global.framesPaused = Calc.parseLong(p.readLine());
			
			p.close();
			r.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
