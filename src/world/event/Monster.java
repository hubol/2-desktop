package world.event;

import audio.Audio;
import world.Fg;
import world.control.EV;
import world.control.Global;
import world.control.Shake;
import world.control.Sound;
import world.control.SoundLoader;
import world.control.SpriteLoader;
import world.player.Player;
import graphics.Graphics;
import graphics.Sprite;
import main.Calc;
import main.Entity;
import main.Scene;

public class Monster extends Entity{
	public double wingAmount;
	public int bodyImage, eyeImage;
	public double wingImage;
	public boolean standard;
	public final double jointLength = 32;
	public Shake shake;
	public static Monster me;
	
	public double[] angles;
	public boolean forceStand, begun, animate;
	
	public boolean fly;
	public double shit, u;
	
	public boolean guy, nomore;
	public MonsterBack back;

	public Monster(double x, double y) {
		super(x, y);
		
		new SpriteLoader("sMonster_4", "sMonsterLeg", "sMonsterWing_2");
		
		mask = Global.sBLOCK.mask;
		sprite = Sprite.get("sMonster");
		
		me = this;
		
		new NoOccupy(x - 16, y - 16);
		
		imageSingle = 0;
		imageSpeed = 0;
		
		guy = false;
		nomore = false;
		
		bodyImage = 0;
		eyeImage = 0;
		wingImage = 0;
		
		forceStand = false;
		fly = false;
		
		shit = 32;
		u = 0;
		
		wingAmount = 0;
		animate = false;
		if (Global.event[EV.MONSTER_WINGED] == 1){
			wingAmount = 1.0;
			animate = true;
		}

		standard = true;
		
		setDepth(-3);
		setCollisionGroup(Global.DEACTIVATEME, Global.SOLIDBLOCK, Global.MONSTER, Global.INTERACTABLE);
		
		alarmInitialize(5);
		
		if (Global.event[EV.MONSTER_WINGED] == 0){
			new SpriteLoader("sBagSpill_12", "sBag");
			new SoundLoader("sMonsterBlink", "sBagShake", "sBagSpill", "sBagIn", "sMonsterGrow");
		}
		else
			new SoundLoader("sMonsterBlink");
		
		alarm[0] = 45;
		
		if (Global.roomY != 4){
			Audio.get("sTone").setPitch(1.0);
			Audio.fade("sTone", .3, .3);
			Audio.get("sTone").setGain(.3);
		}
		
		shake = new Shake();
		
		angles = new double[4];
		for (int i=0; i<4; i++)
			angles[i] = 0;
		
		double h = (ystart - y) / 2;
		final double aprch = Calc.pointDirection(0, 0, Math.sqrt(Math.pow(jointLength, 2) - (Math.pow(h, 2))), h);
		angles[0] = aprch;
		angles[1] = (360 - aprch + 180);
		angles[2] = 180 + (360 - aprch);
		angles[3] = 360 - (180 + (360 - aprch)) + 180;
		
		back = new MonsterBack(this);
	}
	
	/**call this when entering the room above where u sleep!!!*/
	public void enterRoomFlying(){
		x = 320;
		y = 512;
		ystart = 1024;//idk
		fly = true;
		standard = false;
		shit = 32;
		u = 0;
		vspeed = -5;
		
		Audio.fade("sTone", 0, .005);
	}
	
	public void alarmEvent(int i){
		if (i == 0){
			blink(false);
			//blink
		}
		else if (i == 1){
			eyeImage = 0;
			//unblink
		}
		else if (i == 2){
			bodyImage = 0;
		}
	}
	
	public void sound(String s, double r){
		if (Audio.soundExists(s))
			Sound.playPitched(s, r);
	}
	
	public void blink(boolean hold){
		eyeImage = 1;
		sound("sMonsterBlink", .1);
		if (!hold){
			alarm[0] = 30 + (int)Calc.random(60);
			alarm[1] = 3 + (int)Calc.random(3);
		}
	}
	
	public void step(){
		//all vspeed altering things *MUST* occur here!!!!
		
		if (animate){
			wingImage += 1.0 / 30.0;
			if (wingImage >= 2)
				wingImage -= 2;
		}
		
		if (Calc.pointDistance(xstart, ystart, Player.me.x, Player.me.y) < 128 && Global.butterfliesCaught() == 50 && Global.event[EV.MONSTER_WINGED] == 0 && !begun){
			begun = true;
			forceStand = true;
			new BagGive(Player.me.x, ystart - 70, this);
			Player.control = false;
			//TODO winging scene
		}
		
		if (standard && !fly){
			if ((Calc.pointDistance(xstart, ystart, Player.me.x, Player.me.y) < 128 && wingAmount < 1.0) || forceStand){
				if (vspeed > 0)
					vspeed = 0;
				vspeed = Calc.approach(vspeed, -7, 6);
			}
			else
				vspeed = Calc.approach(vspeed, 10, 20);
			
			if (y + vspeed < ystart - 64)
				vspeed = (ystart - 64) - y;
			else if (y + vspeed > ystart)
				vspeed = ystart - y;
		}
		
		alarmStep();
		
		boolean stand = (Scene.collision(this, x, y + vspeed - 1, Global.PLAYER) && !nomore);
		
		if (fly && stand){
			final double a = .008;
			Global.storedPitch += a;
			Audio.get("sTone").setPitch(1.0 + Global.storedPitch);
		}
		
		if (stand && wingAmount > 0 && !fly){
			shit = 32;
			fly = true;
			vspeed = -1.4;
			bodyImage = 1;
			alarm[2] = 7;
			
			Global.storedPitch = 0;
			
			Audio.get("sTone").setLooping(true);
			Audio.get("sTone").setPitch(1.0);
			Audio.fade("sTone", .3, .3);
			Audio.get("sTone").setGain(.3);
			Audio.fade("sTone", 1, .005);
			Sound.play("sTone");
			//TODO fly begin sound!!
		}
		
		//step!
		xprevious = x;
		yprevious = y;
		
		x += hspeed;
		y += vspeed;
		//end step!
		
		if (stand && !nomore){
			if (vspeed < 0)
				Player.me.y += vspeed;
			
			//if (Player.me.vspeed >= 0){
				//Player.me.vspeed = 0;
				Player.me.y = y - 32;
			//}
		}
		
		if (fly){
			if (y <= ystart - 64){
				standard = false;
				vspeed = Calc.approach(vspeed, -5, 40);
				u += 1;
				shit = Calc.approach(shit, 24 + Math.abs(8 * Math.cos(u / 15.0)), 2);
			}
			else{
				vspeed = Calc.approach(vspeed, -2.2, 25);
			}
		}
		
		if (!stand && fly){
			Audio.fade("sTone",0,.0125);
		}
		
		if (fly && stand){
			Fg.me.shakeMultiplier = .35;
			Fg.me.shakeTimer = 5;
		}
		
		if (standard){
			if (y <= ystart - 64 || y >= ystart)
				vspeed = 0;
		}
		
		guy = stand;
		
		if (Global.roomY == 4 && y <= 192 && !nomore){
			nomore = true;
			setCollisionGroup(Global.DEACTIVATEME);
			setDepth(3);
			back.setDepth(4);
		}
		
		if (y <= -96)
			destroy();
	}
	
	public void destroy(){
		thing();
		super.destroy();
	}
	
	public void roomDestroy(){
		thing();
		super.roomDestroy();
	}
	
	public void thing(){
		me = null;
		if (!guy && fly){
			Audio.get("sTone").stop();
			Audio.get("sTone").setPitch(1.0);
			Audio.fade("sTone", .3, .3);
			Audio.get("sTone").setGain(.3);
		}
	}
	
	public static void drawLine(double x1, double y1, double x2, double y2, double width){
		Sprite.get("sMonsterLeg").render(0, Sprite.WEST, x1, y1, Calc.pointDistance(x1, y1, x2, y2)/96, width/16, Calc.pointDirection(x1, y1, x2, y2), Graphics.alpha, 1, 1, 1);
	}
	
	public void backRender(){
		//this is atrocious
		double x = this.x + Calc.rangedRandom(.4) + (shake.x / 2.5), y = this.y + Calc.rangedRandom(.4) + (shake.y / 2.5);
		
		//wings
		Sprite s = Sprite.get("sMonsterWing");
		s.renderPart(wingImage, Sprite.NORTHWEST, x + 5, y - (s.imageHeight / 2.0), 0, 0, s.imageWidth * wingAmount, 108, 1, 1, 0, 1, "FFFFFF");
		s.renderPart(wingImage, Sprite.NORTHWEST, x - 5, y - (s.imageHeight / 2.0), 0, 0, s.imageWidth * wingAmount, 108, -1, 1, 0, 1, "FFFFFF");
		
		Graphics.setColor("007CFF");
		Graphics.setAlpha(1);
		
		final double thighGap = 16, lineWidth = 5.8;
		double h = (ystart - this.y) / 2;
		if (!standard)
			h = shit; //TODO
		final double aprch = Calc.pointDirection(0, 0, Math.sqrt(Math.pow(jointLength, 2) - (Math.pow(h, 2))), h);
		final double spd = 2.2;
		
		//legs!!
		//if (standard){
		//rtop
		angles[0] = Calc.angleApproach(angles[0], aprch, spd);
		//rbot
		angles[1] = Calc.angleApproach(angles[1], (360 - aprch + 180) % 360, spd);
		//ltop
		angles[2] = angles[1];//Calc.angleApproach(angles[2], (180 + (360 - aprch)) % 360, spd);
		//lbot
		angles[3] = angles[0];//Calc.angleApproach(angles[3], (360 - (180 + (360 - aprch)) + 180) % 360, spd);
		
		/*angles[0] = aprch;
		angles[1] = (360 - aprch + 180);
		angles[2] = 180 + (360 - aprch);
		angles[3] = 360 - (180 + (360 - aprch)) + 180;*/
		//}
		
		//rtop
		drawLine(x + (thighGap / 2.0), y + 16, x + (thighGap / 2.0) + Calc.dirX(jointLength, angles[0]), y + 16 + Calc.dirY(jointLength, angles[0]), lineWidth);
		//rbottom
		drawLine(x + (thighGap / 2.0) + Calc.dirX(jointLength, angles[0]), y + 16 + Calc.dirY(jointLength, angles[0]), x + (thighGap / 2.0) + Calc.dirX(jointLength, angles[0]) + Calc.dirX(jointLength, angles[1]), y + 16 + Calc.dirY(jointLength, angles[0]) + Calc.dirY(jointLength, angles[1]), lineWidth);
		//ltop
		drawLine(x - (thighGap / 2.0), y + 16, x - (thighGap / 2.0) + Calc.dirX(jointLength, angles[2]), y + 16 + Calc.dirY(jointLength, angles[2]), lineWidth);
		//lbottom
		drawLine(x - (thighGap / 2.0) + Calc.dirX(jointLength, angles[2]), y + 16 + Calc.dirY(jointLength, angles[2]), x - (thighGap / 2.0) + Calc.dirX(jointLength, angles[2]) + Calc.dirX(jointLength, angles[3]), y + 16 + Calc.dirY(jointLength, angles[2]) + Calc.dirY(jointLength, angles[3]), lineWidth);
	}
	
	public void render(){
		double x = this.x + Calc.rangedRandom(.4) + (shake.x / 2.5), y = this.y + Calc.rangedRandom(.4) + (shake.y / 2.5);
		
		//head
		sprite.render(bodyImage, Sprite.CENTERED, x, y, 1, 1, 0, 1, 1, 1, 1);
		sprite.render(2 + eyeImage, Sprite.CENTERED, x, y, 1, 1, 0, 1, 1, 1, 1);
	}

}
