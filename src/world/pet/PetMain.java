package world.pet;

import world.control.Sound;
import main.Calc;

public class PetMain extends PetEntity{
	public boolean upclose, active, hide;
	public int cX, cY, cImg, cXsc, uImg, uXsc, uphs;
	
	public int poopXsc, washOff, wait;

	public PetMain(double x, double y, PetShell p) {
		super(x, y, p);
		upclose = false;
		active = true;
		hide = false;
		
		wait = 180;
		
		cInit();
		
		uImg = 0;
		uXsc = 1;
		
		washOff = 0;
		
		uphs = -1;
		
		poopXsc = 1;
		
		alarmInitialize(4);
		animate();
		
		if (!shell.sick && shell.hungry == 4 && shell.happy == 4 && shell.poops == 0){
			shell.selected = -1;
			new PetWow(0, 0, shell);
			destroy();
		}
	}
	
	public void cInit(){
		cX = 8;
		cY = 14;
		cXsc = 1;
		cImg = 0;
	}
	
	public void animate(){
		if (active && !upclose){
			poopXsc *= -1;
			
			//HEALTHY ANIMATION
			if (!shell.sick && shell.happy > 0){
				int oldX = cX;
				cX = oldX + (int)Calc.rangedRandom(7);
				while (cX > 16 || cX < 0)
					cX = oldX + (int)Calc.rangedRandom(7);
				if (Calc.pointDistance(oldX, 0, cX, 0) < 3){
					cXsc *= -1;
					
					if (Calc.random(1) > .7){
						if (cImg == 0)
							cImg = 4;
						else
							cImg = 0;
					}
				}
				else{
					if (cImg == 0)
						cImg = 4;
					else
						cImg = 0;
					
					if (cX > oldX)
						cXsc = 1;
					else
						cXsc = -1;
				}
			}
			//UNHEALTHY
			else{
				if (shell.sick)
					cImg = 3;
				else
					cImg = 1;
				cXsc *= -1;
			}
		}
		alarm[0] = 15;
	}
	
	public void alarmEvent(int i){
		if (i == 0){
			animate();
		}
		else if (i == 1){
			uphs += 1;
			if (uphs < 3){
				if (cImg == 2)
					cImg = 0;
				else
					cImg = 5;
				cY = 8;
			}
			else{
				hide = true;
				
				if (uphs < 5){
					uXsc *= -1;
					uImg = 0;
				}
				else if (uphs < 11){
					uXsc = 1;
					if (uImg != 2)
						uImg = 2;
					else{
						uImg = 1;
						Sound.playPitched("sPetCall", .005);
					}
				}
				else if (uphs < 13){
					uXsc *= -1;
					uImg = 0;
				}
				else if (uphs < 15){
					hide = false;
					if (cImg == 5)
						cImg = 0;
					else
						cImg = 5;
				}
			}
			
			if (uphs < 15)
				alarm[1] = 15;
			else{
				upclose = false;
				uphs = -1;
				cInit();
				hide = false;
			}
		}
	}
	
	public void step(){
		if (active){
			alarmStep();
			
			wait -= 1;
			if (wait == 0)
				shell.selected = -1;
		}
		else{
			wait = 180;
			cInit();
		}
	}
	
	public void select(){
		if (active){
			if (upclose){
				alarm[1] = -1;
				upclose = false;
				uphs = -1;
				cInit();
				hide = false;
				Sound.playPitched("sPetCancel", .005);
			}
			else
				Sound.playPitched("sPetSelect", .005);
			wait = 180;
			shell.selected += 1;
			if (shell.selected > 3)
				shell.selected = -1;
		}
	}
	
	public void render(){
		int xOff = 0;
		if (shell.poops > 0 && !upclose){
			if (washOff == 0){
				xOff = -10;
				
				if (shell.sick || shell.happy == 0)
					xOff += 6;
			}
			
			for(int i=0; i<shell.poops; i++)
				shell.poop.render(0, 23 + washOff, 22 - (i * 7), poopXsc, 1, shell);
		}
		
		if (!hide)
			shell.character.render(cImg, cX + xOff + washOff, cY, cXsc, 1, shell);
		
		if (uphs >= 3 && uphs < 13)
			shell.upClose.render(uImg, 0, 0, uXsc, 1, shell);
		
		if (uphs == 0)
			shell.startle.render(0, cX + 18, cY - 6, shell);
		
		if (shell.sick && washOff == 0)
			shell.skull.render(0, 2, 2, shell);
	}
	
	public void confirm(){
		if (active){
			boolean go = true;
			
			if (shell.selected == 0){
				new PetStatus(0, 0, shell);
				destroy();
			}
			else if (shell.selected == 1){
				new PetFeed(0, 0, shell);
				destroy();
			}
			else if (shell.selected == 2){
				active = false;
				new PetWash(0,0,shell, this);
			}
			else if (shell.selected == 3){
				if (shell.sick){
					new PetNeedle(0, 0, shell);
					destroy();
				}
				else{
					go = false;
					Sound.playPitched("sPetFail", .005);
				}
			}
			
			if (shell.selected > -1 && go)
				Sound.playPitched("sPetConfirm", .005);
		}
	}
	
	public void cancel(){
		if (active){
			if (shell.selected == -1){
				if (!upclose){
					if (!shell.sick && shell.happy > 0){
						upclose = true;
						cInit();
						cY = 10;
						cImg = 2;
						uphs = 0;
						alarm[1] = 15;
						Sound.playPitched("sPetStartle", .005);
					}
					else{
						Sound.playPitched("sPetCancel", .005);
					}
				}
				
			}
			else{
				shell.selected = -1;
				Sound.playPitched("sPetCancel", .005);
			}
		}
	}

}
