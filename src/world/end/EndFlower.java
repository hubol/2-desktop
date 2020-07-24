package world.end;

import graphics.Sprite;
import world.control.Global;
import world.control.Shake;
import world.control.SpriteLoader;
import main.Calc;
import main.Entity;

public class EndFlower extends Entity{
	public boolean isHeart = true;
	public int id = -1;
	
	private Shake s = new Shake(.3);
	private String petal = "FF2667", home = "DAFF26";
	
	private boolean growStem = false, growHome = false, growPetal = false;
	private double stem = 0, homeScale = 0, petalScale = 0;
	private double scale = 1;
	
	private Sprite sprStem, sprFlower, sprLeaves;
	
	private final double global = .675;
	public boolean fade = false;

	public EndFlower(double x, double y, int id) {
		super(x, y);
		this.id = id;
		angle = id * 5;
		
		yscale = .8;
		setDepth(Integer.MIN_VALUE+20);
		
		if (id % 2 == 0)
			scale = -1;
		
		if (!Sprite.exists("sEndStem"))
			new SpriteLoader("sEndStem_8", "sEndFlower_4", "sEndLeaves");
		sprStem = Sprite.get("sEndStem");
		sprFlower = Sprite.get("sEndFlower");
		sprLeaves = Sprite.get("sEndLeaves");
	}
	
	public EndFlower(double x, double y, int id, String petal, String home){
		this(x, y, id);
		this.petal = petal;
		this.home = home;
		isHeart = false;
	}
	
	public boolean grow(){
		boolean grow = (isHeart && Global.heartGot[id]) || (!isHeart && Global.event[id] > 0);
		if (grow)
			growStem = true;
		
		return grow;
	}
	
	public void step(){
		if (growStem){
			stem = Math.min(7.9, (stem + .5) * 1.5);
			if (stem >= 7){
				growStem = false;
				growHome = true;
			}
		}
		
		if (growPetal)
			petalScale = Calc.approach(petalScale, 1, 3);
		
		if (growHome){
			homeScale = Calc.approach(homeScale, 1, 2);
			if (homeScale > .8)
				growPetal = true;
		}
		
		if (fade && alpha > 0)
			alpha -= 1.0 / (400.0 + id);
	}
	
	public void render(){
		
	}
	
	public void brender(){
		if (stem > 0){
			sprStem.render(stem, Sprite.SOUTH, x + s.x, y + s.y + (3 * global), scale * global, yscale * global, 0, alpha, 1, 1, 1);
			sprLeaves.render(0, Sprite.SOUTH, x+ s.x, y + s.y + 3, scale * .2, .2 * (stem  / 7.0), 0, alpha, 1, 1, 1); 
			sprFlower.render(3, Sprite.CENTERED, x + (3 * scale * global) + s.x, y - (39 * yscale * global) + s.y, petalScale * global, petalScale * global, angle, alpha, petal);
			sprFlower.render(2, Sprite.CENTERED, x + (3 * scale * global) + s.x, y - (39 * yscale * global) + s.y, petalScale * global, petalScale * global, angle, alpha, 1, 1, 1);
			sprFlower.render(1, Sprite.CENTERED, x + (3 * scale * global) + s.x, y - (39 * yscale * global) + s.y, homeScale * global, homeScale * global, 0, alpha, home);
			sprFlower.render(0, Sprite.CENTERED, x + (3 * scale * global) + s.x, y - (39 * yscale * global) + s.y, homeScale * global, homeScale * global, 0, alpha, 1, 1, 1);
		}
	}

}
