package world.event;

import java.util.ArrayList;

import audio.Audio;

import graphics.Sprite;
import world.Fg;
import world.control.EV;
import world.control.Global;
import world.control.Sound;
import world.player.Player;
import main.Calc;
import main.Entity;
import main.Scene;

public class DishBlock extends Entity {
	public double addY, toY, toXsc, toYsc;
	public boolean bounce;
	
	public int id, timer;
	
	//public Digit pal;

	public DishBlock(double x, double y, int i) {
		super(x, y);
		setCollisionGroup(Global.SOLIDBLOCK, Global.DEACTIVATEME);
		setDepth(-1);
		
		addY = 0;
		toY = 0;
		toXsc = 1;
		toYsc = 1;
		
		orientation = Sprite.NORTHWEST;
		mask = Global.sBLOCK.mask;
		
		bounce = false;
		
		id = i;
		timer = -1;
		
		if (Global.event[EV.DISH_PUZZLE] == 1){
			visible = false;
			destroy();
		}
		
		/*ArrayList<Digit> list = Scene.getEntityList(Digit.class);
		for (int j=0; j<list.size(); j++){
			if (list.get(j).id == id)
				pal = list.get(j);
		}*/
	}
	
	public void brekken(){
		Global.pieceParticles(x - 16, y - 16, "sDishBlock", 0, 0, 16, 16, Integer.MIN_VALUE + 19);
		
		Global.explosionEffect(x, y, 1);
		Sound.playPitched("sBombBlast",.05);
		Sound.playPitched("sDishSplode",.08);
		
		destroy();
	}
	
	public void step(){
		if (timer > - 1)
			timer -= 1;
		
		if(bounce){
			toY += 1.6;
			toXsc -= .0365;
			toYsc += .03675;
			if (toY > 0)
				toY = 0;
			if (toXsc < 1)
				toXsc = 1;
			if (toYsc > 1)
				toYsc = 1;
			if (toY == 0 && toXsc == 1 && toYsc == 1)
				bounce = false;
		}
		
		double preXsc = xscale, preYsc = yscale;
		xscale = 1;
		yscale = 1;
		
		if (Player.me.vspeed <= 0 && Player.me.y <= y + 48 && timer == -1 && Scene.collision(this, x, y + 1, Global.PLAYER)){
			if (Audio.soundExists("sDishBlock"))
				Sound.playPitched("sDishBlock",.22);
			bounce = true;
			timer = 9;
			toY = -22;
			toXsc = 1.6;
			toYsc = .62;
			
			if (id < 3){
				/*int a = pal.digit + 1;
				if (a > 9)
					a = 0;
				pal.changeDigit(a);*/
				ArrayList<Digit> list = Scene.getEntityList(Digit.class);
				for (int j=0; j<list.size(); j++){
					Digit pal = list.get(j);
					if (id <= pal.id){
						int a = pal.digit + 1;
						if (pal.id == 1)
							a = pal.digit + 3;
						
						if (a > 9)
							a -= 10;
						pal.changeDigit(a);
					}
				}
			}
			else{
				int[] numbers = new int[3];
				numbers[0] = 9; numbers[1] = 9; numbers[2] = 9;
				
				ArrayList<Digit> list = Scene.getEntityList(Digit.class);
				for (int j=0; j<list.size(); j++){
					Digit pal = list.get(j);
					numbers[pal.id] = pal.digit;
				}
				
				String str = numbers[0]+""+numbers[1]+""+numbers[2];
				if (str.equals("115")){ //great job
					//TODO great job
					Global.addTweet("puzzles are fun!!!!!!");
					Player.control = false;
					Global.event[EV.DISH_PUZZLE] = 1;
					
					Sound.play("sDishWin");
					
					new DishPuzzleComplete();
				}
				else if (str.equals("420") && Global.event[EV.WEED_JOKE] == 0){ //smoke wed everda
					Global.addTweet("smok eweed ever yday xD");
					
					Global.event[EV.WEED_JOKE] = 1;
					
					Global.dropExactLoot(320, 224, 70); //epic sex joke
					
					Fg.me.shakeTimer = 38;
					
					Sound.play("sDishWin");
					Sound.play("sTakstein");
					new EpicWeedJoke(0,0);
					
					for (int j=0; j<list.size(); j++){
						Digit pal = list.get(j);
						pal.changeDigit(9);
					}
				}
				else{ //fuck you dingus
					Fg.me.shakeTimer = 16;
					
					Sound.playPitched("sDishError",.08);
					
					for (int j=0; j<list.size(); j++){
						Digit pal = list.get(j);
						pal.changeDigit(9);
					}
				}
			}
		}
		
		xscale = preXsc;
		yscale = preYsc;
		
		addY = Calc.approach(addY, toY, 2);
		xscale = Calc.approach(xscale, toXsc, 2.25);
		yscale = Calc.approach(yscale, toYsc, 2.5);
	}
	
	public void render(){
		Sprite.get("sDishBlock").render(0, Sprite.CENTERED, x + 16 + Calc.rangedRandom(.7), y + addY + 16 + Calc.rangedRandom(.7), xscale, yscale, 0, 1, 1, 1, 1);
		if (id >= 3)
			Sprite.get("sDishBlock").render(1, Sprite.CENTERED, x + 16 + Calc.rangedRandom(.7), y + addY + 16 + Calc.rangedRandom(.7), xscale, yscale, 0, 1, 1, 1, 1);
	}

}
