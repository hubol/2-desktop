package world.boss.puke;

import java.util.ArrayList;

import audio.Audio;
import world.Fg;
import world.control.Global;
import world.control.Sound;
import world.player.Player;
import main.Entity;
import main.Scene;

public class PukeSecond extends Entity{
	public ArrayList<Rect[]> deaths = new ArrayList<Rect[]>();
	public ArrayList<Coord[]> pukes = new ArrayList<Coord[]>();
	public ArrayList<Coord[]> balloons = new ArrayList<Coord[]>();
	
	public boolean ready = false;
	public int at = 0;

	public PukeSecond() {
		super(0, 0);
		setCollisionGroup(Global.DEACTIVATEME);
		
		//0
		deaths.add(new Rect[]{new Rect(64, 64, 128, 416)});
		pukes.add(new Coord[]{new Coord(416, 192), new Coord(320, 288)});
		balloons.add(new Coord[]{});
		Rect r = deaths.get(0)[0];
		new DeathShadow(r.x, r.y, r.a, r.b);
		//1
		deaths.add(new Rect[]{new Rect(480, 352, 512, 416)});
		pukes.add(new Coord[]{new Coord(544, 384)});
		balloons.add(new Coord[]{new Coord(384, 160), new Coord(256, 160)});
		//2
		deaths.add(new Rect[]{new Rect(160, 352, 480, 416)});
		pukes.add(new Coord[]{new Coord(416,192), new Coord(320,208), new Coord(224,192), new Coord(96,224), new Coord(96,352)});
		balloons.add(new Coord[]{});
		//3
		deaths.add(new Rect[]{new Rect(64,64,576,160)});
		pukes.add(new Coord[]{new Coord(224,336)});
		balloons.add(new Coord[]{new Coord(416, 384), new Coord(352, 336)});
		//4
		deaths.add(new Rect[]{new Rect(224, 64, 416, 160)});
		pukes.add(new Coord[]{new Coord(320, 256), new Coord(320, 336)});
		balloons.add(new Coord[]{});
		//5
		deaths.add(new Rect[]{});
		pukes.add(new Coord[]{new Coord(224, 192), new Coord(320, 192), new Coord(416, 192), new Coord(224, 272), new Coord(320, 272), new Coord(416, 272), new Coord(224, 352), new Coord(320, 352), new Coord(416, 352)});
		balloons.add(new Coord[]{});
		//6
		deaths.add(new Rect[]{});
		pukes.add(new Coord[]{new Coord(192, 384)});
		balloons.add(new Coord[]{});
		//7
		deaths.add(new Rect[]{new Rect(272,64,336,416)});
		pukes.add(new Coord[]{new Coord(224, 192)});
		balloons.add(new Coord[]{});
		//8
		deaths.add(new Rect[]{new Rect(64,64,144,416), new Rect(144,368,576,416)});
		pukes.add(new Coord[]{new Coord(304, 336)});
		balloons.add(new Coord[]{});
		//9
		deaths.add(new Rect[]{new Rect(64,64,480,256), new Rect(64,352,352,416)});
		pukes.add(new Coord[]{new Coord(416, 368)});
		balloons.add(new Coord[]{});
		//10
		deaths.add(new Rect[]{new Rect(64,64,352,224), new Rect(64,224,448,416)});
		pukes.add(new Coord[]{new Coord(416, 160)});
		balloons.add(new Coord[]{});
		//11
		deaths.add(new Rect[]{});
		pukes.add(new Coord[]{new Coord(0, 0)});
		balloons.add(new Coord[]{});
		
		alarmInitialize(3);
		alarm[0] = 60;
	}
	
	public void spawn(Rect[] a, Coord[] b, Coord[] c){
		Sound.playPitched("sPukeNext", .1);
		
		for (int i=0; i<a.length; i++)
			new Deathtangle(a[i].x, a[i].y, a[i].a, a[i].b);
		for (int i=0; i<b.length; i++)
			new Puke(b[i].x, b[i].y);
		for (int i=0; i<c.length; i++)
			new PukeSpawn(c[i].x, c[i].y);
		
		if (at != 11){
			Coord[] d = pukes.get(at + 1);
			for (int i=0; i<d.length; i++)
				new PukeShadow(d[i].x, d[i].y);
			
			Rect[] r = deaths.get(at + 1);
			for (int i=0; i<r.length; i++)
				new DeathShadow(r[i].x, r[i].y, r[i].a, r[i].b);
		}
	}
	
	public void alarmEvent(int i){
		if (i == 0){
			Audio.fade("sSkullQuake", 0, .0334);
			alarm[1] = 30;
		}
		else if (i == 1){
			Audio.get("sSkullQuake").stop(); //fck m
			alarm[2] = 1;
		}
		else if (i == 2){
			Scene.destroy(DeathShadow.class);
			spawn(deaths.get(0), pukes.get(0), balloons.get(0));
			ready = true;
		}
	}
	
	public void step(){
		boolean go = true;
		if (at == 4)
			go = Scene.collision(Player.me, Player.me.x, Player.me.y + 1, Global.SOLIDBLOCK);
		
		if (ready && !Scene.instanceExists(Puke.class) && !Scene.instanceExists(PukeDeath.class) && go){
			if (at < 10){
				Fg.me.shakeTimer = 10;
				PukeMusic.me.music(!PukeMusic.me.phase);
				
				Scene.destroy(Deathtangle.class);
				Scene.destroy(PukeSpawn.class);
				Scene.destroy(PukeShadow.class);
				Scene.destroy(DeathShadow.class);
				
				ArrayList<PukeBalloon> list = Scene.getEntityList(PukeBalloon.class);
				for (int i=0; i<list.size(); i++){
					Sound.playPitched("sBalloonPop");
					list.get(i).pop();
				}
				
				at += 1;
				spawn(deaths.get(at), pukes.get(at), balloons.get(at));
				
				if (at == 10){
					ArrayList<Puke> bist = Scene.getEntityList(Puke.class);
					bist.get(0).last = true;
				}
			}
			else{
				destroy();
			}
		}
		
		alarmStep();
	}

}
