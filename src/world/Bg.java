package world;

import java.util.ArrayList;

import world.control.Global;
import world.control.SpriteLoader;
import world.gameplay.Heart;

import graphics.Graphics;
import graphics.Shape;
import graphics.Sprite;
import main.Calc;
import main.Entity;
import main.Scene;

public class Bg extends Entity{
	public static Bg me;
	public boolean bgVis;
	public boolean frontBgVis;
	
	//shit for the scribbles in the background
	public double bgTimer;
	public double[] bgColor, unColor;
	public double[] sX, sY, sH, sA, sXs, sYs;
	public int[] sI;
	
	public boolean fade = false;
	
	public Bg(double x, double y) {
		super(x, y);
		me=this;
		persistent=true;
		bgVis=false;
		frontBgVis = false;
		setDepth(Integer.MAX_VALUE-20);
		addCollisionGroup(Global.CONTROLLER);
		
		bgColor=new double[3];
		unColor=new double[3];
		sX=new double[20];
		sY=new double[20];
		sH=new double[20];
		sXs=new double[20];
		sYs=new double[20];
		sA=new double[20];
		sI=new int[20];
		randomize();
	}
	
	@Override
	public void destroy(){
		if (me==this)
			me = null;
		super.destroy();
	}
	
	public void step(){
		for(int i=0; i<20; i++){
			sX[i]+=Calc.rangedRandom(2);
			sY[i]+=Calc.rangedRandom(2);
			sH[i]+=Calc.rangedRandom(.012);
			sXs[i]+=Calc.rangedRandom(.00512);
			sYs[i]+=Calc.rangedRandom(.00512);
			sA[i]+=Calc.rangedRandom(.1);
		}
		
		//make the background flash like a motherfucker
		bgTimer-=1;
		if (bgTimer<=0)
			randomize();
		
		if (fade && alpha > 0)
			alpha -= 1.0 / 480.0;
	}
	
	public double[] fuck(int[] in){
		return new double[]{(double)in[0] / 255.0, (double)in[1] / 255.0, (double)in[2] / 255.0};
	}
	
	public void randomize(){
		bgTimer=Math.max(Math.ceil(Calc.random(65)), 5);
		
		bgColor=Calc.getColorHSV((int)Calc.random(256), 255, 255);
		unColor=Calc.getColorHSV((int)Calc.random(256), 255, 255);
		
		for(int i=0; i<20; i++){
			sX[i]=Calc.random(640);
			sY[i]=Calc.random(480);
			sH[i]=Calc.random(255);
			sXs[i]=(.75+Calc.random(.5))*(Double)Calc.choose(1.0,-1.0);
			sYs[i]=(.75+Calc.random(.5))*(Double)Calc.choose(1.0,-1.0);
			sA[i]=Calc.random(360);
			sI[i]=(int)Calc.random(5);
		}
	}
	
	public void scribbleRender(String s){
		if (Sprite.exists(s)){
			for (int i=0; i<20; i++){
				double[] col = Calc.getColorHSV((int)sH[i], 255, 255);
				Sprite.get(s).render(sI[i], Sprite.CENTERED, sX[i] + Scene.viewX, sY[i] + Scene.viewY, sXs[i], sYs[i], sA[i], 1, col[0], col[1], col[2]);
			}
		}
	}
	
	public void delete(){
		if (Sprite.exists("bg"+Global.roomX+","+Global.roomY))
			Sprite.get("bg"+Global.roomX+","+Global.roomY).remove();
		if (Sprite.exists("bg"+Global.roomX+","+Global.roomY+"FRONT"))
			Sprite.get("bg"+Global.roomX+","+Global.roomY+"FRONT").remove();
	}
	
	public void errorRender(){
		if (!Sprite.exists("sBanaan"))
			new SpriteLoader("sBanaan_2");
		
		for (int i=0; i<20; i++)
			Sprite.get("sBanaan").render(sI[i] % 2, Sprite.CENTERED, sX[i] + Scene.viewX, sY[i] + Scene.viewY, sXs[i] * 3, sYs[i] * 3, sA[i], 1, 1, 1, 1);
	}
	
	public void render(){
		if (Global.mainActive){
			if (Global.room(29, 0)){
				Graphics.setColor("0081CF");
				Shape.drawRectangle(0, 0, 640, 480); 
			}
			
			boolean a = false;
			String scribble = "sBedScribbles";
			if ((Global.currentArea.equals("SUNLIGHT"))||Global.room(28,0)||Global.room(7, 5)||Global.room(7, 3)||Global.room(7,4)||Global.room(6,6)||Global.room(7,6)||Global.room(5, 5))
				a = true;
			
			if (Global.roomX == 21){
				a = true;
				scribble = "sHeartScribbles";
			}
			else if (Global.roomX == 18){
				a = true;
				scribble = "sSunScribbles";
			}
			else if (Global.roomX == 23){
				a  = true;
				scribble = "sJarScribbles";
			}
			
			if (a){
				//draw the background things
				Graphics.setColor(bgColor);
				Graphics.setAlpha(1);
				Shape.drawRectangle(0, 0, 640, 480);
				
				//draw the scribbles
				scribbleRender(scribble);
			}
			
			if (bgVis){
				Sprite s = Sprite.get("bg"+Global.roomX+","+Global.roomY);
				int l = s.textures.length;
				//for (int i=0; i<l; i++)
					
			
				for (int i=0; i<l; i++){
					s.render(i,Sprite.CENTERED, ((642/l)*(.5+i)) + Scene.viewX, 240 + Scene.viewY, 1.02, 1, 0, alpha, 1, 1, 1);
					s.render(i,Sprite.CENTERED, ((642/l)*(.5+i))+Calc.rangedRandom(.4) + Scene.viewX, 240-1+Calc.rangedRandom(.4) + Scene.viewY, 1.02+Calc.random(.0012), 1.01+Calc.random(.0012), 0, alpha, 1, 1, 1);
				}
			}
			
			if (Global.room(16,1))
				errorRender();
			
			if (frontBgVis)
				Fg.me.fgRender(Sprite.get("bg"+Global.roomX+","+Global.roomY+"FRONT"), alpha);
			
			//lets draw the shinies for the items and stuff
			ArrayList<Heart> list= Scene.getEntityList(Heart.class);
			for (int i=0; i<list.size(); i++)
				list.get(i).drawGlow();
		}
	}

}
