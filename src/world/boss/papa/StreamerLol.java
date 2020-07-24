package world.boss.papa;

import graphics.Graphics;
import graphics.Sprite;

import java.util.ArrayList;

import world.control.Global;
import world.control.Shake;
import main.Calc;
import main.Entity;

public class StreamerLol extends Entity{
	private String color;
	private int life;
	
	private double addDirection, dAddDir;
	private double addSpeed, dAddSpd;
	
	private Shake s = new Shake(.3);
	
	private ArrayList<StreamerGhost> ghosts = new ArrayList<StreamerGhost>();
	private Sprite circle = Sprite.get("sCircle");
	
	private PapaGhost dad;
	
	private double relativeDist, relativeDir, lastAngle;
	private double lastX, lastY;
	
	private double size = 7 + Calc.random(5.5);
	
	private double startSqueeze;

	public StreamerLol(PapaGhost p, double dist, double dir, double ang, double x, double y, double dirr, String c, int life) {
		super(x, y);
		color = c;
		this.life = life;
		
		relativeDist = dist;
		relativeDir = dir;
		lastAngle = ang;
		
		startSqueeze = p.showSqueeze;
		
		dad = p;
		
		lastX = x;
		lastY = y;
		
		setDepth(-25);
		setCollisionGroup(Global.DEACTIVATEME);
		setDirSpeed(dirr, Calc.random(4));
		addDirection = Calc.rangedRandom(30);
		addSpeed = Calc.random(.8);
		
		dAddDir = Calc.rangedRandom(1);
		dAddSpd = Calc.random(.05);
	}
	
	public void destroy(){
		s.destroy();
		ghosts.clear();
		super.destroy();
	}
	
	public void updateGhosts(){
		if (life > 0)
			ghosts.add(new StreamerGhost(x, y));
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
		final double dir = getDirection() + addDirection, spd = getSpeed() + addSpeed;
		setDirSpeed(dir, spd);
		addSpeed += dAddSpd;
		addDirection += dAddDir;
		
		final double sspd = spd / 6.0;
		final double newDist = (relativeDist / startSqueeze) * dad.showSqueeze;
		
		final double thisDir = (dad.angle - lastAngle) + relativeDir;
		final double thisX = dad.x + Calc.dirX(newDist, thisDir), thisY = dad.y + Calc.dirY(newDist, thisDir);
		final double difX = thisX - lastX, difY = thisY - lastY;
		lastX = thisX;
		lastY = thisY;
		
		x += difX;
		y += difY;
		for (int i=0; i<ghosts.size(); i++){
			StreamerGhost g = ghosts.get(i);
			g.x += difX;
			g.y += difY;
		}
		for (int i=0; i<6; i++){
			x += Calc.dirX(sspd, dir);
			y += Calc.dirY(sspd, dir);
			updateGhosts();
		}
		
		life--;
	}
	
	public void render(){
		Graphics.setAlpha(1);
		Graphics.setColor("0078F7");
		trail(s.x, s.y, size, 5);
		Graphics.setColor(color);
		trail(s.x, s.y, size, 0);
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
