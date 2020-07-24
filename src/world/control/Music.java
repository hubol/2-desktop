package world.control;

import java.util.HashMap;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLRunnable;

import main.Main;

import audio.Audio;

public class Music extends SoundLoader {
	
	/**DO NOT LOOK AT THIS CODE DO NOT EVER LOOK AT THIS CODE PLEASE!!!!!!!!! PLEASE PLEASE PLEASE
	 * YOU WILL PUKE EVERYWHERE AND YOUR EYEBALLS WILL FALL OUT!!!!!!!!! PLEASE DO NOT LOOK AT THIS
	 * CODE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!*/
	
	public static String music;
	public static boolean loaded;
	
	public static String musicRef;
	public static int onceLoaded;
	
	public static Thread t;
	
	public static String musicPrev;
	public static boolean unloaded, firstStep;
	public static int timer;
	
	public static boolean awaitLoad, fadeKept;
	public static String musicKept;
	
	public static String musicLoading;
	
	public static HashMap<String, String> titles;
	
	public static long cancel;
	
	public static String storedToPlay;
	public static boolean storedType, storedFadeIn;
	public static boolean storage;
	public static boolean storedThread;
	
	public static Music me;

	public Music(double x, double y) {
		super(x, y);
		persistent=true;
		visible=false;
		setCollisionGroup(Global.CONTROLLER);
		
		me = this;
		
		musicLoading = "";
		
		firstStep = true;
		
		cancel = System.currentTimeMillis();
		
		music = "";
		musicRef = "";
		
		storedThread = true;
		
		loaded = true;
		timer = -1;
		onceLoaded = -1;
		
		musicPrev = "";
		unloaded = true;
		
		awaitLoad = false;
		musicKept = "";
		fadeKept = false;
		
		titles = new HashMap<String, String>();
		setTitles();
	}
	
	public void join() throws InterruptedException{
		if (t != null)
			t.join();
	}
	
	/**do not talk to me about this*/
	public static void setStorage(String music, boolean type, boolean fade){
		storage = true;
		storedToPlay = music;
		storedType = type;
		storedFadeIn = fade;
	}
	
	/**adjust music volume when pausing!!!!!!! you fuckin dumpass!!!!!*/
	public static void pauseVolume(boolean p){
		if (isSound(music) && loaded){
			if (p)
				Audio.fade(music, .7, .04);
			else
				Audio.fade(music, 1, .1);
		}
	}
	
	/**this is called in case music still exists in the memory and youve forgotten to delete it*/
	public static void deleteExisting(){
		if (!unloaded){
			if (isSound(musicPrev))
				Audio.delete(musicPrev);
			musicPrev = "";
			unloaded = true;
			timer = -1;
		}
	}
	
	/**is this a sound*/
	public static boolean isSound(String s){
		return (Audio.soundExists(s) && !s.equals(""));
	}
	
	public static boolean passes(String toPlay){
		return (!musicLoading.equals(toPlay)||toPlay.equals(""));
	}
	
	/**go away*/
	public static void changeMusic(String toPlay){
		changeMusic(toPlay, true);
	}
	
	/**change music instantly without fading*/
	public static void changeMusic(String toPlay, boolean thread){
		storedThread = thread;
		
		if (Global.soundLoadClaimed != null){
			setStorage(toPlay, false, false);
		}
		else{
			if (passes(toPlay)){
				cancel = System.currentTimeMillis();
				if (!unloaded && toPlay.equals(musicPrev) && isSound(musicPrev) && !toPlay.equals("")){
					String shit = musicPrev;
					
					/*if (isSound(music))
						Audio.fade(music, 0, .025);
					timer = 40;*/
					if (isSound(music))
						Audio.delete(music);
					
					timer = -1;
					
					loaded = true;
					
					musicKept = "";
					musicPrev = "";
					music = shit;
					Audio.fade(music, 1.0, .025);
					loaded = true;
					unloaded = true;
					
					storage = false;
				}
				else{
					musicKept = "";
					deleteExisting();
					if (isSound(music)){
						Audio.delete(music);
						music = "";
					}
					unloaded = true;
					loadMusic(toPlay, 0);
					
					storage = false;
				}
			}
		}
	}
	
	/**fuck*/
	public static void fadeMusic(String toPlay, boolean fadeIn){
		fadeMusic(toPlay, fadeIn, true);
	}
	
	/**fade out current music, whether or not to fade in input music*/
	public static void fadeMusic(String toPlay, boolean fadeIn, boolean thread){
		storedThread = thread;
		
		if (Global.soundLoadClaimed != null){
			setStorage(toPlay, true, fadeIn);
		}
		else{
			if (passes(toPlay)){
				cancel = System.currentTimeMillis();
				if (!toPlay.equals(music)){
					if (loaded){
						musicKept = "";
						if (!unloaded && toPlay.equals(musicPrev) && isSound(musicPrev) && !toPlay.equals("") && Global.soundLoadClaimed == null){
							String shit = musicPrev;
							
							if (isSound(music))
								Audio.fade(music, 0, .025);
							timer = 40;
							musicPrev = music;
							
							music = shit;
							Audio.fade(music, 1.0, .025);
							
							loaded = true;
							
							storage = false;
						}
						else{
							deleteExisting();
							musicPrev = music;
							if (isSound(musicPrev))
								Audio.fade(musicPrev, 0, .025);
							unloaded = false;
							timer = 40;
							
							music = "";
							
							if (fadeIn)
								loadMusic(toPlay,1);
							else
								loadMusic(toPlay,0);
							
							storage = false;
						}
					}
					else{
						storage = false;
						awaitLoad = true;
						musicKept = toPlay;
						fadeKept = fadeIn;
					}
				}
			}
		}
	}
	
	/**load the music*/
	public static void loadMusic(String load, int onLoad){
		musicRef = load;
		onceLoaded = onLoad;
		musicLoading = load;
		
		loaded = false;
		
		if (storedThread){
			t = new Thread(){@Override
				public void run(){
				long fuck = System.currentTimeMillis();
				if (fuck >= cancel){
					if (!musicRef.equals("")){
						Global.soundLoadClaimed = me;
						Audio.loadMusic(musicRef+".ogg");
					}
					if (fuck >= cancel){
						if (isSound(musicRef))
							Audio.get(musicRef).setLooping(true);
						
						loaded = true;
						music = musicRef;
						musicRef = "";
						onLoad();
					}
					else{
						musicKept = "";
						Audio.delete(musicRef);
						musicRef = "";
					}
					
					musicLoading = "";
				}
				
				Global.soundLoadClaimed = null;
			}
			};
			
			t.setDaemon(true);
			t.start();
		}
		else{ //no thread
			Global.soundLoadClaimed = me;
			Audio.loadMusic(musicRef+".ogg");
			
			Audio.get(musicRef).setLooping(true);
			loaded = true;
			music = musicRef;
			musicRef = "";
			onLoad();
			musicLoading = "";
			
			Global.soundLoadClaimed = null;
		}
		
		storedThread = true;
	}
	
	/**once music is loaded this executes*/
	public static void onLoad(){
		if (isSound(music)){
			if (onceLoaded <= 0){
				Audio.fade(music, 1, 1);
				Audio.get(music).setGain(1);
			}
			else if (onceLoaded == 1){
				Audio.fade(music, 1, .025);
				Audio.get(music).setGain(0);
			}
			
			Audio.get(music).play();
			
			if (Global.lastDream && !Global.room(0, 0))
				Main.canvas.invoke(true, new GLRunnable(){@Override public boolean run(GLAutoDrawable d) {new PostDreamMusic(music); return true;}});
			
			Global.lastDream = false;

		}
		onceLoaded = -1;
	}
	
	public void step(){
		firstStep = false;
		
		timer -= 1;
		if (timer == 0){
			deleteExisting();
		}
		
		if (awaitLoad && loaded && !musicKept.equals("")){
			if (!musicKept.equals(music)){
				awaitLoad = false;
				fadeMusic(musicKept,fadeKept);
				fadeKept = false;
				musicKept = "";
			}
			else{
				awaitLoad = false;
				fadeKept = false;
				musicKept = "";
			}
		}
		
		if (storage){
			if (!storedType)
				changeMusic(storedToPlay);
			else
				fadeMusic(storedToPlay, storedFadeIn);
		}
	}
	
	public void setTitles(){
		titles.put("musAllergies","Allergies-Hubol");
		titles.put("musAirplort","Airplort-Hubol");
		titles.put("musAmway", "What is This Thing Called Amway?-Sanborn Singers");
		titles.put("musAndOrGuts","stu-GUTS.WAV and Hubol");
		titles.put("musAndOrHubol","uts-Hubol and GUTS.WAV");
		titles.put("musAxe","Axe-Hubol");
		titles.put("musBadGuy","Bad Guy-Hubol");
		titles.put("musBigEmptyLight","Big Empty Light-ehafh");
		titles.put("musBitterAftertaste","Bitter Aftertaste-Audiotist");
		titles.put("musBluh","Bluh-Hubol");
		titles.put("musBoss01","Hardcore Bosscore-Hubol");
		titles.put("musBoss02","Flowercore Bosscore-Hubol");
		titles.put("musBoss03","Mysticcore Bosscore-Hubol");
		titles.put("musBoss04","Orkestercore Bosscore-Hubol and Oddwarg");
		titles.put("musBoss05Bg","Dinguscore Bosscore-Hubol");
		titles.put("musBoss06Bg","Royalcore Bosscore-Hubol");
		titles.put("musBoss07Bg","Urchincore Bosscore-Hubol");
		titles.put("musBreak","Break-Hubol");
		titles.put("musCar","I Hate Cars-Hubol");
		titles.put("musCave","Cave Tunes-Hubol");
		titles.put("musChest","Chest-Hubol");
		titles.put("musChimey","Chimey-Hubol");
		titles.put("musClang","Clang-Hubol");
		titles.put("musClock","Clock-GUTS.WAV and Hubol");
		titles.put("musCurrentStateOfAffairs","The Current State of Affairs-The Queenstons");
		titles.put("musCute","Cute V3-Hubol");
		titles.put("musDeath", "Sad Flower-Hubol");
		titles.put("musDocumentary","Documentary-Hubol");
		titles.put("musEli", "Eli-Hubol");
		titles.put("musElseIf", "Else If-Hubol");
		titles.put("musFfunkyTtown","Ffunky Ttown-Hubol");
		titles.put("musField","Field-Hubol");
		titles.put("musFjaakTownMarch","Tamago-Hubol");
		titles.put("musFren","Fren-Hubol");
		titles.put("musFunkyTown","Funky Town-Hubol");
		titles.put("musGabbie","Gabbie-Hubol");
		titles.put("musGimmickRain","Gimmick-Hubol");
		titles.put("musGlontch","Glontch-Hubol");
		titles.put("musGoodNews","Good News-Hubol");
		titles.put("musHandsomePrinceRain","Handsome Prince-Hubol");
		titles.put("musHaunting","Haunting-Hubol");
		titles.put("musHolyFuck","Holy Fuck-Hubol");
		titles.put("musIdk","Idk-Hubol");
		titles.put("musIDont","I Don't-Hubol");
		titles.put("musILoveYou","I Love You-Lil B vs. C418");
		titles.put("musIReallyLikeYouAndYouAndYouAndYou", "I Really Like You and You and You and You-Hubol");
		titles.put("musJellycat","Jellycat-Oddwarg");
		titles.put("musKiss","Kiss-Hubol");
		titles.put("musKissV2","Kiss V2-Hubol");
		titles.put("musKlek","Klek-Hubol");
		titles.put("musKreep","Kreep-Hubol");
		titles.put("musMartin","Martin-Hubol");
		titles.put("musMeadow","Meadow-Hubol");
		titles.put("musMiiiiiiist", "Miiiiiiist-Of Montreal and Hubol");
		titles.put("musMirrorDance","Mirror Dance-Hubol");
		titles.put("musNature","Nature is my Friend-Hubol and Oddwarg");
		titles.put("musNewFriends","New Friends-Hubol");
		titles.put("musNulla","Nulla-Hubol");
		titles.put("musPadBullshit","Pad Bullshit-Hubol");
		titles.put("musPiss","Piss-Hubol");
		titles.put("musRuck","Ruck-Hubol");
		titles.put("musPretty","Pretty-Hubol");
		titles.put("musSecretA","Secret-Hubol");
		titles.put("musSecretB","Terces-Hubol");
		titles.put("musSed90","Sed90-Sam Bowers and Hubol");
		titles.put("musShave","Shave-Hubol");
		titles.put("musShifting","Shifting-Hubol");
		titles.put("musShit","Shit-Hubol");
		titles.put("musSnore","Snore-Hubol");
		titles.put("musSoldOut","Sold Out-Hubol");
		titles.put("musSpace","Space-Hubol");
		titles.put("musStormFlow","STORMFLOW-Hubol");
		titles.put("musSunlight","Sunlight-Tiny Tim and Hubol");
		titles.put("musSup","Sup-Hubol");
		titles.put("musT","T-Hubol");
		titles.put("musTack","Tack-Hubol");
		titles.put("musTheWettest","The Wettest-Hubol and Supa Day");
		titles.put("musThirtyThousandDollars","Thirty Thousand Dollars-Hubol and Lil B");
		titles.put("musThisJelly","This Jelly-Hubol");
		titles.put("musTrolls","Trolls-Hubol");
		titles.put("musTup","Tup-Hubol");
		titles.put("musUh","Uh-Hubol");
		titles.put("musUlum","Ulum-Hubol");
		titles.put("musWent","Went-Hubol");
		titles.put("musWhoCares","Who Cares-Hubol");
	}

}
