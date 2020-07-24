package world.event;

import graphics.Sprite;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import world.control.Global;
import world.player.Player;

import main.Calc;
import main.Entity;
import main.Main;
import main.Scene;

public class Book extends Entity{
	public String extension, messageText;
	public ArrayList<String> spriteNames;
	
	public int timer;

	public Book(double x, double y, String s) {
		super(x, y);
		
		setDepth(8);
		
		File f = new File(Main.DIRECTORY, "res/books/"+s+".sup");
		FileReader w;
	
		try {
			w = new FileReader(f);
			BufferedReader p = new BufferedReader(w);
			
			orientation = Sprite.SOUTH;
			sprite = Sprite.get(p.readLine());
			
			xscale = Calc.parseDouble(p.readLine());
			yscale = xscale;
			
			mask = sprite.mask;
			
			messageText = p.readLine();
			extension = p.readLine();
			
			spriteNames = new ArrayList<String>();
			
			String h = p.readLine();
			while (!h.equals("$eof")){
				spriteNames.add(h);
				h = p.readLine();
			}
			
			p.close();
			w.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		setCollisionGroup(Global.DEACTIVATEME, Global.INTERACTABLE);
		
		timer = -1;
	}
	
	public void step(){
		boolean ok = true;
		if (sprite == Sprite.get("sDrawingPoster")) //fuck you
			ok = (Player.me.y >= y);
		
		if (Scene.collision(this, x, y, Global.PLAYER) && Player.control && timer <= 0 && Player.me.vspeed == 0 && ok){
			
			Global.drawDown(x, y-2 - (sprite.imageHeight * yscale));
			
			if (Player.canDownInteract())
				new BookMessage(x, y - (sprite.imageHeight * yscale), this);
		}
		
		if (timer > -1)
			timer -= 1;
	}
	
	public void render(){
		sprite.render(0, orientation, x + Calc.rangedRandom(.4), y + Calc.rangedRandom(.4), xscale, yscale, 0, alpha, 1, 1, 1);
	}

}
