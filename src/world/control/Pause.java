package world.control;

import world.Root;
import world.player.Player;
import audio.Audio;
import graphics.Graphics;
import graphics.Shape;
import graphics.Sprite;
import graphics.Text;
import main.Calc;
import main.Entity;
import main.Main;

public class Pause extends Entity{
	public static Pause me;
	
	public boolean paused, contains;
	public Sprite pauseSprite;
	
	public int page;
	public final int pages = 4;
	
	public String[] pageColor, pageTitle;
	
	public boolean toPause, pausing;
	public int pauseTimer;
	
	public int rArrow, lArrow;
	public int arrowTimer;
	public double arrowPosition;
	
	public double pageAlpha;
	
	public int optionSelect, quitSelect, eyeImage;

	public Pause(double x, double y) {
		super(x, y);
		
		reset();
	}
	
	public void reset(){
		arrowPosition = 0;
		rArrow = 0;
		lArrow = 0;
		arrowTimer = -1;
		
		pauseTimer = 0;
		toPause = false;
		pausing = false;
		
		me = this;
		paused = false;
		contains = false;
		
		pageAlpha = 1;
		
		setCollisionGroup(Global.CONTROLLER);
		persistent = true;
		setDepth(Integer.MIN_VALUE + 1);
		
		page = 0;
		alpha = 0;
		
		optionSelect = 0;
		quitSelect = 0;
		
		pageColor = new String[pages];
		pageColor[0] = "FF3866";
		pageColor[1] = "D1FF3A";
		pageColor[2] = "23E153";
		pageColor[3] = "51FFD0";
		
		pageTitle = new String[pages];
		pageTitle[0] = "MAP";
		pageTitle[1] = "THINGS";
		pageTitle[2] = "OPTIONS";
		pageTitle[3] = "QUIT";
		
		alarmInitialize(1);
		eyeImage = 0;
	}
	
	public void alarmEvent(int i){
		if (i == 0){
			eyeImage = 0;
		}
	}
	
	public void allStop(){
		for (int i=1; i<5; i++){
			Audio.get("sPause"+i).stop();
			Audio.get("sUnpause"+i).stop();
		}
	}
	
	public void pressSound(String s){
		s = s + ((int)Calc.random(4) + 1);
		Audio.get(s).setGain(.9);
		Audio.fade(s, .9, .9);
		Sound.playPitched(s, .007);
	}
	
	public int retrievePage(int id){
		if (id < 0)
			id += pages;
		else if (id > pages - 1)
			id -= pages;
		
		return id;
	}
	
	public void changePage(int add){
		page = retrievePage(page + add);
		
		rArrow = 0;
		lArrow = 0;
		arrowTimer = 6;
		
		Sound.playPitched("sReadFlick");
		
		if (add > 0){
			rArrow = 1;
			Sound.playPitched("sReadForward");
		}
		else if (add < 0){
			lArrow = 1;
			Sound.playPitched("sReadBackward");
		}
		
		pageAlpha = 0;
		arrowPosition = 32;
	}
	
	public void step(){
		if (Global.mainActive && !Global.playerDead){
			angle += 1;
			if (angle >= 360)
				angle -= 360;
			
			if (IO.checkFrameKey(Global.PAUSE) && Player.control){
				allStop();
				//System.out.println("fuck!!!!");
				if (!pausing){
					paused = !paused;
					pauseStuff();
				}
				else
					toPause = true;
			}
			
			Global.paused = paused;
			
			pageAlpha = Calc.approach(pageAlpha, 1, 5);
			
			if (paused){
				Music.pauseVolume(paused);
				//Hud.showAll();
				
				if (arrowTimer > -1){
					arrowTimer -= 1;
					if (arrowTimer == 0){
						rArrow = 0;
						lArrow = 0;
					}
				}
				
				arrowPosition = Calc.approach(arrowPosition, 0, 3);
				
				if (arrowTimer <= 4 && alpha >= 1){
					int add = 0;
					if (IO.checkFrameKey(Global.LEFT))
						add -= 1;
					if (IO.checkFrameKey(Global.RIGHT))
						add += 1;
					if (add != 0)
						changePage(add);
				}
				
				//MAPPA CHANGE!!!
				if (page == 0 && (Global.gotMapDoor || Global.gotMapIcons) && eyeImage == 0){
					if (IO.checkFrameKey(Global.INTERACT)){
						eyeImage = 1;
						alarm[0] = 5;
						Sound.playPitched("sPauseMenuConfirm");
						Global.event[EV.MAP_VIEW] += 1;
						if (Global.event[EV.MAP_VIEW] > 3)
							Global.event[EV.MAP_VIEW] = 0;
						else{
							if (Global.event[EV.MAP_VIEW] == 1 && !Global.gotMapDoor)
								Global.event[EV.MAP_VIEW] += 1;
							if ((Global.event[EV.MAP_VIEW] == 2 && !Global.gotMapIcons)||(Global.event[EV.MAP_VIEW] == 3 && (!Global.gotMapDoor || !Global.gotMapIcons)))
								Global.event[EV.MAP_VIEW] = 0;
						}
					}
				}
				
				//OPTIONS / QUIT CONTROL!!!!
				if (page >= 2){
					int add = 0;
					if (IO.checkFrameKey(Global.UP))
						add -= 1;
					if (IO.checkFrameKey(Global.DOWN))
						add += 1;
					
					if (add != 0){
						int[] options = {2, 3};
						
						if (add < 0)
							Sound.playPitched("sPauseMenuUp");
						else
							Sound.playPitched("sPauseMenuDown");
						
						int at = optionSelect + add;
						if (page == 3)
							at = quitSelect + add;
						
						if (at > options[page - 2] - 1)
							at -= options[page - 2];
						else if (at < 0)
							at += options[page - 2];
						
						if (page == 2)
							optionSelect = at;
						else
							quitSelect = at;
					}
					
					if (IO.checkFrameKey(Global.INTERACT)){
						Sound.playPitched("sPauseMenuConfirm");
						
						if (page == 2){
							if (optionSelect == 0){
								Global.shiftScreenMode();
							}
							else if (optionSelect == 1){
								Global.mute = !Global.mute;

								if (Global.mute)
									Audio.setGlobalGain(0);
								else
									Audio.setGlobalGain(1);
							}
						}
						else{
							SaveFile.saveStats();
							
							if (quitSelect == 0){
								if (Global.roomX != 16){
									boolean a = Global.mute;
									SaveFile.load(Global.saveFileNumber);
									
									if (a != Global.mute){
										if (Global.mute)
											Audio.setGlobalGain(0);
										else
											Audio.setGlobalGain(1);
									}
									
									Sound.playPitched("sRestart");
									paused = !paused;
									pauseStuff();
								}
								else
									Sound.errorPlay();
							}
							else if (quitSelect == 1){
								//Main.animator.setUpdateFPSFrames(1, null);
								Main.animator.resetFPSCounter();
								Player.me.destroy();
								Global.mainActive = false;
								Global.deactivate();
								Music.changeMusic("");
								Root.changeRoom("d");
							}
							else if (quitSelect == 2)
								System.exit(0);
						}
					}
				}
			}
			
			if (!paused){
				alpha -= .2;
				if (alpha <= 0 && contains){
					contains = false;
					//pauseSprite.remove();
				}
			}
			else if (alpha < 1){
				alpha += .25;
			}
			
			pauseTimer -= 1;
			if (pauseTimer == 0){
				Global.activateGameplayObjects();
				paused = false;
				pausing = false;
				toPause = false;
				Music.pauseVolume(paused);
			}
			else if (pauseTimer == -1 && !paused && pausing && toPause){
				paused = true;
				pauseStuff();
			}
			
			alarmStep();
		}
	}
		
		public void pauseStuff(){
		Music.pauseVolume(paused);
		
		pageAlpha = 1;
		
		if (paused){
			arrowPosition = 0;
			rArrow = 0;
			lArrow = 0;
			arrowTimer = -1;
			
			Hud.me.immediateHide();
			pressSound("sPause");
			//Scene.screenSave("res/pauseTemp.jpg");
			Global.deactivateGameplayObjects();
			//Sprite.loadFile("pauseTemp.jpg", "res/", 1);
			//pauseSprite = Sprite.get("pauseTemp");
			contains = true;
			alpha = 0;
		}
		else{
			Hud.showAll();
			pressSound("sUnpause");
			
			paused = true;
			pausing = true;
			pauseTimer = 3;
			toPause = true;
		}
		arrowTimer = 10;
		
	}
		
	public String wackyFormat(String s){
		return s.toUpperCase().replace('A', (Character)Calc.choose('4','A','A','A','A','A','A','A','A','A','A','A','A','A','A','A','A','A','A','A','A','A','A','A','A','A','A','A','A','A','A','A','A','A','A','A')).replace('I', (Character)Calc.choose('1','I','I','I','I','I','I','I','I','I','I','I','I','I','I','I','I','I','I','I','I','I','I','I','I','I','I','I','I','I','I','I','I','I','I','I')).replace('O', (Character)Calc.choose('0','O','O','O','O','O','O','O','O','O','O','O','O','O','O','O','O','O','O','O','O','O','O','O','O','O','O','O','O','O')).replace('S', (Character)Calc.choose('2','5','$','S','S','S','S','S','S','S','S','S','S','S','S','S','S','S','S','S','S','S','S','S','S','S','S','S','S','S','S','S','S','S','S','S','S','S','S','S','S','S','S','S','S','S','S','S','S','S','S','S','S','S','S','S','S','S','S','S','S','S','S','S','S','S','S','S','S','S','S','S','S','S','S','S','S','S','S','S','S','S','S','S','S','S','S','S')).replace('E', (Character)Calc.choose('3','E','E','E','E','E','E','E','E','E','E','E','E','E','E','E','E','E','E','E','E','E','E','E','E','E','E','E','E','E','E','E','E','E','E','E','E','E','E','E','E','E','E')).replace('B', (Character)Calc.choose('8','B','B','B','B','B','B','B','B','B','B','B','B','B','B','B','B','B','B','B','B','B','B','B','B','B','B','B','B','B','B','B','B','B','B','B','B','B','B','B','B','B','B','B'));
	}
	
	public void render(){
		if (Global.mainActive){
			if (contains && alpha > 0){
				//pauseSprite.render(0, Sprite.CENTERED, 319 + Calc.rangedRandom(.75), 239 + Calc.rangedRandom(.75), 1.001 + Calc.random(.001), 1.001 + Calc.random(.001), Calc.rangedRandom(.2), alpha*.7, "ffffff");
			}
			Graphics.setAlpha(alpha/3.0);
			Graphics.setColor(.2,.2,.2);
			Sprite.get("sPauseOverlay").render(0, Sprite.CENTERED, 319 + Calc.rangedRandom(.75), 239 + Calc.rangedRandom(.75), 1.01125 + Calc.random(.001), 1.011 + Calc.random(.001), Calc.rangedRandom(.2), alpha*.73, .6, .6, .6);
			//Shape.drawRectangle(0, 0, 640, 480);
			Graphics.setAlpha(1);
			
			double xx = Calc.rangedRandom(1), yy = Calc.rangedRandom(1);
			for (int i=0; i<64; i++){
				Graphics.setAlpha(alpha/3.0);
				Graphics.setColor(.2,.2,.2);
				Shape.drawRectangle(xx, (i*8) + yy, 640 + xx, ((i + .5) *8) + yy);
				Graphics.setAlpha(1);
				xx += Calc.rangedRandom(.0625);
				yy += Calc.rangedRandom(.0625);
			}
			
			/*if (paused){
				Hud.me.render();
			}*/
			
			Graphics.setAlpha(alpha * pageAlpha);
			
			//pause text
			Text.randomize(.3);
			Graphics.setColor(pageColor[page]);
			Text.setFont(Global.FONT);
			Text.orientation = Text.CENTERED;
			textShadow(wackyFormat(pageTitle[page]), 355, 98, 1.9, 2, 0, 4);
			
			Graphics.setAlpha(alpha);
			
			Text.orientation = Text.SOUTHEAST;
			textShadow(wackyFormat("PAUSED"), 648, 488, .5, .5, 0, 4);
			
			//map
			if (page == 0){
				boolean a = false, b = false;
				//String display = "PLAIN MAP";
				if (Global.event[EV.MAP_VIEW] == 1){
					a = true;
					//display = "DOOR CONNECTIONS";
				}
				else if (Global.event[EV.MAP_VIEW] == 2){
					b = true;
					//display = "CUTE ICONS";
				}
				else if (Global.event[EV.MAP_VIEW] == 3){
					a = true;
					b = true;
					//display = "CONNECTIONS AND ICONS";
				}
				Map.drawMap(192, 176, alpha * pageAlpha, a, b);
				
				Graphics.setColor(pageColor[page]);
				Graphics.setAlpha(alpha * pageAlpha);
				Text.randomize(.3);
				
				Text.orientation = Text.CENTERED;
				Text.idiot = false;
				if (Global.gotMapDoor || Global.gotMapIcons){
					/*textShadow(wackyFormat("NOW SHOWING: "+display+", INTERACT TO CHANGE"), 320, 382, .34, .4, 0, 2.8);*/
					textShadow(wackyFormat("INTERACT TO CHANGE"), 320, 382, .43 + (.035 * eyeImage), .43 + (.035 * eyeImage), 0, 3.2);
					double xxx = -160 - (eyeImage * 1), yyy = 378, xs = .8, ys = .8;
					//left
					Sprite.get("sMapShow").render((eyeImage * 2), Sprite.CENTERED, 320+xxx+Calc.rangedRandom(.4), yyy+Calc.rangedRandom(.4), xs, ys, 0, alpha * pageAlpha, "006187");
					Sprite.get("sMapShow").render((eyeImage * 2) + 1, Sprite.CENTERED, 320+xxx+Calc.rangedRandom(.4), yyy+Calc.rangedRandom(.4), xs, ys, 0, alpha * pageAlpha, pageColor[page]);
					
					Sprite.get("sMapShow").render((eyeImage * 2), Sprite.CENTERED, 320-xxx+Calc.rangedRandom(.4), yyy+Calc.rangedRandom(.4), xs, ys, 0, alpha * pageAlpha, "006187");
					Sprite.get("sMapShow").render((eyeImage * 2) + 1, Sprite.CENTERED, 320-xxx+Calc.rangedRandom(.4), yyy+Calc.rangedRandom(.4), xs, ys, 0, alpha * pageAlpha, pageColor[page]);
				}
				Text.idiot = true;
				
				spriteShadow("sNowPlaying", 0, Sprite.CENTERED, 40, 440, 1, 1, 0, alpha * pageAlpha, pageColor[page], 4);
				
				String txt = "NOW PLAYING:%NOTHING IS PLAYING";
				if (Music.titles.containsKey(Music.music)){
					String[] split = Music.titles.get(Music.music).split("-");
					txt = "NOW PLAYING:%" + split[0] + "%" + split[1];
				}
				
				Text.orientation = Text.WEST;
				textShadow(wackyFormat(txt), 80, 452, .5, .5, 0, 4);
				//textShadow(txt, 308, 448, .5, .5, 0, 4);
				
			}
			else if (page == 1){ //things
				xx = 112;
				yy = 118;
				
				int id = 0;
				double size = 42 * (8.0 / 6.0);
				double scale = size / 128.0;
				double half = size / 2.0;
				
				//event items!!!
				
				xx += size / 2.0;
				//for (int i=0; i<2; i++){
					for (int j = 0; j<6; j++){
						if (id < 16){
							STARBURST.render(0, Sprite.CENTERED, xx + half + Calc.rangedRandom(.3), yy + half + Calc.rangedRandom(.3), scale * .45, scale * .45, Calc.boolToInt(Global.eventItem[id] != 0) * angle, thingAlpha(id), thingColor(id));
							if (Global.eventItem[id] != 0)
								Sprite.get("sEventItem").render(id, Sprite.CENTERED, xx + half + Calc.rangedRandom(.3), yy + half + Calc.rangedRandom(.3), scale, scale, 0, alpha * pageAlpha, 1, 1, 1);
							if (Global.eventItem[id] == 2)
								Sprite.get("sEventItemCross").render(0, Sprite.CENTERED, xx + half + Calc.rangedRandom(.9), yy + half + Calc.rangedRandom(.9), scale, scale, 0, alpha * pageAlpha, 1, 1, 1);
						}
						yy += size;
						if (id % 2 == 0)
							xx += size / 2.0;
						else
							xx -= size / 2.0;
						id += 1;
					}
				/*	xx += size;
					yy = 118;
				}*/
				
				//hearts!!!!!
				//378, 172
				if (Global.heartsGot() > 0){
					STARBURST.render(0, Sprite.CENTERED, 378 + Calc.rangedRandom(.3), 167 + Calc.rangedRandom(.3), .32, .32, angle, alpha * pageAlpha, "0095EF");
					Sprite.get("sPauseHeart").render((Integer)Calc.choose(0,2,4,6), Sprite.CENTERED, 378 + Calc.rangedRandom(.3), 167 + Calc.rangedRandom(.3), 1, 1, 0, alpha * pageAlpha, 1, 1, 1);
					
					Graphics.setColor("FF2873");
					Graphics.setAlpha(alpha * pageAlpha);
					Text.orientation = Text.CENTERED;
					textShadow(wackyFormat(Global.heartsGot() + " OF " + Global.heartAmount), 383.8, 193, .3, .3, 0, 2);
				}
				else
					STARBURST.render(0, Sprite.CENTERED, 378 + Calc.rangedRandom(.3), 167 + Calc.rangedRandom(.3), .32, .32, 0, .5 * alpha * pageAlpha, "006187");
				Graphics.setColor(pageColor[page]);
				
				//chests / abilities / what have you
				//378, 286
				if (Global.chestsOpened() > 0){
					STARBURST.render(0, Sprite.CENTERED, 378 + Calc.rangedRandom(.3), 286 + Calc.rangedRandom(.3), .32, .32, angle, alpha * pageAlpha, "0095EF");
					Sprite.get("sPauseChest").render(2, Sprite.CENTERED, 378 + Calc.rangedRandom(.3), 286 + Calc.rangedRandom(.3), .8, .8, 0, alpha * pageAlpha, Global.roomColor);
					Sprite.get("sPauseChest").render(1, Sprite.CENTERED, 378 + Calc.rangedRandom(.3), 286 + Calc.rangedRandom(.3), .8, .8, 0, alpha * pageAlpha, 1, 1, 1);
					Sprite.get("sPauseChest").render(0, Sprite.CENTERED, 378 + Calc.rangedRandom(.3), 286 + Calc.rangedRandom(.3), .8, .8, 0, alpha * pageAlpha, 1, 1, 1);
					
					Graphics.setColor(pageColor[page]);
					Graphics.setAlpha(alpha * pageAlpha);
					Text.orientation = Text.CENTERED;
					textShadow(wackyFormat(Global.chestsOpened() + " OF " + Global.totalChests), 383.8, 315, .3, .3, 0, 2);
				}
				else
					STARBURST.render(0, Sprite.CENTERED, 378 + Calc.rangedRandom(.3), 286 + Calc.rangedRandom(.3), .32, .32, 0, .5 * alpha * pageAlpha, "006187");
				
				//key!!!!!
				double alp = .5, ang = 0;
				String col = "006187";
				if (!Global.heldKey.equals("")){
					alp = 1;
					col = "0095EF";
					ang = angle;
				}
				
				STARBURST.render(0, Sprite.CENTERED, 378 + Calc.rangedRandom(.3), 400 + Calc.rangedRandom(.3), .2, .2, ang, alp * alpha * pageAlpha, col);
				if (alp == 1){
					Sprite.get("sPauseKey").render(1, Sprite.CENTERED, 378 + Calc.rangedRandom(.3), 400 + Calc.rangedRandom(.3), 1, 1, 0, alp * alpha * pageAlpha, Global.heldKey);
					Sprite.get("sPauseKey").render(0, Sprite.CENTERED, 378 + Calc.rangedRandom(.3), 400 + Calc.rangedRandom(.3), 1, 1, 0, alp * alpha * pageAlpha, 1, 1, 1);
				}
				
				//lines!!!
				Sprite.get("sPauseBigDotted").render(0, Sprite.WEST, 230 + Calc.rangedRandom(.3), 116 + Calc.rangedRandom(.3), 1, 1, 270, alpha * pageAlpha, 1, 1, 1);
				Sprite.get("sPauseSmallDotted").render(0, Sprite.WEST, 251 + Calc.rangedRandom(.3), 228 + Calc.rangedRandom(.3), 1, 1, 0, alpha * pageAlpha, 1, 1, 1);
				Sprite.get("sPauseSmallDotted").render(0, Sprite.WEST, 251 + Calc.rangedRandom(.3), 340 + Calc.rangedRandom(.3), 1, 1, 0, alpha * pageAlpha, 1, 1, 1);
			}
			else if (page == 2){//options
				
				STARBURST.render(0, Sprite.CENTERED, 320 + Calc.rangedRandom(.3), 240 + Calc.rangedRandom(.3), 1.1, 1.1, angle, .5 * alpha * pageAlpha, "006187");
				
				Graphics.setAlpha(alpha * pageAlpha);
				Text.orientation = Text.CENTERED;
				
				String txt = "FULLSCREEN: ", colo = "7FA8E2";
				double scale = .5;
				if (optionSelect == 0){
					colo = pageColor[page];
					scale = .62;
				}
				if (Global.screenMode == 1)
					txt += "ON";
				else
					txt += "OFF";
				
				Graphics.setColor(colo);
				textShadow(wackyFormat(txt), 320 + (16 * scale), 200, scale, scale, 0, 3);
				
				txt = "GAME AUDIO: ";
				colo = "7FA8E2";
				scale = .5;
				if (optionSelect == 1){
					colo = pageColor[page];
					scale = .62;
				}
				if (!Global.mute)
					txt += "ON";
				else
					txt += "OFF";
				
				Graphics.setColor(colo);
				textShadow(wackyFormat(txt), 320 + (16 * scale), 280, scale, scale, 0, 3);
				
				
				Graphics.setColor(pageColor[page]);
				txt = "FULLSCREEN IS STRONGLY RECOMMENDED!!";
				if (optionSelect == 1)
					txt = "GAME AUDIO IS STRONGLY RECOMMENDED!!";
				textShadow(wackyFormat(txt), 326, 400, .45, .45, 0, 2);
			}
			else if (page == 3){//quit
				
				STARBURST.render(0, Sprite.CENTERED, 320 + Calc.rangedRandom(.3), 240 + Calc.rangedRandom(.3), 1.1, 1.1, angle, .5 * alpha * pageAlpha, "006187");
				
				Graphics.setAlpha(alpha * pageAlpha);
				Text.orientation = Text.CENTERED;
				
				String colo = "7FA8E2";
				double scale = .5;
				if (quitSelect == 0){
					colo = pageColor[page];
					scale = .62;
				}
				
				Graphics.setColor(colo);
				textShadow(wackyFormat("RESTART FROM LAST SAVE"), 320 + (16 * scale), 170, scale, scale, 0, 3);
				
				colo = "7FA8E2";
				scale = .5;
				if (quitSelect == 1){
					colo = pageColor[page];
					scale = .62;
				}
	
				Graphics.setColor(colo);
				textShadow(wackyFormat("QUIT TO MENU"), 320 + (16 * scale), 240, scale, scale, 0, 3);
				
				colo = "7FA8E2";
				scale = .5;
				if (quitSelect == 2){
					colo = pageColor[page];
					scale = .62;
				}
	
				Graphics.setColor(colo);
				textShadow(wackyFormat("QUIT GAME"), 320 + (16 * scale), 310, scale, scale, 0, 3);
				
				//if (quitSelect >= 1){
					Graphics.setColor("FF184E");
					textShadow(wackyFormat("ALL PROGRESS UP TO THE LAST%%SAVE WILL BE LOST!!!"), 330, 400, .6, .6, 0, 3);
				//}
			}
			
			Graphics.setAlpha(alpha * pageAlpha);
			
			Text.orientation = Text.CENTERED;
			Graphics.setColor(pageColor[retrievePage(page + 1)]);
			textShadow(wackyFormat(pageTitle[retrievePage(page + 1)]), 583, 200, .4, .4, 0, 3);
			Graphics.setColor(pageColor[retrievePage(page - 1)]);
			textShadow(wackyFormat(pageTitle[retrievePage(page - 1)]), 71, 200, .4, .4, 0, 3);
			Graphics.setColor(pageColor[page]);
			
			PAUSE_ARROW.render(rArrow, Sprite.CENTERED, 576 + Calc.rangedRandom(.4) + (arrowPosition * rArrow), 240 + Calc.rangedRandom(.4), .6 + (rArrow * .1), .6 + (rArrow * .1), 0, alpha, pageColor[retrievePage(page + 1)]);
			PAUSE_ARROW.render(rArrow + 2, Sprite.CENTERED, 576 + Calc.rangedRandom(.4) + (arrowPosition * rArrow), 240 + Calc.rangedRandom(.4), .6 + (rArrow * .1), .6 + (rArrow * .1), 0, alpha, 1, 1, 1);
			PAUSE_ARROW.render(lArrow, Sprite.CENTERED, 64 + Calc.rangedRandom(.4) - (arrowPosition * lArrow), 240 + Calc.rangedRandom(.4), -.6 - (lArrow * .1), .6 + (lArrow * .1), 0, alpha, pageColor[retrievePage(page - 1)]);
			PAUSE_ARROW.render(lArrow + 2, Sprite.CENTERED, 64 + Calc.rangedRandom(.4) - (arrowPosition * lArrow), 240 + Calc.rangedRandom(.4), -.6 - (lArrow * .1), .6 + (lArrow * .1), 0, alpha, 1, 1, 1);
			
			Graphics.setAlpha(1);
			Text.randomize(0);
			
			if (paused)
				Hud.me.dicks();
		}
	}
	
	public final static Sprite STARBURST = Sprite.get("sStarburst"), PAUSE_ARROW = Sprite.get("sPauseArrow");
	
	public String thingColor(int id){
		if (Global.eventItem[id] == 0)
			return "006187";
		else
			return "0095EF";
	}
	
	public double thingAlpha(int id){
		if (Global.eventItem[id] == 0)
			return .5 * alpha * pageAlpha;
		else
			return 1 * alpha * pageAlpha;
	}
	
	public void textShadow(String text, double x, double y, double xsc, double ysc, double angle, double spacing){
		String c = Graphics.getColor();
		double s = spacing; 
		
		text = text.toUpperCase();
		
		Graphics.setColor("006187");
		Text.drawTextExt(x + s, y, text, xsc, ysc, angle);
		Text.drawTextExt(x, y + s, text, xsc, ysc, angle);
		Text.drawTextExt(x - s, y, text, xsc, ysc, angle);
		Text.drawTextExt(x, y - s, text, xsc, ysc, angle);
		Text.drawTextExt(x + s - 1, y + s - 1, text, xsc, ysc, angle);
		Text.drawTextExt(x + s - 1, y - s + 1, text, xsc, ysc, angle);
		Text.drawTextExt(x - s + 1, y + s - 1, text, xsc, ysc, angle);
		Text.drawTextExt(x - s + 1, y - s + 1, text, xsc, ysc, angle);
		Graphics.setColor(c);
		Text.drawTextExt(x, y, text, xsc, ysc, angle);
	}
	
	public void spriteShadow(String sprite, int imageSingle, int orientation, double x, double y, double xsc, double ysc, double angle, double alpha, String color, double spacing){
		Sprite p = Sprite.get(sprite);
		int o = orientation;
		double s = spacing;
		
		p.render(imageSingle, o, x + s + Calc.rangedRandom(Text.letterRandom), y + Calc.rangedRandom(Text.letterRandom), xsc, ysc, angle, alpha, "006187");
		p.render(imageSingle, o, x + Calc.rangedRandom(Text.letterRandom), y + s + Calc.rangedRandom(Text.letterRandom), xsc, ysc, angle, alpha, "006187");
		p.render(imageSingle, o, x - s + Calc.rangedRandom(Text.letterRandom), y + Calc.rangedRandom(Text.letterRandom), xsc, ysc, angle, alpha, "006187");
		p.render(imageSingle, o, x + Calc.rangedRandom(Text.letterRandom), y - s + Calc.rangedRandom(Text.letterRandom), xsc, ysc, angle, alpha, "006187");
		p.render(imageSingle, o, x + s - 1 + Calc.rangedRandom(Text.letterRandom), y + s - 1 + Calc.rangedRandom(Text.letterRandom), xsc, ysc, angle, alpha, "006187");
		p.render(imageSingle, o, x - s + 1 + Calc.rangedRandom(Text.letterRandom), y - s + 1 + Calc.rangedRandom(Text.letterRandom), xsc, ysc, angle, alpha, "006187");
		p.render(imageSingle, o, x + s - 1 + Calc.rangedRandom(Text.letterRandom), y - s + 1 + Calc.rangedRandom(Text.letterRandom), xsc, ysc, angle, alpha, "006187");
		p.render(imageSingle, o, x - s + 1 + Calc.rangedRandom(Text.letterRandom), y + s - 1 + Calc.rangedRandom(Text.letterRandom), xsc, ysc, angle, alpha, "006187");
		p.render(imageSingle, o, x + Calc.rangedRandom(Text.letterRandom), y + Calc.rangedRandom(Text.letterRandom), xsc, ysc, angle, alpha, color);
	}
	
	public void destroy(){
		super.destroy();
		me = null;
	}

}
