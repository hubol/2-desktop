package world.turkey;

import java.util.ArrayList;

import audio.Audio;

import world.control.Global;
import world.control.Sound;
import world.control.SoundLoader;
import world.control.SpriteLoader;
import world.event.SmallMessage;
import world.interact.BasicNpc;
import world.particles.GlitchBlood;
import world.particles.JumpDust;
import world.player.Player;
import main.Calc;
import main.Scene;
import graphics.Sprite;

public class Turkey extends BasicNpc{
	//customizations
	public Sprite face, feather;
	public String col;
	public double speed;
	public String speech;
	
	//internal things
	public int moveTimer;
	
	//constants
	public final int NEUTRAL = 0, HAPPY = 1, SAD = 2;
	
	//animation
	public int emotion = NEUTRAL;
	
	public double a;
	public double b;
	
	public boolean lastSad;
	public double sadImage;
	
	//featherstuff
	public double distMult;
	public double scaleMult;
	
	public double breathe;
	
	public int legImage;
	public int lastLeg;
	public double legHspeed;
	
	public double bodY;
	public double allY;
	
	public double desH;
	
	public int jumpTimer;
	public int hurt;
	
	public boolean should;
	
	public boolean obey;
	public ArrayList<Integer> emotions;
	
	public ArrayList<String> yelps;

	public Turkey(double x, double y, String face, String feather, String color, String speech, double speed) {
		super(x, y);
		new SpriteLoader(face + "_3", feather);
		this.face = Sprite.get(face);
		this.feather = Sprite.get(feather);
		this.col = color;
		this.speech = speech;
		this.speed = speed;
		
		lastSad = false;
		
		initialize();
		
		/*emotion = HAPPY;
		hspeed = 0;
		desH = 0;*/
	}
	
	public Turkey(double x, double y){
		super(x, y);
		initialize();
	}
	
	public void initialize(){
		hurt = -1;
		
		yelps = new ArrayList<String>();
		
		lastLeg = 0;
		
		desH = 0;
		
		if (this.speed == 0)
			moveTimer = -1;
		else{
			double a = Player.me.random(1);
			desH = 0;
			if (a >= .67)
				desH = speed;
			else if (a >= .33)
				desH = -speed;
			moveTimer = 1 + (int)Player.me.random(60);
		}
		
		bubbleX = 0;
		bubbleY = (ystart - y)-88;
		
		talkingDistance = 32;
		
		orientation = Sprite.CENTERED;
		mask = Sprite.get("sPlayerMask").mask;
		sprite = Sprite.get("sPlayerMask");
		
		a = 0;
		b = 0;
		
		distMult = 1;
		scaleMult = 1;
		
		breathe = 1;
		
		jumpTimer = -1;
		
		bodY = 0;
		allY = 0;
		
		setDepth(3);
		
		setYelps("ow!","ouch!","oof!","yow!");
		
		should = true;
		
		obey = false;
		emotions = new ArrayList<Integer>();
		
		if ((Global.roomX == 14 || Global.roomY == 15) && Global.roomY == 1)
			should = false;
	}
	
	public void setEmotions(int... i){
		obey = true;
		emotions.clear();
		for (int j=0; j<i.length; j++)
			emotions.add(i[j]);
	}
	
	public void setYelps(String... s){
		yelps.clear();
		for (int i=0; i<s.length; i++)
			yelps.add(s[i].toUpperCase());
	}
	
	/**checks if there is a solid block, upblock, or invisible turn block in the direction you are moving. if so, it flips you around. funky town*/
	public boolean turning(){
		double hSign = Math.signum(hspeed);
		
		if(Scene.collision(this, Math.round(x)+hspeed+hSign, y, Global.SOLIDBLOCK, Global.UPBLOCK, Global.TURNER))
		{
			double fa = hspeed;
			hspeed=0;
			x = Math.round(x);
			while(!Scene.collision(this, x+hSign, y, Global.SOLIDBLOCK, Global.UPBLOCK, Global.TURNER))
				x+=hSign;
			hspeed = fa*-1;
			
			desH = hspeed;
		}
		
		return (Scene.collision(this, Math.round(x)+hspeed+hSign, y, Global.SOLIDBLOCK, Global.UPBLOCK, Global.TURNER));
	}
	
	public void step(){
		if (should){
			if (!Audio.soundExists(speech))
				new SoundLoader(speech);
			should = false;
		}
		
		hurt -= 1;
		if (hurt > 0)
			visible = !visible;
		else
			visible = true;
		
		if (hurt <= 0)
			messageStep();
		
		if (this.speed != 0){		
			if (!active){
				turning();
				
				moveTimer -= 1;
				if (moveTimer == 0){
					double a = Player.me.random(1);
					desH = 0;
					if (a >= .67)
						desH = speed;
					else if (a >= .33)
						desH = -speed;
					moveTimer = 1 + (int)Player.me.random(60);
				}
			}
			
			if (vspeed != 0){
				vspeed += .8;
			}
			
			if (emotion == HAPPY && desH == 0){
				//desH = 0;
				jumpTimer -= 1;
				
				if (jumpTimer < 0)
					jump();
			}
			
			super.step();
			
			if (y >= ystart && vspeed > 0){
				Sound.playPitched("sTurkeyLand");
				lastLeg = 0;
				jumpTimer = 2;
				vspeed = 0;
				y = ystart;
			}
			
			if (hspeed != 0)
				xscale = Math.signum(hspeed);
		}
		
		hspeed = Calc.approach(hspeed, desH, 5.0);
		
		//adjust leg images!!!!!
		if (desH == 0)
			legHspeed = 0;
		else{
			legHspeed += Math.abs(hspeed / 10.0);
			while (legHspeed >= 2)
				legHspeed -= 2;
		}
		
		boolean fuck = false;
		
		if (vspeed != 0)
			legImage = 1;
		else{
			legImage = (int)legHspeed;
			fuck = true;
		}
		
		if (legImage != lastLeg && legImage == 0)
			Sound.playPitched("sTurkeyStep");
		lastLeg = legImage;
		//end of leg!!!!!!
		
		a += 1;
		if (emotion == HAPPY)
			a += 1;
		else if (emotion == SAD)
			a -= .5;
		b = Math.sin(a / 30.0);
		
		breathe = 1.05 + (Math.sin(a / 90) * .05);
		double jesus = 1;
		if (fuck && legImage == 1)
			jesus = 1.001;
		
		if (emotion == NEUTRAL){
			distMult = Calc.approach(distMult, breathe, 15)*jesus;
			scaleMult = Calc.approach(scaleMult, breathe, 15);
		}
		else if (emotion == HAPPY){
			distMult = Calc.approach(distMult, (1.05 + (b * .05)) * breathe*jesus, 15);
			scaleMult = Calc.approach(scaleMult, (1.05 + (b * .05)) * breathe, 15);
		}
		else if (emotion == SAD){
			distMult = Calc.approach(distMult, (1.05 + (b * .05)) * breathe*jesus, 15);
			scaleMult = Calc.approach(scaleMult, (.9 + (b * .05)) * breathe, 15);
		}
		
		allY = 0;
		
		double g = 0;
		if (emotion == SAD)
			g = 5;
		if (fuck && legImage == 1)
			g += 2;
		
		bodY = Calc.approach(bodY, g, 4);
		
		if (fuck && legImage == 1)
			allY -= 5;
		
		bubbleY = (ystart - y) - 88;
		
		if (emotion == SAD && lastSad == false)
			sadImage = 0;
		lastSad = (emotion == SAD);
		sadImage += .1;
		while (sadImage >= 3)
			sadImage -= 3;
	}
	
	public void jump(){
		if (y == ystart && vspeed == 0){
			vspeed = -6.11;
			Sound.playPitched("sTurkeyJump");
			for (int i=0; i < 8; i++){
				JumpDust j = new JumpDust(x + Calc.rangedRandom(16), y + 16);
				j.setDepth(2);
			}
		}
	}
	
	public void render(){
		double ang = 15 * distMult * xscale;
		for (int i=0; i<8; i++){
			feather.render(0, Sprite.SOUTH, x + Calc.rangedRandom(.3), y + allY + Calc.rangedRandom(.3) + bodY, scaleMult * .8, scaleMult, ang, 1, 1, 1, 1);
			
			if (Math.abs(ang) != ang * xscale)
				ang -= 30 * distMult * xscale;
			ang *= -1;
		}
		
		
		//bod shit
		Sprite s = Sprite.get("sTurkeyBody");
		int o = Sprite.CENTERED;
		
		//leg
		s.render(1 + legImage, o, x + Calc.rangedRandom(.3), y + allY  + Calc.rangedRandom(.3), 1 * xscale, 1.01, 0, 1, 1, 1, 1);
		
		//main bod
		s.render(3, o, x + Calc.rangedRandom(.3), y + allY  + Calc.rangedRandom(.3) + bodY, 1.04 * xscale, 1.04, 0, 1, col);
		s.render(0, o, x + Calc.rangedRandom(.3), y + allY  + Calc.rangedRandom(.3) + bodY, 1 * xscale, 1.01, 0, 1, 1, 1, 1);
		face.render(emotion, o, x + Calc.rangedRandom(.3), y + allY  + Calc.rangedRandom(.3) + bodY, 1 * xscale, 1.01, 0, 1, 1, 1, 1);
		
		if (emotion == SAD)
			Sprite.get("sTurkeyCry").render(sadImage, Sprite.CENTERED, x + Calc.rangedRandom(.3), y + Calc.rangedRandom(.3), 1, 1, 0, 1, 1, 1, 1);
	}
	
	public void initConversationEvent(){
		hspeed = 0;
		desH = 0;
		moveTimer = -1;
	}
	
	public void endConversationEvent(){
		emotion = NEUTRAL;
		if (this.speed != 0)
			moveTimer = 1 + (int)Player.me.random(90);
	}
	
	public void showMessage(int id){
		if (obey && id < emotions.size())
			emotion = emotions.get(id);
		Sound.playPitched(speech);
		super.showMessage(id);
	}

	public void gunDamage(){
		pain();
	}
	
	public void bombDamage(){
		pain();
	}
	
	public void pain(){
		if (hurt <= 0){
			jump();
			
			desH = 0;
			
			Sound.playPitched(speech);
			new SmallMessage(this, 0, -18, yelps.get((int)Calc.random(yelps.size())), 10);
			hurt = 20;
			
			for (int i=0; i<6; i++)
				new GlitchBlood(x + Calc.rangedRandom(8), y - 16);
			
			for (int i=0; i<4; i++)
				Global.squareParticle(x + Calc.rangedRandom(16), y + Calc.rangedRandom(16), 3 + (int)Calc.random(5), "FF1E56", 1.5 + Calc.random(5));
		}
	}
}
