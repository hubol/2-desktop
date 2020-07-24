package world.boss.papa;

import java.util.ArrayList;

import world.Fg;
import world.control.Global;
import world.control.Sound;
import world.player.Bullet;
import world.player.Player;
import graphics.Graphics;
import graphics.Shape;
import graphics.Sprite;
import main.Calc;
import main.Scene;

public class PapaSpike extends PapaEntity{
	public boolean attached;
	public double at = 0;
	public int id = 0;
	private Papa dad;
	
	public Doint a, b, c;
	
	public double launchMult = 1.0, storedSpeed;
	private ArrayList<SpikeGhost> ghosts = new ArrayList<SpikeGhost>();
	
	private final Sprite circle = Sprite.get("sCircle");
	private final Sprite lazy = Sprite.get("sLazy");

	public PapaSpike(boolean already, int id, Papa father) {
		super(0, 0);
		this.id = id;
		angle = id * 45;
		dad = father;
		attached = true;
		
		a = new Doint(Calc.dirX(24, angle + 90), Calc.dirY(24, angle + 90));
		b = new Doint(Calc.dirX(24, angle), Calc.dirY(24, angle));
		c = new Doint(Calc.dirX(24, angle - 90), Calc.dirY(24, angle - 90));
		
		if (already)
			at = dad.apothem;
		else
			Sound.playPitched("sPapaSpikeIn");
		
		setDepth(-3); 
		calcPosition();
		
		setCollisionGroup(Global.DEACTIVATEME);
		alarmInitialize(2);
	}
	
	public void updateGhosts(){
		if (!attached)
			ghosts.add(new SpikeGhost(x, y));
		
		for (int i=0; i<ghosts.size(); i++)
			ghosts.get(i).update();
		for (int i=0; i<ghosts.size(); i++){
			if (ghosts.get(i).size <= 0){
				ghosts.remove(i);
				i--;
			}
		}
	}
	
	public void calcPosition(){
		if (attached){
			x = dad.x + Calc.dirX(at, angle);
			y = dad.y + Calc.dirY(at, angle);
		}
		else{
			final double dir = getDirection();
			
			for (int i=0; i<6; i++){
				x += Calc.dirX(getSpeed() / 6.0, dir);
				y += Calc.dirY(getSpeed() / 6.0, dir);
				updateGhosts();
			}
		}
	}
	
	public void alarmEvent(int i){
		if (i == 0 && alarm[1] > 0){
			Sound.playPitched("sPapaSpikeLaunchCount");
			launchMult = 1.6;
			alarm[0] = 8;
		}
		else if (i == 1){
			Fg.me.shakeTimer = 15;
			Sound.playPitched("sPapaSpikeLaunch", .01);
			launchMult = 1;
			attached = false;
			setDirSpeed(angle, storedSpeed);
			alarm[0] = -1;
		}
	}
	
	public void destroy(){
		ghosts.clear();
		dad.spikes.remove(this);
		super.destroy();
	}
	
	public void bigBreak(){
		Fg.me.shakeTimer = 6;
		new SpikeShard(x, y);
		new SpikeShard(x + a.x, y + a.y);
		new SpikeShard(x + b.x, y + b.y);
		new SpikeShard(x + (b.x * .5), y + (b.y * .5));
		new SpikeShard(x + c.x, y + c.y);
		Sound.playPitched("sPapaSpikeBreak");
		destroy();
	}
	
	public void step(){
		if (attached && at < dad.apothem)
			at = Math.min(Calc.approach(at + 4, dad.apothem, 10), dad.apothem);
		
		calcPosition();
		
		if (Calc.triangleCircleCollision(Player.me.x, Player.me.y, 15.5, x + a.x, y + a.y, x + b.x, y + b.y, x + c.x, y + c.y))
			Player.hurtPlayer(110);
		bulletHandle();
		
		launchMult = Calc.approach(launchMult, 1.0, 12);
		alarmStep();
		
		if (!attached && (x < -210 || x > 850 || y < -210 || y > 690))
			destroy();
	}
	
	public void bulletHandle(){
		ArrayList<Bullet> list = Scene.getEntityList(Bullet.class);
		for (int i=0; i<list.size(); i++){
			Bullet b = list.get(i);
			if (Calc.triangleCircleCollision(b.x - (Math.signum(b.hspeed) * 11), b.y, 14, x + a.x, y + a.y, x + this.b.x, y + this.b.y, x + c.x, y + c.y)){
				b.destroy();
				bigBreak();
			}
		}
	}
	
	public void trail(double x, double y, double size, double force){
		for (int i=0; i<ghosts.size(); i++)
			ghostCircle(x, y, ghosts.get(i), size, force);
	}
	
	public void ghostCircle(double x, double y, SpikeGhost g, double size, double force){
		final double scale = g.size * (size / 640.0) + (force / 640.0);
		circle.render(0, Sprite.CENTERED, x + g.x, y + g.y, scale, scale, 0, 1, Graphics.color[0], Graphics.color[1], Graphics.color[2]);
	}
	
	public double getXscale(){
		if (attached)
			return (48.0 / 188.0) * (1 + (.25 * Math.sin(dad.fx[id] / 15.0))) * launchMult;
		return 48.0 / 188.0;
	}
	
	public void render(){
		
	}
	
	public void launch(int timer, double spd){
		alarm[1] = timer;
		alarmEvent(0);
		storedSpeed = spd;
	}
	
	public void front(double x, double y){
		Graphics.setColor("0078F7");
		trail(x, y, 20, 5);
		Graphics.setColor("FF1864");
		trail(x, y, 20, 0);
		lazy.render(0, Sprite.WEST, this.x + x, this.y + y, getXscale(), (48.0 / 188.0) * launchMult, angle, 1, "FF1864");
		
		/*drawDoint(a);
		drawDoint(b);
		drawDoint(c);*/
	}
	
	public void drawDoint(Doint d){
		Graphics.setColor(0,0,0);
		Graphics.setAlpha(1);
		Shape.drawRectangle(x + d.x - 3, y + d.y - 3, x + d.x + 3, y + d.y + 3);
	}
	
	public void back(double x, double y){
		lazy.render(0, Sprite.WEST, this.x + x, this.y + y, getXscale(), (48.0 / 188.0) * launchMult, angle, 1, "0078F7");
	}

}
