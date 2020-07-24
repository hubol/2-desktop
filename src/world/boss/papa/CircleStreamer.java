package world.boss.papa;

import graphics.Graphics;
import graphics.Sprite;

import java.util.ArrayList;

import world.control.Global;
import world.control.Shake;
import main.Calc;
import main.Entity;

public class CircleStreamer extends Entity{
	private String color;
	private int life;
	
	private Shake s = new Shake(.3);
	
	private ArrayList<StupidGhost> ghosts = new ArrayList<StupidGhost>();
	private Sprite circle = Sprite.get("sCircle");
	
	private Papa dad;
	
	private double size = 16;
	
	private double dist, dir;

	public CircleStreamer(Papa p, double dist, double dir, String color) {
		super(0, 0);
		dad = p;
		this.dist = dist;
		this.dir = dir;
		this.color = color;
		
		x = Calc.dirX(dist, dir);
		y = Calc.dirY(dist, dir);
		
		life = 60;
		
		setDepth(-25);
		setCollisionGroup(Global.DEACTIVATEME);
	}
	
	public void calcPos(){
		
	}
	
	public void destroy(){
		s.destroy();
		ghosts.clear();
		super.destroy();
	}
	
	public void updateGhosts(){
		if (life > 0)
			ghosts.add(new StupidGhost(x, y));
		else if (ghosts.size() <= 0)
			destroy();
		
		for (int i=0; i<ghosts.size(); i++)
			ghosts.get(i).update();
		for (int i=0; i<ghosts.size(); i++){
			if (ghosts.get(i).size <= 0){
				ghosts.remove(i);
				i--;
			}
		}
	}
	
	public void step(){
		for (int i=0; i<6; i++){
			dir += 1;
			if (dir >= 360)
				dir -= 360;
			x = Calc.dirX(dist, dir);
			y = Calc.dirY(dist, dir);
			updateGhosts();
		}
		
		life--;
	}
	
	public void render(){
		Graphics.setAlpha(1);
		Graphics.setColor("0078F7");
		trail(dad.x + s.x, dad.y + s.y, size, 5);
		Graphics.setColor(color);
		trail(dad.x + s.x, dad.y + s.y, size, 0);
	}
	
	public void trail(double x, double y, double size, double force){
		for (int i=0; i<ghosts.size(); i++)
			ghostCircle(x, y, ghosts.get(i), size, force);
	}
	
	public void ghostCircle(double x, double y, SpikeGhost g, double size, double force){
		final double scale = g.size * (size / 640.0) + (force / 640.0);
		circle.render(0, Sprite.CENTERED, x + g.x, y + g.y, scale, scale, 0, 1, Graphics.color[0], Graphics.color[1], Graphics.color[2]);
	}

}
