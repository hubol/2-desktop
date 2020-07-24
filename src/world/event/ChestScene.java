package world.event;

import java.util.ArrayList;

import audio.Audio;
import world.control.EV;
import world.control.Glitch;
import world.control.Global;
import world.control.Hud;
import world.control.IO;
import world.control.Sound;
import world.control.SoundLoader;
import world.control.SpriteLoader;
import world.particles.ChestStar;
import graphics.Font;
import graphics.Graphics;
import graphics.Shape;
import graphics.Sprite;
import graphics.Text;
import main.Calc;
import main.Entity;
import main.Scene;

public class ChestScene extends Entity{
	public Sprite item;
	
	public boolean bonk, complete, open;
	public double chestY, chestV, chestA, toA, damage, intensity, size, sin;
	public int timer, phase, showFlash;
	
	public double fistY;
	public boolean fistIn, fistOut;
	public int leftTimer, rightTimer;
	
	public SoundLoader mine;
	public SpriteLoader sprites;
	public Glitch myGlitch;
	
	public boolean showItem, showItemFlash;
	public double itemY;
	
	public int explodeTimer, textShow;
	
	public String itemName="", toTweet="";
	
	public boolean display;
	
	public double tuneCount;
	public Chest mother;
	
	public String string;
	
	/**jesus fuckin christ*/
	public void unlock(){
		if (string.equals("sCutsceneStench")){
			//TODO ????
		}
		else if (string.equals("sCutsceneCoolBoots"))
			Global.gotBoots = true;
		else if (string.equals("sCutsceneJellyMap")){
			Global.gotMapDoor = true;
			Global.event[EV.MAP_VIEW] = 1;
			if (Global.gotMapIcons)
				Global.event[EV.MAP_VIEW] = 3;
		}
		else if (string.equals("sCutsceneBloodyMap")){
			Global.gotMapIcons = true;
			Global.event[EV.MAP_VIEW] = 2;
			if (Global.gotMapDoor)
				Global.event[EV.MAP_VIEW] = 3;
		}
		else if (string.equals("sCutsceneBomb"))
			Global.unlockWeapon(1);
		else if (string.equals("sCutsceneSnorkel")){
			//TODO>???
		}
		else if (string.equals("sCutsceneClover")){
			Global.gotClover = true;
			Global.luck = 1.55;
		}
		else if (string.equals("sCutsceneBanner")){
			Global.gotWallSlide = true;
		}
		else if (string.equals("sCutsceneGun")){
			Global.unlockWeapon(0);
		}
		else if (string.equals("sCutsceneBombUp")){
			Global.event[EV.BOMBUPGRADE] = 1;
			Global.playerBombs = Global.playerMaxBombs;
		}
		else if (string.equals("sCutsceneBulb"))
			Global.event[EV.BULB] = 1;
	}
	
	public ChestScene(double x, double y, String s) {
		super(x, y);
		setDepth(Integer.MIN_VALUE+1); //TODO change
		
		ArrayList<AmbientLoop> list = Scene.getEntityList(AmbientLoop.class);
		for (int i=0; i<list.size(); i++)
			list.get(i).stepActive = false;
		
		mine = new SoundLoader("sChestComplete","sChestOpen","sChestOpenChime","sChestExplode","sChestOpenTune");
		sprites = new SpriteLoader("sChestFists_2", s+"_2");
		myGlitch = new Glitch();
		
		display = true;
		
		mother = null;
		
		string = s;
		item = Sprite.get(s);
		
		if (s.equals("sCutsceneStench")){
			itemName = "the stench";
			toTweet = "i #smell really bad! #frowning, #nosed #urchins will promptly explode when i get near them!!! :vc";
		}
		else if (s.equals("sCutsceneCoolBoots")){
			itemName = "the cool boots";
			toTweet = "these #coolboots are cute and expensive! now i can stand on those big colorful #urchins!!!";
		}
		else if (s.equals("sCutsceneJellyMap")){
			itemName = "jelly map upgrade";
			toTweet = "this artifically flavored map #upgrade allows me to press #interact on the pause map to display #door connections!!! wooo!!";
		}
		else if (s.equals("sCutsceneBloodyMap")){
			itemName = "bloody map upgrade";
			toTweet = "this unsanitary map #upgrade allows me to press #interact on the pause map to display cute #icons!!!!! wowowow!!!";
		}
		else if (s.equals("sCutsceneBomb")){
			itemName = "cherry bombs";
			toTweet = "i found some adorable #cherrybombs!!! i can press #power to place one!!!!! they're supe #dangerous aah!!!";
		}
		else if (s.equals("sCutsceneSnorkel")){
			itemName = "the snorkel";
			toTweet = "got a #snorkel!!!! perfect for diving in pools of #blood!!!!!";
		}
		else if (s.equals("sCutsceneClover")){
			itemName = "the clover";
			toTweet = "i plucked a cute #clover!!!! my #luck has increased by 55 percent!!!!! whatever that means!!!";
		}
		else if (s.equals("sCutsceneGun")){
			itemName = "the stocking";
			toTweet = "got a cute knitted #stocking that fires #ammo!! it reloads whenever i enter a room!! i can swap weapons with #interact!!";
		}
		else if (s.equals("sCutsceneBanner")){
			itemName = "the banner";
			toTweet = "i got the enchanted #banner!!! i can now #jump off of walls while #sliding!!! hella cool!!!";
		}
		else if (s.equals("sCutsceneBombUp")){
			itemName = "bomb upgrade";
			toTweet = "i got the adorable #cherrybomb upgrade!! this #upgrade makes my #bombs both cooler and #stackable!! i can also stand on them!! #amazing!!";
		}
		else if (s.equals("sCutsceneBulb")){
			itemName = "bombastic bulb";
			toTweet = "i found the #bombastic bulb!! this bulb will reveal hidden #terrain!! wow!!";
		}
		
		tuneCount = 0;
		textShow = 0;
		fistY = 960;
		fistIn = false;
		fistOut = false;
		leftTimer = 0;
		rightTimer = 0;
		
		explodeTimer = -1;
		
		bonk = false;
		complete = false;
		open = false;
		
		showItem = false;
		showItemFlash = false;
		itemY = 280;
		
		chestY = - 128;
		chestV = 14;
		
		toA = 0;
		chestA = 0;
		
		timer = -1;
		phase = 0;
		
		damage = 0;
		intensity = 0;
		
		size = 1;
		sin = 0;
		showFlash = 0;
	}
	
	public void step(){
		if (!complete){
			chestV += 2.3;
			
			chestY += chestV;
			if (chestY >= 240 && chestV > 0 && !complete){
				bonk = true;
				Sound.playPitched("sChestClang",.1);
				chestV *= -.71;
				chestY = 240;
				
				if (chestV >= -20)
					fistIn = true;
				
				if (chestV >= -1){
					chestV = 0;
					complete = true;
					timer = 7;
				}
			}
			
			if (bonk){
				chestA = Calc.approach(chestA, toA, 6);
				toA = Calc.approach(toA, chestV * 2.0, 12);
			}
		}
		else{
			timer -= 1;
			if (timer <= 0){
				if (phase == 0){
					Sound.playPitched("sChestIndicate",.1);
					phase = 1;
				}
				
				if (phase == 3){
					Sound.playPitched("sChestOpen",.05);
					//Audio.get("sChestOpenChime").setPitch(.7);
					Sound.playPitched("sChestOpenChime",.05);
					phase = 4;
					open = true;
					showItem = true;
					
					mother.opened = true;
					
					Audio.get("sChestOpenTune").setGain(0);
					Audio.get("sChestOpenTune").setLooping(true);
					Sound.play("sChestOpenTune");
					Audio.fade("sChestOpenTune", .5, .05);
					
					explodeTimer = 300;
				}
				
				if (phase == 7){
					display = false;
					//TODO to tweet
					Global.toTweetScene(toTweet);
					phase = 8;
					
					if (mother.id != 2)
						mother.review(toTweet);
				}
				
				if (phase == 6){
					Sound.playPitched("sChestExplode",.05);
					textShow += 1;
					phase = 7;
					timer = 120;
					
					unlock();
				}
				
				if (phase == 5){
					Sound.playPitched("sChestExplode",.05);
					textShow += 1;
					phase = 6;
					timer = 15;
				}
			}
			
			if (phase >= 3 && phase <= 9){
				for (int i=0; i<2; i++)
					new ChestStar(310,240);
			}
			
			if (phase >= 4){
				itemY = Calc.approach(itemY, 130, 40);
				itemY -= .2;
				
				if (phase == 4 && itemY <= 220){
					timer = 1;
					phase = 5;
					tuneCount = 0;
				}
				
				if (itemY <= 130)
					showItemFlash = true;
			}
			
			if (phase == 1 && IO.checkFrameKey(Global.DOWN)){
				Sound.playPitched("sChestIndicate",.1);
				phase = 2;
			}
			
			if (phase == 2){
				intensity = Calc.approach(intensity, 0, 12.0);
				
				if (IO.checkFrameKey(Global.DOWN)){
					for (int i=0; i<6; i++)
						new ChestStar(320, chestY);
					
					boolean bluh = (Boolean)Calc.choose(true, false);
					if (bluh){
						if (rightTimer <= 0)
							rightTimer = 4;
						else
							leftTimer = 4;
					}
					else{
						if (leftTimer <= 0)
							leftTimer = 4;
						else
							rightTimer = 4;
					}
					
					chestV = -10 - (damage * 4);
					chestY = Calc.approach(chestY, 240, 1.6);
					showFlash = 2;
					damage += .088;
					intensity = 1.0;
					Sound.playPitched("sChestHit",.1);
					Sound.playPitched("sChestClang",.1);
					
					if (damage >= .65){
						Sound.playPitched("sChestBreak",.1);
						chestV -= 3;
						
						for (int i=0; i<4; i++){
							ChestStar j = new ChestStar(320, chestY);
							j.addAngle *= 20;
							j.setSpeed(j.getSpeed() * 2);
							j.xscale *= 2;
							j.yscale = j.xscale;
						}
					}
					
					if (damage >= 1.0){
						Sound.playPitched("sChestComplete",.1);
						//TODO success sound
						fistOut = true;
						intensity = 0;
						damage = .1;
						phase = 3;
						timer = 30;
						
						chestY = 240;
					}
				}
				
				if (phase > 0){
					if (chestY < 240){
						chestV += 2;
					}
					else if (chestV > 0){
						chestY = 240;
						chestV = 0;
					}
					
					chestY += chestV;
				}
				
				damage *= .983;
				damage -= .0005;
				if (damage < 0){
					damage = 0;
					phase = 1;
					Sound.playPitched("sChestIndicate",.1);
				}
				
				if (phase == 2)
					Audio.get("musChest").setPitch(1.0 + (damage / 2.0));
				else if (phase == 3)
					Audio.get("musChest").setPitch(1.5);
			}
			
			sin += 1;
			
			double aprch = 1.0;
			if (phase == 2)
				aprch = 1.0 + (damage / 4.0);
			else if (phase == 3)
				aprch = 1;
			if (phase < 3)
				size = Calc.approach(size, ((aprch + 1.0) / 2.0) + (.05 * aprch * Math.abs(Math.sin(sin / 15.0))), 5);
			else
				size = Calc.approach(size, 1.0, 18);
		}
		
		Hud.hideAll();
		
		showFlash -= 1;
		leftTimer -= 1;
		rightTimer -= 1;
		explodeTimer -= 1;
		
		tuneCount += 1;
		if (open)
			Audio.get("sChestOpenTune").setPitch(.93 + (.07 * (tuneCount / 180.0)));
		
		if (open && display){
			for (int i=0; i<3; i++)
				new ChestStar(300 + Calc.rangedRandom(120), 290, true);
		}
		
		if (fistOut)
			fistY = Calc.approach(fistY, 960, 18);
		else if (fistIn)
			fistY = Calc.approach(fistY, 480, 21);
		
		if (open)
			Audio.fade("musChest",0,.086);
	}
	
	public void render(){
		if (display){
			//opened
			if (!open){
				double xx = Calc.rangedRandom(32) * damage * intensity, yy = Calc.rangedRandom(32) * damage * intensity;
				
				Sprite.get("sCutsceneChest").render(2, Sprite.CENTERED, 320 + Calc.rangedRandom(.7) + xx, chestY + Calc.rangedRandom(.7) + yy, size, size, chestA, 1, Global.roomColor);
				Sprite.get("sCutsceneChest").render(1, Sprite.CENTERED, 320 + Calc.rangedRandom(.7) + xx, chestY + Calc.rangedRandom(.7) + yy, size, size, chestA, 1, 1, 1, 1);
				Sprite.get("sCutsceneChest").render(0, Sprite.CENTERED, 320 + Calc.rangedRandom(.7) + xx, chestY + Calc.rangedRandom(.7) + yy, size, size, chestA, 1, 1, 1, 1);
				if (showFlash > 0)
					Sprite.get("sCutsceneChestAction").render((Integer)Calc.choose(0,1), Sprite.CENTERED, 320 + Calc.rangedRandom(4) + xx, chestY + Calc.rangedRandom(4) + yy, size, size, chestA, 1, 1, 1, 1);
			}
			else{
				Graphics.setAlpha(1);
				/*Graphics.setColor("189EFF");
				for (int i=0; i<6; i++)
					spark(308, 240, 400 + Calc.random(200), 16 + (int)Calc.random(12));*/
				
				double xx = 0, yy = 0;
				
				Sprite.get("sCutsceneChest").render(4, Sprite.CENTERED, 320 + Calc.rangedRandom(.7) + xx, chestY + Calc.rangedRandom(.7) + yy, size, size, chestA, 1, 1, 1, 1);
				Sprite.get("sCutsceneChest").render(3, Sprite.CENTERED, 320 + Calc.rangedRandom(.7) + xx, chestY + Calc.rangedRandom(.7) + yy, size, size, chestA, 1, 1, 1, 1);
				
				ArrayList<ChestStar> list = Scene.getEntityList(ChestStar.class);
				for (int i=0; i<list.size(); i++){
					ChestStar u = list.get(i);
					if (u.special)
						u.r();
				}
				
				//AHAHAHAHAHAHSDFFFF
				if (showItem && !showItemFlash){
					item.render(0, Sprite.CENTERED, 290 + Calc.rangedRandom(.7), itemY + Calc.rangedRandom(.7), 1, 1, 0, 1, 1, 1, 1);
					item.render(1, Sprite.CENTERED, 290 + Calc.rangedRandom(.7), itemY + Calc.rangedRandom(.7), 1, 1, 0, 1, 1, 1, 1);
				}
				
				Sprite.get("sCutsceneChest").render(7, Sprite.CENTERED, 320 + Calc.rangedRandom(.7) + xx, chestY + Calc.rangedRandom(.7) + yy, size, size, chestA, 1, Global.roomColor);
				Sprite.get("sCutsceneChest").render(6, Sprite.CENTERED, 320 + Calc.rangedRandom(.7) + xx, chestY + Calc.rangedRandom(.7) + yy, size, size, chestA, 1, 1, 1, 1);
				Sprite.get("sCutsceneChest").render(5, Sprite.CENTERED, 320 + Calc.rangedRandom(.7) + xx, chestY + Calc.rangedRandom(.7) + yy, size, size, chestA, 1, 1, 1, 1);
				
				if (showItem && showItemFlash){
					/*Graphics.setColor("FFFA0C");
					for (int i=0; i<6; i++)
						spark(290, itemY, 160 + Calc.random(80), 20);*/
					
					/*double dir = 0;
					for (int i=0; i<8; i++){
						double sc = .7;
						if (i % 2 == 0)
							sc = .4;
						Sprite.get("sStarburstShit").render(0, Sprite.WEST, 290, itemY, 1, sc, (sin * 1.7) + dir, 1, 1, 1, 1);
						dir += (360.0 / 8.0);
					}
					
					Sprite.get("sStarburst").render(0, Sprite.CENTERED, 290, itemY, .7, .7, sin * 5.5, 1, 1, 1, 1);*/
					//Sprite.get("sStarburst").render((sin / 3.0) % 4.0, Sprite.CENTERED, 290, itemY, 1.1, 1.1, sin, 1, 1, 1, 1);
					myGlitch.render(item, 0, Sprite.CENTERED, 290 + Calc.rangedRandom(.7), itemY + Calc.rangedRandom(.7), 1, 1, 0, 1, "ffffff");
					myGlitch.render(item, 1, Sprite.CENTERED, 290 + Calc.rangedRandom(.7), itemY + Calc.rangedRandom(.7), 1, 1, 0, 1, "ffffff");
				}
			}
			
			//fists
			int img = 0;
			double mult = 1;
			if (rightTimer > 0){
				img = 1;
				mult = 0;
			}
			Sprite.get("sChestFists").render(img, Sprite.SOUTH, 320 + (Calc.dirX(2.8, sin * 7.5) * mult) + Calc.rangedRandom(.2), fistY + (Calc.dirY(2.8, sin * 7.6) * mult) + Calc.rangedRandom(.2) + 3, 1, 1, 0, 1, 1, 1, 1);
			img = 0;
			mult = 1;
			if (leftTimer > 0){
				img = 1;
				mult = 0;
			}
			Sprite.get("sChestFists").render(img, Sprite.SOUTH, 280 - (Calc.dirX(2.8, sin * 7.7) * mult) + Calc.rangedRandom(.2), fistY + (Calc.dirY(2.8, sin * 7.6) * mult) + Calc.rangedRandom(.2) + 13, -1, 1, 0, 1, 1, 1, 1);
		
			//text/bar
			if (phase == 1){
				double a=-11, b=0;
				
				a += Math.round(Calc.rangedRandom(16) / 8.0);
				b += Math.round(Calc.rangedRandom(16) / 8.0);
				
				Text.setFont(Global.FONT);
				Text.randomize(.2);
				Text.orientation = Text.CENTERED;
				Graphics.setAlpha(.5);
				Graphics.setColor("21A6FF");
				/*Text.drawTextExt(323+a, 442+b, "TAP INTERACT KEY!!!", .7, .6, 0);
				Text.drawTextExt(317+a, 442+b, "TAP INTERACT KEY!!!", .7, .6, 0);
				Text.drawTextExt(323+a, 448+b, "TAP INTERACT KEY!!!", .7, .6, 0);
				Text.drawTextExt(317+a, 448+b, "TAP INTERACT KEY!!!", .7, .6, 0);*/
				fancyText(323+a, 442+b, "TAP DOWN!!!", .7, .6);
				fancyText(317+a, 442+b, "TAP DOWN!!!", .7, .6);
				fancyText(323+a, 448+b, "TAP DOWN!!!", .7, .6);
				fancyText(323+a, 448+b, "TAP DOWN!!!", .7, .6);
				Graphics.setAlpha(1.0);
				Graphics.setColor("FF283A"/*"FF283A"*/);
				//Text.drawTextExt(320+a, 445+b, "TAP INTERACT KEY!!!", .7, .6, 0);
				fancyText(320+a, 445+b, "TAP DOWN!!!", .7, .6);
				Text.randomize(0);
			}
			else if (phase == 2){
				Graphics.setAlpha(1.0);
				Graphics.setColor("FF283A");
				Shape.drawRectangle(0, 460, 640, 480);
				Graphics.setColor("21A6FF");
				Shape.drawRectangle(0, 460, (640 * damage) + Calc.rangedRandom(1), 480);
			}
			
			double aX = 0;
			
			Graphics.setAlpha(1);
			Graphics.setColor("21A6FF");
			
			String s = "";
			if (textShow > 0)
				s += "YOU GOT";
			if (textShow > 1)
				s += "%"+itemName.toUpperCase()+"!!!!!";

			if (textShow > 0){
				fancyText(aX + 323, 397, s,.8,.9);
				fancyText(aX + 317, 397, s,.8,.9);
				fancyText(aX + 323, 403, s,.8,.9);
				fancyText(aX + 317, 403, s,.8,.9);
			}
			Graphics.setAlpha(1.0);
			Graphics.setColor("FF283A"/*"FF283A"*/);
			if (textShow > 0)
				fancyText(aX + 320, 400, s,.8,.9);
		}
	}
	
	public void spark(double x, double y, double dist, int parts){
		double xx = x, yy = y, dir, width;
		width = 16;
		dir = Calc.random(360);
		while (Calc.pointDistance(x, y, xx, yy) < dist){
			double xxx = xx + Calc.dirX(dist / (double)parts, dir), yyy = yy + Calc.dirY(dist / (double)parts, dir);
			Shape.drawLine(xx, yy, xxx, yyy, width);
			xx = xxx;
			yy = yyy;
			dir += Calc.rangedRandom(30);
			width = Calc.approach(width, 0, 14);
		}
	}
	
	public void fancyText(double x, double y, String s, double xsc, double ysc){
		Font f = Global.FONT;
		
		int line = 0;
		
		double xx = x - (((double)s.split("%")[line].length() / 2.0) * 26.0);
		for (int i=0; i<s.length(); i++){
			while (s.charAt(i) == '%'){
				line += 1;
				xx = x - (((double)s.split("%")[line].length() / 2.0) * 26.0);
				y += (ysc * 32.0) + 8.0;
				i += 1;
			}
			Sprite.get("font").render(f.fetchSymbolId(s.charAt(i)), Sprite.CENTERED, xx + Calc.rangedRandom(.08) + 12, y + (4.0 * Math.sin((sin + (double)(i * 4))/(double)(s.length()/4.0))) + Calc.rangedRandom(.08), xsc, ysc, 0, Graphics.alpha, Graphics.color[0], Graphics.color[1], Graphics.color[2]);
			xx += 26;
		}
	}
	
	public void destroy(){
		sprites.destroy();
		myGlitch.destroy();
		super.destroy();
	}

}
