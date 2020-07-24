package world.particles;

import world.control.Global;
import graphics.BlendMode;
import graphics.Sprite;
import main.Calc;
import main.Entity;

public class JumpDust extends Entity{
	public double add, img, imgS;
	
	public final static Sprite DUST = Sprite.get("sDust");

	public JumpDust(double x, double y) {
		super(x, y);
		sprite=DUST;
		angle=Calc.random(360);
		
		img=0;
		imgS=.2+Calc.random(.5);
		
		add=-3+Calc.random(6);
		
		xscale=.9+Calc.random(.3);
		yscale=xscale;
		
		setDepth(-8+(int)Calc.random(16));
		
		setSpeed(.5+Calc.random(2.5));
		setDirection(Calc.random(180));

		setCollisionGroup(Global.DEACTIVATEME);
	}
	
	public void step(){
		angle+=add;
		img+=imgS;
		if (img>=6)
			super.destroy();
		super.step();
	}
	
	public void render(){
		int a=(Integer)Calc.choose(0,0,0,0,0,0,1,2);
		if (a==1)
			BlendMode.SUBTRACT.set();
		else if (a==2)
			BlendMode.ADD.set();
		sprite.render((int)img,Sprite.CENTERED,x,y,xscale,xscale,angle,1,1,1,1);
		BlendMode.NORMAL.set();
	}

}
