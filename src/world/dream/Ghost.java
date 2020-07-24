package world.dream;

import graphics.Sprite;
import world.control.Global;
import world.control.PseudoGenerator;
import world.control.Shake;
import world.player.Player;
import main.Entity;
import main.Scene;

public class Ghost extends Entity{
	public PseudoGenerator g;
	public Shake s;
	
	public double a, alp;
	public String face;

	public Ghost(double x, double y) {
		super(x, y);
		setDepth(Integer.MIN_VALUE+10);
		setCollisionGroup(Global.DEACTIVATEME, Global.DEACTIVATEENEMY);
		
		orientation = Sprite.NORTHWEST;
		mask = Global.sBLOCK.mask;
		vspeed = -1;
		
		s = new Shake(.5);
		g = new PseudoGenerator(1.1,1.09,6.9,169420,4.2,1.25,(x - 6.3) * (y + 3.4));
		//setColor(Calc.makeHexColor(Calc.getColorHSV((int)g.random(256), 150, 255)));
		
		int b = ((((int)x % 64) / 32) + (((int)y % 64) / 32)) % 2;
		
		face = "FF6E28";
		
		if (b == 0)
			setColor("FFBA18");
		else{
			setColor("FF6E28");
			face = "FFBA18";
		}
		
		a = g.random(30);
		calcAlp();
		
		imageSingle = g.random(4);
		imageSpeed = .2 + g.random(.4);
		alarmInitialize(1);
		alarm[0] = 1 + (int)g.random(30);
	}
	
	public void alarmEvent(int i){
		imageSpeed = .2 + g.random(.4);
		alarm[0] = 1 + (int)g.random(30);
	}
	
	public void calcAlp(){
		alp = Math.abs(Math.sin(a / 30.0)) * .7;
	}
	
	public void step(){
		a += 1;
		calcAlp();
		
		sprite = Sprite.get("sGhostBack");
		
		super.step();
		
		if (Scene.collision(this, x, y, Global.PLAYER) ||Scene.collision(this, x, y + 480, Global.PLAYER) ||Scene.collision(this, x, y - 480, Global.PLAYER))
			Player.hurtPlayer(70);
			
		while (y < 0)
			y += 480;
		while (y > 480)
			y -= 480;
	}
	
	public void draw(double y){
		//Sprite.get("sGhostBack").render(imageSingle, Sprite.CENTERED, x + 16 + s.x, y + 16 + s.y, 1, 1, 0, .8 + Calc.random(.3), 1, 1, 1);
		//Sprite.get("sGhostBack").render(imageSingle, Sprite.CENTERED, x + 16 + s.x, y + 16 + s.y, 1, 1, 0, alp, colR, colG, colB);
		Sprite.get("sGhostOutline").render(imageSingle, Sprite.CENTERED, x + 17 + s.x, y + 16 + s.y, 1, 1, 0, 1, 1, 1, 1);
		Sprite.get("sGhostOutline").render(imageSingle, Sprite.CENTERED, x + 15 + s.x, y + 16 + s.y, 1, 1, 0, 1, 1, 1, 1);
		Sprite.get("sGhostOutline").render(imageSingle, Sprite.CENTERED, x + 16 + s.x, y + 17 + s.y, 1, 1, 0, 1, 1, 1, 1);
		Sprite.get("sGhostOutline").render(imageSingle, Sprite.CENTERED, x + 16 + s.x, y + 15 + s.y, 1, 1, 0, 1, 1, 1, 1);
		
		Sprite.get("sGhostFace").render(imageSingle, Sprite.CENTERED, x + 16 + s.x, y + 16 + s.y, 1, 1, 0, 1, face);
		Sprite.get("sGhostBack").render(imageSingle, Sprite.CENTERED, x + 16 + s.x, y + 16 + s.y, 1, 1, 0, 1, colR, colG, colB);
		Sprite.get("sGhostOutline").render(imageSingle, Sprite.CENTERED, x + 16 + s.x, y + 16 + s.y, 1, 1, 0, 1, 1, 1, 1);
	}
	
	public void render(){
		draw(y);
		draw(y - 480);
		draw(y + 480);
	}

}
