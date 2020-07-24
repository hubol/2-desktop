package world.junk;

import javax.media.opengl.GL2;

import world.Fg;
import world.Root;
import world.control.EV;
import world.control.Global;
import world.control.Shake;
import world.player.Player;
import graphics.Sprite;
import main.Calc;
import main.Entity;
import main.Scene;

public class Glow extends Entity{
	private Sprite me, out;
	private Shake s = new Shake(.4);
	
	public Glow(double x, double y) {
		super(x, y);
		
		final int i = (int)Math.ceil(Calc.random(6));
		Root.loadBackground("glow"+Global.roomX+","+Global.roomY+".png", i);
		Root.loadBackground("glow"+Global.roomX+","+Global.roomY+"OUT.png", i);
		me = Sprite.get("glow"+Global.roomX+","+Global.roomY);
		out = Sprite.get("glow"+Global.roomX+","+Global.roomY+"OUT");
		
		setCollisionGroup(Global.DEACTIVATEME);
	}
	
	public void step(){
		angle += 2.5;
		while (angle >= 360)
			angle -= 360;
	}
	
	public void render(){
		
	}
	
	public void draw(){
		if (Global.event[EV.BULB] > 0){
		//outline
			Scene.gl.glEnable(GL2.GL_ALPHA_TEST);
			
			Scene.gl.glClearStencil(0);
			Scene.gl.glClear( GL2.GL_STENCIL_BUFFER_BIT );
			Scene.gl.glEnable( GL2.GL_STENCIL_TEST );
			
			Scene.gl.glStencilFunc( GL2.GL_ALWAYS, 1, 0xFF );
			Scene.gl.glStencilOp( GL2.GL_KEEP, GL2.GL_KEEP, GL2.GL_REPLACE );
			
			//cut holes in a metaphorical board
			Sprite.get("sGlowOutline").render(0, Sprite.CENTERED, Player.me.x + s.x, Player.me.y + s.y, 1, 1, angle, .004, 1, 1, 1);
			
			Scene.gl.glStencilFunc( GL2.GL_EQUAL, 1, 0xFF );
			Scene.gl.glStencilOp( GL2.GL_KEEP, GL2.GL_KEEP, GL2.GL_KEEP );
			
			Scene.gl.glDisable(GL2.GL_ALPHA_TEST);
			
			//in essence, spray paint the metaphorical board with the holes
			Fg.me.fgRender(out, 1);
			
			Scene.gl.glDisable( GL2.GL_STENCIL_TEST );
		
		//body
			Scene.gl.glEnable(GL2.GL_ALPHA_TEST);
			
			Scene.gl.glClearStencil(0);
			Scene.gl.glClear( GL2.GL_STENCIL_BUFFER_BIT );
			Scene.gl.glEnable( GL2.GL_STENCIL_TEST );
			
			Scene.gl.glStencilFunc( GL2.GL_ALWAYS, 1, 0xFF );
			Scene.gl.glStencilOp( GL2.GL_KEEP, GL2.GL_KEEP, GL2.GL_REPLACE );
			
			//cut holes in a metaphorical board
			Sprite.get("sGlow").render(0, Sprite.CENTERED, Player.me.x + s.x, Player.me.y + s.y, 1, 1, angle, .004, 1, 1, 1);
			
			Scene.gl.glStencilFunc( GL2.GL_EQUAL, 1, 0xFF );
			Scene.gl.glStencilOp( GL2.GL_KEEP, GL2.GL_KEEP, GL2.GL_KEEP );
			
			Scene.gl.glDisable(GL2.GL_ALPHA_TEST);
			
			//in essence, spray paint the metaphorical board with the holes
			Fg.me.fgRender(me, 1);
			
			Scene.gl.glDisable( GL2.GL_STENCIL_TEST );
		}
	}

}
