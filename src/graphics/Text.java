package graphics;

import javax.media.opengl.GL2;

import main.Calc;
import main.Scene;

import graphics.Sprite;

public class Text {
public static Sprite sprite = Sprite.get("font");

public final static int CENTERED = -1;
public final static int EAST= 0;
public final static int NORTHEAST = 1;
public final static int NORTH = 2;
public final static int NORTHWEST = 3;
public final static int WEST = 4;
public final static int SOUTHWEST = 5;
public final static int SOUTH = 6;
public final static int SOUTHEAST = 7;

public static boolean idiot = true;

public static int orientation=NORTHWEST;
public static double letterRandom=0.0, hSpacing = 1.0, vSpacing = 1.0;

public static Font myFont = new Font("font");

	/**set the font to a font created with font*/
	public static void setFont(String font){
		sprite=Sprite.get(font);
		myFont = new Font(font);
	}
	
	/**set the font to a font created with font & sym*/
	public static void setFont(String font, char... sym){
		sprite=Sprite.get(font);
		myFont = new Font(font, sym);
	}
	
	/**set the font to an already created font*/
	public static void setFont(Font font){
		sprite=font.sprite;
		myFont = font;
	}
	
	/**randomize the position of each letter*/
	public static void randomize(double a){
		letterRandom=a;
	}
	
	/**set spacing*/
	 public static void setSpacing(double h, double v){
		 hSpacing = h;
		 vSpacing = v;
	 }

	/**draw some text with no cool things*/
	public static void drawText(double x, double y, String text){
		int currentLine=0;
		double ax=0, ay=0;
		
		int spriteOrientation = orientation;
		if (!idiot)
			spriteOrientation = NORTHWEST;
		//text = text.toLowerCase();
		
		String[] result=text.split("%");
		int[] widths=new int[result.length];
		for (int i=0; i<result.length; i++){
			widths[i]=result[i].length();
		}
		
		ax=reallignX(orientation,text,widths[currentLine]);
		ay=reallignY(orientation,text);
		
		for (int i=0; i<text.length(); i++){
			int s=46;
			s = charToImg(text,i);
			if (s==-1){
				currentLine+=1;
				ax=reallignX(orientation,text,widths[currentLine]);
				ay+=sprite.imageHeight*vSpacing;
			}
			else{				
				sprite.render(s, spriteOrientation, x+Scene.viewX+ax-letterRandom+(Calc.random(letterRandom*2)), y+Scene.viewY+ay-letterRandom+(Calc.random(letterRandom*2)), 1, 1, 0, Graphics.alpha, Graphics.color[0], Graphics.color[1], Graphics.color[2]);
				ax+=sprite.imageWidth*hSpacing;
			}
		}
	}
	/**
	 * @param x
	 * @param y
	 * @param text
	 * @param orientation
	 * @param xscale
	 * @param yscale
	 * @param angle
	 * @param colA
	 * @param colR
	 * @param colG
	 * @param colB
	 */
	public static void drawTextExt(double x, double y, String text, double xscale, double yscale, double angle){
		int currentLine=0;
		double ax=0, ay=0;
		//text = text.toLowerCase();
		
		int spriteOrientation = orientation;
		if (!idiot)
			spriteOrientation = NORTHWEST;
		
		String[] result=text.split("%");
		int[] widths=new int[result.length];
		for (int i=0; i<result.length; i++){
			widths[i]=result[i].length();
		}
		
		ax=reallignX(orientation,text,widths[currentLine]);
		ay=reallignY(orientation,text);
		
		GL2 gl = Scene.gl;
		gl.glPushMatrix();
		gl.glTranslated(x + Scene.viewX, y + Scene.viewY, 0);
		gl.glRotated(-angle,0,0,1);
		gl.glScaled(xscale, yscale, 0);
		
		for (int i=0; i<text.length(); i++){
			int s=46;
			s = charToImg(text,i);
			if (s==-1){
				currentLine+=1;
				if (widths.length > currentLine)
					ax=reallignX(orientation,text,widths[currentLine]);
				ay+=sprite.imageHeight*vSpacing;
			}
			else{
				sprite.renderWithoutView(s, spriteOrientation, ax-letterRandom+(Calc.random(letterRandom*2)), ay-letterRandom+(Calc.random(letterRandom*2)), 1, 1, 0, Graphics.alpha, Graphics.color[0], Graphics.color[1], Graphics.color[2]);
				ax+=sprite.imageWidth*hSpacing;
			}
		}	
		gl.glPopMatrix();
	}
	
	/**this allows for colored keywords!!!*/
	public static void drawTextExt(double x, double y, String text, double xscale, double yscale, double angle, String keyword){
		int currentLine=0;
		double ax=0, ay=0;
		
		String g = Graphics.getColor();
		//text = text.toLowerCase();
		
		int spriteOrientation = orientation;
		if (!idiot)
			spriteOrientation = NORTHWEST;
		
		String[] result=text.split("%");
		int[] widths=new int[result.length];
		for (int i=0; i<result.length; i++){
			widths[i]=realChars(result[i]);
		}
		
		ax=reallignX(orientation,text,widths[currentLine]);
		ay=reallignY(orientation,text);
		
		GL2 gl = Scene.gl;
		gl.glPushMatrix();
		gl.glTranslated(x + Scene.viewX, y + Scene.viewY, 0);
		gl.glRotated(-angle,0,0,1);
		gl.glScaled(xscale, yscale, 0);
		
		for (int i=0; i<text.length(); i++){
			int s=46;
			s = charToImg(text,i);
			if (s==-1){
				currentLine+=1;
				if (widths.length > currentLine)
					ax=reallignX(orientation,text,widths[currentLine]);
				ay+=sprite.imageHeight*vSpacing;
			}
			else if (s == -2)
				Graphics.setColor(keyword);
			else if (s == -3)
				Graphics.setColor(g);
			else{
				sprite.renderWithoutView(s, spriteOrientation, ax-letterRandom+(Calc.random(letterRandom*2)), ay-letterRandom+(Calc.random(letterRandom*2)), 1, 1, 0, Graphics.alpha, Graphics.color[0], Graphics.color[1], Graphics.color[2]);
				ax+=sprite.imageWidth*hSpacing;
			}
		}	
		gl.glPopMatrix();
		
		Graphics.setColor(g);
	}
			
	/**@return the subimage index of the character at position i of text*/
	public static int charToImg(String text, int i){
		int s;
		
		if (text.charAt(i)=='%') //THIS MEANS A NEW PARAGRAPH
			s=-1;
		else if (text.charAt(i)=='[') //THIS MEANS BEGIN KEYWORD
			s=-2;
		else if (text.charAt(i)==']') //THIS MEANS END KEYWORD
			s=-3;
		else
			s=myFont.fetchSymbolId(text.charAt(i));
		
		return s;
	}
	
	public static String widthLimit(String text, int limit){
		String result = "";
		
		int lineStartIndex = 0;
		int lastSpaceIndex = -1;//Not found yet
		int stupid = 0;
		if (text.length() > limit){
			for (int i=0; i<text.length(); i++){
				//System.out.println(text.charAt(i)+", "+(i-lineStartIndex));
				
				if (text.charAt(i)==' ')
					lastSpaceIndex = i;
				
				int lineEndIndex = -1;
				if (text.charAt(i)=='%'){
					lineEndIndex = i;
					
					stupid = 0;
				}
				else if (text.charAt(i) == '[' || text.charAt(i) == ']')
					stupid += 1;
				else if(i-lineStartIndex - stupid>=limit){//Need to start a new line
					if(lastSpaceIndex==-1)//No space on current line
						lineEndIndex = i;//Cut off in the middle of a word
					else
						lineEndIndex = lastSpaceIndex;//End the line at the previous space
					
					stupid = 0;
				}
				if(lineEndIndex!=-1){
					//append characters from lineStartIndex to lineEndIndex to result
					if(lineStartIndex==0)
						result = text.substring(lineStartIndex, lineEndIndex);
					else
						result = result + "%" + text.substring(lineStartIndex, lineEndIndex);
					
					
					//New line
					if (lastSpaceIndex!=-1 || text.charAt(i)=='%')
						lineStartIndex = lineEndIndex+1;
					else
						lineStartIndex = lineEndIndex;
					
					lastSpaceIndex = -1;
					
				}
				
			}
			//append characters from lineStartIndex to text.length() to result
			result = result + "%" + text.substring(lineStartIndex, text.length());
			
			//return
			return result;
		}
		else
			return text;
	}
	
	//RETURNS WIDTH OF TEXT IN PIXELS
	public static double textWidth(String text){
		String[] result=text.split("%");
		int highest=0;
		for (int i=0; i<result.length; i++){
			int a = realChars(result[i]);
			if (a>highest)
				highest=a;
		}
		return highest*sprite.imageWidth*hSpacing;
	}
	
	public static int realChars(String s){
		int x = 0;
		for (int i=0; i<s.length(); i++){
			if (!(s.charAt(i) == '[' || s.charAt(i) == ']'))
				x += 1;
		}
		
		return x;
	}
	
	//RETURNS HEIGHT OF TEXT IN PIXELS
	public static double textHeight(String text){
		String[] result=text.split("%");
		return result.length*sprite.imageHeight*vSpacing;
	}
	
	//REALLIGNS THE X OF TEXT FOR ORIENTATION
	public static double reallignX(int orientation, String text, double thing){
		double ax=0;
		if (orientation==NORTHWEST||orientation==WEST||orientation==SOUTHWEST)
			ax=0;
		else if (orientation==NORTHEAST||orientation==EAST||orientation==SOUTHEAST)
			ax=-textWidth(text);
		else if (orientation==NORTH||orientation==CENTERED||orientation==SOUTH)
			ax=-(thing/2)*sprite.imageWidth;
		return ax;
	}
	
	//REALLIGNS THE Y OF TEXT FOR ORIENTATION
	public static double reallignY(int orientation, String text){
		double ay=0;
		if (orientation==NORTHWEST||orientation==NORTH||orientation==NORTHEAST)
			ay=0;
		else if (orientation==WEST||orientation==CENTERED||orientation==EAST)
			ay=-textHeight(text)/2;
		else if (orientation==SOUTHWEST||orientation==SOUTH||orientation==SOUTHEAST)
			ay=-textHeight(text);
		return ay;
	}
}
