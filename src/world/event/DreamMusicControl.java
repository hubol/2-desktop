package world.event;

import graphics.BlendMode;
import graphics.Graphics;
import graphics.Shape;
import graphics.Sprite;
import audio.Audio;
import world.Fg;
import world.control.Global;
import world.control.Sound;
import world.control.SoundLoader;
import world.gameplay.Heart;
import world.player.Player;
import main.Calc;
import main.Entity;

public class DreamMusicControl extends Entity{
	public boolean[][] activated;
	public boolean[][] desired;
	
	public long startTime;
	
	public boolean playing, shouldPlay;
	public int currentMeasure, editSelect, timer;
	public SoundLoader myLoader;
	
	public static DreamMusicControl me;
	
	public double blink;
	public double pitch;
	public double seed;
	
	public final double screenX = 163, screenY = 35, screenW = 314, screenH = 90;

	public DreamMusicControl(double x, double y) {
		super(x, y);
		activated = new boolean[4][4];
		desired = new boolean[4][4];
		
		seed = Global.framesPlayed;
		
		editSelect = 0;
		timer = -1;
		
		pitch = 1.0;
		
		setDepth(-3);
		
		me = this;
		
		blink = 1.0;
		
		currentMeasure = 0;
		playing = false;
		shouldPlay = false;
		myLoader = new SoundLoader(false, "musGimmick0","musGimmick1","musGimmick2","musGimmick3","sSkullQuake","sGimmickFadein");
		
		//0 = drums, 1 = low calimba, 2 = nebulophone, 3 = high calimba
		//drums
		desired[0][0] = false; desired[1][0] = true; desired[2][0] = true; desired[3][0] = true;
		//low calimba
		desired[0][1] = true; desired[1][1] = true; desired[2][1] = false; desired[3][1] = true;
		//nebulophone
		desired[0][2] = false; desired[1][2] = false; desired[2][2] = true; desired[3][2] = true;
		//high calimba
		desired[0][3] = false; desired[1][3] = false; desired[2][3] = false; desired[3][3] = true;
		
		//if we've never completed the "puzzle"
		if (Global.event[2] == 0){
			for (int i=0; i < 4; i++){
				for (int j = 0; j < 4; j++)
					activated[i][j] = choose();
			}
		}
		else{ //and if we have
			for (int i=0; i < 4; i++){
				for (int j = 0; j < 4; j++)
					activated[i][j] = desired[i][j];
			}
			
			new Heart(448, 384, 18);
			shouldPlay = true;
		}
	}
	
	public boolean choose(){
		seed += 6;
		seed *= 1.0169;
		seed -= 4;
		return ((seed % 4.9)/4.9) < .5;
	}
	
	public void step(){
		blink = Calc.approach(blink, 0, 34);
		blink -= .04;
		if (blink <= 0)
			blink = 1.0;
		
		if (timer > -1)
			timer -= 1;
		if (timer > 0){
			pitch += .0075;
			Audio.get("sGimmickFadein").setPitch(pitch);
		}
		if (timer == 0){
			Audio.fade("sGimmickFadein",0,.0125);
			Player.control = true;
			Audio.fade("sSkullQuake",0,.175);
			Fg.me.shakeTimer = 0;
			play();
		}
		
		if (shouldPlay && !playing){
			if (myLoader.loaded)
				play();
		}
		
		if (playing){
			if (System.currentTimeMillis() >= startTime + 7678.893){
				currentMeasure += 1;
				if (currentMeasure > 3)
					currentMeasure = 0;
				startTime += 7678.893 ; //TODO may need slight adjusting
			}
			
			for (int i=0; i<4; i++){
				if (activated[currentMeasure][i]){
					Audio.get("musGimmick"+i).setGain(.8);
					Audio.fade("musGimmick"+i, 1.0, .8);
				}
				else
					Audio.fade("musGimmick"+i, 0.0, .01);
			}
		}
	}
	
	public void render(){
		Graphics.setAlpha(1.0);
		double barWidth = screenW / 4.0, barHeight = screenH / 4.0;
		/*Graphics.setColor("0081CF");
		Shape.drawRectangle(screenX - 3, screenY - 3, screenX + screenW + 3, screenY + screenH + 3);*/
		//select Bg
		Graphics.setColor("CCFF16");
		Shape.drawRectangle(screenX + (barWidth * editSelect), screenY, screenX + (barWidth * (editSelect + 1)), screenY + screenH);
		
		for (int i=0; i<4; i++){
			for (int j = 0; j<4; j++){
				if (activated[i][j]){
					double addX = 0, addY = 0;
					
					Graphics.setColor("90FF30");
					if (editSelect == i)
						Graphics.setColor("EE75FF");
					if (currentMeasure == i && playing){
						addX = Calc.rangedRandom(2.5);
						addY = Calc.rangedRandom(2.5);
					}
					Shape.drawRectangle(screenX + (barWidth * i) + addX, screenY + (barHeight * j) + addY, screenX + (barWidth * (i + 1)) + addX, screenY + (barHeight * (j + 1)) + addY - 3);
					if (editSelect == i){
						Graphics.setColor("F2FF07");
						Graphics.setAlpha(blink);
						Shape.drawRectangle(screenX + (barWidth * i) + addX, screenY + (barHeight * j) + addY, screenX + (barWidth * (i + 1)) + addX, screenY + (barHeight * (j + 1)) + addY - 3);
						Graphics.setAlpha(1.0);
					}
				}
			}
		}
		
		if (playing){
			Graphics.setColor("0081CF");
			double cursorMult = ((System.currentTimeMillis() - startTime) / 7678.89) + (double)currentMeasure;
			Shape.drawLine(screenX + (barWidth * cursorMult), screenY, screenX + (barWidth * cursorMult), screenY + screenH, 2.8);
		}
		
		BlendMode.SUBTRACT.set();
		Sprite.get("sOver").render(0, Sprite.NORTHWEST, screenX, screenY, screenW / 640.0, screenH / 480.0, 0, .2, 1, 1, 1);
		BlendMode.NORMAL.set();
	}
	
	/**when u press the ok box*/
	public void confirm(){
		editSelect += 1;
		if (editSelect > 3){
			//if we've never completed the "puzzle"
			if (Global.event[2] == 0 && !shouldPlay){
				boolean fail = false;
				for (int i=0; i<4; i++){
					for (int j=0; j<4; j++){
						if (activated[i][j] != desired[i][j])
							fail = true;
					}
				}
				
				if (fail){
					//TODO fail sound
				}
				else{
					Audio.get("sSkullQuake").setLooping(true);
					Sound.play("sSkullQuake");
					Player.control = false;
					timer = 192;
					Global.event[2] = 1;
					Fg.me.shakeTimer = 192;
					
					Audio.get("sGimmickFadein").setPitch(pitch);
					Audio.get("sGimmickFadein").setGain(0);
					Audio.fade("sGimmickFadein", 1.0, .008);
					Audio.get("sGimmickFadein").setLooping(true);
					Sound.play("sGimmickFadein");
					new DreamMusicHeartAppear(448, 432);
				}
			}
			editSelect = 0;
		}
	}
	
	public void destroy(){
		me = null;
		super.destroy();
	}
	
	public void roomDestroy(){
		destroy();
	}
	
	public void play(){
		for (int i=0; i<4; i++){
			Audio.get("musGimmick"+i).setLooping(true);
			Audio.get("musGimmick"+i).setGain(0.0);
			Audio.fade("musGimmick"+i, 0.0, 1.0);
		}
		
		for (int i=0; i<4; i++)
			Sound.play("musGimmick"+i);
		
		startTime = System.currentTimeMillis();
		playing = true;
		currentMeasure = 0;
	}

}
