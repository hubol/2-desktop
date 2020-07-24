package world.particles;

import world.control.Global;
import graphics.Sprite;
import main.Entity;

public class PieceParticle extends Entity{
	public double left, top;
	public double width, height;
	
	public double gravity;
	public int life;

	public PieceParticle(double x, double y, String sprite, double imageSingle, double imageSpeed, double left, double top, double width, double height, int depth, double direction, double speed, double gravity, int life) {
		super(x - (width / 2.0), y - (height / 2.0));
		this.sprite = Sprite.get(sprite);
		this.imageSingle = imageSingle;
		this.imageSpeed = imageSpeed;
		setDepth(depth);
		
		this.left = left;
		this.top = top;
		this.width = width;
		this.height = height;
		
		setDirSpeed(direction, speed);
		this.gravity = gravity;
		this.life = life;
		
		setCollisionGroup(Global.DEACTIVATEME);
	}

	public void step(){
		super.step();
		vspeed += gravity;
		life -= 1;
		
		if (y >= 520 || life <= 0)
			destroy();
	}
	
	public void render(){
		sprite.renderPart((int)imageSingle, Sprite.NORTHWEST, x, y, left, top, width, height, 1, 1, 0, 1, 1, 1, 1);
	}

}
