package world.gameplay;

import world.control.Global;
import world.player.Player;
import graphics.Graphics;
import graphics.Sprite;
import graphics.Text;
import main.Calc;
import main.Entity;

public class TextSign extends Entity {
	public final static Sprite SIGN = Sprite.get("sSign"), BUBBLE = Sprite.get("sBubble");
	
	public String text, font, color;
	public Sprite sign;
	public boolean show, shake;
	public double timer;
	public double moveUp;

	public TextSign(double x, double y, String s, String f, String c) {
		super(x, y);
		text=s.toUpperCase();
		font=f;
		color=c;
		
		alpha=0;
		
		moveUp = 0;
		
		sprite=BUBBLE;
		sign = SIGN;
		show=false;
		
		shake = (Boolean)Calc.choose(true, false);
		timer = 1 + Calc.random(30);
		
		setDepth(20);
		setCollisionGroup(Global.INTERACTABLE);
		
		imageSpeed=1.0/6.0;
	}
	
	public void step(){
		//MOVING!!!!
		if (moveUp != 0){
			double div = .5;
			
			y += Math.signum(moveUp) * div;
			double sign = Math.signum(moveUp);
			moveUp += -Math.signum(moveUp) * div;
			if (sign != Math.signum(moveUp)){
				y -= moveUp;
				moveUp = 0;
			}
		}
		
		show=(Calc.pointDistance(x+16,y+16,Global.playerX,Global.playerY)<=32 && Player.me.y==y+16);
		if (show)
			alpha=1;
		else
			alpha=0;
		alpha=Math.max(0, Math.min(1,alpha));
		
		timer -= 1;
		if (timer <= 0){
			timer = 1 + Calc.random(30);
			shake = !shake;
		}
		
		super.step();
	}
	
	public void render(){
		double ax=0, ay=0;
		if (shake){
			ax = Calc.rangedRandom(.75);
			ay = Calc.rangedRandom(.75);
		}
		sign.render(0,Sprite.NORTHWEST,x+ax,y+ay,1,1,0,1,"#ffffff");
	}
	
	public void drawBubble(){
		if (alpha>0){
			double ax=0, ay=0;
			if (shake){
				ax = Calc.rangedRandom(.75);
				ay = Calc.rangedRandom(.75);
			}
			
			sprite.render((int)imageSingle, Sprite.CENTERED, x+16+ax, y-68+ay, 1, 1, 0, alpha, "#ffffff");
			Graphics.setColor(color);
			Graphics.setAlpha(alpha);
			Text.setSpacing(1, 1.25);
			Text.setFont(font);
			Text.orientation=Text.CENTERED;
			Text.randomize(.01);
			Text.drawTextExt(x+24+ax, y-64+ay, text, .3, .3, 0, "FF2658");
			Text.randomize(0);
			Text.setSpacing(1, 1);
			Graphics.setAlpha(1);
		}
	}

}
