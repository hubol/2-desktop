package world.enemy;

import graphics.Sprite;
import main.Calc;
import world.control.Global;
import world.player.Player;
import main.Scene;

public class Spike extends BasicEnemy {
	public boolean blood;
	public int face = (Global.roomX + Global.roomY) % 4;
	public double[] addX, addY;
	public double layerTime;
	
	public int damage = 40;
	
	public final static Sprite SPIKE = Sprite.get("sSpike");

	public Spike(double x, double y) {
		super(x, y);
		
orientation=Sprite.NORTHWEST;
		
		sprite=SPIKE;
		mask=SPIKE.mask;
		
		angle = 0;
		
		if (Scene.collision(this, x - 2, y, Global.BLOCK, Global.UPBLOCK))
			angle = 270;
		else if (Scene.collision(this, x + 2, y, Global.BLOCK, Global.UPBLOCK))
			angle = 90;
		else if (Scene.collision(this, x, y - 2, Global.BLOCK, Global.UPBLOCK))
			angle = 180;
		if (Scene.collision(this, x, y + 2, Global.BLOCK, Global.UPBLOCK)) //override that fucker
			angle = 0;
		
		init();
	}
	
	public Spike(double x, double y, double a){
		super(x, y);
		angle = a;
		init();
	}
	
	public void init(){
		orientation=Sprite.NORTHWEST;
		
		sprite=Sprite.get("sSpike");
		mask=Sprite.get("sSpike").mask;
		
		imageSingle = Calc.random(2);
		imageSpeed=Calc.random(.4);
		
		damage = 40;
		if (Global.currentArea.equals("CAVE"))
			damage = 60;
		else if (Global.currentArea.equals("STONE"))
			damage = 70;
		else if (Global.currentArea.equals("PLANT") || Global.currentArea.equals("KISS"))
			damage = 90;
		else if (Global.currentArea.equals("FINAL"))
			damage = 270;
		
		setCollisionGroup(Global.BASICENEMY,Global.SOLIDBLOCK);
		
		addX = new double[4];
		addY = new double[4];
		initLayers();
	}
	
	public void initLayers(){
		for (int i =0; i<4; i++){
			addX[i] = 0;
			addY[i] = 0;
		}
		layerTime = Calc.random(30);
	}
	
	public void resetLayers(){
		for (int i =0; i<4; i++){
			addX[i] = Calc.approach(addX[i], 0, 1.7);
			addY[i] = Calc.approach(addY[i], 0, 1.7);
		}
		layerTime = Calc.random(30);
	}
	
	public void stepLayers(){
		for (int i =0; i<4; i++){
			addX[i] += Calc.rangedRandom(.25);
			addY[i] += Calc.rangedRandom(.25);
		}
		layerTime -= 1;
		if (layerTime <= 0)
			resetLayers();
	}
	
	public void step(){
		if ((Scene.collision(this, x-1, y, Global.PLAYER)||Scene.collision(this, x+1, y, Global.PLAYER)||Scene.collision(this, x, y-1, Global.PLAYER)||Scene.collision(this, x, y+1, Global.PLAYER)) && !Scene.collision(Player.me, Player.me.x, Player.me.y + 1, Global.BLOCK, Global.UPBLOCK)){
			blood = true;
			Player.hurtPlayer(damage);
		}
		
		stepLayers();
		
		super.step();
	}
	
	public void bombDamage(){
		//
	}
	
	public void gunDamage(){
		//
	}
	
	public void render(){
		int[] i = new int[4];
		i[0] = 2;
		i[1] = -1;
		if (blood)
			i[1] = 1;
		i[2] = 0;
		i[3] = 3 + face;
		
		for (int j = 0; j<4; j++){
			String b = "ffffff";
			if (i[j] == 2)
				b = Global.roomColor;
			if (i[j]!=-1)
				sprite.render(i[j], Sprite.CENTERED, x + 16 +addX[j] + Calc.rangedRandom(.25), y + 16 + addY[j] + Calc.rangedRandom(.25), 1, 1, angle, 1, b);
		}
	}

}
