package graphics;

import java.awt.Font;
import main.Scene;
import com.jogamp.opengl.util.awt.TextRenderer;

public class TextThing {

	public static final int BOTTOM = 2;
	public static final int MID = 1;
	public static final int TOP = 0;

	public static final int LEFT = 0;
	public static final int CENTER = 1;
	public static final int RIGHT = 2;
	
	public static TextThing courier12 = new TextThing("Courier New", Font.PLAIN, 12);
	
	TextRenderer awesomefont;
	
	
	public TextThing(String fontName,int fontStyle, int fontSize) {
		awesomefont =  new TextRenderer(new Font(fontName, fontStyle, fontSize));
		awesomefont.setColor(1,1,1,1);
		awesomefont.setSmoothing(false);
	}


	public void textBegin(){
		//Need yet another y-axis flip
		Scene.gl.glPushMatrix();
		Scene.gl.glTranslated(0,Scene.height,0);
		Scene.gl.glScaled(1, -1, 1);
		awesomefont.begin3DRendering();
	}
	public void textDraw(String str, int x, int y, double scale, int hAlign, int vAlign){
		String[] lines = str.split("\n",-1);
		for(int i=0; i<lines.length; i++){
			int w = (int)(scale*awesomefont.getBounds(lines[i]).getWidth());
			int h = (int)scale*awesomefont.getFont().getSize();
			awesomefont.draw3D(lines[i], x-(int)(hAlign*w)/2, Scene.height-y-h*(i+1)+(h*lines.length*vAlign)/2, 0, (float)scale);
		}
	}

	public void textEnd(){
		awesomefont.end3DRendering();
		Scene.gl.glPopMatrix();
	}


	public void setColor(float r, float g, float b, float a) {
		awesomefont.setColor(r, g, b, a);
		
	}
	
}
