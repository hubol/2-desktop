package world.pet;

import world.control.Sound;
import main.Calc;

public class PetStatus extends PetEntity{

	public PetStatus(double x, double y, PetShell p) {
		super(x, y, p);
	}
	
	public void select(){
		cancel();
	}
	
	public void confirm(){
		cancel();
	}
	
	public void cancel(){
		Sound.playPitched("sPetCancel", .005);
		new PetMain(0, 0, shell);
		destroy();
	}
	
	public void render(){
		shell.screenStatus.render(0, 0, 0, shell);
		for (int i=0; i<4; i++)
			shell.heartFill.render(Calc.boolToInt(shell.hungry > i), 1 + (i * 8), 7, shell);
		for (int i=0; i<4; i++)
			shell.heartFill.render(Calc.boolToInt(shell.happy > i), 1 + (i * 8), 22, shell);
	}

}
