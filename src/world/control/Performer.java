package world.control;

import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import main.Entity;
import main.Input;
import main.Main;

public class Performer extends Entity{
	public boolean[] lastPressed, pressed;
	public static Performer me;
	public File f;
	public FileReader r;
	public BufferedReader j;
	public int id;
	public int times;
	
	public Performer(int id) {
		super(0, 0);
		
		setDepth(Integer.MAX_VALUE);
		
		times = 0;
		
		this.id = id;
		persistent = true;
		visible = false;
		setCollisionGroup(Global.CONTROLLER);
		
		f = new File(Main.DIRECTORY, "res/data/run"+id+".run");
		try {
			r = new FileReader(f);
			j = new BufferedReader(r);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		me = this;
		
		lastPressed = new boolean[7];
		pressed = new boolean[7];
		for (int i=0; i<7; i++){
			lastPressed[i] = false;
			pressed[i] = false;
		}
	}
	
	public void destroy(){
		try {
			r.close();
			j.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		me = null;
		super.destroy();
	}
	
	public boolean[] intToBool(int n){
		boolean[] array = new boolean[7];
		int mask = 1;
		for (int i = 6; i>=0; i--) {
			array[i] = (n&mask)>0;
			mask = mask << 1;
		}
		
		return array;
	}
	
	public void fps(int i){
		Main.FPS = i;
		Main.animator.targetFPS = i;
		Main.animator.updateInterval = i;
	}
	
	public void step(){
		if (Input.checkKey(KeyEvent.VK_1))
			fps(30);
		else if (Input.checkKey(KeyEvent.VK_2))
			fps(60);
		else if (Input.checkKey(KeyEvent.VK_3))
			fps(90);
		
		for (int i=0; i<7; i++)
			lastPressed[i] = pressed[i];

		//read file!!!
		if (times == 0){
			try {
				int c = j.read();
				if (c != -1){
					int stored = c;
					j.mark(5);
					if (c == 127){
						if (j.read() == 126 && j.read() == 125){
							times = (j.read() - 48);
							int a;
							while ((a = j.read()) != 125){
								times *= 10;
								times += (a - 48);
							}
							times -= 1;
							c = j.read();
						}
						else{
							j.reset();
							c = stored;
						}
					}

					pressed = intToBool(c);
				}
				else{
					IO.playback = false;
					Global.identifier = id;
					new Recorder();
					destroy();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
			times -= 1;
	}
	
	public int getKey(int keyCode){
		if (keyCode == Global.LEFT) return 0;
		if (keyCode == Global.RIGHT) return 1;
		if (keyCode == Global.UP) return 2;
		if (keyCode == Global.DOWN) return 3;
		if (keyCode == Global.JUMP) return 4;
		if (keyCode == Global.ACTION) return 5;
		if (keyCode == Global.INTERACT) return 6;
		return -1;
	}
	
	public boolean check(int keyCode){
		int i = getKey(keyCode);
		if (i != -1)
			return pressed[i];
		return false;
	}

	public boolean press(int keyCode){
		int i = getKey(keyCode);
		if (i != -1)
			return check(keyCode) && !lastPressed[getKey(keyCode)];
		return false;
	}

}
