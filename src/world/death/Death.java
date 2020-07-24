package world.death;

import graphics.BlendMode;
import graphics.Font;
import graphics.Graphics;
import graphics.Shape;
import graphics.Sprite;
import graphics.Text;
import world.Root;
import world.control.Global;
import world.control.Hud;
import world.control.IO;
import world.control.Music;
import world.control.NewGen;
import world.control.Pause;
import world.control.Shake;
import world.control.Sound;
import main.Calc;
import main.Entity;
import main.Scene;

public class Death extends Entity{
	public Shake[] s;
	public int a;
	
	public int width;
	public boolean drawTwo, go;
	
	public NewGen g;

	public double widthPercent, bidthPercent;
	
	public double selectionX;
	
	public int menuSelection, menuButtons;
	public String menuHeading;
	public String[] buttonText, buttonInfo;
	public int[] buttonCodeId;
	
	public int error;
	public double img = 0;
	
	public Font f;

	public boolean escape = Scene.killOnEscape;
	
	public int show = 0;

	public Death() {
		super(0, 0);
		s = new Shake[10];
		for (int i=0; i<s.length; i++)
			s[i] = new Shake(.3);
		s[0] = new Shake(.7);
		
		f = new Font("menufont", true, '-', '<');
		
		a = 0;
		
		Pause.me.reset();
		Hud.me.currentMoney = 0;
		
		selectionX = 96;
		
		error = 0;
		
		show = 0;
		
		widthPercent = 0;
		bidthPercent = 0;
		menuSelection = 0;
		menuButtons = 0;
		
		g = new NewGen(1.1,1.21,1.33,1.46,1.6,Global.menuOverlay * 1.65);
		
		go = false;

		alarmInitialize(2);
		alarm[0] = 60;
		alarm[1] = alarm[0] + 30;
		setMenu(0, (String)Calc.choose("YOU DEAD", "DU DODE", "YOU HAVE DIED", "YE DEAD", "YOU DIED", "YOU ARE DEAD", "YOU DEAD", "YOU DEAD", "YOU DEAD", "YOU DEAD", "YOU DEAD", "YOU DEAD", "YOU DEAD", "YOU DEAD", "YOU DEAD", "YOU DEAD"), new String[]{"RESTART FROM LAST SAVE","QUIT TO MENU","QUIT GAME"},new String[]{"","",""},new int[]{0,1,2});
	}
	
	public void alarmEvent(int i){
		Sound.playPitched("sDeathShow",.05);
		show += 1;
	}
	
	public void confirmSound(){
		if (Global.menuOverlay == 69)
			Sound.playPitched("sMenuFart", .05);
		else
			Sound.playPitched("sMenuConfirm", .05);
	}
	
	public void buttonConfirm(int id){
		boolean confirm = true;
		
		if (id == 2) //quit
			System.exit(0);
		else if (id == 1){ //let's return to the main main main menu (BACK)
			Music.changeMusic("");
			Root.changeRoom("d");
		}
		else if (id == 0){
			Music.changeMusic("");
			Global.storedSaveFile = Global.saveFileNumber;
			Root.changeRoom("b");
		}
		
		if (confirm){
			confirmSound();
			widthPercent = 0;
		}
		else{
			if (Global.menuOverlay == 69)
				Sound.playPitched("sMenuFart", .1);
			else
				Sound.playPitched("sDishError", .05);
			error = 6;
		}
	}
	
	/**set the menu. initial position on menu, heading, text for buttons, additional info for buttons, code id for buttons*/
	public void setMenu(int initial, String heading, String[] text, String[] info, int[] code){
		menuSelection = initial;
		menuHeading = heading;
		buttonText = text;
		buttonInfo = info;
		buttonCodeId = code;
		menuButtons = text.length;
		selectionX = 96;
	}
	
	public void step(){
		img += (6.0 + Calc.random(2.2)) / 30.0;
		while (img >= 3.0)
			img -= 3;
		
		if (show > 0)
			widthPercent = Math.min(1, (Calc.approach(widthPercent, 1, 5) + .04) * 1.3);
		
		if (show > 1)
			bidthPercent = Math.min(1, (Calc.approach(bidthPercent, 1, 5) + .04) * 1.3);
		
		if (show >= 2){
			if (bidthPercent >= 1 && IO.checkFrameKey(Global.INTERACT))
				buttonConfirm(buttonCodeId[menuSelection]);
			
			if (IO.checkFrameKey(Global.UP)){
				menuSelection -= 1;
				selectionX = 0;
				if (menuSelection < 0)
					menuSelection = menuButtons - 1;
				if (Global.menuOverlay == 69)
					Sound.playPitched("sMenuFart", .05);
				else
					Sound.playPitched("sMenuUp", .05);
			}
			else if (IO.checkFrameKey(Global.DOWN)){
				menuSelection += 1;
				selectionX = 0;
				if (menuSelection >= menuButtons)
					menuSelection = 0;
				if (Global.menuOverlay == 69)
					Sound.playPitched("sMenuFart", .05);
				else
					Sound.playPitched("sMenuDown", .05);
			}
		}
		
		selectionX = Math.min(96, (Calc.approach(selectionX, 96, 3) + 9) * 1.1);

		if (error > 0)
			error -= 1;
		
		alarmStep();
	}
	
	public void button(double x, double y, double width, double height, String color){
		if (width > 0){
			Graphics.setColor(color);
			Sprite.get("sMenuButton").render(0, Sprite.NORTHWEST, x, y, Math.min(1, (width / 64.0)), height / 32.0, 0, 1, color);
			Sprite.get("sMenuButton").render(1, Sprite.NORTHWEST, x + width - 32, y, Math.min(1, (width / 64.0)), height / 32.0, 0, 1, color);
			if (width > 64)
				Shape.drawRectangle(x + 32, y, x + width - 32, y + height);
		}
	}
	
	public void render(){
		double xx = 0;
		if (error > 0)
			xx = -9 + (18 * (error % 2));
		
		Text.setFont(f);
		
		Sprite.get("sDeathBack").render(img, Sprite.NORTHWEST, 0, 0, 1, 1, 0, 1, Global.menuBackgroundColor);
		Sprite.get("sDeathLine").render(img, Sprite.NORTHWEST, 0, 0, 1, 1, 0, 1, Global.menuLineColor);
		
		if (menuButtons > 0){
			if (!menuHeading.equals("") && show > 0){
				Graphics.setColor(Global.menuLineColor);
				Shape.drawRectangle(0, 140 + s[3].y - 12, 640, 140 + s[3].y + 12);
				textButton(320 + s[3].x + xx, 140 + s[3].y, menuHeading, Text.CENTERED, widthPercent, 24, Global.menuLineColor, Global.menuBackgroundColor);
			}
			
			if (show > 1){
				double fck = 360;
				for (int i=0; i<menuButtons; i++){
					if (menuSelection != i)
						textButton(64 + s[4 + i].x + xx, fck + (i * 32) + s[4 + i].y, buttonText[i], Text.WEST, bidthPercent, 24, Global.menuLineColor, Global.menuBackgroundColor);
					else{
						/*textButton(56 + selectionX + s[4 + i].x, 304 + (i * 32) + s[4 + i].y, buttonText[i], Text.WEST, widthPercent, 28, Global.menuLineColor, Global.menuBackgroundColor);
						textButton(72 + selectionX + s[4 + i].x, 304 + (i * 32) + s[4 + i].y, buttonText[i], Text.WEST, widthPercent, 28, Global.menuLineColor, Global.menuBackgroundColor);*/
						Graphics.setColor(Global.menuLineColor);
						Shape.drawRectangle(0, fck + (i * 32) + s[4 + i].y - 12, 640, fck + (i * 32) + s[4 + i].y + 12);
						textButton(64 + selectionX + s[4 + i].x + xx, fck + (i * 32) + s[4 + i].y, buttonText[i], Text.WEST, bidthPercent, 24, Global.menuBackgroundColor, Global.menuLineColor);
					}
				}
				
				if (!buttonInfo[menuSelection].equals(""))
					textButton(320 + s[9].x + xx, 440 + s[9].y, buttonInfo[menuSelection], Text.CENTERED, bidthPercent, 24, Global.menuLineColor, Global.menuBackgroundColor);
			}
		}
		
		BlendMode.SUBTRACT.set();
		Sprite.get("overlay"+Global.menuOverlay).render(0,Sprite.NORTHWEST, 0, 0, 1+Calc.random(.001), 1+Calc.random(.001), 0, .22, 1, 1, 1);
		BlendMode.NORMAL.set();
	}

	/**use orientation CENTERED or WEST*/
	public void textButton(double x, double y, String text, int orientation, double widthPercent, double height, String buttonColor, String textColor){
		double width = ((text.length() + 6) * 16) * widthPercent;
		String txt = text.substring(0, (int)Math.min(text.length(), Math.max(0, (width - 64.0) / 16.0)));
		double xx = x - 32, yy = y - (height / 2.0);
		if (orientation == Text.CENTERED)
			xx = x - (text.length() + 6) * 8;
		
		button(xx, yy, width, height, buttonColor);
		Text.idiot = false;
		Text.orientation = Text.WEST;
		Graphics.setColor(textColor);
		if (orientation != Text.CENTERED)
			x += 16;
		else{
			x = xx + 48;
		}
		Text.drawTextExt(x, y + 1, txt.toUpperCase(), .5, .5, 0);
		Text.idiot = true;
	}

}
