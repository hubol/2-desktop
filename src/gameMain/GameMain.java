package gameMain;

import java.awt.Color;

import world.Root;
import main.Calc;
import main.GameEngineApplet;
import main.GameStartListener;
import main.Main;
import main.RuntimeFixer;
import main.Scene;

public class GameMain extends GameEngineApplet {
	private static final long serialVersionUID = 2864092535021560372L;
	public static final boolean SCREENSHOT = false;

	@Override
	public void initializeApplet() {
		setup();
	}
	
	private static void setup() {
		System.setErr(new ErrorHandler().getStream());
		
		Main.preferredWidth=720;
		Main.preferredHeight=540;
		Main.preferredProjectionWidth = 640;
		Main.preferredProjectionHeight = 480;
		Main.loadText = Color.white;  
		Main.loadFront = Color.decode("#0095CF");
		Main.loadBack = Color.white;
	    Main.loaderImageFileName = ("res/load"+(int)Calc.random(55)+".png");
	    Main.icon = "res/icon"+(int)Calc.random(10)+".png";
	    Main.title = "2";
		RuntimeFixer.DEMANDED_MEMORY = 512; //TODO ?????
		  
		
		Scene.setGameStarter(new GameStartListener(){
			@Override
			public void gameInitialized() { 
				new Root(0,0);
			}
		});
	} 
 
	public static void main(String[] args){
		setup();
		Main.main(args);
	}
}