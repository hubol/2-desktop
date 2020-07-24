package world.pet;

import world.control.Sound;

public class PetNeedle extends PetEntity{
	public int cImg, cX, times, needle;

	public PetNeedle(double x, double y, PetShell p) {
		super(x, y, p);
		cX = 1;
		cImg = 1;
		needle = -1;
		alarmInitialize(1);
		times = 1;
		alarm[0] = 15;
	}
	
	public void alarmEvent(int i){
		times += 1;
		
		if (times < 3){
			if (cX == 1)
				cX = 0;
			else
				cX = 1;
		}
		else{
			cX = 0;
			
			needle += 1;
			
			if (needle < 4)
				Sound.playPitched("sPetNeedle", .025);
			
			if (cImg == 1)
				cImg = 3;
			else
				cImg = 1;
		}
		
		if (needle == 4){
			shell.sick = false;
			new PetHappy(0, 0, shell);
			destroy();
		}
		else
			alarm[0] = 15;
	}
	
	public void render(){
		shell.character.render(cImg, 6 + cX, 14, shell);
		if (needle > -1 && needle < 4)
			shell.needle.render(needle, 21, 3, shell);
	}

}
