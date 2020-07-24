package world.intro;

import graphics.Sprite;
import world.control.Sound;
import main.Calc;
import main.Entity;

public class TypeDass extends Entity{
	public int[] img;
	public double scale = 0.47619;
	
	public boolean startDass;
	public int phase, at;
	public boolean endFlash;
	
	public double[] dassMult;
	public int[] dassId;

	public TypeDass() {
		super(0, 0);
		startDass = Intro.me.myGen.random(1) < .125;
		endFlash = Intro.me.myGen.random(1) < .5;
		
		img = new int[19];
		//HUBOLHUBOLHUBOL.COM
		img[0] = 0; img[1] = 1; img[2] = 2; img[3] = 3; img[4] = 4;
		img[5] = 0; img[6] = 1; img[7] = 2; img[8] = 3; img[9] = 4;
		img[10] = 0; img[11] = 1; img[12] = 2; img[13] = 3; img[14] = 4;
		img[15] = 5; img[16] = 6; img[17] = 3; img[18] = 7;
		
		//DASS THAT SHIT UP!!!
		if (startDass){
			img[3] = 8; img[8] = 8; img[13] = 8; img[17] = 8;
		}
		
		dassMult = new double[4];
		for (int i=0; i<4; i++)
			dassMult[i] = 1;
		
		dassId = new int[4];
		dassId[0] = 3; dassId[1] = 8; dassId[2] = 13; dassId[3] = 17;
		
		phase = 0;
		at = -1;
		alarmInitialize(5);
		alarm[0] = 2;
	}
	
	public void alarmEvent(int i){
		if (i == 0){
			at += 1;
			if (at < 19){
				if (startDass && (at == 3 || at == 8 || at == 13 || at == 17)){
					Sound.playPitched("sBanaanKopiAvOw",.05);
					if (at == 3)
						dassMult[0] = 48;
					else if (at == 8)
						dassMult[1] = 48;
					else if (at == 13)
						dassMult[2] = 48;
					else
						dassMult[3] = 48;
				}
				Sound.playPitched("sIntroKey", .075);
				alarm[0] = 2 + (int)Intro.me.myGen.random(4);
			}
			else{
				if (startDass)
					alarm[4] = 60;
				else{
					if (endFlash)
						alarm[1] = 10;
					else
						alarm[3] = 15 + (int)Intro.me.myGen.random(16);
				}
			}
		}
		else if (i == 1){
			phase += 1;
			if (phase < 3){
				Sound.playPitched("sBanaanKopiEnd",.6);
				img[3] = 8; img[8] = 8; img[13] = 8; img[17] = 8;
				alarm[2] = 3 + (int)Intro.me.myGen.random(4);
			}
			else{
				Sound.playPitched("sBanaanKopiAvOw",.02);
				img[3] = 8; img[8] = 8; img[13] = 8; img[17] = 8;
				for (int j=0; j<4; j++)
					dassMult[j] = 48.0;
				alarm[4] = 60;
			}
			//flash in
		}
		else if (i == 2){
			img[3] = 3; img[8] = 3; img[13] = 3; img[17] = 3;
			alarm[1] = 8 + (int)Intro.me.myGen.random(8);
		}
		else if (i == 3){
			//single flash
			Sound.playPitched("sBanaanKopiAvOw",.02);
			int a = (int)Intro.me.myGen.random(4);
			dassMult[a] = 48;
			img[dassId[a]] = 8;
			alarm[4] = 60;
		}
		else if (i == 4){
			Intro.me.complete();
		}
	}
	
	public void step(){
		for (int i=0; i<4; i++)
			dassMult[i] = Calc.approach(dassMult[i], 1, 5.0);
		
		alarmStep();
	}
	
	public void render(){
		double xx = 320 - (9 * 64 * scale);
		int dass = -1;
		for (int i=0; i<at; i++){
			double rand = 1.05;
			if (i == 3 || i == 8 || i == 13 || i == 17){
				dass += 1;
				rand *= dassMult[dass];
			}
			Sprite.get("sIntroHubolFont").render(img[i], Sprite.CENTERED, xx + Calc.rangedRandom(.3 * rand), 240 + Calc.rangedRandom(.3 * rand), scale, scale, 0, 1, Intro.me.LINE);
			xx += 64 * scale;
		}
	}

}
