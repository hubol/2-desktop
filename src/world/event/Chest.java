package world.event;

import graphics.Sprite;
import world.control.EV;
import world.control.Global;
import world.control.Hud;
import world.control.MapIconInfluence;
import world.control.Music;
import world.control.Sound;
import world.control.SpriteLoader;
import world.junk.TweetReview;
import world.particles.Sparkle;
import world.player.Player;
import main.Calc;
import main.Scene;

public class Chest extends MapIconInfluence{
	public boolean opened, opening;
	public int id, openTimer;
	
	public double sparkleTimer;
	public String item;
	
	public double moveUp;
	public TweetReview review;

	public Chest(double x, double y, int i) {
		super(x, y);
		setCollisionGroup(Global.INTERACTABLE, Global.DEACTIVATEME, Global.SOLIDBLOCK);
		setDepth(2);
		
		orientation = Sprite.SOUTH;
		mask = Sprite.get("sBedMask").mask;
		
		opened = false;
		opening = false;
		id = i;
		
		moveUp = 0;
		
		openTimer = -1;
		
		//item check stuff
		String string = "";
		item = "";
		if (id == 0){
			item = "sCutsceneStench";
			string = "i #smell really bad! #frowning, #nosed #urchins will promptly explode when i get near them!!! :vc";
			if (Global.gotStench)
				opened = true;
		}
		else if (id == 1){
			item = "sCutsceneCoolBoots";
			string = "these #coolboots are cute and expensive! now i can stand on those big colorful #urchins!!!";
			if (Global.gotBoots)
				opened = true;
		}
		else if (id == 2){
			item = "sCutsceneJellyMap";
			moveUp = -48;
			string = "this artifically flavored map #upgrade allows me to press #interact on the pause map to display #door connections!!! wooo!!";
		}
		else if (id == 3){
			item = "sCutsceneBloodyMap";
			if (Global.gotMapIcons)
				opened = true;
			string = "this unsanitary map #upgrade allows me to press #interact on the pause map to display cute #icons!!!!! wowowow!!!";
		}
		else if (id == 4){
			item = "sCutsceneBomb";
			if (Global.gotBombs)
				opened = true;
			string = "i found some adorable #cherrybombs!!! i can press #power to place one!!!!! they're supe #dangerous aah!!!";
		}
		else if (id == 5){
			item = "sCutsceneSnorkel";
			if (Global.event[EV.GOT_SNORKEL] == 1)
				opened = true;
			string = "got a #snorkel!!!! perfect for diving in pools of #blood!!!!!";
		}
		else if (id == 6){
			item = "sCutsceneBanner";
			if (Global.gotWallSlide)
				opened = true;
			string = "i got the enchanted #banner!!! i can now #jump off of walls while #sliding!!! hella cool!!!";
			
		}
		else if (id == 7){
			item = "sCutsceneGun";
			if (Global.gotGun)
				opened = true;
			string = "got a cute knitted #stocking that fires #ammo!! it reloads whenever i enter a room!! i can swap weapons with #interact!!";
		}
		else if (id == 8){
			item = "sCutsceneClover";
			if (Global.gotClover)
				opened = true;
			string = "i plucked a cute #clover!!!! my #luck has increased by 55 percent!!!!! whatever that means!!!";
		}
		else if (id == 9){
			item = "sCutsceneBombUp";
			if (Global.event[EV.BOMBUPGRADE] == 1)
				opened = true;
			string = "i got the adorable #cherrybomb upgrade!! this #upgrade makes my #bombs both cooler and #stackable!! i can also stand on them!! #amazing!!";
		}
		else if (id == 10){
			item = "sCutsceneBulb";
			if (Global.event[EV.BULB] == 1)
				opened = true;
			string = "i found the #bombastic bulb!! this bulb will reveal hidden #terrain!! wow!!";
		}
		
		if (!opened)
			new SpriteLoader("sCutsceneChestAction_2", "sChestStars_2","sCutsceneChest_8","sCellPhone_3", "sCellPhoneNewTweet", "sCellThumbs_6");
		else
			review(string);
		
		//xscale shit
		if (id == 0 || id == 2)
			xscale = -1;
		
		resetSparkle();
	}
	
	public void review(String s){
		if (!Global.room(17,0))
			review = new TweetReview(x, y - 90, s);
		else
			review = new TweetReview(x + 100, y - 90, s);
	}
	
	public void step(){
		//MOVING!!!!
		if (moveUp != 0){
			double div = .5;
			
			y += Math.signum(moveUp) * div;
			double sign = Math.signum(moveUp);
			moveUp += -Math.signum(moveUp) * div;
			if (sign != Math.signum(moveUp)){
				y -= moveUp;
				moveUp = 0;
			}
		}
		
		sparkleTimer -= 1;
		if (sparkleTimer <= 0 && !opened)
			resetSparkle();
		
		openTimer -= 1;
		if (openTimer == 0){
			ChestScene i = new ChestScene(0,0, item);
			i.mother = this;
		}
		
		if (!opened)
			mask = Sprite.get("sBedMask").mask;
		else
			mask = Sprite.get("sChestOpenMask").mask;
		
		if (!opened && !opening && Player.me.vspeed == 0 && Scene.collision(this, x, y - 1, Global.PLAYER)){
			Global.drawDown(x, y-70);
			if (Player.canDownInteract()){
				Sound.playPitched("sChestBegin",.05);
				Music.fadeMusic("musChest", false);
				
				opening = true;
				
				Hud.hideAll();
				
				openTimer = 15;
				
				Player.control = false;
				Player.me.cutMove = true;
				
				if (xscale == -1){
					Player.me.cutRight = true;
					Player.me.cutToX = x + 48;
				}
				else{
					Player.me.cutRight = false;
					Player.me.cutToX = x - 48;
				}
			}
		}
		
		if (review != null && opened)
			review.on = Player.me.vspeed == 0 && Scene.collision(this, x, y - 1, Global.PLAYER);
	}
	
	public void refreshIcon(){
		if (!isDestroyed() && !Global.dream && !opened)
			Global.setIconMap(Global.roomX, Global.roomY, 8);
	}
	
	public void resetSparkle(){
		sparkleTimer = 5 + Calc.random(15);
		Sparkle i = new Sparkle(x + Calc.rangedRandom(32), y - Calc.random(32));
		i.setDepth(1);
	}
	
	public void render(){
		int img = 0;
		if (opened)
			img = 3;
		
		Sprite.get("sChest").render(img + 2, Sprite.SOUTH, x + Calc.rangedRandom(.7), y + Calc.rangedRandom(.7), xscale, 1, 0, 1, Global.roomColor);
		Sprite.get("sChest").render(img + 1, Sprite.SOUTH, x + Calc.rangedRandom(.7), y + Calc.rangedRandom(.7), xscale, 1, 0, 1, 1, 1, 1);
		Sprite.get("sChest").render(img, Sprite.SOUTH, x + Calc.rangedRandom(.7), y + Calc.rangedRandom(.7), xscale, 1, 0, 1, 1, 1, 1);
	}

}
