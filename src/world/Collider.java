package world;

import main.Entity;
import main.Mask;
import main.Scene;

public class Collider extends Entity{
	public Entity parent;

	/**this class can be used for extra collisions for things, in case you need more than one mask for whatever reason
	 * x: where to create the mask
	 * y: where to create the mask
	 * w: the width of the mask
	 * h: height of the mask
	 * p: parent of mask, i.e. who is responsible for the mask*/
	public Collider(double x, double y, double w, double h, Entity p) {
		super(x, y);
		setMask(w, h);
		
		visible = false;
		parent = p;
	}
	
	public void step(){
		if (parent.isDestroyed())
			destroy();
	}
	
	public void setPos(double ax, double ay){
		x = ax;
		y = ay;
	}
	
	public void setMask(double w, double h){
		mask = new Mask((int)w,(int)h,(int)(w/2),(int)(h/2));
	}
	
	public boolean collision(int... groups){
		if (parent.stepActive && !parent.isDestroyed())
			return Scene.collision(this, x, y, groups);
		
		return false;
	}

}
