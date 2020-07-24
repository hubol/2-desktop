package world.boss;

import world.control.Global;
import world.control.Shake;
import graphics.Sprite;
import main.Calc;
import main.Entity;

public class BossBlock extends Entity {
	public boolean fadeIn;
	public Shake s;

	public BossBlock(double x, double y) {
		super(x, y);
		sprite=Sprite.get("sBossBlock");
		orientation=Sprite.NORTHWEST;
		imageSingle=(Integer)Calc.choose(0,1,2,3);
		imageSpeed=0;
		alpha=0;
		fadeIn=false;
		
		s = new Shake(.5);
		
		setDepth( 8 );
		
		setCollisionGroup(Global.DEACTIVATEME, Global.TEMPSOLID);
	}
	
	public void step(){
		if (fadeIn)
			alpha+=.1;
		else
			alpha-=.1;
		alpha=Math.min(1, Math.max(0,alpha));
		
		if (alpha>0)
			setCollisionGroup(Global.SOLIDBLOCK,Global.BOSSBLOCK);
		else
			setCollisionGroup(0);
		addCollisionGroup(Global.DEACTIVATEME);
		addCollisionGroup(Global.TEMPSOLID);
	}
	
	public void destroy(){
		s.destroy();
		super.destroy();
	}
	
	public void render(){
		double ax=x, ay=y,a=angle;
		x+=s.x;
		y+=s.y;
		angle+=(s.x + s.y) / 2.0;
		super.render();
		x=ax;
		y=ay;
		angle=a;
	}

}
