package world.dream;

import java.util.ArrayList;

import world.control.EV;
import world.control.Global;
import world.control.Sound;
import world.gameplay.Heart;
import world.gameplay.Jar;

import main.Calc;
import main.Entity;
import main.Scene;

public class JarControl extends Entity{
	public boolean thing;

	public JarControl(double x, double y) {
		super(x, y);
		
		thing = false;
		
		if (Global.event[EV.JAR_DREAM] == 0){
			ArrayList<Heart> list = Scene.getEntityList(Heart.class);
			list.get(0).destroy();
		}
		else
			thing = true;
	}
	
	public void step(){
		if (!thing && Scene.instanceNumber(Jar.class) == 0){
			thing = true;
			
			Global.event[EV.JAR_DREAM] = 1;
			
			double x = 480, y = 320;
			
			for (int i=0; i<12; i++){
				new DreamHeartParticles(x, y, Calc.random(360), Integer.MIN_VALUE + 10);
				DreamHeartParticles j = new DreamHeartParticles(x, y, Calc.random(360), Integer.MIN_VALUE + 10);
				j.setSpeed(j.getSpeed() * 4);
				if (i > 3){
					j = new DreamHeartParticles(x, y, Calc.random(360), Integer.MIN_VALUE + 10);
					j.setSpeed(j.getSpeed() * 8);
				}
			}
			
			Global.addTweet("i "+(String)Calc.choose("like ", "love ")+(String)Calc.choose("","","","","#")+"jars"+(String)Calc.choose("","","","!","!!","!!!"));
			
			new Heart(x, y, 25);
			Sound.play("sDreamHeartAppear");
		}
	}
	
	public void render(){
		
	}

}
