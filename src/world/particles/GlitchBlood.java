package world.particles;

import world.control.Global;
import graphics.BlendMode;
import graphics.Sprite;
import main.Calc;
import main.Entity;

public class GlitchBlood extends Entity {
	public double life;

	public final static Sprite BLOOD = Sprite.get("sGlitchBlood");
	
	public GlitchBlood(double x, double y) {
		super(x, y);
		hspeed=-6+Calc.random(12);
		vspeed=-Calc.random(6);
		
		setDepth(-10+(int)Calc.random(20));
		
		sprite=BLOOD;
		orientation=-1+(int)Calc.random(9);
		angle=Calc.random(360);
		imageSingle=(int)Calc.random(4);
		imageSpeed=(Integer)Calc.choose(0,0,0,0,0,0,1)*Calc.random(1);
		xscale=.25+Calc.random(1);
		yscale=xscale;
		alpha=Math.min(1, .5+Calc.random(.7));
		
		life=1+Calc.random(15);
		
		setCollisionGroup(Global.DEACTIVATEME);
	}
	
	public void step(){
		vspeed+=.8;
		super.step();
		
		life-=1;
		if (life<=0)
			super.destroy();
	}
	
	public void render(){
		int arf=(Integer)Calc.choose(0,1,2,3);
		if (arf==1)
			BlendMode.ADD.set();
		else if (arf==2)
			BlendMode.SUBTRACT.set();
		else if (arf==3)
			BlendMode.MULTIPLY.set();
		super.render();
		BlendMode.NORMAL.set();
	}

}
