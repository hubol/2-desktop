package world.control;

import graphics.Graphics;
import graphics.Sprite;
import graphics.Text;
import main.Calc;
import main.Entity;

public class Hud extends Entity{
	public static boolean showHealth, showArea, showMoney, showWeapon, forceHide;
	public static double timeHealth, timeArea, timeMoney, timeWeapon;
	public static Hud me;
	public final String TEXT = "FF2879", SHADOW = "006187";
	public final double IN = 3.2, OUT = 4.9;
	
	//health
	public Sprite health = Sprite.get("sLifeMeter");
	public double healthImg = Calc.random(2), healthMaxY = -46, healthToY = 4, healthY = healthMaxY;
	//area
	public Sprite area = Sprite.get("sAreaTag");
	public double areaImg = Calc.random(2), areaAlpha = 0;
	//money
	public Sprite money = Sprite.get("sGemMeter");
	public double moneyImg = Calc.random(2), moneyMaxY = 536, moneyToY = 474, moneyY = moneyMaxY;
	public int currentMoney;
	//weapon
	public Sprite glasses = Sprite.get("sGlassesIcon"), door = Sprite.get("sDoorIcon"), ball = Sprite.get("sWeaponBall"), arrow = Sprite.get("sWeaponArrow"), gun = Sprite.get("sNewGunIcon"), bomb = Sprite.get("sBombIcon"), bomb2 = Sprite.get("sBombUpIcon"), weapon;
	public static double uS = 1, dS = 1, bS = 1, arrowTimer = 0;
	public double ballImg = Calc.random(2), ballMaxY = 540, ballToY = 440, ballY = ballMaxY;
	public int ammo, maxAmmo;
	public static double[] color;
	
	public static int weapons;

	public Hud(double x, double y) {
		super(x, y);
		setCollisionGroup(Global.CONTROLLER);
		persistent = true;
		setDepth(Integer.MIN_VALUE+2);
		me = this;
		
		getWeapons();
		
		forceHide = false;
		showAll();
		
		randomizeColor();
		
		currentMoney = Global.money;
	}
	
	public void immediateShow(){
		showAll();
		ballY = ballToY;
		moneyY = moneyToY;
		areaAlpha = 1;
		healthY = healthToY;
	}
	
	public void immediateHide(){
		hideAll();
		ballY = ballMaxY;
		moneyY = moneyMaxY;
		areaAlpha = 0;
		healthY = healthMaxY;
	}
	
	public void step(){
		if (currentMoney != Global.money){
			if (currentMoney > Global.money){
				currentMoney -= 1;
				Sound.spendPlay();
				showMoney();
			}
			else
				currentMoney = Global.money;
		}
		
		getWeapons();
		
		if (Global.alwaysShowHud)
			showAll();
		
		if (forceHide)
			hideAll();
		
		//health fading
		if (showHealth){
			healthY = Calc.approach(healthY, healthToY, IN);
			timeHealth -= 1;
			if (timeHealth <= 0)
				showHealth = false;
		}
		else{
			healthY = Calc.approach(healthY, healthMaxY, OUT);
		}
		
		//area fading
		if (showArea){
			areaAlpha += .11;
			timeArea -= 1;
			if (timeArea <= 0)
				showArea = false;
		}
		else{
			areaAlpha -= .06;
		}
		
		//money ffading
		if (showMoney){
			moneyY = Calc.approach(moneyY, moneyToY, IN);
			timeMoney -= 1;
			if (timeMoney <= 0)
				showMoney = false;
		}
		else{
			moneyY = Calc.approach(moneyY, moneyMaxY, OUT);
		}
		
		//weapon fading
		if (showWeapon){
			ballY = Calc.approach(ballY, ballToY, IN);
			timeWeapon -= 1;
			if (timeWeapon <= 0)
				showWeapon = false;
		}
		else{
			ballY = Calc.approach(ballY, ballMaxY, OUT);
		}
		
		//thing
		areaAlpha = Math.min(1, Math.max(0, areaAlpha));
		
		//animate stuff!!!!!!
		healthImg += .1 + Calc.random(.075);
		if (healthImg >= 2)
			healthImg -= 2;
		
		areaImg += .1 + Calc.random(.075);
		if (areaImg >= 2)
			areaImg -= 2;
		
		moneyImg += .1 + Calc.random(.075);
		if (moneyImg >= 2)
			moneyImg -= 2;
		
		ballImg += .1 + Calc.random(.075);
		if (ballImg >= 2)
			ballImg -= 2;
		
		//"de-animate" the arrows after youve pressed them or whatever!!!!
		arrowTimer -= 1;
		if (arrowTimer == 0){
			bS = 1;
			uS = 1;
			dS = 1;
		}
	}
	
	/**push a button! argument is true if it is for up!*/
	public static void pushArrow(boolean up){
		if (weapons > 1){
			randomizeColor();
			uS = 1;
			dS = 1;
			if (up)
				uS = 1.5;
			else
				dS = 1.5;
			bS = 1.08;
			arrowTimer = 5;
		}
	}
	
	public void destroy(){
		if (me==this)
			me = null;
		super.destroy();
	}
	
	public void refreshWeaponSprite(){
		if (Global.selectedWeapon == 0){
			weapon = gun;
			ammo = Global.playerBullets;
			maxAmmo = Global.playerMaxBullets;
		}
		else if (Global.selectedWeapon == 1){
			if (Global.event[EV.BOMBUPGRADE] == 0)
				weapon = bomb;
			else
				weapon = bomb2;
			ammo = Global.playerBombs;
			maxAmmo = Global.playerMaxBombs;
		}
		else if (Global.selectedWeapon == 2){
			weapon = door;
			ammo = Global.playerDoors;
			maxAmmo = 2;
		}
		else if (Global.selectedWeapon == 3){
			weapon = glasses;
			ammo = 0;
			maxAmmo = 0;
		}
	}
	
	public static void randomizeColor(){
		color = Calc.getColorHSV((int)Calc.random(256), 150, 255);
	}
	
	public int scaleToImg(double a){
		if (a >= 1.5)
			return 1;
		else
			return 0;
	}
	
	public void dicks(){
		double i;
		
		Text.setFont(Global.CRAZDFONT);
		Text.orientation = Text.CENTERED;
		Graphics.setAlpha(1);
		
		//draw health
		Text.setSpacing(1.05, 1);
		i = 1 - ((double)Global.playerHealth / Global.playerMaxHealth);
		i += .2;
		i *= i;
		health.render((int)healthImg, Sprite.NORTHWEST, 4 + Calc.rangedRandom(i * 2), healthToY + Calc.rangedRandom(i * 2), 1, 1, 0, 1, 1, 1, 1);
		Text.randomize(i / 2);
		
		Graphics.setColor(SHADOW);
		Text.drawTextExt(5.4 + 61 + Calc.rangedRandom(i * 2), healthToY + 31.4 + Calc.rangedRandom(i * 2), "" + Math.max(0, Global.playerHealth), .4, .3, 0);
		Graphics.setColor(TEXT);
		Text.drawTextExt(4 + 61 + Calc.rangedRandom(i * 2), healthToY + 30 + Calc.rangedRandom(i * 2), "" + Math.max(0, Global.playerHealth), .4, .3, 0);
		
		//draw money
		Text.setSpacing(1.1, 1);
		Text.randomize( .2 );
		money.render((int)moneyImg, Sprite.SOUTHEAST, 635 + Calc.rangedRandom(.5), healthToY + Calc.rangedRandom(.5) + 44, 1, 1, 0, 1, "ffffff");
		Graphics.setColor(SHADOW);
		Text.drawTextExt(636.4 + Calc.rangedRandom(.2) - 33, 1.4 + healthToY + Calc.rangedRandom(.2) - 14 + 44, "" + currentMoney, .4, .3, 0);
		Graphics.setColor("DF87FF");
		Text.drawTextExt(635 + Calc.rangedRandom(.2) - 33, healthToY + Calc.rangedRandom(.2) - 14 + 44, "" + currentMoney, .4, .3, 0);
		Text.setSpacing(1, 1);
	}
	
	public void render(){
		if (Global.mainActive && !Global.playerDead)
			subRender();
	}
	
	public void subRender(){
		double i;
		
		refreshWeaponSprite();
		
		Text.setFont(Global.CRAZDFONT);
		Text.orientation = Text.CENTERED;
		Graphics.setAlpha(1);
		
		//Sprite.get("sBedOverlay").render(0, Sprite.CENTERED, Overlay.me.xx, Overlay.me.yy, Overlay.me.xsc, Overlay.me.ysc, 0, Overlay.me.alp, 1, 1, 1);
		
		//draw health
		Text.setSpacing(1.05, 1);
		i = 1 - ((double)Global.playerHealth / Global.playerMaxHealth);
		i += .2;
		i *= i;
		health.render((int)healthImg, Sprite.NORTHWEST, 4 + Calc.rangedRandom(i * 2), healthY + Calc.rangedRandom(i * 2), 1, 1, 0, 1, 1, 1, 1);
		Text.randomize(i / 2);
		
		Graphics.setColor(SHADOW);
		Text.drawTextExt(5.4 + 61 + Calc.rangedRandom(i * 2), healthY + 31.4 + Calc.rangedRandom(i * 2), "" + Math.max(0, Global.playerHealth), .4, .3, 0);
		Graphics.setColor(TEXT);
		Text.drawTextExt(4 + 61 + Calc.rangedRandom(i * 2), healthY + 30 + Calc.rangedRandom(i * 2), "" + Math.max(0, Global.playerHealth), .4, .3, 0);
	
		//draw area
		if (!Global.dream){
			Text.setSpacing(1.1, 1);
			Text.randomize( .2 );
			area.render((int)areaImg, Sprite.NORTHEAST, 635 + Calc.rangedRandom(.5), 4 + Calc.rangedRandom(.5), 1, 1, 0, areaAlpha, Global.BLUELINE);
			//area.render((int)areaImg + 2, Sprite.NORTHEAST, 635 + Calc.rangedRandom(.5), 4 + Calc.rangedRandom(.5), 1, 1, 0, areaAlpha, "ffffff");
			Graphics.setAlpha(areaAlpha);
			double xshift = 2.5, yshift = 0;
			Graphics.setColor(SHADOW);
			//areaShadowDraw(2+xshift,yshift);
			//areaShadowDraw(-2+xshift,yshift);
			areaShadowDraw(1.6+xshift,1.6+yshift);
			//areaShadowDraw(xshift,-2+yshift);
			Graphics.setColor(Global.roomColor);
			Text.drawTextExt(635 + Calc.rangedRandom(.2) - 80 + xshift, 4 + Calc.rangedRandom(.2) + 28 + yshift, Global.currentArea.toUpperCase(), .4, .3, 0);
			Graphics.setAlpha(1);
		}
		
		//draw weapon
		if (weapons > 0){
			Text.setSpacing(1, 1);
			ball.render((int)ballImg, Sprite.CENTERED, 36 + Calc.rangedRandom(.75), ballY + Calc.rangedRandom(.75), bS, bS, Calc.rangedRandom(2), 1, "ffffff");
			if (Global.selectedWeapon != 2)
				weapon.render(0, Sprite.CENTERED, 36 + Calc.rangedRandom(.9), ballY + Calc.rangedRandom(.9), bS, bS, Calc.rangedRandom(.4), 1, "ffffff");
			else{
				weapon.render(1, Sprite.CENTERED, 36 + Calc.rangedRandom(.9), ballY + Calc.rangedRandom(.9), bS, bS, Calc.rangedRandom(.4), 1, Global.playerDoorColor);
				weapon.render(0, Sprite.CENTERED, 36 + Calc.rangedRandom(.9), ballY + Calc.rangedRandom(.9), bS, bS, Calc.rangedRandom(.4), 1, "ffffff");
			}
			if (weapons > 1){
				arrow.render(scaleToImg(uS), Sprite.CENTERED, 36 + Calc.rangedRandom(1), ballY + Calc.rangedRandom(1) - (32 * bS), uS, uS, 0, 1, 1, 1, 1);
				//arrow.render(scaleToImg(dS), Sprite.CENTERED, 36 + Calc.rangedRandom(1), ballY + Calc.rangedRandom(1) + (32 * bS), dS, -dS, 0, 1, 1, 1, 1);
			}
			Text.setFont(Global.FONT);
			Text.randomize( .3 );
			if (maxAmmo > 0){
				Graphics.setColor(SHADOW);
				weaponShadowDraw(-1, 0);
				weaponShadowDraw(0, -1);
				weaponShadowDraw(1, 0);
				weaponShadowDraw(0, 1);
				
				Graphics.setColor(color);
				Text.drawTextExt(26 + Calc.rangedRandom(.5), ballY + 25 + Calc.rangedRandom(.5), Calc.formatNumberString(""+ammo, 2), .4, .3, 0);
				Sprite.get("sSlash").render(0, Sprite.CENTERED, 35 + Calc.rangedRandom(.5), ballY + 18 + Calc.rangedRandom(.5), .55, .5, 22 + Calc.rangedRandom(2), 1, Graphics.getColor());
				Text.drawTextExt(58 + Calc.rangedRandom(.5), ballY + 25 + Calc.rangedRandom(.5), Calc.formatNumberString(""+maxAmmo, 2), .4, .3, 0);
			}
			else{
				String s = "on";
				if (Global.selectedWeapon == 3 && !Global.glassesEnabled)
					s = "off";
				
				Graphics.setColor(SHADOW);
				textShadowDraw(-1, 0,s);
				textShadowDraw(0, -1,s);
				textShadowDraw(1, 0,s);
				textShadowDraw(0, 1,s);
				
				Graphics.setColor(color);
				Text.drawTextExt(45 + Calc.rangedRandom(.5), ballY + 25 + Calc.rangedRandom(.5), s.toUpperCase(), .4, .3, 0);
			}
		}
		
		Text.setFont(Global.CRAZDFONT);
		//draw money
		Text.setSpacing(1.1, 1);
		Text.randomize( .2 );
		money.render((int)moneyImg, Sprite.SOUTHEAST, 635 + Calc.rangedRandom(.5), moneyY + Calc.rangedRandom(.5), 1, 1, 0, 1, "ffffff");
		Graphics.setColor(SHADOW);
		Text.drawTextExt(636.4 + Calc.rangedRandom(.2) - 33, 1.4 + moneyY + Calc.rangedRandom(.2) - 14, "" + currentMoney, .4, .3, 0);
		Graphics.setColor("DF87FF");
		Text.drawTextExt(635 + Calc.rangedRandom(.2) - 33, moneyY + Calc.rangedRandom(.2) - 14, "" + currentMoney, .4, .3, 0);
		Text.setSpacing(1, 1);
	}
	
	public static void showAll(){
		showHealth();
		showArea();
		showMoney();
		showWeapon();
	}
	
	public static void hideAll(){
		hideHealth();
		hideArea();
		hideMoney();
		hideWeapon();
	}
	
	public void textShadowDraw(double xx, double yy, String s){
		Text.drawTextExt(45 + Calc.rangedRandom(.5) + xx, ballY + 25 + Calc.rangedRandom(.5) +yy, s.toUpperCase(), .4, .3, 0);
	}
	
	public void areaShadowDraw(double j, double k){
		Text.drawTextExt(635 + Calc.rangedRandom(.2) - 80 + j, 4 + Calc.rangedRandom(.2) + 28 + k, Global.currentArea.toUpperCase(), .4, .3, 0);
	}
	
	public void weaponShadowDraw(double j, double k){
		Text.drawTextExt(27 + j + Calc.rangedRandom(.5), ballY + 25 + k + Calc.rangedRandom(.5), Calc.formatNumberString(""+ammo, 2), .4, .3, 0);
		Sprite.get("sSlash").render(0, Sprite.CENTERED, 36 + j + Calc.rangedRandom(.5), ballY + 18 + k + Calc.rangedRandom(.5), .55, .5, 22 + Calc.rangedRandom(2), 1, SHADOW);
		Text.drawTextExt(59 + j + Calc.rangedRandom(.5), ballY + 25 + k + Calc.rangedRandom(.5), Calc.formatNumberString(""+maxAmmo, 2), .4, .3, 0);
	
	}
	
	public void getWeapons(){
		weapons = Global.getWeapons();
	}
	
	public static void showHealth(){
		showHealth = true;
		timeHealth = 60;
	}
	
	public static void hideHealth(){
		showHealth = false;
		timeHealth = 0;
	}
	
	public static void showArea(){
		showArea = true;
		timeArea = 60;
	}
	
	public static void hideArea(){
		showArea = false;
		timeArea = 0;
	}
	
	public static void showMoney(){
		showMoney = true;
		timeMoney = 60;
	}
	
	public static void hideMoney(){
		showMoney = false;
		timeMoney = 0;
	}
	
	public static void showWeapon(){
		showWeapon = true;
		timeWeapon = 60;
	}
	
	public static void hideWeapon(){
		showWeapon = false;
		timeWeapon = 0;
	}

}
