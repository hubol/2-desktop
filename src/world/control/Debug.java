package world.control;

import java.awt.Point;
import static java.awt.event.KeyEvent.*;
import java.io.File;
import java.util.ArrayList;

import world.Root;
import world.player.Player;
import graphics.Graphics;
import graphics.Shape;
import graphics.Sprite;
import graphics.Text;

import main.Calc;
import main.Entity;
import main.Input;
import main.Main;
import main.Scene;

public class Debug extends Entity{
	public boolean last = false, bast = false, maskOn = false;
	public int mode = -1, inputLength = 8, current = 0, inTime = 0, fuckyou = 0;
	public int[] input;
	public String[] modes;

	public Debug(double x, double y) {
		super(x, y);
		persistent = true;
		
		setDepth(Integer.MIN_VALUE);
		
		modes = new String[10];
		modes[0] = "mask display on";
		modes[1] = "tele room rx1,rx2,ry1,ry2";
		modes[2] = "mhealth,mbomb,mbullet,skelkey,gem";
		modes[3] = "event1,event2,value";
		modes[4] = "evitem, evitem, value";
		modes[5] = "enter 1 for upgrades";
		modes[6] = "";
		modes[7] = "";
		modes[8] = "";
		modes[9] = "";
		
		input = new int[inputLength];
		reset();
	}
	
	public void step(){
		inTime -= 1;
		
		if (IO.checkKey(VK_CONTROL) && IO.checkKey(VK_D)){
			if (!last){
				Global.debug = !Global.debug;
				if (!Global.debug)
					reset();
			}
			last = true;
		}
		else{
			last = false;
		}
		
		if (Global.debug){ // is debug mode enabled (have you pressed ctrl+d?)
			if (mode == -1){ //have you not selected a mode? (pressed 0-9)
				int input = getIntIO();
				if (input != -1){
					inTime = 5;
					reset();
					mode = input;
				}
			}
			else{ //ok so you have selected a mode
				int a = getIntIO();
				if (a!=-1 && inTime <= 0){ //if a key was pressed for input
					inTime = 5;
					input[current] = a;
					current += 1;
					if (current >= inputLength) //youve input maximum input so now you must execute
						execute();
				}
				if (IO.checkKey(VK_BACK_SPACE) && mode != -1){ //change a previous integer OR cancel debug option
					if (!bast){
						current -= 1;
						if (current < 0)
							reset();
					}
					bast = true;
				}
				else
					bast = false;
				
				if (Input.checkKey(VK_ENTER) && mode != -1){ //you have enough balls to enter less than maximum to execute your debug option
					execute();
				}
			}
		}
	}
	
	public void refreshDream(){
		if (Global.roomX > 15){
			Global.dream = true;
			Global.currentDream = Global.roomX - 16;
		}
		else{
			Global.dream = false;
			Global.currentDream = -1;
		}
	}
	
	public void execute(){
		if (mode == 1){
			int preX = Global.lastX, preY = Global.lastY;
			
			Global.lastX = Global.roomX;
			Global.lastY = Global.roomY;
			
			Global.roomX = (input[0]*10) + input[1];
			Global.roomY = (input[2]*10) + input[3];
			
			Global.teleport = true;
			
			Global.refreshIconMap();
			
			File fuck = new File(Main.DIRECTORY, "res/rooms/rm"+Global.roomX+"_"+Global.roomY+".dongs");
			
			if ((Global.roomX > 15 && Global.roomY > 0) || Global.roomY > 15 || !fuck.exists()){
				Global.roomX = Global.lastX;
				Global.roomY = Global.lastY;
				
				Global.lastX = preX;
				Global.lastY = preY;
				
				Sound.errorPlay();
				
				refreshDream();
			}
			else{
				refreshDream();
				Root.changeRoom(Global.roomX, Global.roomY, Global.dream);
			}
		}
		else if (mode == 2){
			if (input[0] == 1)
				Global.heal();
			if (input[1] == 1)
				Global.playerBombs = Global.playerMaxBombs;
			if (input[2] == 1)
				Global.playerBullets = Global.playerMaxBullets;
			if (input[3] == 1)
				Global.pickupKey("FFFFFF", Player.me.x - 16, Player.me.y - 16);
			if (input[4] == 1)
				Global.money += 200;
			
			Hud.showAll();
		}
		else if (mode == 3){
			Global.event[(input[0]*10) + input[1]] = input[2];
		}
		else if (mode == 4){
			Global.eventItem[(input[0]*10) + input[1]] = input[2];
		}
		else if (mode == 0){
			maskOn = !maskOn;
		}
		else if (mode == 5){
			Global.gotWallSlide = true;
			if (fuckyou == 0){
				Global.lockWeapon(0);
				Global.lockWeapon(1);
			}
			else if (fuckyou == 1){
				Global.unlockWeapon(0);
				Global.unlockWeapon(1);
			}
			fuckyou++;
			if (fuckyou > 1)
				fuckyou = 0;
		}
		
		reset();
	}
	
	public void reset(){
		inTime = 5;
		mode = -1;
		current = 0;
		for (int i=0; i<8; i++){
			input[i] = 0;
		}
	}
	
	public int getIntIO(){
		int mode;
		if (IO.checkKey(VK_1))
			mode = 1;
		else if (IO.checkKey(VK_2))
			mode = 2;
		else if (IO.checkKey(VK_3))
			mode = 3;
		else if (IO.checkKey(VK_4))
			mode = 4;
		else if (IO.checkKey(VK_5))
			mode = 5;
		else if (IO.checkKey(VK_6))
			mode = 6;
		else if (IO.checkKey(VK_7))
			mode = 7;
		else if (IO.checkKey(VK_8))
			mode = 8;
		else if (IO.checkKey(VK_9))
			mode = 9;
		else if (IO.checkKey(VK_0))
			mode = 0;
		else
			mode = -1;
		return mode;
	}
	
	public void render(){
		if (Global.debug){
			if (maskOn){
				Graphics.setColor(1, 0, 0);
				Graphics.setAlpha(.8);
				ArrayList<Entity> list = Scene.getEntityList(Entity.class);
				for (int i=0; i<list.size(); i++){
					Entity boy = list.get(i);
					//compute center
					Point o = boy.mask.getOrigin(boy.orientation);
					double xx = boy.x, yy = boy.y;
					o.x*=-boy.xscale;
					o.y*=-boy.yscale;
					xx+=o.x;
					yy+=o.y;
					xx+=boy.xscale*boy.mask.width/2;
					yy+=boy.yscale*boy.mask.height/2;
					Graphics.setColor(Calc.stupidStringToHex(boy.getClass().getName()));
					Shape.drawRectangle(xx - (boy.mask.width / 2), yy - (boy.mask.height / 2), xx + (boy.mask.width / 2), yy + (boy.mask.height / 2));
				}
				Graphics.setAlpha(1);
			}
			
			if (mode != -1){
				double xStart = 0, yStart = 416, h = 4, v = 4;
				
				Graphics.setAlpha(.4);
				Graphics.setColor(0,0,0);
				Shape.drawRectangle(0,380,640,480);
				
				Graphics.setAlpha(1);
				Text.setFont(Global.FONT);
				Text.orientation = Text.SOUTHWEST;
				Graphics.setColor(1, .95 + Calc.random(.1), 1);
				Text.drawTextExt(xStart, yStart + ((v/8)*32), ("DEBUG "+mode+": %["+modes[mode]).toUpperCase()+"]", h/8, v/8, 0,"21CBFF");
				Text.orientation = Text.CENTERED;
				
				double xMult = 11;
				
				for (int i=0; i<inputLength; i++){
					if (i == current){
						Graphics.setColor("21CBFF");
						Shape.drawRectangle(xStart + (h*xMult*i), yStart, xStart + (h*xMult*(i + 1)), yStart + (v*16));
					}
					Sprite.get("sDebugNumbers").render(input[i], Sprite.NORTHWEST, xStart + (h*xMult*i), yStart, h, v, 0, 1, 1, .95 + Calc.random(.1), 1);
				}
			}
			else{
				Graphics.setAlpha(.4);
				Graphics.setColor(0,0,0);
				Shape.drawRectangle(0,460,640,480);
				
				Graphics.setAlpha(1);
				Text.setFont(Global.FONT);
				Text.orientation = Text.SOUTHWEST;
				Graphics.setColor(1, .95 + Calc.random(.1), 1);
				Text.drawTextExt(0, 496, "DEBUG ENABLED: [PUSH NUMBERS FOR FUNCTIONS]", .49, .5, 0, "21CBFF");
			}
		}
	}

}
