package world.junk;

import java.util.ArrayList;

import graphics.Sprite;
import world.control.EV;
import world.control.Global;
import world.control.Shake;
import world.control.Sound;
import world.control.SpriteLoader;
import world.particles.BulletPop;
import world.player.Player;
import main.Calc;
import main.Entity;
import main.Scene;

public class StupidOrb extends Entity{
	private Shake s = new Shake();
	private double xxscale = 1;
	private boolean fuck = false;
	private Sprite star;
	
	private ArrayList<StupidGhost> ghosts = new ArrayList<StupidGhost>();

	public StupidOrb(double x, double y) {
		super(x, y);
		setCollisionGroup(Global.DEACTIVATEME, Global.STUPIDORB);
		setDepth(-1);
		
		if (Global.event[EV.KISSFED] > 0){
			roomDestroy();
		}
		else{
			new SpriteLoader("sSprak", "sOrbStar", "sOrbTrail");
			star = Sprite.get("sOrbTrail");
			sprite = Sprite.get("sSprak");
			mask = sprite.mask;
		}
	}
	
	public void kiss(){
		for (int i=0; i<ghosts.size(); i++)
			ghosts.get(i).update();
		for (int i=0; i<ghosts.size(); i++){
			if (ghosts.get(i).size <= 0){
				ghosts.remove(i);
				i--;
			}
		}
	}
	
	public void updateGhosts(){
		ghosts.add(new StupidGhost(x, y, angle));
		
		kiss();
	}
	
	public void boink(){
		Sound.playPitched("sStupidOrbBoink", .07);
	}
	
	public void playerCheck(boolean fuck){
		if (Calc.pointDistance(x, y, Player.me.x, Player.me.y) < 28 && !fuck){
			this.fuck = true;
			boink();
			setDirSpeed(Calc.pointDirection(Player.me.x, Player.me.y + 12, x, y), Player.me.getSpeed() * 1.5);
			if (vspeed <= 0)
				vspeed = Math.min(-5, vspeed * 1.5);
			
			hspeed += Player.me.hspeed;
			vspeed += Player.me.vspeed;
			
			if (Player.me.xsc < 0)
				hspeed -= 1;
			else
				hspeed += 1;
			
			double dir = getDirection();
			boolean cancel = false;
			while (Calc.pointDistance(x, y, Player.me.x, Player.me.y) < 28 && !cancel){
				double xx = Calc.dirX(1, dir), yy = Calc.dirY(1, dir);
				if (!Scene.collision(this, x + xx, y + yy, Global.SOLIDBLOCK)){
					x += xx;
					y += yy;
					updateGhosts();
				}
				else
					cancel = true;
			}
			
			double d = Calc.pointDirection(x, y, Player.me.x, Player.me.y);
			for (int i=0; i<3; i++)
				new StupidStar(x + Calc.dirX(12, d) + Calc.rangedRandom(4), y + Calc.dirY(12, d) + Calc.rangedRandom(4));
		}
	}
	
	public void step(){
		xxscale = Calc.approach(xxscale, 1, 20);
		fuck = false;
		
		if (!Scene.collision(this, x, y + 1, Global.SOLIDBLOCK))
			vspeed += .8;
		playerCheck(fuck);
		
		double h = Math.signum(hspeed);
		for (int i=0; i<Math.abs(hspeed); i++){
			if (!Scene.collision(this, x + h, y, Global.SOLIDBLOCK)){
				x += h;
				updateGhosts();
				playerCheck(fuck);
			}
			else{
				for (int j=0; j<3; j++)
					new StupidStar(x + (12 * h) + Calc.rangedRandom(4), y + Calc.rangedRandom(4));
				
				hspeed *= -.2;
				h *= -1;
				boink();
			}
		}
		
		double v = Math.signum(vspeed);
		for (int i=0; i<Math.abs(vspeed); i++){
			if (!Scene.collision(this, x, y + v, Global.SOLIDBLOCK)){
				y += v;
				updateGhosts();
				playerCheck(fuck);
			}
			else{
				if (vspeed > 0 && vspeed < 1){
					vspeed = 0;
					i = 1000000;
				}
				
				for (int j=0; j<3; j++)
					new StupidStar(x + Calc.rangedRandom(4), y + (12 * v) + Calc.rangedRandom(4));
				
				vspeed *= -.2;
				v *= -1;
				boink();
			}
		}
		
		angle += hspeed * Math.signum(vspeed);
		hspeed *= .99;
		if (hspeed > 0){
			hspeed -= .05;
			if (hspeed < 0)
				hspeed = 0;
		}
		else if (hspeed < 0){
			hspeed += .05;
			if (hspeed > 0)
				hspeed = 0;
		}
		
		if (x >= 656){
			Sound.playPitched("sStupidOrbRespawn",.025);
			xxscale = 0;
			angle = 0;
			x = xstart;
			y = ystart;
			setDirSpeed(0,0);
		}
		
		double dir = getDirection();
		boolean cancel = false;
		while (Calc.pointDistance(x, y, Player.me.x, Player.me.y) < 28 && !cancel){
			double xx = Calc.dirX(1, dir), yy = Calc.dirY(1, dir);
			if (!Scene.collision(this, x + xx, y + yy, Global.SOLIDBLOCK)){
				x += xx;
				y += yy;
				updateGhosts();
			}
			else
				cancel = true;
		}
		
		updateGhosts();
		if (getSpeed() < 1)
			kiss();
		if (getSpeed() < .5)
			kiss();
		if (getSpeed() < .25)
			kiss();
	}
	
	public void destroy(){
		new BulletPop(x, y);
		Sound.playPitched("sGunPop");
		s.destroy();
		super.destroy();
	}
	
	public void render(){
		for (int i=0; i<ghosts.size(); i++){
			StupidGhost g = ghosts.get(i);
			double ss = g.size * xxscale;
			star.render(0, orientation, g.x + s.x + Calc.rangedRandom(.1), g.y + s.y + Calc.rangedRandom(.1), ss, ss, g.angle, 1, 1, 1, 1); 
		}
		
		sprite.render(0, orientation, x + s.x + Calc.rangedRandom(.1), y + s.y + Calc.rangedRandom(.1), xxscale, xxscale, angle, 1, 1, 1, 1); 
	}

}
