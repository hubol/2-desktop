package world.enemy;

import audio.Audio;
import graphics.Sprite;
import main.Calc;
import world.control.Global;
import world.control.Sound;
import world.enemy.BasicEnemy;
import world.particles.Sparkle;
import world.player.Player;
import main.Scene;

public class BigUrchin extends BasicEnemy {
	public double bodyTimer;
	public double outImg, patImg;
	public double faceX, faceY, toX, toY;
	
	public double xx, yy;
	
	public double sparkleTimer;
	public double pitch;
	
	public boolean playing;
	public boolean reset;
	
	public final static Sprite SING = Sprite.get("sBigUrchinSing"), FACE = Sprite.get("sBigUrchinFace"), OUTLINE = Sprite.get("sBigUrchinOutline"), PATTERN = Sprite.get("sBigUrchinPattern"), MASK = Sprite.get("sBigUrchinMask");
	private Sprite face;
	
	public BigUrchin(double x, double y) {
		super(x, y);
		orientation=Sprite.CENTERED;
		
		landCheckNecessary = false;
		
		mask=Global.sBLOCK.mask;
		face = FACE;
		
		imageSingle = Calc.random(10);
		imageSpeed=Calc.random(.4);
		
		angle = Calc.rangedRandom(4);
		
		setDepth(-5);
		
		resetCollision();
		updateFace();
		resetBody();
		
		resetSparkle();
		resetPitch();
		
		playing = false;
		reset = true;
		
		toX = Player.me.x;
		toY = Player.me.y;
	}
	
	public void resetPitch(){
		//pitch = 1 + Calc.random(.3);
		pitch = 1 + ((480.0 - y) / 480.0);
	}
	
	public void resetSparkle(){
		sparkleTimer = 2 + Calc.random(8);
		new Sparkle(x + Calc.rangedRandom(20), y + Calc.rangedRandom(20));
	}
	
	public void updateFace(){
		faceX = 4.5 + (toX - x)/135;
		faceY = -4.5 + (toY - y)/90;
	}
	
	public void resetBody(){
		outImg = Calc.random(2);
		patImg = Calc.random(2);
		bodyTimer = Calc.random(120);
	}
	
	public void resetCollision(){
			setCollisionGroup(Global.BASICENEMY, Global.SOLIDBLOCK, Global.DEACTIVATEME);
	}
	
	public void sing(){
		if (reset){
			reset = false;
			resetPitch();
		}
		if (!playing){
			playing = true;
			Audio.get("sBigUrchinSing").setGain(.6);
			Audio.fade("sBigUrchinSing", .6, 1);
			Audio.get("sBigUrchinSing").setPitch(pitch);
			Audio.get("sBigUrchinSing").setLooping(true);
			Sound.play("sBigUrchinSing");
		}
	}
	
	public void step(){
		boolean conditions = false;
		
		//yo if you want to walk on me you gotta get the mothafuckin cool boots
		if(!Global.gotBoots){
			if (Scene.collision(this, x-1, y, Global.PLAYER)||Scene.collision(this, x+1, y, Global.PLAYER)||Scene.collision(this, x, y-1, Global.PLAYER)||Scene.collision(this, x, y+1, Global.PLAYER))
					Player.hurtPlayer(500);
		}
		else if (Scene.collision(this, x, y-1, Global.PLAYER)){
				toX = Calc.approach(toX, x, 12);
				toY = Calc.approach(toY, y - 480, 12);
			
				conditions = true;
				sing();
		}
		else if (Scene.collision(this, x-1, y, Global.PLAYER) && Global.gotWallSlide && Player.me.wallSlide){
			toX = Calc.approach(toX, x - 640, 12);
			toY = Calc.approach(toY, y, 12);
		
			conditions = true;
			sing();
		}
		else if (Scene.collision(this, x+1, y, Global.PLAYER) && Global.gotWallSlide && Player.me.wallSlide){
			toX = Calc.approach(toX, x + 640, 12);
			toY = Calc.approach(toY, y, 12);
		
			conditions = true;
			sing();
		}
		else{
			reset = true;
			if (playing){
				Audio.fade("sBigUrchinSing", 0, .075);
				playing = false;
			}
		}
		
		resetCollision();
		
		face = FACE;
		imageSingle += imageSpeed;
		if (conditions){
			imageSingle += imageSpeed/2;
			face = SING;
			
			xx = Calc.rangedRandom(1);
			yy = Calc.rangedRandom(1);
		}
		else{
			xx = 0;
			yy = 0;
			
			toX = Calc.approach(toX, Player.me.x, 12);
			toY = Calc.approach(toY, Player.me.y, 12);
		}
		
		while (imageSingle >= face.textures.length)
			imageSingle -= face.textures.length;
		
		updateFace();
		
		bodyTimer -= 1;
		if (bodyTimer <= 0)
			resetBody();
		
		sparkleTimer -= 1;
		if (sparkleTimer <= 0)
			resetSparkle();
	}
	
	public void bombDamage(){
		clank();
	}
	
	public void gunDamage(){
		clank();
	}
	
	public void clank(){
	}
	
	public void render(){
		if (!stepActive && playing){
			playing = false;
			Audio.get("sBigUrchinSing").stop();
		}
		
		PATTERN.render(patImg, orientation, x+Calc.rangedRandom(.7) + xx, y+Calc.rangedRandom(.7) + yy, xscale * 1.1, 1.1, angle + Calc.rangedRandom(1), 1, 1, 1, 1);
		MASK.render(0, orientation, x+Calc.rangedRandom(.7) + faceX + xx, y+Calc.rangedRandom(.7) + faceY + yy, xscale, 1, angle + Calc.rangedRandom(1), 1, 1, 1, 1);
		face.render(imageSingle, orientation, x+Calc.rangedRandom(.7) + faceX + xx, y+Calc.rangedRandom(.7) + faceY + yy, xscale, 1, angle + Calc.rangedRandom(1), 1, 1, 1, 1);
		OUTLINE.render(outImg, orientation, x+Calc.rangedRandom(.7) + xx, y+Calc.rangedRandom(.7) + yy, xscale, 1, angle + Calc.rangedRandom(1), 1, 1, 1, 1);
	}

}
