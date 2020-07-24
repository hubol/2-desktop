package world.player;

import java.awt.Point;
import java.io.File;
import java.util.ArrayList;

import javax.media.opengl.GL2;

import world.Fg;
import world.Root;
import world.boss.jelly.JellyMan;
import world.control.EV;
import world.control.Global;
import world.control.HLoop;
import world.control.Hud;
import world.control.IO;
import world.control.Music;
import world.control.SaveFile;
import world.control.Shake;
import world.control.Sound;
import world.control.VLoop;
import world.death.Dead;
import world.dream.DreamHeartParticles;
import world.dream.JumpControl;
import world.enemy.BasicEnemy;
import world.enemy.BombBaby;
import world.event.Monster;
import world.gameplay.Door;
import world.gameplay.StupidBlockRender;
import world.junk.Flapper;
import world.particles.GlitchBlood;
import world.particles.JumpDust;
import world.particles.Stench;
import world.water.AirBubble;
import world.water.Splash;

import java.awt.event.KeyEvent;

import audio.Audio;
import graphics.Sprite;
import main.Calc;
import main.Entity;
import main.Main;
import main.Scene;

public class Player extends Entity {
	public Sprite run, jBod, jFace, fall, duck, blud, stand, spr;
	public static Player me;
	public int animation, glitch, glitchTime, glitchStart, xsc, face, la, tempXsc, jumpS, li, stillTimer, ugh;
	public double img, rmg, jmg, slideImg, blood, gravity, smg, assAng, glitchX=0, glitchY=0, notZero=0;
	public static int permit, invincible;
	
	public boolean landLast;
	public static boolean control;
	public boolean ducking, cameOffWallJump;
	
	public boolean apart, checkingForPieces;
	
	public boolean abortVspeed;
	
	public boolean record, cancel;
	public double cancelSpd;
	
	public int downTimer;
	
	//bomb animation
	public int bombShow, bombTimer;
	public double bombShowY;
	
	//bomb animation
	public int hammerShow, hammerTimer;
	public double hammerShowY;
	
	//wall sliding st
	public boolean wallSlide, wallLeft, wallRight;
	
	//weapon
	public boolean lastUp, lastDown, lastAction, lastError;
	public static boolean canChangeWeapon;
	public double gunTimer;
	
	//gun drawing
	public double gunMult, gunImg;
	
	//hammer drawing
	public double hamMult;
	
	//bomb drawing
	public double bombX, bombY, bombAddX;
	
	//door st
	public static int doorPhase, doorTime, cantDoor;
	public Door myDoor;
	public Door target;
	
	//conversation st
	public boolean cutMove, cutRight;
	public double cutToX;
	
	//hurt st
	public static int hurtTime;
	
	//key st
	public double kX, kY;
	public int kT;
	public Sprite key;
	public static int downTime;
	public boolean laDo;
	
	//blood?
	public boolean[] blVis;
	public double[] blX, blY, blAng, blSc;
	public int[] blImg;
	
	//timer for taking off/on glasses
	public int glassesTime;
	
	public double stenchTime;
	
	public double weaponScale;
	
	public boolean jetpack;
	
	public boolean hLoop, vLoop;
	
	public boolean waterSubmerged, waterSubmergedLast;
	
	public int wallSplashTime;
	
	public double tickYsc, air;
	public int lastTick;
	
	public double seed;
	
	public ArrayList<Point> jumpBow;
	
	public Sprite bow = Sprite.get("sBow");
	public Shake bs = new Shake(.2);
	
	//i hate myself:
	public boolean noGrav = false;

	public double endAddX = 0, endAddY = 0, endToX = 0, endToY = 0, endAlpha = 1;
	
	public Player(double x, double y) {
		super(x, y);
		me=this;
		setCollisionGroup(Global.PLAYER, Global.DEACTIVATEME);
		
		record = false;
		cancel = false;
		cancelSpd = 0;
		
		seed = 0;
		
		jumpBow = new ArrayList<Point>();
		
		waterSubmerged = false;
		waterSubmergedLast = false;
		
		hLoop = false;
		vLoop = false;
		
		wallSplashTime = 0;
		
		downTimer = -1;
		
		lastTick = 100;
		
		weaponScale = 0;
		
		tickYsc = 0;
		air = 30;
		
		jetpack = false;
		
		persistent=true;
		permit=0;
		visible=false;
		stand=Sprite.get("sStand");
		run=Sprite.get("sRun");
		jBod=Sprite.get("sJumpBody");
		jFace=Sprite.get("sJumpFace");
		fall=Sprite.get("sFall");
		duck=Sprite.get("sDuck");
		blud=Sprite.get("sGlitchBlood");
		
		orientation=Sprite.CENTERED;
		//mask=stand.mask;
		mask = Sprite.get("sPlayerMask").mask;
		
		apart = false;
		checkingForPieces = false;
		
		bombTimer = 0;
		bombShow = 0;
		bombShowY = 0;
		
		doorPhase=0;
		doorTime=0;
		
		glassesTime = 0;
		
		cameOffWallJump = false;
		
		abortVspeed = false;
		
		stenchTime = 0;
		
		//weapon st
		lastUp = false;
		lastDown = false;
		lastAction = false;
		lastError = false;
		canChangeWeapon = true;
		gunTimer = 0;
		
		//gun animation
		gunMult = 0;
		gunImg = 0;
		
		//hammer animation
		hamMult = 0;
		
		//bomb animation
		bombX = 0;
		bombY = 0;
		bombAddX = 1;
		
		stillTimer = 0;
		
		//dooooor
		target = null;
		
		li=0;
		jumpS=1;
		
		ducking=false;
		
		blVis=new boolean[3];
		blX=new double[3];
		blY=new double[3];
		blAng=new double[3];
		blSc=new double[3];
		blImg=new int[3];
		
		gravity=0.8;
		landLast=false;
		control=true;
		
		ugh = 1;
		xsc=1;
		animation=0;
		spr=stand;
		img=0;
		setGlitch();
		
		hurtTime=0;
		
		kX=x;
		kY=y-32;
		kT=0;
		key=Sprite.get("sKey");
		downTime = 0;
		laDo = false;
		
		smg = 0;
		
		cantDoor = 0;
		invincible = 0;
	}
	
	public void resetEnd(){
		endAddX = 0;
		endAddY = 0;
		endToX = 0;
		endToY = 0;
		endAlpha = 1;
	}
	
	/**aaahhh!!!!!! call this when the room is reset!!!!*/
	public void roomStart(){
		hLoop = (Scene.getEntityList(HLoop.class).size() > 0);
		vLoop = (Scene.getEntityList(VLoop.class).size() > 0);
	}
	
	public static void hurtPlayer(int damage){
		if (damage > 0){
			if (Global.heroMode)
				damage = Global.playerMaxHealth;
			
			if (invincible <= 0 && control){
				if (hurtTime<27){
					//me.hspeed *= -1;
					//if (me.vspeed >= 0)
					//	me.jump();
				}
				if (hurtTime<=0){
					new Damage(me.x, me.y - 10, damage);
					hurtTime=30; //guess this could vary depending on difficulty (if implemented for whatever reason)
					Global.playerHealth-=damage;
					Global.playerHealth = Math.max(0, Global.playerHealth);
					Sound.playPitched("sHurtPrimary", .02);
					Sound.hurtPlay();
					
					if (Global.playerHealth <= 0){
						Global.playerDead = true;
						Global.totalDeaths += 1;
						SaveFile.saveStats();
						Global.deactivateGameplayObjects();
						Music.changeMusic("");
						new Dead(me.x, me.y);
					}
				}
				Hud.showHealth();
				//TODO hurt stuff
			}
		}
	}
	
	/**fall apart and become invisible*/
	public void fallApart(boolean dingle){
		Sound.playPitched("sDreamExplode",.03);
		apart = true;
		for (int i=0; i<16; i++){
			for (int j=0; j<16; j++){
				new PlayerPieces(x - 16 + (i * 2), y - 16 + (j * 2), i * 2, j * 2, true, dingle);
			}
		}
	}
	
	/**return pieces after having fallen apart*/
	public void returnPieces(){
		Sound.playPitched("sDreamReform",.03);
		checkingForPieces = true;
		
		ArrayList<PlayerPieces> list = Scene.getEntityList(PlayerPieces.class);
		for (int i=0; i<list.size(); i++)
			list.get(i).toTarget();
	}
	
	/**call this when there are pieces and you need to regain control*/
	public void checkPieces(){
		boolean sure = true;
		
		ArrayList<PlayerPieces> list = Scene.getEntityList(PlayerPieces.class);
		for (int i=0; i<list.size(); i++){
			if (Calc.pointDistance(list.get(i).x, list.get(i).y, list.get(i).targetX, list.get(i).targetY) > 1)
				sure = false;
		}
		
		if (sure){
			if (Scene.console)
				System.out.println("pieces returned!");
			checkingForPieces = false;
			control = true;
			apart = false;
			
			for (int i=0; i<list.size(); i++)
				list.get(i).destroy();
		}
	}
	
	public double random(double x){
		if (seed > Double.MAX_VALUE)
			seed = 68.29;
		seed += x;
		seed *= 1.08 + (x / 1028);
		seed -= 1280;
		seed = Math.abs(seed);
		return x * ((seed % 12.69) / 12.69);
	}
	
	public double rangedRandom(double x){
		return -x + random(x * 2);
	}
	
	public Object choose(Object... entries){
		int i = (int)(random(1)*entries.length);
		return entries[i];
	}
	
	/**transitional thing. you return to the overworld and your pieces come back together*/
	public void returnPiecesInstant(){
		Sound.playPitched("sDreamReform",.03);
		checkingForPieces = true;
		apart = true;
		
		seed = Global.framesPlayed;
		double muelt = 0;
		
		for (int i=0; i<16; i++){
			for (int j=0; j<16; j++){
				muelt += .773;
				double dir = random(360), dist = 60 + random(30);
				new PlayerPieces(x + Calc.dirX(dist, dir), y + Calc.dirY(dist, dir), i * 2, j * 2, false, false);
				seed += muelt;
			}
		}
	}
	
	public static void bouncePlayer(Entity enemy){
		//speed = speed - 2*normalize(position-urchinPosition)*sign(abs(dotProduct(position-urchinPosition,speed)));
		//System.out.println(hurtTime);
		/*V3D diff = new V3D(me.x-enemy.x, me.y-enemy.y,0);
		V3D playerSpd = new V3D(me.hspeed, me.vspeed,0);
		System.out.println(-2*diff.dot(playerSpd));
		playerSpd.add(diff.normalize(),-2*diff.dot(playerSpd));
		me.hspeed = playerSpd.x();
		me.vspeed = playerSpd.y();*/
	}
	
	public void step(){
		if (Global.room(29, 0)){
			if (Fg.me.alpha <= 1.0 / 256.0){
				endToX = 320 - x;
				endToY = 440 - y;
			}
			endAddX = Calc.approach(endAddX, endToX, 120);
			endAddY = Calc.approach(endAddY, endToY, 90);
		}
		
		ArrayList<StupidBlockRender> stupid = Scene.getEntityList(StupidBlockRender.class);
		Global.framesPlayed += 1;
		
		if (downTimer > -1)
			downTimer -= 1;
		
		if (Global.roomX == 21 && control)
			new DreamHeartParticles(x, y - 16, 90 + Calc.rangedRandom(20), Integer.MIN_VALUE + 10);
		
		//Stone bell b
		if (Global.roomY == 2 && Music.music.equals("musGabbie")){
			if (Global.roomX >= 11)
				Sound.stoneBellFadeIn = true;
			else if (Global.roomX >= 9 && Scene.collision(this, x, y, Global.STONEBELLON))
				Sound.stoneBellFadeIn = true;
		}
		
		if (Global.canDebug){
			/*if (IO.checkFrameKey(KeyEvent.VK_Y)){
				Global.eventItemGet(0);
				Global.event[EV.TREE_PROG] = 1;
			}*/
			
			if (IO.checkFrameKey(KeyEvent.VK_M)){
				Global.mute = !Global.mute;
				if (Global.mute)
					Audio.setGlobalGain(0);
				else
					Audio.setGlobalGain(1);
			}
			
			if (IO.checkFrameKey(KeyEvent.VK_P)){
				int a = 0;
				File b = new File(Main.DIRECTORY, "screenshot"+Calc.formatNumberString(a, 3)+".png");
				while (b.exists()){
					a += 1;
					b = null; //well
					b = new File(Main.DIRECTORY, "screenshot"+Calc.formatNumberString(a, 3)+".png");
				}
				Scene.screenSave("screenshot"+Calc.formatNumberString(a, 3)+".png");
			}
			
			if (IO.checkFrameKey(KeyEvent.VK_Q))
				Global.shiftScreenMode();
			
			/*if (IO.checkFrameKey(KeyEvent.VK_C)){
				Global.toTweetScene("z");
			}*/
		}
		
		stenchTime -= 1;
		if (Global.gotStench && stenchTime <= 0 && visible && !apart){
			if (Global.suicidalUrchinsExist()){
				stenchTime = Calc.random(4);
				
				double a = 0;
				if (hspeed != 0)
					a = hspeed * 4;
				
				new Stench(x, y - 22, a);
			}
		}
		
		//ing timers man
		glassesTime -= 1;
		
		//new glitch stuff
		glitchX += Calc.rangedRandom(.2);
		glitchY += Calc.rangedRandom(.2);
		
		invincible -= 1;
		//gun animatin'
		gunMult = Calc.approach(gunMult, .01, 7);
		gunTimer -= 1;
		gunImg += (3.0 + Calc.random(1)) / 30.0;
		if (gunImg >= 2)
			gunImg -= 2;
		
		//hammer animate
		hamMult = Calc.approach(hamMult, .01, 7);
		
		//bomb animatin'
		bombX += 1.0;
		bombY = 3.0 * Math.sin(bombX / 15.0);
		
		if (hspeed != 0){
			ugh = (int)Math.signum(hspeed);
			notZero = hspeed;
		}
		
		bombAddX = Calc.approach(bombAddX, ugh, (1 - Math.min(notZero / Global.runSpeed, 1.0) + .75 ) * 4);
		if (Math.abs(bombAddX) <= .3)
			bombAddX = Calc.approach(bombAddX, ugh, (1 - Math.min(notZero / Global.runSpeed, 1.0) + .75 ) * 3);
		
		//door
		cantDoor -= 1;
		
		//gosh golly
		if (checkingForPieces)
			checkPieces();
		
		if (Global.mainActive){
			if (permit==2){
				/*if (IO.checkKey(KeyEvent.VK_W)){
					//Global.money += 20;
					Global.jumpSpeed+=.1;
					System.out.println(Global.jumpSpeed);
				}
				if (IO.checkKey(KeyEvent.VK_S)){
					Global.jumpSpeed-=.1;
					System.out.println(Global.jumpSpeed);
				}/*
				if (IO.checkKey(KeyEvent.VK_S)){
					Global.money -= 10;
				}*/
				if (doorPhase>0){
					doorTime-=1;
					if (doorTime<=0){
						if (doorPhase==1){
							visible=false;
							doorPhase=2;
							doorTime=25;
						}
						else if (doorPhase==2){
							//find a door to play with
							myDoor=null;
							ArrayList<Door> list = Scene.getEntityList(Door.class);
							for (int i=0; i<list.size(); i++){
								if (Calc.pointDistance(x,y,list.get(i).x,list.get(i).y)<=64)
									myDoor=list.get(i);
							}
							if (myDoor!=null){
								if (Scene.console)
									System.out.println("i found a door!");
								doorPhase=3;
								myDoor.phase=4;
								myDoor.timer=10;
							}
							else{
								if (Scene.console)
									System.out.println("door not found!");
								visible=true;
								control=true;
								doorPhase=0;
								doorTime=0;
							}
						}
					}
				}
				
				xscale = .08;
				yscale = .08;
				y += 12;
				
				waterSubmerged = Scene.collision(this, x, y, Global.WATER);
				
				if (waterSubmerged){
					air -= 1.0 / (16.0 * (1 + Global.event[EV.GOT_LUNGS]));
					tickYsc = Calc.approach(tickYsc, 1, 3);
					if (air <= 0)
						hurtPlayer(125);
					
					if (waterSubmergedLast && (int)Math.round(air) != lastTick && air >= 0){
						lastTick = (int)Math.round(air);
						Sound.playPitched("sAirTick",.125);
						new AirBubble(x, y - 10);
					}
				}
				else{
					lastTick = 30;
					air = 30;
					tickYsc = Calc.approach(tickYsc, 0, 3);
					tickYsc = Math.max(0, tickYsc - .125);
				}
				
				xscale = 1;
				yscale = 1;
				y-=12;
				
				if (waterSubmerged != waterSubmergedLast){
					new Splash(x, y + 16, 16, 1, 6);
					new Splash(x, y + 16, 12, .6, 12);
					Sound.playPitched("sSplash",.05);
				}
				
				//keyboard controls!
				control();
				
				wallSplashTime += 1;
				wallSplashTime = wallSplashTime % 6;
				if (wallSplashTime == 0){
					if (wallRight && Scene.collision(this, x + 3, y, Global.WATERSOLID))
						splazh(16, 0, 180);
					else if (wallLeft && Scene.collision(this, x - 3, y, Global.WATERSOLID))
						splazh(-16, 0, 0);
				}
				
				if (!wallRight && !wallLeft)
					wallSplashTime = -1;
				
				waterSubmergedLast = waterSubmerged;
				
				/*if (IO.checkKey(KeyEvent.VK_A))
					Root.changeRoom("BedSceneA");*/
				
				//hey wow animation!
				animation();
				
				if (!waterSubmerged)
					vspeed = Math.min(16, vspeed);
				else
					vspeed = Math.min(4, vspeed);
				
				//adjust coordinates based on horizontal and vertical movement!
				super.step();
				//adjustCoordinates();
				
				//fjak
				bombShow -= 1;
				bombTimer += 1;
				if (bombShow > 0){
					if (bombTimer >= 3){
						if (bombShowY == 0)
							bombShowY = -8;
						else
							bombShowY = 0;
						bombTimer = 0;
					}
				}
				else{
					bombTimer = 0;
					bombShowY = 0;
				}
				
				//off
				hammerShow -= 1;
				hammerTimer += 1;
				if (hammerShow > 0){
					if (hammerTimer >= 3){
						if (hammerShowY == 0)
							hammerShowY = -8;
						else
							hammerShowY = 0;
						hammerTimer = 0;
					}
				}
				else{
					hammerTimer = 0;
					hammerShowY = 0;
				}
				
				//show all hud if youve stood still long enough
				if (x == xprevious && y == yprevious && control)
					stillTimer += 1;
				else if (stillTimer > 0){
					if (stillTimer >= 120)
						Hud.hideAll();
					stillTimer = 0;
				}
				if (stillTimer >= 120)
					Hud.showAll();
				//end of that
				
				leaveRoom();
				
				Global.playerX=x;
				Global.playerY=y;
				
				//hurtTime
				hurtTime-=1;
				
				//lets spit out some blood based on glitch intensity AND being hurt
				int amnt=0;
				if ((glitchTime/glitchStart)>.9)
					amnt+=.3;
				if ((glitchTime/glitchStart)<=.3)
					amnt+=.35;
				if ((glitchTime/glitchStart)<=.2)
					amnt+=.35;
				if ((glitchTime/glitchStart)<=.1)
					amnt+=.35;
				if (hurtTime>0)
					amnt+=4;
				amnt += (int)(hurtTime/4);
				if (amnt>0){
					int rpt = (int)Calc.random(amnt);
					for (int i=0; i<rpt; i++){
						new GlitchBlood(x-10+Calc.random(20),y-10+Calc.random(20));
					}
				}
			}
			else if (permit==1){
				prepare();
			}
		}
		
		kX=Calc.approach(kX, x, 1.2);
		kY=Calc.approach(kY, y-29, 3);
		kT-=1;
		if (kT<=0){
			kX=x;
			kY=y-29;
		}
		
		weaponScale = Calc.approach(weaponScale, 1, 4);
		
		while (jumpBowSize() > 20){
			Point p = jumpBow.get(0);
			if (p.x == -69 && p.y == -69)
				jumpBow.remove(0);
			jumpBow.remove(0);
		}
		
		noGrav = false;
		refreshStupid(stupid);
	}
	
	//fjak i had to make a function for this because this is a nightmare. input whether to face right (true) or left (false)
	public void face(boolean right){
		xsc = -1;
		notZero = -1;
		ugh = -1;
		
		if (right){
			xsc = 1;
			notZero = 1;
			ugh = 1;
		}
	}
	
	public void pickupKey(double sX, double sY){
		kX=sX;
		kY=sY;
		kT=10;
		Sound.playPitched("sKeyGet");
	}
	
	/**if youve pressed down and you are capable of interacting with a thing*/
	public static boolean canDownInteract(){
		return IO.checkKey(Global.DOWN) && control && !IO.checkKey(Global.INTERACT) && me.downTimer <= -1;
	}
	
	public static void enterDoor(){
		control=false;
		doorPhase=1;
		doorTime=11;
	}
	
	public void leaveDoor(){
		
	}
	
	public void destroy(){
		if (me==this)
			me = null;
		super.destroy();
	}
	
	/**sighs deeply*/
	public void adjustCoordinates(){
		for (int i=0; i < Math.round(Math.abs(vspeed)); i++){
			if (!Scene.collision(this, x, y + Math.signum(vspeed), Global.SOLIDBLOCK) && !abortVspeed){
				y += Math.signum(vspeed);
				
				ArrayList<BasicEnemy> list = Scene.getEntityList(BasicEnemy.class);
				for (int j=0; j<list.size(); j++){
					if (!abortVspeed)
						list.get(j).playerCollisionDamagesMe();
				}
			}
			else{
				if (vspeed != 0)
					landSound();
				if (vspeed > 0){
					stepSound();
				}
				vspeed = 0;
				cameOffWallJump = false;
				jumpBowKill();
				wallSlide = false;
			}
		}
		
		for (int i=0; i<Math.round(Math.abs(hspeed)); i++){
			if (!Scene.collision(this, x + Math.signum(hspeed), y, Global.SOLIDBLOCK))
				x += Math.signum(hspeed);
			else
				hspeed = 0;
		}
		
	}
	
	/**leave room if you intersect boundary*/
	public void leaveRoom(){
		boolean change=false;
		
		int lX=Global.roomX, lY=Global.roomY;
		int jX = 0, jY = 0;
		
		int oX = (int)x, oY = (int)y;
		
		if (x<0){
			if (Global.roomX==0){
				x+=1;
				hspeed*=-1;
			}
			else{
				x+=640;
				if (!hLoop){
					jX += 640;
					change=true;
					Global.roomX-=1;
				}
				else if (record){
					jumpBow.add(new Point(-5, oY));
					jumpBow.add(new Point(-69, -69));
					jumpBow.add(new Point(645, oY));
				}
			}
		}
		else if (x>640){
			if (Global.roomX==15){
				x-=1;
				hspeed*=-1;
			}
			else{
				x-=640;
				if (!hLoop){
					jX -= 640;
					change=true;
					Global.roomX+=1;
				}
				else if (record){
					jumpBow.add(new Point(645, oY));
					jumpBow.add(new Point(-69, -69));
					jumpBow.add(new Point(-5, oY));
				}
			}
		}
		if (y<0){
			if (Global.roomY==0){
				y+=1;
				vspeed*=-1;
			}
			else{
				y+=480;
				if (!vLoop){
					jY += 480;
					change=true;
					Global.roomY-=1;
				}
				else if (record){
					jumpBow.add(new Point(oX, -5));
					jumpBow.add(new Point(-69, -69));
					jumpBow.add(new Point(oX, 485));
				}
			}
		}
		else if (y>480){
			if (Global.roomY==15){
				y-=1;
				vspeed*=-1;
			}
			else{
				y-=480;
				if (!vLoop){
					jY -= 480;
					change=true;
					Global.roomY+=1;
				}
				else if (record){
					jumpBow.add(new Point(oX, 485));
					jumpBow.add(new Point(-69, -69));
					jumpBow.add(new Point(oX, -5));
				}
			}
		}
		
		if (change){
			//just for good measure
			Global.refreshIconMap();
			
			Global.lastX=lX;
			Global.lastY=lY;

			if (jX != 0 || jY != 0)
				jumpBowShift(jX, jY);
			
			Root.changeRoom(Global.roomX,Global.roomY);
		}
	}
	
	public int jumpBowSize(){
		int size = 0;
		for (int i=0; i<jumpBow.size(); i++){
			Point p = jumpBow.get(i);
			if (!(p.x == -69 && p.y == -69))
				size += 1;
		}
		
		return size;
	}
	
	public void jumpBowShift(int x, int y){
		for (int i=0; i<jumpBow.size(); i++){
			Point g = jumpBow.get(i);
			if (!(g.x == -69 && g.y == -69))
				jumpBow.set(i, new Point(g.x + x, g.y + y));
		}
	}
	
	public void jumpBowKill(){
		if (!cancel){
			cancel = true;
			cancelSpd = .3;
		}
		//jumpBow.clear();
	}
	
	public void refreshStupid(ArrayList<StupidBlockRender> s){
		for (int i=0; i<s.size(); i++)
			s.get(i).refresh();
	}
	
	/**take input controls*/
	public void control(){
		ArrayList<StupidBlockRender> stupid = Scene.getEntityList(StupidBlockRender.class);
		
		//ducking! a very useful feature!
		ducking=false;
		if (IO.checkKey(Global.DOWN) && Global.gotDuck && control && vspeed==0 && Scene.collision(this, x, y+1, Global.SOLIDBLOCK))
			ducking=true;
		
		//moving left/right!
		if (!ducking){
			if ((IO.checkKey(Global.LEFT) && hspeed>-Global.runSpeed && control)||(cutMove && !control && hspeed>-Global.runSpeed && !cutRight && x > cutToX))
				hspeed-=Global.runSpeed/7.5;
			if ((IO.checkKey(Global.RIGHT) && hspeed<Global.runSpeed && control)||(cutMove && !control && hspeed<Global.runSpeed && cutRight && x < cutToX))
				hspeed+=Global.runSpeed/7.5;
		}
		
		//what this mess does is decide whether or not you should be stopping
		boolean stop = false;
		if (hspeed != 0){
			if (control){
				stop = (!IO.checkKey(Global.LEFT) && !IO.checkKey(Global.RIGHT) && control)||(ducking);
				
				if (cameOffWallJump && Math.abs(hspeed)>Global.runSpeed)
					stop = true;
			}
			else{
				if (cutMove)
					stop = ((cutRight && x >= cutToX - (hspeed/16))||(!cutRight && x <= cutToX + (hspeed/16)));
				else
					stop = true;
			}
		}
		
		//this performs the actual stopping
		if (stop)
		{
			if (hspeed>0)
			{
				hspeed-=(Global.runSpeed/7.5)+.2;
				if (hspeed<0)
				hspeed=0;
			}
			if (hspeed<0)
			{
				hspeed+=(Global.runSpeed/7.5)+.2;
				if (hspeed>0)
				hspeed=0;
			}
		}
		
		//this is for stopping and facing the npc
		if (hspeed == 0 && cutMove && !control){
			if (Scene.console)
				System.out.println("i've stopped!");
			cutMove = false;
			if (cutRight)
				xsc = -1;
			else
				xsc = 1;
			notZero = xsc;
			ugh = xsc;
		}
		
		hMovement();
		
		refreshStupid(stupid);
		
		//wall sliding
		boolean tSlide = wallSlide, wallFarleft, wallFarright;
		wallSlide = false;
		wallLeft = false;
		wallRight = false;
		if (Global.gotWallSlide && hspeed == 0){
			if (!Scene.collision(this, x, y + 1, Global.SOLIDBLOCK)){
				wallLeft = Scene.collision(this, x - 4, y, Global.SOLIDBLOCK)/* && IO.checkKey(Global.LEFT)*/;
				wallRight = Scene.collision(this, x + 4, y, Global.SOLIDBLOCK)/* && IO.checkKey(Global.RIGHT)*/;
				wallFarleft = Scene.collision(this, x - 8, y, Global.SOLIDBLOCK);
				wallFarright = Scene.collision(this, x + 8, y, Global.SOLIDBLOCK);
				if (!(wallLeft && wallRight) && (wallLeft || wallRight) && ((wallLeft && !wallFarright) || (wallRight && !wallFarleft)) && vspeed > -Global.jumpSpeed - 3){
					cameOffWallJump = false;
					//jumpBowKill();
					if (!tSlide){
						Sound.playPitched("sWallAttach",.05);
						slideImg = Calc.random(8);
						if (Calc.random(1) > .9998735)
							slideImg = 8.0; //oh man memes
					}
					wallSlide = true;
				}
				else{
					wallLeft = false;
					wallRight = false;
				}
			}
			else{
				cameOffWallJump = false;
				jumpBowKill();
			}
		}
		
		if (wallSlide){
			double yy = 8;
			for (int i=0; i<3; i++){
				double xx = 16;
				double dir = 90 + Calc.random(30);
				if (vspeed > 0)
					dir = 270 - Calc.random(30);
				if (wallLeft){
					xx = -16;
					dir = 90 - Calc.random(30);
					if (vspeed > 0)
						dir = 270 + Calc.random(30);
				}
				JumpDust j = new JumpDust(x + xx, y + yy + (i * 1.5));
				j.setSpeed(.5+Calc.random(1.5));
				j.setDirection(dir);
				j.xscale = .7 + Calc.random(.2);
				j.yscale = j.xscale;
				j.imgS *= 1.5;
			}
		}
		
		/*if (wallSlide){
			String s = "sWallRainbowL";
			if (wallRight)
				s = "sWallRainbowR";
			
			double ax, ay;
			ax = Math.round(x - 16);
			ay = Math.floor(((y - 16.0)/ 32.0)) * 32.0;
			if (!Scene.collision(this, ax, ay, Global.RAINBOW))
				new RainbowTrail(ax, ay, s);
		}*/
		
		//jumpin'
		boolean capable = true;
		if (Global.roomY == 0 && y - Global.jumpSpeed - 1 <= 0)
			capable = false;
		
		boolean can = false;
		if ((Scene.collision(this, x, y + 1, Global.MONSTER) && Monster.me.vspeed == 0)||(!Scene.collision(this, x, y + 1, Global.MONSTER)))
			can = true;
		
		if (IO.checkKey(Global.JUMP) && !IO.checkKey(Global.INTERACT) && !ducking && control && !Scene.collision(this, x + hspeed, y-Global.jumpSpeed-1, Global.SOLIDBLOCK) && capable && can)
		{
			if ((Scene.collision(this, x, y+1, Global.SOLIDBLOCK) && vspeed==0) || (wallSlide && IO.checkFrameKey(Global.JUMP)) || noGrav){
				if (!wallSlide){
					jump();
					if (JellyMan.me!=null)
						JellyMan.me.stunTimer -= 20;
				}
				else{
					wallJump();
					record = true;
					cancel = false;
					cameOffWallJump = true;
					hspeed = Global.runSpeed * 2.3;
					if (wallRight)
						hspeed *= -1;
				}
			}
			else if (vspeed < -1)
				vspeed-=.35;
		}
		
		gravity = .8;
		if (Scene.collision(this, x, y, Global.WATER))
			gravity = .55;
		
		if(!Scene.collision(this, x+hspeed,y+1, Global.SOLIDBLOCK)){
			if (!noGrav)
				vspeed+=gravity;
		}
		else{
			if (vspeed>0 && !noGrav){
				landSound();
				stepSound();
			}
			
			cameOffWallJump = false;
			jumpBowKill();
			wallSlide = false;
		}
		
		hMovement();
		
		if (/*Math.abs(hspeed) > (Global.runSpeed + (Global.runSpeed/7.5)) || */cameOffWallJump){
			for (int i=0; i<2; i++){
				if (Global.roomX == 21){
					double d = 90;
					/*if (hspeed < 0)
						d = 0;*/
					DreamHeartParticles j = new DreamHeartParticles(x - hspeed, y + Calc.rangedRandom(8), 0, 4);
					j.setDirSpeed(d + Calc.rangedRandom(25), Math.max(Math.abs(hspeed) * .2, .75));
					j.timer = Calc.random(10);
				}
				else{
					//TODO jumpBow
					
					//new SpeedSparks(x - hspeed, y + Calc.rangedRandom(8), -hspeed * .2);
				}
			}
		}
		
		if (record && !Global.room(21,0)){
			jumpBow.add(new Point((int)x, (int)y));
		}
		if (cancel){
			for (int i=0; i<cancelSpd; i++){
				if (jumpBow.size() > 0){
					Point p = jumpBow.get(0);
					if (p.x == -69 && p.y == -69){
						jumpBow.remove(0);
						i -= 1;
					}
					else
						jumpBow.remove(0);
				}
			}
			if (jumpBow.size() == 0){
				record = false;
				cancel = false;
				cancelSpd = 0;
			}
			else{
				cancelSpd += .3;
				cancelSpd *= 1.04;
			}
		}
		
		if (vspeed!=0)
			landLast=false;
		
		double vSign = (vspeed<0?-1:1);

		if(Scene.collision(this, x+hspeed,Math.round(y)+vspeed+vSign, Global.SOLIDBLOCK))
		{
			if (vspeed != 0){
				landSound();
				
				if (vspeed > 0)
					stepSound();
			}
			
				if (vspeed < 0)
					wallSlide = false;
				
				vspeed=0;
				y = Math.round(y);
				while(!Scene.collision(this, x+hspeed,y+vSign, Global.SOLIDBLOCK) && !abortVspeed){
					ArrayList<BasicEnemy> list = Scene.getEntityList(BasicEnemy.class);
					for (int i=0; i<list.size(); i++){
						if (!abortVspeed)
							list.get(i).playerCollisionDamagesMe();
					}
	
					y+=vSign;
				}
		}
		double fuckYou = vspeed;
		vspeed = vSign;
		refreshStupid(stupid);
		vspeed = fuckYou;
		
		abortVspeed = false;
		
		useWeapon();
	}
	
	public void stepSound(){
		String s = Global.stepSound;
		if (Scene.collision(this, x, y + 1, Global.WATERSOLID)){
			s = "sStepSplash"+(1 + (int)Calc.random(5));
			new Splash(x, y + 16, 4, .25, 9, 90);
			new Splash(x, y + 16, 3, .35, 7, 90);
		}
		
		Audio.get(s).setGain(.7);
		Audio.fade(s,.7,.7);
		Audio.get(s).setPitch(.95+Calc.random(.1));
		Sound.play(s);
	}
	
	public void splazh(double addX, double addY, double angle){
		String s = "sStepSplash"+(1 + (int)Calc.random(5));
		new Splash(x + addX, y + addY, 4, .25, 9, angle);
		new Splash(x + addX, y + addY, 3, .35, 7, angle);
		
		Audio.get(s).setGain(.7);
		Audio.fade(s,.7,.7);
		Audio.get(s).setPitch(.95+Calc.random(.1));
		Sound.play(s);
	}
	
	public void landSound(){
		double a = Calc.random(1);
		int s = 1;
		if (a<.025)
			s=4;
		else if (a<.25)
			s=3;
		else if (a<.6)
			s=2;
		Audio.get("sLand"+s).setPitch(.975+Calc.random(.05));
		Audio.get("sLand"+s).setGain(.7);
		Audio.fade("sLand"+s,.7,.7);
		Sound.play("sLand"+s);
	}
	
	/**take care of running into blocks before super.step() occurs!*/
	public void hMovement(){
		ArrayList<StupidBlockRender> stupid = Scene.getEntityList(StupidBlockRender.class);
		//move horizontally!
		double hSign = Math.signum(hspeed);
		if (hSign != 0){
			if(Scene.collision(this, Math.round(x)+hspeed+hSign, y, Global.SOLIDBLOCK))
			{	
				cameOffWallJump = false;
				//jumpBowKill();
				for(int j = (int)y-3; j<y+3; j++){
					if(!Scene.collision(this, Math.round(x)+hspeed+hSign, j, Global.SOLIDBLOCK)){
						y=j; 
						return;
					}
				}
				
				hspeed=0;
				x = Math.round(x);
				while(!Scene.collision(this, x+hSign, y, Global.SOLIDBLOCK))
					x+=hSign;
			}
		}
		
		double fuckYou = hspeed;
		hspeed = hSign;
		refreshStupid(stupid);
		hspeed = fuckYou;
	}
	
	/**jump when on ground or when bouncing off enemies!*/
	public void jump(){
		abortVspeed = true;
		
		cameOffWallJump = false;
		jumpBowKill();
		for (int i=0; i<8; i++){
			if (Global.roomX == 21)
				new DreamHeartParticles(x-4+Calc.random(8),y+14+Calc.random(4), 90 + Calc.rangedRandom(80), -15);
			else
				new JumpDust(x-4+Calc.random(8),y+14+Calc.random(4));
		}
		
		jumpS+=1;
		if (jumpS>4)
			jumpS=1;
		
		Audio.get("sJump"+jumpS).setPitch(.975+Calc.random(.05));
		Audio.get("sJump"+jumpS).setGain(.7);
		Audio.fade("sJump"+jumpS,.7,.7);
		Sound.play("sJump"+jumpS);
		
		baseJump();
	}
	
	/**jump off a wall slide*/
	public void wallJump(){
		double addX = -16;
		if (wallRight)
			addX = 16;
		
		for (int i=0; i<9; i++){
			if (Global.roomX == 21){
				if (wallRight)
					new DreamHeartParticles(x-4+Calc.random(8) + addX,y-16.0 + (i * 3.55555) +Calc.random(4), 180 + Calc.rangedRandom(80), -15);
				else
					new DreamHeartParticles(x-4+Calc.random(8) + addX,y-16.0 + (i * 3.55555) +Calc.random(4), Calc.rangedRandom(80), -15);
			}
			else
				new JumpDust(x-4+Calc.random(8) + addX,y-16.0 + (i * 3.55555) +Calc.random(4));
		}
		
		Sound.playPitched("sWallJump",.05);
		
		baseJump();
		vspeed-=1.6;
	}
	
	/**this changes vertical speed*/
	public void baseJump(){
		vspeed = -Global.jumpSpeed;
		if (Global.room(22, 0)){
			JumpControl.me.playerJump();
			if (JumpControl.me.jumpSpeed != 0){
				Audio.get("sJumpDream").setPitch(1.8 - (JumpControl.me.jumpSpeed / 20));
				Sound.play("sJumpDream");
			}
			vspeed -= JumpControl.me.jumpSpeed;
		}
		else if (Global.room(1, 4)){
			ArrayList<Flapper> f = Scene.getEntityList(Flapper.class);
			for (int i=0; i<f.size(); i++)
				f.get(i).flap();
		}
	}
	
	/**set stuff for animation, moved here for some kind of "organization"*/
	public void animation(){
		//set animations depending on speeds
		if (vspeed<0 && !noGrav)
			animation=2;
		else if (vspeed>0 && !noGrav)
			animation=3;
		else if (vspeed==0 && (Scene.collision(this, x, y+1, Global.SOLIDBLOCK))){
			if (hspeed==0)
				animation=0;
			else
				animation=1;
		if (ducking)
			animation=4;
		}
		if (wallSlide)
			animation = 5;
		
		if (jetpack)
			animation = 0;
		
		if (hspeed!=0)
			xsc=(int)Math.signum(hspeed);
		
		tempXsc=xsc;
		
		//standing
		if (animation==0){
			spr=stand;
			if (glitch == 2 || glitch == 4 || glitch ==5 || glitch ==10){
				if (smg > 0)
					smg += (12.0 + Calc.random(3)) /30.0;
			}
			else
				smg = .1;
			if (smg >= 6)
				smg = 0;
			
			img=smg;
			if (glitch==8)
				tempXsc=(Integer)Calc.choose(xsc,xsc,xsc,-xsc);
		}
		//running
		else if (animation==1){
			spr=run;
			if (animation!=la)
				rmg=0;
			
			rmg+=Math.abs(hspeed/25);
			img=rmg % 2;
			
			if ((int)img!=li && visible)
				stepSound();
			li=(int)img;
		}
		//jumping
		else if (animation==2){
			spr=jBod;
			if (animation!=la){
				face=(Integer)Calc.choose(0,1,2,3,4,5);
				jmg=0;
			}
		    jmg+=.3;
			img=jmg % 2;
		}
		//falling
		else if (animation==3){
			spr=fall;
			img=0;
			if (glitch==8)
				tempXsc=(Integer)Calc.choose(xsc,xsc,xsc,-xsc);
		}
		//ducking
		else if (animation==4){
			spr=duck;
			img=0;
			if (glitch>6)
				tempXsc=(Integer)Calc.choose(xsc,xsc,xsc,-xsc);
		}
		//wall sliding
		else if (animation==5){
			spr=Sprite.get("sWallSlide");
			img=slideImg;
			
			if (wallLeft)
				tempXsc = 1;
			else
				tempXsc = -1;
		}
		
		//set last animation to the current animation
		la=animation;
		
		//glitch animation
		glitchTime-=1;
		if (glitchTime<=0){
			setGlitch();
		}
	}
	
	/**call this method when the player is prepared for take off*/
	public static void release(){
		if (permit==0)
			permit=1;
	}
	
	/**this is what happens when the player is prepared*/
	public void prepare(){
		x=Global.playerX;
		y=Global.playerY;
		visible=true;
		permit=2;
		animation();
	}
	
	/**dropping a key!*/
	public void dropKey(){
		if (control && !Global.dream && !laDo && stepActive && IO.checkKey(Global.DOWN) && !Scene.collision(this, x, y, Global.LOCKED) && canChangeWeapon && !Global.heldKey.equals("") && vspeed==0 && downTime <= 0 && canDownInteract()){
			if (!Scene.collision(this, x, y + 1, Global.BED, Global.INTERACTABLE) && Scene.collision(this, x, y + 1, Global.SOLIDBLOCK, Global.UPBLOCK) && !Scene.collision(this, x, y, Global.KEY, Global.INTERACTABLE)){
				if (Scene.collision(this, x, y + 1, Global.BLOCK, Global.UPBLOCK) && !Scene.collision(this, x, y + 1, Global.JELLYBLOCK) && !Scene.collision(this, x, y, Global.JAR, Global.NOOCCUPY)){
					Global.createKey(Global.addDropEntry(Global.roomX,Global.roomY,x - 16,y - 16,Global.heldKey));
					Global.refreshIconMap();
					Global.heldKey = "";
					Sound.playPitched("sKeyDrop");
				}
				else
					Sound.errorPlay();
			}
		}
		laDo = IO.checkKey(Global.DOWN);
	}
	
	/**weapon changing! (is in render code because :( )*/
	public void weaponChanging(){
		boolean i = IO.checkKey(Global.INTERACT);
		if (control){
			if (i)
				Hud.showWeapon();
		
			if (Global.getWeapons() > 1 && IO.checkFrameKey(Global.INTERACT)){
				boolean u = Global.selectedWeapon == 1;
				
				if (i && u){
					weaponScale = .8;
					Global.changeWeapon(1);
					Hud.showWeapon();
					Hud.pushArrow(true);
					Sound.playPitched("sSelectUp");
				}
				else{
					weaponScale = .8;
					Global.changeWeapon(-1);
					Hud.showWeapon();
					Hud.pushArrow(true);
					Sound.playPitched("sSelectDown");
				}
				
				lastUp = u;
				lastDown = false;
			}
		}
	}
	
	/**weapon using!*/
	public void useWeapon(){
		if (control && Global.getWeapons() > 0){
			boolean a = IO.checkKey(Global.ACTION);
			
			if (a){
				if (Global.selectedWeapon == 0 && Global.gotGun){
					if (Global.playerBullets > 0){
						if (gunTimer <= 0){
							gunTimer = 4;
							Global.playerBullets -= 1;
							gunMult = 1;
							Sound.playPitched("sGunShoot");
							Hud.showWeapon();
							Hud.randomizeColor();
							
							int zsc = xsc;
							if (wallLeft)
								zsc = 1;
							if (wallRight)
								zsc = -1;
							new Bullet(x + (16 * Math.signum(zsc)), y - 4, 20 * Math.signum(zsc));
						}
					}
					else if (!lastError){
						if (IO.checkFrameKey(Global.ACTION)){
							gunMult = 1;
							lastError = true;
							Sound.errorPlay();
							Hud.showWeapon();
							Hud.randomizeColor();
						}
					}
				}
				else if (Global.selectedWeapon == 1 && Global.gotBombs && !lastAction){
					if (!dropBomb() && !lastError){
						lastError = true;
						Sound.errorPlay();
						Hud.showWeapon();
						Hud.randomizeColor();
					}
				}
				else if (Global.selectedWeapon == 2 && Global.gotDoors && !lastAction && Global.playerDoors > 0 && !Global.dream){
					if (!Scene.collision(this, x, y, Global.INTERACTABLE, Global.JAR) && !Scene.collision(this, x + 32, y, Global.INTERACTABLE, Global.JAR) && !Scene.collision(this, x - 32, y, Global.INTERACTABLE, Global.JAR) && !Scene.collision(this, x, y + 1, Global.INTERACTABLE, Global.TEMPSOLID, Global.JAR) && vspeed == 0  && !Scene.collision(this, x, y, Global.NOOCCUPY) && Scene.collision(this, x, y + 1, Global.SOLIDBLOCK)){
						Global.addDoorEntry(x - 16, y - 48);
						Hud.showWeapon();
						Hud.randomizeColor();
						gunTimer = 6;
						Global.sparkEffect(x + Calc.rangedRandom(4), y + Calc.rangedRandom(4), .6 + Calc.random(.4));
						Global.sparkEffect(x + Calc.rangedRandom(4), y - 24 + Calc.rangedRandom(4),  .6 + Calc.random(.4));
						Global.sparkEffect(x + Calc.rangedRandom(4), y - 48 + Calc.rangedRandom(4),  .6 + Calc.random(.4));
						
						hamMult = 1;
						Sound.playPitched((String)Calc.choose("sHammer1", "sHammer2"), .01);
						
						if (Global.playerDoors == 1)
							Sound.play("sPlaceDoor1");
						else
							Sound.play("sPlaceDoor2");
					}
					else if (!lastError){
						lastError = true;
						Sound.errorPlay();
						Hud.showWeapon();
						Hud.randomizeColor();
					}
				}
				/*else if (Global.selectedWeapon == 3 && Global.gotGlasses && !lastAction && glassesTime <= 0){
					glassesTime = (int)(30 * .285);
					Global.glassesEnabled = !Global.glassesEnabled;
					
					if (Global.glassesEnabled)
						Sound.playPitched("sGlassesOn",.05);
					else
						Sound.playPitched("sGlassesOff",.05);
					
					Hud.showWeapon();
					Hud.randomizeColor();
				}*/
			}
			else
				lastError = false;
			
			lastAction = a;
		}
	}
	
	/**lets drop a bomb*/
	public boolean dropBomb(){
		boolean a = (Global.playerBombs > 0 && !Scene.collision(this, x, y + 1, Global.MONSTER, Global.DINGUS));
		if (a){
			Global.playerBombs -= 1;
			Hud.showWeapon();
			Hud.randomizeColor();
			new Bomb(x,y+16);
		}
		return a;
	}
	
	/**reset glitch animation*/
	public void setGlitch(){
		glitchX = 0;
		glitchY = 0;
		
		assAng = (Double)Calc.choose(0.0, 90.0, 180.0, 270.0);
		glitch=(int)Calc.random(11);
		glitchTime=(int)Math.ceil(Calc.random(45));
		glitchStart=glitchTime;
		
		//set blood stuff
		blood=Calc.random(1);
		for (int i=0; i<3; i++){
			blVis[i]=(Boolean)Calc.choose(true, false);
			blX[i]=-12+Calc.random(24);
			blY[i]=-12+Calc.random(24);
			blAng[i]=Calc.random(360);
			blSc[i]=.7+Calc.random(.32);
			blImg[i]=(Integer)Calc.choose(0,1,2,3);
		}
	}
	
	public void render(){
		if (!Global.playerDead){
			if (Global.paused)
				Global.framesPaused += 1;
			
			fjakRender();
		}
	}
	
	/**draw the character*/
	public void fjakRender(){
		if (stepActive){
			//here so it doesnt occur before locks
			downTime -= 1;
			dropKey();
			
			weaponChanging();
		}
		
		if (jumpBow.size() > 0){
			double size = 0;
			double x2 = 0, y2 = 0;
			
			double c = 0, d = 0, e = 0, f = 0;
			boolean fjakoff = false;
			
			for (int i=0; i<jumpBow.size(); i++){
				if (i < jumpBow.size() - 1){
					Point q = jumpBow.get(i + 1);
					if (q.x == -69 && q.y == -69){
						int toX = (int)x, toY = (int)y;
						if (i < jumpBow.size() - 3){
							toX = jumpBow.get(i + 3).x;
							toY = jumpBow.get(i + 3).y;
						}
						
						if (i < jumpBow.size() - 2){
							i += 2;
							q = jumpBow.get(i);
							x2 = q.x;
							y2 = q.y;
							
							double b = size;
							double dir = Calc.pointDirection(x2, y2, toX, toY);
							
							c = x2 + Calc.dirX(b * 16, dir - 90) + Calc.rangedRandom(.6);
							d = y2 + Calc.dirY(b * 16, dir - 90) + Calc.rangedRandom(.6);
							e = x2 + Calc.dirX(b * 16, dir + 90) + Calc.rangedRandom(.6);
							f = y2 + Calc.dirY(b * 16, dir + 90) + Calc.rangedRandom(.6);
						}
						else
							fjakoff = true;
					}
				}
				
				double a = size;
				size = Calc.approach(size, 1, 4.5);
				double b = size;
				
				if (!fjakoff){
					double x1 = x2, y1 = y2;
					if (i == 0){
						Point p = jumpBow.get(i);
						x1 = p.x;
						y1 = p.y;
					}
					
					double t1 = x, u1 = y;
					
					x2 = Calc.approach(x1, t1, 3) + Calc.rangedRandom(.6);
					y2 = Calc.approach(y1, u1, 3) + Calc.rangedRandom(.6);
					
					if (i < jumpBow.size() - 1){
						Point p = jumpBow.get(i + 1);
						x2 = p.x;
						y2 = p.y;
					}
	
					//x1, y1 -> the current point being drawn
					//x2, y2 -> the point being drawn to
					//a -> the height of the current piece of the "flag"
					//b -> the height of the next piece of the "flag"
					
					double dir = Calc.pointDirection(x1, y1, x2, y2);
					
					if (i == 0){
						c = x1 + Calc.dirX(a * 16, dir - 90) + Calc.rangedRandom(.6);
						d = y1 + Calc.dirY(a * 16, dir - 90) + Calc.rangedRandom(.6);
						e = x1 + Calc.dirX(a * 16, dir + 90) + Calc.rangedRandom(.6);
						f = y1 + Calc.dirY(a * 16, dir + 90) + Calc.rangedRandom(.6);
					}
					
					GL2 gl = Scene.gl;
					gl.glEnable(GL2.GL_TEXTURE_2D);
					gl.glBindTexture(GL2.GL_TEXTURE_2D, JUMP.textures[0]);
					gl.glColor4d(1, 1, 1, 1);
					
					gl.glPushMatrix();
	
					gl.glBegin(GL2.GL_TRIANGLE_STRIP);
					
					double j = x2 + Calc.dirX(b * 16, dir - 90) + Calc.rangedRandom(.6), k = y2 + Calc.dirY(b * 16, dir - 90) + Calc.rangedRandom(.6), l = x2 + Calc.dirX(b * 16, dir + 90) + Calc.rangedRandom(.6), m = y2 + Calc.dirY(b * 16, dir + 90) + Calc.rangedRandom(.6);
					
					gl.glTexCoord2d(0,0);
					gl.glVertex2d(c, d);
					gl.glTexCoord2d(1, 0);
					gl.glVertex2d(j, k);
					gl.glTexCoord2d(0, 1); 
					gl.glVertex2d(e, f);
					gl.glTexCoord2d(1, 1);
					gl.glVertex2d(l, m);
					
					c = j; d = k; e = l; f = m;
					
					gl.glEnd();
					
					gl.glPopMatrix();
					
					gl.glColor4d(1,1,1,1);
					gl.glDisable(GL2.GL_TEXTURE_2D);
					
					Scene.trianglesDrawn += 2;
					
					//System.out.println(jumpBow.get(i).x + ", " + jumpBow.get(i).y);
				}
			}
		}
		
		drawPlayer(x + endAddX, y + endAddY);
		if (hLoop){
			drawPlayer(x - 640, y);
			drawPlayer(x + 640, y);
		}
		if (vLoop){
			drawPlayer(x, y - 480);
			drawPlayer(x, y + 480);
		}
		
		//fuuuuuck!
		canChangeWeapon = true;
	}
	
	public final Sprite HOLD_CHERRY = Sprite.get("sHoldCherryBomb"), NO_CHERRY = Sprite.get("sNoCherryBomb"), GUN = Sprite.get("sStockingGun"), JUMP = Sprite.get("sJumpBow");
	
	public void drawPlayer(double x, double y){
		//draw bombs
		if (Global.gotBombs && Global.selectedWeapon == 1 && !apart){
			int m = 1;
			for (int i=Global.playerMaxBombs-1; i>=0; i--){
				m *= -1;
				double a = 1.0;
				Sprite s = HOLD_CHERRY;
				if (Global.playerBombs <= i){
					s = NO_CHERRY;
					a = .5;
				}
				double mult = Math.min(1, Math.max(-1, bombAddX * (1 + (1-((double)i / (double)Global.playerMaxBombs)))));
				s.render((int)Calc.random(8), Sprite.CENTERED, x - (((bombAddX * 18) + ((10 * i)) * (mult * weaponScale))) + Calc.rangedRandom(.5), y + (bombY * m * Math.abs(Math.pow(i + 1.0, 1.05) / (Global.playerMaxBombs)))  + Calc.rangedRandom(.5), weaponScale, weaponScale, Calc.rangedRandom(1), a * Fg.me.alpha, "ffffff");
			}
		}
		
		double intensity=.25+Calc.random(.25);
		if (Math.abs(hspeed)<1)
			intensity+=Calc.random(.2);
		if (Math.abs(vspeed)<1)
			intensity+=Calc.random(.2);
		if (hspeed==0)
			intensity+=Calc.random(.125);
		if (vspeed==0)
			intensity+=Calc.random(.125);
		if (wallSlide){
			intensity+=Calc.random(.9);
			if (glitch >= 2 && glitch <= 4){
				glitch = Calc.fRandom(11);
			}
		}
		intensity+=1-(glitchTime/glitchStart);
		
		double ax,ay,xs,ys,ang,alp;
		ax=x; ay=y; xs=tempXsc; ys=1; ang=0; alp=1;
		
		if (glitch==0){
			ax+=(-1+Calc.random(2))*(intensity+.5);
			ay+=(-1+Calc.random(2))*(intensity+.5);
			drawGuy(ax, ay, xs, ys, ang, alp);
		}
		else if (glitch==1){
			drawGuy(ax, ay, xs, ys, ang, alp);
			setColor(Calc.random(1),Calc.random(1),Calc.random(1));
			ax+=(-1+Calc.random(2))*(intensity+.5);
			ay+=(-1+Calc.random(2))*(intensity+.5);
			drawGuy(ax, ay, xs, ys, ang, .1);
			setColor(1,1,1);
		}
		else if (glitch==2 || glitch==3 || glitch==4){
			if (animation == 0 && (glitch == 2|| glitch == 4) && smg > 0)
				ang = assAng;
			drawGuy(ax, ay, xs, ys, ang, alp);
		}
		else if (glitch==5){
			if (animation == 0 && smg > 0)
				ang = assAng;
			ang+=(-4+Calc.random(8))*(intensity+.25);
			drawGuy(ax, ay, xs, ys, ang, alp);
		}
		else if (glitch==6){
			int lol=(int)((4+(int)Calc.random(8))*intensity);
			glitchTime-=1.5;
			lol+=2;
			for (int i=0; i<Math.min(lol, 32); i++){
				alp=Calc.random(.2);
				ang=(-2+Calc.random(4))*(intensity+.25);
				ax+=(-2+Calc.random(4))*(intensity+.5);
				ay+=(-2+Calc.random(4))*(intensity+.5);
				drawGuy(ax, ay, xs, ys, ang, alp);
			}
		}
		else if (glitch==7){
			xs*=1-(intensity/8)+Calc.random(intensity/4);
			ys=1-(intensity/8)+Calc.random(intensity/4);
			drawGuy(ax, ay, xs, ys, ang, alp);
		}
		else if (glitch==8){
			//setColor(Calc.getColorHSV((int)Calc.random(256), 255, 255));
			ax+=(-1+Calc.random(2))*(intensity+.1);
			ay+=(-1+Calc.random(2))*(intensity+.1);
			drawGuy(ax, ay, xs, ys, ang, alp);
			//setColor(1,1,1);
		}
		else if (glitch==9){
			ang += (glitchX * 1.1) + (glitchY * 1.1);
			ax += glitchX;
			ay += glitchY;
			drawGuy(ax, ay, xs, ys, ang, alp);
		}
		else if (glitch == 10){
			ax += glitchX;
			ay += glitchY;
			drawGuy(ax, ay, xs, ys, ang, alp);
		}
		
		//draw key
		if (!Global.heldKey.equals("") && !apart){
			double fx=Calc.rangedRandom(.45), fy=Calc.rangedRandom(.45);
			
			if (animation==1 && (int)img==1)
				fy-=1.5;
			
			key.render(0,Sprite.CENTERED,kX+fx+Calc.rangedRandom(.325),kY+fy+Calc.rangedRandom(.325),xsc,1,0,Fg.me.alpha,1,1,1);
			key.render(1,Sprite.CENTERED,kX+fx+Calc.rangedRandom(.325),kY+fy+Calc.rangedRandom(.325),xsc,1,0,Fg.me.alpha,Global.heldKey);
		}
		
		//draw gun
		if (Global.gotGun && Global.selectedWeapon == 0 && !apart){
			int zsc = xsc;
			if (wallLeft)
				zsc = 1;
			if (wallRight)
				zsc = -1;
			GUN.render((int)gunImg, Sprite.CENTERED, x + ((28 - (gunMult * 12)) * Math.signum(zsc)) + Calc.rangedRandom(gunMult * 4), y + Calc.rangedRandom(gunMult * 4) + (vspeed / 16.0), zsc * weaponScale, weaponScale, 20 * gunMult * Math.signum(zsc), Fg.me.alpha, "ffffff");
		}
		
		//draw doors
		if (Global.gotDoors && Global.selectedWeapon == 2 && !apart){
			Sprite.get("sHammer").render((int)gunImg, Sprite.CENTERED, x + ((25 - (hamMult * 12)) * Math.signum(xsc)) + Calc.rangedRandom(hamMult * 4), y + Calc.rangedRandom(hamMult * 4) + (vspeed / 16.0), xsc * weaponScale, weaponScale, -40 * hamMult * Math.signum(xsc), 1, "ffffff");
		}
		
		//draw get bomb
		if (bombShow > 0 && !apart)
			BombBaby.JUMP.render(0, Sprite.CENTERED, x + Calc.rangedRandom(.5), y - 32 + bombShowY + Calc.rangedRandom(.5), 1, 1, 0, 1, 1, 1, 1);
		
		//draw get bomb
		if (hammerShow > 0 && !apart)
			Sprite.get("sNails").render(0, Sprite.CENTERED, x + Calc.rangedRandom(.5), y - 32 + hammerShowY + Calc.rangedRandom(.5), 1, 1, 0, 1, 1, 1, 1);
	}
	
	/**code for drawing the character herself lmao*/
	public void drawGuy(double x, double y, double xc, double yc, double ang, double alp){
		alp *= endAlpha;
		
		if ((hurtTime<=0 || hurtTime % 2 ==0) && Global.mainActive && !apart){
			//draw your body
			spr.render((int)img, Sprite.CENTERED, x, y, xc, yc, ang, alp, colR, colG, colB);
			//draw face if youre jumping
			if (animation==2)
				jFace.render((int)face, Sprite.CENTERED, x, y, xc, yc, ang, alp, colR, colG, colB);
			
			if (Global.glassesEnabled)
				Sprite.get("sGlasses").render((int)(img % 2), Sprite.CENTERED, x + Calc.rangedRandom(.8), y + Calc.rangedRandom(.8), xc, yc, ang, alp, 1, 1, 1);
			
			if (Global.room(10, 6) || Global.heroMode){
				double yy = 0, aang = 0;
				if (animation == 2)
					yy -= 2;
				if (animation == 1 && (int)img == 1)
					yy -= 1.5;
				if (animation == 0 && smg >= 2){
					int g = (int)smg;
					if (g == 2)
						aang = 90;
					if (g == 3)
						aang = 180;
					if (g == 4)
						aang = 270;
				}
				double al = alp;
				if (Global.room(10, 6))
					al = alp * (x / 592.0);
				if (Global.heroMode)
					al = alp;
				bow.render(0, Sprite.CENTERED, x + bs.x, y + yy + bs.y, xc, yc, ang + aang, al, 1, 1, 1);
			}
			//draw blood
			/*BlendMode.MULTIPLY.set();
			for (int i=0; i<3; i++){
				if (blVis[i])
				blud.render(blImg[i], Sprite.CENTERED, x+blX[i]-2+Calc.random(4), y+blY[i]-2+Calc.random(4), blSc[i]-.05+Calc.random(.1), blSc[i]-.05+Calc.random(.1), blAng[i]-2+Calc.random(4), blood, colR, colG, colB);
			}
			BlendMode.NORMAL.set();*/
		}
	}
	
	public static final Sprite AIR_TICK = Sprite.get("sAirTick");
	
	public void drawAir(){
		if (tickYsc > 0){
			
			int[] input=Calc.getColorHex(Global.bloodOutlineColor);
			double rr = input[0]/255.0, gg = input[1]/255.0, bb = input[2]/255.0;
			
			double a = Calc.rangedRandom(.5), b = Calc.rangedRandom(.5);
			
			double amt = 30;
			double mult = 100.0 / amt;
			
			for (int i=0; i<amt; i++){
				if (Math.round(air) > i)
					AIR_TICK.render(0, Sprite.WEST, x + a + Calc.dirX(40 - (8 * tickYsc), (double)i * 3.6 * mult), y + b + Calc.dirY(40 - (8 * tickYsc), (double)i * 3.6 * mult), tickYsc * .175, .175, (double)i * 3.6 * mult, 1, rr, gg, bb);
			}
		}
	}

}
