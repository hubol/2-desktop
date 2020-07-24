package world.boss;

import world.control.Global;
import world.control.Sound;
import graphics.Font;
import graphics.Sprite;
import main.Calc;
import main.Entity;

public class BossAnnounce extends Entity{
	public String name, color, sound;
	
	public int amount, timer, wait, at, phase, amountNoSpace;
	
	public boolean[] vis;
	public double[] xx, yy, alp, ang, xsc, ysc;
	public int[] glitch, count, img, desImg;
	
	public Font myFont;
	
	public double xStart, shineScale, shineAngle, shineImg;

	public BossAnnounce(String name, String color, String sound) {
		super(0, 0);
		setDepth(Integer.MIN_VALUE+15);
		setCollisionGroup(Global.DEACTIVATEME);
		
		myFont = Global.FONT;
		
		this.name = name;
		this.color = color;
		this.sound = sound;
		
		shineAngle = 0;
		shineScale = 1;
		
		amount = this.name.length();
		
		amountNoSpace = amount;
		for (int i=0; i<amount; i++){
			if (name.charAt(i) == ' ')
				amountNoSpace -= 1;
		}
		
		wait = (int)Math.max(1, 30.0/(double)amountNoSpace);
		
		xStart = 352.0 - ((double)amount * 24.0);
		
		vis = new boolean[amount];
		xx = new double[amount];
		yy = new double[amount];
		alp = new double[amount];
		ang = new double[amount];
		xsc = new double[amount];
		ysc = new double[amount];
		glitch = new int[amount];
		count = new int[amount];
		img = new int[amount];
		desImg = new int[amount];
		
		shineImg = 0;
		
		for (int i=0; i<amount; i++){
			vis[i] = false;
			xx[i] = 0;
			yy[i] = 0;
			alp[i] = 1;
			ang[i] = 0;
			xsc[i] = 1;
			ysc[i] = 1;
			glitch[i] = (int)Calc.random(8);
			count[i] = 9000;
			
			desImg[i] = myFont.fetchSymbolId(name.charAt(i));
			img[i] = desImg[i];
		}
		
		timer = 1;
		at = 0;
		
		phase = 0;
	}
	
	public void step(){
		shineAngle += 8 + (shineScale - 1)*2;
		shineScale = Calc.approach(shineScale, 1, 6);
		
		timer -= 1;
		if (timer <= 0){
			if (phase == 0){
				Sound.playPitched(sound, .2);
				if (glitch[at] == 0) //shake pos
					setGlitch(Calc.rangedRandom(8),Calc.rangedRandom(8),1,0,1,1);
				else if (glitch[at] == 1) //shake angle
					setGlitch(0,0,1,Calc.rangedRandom(-0),1,1);
				else if (glitch[at] == 2) //shrink in
					setGlitch(0,0,1,0,1 + Calc.random(.3),1 + Calc.random(.3));
				else if (glitch[at] == 3) //img shifting
					setGlitch(0,0,1,0,1,1);
				else if (glitch[at] == 4) //grow in
					setGlitch(0,0,1,0,1 - Calc.random(.3),1 - Calc.random(.3));
				else if (glitch[at] == 5) //flash
					setGlitch(0,0,Calc.random(1),0,1,1);
				else if (glitch[at] == 6) //big horizontal
					setGlitch(Calc.rangedRandom(16),0,1,0,1,1);
				else if (glitch[at] == 7) //big vert + img
					setGlitch(0,Calc.rangedRandom(16),1,0,1,1);
				
				at += 1;
				
				if (at < amount){
					while (name.charAt(at) == ' ')
						at += 1;
				}
				
				timer = wait;
				
				if (at >= amount){
					Sound.play("sBossTwinkle");
					shineScale = 3;
					shineImg = 1;
					at = 0;
					timer = 60;
					phase = 1;
				}
			}
			else if (phase == 1){
				Sound.playPitched(sound, .4);
				
				vis[at] = false;
				at += 1;
				
				if (at < amount){
					while (name.charAt(at) == ' ')
						at += 1;
				}
				
				timer = 3;
				
				if (at >= amount){
					timer = 5;
					phase = 2;
				}
			}
			else if (phase == 2){
				destroy();
			}
		}
		
		for (int i=0; i<amount; i++){
			if (vis[i]){
				if (count[i] > 0){
					if (glitch[i] == 0) //shake pos
						setGlitch(i,Calc.rangedRandom(8),Calc.rangedRandom(8),1,0,1,1);
					else if (glitch[i] == 1) //shake angle
						setGlitch(i,0,0,1,Calc.rangedRandom(-0),1,1);
					else if (glitch[i] == 4 || glitch[i] == 2){ //grow in || shrink in
						xsc[i] = Calc.approach(xsc[i],1,9 + Calc.rangedRandom(2));
						ysc[i] = Calc.approach(xsc[i],1,9 + Calc.rangedRandom(2));
					}
					else if (glitch[i] == 5) //flash
						setGlitch(i,0,0,Calc.random(1),0,1,1);
					else if (glitch[i] == 6) //big horizontal
						setGlitch(i,Calc.rangedRandom(16),0,1,0,1,1);
					else if (glitch[i] == 7) //big vert
						setGlitch(i,0,Calc.rangedRandom(16),1,0,1,1);
					
					if (glitch[i] == 7 || glitch[i] == 3){ //img shifting
						img[i] = myFont.fetchSymbolId((Character)Calc.choose('I','W','A','N','T','A','B','O','Y','T','O','F','U','C','K','M','E','!'));
					}
				}
				else{
					xx[i] = 0;
					yy[i] = 0;
					alp[i] = 1;
					ang[i] = 0;
					xsc[i] = 1;
					ysc[i] = 1;
					img[i] = desImg[i];
					glitch[i] = 69;
				}
				count[i] -= 1;
			}
		}
		
		shineImg += .05 + (shineScale / 30.0);
		if (shineImg >= 2)
			shineImg -= 2;
	}
	
	public void setGlitch(double x, double y, double al, double an, double xs, double ys){
		vis[at] = true;
		xx[at] = x;
		yy[at] = y;
		alp[at] = al;
		ang[at] = an;
		xsc[at] = xs;
		ysc[at] = ys;
		count[at] = 10;
	}
	
	public void setGlitch(int at, double x, double y, double al, double an, double xs, double ys){
		vis[at] = true;
		xx[at] = x;
		yy[at] = y;
		alp[at] = al;
		ang[at] = an;
		xsc[at] = xs;
		ysc[at] = ys;
	}
	
	public void render(){
		double x = xStart, y = 432;
		
		for (int i=0; i<amount; i++){
			myFont.sprite.render(desImg[i], Sprite.CENTERED, x, y, 1.5, 1.5, 0, 1, "006187");
			myFont.sprite.render(desImg[i], Sprite.CENTERED, x + 3, y, 1.5, 1.5, 0, 1, "006187");
			myFont.sprite.render(desImg[i], Sprite.CENTERED, x - 3, y, 1.5, 1.5, 0, 1, "006187");
			myFont.sprite.render(desImg[i], Sprite.CENTERED, x, y + 3, 1.5, 1.5, 0, 1, "006187");
			myFont.sprite.render(desImg[i], Sprite.CENTERED, x, y - 3, 1.5, 1.5, 0, 1, "006187");
			myFont.sprite.render(desImg[i], Sprite.CENTERED, x + 2, y + 2, 1.5, 1.5, 0, 1, "006187");
			myFont.sprite.render(desImg[i], Sprite.CENTERED, x + 2, y - 2, 1.5, 1.5, 0, 1, "006187");
			myFont.sprite.render(desImg[i], Sprite.CENTERED, x - 2, y + 2, 1.5, 1.5, 0, 1, "006187");
			myFont.sprite.render(desImg[i], Sprite.CENTERED, x - 2, y - 2, 1.5, 1.5, 0, 1, "006187");
			x += 48;
		}
		
		x = xStart;
		
		for (int i=0; i<amount; i++){
			if (vis[i]){
				myFont.sprite.render(img[i], Sprite.CENTERED, x + xx[i], y + yy[i], 1.5 * xsc[i], 1.5 * ysc[i], ang[i], alp[i], color);
				if (i == amount - 1)
					Sprite.get("sSparkle").render(1 + shineImg, Sprite.CENTERED, x + xx[i] + (20.0 * xsc[i]), y + yy[i] - (26.0 * ysc[i]), 3 * shineScale, 3 * shineScale, shineAngle, .8, 1, 1, 1);
			}
			x += 48;
		}
	}

}
