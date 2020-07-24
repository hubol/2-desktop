package world.intro;

import graphics.Graphics;
import graphics.Shape;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import audio.Audio;

import world.Root;
import world.control.Global;
import world.control.NewGen;
import world.control.Sound;
import world.control.SoundLoader;
import world.control.SpriteLoader;

import main.Entity;
import main.Input;

public class Intro extends Entity{
	public NewGen myGen;
	public static Intro me;
	
	public String BACK, LINE;
	
	public int[] pattern = new int[4];
	public int timer, at;
	
	public ArrayList<String> birthdays;
	public String birthday;
	
	public Intro(int a){
		super(0, 0);
		
		me = this;
		
		birthdays = new ArrayList<String>();
		birthday = "";
		initBirthdays();
		
		BACK = "#FFFFFF";
		LINE = "#FFFFFF";
		myGen = new NewGen(3.53719,2.14,6.375,3.29,.12,(double)a * (double)a * 6.7297);
		
		String dark = "#0094CF";//(String)myGen.choose("#007CFF", "#FF2339", "#FF2714", "#30BA7C", "#2BC9BA", "#A55BFF", "#FF70FF");
		
		if (myGen.random(1) > .5)
			LINE = dark;
		else
			BACK = dark;
		
		Global.menuBackgroundColor = LINE;
		Global.menuLineColor = BACK;
		
		setDepth(50);
		
		for (int i=0; i<4; i++)
			pattern[i] = (Integer)myGen.choose(4, 4, 8, 8, 8, 16);
		
		new SoundLoader(false, "sIntroCover"); //TODO this will be for covering up the screen with LINE color
		
		//TODO DIFFERENT INTROS!!!!:
		if (!birthday.equals("")){
			new SpriteLoader("sIntro115_2","sIntroBlind_2");
			new Birthday(320,240);
		}
		else{
			int which = 1 + (int)myGen.random(9);
			//which = 9;
			//int which = 8;
			
			if (which == 0){
				new SoundLoader(false, "sIntroKey","sBanaanKopiAvOw","sBanaanKopiEnd");
				new SpriteLoader("sIntroHubolFont_9");
				new TypeDass();
			}
			else if (which == 1){
				new SoundLoader(false, "sIntroT0","sIntroT1","sIntroT2","sIntroT3","sIntroT4","sIntroT5");
				new TypeName();
			}
			else if (which == 2){
				new SpriteLoader("sIntroHubol_2", "sIntroHeart_3");
				new SoundLoader(false, "sIntroHeartShake");
				new HeartIntro();
			}
			else if (which == 3){
				new SpriteLoader("sIntro115Run_4", "sIntroDassPlanet");
				new DassRunner();
			}
			else if (which == 4){
				new SpriteLoader("sIntroDassBlock", "sIntroLightning_2");
				new SoundLoader(false, "sIntroB0", "sIntroB1", "sIntroB2", "sIntroThunder");
				new DassBlockMake();
			}
			else if (which == 5){
				new SpriteLoader("sIntroHubolSlide_6");
				new SoundLoader(false, "sIntroClack");
				new SlideUh(0,0);
			}
			else if (which == 6){
				new SpriteLoader("sIntroPencil_2");
				new SoundLoader(false, "sIntroWrite0", "sIntroWrite1", "sIntroWrite2");
				new Pencil();
			}
			else if (which == 7){
				new SpriteLoader("sIntroSnake_7");
				new SoundLoader(false, "sIntroSnakeMove", "sIntroSnakeGet");
				new Snake(-64, 208);
				letter(0,0);
				if (myGen.random(1.0) < 0.5)
					letter(-64, -64);
				else
					letter(64, -64);
				
				if (myGen.random(1.0) < 0.5)
					letter(-64, 64);
				else
					letter(64, 64);
			}
			else if (which == 8){
				new SoundLoader(false, "sIntroStupid0", "sIntroStupid1", "sIntroStupid2", "sIntroStupid3", "sIntroStupid4");
				new Stupid();
			}
			else if (which == 9){
				new SoundLoader(false, "sIntroFallLand");
				new SpriteLoader("sIntroGrass_3", "sIntroHubolDust_2", "sIntroHubolFallBack_5", "sIntroHubolFallLine_5");
				new Fall();
			}
		}
		
		//LOAD SOME SOUNDS
		new SoundLoader(false, "sIntro0","sIntro1","sIntro2","sIntro3","sIntro4","sIntro5","sIntro6","sIntro7","sIntro8","sIntro9");
		at = -1;
		tone();
	}
	
	public void letter(int x, int y){
		new SnakeLetter(128 + x, 208 + y, 2);
		new SnakeLetter(128 + 64 + x, 208 + y, 3);
		new SnakeLetter(128 + 64 + 64 + x, 208 + y, 4);
		new SnakeLetter(128 + 64 + 64 + 64 + x, 208 + y, 5);
		new SnakeLetter(128 + 64 + 64 + 64 + 64 + x, 208 + y, 6);
	}
	
	public void tone(){
		String s = "sIntro" + (int)myGen.random(10);
		if (Audio.soundExists(s)){
			Audio.get(s).setPitch(.75 + myGen.random(.75));
			Sound.play(s);
		}
		at += 1;
		if (at > 3)
			at = 0;
		timer = pattern[at];
	}
	
	public void step(){
		if (Input.checkFrameKey(KeyEvent.VK_ESCAPE))
			Root.changeRoom("d");
		timer -= 1;
		if (timer <= 0)
			tone();
	}
	
	public void complete(){
		new IntroCover(0,0);
	}
	
	public void destroy(){
		me = null;
		super.destroy();
	}
	
	public void roomDestroy(){
		me = null;
		super.roomDestroy();
	}
	
	public void render(){
		Graphics.setAlpha(1);
		Graphics.setColor(BACK);
		Shape.drawRectangle(0,0,640,480);
	}
	
	public void initBirthdays(){
		birthdays.add("1-13-oddwarg");
		birthdays.add("1-19-hubol");
		birthdays.add("2-10-megan");
		birthdays.add("2-18-trey");
		birthdays.add("3-18-matt");
		birthdays.add("4-28-hillary");
		birthdays.add("5-2-titus");
		birthdays.add("5-6-2");
		birthdays.add("5-10-testdummy");
		birthdays.add("5-15-chuchino");
		birthdays.add("6-10-daphny");
		birthdays.add("7-15-erin");
		birthdays.add("9-5-yasha");
		birthdays.add("12-4-craz'd");
		birthdays.add("12-25-santa");
		birthdays.add("12-30-sam");
		for (int i=0; i<birthdays.size(); i++){
			String[] s = birthdays.get(i).split("-");

			if (Global.checkDate(s[0], s[1]))
				birthday = s[2];
		}
	}

}
