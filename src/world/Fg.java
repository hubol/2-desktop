package world;

import java.util.ArrayList;

import javax.media.opengl.GL2;

import world.control.EV;
import world.control.Global;
import world.end.EndFlower;
import world.gameplay.AddBlockEvent;
import world.gameplay.CrackedBlock;
import world.gameplay.Spring;
import world.junk.AnalBead;
import world.junk.Cloud;
import world.junk.Glow;
import world.particles.HeartParticles;

import graphics.Sprite;
import main.Calc;
import main.Entity;
import main.Scene;

public class Fg extends Entity{
	public static Fg me;
	public boolean fgVis, ovVis;
	
	public static boolean touchSecret, hasSecret;
	public double secretAlpha;
	
	public static boolean shake;
	public static double timer,intensity;
	
	public int shakeTimer;
	public double addX;
	public double shakeMultiplier;
	
	public boolean fade = false;

	public Fg(double x, double y) {
		super(x, y);
		me = this;
		persistent=true;
		intensity=0;
		timer=3;
		fgVis=false;
		ovVis=false;
		shake=true;
		setDepth(Integer.MIN_VALUE+20);
		shake();
		
		shakeTimer = 0;
		
		touchSecret = false;
		secretAlpha = 1;
		
		shakeMultiplier = 1;
	}
	
	@Override
	public void destroy(){
		if (me==this)
			me = null;
		super.destroy();
	}
	
	public void step(){
		timer-=1;
		if (timer<=0)
			shake();
		
		shakeTimer -= 1;
		if (shakeTimer > 0){
			if (addX == 0)
				addX = 2 * shakeMultiplier;
			else
				addX *= -1;
		}
		else
			addX = 0;
		
		//stupid
		boolean fjak = (Global.room(12, 6));
		
		if (touchSecret){
			secretAlpha -= .0675;
			if (fjak)
				secretAlpha = 0;
		}
		else{
			secretAlpha += .1;
			if (fjak)
				secretAlpha = 1;
		}
		secretAlpha = Math.min(1, Math.max(0, secretAlpha));
		
		if (Global.room(10, 7)){
			if (secretAlpha == 0)
				Global.event[EV.PLANTDOOR] = 1;
			if (Global.event[EV.PLANTDOOR] == 1)
				secretAlpha = 0;
		}
		
		if (fade && alpha > 0)
			alpha -= 1.0 / 240.0;
	}
	
	public static void setIntensity(double in){
		intensity=in;
	}
	
	public void shake(){
		shake=!shake;
		timer=1+Math.ceil(Calc.random(60));
		if (shake){
			timer/=2;
			timer+=1;
		}
	}
	
	public void delete(){
		if (Sprite.exists("fg"+Global.roomX+","+Global.roomY))
			Sprite.get("fg"+Global.roomX+","+Global.roomY).remove();
	}
	
	public double getRand(){
		double f = intensity;
		if (!shake)
			f/=16;
		
		return Calc.rangedRandom(f * 4);
	}
	
	public void render(){
		if (Global.mainActive){
			//YES THIS SHOULD DEFINITELY BE HERE NO THIS IS NOT THE RESULT OF LAZINESS
			if (Global. room(5, 5)){
				ArrayList<AnalBead> nt = Scene.getEntityList(AnalBead.class);
				for (int i=0; i<nt.size(); i++)
					nt.get(i).drawBead(1);
				
				Scene.gl.glEnable(GL2.GL_ALPHA_TEST);
				
				Scene.gl.glClearStencil(0);
				Scene.gl.glClear( GL2.GL_STENCIL_BUFFER_BIT );
				Scene.gl.glEnable( GL2.GL_STENCIL_TEST );
				
				Scene.gl.glStencilFunc( GL2.GL_ALWAYS, 1, 0xFF );
				Scene.gl.glStencilOp( GL2.GL_KEEP, GL2.GL_KEEP, GL2.GL_REPLACE );
				
				//cut holes in a metaphorical board
				for (int i=0; i<nt.size(); i++)
					nt.get(i).drawBead(0);
				
				Scene.gl.glStencilFunc( GL2.GL_EQUAL, 1, 0xFF );
				Scene.gl.glStencilOp( GL2.GL_KEEP, GL2.GL_KEEP, GL2.GL_KEEP );
				 
				Scene.gl.glDisable(GL2.GL_ALPHA_TEST);
				
				//in essence, spray paint the metaphorical board with the holes
				Sprite.get("sAnalTexture").render(0, Sprite.CENTERED, Global.rain.x + 320, Global.rain.y + 240, 1, 1, 0, 1, 1, 1, 1);
				
				Scene.gl.glDisable( GL2.GL_STENCIL_TEST );
			}
			
			ArrayList<Cloud> jist = Scene.getEntityList(Cloud.class);
			for (int i=0; i<jist.size(); i++)
				jist.get(i).fgRender();
			
			ArrayList<Spring> nist = Scene.getEntityList(Spring.class);
			for (int i=0; i<nist.size(); i++)
				nist.get(i).fuck();
			
			if (fgVis)
				fgRender(Sprite.get("fg"+Global.roomX+","+Global.roomY), alpha);
			
			if (hasSecret)
				fgRender(Sprite.get("fg"+Global.roomX+","+Global.roomY+"s"), secretAlpha);
			
			//lets draw the cracked block shit
			ArrayList<CrackedBlock> pist= Scene.getEntityList(CrackedBlock.class);
			for (int i=0; i<pist.size(); i++)
				pist.get(i).draw(getRand()+addX,getRand());
			
			//lets draw the add block shit
			ArrayList<AddBlockEvent> cyst= Scene.getEntityList(AddBlockEvent.class);
			for (int i=0; i<cyst.size(); i++)
				cyst.get(i).draw();
			
			//lets draw the glow block shit
			ArrayList<Glow> jizz= Scene.getEntityList(Glow.class);
			for (int i=0; i<jizz.size(); i++)
				jizz.get(i).draw();
			
			//cloud meters
			for (int i=0; i<jist.size(); i++)
				jist.get(i).frontRender();
			
			ArrayList<EndFlower> wtf = Scene.getEntityList(EndFlower.class);
			for (int i=0; i<wtf.size(); i++)
				wtf.get(i).brender();
			
			//lets draw the heart particles
			ArrayList<HeartParticles> list= Scene.getEntityList(HeartParticles.class);
			for (int i=0; i<list.size(); i++)
				list.get(i).overRender();
				
			//if (fgVis)
			//	Sprite.get("fg"+Global.roomX+","+Global.roomY).render(0,Sprite.CENTERED, 320-.25+Calc.random(.5), 240-.25+Calc.random(.5), 1.001, 1.001, 0, 1, 1, 1, 1);
			
			//MOVED THE FOREGROUND OVERLAY STUFF TO THE OVERLAY CLASS IM SO SORRY THIS IS A MESS!!!!!!
			/*if (ovVis){
				BlendMode.SUBTRACT.set();
				Sprite.get("overlay"+Calc.getGridPosition(Global.roomX, Global.roomY, 16)).render(0,Sprite.NORTHWEST, 0, 0, 1+Calc.random(.001), 1+Calc.random(.001), 0, .2, 1, 1, 1);
				BlendMode.NORMAL.set();
			}*/
			
			touchSecret = false; //yep
		}
	}
	
	public void fgRender(Sprite s, double alp){
		int l = s.textures.length;
		//intensity stuff
		double f = intensity;
		if (!shake)
			f/=16;
		
		double mult=1.5;
		
		for (int i=0; i<l; i++)
			s.render(i,Sprite.CENTERED, ((642/l)*(.5+i))-(mult*f)+Calc.random(mult*f*2)+addX, 240-(mult*f)+Calc.random(mult*f*2), 1.01+Calc.random((mult/80)*f), 1.01+Calc.random((mult/80)*f), 0, alp, 1, 1, 1);
	
	}

}
