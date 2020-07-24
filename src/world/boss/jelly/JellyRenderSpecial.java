package world.boss.jelly;

import java.util.ArrayList;

import main.Calc;
import main.Entity;
import main.Scene;

public class JellyRenderSpecial extends Entity{
	public static JellyRenderSpecial me;
	
	public int glitchAmount = 22;
	public double glitchTimer;
	public boolean[] glitchActive;
	
	public double intensity = 1;
	
	/**this class exists to draw the back of the jelly guy! you only need one of these during the jelly fight ever!*/
	public JellyRenderSpecial(double x, double y) {
		super(x, y);
		setDepth(Integer.MIN_VALUE + 9);
		
		me = this;
		
		glitchActive = new boolean[glitchAmount];
		glitchReset();
	}
	
	public void step(){
		glitchTimer -= 1;
		if (glitchTimer <= 0)
			glitchReset();
	}
	
	public void glitchReset(){
		for (int i=0; i<glitchAmount; i++)
			glitchActive[i] = false;
		
		int amount = (Integer)Calc.choose(0,0,1,1,1,1,1,2,2,2,3,3,4,5) + (int)(intensity/5);
		for (int i=0; i<amount; i++)
			glitchActive[(int)Calc.random(glitchAmount)] = true;
		
		glitchTimer = Calc.random(60/intensity);
	}
	
	public void destroy(){
		me = null;
		super.destroy();
	}
	
	public void roomDestroy(){
		destroy();
	}
	
	public void render(){
		double addX=0, addY=0;
		xscale = 1;
		yscale = 1;
		angle = 0;
		alpha = 1;
		
		int draws = 1;
		
		if (glitchActive[8])
			draws = (int)Calc.random(3) + 1;
		
		for (int p=0; p<draws; p++){
				
			if (glitchActive[0])
				addX += Calc.rangedRandom(2 * intensity);
			if (glitchActive[1])
				addY += Calc.rangedRandom(2 * intensity);
			if (glitchActive[2]){
				addY += Calc.rangedRandom(2 * intensity);
				addX += Calc.rangedRandom(2 * intensity);
			}
			if (glitchActive[3])
				angle += Calc.rangedRandom(4 * intensity);
			if (glitchActive[4])
				xscale += Calc.rangedRandom(.2 * intensity);
			if (glitchActive[5])
				yscale += Calc.rangedRandom(.2 * intensity);
			if (glitchActive[8]){
				alpha = .6 + Calc.random(.4);
				addX += Calc.rangedRandom(6 * intensity);
				addY += Calc.rangedRandom(6 * intensity);
				angle += Calc.rangedRandom(12 * intensity);
				glitchTimer -= Calc.random(6);
			}
			if (glitchActive[11]){
				addX += Calc.rangedRandom(16 * intensity);
				addY += Calc.rangedRandom(16 * intensity);
				glitchTimer -= Calc.random(6);
			}
			if (glitchActive[9])
				alpha = .3 + Calc.random(.3 * intensity);
			if (glitchActive[10]){
				addX += Calc.rangedRandom(.7);
				addY += Calc.rangedRandom(.7);
			}
			if (glitchActive[12])
				addX += Calc.rangedRandom(1);
			if (glitchActive[13])
				addY += Calc.rangedRandom(1);
			
			//nonenemy
			ArrayList<JellyBubble> list = Scene.getEntityList(JellyBubble.class);
			for (int i=0; i<list.size(); i++){
				JellyBubble me = list.get(i);
				if (me.visible && me.vis){
					double xx = me.x, yy = me.y, xs = me.xscale, ys = me.yscale, an = me.angle, alp = me.alpha;
					me.x += addX;
					me.y += addY;
					me.xscale *= xscale;
					me.yscale *= yscale;
					me.angle += angle;
					me. alpha *= alpha;
					me.renderBack();
					me.x = xx; me. y = yy; me.xscale = xs; me.yscale = ys; me.angle = an; me.alpha = alp;
				}
			}
			
			//non enemy
			for (int i=0; i<list.size(); i++){
				JellyBubble me = list.get(i);
				if (me.visible && me.vis){
					double xx = me.x, yy = me.y, xs = me.xscale, ys = me.yscale, an = me.angle, alp = me.alpha;
					me.x += addX;
					me.y += addY;
					me.xscale *= xscale;
					me.yscale *= yscale;
					me.angle += angle;
					me. alpha *= alpha;
					me.renderFront();
					me.x = xx; me. y = yy; me.xscale = xs; me.yscale = ys; me.angle = an; me.alpha = alp;
				}
			}
		}
	}

}
