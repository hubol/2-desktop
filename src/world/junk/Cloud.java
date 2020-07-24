package world.junk;

import javax.media.opengl.GL2;

import audio.Audio;
import graphics.Font;
import graphics.Graphics;
import graphics.Shape;
import graphics.Sprite;
import graphics.Text;
import world.control.Global;
import world.control.Hud;
import world.control.Shake;
import world.control.Sound;
import world.gameplay.Upblock;
import world.player.Player;
import main.Calc;
import main.Entity;
import main.Scene;

public class Cloud extends Entity{
	public Shake[] s;
	public Upblock[] u = new Upblock[2];
	
	public int event;
	public int gone = 0, paid, max;
	
	public boolean show = false;
	
	private final Sprite cloud = Sprite.get("sKissCloudBack"), front = Sprite.get("sKissCloudFront"), meter = Sprite.get("sKissCloudMeter"), xx = Sprite.get("sKissCloudX");
	
	private int xAmt;
	private double xDist;
	
	private double img;
	
	private final Font font = Global.FONT;
	
	public Cloud(double x, double y, int e, int m) {
		super(x, y);
		setDepth(25);
		setCollisionGroup(Global.DEACTIVATEME, Global.INTERACTABLE, Global.DINGUS);
		
		event = e;
		paid = Global.event[e];
		max = m * 2;
		
		xAmt = max / 64;
		s = new Shake[4 + xAmt];
		xDist = (double)m / (double)xAmt;
		
		for (int i=0; i<s.length; i++)
			s[i] = new Shake();
		
		mask = Global.sBLOCK.mask;
		orientation = Sprite.NORTH;
		xscale = 1.5 ;
		
		u[0] = new Upblock(x - 24, y);
		u[1] = new Upblock(x - 8, y);
		
		img = 0;
		
		Audio.fade("sCloud", 0, 0);
		Audio.get("sCloud").setGain(0);
		Audio.get("sCloud").setPitch(.4);
		Audio.get("sCloud").setLooping(true);
		Sound.play("sCloud");
	}
	
	public void up(){
		Audio.get("sCloud").setGain(.7);
		Audio.get("sCloud").setPitch(.4 + (double)(((double)gone / (double)max) * .6));
		Audio.fade("sCloud", 0, .007);
		
		y -= .5;
		Player.me.y = y - 16;
		Player.me.noGrav = true; //terrible. shit terrible
		Player.me.vspeed = 0;
		
		Player.me.animation = 1;
		Player.me.la = 1;
		
		if (Player.me.hspeed == 0)
			Player.me.animation = 0;
		
		Player.me.cameOffWallJump = false;
		Player.me.jumpBowKill();
		Player.me.wallSlide = false;
		
		gone += 1;
	}
	
	public void roomDestroy(){
		Audio.get("sCloud").stop();
		super.roomDestroy();
	}
	
	public void step(){
		for (int i=0; i<2; i++){
			u[i].y = y;
			u[i].step();
		}
		
		show = (Scene.collision(this, x, y - 1, Global.PLAYER) && Scene.collision(Player.me, Player.me.x, Player.me.y + 1, Global.SOLIDBLOCK) && Player.me.vspeed == 0 && Player.me.y < y - 15);
		if (show){
			if (paid < max){
				Hud.showMoney = true;
				if (Hud.timeMoney <= 5)
					Hud.timeMoney = 5;
			}
			
			if (gone < paid)
				up();
			else if (gone == paid && paid < max && Global.money > 0){
				Global.money -= 1;
				paid += 1;
				Global.event[event] = paid;
				
				new CloudSpend(x + 64, y);
				
				up();
			}
		}
		
		for (int i=0; i<2; i++)
			u[i].y = y;
		
		img += 1.0 / 6.0;
		while (img >= 3)
			img -= 3;
	}
	
	public void render(){
		//x
		for (int i=0; i<=xAmt; i++)
			xx.render(0, Sprite.CENTERED, x + s[3 + i].x + Calc.rangedRandom(.05), ystart - (i * xDist) + s[3 + i].y + Calc.rangedRandom(.05), 1, 1, 0, 1, 1, 1, 1);
		
		//cloud
		cloud.render(img, Sprite.CENTERED, x + s[0].x, y + s[0].y, 1, 1, 0, 1, 1, 1, 1);
	}
	
	public void text(double mX){
		Text.idiot = false;
		Text.setFont(font);
		Text.orientation = Text.CENTERED;
		Text.drawTextExt(x + mX + s[2].x + .25, y + s[2].y + .25, ""+(max - paid), 1 / 3.0, 1 / 3.0, 0);
		Text.idiot = true;
	}
	
	public void fgRender(){
		front.render(img, Sprite.CENTERED, x + s[1].x, y + s[1].y, 1, 1, 0, 1, 1, 1, 1);
	}
	
	public void frontRender(){
		//meter
				final double mX = 64;
				if (show){
					Graphics.setColor("FAFF58");
					Shape.drawCircle(x + mX + s[2].x, y + s[2].y, 16, 16);
					
					Graphics.setColor("FC9FFF");
					text(mX);
					
					meter.render((paid / (double)max) * 8, Sprite.CENTERED, x + mX + s[2].x, y + s[2].y, 1, 1, 0, 1, "FC9FFF");
						
						/////////////////////////////////////////////////////////////////
						//we are doing a stencil
						Scene.gl.glEnable(GL2.GL_ALPHA_TEST);
						
						Scene.gl.glClearStencil(0);
						Scene.gl.glClear( GL2.GL_STENCIL_BUFFER_BIT );
						Scene.gl.glEnable( GL2.GL_STENCIL_TEST );
						
						Scene.gl.glStencilFunc( GL2.GL_ALWAYS, 1, 0xFF );
						Scene.gl.glStencilOp( GL2.GL_KEEP, GL2.GL_KEEP, GL2.GL_REPLACE );
						
						//cutting a hole
						meter.render((paid / (double)max) * 8, Sprite.CENTERED, x + mX + s[2].x, y + s[2].y, 1, 1, 0, 1, "FC9FFF");
						
						Scene.gl.glStencilFunc( GL2.GL_EQUAL, 1, 0xFF );
						Scene.gl.glStencilOp( GL2.GL_KEEP, GL2.GL_KEEP, GL2.GL_KEEP );
						
						Scene.gl.glDisable(GL2.GL_ALPHA_TEST);
						
						//in essence, spray paint the metaphorical board with the holes
						Graphics.setColor("FAFF58");
						text(mX);
						
						Scene.gl.glDisable( GL2.GL_STENCIL_TEST );
						/////////////////////////////////////////////////////////////////
				}
	}

}
