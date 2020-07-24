package world.event;

import java.util.ArrayList;

import javax.media.opengl.GL2;

import audio.Audio;
import world.control.EV;
import world.control.Global;
import world.control.Music;
import world.control.Sound;
import world.control.SoundLoader;
import world.player.Player;
import graphics.Graphics;
import graphics.Shape;
import graphics.Sprite;
import graphics.Text;
import main.Calc;
import main.Entity;
import main.Scene;

public class CellPhone extends Entity{
	public int phase; //wait, new tweet moves down, typing the tweet
	public int lImg, rImg;
	public int timer;
	
	public boolean showNewTweet;
	
	public SoundLoader mySounds;
	
	public double screenFade = 1;
	
	public String typed;
	
	public double addY = 0;
	
	public double img = Calc.random(6), angler = (Double)Calc.choose(0.0,90.0,180.0,270.0), angTimer = Calc.random(60);

	public CellPhone(double x, double y) {
		super(x, y);
		
		setDepth(Integer.MIN_VALUE+1); //TODO change
		
		phase = 0;
		
		mySounds = new SoundLoader("sTweetLetter","sTweetNew","sTweetSend","sTweetA","sTweetB","sTweetC","sTweetD","sTweetE","sTweetF","sTweetG","sTweetH","sTweetI","sTweetJ","sTweetK","sTweetL","sTweetM","sTweetN","sTweetO","sTweetP","sTweetQ","sTweetR","sTweetS","sTweetT","sTweetU","sTweetV","sTweetW","sTweetX","sTweetY","sTweetZ");
		
		timer = 40;
		typed = "";
		
		showNewTweet = false;
		
		
		
		prepareThumbs();
	}
	
	public void step(){
		addY = Calc.approach(addY, 0, 8);
		
		img += Calc.random(.15);
		if (img >= 6)
			img -= 6;
		angTimer -= 1;
		if (angTimer <= 0){
			angTimer = Calc.random(60);
			angler = (Double)Calc.choose(0.0,90.0,180.0,270.0);
		}
		
		timer -= 1;
		if (phase == 0){
			screenFade -= .125;
			if (timer <= 0){
				Sound.play("sTweetNew");
				moveThumb();
				phase = 1;
				timer = 40;
			}
		}
		else if (phase == 1){
			showNewTweet = true;
			if (timer <= 0){
				phase = 2;
				timer = 3;
			}   
		}
		else if (phase == 2){
			if (timer <= 0){
				Sound.playPitched("sTweetLetter",.1);
				moveThumb();
				char s = Global.cutsceneTweet.charAt(typed.length());
				typed += s;
				if (Audio.soundExists("sTweet"+s))
					Sound.playPitched("sTweet"+s,.1);

				if (!typed.equals(Global.cutsceneTweet)){
					timer = 3;
				}
				else{
					timer = 90;
					phase = 3;
				}
			}
		}
		else if (phase == 3){
			if (timer <= 0){
				moveThumb();
				showNewTweet = false;
				Global.addTweet(Global.cutsceneTweet);
				addY = -48;
				Sound.play("sTweetSend");
				phase = 4;
				timer = 45;
			}
		}
		else if (phase == 4){
			if (timer <= 0){
				phase = 5;
				timer = 10;
				moveThumb();
				Sound.play("sTweetSend");
			}
		}
		else if (phase == 5){
			screenFade += .24;
			if (timer <= 0){
				phase = 6;
				timer = 10;
			}
		}
		else if (phase == 6){
			if (timer <= 0){
				Player.control = true;
				Player.me.cutMove = false;
				
				Audio.fade("sChestOpenTune", 0, .0075125);
				ArrayList<ChestScene> list = Scene.getEntityList(ChestScene.class);
				for (int i=0; i<list.size(); i++)
					list.get(i).destroy();
				destroy();
				
				if (Global.roomX == 17){
					Global.gotStench = true;
					Player.me.stenchTime = Calc.random(4);
					if (Global.heartGot[17])
						Global.clearDream();
				}
				else if (Global.roomX == 3 && Global.roomY == 3){
					//Sound.playPitched("sGetBomb",.02);
					//Player.me.hammerShow = 15;
					list.get(0).mother.moveUp = 64;
					JellySalesmanControl.me.cutPhase = 0;
					JellySalesmanControl.me.cutTimer = 128;
					JellySalesmanControl.me.count = -1;
					//Hud.showWeapon();
				}
				else if (Global.room(26, 0)){
					Global.event[EV.GOT_SNORKEL] = 1;
				}
				
				Music.fadeMusic(Global.roomMusic, true);
				
				ArrayList<AmbientLoop> bist = Scene.getEntityList(AmbientLoop.class);
				for (int i=0; i<bist.size(); i++)
					bist.get(i).stepActive = true;
			}
			
			Global.refreshIconMap();
		}
		
		screenFade = Math.max(0, Math.min(1, screenFade));
	}
	
	public void render(){
		Sprite.get("sCellPhone").render(2, Sprite.NORTHWEST, Calc.rangedRandom(1), Calc.rangedRandom(1), 1, 1, 0, 1, 1, 1, 1);
		
		//draw actual tweet menu stuff
		double yy = 159 + addY;
		int id = Global.tweetText.length - 1;
		for (int i=0; i<7; i++){
			if (Global.tweetText.length >= i+1){
				Graphics.setColor(1,1,1);
				Graphics.setAlpha(.8);
				
				Shape.drawRectangle(138 + Calc.rangedRandom(.5), yy + Calc.rangedRandom(.5), 435 + Calc.rangedRandom(.5), yy + 48 + Calc.rangedRandom(.5));
				double k = Calc.rangedRandom(.5), l = Calc.rangedRandom(.5);
				Sprite.get("sStand").render(1, Sprite.CENTERED, 158 + k, yy + 24 + l, 1, 1, 0, 1, 1, 1, 1);
				if (Global.heroMode)
					Sprite.get("sBow").render(0, Sprite.CENTERED, 158 + k, yy + 24 + l, 1, 1, 0, 1, 1, 1, 1);
				
				Text.setFont(Global.FONT);
				Text.randomize(.2);
				Graphics.setColor("167BFF");
				
				/*Text.orientation = Text.SOUTHEAST;
				Graphics.setAlpha(.5);
				
				Text.drawTextExt(433 + Calc.rangedRandom(.5), yy + 40 + Calc.rangedRandom(.5), Global.tweetTimestamp[id], .2, .2, 0);
				
				Graphics.setAlpha(1);*/
				
				String s = Text.widthLimit(Global.tweetText[id],30);
				Text.orientation = Text.NORTHWEST;
				drawTextExt(138 + 44 + Calc.rangedRandom(.2), yy + 4 + Calc.rangedRandom(.2), s, .25, .25, 0);
				
				Graphics.setColor("167BFF");
				
				yy += 56;
				id -= 1;
			}
		}
		
		Graphics.setColor("167BFF");
		Graphics.setAlpha(screenFade);
		Shape.drawRectangle(128,146,128+326,146+295);
		Graphics.setAlpha(1);
		
		if (showNewTweet){
			Sprite.get("sCellPhoneNewTweet").render(0, Sprite.NORTHWEST, Calc.rangedRandom(1), Calc.rangedRandom(1), 1, 1, 0, 1, 1, 1, 1);
			Text.setFont(Global.FONT);
			Text.randomize(.5);
			Graphics.setColor("167BFF");
			Graphics.setAlpha(1);
			Text.orientation = Text.NORTH;
			Text.drawTextExt(281+Calc.rangedRandom(1), 164+Calc.rangedRandom(1), "NEW TWEET", .7, .7, Calc.rangedRandom(.01));
			Text.orientation = Text.NORTHWEST;
			//System.out.println(Text.widthLimit(typed, 24) + ", "+(Text.widthLimit(typed, 24).split("%").length));
			Text.setSpacing(1, 1.2);
			if (typed.length() > 0){
				drawTextExt(141+Calc.rangedRandom(1), 200+Calc.rangedRandom(1), Text.widthLimit(typed, 24), .36, .36, Calc.rangedRandom(.01));
			}
			
			Graphics.setColor("167BFF");
			
			//characters left
			Text.drawTextExt(157+Calc.rangedRandom(1), 394+Calc.rangedRandom(1), ""+(140 - typed.length()), .36, .36, Calc.rangedRandom(.01));
			
			Text.setSpacing(1, 1);
			Text.drawTextExt(293+Calc.rangedRandom(1), 400+Calc.rangedRandom(1), "SNUFFLUVR", .36, .36, Calc.rangedRandom(.01));
			Text.randomize(0);
			
			//Sprite.get("sStand").render(img,Sprite.CENTERED,163+Calc.rangedRandom(1), 417+Calc.rangedRandom(1),1,1,angler,1,1,1,1);
		}
		
		Text.randomize(0);
		
		Sprite.get("sCellPhone").render(1, Sprite.NORTHWEST, Calc.rangedRandom(1), Calc.rangedRandom(1), 1, 1, 0, 1, 1, 1, 1);
		Sprite.get("sCellPhone").render(0, Sprite.NORTHWEST, Calc.rangedRandom(1), Calc.rangedRandom(1), 1, 1, 0, 1, 1, 1, 1);
		Sprite.get("sCellThumbs").render(lImg, Sprite.NORTHWEST, Calc.rangedRandom(1), Calc.rangedRandom(1), 1, 1, 0, 1, 1, 1, 1);
		Sprite.get("sCellThumbs").render(rImg + 3, Sprite.NORTHWEST, Calc.rangedRandom(1), Calc.rangedRandom(1), 1, 1, 0, 1, 1, 1, 1);
	}
	
	public void drawTextExt(double x, double y, String text, double xscale, double yscale, double angle){
		int currentLine=0;
		double ax=0, ay=0;
		//text = text.toLowerCase();

		String hash = "FF2151";
		
		String[] result=text.split("%");
		int[] widths=new int[result.length];
		for (int i=0; i<result.length; i++){
			widths[i]=result[i].length();
		}
		
		ax=Text.reallignX(Text.orientation,text,widths[currentLine]);
		ay=Text.reallignY(Text.orientation,text);
		
		GL2 gl = Scene.gl;
		gl.glPushMatrix();
		gl.glTranslated(x + Scene.viewX, y + Scene.viewY, 0);
		gl.glRotated(-angle,0,0,1);
		gl.glScaled(xscale, yscale, 0);
		
		boolean hashtag = false, last = false;
		
		for (int i=0; i<text.length(); i++){
			int s=46;
			s = Text.charToImg(text,i);
			
			last = false;
			
			if (!hashtag && s == 40){
				hashtag = true;
				last = true;
			}
			if (hashtag && (s < 10 || s > 35) && !last)
				hashtag = false;
			
			if (!hashtag)
				Graphics.setColor("167BFF");
			else
				Graphics.setColor(hash);
			
			if (s==-1){
				currentLine+=1;
				if (widths.length > currentLine)
					ax=Text.reallignX(Text.orientation,text,widths[currentLine]);
				ay+=Text.sprite.imageHeight*Text.vSpacing;
			}
			else{
				Text.sprite.renderWithoutView(s, Text.orientation, ax-Text.letterRandom+(Calc.random(Text.letterRandom*2)), ay-Text.letterRandom+(Calc.random(Text.letterRandom*2)), 1, 1, 0, Graphics.alpha, Graphics.color[0], Graphics.color[1], Graphics.color[2]);
				ax+=Text.sprite.imageWidth*Text.hSpacing;
			}
		}	
		gl.glPopMatrix();
	}
	
	public void prepareThumbs(){
		lImg = (int)Calc.random(3);
		rImg = (int)Calc.random(3);
	}
	
	public void moveThumb(){
		int a;
		if (Calc.random(1) < .5){
			a = lImg;
			while (a == lImg)
				lImg = (int)Calc.random(3);
		}
		else{
			a = rImg;
			while (a == rImg)
				rImg = (int)Calc.random(3);
		}
	}

}
