package world.pet;

import main.Calc;
import world.control.Sound;

public class PetWow extends PetEntity{
	public double a = 0;

	public PetWow(double x, double y, PetShell p) {
		super(x, y, p);
		
		alarmInitialize(1);
		if (!shell.win){
			alarm[0] = 54;
			Sound.playPitched("sPetWin", .025);
		}
		
		setDepth( -1 );
	}
	
	public void alarmEvent(int i){
		shell.win = true;
	}
	
	public void step(){
		a += 1;
		x = 2 * Math.sin(a / 30);
		y = 2 * Math.cos(a / 32);
		
		new PetHeart(-10 + Calc.random(42), 30, shell);
		alarmStep();
	}
	
	public void render(){
		shell.wow.render(0, x, y + 2, shell);
	}

}
