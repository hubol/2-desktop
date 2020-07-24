package world.gameplay;

import world.control.Global;
import main.Entity;

public class Key extends Entity{

	//the deal with this class is that it just exists to create dropkeys from the editor. it helps populate the world with keys i guess you could say.
	public Key(double x, double y, int identity, String c) {
		super(x, y);
		
		if (!Global.pickedUp[identity]){
			Global.pickedUp[identity] = true;
			Global.addDropEntry(Global.roomX,Global.roomY,x,y,c);
		}
		
		visible = false;
		stepActive = false;
		destroy();
	}
	
	public void step(){

	}
	
	public void render(){
		
	}

}
