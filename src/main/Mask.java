package main;

import java.awt.Point;

import graphics.Sprite;

public class Mask {
	public double width, height;
	public Point originOffset;
	public boolean generated;
	
	/**create a mask from a sprite*/
	public Mask(Sprite sprite)
	{
		width=sprite.imageWidth;
		height=sprite.imageHeight;
		originOffset = sprite.originOffset;
		generated = false;
	}
	
	/**create a mask from width, height, originX, and originY (might be totally broken! who knows!)*/
	public Mask(int w, int h, int x, int y)
	{
		width=w;
		height=h;
		originOffset = new Point(x, y);
		generated = true;
	}
	
	public boolean collidesWith(Entity boy, double x1, double y1, Entity girl){
		//Compute center of boy
		Point o1 = getOrigin(boy.orientation);
		o1.x*=-boy.xscale;
		o1.y*=-boy.yscale;
		x1+=o1.x;
		y1+=o1.y;
		x1+=boy.xscale*width/2;
		y1+=boy.yscale*height/2;
		//Compute center of girl
		double x2 = girl.x;
		double y2 = girl.y;
		
		Point o2 = girl.mask.getOrigin(girl.orientation);
		o2.x*=-girl.xscale;
		o2.y*=-girl.yscale;
		x2+=o2.x;
		y2+=o2.y;
		x2+=girl.xscale*girl.mask.width/2;
		y2+=girl.yscale*girl.mask.height/2;
		double dx,dy;
		dx = Math.abs(x2-x1);
		dy = Math.abs(y2-y1);
		return (dx<width/2*Math.abs(boy.xscale)+girl.mask.width/2*Math.abs(girl.xscale) && 
				dy<height/2*Math.abs(boy.yscale)+girl.mask.height/2*Math.abs(girl.yscale));
	}

	public Point getOrigin(int thisOrientation) {
		if (!generated){
			double x,y;
			if(thisOrientation==Sprite.CENTERED){
				x=0.5;
				y=0.5;
			}
			else{
				if (thisOrientation<=1 || thisOrientation==7)
					x=1;
				else if (thisOrientation>=3 && thisOrientation<=5)
					x=0;
				else
					x=0.5;
				if (thisOrientation>=1 && thisOrientation<=3)
					y=0;
				else if (thisOrientation>=5)
					y=1;
				else
					y=0.5;
			}
			x*=width;
			y*=height;
			x+=originOffset.x;
			y+=originOffset.y;
			return new Point((int)x,(int)y);
		}
		
		return new Point(originOffset.x, originOffset.y);
	}

}
