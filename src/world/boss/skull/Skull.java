package world.boss.skull;

import java.util.ArrayList;

import audio.Audio;
import world.Fg;
import world.boss.BossAnnounce;
import world.control.EV;
import world.control.Global;
import world.control.Music;
import world.control.Sound;
import world.player.Player;
import graphics.Font;
import graphics.Sprite;
import main.Calc;
import main.Entity;
import main.Scene;

public class Skull extends Entity{
	public final int stemParts = 16, petals = 9;
	public int health, maxHealth = 3;
	public double[] spd, toSpd, ang, dist, alp, size;
	public double animateSpeed;
	public double flowerAngle;
	
	public int havePetals;
	public int hurt;
	
	public final Sprite stem = Sprite.get("sSkullStem"), petal = Sprite.get("sSkullPetal"), spetal = Sprite.get("sSkullPetalShadow"), smoke = Sprite.get("sJetpackSmoke"), flower = Sprite.get("sSkullFlowerExplosion"), seedPart = Sprite.get("sSkullParticle"), sprout = Sprite.get("sSkullSprout"), smallPetal = Sprite.get("sSkullPetalSmall");
	
	public int quiverPhase = 0;
	public int quiverBetween = 0, quiverTime = 0;
	public boolean quiver = false;
	
	public SkullShadowRender back = new SkullShadowRender(this);
	
	public Font font = Global.FONT;
	
	public int eyes = 0;
	public boolean emotion = false, showEmotion = false;
	
	public double heightMult = 0, heightAdd = 10, petalMult = 0, centerMult = 0;
	public int phase = 0, mouthImage = 0;
	
	public int introPhase = 0;
	public int timer = 30;
	
	public int squeezeTimer;
	
	public boolean floating = false;
	public boolean determine = true;
	public boolean left = false;
	public double sin = 0, risePitch = 1;
	
	public double shakeIntensity = 0;
	public double expressionDir=0, expressionY=0;
	
	public double flowerX, flowerY;
	
	public double phsADist, phsADir;
	public double desX, desY;
	
	public static Skull me;
	
	public int flowerPhase = 0;
	
	public Seed mySeed;
	public Seed seedClaimedWhistle;
	
	public double[] keptSeedX;
	public double targetX;
	
	public int jetpackTimer;
	public boolean imLeaving;
	
	public boolean jesusFuckingChrist;
	public double numberGen;
	
	public double smult = 0;
	
	public ArrayList<SkullShadow> shadows = new ArrayList<SkullShadow>();

	//PHASES!!!!!!!!!!!!!!!!!!! cutscene, initialize (move to center), poopin', waiting period, jetpack phase, falling to ground phase
	public Skull(double x, double y) {
		super(x, y);
		
		angle = 0;
		
		numberGen = 694.2953; //681.7969; was ok
		
		jesusFuckingChrist = false;
		
		keptSeedX = new double[2];
		wipeSeedRecord();
		
		targetX = 320;
		
		imLeaving = false;
		
		havePetals = petals;
		
		me = this;
		mySeed = null;
		
		seedClaimedWhistle = null;
		
		flowerX = 0;
		flowerY = 0;
		
		jetpackTimer = -1;
		
		squeezeTimer = -1;
		
		hurt = -1;
		
		setDepth(-10);
		setCollisionGroup(Global.BASICENEMY, Global.DEACTIVATEME);
		
		health = maxHealth;
		//health = 1;
		animateSpeed = .3;
		
		spd = new double[stemParts];
		toSpd = new double[stemParts];
		ang = new double[stemParts];
		
		dist = new double[petals];
		
		for (int i=0; i<stemParts; i++){
			if (i == 0)
				ang[i] = 90 + Calc.rangedRandom(20);
			else
				ang[i] = Calc.rangedRandom(12 - (11.0 * ((double)i/(double)stemParts)));
			spd[i] = 2 + (4 * ((double)i / (double)stemParts));
			spd[i] *= (Double)Calc.choose(1.0,-1.0);
			toSpd[i] = spd[i];
		}
		
		for (int i=0; i<petals; i++){
			dist[i] = 0;
		}
		
		quiverPetals(30);
	}
	
	//PSEUDORANDOM FUNCTIONS!!!!!!!
	public double pissRandom(double a){
		numberGen -= health * 5.2734;
		numberGen += a * 4.929456;
		numberGen += 1.66912;
		numberGen *= 1.00125;
		
		if (numberGen < 0)
			numberGen += 9001.4206969;
		
		if (Math.abs(numberGen) > Double.MAX_VALUE - 1000)
			numberGen = .42;
		
		return (numberGen % 1) * a;
	}
	
	public double pissRangedRandom(double a){
		return -a + pissRandom(a * 2);
	}
	
	/**return a random number in between a - b (note: this function is practically useless)*/
	public double pissRandom(double a, double b){
		double min = b, max = a;
		if (a < b){
			min = a;
			max = b;
		}
		return min + pissRandom(max - min);
	}
	
	public Object pissChoose(Object... entries){
		int i = (int)(pissRandom(1)*entries.length);
		return entries[i];
	}
	//END OF PSEUDORANDOM!!!!!!!!!
	
	public void unloss(){
		if (havePetals != petals){
			havePetals = petals;
			for (int i=0; i<petals; i++)
				dist[i] = 0;
		}
	}
	
	public void landDamage(){
		Audio.get("sSkullHit").setPitch(1 + (1 - (havePetals / petals)));
		Sound.play("sSkullHit");
		Sound.play("sLandOnEnemy");
		//Sound.playPitched("sSkullSqueel",.075);
		
		//quiverPetals(30);
		
		Player.me.jump();
		Player.me.vspeed -= 1.1;
		
		int loss = 2;
		if (health == maxHealth || (health == maxHealth - 1 && havePetals == petals))
			loss = 3;
		/*for (int i=0; i<loss; i++){
			System.out.println(havePetals + ", " + (petals - havePetals + i));
			
			if (petals - havePetals + i < petals)
				dist[petals - havePetals + i] = 696942042;
		}*/
		
		if (shadows.size() > 0)
			shadows.remove(0);
		
		if (havePetals > 0){
			double atX = desX;
			
			final double initial = 56, additional = 22;
			
			if (atX < 220)
				desX += initial + ((1 - (havePetals / petals)) * additional);
			else if (atX > 420)
				desX -= initial + ((1 - (havePetals / petals)) * additional);
			else if (health != 1)
				desX += (initial + ((1 - (havePetals / petals)) * additional)) * (Double)pissChoose(1.0, -1.0);
			else{
				if (atX < 320)
					desX += initial + ((1 - (havePetals / petals)) * additional);
				else
					desX -= initial + ((1 - (havePetals / petals)) * additional);
			}
			
			targetX = desX;
			
			//System.out.println(atX + ", " + desX);
			
			desY += 55 - (12 * invHealthPercent());
			y += 4;
			
			for (int i=0; i<22; i++)
				new GroundParticle(x + Calc.rangedRandom(9), y + Calc.rangedRandom(9));
		}
		
		havePetals -= loss;
		
		if (havePetals <= 0){
			jesusFuckingChrist = true;
			phase = 5;
			Sound.playPitched("sSkullFall",.08);
		}
		
		animateSpeed = 12;
		
		hurt = 10;
		
		Player.me.y -= 4;
	}
	
	public void calculateTargetX(){
		final boolean consider = false;
		
		boolean a = (keptSeedX[0] != -69), b = (keptSeedX[1] != -420);
		targetX = pissRandom(80, 560);
		
		boolean left = false;
		if (keptSeedX[0] > Player.me.x)
			left = true;
		
		while ((b && Math.abs(targetX - keptSeedX[1]) < 64) || (a && ((Math.abs(targetX - keptSeedX[0]) < 64)||(left && targetX > Player.me.x && consider)||(!left && targetX < Player.me.x && consider))))
			targetX = pissRandom(80, 560);
	}
	
	public void wipeSeedRecord(){
		keptSeedX[0] = -69;
		keptSeedX[1] = -420;
	}
	
	public void step(){
		if (phase == 0)
			cutsceneControl();
		else if (phase >= 1){
			if (phase == 1){
				phsADir = 0;
				phsADist = 0;
				flowerPhase = 0;
				mouthImage = 0;
				
				desX = 320;
				desY = 160;
				
				visible = true;
				
				x = Calc.approach(x, desX, 12);
				y = Calc.approach(y, desY, 12);
				
				if (Calc.pointDistance(x, y, desX, desY) < 12){
					timer = -1;
					phase = 2;
					
					if (health < maxHealth)
						timer = 20;
				}
			}
			else if (phase == 2){
				imLeaving = false;
				
				visible = true;
				
				if (timer < 0){
					timer = (int)(12 + (25 * healthPercent()));
					quiverPetals(timer / 2);
					squeezeTimer = timer;
					if (health == 3){
						if (flowerPhase % 2 == 1)
							mySeed = new SeedBaby(this, timer);
						else
							mySeed = new Seed(this, timer, false);
					}
					else if (health == 2){
						if (flowerPhase % 2 == 1)
							mySeed = new Seed(this, timer, true);
						else
							mySeed = new Seed(this, timer, false);
					}
					else if (health == 1){
						if (flowerPhase % 2 == 1)
							mySeed = new SeedBaby(this, timer);
						else
							mySeed = new Seed(this, timer, true);
					}
					calculateTargetX();
				}
				else if (timer == 0){
					/*for (int i=0; i< 2 + (Math.floor((maxHealth - health) / 3)); i++){
						double dirs = 25 + Calc.random(130), dists = 160 + Calc.random(64);
						new SkullFlower(320 + Calc.dirX(dists, dirs), 416 + Calc.dirY(dists, dirs));
					}*/
					/*for (int i=0; i< 2 + (Math.floor((maxHealth - health) / 3)); i++){
						double xx = 112 + Calc.random(416), yy = 112 + Calc.random(192);
						while (Calc.pointDistance(flowerX, flowerY, xx, yy) < 128){
							xx = 112 + Calc.random(416);
							yy = 112 + Calc.random(192);
						}
						new ParticleGuide(flowerX, flowerY, xx, yy, 10 + (8 * healthPercent()), false);
					}*/
					
					if (mySeed != null){
						Sound.playPitched("sSkullPoop",.07);
						mySeed.held = false;
					}
					flowerPhase += 1;
					
					keptSeedX[1] = keptSeedX[0];
					keptSeedX[0] = x;
					
					phase = 3;
					timer = 17 + (int)(18 * healthPercent());
					
					if (flowerPhase >  5 + (Math.floor((maxHealth - health)*.75))){
						int addition = (int)(invHealthPercent() * 25);
						jetpackTimer = 90 + addition;
						flowerPhase = 0;
						wipeSeedRecord();
						timer = 210 + (int)(40 * healthPercent()) + addition;
					}
				}
				
				timer -= 1;
			}
			else if (phase == 3){
				timer -= 1;
				if (timer < 0){
					phase = 2;
					shadows.clear();
				}
			}
			else if (phase == 4){
				if (Scene.collision(this, x, y - 1, Global.PLAYER) && Player.me.vspeed > 0 && Player.me.y < y - 12 && !Player.me.jetpack)
					landDamage();
				
				if (Player.me.y >= 398 && !Player.me.jetpack && !imLeaving){
					timer = 20;
					imLeaving = true;
				}
				
				if (imLeaving){
					if (timer % 2 == 0){
						if (9 - ((timer - 2) / 2) >= 0 && 9 - ((timer - 2) / 2) <= petals - 1)
						dist[9 - ((timer - 2) / 2)] = 0;
						havePetals += 1;
					}
					
					timer -= 1;
					
					if (timer <= 0){
						unloss();
						phase = 1;
						timer = 50;
					}
					//im leaving sound muwhuhwuahwuha
				}

			}
			
			if (phase < 4)
				unloss();
			
			if (phase == 4 || phase == 5){
				hurt -= 1;
				if (hurt > 0)
					visible = !visible;
				else
					visible = true;
			}
			
			if (phase != 0)
				animateSpeed = Calc.approach(animateSpeed, .3, 10);
			
			if (phase >= 2 && phase <= 4){
				double speed = 14 + (invHealthPercent() * 9);
				
				if (phase == 4)
					speed += 8;
				
				//targetX = Player.me.x;
				
				if (hurt > 0)
					mouthImage = 1;
				else
					mouthImage = 0;
				
				if (Math.abs(desX - targetX) < speed)
					speed = Math.abs(desX - targetX);
				
				if (desX < targetX)
					desX += speed;
				else if (desX > targetX)
					desX -= speed;
				
				phsADist = Calc.approach(phsADist, 5 + (5 * invHealthPercent()), 16);
				phsADir += 2 + (4 * invHealthPercent());
				
				double sub = 0;
				if (phase == 4)
					sub = 4;
				
				x = Calc.approach(x, desX/* + Calc.dirX(phsADist, phsADir)*/, 12 - sub);
				y = Calc.approach(y, desY + Calc.dirY(phsADist, phsADir), 12 - sub);
			}
			else if (phase == 5){
				eyes = 3;
				mouthImage = 1;
				
				vspeed += .3;
				vspeed *= 1.02;
				angle += 5 + (vspeed / 8.0);
				
				y += vspeed;
				
				if (y >= 402){
					Sound.playPitched("sSkullShatter",.09);
					
					for (int i=0; i<8; i++)
						new SkullShatter(x, y, this, i, angle);
					
					visible = true;
					timer = 30;
					phase = 6;
					alpha = 0;
					heightMult = 0;
					heightAdd = 10;
					petalMult = 0;
					centerMult = 0;
					angle = 0;
					
					if (health == 1){
						y = 402;
						
						Audio.get("musBoss02").stop();
						Music.changeMusic("musNulla");
						
						Player.control = false;
						
						phase = 70;
						timer = 30;
					}
				}
			}
			else if (phase == 6){
				x = 320;
				y = 160;
				timer -= 1;
				if (timer <= 0){
					phase = 7;
					alpha = 0;
					timer = 60;
				}
			}
			else if (phase == 7){
				timer -= 1;
				if (timer <= 0){
					Sound.play("sSkullVineGrow");
					alpha = 0;
					hurt = -1;
					eyes = 2;
					phase = 8;
					timer = 30;
					unloss();
					mouthImage = 0;
					health -= 1;
				}
			}
			else if (phase == 8){
				timer -= 1;
				if (timer <= 0){
					ArrayList<SkullShatter> list = Scene.getEntityList(SkullShatter.class);
					for (int i=0; i<list.size(); i++)
						list.get(i).destroy();
					
					mouthImage = 0;
					phase = 1;
					timer = 30;
					jesusFuckingChrist = false;
				}
			}
			else if (phase == 69){
				timer -= 1;
				if (timer <= 0){
					phase = 419;
					timer = -1;
					new SkullDeathTalk(x, y, this);
					//new SmallMessage(this, 0, -8, "is this the part where we shake hands?", 90);
				}
			}
			else if (phase == 70){
				timer -= 1;
				if (timer <= 0){
					double toX = x - 48;
					if (toX < 80)
						toX = x + 48;
					
					y = 402;

					Player.me.cutMove = true;
					Player.me.cutRight = (Player.me.x < toX);
					Player.me.cutToX = toX;
					
					phase = 69;
					timer = 90;
				}
			}
			else if (phase == 420){
				timer -= 1;
				if (timer <= 0){
					Sound.play("sSkullSqueel");
					Sound.playPitched("sSkullSeedLand", .07);
					Fg.me.shakeTimer = 17;
					
					ArrayList<SkullShatter> list = Scene.getEntityList(SkullShatter.class);
					for (int i=0; i<list.size(); i++){
						for (int j=0; j<8; j++)
							new SeedParticle(list.get(i).x + Calc.rangedRandom(5), 416 - Calc.random(8));
						list.get(i).destroy();
					}
					
					ArrayList<SeedBaby> bist = Scene.getEntityList(SeedBaby.class);
					for (int i=0; i<bist.size(); i++){
						for (int j=0; j<8; j++)
							new SeedParticle(bist.get(i).x + Calc.rangedRandom(5), bist.get(i).x + Calc.rangedRandom(5));
						bist.get(i).destroy();
					}
					
					timer = 30;
					phase = 421;
				}
			}
			else if (phase == 421){
				timer -= 1;
				if (timer <= 0){
					Music.fadeMusic(Global.roomMusic, true);
					Global.blockFade(false);
					
					Global.heal();
					
					Player.control = true;
					Player.me.cutMove = false;
					
					Global.addTweet("flower #drifter defeated!!!!!");
					
					Global.dropExactLoot(x, 160, 45);
					if (Global.playerHealth == Global.playerMaxHealth)
						Global.dropExactLoot(x, 160, 45);
					Global.dropRandomLoot(x, 160, 8);
					
					Global.event[EV.SKULL_DEFEAT] = 1;
					Global.refreshIconMap();
					back.destroy();
					destroy();
				}
			}
		}
		
		if (phase !=6 && phase != 7 && phase != 0 && phase < 69){
			if (alpha < 1)
				alpha += .1;
			
			heightAdd = Calc.approach(heightAdd, 0, 16);
			heightMult = Calc.approach(heightMult, 1, 35);
		
			centerMult = Calc.approach(centerMult, 1, 20);
			centerMult += .025;
			if (centerMult > 1)
				centerMult = 1;
		
			petalMult = Calc.approach(petalMult, 1, 30);
			petalMult += .010975;
			if (petalMult > 1)
				petalMult = 1;
		}
		
		if (squeezeTimer > -4)
			squeezeTimer -= 1;
		if (squeezeTimer == 0){
			eyes = 2;
		}
		else if (squeezeTimer > 0)
			eyes = 3;
		
		jetpackTimer -= 1;
		if (jetpackTimer == 0)
			new Jetpack(Calc.random(640), 510, this);
		
		animateStem();
		animatePetals();
		
		smult = Calc.approach(smult, 1, 3.3);
	}
	
	//TODO
	public void buildShadows(){
		double stored = numberGen;
		int storedHealth = health, storedPetals = havePetals;
		double storedDesX = desX, storedDesY = desY, storedTargetX = targetX;
		double storedY = y;
		smult = 0;
		
		shadows.clear();
		shadows.add(new SkullShadow(desX, desY, false, 0));
		
		/*int loss = 2;
		if (health == maxHealth || (health == maxHealth - 1 && havePetals == petals))
			loss = 3;*/
		
		//havePetals -= 3;
		
		int iterator = 1;
		while (havePetals > 0){
			int loss = 2;
			if (health == maxHealth || (health == maxHealth - 1 && havePetals == petals))
				loss = 3;
			
			if (havePetals > 0){
				double atX = desX;
				
				final double initial = 56, additional = 22;
				
				if (atX < 220)
					desX += initial + ((1 - (havePetals / petals)) * additional);
				else if (atX > 420)
					desX -= initial + ((1 - (havePetals / petals)) * additional);
				else if (health != 1)
					desX += (initial + ((1 - (havePetals / petals)) * additional)) * (Double)pissChoose(1.0, -1.0);
				else{
					if (atX < 320)
						desX += initial + ((1 - (havePetals / petals)) * additional);
					else
						desX -= initial + ((1 - (havePetals / petals)) * additional);
				}
				
				targetX = desX;
				
				//System.out.println(atX + ", " + desX);
				
				desY += 55 - (12 * invHealthPercent());
				y += 4;
				
				shadows.add(new SkullShadow(desX, desY, (shadows.get(iterator - 1).x < desX), iterator));
			}
			
			havePetals -= loss;
			iterator++;
		}
		
		if (shadows.get(1).x > shadows.get(0).x)
			shadows.get(0).right = false;
		else
			shadows.get(0).right = true;
		
		shadows.remove(shadows.size() - 1);
		
		numberGen = stored;
		health = storedHealth;
		havePetals = storedPetals;
		desX = storedDesX;
		desY = storedDesY;
		targetX = storedTargetX;
		y = storedY;
	}
	
	public double healthPercent(){
		return (double)(health - .45)/(double)maxHealth;
	}
	
	public double invHealthPercent(){
		return 1 - healthPercent();
	}
	
	public void destroy(){
		me = null;
		super.destroy();
	}
	
	public void roomDestroy(){
		destroy();
	}
	
	public void render(){
		double bluh = 0;
		if (phase == 0){
			if (floating){
				sin += 1;
				bluh = 4 * Math.sin(sin / 16.0);
			}
			
			y += bluh;
		}
		
		//drawing stem
		double[] myPoint = Calc.transformPointByAngle(0, -10 + heightAdd, angle);
		
		double xsc = .32 * heightMult, ysc = .4, currentAngle = 0, xx = x + myPoint[0], yy = y + myPoint[1];
		for (int i=0; i<stemParts; i++){
			if (i == 0)
				currentAngle = ang[0] + angle;
			else
				currentAngle += ang[i];
			
			stem.render(0, Sprite.WEST, xx, yy, xsc, ysc, currentAngle, alpha, 1, 1, 1);
			
			xx += Calc.dirX(xsc * 11.0, currentAngle);
			yy += Calc.dirY(xsc * 11.0, currentAngle);
			ysc -=.02;
			ysc *= .985;
		}
		
		Sprite.get("sSkull").render(0, Sprite.CENTERED, x, y, xscale, yscale, angle, alpha, 1, 1, 1);
		if (eyes > 0){
			Sprite.get("sSkull").render(eyes, Sprite.CENTERED, x + Math.round(Calc.dirX(3.2 * shakeIntensity, sin * 15)), y + Math.round(Calc.dirY(3.2 * shakeIntensity, sin * 15)), xscale, yscale, angle, alpha, 1, 1, 1);
			double eyeAddY = 4, dir = 0;
			if (emotion){
				eyeAddY *= -1;
				dir += 180;
			}
			
			expressionY = Calc.approach(expressionY, eyeAddY, 10);
			expressionDir = Calc.approach(expressionDir, dir, 10);
			
			if (showEmotion){
				if (eyes != 3){
					myPoint = Calc.transformPointByAngle(-4, expressionY, angle);
					Sprite.get("sSkullEyebrow").render(0, Sprite.CENTERED, x + myPoint[0] + Calc.rangedRandom(.1), y + Calc.rangedRandom(.1) + myPoint[1], 1, 1, angle - expressionDir + Calc.rangedRandom(2), alpha, 1, 1, 1);
					myPoint = Calc.transformPointByAngle(6, expressionY, angle);
					Sprite.get("sSkullEyebrow").render(0, Sprite.CENTERED, x + myPoint[0] + Calc.rangedRandom(.1), y + Calc.rangedRandom(.1) + myPoint[1], 1, 1, angle + expressionDir + Calc.rangedRandom(2), alpha, 1, 1, 1);
				}
				myPoint = Calc.transformPointByAngle(1, 9, angle);
				Sprite.get("sSkullMouth").render(mouthImage, Sprite.CENTERED, x + Calc.rangedRandom(.1) + myPoint[0], y + Calc.rangedRandom(.1) + myPoint[1], 1 + (Calc.random(.4) * mouthImage), Calc.dirY(1, expressionDir + 90) * (1 + (Calc.random(.4) * mouthImage)), angle + Calc.rangedRandom(2), alpha, 1, 1, 1);
			}
			
			for (int i=0; i<4 - health; i++)
				Sprite.get("sSkull").render(4 + i, Sprite.CENTERED, x + Calc.rangedRandom(.1), y + Calc.rangedRandom(.1), 1, 1, angle, alpha, 1, 1, 1);
		}
		
		if (introPhase != 0){
			double dir = flowerAngle, mult = 1.0;
			int j = 0;
			for (int i=0; i<petals; i++){
				if (havePetals >= petals - i)
					petal.render(0, Sprite.WEST, xx + Calc.dirX(dist[j], dir), yy + Calc.dirY(dist[j], dir), .5 * petalMult, .55 * mult * petalMult, dir + Calc.rangedRandom(1 + animateSpeed), alpha, 1, 1, 1);
				j += 2;
				if (j >= petals){
					mult = -1.0;
					j = 1;
				}
				dir += 720.0/petals;
			}
			flowerX = xx; flowerY = yy;
			Sprite.get("sSkullFlowerCenter").render(0, Sprite.CENTERED, xx, yy, centerMult, centerMult, flowerAngle, alpha, 1, 1, 1);
		}
		
		y -= bluh;
	}
	
	//TODO
	public void shadow(double x, double y){
		double fX = flowerX, fY = flowerY;
		y += Calc.dirY(phsADist, phsADir);
		
		//drawing stem
		double[] myPoint = Calc.transformPointByAngle(0, -10 + heightAdd, angle);
		
		double xsc = .32 * heightMult, ysc = .4 * smult, currentAngle = 0, xx = x + myPoint[0], yy = y + myPoint[1];
		for (int i=0; i<stemParts; i++){
			if (i == 0)
				currentAngle = ang[0] + angle;
			else
				currentAngle += ang[i];
			
			stem.render(0, Sprite.WEST, xx, yy, xsc, ysc, currentAngle, alpha, 1, 1, 1);
			
			xx += Calc.dirX(xsc * 11.0, currentAngle);
			yy += Calc.dirY(xsc * 11.0, currentAngle);
			ysc -=.02;
			ysc *= .985;
		}
		
		Sprite.get("sSkullShadow").render(0, Sprite.CENTERED, x, y, xscale * smult, yscale * smult, angle, alpha, 1, 1, 1);
		
		if (introPhase != 0){
			double dir = flowerAngle, mult = 1.0;
			int j = 0;
			for (int i=0; i<petals; i++){
				if (havePetals >= petals - i)
					spetal.render(0, Sprite.WEST, xx + Calc.dirX(dist[j], dir), yy + Calc.dirY(dist[j], dir), .5 * petalMult * smult, .55 * mult * petalMult * smult, dir + Calc.rangedRandom(1 + animateSpeed), alpha, 1, 1, 1);
				j += 2;
				if (j >= petals){
					mult = -1.0;
					j = 1;
				}
				dir += 720.0/petals;
			}
			flowerX = xx; flowerY = yy;
			Sprite.get("sSkullFlowerCenterShadow").render(0, Sprite.CENTERED, xx, yy, centerMult * smult, centerMult * smult, flowerAngle, alpha, 1, 1, 1);
		}
		
		flowerX = fX;
		flowerY = fY;
	}
	
	public void animateStem(){
		/*for (int i=0; i<stemParts; i++){
			if (i == 0)
				ang[i] = 90 + Calc.rangedRandom(20);
			else
				ang[i] = Calc.rangedRandom(12 - (11.0 * ((double)i/(double)stemParts)));
			spd[i] = 2 + (4 * ((double)i / (double)stemParts));
			spd[i] *= (Double)Calc.choose(1.0,-1.0);
			toSpd[i] = spd[i];
		}*/
		for (int i=0; i<stemParts; i++){
			double mult = 1.0;
			if (i >= stemParts * .2 && i <= stemParts * .375)
				mult = 1.4;
			
			ang[i] += spd[i];
			if (i == 0){
				double dist = 21.75;
				if (ang[0] <= 90 - dist && toSpd[0] < 0)
					toSpd[0] = 2 + (2.2 * ((double)i / (double)stemParts));
				else if (ang[0] >= 90 + dist && toSpd[0] > 0)
					toSpd[0] = -2 - (2.2 * ((double)i / (double)stemParts));
			}
			else{
				double dist = 20.0, multiplier = 12.0;
				if (ang[i] <= (-dist + (multiplier * (1 - ((double)i/(double)stemParts)))) * mult && toSpd[i] < 0)
					toSpd[i] = 2 + (2.2 * ((double)i / (double)stemParts));
				else if (ang[i] >= (dist - (multiplier * (1 - ((double)i/(double)stemParts)))) * mult && toSpd[i] > 0)
					toSpd[i] = -2 - (2.2 * ((double)i / (double)stemParts));
			}
			spd[i] = Calc.approach(spd[i], toSpd[i] * animateSpeed, 5);
		}
	}
	
	public void quiverPetals(double time){
			quiver = true;
			quiverPhase = 0;
			quiverBetween = (int)time / petals;
			quiverTime = 0;
	}
	
	public void animatePetals(){
		for (int i=0; i<petals; i++){
			if (dist[i] != 0 && dist[i] < 10)
				dist[i] = Calc.approach(dist[i], 0, 7);
		}
		
		if (quiver){
			quiverTime -= 1;
			if (quiverTime <= 0){
				dist[quiverPhase] = 7;
				quiverPhase += 1;
				if (quiverPhase < petals){
					quiverTime = quiverBetween;
				}
				else
					quiver = false;
			}
		}
		
		flowerAngle += animateSpeed;
		
		if ((animateSpeed > 1.2 || quiver) && introPhase >= 10 && !jesusFuckingChrist){
			for (int i=0; i<Calc.random(3); i++){
				double dir = Calc.random(360), dist = 10 + Calc.random(25);
				new PetalParticle(flowerX + Calc.dirX(dist, dir), flowerY + Calc.dirY(dist, dir));
			}
		}
	}
	
	public void cutsceneControl(){
		timer -= 1;
		if (timer == 0){
			if (introPhase == 0){
				Fg.me.shakeTimer = 2;
				Global.preventAudioCrash();
				Audio.get("sSkullQuake").setLooping(true);
				Sound.play("sSkullQuake");
				introPhase = 1;
			}
			else if (introPhase == 2 || introPhase == 3){
				eyes += 1;
				timer = 5;
				introPhase += 1;
				if (introPhase == 4)
					timer = 16;
			}
			else if (introPhase == 4){
				eyes = 1;
				Audio.get("sSkullBlink").setPitch(1.0);
				Sound.play("sSkullBlink");
				timer = 4;
				introPhase += 1;
			}
			else if (introPhase == 5){
				eyes = 2;
				timer = 5;
				introPhase += 1;
			}
			else if (introPhase == 6){
				eyes = 1;
				Audio.get("sSkullBlink").setPitch(.7);
				Sound.play("sSkullBlink");
				timer = 4;
				introPhase += 1;
			}
			else if (introPhase == 7){
				eyes = 2;
				timer = 35;
				introPhase += 1;
			}
			else if (introPhase == 8){
				floating = true;
				Audio.get("sSkullRise").setLooping(true);
				Audio.get("sSkullRise").setPitch(risePitch);
				Sound.play("sSkullRise");
				Music.fadeMusic("musWhoCares", true);
				timer = 900000;
			}
			else if (introPhase == 9){
				Sound.play("sSkullFlowerBloom");
				timer = 10;
				introPhase = 10;
			}
			else if (introPhase == 10){
				Sound.play("sSkullFlowerBloom2");
				timer = 35;
				introPhase = 11;
			}
			else if (introPhase == 11){
				emotion = false;
				
				timer = 30;
				introPhase = 12;
			}
			else if (introPhase == 12){
				introPhase = 13;
				timer = 75;
				mouthImage = 1;
				animateSpeed = 11;
				
				Audio.get("sSkullQuake").setGain(1);
				Audio.fade("sSkullQuake", 1, 1);
				Audio.get("sSkullQuake").setLooping(true);
				Sound.play("sSkullQuake");
				
				Sound.play("sSkullShriek");
			}
			else if (introPhase == 13){
				Music.changeMusic("musBoss02");
				Global.addTweet("motherfuckin skull fight aah");
				
				animateSpeed = .3;
				mouthImage = 0;
				Audio.fade("sSkullQuake",0,.08);
				introPhase = 14;
				
				new BossAnnounce("DRIFTER","E2FF18","sBossTextSkull");
				Player.control = true;
				Player.me.cutMove = false;
				
				phase = 1;
			}
		}
		
		if (introPhase == 1){
			y -= .1;
			Fg.me.shakeTimer = 2;
			
			for (int i=0; i<2; i++)
				new GroundParticle(x + Calc.rangedRandom(9), Math.max(416, y - 13 - Calc.random(6)));
			
			if (y <= 402){
				Fg.me.shakeTimer = 0;
				Audio.fade("sSkullQuake",0,.08);
				introPhase = 2;
				timer = 5;
			}
		}
		else if (introPhase == 8 && floating){
			y = Calc.approach(y, 240, 70);
			y -= .5;
			risePitch += .0009;
			risePitch = Calc.approach(risePitch, 1.8, 120);
			Audio.get("sSkullRise").setPitch(risePitch);
			if (y <= 240){
				Audio.fade("sSkullRise", 0.0, .035);
				y = 240;
				introPhase = 9;
				shakeIntensity = 1.0;
				Sound.play("sSkullVineGrow");
				emotion = true;
				showEmotion = true;
				animateSpeed = 7;
				timer = 60;
			}
		}
		
		if (introPhase == 13)
			Fg.me.shakeTimer = 2;
		
		if (introPhase >= 9){
			shakeIntensity = Calc.approach(shakeIntensity, 0, 40);
			heightAdd = Calc.approach(heightAdd, 0, 16);
			heightMult = Calc.approach(heightMult, 1, 35);
			if (introPhase < 13)
				animateSpeed = Calc.approach(animateSpeed, .3, 13);
			else
				animateSpeed = Calc.approach(animateSpeed, .3, 20);
		}
		
		if (introPhase >= 10){
			centerMult = Calc.approach(centerMult, 1, 12);
			centerMult += .025;
			if (centerMult > 1)
				centerMult = 1;
		}
		
		if (introPhase >= 11){
			petalMult = Calc.approach(petalMult, 1, 16);
			petalMult += .010975;
			if (petalMult > 1)
				petalMult = 1;
		}
	}

}
