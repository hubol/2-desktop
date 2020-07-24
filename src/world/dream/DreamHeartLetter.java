package world.dream;

import world.control.Global;
import world.control.Sound;
import world.enemy.BasicEnemy;
import world.player.Player;
import graphics.Sprite;
import main.Calc;

public class DreamHeartLetter extends BasicEnemy{
	public DreamHeartControl mother;
	public int id;
	public double a, sinAdd;

	public DreamHeartLetter(double x, double y, int id) {
		super(x, y);
		
		this.id = id;
		mother = DreamHeartControl.me;
		
		sprite = Sprite.get("font");
		orientation = Sprite.CENTERED;
		setDepth(Integer.MIN_VALUE + 5);
		imageSingle = mother.myFont.fetchSymbolId(mother.message.charAt(id));
		imageSpeed = 0;
		
		mask = Sprite.get("sDreamHeartLetterMask").mask;
		
		setCollisionGroup(Global.DEACTIVATEME);
		
		visible = false;
		
		a = 0;
		calcSine();
	}
	
	public void calcSine(){
		sinAdd = 4 * Math.sin(a / 15.0);
	}
	
	public void step(){
		if (mother.at == id){
			if (!visible){
				setCollisionGroup(Global.DEACTIVATEME, Global.BASICENEMY);
				visible = true;
				pop();
				Sound.playPitched("sDreamHeartLetterAppear", .09);
			}
			else{
				a += 1;
				calcSine();
				
				new DreamHeartParticles(x, y, Calc.random(360),-3);
				
				if (Calc.pointDistance(x, y, Player.me.x, Player.me.y) < 32)
					collect();
			}
		}
	}
	
	public void collect(){
		pop();
		visible = false;
		
		Global.addTweet("#"+mother.message.charAt(mother.at));
		mother.at += 1;
		
		setCollisionGroup(Global.DEACTIVATEME);
	}
	
	public void gunDamage(){
		if (mother.at == id && visible)
			collect();
	}
	
	public void pop(){
		for (int i=0; i<8; i++){
			new DreamHeartParticles(x, y, Calc.random(360), Integer.MIN_VALUE + 10);
			DreamHeartParticles j = new DreamHeartParticles(x, y, Calc.random(360), Integer.MIN_VALUE + 10);
			j.setSpeed(j.getSpeed() * 4);
			if (i > 3){
				j = new DreamHeartParticles(x, y, Calc.random(360), Integer.MIN_VALUE + 10);
				j.setSpeed(j.getSpeed() * 8);
			}
		}
	}
	
	public void render(){
		//hell fucking yes totally illegible code
		draw(-2, -2, "0081CF");
		draw(-3, 0, "0081CF");
		draw(2, 2, "0081CF");
		draw(3, 0, "0081CF");
		draw(0, 3, "0081CF");
		draw(0, -3, "0081CF");
		draw(2, -2, "0081CF");
		draw(-2, 2, "0081CF");
		draw(0, 0, (String)Calc.choose("FF2878", "FF2878", "FF2878", "FF5691", "FF44A1"));
	}
	
	public void draw(double xx, double yy, String color){
		sprite.render(imageSingle, orientation, x + xx + Calc.rangedRandom(.4), y + yy + Calc.rangedRandom(.4) + sinAdd, .6, .6, Calc.rangedRandom(.5), 1, color);
	}

}
