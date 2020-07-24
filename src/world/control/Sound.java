package world.control;

import java.util.ArrayList;

import world.enemy.Urchin;
import world.player.Player;

import audio.Audio;
import main.Calc;
import main.Entity;
import main.Scene;

public class Sound extends Entity{
	public static int chains,gems,explodes,jars,hearts,spends,babies,bubbles,angels,pops,squish,drops, add, in, out, flap = 1, peep = 1;
	public double suicideTimer, stoneBellVolume, stoneBellTime, stoneBellPitch;
	public static boolean stoneBellFadeIn;
	public static boolean onStep;
	public static int[] splashTime;

	public Sound(double x, double y) {
		super(x, y);
		persistent=true;
		visible=false;
		onStep = false;
		
		chains=1;
		gems=1;
		explodes=1;
		jars = 1;
		hearts = 1;
		spends = 1;
		babies = 1;
		bubbles = 1;
		angels = 1;
		pops = 1;
		squish = 1;
		add = 1;
		drops = 1;
		in = 1;
		out = 1;
		
		splashTime = new int[3];
		for (int i=0; i<3; i++)
			splashTime[i] = 0;
		
		stoneBellVolume = 0;
		stoneBellFadeIn = false;
		stoneBellPitch = 1.0;
		stoneBell();
		
		suicideTimer = 10;
		
		setCollisionGroup(Global.CONTROLLER);
	}
	
	public static void deep(){
		peep += 1;
		if (peep > 4)
			peep = 1;
		playPitched("sDukePeep"+peep, .12);
	}
	
	public static void peep(){
		peep += 1;
		if (peep > 4)
			peep = 1;
		playPitched("sPukePeep"+peep, .12);
	}
	
	public static void flap(){
		if (flap == 1)
			flap = 2;
		else
			flap = 1;
		playPitched("sPukeFlap"+flap, .12);
	}
	
	public void stoneBell(){
		Audio.fade("sStoneBells", stoneBellVolume, stoneBellVolume);
		Audio.get("sStoneBells").setGain(stoneBellVolume);
		Audio.get("sStoneBells").setPitch(stoneBellPitch);
		Audio.get("sStoneBells").setPan(Calc.rangedRandom(.3));
		Sound.play("sStoneBells");
		
		stoneBellTime = (Double)Calc.choose(4.0, 4.0, 8.0, 8.0, 8.0, 8.0, 16.0, 16.0, 32.0) * 2.0;
		if (stoneBellTime <= 8.0)
			stoneBellPitch = (Double)Calc.choose(stoneBellPitch, .55 + Calc.rangedRandom(.1), .6 + Calc.rangedRandom(.125), .7 + Calc.rangedRandom(.15));
		else
			stoneBellPitch = .5 + Calc.rangedRandom(.075);
	}
	
	public static void play(String s){
			if (Audio.soundExists(s))
				Audio.get(s).play();
			else
				System.out.println("hey silly ass \""+s+"\" is not an existing sound!!!");
	}
	
	/**play soundString + (1 - soundAmount) e.g. soundIndexedPlayer("sDicks", 3); --> will play "sDicks1", "sDicks2", "sDicks3"*/
	public static void soundIndexedPlay(String soundString, int soundAmount){
		playPitched(soundString + ((add % soundAmount) + 1), .06);
		add += 1;
	}
	
	public static void in(){
		playPitched("sFlowerIn"+in, .2);
		if (in == 1)
			in = 2;
		else
			in = 1;
	}
	
	public static void out(){
		playPitched("sFlowerOut"+out, .2);
		if (out == 1)
			out = 2;
		else
			out = 1;
	}
	
	public static void splashPlay(int splash){
		if (splashTime[splash - 1] <= 0){
			double vol = .2;
			Audio.fade("sDickSplash"+splash, vol, vol);
			Audio.get("sDickSplash"+splash).setGain(vol);
			playPitched("sDickSplash"+splash,.125);
			splashTime[splash - 1] = 2;
		}
	}
	
	public static void squishPlay(){
		if (!onStep){
			playPitched("sSquish"+squish,.1);
			squish+=1;
			if (squish>3)
				squish=1;
			onStep = true;
		}
	}
	
	public static void chainPlay(){
		play("sChain"+chains);
		chains+=1;
		if (chains>5)
			chains=1;
	}
	
	public static void dropsPlay(){
		playPitched("sWaterDrop"+drops, .33);
		drops+=1;
		if (drops>2)
			drops=1;
	}
	
	public static void angelPlay(){
		Audio.get("sAngel"+angels).setGain(.5);
		Audio.fade("sAngel"+angels, .5, .5);
		Audio.get("sAngel"+angels).setPitch(.8 + Calc.random(.6));
		Sound.play("sAngel"+angels);
		angels+=1;
		if (angels>4)
			angels=1;
	}
	
	public static void bubblesPlay(){
		playPitched("sBubblePop"+bubbles, .05);
		bubbles+=1;
		if (bubbles>4)
			bubbles=1;
	}
	
	public static void babyPlay(){
		playPitched("sBombBabyLand"+babies, .3);
		
		babies+=1;
		if (babies>3)
			babies=1;
	}
	
	public static void spendPlay(){
		playPitched("sSpend"+spends, .02);
		
		spends+=1;
		if (spends>4)
			spends=1;
	}
	
	public static void errorPlay(){
		for (int i = 0; i < 5; i++)
			Audio.get("sError"+(i + 1)).stop();
		playPitched("sError"+(1+(int)Calc.random(5)));
	}
	
	public static void gemPlay(){
		playPitched("sGemLand"+gems);

		gems+=1;
		if (gems>3)
			gems=1;
	}
	
	public static void popPlay(){
		playPitched("sJellyBlockPop"+pops,.12);

		pops+=1;
		if (pops>3)
			pops=1;
	}
	
	public static void heartPlay(){
		play("sGetHeart"+hearts);

		hearts+=1;
		if (hearts>4)
			hearts=1;
	}
	
	public static void jarBreakPlay(){
		playPitched("sJarBreak"+jars);

		jars+=1;
		if (jars>3)
			jars=1;
	}
	
	public static void explodePlay(){
		playPitched("sExplode"+explodes);

		explodes+=1;
		if (explodes>9)
			explodes=1;
	}
	
	public static void hurtPlay(){
		playPitched("sHurt"+(1+(int)Calc.random(9)));
	}
	
	public static void playPitched(String s){
		playPitched(s, .05);
	}
	
	public static void playPitched(String s, double rand){
		if (Audio.soundExists(s)){
			Audio.get(s).setPitch(1 + Calc.rangedRandom(rand));
			play(s);
		}
		else
			System.out.println("hey silly ass \""+s+"\" is not an existing sound!!!");
	}
	
	public void step(){
		for (int i=0; i<3; i++)
			splashTime[i] -= 1;
		
		onStep = false;
		
		if (stoneBellFadeIn)
			stoneBellVolume = Calc.approach(stoneBellVolume, .35, 12.0);
		else
			stoneBellVolume = Calc.approach(stoneBellVolume, 0, 19.0);
		
		stoneBellTime -= 1.0;
		if (stoneBellTime <= 0)
			stoneBell();
		
		stoneBellFadeIn = false;
		
		if (Global.gotStench){
			if (Global.suicidalUrchinsExist()){
				double dist = 90000;
				ArrayList<Urchin> list = Scene.getEntityList(Urchin.class);
				for (int i=0; i<list.size(); i++){
					if (list.get(i).suicideTimer > 0){
						double fa = Calc.pointDistance(list.get(i).x, list.get(i).y, Player.me.x, Player.me.y);
						if (fa <= 180 && fa < dist)
							dist = fa;
					}
				}
				
				if (dist <= 180){
					suicideTimer -= (360.0 - dist) / 90.0;
					if (suicideTimer <= 0){
						Audio.get("sSuicideNear").setPitch(1.0 + ((180.0 - dist) / 90.0));
						
						boolean can = true;
						if (Global.room(4, 4))
							can = (Global.event[EV.PAPA_DEFEAT] > 0);
						if (can)
							play("sSuicideNear");
						suicideTimer = 10;
					}
				}
			}
		}
	}

}
