package world.boss.slick;

import audio.Audio;
import world.control.EV;
import world.control.Global;
import world.control.Sound;
import main.Calc;
import main.Entity;

public class SlickBrokenControl extends Entity{
	public SlickBroken[] list;
	public double timer, pitch, mitch;
	public boolean a;
	public static SlickBrokenControl me;
	
	public boolean go;

	public SlickBrokenControl(double x, double y) {
		super(x, y);
		me = this;
		
		if (Global.event[EV.SLICK_DEFEAT] == 0){
			list = new SlickBroken[5];
			list[1] = new SlickBroken(320, 384, -360, 2); //hemfoot
			list[2] = new SlickBroken(512, 384, 720, 3); //fut2
			list[3] = new SlickBroken(264, 386, 15, 1); //mandib1
			list[4] = new SlickBroken(464, 368, 100, 4); //mandib2
			list[0] = new SlickBroken(400, 368, 180, 0); //bod
			
			setDepth(Integer.MIN_VALUE + 9);
			setCollisionGroup(Global.DEACTIVATEME);
			timer = Calc.random(60);
			a = false;
			
			go = false;
			
			pitch = 1;
			mitch = 1;
			
			alarmInitialize(6);
		}
		else
			destroy();
	}
	
	public void roomDestroy(){
		me = null;
		super.roomDestroy();
	}
	
	public void destroy(){
		me = null;
		super.destroy();
	}
	
	public void alarmEvent(int i){
		Audio.get("sSlickForm").setPitch(pitch);
		Sound.play("sSlickForm");
		pitch += .075;

		if (i == 0){ //bod
			list[i].go(336, 352, 0, -6);
			go = true;
			Audio.get("sSlickIntro").setLooping(true);
			Audio.get("sSlickIntro").setPitch(1);
			Audio.get("sSlickIntro").setGain(0);
			Audio.fade("sSlickIntro", 1, .03);
			Sound.play("sSlickIntro");
		}
		else if (i == 1) //footr
			list[i].go(352, 384, 0, -8);
		else if (i == 2) //footl
			list[i].go(320, 384, 0, -7);
		else if (i == 3) //mr
			list[i].go(352, 336, -15, -7.5);
		else if (i == 4) //ml
			list[i].go(320, 336, 15, -8);
		else{
			for (int j =0; j<5; j++)
				list[j].destroy();
			destroy();
			new Slick(336, 352);
		}
		
		if (i == 0)
			alarm[1] = 30;
		else if (i < 4)
			alarm[i + 1] = 12 - i;
		else if (i == 4){
			Audio.fade("sSlickIntro", 0, .001);
			alarm[5] = 60;
		}
	}
	
	public void reset(){
		String s = "sSlickSpark"+(Calc.boolToInt(a) + 1);
		if (Audio.soundExists(s)){
			final double g = .4;
			Audio.get(s).setGain(g);
			Audio.fade(s, g, 1);
			Audio.get(s).setPitch(.9 + Calc.random(.75));
			Sound.play(s);
		}
		int id = (int)Calc.random(5);
		double amt = 1 + Calc.random(5);
		for (int i=0; i<amt; i++){
			double dir = Calc.random(360), dist = Calc.random(16);
			new SlickSpark(list[id].x + Calc.dirX(dist, dir), list[id].y + Calc.dirY(dist, dir));
		}
		timer = 8 + Calc.random(22);
		a = !a;
	}
	
	public void step(){
		if (go){
			mitch = Calc.approach(mitch, 1.5, 500.0);
			Audio.get("sSlickIntro").setPitch(mitch);
		}
		
		timer -= 1;
		if (timer <= 0)
			reset();
		
		alarmStep();
	}

}
