package world.pet;

import world.control.Sound;

public class PetHappy extends PetEntity{
	public int cY, times;

	public PetHappy(double x, double y, PetShell p) {
		super(x, y, p);
		cY = 0;
		alarmInitialize(1);
		times = 1;
		alarm[0] = 15;
		
		Sound.playPitched("sPetHappy", .005);
	}
	
	public void alarmEvent(int i){
		if (cY == 0)
			cY = 3;
		else
			cY = 0;
		times += 1;
		if (times == 7){
			new PetMain(0, 0, shell);
			destroy();
		}
		else
			alarm[0] = 15;
	}
	
	public void render(){
		int img = 4;
		if (cY > 0){
			img = 6;
			shell.sun.render(0, 25, 11 - cY, shell);
		}
		shell.character.render(img, 8, 14 - cY, shell);
	}

}
