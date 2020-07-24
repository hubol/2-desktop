package world.adventure;

import graphics.Font;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import world.control.SoundLoader;
import world.control.SpriteLoader;

import main.Calc;
import main.Entity;
import main.Main;

public class Adventure extends Entity{
	public String[] title, text;
	public int[] attach, attachments;
	public int select, subSelect;
	
	public Font font;
	public SpriteLoader mySprites;
	public SoundLoader mySounds;

	public Adventure(double x, double y, String z) {
		super(x, y);
		
		font = new Font("adventureFont", true, '-');
		mySprites = new SpriteLoader("sChooseChoicebox", "sChooseTextbox", "sMinigameOver","adventureFont");
		mySounds = new SoundLoader("sAdventureConfirm","sAdventureSelect","sAdventureHighlight","sAdventureType");
		
		try {
			File f = new File(Main.DIRECTORY, "res/rooms/"+z+".fuck");
			FileReader w;
		
			w = new FileReader(f);
			BufferedReader p = new BufferedReader(w);
			
			int amount = Calc.parseInt(p.readLine());
			
			title = new String[amount];
			text = new String[amount];
			attach = new int[amount * 3];
			attachments = new int[amount];
			
			for (int i=0; i<amount; i++){
				int mine = Calc.parseInt(p.readLine());
				title[mine] = p.readLine();
				text[mine] = p.readLine();
				attachments[i] = 0;
				for (int j=0; j<3; j++){
					int a = Calc.parseInt(p.readLine());
					attach[Calc.getGridPosition(mine, j, amount)] = a;
					if (a != -1)
						attachments[i] += 1;
				}
			}
			
			p.close();
			w.close();
		} 
		catch (Exception e) {
			System.err.println("adventure parsing fail: "+z);
			e.printStackTrace();
		}
		
		select = 0;
		subSelect = 0;
	}

}
