package world.pet;

import java.util.ArrayList;
import java.util.Collections;

public class PetShell{
	public ArrayList<PetEntity> entities;
	public boolean[][] display;
	public int width, height;
	public int selected;
	
	public int lastFood;
	public int hungry = 0, happy = 0, poops = 0;
	public boolean sick = false, win = false;
	
	public PetSprite character, upClose, screenStatus, heartFill, heartEffect, screenFood, meal, snack, needle, poop, skull, wash, startle, sun, wow;
	
	public PetShell(int w, int h){
		display = new boolean[w][h];
		width = w;
		height = h;
		
		lastFood = 0;
		selected = -1;
		
		entities = new ArrayList<PetEntity>();
		
		character = new PetSprite("bogus");
		upClose = new PetSprite("bogusUpclose");
		screenStatus = new PetSprite("statusScreen");
		heartFill = new PetSprite("heart");
		heartEffect = new PetSprite("heartEffect");
		screenFood = new PetSprite("foodMenu");
		meal = new PetSprite("foodBoat");
		snack = new PetSprite("foodButterfly");
		needle = new PetSprite("needle");
		poop = new PetSprite("poop");
		skull = new PetSprite("skull");
		wash = new PetSprite("wash");
		startle = new PetSprite("startle");
		sun = new PetSprite("sun");
		wow = new PetSprite("wow");
		
		wipe();
	}
	
	/**the select button is pressed*/
	public void select(){
		for (int i=0; i<entities.size(); i++)
			entities.get(i).select();
	}
	
	/**the confirm button is pressed*/
	public void confirm(){
		for (int i=0; i<entities.size(); i++)
			entities.get(i).confirm();
	}
	
	/**the cancel button is pressed*/
	public void cancel(){
		for (int i=0; i<entities.size(); i++)
			entities.get(i).cancel();
	}
	
	/**call the step methods of all entities, reserved for any game logic or anything not pertaining to visuals
	 * that should be updated each frame.
	 */
	public void update(){
		for (int i=0; i<entities.size(); i++)
			entities.get(i).step();
	}
	
	/**clear the virtual display and call the render methods of all entities, reserved for updating
	 * visuals each frame.
	 */
	public void render(){
		wipe();
		
		Collections.sort(entities);
		
		for (int i=0; i<entities.size(); i++)
			entities.get(i).render();
	}
	
	/**clear the virtual display of all pixels*/
	public void wipe(){
		for (int i=0; i<width; i++){
			for (int j=0; j<height; j++)
				display[i][j] = false;
		}
	}

}
