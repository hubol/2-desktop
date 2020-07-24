package world.pet;

import world.control.Sound;

public class PetFeed extends PetEntity{
	public int img;

	public PetFeed(double x, double y, PetShell p) {
		super(x, y, p);
		img = shell.lastFood;
	}
	
	public void select(){
		Sound.playPitched("sPetSelect", .005);
		if (img == 0)
			img = 1;
		else
			img = 0;
	}
	
	public void confirm(){
		Sound.playPitched("sPetConfirm", .005);
		shell.lastFood = img;
		
		if (!shell.sick){
			if (img == 0)
				shell.hungry += 1;
			else
				shell.happy += 1;
			
			shell.hungry = Math.min(4, shell.hungry);
			shell.happy = Math.min(4, shell.happy);
			
			new PetEat(0,0,shell,img);
		}
		else
			new PetRefuse(0,0,shell);
		destroy();
	}
	
	public void cancel(){
		Sound.playPitched("sPetCancel", .005);
		new PetMain(0, 0, shell);
		destroy();
	}
	
	public void render(){
		shell.screenFood.render(img, 0, 0, shell);
	}

}
