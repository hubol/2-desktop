package world.intro;

import graphics.Graphics;
import graphics.Shape;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import audio.Audio;

import world.control.Global;
import world.control.NewGen;
import main.Calc;
import main.Entity;
import main.Main;

public class IntroWait extends Entity{
	public int timer, aaa;
	public String BACK, LINE;
	public NewGen myGen;
	
	public boolean first = false;

	public IntroWait() {
		super(0,0);
		timer = 5;
		
		int a = 0;
		try {
			//if youve got a runs.ily file we can read this to get the seed
			File f = new File(Main.DIRECTORY, "res/data/runs.ily");
			if (f.exists()){
				FileReader r;
				r = new FileReader(f);
				BufferedReader p = new BufferedReader(r);
				a = Calc.parseInt(p.readLine());
				p.close();
				r.close();
			}
			//write your seed + 1
			FileWriter w = new FileWriter(f);
			PrintWriter q = new PrintWriter(w);
			q.println(a + 1);
			q.close();
			w.close();
			
			//OKAY!!! LETS DO THE SETTINGS THING!!!!
			
			int oldScreen = Global.screenMode;
			boolean oldMute = Global.mute;
			
			f = new File(Main.DIRECTORY, "res/data/runsettings.ass");
			if (!f.exists()){
				w = new FileWriter(f);
				PrintWriter p = new PrintWriter(w);
				
				Global.mute = false;
				Global.screenMode = 1;
				
				p.println("MUTE:");
				p.println(Global.mute);
				p.println("SCREEN MODE (0 = WINDOWED, 1 = FULLSCREEN):");
				p.println(Global.screenMode);
				
				p.close();
				w.close();
			}
			
			FileReader r = new FileReader(f);
			BufferedReader p = new BufferedReader(r);
			p.readLine();
			Global.mute = Calc.parseBoolean(p.readLine());
			p.readLine();
			Global.screenMode = Calc.parseInt(p.readLine());
			p.close();
			r.close();
			
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
			
			//SETTINGS THINGS END!!!!
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		aaa = a;
		first = (aaa == 0);
		
		BACK = "#FFFFFF";
		LINE = "#FFFFFF";
		myGen = new NewGen(3.53719,2.14,6.375,3.29,.12,(double)a * (double)a * 6.7297);
		
		String dark = "#0094CF";//(String)myGen.choose("#007CFF", "#FF2339", "#FF2714", "#30BA7C", "#2BC9BA", "#A55BFF", "#FF70FF");
		
		if (myGen.random(1) > .5)
			LINE = dark;
		else
			BACK = dark;
		
		new IntroOverlay((int)myGen.random(256));
		
		setDepth(51);
	}
	
	public void step(){
		timer -= 1;
		if (timer == 0){
			if (!first)
				new Intro(aaa);
			else
				new IntroTutorial();
			destroy();
		}
	}
	
	public void render(){
		Graphics.setAlpha(1);
		Graphics.setColor(BACK);
		Shape.drawRectangle(0, 0, 640, 480);
	}

}
