package world.gameplay;

import world.control.Global;
import world.control.Hud;
import world.control.MapIconInfluence;
import world.control.Sound;
import world.dream.DreamHeartParticles;
import world.end.EndControl;
import world.junk.FalseHeart;
import world.particles.HeartParticles;
import world.player.Player;
import graphics.Sprite;
import main.Calc;
import main.Scene;

public class Heart extends MapIconInfluence{
	public int me;
	public double ang, img, ax;
	
	public double sparkleTimer;

	public Heart(double x, double y){
		super(x, y);
	}
	
	public Heart(double x, double y, int id) {
		super(x, y);
		
		if (Global.roomX == 28 && Global.roomY == 0 && y <= 128)
			destroy();
		
		if (!Global.heartGot[id]){
			me = id;
			setCollisionGroup(Global.HEART);
			addCollisionGroup(Global.ITEM, Global.DEACTIVATEME);
			setDepth( -2 );
			orientation = Sprite.CENTERED;
			sprite = Global.sHEART;
			mask = sprite.mask;
			ax = 0;
			
			resetSparkle();
		}
		else{
			stepActive = false;
			visible = false;
			destroy();
			
			if (Global.room(29, 0))
				new FalseHeart(x, y);
		}
	}
	
	public void resetSparkle(){
		sparkleTimer = 4 + Calc.random(8);
		//new Sparkle(x + Calc.rangedRandom(12), y + Calc.rangedRandom(12));
	}
	
	public void step(){
		if (Global.roomX == 21 && Global.dream)
			new DreamHeartParticles(x, y, Calc.random(360),-1);
		
		img += 4.0 / 30.0;
		if (img >= 2)
			img -= 2;
		ang += 1;
		
		ax += 1;
		y = ystart + (4 * Math.sin(ax/15));
		
		sparkleTimer -= 1;
		if (sparkleTimer <= 0)
			resetSparkle();
		
		if (Calc.pointDistance(x, y, Player.me.x, Player.me.y) < 28)
			collect();
		
		if (Global.room(3, 6) || Global.room(1, 4)){
			if (Scene.collision(this, x, y, Global.PLAYER))
				collect();
		}
	}
	
	public void collect(){
		if (!Global.heartGot[me]){
			
			Global.heartGot[me] = true;
			Global.playerMaxHealth += 10;
			Global.playerHealth += 10;
			
			if (((me >= 14 && me <= 21) || (me >= 24 && me <= 25) || (me >= 27 && me <= 29) || me == 71) && me != 17)
				Global.clearDream();
			
			if (me == 71){
				Player.control = false;
				Player.me.cutMove = false;
				Player.me.cutRight = false;
				Player.me.cutToX = Player.me.x + 1;
				new EndControl();
			}
			
			if (me == 17 && Global.gotStench)
				Global.clearDream();
			
			String s = "";
			int h = (Global.playerMaxHealth - 90)/10;
			
			if ((Boolean)Calc.choose(true,true,true,false)){
				s = (String)Calc.choose("hey ","whoa ","cool ","oh shit ","oh yeah ","oh yeaaaah ", "oh yea ", "shit yeah ", "","","");
				s += (String)Calc.choose("i ", "");
				s += (String)Calc.choose("found ", "picked up ","got ","acquired ","motherfuckin obtained ","swallowed ","found ","pickdd up ", "got ");
				if (Global.playerMaxHealth > 100)
					s += (String)Calc.choose("that ", "a ", "another ", "that ", "a ", "another ", "'nother ");
				else
					s += (String)Calc.choose("that ", "a ");
				s += (String)Calc.choose("motherfuckin ","cool ass ", "sweet ass ", "delicate little ", "precious ", "cute ", "god damn ", "pretty little ", "", "", "", "", "");
				s += "heart" + (String)Calc.choose("!", "", "", "", "", "", ".", " !");
			}
			else{
				if ((Boolean)Calc.choose(true,false)){
					s = (String)Calc.choose("#heartflow","#teamhearts","#heartsfordays");
					s += (String)Calc.choose(" !!!", " !!!!!", "!!!!!", "!!!");
				}
				else{
					s = "";
					if ((Boolean)Calc.choose(true,false,false))
						s = "#" + (String)Calc.choose("got ","found ");
					s += ""+h;
					if ((Boolean)Calc.choose(true,true,true,true,true,true,true,true,false))
						s += " ";
					s += (String)Calc.choose("hearts!!!","heart's!!!!!!","th heart !","heart");
				}
			}		
			
			Global.addTweet(s);

			Hud.showHealth();
			Sound.heartPlay();

			for (int i=0; i<16; i++){
				new HeartParticles(x,y);
			}
			Global.squareParticle(x, y, 16, "#ff2879", 4);
			destroy();
			Global.refreshIconMap();
		}
	}
	
	public void refreshIcon(){
		if (!isDestroyed())
			Global.setIconMap(Global.roomX, Global.roomY, 4);
	}
	
	public final static Sprite GLOW = Sprite.get("sItemGlow");
	
	public void shabip(double xx, double yy){
		for (int i=0; i<4; i++)
			GLOW.render((int)Calc.loopBounds(img + i, 0, 2), Sprite.WEST, x + Calc.rangedRandom(Calc.random(1)) + xx, y + Calc.rangedRandom(Calc.random(1)) + yy, .25, .8, ang + (i * 90), 1, "#ff2879");
	}
	
	public void drawGlow(){
		//if (stepActive){
			shabip(0, 0);
			if (Player.me.vLoop && !Global.room(18, 0)){
				shabip(0, 480);
				shabip(0, -480);
			}
			if (Player.me.hLoop){
				shabip(640, 0);
				shabip(-640, 0);
			}
		//}
	}
	
	public void bippity(int i, double xx, double yy){
		sprite.render(i, orientation, x + Calc.rangedRandom(Calc.random(1)) + xx, y + Calc.rangedRandom(Calc.random(1)) + yy, 1, 1, 0, 1, "ffffff");
	}
	
	public void render(){
		int i = (int)imageSingle;
		if (i == 0)
			i = (Integer)Calc.choose(0,2,4,6);
		else
			i = (Integer)Calc.choose(1,3,5,7);
		
		bippity(i, 0, 0);
		if (Player.me.vLoop && !Global.room(18, 0)){
			bippity(i, 0, 480);
			bippity(i, 0, -480);
		}
		if (Player.me.hLoop){
			bippity(i, 640, 0);
			bippity(i, -640, 0);
		}
	}

}
