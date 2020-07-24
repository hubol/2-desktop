package world.minigame;

import java.util.ArrayList;

import audio.Audio;
import world.control.Global;
import world.control.IO;
import world.control.Music;
import world.control.PseudoGenerator;
import world.control.Sound;
import world.particles.Explosion;
import world.player.Player;
import graphics.Graphics;
import graphics.Shape;
import graphics.Sprite;
import graphics.Text;
import main.Calc;
import main.Entity;
import main.Scene;

public class ChopPlayer extends Entity{
	public ChopControl mom;
	public int phase;
	public int phaseTimer;
	
	public int showEarned, earned;
	
	public int textTime;
	public boolean textVis;
	
	public double direction, timer, addX, textY;
	public double a;
	
	public double bar;
	public boolean barDir;
	
	public int times;
	public int intro;
	
	public double addY;
	public double pitch;
	
	public double plalpha;
	public int introPhase;
	public int introTimer;
	
	public double treeAlpha;
	public boolean treeChopped;
	
	public double arrowTimer;
	public int arrow;
	public boolean arrowAttention;
	public double arrowScale = 1.0;
	public double treeHealth = 100;
	
	public double axeAngle, axeAlpha;
	
	public double nextEarn;
	
	public int explodeTime, explodeMax, explodes;
	
	public PseudoGenerator myGen;

	public ChopPlayer(double x, double y, ChopControl m) {
		super(x, y);
		setDepth(Integer.MIN_VALUE+14);
		
		axeAngle = 0;
		axeAlpha = 1;
		
		explodeTime = 0;
		explodeMax = 15;
		explodes = 0;
		
		arrowTimer = 60;
		arrowScale = 1.0;
		arrow = 0;
		arrowAttention = false;
		
		pitch = 1.0;
		
		treeChopped = false;
		treeAlpha = 0;
		
		introPhase = 0;
		introTimer = 30;
		
		plalpha = 1.0;
		
		nextEarn = 95;
		
		a = 0;
		times = 0;
		
		addY = 300;
		
		intro = 45;
		
		sprite = Sprite.get("sTreeMiniGirl");
		imageSpeed = 9.5 / 30.0;
		
		mom = m;
		textY = -64;
		
		bar = 0;
		barDir = true;
		
		phaseTimer = 0;
		
		showEarned = 0;
		earned = 0;
		phase = 0;
		
		xscale = .8;
		yscale = .8;
		addX = 0;
		
		textVis = false;
		resetVis();
		
		reset();
		
		///why am i doing this bluh
		myGen = new PseudoGenerator(1.1, 1.069, .69, 9069, 1.69, 1, 0);
		
		addX = -160;
		phase = 3;
		
		introPhase = 69;
		
		addY = 0;
		textY = 64;
		
		Audio.get("sChopBar").setLooping(true);
		//Audio.get("sChopBar").setGain(0);
		//Audio.fade("sChopBar", 1, .1);
		//Audio.get("sChopBar").setPitch(1.5);
		//Sound.play("sChopBar");
		
		Sound.playPitched("sChopBells", .2);
		phaseTimer = 60;
		
		bar = 75;
		
		//new ChopGemCounter(640 - 64, 416, this);
	}
	
	public void resetVis(){
		textVis = !textVis;
		if (textVis)
			textTime = 20 + (int)Calc.random(90);
		else
			textTime = (int)Calc.random(30);
	}
	
	public void step(){
		if (introPhase <= 4){
			introTimer -= 1;
			if (introTimer <= 0){
				if (introPhase == 0){
					addY = -64;
					
					//TODO oh man sound!!
					introPhase = 5;
				}
			}
		}
		
		if (introPhase > 4){
			plalpha -= .1;
			intro -= 1;
			if (addY < 0){
				addY = Calc.approach(addY, 0, 18);
				addY += .2;
				if (addY > 0)
					addY = 0;
			}
			
			textY = Calc.approach(textY, 64, 10);
		}
		
		if (intro == 0)
			Sound.playPitched("sChopBells",.2);
		
		a += 1;
		
		textTime -= 1;
		if (textTime <= 0)
			resetVis();
		
		if (showEarned != earned){
			//new ChopGem(640 - 64, 416);
			
			//Sound.spendPlay();
			if (showEarned < earned)
				showEarned += 1;
			else
				showEarned -= 1;
		}
		
		if (phase == 0){
			if (IO.checkKey(Global.INTERACT) && intro <= 0){
				Sound.playPitched("sChopBells",.2);
				//TODO sound??
				phase = 1;
				
				Audio.get("sChopBar").setGain(1);
				Audio.fade("sChopBar", 1, 1);
				Audio.get("sChopBar").setLooping(true);
				Audio.get("sChopBar").setPitch(1);
				Sound.play("sChopBar");
			}
		}
		
		if (phase == 1){
			if (barDir){
				if (bar == 100)
					barDir = false;
				bar += 1;
				bar *= 1.3;
				if (bar > 100)
					bar = 100;
			}
			else{
				bar *= .75;
				bar -= 1.5;
				if (bar < 0){
					bar = 0;
					times += 1;
					barDir = true;
				}
			}
			
			pitch = Calc.approach(pitch, 1.0 + (.5 * (bar / 100.0)), 2);
			
			Audio.get("sChopBar").setPitch(pitch);
			
			if (!IO.checkKey(Global.INTERACT)){
				if (bar > 30)
					earned += (int)(bar / 10.0);
				if (bar > 70)
					earned -= 2 * (times - 4);
				if (bar == 100){
					if (earned < 0)
						earned = 25;
					else
						earned += 25;
				}
				earned = Math.max(0, earned);
				
				Audio.get("sChopBar").setGain(1);
				Audio.fade("sChopBar", 0, .02);
				
				phase = 2;
				phaseTimer = 60;
			}
		}
		
		if (phase == 2){
			phaseTimer -= 1;
			if (phaseTimer <= 0){
				Audio.get("sChopBar").stop();
				Sound.playPitched("sChopBells",.2);
				//TODO sound
				phase = 3;
			}
		}
		
		//TODO eat shit
		if (phase == 3){
			addX = Calc.approach(addX, -160, 20);
			addX -= .5;
			treeAlpha = Calc.approach(treeAlpha, 1, 20);
			treeAlpha += .005;
			if (treeAlpha > 1)
				treeAlpha = 1;
			if (addX <= -160 && treeAlpha >= 1){
				addX = -160;
				phase = 4;
				
				Audio.get("sChopBar").setLooping(true);
				Audio.get("sChopBar").setGain(0);
				Audio.fade("sChopBar", 1, .1);
				Audio.get("sChopBar").setPitch(1.5);
				Sound.play("sChopBar");
				
				Sound.playPitched("sChopBells", .2);
				phaseTimer = 60;
			}
		}
		
		if (phase > 3){
			arrowScale = Calc.approach(arrowScale, 1.0, 4);
			arrowScale -= .1;
			if (arrowScale <= 1.0)
				arrowScale = 1.4;
		}
		
		if (phase == 4){
			phaseTimer -= 1;
			Audio.get("sChopBar").setPitch(1.5 + ((60 - phaseTimer) / 60.0));
			if (phaseTimer <= 0){
				phase = 5;
				resetArrow();
				Audio.fade("sChopBar", 0, .05);
				//TODO show arrow!!!!
			}
		}
		
		axeAngle = Math.min(0, Calc.approach(axeAngle, 0, 5) + 8);
		
		if (phase == 5){
			if (axeAngle > -20){
				if ((IO.checkFrameKey(Global.RIGHT) && arrow == 0)||(IO.checkFrameKey(Global.UP) && arrow == 1)||(IO.checkFrameKey(Global.LEFT) && arrow == 2)||(IO.checkFrameKey(Global.DOWN) && arrow == 3)){
					Sound.playPitched("sChopTreeHit");
					Audio.get("sChopTreeTone").setPitch(1.0 + ((100 - treeHealth) / 100.0));
					Sound.play("sChopTreeTone");
					
					for (int i=0; i<4; i++){
						new ChopDebris(x + addX + 320 + Calc.rangedRandom(150), y + addY - 160 - Calc.random(150), 1);
					}
					for (int i=0; i<3; i++){
						new ChopDebris(x + addX + 320 + Calc.rangedRandom(120), y + addY - Calc.random(160), 0);
					}
					
					arrowTimer -= 1.75;
					arrowAttention = false;
					axeAngle = -70;
					arrowScale = 1.4;
					treeHealth -= 1.2;
					
					if (treeHealth <= nextEarn){
						earned += 1;
						nextEarn -= 7;
					}
				}
				
				if (treeHealth <= 0){
					phase = 6;
					treeHealth = 0;
					Audio.fade("musAxe", 0, .004);
					//treeChopped = true;
				}
			}
			
			arrowTimer -= 1;
			if (arrowTimer <= 0)
				resetArrow();
		}
		
		if (phase == 6){
			explodeTime -= 1;
			if (explodeTime <= 0){
				explodeTime = explodeMax;
				if (explodeMax > 4)
					explodeMax -= 1;
				explodes += 1;
				
				for (int i=0; i<2; i++){
					new ChopDebris(x + addX + 320 + Calc.rangedRandom(150), y + addY - 160 - Calc.random(150), 1);
					new ChopDebris(x + addX + 320 + Calc.rangedRandom(120), y + addY - Calc.random(160), 0);
				}
				
				for (int i=0; i<2; i++)
					new Explosion(320 + Calc.rangedRandom(32), 288 - Calc.random(288));
				
				Sound.playPitched("sChopExplode", .2);
				for (int i=0; i<2; i++)
					new ChopExplosion(x + addX + 320 + Calc.rangedRandom(132), y + addY -  26 - Calc.random(274));
				
				if (explodes >= 30){
					mom.mother.kill();
					
					Sound.jarBreakPlay();
					Sound.jarBreakPlay();
					
					Sound.play("sChopGiggle");
					
					//TODO finalexplodesound??
					
					phase = 7;
					phaseTimer = 120;
					
					for (int i=0; i<3; i++)
						new Explosion(320 + Calc.rangedRandom(32), 80 - Calc.random(80));
					
					for (int i=0; i<6; i++)
						new Explosion(320 + Calc.rangedRandom(32), 288 - Calc.random(288));
					
					for (int i=0; i<5; i++){
						new ChopDebris(x + addX + 320 + Calc.rangedRandom(150), y + addY - 160 - Calc.random(150), 1);
						new ChopDebris(x + addX + 320 + Calc.rangedRandom(120), y + addY - Calc.random(160), 0);
					}
					
					for (int i=0; i<14; i++)
						new ChopExplosion(x + addX + 320 + Calc.rangedRandom(158), y + addY - Calc.random(312));
					
					treeChopped = true;
					phase = 7;
				}
			}
		}
		
		if (phase == 7){
			phaseTimer -= 1;
			axeAlpha = Math.max(0, Calc.approach(axeAlpha, 0, 30) - .005);
			addX = Math.max(-320, Calc.approach(addX, -320, 35) - .5);
			
			if (phaseTimer <= 0){
				Sound.playPitched("sTreeWhine", .2);
				
				ArrayList<ChopClouds> i = Scene.getEntityList(ChopClouds.class);
				for (int j=0; j<i.size(); j++)
					i.get(j).destroy();
				
				//ArrayList<ChopGemCounter> f = Scene.getEntityList(ChopGemCounter.class);
				//f.get(0).destroy();
				
				Global.addTweet("am i a #murderer?");
				
				Global.eventItemUse(0);
				
				Player.control = true;
				Music.fadeMusic(Global.roomMusic, true);
				mom.destroy();
				destroy();
			}
		}
		
		x += Calc.rangedRandom(.2);
		y += Calc.rangedRandom(.2);
		timer -= 1;
		if (timer <= 0)
			reset();
		
		super.step();
	}
	
	public void render(){
		//Sprite.get("sTreeMiniGem").render(imageSingle, Sprite.CENTERED, 640 - 64 + Calc.rangedRandom(.5), 480 - 64 + Calc.rangedRandom(.5), .4, .4, 0 , 1, 1, 1, 1);
		
		if (phase == 1 || phase == 2){
			Graphics.setAlpha(1.0);
			Graphics.setColor("FF283A");
			Shape.drawRectangle(0, 460, 640, 480);
			Graphics.setColor("21A6FF");
			if (bar == 100)
				Graphics.setColor("0FCD61");
			Shape.drawRectangle(0, 460, (640 * (bar / 100.0)) + Calc.rangedRandom(1), 480);
		}
		
		//Sprite.get("sTreeMiniGirl").render(imageSingle, Sprite.SOUTH, x + Calc.rangedRandom(4) + addX, y + Calc.rangedRandom(4), .35, .35, Calc.rangedRandom(4), plalpha, 1, 1, 1);
		String s = "sTreeMiniTree";
		if (treeChopped)
			s = "sTreeMiniChopped";
		Sprite.get(s).render(imageSingle, Sprite.SOUTH, x + Calc.rangedRandom(.5) + 320 + addX, y + Calc.rangedRandom(.5) + addY, 1, 1, 0, treeAlpha, 1, 1, 1);
		if (treeHealth > 0){
			final double w = 64, h = 8, xx = x + addX + 320, yy = y + addY - 250;
			Graphics.setAlpha(treeAlpha);
			Graphics.setColor("0897FF");
			Shape.drawRectangle(xx - (w/2.0) - 3, yy - (h/2.0) - 3, xx + (w/2.0) + 3, yy + (h/2.0) + 3);
			Graphics.setColor("FF3A30");
			Shape.drawRectangle(xx - (w/2.0), yy - (h/2.0), xx + (w/2.0), yy + (h/2.0));
			Graphics.setColor("94FF00");
			Shape.drawRectangle(xx - (w/2.0), yy - (h/2.0), xx - (w/2.0) + (w * (treeHealth / 100.0)), yy + (h/2.0));
			Graphics.setAlpha(1);
		}
		
		Sprite.get("sTreeMiniAxe").render(imageSingle, Sprite.SOUTH, x + Calc.rangedRandom(.5) + (addX * (.1 + (.8 * (bar / 100.0)))) , y + Calc.rangedRandom(.5) + addY, .2 + (.8 * (bar / 100.0)), .2 + (.8 * (bar / 100.0)), axeAngle, axeAlpha, 1, 1, 1);
		
		if (phase == 5){
			Sprite.get("sTreeArrow").render(arrow, Sprite.CENTERED, 320 + Calc.random(1.5), 240 + Calc.random(1.5), arrowScale, arrowScale, 0, 1, 1, 1, 1);
			if (arrowAttention)
				Sprite.get("sTreeArrowAttention").render(Calc.random(2), Sprite.CENTERED, 320 + Calc.random(1.5), 240 + Calc.random(1.5), arrowScale, arrowScale, 0, 1, 1, 1, 1);
		}
		
		if (intro <= 0){
			Text.setFont(Global.FONT);
			Text.orientation = Text.NORTH;
			Text.randomize(.9);
			if (!textVis)
				Text.randomize(7);
			Graphics.setColor("0C91FF");
			text(3,0);
			text(-3,0);
			text(0,3);
			text(0,-3);
			text(2,-2);
			text(2,2);
			text(-2,2);
			text(-2,-2);
			Graphics.setColor("94FF30");
			if (!textVis)
				Graphics.setColor(Global.randomColor());
			text(0,0);
			Text.randomize(0);
		}
	}
	
	public void resetArrow(){
		if (phase == 5)
			Sound.playPitched("sChopArrowChange", .2);
		
		int oldArrow = arrow;
		while (arrow == oldArrow){
			arrow = (int)myGen.random(4);
		}
		
		arrowAttention = true;
		arrowScale = 1.4;
		arrowTimer = 30 + Math.max(0, (treeHealth / 3.0) * (1.0 + Calc.rangedRandom(.25)));
	}
	
	public void text(double x, double y){
		Text.drawTextExt(320 + 12 + x, 12 + y + textY, getTip(phase).toUpperCase(), .75, .75, 0);
	}
	
	public String getTip(int id){
		String[] tips = new String[6];
		tips[0] = "hold interact key!!!";
		tips[1] = "release interact key%when bar is filled!!!";
		tips[2] = "";
		tips[3] = "";
		tips[4] = "are you ready...";
		tips[5] = "tap the direction%displayed on screen!!!";
		if (id < tips.length)
			return tips[id];
		else
			return "";
	}
	
	public void reset(){
		x = xstart;
		y = ystart;
		timer = Calc.random(20);
	}

}
