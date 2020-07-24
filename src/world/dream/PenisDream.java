package world.dream;

import graphics.Graphics;
import graphics.Sprite;

import java.util.ArrayList;

import world.control.EV;
import world.control.Global;
import world.control.Sound;
import world.control.SoundLoader;
import world.gameplay.Heart;
import main.Calc;
import main.Entity;
import main.Scene;

public class PenisDream extends Entity{
	public static PenisDream me;
	public final int goal = 1200;
	public int total;
	public double atTotal;

	public PenisDream(double x, double y) {
		super(x, y);
		
		me = this;
		total = 0;
		atTotal = 0;
		
		sprite = Sprite.get("sCircle");
		
		if (Global.event[EV.PENIS_DREAM] == 0){
			new SoundLoader(false, "sDreamHeartAppear");
			
			ArrayList<Heart> list = Scene.getEntityList(Heart.class);
			for (int i=0; i<list.size(); i++)
				list.get(i).destroy();
		}
		
		setDepth(Integer.MIN_VALUE+19);
		setCollisionGroup(Global.DEACTIVATEME);
	}
	
	public void step(){
		total = Math.min(total, goal);
		
		if (total == goal && Global.event[EV.PENIS_DREAM] == 0){
			Global.event[EV.PENIS_DREAM] = 1;
			
			double x = 144, y = 176;
			
			for (int i=0; i<12; i++){
				new DreamHeartParticles(x, y, Calc.random(360), Integer.MIN_VALUE + 10);
				DreamHeartParticles j = new DreamHeartParticles(x, y, Calc.random(360), Integer.MIN_VALUE + 10);
				j.setSpeed(j.getSpeed() * 4);
				if (i > 3){
					j = new DreamHeartParticles(x, y, Calc.random(360), Integer.MIN_VALUE + 10);
					j.setSpeed(j.getSpeed() * 8);
				}
			}
			
			Global.addTweet("i "+(String)Calc.choose("like ", "love ")+(String)Calc.choose("","","","","#")+(String)Calc.choose("dicks","cocks","penises","penis","dick","cock")+(String)Calc.choose("","","","!","!!","!!!"));
			
			new Heart(x, y, 19);
			Sound.play("sDreamHeartAppear");
		}
		
		atTotal = Calc.approach(atTotal, total, 5);
	}
	
	public void drawCircle(double x, double y, double r){
		sprite.render(0, Sprite.CENTERED, x + Calc.rangedRandom(.25), y + Calc.rangedRandom(.25), r / 640.0, r / 640.0, 0, Graphics.alpha, Graphics.myColor);
	}
	
	public void render(){
		if (total < goal){
			final double barHeight = 256, barWidth = 30, faceRadius = 36, lineWeight = 6.2, barParts = 96, xx = 592;
			double yy = 240 + (barHeight / 2.0) + faceRadius - (barWidth / 2.0), record;
			
			Graphics.setAlpha(1);
			Graphics.setColor("0093FF");
			for (int i=0; i<barParts; i++){
				drawCircle(xx, yy, (barWidth / 2) + lineWeight);
				yy -= barHeight / barParts;
			}
			yy += (barHeight / barParts) - (barWidth / 2);
			record = yy;
			drawCircle(xx, yy, faceRadius + lineWeight);
			
			Graphics.setColor("ffffff");
			yy = 240 + (barHeight / 2.0) + faceRadius - (barWidth / 2.0);
			
			if (total > 0){
				for (int i=0; i<barParts; i++){
					drawCircle(xx, yy, (barWidth / 2));
					yy -= (barHeight * (atTotal / (double)goal)) / barParts;
				}
			}
			yy = record;
			drawCircle(xx, yy, faceRadius);
			Sprite.get("sPenisMeterDassFace").render(0,Sprite.CENTERED,xx+Calc.rangedRandom(.4),yy+Calc.rangedRandom(.4),.5,.5,0,1,"0093FF");
		}
	}
	
	public void destroy(){
		me = null;
		super.destroy();
	}
	
	public void roomDestroy(){
		me = null;
		super.roomDestroy();
	}

}
