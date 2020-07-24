package world.control;

import graphics.Sprite;

import main.Entity;
import main.Scene;

public class SpriteLoader extends Entity{
	public boolean killYourself = false;
	public String[] mySprites, mySpriteNames;
	public String extension, folder;

	/**load a list of sprites with png format*/
	public SpriteLoader(String... s) {
		super(0, 0);
		extension = ".png";
		mySprites = s;
		folder = "flyImage";
		initialize();
	}
	
	/**load a list of sprites with ANY format!! change the SECOND argument to yr desired format (include the '.' !!!!!!!) [e.g. ".jpg", ".gif", etc]*/
	public SpriteLoader(boolean fuckYou, String ext, String... s) {
		super(0, 0);
		extension = ext;
		mySprites = s;
		folder = "flyImage";
		initialize();
	}
	
	/**load a list of sprites with ANY format in ANY subfolder of res/!! change the SECOND argument to yr desired format (include the '.' !!!!!!!) [e.g. ".jpg", ".gif", etc]*/
	public SpriteLoader(String subfolder, boolean fuckYou, String ext, String... s) {
		super(0, 0);
		extension = ext;
		mySprites = s;
		folder = subfolder;
		initialize();
	}
	
	/**i hate my life!!!!!!!!!!*/
	public void initialize(){
		killYourself = false;
		visible = false;

		mySpriteNames = new String[mySprites.length];
		for (int i=0; i<mySprites.length; i++){
			Sprite.loadFile(mySprites[i] + extension,"res/"+folder+"/",0);
			
			if (Scene.console)
				System.out.println("temp sprite loaded: "+mySprites[i]);
			
			int lastUnderscore = mySprites[i].lastIndexOf('_');
			if (lastUnderscore == -1)
				mySpriteNames[i] = mySprites[i];
			else
				mySpriteNames[i] = mySprites[i].substring(0,lastUnderscore);
		}
	}
	
	public void step(){
		kill();
	}
	
	public void kill(){
		if (killYourself){
			for (int i=0; i<mySprites.length; i++){
				if (Sprite.exists(mySpriteNames[i]))
					Sprite.get(mySpriteNames[i]).remove();
			}
			super.destroy();
		}
	}
	
	public void destroy(){
		killYourself = true;
		kill();
	}
	
	public void roomDestroy(){
		destroy();
	}

}
