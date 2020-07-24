package world.event;

import world.Overlay;
import world.Root;
import world.control.Global;
import graphics.Graphics;
import graphics.Shape;
import graphics.Sprite;
import graphics.Text;
import main.Calc;
import main.Entity;

public class BedScene extends Entity{
	public Sprite bed, floor, under, scribbles, box, glow;
	public int texts, timer, line, c;
	public String[] lines;
	public int[] timers;
	
	public double bedImg, floorImg, boxImg, shake;
	public boolean showBox;
	public int at;
	
	//shit for the scribbles in the background
	public double bgTimer;
	public double[] bgColor, unColor;
	public double[] sX, sY, sH, sA, sXs, sYs;
	public int[] sI;

	public BedScene(double x, double y) {
		super(x, y);
		bed=Sprite.get("sBed");
		floor=Sprite.get("sBedFloor");
		under=Sprite.get("sBedUnder");
		scribbles=Sprite.get("sBedScribbles");
		box=Sprite.get("sBedBox");
		glow=Sprite.get("sBedGlow");
		
		bedImg=Calc.random(4); //glow image is the same
		floorImg=Calc.random(4);
		boxImg=Calc.random(4);
		
		c=Sprite.CENTERED;
		
		texts=5;
		lines=new String[texts];
		timers=new int[texts];
		at=0;
		
		shake=3;
		
		//prepare text
		addLine("i haven't left this%bed in months.",80);
		addLine("what am i doing with my life?",90);
		addLine("",15);
		addLine("why am i here?",80);
		addLine("",15);
		
		timer=60;
		line=-1;
		showBox=false;
		
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
	
	public void step(){
		boxImg+=2.0/20;
		floorImg+=3.0/24;
		bedImg+=1.0/18;
		if (boxImg>=4.0)
			boxImg-=4.0;
		if (floorImg>=6.0)
			floorImg-=6.0;
		if (bedImg>=4.0)
			bedImg-=4.0;
		
		timer-=1;
		if (timer==0){
			if (!showBox){
				line+=1;
				if (line<texts){
					showBox=true;
					timer=timers[line];
					//TODO probably some kind of ambient sound here
				}
				else{
					Overlay.fadeOut(0, 0, 0, 1, 60);
					Root.changeRoom(Global.lastX,Global.lastY);
				}
			}
			else{
				showBox=false;
				timer=15;
			}
		}
		
		//make the background flash like a motherfucker
		bgTimer-=1;
		if (bgTimer<=0)
			randomize();
	}
	
	public void addLine(String txt, int tim){
		lines[at]=txt;
		timers[at]=tim;
		at+=1;
	}
	
	public void randomize(){
		bgTimer=Math.max(Math.ceil(Calc.random(45))-shake, 5);
		
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
	
	public void render(){
		//draw the background things
		Graphics.setColor(bgColor);
		Graphics.setAlpha(1);
		Shape.drawRectangle(0, 0, 640, 480);
		under.render(0, c, 320-(shake/2)+Calc.random(shake), 240-(shake/2)+Calc.random(shake), 1.01, 1.01, 0, 1, unColor[0], unColor[1], unColor[2]);
		
		//draw the scribbles
		for (int i=0; i<20; i++){
			double[] col = Calc.getColorHSV((int)sH[i], 255, 255);
			scribbles.render(sI[i], c, sX[i], sY[i], sXs[i], sYs[i], sA[i], 1, col[0], col[1], col[2]);
		}
		
		//stuff
		floor.render((int)floorImg,Sprite.NORTHWEST,0,0,1,1,0,1,1,1,1);
		glow.render((int)bedImg,Sprite.NORTHWEST,0,0,1,1,0,.5,1,1,1);
		bed.render((int)bedImg,Sprite.NORTHWEST,0,0,1,1,0,1,1,1,1);
		
		//draw the fancy text box
		if (showBox && line>=0 && line<texts){
			if (lines[line]!=""){
				box.render((int)boxImg, c, 320-shake+Calc.random(shake*2), 80-shake+Calc.random(shake*2), 1, 1, 0, 1, "#ffffff");
				Text.randomize(shake);
				Text.orientation=c;
				Graphics.setColor((String)Calc.choose("#FF3F85","#4F9BFF"));
				Text.setFont("crazdFont");
				Text.drawTextExt(324, 96, lines[line].toUpperCase(), .49, .6, 0);
				Text.randomize(0);
			}
		}
	}

}
