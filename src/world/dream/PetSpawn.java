package world.dream;

import java.util.ArrayList;

import graphics.Graphics;
import graphics.Shape;
import graphics.Sprite;
import world.control.EV;
import world.control.Global;
import world.control.Shake;
import world.control.Sound;
import world.control.SpriteLoader;
import world.gameplay.Heart;
import world.pet.PetMain;
import world.pet.PetShell;
import main.Calc;
import main.Entity;
import main.Scene;

public class PetSpawn extends Entity{
	public PetButton select, confirm, cancel;
	public PetShell shell;
	public Shake s;
	
	public final double pixel = .98, nopixel = .1;
	public final double tx = 256, ty = 160; //top right of pixel display (icons go above)
	public final double dx = 4, dy = (128.0 / 30.0); //xsc,ysc of pixels

	public PetSpawn(double x, double y) {
		super(x, y);
		setCollisionGroup(Global.DEACTIVATEME);
		
		setDepth(2);
		
		new SpriteLoader("sPetButton_2", "sPetButtonMask", "sPetIcons_4");
		
		s = new Shake(.25);
		
		//initialize!!
		shell = new PetShell(32, 30);
		//shell.hungry = 0;
		//shell.happy = 4;
		shell.poops = 4;
		shell.sick = true;
		
		if (Global.event[EV.PET_DREAM] == 1){
			shell.poops = 0;
			shell.sick = false;
			shell.hungry = 4;
			shell.happy = 4;
		}
		else{
			ArrayList<Heart> list = Scene.getEntityList(Heart.class);
			list.get(0).destroy();
		}
		
		new PetMain(0, 0, shell);
		
		select = new PetButton(256,384);
		confirm = new PetButton(320,384);
		cancel = new PetButton(384,384);
	}
	
	public void step(){
		if (select.pressed())
			shell.select();
		if (confirm.pressed())
			shell.confirm();
		if (cancel.pressed())
			shell.cancel();
		shell.update();
		shell.render(); //this doesnt ACTUALLY render in the game yet yo
		
		if (shell.win && Global.event[EV.PET_DREAM] == 0){
			Global.event[EV.PET_DREAM] = 1;
			
			for (int i=0; i<12; i++){
				new DreamHeartParticles(160, 320, Calc.random(360), Integer.MIN_VALUE + 10);
				DreamHeartParticles j = new DreamHeartParticles(160, 320, Calc.random(360), Integer.MIN_VALUE + 10);
				j.setSpeed(j.getSpeed() * 4);
				if (i > 3){
					j = new DreamHeartParticles(160, 320, Calc.random(360), Integer.MIN_VALUE + 10);
					j.setSpeed(j.getSpeed() * 8);
				}
			}
			Sound.play("sDreamHeartAppear");
			new Heart(160, 320, 27);
			
			Global.addTweet("thank you");
		}
	}
	
	public void render(){
		Graphics.setColor("0095cf");
		//ICONS
		for (int i=0; i<4; i++){
			double al = nopixel;
			if (shell.selected == i){
				al = pixel;
				Sprite.get("sPetIcons").render(i, Sprite.CENTERED, tx + 18 + (32 * i) - s.y + Calc.rangedRandom(.3), ty - 14 + s.x + Calc.rangedRandom(.3), .28, .28, 0, .2, "0095cf");
			}
			Sprite.get("sPetIcons").render(i, Sprite.CENTERED, tx + 16 + (32 * i) - s.y + Calc.rangedRandom(.3), ty - 16 + s.x + Calc.rangedRandom(.3), .28, .28, 0, al, "0095cf");
		}
		
		//PIXELS
		for (int i=0; i<32; i++){
			for (int j=0; j<30; j++){
				double xx = tx + s.x + Calc.rangedRandom(.25) + (i * dx), yy = ty + s.y + Calc.rangedRandom(.25) + (j * dy);
				double al = nopixel;
				if (shell.display[i][j]){
					Graphics.setAlpha(.2);
					int xxx = 2, yyy = 2;
					if (i == 31)
						xxx = 0;
					if (j == 29)
						yyy = 0;
					Shape.drawRectangle(xx + xxx, yy + yyy, xx + (dx * .95) + xxx, yy + (dy * .95) + yyy);
					al = pixel;
				}
				Graphics.setAlpha(al);
				Shape.drawRectangle(xx, yy, xx + (dx * .95), yy + (dy * .95));
			}
		}
		
		Graphics.setAlpha(1);
	}

}
