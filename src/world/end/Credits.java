package world.end;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import graphics.Font;
import audio.Audio;
import world.Bg;
import world.Fg;
import world.Overlay;
import world.Root;
import world.control.Global;
import world.control.Music;
import world.player.Player;
import main.Entity;
import main.Input;
import main.Main;

public class Credits extends Entity{
	private String credits =
			"a game by hubol persson-gordon%" +
			"developed 30 june 2012%" +
			"through 6 may 2014%" +
			"%" +
			"--------------------------------------%" +
			"this game utilizes an engine designed%" +
			"under the guidance of the oddwarg.%" +
			"%" +
			"--------------------------------------%" +
			"this game features music by:%" +
			"hubol%" +
			"oddwarg%" +
			"guts.wav%" +
			"sam bowers%" +
			"ehafh%" +
			"c418%" +
			"lil b%" +
			"sanborn singers%" +
			"supa day%" +
			"studio pixel%" +
			"tiny tim%" +
			"the residents%" +
			"%" +
			"--------------------------------------%" +
			"this game features sound effects from:%" +
			"hubol%" +
			"oddwarg%" +
			"megan reiland%" +
			"freesound.org%" +
			"carmageddon%" +
			"descent 2%" +
			"shelby smith%" +
			"%" +
			"--------------------------------------%" +
			"this game was designed using:%" +
			"audacity%" +
			"eclipse%" +
			"game maker 7%" +
			"jsmooth%" +
			"notepad%" +
			"paint.net%" +
			"psycle modular music creation studio%" +
			"reaper%" +
			"sfxr%" +
			"sunvox%" +
			"voice memos%" +
			"yiffyaff 2.0%" +
			"%" +
			"--------------------------------------%" +
			"this game is inspired by:%" +
			"the works of the oddwarg%" +
			"the works of c418%" +
			"the works of the residents%" +
			"the works of biggt%" +
			"super mario sunshine%" +
			"moneyseize%" +
			"an untitled story%" +
			"uratama%" +
			"%" +
			"--------------------------------------%" +
			"special thanks to:%" +
			"oddvar hungnes%" +
			"sam bowers%" +
			"hillary gordon%" +
			"treyner wentzein%" +
			"titus turner%" +
			"chuchino%" +
			"badwolf%" +
			"ducko%" +
			"vghime%" +
			"quakster%" +
			"sschol%" +
			"ljradio%" +
			"aren%" +
			"%" +
			"--------------------------------------%" +
			"this game is dedicated to:%" +
			"megan reiland%" +
			"%" +
			"%" +
			"%" +
			"--------------------------------------%" +
			"if you enjoy this game,%" +
			"please consider donating!%" +
			"%" +
			"www.hubolhubolhubol.com%" +
			"2014%";
	private String[] credit;
	public final Font font = new Font("menufont", true, '-', '<');
	
	private int steps = 1;
	private int at = 0;

	private boolean done = false;
	private boolean unlocked = true;
	
	public Credits() {
		super(0, 0);
		
		File f = new File(Main.DIRECTORY, "res/data/hema.dongs");
		unlocked = !f.exists();
		try {
			f.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (unlocked)
			credits += "%%%you unlocked heroine mode!";
		credit = credits.split("%");
		steps = (int)(3450.0 / (double)(credit.length + 10));
		
		Music.changeMusic("musJellycat", false);
		
		new Two(-1);
		new CreditsPattern(.8);
		alarmInitialize(2);
		alarm[0] = 332;
	}
	
	public void alarmEvent(int i){
		if (i == 0){
			if (at < credit.length){
				new Credit(credit[at].toUpperCase(), this, -1);
				at++;
				alarm[0] = steps;
			}
			else
				alarm[1] = 300;
		}
		else
			end();
	}
	
	public void end(){
		Player.me.resetEnd();
		Fg.me.fade = false;
		Bg.me.fade = false;
		Overlay.me.fade = false;
		Fg.me.alpha = 1;
		Bg.me.alpha = 1;
		Overlay.me.alpha = 1;
		
		Main.animator.resetFPSCounter();
		Player.me.destroy();
		Global.mainActive = false;
		Global.deactivate();
		Music.changeMusic("");
		Root.changeRoom("d");
	}
	
	public void step(){
		if (!done){
			if (Audio.soundExists("musJellycat")){
				Audio.get("musJellycat").setLooping(false);
				done = true;
			}
		}
		alarmStep();
		
		if (!unlocked && Input.checkFrameKey(KeyEvent.VK_ESCAPE))
			end();
	}

}
