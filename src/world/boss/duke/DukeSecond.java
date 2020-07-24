package world.boss.duke;

import java.util.ArrayList;

import audio.Audio;
import world.Fg;
import world.boss.puke.Coord;
import world.boss.puke.DeathShadow;
import world.boss.puke.Rect;
import world.control.Global;
import world.control.Hud;
import world.control.Sound;
import main.Entity;
import main.Scene;

public class DukeSecond extends Entity{
	public ArrayList<Coord[]> balloons = new ArrayList<Coord[]>();
	public ArrayList<Coord[]> dukes = new ArrayList<Coord[]>();
	public ArrayList<Rect[]> deaths = new ArrayList<Rect[]>();
	
	public boolean ready = false;
	public int at = 0;
	
	public int MAX = 0;

	public DukeSecond() {
		super(0, 0);
		setCollisionGroup(Global.DEACTIVATEME);
		//0
		balloons.add(new Coord[]{new Coord(240, 240), new Coord(336, 240)});
		dukes.add(new Coord[]{new Coord(448, 240)});
		deaths.add(new Rect[]{});

		//1
		balloons.add(new Coord[]{new Coord(144, 176)});
		dukes.add(new Coord[]{new Coord(336, 224), new Coord(224, 208), new Coord(448, 112)});
		deaths.add(new Rect[]{new Rect(352, 384, 576, 416)});

		//2
		balloons.add(new Coord[]{});
		dukes.add(new Coord[]{new Coord(320, 160)});
		deaths.add(new Rect[]{});

		//3
		balloons.add(new Coord[]{});
		dukes.add(new Coord[]{new Coord(512, 304), new Coord(384, 304), new Coord(256, 304)});
		deaths.add(new Rect[]{new Rect(208, 352, 560, 416)});

		//4
		balloons.add(new Coord[]{});
		dukes.add(new Coord[]{new Coord(384, 240), new Coord(272, 240), new Coord(272, 368)});
		deaths.add(new Rect[]{new Rect(416, 384, 576, 416), new Rect(64, 128, 224, 256), new Rect(96, 64, 224, 128)});

		//5
		balloons.add(new Coord[]{});
		dukes.add(new Coord[]{new Coord(320, 144)});
		deaths.add(new Rect[]{});

		//6
		balloons.add(new Coord[]{});
		dukes.add(new Coord[]{new Coord(480, 128), new Coord(528, 192), new Coord(432, 192), new Coord(480, 256), new Coord(528, 320), new Coord(432, 320), new Coord(480, 384)});
		deaths.add(new Rect[]{new Rect(256, 64, 384, 416)});

		//7
		balloons.add(new Coord[]{new Coord(224, 176)});
		dukes.add(new Coord[]{new Coord(320, 176), new Coord(416, 128)});
		deaths.add(new Rect[]{});

		//8
		balloons.add(new Coord[]{});
		dukes.add(new Coord[]{new Coord(416, 176), new Coord(320, 240)});
		deaths.add(new Rect[]{new Rect(96, 64, 192, 416)});

		//9
		balloons.add(new Coord[]{});
		dukes.add(new Coord[]{new Coord(416, 240), new Coord(320, 304)});
		deaths.add(new Rect[]{new Rect(96, 64, 224, 416)});

		//10
		balloons.add(new Coord[]{});
		dukes.add(new Coord[]{new Coord(320, 368), new Coord(416, 304)});
		deaths.add(new Rect[]{new Rect(96, 64, 256, 416)});

		//11
		balloons.add(new Coord[]{new Coord(480, 352), new Coord(480, 288), new Coord(480, 224), new Coord(480, 160)});
		dukes.add(new Coord[]{new Coord(160, 112)});
		deaths.add(new Rect[]{new Rect(224, 64, 256, 416)});

		//12
		balloons.add(new Coord[]{});
		dukes.add(new Coord[]{new Coord(384, 352), new Coord(256, 352), new Coord(144, 352)});
		deaths.add(new Rect[]{new Rect(96, 64, 256, 320), new Rect(64, 128, 96, 416), new Rect(96, 384, 272, 416)});

		//13
		balloons.add(new Coord[]{new Coord(288, 288), new Coord(352, 256), new Coord(416, 224)});
		dukes.add(new Coord[]{new Coord(496, 160)});
		deaths.add(new Rect[]{new Rect(96, 64, 256, 192), new Rect(64, 128, 96, 416), new Rect(192, 288, 256, 352), new Rect(256, 320, 464, 352), new Rect(256, 64, 400, 128)});

		//total
		MAX = 13;

		//fuck you
		balloons.add(new Coord[]{});
		dukes.add(new Coord[]{});
		deaths.add(new Rect[]{});

		alarmInitialize(3);
		alarm[0] = 60;
	}
	
	public void reload(){
		if (Global.gotGun){
			if (Global.selectedWeapon == 0 && Global.playerBullets != Global.playerMaxBullets){
				Sound.playPitched("sGunReload",.08);
				Hud.showWeapon();
				Hud.randomizeColor();
			}
			
			Global.playerBullets = Global.playerMaxBullets;
		}
	}
	
	public void spawn(Rect[] a, Coord[] b, Coord[] c, int at){
		reload();
		Sound.playPitched("sDukeNext", .1);
		
		for (int i=0; i<a.length; i++)
			new Duketangle(a[i].x, a[i].y, a[i].a, a[i].b);
		for (int i=0; i<b.length; i++)
			new Duke(b[i].x, b[i].y);
		for (int i=0; i<c.length; i++)
			new DukeSpawn(c[i].x, c[i].y);
		
		if (at != MAX){
			Coord[] d = dukes.get(at + 1);
			for (int i=0; i<d.length; i++)
				new DukeShadow(d[i].x, d[i].y);
			
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
			Scene.destroy(Duketangle.class);
			spawn(deaths.get(0), dukes.get(0), balloons.get(0), 0);
			ready = true;
		}
	}
	
	public void step(){
		boolean go = true;
		/*if (at == 4)
			go = Scene.collision(Player.me, Player.me.x, Player.me.y + 1, Global.SOLIDBLOCK);
			
			//if a phase can only fairly begin when the player has landed on the ground
			*/
		
		if (ready && !Scene.instanceExists(Duke.class) && !Scene.instanceExists(DukeDeath.class) && go){
			if (at < MAX){ //should be the id of the last level
				Fg.me.shakeTimer = 10;
				DukeMusic.me.music(!DukeMusic.me.phase);
				
				Scene.destroy(Duketangle.class);
				Scene.destroy(DukeSpawn.class);

				ArrayList<DukeShadow> jist = Scene.getEntityList(DukeShadow.class);
				for (int i=0; i<jist.size(); i++)
					jist.get(i).kill();
				
				ArrayList<DukeBalloon> list = Scene.getEntityList(DukeBalloon.class);
				for (int i=0; i<list.size(); i++){
					Sound.playPitched("sBalloonPop");
					list.get(i).pop();
				}
				
				Scene.destroy(DeathShadow.class);
				
				at += 1;
				spawn(deaths.get(at), dukes.get(at), balloons.get(at), at);

				if (at == MAX){
					ArrayList<Duke> bist = Scene.getEntityList(Duke.class);
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
