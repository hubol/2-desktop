package world.control;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import main.Entity;
import main.Main;

public class Recorder extends Entity{
	public String data;
	public static Recorder me;
	public int times;
	public char lastChar;

	public Recorder(){
		super(0, 0);
		
		setDepth(Integer.MIN_VALUE);
		
		times = 0;
		lastChar = (char)' ';
		data = "";
		persistent = true;
		visible = false;
		me = this;
		setCollisionGroup(Global.CONTROLLER);
	}
	
	public void destroy(){
		me = null;
		super.destroy();
	}
	
	public void step(){
		if (Global.mainActive && !Global.paused){
			//LEFT, RIGHT, UP, DOWN, JUMP, ACTION, INTERACT;
			boolean[] pressed = new boolean[7];
			pressed[0] = IO.checkKey(Global.LEFT);
			pressed[1] = IO.checkKey(Global.RIGHT);
			pressed[2] = IO.checkKey(Global.UP);
			pressed[3] = IO.checkKey(Global.DOWN);
			pressed[4] = IO.checkKey(Global.JUMP);
			pressed[5] = IO.checkKey(Global.ACTION);
			pressed[6] = IO.checkKey(Global.INTERACT);
			char c = getChar(pressed);
			if (c != lastChar){
				appendData();
				times = 1;
				lastChar = c;
			}
			else{
				lastChar = c;
				times += 1;
			}
		}
	}
	
	public void appendData(){
		if (times <= 6){
			for (int i=0; i<times; i++)
				data += lastChar;
		}
		else{
			data += ((char)127);
			data += ((char)126);
			data += ((char)125);
			data += times;
			data += ((char)125);
			data += lastChar;
		}
	}
	
	public void save(){
		appendData();
		times = 0;
		lastChar = (char)' ';
		
		if (Global.mainActive && !Global.paused){
			try {
				File f = new File(Main.DIRECTORY, "res/data/run"+Global.identifier+".run");
				
				String s = "";
				if (f.exists()){
					FileReader r = new FileReader(f);
					BufferedReader j = new BufferedReader(r);
					s = j.readLine();
					r.close();
					j.close();
				}
				
				FileWriter w;
				w = new FileWriter(f);
				
				PrintWriter p = new PrintWriter(w);
				
				p.println(s + data);
				data = "";
				
				p.close();
				w.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public char getChar(boolean[] pressed){
		int id = 0;
		for (int i=0; i<7; i++){
			if (pressed[i])
				id += Math.pow(2, 6-i);
		}
		return (char)id;
	}
	
	public void render(){
		
	}

}
