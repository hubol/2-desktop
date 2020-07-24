package world.pet;

import world.control.Sound;


public class PetEat extends PetEntity{
	public PetSprite meal;
	public int fImg, cImg, cXsc, fY;
	public int phase;
	
	public final int spd = 15;

	public PetEat(double x, double y, PetShell p, int meal) {
		super(x, y, p);
		this.meal = shell.snack;
		if (meal == 0)
			this.meal = shell.meal;
		
		phase = -1;
		
		fImg = 0;
		cImg = 0;
		cXsc = 1;
		fY = 0;
		
		alarmInitialize(1);
		alarm[0] = spd;
	}
	
	public void alarmEvent(int i){
		phase += 1;
		if (phase == 0){
			cXsc = -1;
			fY = 14;
		}
		else if (phase == 1){
			cImg = 2;
		}
		else if (phase == 2){
			cImg = 0;
			fImg = 1;
		}
		else if (phase == 3){
			cImg = 2;
		}
		else if (phase == 4){
			cImg = 0;
			fImg = 2;
		}
		else if (phase == 5){
			cImg = 2;
		}
		else if (phase == 6){
			cImg = 0;
			fImg = 3;
		}
		else if (phase == 7){
			new PetFeed(0, 0, shell);
			destroy();
		}
		
		if (phase < 7)
			alarm[0] = spd;
	}
	
	public void step(){
		alarmStep();
	}
	
	public void select(){
		Sound.playPitched("sPetCancel", .005);
		new PetFeed(0, 0, shell);
		destroy();
	}
	
	public void confirm(){
		select();
	}
	
	public void cancel(){
		select();
	}
	
	public void render(){
		if (fImg < 3)
			meal.render(fImg, 0, fY, shell);
		shell.character.render(cImg, 16, 14, cXsc, 1, shell);
	}


}
