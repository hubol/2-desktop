package world.junk;

import javax.media.opengl.GL2;

import graphics.Font;
import graphics.Graphics;
import graphics.Shape;
import graphics.Sprite;
import graphics.Text;
import world.control.Global;
import world.control.Shake;
import main.Calc;
import main.Entity;
import main.Scene;

public class TweetReview extends Entity{
	public boolean on = false;
	private final Shake s = new Shake(.4);
	private final Font font = Global.FONT;
	private String text;

	public TweetReview(double x, double y, String text) {
		super(x, y);
		this.text = text.toUpperCase();
		setCollisionGroup(Global.DEACTIVATEME);
		
		xscale = 0;
		yscale = 1.8;
		
		setDepth(Integer.MIN_VALUE + 10);
	}
	
	public void step(){
		if (!on){
			xscale = 0;
			yscale = 1.8;
		}
		else{
			xscale = Calc.approach(xscale, yscale, 2);
			yscale = Calc.approach(yscale, 1, 3);
		}
	}
	
	public void render(){
		if (xscale > 0){
			Graphics.setColor(1,1,1);
			Graphics.setAlpha(.8);
			double w = 280 * xscale, h = 58 * xscale;
			rect(x + s.x, y + s.y, w, h);
			
			double k = Calc.rangedRandom(.5), l = Calc.rangedRandom(.5);
			Sprite.get("sStand").render(1, Sprite.CENTERED, x + (120 * xscale) + k + s.x, y + (9 * xscale) + l + s.y, xscale, xscale, 0, 1, 1, 1, 1);
			if (Global.heroMode)
				Sprite.get("sBow").render(0, Sprite.CENTERED, x + (120 * xscale) + k + s.x, y + (9 * xscale) + l + s.y, xscale, xscale, 0, 1, 1, 1, 1);
			
			Graphics.setColor("167BFF");
			
			String ss = Text.widthLimit(text,30);
			Text.orientation = Text.NORTHWEST;
			Text.setFont(font);
			drawTextExt(x - (w / 2.0) + (4 * xscale) + Calc.rangedRandom(.2) + s.x, y - (h / 2.0) + (4 * xscale) + Calc.rangedRandom(.2) + s.y, ss, .24 * xscale, .28 * xscale, 0);
		}
	}
	
	public void rect(double x, double y, double width, double height){
		Shape.drawRectangle(x - (width / 2), y - (height / 2), x + (width / 2), y + (height / 2));
	}
	
	public void drawTextExt(double x, double y, String text, double xscale, double yscale, double angle){
		int currentLine=0;
		double ax=0, ay=0;

		String hash = "FF2151";
		
		String[] result=text.split("%");
		int[] widths=new int[result.length];
		for (int i=0; i<result.length; i++){
			widths[i]=result[i].length();
		}
		
		ax=Text.reallignX(Text.orientation,text,widths[currentLine]);
		ay=Text.reallignY(Text.orientation,text);
		
		GL2 gl = Scene.gl;
		gl.glPushMatrix();
		gl.glTranslated(x + Scene.viewX, y + Scene.viewY, 0);
		gl.glRotated(-angle,0,0,1);
		gl.glScaled(xscale, yscale, 0);
		
		boolean hashtag = false, last = false;
		
		for (int i=0; i<text.length(); i++){
			int s=46;
			s = Text.charToImg(text,i);
			
			last = false;
			
			if (!hashtag && s == 40){
				hashtag = true;
				last = true;
			}
			if (hashtag && (s < 10 || s > 35) && !last)
				hashtag = false;
			
			if (!hashtag)
				Graphics.setColor("167BFF");
			else
				Graphics.setColor(hash);
			
			if (s==-1){
				currentLine+=1;
				if (widths.length > currentLine)
					ax=Text.reallignX(Text.orientation,text,widths[currentLine]);
				ay+=Text.sprite.imageHeight*Text.vSpacing * 1.2;
			}
			else{
				Text.sprite.renderWithoutView(s, Text.orientation, ax-Text.letterRandom+(Calc.random(Text.letterRandom*2)), ay-Text.letterRandom+(Calc.random(Text.letterRandom*2)), 1, 1, 0, Graphics.alpha, Graphics.color[0], Graphics.color[1], Graphics.color[2]);
				ax+=Text.sprite.imageWidth*Text.hSpacing;
			}
		}	
		gl.glPopMatrix();
	}

}
