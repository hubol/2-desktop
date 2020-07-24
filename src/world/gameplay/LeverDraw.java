package world.gameplay;

import main.Entity;

public class LeverDraw extends Entity{
	public Lever mother;

	public LeverDraw(double x, double y, Lever mom) {
		super(x, y);
		mother = mom;
		setDepth(-1);
	}
	
	public void step(){
		
	}
	
	public void render(){
		mother.draw();
	}

}
