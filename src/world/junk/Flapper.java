package world.junk;

import graphics.Sprite;

import java.util.ArrayList;

import javax.media.opengl.GL2;

import world.control.Global;
import world.control.Shake;
import world.control.Sound;
import world.control.SoundLoader;
import world.control.SpriteLoader;

import main.Calc;
import main.Entity;
import main.Scene;

public class Flapper extends Entity{
	private ArrayList<FlapperGhost> ghosts = new ArrayList<FlapperGhost>();
	private Shake s = new Shake(.3);
	
	private boolean faceVisible = true, go = false;
	private double fadeY = 80;

	public Flapper(double x, double y) {
		super(x, y);
		new SpriteLoader("sFlapper_4", "sFlapperFader");
		new SoundLoader(false, "sFlapperIn", "sFlapperFlap", "sFlapperDeath");
		sprite = Sprite.get("sFlapper");
		setCollisionGroup(Global.DEACTIVATEME, Global.PLAYER);
		mask = Global.sBLOCK.mask;
		
		if (Global.heartGot[58])
			faceVisible = false;
		else
			Sound.playPitched("sFlapperIn");
		
		alarmInitialize(1);
	}
	
	public void updateGhosts(){
		if (faceVisible)
			ghosts.add(new FlapperGhost(x, y));
		
		for (int i=0; i<ghosts.size(); i++)
			ghosts.get(i).update();
		for (int i=0; i<ghosts.size(); i++){
			if (ghosts.get(i).size <= 0){
				ghosts.remove(i);
				i--;
			}
		}
	}
	
	public void alarmEvent(int i){
		if (ghosts.size() == 0 && !Global.heartGot[58]){
			Sound.playPitched("sFlapperIn");
			faceVisible = true;
		}
		else
			alarm[i] = 1;
	}
	
	public void death(){
		Sound.playPitched("sFlapperDeath", .1);
		Global.squareParticle(x, y, 6, "0095EF", 8);
		Global.squareParticle(x, y, 7, "68E3FF", 5);
		hspeed = 0;
		vspeed = 0;
		go = false;
		faceVisible = false;
		fadeY = 80;
		x = xstart;
		y = ystart;
		angle = 0;
		alarm[0] = 30;
	}
	
	public void flap(){
		if (faceVisible && go){
			y -= 1;
			Sound.playPitched("sFlapperFlap", .1);
			vspeed = -8;
		}
	}
	
	public void step(){
		final double dir = getDirection();
		
		if (faceVisible){
			fadeY = Calc.approach(fadeY, 0, 12);
			fadeY -= 1;
			if (fadeY <= 16){
				fadeY = 16;
				if (!go){
					hspeed = 2.8;
					go = true;
					flap();
				}
			}
		}
		else
			fadeY = 80;
		
		for (int i=0; i<6; i++){
			x += Calc.dirX(getSpeed() / 6.0, dir);
			y += Calc.dirY(getSpeed() / 6.0, dir);
			updateGhosts();
		}
		
		if (go){
			vspeed += .8;
			angle = Calc.approach(angle, dir, 5.0);
		}
		
		if (faceVisible && go && Scene.collision(this, x, y, Global.BLOCK))
			death();
		
		alarmStep();
	}
	
	public void render(){
		if (!go && faceVisible){
			Scene.gl.glEnable(GL2.GL_ALPHA_TEST);
			
			Scene.gl.glClearStencil(0);
			Scene.gl.glClear( GL2.GL_STENCIL_BUFFER_BIT );
			Scene.gl.glEnable( GL2.GL_STENCIL_TEST );
			
			Scene.gl.glStencilFunc( GL2.GL_ALWAYS, 1, 0xFF );
			Scene.gl.glStencilOp( GL2.GL_KEEP, GL2.GL_KEEP, GL2.GL_REPLACE );
			
			//cut holes in a metaphorical board
			Sprite.get("sFlapperFader").render(0, Sprite.SOUTH, x + s.x, y + fadeY + s.y - 2.1, 1, 1, 0, 1.0 / 256.0, 1, 1, 1);
			Sprite.get("sFlapperFader").render(0, Sprite.SOUTH, x + s.x + 2.1, y + fadeY + s.y, 1, 1, 0, 1.0 / 256.0, 1, 1, 1);
			Sprite.get("sFlapperFader").render(0, Sprite.SOUTH, x + s.x - 2.1, y + fadeY + s.y, 1, 1, 0, 1.0 / 256.0, 1, 1, 1);
			
			Scene.gl.glStencilFunc( GL2.GL_EQUAL, 1, 0xFF );
			Scene.gl.glStencilOp( GL2.GL_KEEP, GL2.GL_KEEP, GL2.GL_KEEP );
			
			Scene.gl.glDisable(GL2.GL_ALPHA_TEST);
			
			//in essence, spray paint the metaphorical board with the holes
			///////////////////////////////////////////////////////////////
			sprite.render(0, Sprite.CENTERED, x + s.x, y + s.y, 1, 1, angle, 1, 1, 1, 1);
			///////////////////////////////////////////////////////////////
			Scene.gl.glDisable( GL2.GL_STENCIL_TEST );
			
			Scene.gl.glEnable(GL2.GL_ALPHA_TEST);
			
			Scene.gl.glClearStencil(0);
			Scene.gl.glClear( GL2.GL_STENCIL_BUFFER_BIT );
			Scene.gl.glEnable( GL2.GL_STENCIL_TEST );
			
			Scene.gl.glStencilFunc( GL2.GL_ALWAYS, 1, 0xFF );
			Scene.gl.glStencilOp( GL2.GL_KEEP, GL2.GL_KEEP, GL2.GL_REPLACE );
			
			//cut holes in a metaphorical board
			Sprite.get("sFlapperFader").render(0, Sprite.SOUTH, x + s.x, y + fadeY + s.y, 1, 1, 0, 1.0 / 256.0, 1, 1, 1);
			
			Scene.gl.glStencilFunc( GL2.GL_EQUAL, 1, 0xFF );
			Scene.gl.glStencilOp( GL2.GL_KEEP, GL2.GL_KEEP, GL2.GL_KEEP );
			
			Scene.gl.glDisable(GL2.GL_ALPHA_TEST);
			
			//in essence, spray paint the metaphorical board with the holes
			///////////////////////////////////////////////////////////////
			sprite.render(1, Sprite.CENTERED, x + s.x, y + s.y, 1, 1, angle, 1, 1, 1, 1);
			///////////////////////////////////////////////////////////////
			Scene.gl.glDisable( GL2.GL_STENCIL_TEST );
		}
		else{
			//draw your back
			for (int i=0; i<ghosts.size(); i++){
				FlapperGhost g = ghosts.get(i);
				final double size = g.size + ((1 - g.size) * .1);
				draw(g.x + s.x, g.y + s.y, size);
			}
			if (faceVisible)
				sprite.render(0, Sprite.CENTERED, x + s.x, y + s.y, 1, 1, angle, 1, 1, 1, 1);
			
			//draw your front
			for (int i=0; i<ghosts.size(); i++){
				FlapperGhost g = ghosts.get(i);
				sprite.render(3, Sprite.CENTERED, g.x + s.x, g.y + s.y, g.size, g.size, 0, 1, 1, 1, 1);
			}
			if (faceVisible)
				sprite.render(1, Sprite.CENTERED, x + s.x, y + s.y, 1, 1, angle, 1, 1, 1, 1);
		}
	}
	
	public void draw(double x, double y, double size){
		sprite.render(2, Sprite.CENTERED, x, y, size, size, 0, 1, 1, 1, 1);
	}

}
