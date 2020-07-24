package world.intro;

import main.Entity;

public class SlideUh extends Entity{

	public SlideUh(double x, double y) {
		super(x, y);
		alarmInitialize(1);
		alarm[0] = 10;
	}
	
	public void alarmEvent(int i){
		new Slide();
		destroy();
	}
	
	public void step(){
		alarmStep();
	}
	
	public void render(){
		
	}

}
