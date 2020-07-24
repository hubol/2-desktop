package world.intro;

import java.util.ArrayList;

import graphics.Font;
import world.control.Global;
import world.control.Sound;
import audio.Audio;
import main.Calc;
import main.Entity;
import main.Scene;

public class DassBlockMake extends Entity{
	public int[] bX, bY;
	public boolean[] spawned;
	public int timer, spawn;
	
	public String chime;
	
	public double addX=0, addY=0;
	public Font myFont;
	
	public final String txt = "HUBOL";
	public boolean thing;
	
	public DassBlockMake() {
		super(0, 0);
		bX = new int[76];
		bY = new int[76];
		spawned = new boolean[76];
		
		for (int i=0; i<76; i++)
			spawned[i] = false;
		
		thing = (Intro.me.myGen.random(1) < .5);
		
		bX[0] = 112; bY[0] = 32; bX[1] = 240; bY[1] = 32; bX[2] = 272; bY[2] = 32; bX[3] = 304; bY[3] = 32; bX[4] = 336; bY[4] = 32; bX[5] = 368; bY[5] = 32; bX[6] = 496; bY[6] = 32; bX[7] = 144; bY[7] = 64; bX[8] = 176; bY[8] = 64; bX[9] = 208; bY[9] = 64; bX[10] = 400; bY[10] = 64; bX[11] = 432; bY[11] = 64; bX[12] = 464; bY[12] = 64; bX[13] = 144; bY[13] = 96; bX[14] = 176; bY[14] = 96; bX[15] = 432; bY[15] = 96; bX[16] = 464; bY[16] = 96; bX[17] = 112; bY[17] = 128; bX[18] = 208; bY[18] = 128; bX[19] = 400; bY[19] = 128; bX[20] = 496; bY[20] = 128; bX[21] = 80; bY[21] = 160; bX[22] = 240; bY[22] = 160; bX[23] = 368; bY[23] = 160; bX[24] = 528; bY[24] = 160; bX[25] = 80; bY[25] = 192; bX[26] = 240; bY[26] = 192; bX[27] = 272; bY[27] = 192; bX[28] = 336; bY[28] = 192; bX[29] = 368; bY[29] = 192; bX[30] = 528; bY[30] = 192; bX[31] = 80; bY[31] = 224; bX[32] = 240; bY[32] = 224; bX[33] = 272; bY[33] = 224; bX[34] = 304; bY[34] = 224; bX[35] = 336; bY[35] = 224; bX[36] = 368; bY[36] = 224; bX[37] = 528; bY[37] = 224; bX[38] = 80; bY[38] = 256; bX[39] = 272; bY[39] = 256; bX[40] = 304; bY[40] = 256; bX[41] = 336; bY[41] = 256; bX[42] = 528; bY[42] = 256; bX[43] = 80; bY[43] = 288; bX[44] = 240; bY[44] = 288; bX[45] = 272; bY[45] = 288; bX[46] = 304; bY[46] = 288; bX[47] = 336; bY[47] = 288; bX[48] = 368; bY[48] = 288; bX[49] = 528; bY[49] = 288; bX[50] = 112; bY[50] = 320; bX[51] = 208; bY[51] = 320; bX[52] = 240; bY[52] = 320; bX[53] = 272; bY[53] = 320; bX[54] = 304; bY[54] = 320; bX[55] = 336; bY[55] = 320; bX[56] = 368; bY[56] = 320; bX[57] = 400; bY[57] = 320; bX[58] = 496; bY[58] = 320; bX[59] = 144; bY[59] = 352; bX[60] = 176; bY[60] = 352; bX[61] = 432; bY[61] = 352; bX[62] = 464; bY[62] = 352; bX[63] = 144; bY[63] = 384; bX[64] = 176; bY[64] = 384; bX[65] = 208; bY[65] = 384; bX[66] = 400; bY[66] = 384; bX[67] = 432; bY[67] = 384; bX[68] = 464; bY[68] = 384; bX[69] = 112; bY[69] = 416; bX[70] = 240; bY[70] = 416; bX[71] = 272; bY[71] = 416; bX[72] = 304; bY[72] = 416; bX[73] = 336; bY[73] = 416; bX[74] = 368; bY[74] = 416; bX[75] = 496; bY[75] = 416;
		
		spawn = 0;
		
		myFont = Global.FONT;
		
		chime = "sIntroB"+(int)Intro.me.myGen.random(3);
		chime();
		
		alarmInitialize(3);
		alarm[1] = 1 + (int)Calc.random(29);
	}
	
	public void alarmEvent(int i){
		if (i == 0){
			Intro.me.complete();
		}
		else if (i == 1){
			addX = Calc.approach(addX, 0, 3);
			addY = Calc.approach(addY, 0, 3);
			alarm[1] = 1 + (int)Calc.random(29);
		}
		else if (i == 2){
			Sound.play("sIntroThunder");
			new Lightning(500, -10);
			
			ArrayList<DassBlock> list = Scene.getEntityList(DassBlock.class);
			for (int j = 0; j<list.size(); j++)
				list.get(j).vis = true;
		}
	}
	
	public void chime(){
		if (spawn < 76){
			for (int i=0; i<2; i++){
				if (spawn < 76){
					int a = (int)Intro.me.myGen.random(76);
					while(spawned[a]){
						a += 1;
						if (a > 75)
							a = 0;
					}
					
					spawned[a] = true;
					spawn += 1;
					
					char c = txt.charAt(a % txt.length());
					boolean fa = false;
					if (thing)
						fa = true;
					new DassBlock(bX[a], bY[a], this, c, fa);
				}
			}
			
			timer = 2 + (int)Intro.me.myGen.random(3);
			Audio.get(chime).setPitch(1 + Intro.me.myGen.rangedRandom(.3));
			Sound.play(chime);
		}
		if (spawn >= 76){
			timer = -69;
			if (thing){
				alarm[0] = 60;
				alarm[1] = 60000;
			}
			else{
				alarm[2] = 30;
				alarm[0] = 100;
			}
		}
	}
	
	public void step(){
		timer -= 1;
		if (timer == 0)
			chime();
		
		addX += Calc.rangedRandom(.7);
		addY += Calc.rangedRandom(.7);
		alarmStep();
	}
	
	public void render(){
		
	}

}
