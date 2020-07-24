package world.menu;

import java.io.File;

import graphics.Font;
import graphics.Graphics;
import graphics.Shape;
import graphics.Sprite;
import graphics.Text;
import world.Root;
import world.control.Global;
import world.control.Hud;
import world.control.IO;
import world.control.NewGen;
import world.control.Pause;
import world.control.SaveFile;
import world.control.Shake;
import world.control.Sound;
import main.Calc;
import main.Entity;
import main.Input;
import main.Main;
import main.Scene;

public class Menu extends Entity{
	public Shake[] s;
	public int a;
	public double sin;
	public int twoImg;
	public double img;
	
	public int width;
	public boolean drawTwo, go;
	
	public MenuMusic music;
	
	public NewGen g;
	
	public boolean start;
	public double widthPercent;
	
	public double addY;
	
	public double at, to;
	
	public double selectionX;
	
	public int menuSelection, menuButtons;
	public String menuHeading;
	public String[] buttonText, buttonInfo;
	public int[] buttonCodeId;
	
	public int error;
	
	public Font f;
	
	public boolean active;
	public boolean escape = Scene.killOnEscape;
	
	public boolean beaten = false;
	
	public Shake z = new Shake(1.4);
	
	private final String versionText = "V"+(""+Global.REAL_VERSION).substring(0, 4), memoryText = Runtime.getRuntime().maxMemory() + "K";
	private double versionPosition = -128, memoryPosition = 128;
	private int versionTimer = 45;

	public Menu() {
		super(0, 0);
		s = new Shake[10];
		for (int i=0; i<s.length; i++)
			s[i] = new Shake(.3);
		s[0] = new Shake(.7);
		
		f = new Font("menufont", true, '-', '<');
		
		beaten = new File(Main.DIRECTORY, "res/data/hema.dongs").exists(); //TODO
		
		a = 0;
		sin = 0;
		
		at = 0;
		to = 0;
		
		active = true;
		
		Pause.me.reset();
		Hud.me.currentMoney = 0;
		
		selectionX = 96;
		
		addY = 0;
		
		error = 0;
		
		widthPercent = 0;
		start = true;
		menuSelection = 0;
		menuButtons = 0;
		
		g = new NewGen(1.1,1.21,1.33,1.46,1.6,Global.menuOverlay * 1.65);
		
		img = 0;
		
		go = false;
		
		clouds();
		
		music = new MenuMusic();
		
		alarmInitialize(3);
		two();
		grow();
		
		new MenuOverlay();
	}
	
	public void confirmSound(){
		if (Global.menuOverlay == 69)
			Sound.playPitched("sMenuFart", .05);
		else
			Sound.playPitched("sMenuConfirm", .05);
	}
	
	public void buttonConfirm(int id){
		boolean confirm = true;
		
		if (id == 69) //quit
			System.exit(0);
		else if (id == 68) //let's return to the main main main menu (BACK)
			toMainMenu();
		else if (id == 1){
			if (beaten) 
				setMenu(0, "SELECT DIFFICULTY MODE", new String[]{"NORMAL", "HEROINE", "BACK"}, new String[]{"THE NORMAL WAY TO PLAY", "ONE HIT KILL MODE", ""}, new int[]{2, 3, 68});
			else{
				Global.storedHeroine = false;
				beginNew();
			}
		}
		else if (id == 2 || id == 3){
			Global.storedHeroine = false;
			if (id == 3)
				Global.storedHeroine = true;
			beginNew();
		}
		else if (id >= 4 && id <= 6){
			File f = new File(Main.DIRECTORY, "res/data/crazd2save"+(id - 3)+".piss");
			if (f.exists())
				f.delete();
			
			f = new File(Main.DIRECTORY, "res/data/crazd2stats"+(id - 3)+".cock");
			if (f.exists())
				f.delete();
			
			Global.roomX = 0;
			Global.roomY = 0;
			Global.storedSaveFile = id - 3;
			Global.lastX = -1;
			Global.lastY = -1;
			Global.playerX = 96;
			Global.playerY = 368;
			Global.createStupidFuckshitFile(Global.storedSaveFile);
			SaveFile.tagWrite(Global.storedSaveFile - 1, "BEDROOM DREAM - 0%");
			Root.changeRoom("a");
		}
		else if (id == 7)
			beginLoad();
		else if (id >= 8 && id <= 10){
			//SaveFile.load(id - 7);
			Global.storedSaveFile = id - 7;
			Global.saveFileNumber = id - 7;
			Root.changeRoom("b");
		}
		else if (id == 72){
			toMainMenu();
			menuSelection = 1;
		}
		else if (id == 78){
			confirm = false;
		}
		else if (id == 100){
			active = false;
			new MenuControls(this);
		}
		
		if (confirm){
			confirmSound();
			widthPercent = 0;
		}
		else{
			if (Global.menuOverlay == 69)
				Sound.playPitched("sMenuFart", .1);
			else
				Sound.playPitched("sDishError", .05);
			error = 6;
		}
	}
	
	public void beginLoad(){
		String[] text = new String[4], info = new String[4];
		int[] code = new int[4];
		for (int i=0; i<3; i++){
			if (new File(Main.DIRECTORY, "res/data/crazd2save"+(i + 1)+".piss").exists()){
				text[i] = "SAVE FILE "+(i + 1);
				code[i] = 8 + i;
				info[i] = SaveFile.tagRead(i).replace('%', '<');
			}
			else{
				text[i] = "NO SAVE FILE";
				code[i] = 78;
				info[i] = "";
			}
		}
		
		text[3] = "BACK";
		info[3] = "";
		code[3] = 72;
		
		setMenu(0, "SELECT SAVE FILE TO PLAY", text, info, code);
	}
	
	public void beginNew(){
		String[] text = new String[4], info = new String[4];
		int[] code = new int[4];
		for (int i=0; i<3; i++){
			if (new File(Main.DIRECTORY, "res/data/crazd2save"+(i + 1)+".piss").exists()){
				text[i] = "OVERWRITE SAVE FILE "+(i + 1);
				info[i] = "SAVE FILE "+(i + 1)+" DATA WILL BE LOST";
			}
			else{
				text[i] = "NEW SAVE FILE "+(i + 1);
				info[i] = "SAVE FILE "+(i + 1)+" DATA WILL BE CREATED";
			}
			
			code[i] = 4 + i;
		}
		
		text[3] = "BACK";
		info[3] = "";
		if (new File(Main.DIRECTORY, "res/data/hema.dongs").exists()){
			code[3] = 1;
			menuSelection = Calc.boolToInt(Global.storedHeroine);
		}
		else
			code[3] = 68;
		
		setMenu(0, "SELECT SAVE FILE", text, info, code);
	}
	
	/**set the menu. initial position on menu, heading, text for buttons, additional info for buttons, code id for buttons*/
	public void setMenu(int initial, String heading, String[] text, String[] info, int[] code){
		menuSelection = initial;
		menuHeading = heading;
		buttonText = text;
		buttonInfo = info;
		buttonCodeId = code;
		menuButtons = text.length;
		selectionX = 96;
	}
	
	public void toMainMenu(){
		setMenu(0, "", new String[] {"NEW FILE", "CONTINUE FILE", "CONTROLS", "QUIT GAME"}, new String[] {"CREATE A FRESH GAME FILE", "LOAD A GAME FILE", "CONFIGURE CONTROLS", "RELIEVE YOURSELF"}, new int[] {1, 7, 100, 69});
	}
	
	public void clouds(){
		if (g.random(1) < .9){
			double hsp = g.rangedRandom(5);
			int amt = 1 + (int)g.random(4);
			for (int i=0; i<amt; i++)
				new MenuCloud(g.random(960), g.random(300), g.random(hsp), (int)g.random(4), (.8 + g.random(.21)) * (Double)Calc.choose(1.0, -1.0), .8 + g.random(.215));
		}
	}
	
	public void alarmEvent(int i){
		if (i == 0)
			two();
		if (i == 1)
			grow();
		if (i == 2)
			Scene.killOnEscape = escape;
	}
	
	public void grow(){
		if (width == 0){
			width = 84;
			flower();
		}
		else if (width == 84)
			width = 155;
		else if (width == 155){
			width = 231;
			flower();
		}
		else if (width == 231)
			width = 299;
		else if (width == 299){
			width = 313;
			flower();
		}
		else if (width == 313){
			width = 399;
			alarm[1] = 10;
			return;
		}
		else if (width == 399){
			flower();
			width = 400;
			drawTwo = true;
			if (Global.menuOverlay == 69)
				Sound.playPitched("sMenuFart", .05);
			else
				Sound.playPitched("sMenuTwo", .1);
			if (music.f.loaded)
				Sound.play(music.f.mySounds[0]);
			else
				music.f.playOnceLoaded[0] = true;
			//Sound.playPitched("sMenuGrow", .1);
			to = 1;
			alarm[1] = 20;
			return;
		}
		else if (width == 400){
			width = 540;
			go = true;
		}
		else
			return;
		
		Sound.playPitched("sMenuGrow", .1);
		alarm[1] = 3;
	}
	
	public void flower(){
		new MenuFlower(g.random(640), 480 + g.random(2), (.9 + g.random(.2)) * ((Double)g.choose(1.0, -1.0)), .9 + g.random(.2), g.random(9), .2 + g.random(1.5), g.random(60), (int)g.random(5), .2 + g.random(1), 1.6 + g.random(2.3), 1.6 + g.random(2.3), g.rangedRandom(8), (int)g.random(6));
		new MenuFlower(640 + g.random(2), g.random(480), (.9 + g.random(.2)) * ((Double)g.choose(1.0, -1.0)), .9 + g.random(.2), g.random(9), .2 + g.random(1.5), g.random(60), (int)g.random(5), .2 + g.random(1), 1.6 + g.random(2.3), 1.6 + g.random(2.3), 90 + g.rangedRandom(8), (int)g.random(6));
		new MenuFlower(g.random(640), -g.random(2), (.9 + g.random(.2)) * ((Double)g.choose(1.0, -1.0)), .9 + g.random(.2), g.random(9), .2 + g.random(1.5), g.random(60), (int)g.random(5), .2 + g.random(1), 1.6 + g.random(2.3), 1.6 + g.random(2.3), 180 + g.rangedRandom(8), (int)g.random(6));
		new MenuFlower(-g.random(2), g.random(480), (.9 + g.random(.2)) * ((Double)g.choose(1.0, -1.0)), .9 + g.random(.2), g.random(9), .2 + g.random(1.5), g.random(60), (int)g.random(5), .2 + g.random(1), 1.6 + g.random(2.3), 1.6 + g.random(2.3), 270 + g.rangedRandom(8), (int)g.random(6));
	}
	
	public void two(){
		twoImg = (int)Calc.random(7) - 1;
		alarm[0] = 1 + (int)Calc.random(20);
	}
	
	public void step(){
		at = Calc.approach(at, to, 2);
		
		if (go){
			if (start){
				start = false;
				toMainMenu();
				if (new File(Main.DIRECTORY, "res/data/crazd2save1.piss").exists() || new File(Main.DIRECTORY, "res/data/crazd2save2.piss").exists() || new File(Main.DIRECTORY, "res/data/crazd2save3.piss").exists())
					menuSelection = 1;
			}
			widthPercent = Math.min(1, (Calc.approach(widthPercent, 1, 5) + .04) * 1.3);
			
			a += 1;
			while (a > 59)
				a -= 60;
		}
		
		if (widthPercent >= 1 && IO.checkFrameKey(Global.INTERACT) && active){
			if (start){
				start = false;
				confirmSound();
				toMainMenu();
			}
			else{
				buttonConfirm(buttonCodeId[menuSelection]);
				//TODO stuff
			}
		}
		
		if (!start){
			addY = Math.max(-120, (Calc.approach(addY, -120, 20) - 4) * 1.14);
		}
		
		if (!start && active){
			if (IO.checkFrameKey(Global.UP)){
				menuSelection -= 1;
				selectionX = 0;
				if (menuSelection < 0)
					menuSelection = menuButtons - 1;
				if (Global.menuOverlay == 69)
					Sound.playPitched("sMenuFart", .05);
				else
					Sound.playPitched("sMenuUp", .05);
			}
			else if (IO.checkFrameKey(Global.DOWN)){
				menuSelection += 1;
				selectionX = 0;
				if (menuSelection >= menuButtons)
					menuSelection = 0;
				if (Global.menuOverlay == 69)
					Sound.playPitched("sMenuFart", .05);
				else
					Sound.playPitched("sMenuDown", .05);
			}
		}
		
		sin = Math.sin((a / 30.0) * Math.PI);
		
		img += 1.0 / 6.0;
		if (img >= 2)
			img -= 2;
		
		selectionX = Math.min(96, (Calc.approach(selectionX, 96, 3) + 9) * 1.1);
		
		alarmStep();
		
		if (error > 0)
			error -= 1;
		
		if (versionTimer > 0)
			versionTimer--;
		if (Input.checkKey(Global.RIGHT) || versionTimer > 0)
			versionPosition = Calc.approach(versionPosition, 6, 2);
		else
			versionPosition = Calc.approach(versionPosition, -128, 12);
		
		if (Input.checkKey(Global.LEFT))
			memoryPosition = Calc.approach(memoryPosition, -8, 2);
		else
			memoryPosition = Calc.approach(memoryPosition, 128, 12);
	}
	
	public void button(double x, double y, double width, double height, String color){
		if (width > 0){
			Graphics.setColor(color);
			Sprite.get("sMenuButton").render(0, Sprite.NORTHWEST, x, y, Math.min(1, (width / 64.0)), height / 32.0, 0, 1, color);
			Sprite.get("sMenuButton").render(1, Sprite.NORTHWEST, x + width - 32, y, Math.min(1, (width / 64.0)), height / 32.0, 0, 1, color);
			if (width > 64)
				Shape.drawRectangle(x + 32, y, x + width - 32, y + height);
		}
	}
	
	public void render(){
		drawTitle(320 + s[0].x + Calc.rangedRandom(.2), 240 + s[1].y + Calc.rangedRandom(.2) + addY);
		
		double xx = 0;
		if (error > 0)
			xx = -9 + (18 * (error % 2));
		
		if (active){
			Text.setFont(f);
			
			if (start)
				textButton(320 + s[3].x, 400 + s[3].y, "PRESS INTERACT TO START", Text.CENTERED, widthPercent, 24, Global.menuLineColor, Global.menuBackgroundColor);
			else if (menuButtons > 0){
				if (!menuHeading.equals(""))
					textButton(320 + s[3].x + xx, 240 + s[3].y, menuHeading, Text.CENTERED, widthPercent, 24, Global.menuLineColor, Global.menuBackgroundColor);
				for (int i=0; i<menuButtons; i++){
					if (menuSelection != i)
						textButton(64 + s[4 + i].x + xx, 288 + (i * 32) + s[4 + i].y, buttonText[i], Text.WEST, widthPercent, 24, Global.menuLineColor, Global.menuBackgroundColor);
					else{
						/*textButton(56 + selectionX + s[4 + i].x, 304 + (i * 32) + s[4 + i].y, buttonText[i], Text.WEST, widthPercent, 28, Global.menuLineColor, Global.menuBackgroundColor);
						textButton(72 + selectionX + s[4 + i].x, 304 + (i * 32) + s[4 + i].y, buttonText[i], Text.WEST, widthPercent, 28, Global.menuLineColor, Global.menuBackgroundColor);*/
						Graphics.setColor(Global.menuLineColor);
						Shape.drawRectangle(0, 288 + (i * 32) + s[4 + i].y - 12, 640, 288 + (i * 32) + s[4 + i].y + 12);
						textButton(64 + selectionX + s[4 + i].x + xx, 288 + (i * 32) + s[4 + i].y, buttonText[i], Text.WEST, widthPercent, 24, Global.menuBackgroundColor, Global.menuLineColor);
					}
				}
				
				if (!buttonInfo[menuSelection].equals(""))
					textButton(320 + s[9].x + xx, 440 + s[9].y, buttonInfo[menuSelection], Text.CENTERED, widthPercent, 24, Global.menuLineColor, Global.menuBackgroundColor);
			}
		}
		
		Graphics.setColor(Global.menuLineColor);
		Text.idiot = false;
		Text.orientation = Text.SOUTHWEST;
		Text.drawTextExt(versionPosition + s[9].x, 474 + s[9].y, versionText, .3, .3, 0);
		Text.orientation = Text.SOUTHEAST;
		Text.drawTextExt(640 + memoryPosition + s[9].x, 474 + s[9].y, memoryText, .3, .3, 0);
		Text.idiot = true;
	}
	
	public void drawTitle(double x, double y){
		drawTitleColor(x + 3, y + 3, true);
		drawTitleColor(x - 3, y + 3, true);
		drawTitleColor(x + 3, y - 3, true);
		drawTitleColor(x - 3, y - 3, true);
		drawTitleColor(x + 1.5, y + 1.5, true);
		drawTitleColor(x - 1.5, y + 1.5, true);
		drawTitleColor(x + 1.5, y - 1.5, true);
		drawTitleColor(x - 1.5, y - 1.5, true);
		drawTitleColor(x + 3.5, y, true);
		drawTitleColor(x - 3.5, y, true);
		drawTitleColor(x, y + 3.5, true);
		drawTitleColor(x, y - 3.5, true);
		
		drawTitleColor(x, y, false);
	}
	
	/**use orientation CENTERED or WEST*/
	public void textButton(double x, double y, String text, int orientation, double widthPercent, double height, String buttonColor, String textColor){
		double width = ((text.length() + 6) * 16) * widthPercent;
		String txt = text.substring(0, (int)Math.min(text.length(), Math.max(0, (width - 64.0) / 16.0)));
		double xx = x - 32, yy = y - (height / 2.0);
		if (orientation == Text.CENTERED)
			xx = x - (text.length() + 6) * 8;
		
		button(xx, yy, width, height, buttonColor);
		Text.idiot = false;
		Text.orientation = Text.WEST;
		Graphics.setColor(textColor);
		if (orientation != Text.CENTERED)
			x += 16;
		else{
			x = xx + 48;
		}
		Text.drawTextExt(x, y + 1, txt.toUpperCase(), .5, .5, 0);
		Text.idiot = true;
	}
	 
	public void drawTitleColor(double x, double y, boolean back){
		String c;
		if (!back)
			c = Global.menuLineColor;
		else
			c = Global.menuBackgroundColor;
		
		int o = Sprite.CENTERED;
		//Sprite.get("sTitleCrazd").renderPart(img, o, x + s[1].x, y + s[1].y, 0, 0, width, 104, 1, 1, 0, 1, c);
		x -= 178;
		
		if (drawTwo){
			double ang = sin * 10;
			Sprite.get("sTitleTwo").render(img, o, x + s[2].x + 178, y + s[2].y, at, at, ang, 1, c);
			Sprite.get("sTitleTwo").render(img + 2, o, x + s[2].x + 178, y + s[2].y, at, at, ang, 1, Global.menuBackgroundColor);
			if (twoImg > -1)
				Sprite.get("sTitleTwo").render(twoImg + 4, o, x + s[2].x + 178, y + s[2].y, at, at, ang, 1, c);
		}
	}

}
