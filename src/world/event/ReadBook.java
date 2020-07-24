package world.event;

import graphics.Graphics;
import graphics.Sprite;
import graphics.Text;
import world.control.Global;
import world.control.Hud;
import world.control.IO;
import world.control.Music;
import world.control.Sound;
import world.control.SpriteLoader;
import world.player.Player;
import main.Calc;
import main.Entity;

public class ReadBook extends Entity{
	public SpriteLoader myLoader;
	public double[] scales;
	
	public double cornerAlpha, cornerX, cornerY;
	
	public int showing;
	public double pageAlpha;
	
	public int rArrow, lArrow;
	public int arrowTimer;
	public double arrowPosition;
	
	public int killTimer;
	public boolean can = false;

	public ReadBook(SpriteLoader l) {
		super(0, 0);
		
		myLoader = l;
		
		showing = 0;
		pageAlpha = 0;
		
		killTimer = -1;
		
		cornerX = 64;
		cornerY = 64;
		cornerAlpha = 0;
		
		arrowPosition = 0;
		rArrow = 0;
		lArrow = 0;
		arrowTimer = -1;
		
		scales = new double[myLoader.mySpriteNames.length];
		for (int i=0; i<myLoader.mySpriteNames.length; i++){
			Sprite s = Sprite.get(myLoader.mySpriteNames[i]);
			if ((480.0 / s.imageWidth) < (430.0 / s.imageHeight))
				scales[i] = 480.0 / s.imageWidth;
			else
				scales[i] = 430.0 / s.imageHeight;
		}
		
		Hud.hideAll();
		
		setDepth(Integer.MIN_VALUE + 2);
		setCollisionGroup(Global.CONTROLLER);
		
		begin();
	}
	
	public void changePage(int add){
		showing += add;
		if (showing < 0 || showing >= myLoader.mySpriteNames.length)
			showing = Math.max(0, Math.min(showing, myLoader.mySpriteNames.length - 1));
		else{
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
			
			arrowPosition = 32;
			pageAlpha = 0;
		}
	}
	
	public void step(){
		Hud.hideAll();
		
		if (arrowTimer > -1){
			arrowTimer -= 1;
			if (arrowTimer == 0){
				rArrow = 0;
				lArrow = 0;
			}
		}
		
		cornerAlpha = Calc.approach(cornerAlpha, 1, 8);
		pageAlpha = Calc.approach(pageAlpha, 1, 4);
		
		cornerX = Calc.approach(cornerX, ((Sprite.get(myLoader.mySpriteNames[showing]).imageWidth / 2.0) * scales[showing]) + 18, 3);
		cornerY = Calc.approach(cornerY, ((Sprite.get(myLoader.mySpriteNames[showing]).imageHeight / 2.0) * scales[showing]) + 18, 3);
		
		arrowPosition = Calc.approach(arrowPosition, 0, 3);
		
		if (arrowTimer <= 4){
			int add = 0;
			if (IO.checkFrameKey(Global.LEFT))
				add -= 1;
			if (IO.checkFrameKey(Global.RIGHT))
				add += 1;
			if (add != 0)
				changePage(add);
			else if (IO.checkFrameKey(Global.DOWN) && can)
				end();
		}
		
		if (killTimer > -1)
			killTimer -= 1;
		if (killTimer == 0){
			Hud.showAll();
			Global.activateGameplayObjects();
			Player.control = true;
			Music.pauseVolume(false);
			myLoader.destroy();
			
			Global.addTweet((String)Calc.choose("","","","what a ")+(String)Calc.choose("fantastic ", "great ", "splendid ", "good ass ", "nice ", "pleasant ")+(String)Calc.choose("read","book")+(String)Calc.choose("","","","!","!","!!","."));
			
			destroy();
		}
		else
			Global.framesPlayed++;
		
		can = true;
	}
	
	public void render(){
		Sprite.get("sPauseOverlay").render(0, Sprite.CENTERED, 319 + Calc.rangedRandom(.75), 239 + Calc.rangedRandom(.75), 1.02125 + Calc.random(.001), 1.021 + Calc.random(.001), Calc.rangedRandom(.2), cornerAlpha*cornerAlpha*cornerAlpha*.8, .6, .6, .6);
		
		Sprite.get("sReadBookCorner").render(0, Sprite.NORTHWEST, 320 + Calc.rangedRandom(.4) - cornerX, 240 + Calc.rangedRandom(.4) - cornerY, 1, 1, 0, cornerAlpha, 1, 1, 1);
		Sprite.get("sReadBookCorner").render(0, Sprite.NORTHWEST, 320 + Calc.rangedRandom(.4) - cornerX, 240 + Calc.rangedRandom(.4) + cornerY, 1, 1, 90, cornerAlpha, 1, 1, 1);
		Sprite.get("sReadBookCorner").render(0, Sprite.NORTHWEST, 320 + Calc.rangedRandom(.4) + cornerX, 240 + Calc.rangedRandom(.4) - cornerY, 1, 1, 270, cornerAlpha, 1, 1, 1);
		Sprite.get("sReadBookCorner").render(0, Sprite.NORTHWEST, 320 + Calc.rangedRandom(.4) + cornerX, 240 + Calc.rangedRandom(.4) + cornerY, 1, 1, 180, cornerAlpha, 1, 1, 1);
		
		Sprite.get(myLoader.mySpriteNames[showing]).render(0, Sprite.CENTERED, 320 + Calc.rangedRandom(.4), 240 + Calc.rangedRandom(.4), scales[showing], scales[showing], 0, pageAlpha, 1, 1, 1);
		
		if (showing < myLoader.mySpriteNames.length - 1 || (arrowTimer > 0 && showing == myLoader.mySpriteNames.length - 1))
			Sprite.get("sReadBookArrow").render(rArrow, Sprite.CENTERED, 576 + Calc.rangedRandom(.4) + (arrowPosition * rArrow), 240 + Calc.rangedRandom(.4), .6 + (rArrow * .1), .6 + (rArrow * .1), 0, cornerAlpha, 1, 1, 1);
		if (showing > 0 || (showing == 0 && arrowTimer > 0))
			Sprite.get("sReadBookArrow").render(lArrow, Sprite.CENTERED, 64 + Calc.rangedRandom(.4) - (arrowPosition * lArrow), 240 + Calc.rangedRandom(.4), -.6 - (lArrow * .1), .6 + (lArrow * .1), 0, cornerAlpha, 1, 1, 1);
		
		Graphics.setAlpha(cornerAlpha*cornerAlpha*cornerAlpha);
		
		String s = "PAGE "+(showing + 1)+" OF "+myLoader.mySpriteNames.length;
		
		Text.setFont(Global.FONT);
		Text.idiot = false;
		
		Text.orientation = Text.SOUTH;
		Graphics.setColor("007cff");
		Text.drawTextExt(328 + Calc.rangedRandom(.4), 478 + Calc.rangedRandom(.4) + 2, s, .5, .5, 0);
		Text.drawTextExt(328 + Calc.rangedRandom(.4), 478 + Calc.rangedRandom(.4) - 2, s, .5, .5, 0);
		Text.drawTextExt(328 + Calc.rangedRandom(.4) + 2, 478 + Calc.rangedRandom(.4), s, .5, .5, 0);
		Text.drawTextExt(328 + Calc.rangedRandom(.4) - 2, 478 + Calc.rangedRandom(.4), s, .5, .5, 0);
		Graphics.setColor("ecff09");
		Text.drawTextExt(328 + Calc.rangedRandom(.4), 478 + Calc.rangedRandom(.4), s, .5, .5, 0);
		
		Text.orientation = Text.NORTHWEST;
		Graphics.setColor("007cff");
		Text.drawTextExt(4 + Calc.rangedRandom(.4), 4 + Calc.rangedRandom(.4) + 2, "PRESS DOWN TO RETURN", .5, .5, 0);
		Text.drawTextExt(4 + Calc.rangedRandom(.4), 4 + Calc.rangedRandom(.4) - 2, "PRESS DOWN TO RETURN", .5, .5, 0);
		Text.drawTextExt(4 + Calc.rangedRandom(.4) + 2, 4 + Calc.rangedRandom(.4), "PRESS DOWN TO RETURN", .5, .5, 0);
		Text.drawTextExt(4 + Calc.rangedRandom(.4) - 2, 4 + Calc.rangedRandom(.4), "PRESS DOWN TO RETURN", .5, .5, 0);
		Graphics.setColor("ecff09");
		Text.drawTextExt(4 + Calc.rangedRandom(.4), 4 + Calc.rangedRandom(.4), "PRESS DOWN TO RETURN", .5, .5, 0);
		
		Graphics.setAlpha(1);
		Text.idiot = true;
	}
	
	public void begin(){
		Player.control = false;
		Global.deactivateGameplayObjects();
		Music.pauseVolume(true);
		Sound.playPitched("sReadOpen");
	}
	
	public void end(){
		if (visible){
			Sound.playPitched("sReadClose");
			visible = false;
			killTimer = 3;
		}
	}

}
