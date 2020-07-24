package world.player;

import java.util.ArrayList;

import world.boss.papa.Papa;
import world.control.EV;
import world.control.Global;
import world.control.Sound;
import world.dream.DreamHeartParticles;
import world.enemy.BasicEnemy;
import audio.Audio;
import graphics.Sprite;
import main.Calc;
import main.Scene;

public class Bomb extends BasicEnemy {
	public double timer, img, a, clickTime, clickMax, clickPitch, zxscale, zyscale;
	public int face, um;
	public boolean played;
	
	public final static Sprite BOMB = Sprite.get("sCherryBomb"), MASK = Sprite.get("sBombMask"), FACE = Sprite.get("sCherryFace"), FACE_UP = Sprite.get("sCherryFaceUp");

	public Bomb(double x, double y) {
		super(Math.round(x), Math.round(y));
		sprite = BOMB;
		orientation = Sprite.SOUTH;
		mask = MASK.mask;
		setCollisionGroup(Global.DEACTIVATEME, Global.BASICENEMY);
		setDepth( -20 );
		
		landCheckNecessary = false;
		
		played = true;
		
		imageSpeed = 0;
		imageSingle = 0;
		timer = 90;
		a = 0;
		
		clickMax = 15;
		clickTime = clickMax + 1;
		clickPitch = 1;
		um = 0;
		clickSound();
		
		face = (int)Calc.random(6);
		
		if(!Scene.collision(this, x,Math.round(y)+1, Global.SOLIDBLOCK, Global.UPBLOCK)){
			vspeed = 2;
			if (Scene.collision(this, x, Math.round(y) + 1, Global.WATER))
				vspeed = 1;
			played = false;
		}
		else
			Sound.playPitched("sBombPlace");
		
		zxscale = 1;
		zyscale = 1;
	}
	
	public void step(){
		if (Global.roomX == 21)
			new DreamHeartParticles(x, y - 38, 90 + Calc.rangedRandom(5), Integer.MIN_VALUE + 10);
		
		img += 1.0 - (timer/90.0);
		img = img % 2;
		if (timer > 60)
			imageSingle = img;
		else if (timer > 30)
			imageSingle = 2 + img;
		else
			imageSingle = 4 + img;
		
		imageSingle = Math.min(5, imageSingle);
		
		double vSign = (vspeed<0?-1:1);
		if(Scene.collision(this, x,Math.round(y)+vspeed+vSign, Global.SOLIDBLOCK, Global.UPBLOCK))
		{
			vspeed=0;
			
			if (!played){
				played=true;
				Sound.playPitched("sBombLand");
			}
			
			y = Math.ceil(y);
			while(!Scene.collision(this, x,y+vSign, Global.SOLIDBLOCK, Global.UPBLOCK)){
				y+=vSign;
			}
		}
		else
		{
			if (!Scene.collision(this, x, y, Global.WATER))
				vspeed+=.85;
			else{
				vspeed += .55;
				if (vspeed > 4)
					vspeed = 4;
			}
			
			if (played)
				played = false;
		}
		
		if (vspeed != 0)
			super.step();
		
		if (Player.me.vLoop){
			while (y > 480)
				y -= 480;
			while (y < 0)
				y += 480;
		}
		
		clickTime -= 1;
		if (clickTime <= 0)
			clickSound();
		
		timer -= 1;
		if (timer <= 0)
			explode();
		
		a += 1;
		zxscale = 1 + Math.abs(.05 * Math.sin(a/Math.max(1, timer/8)));
		zyscale = zxscale;
		
		if (vspeed == 0 && Global.event[EV.BOMBUPGRADE] == 1){
			if (Player.me.y <= y - 44){
				setCollisionGroup(Global.DEACTIVATEME, Global.SOLIDBLOCK, Global.BASICENEMY); //solid block group
				
				//if (Player.me.vspeed >= 0){
					if (Scene.collision(this, x, y - 1, Global.PLAYER)){
						//System.out.println("this is happening!!");
						/*if (Player.me.vspeed >= 0)
							Player.me.y = y - 44;*/
					}
				//}
			}
			else
				setCollisionGroup(Global.DEACTIVATEME, Global.UPBLOCK, Global.BASICENEMY); //non-solid upblock group (for enemies who walk on upblocks)
		}
	}
	
	public void gunDamage(){
		explode();
	}
	
	public void clickSound(){
		Audio.get("sBombTick").setPitch(clickPitch - (um * .2));
		Sound.play("sBombTick");
		clickMax -= .7;
		clickMax *= .94;
		if (clickMax < 1)
			clickMax = 1;
		clickTime = clickMax;
		clickPitch += .05;
		
		if (um == 1)
			um = 0;
		else
			um = 1;
	}
	
	public void explode(){
		Sound.playPitched("sBombBlast",.05);
		
		if (Global.room(4, 4)){
			ArrayList<Papa> list = Scene.getEntityList(Papa.class);
			for (int i=0; i<list.size(); i++)
				list.get(i).bombDetect(x, y - 16);
		}
		
		Global.explosion(x, y-16);
		
		super.destroy();
		
		Global.refreshRain();
	}
	
	public void draw(double x, double y){
		double i = (1.0 - (timer/90.0)) + 0.12;
		double zx = x, zy = y, ix = imageSingle;

		x += (-4 + Calc.random(8)) * i;
		y += (-4 + Calc.random(8)) * i;
		sprite = BOMB;
		sprite.render(imageSingle, orientation, x, y, zxscale, zyscale, angle, 1, 1, 1, 1);
		imageSingle = face;
		if (Global.event[EV.BOMBUPGRADE] == 0)
			sprite = FACE;
		else
			sprite = FACE_UP;
		sprite.render(imageSingle, orientation, x, y, zxscale, zyscale, angle, 1, 1, 1, 1);
		x = zx;
		y = zy;
		imageSingle = ix;
	}
	
	public void render(){
		draw(x, y);
		if (Player.me.vLoop){
			draw(x, y - 480);
			draw(x, y + 480);
		}
	}

}
