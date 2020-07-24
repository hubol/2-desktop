package world.gameplay;

import java.util.ArrayList;

import world.control.Global;
import world.control.MapIconInfluence;
import world.control.Sound;
import world.control.SoundLoader;
import world.dream.DreamHeartParticles;
import main.Calc;
import main.Scene;

public class HeartEvent extends MapIconInfluence{
	public int event, equals, id;

	public HeartEvent(double x, double y, int event, int equals, int id) {
		super(x, y); 
		
		boolean fuck = (Global.room(9,7) && id == 41); //this is terrible im sorry
		
		if (fuck)
			new SoundLoader("sDreamHeartAppear");
		
		fuck = Global.room(9,7); //i will investigate this code later and it wont make sense. to future self: this works. poorly. but it works
		
		ArrayList<Heart> list = Scene.getEntityList(Heart.class);
		for (int i=0; i<list.size(); i++){
			if (list.get(i).me == id){
				list.get(i).destroy();
			}
		}
		
		if (Global.event[event] == equals){
			new Heart(x, y, id);
			destroy();
		}
		else{
			if (!Global.heartGot[id]){
				if (!fuck)
					new SoundLoader("sDreamHeartAppear");
			}
			else
				destroy();
		}
		
		this.event = event;
		this.equals = equals;
		this.id = id;
		
		setCollisionGroup(Global.DEACTIVATEME);
	}
	
	public void refreshIcon(){
		if (!Global.heartGot[id] && Global.event[event] != equals)
			Global.setIconMap(Global.roomX, Global.roomY, 4);
	}
	
	public void step(){
		if (Global.event[event] == equals && !Global.heartGot[id]){
			for (int i=0; i<12; i++){
				new DreamHeartParticles(x, y, Calc.random(360), Integer.MIN_VALUE + 10);
				DreamHeartParticles j = new DreamHeartParticles(x, y, Calc.random(360), Integer.MIN_VALUE + 10);
				j.setSpeed(j.getSpeed() * 4);
				if (i > 3){
					j = new DreamHeartParticles(x, y, Calc.random(360), Integer.MIN_VALUE + 10);
					j.setSpeed(j.getSpeed() * 8);
				}
			}
			Sound.play("sDreamHeartAppear");
			new Heart(x, y, id);
			
			destroy();
		}
	}
	
	public void render(){
		
	}

}
